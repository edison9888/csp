package com.taobao.sentinel.pull;

/***
 * Object for display
 * @author youji.zj
 *
 */
public class DisplayObject {
	
	/*** common field ***/
	private int order;
	
	/*** use for ip find ***/
	private String ip;
	

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
