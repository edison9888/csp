package com.taobao.csp.capacity.tools;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.sf.json.JSONArray;

import org.quartz.JobDetail;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		test2();
		
		

	}
	
	public static void test2() {
//		String [] a = new String [] { "topats", "tvcrm", "thboss", "threport", "mapserver"};
		String [] a = new String [] { "tuiguang" };
		
		int cm3 = 0;
		int cm4 = 0;
//		CspSyncOpsHostInfos.isOnLine = false;
		for (String app : a) {
			List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(app);
			
			for (HostPo po : hosts) {
				System.out.println(po.getHostName());
//				 System.out.println(po.getHostSite());
				
				if (po.getHostSite().equals("cm3")) {
					cm3++;
				}
				
				if (po.getHostSite().equals("cm4")) {
					cm4++;
				}
			}
		}
		
//		System.out.println(cm3 + " " + cm4);
		
	}
	
	public static void test1() {
		List<Map<String,Object>> list = new ArrayList<Map<String, Object>>(); 
		
		Map<String,Object> map1 = new LinkedHashMap<String, Object>();
		map1.put("groupName", "NOMAL_IMS24G");
		map1.put("opsName", "ims");
		map1.put("single_load", 44429);
		map1.put("cluster_load", 3833477);
		map1.put("rooms", 2);
		map1.put("machine_num", 284);
		map1.put("single_capacity", 53000);
		
		Map<String,Object> map2 = new LinkedHashMap<String, Object>();
		map2.put("groupName", "ECLIENT_IMS48G");
		map2.put("opsName", "ims");
		map2.put("single_load", 34182);
		map2.put("cluster_load", 502490);
		map2.put("rooms", 2);
		map2.put("machine_num", 30);
		map2.put("single_capacity", 40000);
		
		Map<String,Object> map3 = new LinkedHashMap<String, Object>();
		map3.put("groupName", "NOMAL_IMS48G");
		map3.put("opsName", "ims");
		map3.put("single_load", 53967);
		map3.put("cluster_load", 1820919);
		map3.put("rooms", 2);
		map3.put("machine_num", 103);
		map3.put("single_capacity", 66000);
		
		Map<String,Object> map4 = new LinkedHashMap<String, Object>();
		map4.put("groupName", "ECLIENT_IMS24G");
		map4.put("opsName", "ims");
		map4.put("single_load", 25494);
		map4.put("cluster_load", 475179);
		map4.put("rooms", 2);
		map4.put("machine_num", 38);
		map4.put("single_capacity", 30000);
		
		list.add(map1);
		list.add(map2);
		list.add(map3);
		list.add(map4);
		
		System.out.println(JSONArray.fromObject(list).toString());
		
	}

}
