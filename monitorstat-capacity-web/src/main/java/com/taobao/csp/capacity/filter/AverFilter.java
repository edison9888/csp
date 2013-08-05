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
 * 去噪点的过滤累
 * 
 * 1、①这是根据当前天和过去两个星期的同一天的比相差不超过20%来过滤突变的
 * 2、当①满足的话再和这个点的前后各两天共四天的一个平均来做比较，如果和周围四天差不多，则不认为是坏点，这里尤其在处理过年的时候管用
 * 3、另外又坏点是因为数据本身为0（收集系统的问题）,所以如果周围四天有一个是0的，则由前两周同一天的均值作为修正值
 * @author wuhaiqian.pt
 *
 */
public class AverFilter implements IfilterBreak {


	@Override
	public List<PvcountPo> filter(List<PvcountPo> needFilterList) {
		if (needFilterList == null || needFilterList.size() == 0) return needFilterList;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int averWeek = 2;	//数据和前面2个星期的同一天比
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
			start.setTime(needFilterList.get(0).getCollectTime());	//获取数据中日期最小的值
			end.setTime(needFilterList.get(needFilterList.size()-1).getCollectTime());	//获取数据中日期最小的值
			System.out.println("数据库第一天数据：" + sdf.format(start.getTime()));
			System.out.println("数据库结尾天数据：" + sdf.format(end.getTime()));
		}
//		start.set(Calendar.DAY_OF_MONTH, start.get(Calendar.DATE) + 30);
		//过滤方法是今天和前两周同一天平均做比较，相差超过20%的去掉
		Calendar preTwoWeek = Calendar.getInstance();
		preTwoWeek.setTime(start.getTime());		//我们从第三周开始作为第一周开始过滤，因为我们需要前面两周的平均，因此数据库数据的第一天作为第三周的倒数第二周的数据
		System.out.println("前两周数据：" + sdf.format(preTwoWeek.getTime()));
		
		start.set(Calendar.DATE, start.get(Calendar.DATE) + 7);
		Calendar preOneWeek = Calendar.getInstance();	
		preOneWeek.setTime(start.getTime());	//这个是第三周的前一周
		System.out.println("前一周数据：" + sdf.format(preOneWeek.getTime()));
		
		start.set(Calendar.DATE, start.get(Calendar.DATE)+ 7);	//这是第三周数据
		System.out.println("这周数据：" + sdf.format(start.getTime()));
		
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
			
			double preAverPvCount = Arith.div(Arith.add(pvMap.get(preTwoWeek.getTime()), pvMap.get(preOneWeek.getTime())), 2);;	//前两周今天的一个平均
			double nowPvCount = pvMap.get(start.getTime());	//今天的数据
			
			double percent = Arith.div(Math.abs(Arith.sub(nowPvCount, preAverPvCount)),preAverPvCount);	//这是当天和前两周同一天的一个差值/前两周的一个平均
			
			if(percent >= 0.2) {
				
				Calendar preOneDate = Calendar.getInstance();	
				preOneDate.setTime(start.getTime());
				preOneDate.set(Calendar.DATE, start.get(Calendar.DATE) - 1);	//前一天
				
				
				Calendar preTwoDate = Calendar.getInstance();	
				preTwoDate.setTime(start.getTime());
				preTwoDate.set(Calendar.DATE, start.get(Calendar.DATE) - 2);	//前两天

				Calendar nextOneDate = Calendar.getInstance();	
				nextOneDate.setTime(start.getTime());
				nextOneDate.set(Calendar.DATE, start.get(Calendar.DATE) + 1);	//后一天

				Calendar nextTwoDate = Calendar.getInstance();	
				nextTwoDate.setTime(start.getTime());
				nextTwoDate.set(Calendar.DATE, start.get(Calendar.DATE) + 2);	//后两天

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
				Double aroundAver = Arith.div(f, s); //这是当天前两天和后两天的平均
				
				double percent1 = Arith.div(Math.abs(Arith.sub(nowPvCount, aroundAver)),aroundAver);	//这是当天和前两周同一天的一个差值/前两周的一个平均
				if(percent1 >= 0.2){
					
					System.out.println("------------------------------------------------------------------");
					System.out.println("倒数第二周当天: " + sdf.format(preTwoWeek.getTime()) + " 倒数第二周当天的pv " + pvMap.get(preTwoWeek.getTime()));
					System.out.println("倒数第一周当天：  " + sdf.format(preOneWeek.getTime()) + "  倒数第一周当天的pv " + pvMap.get(preOneWeek.getTime()));
					System.out.println("当天 " + sdf.format(start.getTime()) + "  当天:" + nowPvCount);
					System.out.println("preOneDate: " + sdf.format(preOneDate.getTime()));
					System.out.println("preTwoDate: " + sdf.format(preOneDate.getTime()));
					System.out.println("nextOneDate: " + sdf.format(preOneDate.getTime()));
					System.out.println("nextTwoDate: " + sdf.format(preOneDate.getTime()));
					
					if(preOneDatePvCount != 0 && preTwoDatePvCount != 0 && nextOneDatePvCount != 0 && nextTwoDatePvCount != 0) {	//如果周围几天都没有出现0的异常值，那么就用周围4天的平均值取代
						
						pvMap.put(start.getTime(), aroundAver);
						System.out.println(sdf.format(start.getTime()) + " 的数据出现问题。比率为" + percent + " 用周围四天的平均值把 " + nowPvCount + " 替换成 " + aroundAver);

					} else {
						pvMap.put(start.getTime(), preAverPvCount);
						System.out.println(sdf.format(start.getTime()) + " 的数据出现问题。比率为" + percent + " 用以往两周同一天的平均值把 " + nowPvCount + " 替换成 " + preAverPvCount);

					}
					num++;
				}
			}
			start.set(Calendar.DATE, start.get(Calendar.DATE) + 1);			//移到下一天
			preOneWeek.set(Calendar.DATE, preOneWeek.get(Calendar.DATE) + 1);
			preTwoWeek.set(Calendar.DATE, preTwoWeek.get(Calendar.DATE) + 1);
			
		}
		
		System.out.println("共替换了" + num + "天");
		int nn = 0;
		for(PvcountPo p : needFilterList){
			
			if(pvMap.get(p.getCollectTime()) != p.getPvCount()){
				
				p.setPvCount(pvMap.get(p.getCollectTime()));
				nn++;
			}
		}
		System.out.println("共修改了" + nn + "个原始数据");
		return needFilterList;
	}
	

}
