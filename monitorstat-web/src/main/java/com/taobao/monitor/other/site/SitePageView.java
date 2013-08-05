
package com.taobao.monitor.other.site;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xiaodu
 * @version 2011-5-30 обнГ04:50:32
 */
public class SitePageView {
	
	private String opsName;
	
	private Map<String,Long> sitePageViewMap = new HashMap<String, Long>();

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public Map<String, Long> getSitePageViewMap() {
		return sitePageViewMap;
	}

	public void setSitePageViewMap(Map<String, Long> sitePageViewMap) {
		this.sitePageViewMap = sitePageViewMap;
	}
	
	
	
}
