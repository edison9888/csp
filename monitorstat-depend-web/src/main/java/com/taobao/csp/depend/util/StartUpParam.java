package com.taobao.csp.depend.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 启动相关的各个参数，在applicationContext.xml中有配置
 * @author zhongting.zy
 *
 */
public class StartUpParam {

	private String onlineApiUrl = "";
	private String tfsUrl = "";
	public String ftpPath = "";
	public String ftpIp = "";  
	public String ftpUsername = "anonymous";  
	public String ftpPwd = "anything"; 
	
	public String eosUrl = ""; 

	private Map<String,String> eagleEyeApiUrlMap = new HashMap<String, String>();  

	public String getOnlineApiUrl() {
		return onlineApiUrl;
	}
	public void setOnlineApiUrl(String onlineApiUrl) {
		this.onlineApiUrl = onlineApiUrl;
	}
	public String getTfsUrl() {
		return tfsUrl;
	}
	public void setTfsUrl(String tfsUrl) {
		this.tfsUrl = tfsUrl;
	}
	public String getFtpPath() {
		return ftpPath;
	}
	public void setFtpPath(String ftpPath) {
		this.ftpPath = ftpPath;
	}
	public String getFtpIp() {
		return ftpIp;
	}
	public void setFtpIp(String ftpIp) {
		this.ftpIp = ftpIp;
	}
	public String getFtpUsername() {
		return ftpUsername;
	}
	public void setFtpUsername(String ftpUsername) {
		this.ftpUsername = ftpUsername;
	}
	public String getFtpPwd() {
		return ftpPwd;
	}
	public void setFtpPwd(String ftpPwd) {
		this.ftpPwd = ftpPwd;
	}
	public Map<String, String> getEagleEyeApiUrlMap() {
		return eagleEyeApiUrlMap;
	}
	public void setEagleEyeApiUrlMap(Map<String, String> eagleEyeApiUrlMap) {
		this.eagleEyeApiUrlMap = eagleEyeApiUrlMap;
	}
	public String getEosUrl() {
		return eosUrl;
	}
	public void setEosUrl(String eosUrl) {
		this.eosUrl = eosUrl;
	}
}
