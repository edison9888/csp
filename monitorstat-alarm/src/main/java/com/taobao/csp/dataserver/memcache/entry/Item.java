package com.taobao.csp.dataserver.memcache.entry;

public interface Item {
	
	public void destroy();
	
	public void free();
	
	/**
	 * 有可能是客户端的时间
	 * @return
	 */
	public long getRecentlyTime(); 
	
	/**
	 * 最近有数据写入的时间
	 * @return
	 */
	public long getRecentlyAcceptTim(); 
}
