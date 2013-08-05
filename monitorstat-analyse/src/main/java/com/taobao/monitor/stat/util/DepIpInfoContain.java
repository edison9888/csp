package com.taobao.monitor.stat.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.OpsFreeHostCache;

/**
 * 
 * @author xiaodu
 * @version 2010-11-7 上午10:43:13
 */
public class DepIpInfoContain {
	
	
	private static DepIpInfoContain instance = new DepIpInfoContain(); 

	private static final Logger logger = Logger.getLogger(DepIpInfoContain.class);

	private DepIpInfoContain() {
	};
	
	public static DepIpInfoContain get(){
		return instance;
	}

	private  Map<String, DepIpInfo> deipInfoMap = new HashMap<String, DepIpInfo>();
	
	private  Map<String, DepIpInfo> deipInfoNameMap = new HashMap<String, DepIpInfo>();
	
	
	
	public  void clear(){
		deipInfoMap.clear();
		deipInfoNameMap.clear();
	}
	
	
	public  DepIpInfo getByName(String name){		
		DepIpInfo depIpInfo = deipInfoNameMap.get(name);		
		if (depIpInfo != null) {
			if(depIpInfo.getIp() == null){
				return null;
			}
			return depIpInfo;
		} else {
			
			HostPo po = OpsFreeHostCache.get().getHostInfoByNodeName( name);
			if(po ==null){
				logger.info("" + name + "：  无法获取信息");
				return null;
			}else{
				DepIpInfo deInfo = new DepIpInfo();			
				String machine_room_name = po.getHostSite();
				String module_name = po.getOpsName();
				String ip = po.getHostIp();
				deInfo.setAppName(module_name);
				deInfo.setSiteName(machine_room_name);
				deInfo.setIp(ip);
				deInfo.setMachineName(name);
				deipInfoMap.put(ip, deInfo);
				deipInfoNameMap.put(name, deInfo);
				logger.info("获取：" + ip + "信息");
				getByOpsName(module_name);
				return deInfo;
			}			

		}
	}

	public  DepIpInfo getByIp(String ip) {
		DepIpInfo depIpInfo = deipInfoMap.get(ip);		
		if (depIpInfo != null) {			
			if(depIpInfo.getIp() == null){
				return null;
			}			
			return depIpInfo;
		} else {
			HostPo host = null;
			try{
				host = OpsFreeHostCache.get().getHostCache(ip);
			}catch (Exception e) {
				// TODO: handle exception
			}
			DepIpInfo deInfo = new DepIpInfo();			
			if(host == null){
				deipInfoMap.put(ip, deInfo);
				deInfo.setAppName("未知");
				deInfo.setIp(ip);
				deInfo.setSiteName("未知");
				deInfo.setMachineName("未知");
				logger.info("" + ip + "：  无法获取信息");
				return deInfo;
			}else{
				String machine_room_name = host.getHostSite();
				String module_name = host.getOpsName();
				String name = host.getHostName();
				deInfo.setAppName(module_name);
				deInfo.setSiteName(machine_room_name);
				deInfo.setIp(ip);
				deInfo.setMachineName(name);
				deipInfoMap.put(ip, deInfo);
				deipInfoNameMap.put(name, deInfo);
				logger.info("获取：" + ip + "信息");
				getByOpsName(module_name);
				return deInfo;
			}		
		}
	}
	
	public  void getByOpsName(String opsName){
		List<HostPo> list = OpsFreeHostCache.get().getHostListNoCache(opsName);
		for (int i = 0; i < list.size(); i++) {
			DepIpInfo deInfo = new DepIpInfo();
			HostPo node = list.get(i);
			String machine_room_name = node.getHostSite();
			String module_name = node.getOpsName();
			String name = node.getHostName();
			String ip_0 = node.getHostIp();
			
			deInfo.setAppName(module_name);
			deInfo.setSiteName(machine_room_name);
			deInfo.setIp(ip_0);
			deInfo.setMachineName(name);
			deipInfoMap.put(ip_0, deInfo);
			deipInfoNameMap.put(name, deInfo);
		}
		
	}
	
}
