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
 * ��̬�������java��.
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-26 - ����01:02:10
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
	 *            �������붯̬���·��
	 * @param parentClassLoader
	 *            �������ж�̬��ĸ�������
	 */
	public DynaCode(String compileClasspath, ClassLoader parentClassLoader) {
		this.compileClasspath = compileClasspath;
		this.parentClassLoader = parentClassLoader;
	}

	/**
	 * ���һ��Ŀ¼���������˶�̬��JavaԴ����
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
			//����Ƿ����
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
	 * �����������¶�̬������ơ�
	 * 
	 * @param className ������
	 * @return
	 * @throws ClassNotFoundException ������ļ�û���ֻ��߱�������׳����쳣
	 * 2011-5-26 - ����11:43:56
	 */
	public Class loadClass(String className) throws ClassNotFoundException {

		LoadedClass loadedClass = null;
		synchronized (loadedClasses) {
			loadedClass = (LoadedClass) loadedClasses.get(className);
		}
		//����java��
		if (loadedClass == null) {
			String resource = className.replace('.', '/') + ".java";
			SourceDir src = locateResource(resource);
			if (src == null) {
				throw new ClassNotFoundException("DynaCode class not found "
						+ className);
			}
			synchronized (this) {
				//���������
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
		//������ص���
		synchronized (loadedClasses) {
			for (Iterator iter = loadedClasses.values().iterator(); iter
					.hasNext();) {
				LoadedClass loadedClass = (LoadedClass) iter.next();
				if (loadedClass.srcDir == src) {
					iter.remove();
				}
			}
		}

		//�����µ��������
		src.recreateClassLoader();
	}

	/**
	 * ����Դdirectories���淵����ԴURL
	 * 
	 * @param resource
	 * @return ������Դ��URL���������Դû�ҵ��ͷ���null
	 * 2011-5-26 - ����11:43:56
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
	 * ʹ��jdk��̬������ʵ����ָ���ӿڵ���(��ûʵ�ֽӿڿ���ʹ��cglib)
	 * 
	 * @param interfaceClass
	 *            ��ʵ�ֵĽӿ�
	 * @param implClassName
	 *            ��̬ʵ�ֽӿڵ���
	 * @return
	 * @throws RuntimeException
	 *             ���ʵ�����ܴ������׳����쳣,����classû�Ҵ�
     * 2011-5-26 - ����11:43:56
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
	 * ʵ��������Լ���̬�����ഴ��
	 * 
	 * 2011-5-27 - ����01:17:21
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
				//��ȡ��
				Class clz = loadClass(backendClassName);
				//ʵ������
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
				//��ʼ��ʼ���������Ϣ
				System.out.println(DateUtil.getDateYMDHMSFormat().format(new Date())+
						" ����selenium rc��ʼ�������...");
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
	 * ��ȡ��һ�����������������·���ַ�����ֻʶ��URLClassLoader�ġ�
	 * 
	 * @param cl
	 * @return
	 * 2011-5-27 - ����01:18:12
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
//	 * �쳣����ʱɾ�����������
//	 * 
//	 * @param useCase
//	 * @param rc
//	 * 2011-6-14 - ����05:43:23
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
	 * ��ӡ��־��Ϣ 
	 * 
	 * @param msg
	 * 2011-5-27 - ����01:18:29
	 */
	private static void info(String msg) {
		System.out.println("[DynaCode] " + msg);
	}

}