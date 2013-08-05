package com.taobao.csp.time.cache;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import com.taobao.csp.common.ZKClient;

public class ShieldIpCache {
	
	private static final Logger logger = Logger.getLogger(ShieldIpCache.class);
	

	private static String ZK_MONITOR_SHIEDIP_ROOT = "/csp/monitor/shield_ip";

	private static ShieldIpCache shieldIpCache = new ShieldIpCache();

	private Set<String> set = Collections.synchronizedSet(new HashSet<String>());

	private ShieldIpCache() {
		findShieldIp();
	}

	
	
	/**
	 * 查看ZK_MONITOR_SHIEDIP_ROOT 节点下面的IP列表，并加入子节点更变Watcher
	 *@author xiaodu
	 *TODO
	 */
	private void findShieldIp() {
		try {
			set.clear();
			List<String> list = ZKClient.get().list(ZK_MONITOR_SHIEDIP_ROOT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {

					switch (event.getType()) {
					case NodeChildrenChanged:
						findShieldIp();
						;
						break;
					}
				}
			});
			set.addAll(list);
		} catch (Exception e) {
			logger.error("findShieldIp",e);
		}
	}

	public static ShieldIpCache get() {
		return shieldIpCache;
	}

	
	/**
	 * 判断这个ip是否在屏蔽列表中
	 *@author xiaodu
	 * @param ip
	 * @return
	 *TODO
	 */
	public boolean isShield(String ip) {
		return set.contains(ip);
	}

	/**
	 * 在ZK_MONITOR_SHIEDIP_ROOT 节点中加入一个需要屏蔽的ip
	 *@author xiaodu
	 * @param ip
	 *TODO
	 */
	public void addShieldIp(String ip) {
		try {
			ZKClient.get().mkdirPersistent(ZK_MONITOR_SHIEDIP_ROOT + "/" + ip);
		} catch (Exception e) {
			logger.error("addShieldIp",e);
		}
	}

	
	
	/**
	 * 删除ZK_MONITOR_SHIEDIP_ROOT 节点下面的ip
	 *@author xiaodu
	 * @param ip
	 *TODO
	 */
	public void deleteShieldIp(String ip) {
		try {
			ZKClient.get().delete(ZK_MONITOR_SHIEDIP_ROOT + "/" + ip);
		} catch (Exception e) {
			logger.error("addShieldIp",e);
		}
	}
	
	public List<String> getShieldIps(){
		
		try {
			List<String> list = ZKClient.get().list(ZK_MONITOR_SHIEDIP_ROOT);
			return list;
		}  catch (Exception e) {
			logger.error("getShieldIps",e);
		}
		return null;
	}
	
	
	public static void main(String[] args){
		
		ShieldIpCache.get().isShield("");
		
	}
	

}
