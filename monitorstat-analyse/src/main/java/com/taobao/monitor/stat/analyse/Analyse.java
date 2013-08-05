
package com.taobao.monitor.stat.analyse;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.taobao.monitor.stat.content.ReportContentInterface;

/**
 * 分析日志文件基类 ，主要用于收集统计数据，统计
 * @author xiaodu
 * @version 2010-4-8 上午10:35:43
 */
public abstract  class Analyse {
	
	private static final Logger logger =  Logger.getLogger(Analyse.class);
	
	//yyyy-MM-dd HH:mm
	
	private Calendar parseLogCalendar; //分析日志对应时间
	private SimpleDateFormat parseLogFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");	//时间段采用的格式
	private SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");	//时间段采用的格式	
	private int logFileNumber = 0;//记录有多少个统计log日志	
	private int currentDay ;  //现在时间
	private String appName;   //应用名称
	public Analyse(String appName){
		if(appName!=null){
			this.appName = appName;

		}else{
			this.appName ="NO_APP";
		}
		parseLogCalendar = Calendar.getInstance();
		currentDay = parseLogCalendar.get(Calendar.DAY_OF_MONTH);
		parseLogCalendar.add(Calendar.DATE, -1);
		parseLogCalendar.set(Calendar.HOUR_OF_DAY, 0);	
		parseLogCalendar.set(Calendar.MINUTE, 0);
		parseLogCalendar.set(Calendar.MILLISECOND, 0);
	}
	
	public void setCollectTime(Calendar cal){
		this.parseLogCalendar = cal;
	}
	
	
	
	public int getLogFileNumber() {
		return logFileNumber;
	}
	public void setLogFileNumber(int logFileNumber) {
		this.logFileNumber = logFileNumber;
	}
	public int getCurrentDay() {
		return currentDay;
	}
	
	public Calendar getParseLogCalendar() {
		return parseLogCalendar;
	}	
	public SimpleDateFormat getParseLogFormat() {
		return parseLogFormat;
	}
	public String getAppName() {
		return appName;
	}
	public SimpleDateFormat getParseLogFormatDate() {
		return parseLogFormatDate;
	}
	


	public abstract void analyseLogFile(ReportContentInterface content);
	
	
	/**
	 * 入库
	 * @param content
	 */
	protected abstract void insertToDb(ReportContentInterface content);
	
//	/**
//	 * 保存时间点 即每分钟的日志数据
//	 */
//	private Map<String,Map<String,Inner>> collectTimeLogMap = new HashMap<String, Map<String,Inner>>();//记录所有日志数据
//
////	/**
////	 * 将时间点做平均
//	 */
//	private void averageTimeValue(ReportContentInterface content){
//		Iterator<Map.Entry<String,Map<String,Inner>>> it = collectTimeLogMap.entrySet().iterator();
//		while(it.hasNext()){
//			Map.Entry<String,Map<String,Inner>> entry = it.next();
//			String collectTime = entry.getKey();
//			Map<String,Inner> keyMap = entry.getValue();
//			
//			
//			Set<String> keySet1 = new HashSet<String>();//带有统计标记
//			Set<String> keySet2 = new HashSet<String>();//没有标记
//			Set<String> _keySet =  keyMap.keySet();
//			
//			for(String keyName:_keySet){
//				if(keyName.indexOf("_"+Constants.USE_EXE)>0||keyName.indexOf("_"+Constants.USE_TIME)>0){
//					int index  = keyName.lastIndexOf("_");
//					if(index>-1){
//						String keyvalue = keyName.substring(0,index);
//						keySet1.add(keyvalue);
//					}else{
//						keySet1.add(keyName);
//					}
//				}else{
//					keySet2.add(keyName);
//				}
//			}	
//			for(String key:keySet1){
//				try{
//					String exe_key = key+"_"+Constants.USE_EXE;
//					String time_key = key+"_"+Constants.USE_TIME;
//					Inner exeL = keyMap.get(exe_key);//这个时间点执行的次数				
//					Inner timeL = keyMap.get(time_key);//这个时间点执行的时间
//					
//					long eCount = exeL.count;
//					long tCount = timeL.count;
//					
//					Double t = Double.parseDouble(tCount+"");
//					double ave = 0;
//					if(eCount>0){
//						ave = t/eCount;
//					}				
//					content.putReportData(this.appName, key+"_"+Constants.COUNT_TIMES_FLAG,eCount, collectTime);	//将时间点平均数据入库
//					content.putReportData(this.appName, key+"_"+Constants.AVERAGE_USERTIMES_FLAG,ave+"", collectTime);
//					
//					
//					double ave2 = 0;				
//					if(this.getLogFileNumber()>0){
//						ave2 = eCount/(double)this.getLogFileNumber();
//					}
//					content.putReportData(this.appName, key+"_"+Constants.AVERAGE_MACHINE_FLAG,ave2+"", collectTime);
//					
//					
//				}catch(Exception e){
//					logger.error(key,e);
//				}
//			}
//			
//			for(String key:keySet2){				
//				Inner inner = keyMap.get(key);				
//				long eCount = inner.count;
//								
//				double ave = 0;				
//				if(this.getLogFileNumber()>0){
//					ave = eCount/this.getLogFileNumber();
//				}
//				double ave2 = 0;	
//				if(inner.size>0){
//					ave2 = eCount/inner.size;
//				}
//				content.putReportData(this.appName, key+"_"+Constants.COUNT_TIMES_FLAG,eCount, collectTime);
//				content.putReportData(this.appName, key+"_"+Constants.AVERAGE_USERTIMES_FLAG,ave2+"", collectTime);
//				content.putReportData(this.appName, key+"_"+Constants.AVERAGE_MACHINE_FLAG,ave+"", collectTime);
//			}
//			
//			
//			
//		}
//	}
//	/**
//	 * 将所有值做平均和将需要统计的数据，做统计
//	 */
//	private void averageAndCountAll(ReportContentInterface content){		
//		Map<String,Long> keyAll = new HashMap<String, Long>();		
//		Iterator<Map.Entry<String,Map<String,Inner>>> it = collectTimeLogMap.entrySet().iterator();
//		
//		Map<String,Long> keyValueSize = new HashMap<String, Long>();
//		
//		while(it.hasNext()){
//			Map.Entry<String,Map<String,Inner>> entry = it.next();
//			String collectTime = entry.getKey();
//			Map<String,Inner> keyMap = entry.getValue();			
//			Iterator<Map.Entry<String,Inner>> vit = keyMap.entrySet().iterator();
//			while(vit.hasNext()){
//				Map.Entry<String,Inner> vEntry = vit.next();
//				String key = vEntry.getKey();
//				Inner vInner = vEntry.getValue();
//				//将key 的时间相加
//				long vcount = vInner.count;				
//				
//				Long vl = keyAll.get(key);
//				if(vl==null){
//					keyAll.put(key, vcount);
//				}else{
//					keyAll.put(key, vl+vcount);
//				}
//				//
//				Long size = keyValueSize.get(key);
//				if(size==null){
//					keyValueSize.put(key, vInner.size);
//				}else{
//					keyValueSize.put(key, size+vInner.size);
//				}
//				
//			}
//		}
//		
//		
//		Set<String> keySet1 = new HashSet<String>();	
//		Set<String> keySet2 = new HashSet<String>();		
//		Set<String> _keySet =  keyAll.keySet();
//		
//		for(String keyName:_keySet){
//			if(keyName.indexOf("_"+Constants.USE_EXE)>0||keyName.indexOf("_"+Constants.USE_TIME)>0){
//				int index  = keyName.lastIndexOf("_");
//				if(index>-1){
//					String keyvalue = keyName.substring(0,index);
//					keySet1.add(keyvalue);
//				}else{
//					keySet1.add(keyName);
//				}
//			}else{
//				keySet2.add(keyName);
//			}
//		}
//		
//		
//		for(String key:keySet1){	
//			try{
//			String exe_key = key+"_"+Constants.USE_EXE;
//			String time_key = key+"_"+Constants.USE_TIME;
//			
//			Long e = keyAll.get(exe_key);
//			Long t = keyAll.get(time_key);
//			
//			Double v = Double.parseDouble(t+"");
//			double ave = 0;
//			if(e>0){
//				ave = v/e;
//			}
//			
//			content.putReportDataByCount(this.getAppName(), key+"_"+Constants.COUNT_TIMES_FLAG, e, this.getCollectDate());	//将汇总值入库
//			content.putReportDataByCount(this.getAppName(), key+"_"+Constants.AVERAGE_USERTIMES_FLAG, ave+"", this.getCollectDate());//平均入库
//			
//			content.putReportDataByCount(this.getAppName(), key+"_"+Constants.AVERAGE_MACHINE_FLAG, e/this.getLogFileNumber(), this.getCollectDate());	//将汇总值入库
//			}catch(Exception e){
//				logger.error(key,e);
//			}
//		}
//		
//		
//		for(String key:keySet2){				
//			long eCount = keyAll.get(key);				
//				
//			double ave = 0;				
//			if(this.getLogFileNumber()>0){
//				ave = eCount/this.getLogFileNumber();
//			}				
//			content.putReportDataByCount(this.appName, key+"_"+Constants.COUNT_TIMES_FLAG,eCount, this.getCollectDate());
//			content.putReportDataByCount(this.appName, key+"_"+Constants.AVERAGE_MACHINE_FLAG,ave+"", this.getCollectDate());
//			
//			Long size = keyValueSize.get(key);
//			
//			double ave2 = 0;				
//			if(size!=null&&size>0){
//				ave2 = eCount/size;
//			}
//			content.putReportDataByCount(this.appName, key+"_"+Constants.AVERAGE_USERTIMES_FLAG,ave2+"", this.getCollectDate());
//			
//		}
//		
//	}
	
	
	
//	/**
//	 * 将所有值根据时间点 保存在map中	
//	 * @param mapValue
//	 * @param collect_time
//	 */
//	protected void putParseValue(Map<String,Long> mapValue,String collect_time){
//		
//		if(collect_time==null)return ;
//		
//		Map<String,Inner> logKeyValueMap =  collectTimeLogMap.get(collect_time);
//		
//		if(logKeyValueMap==null){
//			logKeyValueMap = new HashMap<String, Inner>();
//			collectTimeLogMap.put(collect_time, logKeyValueMap);
//		}
//		
//		Iterator<Map.Entry<String,Long>> it = mapValue.entrySet().iterator();		
//		while(it.hasNext()){			
//			Map.Entry<String,Long> entry =it.next();			
//			String key = entry.getKey();
//			Long value = entry.getValue();		
//			
//			Inner inner = logKeyValueMap.get(key);
//			if(inner==null){
//				inner = new Inner();
//				logKeyValueMap.put(key, inner);
//			}
//			inner.count+=value;
//			inner.size++;
//		}		
//	}
	
//	/**
//	 * 记录峰值和汇总值
//	 * @param reslutMap
//	 */
//	protected void recordPeakAndCountValue(String collectTime,String key,double value){
//		
//	}
	
	
	/**
	 * 取得收集时间 
	 * @param date
	 * @return yyyy-MM-dd HH:mm
	 */
	protected String getCollectTime(){
		return parseLogFormat.format(this.parseLogCalendar.getTime());
	}
	
	/**
	 * 取得收集时间
	 * @param date
	 * @return yyyy-MM-dd
	 */
	protected String getCollectDate(){
		return parseLogFormatDate.format(this.parseLogCalendar.getTime());
	}


	
	
//	public long getCollectMapSize(){
//		long count = 0;
//		Iterator<Map.Entry<String,Map<String,Inner>>> it = collectTimeLogMap.entrySet().iterator();
//		while(it.hasNext()){
//			Map<String,Inner> mm = it.next().getValue();
//			
//			count+=mm.size();
//			
//		}
//		return count;
//	}
//	
//	
//	public class Inner{
//		private long count;
//		private long size;
//	}
	

}
