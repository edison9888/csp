package com.taobao.monitor.web.distrib;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 
 * @author xiaodu
 * @version 2010-11-8 下午04:49:25
 */
public class AppDistribFlowBo implements Comparable<AppDistribFlowBo> {

	private String appName;

	private long callNum;
	
	
	//Map<keyName,KeyDistribFlowBo>
	private Map<String, KeyDistribFlowBo> keyMap = new HashMap<String, KeyDistribFlowBo>();//
	private Map<String, Set<String>> cmIpNumMap = new HashMap<String, Set<String>>();

	private Set<String> callThisAppMethordSet = new HashSet<String>();//调用这个应用的方法的Set
	
	//这是添加按方法来排序信息显示时加的， Map<机房,DistribFlowPo>
	private Map<String,DistribFlowPo> cmMap = new HashMap<String, DistribFlowPo>();
	
	
	private double rate;
	
	private long preCallNum;

	public Map<String, Long> getCmCallMap() {

		Map<String, Long> cmCallsMap = new HashMap<String, Long>();

		for (Map.Entry<String, KeyDistribFlowBo> keyBoEntry : keyMap.entrySet()) {

			for (Map.Entry<String, DistribFlowPo> entry : keyBoEntry.getValue().getMap().entrySet()) {
				Long num = cmCallsMap.get(entry.getKey());
				if (num == null) {
					cmCallsMap.put(entry.getKey(), entry.getValue().getCallNum());
				} else {
					cmCallsMap.put(entry.getKey(), entry.getValue().getCallNum() + num);
				}
			}

		}
		return cmCallsMap;
	}

	
	//这是添加按方法来排序信息显示时加的，返回 Map<机房，调用总数>
	public Map<String, Long> getCmCallMap1() {

		Map<String, Long> cmCallsMap = new HashMap<String, Long>();

		for (Map.Entry<String, DistribFlowPo> entry : cmMap.entrySet()) {
			Long num = cmCallsMap.get(entry.getKey());
			if (num == null) {
				cmCallsMap.put(entry.getKey(), entry.getValue().getCallNum());
			} else {
				cmCallsMap.put(entry.getKey(), entry.getValue().getCallNum() + num);
			}
		}
		return cmCallsMap;

	}
	
	public List<KeyDistribFlowBo> getSortKeyDistribFlowBoList() {
		List<KeyDistribFlowBo> keyBoList = new ArrayList<KeyDistribFlowBo>();
		keyBoList.addAll(keyMap.values());
		Collections.sort(keyBoList);
		return keyBoList;
	}

	public Set<String> getMethodNameSet() {
		Set<String> keys = new HashSet<String>();

		for (Map.Entry<String, KeyDistribFlowBo> keyBoEntry : keyMap.entrySet()) {

			String keyName = keyBoEntry.getValue().getKeyName();
			String[] keyNames = keyName.split("_");
			keys.add(keyNames[2] + keyNames[3]);

		}
		return keys;

	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public long getCallNum() {
		return callNum;
	}

	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}

	public Map<String, KeyDistribFlowBo> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(Map<String, KeyDistribFlowBo> keyMap) {
		this.keyMap = keyMap;
	}

	public Map<String, Set<String>> getCmIpNumMap() {
		return cmIpNumMap;
	}

	public void setCmIpNumMap(Map<String, Set<String>> cmIpNumMap) {
		this.cmIpNumMap = cmIpNumMap;
	}

	
	public int compareTo(AppDistribFlowBo o) {
		if (o.getCallNum() > this.getCallNum()) {
			return 1;
		} else if (o.getCallNum() < this.getCallNum()) {
			return -1;
		}
		return 0;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public long getPreCallNum() {
		return preCallNum;
	}

	public void setPreCallNum(long preCallNum) {
		this.preCallNum = preCallNum;
	}

	public Set<String> getCallThisAppMethordSet() {
		return callThisAppMethordSet;
	}

	public void setCallThisAppMethordSet(Set<String> callThisAppMethordSet) {
		this.callThisAppMethordSet = callThisAppMethordSet;
	}

	public Map<String, DistribFlowPo> getCmMap() {
		return cmMap;
	}

	public void setCmMap(Map<String, DistribFlowPo> cmMap) {
		this.cmMap = cmMap;
	}

}
