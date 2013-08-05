package com.taobao.csp.day.tddl;

import java.io.Serializable;

/***
 * tddl log µÄkeyÐÅÏ¢
 * @author youji.zj
 * 
 * @version 1.0 2012-08-20
 *
 */
public class TddlLogKey implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String appName;
	
	private String dbFeature;
	
	private String dbName;
	
	private String dbIp;
	
	private String dbPort;
	
	private String sql;
	
	private String collectTime;
	
	public TddlLogKey() {
		
	}
	
	public TddlLogKey(String appName, String dbFeature, String dbName, String dbIp,
			String dbPort, String sql, String collectTime) {
		this.appName = appName;
		this.dbFeature = dbFeature;
		this.dbName = dbName;
		this.dbIp = dbIp;
		this.dbPort = dbPort;
		this.sql = sql;
		this.collectTime = collectTime;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDbFeature() {
		return dbFeature;
	}

	public void setDbFeature(String dbFeature) {
		this.dbFeature = dbFeature;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbIp() {
		return dbIp;
	}

	public void setDbIp(String dbIp) {
		this.dbIp = dbIp;
	}

	public String getDbPort() {
		return dbPort;
	}

	public void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof TddlLogKey)) return false;
		
		TddlLogKey po = (TddlLogKey)object;
		if (po.getAppName().equals(this.getAppName()) && po.getDbName().equals(this.getDbName()) && 
				po.getDbFeature().equals(this.getDbFeature()) && 
				po.getDbIp().equals(this.getDbIp()) && po.getDbPort().equals(this.getDbPort()) &&
				po.getSql().equals(this.getSql()) && po.getCollectTime().equals(this.getCollectTime())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + appName.hashCode();
		result = 37 * result + dbFeature.hashCode();
		result = 37 * result + dbName.hashCode();
		result = 37 * result + dbIp.hashCode();
		result = 37 * result + dbPort.hashCode();
		result = 37 * result + sql.hashCode();
		result = 37 * result + collectTime.hashCode();
		
		return result;
	}

}
