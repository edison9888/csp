package com.taobao.csp.depend.po.tair;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author zhongting.zy
 * 存储csp_app_consume_tair_detail中的信息
 */
public class TairConsumeDetailPo implements Comparable<TairConsumeDetailPo>{
	private String app_name;
	private String action_type;
	private String app_host_ip;
	private String app_site_name;
	private String namespace;
	private long invoking_num;
	private long invoking_time;
	private String tair_version;
	private String tair_group_name;
	private Date collect_time;
	
	private long callNum;	//总调用量
	private long preCallNum;
	
	private String option = "same";//add sub same
	
	//记录所有的机房名称
	final public static Set<String> siteName = new TreeSet();
	public Map<String, Long> siteMap = new HashMap<String, Long>();
	
	public String getApp_name() {
		return app_name;
	}
	public void setApp_name(String app_name) {
		this.app_name = app_name;
	}
	public String getAction_type() {
		return action_type;
	}
	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}
	public String getApp_host_ip() {
		return app_host_ip;
	}
	public void setApp_host_ip(String app_host_ip) {
		this.app_host_ip = app_host_ip;
	}
	public String getApp_site_name() {
		return app_site_name;
	}
	public void setApp_site_name(String app_site_name) {
		this.app_site_name = app_site_name;
	}
	public String getNamespace() {
		return namespace;
	}
	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}
	public long getInvoking_num() {
		return invoking_num;
	}
	public void setInvoking_num(long invoking_num) {
		this.invoking_num = invoking_num;
	}
	public long getInvoking_time() {
		return invoking_time;
	}
	public void setInvoking_time(long invoking_time) {
		this.invoking_time = invoking_time;
	}
	public String getTair_version() {
		return tair_version;
	}
	public void setTair_version(String tair_version) {
		this.tair_version = tair_version;
	}
	public String getTair_group_name() {
		return tair_group_name;
	}
	public void setTair_group_name(String tair_group_name) {
		this.tair_group_name = tair_group_name;
	}
	public Date getCollect_time() {
		return collect_time;
	}
	public void setCollect_time(Date collect_time) {
		this.collect_time = collect_time;
	}
	public long getCallNum() {
		return callNum;
	}
	public void setCallNum(long callNum) {
		this.callNum = callNum;
	}
	public long getPreCallNum() {
		return preCallNum;
	}
	public void setPreCallNum(long preCallNum) {
		this.preCallNum = preCallNum;
	}
	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
	@Override
	public int compareTo(TairConsumeDetailPo o) {
		if(o.getCallNum() < getCallNum()){
			return -1;
		}else if(o.getCallNum() > getCallNum()){
			return 1;
		}
		return 0;	
	}
}
