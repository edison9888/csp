
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.item;

import java.io.Serializable;


/**
 * @author xiaodu
 *
 * ÏÂÎç8:32:26
 */
public class ValueObject implements Serializable{
	
	private static final long serialVersionUID = 3431551028328991780L;

	private String name;	//properties name
	
	private Object value;	//property value
	
	private ValueOperate operate;
	
	public ValueObject(){
		
	}
	
	
	public ValueObject(String name,Object value,ValueOperate operate){
		this.name = name;
		this.value = value;
		this.operate = operate;
	}
	
	
	public void handle(ValueObject value){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public ValueOperate getOperate() {
		return operate;
	}

	public void setOperate(ValueOperate operate) {
		this.operate = operate;
	}



}
