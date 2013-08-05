
/**
 * monitorstat-monitor
 */
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

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu
 *
 * 上午9:10:09
 */
public class TpMonitorAnalyse extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(TpMonitorAnalyse.class);
	
	Pattern pattern = Pattern.compile("([\\w-]+)\\s+([\\d]+)\\s+([\\d]+)\\s+([\\d]+)\\s+([\\d]+)\\s+([\\d]+)\\s+([\\d\\.]+)%\\s+([\\d\\.]+)%");
	
	private Map<Date,Map<String,Tp>> tpTimeMap = new HashMap<Date, Map<String,Tp>>();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private Date time = null;
	
	public TpMonitorAnalyse(String appName, String ip) {
		super(appName, ip);
	}

	/**
	 * @param appName
	 * @param ip
	 */
	public TpMonitorAnalyse(String appName, String ip,String f) {
		super(appName, ip,f);
	}

	@Override
	public void analyseOneLine(String line) {
		if(line.indexOf("------")>0){
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
		
		
		Map<String,Tp> tpMap = tpTimeMap.get(time);
		if(tpMap == null){
			tpMap = new HashMap<String, TpMonitorAnalyse.Tp>();
			tpTimeMap.put(time, tpMap);
		}
		
		try {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				String key = matcher.group(1);
				String total = matcher.group(2);
				String success = matcher.group(3);
				String fail = matcher.group(4);
				
				Tp tp = new Tp();
				tp.total = Integer.parseInt(total);
				tp.success = Integer.parseInt(success);
				
				tp.failpercent = Float.parseFloat(matcher.group(8));				
				tp.keycost = Float.parseFloat(matcher.group(6));
				tpMap.put(key ,tp);

				
			}

		} catch (Exception e) {
			logger.error("", e);
		}
		
	}
	
	private class Tp{
		private int total;
		private int success;
		private float failpercent;
		private float keycost;
		
		
	}

	@Override
	public void submit() {
		
		String tp = "tp交易相关";
		
		
		if(StringUtils.isNotBlank(this.getFeature())){
			tp = this.getFeature();
		}
		
		for(Map.Entry<Date,Map<String,Tp>> entrytime: tpTimeMap.entrySet()){
			
			Date time = entrytime.getKey();
			
			for(Map.Entry<String,Tp> entry: entrytime.getValue().entrySet()){
			
				try {
					if(entry.getValue().total >50){
						CollectDataUtilMulti.collect(this.getAppName(), this.getIp(), time.getTime(), 
								new String[]{tp,entry.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.HOST}, new String[]{"failpercent","keycost"}, 
								new Object[]{entry.getValue().failpercent,entry.getValue().keycost},
								new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE});
						}
					
					CollectDataUtilMulti.collect(this.getAppName(), this.getIp(),time.getTime(), 
							new String[]{tp+"总量",entry.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, new String[]{"total","success"}, 
							new Object[]{entry.getValue().total,entry.getValue().success},
							new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD});
					
				} catch (Exception e) {
					logger.error("TopLogAnalyse 分析出错",e);
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
			
			TpMonitorAnalyse job = new TpMonitorAnalyse("itemcenter","172.24.12.99","");
			job.analyseOneLine(" item  total   success fail    threadCnt       cost    successPct      failPct");
			
			job.analyseOneLine("2012-08-06 11:19:42,663 WARN  tpMonitor -  ------------------------tpMonitor------------------------------");
			
			job.analyseOneLine("P1-Buy-DeliverFee                  31        31    0            0          4        100.0%         0.0%");
			
			job.submit();
			
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

}
