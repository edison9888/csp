
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu
 * 
 * 针对ic的/home/admin/itemcenter/logs/misc/icdbMonitor.log
 *
 * 上午10:41:36
 */
public class IcDbMonitorLogAnalyse extends AbstractDataAnalyse{
	
	private static final Logger logger =  Logger.getLogger(IcDbMonitorLogAnalyse.class);
	private Map<String,IcDb> icdbMap = new HashMap<String, IcDb>();
	/**
	 * @param appName
	 * @param ip
	 */
	public IcDbMonitorLogAnalyse(String appName, String ip,String f) {
		super(appName, ip,f);
	}

	@Override
	public void analyseOneLine(String line) {
		try{
			
			String[] tmp = StringUtils.split(line," ");
			if(tmp.length<7){
				return ;
			}
			
			String name = tmp[0];
			String total = tmp[1];
			String success = tmp[2];
			String fail = tmp[3];
			String cost = tmp[4];
			String threadCnt = tmp[5];
			String failPct = tmp[6];

			if (Integer.parseInt(total) < 50) {
				return;
			}
			
			IcDb tp = new IcDb();
			tp.failpercent = Float.parseFloat(failPct.replaceAll("%", ""));
			tp.keycost = Float.parseFloat(cost);
			tp.threadcount = Float.parseFloat(threadCnt);
			

			icdbMap.put(name ,tp);
			
		}catch(Exception e){
			logger.error("分析"+line, e);
		}	
	}
	
	
	private class IcDb{
		private float failpercent;
		private float threadcount;
		private float keycost;
		
		
	}
	

	@Override
	public void submit() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		for(Map.Entry<String,IcDb> entry: icdbMap.entrySet()){
			try {
				CollectDataUtilMulti.collect(this.getAppName(), this.getIp(), cal.getTimeInMillis(), 
						new String[]{"icdb操作相关",entry.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.HOST}, new String[]{"failpercent","threadcount","keycost"}, 
						new Object[]{entry.getValue().failpercent,entry.getValue().threadcount,entry.getValue().keycost},
						new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE});
			} catch (Exception e) {
				logger.error("TopLogAnalyse 分析出错",e);
			}
		}
		
	}

	@Override
	public void release() {
		icdbMap.clear();
	}
	
	
	public static void main(String[] args) {
		IcDbMonitorLogAnalyse analyse = new IcDbMonitorLogAnalyse("aaaa", "25.363.2.3","");
		analyse.analyseOneLine("icdb0                 1188              1188                 0              1.33                 0              0.0%");
	}

}
