package com.taobao.csp.loadrun.core.result;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.taobao.csp.loadrun.core.constant.ResultDetailType;

/***
 * 压测结果详细信息的存储单元
 * 压测结果的详细信息分为
 * 
 * 一、两级key：
 * 1、key的类型(ResultDetailType)
 * 2、具体的key
 * 
 * 二、两个value：
 * 1、调用次数（总和）
 * 2、响应时间（总和）
 * 
 * 三、一个采集时间（毫秒数/10000*10000,10秒钟一次)
 * 
 * 此类负责存储第一部分的两个key和第三部分的采集时间
 * 
 * @author youji.zj
 * @version 2012-06-22
 *
 */
public class ResultDetailKey {
	
	private ResultDetailType mKey;
	
	private String sKey;
	
	private long collectTime;
	
	public ResultDetailKey(ResultDetailType mKey, String sKey, long collectTime) {
		this.mKey = mKey;
		this.sKey = sKey;
		this.collectTime = collectTime;
	}

	public ResultDetailType getmKey() {
		return mKey;
	}

	public void setmKey(ResultDetailType mKey) {
		this.mKey = mKey;
	}

	public String getsKey() {
		return sKey;
	}

	public void setsKey(String sKey) {
		this.sKey = sKey;
	}
	
	public long getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(long collectTime) {
		this.collectTime = collectTime;
	}
	
	public String getTransferDate() {
		SimpleDateFormat sf = new SimpleDateFormat("HH-mm-ss");
		Date date = new Date(collectTime);
		
		String dateS = sf.format(date);
		return dateS;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof ResultDetailKey)) return false;
		
		ResultDetailKey key = (ResultDetailKey)object;
		if (key.getmKey().equals(this.getmKey()) && key.getsKey().equals(this.getsKey())
				&& key.getCollectTime() == this.getCollectTime()) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + mKey.hashCode();
		result = 37 * result + sKey.hashCode();
		result = 37 * result + (int) (collectTime ^ (collectTime >>> 32));
		
		return result;
	}
	
	@Override
	public String toString() {
		String dateS = getTransferDate();
		return mKey + ":"+ sKey + ":" + dateS;
	}
}
