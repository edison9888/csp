package com.taobao.csp.loadrun.core;

import java.text.DecimalFormat;
import java.util.Date;

import com.taobao.csp.loadrun.core.constant.ResultDetailType;

/***
 * 压测结果的详细信息
 * @author youji.zj
 * @version 2012-06-29
 */
public class LoadrunResultDetail implements Comparable<LoadrunResultDetail>{
	
	private String loadId;
	
	private int loadrunOrder;
	
	private ResultDetailType mainKey;
	
	private String secendaryKey;
	
	private double count;
	
	private double times;
	
	private Date collectTime;

	public String getLoadId() {
		return loadId;
	}

	public void setLoadId(String loadId) {
		this.loadId = loadId;
	}

	public int getLoadrunOrder() {
		return loadrunOrder;
	}

	public void setLoadrunOrder(int loadrunOrder) {
		this.loadrunOrder = loadrunOrder;
	}

	public ResultDetailType getMainKey() {
		return mainKey;
	}

	public void setMainKey(ResultDetailType mainKey) {
		this.mainKey = mainKey;
	}

	public String getSecendaryKey() {
		return secendaryKey;
	}

	public void setSecendaryKey(String secendaryKey) {
		this.secendaryKey = secendaryKey;
	}

	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getTimes() {
		return times;
	}

	public void setTimes(double times) {
		this.times = times;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}
	
	public String diaplayString() {
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormat df1 = new DecimalFormat("0");
		
		if (mainKey == ResultDetailType.PERFORMANCE_INDEX) {
			return String.valueOf(df.format(times));
		} else if (mainKey == ResultDetailType.TAIR ){
			if (secendaryKey.indexOf("/hit") > -1) {
				return count + "/" + times + "hit";
			} else if (secendaryKey.indexOf("/len") > -1) {
				return count + "/" + df.format(times / count) + "B";
			} else {
				return count + "/" + df.format(times / count) + "ms";
			}
		} else if(mainKey == ResultDetailType.IO_DATA) {
			if (secendaryKey.indexOf("byt") > -1) {
				return df1.format(times) + "B";
			} else {
				return df1.format(times);
			}
		} else {
			return count + "/" + df.format(times / count) + "ms";
		}
	}

	@Override
	public int compareTo(LoadrunResultDetail o) {
		String priority1 = getPriorityString();
		String priority2 = o.getPriorityString();
		
		int num = priority1.compareTo(priority2);
		if(num != 0) {
			return num;
		} else {
			return getCollectTime().compareTo(o.getCollectTime());
		}
	}
	
	private String getPriorityString() {
		String priority = mainKey.toString();
		
		// 性能数据排最前面
		if (mainKey == ResultDetailType.PERFORMANCE_INDEX) {
			priority = "0" + priority;
		}
		
		if (mainKey == ResultDetailType.IO_DATA) {
			priority = "1" + priority;
		}
		
		if (mainKey == ResultDetailType.URL) {
			priority = "2" + priority;
		}
		
		if (mainKey == ResultDetailType.HSF_PV_RT) {
			priority = "3" + priority;
		}
		
		priority += secendaryKey;
		return priority;
	}
}
