package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * 
 * @author baiyan
 * @version 2012-6-5
 */
public class ShopMonitorAnalyse extends AbstractDataAnalyse {

	
	public ShopMonitorAnalyse(String appName,String ip,String feature){
		super(appName, ip,feature);
	}

	public static String KEY_FAILPERCENT = "TOTALCOUNT";

	/**
	 * 超时次数
	 */
	public static String KEY_TIMEOUT = "TIMEOUT";

	/**
	 * 平均消耗时间
	 */
	public static String KEY_COST = "COST";

	/**
	 * 错误次数
	 */
	public static String KEY_ERROR = "ERROR";

	/**
	 * tair超时次数
	 */
	public static String KEY_TOTAL_TAIR_TIMEOUT = "TOTAL_TAIR_TIMEOUT";

	/**
	 * tair异常次数
	 */
	public static String KEY_TOTAL_TAIR_EXCEPTION = "TOTAL_TAIR_EXCEPTION";

	private Pattern patternMethod = Pattern
			.compile("([a-zA-Z]+.[a-zA-Z]+)\\s+([\\d]+)\\s+([\\d]+)+\\s+([\\d]+)+\\s+([\\d]+)\\s+([\\d]+)");

	private Pattern patternTair = Pattern
			.compile("[\\d]+\\s+[\\d]+\\s+[\\d]+\\s+([\\d]+)\\s+([\\d]+)\\s+[\\d]+\\s+[\\d]+\\s+[\\d]+\\s+[\\d]+"
					+ "\\s+[\\d]+\\s+[\\d]+\\s+([\\d]+)");
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private static final Logger logger = Logger	.getLogger(ShopMonitorAnalyse.class);

	private Map<String, Map<String, int[]>> timeMethodMap = new HashMap<String, Map<String, int[]>>();
	private Map<String,int[]> timeTairMap = new HashMap<String, int[]>();

	private void putMethod(String collectTime, String name, int cost,int timeout,int error) {

		Map<String,  int[]> map = timeMethodMap.get(collectTime);
		if (map == null) {
			map = new HashMap<String,  int[]>();
			timeMethodMap.put(collectTime, map);
		}

		 int[] c = map.get(name);
		if (c == null) {
			c = new int[3];
			c[0] = cost;
			c[1] = timeout;
			c[2] = error;
			map.put(name, c);
		} else {
			c[0] = (cost+c[0])/2 ;
			c[1] += timeout;
			c[2] += error;
			map.put(name, c);
		}

	}
	
	
	private void putTair(String collectTime,int timeout,int error) {


		 int[] c = timeTairMap.get(collectTime);
		if (c == null) {
			c = new int[2];
			c[0] = timeout;
			c[1] = error;
			timeTairMap.put(collectTime, c);
		} else {
			c[0] += timeout;
			c[1] += error;
			timeTairMap.put(collectTime, c);
		}

	}
	

	protected String parseLogLineCollectTime(String logRecord) {
		Pattern pattern = Pattern
				.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");
		Matcher m = pattern.matcher(logRecord);
		if (m.find()) {
			return m.group(1);
		}

		return null;
	}

	/** 日志格式
	 * 2012-06-05 10:00:15,078 WARN
	 * [com.taobao.shopservice.common.monitor.ShopMonitorService] Tair Monitor:
	 * GET_TAIR_COUNT GET_TAIR_SUCCESS GET_TAIR_MISS TOTAL_TAIR_TIMEOUT
	 * TOTAL_TAIR_EXCEPTION UPDATE_TAIR_COUNT UPDA TE_TAIR_SUCCESS
	 * INVALID_TAIR_COUNT INVALID_TAIR_SUCCESS MINVALID_TAIR_COUNT
	 * MINVALID_TAIR_SUCCESS TOTAL_TAIR_COUNT
	 * 
	 * 68295 67626 669 0 0 1309 1309 0 0 0 0 69604 2012-06-05 10:00:18,358 WARN
	 * [com.taobao.shopservice.common.monitor.ShopMonitorService] Method
	 * Monitor: method totalcount costtime avecosttime timeout error
	 * ShopReadService.queryAfterSaleService 53 37 0 0 0
	 */
	public void analyseOneLine(String line) {
		try {
			String collectTime = parseLogLineCollectTime(line);
			if (collectTime != null) {
				return;
			}

			Matcher matcherMethod = patternMethod.matcher(line);
			if (matcherMethod.find()) {
				String key = matcherMethod.group(1);
				String total = matcherMethod.group(2);

				if (Integer.parseInt(total) < 50) {
					return;
				}

				putMethod(collectTime, key ,
						Integer.parseInt(matcherMethod.group(4)),
						Integer.parseInt(matcherMethod.group(5)),
						Integer.parseInt(matcherMethod.group(6))
						);
	
			}

			Matcher matcherTair = patternTair.matcher(line);
			if (matcherTair.find()) {
				String total = matcherTair.group(3);
				if (Integer.parseInt(total) < 50) {
					return;
				}
				
				putTair(collectTime,Integer.parseInt(matcherTair.group(1)),Integer.parseInt(matcherTair.group(2)));

			}

		} catch (Exception e) {
			logger.error("", e);
		}

	}

	public void doAnalyse() {

	}


	@Override
	public void submit() {
		
		for (Map.Entry<String, Map<String, int[]>> entry : timeMethodMap
				.entrySet()) {
			String collectTime = entry.getKey();

			Map<String, int[]> vMap = entry.getValue();

			for (Map.Entry<String,  int[]> ventry : vMap.entrySet()) {
				String key = ventry.getKey();
				int[] v = ventry.getValue();
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), sdf.parse(collectTime).getTime(), new String[]{"SC-method-monitor",key},
							new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"cost","timeout","error"}, 
							new Object[]{v[0],v[1],v[2]},new ValueOperate[]{ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD});
				}catch (Exception e) {
					logger.error("发送失败", e);
				}
			}
		}
		
		for (Map.Entry<String,  int[]> ventry : timeTairMap.entrySet()) {
			String collectTime = ventry.getKey();
			int[] v = ventry.getValue();
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), sdf.parse(collectTime).getTime(), new String[]{"sc-tair-monitor"},
						new KeyScope[]{KeyScope.ALL}, new String[]{"timeout","error"}, 
						new Object[]{v[0],v[1]},new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD});
			}catch (Exception e) {
				logger.error("发送失败", e);
			}
		}
		
		
		
	}

	@Override
	public void release() {
		timeMethodMap.clear();
		timeTairMap.clear();
	}

}
