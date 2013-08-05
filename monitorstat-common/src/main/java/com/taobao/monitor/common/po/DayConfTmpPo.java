package com.taobao.monitor.common.po;

/**
 * 日报的配置类
 * @author wuhaiqian.pt
 *
 */
public class DayConfTmpPo {

	private int tmpId;
	
	private String className;
	
	private String filePath;
	
	//分割每行的 分隔符
	private String splitChar;
	
	//日志文件别名，从线上应用机器拉取过来的，防止有名称有一样
	private String aliasLogName;


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

	public int getTmpId() {
		return tmpId;
	}

	public void setTmpId(int tmpId) {
		this.tmpId = tmpId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	
	
}
