package com.taobao.www.entity;


@SuppressWarnings("serial")
public class PressureVo implements java.io.Serializable {
	
    
	private int pretime;
	
	private int reqinc;
	
	private int other;
	
	private int cycleTotle;
	
	private int appId;
	
	private String uName;
	
	private String uPass;
	
	public int getPretime() {
		return pretime;
	}
	public void setPretime(int pretime) {
		this.pretime = pretime;
	}
	public int getReqinc() {
		return reqinc;
	}
	public void setReqinc(int reqinc) {
		this.reqinc = reqinc;
	}
	public int getOther() {
		return other;
	}
	public void setOther(int other) {
		this.other = other;
	}
	public int getCycleTotle() {
		return cycleTotle;
	}
	public void setCycleTotle(int cycleTotle) {
		this.cycleTotle = cycleTotle;
	}
	public int getAppId() {
		return appId;
	}
	public void setAppId(int appId) {
		this.appId = appId;
	}
	
	public String getUName() {
		return uName;
	}
	public void setUName(String name) {
		uName = name;
	}
	public String getUPass() {
		return uPass;
	}
	public void setUPass(String pass) {
		uPass = pass;
	}
	public PressureVo(int pretime, int reqinc, int other, int cycleTotle,
			int appId) {
		super();
		this.pretime = pretime;
		this.reqinc = reqinc;
		this.other = other;
		this.cycleTotle = cycleTotle;
		this.appId = appId;
	}
	public PressureVo() {
		super();
	}
	public PressureVo(int pretime, int reqinc, int other, int cycleTotle,
			int appId, String uName, String uPass) {
		super();
		this.pretime = pretime;
		this.reqinc = reqinc;
		this.other = other;
		this.cycleTotle = cycleTotle;
		this.appId = appId;
		this.uName = uName;
		this.uPass = uPass;
	}
	 
	
	
	
	
	
}
