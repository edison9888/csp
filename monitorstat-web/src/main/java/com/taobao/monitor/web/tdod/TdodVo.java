package com.taobao.monitor.web.tdod;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author denghaichuan.pt
 * @version 2012-3-20
 */
public class TdodVo {
	private int ipKindCount;
	
	private long ipSumCount;
	
	private Map<String, Integer> ipCountMap = new HashMap<String, Integer>();

	public int getIpKindCount() {
		return ipKindCount;
	}

	public void setIpKindCount(int ipKindCount) {
		this.ipKindCount = ipKindCount;
	}

	public long getIpSumCount() {
		return ipSumCount;
	}

	public void setIpSumCount(long ipSumCount) {
		this.ipSumCount = ipSumCount;
	}

	public Map<String, Integer> getIpCountMap() {
		return ipCountMap;
	}

	public void setIpCountMap(Map<String, Integer> ipCountMap) {
		this.ipCountMap = ipCountMap;
	}
}
