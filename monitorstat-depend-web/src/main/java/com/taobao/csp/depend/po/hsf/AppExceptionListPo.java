package com.taobao.csp.depend.po.hsf;

/**
 * 
 * @author zhongting.zy
 * 异常总数显示类
 */
public class AppExceptionListPo implements Comparable<AppExceptionListPo>{
	private String customerApp;
	private String keyname;
	private long allnum;
	
	public String getCustomerApp() {
		return customerApp;
	}
	public void setCustomerApp(String customerApp) {
		this.customerApp = customerApp;
	}
	public String getKeyname() {
		return keyname;
	}
	public void setKeyname(String keyname) {
		this.keyname = keyname;
	}
	public long getAllnum() {
		return allnum;
	}
	public void setAllnum(long allnum) {
		this.allnum = allnum;
	}
	@Override
	public int compareTo(AppExceptionListPo o) {
		if(o.getAllnum()<getAllnum()){
			return -1;
		}else if(o.getAllnum()>getAllnum()){
			return 1;
		}
		return 0;
	}
}
