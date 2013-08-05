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

public class WebWangWangLogAnalyse extends AbstractDataAnalyse   {

	public WebWangWangLogAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}
	public static final String ONE = "\u0001";
	public static final String TWO = "\u0002";
	public static final String THREE = "\u0003";
	private Map<String, double[]> oneMinuteLog = new HashMap<String, double[]>();
	private static final Logger logger = Logger.getLogger(WebWangWangLogAnalyse.class);
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	public static final String KEY = "OTHER_WEBWW_";

	/**
	 * 
	 * @param logRecord
	 * @return yyyy-MM-dd
	 */
	protected String parseLogLineCollectDate(String logRecord) {
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d)");
		Matcher m = pattern.matcher(logRecord);
		if (m.find()) {
			return m.group(1);
		}

		return null;
	}

	public static double getRate(String fail, String success) {
		long f = Long.parseLong(fail, 10);
		long s = Long.parseLong(success, 10);
		if (f == 0) {
			return 1;
		}

		if ((f + s > 100)) {
			return s * 1.0 / (s + f);
		} else if (f + s > 10 && f < 6) {
			return 1;
		} else if (f < 4) {
			return 1;
		}

		if (f + s > 0) {
			return s * 1.0 / (s + f);
		}
		return 1;
	}

	@Override
	public void analyseOneLine(String logRecord) {
		try {
			double[] logMap = new double[3];
			String[] logLine = logRecord.split(THREE);
			if (logLine.length == 2) {
				String time = logLine[1].substring(0, logLine[1].length() - 3);
				oneMinuteLog.put(time, logMap);

				String[] logValues = logLine[0].split(TWO);
				for (String logValue : logValues) {
					String[] log = logValue.split(ONE);
					if (log.length == 3) {
						logMap[0]=Double.parseDouble(log[1]);
						logMap[1] =Double.parseDouble(log[2]);;
						logMap[2]=getRate(log[1], log[2]);
					}
				}
			}
		} catch (Exception e) {
			logger.error("数据出错:" + logRecord);
		}
	}

	@Override
	public void release() {
		oneMinuteLog.clear();
	}

	@Override
	public void submit() {
		// OTHER_WEBWW_Login-Number 134123 2010年7月22日16:40:00
		for (Map.Entry<String, double[]> logs : oneMinuteLog.entrySet()) {
			 try {
				 	long time = sdf.parse(logs.getKey()).getTime();
				 	double[] v = logs.getValue();
					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{"webww-tair"},
							new KeyScope[]{KeyScope.HOST}, new String[]{"fail-num","suc-num","rate"}, 
							new Object[]{v[0],v[1],v[2]},new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD,ValueOperate.REPLACE});
				} catch (Exception e) {
					logger.error("发送失败", e);
				}
		}
	}
	
	public static void main(String[] args) {
		WebWangWangLogAnalyse web = new WebWangWangLogAnalyse("aa", "0","");
		web.analyseOneLine("tair042941sendmessage6507login0233ic0352tfs025tair_block00wwserverconnection07752uic0922receivemessage0573wwipfetch10db019662012-05-03 14:11:20");
	}

}
