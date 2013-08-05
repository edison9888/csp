
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu
 * 
 * 针对hesper的/home/admin/hesper/logs/Regards-Monitor.log
 *
 * 上午10:41:36
 */
public class HesperSearchLogAnalyse extends AbstractDataAnalyse{
	
	private static final Logger logger =  Logger.getLogger(HesperSearchLogAnalyse.class);
	
	Map<String,Map<String,Double>> map = new HashMap<String, Map<String,Double>>();
	
	SimpleDateFormat parseLogFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");

	/**
	 * @param appName
	 * @param ip
	 */
	public HesperSearchLogAnalyse(String appName, String ip,String f) {
		super(appName, ip, f);
	}

	@Override
	public void analyseOneLine(String line) {
		try{
			String time = line.substring(0,16);
			String[] tmp = line.split(" ");
			String[] results = tmp[1].split(":");				
			String key = results[0];
			String value = results[1];
			
			Map<String,Double> t = map.get(time);
			if(t == null){
				t = new HashMap<String, Double>();
				map.put(time, t);
			}
			t.put(key, Double.parseDouble(value));		
		}catch(Exception e){
			logger.error("", e);
		}	
	}

	@Override
	public void submit() {
		for(Map.Entry<String,Map<String,Double>> entry:map.entrySet()){
			try {
			long time = parseLogFormat.parse(entry.getKey()).getTime();
			Map<String,Double> tmp = entry.getValue();
				for(Map.Entry<String,Double> n:tmp.entrySet()){
						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{"hesper-search",n.getKey()},
								new KeyScope[]{KeyScope.NO,KeyScope.HOST}, new String[]{"rate"}, 
								new Object[]{n.getKey()},new ValueOperate[]{ValueOperate.REPLACE});
				}
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
		HesperSearchLogAnalyse analyse = new HesperSearchLogAnalyse("aaaa", "25.363.2.3","");
		analyse.analyseOneLine("2012-05-17-10-52-22 TAB-所有宝贝搜索平均时间:55");
	}

}
