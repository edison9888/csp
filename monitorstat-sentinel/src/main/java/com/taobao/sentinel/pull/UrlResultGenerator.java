package com.taobao.sentinel.pull;

import com.taobao.sentinel.client.SentinelConfig;
import com.taobao.sentinel.util.LocalUtil;

public class UrlResultGenerator {
	

	public static String generateWhiteListInterfaceResult(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=white_list_interface&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	

	public static String generateBlackListInterfaceResult(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=black_list_interface&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateBlackListAppResult(String appName, String ip) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=black_list_app";
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateWhiteListCustomerResult(String appName, String ip, String keyInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=white_list_customer&key=" + keyInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateBlackListCustomerResult(String appName, String ip, String keyInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=black_list_customer&key=" + keyInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlInterfaceResult(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=flow_control_interface&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlAppResult(String appName, String ip) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=flow_control_app";
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlDependencyResult(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=flow_control_dependency&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlInterfaceResultQps(String appName, String ip, String interfaceInfo) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=qps_interface&interface=" + interfaceInfo;
		sb.append(parameter);
		
		return sb.toString();
	}
	
	public static String generateFlowControlAppResultQps(String appName, String ip) {
		if (SentinelConfig.MOCK_URL) {
			return SentinelConfig.MAP_URL;
		}
		
		StringBuilder sb = new StringBuilder(LocalUtil.getDataUrl(appName, ip));
		
		String parameter = "?type=result&poolName=qps_app";
		sb.append(parameter);
		
		return sb.toString();
	}


}
