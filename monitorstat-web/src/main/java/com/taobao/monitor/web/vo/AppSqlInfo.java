package com.taobao.monitor.web.vo;

/**
 * @author xiaoxie
 * @create time��2010-4-10 ����03:59:58
 * @description
 */
public class AppSqlInfo {
	/** ���ݿ����� */
	private String db;
	/** Ӧ������,��detail */
	private String appName;
	/** ��С������ */
	private String connMin;
	/** ��������� */
	private String connMax;
	/** �������� ��������*ÿ̨���� */
	private String connSum;
	/** SQL���� */
	private String sqlTotal;
	
	private Integer sqlTotalKeyId;
	
	/** SQLִ���� */
	private String execTotal;
	
	private Integer execTotalKeyId;
	
	/** ����ִ��ʱ�� */
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
