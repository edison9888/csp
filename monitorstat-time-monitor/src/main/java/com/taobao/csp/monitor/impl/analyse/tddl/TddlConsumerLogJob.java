package com.taobao.csp.monitor.impl.analyse.tddl;

import java.io.FileNotFoundException;
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
import com.taobao.monitor.common.util.BufferedReader2;

/**
 * tddl日志应用端分析
 * @author denghaichuan.pt
 * @version 2012-4-26
 */
public class TddlConsumerLogJob extends AbstractDataAnalyse {
	private static final Logger logger =  Logger.getLogger(TddlConsumerLogJob.class);
	
	public TddlConsumerLogJob(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}
	
	private Pattern timePattern = Pattern.compile("(\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");	
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	
	private Map<String, Map<String, Map<String, TDDL>>> cacheValue = new HashMap<String, Map<String,Map<String,TDDL>>>();

	// sql#@#my161156_cm6_ic_spu_1_3306#@#10.246.161.156#@#3306#@#ic_spu_1#@#EXECUTE_A_SQL_SUCCESS#@#165#@#91#@#0#@#2#@#12-04-19 10:02:29:019
	@Override
	public void analyseOneLine(String line) {
		String[] values = line.split("#@#");
		
		if(values.length != 11) {
//			logger.info("日志此行格式不匹配，每行有" + values.length + "个字段");
			return;
		}
		if("EXECUTE_A_SQL_SUCCESS".equals(values[5])){
			String collectTime = parseLogLineCollectTime(values[10]);
			String sql = cleanSql(values[0]);
			String dbIp = values[2];
			String dbPort = values[3];
			String dbName = values[4];
			String dBFeature = values[2]+"$"+values[3]+"$"+values[4];
			
			if (dBFeature.equals("") || sql.equals("")) {
				return;
			}
			Map<String, Map<String, TDDL>> dbDataMap = cacheValue.get(collectTime);
			if (dbDataMap == null) {
				dbDataMap = new HashMap<String, Map<String,TDDL>>();
				cacheValue.put(collectTime, dbDataMap);
			}
			
			Map<String, TDDL> sqlTddlMap = dbDataMap.get(dBFeature);
			if (sqlTddlMap == null) {
				sqlTddlMap = new HashMap<String, TDDL>();
				dbDataMap.put(dBFeature, sqlTddlMap);
			}
			
			TDDL tddl = sqlTddlMap.get(sql);
			if (tddl == null) {
				tddl = new TDDL();
				tddl.dbIp = dbIp;
				tddl.dbName = dbName;
				tddl.dbPort = dbPort;
				sqlTddlMap.put(sql, tddl);
			}
			
			
			tddl.exeCount +=  Long.parseLong(values[6]);
			tddl.respTime += Long.parseLong(values[7]);
			
			
			if (tddl.minResp > Integer.parseInt(values[8])) {
				tddl.minResp = Integer.parseInt(values[8]);
			}
			if (tddl.maxResp < Integer.parseInt(values[9])) {
				tddl.maxResp = Integer.parseInt(values[9]);
			}
		}
	}
	
	/**
	 * 格式化sql语言，将直接传入的参数修改为 ？
	 * @param sql
	 * @return
	 */
	
	private String cleanSql(String sql) {
		try {
			return sql.replaceAll("\\(\\?(,\\?)*\\)", "(?)");
		} catch (Exception e) {
			logger.error("清理sql出错", e);
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public void release() {
		cacheValue.clear();
	}

	@Override
	public void submit() {
		
		Map<Long, Map<String, TDDL>> sumValueMap = new HashMap<Long, Map<String,TDDL>>();
		
		
		try {
			for (Map.Entry<String, Map<String, Map<String, TDDL>>> entry : cacheValue.entrySet()) {
				String collectTime = entry.getKey();
				long time = sdf.parse(collectTime).getTime();
				for (Map.Entry<String, Map<String, TDDL>> entry1 : entry.getValue().entrySet()) {
					String dbFeature = entry1.getKey();
					for (Map.Entry<String, TDDL> entry2 : entry1.getValue().entrySet()) {
						String sql = entry2.getKey();
						TDDL tddl = entry2.getValue();
						
						Map<String, TDDL> sumDbMap = sumValueMap.get(time);
						if (sumDbMap == null) {
							sumDbMap = new HashMap<String, TDDL>();
							sumValueMap.put(time, sumDbMap);
						}
						TDDL sumTddl = sumDbMap.get(dbFeature);
						if (sumTddl == null) {
							sumTddl = new TDDL();
							sumTddl.dbIp = tddl.dbIp;
							sumTddl.dbName = tddl.dbName;
							sumTddl.dbPort = tddl.dbPort;
							sumDbMap.put(dbFeature, sumTddl);
						}
						
						sumTddl.exeCount +=  tddl.exeCount;
						sumTddl.respTime += tddl.respTime;
						
						
						if (sumTddl.minResp > tddl.minResp) {
							sumTddl.minResp = tddl.minResp;
						}
						if (sumTddl.maxResp < tddl.maxResp) {
							sumTddl.maxResp = tddl.maxResp;
						}
						
						double respTime = Arith.div(tddl.respTime,tddl.exeCount,2);
						
						Object[] objs = new Object[]{tddl.exeCount, respTime, tddl.maxResp, tddl.minResp};
						try {
							CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.TDDL_CONSUMER,dbFeature,sql}, 
									new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time","Max-C-time","Min-C-time"}, objs,
									new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE,ValueOperate.REPLACE,ValueOperate.REPLACE});
						} catch (Exception e) {
							logger.error("发送失败", e);
						}
					}
				}
			}
			
			for (Map.Entry<Long, Map<String, TDDL>> entry : sumValueMap.entrySet()) {
				Long time = entry.getKey();
				for (Map.Entry<String, TDDL> entry1 : entry.getValue().entrySet()) {
					String dbFeature = entry1.getKey();
					TDDL tddl = entry1.getValue();
					double respTime = Arith.div(tddl.respTime,tddl.exeCount,2);
					Object[] objs = new Object[]{tddl.exeCount, respTime, tddl.maxResp, tddl.minResp};
					try {
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.TDDL_CONSUMER,dbFeature}, 
								new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time","Max-C-time","Min-C-time"}, objs,
								new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE,ValueOperate.REPLACE,ValueOperate.REPLACE});
					} catch (Exception e) {
						logger.error("发送失败", e);
					}
				}
			}
		} catch (ParseException e) {
			logger.error("erroe", e);
		}
	}
	
	private class TDDL {
		
		public String dbIp;
		public String dbPort;
		public String dbName;
		
		public long exeCount;
		public long respTime;
		
		public int maxResp;
		public int minResp = 100;
	}
	
	protected String parseLogLineCollectTime(String logRecord) {
		Matcher m = timePattern.matcher(logRecord);		
		if(m.find()){
			return "20" + m.group(1);
		}
		
		return null;
	}
	
	public static void main(String[] args) {
		try {
			
			TddlConsumerLogJob job = new TddlConsumerLogJob("itemcenter","172.24.12.99","");
			
			BufferedReader2 reader = new BufferedReader2(new FileReader("D:\\work\\csp\\monitorstat-alarm\\target\\tddl-atom-statistic.log"));
			String line = null;
			while((line=reader.readLine())!=null){
				
				
				job.analyseOneLine(line);
				
			}
			
			job.submit();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
