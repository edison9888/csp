
package com.taobao.monitor.web.cache;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.taobao.monitor.web.ao.MonitorDistribFlowAo;

/**
 * 这个类主要是缓存着 流量分布中所有的应用名称
 * @author xiaodu
 * @version 2010-11-15 下午03:04:11
 */
public class CacheProviderCustomer {
	
	private CacheProviderCustomer(){}
	
	private static CacheProviderCustomer pc = new CacheProviderCustomer();
	
	public static CacheProviderCustomer get(){
		return pc;
	}
	
	private Set<String> provider = new HashSet<String>();
	
	private Set<String> customer = new HashSet<String>();
	
	public Set<String> getProviderAppName(){
		
		if(provider.size()==0){
			Set<String> providerSet = MonitorDistribFlowAo.get().findAllProviderAppName();
			provider.addAll(providerSet);
		}
		
		
		return provider;
	}
	
	
	public Set<String> getCustomerAppName(){
		
		if(customer.size()==0){
			Set<String> customerSet = MonitorDistribFlowAo.get().findAllCustomerAppName();
			customer.addAll(customerSet);
		}
		
		return customer;
	}
	
	
	public void reset(){
		provider.clear();
		customer.clear();
		Set<String> customerSet = MonitorDistribFlowAo.get().findAllCustomerAppName();
		Set<String> providerSet = MonitorDistribFlowAo.get().findAllProviderAppName();
		provider.addAll(providerSet);
		customerSet.addAll(customerSet);
		
	}
	
	
	

}
