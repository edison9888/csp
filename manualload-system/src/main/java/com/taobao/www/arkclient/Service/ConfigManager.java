package com.taobao.www.arkclient.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

public class ConfigManager {
	private static String fileName = "ARKConfig.properties";
	private static final ConfigManager configmanager = new ConfigManager();	
	private static Properties p;
	private static Set<String> unProtectList;

	public ConfigManager() {
		
		Properties props = System.getProperties();
		Object systemname = props.get("os.name");
		if(systemname!=null){				
			String name = (String)systemname;
			if(name.toLowerCase().indexOf("window")>-1){
				fileName = "ARKConfig_window.properties";
			}				
		}
		
		String classPathUrl = Thread.currentThread().getContextClassLoader().getResource("/").getPath();
		File propFile = new File(classPathUrl + fileName);
		FileInputStream fis = null;
		try {
			if (propFile.exists()) {
				fis = new FileInputStream(propFile);
			} else {
				String webInfoPath = StringUtils.substringBeforeLast(classPathUrl, "classes/");
				propFile = new File(webInfoPath + fileName);
				if (propFile.exists()) {
					fis = new FileInputStream(propFile);
				}
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("coun't find config " + fileName);
		}
		if (null == fis) {
			throw new IllegalArgumentException("coun't find config " + fileName);
		}
		try {
			p = new Properties();
			p.load(fis);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				fis.close();
			} catch (IOException e) {
			}
		}
		 
		if (null != p) {
			if (!p.isEmpty()) {
				ConfigManager.unProtectList = new HashSet<String>(p.size());
				for (Object keyObj : p.keySet()) {
					String key = (String) keyObj;
					if (StringUtils.isNotBlank(key) && key.startsWith("unProjectURL")) {
						ConfigManager.unProtectList.add(StringUtils.defaultString(p.getProperty(key)));
					}
				}
			}
			 
			if (!p.containsKey("arkserver")) {
				p.put("arkserver", "https://ark.taobao.org:4430/arkserver");
			}
			if (!p.containsKey("server")) {
				p.put("server", "ark.taobao.org");
			}
			if (!p.containsKey("port")) {
				p.put("port", "4430");
			}
			if (!p.containsKey("secretcache")) {
				p.put("secretcache", "60");
			}
		}
	}

	public boolean isCommonUnProtect(String s) {
		return (s.startsWith("/k2.do") || s.startsWith("/daily.do") || s.startsWith("/confirmStep.do")
				|| s.startsWith("/changeToSignedStep.do") || s.startsWith("/k2.htm") || s.startsWith("/login/")
				|| s.startsWith("/doPointChange.do") || s.startsWith("/doBannerChange.do")
				|| s.startsWith("/doBuildIndex.do") || s.startsWith("/doUpdatePeopleSoftInfo.do")
				|| s.startsWith("/doCreateTrainCache.do") || s.startsWith("/errorPagenotfound.htm")
				|| s.startsWith("/error.htm") || s.endsWith(".jpg") || s.endsWith(".gif") || s.endsWith(".png")
				|| s.endsWith(".bmp") || s.endsWith(".jpeg") || s.endsWith(".txt") || s.indexOf("check") > 0);
	}

	public boolean isUserUnProtect(String url) {
		boolean res = false;
		for (String target : ConfigManager.unProtectList) {
			if (url.startsWith(target)) {
				res = true;
				break;
			}
		}
		return res;
	}

	public static ConfigManager getInstance() {
		return configmanager;
	}

	public String getValue(String name) {
		if (p != null) {
			if (p.getProperty(name) == null)
				return null;
			else
				return p.getProperty(name).toString();
		} else {
			return null;
		}
	}
}
