package com.taobao.csp.capacity.tools;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.monitor.common.util.GroupManager;


public class TestIps {

/***
 * 计算ic各个分组的调用量
 * @param args
 * @throws Exception
 */
public static void main(String[] args) throws Exception {
		
		InputStream inputStream = TestIps.class.getClassLoader().getResourceAsStream("srcApp.txt");
		
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		
		
		Map<String, Long> pvM = new HashMap<String, Long>();
		String readString;
	
		
		while ((readString = bufferedReader.readLine()) != null) {
			
			

			String name = readString.trim();

			System.out.println(name.split(" ")[1]);
			pvM.put(name.split("\\s+")[1], Long.parseLong(name.split("\\s+")[3]));
		}
		
		Map<String, Long> groupM = new HashMap<String, Long>();
		List<HostPo> list = CspSyncOpsHostInfos.findOnlineHostByOpsName("itemcenter");
		
		for (Map.Entry<String, Long> entry1 : pvM.entrySet()) {
			String hostName = entry1.getKey();
			long pv = entry1.getValue();
			
			for (HostPo po : list) {
				if (po.getHostName().equals(hostName)) {
					String ip = po.getHostIp();
					String group = GroupManager.get().getGroupByAppAndIp("itemcenter", ip);
					
					if (groupM.containsKey(group)) {
						pv += groupM.get(group);
					}
					
					groupM.put(group, pv);
				}
			}
		}
		
		for (Map.Entry<String, Long> entry2 : groupM.entrySet()) {
			System.out.println(entry2.getKey() + ":" + entry2.getValue());
		}
		
		
	}

}
