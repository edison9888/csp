package com.taobao.monitor.common.po;

import java.util.HashMap;

public class CspKeyDependInfoPo {
	private long id;
	private String curKeyame;
	private String parentKeynames;
	private String url;
	private String depend_me_keyname;

	private String curAppName;
	private String depend_me_appname;
	private String depend_app_type;
	private String dDevel;

	// 依赖我相关
	private HashMap<String, String> dependMeMap = new HashMap<String, String>();

	// 我依赖相关
	private HashMap<String, String> meDependMap = new HashMap<String, String>();

	/**
	 * 配置类型。auto 表示为机器自动配置，mannual 表示为人手动配置
	 */
	private String configType = "auto";

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCurAppName() {
		return curAppName;
	}
	public void setCurAppName(String curAppName) {
		this.curAppName = curAppName;
	}
	public String getCurKeyame() {
		return curKeyame;
	}
	public void setCurKeyame(String curKeyame) {
		this.curKeyame = curKeyame;
	}

	public String getParentKeynames() {
		return parentKeynames;
	}

	public void setParentKeynames(String parentKeynames) {
		this.parentKeynames = parentKeynames;
	}

	public String getdDevel() {
		return dDevel;
	}
	public void setdDevel(String dDevel) {
		this.dDevel = dDevel;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public String getDepend_me_appname() {
		return depend_me_appname;
	}

	public void setDepend_me_appname(String depend_me_appname) {
		this.depend_me_appname = depend_me_appname;
	}

	public String getDepend_app_type() {
		return depend_app_type;
	}

	public void setDepend_app_type(String depend_app_type) {
		this.depend_app_type = depend_app_type;
	}
	public String getConfigType() {
		return configType;
	}

	public void setConfigType(String configType) {
		this.configType = configType;
	}

	public HashMap<String, String> getDependMeMap() {
		return dependMeMap;
	}

	public void setDependMeMap(HashMap<String, String> dependMeMap) {
		this.dependMeMap = dependMeMap;
	}
	public HashMap<String, String> getMeDependMap() {
		return meDependMap;
	}

	public void setMeDependMap(HashMap<String, String> meDependMap) {
		this.meDependMap = meDependMap;
	}

	public String getDepend_me_keyname() {
		return depend_me_keyname;
	}

	public void setDepend_me_keyname(String depend_me_keyname) {
		this.depend_me_keyname = depend_me_keyname;
	}
}
