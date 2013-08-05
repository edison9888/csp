package com.taobao.csp.capacity.po;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * pv统计表的实体类
 * @author wuhaiqian.pt
 *
 */
public class PvcountPo implements Comparable<PvcountPo>{

	private int appId;
	
	private Date collectTime;
	
	private Double pvCount;
	
	private String pvType;
	
	private int year;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		
		this.collectTime = collectTime;
	}

	public Double getPvCount() {
		return pvCount;
	}

	public void setPvCount(Double pvCount) {
		this.pvCount = pvCount;
	}

	public String getPvType() {
		return pvType;
	}

	public void setPvType(String pvType) {
		this.pvType = pvType;
	}

	@Override
	public int compareTo(PvcountPo o) {
		return this.getCollectTime().compareTo(o.getCollectTime());
	}

	
	
}
