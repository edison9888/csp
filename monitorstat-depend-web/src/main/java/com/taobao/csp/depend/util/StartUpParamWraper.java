package com.taobao.csp.depend.util;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

/**
 * @author zhongting.zy
 * FIXME，spring 无法初始化static bean，所以使用StartUpParamWraper来包裹StartUpParam
 */
@Resource(name = "startUpParamWraper")
public class StartUpParamWraper {
	private static final Logger logger = Logger.getLogger(StartUpParamWraper.class);

	private static StartUpParam startUpParam;  
	public static StartUpParam getStartUpParam() {
		if(startUpParam == null)
			startUpParam = new StartUpParam();
		return startUpParam;
	}
	/**
	 * set方法去掉static，否则spring注入的时候会报错。
	 * @param startUpParam
	 */
	public void setStartUpParam(StartUpParam startUpParam) {
		StartUpParamWraper.startUpParam = startUpParam;
	}
	public static String getOnlineApiUrl() {
		return getStartUpParam().getOnlineApiUrl();
	}
	public static String getTfsUrl() {
		return getStartUpParam().getTfsUrl();
	}

	public static String getFtpPath() {
		return getStartUpParam().getFtpPath();
	}
	public static String getFtpIp() {
		return getStartUpParam().getFtpIp();
	}
	public static String getFtpUsername() {
		return getStartUpParam().getFtpUsername();
	}
	public static String getFtpPwd() {
		return getStartUpParam().getFtpPwd();
	}

	public static String getEagleeyeUrlByType(String key) {
		return getStartUpParam().getEagleEyeApiUrlMap().get(key);
	}
	
	public static String getEosUrl() {
		return getStartUpParam().getEosUrl();
	}
	
}
