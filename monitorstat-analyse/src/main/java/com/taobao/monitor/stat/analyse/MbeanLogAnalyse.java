
package com.taobao.monitor.stat.analyse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.common.util.Constants;


/**
 * 
 * DS:
 * 2010-04-08 14:35:17,168 DS:DBCDataSource[0,0,20,0,20,1]DB1DataSource_cm2[0,0,10,0,10,2]JmsXA[0,0,20,0,20,0]CRMDataSource[0,0,15,0,15,15]DefaultDS[1,0,20,5,20,5]DBHDataSource[0,0,5,0,5,2]DBArkDataSource_cm2[0,0,10,0,10,2]FORUMDatasource[0,0,5,0,5,2]DBCDataSource_cm3[0,0,10,0,10,2]ETDataSource[0,0,15,0,15,15]DBQADataSource[0,0,10,0,10,2]DB1DataSource_cm3[0,0,10,0,10,2]DB2DataSource[0,0,5,0,5,2]DB2DataSource_cm3[0,0,10,0,10,2]DBArkDataSource[0,0,10,0,10,2]DBCDataSource_cm2[0,0,10,0,10,2]CTUDatasource[0,0,20,0,20,1]DBFBDataSource[0,0,10,0,10,2]DBArkDataSource_cm3[0,0,10,0,10,2]DB1DataSource[0,0,5,0,5,2]DB2DataSource_cm2[0,0,10,0,10,2]
 * Thread-dump:
 * 2010-04-08 14:35:17,168 Thread-dump:ajp[0,0,1,0,0,0]http[0,0,1,0,0,0]ajp-thread-pool[40,0]
 * Memory:
 * 2010-04-08 14:35:17,168 Memory:[PS Scavenge: Count=54 GCTime=2.2050sec][PS MarkSweep: Count=7 GCTime=4.7110sec][Code Cache: Used=2711K Committed=2752K][PS Eden Space: Used=19461K Committed=38912K][PS Survivor Space: Used=80K Committed=38784K][PS Old Gen: Used=195472K Committed=293952K][PS Perm Gen: Used=40323K Committed=65536K][HeapMemoryUsage: Used=215014K Committed=371648K][NonHeapMemoryUsage: Used=43034K Committed=68288K]
 * 
 * 连接池监控
数据源名称[MaxConnectionsInUseCount,InUseConnectionCount,AvailableConnectionCount,ConnectionCount,MaxSize,MinSize]

2010-04-08 12:59:52,710 DS:DBCDataSource[0,0,20,0,20,1]DB1DataSource_cm2[0,0,10,0,10,2]JmsXA[0,0,20,0,20,0]CRMDataSource[0,0,15,0,15,15]DefaultDS[1,0,20,5,20,5]DBHDataSource[0,0,5,0,5,2]DBArkDataSource_cm2[0,0,10,0,10,2]FORUMDatasource[0,0,5,0,5,2]DBCDataSource_cm3[0,0,10,0,10,2]ETDataSource[0,0,15,0,15,15]DBQADataSource[0,0,10,0,10,2]DB1DataSource_cm3[0,0,10,0,10,2]DB2DataSource[0,0,5,0,5,2]DB2DataSource_cm3[0,0,10,0,10,2]DBArkDataSource[0,0,10,0,10,2]DBCDataSource_cm2[0,0,10,0,10,2]CTUDatasource[0,0,20,0,20,1]DBFBDataSource[0,0,10,0,10,2]DBArkDataSource_cm3[0,0,10,0,10,2]DB1DataSource[0,0,5,0,5,2]DB2DataSource_cm2[0,0,10,0,10,2]

线程监控
Thread-dump:线程名[NEW,BLOCKED,RUNNABLE,WAITING,TERMINATED,TIMED_WAITING]
线程池名-thread-pool[maxThreads,currentThreadCount]

2010-04-08 12:59:52,710 Thread-dump:ajp[0,0,1,0,0,0]http[0,0,1,0,0,0]ajp-thread-pool[40,0]

内存监控
2010-04-08 12:59:52,710 Memory:[PS Scavenge: Count=50 GCTime=2.1510sec][PS MarkSweep: Count=6 GCTime=3.7130sec][Code Cache: Used=2565K Committed=2592K][PS Eden Space: Used=21896K Committed=38912K][PS Survivor Space: Used=11419K Committed=38784K][PS Old Gen: Used=222966K Committed=293952K][PS Perm Gen: Used=40241K Committed=65536K][HeapMemoryUsage: Used=256282K Committed=371648K][NonHeapMemoryUsage: Used=42807K Committed=68128K]
 
 * 
 * 
 * 
 * @author xiaodu
 * @version 2010-4-2 下午05:14:57
 */
public class MbeanLogAnalyse extends AnalyseFile{
	
	private static final Logger logger =  Logger.getLogger(MbeanLogAnalyse.class);
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	Map<String,Map<String, Inner>> cacheValue = new HashMap<String, Map<String,Inner>>();
	
	public MbeanLogAnalyse(String appName)throws Exception{
		super(appName,"mbean");
	}

	private Map<String, Long> parse(String logLine) {
		
		String time = parseLogLineCollectTime(logLine);
		
		if(logLine.indexOf("DS:")>0){			
			int index = logLine.indexOf("DS:");
			if(index>-1){
				logLine = logLine.substring(index+3, logLine.length());
			}			
			parseDs(logLine,time);
		}else if(logLine.indexOf("Thread-dump:")>0){
			int index = logLine.indexOf("Thread-dump:");
			if(index>-1){
				logLine = logLine.substring(index+12, logLine.length());
			}			
			parseThread(logLine,time);			
		}else if(logLine.indexOf("Memory:")>0){			
			
			
		}		
		return null;
	}
	/**
	 *  * Thread-dump:
        * 2010-04-08 14:35:17,168 Thread-dump:ajp[0,0,1,0,0,0]http[0,0,1,0,0,0]ajp-thread-pool[40,0]
        * Thread-dump:线程名[NEW,BLOCKED,RUNNABLE,WAITING,TERMINATED,TIMED_WAITING]
			线程池名-thread-pool[maxThreads,currentThreadCount]
	 * @param logLine
	 */
	private void parseThread(String logLine,String collectTime){		
		Pattern pattern = Pattern.compile("([^\\[]+)\\[([^\\]]+)\\]");
		Matcher matcher = pattern.matcher(logLine);
		while(matcher.find()){
			String name = matcher.group(1);
			String value = matcher.group(2);			
			String[] _values =  value.split(",");
			if(name!=null){
				name= name.replaceAll("_", "-");
			}
			if(_values.length==6){
				String NEW = _values[0];
				String BLOCKED = _values[1];
				String RUNNABLE = _values[2];
				String WAITING = _values[3];
				String TERMINATED = _values[4];
				String TIMED_WAITING = _values[5];
				
				Map<String, Inner> valueMap = cacheValue.get(collectTime);
				if(valueMap==null){
					valueMap = new HashMap<String, Inner>();
					cacheValue.put(collectTime, valueMap);
				}
				
				String[] keys = new String[]{"SELF_Thread_"+name+"_NEW",
						"SELF_Thread_"+name+"_BLOCKED","SELF_Thread_"+name+"_RUNNABLE",
						"SELF_Thread_"+name+"_WAITING","SELF_Thread_"+name+"_TERMINATED",
						"SELF_Thread_"+name+"_TIMEDWAITING"};
				
				for(int i=0;i<keys.length;i++ ){
					Inner inner = valueMap.get(keys[i]);
					if(inner==null){
						inner = new Inner();
						valueMap.put(keys[i], inner);
					}
					inner.count +=Long.parseLong(_values[i]);
					inner.size++;
				}
							
				
			}else if(_values.length==2){				
				String maxThreads = _values[0];
				String currentThreadCount = _values[1];
				
				Map<String, Inner> valueMap = cacheValue.get(collectTime);
				if(valueMap==null){
					valueMap = new HashMap<String, Inner>();
					cacheValue.put(collectTime, valueMap);
				}
				
				
				String[] keys = new String[]{"SELF_ThreadPool_"+name+"_maxThreads","SELF_ThreadPool_"+name+"_currentThreadCount"};
				
				for(int i=0;i<keys.length;i++ ){
					Inner inner = valueMap.get(keys[i]);
					if(inner==null){
						inner = new Inner();	
						valueMap.put(keys[i], inner);
					}
					inner.count +=Long.parseLong(_values[i]);
					inner.size++;
				}				
				
			}else{
				logger.error("mbean 数据:"+logLine+" 有问题，需检查!");
			}
		}
	}
	
	/**
	 * //MaxConnectionsInUseCount,InUseConnectionCount,AvailableConnectionCount,ConnectionCount,MaxSize,MinSize	*
	 * DBCDataSource[0,0,20,0,20,1]	 
	 * @param logLine
	 */
	private void parseDs(String logLine,String collectTime){
		Pattern pattern = Pattern.compile("([^\\[]+)\\[([^\\]]+)\\]");
		Matcher matcher = pattern.matcher(logLine);
		while(matcher.find()){
			String name = matcher.group(1);
			String value = matcher.group(2);			
			String[] _values =  value.split(",");
			if(name!=null){
				name= name.replaceAll("_", "-");
			}
			if(_values.length==6){
				String MaxConnectionsInUseCount = _values[0];
				String InUseConnectionCount = _values[1];
				String AvailableConnectionCount = _values[2];
				String ConnectionCount = _values[3];
				String MaxSize = _values[4];
				String MinSize = _values[5];
				try{
					
					
					Map<String, Inner> valueMap = cacheValue.get(collectTime);
					if(valueMap==null){
						valueMap = new HashMap<String, Inner>();
						cacheValue.put(collectTime, valueMap);
					}
					
					
					String[] keys = new String[]{"SELF_DataSource_"+name+"_MaxConnectionsInUseCount"
							,"SELF_DataSource_"+name+"_InUseConnectionCount",
							"SELF_DataSource_"+name+"_AvailableConnectionCount",
							"SELF_DataSource_"+name+"_ConnectionCount",
							"SELF_DataSource_"+name+"_MaxSize",
							"SELF_DataSource_"+name+"_MinSize"};
					
					for(int i=0;i<keys.length;i++ ){
						Inner inner = valueMap.get(keys[i]);
						if(inner==null){
							inner = new Inner();	
							valueMap.put(keys[i], inner);
						}
						inner.count +=Long.parseLong(_values[i]);
						inner.size++;
					}
				}catch(Exception e){
					logger.error("mbean 数据:"+logLine+" 有问题，需检查!",e);
				}
			}else{
				logger.error("mbean 数据:"+logLine+" 有问题，需检查!");
			}
		}
	}


	@Override
	protected void parseLogLine(String logRecord) {
		parse(logRecord);
	}

	@Override
	protected String parseLogLineCollectTime(String logRecord) {
		String time = logRecord.substring(0, 16);		
		return time;		
	}

	@Override
	protected String parseLogLineCollectDate(String logRecord) {
		String time = logRecord.substring(0, 10);		
		return time;		
	}
		

		
	private class Inner{		
		private long count;		
		private long size;
	}



	@Override
	protected void insertToDb(ReportContentInterface content) {

		Iterator<Map.Entry<String,Map<String,Inner>>> it = cacheValue.entrySet().iterator();
		
		Map<String,Inner> allInnerMap = new HashMap<String, Inner>();
		
		while(it.hasNext()){			
			Map.Entry<String,Map<String,Inner>> entry = it.next();			
			String collectTime = entry.getKey();
			Map<String,Inner> keyMap = entry.getValue();
			
			for(Map.Entry<String, Inner> countEntry:keyMap.entrySet()){
				String key = countEntry.getKey();
				Inner count =  countEntry.getValue();
				Inner allinner = allInnerMap.get(key);
				if(allinner==null){
					allinner = new Inner();
					allInnerMap.put(key, allinner);
				}				
				allinner.count+=count.count;
				allinner.size+=count.size;				
				//content.putReportData(this.getAppName(), key+"_"+Constants.AVERAGE_USERTIMES_FLAG,count.count/count.size, collectTime);				
			}
		}
		
		
		for(Map.Entry<String, Inner> entry:allInnerMap.entrySet()){
			content.putReportDataByCount(this.getAppName(), entry.getKey()+"_"+Constants.AVERAGE_USERTIMES_FLAG,entry.getValue().count/entry.getValue().size, this.getCollectDate());	
		}
		
	}
	

}
