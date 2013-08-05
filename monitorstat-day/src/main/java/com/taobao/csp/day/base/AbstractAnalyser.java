package com.taobao.csp.day.base;

import java.util.List;

/***
 * 
 * @author youji.zj
 *
 * @param <T>
 */
public abstract class AbstractAnalyser {
	
	public AbstractAnalyser (String appName, HostInfo hostInfo, char lineSplit) {
		this.appName = appName;
		this.hostInfo = hostInfo;
		this.lineSplit = lineSplit;
	}
	
	/*** 应用名 ***/
	private String appName;
	
	/*** 机器信息 *///
	private HostInfo hostInfo;
	
	/*** 行的分隔符 ***/
	private char lineSplit;
	
	/*** 一次采集的残留串 ***/
	private String residue = "";
	
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public HostInfo getHostInfo() {
		return hostInfo;
	}

	public void setHostInfo(HostInfo hostInfo) {
		this.hostInfo = hostInfo;
	}

	public char getLineSplit() {
		return lineSplit;
	}

	public void setLineSplit(char lineSplit) {
		this.lineSplit = lineSplit;
	}

	public String getResidue() {
		return residue;
	}

	public void setResidue(String residue) {
		this.residue = residue;
	}
	
	public abstract  List<Log> analyse(String segment);
	
	public abstract void analyseOneLine(String line);
	
	public abstract boolean isCurrentLog(String segment);

}
