package com.taobao.csp.monitor.impl.analyse.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
public class UicTairLogFileAnalyse extends AbstractDataAnalyse  {
	private static final Logger logger = Logger.getLogger(UicTairLogFileAnalyse.class);

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private Map<String, Map<String, Double>> timeValueMap = new HashMap<String, Map<String, Double>>();

	public UicTairLogFileAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

	@Override
	public void analyseOneLine(String logRecord) {
		String time = parseLogLineCollectTime(logRecord);

		Map<String, Double> valueMap = timeValueMap.get(time);
		if (valueMap == null) {
			valueMap = new HashMap<String, Double>();
			timeValueMap.put(time, valueMap);
		}

		Pattern pattern = Pattern.compile("sample tair thread: tairPercent:([\\d\\.]+)%");
		Matcher m = pattern.matcher(logRecord);
		if (m.find()) {
			String tairPercent = m.group(1);
			setMap(valueMap, "tairPercent", Double.parseDouble(tairPercent));
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
	
	private void setMap(Map<String, Double> valueMap, String keyName, double value) {

		Double totalValue = valueMap.get(keyName);
		if (totalValue == null) {
			valueMap.put(keyName, value);
		} else {
			valueMap.put(keyName, Arith.div(Arith.add(totalValue, value), 2, 2));
		}
	}



	@Override
	public void release() {
		timeValueMap.clear();
	}
	@Override
	public void submit() {
		long t = System.currentTimeMillis();

		for (Map.Entry<String, Map<String, Double>> entry : timeValueMap.entrySet()) {
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map<String, Double> valueMap = entry.getValue();

			for (Map.Entry<String, Double> valueEntry : valueMap.entrySet()) {
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_TAIR, valueEntry.getKey()},
							new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"F-data"}, 
							new Object[]{valueEntry.getValue()},new ValueOperate[]{ValueOperate.AVERAGE});
				} catch (Exception e) {
					logger.error("∑¢ÀÕ ß∞‹", e);
				}
			}
		}
		logger.info("UicTairLogFileAnalyse insertToDb:" + (System.currentTimeMillis() - t));
	}
	
	public static void main(String[] args) {
		UicTairLogFileAnalyse analyse = new UicTairLogFileAnalyse("aaaa", "25.363.2.3","");
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
