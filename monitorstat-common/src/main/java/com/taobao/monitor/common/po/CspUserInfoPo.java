package com.taobao.monitor.common.po;

import java.io.Serializable;

public class CspUserInfoPo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7007954104589592482L;
	private Integer id;
	private String phone;
	private String wangwang;
	private String mail;
	private String phone_feature;
	private String wangwang_feature;
	private String permission_desc;
	private String accept_apps;

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getAccept_apps() {
		return accept_apps;
	}
	public void setAccept_apps(String accept_apps) {
		this.accept_apps = accept_apps;
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
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getPhone_feature() {
		return phone_feature;
	}
	public void setPhone_feature(String phone_feature) {
		this.phone_feature = phone_feature;
	}
	public String getWangwang_feature() {
		return wangwang_feature;
	}
	public void setWangwang_feature(String wangwang_feature) {
		this.wangwang_feature = wangwang_feature;
	}
	public String getPermission_desc() {
		return permission_desc;
	}
	public void setPermission_desc(String permission_desc) {
		this.permission_desc = permission_desc;
	}
}
