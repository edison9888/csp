
package com.taobao.monitor.stat.analyse;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.stat.content.ReportContentInterface;

/**
 * 2010-04-02T18:18:50.342+0800: 1.372: [Full GC (System) [PSYoungGen: 1328K->0K(51008K)] [PSOldGen: 0K->1309K(466048K)] 1328K->1309K(517056K) [PSPermGen: 6458K->6458K(16384K)], 0.0289910 secs]
 * GC 记录两个数据
 * 1.GC 没分钟的数量
 * 2.每分钟 gc 的平均时间 1000000
 * @author xiaodu
 * @version 2010-4-2 下午05:14:45
 */
public class GCLogAnalyse extends AnalyseFile{
	
	private static final Logger logger =  Logger.getLogger(GCLogAnalyse.class);
	
	private Map<String,Map<String,GC>> collectTimeLogMap = new HashMap<String, Map<String,GC>>();//记录所有日志数据
	
	private Map<String,Long> cacheValue = new HashMap<String, Long>();
	
	public GCLogAnalyse(String appName) throws Exception {
		super(appName,"gc.log");		
	}	
	
	public Map<String,Long> getAverageGcLogMap(){		
		return cacheValue;
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
	Pattern cmsPattern = Pattern.compile("\\[Times:\\s*user=([\\d\\.]+)\\s*sys=([\\d\\.]+)\\s*,\\s*real=([\\d\\.]+)\\s*secs\\]");
	
	List<String> cmsTileList = new ArrayList<String>();
	{
		cmsTileList.add("CMS-initial-mark");
		cmsTileList.add("CMS-concurrent-sweep");
		cmsTileList.add("CMS-concurrent-reset");
		cmsTileList.add("CMS-concurrent-mark");
		cmsTileList.add("CMS-concurrent-preclean");
		cmsTileList.add("CMS-remark");
	}
	
	private GC parseCMSLog(String gclog){
		for(String title:cmsTileList){
			if(gclog.indexOf(title) >0){
				Matcher matcher = cmsPattern.matcher(gclog);
				if(matcher.find()){
					String time = matcher.group(3);
					Float _time = Float.parseFloat(time);
					long newTime = (long)(_time * 1000000);	
					GC gc = new GC();
					gc.name = "SELF_GC_"+title;
					gc.userTime +=newTime;
					gc.count+=1;
					return gc;
				}
				break;
			}
			
		}
		
		return null;
		
		
	}
	
	
	private  GC parseGcLog(String gcLog){
		
		try{
			Pattern pattern = Pattern.compile("(.*[^\\d])(\\d+)K->(\\d+)K\\((\\d+)K\\)\\s*,\\s*([\\d\\.]+)\\s*(secs)");		
			Matcher matcher = pattern.matcher(gcLog);
			if(matcher.find()){								
				String name = matcher.group(1);				
				String old_value = matcher.group(2);
				String new_value = matcher.group(3);
				String cap = matcher.group(4);
				String time = matcher.group(5);
				
				Float _time = Float.parseFloat(time);
				long newTime = (long)(_time * 1000000);			
				name = keyFilter(name);	
				
				GC gc = new GC();
				gc.name = "SELF_GC_"+name;
				gc.userTime +=newTime;
				gc.count+=1;
				
				return gc;
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

	protected void parseLogLine(String logRecord) {
		String time = parseLogLineCollectTime(logRecord);
		if(time==null)return ;	
		
		GC cms = parseCMSLog(logRecord);
		if(cms !=null){
			putParseValue(cms,time);
			return ;
		}
		List<String> gcLogList = readerTimeSelectFileLine(logRecord);
		for(String gcLog:gcLogList){
			GC gc = parseGcLog(gcLog);
			if(gc!=null)
			putParseValue(gc,time);
		}
	}

	/**
	 * 2010-04-02T18:18:50
	 */
	protected String parseLogLineCollectTime(String logRecord) {
		try{
			if(logRecord.length()<17){
				return null;
			}
			String time = logRecord.substring(0, 16).replaceAll("T", " ");	
			return time;
		}catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 2010-04-02
	 */
	protected String parseLogLineCollectDate(String logRecord) {
		try{
			if(logRecord.length()<10){
				return null;
			}
			String time = logRecord.substring(0, 10);			
			return time;	
		}catch (Exception e) {
			return null;
		}	
	}
	
	
	/**
	 * 将所有值根据时间点 保存在map中	
	 * @param mapValue
	 * @param collect_time
	 */
	protected void putParseValue(GC gc,String collect_time){
		
		if(collect_time==null)return ;
		
		Map<String,GC> logKeyValueMap =  collectTimeLogMap.get(collect_time);
		
		if(logKeyValueMap==null){
			logKeyValueMap = new HashMap<String, GC>();
			collectTimeLogMap.put(collect_time, logKeyValueMap);
		}
		
		GC gcObj = logKeyValueMap.get(gc.name);
		if(gcObj==null){
			logKeyValueMap.put(gc.name, gc);
		}else{
			gcObj.count=gcObj.count+gc.count;
			gcObj.userTime = gcObj.userTime+gc.userTime;
		}
		
		
	}
	
	private class GC{
		private String name;
		private long count;
		private long userTime;
		
	}

	@Override
	protected void insertToDb(ReportContentInterface content) {
		
		AppInfoPo app = AppInfoAo.get().getAppInfoByOpsName(this.getAppName());
		
		List<HostPo> hostList = OpsFreeHostCache.get().getHostListNoCache(app.getOpsField(), app.getOpsName());
		
		
		Iterator<Map.Entry<String,Map<String,GC>>> it = collectTimeLogMap.entrySet().iterator();
		
		Map<String, GC> allgcmap = new HashMap<String, GC>();
		
		while(it.hasNext()){			
			Map.Entry<String,Map<String,GC>> entry = it.next();			
			String collectTime = entry.getKey();
			Map<String,GC> keyMap = entry.getValue();
			
			for(Map.Entry<String, GC> countEntry:keyMap.entrySet()){
				String key = countEntry.getKey();
				GC count =  countEntry.getValue();	
				
				GC allgc = allgcmap.get(key);
				if(allgc==null){
					allgc = new GC();
					allgcmap.put(key, allgc);
				}
				allgc.count+=count.count;
				allgc.userTime+=count.userTime;				
				content.putReportData(this.getAppName(), key+"_"+Constants.AVERAGE_MACHINE_FLAG,count.count/hostList.size(), collectTime);
				content.putReportData(this.getAppName(), key+"_"+Constants.AVERAGE_USERTIMES_FLAG,count.userTime/count.count, collectTime);
				
			}
		}
		
		for(Map.Entry<String,GC> entry:allgcmap.entrySet()){			
			content.putReportDataByCount(this.getAppName(), entry.getKey()+"_"+Constants.AVERAGE_MACHINE_FLAG,entry.getValue().count/hostList.size(), this.getCollectDate());
			content.putReportDataByCount(this.getAppName(), entry.getKey()+"_"+Constants.AVERAGE_USERTIMES_FLAG,entry.getValue().userTime/entry.getValue().count, this.getCollectDate());
		}
	}
	
	
	
	public static void main(String[] args){
		try{
			GCLogAnalyse gc = new GCLogAnalyse("");
			BufferedReader reader = new BufferedReader(new FileReader("D:\\taobao-SVNROOT\\coremonitor\\monitorstat-web\\target\\gc.log"));
			String str = null;
			while((str=reader.readLine())!=null){
				gc.parseLogLine(str);
			}
			gc.insertToDb(null);
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
