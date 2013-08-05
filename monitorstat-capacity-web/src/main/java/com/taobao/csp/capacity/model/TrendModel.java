
package com.taobao.csp.capacity.model;

import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2011-4-14 ����11:20:49
 */
public interface TrendModel {
	
	/**
	 * ʵ���ƶ�ƽ�� ���ƶ�ƽ����Ҫ���������ݵ�������
	 * @param 
	 * @return
	 */
	public List<Coordinate> movingAverages(List<Coordinate> datas);
	/**
	 * ��ģ��ʽ
	 * @param datas �����ǵ��ƶ�ƽ��ֵ,���н�ģ������ long[] ��ʾ [0] ��ʾ x�� [1] ��ʾy��
	 * @return δ��һ��12�µ�ֵ
	 */
	public void model(List<Coordinate> datas);
	
	/**
	 * ��ȡ�˷��̵� ��϶�
	 * @return
	 */
	public Double degreeOfFitting();
	
	/**
	 * ����ģ�� ��ȡY
	 * @param x
	 * @return
	 */
	public Double getY(int x);
	
	
	/**
	 *  ��ȡĳ���·ݵĲ���ϵ��
	 * @param x
	 * @return
	 */
	public double getWaveRatio(int x);

}
