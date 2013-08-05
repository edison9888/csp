
package com.taobao.csp.loadrun.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.HttpLoadLogType;
import com.taobao.csp.loadrun.core.fetch.UrlElement;

/**
 * 
 * @author xiaodu
 * @version 2011-6-23 下午01:14:55
 */
public class LoadrunTarget implements Serializable{
	
	public static Logger logger = Logger.getLogger(LoadrunTarget.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8561512017800937608L;

	private int appId;
	
	private String appName;
	
	private String targetIp;
	
	private AutoLoadType loadrunType;
	
	private String wangwangs;
	
	private String targetUserName;
	
	private String targetPassword;
	
	private String appUser;
	
	private String cronExpression;
		
	private Set<String> fetchClasses;
	
	private int loadAuto;
	
	private String jkConfigPath ;
	
	private String apacheBinPath ;
	
	private int loadrunOrder;
	
	private String httploadpath;
	
	private String httploadProxy;
	
	private HttpLoadLogType httpLoadLogType;
	
	private String httpLoadLog;
	
	/**
	 * 应用访问 高峰时刻 H-H 格式 如:20-22
	 */
	private String appRushHours; //
	
	
	/**
	 * 压测的 限制描述字段，maxCpu:75;maxLoad:4;maxQps:4000;maxRest:400;
	 */
	private String limitFeature;
	
	/**
	 * 对应不类型的压测 存放不同的信息 
		httpload:压测并发数5,10,15...
		apache 存放ip1,ip2,ip3,...
		hsf 存放ip倍数 1,2,3,4,5
	 */
	private String loadFeature;
	
	/**
	 * 这个存放对配置文件的说明
		hsf 存放的是接口的名称 interface1,interface2
		apache配置名称

	 */
	private String configFeature;
	
	private String apache_default_config;
	
	private String apache_split_config;
	
	private String curControlFeature;
	
	private String opsField;//opsfree 上使用的字段
	
	private String opsName;
	
	private String startUrl;
	
	private String endUrl;
	
	/******************************/
	private AutoLoadMode mode; 
	
	private String backScript;
	
	private String runScript;
	
	private String resetScript;
	
	private int collectEagleeye;
	
    /******  存放动态url信息,采集url信息分析的时候有用到，从配置文件中读取 ******/
	private List<UrlElement> url;
	
	public LoadrunTarget() {
		
	}
	
	public String getTargetIp() {
		return targetIp;
	}

	public void setTargetIp(String targetIp) {
		this.targetIp = targetIp;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	

	public AutoLoadType getLoadrunType() {
		return loadrunType;
	}

	public void setLoadrunType(AutoLoadType loadrunType) {
		this.loadrunType = loadrunType;
	}

	public String getTargetUserName() {
		return targetUserName;
	}

	public void setTargetUserName(String targetUserName) {
		this.targetUserName = targetUserName;
	}

	public String getTargetPassword() {
		return targetPassword;
	}

	public void setTargetPassword(String targetPassword) {
		this.targetPassword = targetPassword;
	}
	
	public String getAppUser() {
		return appUser;
	}

	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Set<String> getFetchClasses() {
		return fetchClasses;
	}

	public void setFetchClasses(Set<String> fetchClasses) {
		this.fetchClasses = fetchClasses;
	}

	public String getLimitFeature() {
		return limitFeature;
	}

	public void setLimitFeature(String limitFeature) {
		this.limitFeature = limitFeature;
	}

	public String getLoadFeature() {
		return loadFeature;
	}

	public void setLoadFeature(String loadFeature) {
		this.loadFeature = loadFeature;
	}

	public String getConfigFeature() {
		return configFeature;
	}

	public void setConfigFeature(String configFeature) {
		this.configFeature = configFeature;
	}

	public String getAppRushHours() {
		return appRushHours;
	}

	public void setAppRushHours(String appRushHours) {
		this.appRushHours = appRushHours;
	}

	public String getJkConfigPath() {
		return jkConfigPath;
	}

	public void setJkConfigPath(String jkConfigPath) {
		this.jkConfigPath = jkConfigPath;
	}

	public String getApacheBinPath() {
		return apacheBinPath;
	}

	public void setApacheBinPath(String apacheBinPath) {
		this.apacheBinPath = apacheBinPath;
	}

	public String getCurControlFeature() {
		return curControlFeature;
	}

	public void setCurControlFeature(String curControlFeature) {
		this.curControlFeature = curControlFeature;
	}

	public String getApache_default_config() {
		return apache_default_config;
	}

	public void setApache_default_config(String apache_default_config) {
		this.apache_default_config = apache_default_config;
	}

	public String getApache_split_config() {
		return apache_split_config;
	}

	public void setApache_split_config(String apache_split_config) {
		this.apache_split_config = apache_split_config;
	}

	public int getLoadrunOrder() {
		return loadrunOrder;
	}

	public void setLoadrunOrder(int loadrunOrder) {
		this.loadrunOrder = loadrunOrder;
	}

	public String getHttploadpath() {
		return httploadpath;
	}

	public void setHttploadpath(String httploadpath) {
		this.httploadpath = httploadpath;
	}

	public String getWangwangs() {
		return wangwangs;
	}

	public void setWangwangs(String wangwangs) {
		this.wangwangs = wangwangs;
	}

	public int getLoadAuto() {
		return loadAuto;
	}

	public void setLoadAuto(int loadAuto) {
		this.loadAuto = loadAuto;
	}

	public String getOpsField() {
		return opsField;
	}

	public void setOpsField(String opsField) {
		this.opsField = opsField;
	}

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public String getHttploadProxy() {
		return httploadProxy;
	}

	public void setHttploadProxy(String httploadProxy) {
		this.httploadProxy = httploadProxy;
	}

	public HttpLoadLogType getHttpLoadLogType() {
		return httpLoadLogType;
	}

	public void setHttpLoadLogType(HttpLoadLogType httpLoadLogType) {
		this.httpLoadLogType = httpLoadLogType;
	}

	public String getHttpLoadLog() {
		return httpLoadLog;
	}

	public void setHttpLoadLog(String httpLoadLog) {
		this.httpLoadLog = httpLoadLog;
	}

	public String getStartUrl() {
		return startUrl;
	}

	public void setStartUrl(String startUrl) {
		this.startUrl = startUrl;
	}

	public String getEndUrl() {
		return endUrl;
	}

	public void setEndUrl(String endUrl) {
		this.endUrl = endUrl;
	}

	public AutoLoadMode getMode() {
		return mode;
	}

	public void setMode(AutoLoadMode mode) {
		this.mode = mode;
	}

	public String getBackScript() {
		return backScript;
	}

	public void setBackScript(String backScript) {
		this.backScript = backScript;
	}

	public String getRunScript() {
		return runScript;
	}

	public void setRunScript(String runScript) {
		this.runScript = runScript;
	}

	public String getResetScript() {
		return resetScript;
	}

	public void setResetScript(String resetScript) {
		this.resetScript = resetScript;
	}

	public int getCollectEagleeye() {
		return collectEagleeye;
	}

	public void setCollectEagleeye(int collectEagleeye) {
		this.collectEagleeye = collectEagleeye;
	}


	public List<UrlElement> getUrl() {
		return url;
	}

	public void setUrl(List<UrlElement> url) {
		this.url = url;
	}

	@Override
	public String toString() {
		return appName+"="+targetIp+"="+loadrunType;
	}
	
	public static void  main(String [] args) {
		InputStream inputStream = LoadrunTarget.class.getClassLoader().getResourceAsStream("dynamic_urls");
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		String line = null;
		try {
			while ((line = bufferedReader.readLine()) != null && line.trim().length() > 0) {
				Pattern pattern = Pattern.compile(line.trim());
				Matcher matcher = pattern.matcher("http://bbs.taobao.com/catalog/10145510.htm");
				System.out.println(matcher.matches());
			}
		} catch (IOException e) {
		}
	}
	
}
