package com.taobao.www.arkclient.authen.entity;

import java.util.Date;

public class SecretKeyIV implements java.io.Serializable {
	private static final long serialVersionUID = 8093966064607470036L;
	private String secretkey;
	private String secretiv;
	private Date secretexp;
	private boolean isexpired;
	private int errorcode;

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
}
