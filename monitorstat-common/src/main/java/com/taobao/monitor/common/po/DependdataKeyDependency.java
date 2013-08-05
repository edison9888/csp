package com.taobao.monitor.common.po;

public class DependdataKeyDependency {
	private long id;
	private String curKeyame;
	private String parentKeynames;
	private String parentKeyIds;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getCurKeyame() {
		return curKeyame;
	}
	public void setCurKeyame(String curKeyame) {
		this.curKeyame = curKeyame;
	}
	public String getParentKeynames() {
		return parentKeynames;
	}
	public void setParentKeynames(String parentKeynames) {
		this.parentKeynames = parentKeynames;
	}
	public String getParentKeyIds() {
		return parentKeyIds;
	}
	public void setParentKeyIds(String parentKeyIds) {
		this.parentKeyIds = parentKeyIds;
	}
}
