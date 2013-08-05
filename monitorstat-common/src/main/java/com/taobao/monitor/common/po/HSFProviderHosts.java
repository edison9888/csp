package com.taobao.monitor.common.po;

import java.util.ArrayList;
import java.util.List;

public class HSFProviderHosts {
	public static final String stateOnline = "working_online";
	public static final String siteCm3 = "CM3";
	public static final String siteCm4 = "CM4";
	public static final String siteCm5 = "CM5";
	public static final String SPLIT = ";";
	public static final float percent = 0.8f;
	
	//提供HSF服务的某个应用的全部机器数量
	private int totalCnt;
	//提供HSF服务的某个应用的CM3全部机器数量
	private int cm3Cnt;
	//提供HSF服务的某个应用的CM4全部机器数量
	
    private int cm4Cnt;
	
	private int cm5Cnt;
	
	private List<String> cm3List = new ArrayList<String>();
	
	private List<String> cm4List = new ArrayList<String>();
	
	private List<String> cm5List = new ArrayList<String>();

	//系统名称
	private  String systemName;
	
	public int getTotalCnt() {
		return totalCnt;
	}

	public void setTotalCnt(int totalCnt) {
		this.totalCnt = totalCnt;
	}

	public int getCm3Cnt() {
		return cm3Cnt;
	}

	public void setCm3Cnt(int cm3Cnt) {
		this.cm3Cnt = cm3Cnt;
	}

	public int getCm4Cnt() {
		return cm4Cnt;
	}

	public void setCm4Cnt(int cm4Cnt) {
		this.cm4Cnt = cm4Cnt;
	}

	public int getCm5Cnt() {
		return cm5Cnt;
	}

	public void setCm5Cnt(int cm5Cnt) {
		this.cm5Cnt = cm5Cnt;
	}

	public List<String> getCm3List() {
		return cm3List;
	}

	public void setCm3List(List<String> cm3List) {
		this.cm3List = cm3List;
	}

	public List<String> getCm4List() {
		return cm4List;
	}

	public void setCm4List(List<String> cm4List) {
		this.cm4List = cm4List;
	}


	public List<String> getCm5List() {
		return cm5List;
	}

	public void setCm5List(List<String> cm5List) {
		this.cm5List = cm5List;
	}

	public static String getStateonline() {
		return stateOnline;
	}

	public static String getSitecm3() {
		return siteCm3;
	}

	public static String getSitecm4() {
		return siteCm4;
	}

	public static String getSitecm5() {
		return siteCm5;
	}

	public String getSystemName() {
		return systemName;
	}

	public void setSystemName(String systemName) {
		this.systemName = systemName;
	}

	public HSFProviderHosts(int totalCnt, int cm3Cnt, int cm4Cnt, int cm5Cnt,
			List<String> cm3List, List<String> cm4List, List<String> cm5List,String systemName) {
		this.totalCnt = totalCnt;
		this.cm3Cnt = cm3Cnt;
		this.cm4Cnt = cm4Cnt;
		this.cm5Cnt = cm5Cnt;
		this.cm3List = cm3List;
		this.cm4List = cm4List;
		this.cm5List = cm5List;
		this.systemName = systemName;
	}


	@Override
	public String toString() {
		return "HSFProviderHosts [totalCnt=" + totalCnt + ", cm3Cnt=" + cm3Cnt
				+ ", cm4Cnt=" + cm4Cnt + ", cm5Cnt=" + cm5Cnt + ", cm3List="
				+ cm3List + ", cm4List=" + cm4List + ", cm5List=" + cm5List
				+ ", systemName=" + systemName + "]";
	}

	public  boolean compareIsLegal(HSFProviderHosts csHost){
		//比较csHost 的机器数量，不能少于全部机器数量的80%，比例可以设置,包括cm3和cm4的单独进行比较
		if(totalCnt * percent > csHost.getTotalCnt() ||
				cm3Cnt * percent > csHost.getCm3Cnt() ||
				cm4Cnt * percent > csHost.getCm4Cnt()){
			return false;
		}else{
			return true;
		}
	}
	
	public String getCm3(){
		return "[cm3Cnt=" + cm3Cnt + SPLIT + "cm3List=" + cm3List + "]";
	}
	
	public String getCm4(){
		return "[cm4Cnt=" + cm4Cnt  + SPLIT+ "cm4List=" + cm4List + "]";
	}
	
	public String getCm5(){
		return "[cm5Cnt=" + cm5Cnt  + SPLIT+ "cm5List=" + cm5List + "]";
	}
	
	public  List<String> getAllHost(){
		List<String> hostList =  new ArrayList<String>();
		hostList.addAll(cm3List);
		hostList.addAll(cm4List);
		hostList.addAll(cm5List);
		return hostList;
	}

}
