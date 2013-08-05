
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.po;

/**
 * @author xiaodu
 *
 * ÏÂÎç4:59:59
 */
public class CspCallsRelationship {
	
	private String sourceUrl;
	
	private String sourceApp;
	
	private String origin;
	
	private String originApp;
	
	private String target;
	
	private String targetApp;
	
	private float rate;

	private int type;
	
	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public float getRate() {
		return rate;
	}

	public void setRate(float rate) {
		this.rate = rate;
	}

	public String getSourceApp() {
		return sourceApp;
	}

	public void setSourceApp(String sourceApp) {
		this.sourceApp = sourceApp;
	}

	public String getOriginApp() {
		return originApp;
	}

	public void setOriginApp(String originApp) {
		this.originApp = originApp;
	}

	public String getTargetApp() {
		return targetApp;
	}

	public void setTargetApp(String targetApp) {
		this.targetApp = targetApp;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
