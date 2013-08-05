package com.taobao.monitor.common.po;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.util.Arith;

public class TairSumData {
	
	private String appName;
	
	private long appCallSum;
	
	private double rushQps;
	
	private double rushRt;
	
//	Map<String, List<TairNamespacePo>> appTairNamespaceListMap =  new HashMap<String, List<TairNamespacePo>>();
	
	List<TairNamespacePo> tairNamespaceList = new ArrayList<TairNamespacePo>();
	
//	public Map<String, List<TairNamespacePo>> getAppTairNamespaceListMap() {
//		return appTairNamespaceListMap;
//	}

	public List<TairNamespacePo> getTairNamespaceList() {
		return tairNamespaceList;
	}

	public void appTairNamespaceList(TairNamespacePo po) {
		tairNamespaceList.add(po);
	}

	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public long getAppCallSum() {
		return appCallSum;
	}


	public void addAppCallSum(long appCallSum) {
		this.appCallSum += appCallSum;
	}


	public double getRushQps() {
//		DecimalFormat fomat = 
//		DecimalFormat df= new DecimalFormat("0.00");
		return rushQps;
	}


	public void calculateRushQps(double rushQps) {
		this.rushQps = Arith.div(rushQps + this.rushQps, 2, 6);
	}


	public double getRushRt() {
		return rushRt;
	}


	public void calculateRushRt(double rushRt) {
		this.rushRt = Arith.div(rushRt + this.rushRt, 2, 6);
	}


}
