package com.taobao.sentinel.pull;

import com.taobao.sentinel.client.SentinelConfig;
import com.taobao.sentinel.util.LocalUtil;

/***
 * generate config query url 
 * @author youji.zj
 *
 */
public class UrlQueryGenerator {
	
	public static String generateWhiteListInterfaceConfigQuery(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.LIST_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=white_list_interface&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateBlackListInterfaceConfigQuery(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.LIST_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=black_list_interface&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateBlackListAppConfigQuery(String appName, String ip) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.LIST_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=black_list_app";
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateWhiteListCustomerConfigQuery(String appName, String ip, String keyInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.LIST_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=white_list_customer&key=" + keyInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateBlackListCustomerConfigQuery(String appName, String ip, String keyInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.LIST_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=black_list_customer&key=" + keyInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlInterfaceConfigQuery(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=flow_control_interface&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlAppConfigQuery(String appName, String ip) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=flow_control_app";
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlDependencyConfigQuery(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=flow_control_dependency&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlInterfaceConfigQueryQps(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=qps_interface&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlAppConfigQueryQps(String appName, String ip) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=query&poolName=qps_app";
		sb.append(parameter);
		
		return sb.toString();
	}


}
