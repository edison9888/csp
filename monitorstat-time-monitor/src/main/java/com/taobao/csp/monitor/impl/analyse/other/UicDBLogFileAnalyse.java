
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
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
 * 上午9:12:07
 */
public class UicDBLogFileAnalyse extends AbstractDataAnalyse{
	private static final Logger logger = Logger.getLogger(UicDBLogFileAnalyse.class);

	private Map<String, Map<String, Double>> timeAddValueMap = new HashMap<String, Map<String, Double>>();
	private Map<String, Map<String, Double>> timeAverageValueMap = new HashMap<String, Map<String, Double>>();
	private Map<String, Map<String, Double>> timeReplaceValueMap = new HashMap<String, Map<String, Double>>();
	private SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
	/**
	 * @param appName
	 * @param ip
	 */
	public UicDBLogFileAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}
	
	protected String parseLogLineCollectTime(String logRecord) {
		return sdfDate.format(new Date()) + " " + logRecord.substring(0, 5);
	}

	/**
	 * 1 add 2 average 3replace
	 * @param valueMap
	 * @param keyName
	 * @param value
	 * @param type
	 */
	private void setMap(Map<String, Double> valueMap, String keyName, double value, int type) {

		if (type == 1) {
			Double totalValue = valueMap.get(keyName);
			if (totalValue == null) {
				valueMap.put(keyName, value);
			} else {
				valueMap.put(keyName, Arith.add(totalValue, value));
			}
		} else if (type == 2) {
			Double totalValue = valueMap.get(keyName);
			if (totalValue == null) {
				valueMap.put(keyName, value);
			} else {
				valueMap.put(keyName, Arith.div(Arith.add(totalValue, value), 2, 2));
			}

		} else if (type == 3) {
			Double totalValue = valueMap.get(keyName);
			if (totalValue == null) {
				valueMap.put(keyName, value);
			} else {
				valueMap.put(keyName, Arith.add(totalValue, value));
			}

		}
	}
	
	/**
	 * 2010-10-19 09:50:30,504 WARN dblog - DB Stat: dbname uic_13_master total
	 * 0 expt 0 timeout 0 flow 0 isFlowing false overCnt 0 dbFlow 0 baseDBNumber
	 * 100 dbname表示数据库名称，会有16个，需要监控 total，expt,timeout, 当isFlowing=true
	 * 需要报警如果expt+timeout超过总数的40%，就要报警
	 * 
	 * @param line
	 */
	private void parseDBStat(String line, Map<String, Double> valueAddMap, Map<String, Double> valueAveMap, Map<String, Double> valueRepMap) {
		Pattern pattern = Pattern
				.compile("DB Stat: dbname\\s*(\\w+)\\s*total\\s*([\\d\\.]+)\\s*expt\\s*([\\d\\.]+)\\s*timeout\\s*([\\d\\.]+)\\s*flow\\s*[\\d\\.]+\\s*isFlowing\\s(false|true)\\soverCnt");
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			String dbname = matcher.group(1);
			String total = matcher.group(2);
			String expt = matcher.group(3);
			String timeout = matcher.group(4);
			String isFlowing = matcher.group(5).equals("true") ? "1" : "0";

			String k = dbname.replace("_", "-");

			setMap(valueAddMap, k + ">total", Double.parseDouble(total), 1);
			setMap(valueAddMap, k + ">expt", Double.parseDouble(expt), 1);
			setMap(valueAddMap, k + ">timeout", Double.parseDouble(timeout), 1);
			setMap(valueRepMap, k + ">isFlowing", Double.parseDouble(isFlowing), 3);

			if (Double.parseDouble(total) > 0) {
				double v = Arith.div((Double.parseDouble(timeout) + Double.parseDouble(expt)), Double.parseDouble(total), 3);
				setMap(valueAveMap, k + ">(expt+timeout)/total", v, 2);
			}

		}

	}

	/**
	 * 2010-10-19 10:06:51,498 WARN dblog - Total stat:total 0 total_select 0
	 * total_update 0 total_insert 0 total_remove 0 expt 0 expt_select 0
	 * expt_update 0 expt_insert 0 expt_remove 0 duplicate 0 getData 0.0
	 * insertData 0.0 updateData 0.0 removeData 0.0 这些字段都需要监控， getData 0.64
	 * insertData 0.0 updateData 12.4这些SQL是平均响应时间，单位ms
	 * 
	 * @param line
	 */
	private void parseDBlogTotalstat(String line, Map<String, Double> valueAddMap, Map<String, Double> valueAveMap, Map<String, Double> valueRepMap) {
		Pattern pattern = Pattern
				.compile("Total stat:total\\s*([\\d\\.]+)\\s*total_select\\s*([\\d\\.]+)\\s*total_update\\s*([\\d\\.]+)\\s*total_insert\\s*([\\d\\.]+)\\s*total_remove\\s*([\\d\\.]+)\\s*expt\\s*([\\d\\.]+)\\s*expt_select\\s*([\\d\\.]+)\\s*expt_update\\s*([\\d\\.]+)\\s*expt_insert\\s*([\\d\\.]+)\\s*expt_remove\\s*([\\d\\.]+)\\s*duplicate\\s*([\\d\\.]+)\\s*getData\\s*([\\d\\.]+)\\s*insertData\\s*([\\d\\.]+)\\s*updateData\\s*([\\d\\.]+)\\s*removeData\\s*([\\d\\.]+)");
		Matcher matcher = pattern.matcher(line);
		if (matcher.find()) {
			String total = matcher.group(1);
			String total_select = matcher.group(2);
			String total_update = matcher.group(3);
			String total_insert = matcher.group(4);
			String total_remove = matcher.group(5);
			String expt = matcher.group(6);
			String expt_select = matcher.group(7);
			String expt_update = matcher.group(8);
			String expt_insert = matcher.group(9);
			String expt_remove = matcher.group(10);
			String duplicate = matcher.group(11);
			String getData = matcher.group(12);
			String insertData = matcher.group(13);
			String updateData = matcher.group(14);
			String removeData = matcher.group(15);

			setMap(valueAddMap, "Total-stat>total", Double.parseDouble(total), 1);
			setMap(valueAddMap, "Total-stat>total-select", Double.parseDouble(total_select), 1);
			setMap(valueAddMap, "Total-stat>total-update", Double.parseDouble(total_update), 1);
			setMap(valueAddMap, "Total-stat>total-insert", Double.parseDouble(total_insert), 1);
			setMap(valueAddMap, "Total-stat>total-remove", Double.parseDouble(total_remove), 1);

			setMap(valueAddMap, "Total-stat>expt", Double.parseDouble(expt), 1);
			setMap(valueAddMap, "Total-stat>expt-select", Double.parseDouble(expt_select), 1);
			setMap(valueAddMap, "Total-stat>expt-update", Double.parseDouble(expt_update), 1);
			setMap(valueAddMap, "Total-stat>expt-insert", Double.parseDouble(expt_insert), 1);
			setMap(valueAddMap, "Total-stat>expt-remove", Double.parseDouble(expt_remove), 1);

			setMap(valueAddMap, "Total-stat>duplicate", Double.parseDouble(duplicate), 1);

			setMap(valueAveMap, "Total-stat>getData", Double.parseDouble(getData), 2);
			setMap(valueAveMap, "Total-stat>updateData", Double.parseDouble(updateData), 2);
			setMap(valueAveMap, "Total-stat>removeData", Double.parseDouble(removeData), 2);
			setMap(valueAveMap, "Total-stat>insertData", Double.parseDouble(insertData), 2);

		}

	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#analyseOneLine(java.lang.String)
	 */
	@Override
	public void analyseOneLine(String logRecord) {
		String time = parseLogLineCollectTime(logRecord);
		if (time == null) {
			return;
		}

		Map<String, Double> valueAddMap = timeAddValueMap.get(time);
		if (valueAddMap == null) {
			valueAddMap = new HashMap<String, Double>();
			timeAddValueMap.put(time, valueAddMap);
		}
		
		Map<String, Double> valueAveMap = timeAverageValueMap.get(time);
		if (valueAveMap == null) {
			valueAveMap = new HashMap<String, Double>();
			timeAverageValueMap.put(time, valueAveMap);
		}
		
		Map<String, Double> valueRepMap = timeReplaceValueMap.get(time);
		if (valueRepMap == null) {
			valueRepMap = new HashMap<String, Double>();
			timeReplaceValueMap.put(time, valueRepMap);
		}

		if (logRecord.indexOf("DB Stat") > -1) {
			parseDBStat(logRecord, valueAddMap, valueAveMap, valueRepMap);
		}

		if (logRecord.indexOf("Total stat") > -1) {
			parseDBlogTotalstat(logRecord, valueAddMap, valueAveMap, valueRepMap);
		}
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#submit()
	 */
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Override
	public void submit() {
		long t = System.currentTimeMillis();
		for (Map.Entry<String, Map<String, Double>> entry : timeAddValueMap.entrySet()) {
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map<String, Double> valueMap = entry.getValue();

			for (Map.Entry<String, Double> valueEntry : valueMap.entrySet()) {
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_DB, valueEntry.getKey()},
							new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"Uic-db"}, 
							new Object[]{valueEntry.getValue()},new ValueOperate[]{ValueOperate.ADD});
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}
		
		for (Map.Entry<String, Map<String, Double>> entry : timeAverageValueMap.entrySet()) {
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map<String, Double> valueMap = entry.getValue();

			for (Map.Entry<String, Double> valueEntry : valueMap.entrySet()) {
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_DB, valueEntry.getKey()},
							new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"Uic-db"}, 
							new Object[]{valueEntry.getValue()},new ValueOperate[]{ValueOperate.AVERAGE});
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}
		
		for (Map.Entry<String, Map<String, Double>> entry : timeReplaceValueMap.entrySet()) {
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Map<String, Double> valueMap = entry.getValue();

			for (Map.Entry<String, Double> valueEntry : valueMap.entrySet()) {
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.UIC_DB, valueEntry.getKey()},
							new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"Uic-db"}, 
							new Object[]{valueEntry.getValue()},new ValueOperate[]{ValueOperate.REPLACE});
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}
		logger.info("UicDBLogFileAnalyse insertToDb:" + (System.currentTimeMillis() - t));
		
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#release()
	 */
	@Override
	public void release() {
		timeAddValueMap.clear();
		timeAverageValueMap.clear();
		timeReplaceValueMap.clear();
	}
	
	public static void main(String[] args) {
		UicDBLogFileAnalyse analyse = new UicDBLogFileAnalyse("aaaa", "25.363.2.3","");
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
