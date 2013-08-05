
package com.taobao.csp.capacity.model.month;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.TrendModel;

/**
 * ������ģ�ͽӿڣ�����Ԥ��δ��һ���ÿ�������������
 * �����ƣ�һ��12��Ϊһ������
 * @author xiaodu
 * @version 2011-3-31 ����01:28:58
 */
public interface MonthTrendModel extends TrendModel{
	/**
	 * ʵ���ƶ�ƽ�� 
	 * @param datas list ������ڵ���24��
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
	 * ��ȡ�����12���µ�ֵ
	 * @return
	 * ����ֵΪ12���µ�ֵ  double[0] = �·� �� double[1] = �·ݶ�Ӧ��ֵ
	 */
	public List<Coordinate> getFutureYear();
	
	/**
	 *  ��ȡĳ���·ݵĲ���ϵ��
	 * @param x
	 * @return
	 */
	public double getWaveRatio(int x);
	
	
	

}
