
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
 * 2010-08-31 10:51:31,273 WARN  alarmServiceLog - appName malllist totalCount 1 getCount 1 insertCount 0 updateCount 0 removeCount 0 
 * @author xiaodu
 * @version 2010-8-5 ÉÏÎç10:48:57
 */
public class UicFinalLogFileAnalyse extends AnalyseFile{
	
	private static final Logger logger =  Logger.getLogger(UicFinalLogFileAnalyse.class);

	public UicFinalLogFileAnalyse(String appName)
			throws Exception {
		super(appName, "alarmServiceLogFile.log");
	}
	
	
	
	private Map<String,Inner> map = new HashMap<String, Inner>();
	
	private class Inner{
		private long totalCount;
		private long getCount;
		private long insertCount;
		private long updateCount;
		private long removeCount;
	}

	@Override
	protected void parseLogLine(String logRecord) {	
		try{
			Pattern pattern = Pattern.compile("alarmServiceLog - appName\\s+(\\w+)\\s+totalCount\\s+(\\d+)\\s+getCount\\s+(\\d+)\\s+insertCount\\s+(\\d+)\\s+updateCount\\s+(\\d+)\\s+removeCount\\s+(\\d+)");		
			Matcher m = pattern.matcher(logRecord);		
			if(m.find()){
				String appName =  m.group(1);
				String totalCount = ( m.group(2));
				String getCount = ( m.group(3));
				String insertCount = ( m.group(4));
				String updateCount = ( m.group(5));
				String removeCount = ( m.group(6));
				
				Inner inner = map.get(appName);
				
				if(inner == null){
					inner = new Inner();
					map.put(appName, inner);
				}
				inner.totalCount+=Long.parseLong(totalCount);
				inner.getCount+=Long.parseLong(getCount);
				inner.insertCount+=Long.parseLong(insertCount);
				inner.updateCount+=Long.parseLong(updateCount);
				inner.removeCount+=Long.parseLong(removeCount);
			}
		}catch (Exception e) {
			e.printStackTrace();
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
		
		Pattern pattern = Pattern.compile("alarmServiceLog - appName\\s+(\\w+)\\s+totalCount\\s+(\\d+)\\s+getCount\\s+(\\d+)\\s+insertCount\\s+(\\d+)\\s+updateCount\\s+(\\d+)\\s+removeCount\\s+(\\d+)");		
		Matcher m = pattern.matcher("2010-08-31 10:51:31,273 WARN  alarmServiceLog - appName malllist totalCount 1 getCount 1 insertCount 0 updateCount 0 removeCount 0");		
		if(m.find()){
			System.out.println( m.group(1));
			System.out.println( m.group(2));
			System.out.println( m.group(3));
			System.out.println( m.group(4));
			System.out.println( m.group(5));
			System.out.println( m.group(6));
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
				
		for(Map.Entry<String,Inner> entry:map.entrySet()){
			Inner inner = entry.getValue();
			content.putReportDataByCount(this.getAppName(), "OTHER_uicfinal-alarmServiceLog-"+entry.getKey()+"_totalCount_"+ Constants.COUNT_TIMES_FLAG,
					inner.totalCount+"", this.getCollectDate());
			content.putReportDataByCount(this.getAppName(), "OTHER_uicfinal-alarmServiceLog-"+entry.getKey()+"_getCount_"+ Constants.COUNT_TIMES_FLAG,
					inner.getCount+"", this.getCollectDate());
			content.putReportDataByCount(this.getAppName(), "OTHER_uicfinal-alarmServiceLog-"+entry.getKey()+"_insertCount_"+ Constants.COUNT_TIMES_FLAG,
					inner.insertCount+"", this.getCollectDate());
			content.putReportDataByCount(this.getAppName(), "OTHER_uicfinal-alarmServiceLog-"+entry.getKey()+"_removeCount_"+ Constants.COUNT_TIMES_FLAG,
					inner.removeCount+"", this.getCollectDate());
			content.putReportDataByCount(this.getAppName(), "OTHER_uicfinal-alarmServiceLog-"+entry.getKey()+"_updateCount_"+ Constants.COUNT_TIMES_FLAG,
					inner.updateCount+"", this.getCollectDate());
			
		}
		
	}
	
	


}
