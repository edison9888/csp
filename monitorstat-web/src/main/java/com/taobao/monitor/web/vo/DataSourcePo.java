
package com.taobao.monitor.web.vo;
/**
 * 
 * @author xiaodu
 * @version 2010-4-15 ÉÏÎç10:13:59
 */
public class DataSourcePo {
	
	private String maxConnectionsInUseCount= "0";
	private String inUseConnectionCount= "0";
	private String availableConnectionCount= "0";
	private String connectionCount= "0";
	private String maxSize= "0";
	private String minSize= "0";
	public String getMaxConnectionsInUseCount() {
		return maxConnectionsInUseCount;
	}
	public void setMaxConnectionsInUseCount(String maxConnectionsInUseCount) {
		this.maxConnectionsInUseCount = maxConnectionsInUseCount;
	}
	public String getInUseConnectionCount() {
		return inUseConnectionCount;
	}
	public void setInUseConnectionCount(String inUseConnectionCount) {
		this.inUseConnectionCount = inUseConnectionCount;
	}
	public String getAvailableConnectionCount() {
		return availableConnectionCount;
	}
	public void setAvailableConnectionCount(String availableConnectionCount) {
		this.availableConnectionCount = availableConnectionCount;
	}
	public String getConnectionCount() {
		return connectionCount;
	}
	public void setConnectionCount(String connectionCount) {
		this.connectionCount = connectionCount;
	}
	public String getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(String maxSize) {
		this.maxSize = maxSize;
	}
	public String getMinSize() {
		return minSize;
	}
	public void setMinSize(String minSize) {
		this.minSize = minSize;
	}
	
	
	
	
	

}
