package com.taobao.csp.time.web.po;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AmchartTwoYPo {

	/**
	 * ������ƽ��ʱ�䣬Ҳ����ʹ����ʱ��
	 */
	private String timeUnit = "mm";
	private List<ValuePo> list = new ArrayList<ValuePo>(); 

	public class ValuePo implements Comparable<ValuePo>{
		public ValuePo(long executeSum, float timeNum, long collectTimeL) {
			this.executeSum = executeSum;
			this.timeNum = timeNum;
			this.collectTimeL = collectTimeL;
		} 
		private long executeSum;
		private float timeNum;	
		private long collectTimeL;	//�ռ����ݵ�ʱ��ĺ���ֵ		
		public long getExecuteSum() {
			return executeSum;
		}
		public void setExecuteSum(long executeSum) {
			this.executeSum = executeSum;
		}
		public float getTimeNum() {
			return timeNum;
		}
		public void setTimeNum(float timeNum) {
			this.timeNum = timeNum;
		}
		public long getCollectTimeL() {
			return collectTimeL;
		}
		public void setCollectTimeL(long collectTimeL) {
			this.collectTimeL = collectTimeL;
		}
		@Override
		public int compareTo(ValuePo o) {
			if(collectTimeL > o.collectTimeL)
				return 1;
			else if(collectTimeL > o.collectTimeL)
				return -1;
			return 0;
		}
	}

	public void addNewValueToList(long executeSum, float timeNum, long collectTimeL) {
		list.add(new ValuePo(executeSum, timeNum, collectTimeL));
	}

	public String getTimeUnit() {
		return timeUnit;
	}

	public void setTimeUnit(String timeUnit) {
		this.timeUnit = timeUnit;
	}

	public List<ValuePo> getList() {
		Collections.sort(list);
		return list;
	}

	public void setList(List<ValuePo> list) {
		Collections.sort(list);	//����һ��
		this.list = list;
	}
}	
