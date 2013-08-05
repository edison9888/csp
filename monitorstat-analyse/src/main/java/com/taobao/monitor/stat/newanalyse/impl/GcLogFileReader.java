
package com.taobao.monitor.stat.newanalyse.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.monitor.stat.analyse.GCLogAnalyse;
import com.taobao.monitor.stat.newanalyse.FetchData;
import com.taobao.monitor.stat.newanalyse.FetchData.ReaderRecord;
import com.taobao.monitor.common.util.Constants;

/**
 * 
 * @author xiaodu
 * @version 2010-5-10 下午04:54:19
 */
public class GcLogFileReader extends AbstractFileReader implements FetchData {
	
	private static final Logger logger =  Logger.getLogger(GcLogFileReader.class);	
	
	
	public GcLogFileReader(File filePath){
		super(filePath);
	}
	
	
	public void doFetchData(ReaderRecord readerRecord){		
		try {
			AnalyseBufferedReader reader = new AnalyseBufferedReader(new FileReader(this.getFilePath()),this.getRecordSeparator());			
			String line = null;			
			while((line=reader.readLine())!=null){
				
				List<String> gcLog =  readerTimeSelectFileLine(line);
				
				for(String str:gcLog){
					String[] result = parseGcLog(str);
					if(result!=null){
						readerRecord.doReaderRecord(result,doFetchTime(line));
					}					
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 将读取到的gc 通过 [] 分割成不同的记录
	 * @param line
	 * @return
	 */
	private List<String> readerTimeSelectFileLine(String line){		
		List<String> gcResultList = new ArrayList<String>();
		
		int len = line.length();
		char first = '[';
		char lase = ']';
		Stack<Character> stack = new Stack<Character>();
		for(int i=0;i<len;i++){
			if(line.charAt(i)==lase){
				String result = "";
				char _result = ' ';
				while((_result=stack.pop())!=first){
					result=_result+result;;
				}
				gcResultList.add(result);
			}else{			
				stack.add(line.charAt(i));
			}
			
		}	
		
		return gcResultList;		
	}
	
	
	/**
	 * 
	 * ParNew: 157995K->2662K(176960K), 0.0081480 secs
	 * GC 6052.576:  185472K->30139K(1553216K), 0.0082680 secs
	 * 
	 * 读取gc 不同信息数据，并放入cacheMap 存储
	 * @param gcLog
	 */
	private  String[] parseGcLog(String gcLog){	
		
		try{
			Pattern pattern = Pattern.compile("(.*[^\\d])(\\d+)K->(\\d+)K\\((\\d+)K\\)\\s*,\\s*([\\d\\.]+)\\s*(secs)");		
			Matcher matcher = pattern.matcher(gcLog);
			if(matcher.find()){								
				String name = matcher.group(1);				
				String old_value = matcher.group(2);
				String new_value = matcher.group(3);
				String cap = matcher.group(4);
				String time = matcher.group(5);
								
				name = keyFilter(name);
				
				String[] values = new String[3];
				values[0] = name;
				values[1] = "1";
				values[2] = time;
//				cacheValue.put("SELF_GC_"+name+"_"+Constants.USE_TIME, newTime);//记录这条记录的执行时间
//				cacheValue.put("SELF_GC_"+name+"_"+Constants.USE_EXE, 1l);//记录这个记录的次数				
//				//cacheValue.put("SELF_GC_"+name+"_[C]_"+Constants.AVERAGE_USERTIMES_FLAG, Long.parseLong(new_value));//记录容量
				
				return values;
				
			}
		}catch(Exception e){
			logger.error("parseGcLog:"+gcLog, e);
		}	
		
		return null;
	}
	
	private String keyFilter(String key){		
		Pattern pattern = Pattern.compile("(\\w+)[^\\w]");
		Matcher m = pattern.matcher(key);
		if(m.find()){
			key = m.group(1);
		}		
		return key;
	}
	
	
}
