package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
import com.taobao.csp.monitor.util.MonitorTimeUtil;

public class CommonLogFileAnalyse extends AbstractDataAnalyse  {

	public CommonLogFileAnalyse(String appName, String ip,String f) {
		super(appName, ip, f);
	}

	private static final Logger logger =  Logger.getLogger(CommonLogFileAnalyse.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	Pattern countPattern = Pattern.compile("(\\w+):(\\d+):count");
	Pattern averagePattern = Pattern.compile("(\\w+):(\\d+):average");
	
	
	Map<String,Map<String,CommonValue>> timeCountMap = new HashMap<String, Map<String,CommonValue>>();
	
	
	@Override
	public void analyseOneLine(String logRecord) {
		//2012-05-03 14:26:56 TC_REFUND_BUYER_RETURN_GOODS:1:count;TC_REFUND_BUYER_RETURN_GOODS:0:average;
		String time = parseLogLineCollectTime(logRecord);
		
		Map<String,CommonValue> countmap = timeCountMap.get(time);
		if(countmap == null){
			countmap = new HashMap<String, CommonValue>();
			timeCountMap.put(time, countmap);
		}
		
		
		if(time!=null){
			Matcher countmatcher = countPattern.matcher(logRecord);
			if(countmatcher.find()){
				String name = countmatcher.group(1);
				String num = countmatcher.group(2);
				
				CommonValue cv = countmap.get(name);
				if(cv == null){
					cv = new CommonValue();
					countmap.put(name, cv);
				}
				cv.count+=Integer.parseInt(num);				
				
			}
			
			Matcher averagematcher = averagePattern.matcher(logRecord);
			if(averagematcher.find()){
				String name = averagematcher.group(1);
				String num = averagematcher.group(2);
				
				CommonValue cv = countmap.get(name);
				if(cv == null){
					cv = new CommonValue();
					countmap.put(name, cv);					
				}
				cv.average+=Integer.parseInt(num);
			}
			
			
		}
	}
	protected String parseLogLineCollectTime(String logRecord) {
		return MonitorTimeUtil.getLogRecordTime(logRecord);
		
	}
	protected  String parseLogLineCollectDate(String logRecord){		
		return MonitorTimeUtil.getLogRecordDate(logRecord);
	}
	private class CommonValue{
		private long count;
		private int average;
		
	}

	@Override
	public void release() {
		timeCountMap.clear();
	}

	@Override
	public void submit() {
		long t = System.currentTimeMillis();
		
		for(Map.Entry<String,Map<String,CommonValue>> entry:timeCountMap.entrySet()){
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map<String,CommonValue> keyMap = entry.getValue();
			for(Map.Entry<String,CommonValue> entryKey:keyMap.entrySet()){
				CommonValue  cv = entryKey.getValue();	
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.COMMON_LOG, entryKey.getKey()},
							new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"count"}, 
							new Object[]{entryKey.getValue().count},new ValueOperate[]{ValueOperate.ADD});
				} catch (Exception e) {
					logger.error("∑¢ÀÕ ß∞‹", e);
				}
				
				if(cv.count >0){
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.COMMON_LOG, entryKey.getKey()},
								new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"average"}, 
								new Object[]{entryKey.getValue().average},new ValueOperate[]{ValueOperate.AVERAGE});
					} catch (Exception e) {
						logger.error("∑¢ÀÕ ß∞‹", e);
					}
				}
			}			
		}		
		logger.info("UicServiceLogFileAnalyse insertToDb:"+(System.currentTimeMillis()-t));
		
	}
	
	public static void main(String[] args) {
		CommonLogFileAnalyse analyse = new CommonLogFileAnalyse("aaaa", "25.363.2.3","");
		try {
//			BufferedReader br = new BufferedReader(new FileReader(new File("d:/test/log.txt")));
//			String line;
//			while ((line = br.readLine()) != null) {
//				
//			}
			analyse.analyseOneLine("2012-05-25 16:10:11,271 TASK_LoginLimitUICHandler:1:count;TASK_LoginLimitUICHandler:5:average");
			analyse.submit();
			analyse.release();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}