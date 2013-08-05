package com.taobao.csp.time.web.po;

import net.sf.json.JSONObject;

public class UserCustomTreeKeyPo {
	private String id;
	private String text;
	private String value;
	private boolean showcheck;
	private boolean complete;
	private boolean isexpand;
	private Integer checkstate;
	private boolean hasChildren;
	private UserCustomTreeKeyPo[] childNodes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isShowcheck() {
		return showcheck;
	}
	public void setShowcheck(boolean showcheck) {
		this.showcheck = showcheck;
	}
	public boolean isComplete() {
		return complete;
	}
	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	public boolean isIsexpand() {
		return isexpand;
	}
	public void setIsexpand(boolean isexpand) {
		this.isexpand = isexpand;
	}
	public Integer getCheckstate() {
		return checkstate;
	}
	public void setCheckstate(Integer checkstate) {
		this.checkstate = checkstate;
	}
	public boolean isHasChildren() {
		return hasChildren;
	}
	public void setHasChildren(boolean hasChildren) {
		this.hasChildren = hasChildren;
	}
	public UserCustomTreeKeyPo[] getChildNodes() {
		return childNodes;
	}
	public void setChildNodes(UserCustomTreeKeyPo[] childNodes) {
		this.childNodes = childNodes;
	}
	public static void main(String args[]){
		UserCustomTreeKeyPo po = new UserCustomTreeKeyPo();
		po.setCheckstate(0);
		po.setComplete(false);
		po.setHasChildren(false);
		po.setId("1");
		po.setIsexpand(false);
		po.setShowcheck(false);
		po.setText("just test");
		po.setValue("value test");
		UserCustomTreeKeyPo po2 = new UserCustomTreeKeyPo();
		po2.setCheckstate(0);
		po2.setComplete(false);
		po2.setHasChildren(false);
		po2.setId("1");
		po2.setIsexpand(false);
		po2.setShowcheck(false);
		po2.setText("just test");
		po2.setValue("value test");
		po2.setChildNodes(new UserCustomTreeKeyPo[]{po});
		JSONObject obj = JSONObject.fromObject(po2);
		
	}
}
