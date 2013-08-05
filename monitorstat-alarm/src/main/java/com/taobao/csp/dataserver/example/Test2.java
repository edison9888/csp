
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 *
 * ÏÂÎç8:19:31
 */
public class Test2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Map<String,HostPo> map111 = CspCacheTBHostInfos.get().getHostInfoMapByOpsName("detail");
		
		
		List<String> ips = new ArrayList<String>();
		for(Map.Entry<String,HostPo> entry:map111.entrySet()){
			ips.add(entry.getValue().getHostIp());
		}
		
		Map<String, Map<String, DataEntry>> map;
		try {
			map = QueryUtil.queryRecentlyHostRealTime("detail", KeyConstants.MBEAN+Constants.S_SEPERATOR+ KeyConstants.THREADPOOL, ips);
			for(Map.Entry<String, Map<String, DataEntry>> e:map.entrySet()){
				for( Map.Entry<String, DataEntry> ee:e.getValue().entrySet()){
					System.out.println(ee.getKey()+":"+ee.getValue().getValue()+"<br/>");
				}
			}
			System.out.println("ff");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}

}
