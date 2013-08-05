
/**
 * monitorstat-monitor
 */
package com.taobao.csp.dataserver.example;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.BufferedReader2;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 *
 * ÏÂÎç4:04:03
 */
public class SearchConsumerLogJob {
	
	private static final Logger logger = Logger.getLogger(SearchConsumerLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	private String appname = null;
	private String ip = null;
	public SearchConsumerLogJob(String appName,String ip){
		this.appname = appName;
		this.ip = ip;
	} 
	
	public void analyseLine(String line) throws ParseException{
		
		 if (line.startsWith("SearchEngine")) {
			 try{
			 		String[] logResult = StringUtils.splitPreserveAllTokens(line, '\01');
			 
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
						CollectDataUtilMulti.collect(appname, ip, time, new String[]{KeyConstants.SEARCH_CONSUMER,className+"_"+methodName}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time"}, objs,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE});
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
	
	
	
	
	
	
	
	
	public static void main(String[] args){
		
		try {
			
			Map<String,HostPo> map = CspCacheTBHostInfos.get().getHostInfoMapByOpsName("itemcenter");
			for(Map.Entry<String, HostPo> entry:map.entrySet()){
				SearchConsumerLogJob job = new SearchConsumerLogJob("itemcenter",entry.getKey());
				
				BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\work\\csp\\search.log"), '\02');
				String line = null;
				while((line=reader.readLine())!=null){
					
					
					job.analyseLine(line);
					
				}
				
				job.submit();
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	

}
