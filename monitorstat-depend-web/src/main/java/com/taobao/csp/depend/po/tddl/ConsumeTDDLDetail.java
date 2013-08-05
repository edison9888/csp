package com.taobao.csp.depend.po.tddl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;

import com.taobao.monitor.common.util.Utlitites;

public class ConsumeTDDLDetail {
	private String appName;
	private String dbName;
	private String dbIp;
	private String dbPort;
	private String sqlText;
	private String executeType;
	private long executeSum;
	private float timeAverage;
	private String maxTime;
	private String minTime;
	private String appHostIp;
	private String appSiteName;
	private Date collect_time;

	private String collect_time_str;
	// 总时间
	private float totalTime;

	private long readCount;
	private long writeCount;
	private String readWriteRate;
	private String tableName;

	private String readCountStr;
	private String writeCountStr;
	
	private String totalTimeStr;
	
	
	
	public String getCollect_time_str() {
		return collect_time_str;
	}

	public void setCollect_time_str(String collect_time_str) {
		this.collect_time_str = collect_time_str;
	}

	// db中使用别名，来分别指代应用名、db名
	private String name;
	// <%=Utlitites.fromatLong(is.getCallAllNum()+"")%>
	private String executeSumStr;

	public String getExecuteSumStr() {
		return Utlitites.fromatLong(executeSum + "");
	}

	public void setExecuteSumStr(String executeSumStr) {
		this.executeSumStr = executeSumStr;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMaxTime() {
		return maxTime;
	}

	public void setMaxTime(String maxTime) {
		this.maxTime = maxTime;
	}

	public String getMinTime() {
		return minTime;
	}

	public void setMinTime(String minTime) {
		this.minTime = minTime;
	}

	public HashMap<String, Long> getSiteMap() {
		return siteMap;
	}

	public void setSiteMap(HashMap<String, Long> siteMap) {
		this.siteMap = siteMap;
	}

	// key-value: machineroomname-Long
	public HashMap<String, Long> siteMap = new HashMap<String, Long>();

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
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

	public float getTimeAverage() {
		// /System.out.println("timeAverage: " + timeAverage);
		if (Float.isNaN(timeAverage))
			return timeAverage;
		BigDecimal bd = new BigDecimal(timeAverage);
		bd = bd.setScale(4, RoundingMode.HALF_EVEN);
		return bd.floatValue();
	}

	public void setTimeAverage(float timeAverage) {
		this.timeAverage = timeAverage;
	}

	public String getAppSiteName() {
		return appSiteName;
	}

	public void setAppSiteName(String appSiteName) {
		this.appSiteName = appSiteName;
	}

	public Date getCollect_time() {
		return collect_time;
	}

	public void setCollect_time(Date collect_time) {
		this.collect_time = collect_time;
	}

	public String getAppHostIp() {
		return appHostIp;
	}

	public void setAppHostIp(String appHostIp) {
		this.appHostIp = appHostIp;
	}

	public float getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(float totalTime) {
		this.totalTime = totalTime;
	}

	public long getReadCount() {
		return readCount;
	}

	public void setReadCount(long readCount) {
		this.readCount = readCount;
	}

	public long getWriteCount() {
		return writeCount;
	}

	public void setWriteCount(long writeCount) {
		this.writeCount = writeCount;
	}

	public String getReadWriteRate() {
		return readWriteRate;
	}

	public void setReadWriteRate(String readWriteRate) {
		this.readWriteRate = readWriteRate;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	

	public String getReadCountStr() {
		return Utlitites.fromatLong(readCount + "");
	}

	public String getWriteCountStr() {
		return Utlitites.fromatLong(writeCount + "");
	}
	
	

	public String getTotalTimeStr() {
		return Utlitites.fromatLong(((long)totalTime) + "");
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + "\t " + this.executeSum + "\t "
				+ this.totalTime + "\t " + this.timeAverage + "\t"
				+ this.sqlText + "\n";
	}


}
