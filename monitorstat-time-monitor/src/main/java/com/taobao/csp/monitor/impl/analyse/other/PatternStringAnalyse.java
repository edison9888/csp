
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.log4j.Logger;

import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu
 * 不知道做什么用的，去掉
 * 上午10:02:44
 */
public class PatternStringAnalyse extends AbstractDataAnalyse{
	private static final Logger logger = Logger.getLogger(PatternStringAnalyse.class);
	private List<String> pList = new ArrayList<String>();
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	

	public PatternStringAnalyse(String appName, String ip, String feature) {
		super(appName, ip, feature);
		String[] args = feature.split(",");
		if(args!=null&&args.length >0){
			for(String s:args){
				pList.add(s);
			}
		}
	}
	
	private Map<String,Map<String,ExceptionPo>> collectValue = new HashMap<String, Map<String,ExceptionPo>>();
	
	private Queue<String> queue = new LinkedList<String>();
		
	private void parse() throws Exception{
		
		String time = sdf.format(new Date());
		String str = "";
		while((str=queue.poll())!=null){
			for(String s:pList){
				if(str.indexOf(s)>0){
					put(time,s,str);
				}
			}
		}
	}	
	
	private void put(String collectTime,String name,String stackMessage){
		
		Map<String,ExceptionPo> map = collectValue.get(collectTime);
		if(map==null){
			map = new HashMap<String, ExceptionPo>();
			collectValue.put(collectTime, map);
		}
		
		ExceptionPo c = map.get(name);
		if(c==null){
			c = new ExceptionPo();
			c.num=1;
			c.stackMessage = stackMessage;
			map.put(name, c);
		}else{
			c.num += 1;
			c.stackMessage = stackMessage;
		}		
	}
	
	public void doAnalyse() {
		try {
			parse();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	private class ExceptionPo implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int num;
		private String stackMessage;
	}

	@Override
	public void analyseOneLine(String line) {
//		if(StringUtils.isNotBlank(line))
//			queue.add(line);		
	}

	@Override
	public void submit() {
//		for (Map.Entry<String, Map<String, ExceptionPo>> entry : collectValue.entrySet()) {
//
//			long time = 0l;
//			try {
//				time = sdf.parse(entry.getKey()).getTime();
//			} catch (Exception e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//
//			Map<String, ExceptionPo> vMap = entry.getValue();
//
//			for (Map.Entry<String, ExceptionPo> ventry : vMap.entrySet()) {
//				String key = ventry.getKey();
//				long value = ventry.getValue().num;
//				try {
//					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.PATTERN, ventry.getKey()},
//							new KeyScope[]{KeyScope.NO, KeyScope.HOST}, new String[]{"E-times"}, 
//							new Object[]{ventry.getValue()},new ValueOperate[]{ValueOperate.ADD});
//				} catch (Exception e) {
//					logger.error("发送失败", e);
//				}
//			}
//		}
	}

	@Override
	public void release() {
		collectValue.clear();
	}

}