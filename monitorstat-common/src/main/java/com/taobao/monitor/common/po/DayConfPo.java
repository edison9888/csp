package com.taobao.monitor.common.po;

/**
 * 日报的配置类
 * @author wuhaiqian.pt
 *
 */
public class DayConfPo {

	private int confId;
	
	private int appId;
	
	private String className;
	
	private String filePath;
	
	//分割每行的 分隔符
	private String splitChar;
	
	//日志文件别名，从线上应用机器拉取过来的，防止有名称有一样
	private String aliasLogName;
	
	private String future;

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}



	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getSplitChar() {
		return splitChar;
	}

	public void setSplitChar(String splitChar) {
		this.splitChar = splitChar;
	}

	public String getAliasLogName() {
		return aliasLogName;
	}

	public void setAliasLogName(String aliasLogName) {
		this.aliasLogName = aliasLogName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getConfId() {
		return confId;
	}

	public void setConfId(int confId) {
		this.confId = confId;
	}

	public String getFuture() {
		return future;
	}

	public void setFuture(String future) {
		this.future = future;
	}

	
}
