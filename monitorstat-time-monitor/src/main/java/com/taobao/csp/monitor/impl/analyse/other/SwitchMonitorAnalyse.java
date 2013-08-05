package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
/**
 * 开关中心分析器
 * @author hongbing.ww
 * @since 2013-5-21
 */

public class SwitchMonitorAnalyse extends AbstractDataAnalyse {

	private static final Logger logger = Logger.getLogger(TpMonitorAnalyse.class);
	
	Pattern pattern = Pattern.compile("[\\d]*");
	
	/**
	 * 用后缀表达式识别"Aden.auks.group.checkIpInvalid 5 2 10 50"
	 */
	Pattern postfix = Pattern.compile("\\s[\\d]*\\s[\\d]*\\s[\\d]*\\s[\\d]*$");
	
	private Map<Date,Map<String,SWITCH>> tpTimeMap = new HashMap<Date, Map<String,SWITCH>>();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Date time = null;

	/**
	 * @param appName
	 * @param ip
	 */
	public SwitchMonitorAnalyse(String appName, String ip,String f) {
		super(appName, ip,f);
	}

	@Override
	public void analyseOneLine(String line) {
		if(line.indexOf("|crr")>0){
			String timeStr = line.substring(0, 16);
			try{
			time = sdf.parse(timeStr);
			}catch (Exception e) {
				time = null;
			}
		}
		
		if(time == null){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			time = cal.getTime();
		}
		
		Map<String,SWITCH> tpMap = tpTimeMap.get(time);
		if(tpMap == null){
			tpMap = new HashMap<String, SwitchMonitorAnalyse.SWITCH>();
			tpTimeMap.put(time, tpMap);
		}
		
		try {
			String[] arr = StringUtil.split(line, " ");
			String key = arr[0];
			String ccr  = arr[1];
			String openqps = arr[2];
			String qps = arr[3];
			String rt = arr[4];
			
			SWITCH was = new SWITCH();
			was.ccr = Integer.parseInt(ccr);
			was.openqps = Integer.parseInt(openqps);
			was.qps = Integer.parseInt(qps);				
			was.rt = Integer.parseInt(rt);
			tpMap.put(key ,was);

		} catch (Exception e) {
			logger.error("Wap Switch Logformat error:", e);
		}
	}
	
	/**
	 * 开关中心输入参数
	 * @author hongbing.ww
	 */
	private class SWITCH{
		private int ccr; //线程并发数
		private int openqps; //打开的QPS
		private int qps; //OPS
		private int rt; //响应时间
	}

	@Override
	public void submit() {
		
		String wapSwitch = "Switch-rel";
		if(StringUtils.isNotBlank(this.getFeature())){
			wapSwitch = this.getFeature();
		}
		
		for(Map.Entry<Date,Map<String,SWITCH>> entrytime: tpTimeMap.entrySet()){
			Date time = entrytime.getKey();
			for(Map.Entry<String,SWITCH> entry: entrytime.getValue().entrySet()){
			
				try {
					if(entry.getValue().qps >50){
						CollectDataUtilMulti.collect(this.getAppName(), this.getIp(), time.getTime(), 
								new String[]{wapSwitch, entry.getKey()}, new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"Concurrentw","openQPS"}, 
								new Object[]{entry.getValue().ccr, entry.getValue().openqps},
								new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE});
						}
					
					CollectDataUtilMulti.collect(this.getAppName(), this.getIp(),time.getTime(), 
							new String[]{wapSwitch+"Property",entry.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.HOST}, new String[]{"QPS","RT"}, 
							new Object[]{entry.getValue().qps, entry.getValue().rt},
							new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD});
					
				} catch (Exception e) {
					logger.error("SwitchMonitorAnalyse 分析出错",e);
				}
			}
		}
	}

	@Override
	public void release() {
		tpTimeMap.clear();
	}
	
	public static void main(String[] args) {
		try {
			
			SwitchMonitorAnalyse job = new SwitchMonitorAnalyse("wass","172.24.12.99","");
			job.analyseOneLine("2013-05-21 10:08:57,982|ccr openqps qps rt");
			job.analyseOneLine("Aden.auks.group.checkIpInvalid 5 2 10 50");
			job.analyseOneLine("Aden.auks.group.checkIp 3 4 30 20");
			job.analyseOneLine("Aden.auks.group.getGroupByName 3 4 30 20");
			job.analyseOneLine("Aden.auks.group.getAllGroupByOperatorEnum 3 4 30 20");
			job.analyseOneLine("Aden.auks.group.deleteGroupsByName 3 4 30 20");
			job.analyseOneLine("Aden.auks.group.searchByQuery 3 4 30 20");
			job.submit();
			
		}catch (Exception e) {
			System.out.println(e);
		} 
	}

}
