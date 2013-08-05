
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.search;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.monitor.common.util.BufferedReader2;

/**
 * @author xiaodu
 *
 * ÏÂÎç4:04:03
 */
public class SearchConsumerLogJob extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(SearchConsumerLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	public SearchConsumerLogJob(String appName,String ip,String feature){
		super(appName,ip, feature);
	} 
	public void analyseOneLine(String line){
		analyseOneLine(line, '\01');
	}
	
	public void analyseOneLine(String line,char split) {
		
		 if (line.startsWith("SearchEngine")) {
			 try{
			 		String[] logResult = StringUtils.splitPreserveAllTokens(line, split);
			 
			 		String logkey1 = logResult[1];
			 		String logkey2 = logResult[2];
			 		String rt = logResult[3];
			 		String pv = logResult[4];
			 		String time = logResult[5];
			 		analyseConsumer(rTimeFormat.parse(time).getTime(),logkey1,logkey2,Integer.parseInt(pv),Integer.parseInt(rt));
				}catch (Exception e) {
					logger.error("ËÑË÷Ê§°Ü", e);
				}
		 }
		
	}
	
	
	public void submit(){
		
		for(Map.Entry<Long,Map<String,Map<String,int[]>>> entry:timeConsumerMap.entrySet()){
			long time = entry.getKey();
			Map<String,Map<String,int[]>> classMap = entry.getValue();
			for(Map.Entry<String,Map<String,int[]>> clazz:classMap.entrySet()){
				String className = clazz.getKey();
				Map<String,int[]> methodMap = clazz.getValue();
				for(Map.Entry<String,int[]> method:methodMap.entrySet()){
					String methodName = method.getKey();
					int[] values = method.getValue();
					Object[] objs = new Object[]{values[0],values[1]/values[0]};
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.SEARCH_CONSUMER,className+"_"+methodName}, new KeyScope[]{KeyScope.APP,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
					} catch (Exception e) {
						logger.error("·¢ËÍÊ§°Ü", e);
					}
					
				}
			}
		}
		
	}
	
	
	Map<Long,Map<String,Map<String,int[]>>> timeConsumerMap = new HashMap<Long, Map<String,Map<String,int[]>>>();
	
	/**
	 * 
	 * @param time
	 * @param className
	 * @param methodName
	 * @param executes
	 * @param rt
	 */
	private void analyseConsumer(long time,String className,String methodName,int executes,int rt ){
		Map<String,Map<String,int[]>> classMap = timeConsumerMap.get(time);
		if(classMap == null){
			classMap = new HashMap<String, Map<String,int[]>>();
			timeConsumerMap.put(time, classMap);
		}
		
		Map<String,int[]> methodMap = classMap.get(className);
		if(methodMap == null){
			methodMap = new HashMap<String, int[]>();
			classMap.put(className, methodMap);
		}
		
		int[] values = methodMap.get(methodName);
		if(values == null){
			values = new int[2];
			methodMap.put(methodName, values);
		}
		values[0]+=executes;
		values[1]+=rt;
		
	}
	
	@Override
	public void doAnalyse() {
		
	}

	@Override
	public void release() {
		timeConsumerMap.clear();
	}
	
	
	
	
	
	
	
	
	public static void main(String[] args){
		
		try {
			
			
			SearchConsumerLogJob job = new SearchConsumerLogJob("itemcenter","172.24.12.99","");
			
			
			BufferedWriter writer  = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D:\\work\\csp\\search.log_f"),"gbk"));
			
			BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\work\\csp\\search.log"), '\02');
			String line = null;
			while((line=reader.readLine())!=null){
				
				writer.write(line);
				
				job.analyseOneLine(line);
				writer.newLine();
				
			}
			
			job.submit();
			
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	
	
	

}