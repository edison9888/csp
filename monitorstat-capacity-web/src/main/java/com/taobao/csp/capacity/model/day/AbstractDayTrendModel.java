
package com.taobao.csp.capacity.model.day;

import java.util.List;

import com.taobao.csp.capacity.po.PvcountPo;

/**
 * 
 * @author xiaodu
 * @version 2011-4-6 上午11:12:22
 */
public abstract class AbstractDayTrendModel  implements DayTrendModel{
	
	private List<PvcountPo> originalDayList;//原始数据数据，在日模型总中原始数据 为一个365天的list数据 
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
	 * 构造函数，参数为365天的原始数据
	 * @param days
	 */
	public AbstractDayTrendModel(int appId,int year,List<PvcountPo> days){
		this.year = year;
		this.appId = appId;
		this.originalDayList = days;
	}
	
	
	

}
