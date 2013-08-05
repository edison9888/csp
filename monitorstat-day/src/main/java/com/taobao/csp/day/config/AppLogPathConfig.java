package com.taobao.csp.day.config;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.day.base.DataType;

/***
 * 应用的日志路径配置
 * 系统设置一个默认配置，是默认配置的无需再配，不是默认配置的需要额外添加
 * @author youji.zj
 * @version 1.0
 *
 */
public class AppLogPathConfig {
	
	private static Map<DataType, Map<String, String>> logAppPath = new ConcurrentHashMap<DataType, Map<String, String>>();
	
	private static final String DEFAULT = "default";
	
	private static final String DEFAULT_TDDL = "/home/admin/logs/tddl/tddl-atom-statistic.log";
	
	private static final String DEFAULT_SPH = "/home/admin/logs/sph.log";
	
	private static final String DEFAULT_APACHE_SPECIAL = "/home/admin/cai/logs/cronolog/2012/11/2012-11-10-taobao-access_log";
	
	private static final String DEFAULT_TDOD = "/home/admin/cai/logs/tmd.log";
	
	private static final String PINKIE_ACCESS = "/home/admin/cai/logs/cronolog/${yyyy}/${MM}/${yyyy-MM-dd}-taobao-access_log";
	
	private static final String DEFAULT_GC = "/home/admin/logs/gc.log";
	
	
	static {
		Map<String, String> tddlPath = new ConcurrentHashMap<String, String>();
		tddlPath.put(DEFAULT, DEFAULT_TDDL);
		tddlPath.put("bp_sem", "/home/ads/logs/tddl/tddl-atom-statistic.log");
		tddlPath.put("bpbsev_kgb", "/home/ads/logs/tddl/tddl-atom-statistic.log");
		tddlPath.put("bpfe_kgb", "/home/ads/logs/tddl/tddl-atom-statistic.log");
		tddlPath.put("bpreview_kgb", "/home/ads/logs/tddl/tddl-atom-statistic.log");
		tddlPath.put("bpqc_kgb_cnz", "/home/ads/logs/tddl/tddl-atom-statistic.log");
		tddlPath.put("bpne_kgb", "/home/ads/logs/tddl/tddl-atom-statistic.log");
		tddlPath.put("dbm_p4p", "/home/ads/logs/tddl/tddl-atom-statistic.log");
		logAppPath.put(DataType.TDDL, tddlPath);
		
		Map<String, String> sphPath = new ConcurrentHashMap<String, String>();
		sphPath.put(DEFAULT, DEFAULT_SPH);
		logAppPath.put(DataType.SPH, sphPath);
		
		Map<String, String> apacheSpecialPath = new ConcurrentHashMap<String, String>();
		apacheSpecialPath.put(DEFAULT, DEFAULT_APACHE_SPECIAL);
		logAppPath.put(DataType.APACHE_SPECIAL, apacheSpecialPath);
		
		Map<String, String> tdodPath = new ConcurrentHashMap<String, String>();
		tdodPath.put(DEFAULT, DEFAULT_TDOD);
		logAppPath.put(DataType.TDOD, tdodPath);
		
		Map<String, String> pinkieAccessPath = new ConcurrentHashMap<String, String>();
		pinkieAccessPath.put(DEFAULT, PINKIE_ACCESS);
		logAppPath.put(DataType.PINKIE_ACCESS, pinkieAccessPath);
		
		Map<String, String> gcPath = new ConcurrentHashMap<String, String>();
		gcPath.put(DEFAULT, DEFAULT_GC);
		logAppPath.put(DataType.GC, gcPath);
	}
	
	public static String getLogPath(String appName, DataType dataType) {
		String logPath;
		Map<String, String> logPathM = logAppPath.get(dataType);
		if (logPathM.containsKey(appName)) {
			logPath = logPathM.get(appName);
		} else {
			logPath = logPathM.get(DEFAULT);
		}
		
		if (dataType == DataType.PINKIE_ACCESS) {
			Calendar calendar = Calendar.getInstance();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sf.format(calendar.getTime());
			String year = date.split("-")[0];
			String month = date.split("-")[1];
			logPath = logPath.replace("${yyyy}", year);
			logPath = logPath.replace("${MM}", month);
			logPath = logPath.replace("${yyyy-MM-dd}", date);
			
		}
		return logPath;
	}
	
	public static String getBackLogPath(String appName, DataType dataType) {
		String backLogPath = "";
		
		if (dataType == DataType.PINKIE_ACCESS) {
			Map<String, String> logPathM = logAppPath.get(dataType);
			if (logPathM.containsKey(appName)) {
				backLogPath = logPathM.get(appName);
			} else {
				backLogPath = logPathM.get(DEFAULT);
			}
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String date = sf.format(calendar.getTime());
			String year = date.split("-")[0];
			String month = date.split("-")[1];
			backLogPath = backLogPath.replace("${yyyy}", year);
			backLogPath = backLogPath.replace("${MM}", month);
			backLogPath = backLogPath.replace("${yyyy-MM-dd}", date);
			return backLogPath;
		}
		
		String logPath = getLogPath(appName, dataType);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sf;
		if (dataType == DataType.TDDL) {
			calendar.add(Calendar.HOUR_OF_DAY, -1);
			sf = new SimpleDateFormat("yyyy-MM-dd-HH");
		} else {
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			sf = new SimpleDateFormat("yyyy-MM-dd");
		}
		
		String time = sf.format(calendar.getTime());
		backLogPath = logPath + "." + time;
		
		return backLogPath;
	}
	
	public static void setLogPath(DataType dataType, String appName, String logPath) {
		Map<String, String> tddlPath = logAppPath.get(dataType);
		tddlPath.put(appName, logPath);
	}
	
	public static void main(String [] args) {
		System.out.println(getLogPath("detail", DataType.PINKIE_ACCESS));
		System.out.println(getBackLogPath("detail", DataType.PINKIE_ACCESS));
	}
}
