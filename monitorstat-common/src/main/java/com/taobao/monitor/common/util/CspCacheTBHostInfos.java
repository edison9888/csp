
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.HostAo;
import com.taobao.monitor.common.po.HostPo;

/**
 * @author xiaodu
 *
 * 下午4:04:20
 */
public class CspCacheTBHostInfos {
	
	private static Logger logger = Logger.getLogger(CspCacheTBHostInfos.class);
	
	
	private long nextCheck = System.currentTimeMillis() + 3600000;
	
	private Map<String,HostPo> hostIpMap = new HashMap<String, HostPo>();
	
	private Map<String,HostPo> hostNameMap = new HashMap<String, HostPo>();
	
	private Map<String,Map<String,HostPo>> appHostMap = new HashMap<String, Map<String,HostPo>>();
	
	private Map<String, List<HostPo>> appHostListMap = new HashMap<String, List<HostPo>>();
	
	/*实体机和虚拟机的对应关系*/
	private Map<String, List<HostPo>> parentHostListMap = new HashMap<String, List<HostPo>>();
	
	private  ReentrantLock timerLock = new ReentrantLock();
	
	private static Map<String, String> groupMap = new HashMap<String, String>();
	private static Map<String, String[]> appToGroupMap = new HashMap<String, String[]>();
	static {
		//暂时写死，以后统一数据库配置和diamond的配置
		groupMap.put("itemcenter`G1", "ic-L0-d");
		groupMap.put("itemcenter`G2", "ic-L0-c");
		groupMap.put("itemcenter`G3", "ic-L0-o");
		groupMap.put("itemcenter`G4", "ic-L1");
		groupMap.put("itemcenter`G5", "ic-L2");
		groupMap.put("itemcenter`G6", "ic-bid");

		groupMap.put("sell`G1", "sell");
		groupMap.put("sell`G2", "sell-upload");
		groupMap.put("sell`G3", "sell_top");
		
		groupMap.put("tradeplatform`G1", "tp-g1");
		groupMap.put("tradeplatform`G2", "tp-g2");
		groupMap.put("tradeplatform`G3", "tp-g3");
		
		groupMap.put("ump`G1", "Cart_ump");
		groupMap.put("ump`G2", "detail_ump");
		groupMap.put("ump`G3", "Noraml_ump");
		groupMap.put("ump`G4", "Tmall_ump");
		
		appToGroupMap.put("itemcenter", new String[]{"ic-L0-d","ic-L0-c","ic-L0-o","ic-L1","ic-L2","ic-bid"});
		appToGroupMap.put("sell", new String[]{"sell","sell-upload","sell_top"});
		appToGroupMap.put("tradeplatform", new String[]{"tp-g1","tp-g1","tp-g1"});
		appToGroupMap.put("ump", new String[]{"Cart_ump","detail_ump","Noraml_ump","Tmall_ump"});
	}
	
	private static CspCacheTBHostInfos cache = new CspCacheTBHostInfos();
	
	private CspCacheTBHostInfos(){
		syncHostInfo();
	}
	
	public static CspCacheTBHostInfos get(){
		return cache;
	}
	
	public synchronized int[] getHostType(String opsName) {
		Map<String,HostPo> map = getHostInfoMapByOpsName(opsName);
		if (map == null) {
			return new int[] { 0, 0 };
		}
		int v = 0;
		int s = 0;
		for (Map.Entry<String,HostPo> po : map.entrySet()) {
			if ("vm_server".equals(po.getValue().getManifest())) {
				s++;
			} else {
				v++;
			}
		}
		return new int[] { s, v };
	}
	
	/**
	 * 通过 ip 获取机器对象，可能存在为空
	 * @param ip
	 * @return 
	 */
	public HostPo getHostInfoByIp(String ip){
		isNeedUpdate();
		return hostIpMap.get(ip);
	}
	/**
	 * 是否需要更新
	 */
	private void isNeedUpdate(){
		timerLock.lock();
		try{
			if(isRolling()){
				syncHostInfo();
			}
		}finally{
			timerLock.unlock();
		}
	}
	
	/**
	 * 获得实体机的虚拟机列表
	 * 
	 * @param parentHost
	 * @return
	 */
	public  List<HostPo> getParentHostList(String parentHost){
		isNeedUpdate();
		return parentHostListMap.get(parentHost);
	}
	/**
	 * 通过应用名称获取
	 * @param opsName
	 * @return
	 */
	public Map<String,HostPo> getHostInfoMapByOpsName(String opsName){
		isNeedUpdate();
		return appHostMap.get(opsName);
	}
	/**
	 * 返回应用所有机器
	 * @param opsName
	 * @return
	 */
	public List<HostPo> getHostInfoListByOpsName(String opsName) {
		isNeedUpdate();
		return appHostListMap.get(opsName);
	}
	
	
	public List<String> getIpsListByOpsName(String opsName) {
		timerLock.lock();
		try{
			if(isRolling()){
				syncHostInfo();
			}
		}finally{
			timerLock.unlock();
		}
		List<String> ipList = new ArrayList<String>();
		List<HostPo> list = appHostListMap.get(opsName);
		if(list != null){
			for (HostPo host : list) {
				ipList.add(host.getHostIp());
			}
		}
		return ipList;
	}
	
	
	
	/**
	 * 根据机器名称获取 机器对象
	 * @param hostName
	 * @return
	 */
	public HostPo getHostInfoByHostName(String hostName){
		isNeedUpdate();
		
		return hostNameMap.get(hostName);
	} 
	
	/**
	 * Map<机房，List<机器>>
	 * @param opsName
	 * @return
	 */
	public Map<String,List<HostPo>> getHostMapByRoom(String opsName){
		Map<String,List<HostPo>> map = new HashMap<String, List<HostPo>>();
    try {
      List<HostPo> list = getHostInfoListByOpsName(opsName);
      for (HostPo po : list) {
        List<HostPo> tmp = map.get(po.getHostSite().toUpperCase());
        if (tmp == null) {
          tmp = new ArrayList<HostPo>();
          map.put(po.getHostSite().toUpperCase(), tmp);
        }
        tmp.add(po);
      }
      return map;
    } catch (Exception e) {
      logger.error("", e);
    }
    return map;
	}
	
	
	private void syncHostInfo(){
		
		List<HostPo> list = HostAo.get().findAllSyncHostInfos();
		logger.info("开始同步机器列表 数量:"+list.size());
		if(list.size() <2000){//整个淘宝 java应用类的机器 目前 基本上超过了 这个数目,如果少于不做更新
			return ;
		}
		try{
			//从diamond拉去应用信息，<appName, map<groupname,list<ip>>>
			Map<String, Map<String, List<String>>> mainGroup = GroupManager.get().getGroupInfo();
			//<appName, map<ip,groupname>>
			Map<String, Map<String, String>> appIpGroupMap = new HashMap<String, Map<String, String>>();
			
			for(Entry<String, Map<String, List<String>>> entryMain : mainGroup.entrySet()) {
				String opsName = entryMain.getKey();
				if(!appIpGroupMap.containsKey(opsName)) {
					appIpGroupMap.put(opsName, new HashMap<String, String>());
				}
				Map<String, String> ipGroupMap = appIpGroupMap.get(opsName);
				Map<String, List<String>> ipGroup = entryMain.getValue();
				for(Entry<String, List<String>> entry : ipGroup.entrySet()) {
					String groupName = entry.getKey();
					String groupAppName = groupMap.get(opsName + "`" + groupName);
					if(groupAppName != null) {
						List<String> ipList = entry.getValue();
						for(String ip:ipList) {
							ipGroupMap.put(ip, groupAppName);
						}						
					}
				}
			}
			
			hostIpMap.clear();
			appHostMap.clear();
			appHostListMap.clear();
			parentHostListMap.clear();
			
			for(HostPo po:list){
				if (po.getState() == null || !po.getState().equalsIgnoreCase("working_online")) {
					continue;
				}
				String ip = po.getHostIp();
				if(ip	!= null){
					
					if(hostIpMap.get(ip)!=null){//
						if(po.getCpsVersion()!=-99){
							hostIpMap.put(ip, po);
						}
					}else{
						hostIpMap.put(ip, po);
					}
				}
					
				
				String hostname = po.getHostName();
				if(hostname != null){
					if(hostNameMap.get(hostname)!=null){//
						if(po.getCpsVersion()!=-99){
							hostNameMap.put(hostname, po);
						}
					}else{
						hostNameMap.put(hostname, po);
					}
				}
				
				
				String opsName = po.getOpsName();
				if(opsName != null){
					Map<String,HostPo> map = appHostMap.get(opsName);
					if(map ==null){
						map = new HashMap<String, HostPo>();
						appHostMap.put(opsName, map);
					}
					if(ip != null)
						map.put(ip, po);
					
					List<HostPo> hostList = appHostListMap.get(opsName);
					if (hostList == null) {
						hostList = new ArrayList<HostPo>();
						appHostListMap.put(opsName, hostList);
					}
					
					if (ip != null) {
						hostList.add(po);
					}
					
					Map<String, String> ipToGroupMap = appIpGroupMap.get(opsName);
					if(ipToGroupMap != null && ipToGroupMap.containsKey(ip)) {
						String appNameGroup = ipToGroupMap.get(ip);//分组对应的应用名
						Map<String,HostPo> mapGroup = appHostMap.get(appNameGroup);
						if(mapGroup ==null){
							mapGroup = new HashMap<String, HostPo>();
							appHostMap.put(appNameGroup, mapGroup);
						}
						if(ip != null)
							mapGroup.put(ip, po);
						List<HostPo> hostListGroup = appHostListMap.get(appNameGroup);
						if (hostListGroup == null) {
							hostListGroup = new ArrayList<HostPo>();
							appHostListMap.put(appNameGroup, hostListGroup);
						}
						if (ip != null) {
							hostListGroup.add(po);
						}
					}
				}
			}
			for(HostPo po:list){
				if (po.getState() == null || !po.getState().equalsIgnoreCase("working_online")) {
					continue;
				}
				if(po.isVirtualHost()){
					List<HostPo> hostList = parentHostListMap.get(po.getVmparent());
					if (hostList == null) {
						hostList = new ArrayList<HostPo>();
					}else{
						hostList = parentHostListMap.get(po.getVmparent());
					}

					hostList.add(po);
					parentHostListMap.put(po.getVmparent(), hostList);
				}
			}
		}catch (Exception e) {
			logger.error("开始同步机器列表异常", e);
			e.printStackTrace();
		}

    logger.info("开始同步机器列表结束");
	}
	
	
	private boolean isRolling() {
		long n = System.currentTimeMillis();
		if (n >= nextCheck) {
			
			logger.info("一小时同步 机器列表.....");
			
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(n);
			calendar.add(Calendar.HOUR_OF_DAY, 1);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			nextCheck = calendar.getTimeInMillis();
			return true;
		}
		return false;
	}
	
	public void printList() {
		logger.info("打印机器缓存信息");	
		for(String appName : appHostListMap.keySet()) {
			List<HostPo> appHostList = appHostListMap.get(appName);
			for(HostPo host : appHostList) {
				logger.info(appName + "\t" + host.getHostIp());	
			}
		}
		logger.info("打印机器缓存信息结束");	
	}
	
	public Map<String, String> getCopyOfGroupMap () {
		return new HashMap<String, String>(groupMap);
	}
	
	public Map<String, String[]> getCopyOfAppToGroupMap () {
		return new HashMap<String, String[]>(appToGroupMap);
	}
	public static void main(String[] strs) {
		CspCacheTBHostInfos.get().printList();
	}
}
