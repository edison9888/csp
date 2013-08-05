
package com.taobao.csp.time.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;


/**
 * 实时监控的临时 缓存
 * @author xiaodu
 *
 */
public class TimeCache implements Runnable{
	
	private static final Logger logger = Logger.getLogger(TimeCache.class);
	
	private Thread thread = null;
	
	private Set<String> appIdSet = Collections.synchronizedSet(new HashSet<String>());
	
	private static TimeCache c = new TimeCache();
	
	public static TimeCache get(){
		return c;
	}
	
	private String getMessageByJsp(String url) {
		
		HttpClient httpClient = new HttpClient();
		GetMethod get = new GetMethod(url);
		get.getParams().setContentCharset("GBK"); 
		get.addRequestHeader("Content-Type", "text/html; charset=GBK");  
		get.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"GBK");  

		
		StringBuilder sb = new StringBuilder();
		try {
			httpClient.executeMethod(get);
			return get.getResponseBodyAsString();
		}catch (Exception e) {
		}finally{
			get.releaseConnection();
		}
		return sb.toString();
	}
	
	public synchronized String getHtml(String url){
		
		if(!appIdSet.contains(url)){
			appIdSet.add(url);
		}
		String html = map.get(url);
		if(html == null){
			html= getMessageByJsp(url);
			map.put(url, html);
		}
		return html;
	}
	
	public synchronized Set<String> getSet(){
		
		Set<String> set = new HashSet<String>();
		set.addAll(appIdSet);
		return set;
	}
	
	public TimeCache(){
		thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	private static Map<String,String> map = new ConcurrentHashMap<String, String>();

	/**
	 * 增加重新加载缓存的接口。add by 中亭
	 */
	public void refreshNow() {
		for(String url:getSet()){
			try{
				logger.info("Time cache :"+url);
				String dayHtml= getMessageByJsp(url);
				map.put(url, dayHtml);
			}catch (Exception e) {
				logger.error("", e);
			}
		}
	}
	
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(1000*30);
			} catch (InterruptedException e) {
			}
			for(String url:getSet()){
				try{
					logger.info("Time cache :"+url);
					String dayHtml= getMessageByJsp(url);
					map.put(url, dayHtml);
				}catch (Exception e) {
					logger.error("", e);
				}
			}
		}
		
	}
	
	
	

}
