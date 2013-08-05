package com.taobao.csp.depend.po.tair;

import java.util.Date;

/**
 * 
 * @author zhongting.zy
 * 存储消费tair时的一些信息
 */
public class TairConsumeSummaryPo implements Comparable<TairConsumeSummaryPo>{
	private String app_name;
	private String app_group_name;
	private String tair_group_name;
	private String namespace;
	private float rush_time_qps;
	private float rush_time_rt;
	private long invoking_all_num;
	private String room_feature;
	private Date collect_time;
	
	private long precall;
	
	private String option = "same";
	
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getApp_group_name() {
		return app_group_name;
	}
	public void setApp_group_name(String app_group_name) {
		this.app_group_name = app_group_name;
	}
	public String getTair_group_name() {
		return tair_group_name;
	}
	public void setTair_group_name(String tair_group_name) {
		this.tair_group_name = tair_group_name;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public float getRush_time_qps() {
		return rush_time_qps;
	}
	public void setRush_time_qps(float rush_time_qps) {
		this.rush_time_qps = rush_time_qps;
	}
	public float getRush_time_rt() {
		return rush_time_rt;
	}
	public void setRush_time_rt(float rush_time_rt) {
		this.rush_time_rt = rush_time_rt;
	}
	public long getInvoking_all_num() {
		return invoking_all_num;
	}
	public void setInvoking_all_num(long invoking_all_num) {
		this.invoking_all_num = invoking_all_num;
	}
	public String getRoom_feature() {
		return room_feature;
	}
	public void setRoom_feature(String room_feature) {
		this.room_feature = room_feature;
	}
	public Date getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(Date collect_time) {
		this.collect_time = collect_time;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	
	public long getPrecall() {
		return precall;
	}
	public void setPrecall(long precall) {
		this.precall = precall;
	}
	@Override
	public int compareTo(TairConsumeSummaryPo o) {
		if(o.getInvoking_all_num()<getInvoking_all_num()){
			return -1;
		}else if(o.getInvoking_all_num()>getInvoking_all_num()){
			return 1;
		}
		return 0;	
	}
}
