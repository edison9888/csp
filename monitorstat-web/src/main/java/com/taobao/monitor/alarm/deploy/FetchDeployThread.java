
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
 * ������ȡ ����״̬���߳�
 * @author xiaodu
 * @version 2011-3-3 ����11:24:41
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
	 * �������
	 */
	public void checkAppDeployStatus(){
		try {
			Map<String,String> appReleseMap = fetchDeployMessage();//ȡ�õ�ǰ���ڷ�����Ӧ��
			
			Map<String,String> newReleseAppMessage = new HashMap<String, String>();//�³��ֵķ���Ӧ��
			
			for(Map.Entry<String,String> entry:appReleseMap.entrySet()){
				String appName = entry.getKey();
				if(releseAppMessage.get(appName) == null){
					releseAppMessage.put(appName, entry.getValue());
					newReleseAppMessage.put(appName, entry.getValue());	
				}		
			}
			
			//�³��ֵķ���Ӧ�� ������ʾ
			for(Map.Entry<String,String> entry:newReleseAppMessage.entrySet()){
				
				String opsname = entry.getKey();
				
				String n = appNameMapping.get(opsname);
				if(n == null){
					n = opsname;
				}
				AppInfoPo po = AppCache.get().getOpsName(n);
				if(po != null){
					UserService.get().sendUserExtraMessage(po.getAppId(),"CSPϵͳ-Ӧ�÷�����ʾ","Ӧ��:"+entry.getValue()+"...�������ⷢ������!<table><tr><td><a target='_blank' href='http://cm.taobao.net:9999/monitorstat/extra/app_deploy_message.jsp'>����鿴��ϸ</a></td></tr></table>");
					FilterService.get().register(new AppTmpStopFilter(po.getAppId(),System.currentTimeMillis(),System.currentTimeMillis()+15*1000*60));
				}else{
					logger.warn("deploy-app can find :"+opsname);
				}
			}
			
			Set<String>  releseAppSet = new HashSet<String>();//��ǰ���ڷ�����
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
					UserService.get().sendUserExtraMessage(po.getAppId(), "CSPϵͳ-Ӧ�÷�����ʾ", "Ӧ��:"+appname+"�����Ѿ������������ⷢ������!<table><tr><td><a target='_blank' href='http://cm.taobao.net:9999/monitorstat/extra/app_deploy_message.jsp'>����鿴��ϸ</a></td></tr></table>");
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
	 * �ӷ���ϵͳ�л�ȡ����Ӧ�õķ������
	 * bbj��2̨,��Ҫ����2,���ڷ���CM3:1̨,CM4:0̨,�Ѿ����CM3:0̨,CM4:0̨
	 * sharereport��4̨,��Ҫ����4,���ڷ���CM3:1̨,CM4:0̨,�Ѿ����CM3:1̨,CM4:0̨
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
				int index = line.indexOf("��");				
				String appName = line.substring(0,index);
				map.put(appName, line);
				logger.info("���ڷ���Ӧ�� "+appName+":"+line);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
		}
		return map.size()==0?null:map;
	}
	
	
	
	
	

}
