
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
 * 分析 ic命中率的统计日志：ic-Client-monitor.log
 * 
 * 2010-07-01 00:03:42,476 WARN itemCacheLogger -
 * icTairCacheTotal:770,icTairCacheSuccess
 * :697,icTairCacheFailure:73,icTairCacheCost:1.07;icTairCacheLength:5025
 * .00;icQueryForDetailTotal
 * :770,icQueryForDetailSuccess:769,icQueryForDetailFailure
 * :1,icQueryForDetailCost:5.80;icQueryDBTotal:72,icQueryDBSuccess:72,icQueryDB
 * Failure:0,icQueryDBCost:19.38; 成功率: icTairCacheSuccPct: 90.5%,
 * icQueryForDetailSuccPct: 99.9%, 失效率: icTairCacheFailPct: 9.5%,
 * icQueryForDetailFailPct: 0.1%,
 * 
 * 
 * 在采样过程中，每次是取出其中的一行。如果简单地从icTairCacheSuccPct: 90.5%,从这一行取tair的命中率，很难拿到这个率的统计时间。
 * 其实这个命中率是通过第一行中icTairCacheSuccess/icTairCacheTotal 获得，因此在这里还是分析第一行日志信息。
 * 
 * 
 */
public class ICCacheHitRateAnalyse extends AbstractDataAnalyse{
	private static final Logger logger = Logger.getLogger(ICCacheHitRateAnalyse.class);
	/**
	 * @param appName
	 * @param ip
	 */
	public ICCacheHitRateAnalyse(String appName, String ip,String f) {
		super(appName, ip,f);
	}
	
	static DecimalFormat pctFormat = (DecimalFormat) NumberFormat.getPercentInstance();

	static {
		pctFormat.applyPattern("#0.000"); // 0

	}

	Pattern pattern = Pattern.compile("(\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2})");
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private Map<String, double[]> map = new HashMap<String, double[]>();

	@Override
	public void analyseOneLine(String line) {
		try {

			// 1. 得到一条记录的对象
			Matcher m = pattern.matcher(line);
			// 对于其它line，直接忽略
			if (!m.find()) {
				return;
			}

			String time = m.group(1);
			// System.out.println(time);

			String[] lice = line.split(",");

			String[] success = lice[2].split(":");
			String failure[] = lice[3].split(":");

			long successNum = Long.parseLong(success[1]);
			long failureNum = Long.parseLong(failure[1]);

			double sucPct = Arith.div(successNum, successNum + failureNum, 3);
			double failPct =  Arith.div( failureNum , (successNum + failureNum),3);

			if (logger.isDebugEnabled()) {
				logger.debug("suc num :" + successNum);
				logger.debug("failure num :" + failureNum);
				logger.debug("suc:" + sucPct);
				logger.debug("failure:" + failPct);
			}

			double[] keyMap = map.get(time);
			if (keyMap == null) {
				keyMap = new double[2];
				map.put(time, keyMap);
			}

			keyMap[0]=sucPct;
			keyMap[1]=failPct;

		} catch (Exception e) {
			logger.warn("analyse ic log :", e);

		}
		
	}
	@Override
	public void submit() {
		for (Map.Entry<String, double[]> entry : map.entrySet()) {
			long time = 0l;
			try {
				time = sdf.parse(entry.getKey()).getTime();
			} catch (ParseException e1) {
			}
			double[] v = entry.getValue();
			
			try {
				CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.IC_CACHE},
						new KeyScope[]{KeyScope.HOST}, new String[]{"ic-c-s","ic-c-f"}, 
						new Object[]{v[0],v[1]},new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE});
			} catch (Exception e) {
				logger.error("发送失败", e);
			}
		}
		
	}

	@Override
	public void release() {
		map.clear();
	}
	
	
	public static void main(String[] args) {
		ICCacheHitRateAnalyse analyse = new ICCacheHitRateAnalyse("aaaa", "25.363.2.3","");
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
