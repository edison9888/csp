package com.taobao.csp.time.web.po;

import java.util.ArrayList;
import java.util.List;

/**
 * 按分组汇总的类
 * @author zhongting.zy
 *
 */
public class IndexMinuteTableMain {
	
	private int appId;
	private String appName;
	private String groupName;
	
	private int machines;
	private int capcityQps;
	private String ftime;
	
	
	List<IndexMinuteTable> list = null;

	public List<IndexMinuteTable> getList() {
		if(list == null)
			list = new ArrayList<IndexMinuteTable>();
		return list;
	}

	public void setList(List<IndexMinuteTable> list) {
		this.list = list;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getMachines() {
		return machines;
	}

	public void setMachines(int machines) {
		this.machines = machines;
	}

	public int getCapcityQps() {
		return capcityQps;
	}

	public void setCapcityQps(int capcityQps) {
		this.capcityQps = capcityQps;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}
}
