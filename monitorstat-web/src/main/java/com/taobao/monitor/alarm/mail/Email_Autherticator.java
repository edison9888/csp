package com.taobao.monitor.alarm.mail;

import javax.mail.PasswordAuthentication;

/***
 * 
 * @author youji.zj
 *
 */
class Email_Autherticator extends javax.mail.Authenticator {
	private String strUser;
	private String strPwd;

	public String getStrUser() {
		return strUser;
	}

	public void setStrUser(String strUser) {
		this.strUser = strUser;
	}

	public String getStrPwd() {
		return strPwd;
	}

	public void setStrPwd(String strPwd) {
		this.strPwd = strPwd;
	}

	
	public Email_Autherticator(String user, String password) {
		this.strUser = user;
		this.strPwd = password;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(strUser, strPwd);
	}
}

