
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.configserver;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xiaodu
 *
 * ÏÂÎç8:07:48
 */
public class HsfRuleInfo {
	
	private String appName;
	
	private String interfaceName;
	
	private Set<String> methods = new  HashSet<String>();
	
	private String version;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Set<String> getMethods() {
		return methods;
	}

	public void setMethods(Set<String> methods) {
		this.methods = methods;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public int hashCode() {
		String key1 = appName+""+interfaceName;
		return key1.hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof HsfRuleInfo){
			HsfRuleInfo info = (HsfRuleInfo)obj;
			String key1 = appName+""+interfaceName;
			String key2 = info.getAppName()+""+info.getInterfaceName();
			
			if(key1.equals(key2)){
				return true;
			}
			
		}
		return false;
	}

	@Override
	public String toString() {
		return "{appName:"+appName+",interfaceName:"+interfaceName+",methods:"+methods.toString()+",version:"+version+"}";
	}
	
	
	

}
