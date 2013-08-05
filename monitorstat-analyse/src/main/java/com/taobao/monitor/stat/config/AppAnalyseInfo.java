
package com.taobao.monitor.stat.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.taobao.monitor.common.po.HostPo;

/**
 * 
 * @author xiaodu
 * @version 2010-5-19 ÏÂÎç02:36:20
 */
public class AppAnalyseInfo {
	
	
	private String opsfreeName;
	
	
	private Set<String> analyseList = new HashSet<String>();
	
	private List<HostPo> hostInfoList = new ArrayList<HostPo>();
	
	private Set<String> remoteLogPath = new HashSet<String>();


	

	public List<HostPo> getHostInfoList() {
		return hostInfoList;
	}

	public void setHostInfoList(List<HostPo> hostInfoList) {
		this.hostInfoList = hostInfoList;
	}

	public String getOpsfreeName() {
		return opsfreeName;
	}

	public void setOpsfreeName(String opsfreeName) {
		this.opsfreeName = opsfreeName;
	}

	public Set<String> getAnalyseList() {
		return analyseList;
	}

	public void setAnalyseList(Set<String> analyseList) {
		this.analyseList = analyseList;
	}

	public Set<String> getRemoteLogPath() {
		return remoteLogPath;
	}

	public void setRemoteLogPath(Set<String> remoteLogPath) {
		this.remoteLogPath = remoteLogPath;
	}
	
	
	
	

}
