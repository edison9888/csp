package com.taobao.csp.time.web.po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 依赖数据树形结构的节点
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
	
	private String id;			//全路径标示符
	private String parentId;	//父亲节点的id
	
	private int xIndex = 1;		//记录节点在某行的位置
	private int iDepth = 1;		//深度
	private int iWidth = 1;		//宽度,防止除0错误
	private int iMaxWidth = 1;	//最大宽度
	
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
	 * 添加接点
	 * @param node
	 */
	public void addChildNode(KeyTreeNode node) {
		list.add(node);
	}

	/**
	 * 返回所有子节点
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
	
	//不加 get set 方法，防止打印list
//	public List<KeyTreeNode> getList() {
//		return list;
//	}
//	public void setList(List<KeyTreeNode> list) {
//		this.list = list;
//	}
}
