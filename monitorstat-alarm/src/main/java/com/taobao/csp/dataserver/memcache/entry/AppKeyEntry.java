
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.memcache.entry;

import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.memcache.core.Memcache;

/**
 * @author xiaodu
 *
 * обнГ2:26:16
 */
public class AppKeyEntry extends KeyEntry{

	/**
	 * @param appName
	 * @param kn
	 * @param keyScope
	 * @param memcache
	 */
	public AppKeyEntry(String appName, String kn,String fullName, KeyScope keyScope, Memcache memcache) {
		super(appName, kn,fullName, keyScope, memcache);
	}

}
