package com.taobao.sentinel.pull;

import java.util.Map;

/***
 * result data from client
 * @author youji.zj
 *
 */
public interface IResultPuller {
	
	public Map<String, String> whiteListInterfaceResultFromClient(String interfaceInfo);
	
	public Map<String, String> blackListInterfaceResultFromClient(String interfaceInfo);
	
	public Map<String, String> blackListAppResultFromClient();
	
	public Map<String, String> whiteListCustomerResultFromClient(String interfaceInfo);
	
	public Map<String, String> blackListCustomerResultFromClient(String interfaceInfo);
	
	public Map<String, String> flowControlAppResultFromClient();
	
	public Map<String, String> flowControlInterfaceResultFromClient(String interfaceInfo);
	
	public Map<String, String> flowControlAppResultFromClientQps();
	
	public Map<String, String> flowControlInterfaceResultFromClientQps(String interfaceInfo);
	
	public Map<String, String> flowControlDependencyResultFromClient(String interfaceInfo);
}
