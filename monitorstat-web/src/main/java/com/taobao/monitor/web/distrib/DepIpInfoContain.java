
package com.taobao.monitor.web.distrib;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * 
 * @author xiaodu
 * @version 2010-11-7 上午10:43:13
 */
public class DepIpInfoContain {
	
	private static final Logger logger =  Logger.getLogger(DepIpInfoContain.class);
	
	private DepIpInfoContain(){};
	
	
	private static Map<String,DepIpInfo> deipInfoMap = new HashMap<String,DepIpInfo>();
	
	public static DepIpInfo get(String ipPort){
		
		if(deipInfoMap.get(ipPort) == null){
			String[] ipPorts = ipPort.split(":");
			
			BufferedInputStream input = null;
			try {
				URL url = new URL("http://opsfree.corp.taobao.com:9999/index.php/search_device_full_info/?return_types=xml&ip_0="+ipPorts[0]);
				URLConnection urlCon = url.openConnection();
				urlCon.setDoInput(true);
				urlCon.setConnectTimeout(1000000);
				urlCon.connect();
				input = new BufferedInputStream(urlCon.getInputStream());
				SAXReader reader = new SAXReader();
				Document doc = reader.read(input);	
				
				
				List<Node> nodeList = doc.selectNodes("/root/device");
				
				for(int i=0;i<nodeList.size();i++){
					DepIpInfo deInfo = new DepIpInfo();					
					Node node = nodeList.get(i);
					String machine_room_name = node.selectSingleNode("machine_room_name").getStringValue();
					String module_name = node.selectSingleNode("module_name").getStringValue();
					deInfo.setAppName(module_name);
					deInfo.setSiteName(machine_room_name);
					deInfo.setIp(ipPorts[0]);
					deInfo.setPort(ipPorts[1]);
					deipInfoMap.put(ipPort, deInfo);
					
					logger.info("获取："+ipPort+"信息");
					
					return deInfo;
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
			return null;
		}else{
			return deipInfoMap.get(ipPort);
		}
		
		
	}
	

}
