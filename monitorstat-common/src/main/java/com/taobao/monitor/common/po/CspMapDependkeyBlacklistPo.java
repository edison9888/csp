package com.taobao.monitor.common.po;

import java.sql.Timestamp;
import java.util.Date;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * csp_map_dependkey_blacklist ±í
 * @author zhongting.zy
 */
public class CspMapDependkeyBlacklistPo {
	private String ownApp;
	private String appName;
	private String keyName;
	private Timestamp updateTime;
	private String updateBy;
	public String getOwnApp() {
		return ownApp;
	}
	public void setOwnApp(String ownApp) {
		this.ownApp = ownApp;
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
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	public Timestamp getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}
}
