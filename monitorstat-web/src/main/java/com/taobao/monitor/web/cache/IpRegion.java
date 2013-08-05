
/**
 * monitorstat-monitor
 */
package com.taobao.monitor.web.cache;

/**
 * @author xiaodu
 *
 * ÉÏÎç11:05:43
 */
public class IpRegion {
	
	private long startIp;
	
	private long endIp;
	
	private String network;
	
	private String province;
	
	private String city;

	public String getNetwork() {
		return network;
	}

	public void setNetwork(String network) {
		this.network = network;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public long getStartIp() {
		return startIp;
	}

	public void setStartIp(long startIp) {
		this.startIp = startIp;
	}

	public long getEndIp() {
		return endIp;
	}

	public void setEndIp(long endIp) {
		this.endIp = endIp;
	}
	
	

}
