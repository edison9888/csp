package com.taobao.sentinel.bo;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.sentinel.pull.HttpPullerFactory;
import com.taobao.sentinel.pull.IQueryPuller;
import com.taobao.sentinel.pull.IResultPuller;
import com.taobao.sentinel.pull.PullerFactory;

/***
 * business class
 * communicate with sentinel client
 * 
 * include 
 * 1. get effective configuration from client
 * 2. get result from client
 * 3. alter ip's flow control threshold
 * 4. add white list ip
 * 5. remove white list ip
 * 6. add black list ip
 * 7. remove black list ip
 * 
 * @author youji.zj
 *
 */
public class ClientInteractiveBo {
	
	public static Logger logger = Logger.getLogger(ClientInteractiveBo.class);
	
	/*** interface white list ***/
	public List<String> whiteListInterfaceConfigFromClient(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.whiteListInterfaceConfigFromClient(interfaceInfo);
	}
	
	public Map<String, String> whiteListInterfaceResultFromClient(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.whiteListInterfaceResultFromClient(interfaceInfo);
	}
	
	
	/*** interface black list ***/
	public List<String> blackListInterfaceConfigFromClient(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.blackListInterfaceConfigFromClient(interfaceInfo);
	}
	
	public Map<String, String> blackListInterfaceResultFromClient(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.blackListInterfaceResultFromClient(interfaceInfo);
	}
	
	
	/*** application black list ***/
	public List<String> blackListAppConfigFromClient(String appName, String ip) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.blackListAppConfigFromClient();
	}
	
	public Map<String, String> blackListAppResultFromClient(String appName, String ip) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.blackListAppResultFromClient();
	}
	
	
	/*** customization white list ***/
	public List<String> whiteListCustomerConfigFromClient(String appName, String ip, String customerInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.whiteListCustomerConfigFromClient(customerInfo);
	}
						   
	public Map<String, String> whiteListCustomerResultFromClient(String appName, String ip, String customerInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.whiteListCustomerResultFromClient(customerInfo);
	}
	
	
	/*** customization black list ***/
	public List<String> blackListCustomerConfigFromClient(String appName, String ip, String customerInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.blackListCustomerConfigFromClient(customerInfo);
	}

	public Map<String, String> blackListCustomerResultFromClient(String appName, String ip, String customerInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.blackListCustomerResultFromClient(customerInfo);
	}
	
	
	/*** flow control application ***/
	public Map<String, String> flowControlAppConfigFromClient(String appName, String ip) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.flowControlAppConfigFromClient();
	}
	
	public Map<String, String> flowControlAppResultFromClient(String appName, String ip) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.flowControlAppResultFromClient();
	}

	/*** flow control interface ***/
	public Map<String, String> flowControlInterfaceConfigFromClient(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.flowControlInterfaceConfigFromClient(interfaceInfo);
	}

	public Map<String, String> flowControlInterfaceResultFromClient(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.flowControlInterfaceResultFromClient(interfaceInfo);
	}
	
	/*** flow control application qps ***/
	public Map<String, String> flowControlAppConfigFromClientQps(String appName, String ip) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.flowControlAppConfigFromClientQps();
	}
	
	public Map<String, String> flowControlAppResultFromClientQps(String appName, String ip) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.flowControlAppResultFromClientQps();
	}

	/*** flow control interface ***/
	public Map<String, String> flowControlInterfaceConfigFromClientQps(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.flowControlInterfaceConfigFromClientQps(interfaceInfo);
	}

	public Map<String, String> flowControlInterfaceResultFromClientQps(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.flowControlInterfaceResultFromClientQps(interfaceInfo);
	}

	/*** flow control dependency ***/
	public Map<String, String> flowControlDependencyConfigFromClient(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IQueryPuller puller = factory.createQueryPuller(appName, ip);
		
		return puller.flowControlDependencyConfigFromClient(interfaceInfo);
	}
	
	public Map<String, String> flowControlDependencyResultFromClient(String appName, String ip, String interfaceInfo) {
		PullerFactory factory = new HttpPullerFactory();
		IResultPuller puller = factory.createResultPuller(appName, ip);
		
		return puller.flowControlDependencyResultFromClient(interfaceInfo);
	}


}
