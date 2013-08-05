package com.taobao.csp.depend.po;

import java.io.Serializable;

/**
 * 
 * @author zhongting.zy
 * Dracular 图表中结点的信息
 */
public class GraphNode implements Serializable {

	private static final long serialVersionUID = -8504328025689384425L;

	/**一些属性的具体类型包括，参考{@link com.taobao.csp.depend.util.ConstantParameters}*/
	private String id;				//节点ID
	private String name;			//显示的节点名称
	private String parentId;		//父节点的Id，目前不计划支持数组
	private String[] childIds;		//字节点Id
	private String dependType;		//应用是状态，（如新增，减少，不变）
	private String state;			//依赖状态（如强、弱）未来可能新增类型
	private String appType;			//应用本身类型
	
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
