package com.taobao.csp.time.tair;


import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.taobao.monitor.common.po.CspUserInfoPo;

/**
 *  缓存管理
 * @author root
 *
 */
public interface TairCacheService {
	
	/**
	 * 从cache中读取内容
	 * 
	 * @param key
	 * @param nameSpace
	 * @return
	 */
	public TairCacheResult getS(String key);
	
	/**
	 * 从cache中批量读取内容
	 * 
	 * @param key
	 * @param nameSpace
	 * @return
	 */

	/**
	 * 向cache中存放内容
	 * putS == putSession
	 * @param key
	 * @param value
	 * @param expireTime
	 * @param nameSpace
	 * @return
	 */
	public TairCacheResult putS(String key, Serializable value);

	/**
	 * 根据KEY和namespace失效cache
	 * 
	 * @param key
	 * @param namespace
	 * @return
	 */
	public TairCacheResult invalidateS(String key);
	
/**
 *从request拿到mail，然后从tair中拿 CspUserInfo对象
 */
	public CspUserInfoPo getCspUserInfo(HttpServletRequest request);
}
