
package com.taobao.monitor.stat.newanalyse.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.monitor.stat.analyse.MbeanLogAnalyse;
import com.taobao.monitor.stat.newanalyse.FetchData;
import com.taobao.monitor.stat.newanalyse.FetchData.ReaderRecord;

/**
 * 
 * @author xiaodu
 * @version 2010-5-10 下午04:54:19
 */
public class MbeanLogFileReader extends AbstractFileReader implements FetchData {
	
	private static final Logger logger =  Logger.getLogger(MbeanLogFileReader.class);
	
	public MbeanLogFileReader(File filePath){
		super(filePath);
	}
	
	
	public void doFetchData(ReaderRecord readerRecord){		
		try {
			AnalyseBufferedReader reader = new AnalyseBufferedReader(new FileReader(this.getFilePath()),this.getRecordSeparator());			
			String line = null;			
			while((line=reader.readLine())!=null){				
				String[] datas = StringUtils.splitPreserveAllTokens(line, this.getFieldSeparator());
				readerRecord.doReaderRecord(datas,doFetchTime(line));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	private Map<String, Long> parse(String logLine) {
		
		if(logLine.indexOf("DS:")>0){			
			int index = logLine.indexOf("DS:");
			if(index>-1){
				logLine = logLine.substring(index+3, logLine.length());
			}			
			parseDs(logLine);
		}else if(logLine.indexOf("Thread-dump:")>0){
			int index = logLine.indexOf("Thread-dump:");
			if(index>-1){
				logLine = logLine.substring(index+12, logLine.length());
			}			
			parseThread(logLine);			
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
	private List<String[]> parseThread(String logLine){
		
		List<String[]> list = new ArrayList<String[]>();
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
				{
				String[] result = new String[2];
				result[0]="Thread_"+name+"_NEW";
				result[1]=NEW;
				list.add(result);
				}
				{
				String[] result = new String[2];
				result[0]="Thread_"+name+"_BLOCKED";
				result[1]=BLOCKED;
				list.add(result);
				}
				{
				String[] result = new String[2];
				result[0]="Thread_"+name+"_RUNNABLE";
				result[1]=RUNNABLE;
				list.add(result);
				}
				{
				String[] result = new String[2];
				result[0]="Thread_"+name+"_WAITING";
				result[1]=WAITING;
				list.add(result);
				}
				{
				String[] result = new String[2];
				result[0]="Thread_"+name+"_TERMINATED";
				result[1]=TERMINATED;
				list.add(result);
				}
				{
				String[] result = new String[2];
				result[0]="Thread_"+name+"_TIMEDWAITING";
				result[1]=TIMED_WAITING;
				list.add(result);
				}
				
			}else if(_values.length==2){				
				String maxThreads = _values[0];
				String currentThreadCount = _values[1];
				
				{
					String[] result = new String[2];
					result[0]="ThreadPool_"+name+"_maxThreads";
					result[1]=maxThreads;
					list.add(result);
				}
				{
					String[] result = new String[2];
					result[0]="ThreadPool_"+name+"_currentThreadCount";
					result[1]=currentThreadCount;
					list.add(result);
				}
				
			}else{
				logger.error("mbean 数据:"+logLine+" 有问题，需检查!");
			}
		}
		
		return list;
	}
	
	/**
	 * //MaxConnectionsInUseCount,InUseConnectionCount,AvailableConnectionCount,ConnectionCount,MaxSize,MinSize	*
	 * DBCDataSource[0,0,20,0,20,1]	 
	 * @param logLine
	 */
	private List<String[]> parseDs(String logLine){
		List<String[]> list = new ArrayList<String[]>();
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
					
					{
						String[] result = new String[2];
						result[0]="DataSource_"+name+"_MaxConnectionsInUseCount";
						result[1]=MaxConnectionsInUseCount;
						list.add(result);
					}
					{
						String[] result = new String[2];
						result[0]="DataSource_"+name+"_InUseConnectionCount";
						result[1]=InUseConnectionCount;
						list.add(result);
					}
					{
						String[] result = new String[2];
						result[0]="DataSource_"+name+"_AvailableConnectionCount";
						result[1]=AvailableConnectionCount;
						list.add(result);
					}
					{
						String[] result = new String[2];
						result[0]="DataSource_"+name+"_ConnectionCount";
						result[1]=ConnectionCount;
						list.add(result);
					}
					{
						String[] result = new String[2];
						result[0]="DataSource_"+name+"_MaxSize";
						result[1]=MaxSize;
						list.add(result);
					}
					{
						String[] result = new String[2];
						result[0]="DataSource_"+name+"_MinSize";
						result[1]=MinSize;
						list.add(result);
					}					
				}catch(Exception e){
					logger.error("mbean 数据:"+logLine+" 有问题，需检查!",e);
				}
			}else{
				logger.error("mbean 数据:"+logLine+" 有问题，需检查!");
			}
		}
		
		return list;
	}
	
	
	
}
