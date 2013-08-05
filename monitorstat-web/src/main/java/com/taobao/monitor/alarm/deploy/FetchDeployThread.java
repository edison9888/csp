
package com.taobao.monitor.alarm.deploy;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.filter.AppTmpStopFilter;
import com.taobao.monitor.alarm.n.filter.FilterService;
import com.taobao.monitor.alarm.n.user.UserService;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.web.cache.AppCache;

/**
 * 用来获取 发布状态的线程
 * @author xiaodu
 * @version 2011-3-3 上午11:24:41
 */
public class FetchDeployThread implements Runnable{
	
	
	private Thread thread = null;
	
	private static FetchDeployThread t = new FetchDeployThread();
	
	private FetchDeployThread(){
		thread = new Thread(this);
		thread.setName("Monitor - FetchDeployThread");
		thread.setDaemon(true);
	}
	
	public static FetchDeployThread get(){
		return t;
	}
	
	
	public void startup(){
		thread.start();
	}
	
	private static final Logger logger =  Logger.getLogger(FetchDeployThread.class);
	private Map<String,String> releseAppMessage = new HashMap<String, String>();
	
	private Map<String,String> appNameMapping = new HashMap<String, String>();
	
	
	{
		
		appNameMapping.put("item", "detail");
		appNameMapping.put("list", "hesper");
		
	}
	
	
	@Override
	public void run() {
		
		while(true){
			
			checkAppDeployStatus();
			
			try {
				Thread.sleep(10*1000);
			} catch (InterruptedException e) {
			}
		}
		
		
		
	}
	
	
	
	/**
	 * 
	 * 检查数据
	 */
	public void checkAppDeployStatus(){
		try {
			Map<String,String> appReleseMap = fetchDeployMessage();//取得当前正在发布的应用
			
			Map<String,String> newReleseAppMessage = new HashMap<String, String>();//新出现的发布应用
			
			for(Map.Entry<String,String> entry:appReleseMap.entrySet()){
				String appName = entry.getKey();
				if(releseAppMessage.get(appName) == null){
					releseAppMessage.put(appName, entry.getValue());
					newReleseAppMessage.put(appName, entry.getValue());	
				}		
			}
			
			//新出现的发布应用 发送提示
			for(Map.Entry<String,String> entry:newReleseAppMessage.entrySet()){
				
				String opsname = entry.getKey();
				
				String n = appNameMapping.get(opsname);
				if(n == null){
					n = opsname;
				}
				AppInfoPo po = AppCache.get().getOpsName(n);
				if(po != null){
					UserService.get().sendUserExtraMessage(po.getAppId(),"CSP系统-应用发布提示","应用:"+entry.getValue()+"...，请留意发布过程!<table><tr><td><a target='_blank' href='http://cm.taobao.net:9999/monitorstat/extra/app_deploy_message.jsp'>点击查看详细</a></td></tr></table>");
					FilterService.get().register(new AppTmpStopFilter(po.getAppId(),System.currentTimeMillis(),System.currentTimeMillis()+15*1000*60));
				}else{
					logger.warn("deploy-app can find :"+opsname);
				}
			}
			
			Set<String>  releseAppSet = new HashSet<String>();//当前正在发布的
			for(Map.Entry<String, String> e:releseAppMessage.entrySet()){
				releseAppSet.add(e.getKey());
			}
			
			
			Set<String>  endRrelesingAppSet = new HashSet<String>();
			
			for(String app:releseAppSet){
				if(appReleseMap.get(app)==null){
					endRrelesingAppSet.add(app);
					releseAppMessage.remove(app);
				}
			}
			
			for(String appname:endRrelesingAppSet){
				
				String n = appNameMapping.get(appname);
				if(n == null){
					n = appname;
				}
				AppInfoPo po = AppCache.get().getOpsName(n);
				if(po != null){
					UserService.get().sendUserExtraMessage(po.getAppId(), "CSP系统-应用发布提示", "应用:"+appname+"发布已经结束，请留意发布后结果!<table><tr><td><a target='_blank' href='http://cm.taobao.net:9999/monitorstat/extra/app_deploy_message.jsp'>点击查看详细</a></td></tr></table>");
					FilterService.get().register(new AppTmpStopFilter(po.getAppId(),System.currentTimeMillis(),System.currentTimeMillis()+5*1000*60));
				}else{
					logger.warn("deploy-app can find :"+appname);
				}
				
				releseAppMessage.remove(appname);
			}			
		} catch (Exception e) {
			
		}		
	}
	
	
	
	
	/**
	 * 从发布系统中获取到，应用的发布情况
	 * bbj共2台,需要发布2,正在发布CM3:1台,CM4:0台,已经完成CM3:0台,CM4:0台
	 * sharereport共4台,需要发布4,正在发布CM3:1台,CM4:0台,已经完成CM3:1台,CM4:0台
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> fetchDeployMessage() throws Exception{
		
		Map<String,String> map = new HashMap<String, String>();
		
		try{
			URL url = new URL("http://172.24.108.52/appops-deploy/artoo?act=deployList");
			URLConnection urlCon = url.openConnection();
			urlCon.setDoInput(true);
			urlCon.setConnectTimeout(5000);
			urlCon.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(urlCon.getInputStream(),"gbk"));
			String line = null;
			while((line=reader.readLine())!=null){
				int index = line.indexOf("共");				
				String appName = line.substring(0,index);
				map.put(appName, line);
				logger.info("正在发布应用 "+appName+":"+line);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return map.size()==0?null:map;
	}
	
	
	
	
	

}
