
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
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
import com.taobao.monitor.common.util.Arith;

/**
 * 2010-08-05 18:06:04,208 WARN  uic.common - app-sample-thread tairPercent 99.87% tairTimeout 0.00% tairMissed 0.13% tairDataShoot 100.00% tairCnt 18843 tairSucc 18819 tairTimeout 0 tairMiss 24 tairDataCnt 6 tairDataSuccessCnt 6  
 * 
 * @author xiaodu
 *
 * …œŒÁ9:10:56
 */
public class UicFinalClientLogFileAnalyse extends AbstractDataAnalyse{
	private static final Logger logger =  Logger.getLogger(UicFinalClientLogFileAnalyse.class);

	
	private Map<String,Map<String,Double>> maporacleMap = new HashMap<String, Map<String,Double>>();
	/**
	 * @param appName
	 * @param ip
	 */
	public UicFinalClientLogFileAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#analyseOneLine(java.lang.String)
	 */
	@Override
	public void analyseOneLine(String logRecord) {
		String time = parseLogLineCollectTime(logRecord);
		
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
				
				
				String tairTimeout = m.group(7);
				f = keyValuemap.get("tairTimeout");
				if(f==null){
					keyValuemap.put("tairTimeout", Double.parseDouble(tairTimeout));
				}else{
					keyValuemap.put("tairTimeout", Arith.add(Double.parseDouble(tairTimeout), f));
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
			logger.error("error", e);
		}
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#submit()
	 */
	@Override
	public void submit() {
		long t = System.currentTimeMillis();		
		for(Map.Entry<String,Map<String,Double>> entry:maporacleMap.entrySet()){
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			Map<String,Double> keyMap = entry.getValue();
			
			for(Map.Entry<String, Double> entryKey:keyMap.entrySet()) {
				try {
					if (entryKey.getKey().equals("tairPercent")) {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_CLIENT, entryKey.getKey()},
								new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"Percent"}, 
								new Object[]{entryKey.getValue()},new ValueOperate[]{ValueOperate.AVERAGE});
					} else {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_CLIENT, entryKey.getKey()},
								new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"E-times"}, 
								new Object[]{entryKey.getValue()},new ValueOperate[]{ValueOperate.ADD});
					}
				} catch (Exception e) {
					logger.error("∑¢ÀÕ ß∞‹", e);
				}
			}
		}
		logger.info("UicFinalClientLogFileAnalyse insertToDb:"+(System.currentTimeMillis()-t));
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#release()
	 */
	@Override
	public void release() {
		maporacleMap.clear();
	}

	protected String parseLogLineCollectTime(String logRecord) {
		
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			return m.group(1);
		}
		
		return null;
		
	}
	
	public static void main(String[] args) {
		UicFinalClientLogFileAnalyse analyse = new UicFinalClientLogFileAnalyse("aaaa", "25.363.2.3","");
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("d:/test/log.txt")));
			String line;
			while ((line = br.readLine()) != null) {
				analyse.analyseOneLine(line);
			}
			
			analyse.submit();
			analyse.release();
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
