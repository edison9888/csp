package com.taobao.csp.btrace.po;
/**
 * 
 * @author zhongting.zy
 * @version 2011-10-27 下午14:23:40
 * 为Grid的json格式而加的PO类
 */
public class ClassInfo {
	public ClassInfo(String tranformId, String className , boolean changed) {
		this.className = className;
		this.tranformId = tranformId;
		this.changed = changed;
	}
	private String tranformId;
	private String className;
	private boolean changed;				//用户注入的类，远程JVM是否已经注入
	
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getTranformId() {
		return tranformId;
	}

	public void setTranformId(String tranformId) {
		this.tranformId = tranformId;
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}
}