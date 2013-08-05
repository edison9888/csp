package com.taobao.csp.loadrun.core.fetch;

import java.io.Serializable;

public class UrlElement implements Serializable {

	private static final long serialVersionUID = 1L;

	private String address;
	
	private boolean danymicUrl;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isDanymicUrl() {
		return danymicUrl;
	}

	public void setDanymicUrl(boolean danymicUrl) {
		this.danymicUrl = danymicUrl;
	}
}
