package com.taobao.monitor.web.vo;

/**
 * @author xiaoxie
 * @create time：2010-4-10 下午03:59:58
 * @description
 */
public class AppSqlInfo {
	/** 数据库名称 */
	private String db;
	/** 应用名称,如detail */
	private String appName;
	/** 最小连接数 */
	private String connMin;
	/** 最大连接数 */
	private String connMax;
	/** 总连接数 机器数量*每台数量 */
	private String connSum;
	/** SQL总数 */
	private String sqlTotal;
	
	private Integer sqlTotalKeyId;
	
	/** SQL执行数 */
	private String execTotal;
	
	private Integer execTotalKeyId;
	
	/** 评价执行时间 */
	private String execAvg;
	
	private Integer execAvgKeyId;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getConnMin() {
		return connMin;
	}

	public void setConnMin(String connMin) {
		this.connMin = connMin;
	}

	public String getConnMax() {
		return connMax;
	}

	public void setConnMax(String connMax) {
		this.connMax = connMax;
	}

	public String getConnSum() {
		return connSum;
	}

	public void setConnSum(String connSum) {
		this.connSum = connSum;
	}

	public String getSqlTotal() {
		return sqlTotal;
	}

	public void setSqlTotal(String sqlTotal) {
		this.sqlTotal = sqlTotal;
	}

	public String getExecTotal() {
		return execTotal;
	}

	public void setExecTotal(String execTotal) {
		this.execTotal = execTotal;
	}

	public String getExecAvg() {
		return execAvg;
	}

	public void setExecAvg(String execAvg) {
		this.execAvg = execAvg;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public Integer getSqlTotalKeyId() {
		return sqlTotalKeyId;
	}

	public void setSqlTotalKeyId(Integer sqlTotalKeyId) {
		this.sqlTotalKeyId = sqlTotalKeyId;
	}

	public Integer getExecTotalKeyId() {
		return execTotalKeyId;
	}

	public void setExecTotalKeyId(Integer execTotalKeyId) {
		this.execTotalKeyId = execTotalKeyId;
	}

	public Integer getExecAvgKeyId() {
		return execAvgKeyId;
	}

	public void setExecAvgKeyId(Integer execAvgKeyId) {
		this.execAvgKeyId = execAvgKeyId;
	}
}
