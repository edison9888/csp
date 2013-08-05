
package com.taobao.monitor.web.rating;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.ao.MonitorRatingAo;
import com.taobao.monitor.web.ao.MonitorTimeAo;
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2010-8-18 下午05:18:00
 */
public class RatingManage {
	
//	private List<Integer[]> rushHour = new ArrayList<Integer[]>();	
//	static{
//		rushHour.add(new Integer[]{2030,2230});		
//	}
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
	
	private Date ratingDay = null;
	
	
	public RatingManage(){
		setCollectDay();
	}
	
	/**
	 *  获取全部需要评分的系统
	 * @return
	 */
	public List<RatingApp> findAllRatingApp(){
		return MonitorRatingAo.get().findAllRatingApp();
	}
	
	public void setCollectDay(){
		setCollectDay(null);
	}
	
	/**
	 * 设置评分时间
	 * @param dateTime yyyyMMdd
	 */
	public void setCollectDay(String dateTime){
		
		if(dateTime == null){
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DAY_OF_MONTH, -1);
			ratingDay = cal.getTime();	
		}else{
			try {
				ratingDay = sdf.parse(dateTime);
			} catch (ParseException e) {
				Calendar cal = Calendar.getInstance();
				cal.add(Calendar.DAY_OF_MONTH, -1);
				ratingDay = cal.getTime();					
			}
			
			
		}
		
		
	}
	
	
	/**
	 * 评分
	 */
	public void rating(){		
		List<RatingApp> list = findAllRatingApp();		
		for(RatingApp app:list){
			compute(app);
		}
	}
	
	
	
	private void compute(RatingApp app){
		int collectDay = Integer.parseInt(sdf.format(ratingDay));
		List<RatingIndicator> indicatorList = app.getIndicatorList();
		for(RatingIndicator indicator:indicatorList){
			double indicatorValue = computeRushHours(app.getAppName(),indicator);		
			RatingIndicatorValue value = new RatingIndicatorValue();				
			value.setAppId(app.getAppId());
			value.setIndicatorThresholdValue(indicator.getIndicatorThresholdValue());
			value.setIndicatorWeight(indicator.getIndicatorWeight());
			value.setIndicatorKey(indicator.getIndicatorKeyName());
			value.setCollectDay(collectDay);
			value.setIndicatorValue(indicatorValue);
			value.setKeyId(indicator.getKeyId());
			value.setKeyUnit(indicator.getKeyUnit());
			saveIndicatorValue(value);
		}
	}
	
	
	public void rating(int appId){		
		List<RatingApp> list = findAllRatingApp();
		for(RatingApp app:list){
			if(app.getAppId() == appId){
				compute(app);
				break;
			}
		}
		
	}
	
	
	
	/**
	 * 记录分数
	 * @param indicatorValue
	 */
	public void saveIndicatorValue(RatingIndicatorValue indicatorValue){
		MonitorRatingAo.get().addRatingIndicatorValue(indicatorValue);
	}
	
	
	/**
	 * 计算出高峰期的平均值
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public double computeRushHours(String appName,RatingIndicator ratingIndicator){
		
		//IndicatorEnum indicatorEnum = ratingIndicator.getIndicatorKey();
		
		int appId = AppCache.get().getKey(appName).getAppId();
	
		Map<String, KeyValuePo> timeMap = MonitorTimeAo.get().findKeyValueByDate(appId,ratingIndicator.getKeyId(),  ratingDay);//获取一天的数据		
		Map<Integer,List<KeyValuePo>> tmpMap = new HashMap<Integer, List<KeyValuePo>>();//将高峰期的时间段内的数据保存在这个map中
		
		for(Map.Entry<String, KeyValuePo> entry:timeMap.entrySet()){
			
			List<Integer[]> rushHour = new ArrayList<Integer[]>();
			KeyValuePo po = entry.getValue();
			int time = Integer.parseInt(timeFormat.format(po.getCollectTime()));
			
			int rushHourStart = ratingIndicator.getRushHour_start();
			int rushHourEnd = ratingIndicator.getRushHour_end();	
			
			//由于以前数据是有0的，固把以前的数据改为默认值
			if(rushHourStart == 0 && rushHourEnd == 0) {
				
				rushHourStart = 2030;
				rushHourEnd = 2230;
			}
			
			//如果start 大于 end 则认为出错，并且改为默认值
			if(rushHourStart >= rushHourEnd ) {
				
				rushHourStart = 2030;
				rushHourEnd = 2230;
			}
			
			//rushHour的时间在0000~2359之间
			if(rushHourStart < 0 || rushHourStart > 2359 || rushHourEnd < 0 || rushHourStart > 2359) {
				
				rushHourStart = 2030;
				rushHourEnd = 2230;
			}
				
			
			Integer[] timeArray = new Integer[2];
			timeArray[0] = rushHourStart;
			timeArray[1] = rushHourEnd;
			
			rushHour.add(timeArray);
			
			
			for(int i=0;i<rushHour.size();i++){
				Integer[] tmp = rushHour.get(i);				
				List<KeyValuePo> list = tmpMap.get(i);
				if(list == null){
					list = new ArrayList<KeyValuePo>();
					tmpMap.put(i, list);
				}				
				if(tmp.length==2){
					int start = tmp[0];
					int end = tmp[1];
					
					if(start<=time&&time<=end){
						list.add(po);
					}
				}
			}
		}
		
		double t = 0f;		
		
		//将每个时间段内的 数据做计算
		for(Map.Entry<Integer,List<KeyValuePo>> entry:tmpMap.entrySet()){
			double v = Conversion.conversion(entry.getValue(), ratingIndicator.getIndicatorKeyName());
			t = Arith.add(t, v);
		}
		
		if(tmpMap.size()>0){
			return Arith.div(t, tmpMap.size(),2);
		}
		
		
		return 0f;
	}

}
