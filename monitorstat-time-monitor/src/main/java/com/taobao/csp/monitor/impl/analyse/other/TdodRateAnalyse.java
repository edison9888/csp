package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

public class TdodRateAnalyse extends AbstractDataAnalyse {
	public TdodRateAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}
	Logger logger = Logger.getLogger(TdodRateAnalyse.class);
//	private Map<String,Integer> countmap = new HashMap<String, Integer>();
//	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void analyseOneLine(String line) {
//		try {
//			if (line.indexOf("system_protect.cpp") < 0) {	//只分析system_protect.cpp 这种日志的情况
//				return;
//			}
//			String[] args = line.split("RATIO:");
//			if (args.length > 1) {
//				String time = line.trim().substring(1, 17); //截取时间
//				String[] array = args[1].split(",");
//				Integer value = Integer.parseInt(array[0]);
//				countmap.put(time, value);
//			} else {
//				return;
//			}
//		} catch (Exception e) {
//			logger.error("解析日志：" + line);
//			logger.error("TdodRateAnalyse->analyseOneLine", e);
//		}		
	}
	
	@Override
	public void release() {
//		countmap.clear();		
	}
	
	@Override
	public void submit() {
//		for(Map.Entry<String,Integer> entry:countmap.entrySet()){
//			long time = 0l;
//			try {
//				time = sdf.parse(entry.getKey()).getTime();
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			
//			try {
//				CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.TDOD},
//						new KeyScope[]{KeyScope.HOST}, new String[]{"E-times"}, 
//						new Object[]{entry.getValue()},new ValueOperate[]{ValueOperate.ADD});
//			} catch (Exception e) {
//				logger.error("发送失败", e);
//			}
//		}		
	}
}
