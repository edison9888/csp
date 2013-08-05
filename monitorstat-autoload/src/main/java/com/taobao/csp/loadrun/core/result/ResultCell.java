
package com.taobao.csp.loadrun.core.result;

import java.util.Date;

import com.taobao.csp.loadrun.core.constant.ResultKey;

/**
 * 
 * @author xiaodu
 * @version 2011-6-28 обнГ07:00:38
 */
public class ResultCell {
	
	private ResultKey key;
	private Double value;
	private Date time ;
	public ResultKey getKey() {
		return key;
	}
	public void setKey(ResultKey key) {
		this.key = key;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
	
	

}
