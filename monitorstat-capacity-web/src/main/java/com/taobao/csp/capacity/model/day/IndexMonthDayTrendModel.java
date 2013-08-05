
package com.taobao.csp.capacity.model.day;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.csp.capacity.model.Coordinate;
import com.taobao.csp.capacity.model.month.MonthTrendModel;
import com.taobao.csp.capacity.model.month.MonthTrendModelFactory;
import com.taobao.csp.capacity.po.PvcountPo;
import com.taobao.monitor.common.util.Arith;

/**
 * 
 * @author xiaodu
 * @version 2011-4-6 上午11:17:01
 */
public class IndexMonthDayTrendModel extends AbstractDayTrendModel{
	
	
	private MonthTrendModel model = null;
	
//	public IndexMonthDayTrendModel(int appId ,int year){
//		super(appId,year);
//		List<Coordinate> list = concatDayToMonth(this.getOriginalDayList());//将数据合并成月
//		model(list);
//	}
	
	

	public IndexMonthDayTrendModel(int appId ,int year,List<PvcountPo> days) {
		super(appId,year,days);
		List<Coordinate> list = concatDayToMonth(this.getOriginalDayList());//将数据合并成月
		model(list);
	}

	/**
	 * 
	 * @param datas 历史每天的流量 预测日得采用liangnian
	 */
	public void model(List<Coordinate> datas) {
		this.model = MonthTrendModelFactory.model(datas);//获取月模型
	}
	
	
	/**
	 * 预测某个月 每天的流量趋势
	 * @param x
	 * @return
	 */
	public List<Coordinate> getFeatureDayForMonth(int x){
		
		List<Coordinate> cList = new ArrayList<Coordinate>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		if(model != null){
			List<Coordinate> month = model.getFutureYear();
			Coordinate d = month.get((x-1));
			Map<Integer, Double> weekratioMap = computeWeekDayRatio(x);
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, this.getYear());
			cal.set(Calendar.MONTH, x-1);
			cal.set(Calendar.DAY_OF_MONTH, 1);
			for(int i=0;i<=31;i++){
				
				Coordinate c = new Coordinate();
				
				int week = cal.get(Calendar.DAY_OF_WEEK);
				Double r = weekratioMap.get(week);
				String time = sdf.format(cal.getTime());
				c.setX(Integer.parseInt(time));
				c.setY(Arith.mul(r, d.getY()));
				
				if(cal.get(Calendar.MONTH) == x-1){
					cList.add(c);
					cal.add(Calendar.DAY_OF_MONTH, 1);
				}else{
					break;
				}
				
			}
			
			
		}
		return cList;
		
	}
	
//	/**
//	 * 预测某个年 每天的流量趋势
//	 * @param x
//	 * @return
//	 */
//	public List<Coordinate> getFeatureDayForYear(){
//		
//		List<Coordinate> cList = new ArrayList<Coordinate>();
//		for(int i=1;i<=12;i++){
//			cList.addAll(getFeatureDayForMonth(i));
//		}
//		
//		return cList;
//		
//	}
	
	
	/**
	 * 
	 * @param month 第几个月
	 * @return 这个月 平均每个星期 7天的所占比例
	 */
	private Map<Integer, Double> computeWeekDayRatio(int month){
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, this.getYear());
		cal.add(Calendar.YEAR, -1);
		int pYear = cal.get(Calendar.YEAR);
		double all = 0;
		Map<Integer, Double> weekAllMap = new HashMap<Integer, Double>();
		Map<Integer, Integer> weekAllSize = new HashMap<Integer, Integer>();
		for(PvcountPo po:this.getOriginalDayList()){
			int m = po.getCollectTime().getMonth()+1;
			if(m == month&&pYear == po.getCollectTime().getYear()+1900){
				cal.setTime(po.getCollectTime());
				int w = cal.get(Calendar.DAY_OF_WEEK);
				
				Double d = weekAllMap.get(w);
				Integer s = weekAllSize.get(w);
				if(d == null){
					weekAllMap.put(w, po.getPvCount());
					weekAllSize.put(w, 1);
				}else{
					weekAllMap.put(w, Arith.add(d, po.getPvCount()));
					weekAllSize.put(w, s+1);
				}
				all=Arith.add(po.getPvCount(), all);
			}
		}
		
		Map<Integer, Double> weekRatioMap = new HashMap<Integer, Double>();
		
		for(Map.Entry<Integer, Double> entry:weekAllMap.entrySet()){
			Integer key = entry.getKey();
			Double value = entry.getValue();
			int i = weekAllSize.get(key);
			double r = Arith.div(Arith.div(value, all), i);
			weekRatioMap.put(key, r);
		}
		
		return weekRatioMap;
		
	}
	
	

	private List<Coordinate> concatDayToMonth(List<PvcountPo> newDataList) {
		int minYear = 999999;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		Map<String,PvcountPo> map = new HashMap<String, PvcountPo>();
		for(PvcountPo po:newDataList){			
			int year = po.getCollectTime().getYear();
			if(year <=minYear ){
				minYear = year;
			}
			
			String key = sdf.format(po.getCollectTime());
			PvcountPo pv = map.get(key);
			if(pv == null){
				pv = new PvcountPo();
				map.put(key, pv);
				try{
					pv.setCollectTime(sdf.parse(key));
				}catch (Exception e) {
				}
				pv.setPvCount(po.getPvCount());
			}else{
				pv.setPvCount(Arith.add(pv.getPvCount(), po.getPvCount()));				
			}
		}
		
		List<PvcountPo> monthPvcountPoList = new ArrayList<PvcountPo>();
		monthPvcountPoList.addAll(map.values());
		
		Collections.sort(monthPvcountPoList);
		
		List<Coordinate> list = new ArrayList<Coordinate>();
		for(PvcountPo po:monthPvcountPoList){
			int month = po.getCollectTime().getMonth()+1;
			int year = po.getCollectTime().getYear();
			Coordinate d = new Coordinate();
			d.setX((year-minYear)*12+month);
			d.setY(po.getPvCount()) ;
			list.add(d);
		}
		
		return list;
		
	}

	
	/**
	 * 预测返回12月个流量值
	 * @return 返回值为12月的值  Coordinate x 对应为yyyyMM
	 */
	@Override
	public List<Coordinate> getFurureMonth() {
		
		List<Coordinate> list= model.getFutureYear();
		
		List<Coordinate> cList = new ArrayList<Coordinate>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, this.getYear());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
		
		for(int i=0;i<12;i++){
			cal.set(Calendar.MONTH, i);
			Coordinate c = new Coordinate();
			Coordinate d = list.get(i);
			c.setX(Integer.parseInt(sdf.format(cal.getTime())));
			c.setY(d.getY());
			cList.add(c);
		}
		
		return cList;
	}
	/**
	 * 获取明年预测的365天的数据
	 * @return 返回值为365天的值  Coordinate x 对应为yyyyMMdd
	 */
	@Override
	public List<Coordinate> getFutureDay() {
		List<Coordinate> cList = new ArrayList<Coordinate>();
		for(int i=1;i<=12;i++){
			cList.addAll(getFeatureDayForMonth(i));
		}
		
		return cList;
	}



	
	
	
	
	
	

}
