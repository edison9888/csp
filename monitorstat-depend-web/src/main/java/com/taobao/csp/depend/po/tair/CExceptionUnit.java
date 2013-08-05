package com.taobao.csp.depend.po.tair;

import java.util.HashMap;
import java.util.Map;

/**
 * Tair调用的Exception显示
 * @author zhongting.zy
 *
 */
public class CExceptionUnit implements Comparable<CExceptionUnit> {

	public CExceptionUnit(String exceptionName) {
		this.exceptionName = exceptionName;
	}
	
	public CExceptionUnit() {
		
	}
	
	private String exceptionName;
	private long total;
	private long preTotal;
	
	//key-value = sitename-value
	public Map<String, Long> machineData = new HashMap<String, Long>();
	public Map<String, Long> preMachineData = new HashMap<String, Long>();
	
	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(long total) {
		this.total = total;
	}

	public long getPreTotal() {
		return preTotal;
	}

	public void setPreTotal(long preTotal) {
		this.preTotal = preTotal;
	}

	@Override
	public int compareTo(CExceptionUnit o) {
		if(getTotal() < o.getTotal()) {
			return 1;
		} else if(getTotal() > o.getTotal()) {
			return -1;
		}
		return 0;
	}
}
