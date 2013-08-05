package com.taobao.sentinel.pull;

import java.util.Map;

import org.apache.log4j.Logger;

public class HttpResultPuller extends BasePuller implements IResultPuller {

	public static Logger logger = Logger.getLogger(HttpResultPuller.class);

	public HttpResultPuller(String appName, String ip) {
		super(appName, ip);
	}
	
	@Override
	public Map<String, String> whiteListInterfaceResultFromClient(String interfaceInfo) {
		String urlAddr = UrlResultGenerator.generateWhiteListInterfaceResult(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> blackListInterfaceResultFromClient(String interfaceInfo) {
		String urlAddr = UrlResultGenerator.generateBlackListInterfaceResult(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> blackListAppResultFromClient() {
		String urlAddr = UrlResultGenerator.generateBlackListAppResult(this.getAppName(), this.getIp());
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> whiteListCustomerResultFromClient(String customerInfo) {
		String urlAddr = UrlResultGenerator.generateWhiteListCustomerResult(this.getAppName(), this.getIp(), customerInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> blackListCustomerResultFromClient(String customerInfo) {
		String urlAddr = UrlResultGenerator.generateBlackListCustomerResult(this.getAppName(), this.getIp(), customerInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlAppResultFromClient() {
		String urlAddr = UrlResultGenerator.generateFlowControlAppResult(this.getAppName(), this.getIp());
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlInterfaceResultFromClient(String interfaceInfo) {
		String urlAddr = UrlResultGenerator.generateFlowControlInterfaceResult(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlDependencyResultFromClient(String interfaceInfo) {
		String urlAddr = UrlResultGenerator.generateFlowControlDependencyResult(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlAppResultFromClientQps() {
		String urlAddr = UrlResultGenerator.generateFlowControlAppResultQps(this.getAppName(), this.getIp());
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlInterfaceResultFromClientQps(
			String interfaceInfo) {
		String urlAddr = UrlResultGenerator.generateFlowControlInterfaceResultQps(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

}
