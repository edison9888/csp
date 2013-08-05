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
 * 
 * @author denghaichuan.pt
 * @version 2012-5-2
 */
public class UicConherenceStatisticLogAnalyse  extends AbstractDataAnalyse {

	private static final Logger logger =  Logger.getLogger(UicConherenceStatisticLogAnalyse.class);
	
	private Map<String,Map<String, Double>> timeValueMap = new HashMap<String, Map<String,Double>>();
	
	public UicConherenceStatisticLogAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

	@Override
	public void analyseOneLine(String logRecord) {
		String time = parseLogLineCollectTime(logRecord);
		

		Map<String, Double> valueMap = timeValueMap.get(time);
		if(valueMap == null){
			valueMap = new HashMap<String, Double>();
			timeValueMap.put(time, valueMap);
		}
		
		
		
		Pattern pattern = Pattern.compile("dbName (\\w+) total ([\\d\\.]+) success ([\\d\\.]+) error ([\\d\\.]+) lastCount ([\\d\\.]+)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			String dbName = m.group(1);
//			String total = m.group(2);
//			String success = m.group(3);
			String error = m.group(4);
			String lastCount = m.group(5);
			
			String key = dbName.replace("_", "-");
			
			setMap(valueMap,key+">error",Double.parseDouble(error));
			setMap(valueMap,key+">lastCount",Double.parseDouble(lastCount));
		}
		
	}
	
	/**
	 * 
	 * @param valueMap
	 * @param keyName
	 * @param value
	 * @param type 1 add 2 average 3replace
	 */
	private void setMap(Map<String, Double> valueMap ,String keyName,double value){
		
		Double totalValue = valueMap.get(keyName);
		if(totalValue == null){
			valueMap.put(keyName, value);
		}else{
			valueMap.put(keyName, Arith.add(totalValue, value));
		}
	}
	
	protected String parseLogLineCollectTime(String logRecord) {
		
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			return m.group(1);
		}
		
		return null;
		
	}

	@Override
	public void release() {
		timeValueMap.clear();
		
	}

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Override
	public void submit() {
		long t = System.currentTimeMillis();	
		for(Map.Entry<String,Map<String, Double>> entry:timeValueMap.entrySet()){
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map<String, Double> valueMap = entry.getValue();
			
			for(Map.Entry<String, Double> valueEntry:valueMap.entrySet()){
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_CONHE, valueEntry.getKey()},
							new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"E-times"}, 
							new Object[]{valueEntry.getValue()},new ValueOperate[]{ValueOperate.ADD});
				} catch (Exception e) {
					logger.error("∑¢ÀÕ ß∞‹", e);
				}
			}
		}
		
		logger.info("UicConherenceStatisticLogAnalyse insertToDb:"+(System.currentTimeMillis()-t));
		
	}

	public static void main(String[] args) {
		UicConherenceStatisticLogAnalyse analyse = new UicConherenceStatisticLogAnalyse("aaaa", "25.363.2.3","");
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
