
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
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
 * @author xiaodu
 *
 * …œŒÁ9:12:16
 */
public class UicServiceLogFileAnalyse extends AbstractDataAnalyse{
	private static final Logger logger =  Logger.getLogger(UicServiceLogFileAnalyse.class);
	
	private Map<String,Map<String, Double>> timeValueMap = new HashMap<String, Map<String,Double>>();
	/**
	 * @param appName
	 * @param ip
	 */
	public UicServiceLogFileAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}
	
	
	private void setMap(Map<String, Double> valueMap ,String keyName,double value){
		Double totalValue = valueMap.get(keyName);
		if(totalValue == null){
			valueMap.put(keyName, value);
		}else{
			valueMap.put(keyName, Arith.add(totalValue, value));
		}
	}
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	protected String parseLogLineCollectTime(String logRecord) {
		return sdf.format(new Date());
		
	}
	
	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	
	protected  String parseLogLineCollectDate(String logRecord){
		return sdf1.format(new Date());
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#analyseOneLine(java.lang.String)
	 */
	@Override
	public void analyseOneLine(String logRecord) {
		String time = parseLogLineCollectTime(logRecord);
		if(time == null){
			return ;
		}		
		Map<String, Double> valueMap = timeValueMap.get(time);
		if(valueMap == null){
			valueMap = new HashMap<String, Double>();
			timeValueMap.put(time, valueMap);
		}		
		Pattern pattern = Pattern.compile("appflowcount (\\w+) count ([\\d\\.]+) timeout ([\\d\\.]+) expt ([\\d\\.]+) flowing ([\\d\\.]+)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			String appName = m.group(1);
			String count = m.group(2);			
			appName = appName.replace("_", "-");			
			setMap(valueMap,appName+">count",Double.parseDouble(count));
			
		}
	}
	
	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#submit()
	 */
	@Override
	public void submit() {
		long t = System.currentTimeMillis();
		
		for(Map.Entry<String,Map<String, Double>> entry:timeValueMap.entrySet()){
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map<String, Double> valueMap = entry.getValue();
			
			for(Map.Entry<String, Double> valueEntry:valueMap.entrySet()){
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_SERVICE, valueEntry.getKey()},
							new KeyScope[]{KeyScope.NO, KeyScope.ALL}, new String[]{"E-times"}, 
							new Object[]{valueEntry.getValue()},new ValueOperate[]{ValueOperate.ADD});
				} catch (Exception e) {
					logger.error("∑¢ÀÕ ß∞‹", e);
				}
			}
		}
		logger.info("UicServiceLogFileAnalyse insertToDb:"+(System.currentTimeMillis()-t));
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#release()
	 */
	@Override
	public void release() {
		timeValueMap.clear();
	}

	public static void main(String[] args) {
		UicServiceLogFileAnalyse analyse = new UicServiceLogFileAnalyse("aaaa", "25.363.2.3","");
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
