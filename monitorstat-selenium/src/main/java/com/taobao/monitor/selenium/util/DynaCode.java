package com.taobao.monitor.selenium.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.monitor.selenium.dao.model.SeleniumRc;
import com.taobao.monitor.selenium.dao.model.UseCase;
import com.taobao.monitor.selenium.service.SeleniumBeanService;
import com.taobao.monitor.selenium.service.SeleniumRcService;
import com.taobao.monitor.selenium.service.SeleniumUseCaseService;
import com.taobao.monitor.selenium.util.log.DatabaseResultsFormatter;
import com.taobao.monitor.selenium.util.log.LogCommandProcessor;
import com.taobao.monitor.selenium.util.log.LogDefaultSelenium;
import com.taobao.monitor.selenium.util.log.LogResultsFormatter;
import com.taobao.monitor.selenium.util.log.LogSelenium;
import com.thoughtworks.selenium.HttpCommandProcessor;

/**
 * 动态编译加载java类.
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-26 - 下午01:02:10
 * @version 1.0
 */
public final class DynaCode {
	private static final Log logger = LogFactory.getLog(DynaCode.class);
	
	private String compileClasspath;

	private ClassLoader parentClassLoader;

	private ArrayList sourceDirs = new ArrayList();

	/**
	 * key: class name 
	 * value: LoadedClass
	 */
	private HashMap loadedClasses = new HashMap();

	public DynaCode() {
		this(Thread.currentThread().getContextClassLoader());
	}

	public DynaCode(ClassLoader parentClassLoader) {
		this(extractClasspath(parentClassLoader), parentClassLoader);
	}

	/**
	 * @param compileClasspath
	 *            用来编译动态类的路径
	 * @param parentClassLoader
	 *            加载所有动态类的父加载器
	 */
	public DynaCode(String compileClasspath, ClassLoader parentClassLoader) {
		this.compileClasspath = compileClasspath;
		this.parentClassLoader = parentClassLoader;
	}

	/**
	 * 添加一个目录，它包含了动态的Java源代码
	 * 
	 * @param srcDir
	 * @return true if the add is successful
	 */
	public boolean addSourceDir(File srcDir) {

		try {
			srcDir = srcDir.getCanonicalFile();
		} catch (IOException e) {
			// ignore
		}
		synchronized (sourceDirs) {
			//检查是否存在
			for (int i = 0; i < sourceDirs.size(); i++) {
				SourceDir src = (SourceDir) sourceDirs.get(i);
				if (src.srcDir.equals(srcDir)) {
					return false;
				}
			}
			SourceDir src = new SourceDir(srcDir);
			sourceDirs.add(src);
			info("Add source dir " + srcDir);
		}

		return true;
	}

	/**
	 * 返回向上最新动态类的名称。
	 * 
	 * @param className 类名字
	 * @return
	 * @throws ClassNotFoundException 如果类文件没发现或者编译错误将抛出此异常
	 * 2011-5-26 - 上午11:43:56
	 */
	public Class loadClass(String className) throws ClassNotFoundException {

		LoadedClass loadedClass = null;
		synchronized (loadedClasses) {
			loadedClass = (LoadedClass) loadedClasses.get(className);
		}
		//创建java类
		if (loadedClass == null) {
			String resource = className.replace('.', '/') + ".java";
			SourceDir src = locateResource(resource);
			if (src == null) {
				throw new ClassNotFoundException("DynaCode class not found "
						+ className);
			}
			synchronized (this) {
				//编译加载类
				loadedClass = new LoadedClass(className, src);
				synchronized (loadedClasses) {
					loadedClasses.put(className, loadedClass);
				}
			}

			return loadedClass.clazz;
		}
		// subsequent access
		if (loadedClass.isChanged()) {
			// unload and load again
			unload(loadedClass.srcDir);
			return loadClass(className);
		}

		return loadedClass.clazz;
	}

	private SourceDir locateResource(String resource) {
		for (int i = 0; i < sourceDirs.size(); i++) {
			SourceDir src = (SourceDir) sourceDirs.get(i);
			if (new File(src.srcDir, resource).exists()) {
				return src;
			}
		}
		return null;
	}

	private void unload(SourceDir src) {
		//清除加载的类
		synchronized (loadedClasses) {
			for (Iterator iter = loadedClasses.values().iterator(); iter
					.hasNext();) {
				LoadedClass loadedClass = (LoadedClass) iter.next();
				if (loadedClass.srcDir == src) {
					iter.remove();
				}
			}
		}

		//创建新的类加载器
		src.recreateClassLoader();
	}

	/**
	 * 从资源directories里面返回资源URL
	 * 
	 * @param resource
	 * @return 返回资源的URL或者如果资源没找到就返回null
	 * 2011-5-26 - 上午11:43:56
	 */
	public URL getResource(String resource) {
		try {

			SourceDir src = locateResource(resource);
			return src == null ? null : new File(src.srcDir, resource).toURL();

		} catch (MalformedURLException e) {
			// should not happen
			return null;
		}
	}

	/**
	 * Get a resource stream from added source directories.
	 * 
	 * @param resource
	 * @return the resource stream, or null if resource not found
	 */
	public InputStream getResourceAsStream(String resource) {
		try {

			SourceDir src = locateResource(resource);
			return src == null ? null : new FileInputStream(new File(
					src.srcDir, resource));

		} catch (FileNotFoundException e) {
			// should not happen
			return null;
		}
	}

	/**
	 * 使用jdk动态代理创建实现了指定接口的类(如没实现接口可以使用cglib)
	 * 
	 * @param interfaceClass
	 *            类实现的接口
	 * @param implClassName
	 *            动态实现接口的类
	 * @return
	 * @throws RuntimeException
	 *             如果实例不能创建将抛出此异常,比如class没找打
     * 2011-5-26 - 上午11:43:56
	 */
	public Object newProxyInstance(Class interfaceClass, String implClassName,
			int rcId)
			throws RuntimeException {
		MyInvocationHandler handler = new MyInvocationHandler(
				implClassName, rcId);
		return Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class[] { interfaceClass }, handler);
	}

	private class SourceDir {
		File srcDir;

		File binDir;

		Javac javac;

		URLClassLoader classLoader;

		SourceDir(File srcDir) {
			this.srcDir = srcDir;

			String subdir = srcDir.getAbsolutePath().replace(':', '_').replace(
					'/', '_').replace('\\', '_');
			this.binDir = new File(System.getProperty("java.io.tmpdir"),
					"dynacode/" + subdir);
			this.binDir.mkdirs();

			// prepare compiler
			this.javac = new Javac(compileClasspath, binDir.getAbsolutePath());

			// class loader
			recreateClassLoader();
		}

		void recreateClassLoader() {
			try {
				classLoader = new URLClassLoader(new URL[] { binDir.toURL() },
						parentClassLoader);
			} catch (MalformedURLException e) {
				// should not happen
			}
		}

	}

	private static class LoadedClass {
		String className;

		SourceDir srcDir;

		File srcFile;

		File binFile;

		Class clazz;

		long lastModified;

		LoadedClass(String className, SourceDir src) {
			this.className = className;
			this.srcDir = src;

			String path = className.replace('.', '/');
			this.srcFile = new File(src.srcDir, path + ".java");
			this.binFile = new File(src.binDir, path + ".class");

			compileAndLoadClass();
		}

		boolean isChanged() {
			return srcFile.lastModified() != lastModified;
		}

		void compileAndLoadClass() {

			if (clazz != null) {
				return; // class already loaded
			}

			// compile, if required
			String error = null;
			if (binFile.lastModified() < srcFile.lastModified()) {
				error = srcDir.javac.compile(new File[] { srcFile });
			}

			if (error != null) {
				throw new RuntimeException("Failed to compile "
						+ srcFile.getAbsolutePath() + ". Error: " + error);
			}

			try {
				// load class
				clazz = srcDir.classLoader.loadClass(className);

				// load class success, remember timestamp
				lastModified = srcFile.lastModified();

			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Failed to load DynaCode class "
						+ srcFile.getAbsolutePath());
			}

			info("Init " + clazz);
		}
	}

	/**
	 * 实现类加载以及动态代理类创建
	 * 
	 * 2011-5-27 - 下午01:17:21
	 * @version 1.0
	 */
	private class MyInvocationHandler implements InvocationHandler {

		String backendClassName;

		Object backend;
		
		int rcId;

		MyInvocationHandler(String className, int rcId) {
			backendClassName = className;
			this.rcId = rcId;
			try {
				//获取类
				Class clz = loadClass(backendClassName);
				//实例化类
				backend = newDynaCodeInstance(clz);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		}

		public Object invoke(Object proxy, Method method, Object[] args)
				throws Throwable {
			LogSelenium selenium = null;
			SeleniumUseCaseService ucService = (SeleniumUseCaseService) SeleniumBeanService
		    .getBean("seleniumUseCaseServiceImpl");
			SeleniumRcService rcService = (SeleniumRcService) SeleniumBeanService
		    .getBean("seleniumRcServiceImpl");
			UseCase useCase = null;
			SeleniumRc rc = null;
			try {
				//开始初始化浏览器信息
				System.out.println(DateUtil.getDateYMDHMSFormat().format(new Date())+
						" 调用selenium rc初始化浏览器...");
				String useCaseName = backendClassName.substring(backendClassName.indexOf(".")+1);
				useCase = ucService.getUseCasePlugByName(useCaseName);
				rc = rcService.getSeleniumRcByPrimaryKey(rcId);
				if(!rc.getSeleniumRcIp().equals("")
						|| rc.getSeleniumRcPort() != 0){
					LogResultsFormatter dataBaseFormatter = new DatabaseResultsFormatter();
					String browser = "*iexplore";
					if(useCase.getBrowser().equals("Internet Explorer")){
						browser = "*iexplore";
					}else if(useCase.getBrowser().equals("Mozilla Firefox")){//Safari
						browser = "*firefox";
					}else if(useCase.getBrowser().equals("Safari")){//Safari
						browser = "*safari";
					}else if(useCase.getBrowser().equals("Opera")){//Opera
						browser = "*opera";
					}
					HttpCommandProcessor processor = new HttpCommandProcessor(rc.getSeleniumRcIp(),
							rc.getSeleniumRcPort(), browser, useCase.getBaseUrl());
					LogCommandProcessor myProcessor = new LogCommandProcessor(
							processor, dataBaseFormatter);
					myProcessor.setExcludedCommands(new String[] {});
					selenium = new LogDefaultSelenium(myProcessor);
					selenium.start();
					selenium.windowMaximize();
				}			
				selenium.setContext(SeleniumConstant.SELENIUM_UC_NAME + backendClassName+":"+rcId);
				return method.invoke(backend, selenium);
			} catch (InvocationTargetException e) {
				if(selenium != null){
					selenium.stop();
				}
				selenium = null;
				throw e.getTargetException();
			}finally{
				if(selenium != null){
					selenium.stop();
				}			
			}
		}	
		
		private Object newDynaCodeInstance(Class clz) {
			try {
				return clz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException(
						"Failed to new instance of DynaCode class "
								+ clz.getName(), e);
			}
		}

	}

	/**
	 * 提取从一个给定的类加载器类路径字符串。只识别URLClassLoader的。
	 * 
	 * @param cl
	 * @return
	 * 2011-5-27 - 下午01:18:12
	 */
	private static String extractClasspath(ClassLoader cl) {
		StringBuffer buf = new StringBuffer();

		while (cl != null) {
			if (cl instanceof URLClassLoader) {
				URL urls[] = ((URLClassLoader) cl).getURLs();
				
				for (int i = 0; i < urls.length; i++) {
					if (buf.length() > 0) {
						buf.append(File.pathSeparatorChar);
					}
					String s=urls[i].getFile();
					
					try {
						s=URLDecoder.decode(s, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						continue;
					}
					File f=new File(s);
					
					//f.isFile()
					buf.append(f.getAbsolutePath());
				}
			}
			cl = cl.getParent();
		}

		return buf.toString();
	}

//	/**
//	 * 异常发生时删除浏览器数据
//	 * 
//	 * @param useCase
//	 * @param rc
//	 * 2011-6-14 - 下午05:43:23
//	 */
//	public static void deleteBrowserSession(UseCase useCase, SeleniumRc rc){
//		BrowserSession browserSession = null;
//		browserSession = new BrowserSession();
//		browserSession.setUseCaseId(useCase.getUseCaseId());
//		browserSession.setSeleniumRcIp(rc.getSeleniumRcIp());
//		browserSession.setSeleniumRcPort(rc.getSeleniumRcPort());
//		BrowserSessionCache.getBrowserSessionService().deleteBrowserSession(browserSession);
//	}
	
	/**
	 * 打印日志信息 
	 * 
	 * @param msg
	 * 2011-5-27 - 下午01:18:29
	 */
	private static void info(String msg) {
		System.out.println("[DynaCode] " + msg);
	}

}