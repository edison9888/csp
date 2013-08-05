package com.taobao.csp.monitor.impl.analyse.mbean;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.csp.monitor.impl.analyse.TimeUtil;
import com.taobao.monitor.common.util.Arith;

/**
 * 
 * DS: 2010-04-08 14:35:17,168
 * DS:DBCDataSource[0,0,20,0,20,1]DB1DataSource_cm2[0
 * ,0,10,0,10,2]JmsXA[0,0,20,0,
 * 20,0]CRMDataSource[0,0,15,0,15,15]DefaultDS[1,0,20
 * ,5,20,5]DBHDataSource[0,0,5,
 * 0,5,2]DBArkDataSource_cm2[0,0,10,0,10,2]FORUMDatasource
 * [0,0,5,0,5,2]DBCDataSource_cm3
 * [0,0,10,0,10,2]ETDataSource[0,0,15,0,15,15]DBQADataSource
 * [0,0,10,0,10,2]DB1DataSource_cm3
 * [0,0,10,0,10,2]DB2DataSource[0,0,5,0,5,2]DB2DataSource_cm3
 * [0,0,10,0,10,2]DBArkDataSource
 * [0,0,10,0,10,2]DBCDataSource_cm2[0,0,10,0,10,2]CTUDatasource
 * [0,0,20,0,20,1]DBFBDataSource
 * [0,0,10,0,10,2]DBArkDataSource_cm3[0,0,10,0,10,2]
 * DB1DataSource[0,0,5,0,5,2]DB2DataSource_cm2[0,0,10,0,10,2] Thread-dump:
 * 2010-04-08 14:35:17,168
 * Thread-dump:ajp[0,0,1,0,0,0]http[0,0,1,0,0,0]ajp-thread-pool[40,0] Memory:
 * 2010-04-08 14:35:17,168 Memory:[PS Scavenge: Count=54 GCTime=2.2050sec][PS
 * MarkSweep: Count=7 GCTime=4.7110sec][Code Cache: Used=2711K
 * Committed=2752K][PS Eden Space: Used=19461K Committed=38912K][PS Survivor
 * Space: Used=80K Committed=38784K][PS Old Gen: Used=195472K
 * Committed=293952K][PS Perm Gen: Used=40323K
 * Committed=65536K][HeapMemoryUsage: Used=215014K
 * Committed=371648K][NonHeapMemoryUsage: Used=43034K Committed=68288K]
 * 
 * 连接池监控 数据源名称[MaxConnectionsInUseCount,InUseConnectionCount,
 * AvailableConnectionCount,ConnectionCount,MaxSize,MinSize]
 * 
 * 2010-04-08 12:59:52,710
 * DS:DBCDataSource[0,0,20,0,20,1]DB1DataSource_cm2[0,0,10
 * ,0,10,2]JmsXA[0,0,20,0,
 * 20,0]CRMDataSource[0,0,15,0,15,15]DefaultDS[1,0,20,5,20
 * ,5]DBHDataSource[0,0,5,
 * 0,5,2]DBArkDataSource_cm2[0,0,10,0,10,2]FORUMDatasource
 * [0,0,5,0,5,2]DBCDataSource_cm3
 * [0,0,10,0,10,2]ETDataSource[0,0,15,0,15,15]DBQADataSource
 * [0,0,10,0,10,2]DB1DataSource_cm3
 * [0,0,10,0,10,2]DB2DataSource[0,0,5,0,5,2]DB2DataSource_cm3
 * [0,0,10,0,10,2]DBArkDataSource
 * [0,0,10,0,10,2]DBCDataSource_cm2[0,0,10,0,10,2]CTUDatasource
 * [0,0,20,0,20,1]DBFBDataSource
 * [0,0,10,0,10,2]DBArkDataSource_cm3[0,0,10,0,10,2]
 * DB1DataSource[0,0,5,0,5,2]DB2DataSource_cm2[0,0,10,0,10,2]
 * 
 * 线程监控 Thread-dump:线程名[NEW,BLOCKED,RUNNABLE,WAITING,TERMINATED,TIMED_WAITING]
 * 线程池名-thread-pool[maxThreads,currentThreadCount] 线程有以下几种状态：NEW（已创建但尚未开始执行）、
 * RUNNABLE（正在Java虚拟机中执行）、 BLOCKED（阻塞中：正等待获得监控锁）、
 * WAITING（等待：无条件等候其他线程执行完毕，直到接收到唤醒通知）、
 * TIMED_WAITING（限时等待：等候其他线程执行完毕，直到接收到唤醒通知或等待时间超过了时限）、 TERMINATED（终止：线程已经退出）。
 * 2010-04-08 12:59:52,710
 * Thread-dump:ajp[0,0,1,0,0,0]http[0,0,1,0,0,0]ajp-thread-pool[40,0]
 * 
 * 内存监控 2010-04-08 12:59:52,710 Memory:[PS Scavenge: Count=50
 * GCTime=2.1510sec][PS MarkSweep: Count=6 GCTime=3.7130sec][Code Cache:
 * Used=2565K Committed=2592K][PS Eden Space: Used=21896K Committed=38912K][PS
 * Survivor Space: Used=11419K Committed=38784K][PS Old Gen: Used=222966K
 * Committed=293952K][PS Perm Gen: Used=40241K
 * Committed=65536K][HeapMemoryUsage: Used=256282K
 * Committed=371648K][NonHeapMemoryUsage: Used=42807K Committed=68128K]
 * 
 * 
 * 
 * 
 * @author xiaodu
 * @version 2010-4-2 下午05:14:57
 */
public class MbeanLogAnalyse extends AbstractDataAnalyse {


	/**
	 * @param appName
	 * @param ip
	 */
	public MbeanLogAnalyse(String appName, String ip,String f) {
		super(appName, ip, f);
	}


	private static final Logger logger = Logger.getLogger(MbeanLogAnalyse.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	
	Map<Long,Map<String, DataSourceInfo>> dataSourceMap = new HashMap<Long, Map<String,DataSourceInfo>>();
	Map<Long,Map<String, ThreadInfo>> threadMap = new HashMap<Long, Map<String,ThreadInfo>>();
	Map<Long,Map<String, ThreadPoolInfo>> threadPoolMap = new HashMap<Long, Map<String,ThreadPoolInfo>>();
	
	Map<Long, Map<String, MemoryInfo>> memoryCacheValue = new HashMap<Long, Map<String, MemoryInfo>>();
	


	private Map<String, Long> parse(String logLine) throws ParseException {

		String time = parseLogLineCollectTime(logLine);

		if (time != null) {
			long t = TimeUtil.converMinuteTime(sdf.parse(time));
			try {
				if (logLine.indexOf("DS:") > 0) {
					int index = logLine.indexOf("DS:");
					if (index > -1) {
						logLine = logLine.substring(index + 3, logLine.length());
					}
					parseDs(logLine, t);
				} else if (logLine.indexOf("Thread-dump:") > 0) {
					int index = logLine.indexOf("Thread-dump:");
					if (index > -1) {
						logLine = logLine.substring(index + 12, logLine.length());
					}
					parseThread(logLine, t);
				} else if (logLine.indexOf("Memory:") > 0) {
					parseMemory(logLine, t);
				}
			} catch (Exception e) {
				logger.error(logLine, e);
			}
		}
		return null;
	}

	private void parseMemory(String line,long collectTime ) {
		Pattern pattern = Pattern
				.compile("\\[HeapMemoryUsage:\\s+Used=(\\d+)K\\s+Committed=(\\d+)K\\]\\[NonHeapMemoryUsage:\\s+Used=(\\d+)K\\s+Committed=(\\d+)K\\]");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String heap = matcher.group(1);
			String heapAll = matcher.group(2);
			String noHeap = matcher.group(3);
			String noHeapAll = matcher.group(4);

			int heapInt = Integer.parseInt(heap);
			int heapAllInt = Integer.parseInt(heapAll);
			int noheapInt = Integer.parseInt(noHeap);
			int noheapAllInt = Integer.parseInt(noHeapAll);

			double jvm_memeory = Arith.div(heapInt + noheapInt, heapAllInt + noheapAllInt, 2);

			Map<String, MemoryInfo> valueMap = memoryCacheValue.get(collectTime);

			if (valueMap == null) {
				valueMap = new HashMap<String, MemoryInfo>();
				memoryCacheValue.put(collectTime, valueMap);
			}

			MemoryInfo inner = valueMap.get("JVM_Memory-JvmUseRate");
			if (inner == null) {
				inner = new MemoryInfo();
				valueMap.put("JVM_Memory-JvmUseRate", inner);
			}
			inner.count += jvm_memeory;
			inner.size++;

		}

	}

	/**
	 * * Thread-dump: 2010-04-08 14:35:17,168
	 * Thread-dump:ajp[0,0,1,0,0,0]http[0,0,1,0,0,0]ajp-thread-pool[40,0]
	 * Thread-dump:线程名[NEW,BLOCKED,RUNNABLE,WAITING,TERMINATED,TIMED_WAITING]
	 * 线程池名-thread-pool[maxThreads,currentThreadCount]
	 * 
	 * @param logLine
	 */
	private void parseThread(String logLine, long collectTime) {
		Pattern pattern = Pattern.compile("([^\\[]+)\\[([^\\]]+)\\]");
		Matcher matcher = pattern.matcher(logLine);
		while (matcher.find()) {
			String name = matcher.group(1);
			String value = matcher.group(2);
			String[] _values = value.split(",");
			if (name != null) {
				name = name.replaceAll("_", "-");
			}
			if (_values.length == 6) {
				Map<String, ThreadInfo> valueMap = threadMap.get(collectTime);
				if (valueMap == null) {
					valueMap = new HashMap<String, ThreadInfo>();
					threadMap.put(collectTime, valueMap);
				}
				
				ThreadInfo info = valueMap.get(name);
				if(info == null){
					info = new ThreadInfo();
					valueMap.put(name, info);
				}
				
				info.NEW+=Long.parseLong(_values[0]);
				info.BLOCKED+=Long.parseLong(_values[1]);
				info.RUNNABLE+=Long.parseLong(_values[2]);
				info.WAITING+=Long.parseLong(_values[3]);
				info.TERMINATED+=Long.parseLong(_values[4]);
				info.TIMEDWAITING+=Long.parseLong(_values[5]);
				info.size++;
			} else if (_values.length == 2) {

				Map<String, ThreadPoolInfo> valueMap = threadPoolMap.get(collectTime);
				if (valueMap == null) {
					valueMap = new HashMap<String, ThreadPoolInfo>();
					threadPoolMap.put(collectTime, valueMap);
				}
				
				ThreadPoolInfo info = valueMap.get(name);
				if(info == null){
					info = new ThreadPoolInfo();
					valueMap.put(name, info);
				}
				
				info.maxThreads  += Long.parseLong(_values[0]);
				info.currentThreadCount += Long.parseLong(_values[1]);
				info.size++;


			} else {
				logger.error("mbean 数据:" + logLine + " 有问题，需检查!");
			}
		}
	}

	/**
	 * //MaxConnectionsInUseCount,InUseConnectionCount,AvailableConnectionCount,
	 * ConnectionCount,MaxSize,MinSize * DBCDataSource[0,0,20,0,20,1]
	 * 
	 * @param logLine
	 */
	private void parseDs(String logLine, long collectTime) {
		Pattern pattern = Pattern.compile("([^\\[]+)\\[([^\\]]+)\\]");
		Matcher matcher = pattern.matcher(logLine);
		while (matcher.find()) {
			String name = matcher.group(1);
			String value = matcher.group(2);
			String[] _values = value.split(",");
			if (name != null) {
				name = name.replaceAll("_", "-");
			}
			if (_values.length == 6) {
				Map<String, DataSourceInfo> valueMap = dataSourceMap.get(collectTime);
				if (valueMap == null) {
					valueMap = new HashMap<String, DataSourceInfo>();
					dataSourceMap.put(collectTime, valueMap);
				}	
				
				DataSourceInfo info = valueMap.get(name);
				if(info == null){
					info = new DataSourceInfo();
					valueMap.put(name,info);
				}
				
				info.maxConnectionsInUseCount +=  Long.parseLong(_values[0]);
				info.inUseConnectionCount += Long.parseLong(_values[1]);
				info.availableConnectionCount +=Long.parseLong(_values[2]);
				info.connectionCount +=Long.parseLong(_values[3]);
				info.maxSize += Long.parseLong(_values[4]);
				info.minSize += Long.parseLong(_values[5]);
				info.size++;

			} else {
				logger.error("mbean 数据:" + logLine + " 有问题，需检查!");
			}
		}
	}


	protected String parseLogLineCollectTime(String logRecord) {

		if (logRecord.length() < 16) {
			return null;
		}

		String time = logRecord.substring(0, 16);
		return time;
	}

	/**
	 * 
	 * @param logRecord
	 * @return yyyy-MM-dd
	 */
	protected String parseLogLineCollectDate(String logRecord) {
		try {
			if (logRecord.length() < 10) {
				return null;
			}
			String time = logRecord.substring(0, 10);
			return time;
		} catch (Exception e) {
			return null;
		}
	}
	
	
	private void saveDataSource(){
		Iterator<Map.Entry<Long, Map<String,DataSourceInfo>>> dataSourceIt =  dataSourceMap.entrySet().iterator();
		while (dataSourceIt.hasNext()) {
			Map.Entry<Long, Map<String, DataSourceInfo>> entry = dataSourceIt.next();
			long collectTime = entry.getKey();
			Map<String, DataSourceInfo> keyMap = entry.getValue();
			for (Map.Entry<String, DataSourceInfo> countEntry : keyMap.entrySet()) {
				String key = countEntry.getKey();
				DataSourceInfo info = countEntry.getValue();
				if(info.size>0){					
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), collectTime, new String[]{KeyConstants.MBEAN,KeyConstants.DATASOURCE,key}, 
								new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.HOST},new String[]{"InUse","Available"},
								new Object[]{info.inUseConnectionCount/ info.size,info.availableConnectionCount/ info.size},new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE});
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
					
					
				}
			}
		}			
		
	}
	
	
	private void saveThread(){
		Iterator<Map.Entry<Long, Map<String,ThreadInfo>>> threadIt =  threadMap.entrySet().iterator();
		while (threadIt.hasNext()) {
			Map.Entry<Long, Map<String, ThreadInfo>> entry = threadIt.next();
			long collectTime = entry.getKey();
			Map<String, ThreadInfo> keyMap = entry.getValue();
			for (Map.Entry<String, ThreadInfo> countEntry : keyMap.entrySet()) {
				String key = countEntry.getKey();
				ThreadInfo info = countEntry.getValue();
				
				if(info.size>0){					
					
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), collectTime, new String[]{KeyConstants.MBEAN,KeyConstants.THREAD,key}, 
								new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.HOST},new String[]{"BLOCKED","WAITING","TERMINATED","TIMEDWAITING","RUNNABLE"},
								new Object[]{info.BLOCKED/ info.size,info.WAITING/ info.size,info.TERMINATED/ info.size,info.TIMEDWAITING/ info.size, info.RUNNABLE/ info.size},
								new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE});
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
					
				}
			}
		}			
		threadMap.clear();
	}
	
	
	
	private void saveThreadPool(){
		Iterator<Map.Entry<Long, Map<String,ThreadPoolInfo>>> threadPoolIt =  threadPoolMap.entrySet().iterator();
		while (threadPoolIt.hasNext()) {
			Map.Entry<Long, Map<String, ThreadPoolInfo>> entry = threadPoolIt.next();
			long collectTime = entry.getKey();
			Map<String, ThreadPoolInfo> keyMap = entry.getValue();
			for (Map.Entry<String, ThreadPoolInfo> countEntry : keyMap.entrySet()) {
				String key = countEntry.getKey();
				ThreadPoolInfo info = countEntry.getValue();

				if(info.size>0){					
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), collectTime, new String[]{KeyConstants.MBEAN,KeyConstants.THREADPOOL,key}, 
								new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.HOST},new String[]{"max","current"},
								new Object[]{info.maxThreads/ info.size,info.currentThreadCount/ info.size},
								new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE});
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
				
				}
			}
		}			
		threadPoolMap.clear();
	}
	
	
	private void saveMemory(){
		Iterator<Map.Entry<Long, Map<String, MemoryInfo>>> it1 = memoryCacheValue.entrySet().iterator();
		while (it1.hasNext()) {
			Map.Entry<Long, Map<String, MemoryInfo>> entry = it1.next();
			long collectTime = entry.getKey();
			Map<String, MemoryInfo> keyMap = entry.getValue();

			for (Map.Entry<String, MemoryInfo> countEntry : keyMap.entrySet()) {
				MemoryInfo count = countEntry.getValue();
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), collectTime, new String[]{KeyConstants.MBEAN,KeyConstants.MEMORY}, 
							new KeyScope[]{KeyScope.NO,KeyScope.HOST},new String[]{"mem"},
							new Object[]{Arith.mul(Arith.div(count.count, count.size,4), 100)},
							new ValueOperate[]{ValueOperate.REPLACE});
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
				
			}
		}
	}
	
	
	
	
	private class DataSourceInfo{
		
		private int maxConnectionsInUseCount;
		private int inUseConnectionCount;
		private int availableConnectionCount;
		private int connectionCount;
		private int maxSize;
		private int minSize;		
		private int size;
		
	}
	

	private class ThreadInfo{
		private int NEW;
		private int BLOCKED;
		private int RUNNABLE;
		private int WAITING;
		private int TERMINATED;
		private int TIMEDWAITING;
		private int size;
		
	}

	private class ThreadPoolInfo{
		private int maxThreads;
		private int currentThreadCount;
		private int size;
	}

	private class MemoryInfo {
		private double count;
		private long size;

	}

	
	public void analyseOneLine(String logRecord) {
		try {
			parse(logRecord);
		} catch (ParseException e) {
			logger.error("analyse line "+logRecord,e);
		}
		
	}

	
	public void doAnalyse() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void submit() {
		saveDataSource();
		saveThread();
		saveThreadPool();
		saveMemory();
	}

	@Override
	public void release() {
		dataSourceMap.clear();		
		threadMap.clear();	
		threadPoolMap.clear();
		memoryCacheValue.clear();
	}
	
	
public static void main(String[] args){
		
	MbeanLogAnalyse job = new MbeanLogAnalyse("detail","172.17.134.4","");
		
			String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\work\\mbean.log")));
			int i = 0;
			while((line=reader.readLine())!=null){
			//	System.out.println(line);
				job.analyseOneLine(line);
				i++;
				if(i>10000){
					job.submit();
					job.release();
					Thread.sleep(60000);
					i=0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



}