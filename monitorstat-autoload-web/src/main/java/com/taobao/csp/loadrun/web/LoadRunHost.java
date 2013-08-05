
package com.taobao.csp.loadrun.web;

import java.util.HashSet;
import java.util.Set;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.HttpLoadLogType;
import com.taobao.csp.loadrun.core.dao.LoadrunDao;

/**
 * 
 * @author xiaodu
 * @version 2010-9-2 ����05:31:31
 */
public class LoadRunHost {
	
	private int appId;
	
	private String appName;
	
	private String hostIp;
	
	private String startTime;
	
	private AutoLoadType loadType;
	
	private int loadAuto;		//1�Զ� 0������
	
	private String wangwangs;
	
	private String limitFeature;
	
	private String loadFeature;
	
	private String configFeature;
	
	private String userName;
	
	private String password;
	
	private String appUser;  // Ӧ�������û�
	
	private String jkConfigPath;
	
	private String apachectlPath;
	
	private String apache_default_config;
	
	private String apache_split_config;
	
	private String appRushHours;
	
	private String httploadpath;
	
	private String httploadProxy;
	
	private HttpLoadLogType httploadlogtype;
	
	private String httploadloglog;
	
	private String opsName;
	private String opsField;//opsfree ��ʹ�õ��ֶ�
	
	/*** ѹ�⿪ʼ��ѹ�����ִ�е�url ***/
	private String startUrl;
	private String endUrl;
	
	private AutoLoadMode loadrunMode;
	
	private String startScript;
	
	private String endScript;
	
	/*** �Ƿ�ɼ�eagleeye����hsf��qps ***/
	private int collectEagleeye;

	public LoadrunTarget getTarget(){
		
		Set<String> classSet = new HashSet<String>();
		classSet.add("com.taobao.csp.loadrun.core.fetch.ApacheFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.TomcatFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.CpuFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.GcFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.JvmFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.LoadFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.HsfFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.TairFetchTaskImpl");
		
		// ��ȥ��tddl��ֻ����10������
//		classSet.add("com.taobao.csp.loadrun.core.fetch.TddlFetchTaskImpl");

		// �ɼ�eagleeye����
//		classSet.add("com.taobao.csp.loadrun.core.fetch.EagleeyeFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.IoFetchTaskImpl");
		classSet.add("com.taobao.csp.loadrun.core.fetch.ThreadCountFetchTaskImpl");
		
		LoadrunTarget target = new LoadrunTarget();
		target.setApacheBinPath(apachectlPath);
		target.setAppId(appId);
		target.setAppName(appName);
		target.setAppRushHours(appRushHours);
		target.setConfigFeature(configFeature);
		target.setCronExpression(startTime);
		target.setFetchClasses(classSet);
		target.setJkConfigPath(jkConfigPath);
		target.setLimitFeature(limitFeature);
		target.setLoadFeature(loadFeature);
		target.setLoadrunType(loadType);
		target.setOpsField(opsField);
		target.setOpsName(opsName);
		target.setHttploadProxy(httploadProxy);
		target.setHttpLoadLogType(httploadlogtype);
		target.setHttpLoadLog(httploadloglog);
		target.setStartUrl(startUrl);
		target.setEndUrl(endUrl);
		
		target.setMode(loadrunMode);
		target.setBackScript(null);
		target.setRunScript(startScript);
		target.setResetScript(endScript);
		target.setCollectEagleeye(collectEagleeye);
		
		target.setUrl(LoadrunDao.getInstance().findUrlsByApp(this.getOpsName()));
		
		if(hostIp !=null){
			target.setTargetIp(hostIp.trim());
		}
		if(password !=null){
			target.setTargetPassword(password.trim());
		}
		if(userName!=null){
			target.setTargetUserName(userName.trim());
		}
		if (appUser != null) {
			target.setAppUser(appUser);
		}
		
		target.setApache_default_config(apache_default_config);
		target.setApache_split_config(apache_split_config);
		target.setHttploadpath(httploadpath);
		target.setWangwangs(wangwangs);
		target.setLoadAuto(loadAuto);
		return target;
		
	}
	
	
	public String getAppRushHours() {
		return appRushHours;
	}



	public void setAppRushHours(String appRushHours) {
		this.appRushHours = appRushHours;
	}
	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}



	/**
	 * 1�Զ� 0������
	 * @return
	 */
	public int getLoadAuto() {
		return loadAuto;
	}

	public void setLoadAuto(int loadAuto) {
		this.loadAuto = loadAuto;
	}

	

	public AutoLoadType getLoadType() {
		return loadType;
	}


	public void setLoadType(AutoLoadType loadType) {
		this.loadType = loadType;
	}


	public String getWangwangs() {
		return wangwangs;
	}

	public void setWangwangs(String wangwangs) {
		this.wangwangs = wangwangs;
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getAppUser() {
		return appUser;
	}


	public void setAppUser(String appUser) {
		this.appUser = appUser;
	}

	public String getJkConfigPath() {
		return jkConfigPath;
	}

	public void setJkConfigPath(String jkConfigPath) {
		this.jkConfigPath = jkConfigPath;
	}

	public String getApachectlPath() {
		return apachectlPath;
	}

	public void setApachectlPath(String apachectlPath) {
		this.apachectlPath = apachectlPath;
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

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getHttploadpath() {
		return httploadpath;
	}


	public void setHttploadpath(String httploadpath) {
		this.httploadpath = httploadpath;
	}


	public String getOpsName() {
		return opsName;
	}


	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}


	public String getOpsField() {
		return opsField;
	}


	public void setOpsField(String opsField) {
		this.opsField = opsField;
	}


	public String getHttploadProxy() {
		return httploadProxy;
	}


	public void setHttploadProxy(String httploadProxy) {
		this.httploadProxy = httploadProxy;
	}


	public HttpLoadLogType getHttploadlogtype() {
		return httploadlogtype;
	}


	public void setHttploadlogtype(HttpLoadLogType httploadlogtype) {
		this.httploadlogtype = httploadlogtype;
	}


	public String getHttploadloglog() {
		return httploadloglog;
	}

	public void setHttploadloglog(String httploadloglog) {
		this.httploadloglog = httploadloglog;
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

	public AutoLoadMode getLoadrunMode() {
		return loadrunMode;
	}

	public void setLoadrunMode(AutoLoadMode loadrunMode) {
		this.loadrunMode = loadrunMode;
	}

	public String getStartScript() {
		return startScript;
	}

	public void setStartScript(String startScript) {
		this.startScript = startScript;
	}

	public String getEndScript() {
		return endScript;
	}

	public void setEndScript(String endScript) {
		this.endScript = endScript;
	}

	public int getCollectEagleeye() {
		return collectEagleeye;
	}

	public void setCollectEagleeye(int collectEagleeye) {
		this.collectEagleeye = collectEagleeye;
	}
	
}