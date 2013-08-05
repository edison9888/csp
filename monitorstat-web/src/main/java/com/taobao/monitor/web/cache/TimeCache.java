
package com.taobao.monitor.web.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.web.util.RequestByUrl;


/**
 * 实时监控的临时 缓存
 * @author xiaodu
 *
 */
public class TimeCache implements Runnable{
	
	private static final Logger logger = Logger.getLogger(TimeCache.class);
	
	private Thread thread = null;
	
	private Set<Integer> appIdSet = Collections.synchronizedSet(new HashSet<Integer>());
	
	private static TimeCache c = new TimeCache();
	
	public static TimeCache get(){
		return c;
	}
	
	public synchronized String getHtml(int appId){
		
		if(!appIdSet.contains(appId)){
			appIdSet.add(appId);
		}
		String html = map.get(appId);
		
		return html;
	}
	
	public synchronized Set<Integer> getSet(){
		
		Set<Integer> set = new HashSet<Integer>();
		set.addAll(appIdSet);
		return set;
	}
	
	public TimeCache(){
//		thread = new Thread(this);
//		thread.setDaemon(false);
//		thread.start();
	}
	
	private static Map<Integer,String> map = new ConcurrentHashMap<Integer, String>();

	@Override
	public void run() {
//		appIdSet.add(16); // login
//		appIdSet.add(1); // item
//		appIdSet.add(2); // list
//		appIdSet.add(3); // shopsystem
//		appIdSet.add(323); // tradeface
//		appIdSet.add(330); // tradeface
//		appIdSet.add(341); // cart
//		appIdSet.add(384); // tradeplatform
//		appIdSet.add(8); // ic
//		appIdSet.add(21); // uicfinal
//		appIdSet.add(4); // shopcenter
//		appIdSet.add(338); // ump
//		
//		while(true){
//			
//			for(Integer appId:getSet()){
////				try{
////				
////					AppInfoPo appInfo = AppInfoAo.get().findAppInfoById(appId);
////					
////					logger.info("Time cache :"+appInfo.getAppName());
////					
////					
////					if(appInfo != null&&appInfo.getOpsName()!=null){
////						String dayHtml= RequestByUrl.getMessageByJsp("http://127.0.0.1:9999/monitorstat/index_time_show_b.jsp?appName="+appInfo.getAppName()+"&appId="+appInfo.getAppId() );
////						map.put(appId, dayHtml);
////					}
////				}catch (Exception e) {
////					logger.error("", e);
////				}
//			}
//			
//			try {
//				Thread.sleep(1000*60);
//			} catch (InterruptedException e) {
//			}
//		}
		
		
		
		
	}
	
	
	

}
