package com.taobao.www.arkclient.authen.entity;

import java.util.Date;

public class UserProfile {
	private String workid;
    private String email;
    private String domainuser;
     
    private String wangwang;
	private String secretid;
    private String secretkey;
    private String secretiv;
    private Date secretexp;
    private boolean isexpired;
    private int errorcode;
    
	public String getWorkid() {
		return workid;
	}
	public void setWorkid(String workid) {
		this.workid = workid;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getDomainuser() {
		return domainuser;
	}
	public void setDomainuser(String domainuser) {
		this.domainuser = domainuser;
	}
	public String getSecretid() {
		return secretid;
	}
	public void setSecretid(String secretid) {
		this.secretid = secretid;
	}
	public String getSecretkey() {
		return secretkey;
	}
	public void setSecretkey(String secretkey) {
		this.secretkey = secretkey;
	}
	public String getSecretiv() {
		return secretiv;
	}
	public void setSecretiv(String secretiv) {
		this.secretiv = secretiv;
	}
	public Date getSecretexp() {
		return secretexp;
	}
	public void setSecretexp(Date secretexp) {
		this.secretexp = secretexp;
	}
	public boolean isIsexpired() {
		return isexpired;
	}
	public void setIsexpired(boolean isexpired) {
		this.isexpired = isexpired;
	}
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}

	public String getWangwang() {
			return wangwang;
		}
	public void setWangwang(String wangwang) {
			this.wangwang = wangwang;
		}


}
