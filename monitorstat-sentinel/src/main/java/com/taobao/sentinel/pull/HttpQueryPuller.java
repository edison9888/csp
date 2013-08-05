package com.taobao.sentinel.pull;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class HttpQueryPuller extends BasePuller implements IQueryPuller  {

	public static Logger logger = Logger.getLogger(HttpQueryPuller.class);

	public HttpQueryPuller(String appName, String ip) {
		super(appName, ip);
	}
	
	@Override
	public List<String> whiteListInterfaceConfigFromClient(String interfaceInfo) {
		String urlAddr = UrlQueryGenerator.generateWhiteListInterfaceConfigQuery(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToList(info);
	}

	@Override
	public List<String> blackListInterfaceConfigFromClient(String interfaceInfo) {
		String urlAddr = UrlQueryGenerator.generateBlackListInterfaceConfigQuery(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToList(info);
	}

	@Override
	public List<String> blackListAppConfigFromClient() {
		String urlAddr = UrlQueryGenerator.generateBlackListAppConfigQuery(this.getAppName(), this.getIp());
		String info = getClientInfo(urlAddr);

		return transferJsonToList(info);
	}

	@Override
	public List<String> whiteListCustomerConfigFromClient(String customerInfo) {
		String urlAddr = UrlQueryGenerator.generateWhiteListCustomerConfigQuery(this.getAppName(), this.getIp(), customerInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToList(info);
	}

	@Override
	public List<String> blackListCustomerConfigFromClient(String customerInfo) {
		String urlAddr = UrlQueryGenerator.generateBlackListCustomerConfigQuery(this.getAppName(), this.getIp(), customerInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToList(info);
	}

	@Override
	public Map<String, String> flowControlAppConfigFromClient() {
		String urlAddr = UrlQueryGenerator.generateFlowControlAppConfigQuery(this.getAppName(), this.getIp());
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlInterfaceConfigFromClient(String interfaceInfo) {
		String urlAddr = UrlQueryGenerator.generateFlowControlInterfaceConfigQuery(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlDependencyConfigFromClient(String interfaceInfo) {
		String urlAddr = UrlQueryGenerator.generateFlowControlDependencyConfigQuery(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlAppConfigFromClientQps() {
		String urlAddr = UrlQueryGenerator.generateFlowControlAppConfigQueryQps(this.getAppName(), this.getIp());
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

	@Override
	public Map<String, String> flowControlInterfaceConfigFromClientQps(
			String interfaceInfo) {
		String urlAddr = UrlQueryGenerator.generateFlowControlInterfaceConfigQueryQps(this.getAppName(), this.getIp(), interfaceInfo);
		String info = getClientInfo(urlAddr);
		
		return transferJsonToMap(info);
	}

}
