
package com.taobao.monitor.stat.newanalyse.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.stat.newanalyse.AbstractAnalyse;
import com.taobao.monitor.stat.newanalyse.FetchData;
import com.taobao.monitor.stat.newanalyse.FetchData.ReaderRecord;
import com.taobao.monitor.stat.util.Config;

/**
 * 
 * @author xiaodu
 * @version 2010-5-10 上午10:36:39
 */
public class AnalyseFile extends AbstractAnalyse{
	
	private static final Logger logger =  Logger.getLogger(AnalyseFile.class);
	
	private String appName;
	private String logFileName;
	private AnalyseFileLog analysefileLog;
	private int logFileNumber;
	
	private String logFileDir;
	
	
	private Calendar parseLogCalendar; //分析日志对应时间
	private SimpleDateFormat parseLogFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");	//时间段采用的格式
	private SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");	//时间段采用的格式	
	
	
	Map<String,Map<String,InnerRecord> > recordMap = new HashMap<String,Map<String,InnerRecord> >();
	
	
	
	public AnalyseFile(AnalyseFileLog analysefileLog){
		this.appName = analysefileLog.getAppName();
		this.logFileName = analysefileLog.getLogName();
		this.analysefileLog = analysefileLog;
		parseLogCalendar = Calendar.getInstance();
		parseLogCalendar.add(Calendar.DATE, -1);
		parseLogCalendar.set(Calendar.HOUR_OF_DAY, 0);	
		parseLogCalendar.set(Calendar.MINUTE, 0);
		parseLogCalendar.set(Calendar.MILLISECOND, 0);
		
		this.logFileDir = Config.getValue("LOG_PATH")+"/"+this.getAppName();
		
	}
	
	
	/**
	 * 分析日志文件
	 * 通过采样时间间隔，将间隔时间内的记录 写入临时文件tmp_log ，
	 * 然后通过parseMinFile 分析统计采样数据
	 * 然后删除临时文件
	 * 
	 */
	private void analyse(String logFileDir){		
		String logPath = logFileDir;		
		
		File logFies = new File(logPath);
		
		final File[] _logs = logFies.listFiles(new FilenameFilter(){
			public boolean accept(File dir, String name) {				
				if(name.indexOf(AnalyseFile.this.getLogFileName())>-1){
					return true;
				}
				return false;
			}});
		this.setLogFileNumber(_logs.length);
		
		logger.info("开始 分析"+this.getAppName()+" "+this.getLogFileName()+" 数量:"+_logs.length);
		for(File log:_logs){			
			logger.info(log.getPath());
			FetchData fetchData = new MonitorLogFileReader(log);
			long currentTime = System.currentTimeMillis();
			fetchData.doFetchData(new ReaderRecord(){					
				public void doReaderRecord(String[] datas, Date collectTime) {
					if(collectTime!=null){
						String logDate = parseLogFormatDate.format(collectTime);
						if(AnalyseFile.this.getCollectDate().equals(logDate)){							
							parseLogLine(datas,collectTime);			
						}
					}
				}});			
			logger.info("结束分析"+log.getPath()+" 文件大小:"+log.length()+" 使用时间:"+(System.currentTimeMillis()-currentTime));
			try{			
				log.delete();
			}catch(Exception e){}
		}
		logger.info("结束分析完成"+this.getAppName()+" "+this.getLogFileName()+" 数量:"+_logs.length);
	
	}
	

	@Override
	public void analyseLogFile(ReportContentInterface content) {		
		analyse(this.getLogFileDir());
		intoDb(content);
	}
	
	
	private void intoDb(ReportContentInterface content){
		List<String> inDbMap = this.getAnalysefileLog().getIndbList(); //需要入库的统计
		//读取入库
		for(String useKey:inDbMap){//需要入口的统计
			//入库sum
			SumBo bo = this.getAnalysefileLog().getSumMap().get(useKey);
			if(bo!=null){
				Map<String,InnerRecord> innerMap = recordMap.get(useKey);			
				for(Map.Entry<String, InnerRecord> sumEntry:innerMap.entrySet()){
					String indbKey = sumEntry.getKey();
					InnerRecord innerRecord = sumEntry.getValue();
					if(innerRecord!=null){
						content.putReportDataByCount(this.getAppName(), indbKey,( innerRecord.allCount/innerRecord.offset)+"", this.getCollectDate());
						
						for(Map.Entry<String, Double> sumentry:innerRecord.countMap.entrySet()){
							content.putReportData(this.getAppName(), indbKey,(sumentry.getValue()/innerRecord.offset)+"", sumentry.getKey());	
						}
					}
				}
			}
			
			//入库 平均值 
			String averageName = this.getAnalysefileLog().getAverageMap().get(useKey);
			if(averageName!=null){				
				String[] _averages = averageName.split("/");				
				if(_averages.length==2){					
					String key1 = _averages[0];
					String key2 = _averages[1];
					Map<String,InnerRecord> innerMap1 = recordMap.get(key1);
					Map<String,InnerRecord> innerMap2 = recordMap.get(key2);
					
					for(Map.Entry<String, InnerRecord> averageEntry:innerMap1.entrySet()){
						String indbKey = averageEntry.getKey();
						InnerRecord record1 = averageEntry.getValue();
						InnerRecord record2 = innerMap2.get(averageEntry.getKey());
						
						if(record1!=null&&record2!=null){
							content.putReportDataByCount(this.getAppName(), indbKey, (record1.allCount/(double)record2.allCount)+"", this.getCollectDate());
							Map<String,Double> timeDateMap1 = record1.countMap;
							Map<String,Double> timeDateMap2 = record2.countMap;
							for(Map.Entry<String, Double> timeDataEntry:timeDateMap1.entrySet()){
								String time = timeDataEntry.getKey();
								Double data1 = timeDataEntry.getValue();
								Double data2 = timeDateMap2.get(time);
								if(data2!=null){
									data1 = data1/record1.offset;
									data2 = data2/record2.offset;
									content.putReportData(this.getAppName(), indbKey, (data1/data2)+"", time);
								}															
							}						
						}
					}					
				}
			}
			//入库 平均每台
			String aaverageMatchine = this.getAnalysefileLog().getAverageMachineMap().get(useKey);
			if(aaverageMatchine!=null){
				
				Map<String,InnerRecord> innerMap = recordMap.get(aaverageMatchine);
				if(innerMap!=null){
					
					for(Map.Entry<String,InnerRecord> aveMatchineEntry:innerMap.entrySet()){
						InnerRecord record = aveMatchineEntry.getValue();				
						if(this.getLogFileNumber()!=0){					
							Double allCount = record.allCount/record.offset;					
							content.putReportData(this.getAppName(), aveMatchineEntry.getKey(), (allCount/this.getLogFileNumber())+"", this.getCollectDate());
							Iterator<Map.Entry<String,Double>> it = record.countMap.entrySet().iterator();
							while(it.hasNext()){
								Map.Entry<String,Double> itEntry = it.next();
								String collectTime = itEntry.getKey();
								Double sumCount = itEntry.getValue()/record.offset;						
								content.putReportDataByCount(this.getAppName(), aveMatchineEntry.getKey(), (sumCount/this.getLogFileNumber())+"", collectTime);
								
							}
						}
					}
				}				
			}
		}
		
	}
	
	
	
	private void parseLogLine(String[] records,Date collectTime){
		
		String time = parseLogFormat.format(collectTime);
		
		
		List<String> filedList = this.getAnalysefileLog().getFieldList();
		Map<String,String> fieldTypeMap = this.getAnalysefileLog().getFieldMap();
		Map<String,Integer> fieldIndexMap = this.getAnalysefileLog().getFieldIndexMap();
		
		if(records.length !=filedList.size()){
			logger.debug(records+" 与配置的Filed 数量不相符");
			return ;
		}
				
		Map<String, SumBo> sumMap = this.getAnalysefileLog().getSumMap();
		
		for(Map.Entry<String, SumBo> entry:sumMap.entrySet()){
			String key = entry.getKey(); //sum 的名称
			SumBo sumBo = entry.getValue();//sum 对应的key 的名称
			
			String outKeyname = sumBo.getOutKeyName();
			String indexKeyName = sumBo.getIndexKeyName();
			String newOutKeyname = parseinDbName(outKeyname,this.getAnalysefileLog().getFieldIndexMap(),records);
			
			Integer index = fieldIndexMap.get(indexKeyName);
			if(index==null){
				logger.debug("sum name:"+key+" 对应的filed 在filedList中不存在");
				continue;
			}
			
			String data = records[index];
			
			String type = fieldTypeMap.get(indexKeyName);//取得类型	
			
			
			
			Map<String, InnerRecord> innerMap = recordMap.get(key);
			
			if(innerMap==null){
				innerMap = new HashMap<String, InnerRecord>();
				recordMap.put(key, innerMap);
			}
			
			InnerRecord inner = innerMap.get(newOutKeyname);
			
			if(inner==null){
				inner = new InnerRecord();
				innerMap.put(key, inner);				
				if("DOUBLE".toUpperCase().equals(type.toUpperCase())){
					inner.offset=1000000;
				}
				if("Float".toUpperCase().equals(type.toUpperCase())){				
					inner.offset=1000;				
				}
				if("Long".toUpperCase().equals(type.toUpperCase())){			
					inner.offset=1;				
				}
				if("Integer".toUpperCase().equals(type.toUpperCase())){
					inner.offset=1;
				}
			}			
			Double newData = Double.parseDouble(data)*inner.offset;
			inner.allCount+=newData;
			Double num = inner.countMap.get(time);
			if(num==null){
				inner.countMap.put(time, newData);
			}else{
				inner.countMap.put(time, num+newData);
			}
			inner.size++;
			
		}
		
		
				
		
	}
	
	
	
	private String parseinDbName(String path,Map<String,Integer> nameIndexMap,String[] data){
		 
		 Pattern pattern = Pattern.compile("\\$\\{([\\w-]+)\\}");
		 
		 Matcher m = pattern.matcher(path);
		 
		 while(m.find()){
			 String format = m.group(1);			 
			 Integer index = nameIndexMap.get(format);
			 if(index!=null)			 
			 path = path.replaceAll("\\$\\{"+format+"\\}", data[index]);			 
		 }
		 
		 return path;
	 }
	
	
	
	private Date parseLogLineCollectDate(String record){
		Pattern pattern = Pattern.compile(this.getAnalysefileLog().getCollectTimePattern());
		Matcher matcher = pattern.matcher(record);
		if(matcher.find()){
			String time =  matcher.group(1);
			SimpleDateFormat parseLogFormat = new SimpleDateFormat(this.getAnalysefileLog().getCollectTimeFormat());
			try {
				return parseLogFormat.parse(time);
			} catch (ParseException e) {
				logger.error("parse Time error,Pattern:["+this.getAnalysefileLog().getCollectTimePattern()+"],timeformat:"+this.getAnalysefileLog().getCollectTimeFormat()+",record:["+record+"]",e);
			}
		}
		
		return null;
		
	}
	
	
	
	/**
	 * 取得收集时间
	 * @param date
	 * @return yyyy-MM-dd
	 */
	protected String getCollectDate(){
		return parseLogFormatDate.format(this.parseLogCalendar.getTime());
	}


	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getLogFileName() {
		return logFileName;
	}
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}
	public AnalyseFileLog getAnalysefileLog() {
		return analysefileLog;
	}
	public void setAnalysefileLog(AnalyseFileLog analysefileLog) {
		this.analysefileLog = analysefileLog;
	}
	public int getLogFileNumber() {
		return logFileNumber;
	}
	public void setLogFileNumber(int logFileNumber) {
		this.logFileNumber = logFileNumber;
	}
	
	
	
	public String getLogFileDir() {
		return logFileDir;
	}


	public void setLogFileDir(String logFileDir) {
		this.logFileDir = logFileDir;
	}



	public class InnerRecord{
		
		private Double allCount;
		private Map<String,Double> countMap; //总计
		
		private long size; //个数
		
		private long offset=1;
	}
	
}
