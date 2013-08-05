package com.taobao.monitor.common.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 接口2：返回url或key对应的子接口方法的相关信息
 * @author zhongting.zy
 *
 */
public class EagleeyeChildKeyListPo {
	private String appName;
	private String keyName;

	private long failCallNum;
	private long faliRt;
	
	private long totalCallNum;
	private long rt;
	
	private List<EagleeyeChildKeyListPo> topo = new ArrayList<EagleeyeChildKeyListPo>();

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public long getFailCallNum() {
		return failCallNum;
	}

	public void setFailCallNum(long failCallNum) {
		this.failCallNum = failCallNum;
	}

	public long getFaliRt() {
		return faliRt;
	}

	public void setFaliRt(long faliRt) {
		this.faliRt = faliRt;
	}

	public List<EagleeyeChildKeyListPo> getTopo() {
		return topo;
	}

	public void setTopo(List<EagleeyeChildKeyListPo> topo) {
		this.topo = topo;
	}

	public long getTotalCallNum() {
		return totalCallNum;
	}

	public void setTotalCallNum(long totalCallNum) {
		this.totalCallNum = totalCallNum;
	}

	public long getRt() {
		return rt;
	}

	public void setRt(long rt) {
		this.rt = rt;
	}

}
