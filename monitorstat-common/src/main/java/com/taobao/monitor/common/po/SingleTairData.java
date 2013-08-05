package com.taobao.monitor.common.po;

/**
 * 描述tair中，显示出的最小单位，即一行
 * @author denghaichuan.pt
 * @version 2011-9-7
 */

public class SingleTairData implements Comparable<SingleTairData> {
	
	private String namespace;
	
	private long data1 = 0;
	
	private long data2 = 0;
	
	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public long getData1() {
		return data1;
	}

	public void setData1(long data1) {
		this.data1 = data1;
	}

	public long getData2() {
		return data2;
	}

	public void setData2(long data2) {
		this.data2 = data2;
	}
	
	public void addData1(long data) {
		this.data1 += data;
	}
	
	public void addData2(long data) {
		this.data2 += data;
	}

	@Override
	public int compareTo(SingleTairData o) {
		if (data2 > o.data2) {
			return 1;
		} else if (data2 == o.data2) {
			return 0;
		} else {
			return -1;
		}
	}
	
//	public String getAveData() {
//		String aveData = "";
//		DecimalFormat decimal = new DecimalFormat("0.00");
//		decimal.setRoundingMode(RoundingMode.HALF_UP);
//		if (actionType != null) {
//			if (actionType.indexOf("hit") > -1) {
//				aveData = decimal.format((float)data1 / (float) data2 * 100) + "%";
//			} else {
//				aveData = decimal.format((float)data1 / (float) data2);
//			}
//		}
//		return aveData;
//		
//	}
}
