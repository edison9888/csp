
package com.taobao.monitor.web.vo;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-5-18 ÏÂÎç02:08:14
 */
public class PvUrlPo {
	
	private String url;
	private int pvCount;
	private double rest;
	private double  pagesize;
	
	private Date collectTime;
	private String collectTimeStr;
	
	private Integer siteId;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getPvCount() {
		return pvCount;
	}
	public void setPvCount(int pvCount) {
		this.pvCount = pvCount;
	}
	public double getRest() {
		return rest;
	}
	public void setRest(double rest) {
		this.rest = rest;
	}
	public double getPagesize() {
		return pagesize;
	}
	public void setPagesize(double pagesize) {
		this.pagesize = pagesize;
	}
	public Integer getSiteId() {
		return siteId;
	}
	public void setSiteId(Integer siteId) {
		this.siteId = siteId;
	}
	public Date getCollectTime() {
		return collectTime;
	}
	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	public String getCollectTimeStr() {
		return collectTimeStr;
	}
	public void setCollectTimeStr(String collectTimeStr) {
		this.collectTimeStr = collectTimeStr;
	}
	
	
	
	
	

}
