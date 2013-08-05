package com.taobao.monitor.common.po;

import java.util.HashMap;
import java.util.Map;


/**
 * csp_tair_provide_app_summary±íµÄpoÀà
 * @author denghaichuan.pt
 * @version 2011-12-5
 */
public class TairNamespacePo {

	private String appName;
	
	private String tairGroupName;

	private String namespace;
	
	private long callSumNum;
	
	private double rushQps;
	
	private double rushRt;
	
	private String collectTime;
	
	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	private Map<String, SiteInfo> siteDataInfoMap = new HashMap<String, SiteInfo>();

	public Map<String, SiteInfo> getSiteDataInfoMap() {
		return siteDataInfoMap;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTairGroupName() {
		return tairGroupName;
	}

	public void setTairGroupName(String tairGroupName) {
		this.tairGroupName = tairGroupName;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public long getCallSumNum() {
		return callSumNum;
	}

	public void setCallSumNum(long callSumNum) {
		this.callSumNum = callSumNum;
	}

	public double getRushQps() {
		return rushQps;
	}

	public void setRushQps(double rushQps) {
		this.rushQps = rushQps;
	}

	public double getRushRt() {
		return rushRt;
	}

	public void setRushRt(double rushRt) {
		this.rushRt = rushRt;
	}

	public void createSiteMap(String roomFeature) {
		if (roomFeature == null) {
			return;
		}
		String[] value = roomFeature.split("#");
		for (String str : value) {
			String[] value1 = str.split("\\$");
			
			String siteName = value1[0].substring(0, value1[0].indexOf(":"));
			SiteInfo info = new SiteInfo();
			
			info.siteCallNum = Long.parseLong(value1[0].substring(value1[0].indexOf(":") + 1, value1[0].length()));
			info.siteRushQps = Double.parseDouble(value1[1]);
			info.siteRushRt = Double.parseDouble(value1[2]);
			
			siteDataInfoMap.put(siteName, info);
		}
	}
	
	public class SiteInfo {
		public long siteCallNum;
		public double siteRushQps;
		public double siteRushRt;
	}
	
}
