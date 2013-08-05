package com.taobao.csp.depend.po;

import java.io.Serializable;

/**
 * 
 * @author zhongting.zy
 * Dracular ͼ���н�����Ϣ
 */
public class GraphNode implements Serializable {

	private static final long serialVersionUID = -8504328025689384425L;

	/**һЩ���Եľ������Ͱ������ο�{@link com.taobao.csp.depend.util.ConstantParameters}*/
	private String id;				//�ڵ�ID
	private String name;			//��ʾ�Ľڵ�����
	private String parentId;		//���ڵ��Id��Ŀǰ���ƻ�֧������
	private String[] childIds;		//�ֽڵ�Id
	private String dependType;		//Ӧ����״̬���������������٣����䣩
	private String state;			//����״̬����ǿ������δ��������������
	private String appType;			//Ӧ�ñ�������
	
	public GraphNode(String id, String name, String parentId, String[] childIds, 
			String dependType, String state, String appType) {
		this.id = id;
		this.name = name;
		this.parentId = parentId;
		this.childIds = childIds;
		this.dependType = dependType;
		this.state = state;
		this.appType = appType;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String[] getChildIds() {
		return childIds;
	}
	public void setChildIds(String[] childIds) {
		this.childIds = childIds;
	}
	public String getDependType() {
		return dependType;
	}
	public void setDependType(String dependType) {
		this.dependType = dependType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
}
