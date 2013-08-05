package com.taobao.monitor.web.tddl;


public class TddlPo implements Comparable<TddlPo> {
	
	private String opsName;
	
	private String dbName;
	
	private String sqlText;
	
	private String executeType;
	
	private long executeSum;
	
	private double timeAverage;
	
	private String hostIp;
	
	private String hostSite;
	
	private String collectTime;

	public String getOpsName() {
		return opsName;
	}

	public void setOpsName(String opsName) {
		this.opsName = opsName;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getSqlText() {
		return sqlText;
	}

	public void setSqlText(String sqlText) {
		this.sqlText = sqlText;
	}

	public String getExecuteType() {
		return executeType;
	}

	public void setExecuteType(String executeType) {
		this.executeType = executeType;
	}

	public long getExecuteSum() {
		return executeSum;
	}

	public void setExecuteSum(long executeSum) {
		this.executeSum = executeSum;
	}

	public double getTimeAverage() {
		return timeAverage;
	}

	public void setTimeAverage(double timeAverage) {
		this.timeAverage = timeAverage;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

	public String getHostSite() {
		return hostSite;
	}

	public void setHostSite(String hostSite) {
		this.hostSite = hostSite;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}

	@Override
	public int compareTo(TddlPo o) {
		TddlPo p1 = (TddlPo) o;
		if (executeSum > p1.getExecuteSum()) {
			return -1;
		} else if (executeSum == p1.getExecuteSum()) {
			return 0;
		} else {
			return 1;
		}
	}

}
