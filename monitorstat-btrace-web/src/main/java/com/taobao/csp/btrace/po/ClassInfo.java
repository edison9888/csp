package com.taobao.csp.btrace.po;
/**
 * 
 * @author zhongting.zy
 * @version 2011-10-27 ����14:23:40
 * ΪGrid��json��ʽ���ӵ�PO��
 */
public class ClassInfo {
	public ClassInfo(String tranformId, String className , boolean changed) {
		this.className = className;
		this.tranformId = tranformId;
		this.changed = changed;
	}
	private String tranformId;
	private String className;
	private boolean changed;				//�û�ע����࣬Զ��JVM�Ƿ��Ѿ�ע��
	
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