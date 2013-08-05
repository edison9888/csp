
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
public class KeyDistribFlowBo implements Comparable<KeyDistribFlowBo>{
	
	private String keyName;
	
	private long callNum;
	
	private long preCallNum;
	
	// Map<机房,DistribFlowPo>
	private Map<String,DistribFlowPo> map = new HashMap<String, DistribFlowPo>();
	
	private Set<String> callThisMethodAppSet = new HashSet<String>();//调用这个方法的应用
	
	
	//这是添加按方法来排序信息显示时加的，增加ip来源的Map<cm名，ip的Set集合>
	private Map<String, Set<String>> cmIpNumMap = new HashMap<String, Set<String>>();
	//这是添加按方法来排序信息显示时加的，此key包含的所有app的Map<appName, AppDistribFlowBo>
	private Map<String, AppDistribFlowBo> appMap = new HashMap<String, AppDistribFlowBo>();
	
	
	public String getSimpleKeyName(){
		String[] keyNames = keyName.split("_");
		String _cn = keyNames[2];
		if(_cn.indexOf(":")>-1){
			_cn = _cn.substring(0,_cn.lastIndexOf(":"));
			_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length())+keyNames[2].substring(keyNames[2].lastIndexOf(":"),keyNames[2].length());
		}else{
			_cn = _cn.substring(_cn.lastIndexOf(".")+1,_cn.length());
		}
		
		return _cn+"_"+keyNames[3];
	}
	
	
	
	//返回 Map<机房，调用总数>
	public Map<String, Long> getCmCallMap() {

		Map<String, Long> cmCallsMap = new HashMap<String, Long>();

		for (Map.Entry<String, DistribFlowPo> entry : map.entrySet()) {
			Long num = cmCallsMap.get(entry.getKey());
			if (num == null) {
				cmCallsMap.put(entry.getKey(), entry.getValue().getCallNum());
			} else {
				cmCallsMap.put(entry.getKey(), entry.getValue().getCallNum() + num);
			}
		}
		return cmCallsMap;

	}

	//这是添加按方法来排序信息显示时加的，和getCmCallMap类似
	public Map<String, Long> getCmCallMap1() {

		Map<String, Long> cmCallsMap = new HashMap<String, Long>();

		for (Map.Entry<String, AppDistribFlowBo> appBoEntry : appMap.entrySet()) {

			for (Map.Entry<String, DistribFlowPo> entry : appBoEntry.getValue().getCmMap().entrySet()) {
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
	
	public List<AppDistribFlowBo> getSortAppDistribFlowBoList() {
		List<AppDistribFlowBo> appBoList = new ArrayList<AppDistribFlowBo>();
		appBoList.addAll(appMap.values());
		Collections.sort(appBoList);
		return appBoList;
	}
	
	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public long getCallNum() {
		return callNum;
	}

	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}

	public Map<String, DistribFlowPo> getMap() {
		return map;
	}

	public void setMap(Map<String, DistribFlowPo> map) {
		this.map = map;
	}
	
	
	public int compareTo(KeyDistribFlowBo o) {
		if(o.getCallNum() > this.getCallNum()){
			return 1;
		} else if(o.getCallNum() < this.getCallNum()){
			return -1;
		}
		return 0;
	}

	public Set<String> getCallThisMethodAppSet() {
		return callThisMethodAppSet;
	}

	public void setCallThisMethodAppSet(Set<String> callThisMethodAppSet) {
		this.callThisMethodAppSet = callThisMethodAppSet;
	}

	public long getPreCallNum() {
		return preCallNum;
	}

	public void setPreCallNum(long preCallNum) {
		this.preCallNum = preCallNum;
	}


	public Map<String, Set<String>> getCmIpNumMap() {
		return cmIpNumMap;
	}


	public void setCmIpNumMap(Map<String, Set<String>> cmIpNumMap) {
		this.cmIpNumMap = cmIpNumMap;
	}


	public Map<String, AppDistribFlowBo> getAppMap() {
		return appMap;
	}


	public void setAppMap(Map<String, AppDistribFlowBo> appMap) {
		this.appMap = appMap;
	}
	
	
	

}
