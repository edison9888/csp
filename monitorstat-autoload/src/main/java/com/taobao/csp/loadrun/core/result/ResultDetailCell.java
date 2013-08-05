
package com.taobao.csp.loadrun.core.result;

import java.io.Serializable;
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
 * @see ResultDetailKey
 * 
 * @author youji.zj
 * @version 2012-06-22
 *
 */
public class ResultDetailCell implements Serializable {
	
	private static final long serialVersionUID = -2017987625989064232L;

	/*** key类型 ***/
	private ResultDetailType mKey;
	
	/*** key ***/
	private String sKey;
	
	/*** 采集时间毫秒 ***/
	private long collectTime;
	
	/*** 次数,或者性能指标的值 ***/
	private double count;
	
	/*** 响应时间 ***/
	private double time;

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getTime() {
		return time;
	}

	public void setTime(double time) {
		this.time = time;
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
	
	public double getAveQps() {
		if (time <= 0) return count;
		
		return Math.ceil(((double) count) / time);
	}

	public synchronized void add(ResultDetailCell addend) {
		if (addend == null) return;
		
		count += addend.getCount();
		time += addend.getTime();
	}
	
	public synchronized void average(ResultDetailCell addend) {
		if (addend == null) return;
		
		count = (count + addend.getCount()) / 2;
		time = (time + addend.getTime()) / 2;
	}
	
	@Override
	public String toString() {
		return mKey.toString() + ":" + count + ":" + time;
	}
	
}
