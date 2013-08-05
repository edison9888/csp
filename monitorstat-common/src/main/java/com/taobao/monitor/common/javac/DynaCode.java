package com.taobao.monitor.common.javac;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


public class DynaCode {
	
	private File srcFile;

	private File binDir;

	private Javac javac;

	private URLClassLoader classLoader;
	
	private Class clazz;
	
	private String className;
	
	public DynaCode(String className,File src,String classPath) throws MalformedURLException{
		
		this.srcFile = src;
		this.className = className;

		String subdir = srcFile.getAbsolutePath().replace(':', '_').replace(
				'/', '_').replace('\\', '_');
		this.binDir = new File(System.getProperty("java.io.tmpdir"),
				"dynacode/" + subdir);
		this.binDir.mkdirs();

		// prepare compiler
		this.javac = new Javac(classPath, binDir.getAbsolutePath());
		
		classLoader = new URLClassLoader(new URL[] { binDir.toURL() },Thread.currentThread().getContextClassLoader());
		
	}
	
	public Class<?> compileAndLoadClass(){
		
		


		if (clazz != null) {
			return clazz; // class already loaded
		}

		// compile, if required
		 String error = javac.compile(new File[] { srcFile });

		if (error != null) {
			throw new RuntimeException("Failed to compile "
					+ srcFile.getAbsolutePath() + ". Error: " + error);
		}

		try {
			// load class
			return  classLoader.loadClass(className);

		} catch (Throwable e) {
			throw new RuntimeException("Failed to load DynaCode class "
					+ srcFile.getAbsolutePath());
		}
	}
	
	
	

}
