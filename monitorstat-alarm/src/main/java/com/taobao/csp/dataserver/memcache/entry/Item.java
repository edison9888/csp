package com.taobao.csp.dataserver.memcache.entry;

public interface Item {
	
	public void destroy();
	
	public void free();
	
	/**
	 * �п����ǿͻ��˵�ʱ��
	 * @return
	 */
	public long getRecentlyTime(); 
	
	/**
	 * ���������д���ʱ��
	 * @return
	 */
	public long getRecentlyAcceptTim(); 
}
