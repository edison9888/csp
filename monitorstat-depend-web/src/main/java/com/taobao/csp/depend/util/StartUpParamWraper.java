package com.taobao.csp.depend.util;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

/**
 * @author zhongting.zy
 * FIXME��spring �޷���ʼ��static bean������ʹ��StartUpParamWraper������StartUpParam
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
	 * set����ȥ��static������springע���ʱ��ᱨ��
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
