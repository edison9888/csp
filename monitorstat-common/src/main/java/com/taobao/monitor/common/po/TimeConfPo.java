package com.taobao.monitor.common.po;

/**
 * 实时的配置类
 * @author wuhaiqian.pt
 *
 */
public class TimeConfPo {

	private int confId;
	
	private int appId;
	
	private String className;
	
	private String filePath;
	
	private int analyseFrequency;
	
	//分割每行的 分隔符
	private String splitChar;

	//日志文件别名，从线上应用机器拉取过来的，防止有名称有一样
	private String aliasLogName;
	
	private String analyseFuture;// 配置一些而外的说明，数据将传入className 中，
	
	//这个是对文件获取是使用到的字段
	private String tailType;//主要是用于文件tail 读取是 ，是利用tail -n  还是tail -c
	
	
	private int analyseType;// 1表示读取文件   filePath就是存放日志  2 独立运行className 3
	
	private int obtainType;
	

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
	
	public int getAnalyseFrequency() {
		return analyseFrequency;
	}

	public void setAnalyseFrequency(int analyseFrequency) {
		this.analyseFrequency = analyseFrequency;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getAnalyseFuture() {
		return analyseFuture;
	}

	public void setAnalyseFuture(String analyseFuture) {
		this.analyseFuture = analyseFuture;
	}

	public String getTailType() {
		return tailType;
	}

	public void setTailType(String tailType) {
		this.tailType = tailType;
	}

	public int getAnalyseType() {
		return analyseType;
	}

	public void setAnalyseType(int analyseType) {
		this.analyseType = analyseType;
	}

	public int getConfId() {
		return confId;
	}

	public void setConfId(int confId) {
		this.confId = confId;
	}

	public int getObtainType() {
		return obtainType;
	}

	public void setObtainType(int obtainType) {
		this.obtainType = obtainType;
	}

	@Override
	public String toString() {
		return "TimeConfPo [confId=" + confId + ", appId=" + appId
				+ ", className=" + className + ", filePath=" + filePath
				+ ", analyseFrequency=" + analyseFrequency + ", splitChar="
				+ splitChar + ", aliasLogName=" + aliasLogName
				+ ", analyseFuture=" + analyseFuture + ", tailType=" + tailType
				+ ", analyseType=" + analyseType + ", obtainType=" + obtainType
				+ "]";
	}

	
	
}
