package com.taobao.csp.capacity.filter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.csp.capacity.po.PvcountPo;
import com.taobao.monitor.common.util.Arith;

/**
 * ȥ���Ĺ�����
 * 
 * 1�������Ǹ��ݵ�ǰ��͹�ȥ�������ڵ�ͬһ��ı�������20%������ͻ���
 * 2����������Ļ��ٺ�������ǰ������칲�����һ��ƽ�������Ƚϣ��������Χ�����࣬����Ϊ�ǻ��㣬���������ڴ�������ʱ�����
 * 3�������ֻ�������Ϊ���ݱ���Ϊ0���ռ�ϵͳ�����⣩,���������Χ������һ����0�ģ�����ǰ����ͬһ��ľ�ֵ��Ϊ����ֵ
 * @author wuhaiqian.pt
 *
 */
public class AverFilter implements IfilterBreak {


	@Override
	public List<PvcountPo> filter(List<PvcountPo> needFilterList) {
		if (needFilterList == null || needFilterList.size() == 0) return needFilterList;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int averWeek = 2;	//���ݺ�ǰ��2�����ڵ�ͬһ���
		List<PvcountPo> newList = new ArrayList<PvcountPo>();
		Map<Date, Double> pvMap = new HashMap<Date, Double>();
		for(PvcountPo p : needFilterList) {
			pvMap.put(p.getCollectTime(), p.getPvCount());
		}
		//List<PvcountPo> oldList = needFilterList;
		
		Collections.sort(needFilterList);
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		if(needFilterList.get(0) != null) {
			start.setTime(needFilterList.get(0).getCollectTime());	//��ȡ������������С��ֵ
			end.setTime(needFilterList.get(needFilterList.size()-1).getCollectTime());	//��ȡ������������С��ֵ
			System.out.println("���ݿ��һ�����ݣ�" + sdf.format(start.getTime()));
			System.out.println("���ݿ��β�����ݣ�" + sdf.format(end.getTime()));
		}
//		start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DATE) + 30);
		//���˷����ǽ����ǰ����ͬһ��ƽ�����Ƚϣ�����20%��ȥ��
		Calendar preTwoWeek = Calendar.getInstance();
		preTwoWeek.setTime(start.getTime());		//���Ǵӵ����ܿ�ʼ��Ϊ��һ�ܿ�ʼ���ˣ���Ϊ������Ҫǰ�����ܵ�ƽ����������ݿ����ݵĵ�һ����Ϊ�����ܵĵ����ڶ��ܵ�����
		System.out.println("ǰ�������ݣ�" + sdf.format(preTwoWeek.getTime()));
		
		start.set(Calendar.DATE, start.get(Calendar.DATE) + 7);
		Calendar preOneWeek = Calendar.getInstance();	
		preOneWeek.setTime(start.getTime());	//����ǵ����ܵ�ǰһ��
		System.out.println("ǰһ�����ݣ�" + sdf.format(preOneWeek.getTime()));
		
		start.set(Calendar.DATE, start.get(Calendar.DATE)+ 7);	//���ǵ���������
		System.out.println("�������ݣ�" + sdf.format(start.getTime()));
		
		int num = 0;
		while(start.compareTo(end) <= 0) {
			
			if(pvMap.get(start.getTime()) ==null){
				pvMap.put(start.getTime(), 1d);
			}
			if(pvMap.get(preTwoWeek.getTime())==null){
				pvMap.put(preTwoWeek.getTime(), 1d);
			}
			if(pvMap.get(preOneWeek.getTime())==null){
				pvMap.put(preOneWeek.getTime(), 1d);
			}
			
			double preAverPvCount = Arith.div(Arith.add(pvMap.get(preTwoWeek.getTime()), pvMap.get(preOneWeek.getTime())), 2);;	//ǰ���ܽ����һ��ƽ��
			double nowPvCount = pvMap.get(start.getTime());	//���������
			
			double percent = Arith.div(Math.abs(Arith.sub(nowPvCount, preAverPvCount)),preAverPvCount);	//���ǵ����ǰ����ͬһ���һ����ֵ/ǰ���ܵ�һ��ƽ��
			
			if(percent >= 0.2) {
				
				Calendar preOneDate = Calendar.getInstance();	
				preOneDate.setTime(start.getTime());
				preOneDate.set(Calendar.DATE, start.get(Calendar.DATE) - 1);	//ǰһ��
				
				
				Calendar preTwoDate = Calendar.getInstance();	
				preTwoDate.setTime(start.getTime());
				preTwoDate.set(Calendar.DATE, start.get(Calendar.DATE) - 2);	//ǰ����

				Calendar nextOneDate = Calendar.getInstance();	
				nextOneDate.setTime(start.getTime());
				nextOneDate.set(Calendar.DATE, start.get(Calendar.DATE) + 1);	//��һ��

				Calendar nextTwoDate = Calendar.getInstance();	
				nextTwoDate.setTime(start.getTime());
				nextTwoDate.set(Calendar.DATE, start.get(Calendar.DATE) + 2);	//������

				Double preOneDatePvCount = pvMap.get(preOneDate.getTime())==null?0:pvMap.get(preOneDate.getTime());
				Double preTwoDatePvCount = pvMap.get(preTwoDate.getTime())==null?0:pvMap.get(preTwoDate.getTime());
				Double nextOneDatePvCount = pvMap.get(nextOneDate.getTime())==null?0:pvMap.get(nextOneDate.getTime());
				Double nextTwoDatePvCount = pvMap.get(nextTwoDate.getTime())==null?0:pvMap.get(nextTwoDate.getTime());
				double f = 0d;
				int s = 0;
				if(preOneDatePvCount != null){
					f=Arith.add(f, preOneDatePvCount);
					s++;
				}
				if(preTwoDatePvCount != null){
					f=Arith.add(f, preTwoDatePvCount);
					s++;
				}
				if(nextOneDatePvCount != null){
					f=Arith.add(f, nextOneDatePvCount);
					s++;
				}
				if(nextTwoDatePvCount != null){
					f=Arith.add(f, nextTwoDatePvCount);
					s++;
				}
				Double aroundAver = Arith.div(f, s); //���ǵ���ǰ����ͺ������ƽ��
				
				double percent1 = Arith.div(Math.abs(Arith.sub(nowPvCount, aroundAver)),aroundAver);	//���ǵ����ǰ����ͬһ���һ����ֵ/ǰ���ܵ�һ��ƽ��
				if(percent1 >= 0.2){
					
					System.out.println("------------------------------------------------------------------");
					System.out.println("�����ڶ��ܵ���: " + sdf.format(preTwoWeek.getTime()) + " �����ڶ��ܵ����pv " + pvMap.get(preTwoWeek.getTime()));
					System.out.println("������һ�ܵ��죺  " + sdf.format(preOneWeek.getTime()) + "  ������һ�ܵ����pv " + pvMap.get(preOneWeek.getTime()));
					System.out.println("���� " + sdf.format(start.getTime()) + "  ����:" + nowPvCount);
					System.out.println("preOneDate: " + sdf.format(preOneDate.getTime()));
					System.out.println("preTwoDate: " + sdf.format(preOneDate.getTime()));
					System.out.println("nextOneDate: " + sdf.format(preOneDate.getTime()));
					System.out.println("nextTwoDate: " + sdf.format(preOneDate.getTime()));
					
					if(preOneDatePvCount != 0 && preTwoDatePvCount != 0 && nextOneDatePvCount != 0 && nextTwoDatePvCount != 0) {	//�����Χ���춼û�г���0���쳣ֵ����ô������Χ4���ƽ��ֵȡ��
						
						pvMap.put(start.getTime(), aroundAver);
						System.out.println(sdf.format(start.getTime()) + " �����ݳ������⡣����Ϊ" + percent + " ����Χ�����ƽ��ֵ�� " + nowPvCount + " �滻�� " + aroundAver);

					} else {
						pvMap.put(start.getTime(), preAverPvCount);
						System.out.println(sdf.format(start.getTime()) + " �����ݳ������⡣����Ϊ" + percent + " ����������ͬһ���ƽ��ֵ�� " + nowPvCount + " �滻�� " + preAverPvCount);

					}
					num++;
				}
			}
			start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);			//�Ƶ���һ��
			preOneWeek.set(Calendar.DATE, preOneWeek.get(Calendar.DATE) + 1);
			preTwoWeek.set(Calendar.DATE, preTwoWeek.get(Calendar.DATE) + 1);
			
		}
		
		System.out.println("���滻��" + num + "��");
		int nn = 0;
		for(PvcountPo p : needFilterList){
			
			if(pvMap.get(p.getCollectTime()) != p.getPvCount()){
				
				p.setPvCount(pvMap.get(p.getCollectTime()));
				nn++;
			}
		}
		System.out.println("���޸���" + nn + "��ԭʼ����");
		return needFilterList;
	}
	

}
