package com.taobao.monitor.common.vo;

/**
 * 
 * @author xiaodu
 * @version 2010-9-26 下午05:28:30
 */
public class LoginUserPo {

	private int id;
	private String name;
	private String password = "123456";	
	private String permissionDesc;
	
	private String phone;	
	private String wangwang;
	private String group;   //接收告警应用
	private String sendPhoneFeature;
	private String sendWwFeature;
	private String mail;
	
	private String reportDesc; //接收报表
	
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPermissionDesc() {
		return permissionDesc;
	}
	public void setPermissionDesc(String permissionDesc) {
		this.permissionDesc = permissionDesc;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getWangwang() {
		return wangwang;
	}
	public void setWangwang(String wangwang) {
		this.wangwang = wangwang;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getSendPhoneFeature() {
		return sendPhoneFeature;
	}
	public void setSendPhoneFeature(String sendPhoneFeature) {
		this.sendPhoneFeature = sendPhoneFeature;
	}
	public String getSendWwFeature() {
		return sendWwFeature;
	}
	public void setSendWwFeature(String sendWwFeature) {
		this.sendWwFeature = sendWwFeature;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getReportDesc() {
		return reportDesc;
	}
	public void setReportDesc(String reportDesc) {
		this.reportDesc = reportDesc;
	}
	
	
	

}
