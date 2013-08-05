package com.taobao.csp.time.tair;


import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.taobao.monitor.common.po.CspUserInfoPo;

/**
 *  �������
 * @author root
 *
 */
public interface TairCacheService {
	
	/**
	 * ��cache�ж�ȡ����
	 * 
	 * @param key
	 * @param nameSpace
	 * @return
	 */
	public TairCacheResult getS(String key);
	
	/**
	 * ��cache��������ȡ����
	 * 
	 * @param key
	 * @param nameSpace
	 * @return
	 */

	/**
	 * ��cache�д������
	 * putS == putSession
	 * @param key
	 * @param value
	 * @param expireTime
	 * @param nameSpace
	 * @return
	 */
	public TairCacheResult putS(String key, Serializable value);

	/**
	 * ����KEY��namespaceʧЧcache
	 * 
	 * @param key
	 * @param namespace
	 * @return
	 */
	public TairCacheResult invalidateS(String key);
	
/**
 *��request�õ�mail��Ȼ���tair���� CspUserInfo����
 */
	public CspUserInfoPo getCspUserInfo(HttpServletRequest request);
}
