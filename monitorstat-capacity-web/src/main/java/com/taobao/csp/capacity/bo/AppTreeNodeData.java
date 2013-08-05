package com.taobao.csp.capacity.bo;

import java.util.UUID;

public class AppTreeNodeData {
	
	private String id;
	
	private String appName;
	
	private long totalProvieNum;
	
	private String invokeAppName;
	
	private long invokeNum;
	
	public AppTreeNodeData(String appName, long totalProvieNum, String invokeAppName, long invokeNum) {
		this.id = UUID.randomUUID().toString();
		this.appName = appName;
		this.invokeAppName = invokeAppName;
		this.invokeNum = invokeNum;
		this.totalProvieNum = totalProvieNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getInvokeAppName() {
		return invokeAppName;
	}

	public void setInvokeAppName(String invokeAppName) {
		this.invokeAppName = invokeAppName;
	}

	public long getInvokeNum() {
		return invokeNum;
	}

	public void setInvokeNum(long invokeNum) {
		this.invokeNum = invokeNum;
	}
	
	public long getTotalProvieNum() {
		return totalProvieNum;
	}

	public void setTotalProvieNum(long totalProvieNum) {
		this.totalProvieNum = totalProvieNum;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof AppTreeNodeData)) return false;
		
		AppTreeNodeData po = (AppTreeNodeData)object;
		if (((po.getAppName() == null && this.getAppName() == null) || po.getAppName().equals(this.getAppName())) 
				&& ((po.getId() == null && this.getId() == null) ||  (po.getId().equals(this.getId())))
				&& ((po.getInvokeAppName() == null && this.getInvokeAppName() == null) || (po.getInvokeAppName().equals(this.getInvokeAppName())))) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + id.hashCode();
		result = 37 * result + appName.hashCode();
		
		return result;
	}
}
