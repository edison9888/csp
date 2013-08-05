
package com.taobao.csp.capacity.model.month;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.TrendModel;

/**
 * 月趋势模型接口，用来预测未来一年的每个月流量情况，
 * 月趋势，一年12月为一个周期
 * @author xiaodu
 * @version 2011-3-31 下午01:28:58
 */
public interface MonthTrendModel extends TrendModel{
	/**
	 * 实现移动平均 
	 * @param datas list 必须大于等于24月
	 * @return
	 */
	public List<Coordinate> movingAverages(List<Coordinate> datas);
	/**
	 * 建模公式
	 * @param datas 输入是的移动平均值,进行建模的数据 long[] 表示 [0] 表示 x轴 [1] 表示y轴
	 * @return 未来一年12月的值
	 */
	public void model(List<Coordinate> datas);
	
	/**
	 * 获取此方程的 拟合度
	 * @return
	 */
	public Double degreeOfFitting();
	
	/**
	 * 根据模型 获取Y
	 * @param x
	 * @return
	 */
	public Double getY(int x);
	
	
	/**
	 * 获取明年的12个月的值
	 * @return
	 * 返回值为12个月的值  double[0] = 月份 ； double[1] = 月份对应的值
	 */
	public List<Coordinate> getFutureYear();
	
	/**
	 *  获取某个月份的波动系数
	 * @param x
	 * @return
	 */
	public double getWaveRatio(int x);
	
	
	

}
