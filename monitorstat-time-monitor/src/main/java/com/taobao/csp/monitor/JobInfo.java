
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor;

import java.util.HashSet;
import java.util.Set;

/**
 * @author xiaodu
 *
 * 下午8:13:47
 */
public class JobInfo {
	
	private String jobId;
	
	private String jobName;
	
	private String ip;
	
	private String site;
	
	private String sshUserName;
	
	private String sshPassword;
	
	private String appName;
	
	private String filepath;//文件路径  ，ssh指令 ，url
	
	private String collectorType;//采集类型 http,sshcommon,sshfile
	
	private char linebreaks; //文件使用的行分割符
	
	private long fileLineNum; //记录当前 文件记录字节数 在sshfile 模式中使用
	
	private int analyseFrequency;//执行频率 单位秒
	
	private Set<AnalyseInfo> analyseList = new HashSet<AnalyseInfo>();
	
	private String nodePath;
	
	public  JobInfo(String jobId){
		this.jobId = jobId;
	}
	
	
	public String getJobID(){
		
		return jobId;
	}
	

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public String getCollectorType() {
		return collectorType;
	}

	public void setCollectorType(String collectorType) {
		this.collectorType = collectorType;
	}

	public char getLinebreaks() {
		return linebreaks;
	}

	public void setLinebreaks(char linebreaks) {
		this.linebreaks = linebreaks;
	}


	public String getSshUserName() {
		return sshUserName;
	}


	public void setSshUserName(String sshUserName) {
		this.sshUserName = sshUserName;
	}


	public String getSshPassword() {
		return sshPassword;
	}


	public void setSshPassword(String sshPassword) {
		this.sshPassword = sshPassword;
	}


	public long getFileLineNum() {
		return fileLineNum;
	}


	public void setFileLineNum(long fileLineNum) {
		this.fileLineNum = fileLineNum;
	}


	public int getAnalyseFrequency() {
		return analyseFrequency;
	}


	public void setAnalyseFrequency(int analyseFrequency) {
		this.analyseFrequency = analyseFrequency;
	}


	public Set<AnalyseInfo> getAnalyseList() {
		return analyseList;
	}


	public void setAnalyseList(Set<AnalyseInfo> analyseList) {
		this.analyseList = analyseList;
	}


	public String getSite() {
		return site;
	}


	public void setSite(String site) {
		this.site = site;
	}


	public String getNodePath() {
		return nodePath;
	}


	public void setNodePath(String nodePath) {
		this.nodePath = nodePath;
	}


	public String getJobName() {
		return jobName;
	}


	public void setJobName(String jobName) {
		this.jobName = jobName;
	}



	
	
	

}
