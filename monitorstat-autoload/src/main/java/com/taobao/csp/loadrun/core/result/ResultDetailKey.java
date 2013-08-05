package com.taobao.csp.loadrun.core.result;

import java.text.SimpleDateFormat;
import java.util.Date;

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
 * ���ฺ��洢��һ���ֵ�����key�͵������ֵĲɼ�ʱ��
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
