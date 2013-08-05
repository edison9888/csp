package com.taobao.www.arkclient.Service;

import org.apache.commons.lang.StringUtils;



public class ArkAuthenService {

	private final static String ARKSERVER = "arkserver";
	private final static String SECRETCACHE = "secretcache";
	private final static String SERVER = "https://ark.taobao.org:4430/arkserver";
	private final static int CACHEMINUTE = 60;
	private final static ConfigManager configmanager = ConfigManager.getInstance();

	public static String getArkServer() {
		//获得配置项arkserver
		String arkSer = configmanager.getValue(ARKSERVER);
		return StringUtils.defaultIfEmpty(arkSer, SERVER);
	}

	public static int getSecretCache() {
		//获得配置项secretcache
		String secretCach = configmanager.getValue(SECRETCACHE);
		if (StringUtils.isNotBlank(secretCach)) {
			int secretCache = Integer.parseInt(secretCach);
			return secretCache > 0 ? secretCache : CACHEMINUTE;
		} else {
			return CACHEMINUTE;
		}
	}
}
