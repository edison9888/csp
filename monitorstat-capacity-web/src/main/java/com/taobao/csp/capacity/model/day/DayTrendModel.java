
package com.taobao.csp.capacity.model.day;

import java.util.List;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.TrendModel;

/**
 * 
 * @author xiaodu
 * @version 2011-4-6 ����11:12:22
 */
public interface DayTrendModel {
	
	
	/**
	 * ��ȡ����Ԥ���365�������
	 * @return ����ֵΪ365���ֵ  Coordinate x ��ӦΪyyyyMMdd
	 */
	public List<Coordinate> getFutureDay();
	
	/**
	 * Ԥ�ⷵ��12�¸�����ֵ
	 * @return ����ֵΪ12�µ�ֵ  Coordinate x ��ӦΪyyyyMM
	 */
	public List<Coordinate> getFurureMonth();
	
	

}
