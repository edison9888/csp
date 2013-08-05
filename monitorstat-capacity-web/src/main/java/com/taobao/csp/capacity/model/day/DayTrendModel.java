
package com.taobao.csp.capacity.model.day;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.TrendModel;

/**
 * 
 * @author xiaodu
 * @version 2011-4-6 上午11:12:22
 */
public interface DayTrendModel {
	
	
	/**
	 * 获取明年预测的365天的数据
	 * @return 返回值为365天的值  Coordinate x 对应为yyyyMMdd
	 */
	public List<Coordinate> getFutureDay();
	
	/**
	 * 预测返回12月个流量值
	 * @return 返回值为12月的值  Coordinate x 对应为yyyyMM
	 */
	public List<Coordinate> getFurureMonth();
	
	

}
