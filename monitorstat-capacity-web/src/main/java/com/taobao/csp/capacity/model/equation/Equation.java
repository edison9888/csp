
package com.taobao.csp.capacity.model.equation;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;

/**
 * ���̽ӿ���
 * @author xiaodu
 * @version 2011-4-12 ����01:49:37
 */
public interface Equation {
	
	/**
	 * ����ģ�� ���̽ӿ�
	 * @param datas ����ֵ
	 */
	public void compute(List<Coordinate> datas);
	
	
	public double getY(long x);

}
