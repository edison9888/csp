
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.url;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xiaodu
 *
 * ионГ9:54:04
 */
public class CheckUrl {
	
	private String appName;
	
	private String url;
	
	private Set<String> siteSet = new HashSet<String>();

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Set<String> getSiteSet() {
		return siteSet;
	}

	public void setSiteSet(Set<String> siteSet) {
		this.siteSet = siteSet;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	

}
