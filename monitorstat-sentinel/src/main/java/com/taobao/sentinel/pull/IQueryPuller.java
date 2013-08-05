package com.taobao.sentinel.pull;

import java.util.List;
import java.util.Map;

/***
 * configuration data feedback from client
 * @author youji.zj
 *
 */
public interface IQueryPuller {

	public List<String> whiteListInterfaceConfigFromClient(String interfaceInfo);
	
	public List<String> blackListInterfaceConfigFromClient(String interfaceInfo);
	
	public List<String> blackListAppConfigFromClient();
	
	public List<String> whiteListCustomerConfigFromClient(String interfaceInfo);
	
	public List<String> blackListCustomerConfigFromClient(String interfaceInfo);
	
	public Map<String, String> flowControlAppConfigFromClient();
	
	public Map<String, String> flowControlInterfaceConfigFromClient(String interfaceInfo);
	
	public Map<String, String> flowControlAppConfigFromClientQps();
	
	public Map<String, String> flowControlInterfaceConfigFromClientQps(String interfaceInfo);
	
	public Map<String, String> flowControlDependencyConfigFromClient(String interfaceInfo);
	
}
