package com.taobao.csp.time.web.po;

public class UserCustomNaviKeyPo {
	private Integer navId;
	private String appName;
	private String keyName;
	private String actionUrl;
	private String viewMod;
	private String property;
	private Integer keyId;
	private String search;
	private String domain;
	private String depth;
	public String getSearch() {
		return search;
	}
	public void setSearch(String search) {
		this.search = search;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getDepth() {
		return depth;
	}
	public void setDepth(String depth) {
		this.depth = depth;
	}
	public Integer getKeyId() {
		return keyId;
	}
	public void setKeyId(Integer keyId) {
		this.keyId = keyId;
	}
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public Integer getNavId() {
		return navId;
	}
	public void setNavId(Integer navId) {
		this.navId = navId;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getViewMod() {
		return viewMod;
	}
	public void setViewMod(String viewMod) {
		this.viewMod = viewMod;
	}
}
