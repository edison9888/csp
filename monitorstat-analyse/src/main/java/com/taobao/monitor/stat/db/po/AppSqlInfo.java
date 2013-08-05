package com.taobao.monitor.stat.db.po;

/**
 * @author xiaoxie
 * @create time��2010-4-10 ����03:59:58
 * @description
 */
public class AppSqlInfo {
	/** ���ݿ����� */
	private String db;
	/** Ӧ������,��detail 
	 *  detail[item],itemcenter[ic],sell,buy,shopcenter,tbuic,tc
	 * */
	private String appName;
	/** ��С������ */
	private int connMin;
	/** ��������� */
	private int connMax;
	/** �������� ��������*ÿ̨���� */
	private int connSum;
	/** SQL���� */
	private int sqlTotal;
	/** SQLִ���� */
	private int execTotal;
	/** ����ִ��ʱ�� */
	private int execAvg;
	
	public String getNewAppName(){
		if(this.appName!=null){
			if("detail".equals(this.appName)){
				return "item";
			}
			if("itemcenter".equals(this.appName)){
				return "ic";
			}
		}
		return this.appName;
	}
	

	public String getAppName() {		
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public int getConnMin() {
		return connMin;
	}

	public void setConnMin(int connMin) {
		this.connMin = connMin;
	}

	public int getConnMax() {
		return connMax;
	}

	public void setConnMax(int connMax) {
		this.connMax = connMax;
	}

	public int getConnSum() {
		return connSum;
	}

	public void setConnSum(int connSum) {
		this.connSum = connSum;
	}

	public int getSqlTotal() {
		return sqlTotal;
	}

	public void setSqlTotal(int sqlTotal) {
		this.sqlTotal = sqlTotal;
	}

	public int getExecTotal() {
		return execTotal;
	}

	public void setExecTotal(int execTotal) {
		this.execTotal = execTotal;
	}

	public int getExecAvg() {
		return execAvg;
	}

	public void setExecAvg(int execAvg) {
		this.execAvg = execAvg;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}
}
