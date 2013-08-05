
package com.taobao.csp.loadrun.core.result;

import java.io.Serializable;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;

/***
 * ѹ������ϸ��Ϣ�Ĵ洢��Ԫ
 * ѹ��������ϸ��Ϣ��Ϊ
 * 
 * һ������key��
 * 1��key������(ResultDetailType)
 * 2�������key
 * 
 * ��������value��
 * 1�����ô������ܺͣ�
 * 2����Ӧʱ�䣨�ܺͣ�
 * 
 * ����һ���ɼ�ʱ�䣨������/10000*10000,10����һ��)
 * 
 * @see ResultDetailKey
 * 
 * @author youji.zj
 * @version 2012-06-22
 *
 */
public class ResultDetailCell implements Serializable {
	
	private static final long serialVersionUID = -2017987625989064232L;

	/*** key���� ***/
	private ResultDetailType mKey;
	
	/*** key ***/
	private String sKey;
	
	/*** �ɼ�ʱ����� ***/
	private long collectTime;
	
	/*** ����,��������ָ���ֵ ***/
	private double count;
	
	/*** ��Ӧʱ�� ***/
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
