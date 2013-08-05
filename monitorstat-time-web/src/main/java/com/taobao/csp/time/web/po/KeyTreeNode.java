package com.taobao.csp.time.web.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * �����������νṹ�Ľڵ�
 * @author zhongting.zy
 *
 */
public class KeyTreeNode implements Serializable {

	private static final long serialVersionUID = -661546905507222477L;
	
	public KeyTreeNode() {
		
	}
	
	private String keyName;
	private String parentKey;
	private String[] childKey;
	
	private String id;			//ȫ·����ʾ��
	private String parentId;	//���׽ڵ��id
	
	private int xIndex = 1;		//��¼�ڵ���ĳ�е�λ��
	private int iDepth = 1;		//���
	private int iWidth = 1;		//���,��ֹ��0����
	private int iMaxWidth = 1;	//�����
	
	private List<KeyTreeNode> list = new ArrayList<KeyTreeNode>();
	
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public void setParentKey(String parentKey) {
		this.parentKey = parentKey;
	}
	public void setChildKey(String[] childKey) {
		this.childKey = childKey;
	}
	public String getKeyName() {
		return keyName;
	}
	public String getParentKey() {
		return parentKey;
	}
	public String[] getChildKey() {
		return childKey;
	}
	
	/**
	 * ��ӽӵ�
	 * @param node
	 */
	public void addChildNode(KeyTreeNode node) {
		list.add(node);
	}

	/**
	 * ���������ӽڵ�
	 * @return
	 */
	public List<KeyTreeNode> getChildList() {
		return list;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getiDepth() {
		return iDepth;
	}

	public void setiDepth(int iDepth) {
		this.iDepth = iDepth;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public int getiWidth() {
		return iWidth;
	}
	public void setiWidth(int iWidth) {
		this.iWidth = iWidth;
	}
	public int getiMaxWidth() {
		return iMaxWidth;
	}
	public void setiMaxWidth(int iMaxWidth) {
		this.iMaxWidth = iMaxWidth;
	}
	public int getxIndex() {
		return xIndex;
	}
	public void setxIndex(int xIndex) {
		this.xIndex = xIndex;
	}
	
	//���� get set ��������ֹ��ӡlist
//	public List<KeyTreeNode> getList() {
//		return list;
//	}
//	public void setList(List<KeyTreeNode> list) {
//		this.list = list;
//	}
}
