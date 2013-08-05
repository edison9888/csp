
package com.taobao.monitor.web.distrib;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author xiaodu
 * @version 2010-11-7 ����11:22:58
 */
public class AppInfoContain {
	
	private String appName;
		
	//Map<Ӧ�ñ����������,Map<����Ӧ������,Map<����Ӧ�ö�Ӧ����,Set<ip>>>>
	private Map<String,Map<String,Map<String,Set<String>>>> appJifan = new HashMap<String, Map<String,Map<String,Set<String>>>>();//Ӧ�ñ�������ֲ���Ӧ������Ӧ�÷ֲ�
	
	
	public AppInfoContain(String name){
		this.appName = name;
	}
	
	public void putDepIpInfo(String cm,DepIpInfo depIpInfo){
		
		Map<String,Map<String,Set<String>>> tmp = appJifan.get(cm);
		if(tmp == null ){
			tmp = new HashMap<String, Map<String,Set<String>>>();
			appJifan.put(cm, tmp);
		}
		
		Map<String,Set<String>> tmp2 = tmp.get(depIpInfo.getAppName());
		
		if(tmp2 == null ){
			tmp2 = new HashMap<String, Set<String>>();
			tmp.put(depIpInfo.getAppName(), tmp2);
		}
		
		Set<String> tmp3 = tmp2.get(depIpInfo.getSiteName());
		
		if(tmp3 == null){
			tmp3 = new HashSet<String>();
			tmp2.put(depIpInfo.getSiteName(), tmp3);
		}
		tmp3.add(depIpInfo.getIp()+":"+depIpInfo.getPort());
		
		
	}
	
	
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public Map<String, Map<String, Map<String, Set<String>>>> getAppJifan() {
		return appJifan;
	}

	public void setAppJifan(Map<String, Map<String, Map<String, Set<String>>>> appJifan) {
		this.appJifan = appJifan;
	}

	

}
