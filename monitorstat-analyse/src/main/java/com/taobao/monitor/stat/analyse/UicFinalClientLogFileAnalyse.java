
package com.taobao.monitor.stat.analyse;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.stat.content.ReportContentInterface;

/**
 * 2010-08-05 18:06:04,208 WARN  uic.common - app-sample-thread tairPercent 99.87% tairTimeout 0.00% tairMissed 0.13% tairDataShoot 100.00% tairCnt 18843 tairSucc 18819 tairTimeout 0 tairMiss 24 tairDataCnt 6 tairDataSuccessCnt 6  
 * @author xiaodu
 * @version 2010-8-5 ÉÏÎç10:48:57
 */
public class UicFinalClientLogFileAnalyse extends AnalyseFile{
	
	private static final Logger logger =  Logger.getLogger(UicFinalClientLogFileAnalyse.class);

	public UicFinalClientLogFileAnalyse(String appName)
			throws Exception {
		super(appName, "uicclient.log");
	}
	
	
	private Map<String,Map<String,Double>> maporacleMap = new HashMap<String, Map<String,Double>>();
	
	
	

	@Override
	protected void parseLogLine(String logRecord) {		
		parseTotal(logRecord);		
	}
	
	
	private void parseTotal(String logRecord){
		
		String time = parseLogLineCollectTime(logRecord);
		
		if(time==null){
			logger.error(logRecord);
			return ;
		}
		
		Map<String ,Double> keyValuemap = maporacleMap.get(time);
		if(keyValuemap == null){
			keyValuemap = new HashMap<String, Double>();
			maporacleMap.put(time, keyValuemap);
		}
		
		try{
			Pattern pattern = Pattern.compile("app-sample-thread\\s+tairPercent\\s+([\\d\\.]+)%\\s+tairTimeout\\s+([\\d\\.]+)%\\s+tairMissed\\s+([\\d\\.]+)%\\s+tairDataShoot\\s+([\\d\\.]+)%\\s+tairCnt\\s+([\\d]+)\\s+tairSucc\\s+([\\d]+)\\s+tairTimeout\\s+([\\d\\.]+)\\s+tairMiss\\s+([\\d\\.]+)\\s+tairDataCnt\\s+([\\d\\.]+)\\s+tairDataSuccessCnt");		
			Matcher m = pattern.matcher(logRecord);		
			if(m.find()){
				String tairPercent = m.group(1);
				
				Double f = keyValuemap.get("tairPercent");
				if(f==null){
					keyValuemap.put("tairPercent", Double.parseDouble(tairPercent));
				}else{
					keyValuemap.put("tairPercent", Arith.div(Arith.add(Double.parseDouble(tairPercent), f), 2,2));
				}				
				String tairCnt = m.group(5);
				
				f = keyValuemap.get("tairCnt");
				if(f==null){
					keyValuemap.put("tairCnt", Double.parseDouble(tairCnt));
				}else{
					keyValuemap.put("tairCnt", Arith.add(Double.parseDouble(tairCnt), f));
				}
				
				String tairSucc = m.group(6);
				f = keyValuemap.get("tairSucc");
				if(f==null){
					keyValuemap.put("tairSucc", Double.parseDouble(tairSucc));
				}else{
					keyValuemap.put("tairSucc", Arith.add(Double.parseDouble(tairSucc), f));
				}
				
				String tairDataCnt = m.group(9);
				
				f = keyValuemap.get("tairDataCnt");
				if(f==null){
					keyValuemap.put("tairDataCnt", Double.parseDouble(tairDataCnt));
				}else{
					keyValuemap.put("tairDataCnt", Arith.add(Double.parseDouble(tairDataCnt), f));
				}
				
			}
		}catch (Exception e) {
			
		}
	}
	
	

	@Override
	protected String parseLogLineCollectTime(String logRecord) {
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			return m.group(1);
		}
		
		return null;
		
	}

	
	public static void main(String[] args){
		
		Pattern pattern = Pattern.compile("app-sample-thread\\s+tairPercent\\s+([\\d\\.]+)%\\s+tairTimeout\\s+([\\d\\.]+)%\\s+tairMissed\\s+([\\d\\.]+)%\\s+tairDataShoot\\s+([\\d\\.]+)%\\s+tairCnt\\s+([\\d]+)\\s+tairSucc\\s+([\\d]+)\\s+tairTimeout\\s+([\\d\\.]+)\\s+tairMiss\\s+([\\d\\.]+)\\s+tairDataCnt\\s+([\\d\\.]+)\\s+tairDataSuccessCnt");		
		Matcher m = pattern.matcher("2010-08-05 18:06:04,208 WARN  uic.common - app-sample-thread tairPercent 99.87% tairTimeout 0.00% tairMissed 0.13% tairDataShoot 100.00% tairCnt 18843 tairSucc 18819 tairTimeout 0 tairMiss 24 tairDataCnt 6 tairDataSuccessCnt 6");		
		if(m.find()){
			System.out.println( m.group(1));
			System.out.println( m.group(5));
			System.out.println( m.group(6));
			System.out.println( m.group(9));
		}
		//System.out.println( parseTotal("2010-08-05 18:06:04,208 WARN  uic.common - app-sample-thread tairPercent 99.87% tairTimeout 0.00% tairMissed 0.13% tairDataShoot 100.00% tairCnt 18843 tairSucc 18819 tairTimeout 0 tairMiss 24 tairDataCnt 6 tairDataSuccessCnt 6"));
	}

	@Override
	protected String parseLogLineCollectDate(String logRecord) {
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			return m.group(1);
		}
		
		return null;
	}


	@Override
	protected void insertToDb(ReportContentInterface content) {
		
		
		Map<String,Double> maporaclecountMap = new HashMap<String, Double>();
		int size = 0;
		for(Map.Entry<String,Map<String,Double>> entry:maporacleMap.entrySet()){
			String time = entry.getKey();			
			Map<String,Double> keyMap = entry.getValue();			
			for(Map.Entry<String, Double> entryKey:keyMap.entrySet()){
				content.putReportData(this.getAppName(), "OTHER_uicfinal_"+entryKey.getKey()+"_"+ Constants.COUNT_TIMES_FLAG, entryKey.getValue().toString(),time);		
				String key = entryKey.getKey();				
				Double v = maporaclecountMap.get(key);
				
				if("tairPercent".equals(key)){
					size++;
					if(v==null){
						maporaclecountMap.put(key, entryKey.getValue());
					}else{
						maporaclecountMap.put(key, Arith.add(entryKey.getValue(), v));
					}
				}else{
					if(v==null){
						maporaclecountMap.put(key, entryKey.getValue());
					}else{
						maporaclecountMap.put(key, Arith.add(entryKey.getValue(), v));
					}
				}
			}	
		}
		
		
		for(Map.Entry<String,Double> entry:maporaclecountMap.entrySet()){
			
			if("tairPercent".equals(entry.getKey())){				
				content.putReportDataByCount(this.getAppName(), "OTHER_uicfinal_"+entry.getKey()+"_"+ Constants.COUNT_TIMES_FLAG, Arith.div(entry.getValue(), size, 2)+"", this.getCollectDate());				
			}else{
				content.putReportDataByCount(this.getAppName(), "OTHER_uicfinal_"+entry.getKey()+"_"+ Constants.COUNT_TIMES_FLAG,entry.getValue().longValue(), this.getCollectDate());		
			}
			
			
		}
		
		
		
		
	}
	
	


}
