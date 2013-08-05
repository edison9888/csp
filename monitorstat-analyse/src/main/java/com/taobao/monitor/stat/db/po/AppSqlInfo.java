package com.taobao.monitor.stat.db.po;

/**
 * @author xiaoxie
 * @create time：2010-4-10 下午03:59:58
 * @description
 */
public class AppSqlInfo {
	/** 数据库名称 */
	private String db;
	/** 应用名称,如detail 
	 *  detail[item],itemcenter[ic],sell,buy,shopcenter,tbuic,tc
	 * */
	private String appName;
	/** 最小连接数 */
	private int connMin;
	/** 最大连接数 */
	private int connMax;
	/** 总连接数 机器数量*每台数量 */
	private int connSum;
	/** SQL总数 */
	private int sqlTotal;
	/** SQL执行数 */
	private int execTotal;
	/** 评价执行时间 */
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
