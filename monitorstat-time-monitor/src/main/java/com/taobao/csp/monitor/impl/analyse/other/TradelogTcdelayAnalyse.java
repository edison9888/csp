
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * 
 * 
 * 分析tradelogs /home/admin/tradelogs/logs/tc-delay.log 的日志
 * @author xiaodu
 *
 * 下午1:21:26
 */
public class TradelogTcdelayAnalyse extends AbstractDataAnalyse{
	
	private static final Logger logger = Logger.getLogger(TradelogTcdelayAnalyse.class);
	
	private Map<String,Map<String,Integer>> timeMap = new HashMap<String, Map<String,Integer>>();
	
	
	

	public Map<String, Map<String, Integer>> getTimeMap() {
		return timeMap;
	}
	/**
	 * @param appName
	 * @param ip
	 */
	public TradelogTcdelayAnalyse(String appName, String ip) {
		super(appName, ip);
	}
	/**
	 * @param appName
	 * @param ip
	 */
	public TradelogTcdelayAnalyse(String appName, String ip,String f) {
		super(appName, ip,f);
	}

	@Override
	public void analyseOneLine(String line) {
		
		if(line.indexOf("MemoChangeProcessor")>0){
			return ;
		}
		
		if(line.indexOf("CopyRefund")>0){
			return ;
		}
		
		if(line.indexOf("deliverCount=0")<0){
			return ;
		}
		String time = line.substring(0, 16);
		
		String[] tmp = StringUtils.split(line,"-");
		if(tmp.length>=3){
			String msg = tmp[3];
			String[] msgs = StringUtils.split(msg,",");
			if(msgs.length>2){
				String name = msgs[0].trim();
				
				Map<String,Integer> nameMap = timeMap.get(time);
				if(nameMap == null){
					nameMap = new HashMap<String, Integer>();
					timeMap.put(time, nameMap);
				}
				
				Integer count = nameMap.get(name);
				if(count == null){
					nameMap.put(name, 1);
				}else{
					nameMap.put(name, count+1);
				}
			}
		}
		
		
	}

	@Override
	public void submit() {
		
		SimpleDateFormat sdf1= new SimpleDateFormat("yyyy-MM-dd HH:mm");
		
		for(Map.Entry<String,Map<String,Integer>> entry:timeMap.entrySet()){
			
			String time = entry.getKey();
			
			Map<String,Integer> map = entry.getValue();
			
			for(Map.Entry<String,Integer> nameEntry:map.entrySet()){
				
				try {
					CollectDataUtilMulti.collect(getAppName(), getIp(), sdf1.parse(time).getTime(), new String[]{"TotalProcessor",nameEntry.getKey()},
							new KeyScope[]{KeyScope.APP,KeyScope.APP}, new String[]{"count"}, 
							new Object[]{nameEntry.getValue()},new ValueOperate[]{ValueOperate.ADD});
				}catch (Exception e) {
					logger.error("发送失败", e);
				}
				
			}
			
		
		}
		
	}
	

	@Override
	public void release() {
		timeMap.clear();
	}

}
