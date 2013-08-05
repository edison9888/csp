package com.taobao.csp.day.po;

/***
 * sph po¿‡
 * @author youji.zj
 * 
 * @version 1.0 2012-11-11
 *
 */
public class SphPo {
	
	private String id;
	
	private String appName;
	
	private String ip;
	
	private String blockKey;
	
	private String blockAction;
	
	private int bloackCount;
	
	private String collectTime;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getAppName() {
		return appName;
	}
	
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
	public String getIp() {
		return ip;
	}
	
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getBlockKey() {
		return blockKey;
	}
	
	public void setBlockKey(String blockKey) {
		this.blockKey = blockKey;
	}
	
	public String getBlockAction() {
		return blockAction;
	}
	
	public void setBlockAction(String blockAction) {
		this.blockAction = blockAction;
	}
	
	public int getBloackCount() {
		return bloackCount;
	}
	
	public void setBloackCount(int bloackCount) {
		this.bloackCount = bloackCount;
	}
	
	public String getCollectTime() {
		return collectTime;
	}
	
	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}
}
