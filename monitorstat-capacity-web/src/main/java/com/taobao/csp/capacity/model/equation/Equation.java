
package com.taobao.csp.capacity.model.equation;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;

/**
 * 方程接口类
 * @author xiaodu
 * @version 2011-4-12 下午01:49:37
 */
public interface Equation {
	
	/**
	 * 计算模拟 方程接口
	 * @param datas 坐标值
	 */
	public void compute(List<Coordinate> datas);
	
	
	public double getY(long x);

}
