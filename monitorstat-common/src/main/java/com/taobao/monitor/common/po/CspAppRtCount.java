
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.po;

import java.util.Date;

/**
 * @author xiaodu
 *
 * ÏÂÎç8:40:12
 */
public class CspAppRtCount {
	
	private String appName;
	
	private String url;
	
	private long pvRtCount;
	
	private long pvRt100;
	
	private long pvRt500;
	
	private long pvRt1000;
	
	private long pvRtError;
	
	private String pvRtType;
	
	private Date collectTime;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public long getPvRtCount() {
		return pvRtCount;
	}

	public void setPvRtCount(long pvRtCount) {
		this.pvRtCount = pvRtCount;
	}

	public long getPvRt100() {
		return pvRt100;
	}

	public void setPvRt100(long pvRt100) {
		this.pvRt100 = pvRt100;
	}

	public long getPvRt500() {
		return pvRt500;
	}

	public void setPvRt500(long pvRt500) {
		this.pvRt500 = pvRt500;
	}

	public long getPvRt1000() {
		return pvRt1000;
	}

	public void setPvRt1000(long pvRt1000) {
		this.pvRt1000 = pvRt1000;
	}

	public long getPvRtError() {
		return pvRtError;
	}

	public void setPvRtError(long pvRtError) {
		this.pvRtError = pvRtError;
	}

	

	public String getPvRtType() {
		return pvRtType;
	}

	public void setPvRtType(String pvRtType) {
		this.pvRtType = pvRtType;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	
	

}
