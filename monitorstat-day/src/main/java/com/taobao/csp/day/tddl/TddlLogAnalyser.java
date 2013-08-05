package com.taobao.csp.day.tddl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.Log;

public class TddlLogAnalyser extends AbstractAnalyser {

	public static Logger logger = Logger.getLogger(TddlLogAnalyser.class);

	private Pattern minitePattern = Pattern.compile("(\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");
	
	private Map<TddlLogKey, TDDL> cache = new HashMap<TddlLogKey, TDDL>();
	
	public TddlLogAnalyser(String appName, HostInfo hostInfo, char lineSplit) {
		super(appName, hostInfo, lineSplit);
	}
	
	@Override
	public List<Log> analyse(String segment) {
		segment = this.getResidue() + segment;
		int lastFlag = segment.lastIndexOf(String.valueOf(this.getLineSplit()));
		String newResidue = new String(segment.substring(lastFlag + 1, segment.length()).toCharArray());
		this.setResidue(newResidue);
		segment = segment.substring(0, lastFlag + 1);

		cache.clear();
		String [] lines = segment.split(String.valueOf(this.getLineSplit()));
		for (String line : lines) {
			analyseOneLine(line);
		}
		
		List<Log> list = generateLogPos();
		cache.clear();
		
		return list;
	}
	
	public void analyseOneLine(String line) {
		String[] values = line.split("#*@#*");
		if(values.length != 11) {
			return;
		}
		if("EXECUTE_A_SQL_SUCCESS".equals(values[5])){
			
			String sql = cleanSql(values[0]);
			if (sql == null) {
				return;
			}
			String dBFeature = values[1];
			
			String dbIp = values[2];
			String dbPort = values[3];
			String dbName = values[4];
			
			long executeSum = 0;
			long restTime = 0;
			int minRestTime = 0;
			int maxRestTime = 0;
			String collectMinute = null;
			String collectHour = null;
			
			try {
				executeSum = Long.parseLong(values[6]);
				restTime = Long.parseLong(values[7]);
				
				minRestTime = Integer.parseInt(values[8]);
				maxRestTime = Integer.parseInt(values[9]);
				
				collectMinute = parseLogLineCollectTime(values[10].trim());
				collectHour = collectMinute.substring(0, collectMinute.length() - 3);
				
				// 出现了一些奇怪的日期，过滤掉, 有些应用日志停掉了，已经是很多天前的
				boolean avaliableDate = checkDate(collectHour);
				if (!avaliableDate) {
					return;
				}
			} catch (Exception e) {
				logger.error("transfer log info error", e);
				return;
			}
			
			
			TddlLogKey key = new TddlLogKey(this.getAppName(), dBFeature, dbName, dbIp, dbPort, sql, collectHour);
			
			TDDL tddl;
			if (cache.get(key) != null) {
				tddl = cache.get(key);
				tddl.setExeCount(tddl.getExeCount() + executeSum);;
				tddl.setRespTime(tddl.getRespTime() + restTime);
				if (tddl.getMinResp() > minRestTime) {
					tddl.setMinResp(minRestTime);
					tddl.setMinRespDate(collectMinute);
				}
				if (tddl.getMaxResp() < maxRestTime) {
					tddl.setMaxResp(maxRestTime);
					tddl.setMaxRespDate(collectMinute);
				}
			} else {
				tddl = new TDDL();
				tddl.setExeCount(executeSum);
				tddl.setRespTime(restTime);
				tddl.setMaxResp(minRestTime);
				tddl.setMaxRespDate(collectMinute);
				tddl.setMinResp(minRestTime);
				tddl.setMinRespDate(collectMinute);
				cache.put(key, tddl);
			}
		}
	}
	
	@Override
	public boolean isCurrentLog(String segment) {
		boolean isCurrentLog = true;
		if (StringUtils.isEmpty(segment)) {
			return isCurrentLog;
		}
		
		String sampleRecord;
		String [] valuesS = segment.split(String.valueOf(this.getLineSplit()));
		if (valuesS.length <= 2) {
			sampleRecord = valuesS[0];
			logger.warn(sampleRecord + " sql is somewhat long, be aware of...");
		} else {
			sampleRecord = valuesS[1];
		}
		
		String[] fields = sampleRecord.split("#*@#*");
		if (fields.length < 11) {
			return isCurrentLog;
		}
		
		String collectMinute = parseLogLineCollectTime(fields[10].trim());
		String collectHour = collectMinute.substring(0, collectMinute.length() - 3);
		boolean isCurrentDate = checkCurrentDate(collectHour);
		isCurrentLog = isCurrentDate ? true : false;
		
		return isCurrentLog;
	}
	
	private List<Log> generateLogPos() {
		List<Log> list = new ArrayList<Log>();
		for (Map.Entry<TddlLogKey, TDDL> entry : cache.entrySet()) {
			TddlLogKey key = entry.getKey();
			TDDL tddl = entry.getValue();
			
			String sql = key.getSql();
			String dBFeature = key.getDbFeature();
			String dbIp = key.getDbIp();
			String dbPort = key.getDbPort();
			String dbName = key.getDbName();
			String collectHour = key.getCollectTime();
			
			TddlLog po = new TddlLog();
			po.setAppName(this.getAppName());
			po.setDbFeature(dBFeature);
			po.setDbName(dbName);
			po.setDbIp(dbIp);
			po.setDbPort(dbPort);
			po.setSql(sql);
			po.setCollectTime(collectHour);
			
			po.setExecuteSum(tddl.getExeCount());
			po.setExecuteTime(tddl.getRespTime());
			po.setMaxResp(tddl.getMaxResp());
			po.setMaxRespTime(tddl.getMaxRespDate());
			po.setMinResp(tddl.getMinResp());
			po.setMinRespTime(tddl.getMinRespDate());
			
			list.add(po);
		}
		
		return list;
		
	}
	
	/**
	 * 格式化sql语言，将直接传入的参数修改为 ？
	 * @param sql
	 * @return
	 */
	private String cleanSql(String sql) {
		try {
			if (sql.length() > 2048) {
				return null;
			} else {
				sql = sql.replaceAll("\\(\\?(,\\?)*\\)", "(?)");
				sql = sql.replaceAll("'", "");
				return sql;
			}
		} catch (Exception e) {
			logger.error("清理sql出错", e);
			return null;
		}
	}
	
	/***
	 * 解析时间
	 * @param time
	 * @return
	 */
	private String parseLogLineCollectTime(String time) {
		Matcher m = minitePattern.matcher(time);		
		if(m.find()){
			return "20"+m.group(1);
		}
		
		return null;
	}
	
	/***
	 * 时间校验
	 * @param collectTime
	 * @return
	 */
	private boolean checkDate(String collectTime) {
		boolean pass = true;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
		Calendar calendar = Calendar.getInstance();
		String thisHour = sf.format(calendar.getTime());
		
		calendar.add(Calendar.HOUR_OF_DAY, -1);
		String lastHour = sf.format(calendar.getTime());
		
		if (!thisHour.equals(collectTime) && !lastHour.equals(collectTime)) {
			pass = false;
		}
		
		return pass;
	}
	
	/***
	 * 时间校验
	 * @param collectTime
	 * @return
	 */
	private boolean checkCurrentDate(String collectTime) {
		boolean pass = true;
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH");
		Calendar calendar = Calendar.getInstance();
		String thisHour = sf.format(calendar.getTime());
		
		if (!thisHour.equals(collectTime)) {
			pass = false;
		}
		
		return pass;
	}

}
