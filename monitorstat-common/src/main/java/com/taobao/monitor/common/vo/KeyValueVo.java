
package com.taobao.monitor.common.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.common.po.KeyValuePo;


/**
 * 
 * @author xiaodu
 * @version 2010-5-4 ÏÂÎç07:34:15
 */
public class KeyValueVo implements Comparable<KeyValueVo>{
	

	
	private Date date;
	
	private Map<String,KeyValuePo> map = new HashMap<String, KeyValuePo>();
	
	private String collectTime;
	
	

	public String getCollectTime() {
		return collectTime;
	}



	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}



	public Date getDate() {
		return date;
	}



	public void setDate(Date date) {
		this.date = date;
	}



	public Map<String, KeyValuePo> getMap() {
		return map;
	}



	public void setMap(Map<String, KeyValuePo> map) {
		this.map = map;
	}



	public int compareTo(KeyValueVo o) {
		
		if(o.getDate().equals(this.getDate())){
			return 0;
		}else if(o.getDate().getTime()>this.getDate().getTime()){
			return 1;
		}else if(o.getDate().getTime()<this.getDate().getTime()){
			return -1;
		}
		return 0;
		
	}

}
