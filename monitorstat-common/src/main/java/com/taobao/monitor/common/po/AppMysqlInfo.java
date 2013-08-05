
package com.taobao.monitor.common.po;


/**
 * 
 * @author xiaodu
 * @version 2010-12-31 ÉÏÎç10:09:55
 */
public class AppMysqlInfo {
	
	private int dataBaseId;
	
	private int appId;
	
	private int keyNum;
	
	private int dataNum;
	
	private int dayDataNum;
	
	private int collectTime; //yyyyMMdd

	public int getDayDataNum() {
		return dayDataNum;
	}

	public void setDayDataNum(int dayDataNum) {
		this.dayDataNum = dayDataNum;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public int getKeyNum() {
		return keyNum;
	}

	public void setKeyNum(int keyNum) {
		this.keyNum = keyNum;
	}

	public int getDataNum() {
		return dataNum;
	}

	public void setDataNum(int dataNum) {
		this.dataNum = dataNum;
	}

	public int getDataBaseId() {
		return dataBaseId;
	}

	public void setDataBaseId(int dataBaseId) {
		this.dataBaseId = dataBaseId;
	}

	public int getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(int collectTime) {
		this.collectTime = collectTime;
	}

	
	
	
	
	
}
