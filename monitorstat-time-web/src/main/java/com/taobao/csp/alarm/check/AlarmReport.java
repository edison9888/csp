
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.check;

/**
 * @author xiaodu
 *
 * 上午10:58:51
 */
public class AlarmReport {
	
	private String modeName;
	
	private String appName;
	
	private String keyName;
	
	private String propertyName;
	
	private String keyAlias; // key的别名
	
	private String keyScope;
	
	private String keyLevel = "P1";
	
	private long time;//时间
	
	private Object value;//监控值
	
	private String cause; //原因
	
	private int continuous;
	
	private String ip;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getModeName() {
		return modeName;
	}

	public void setModeName(String modeName) {
		this.modeName = modeName;
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

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getKeyScope() {
		return keyScope;
	}

	public void setKeyScope(String keyScope) {
		this.keyScope = keyScope;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public int getContinuous() {
		return continuous;
	}

	public void setContinuous(int continuous) {
		this.continuous = continuous;
	}

	public String getKeyLevel() {
		return keyLevel;
	}

	public void setKeyLevel(String keyLevel) {
		this.keyLevel = keyLevel;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

}
