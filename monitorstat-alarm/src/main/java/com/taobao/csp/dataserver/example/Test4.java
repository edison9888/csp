
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.example;

import com.taobao.csp.common.ZKClient;

/**
 * @author xiaodu
 *
 * ионГ9:57:12
 */
public class Test4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try{
			 //ZKClient.get().delete("/csp/data_cache_server/10.13.44.20:16512");
			 //ZKClient.get().delete("/csp/data_cache_server/10.232.135.198:16512");
			 //ZKClient.get().delete("/csp/data_cache_server/10.13.44.33:16512");
			 ZKClient.get().delete("/csp/data_cache_server/169.254.225.79:16512");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
