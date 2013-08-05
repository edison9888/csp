
package com.taobao.monitor.web.distrib;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;


/**
 * 
 * @author xiaodu
 * @version 2010-11-3 下午02:22:04
 */
public class FlowDistribution {
	
	private static final Logger logger =  Logger.getLogger(FlowDistribution.class);
	private static Map<String,AppInfoContain> cache = new HashMap<String, AppInfoContain>();
	
	private FlowDistribution(){}
	
	public static  AppInfoContain get(String opsName){
		
		if(cache.get(opsName) == null){
			AppInfoContain g = compute(opsName);
			cache.put(opsName, g);
			return g;
		}else{
			return cache.get(opsName);
		}
		
		
	}
	
	
	public static void clearAll(){
		cache.clear();
	}
	
	public static void clear(String opsName){
		cache.remove(opsName);
	}
	
	
	
	
	private static AppInfoContain compute(String opsName){
		
		
		AppInfoContain appInfoContain = new AppInfoContain(opsName);
		
		///1.依赖应用的机器总数
		///2.各个机房的数量
		Map<String,Set<AppInfo>> appInfoMap = getAllAppHostInfo(opsName);
		for(Map.Entry<String,Set<AppInfo>> appInfoEntry:appInfoMap.entrySet()){
			String jifan = appInfoEntry.getKey();
			
			Set<AppInfo> appInfoSet = appInfoEntry.getValue();//这个机房下有应用机器对象			
			for(AppInfo appinfo:appInfoSet){
				try {
					Set<String> depIpPortSet = appinfo.getDepIpPortSet();//这个台机器有这多依赖的ip
					for(String ipPort:depIpPortSet){
						DepIpInfo tmp =DepIpInfoContain.get(ipPort);//依赖的ip的信息	
						if(tmp == null||tmp.getAppName()==null){
							logger.info(ipPort+":没有应用信息");
							continue;
						}
						appinfo.putDepIpInfo(ipPort, tmp);
											
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		for(Map.Entry<String,Set<AppInfo>> appInfoEntry:appInfoMap.entrySet()){
			String jifanName = appInfoEntry.getKey();
			
			Set<AppInfo> appInfoSet = appInfoEntry.getValue();//这个机房下有应用机器对象			
			for(AppInfo appinfo:appInfoSet){
				Map<String, DepIpInfo> map = appinfo.getDepIpInfoMap();
				
				for(Map.Entry<String, DepIpInfo> entry: map.entrySet()){
						
					DepIpInfo depInfo = entry.getValue();
					
					appInfoContain.putDepIpInfo(jifanName, depInfo);
					
					
				}
				
				
				
			}
			
			
		}
		
		
		return appInfoContain;
	}
	
	
	private static Map<String,Set<AppInfo>> getAllAppHostInfo(String opsName){
		
		Map<String,Set<AppInfo>> mapAppInfo = new HashMap<String, Set<AppInfo>>();
		
		
		BufferedInputStream input = null;
		try {
			URL url = new URL("http://xiaolv.corp.taobao.com:9999/index.php/search_device_full_info/?return_types=xml&module_name="+opsName);
			URLConnection urlCon = url.openConnection();
			urlCon.setDoInput(true);
			urlCon.setConnectTimeout(1000000);
			urlCon.connect();
			input = new BufferedInputStream(urlCon.getInputStream());
			SAXReader reader = new SAXReader();
			Document doc = reader.read(input);	
			if(doc!=null){			
				List<Node> nodeList = doc.selectNodes("/root/device");			
				for(int i=0;i<nodeList.size();i++){
					Node node = nodeList.get(i);
					String machine_room_name = node.selectSingleNode("machine_room_name").getStringValue();
					
					String host_ip = node.selectSingleNode("ip_0").getStringValue();
					String state_name =node.selectSingleNode("state_name").getStringValue();
					if(!"working_online".equals(state_name)){
						continue;
					}
					AppInfo appInfo = new AppInfo();
					appInfo.setIp(host_ip);
					appInfo.setSiteName(machine_room_name);
					
					
					Set<AppInfo> set = mapAppInfo.get(machine_room_name);
					if(set == null){
						set = new HashSet<AppInfo>();
						mapAppInfo.put(machine_room_name, set);
					}
					set.add(appInfo);
				}
				
			}
			
		} catch (Exception e) {
		} finally{
			if(input!=null){
					try {
						input.close();
					} catch (IOException e) {
						
					}
				}
		}
		
		
		return mapAppInfo;
	}
	
	
	

}
