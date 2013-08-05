
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.po;

/**
 * @author xiaodu
 *
 * ÏÂÎç3:37:28
 */
public class CspKeyMode {
	private int id;
	private String appName;
	
	private String keyName;
	
	private String propertyName;
	
	private int baseline;
	
	private int alarm;
	
	private String checkMode;
	
	private String modeConfig;
	
	private String hostModeConfig;
	private String appModeConfig;
	
	
	private String keyScope;
	private String keyAlias;

	private int keyLevel;
	
	public String getHostModeConfig() {
		return hostModeConfig;
	}

	public void setHostModeConfig(String hostModeConfig) {
		this.hostModeConfig = hostModeConfig;
	}

	public String getAppModeConfig() {
		return appModeConfig;
	}

	public void setAppModeConfig(String appModeConfig) {
		this.appModeConfig = appModeConfig;
	}

	public String getKeyAlias() {
		return keyAlias;
	}

	public void setKeyAlias(String keyAlias) {
		this.keyAlias = keyAlias;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getBaseline() {
		return baseline;
	}

	public void setBaseline(int baseline) {
		this.baseline = baseline;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public String getCheckMode() {
		return checkMode;
	}

	public void setCheckMode(String checkMode) {
		this.checkMode = checkMode;
	}

	public String getModeConfig() {
		return modeConfig;
	}

	public void setModeConfig(String modeConfig) {
		this.modeConfig = modeConfig;
	}

	public String getKeyScope() {
		return keyScope;
	}

	public void setKeyScope(String keyScope) {
		this.keyScope = keyScope;
	}

	public int getKeyLevel() {
		return keyLevel;
	}

	public void setKeyLevel(int keyLevel) {
		this.keyLevel = keyLevel;
	}
}
