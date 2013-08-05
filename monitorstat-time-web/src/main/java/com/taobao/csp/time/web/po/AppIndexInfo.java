package com.taobao.csp.time.web.po;

public class AppIndexInfo {
	boolean hsfProvider;
	boolean hsfConsumer;
	boolean pv;
	boolean tair;
	
	public AppIndexInfo(boolean hsfProvider, boolean hsfConsumer, boolean pv,
			boolean tair) {
		super();
		this.hsfProvider = hsfProvider;
		this.hsfConsumer = hsfConsumer;
		this.pv = pv;
		this.tair = tair;
	}
	
	public AppIndexInfo() {
		super();
	}

	public boolean isHsfProvider() {
		return hsfProvider;
	}
	public void setHsfProvider(boolean hsfProvider) {
		this.hsfProvider = hsfProvider;
	}
	public boolean isHsfConsumer() {
		return hsfConsumer;
	}
	public void setHsfConsumer(boolean hsfConsumer) {
		this.hsfConsumer = hsfConsumer;
	}
	public boolean isPv() {
		return pv;
	}
	public void setPv(boolean pv) {
		this.pv = pv;
	}
	public boolean isTair() {
		return tair;
	}
	public void setTair(boolean tair) {
		this.tair = tair;
	}
}
