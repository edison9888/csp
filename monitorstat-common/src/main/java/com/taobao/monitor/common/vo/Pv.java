
package com.taobao.monitor.common.vo;

import java.util.Date;

/**
 * 
 * @author xiaodu
 * @version 2010-8-17 обнГ02:49:42
 */
public class Pv {
	
	private long pv;
	private long uv;
	private int collectDay;
	public long getPv() {
		return pv;
	}
	public void setPv(long pv) {
		this.pv = pv;
	}
	public long getUv() {
		return uv;
	}
	public void setUv(long uv) {
		this.uv = uv;
	}
	public int getCollectDay() {
		return collectDay;
	}
	public void setCollectDay(int collectDay) {
		this.collectDay = collectDay;
	}
	

}
