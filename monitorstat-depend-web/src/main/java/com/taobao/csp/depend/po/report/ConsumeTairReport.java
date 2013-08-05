package com.taobao.csp.depend.po.report;

public class ConsumeTairReport implements Comparable<ConsumeTairReport> {

	private String tair_group_name;
	private float rush_time_qps;
	private float rush_time_rt;
	private long invoking_all_num;	
	private long exceptionNum;
	
	
	public String getTair_group_name() {
		return tair_group_name;
	}

	public void setTair_group_name(String tair_group_name) {
		this.tair_group_name = tair_group_name;
	}


	public float getRush_time_qps() {
		return rush_time_qps;
	}


	public void setRush_time_qps(float rush_time_qps) {
		this.rush_time_qps = rush_time_qps;
	}


	public float getRush_time_rt() {
		return rush_time_rt;
	}


	public void setRush_time_rt(float rush_time_rt) {
		this.rush_time_rt = rush_time_rt;
	}


	public long getInvoking_all_num() {
		return invoking_all_num;
	}


	public void setInvoking_all_num(long invoking_all_num) {
		this.invoking_all_num = invoking_all_num;
	}


	public long getExceptionNum() {
		return exceptionNum;
	}

	public void setExceptionNum(long exceptionNum) {
		this.exceptionNum = exceptionNum;
	}

	@Override
	public int compareTo(ConsumeTairReport o) {
		if(o.getInvoking_all_num()<getInvoking_all_num()){
			return -1;
		}else if(o.getInvoking_all_num()>getInvoking_all_num()){
			return 1;
		}
		return 0;	
	}
}
