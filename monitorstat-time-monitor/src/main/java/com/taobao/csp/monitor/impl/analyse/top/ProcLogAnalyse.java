
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.top;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang.StringUtils;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu
 *mpstat ;echo 'load:';cat /proc/loadavg ;cat /proc/swaps 
 *
 *
 *Linux 2.6.18-164.el5.login (login1.cm3)         03/15/2012

04:22:06 PM  CPU   %user   %nice    %sys %iowait    %irq   %soft  %steal   %idle    intr/s
04:22:06 PM  all    0.93    0.01    3.19    0.04    0.01    0.03    0.00   95.79    288.22
0.11 0.24 0.25 1/5421 9721
Filename                                Type            Size    Used    Priority
/dev/sda3                               partition       2096472 1777244 -1
/home/swap1                             file            2097144 438628  -2
 *
 *
 * ÏÂÎç4:22:41
 */
public class ProcLogAnalyse extends AbstractDataAnalyse{
	
	
	private Queue<String> logQueue = new LinkedList<String>();
	
	private Map<String,Float> map = new HashMap<String, Float>();

	/**
	 * @param appName
	 * @param ip
	 * @param feature
	 */
	public ProcLogAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

	@Override
	public void analyseOneLine(String line) {
		logQueue.add(line);
	}
	
	
	@Override
	public void doAnalyse() {
		
		String line = null;
		while((line = logQueue.poll())!=null){
			if(line.indexOf("CPU")>-1){
				try{
					float v = analyseCpu(logQueue.poll());
					map.put("cpu", v);
				}catch (Exception e) {
				}
			}
			if(line.indexOf("load")>-1){
				try{
					float v =analyseload(logQueue.poll());
					map.put("load", v);
				}catch (Exception e) {
				}
			}
			if(line.indexOf("Filename")>-1){
				try{
					float v = analyseswap(logQueue);
					map.put("swap", v);
				}catch (Exception e) {
				}
				
			}
		}
	}
	//	04:42:08 PM  CPU   %user   %nice    %sys %iowait    %irq   %soft  %steal   %idle    intr/s
	//04:42:08 PM  all    0.93    0.01    3.19    0.04    0.01    0.03    0.00   95.79    288.35
	private float analyseCpu(String line){
		String[] lines = StringUtils.splitByWholeSeparator(line, " ");
		String idle = lines[lines.length-2];
		return 100-Float.parseFloat(idle);
	}
	//0.27 0.25 0.24 1/5268 17568
	private float analyseload(String line){
		String[] tmp = StringUtils.splitByWholeSeparator(line, " ");
		return Float.parseFloat(tmp[0]);
	}
	
	//dev/sda3                               partition       2096472 1778364 -1
	//home/swap1                             file            2097144 438612  -2
	private float analyseswap(Queue<String> logQueue){
		
		long size = 0;
		long use = 0;
		String line = null;
		while((line=logQueue.poll())!=null){
			String[] tmp = StringUtils.splitByWholeSeparator(line, "\t");
			size+=Long.parseLong(tmp[tmp.length-3]);
			use+=Long.parseLong(tmp[tmp.length-2]);
		}
		
		  BigDecimal b1 = new BigDecimal(use);
	        BigDecimal b2 = new BigDecimal(size);
	        return b1.divide(b2,2,BigDecimal.ROUND_HALF_UP).floatValue();
	}
	

	@Override
	public void submit() {
		
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		try {
			
			float load = map.get("load");
			float cpu = map.get("cpu");
			float swap = map.get("swap");
			
			CollectDataUtilMulti.collect(this.getAppName(), this.getIp(), cal.getTimeInMillis(), 
					new String[]{"TOPINFO"}, new KeyScope[]{KeyScope.HOST}, new String[]{"CPU","LOAD","SWAP"}, 
					new Object[]{cpu,load,swap},
					new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void release() {
		logQueue.clear();
		map.clear();
	}

}
