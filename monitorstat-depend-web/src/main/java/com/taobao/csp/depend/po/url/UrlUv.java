
/**
 * monitorstat-depend-web
 */
package com.taobao.csp.depend.po.url;

import java.util.Date;

/**
 * @author xiaodu
 *
 * ÏÂÎç2:06:51
 */
public class UrlUv {
	
	private String url;
	
	private int ipv;
	
	private int uv;
	
	private String urlType;//url or domian
	
	private Date collectTime;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getIpv() {
		return ipv;
	}

	public void setIpv(int ipv) {
		this.ipv = ipv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public String getUrlType() {
		return urlType;
	}

	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	
	
	

}
