
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.entry;

import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.memcache.core.Memcache;

/**
 * @author xiaodu
 *
 * ÏÂÎç2:19:57
 */
public class IPKeyEntry extends KeyEntry{
	
	/**
	 * @param kn
	 * @param fullKn
	 * @param keyScope
	 * @param memcache
	 */
	public IPKeyEntry(String appName,String kn,String fullName, String ip, KeyScope keyScope, Memcache memcache) {
		super(appName, kn, fullName,keyScope, memcache);
		this.ip = ip;
	}
	
	
	private String ip;
	
	public String getIp() {
		return ip;
	}
	public String toString(){
		return super.toString()+",ip"+ip;
	}

}
