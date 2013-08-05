
package com.taobao.csp.capacity.model;

import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2011-4-14 上午11:20:49
 */
public interface TrendModel {
	
	/**
	 * 实现移动平均 ，移动平均主要是消除数据的周期性
	 * @param 
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
	 *  获取某个月份的波动系数
	 * @param x
	 * @return
	 */
	public double getWaveRatio(int x);

}
