
package com.taobao.monitor.web.jprof;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Stack;

import com.taobao.jprof.Arith;
import com.taobao.jprof.sort.SortTimeJprofData;
import com.taobao.jprof.sort.SortTimeJprofDataByTimeMillis;
import com.taobao.jprof.sort.TimeSortData;
import com.taobao.monitor.web.ao.MonitorJprofAo;
import com.taobao.monitor.web.core.po.JprofClassMethod;
import com.taobao.monitor.web.core.po.JprofHost;

/**
 * 
 * @author xiaodu
 * @version 2010-8-13 ÏÂÎç02:36:09
 */
public class JprofSort {
	
	
	public void intoDb(String jprofilePath,JprofHost host,String collectDay){
		
		
		MonitorJprofAo.get().deleteClassMethodByDay(host.getAppName(), collectDay);
		
		
		boolean nano = true;
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(jprofilePath));
			String line = null;
			while((line=reader.readLine())!=null){
				if(line.startsWith("##nano:true")){//##nano:
					nano = true;
					break;
				}
				if(line.startsWith("##nano:false")){//##nano:
					nano = false;
					break;
				}
			}	
			
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}
		
		List<TimeSortData> list = null;
		
		if(nano){
			SortTimeJprofData jprofData = new SortTimeJprofData(jprofilePath,"");
			list = jprofData.getTimeSortData();			
		}else{
			SortTimeJprofDataByTimeMillis jprofData = new SortTimeJprofDataByTimeMillis(jprofilePath,"");
			list = jprofData.getTimeSortData();			
		}
		
		
		for(TimeSortData timeSortData:list){
			String name = timeSortData.name;
			Stack<Long> stack = timeSortData.valueStack;
			String[] t = name.split(":");
			
			String classname = t[0];
			String methodName = t[1];
			String lineNum = t[2];
			
			JprofClassMethod jprof = new JprofClassMethod();
			jprof.setAppName(host.getAppName());
			jprof.setClassName(classname);
			jprof.setMethodName(methodName);
			jprof.setLineNum(Integer.parseInt(lineNum));
			jprof.setExcuteNum(stack.size());
			
			if(nano){
				jprof.setUseTime(Arith.div(timeSortData.getValue(), 1000000,2));
			}else{
				jprof.setUseTime(timeSortData.getValue());
			}
			jprof.setCollectDay(collectDay);
			MonitorJprofAo.get().addClassMethod(jprof);
		}
		
	}
	
	
	
	
	
	public static void main(String[] args){
		
		JprofSort scp = new JprofSort();
		
		JprofHost host = new JprofHost();
		host.setAppName("list");
		
		
		scp.intoDb("D:\\tmp\\jprof\\jprof.txt", host,"");
		
	}

}
