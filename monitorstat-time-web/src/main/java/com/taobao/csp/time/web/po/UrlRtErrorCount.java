
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.po;

/**
 * @author xiaodu
 *统计应用URl 响应时间超过1s的应用
 * 下午3:56:08
 */
public class UrlRtErrorCount {
	
	private String appName;
	
	private long appPv;
	
	private long appErrorPv;
	
	private long appRt100Pv;
	
	private long appRt500pv;
	
	private long appRt1000pv;
	
	
	private String url;
	
	private long urlpv;
	
	private long errorUrlPv;
	
	private long urlRt100Pv;
	
	private long urlRt500pv;
	
	private long urlRt1000pv;
	
	private float averageErrorUrlPv;
	
	private long maxErrorUrlPv;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public long getAppPv() {
		return appPv;
	}

	public void setAppPv(long appPv) {
		this.appPv = appPv;
	}

	public long getAppErrorPv() {
		return appErrorPv;
	}

	public void setAppErrorPv(long appErrorPv) {
		this.appErrorPv = appErrorPv;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getUrlpv() {
		return urlpv;
	}

	public void setUrlpv(long urlpv) {
		this.urlpv = urlpv;
	}

	public long getErrorUrlPv() {
		return errorUrlPv;
	}

	public void setErrorUrlPv(long errorUrlPv) {
		this.errorUrlPv = errorUrlPv;
	}

	public float getAverageErrorUrlPv() {
		return averageErrorUrlPv;
	}

	public void setAverageErrorUrlPv(float averageErrorUrlPv) {
		this.averageErrorUrlPv = averageErrorUrlPv;
	}

	public long getMaxErrorUrlPv() {
		return maxErrorUrlPv;
	}

	public void setMaxErrorUrlPv(long maxErrorUrlPv) {
		this.maxErrorUrlPv = maxErrorUrlPv;
	}

	public long getAppRt100Pv() {
		return appRt100Pv;
	}

	public void setAppRt100Pv(long appRt100Pv) {
		this.appRt100Pv = appRt100Pv;
	}

	public long getAppRt500pv() {
		return appRt500pv;
	}

	public void setAppRt500pv(long appRt500pv) {
		this.appRt500pv = appRt500pv;
	}

	public long getAppRt1000pv() {
		return appRt1000pv;
	}

	public void setAppRt1000pv(long appRt1000pv) {
		this.appRt1000pv = appRt1000pv;
	}

	public long getUrlRt100Pv() {
		return urlRt100Pv;
	}

	public void setUrlRt100Pv(long urlRt100Pv) {
		this.urlRt100Pv = urlRt100Pv;
	}

	public long getUrlRt500pv() {
		return urlRt500pv;
	}

	public void setUrlRt500pv(long urlRt500pv) {
		this.urlRt500pv = urlRt500pv;
	}

	public long getUrlRt1000pv() {
		return urlRt1000pv;
	}

	public void setUrlRt1000pv(long urlRt1000pv) {
		this.urlRt1000pv = urlRt1000pv;
	}



	
	
	

}
