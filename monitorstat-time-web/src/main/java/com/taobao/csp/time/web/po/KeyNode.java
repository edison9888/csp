package com.taobao.csp.time.web.po;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ����汾��KeyNode
 * @author zhongting.zy
 *
 */
public class KeyNode implements Serializable {

	private static final long serialVersionUID = 4951391388460776468L;
	
	private String keyName;
	private String id;
	private ArrayList<String> parentIds = new ArrayList<String>();
  private ArrayList<String> edeges = new ArrayList<String>(); // ΪӦ�õ�ʱ�򣬱ߵ�����
	
	private String nodeId;	//ҳ����Ⱦ��Div��Id
	
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNodeId() {
		return nodeId;
	}
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}
	public ArrayList<String> getParentIds() {
		return parentIds;
	}
	public void setParentIds(ArrayList<String> parentIds) {
		this.parentIds = parentIds;
	}

  public ArrayList<String> getEdeges() {
    return edeges;
  }

  public void setEdeges(ArrayList<String> edeges) {
    this.edeges = edeges;
  }
}
