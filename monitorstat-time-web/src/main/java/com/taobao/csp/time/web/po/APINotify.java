package com.taobao.csp.time.web.po;

public class APINotify {
	//E-times,C-TIME
	//{"E-times":"5752125","C-time":"600218","errormsg":""}
	private long eTimes;
	private long cTimes;
	private long date;
	private String errorMsg;
	public long geteTimes() {
		return eTimes;
	}
	public void seteTimes(long eTimes) {
		this.eTimes = eTimes;
	}
	public long getcTimes() {
		return cTimes;
	}
	public void setcTimes(long cTimes) {
		this.cTimes = cTimes;
	}
	public long getDate() {
		return date;
	}
	public void setDate(long date) {
		this.date = date;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
}
