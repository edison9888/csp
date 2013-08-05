
package com.taobao.csp.capacity.model.day;

import java.util.List;

import com.taobao.csp.capacity.po.PvcountPo;

/**
 * 
 * @author xiaodu
 * @version 2011-4-6 ����11:12:22
 */
public abstract class AbstractDayTrendModel  implements DayTrendModel{
	
	private List<PvcountPo> originalDayList;//ԭʼ�������ݣ�����ģ������ԭʼ���� Ϊһ��365���list���� 
	private int year;
	private int appId;
	
	public int getAppId() {
		return appId;
	}
	public int getYear() {
		return year;
	}
	public List<PvcountPo> getOriginalDayList() {
		return originalDayList;
	}
	
	/**
	 * ���캯��������Ϊ365���ԭʼ����
	 * @param days
	 */
	public AbstractDayTrendModel(int appId,int year,List<PvcountPo> days){
		this.year = year;
		this.appId = appId;
		this.originalDayList = days;
	}
	
	
	

}
