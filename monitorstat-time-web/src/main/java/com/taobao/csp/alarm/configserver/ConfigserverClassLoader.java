
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.configserver;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * @author xiaodu
 *
 * ÏÂÎç5:42:54
 */
public class ConfigserverClassLoader extends ClassLoader{
	
	private URLClassLoader classLoader = null;
	/**
	 * @param urls
	 */
	public ConfigserverClassLoader(String mina115Path) {
		super(Thread.currentThread().getContextClassLoader());
		try {
			classLoader = new URLClassLoader(new URL[]{new URL(mina115Path)});
		} catch (MalformedURLException e) {
		}
	}


	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		if(name.indexOf("org.apache.mina") >-1){
			return  classLoader.loadClass(name);
		}
		return super.loadClass(name, resolve);
	}

}
