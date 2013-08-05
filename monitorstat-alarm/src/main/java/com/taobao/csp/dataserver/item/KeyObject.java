
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.item;

import java.io.Serializable;

/**
 * @author xiaodu
 *
 * ÏÂÎç9:18:11
 */
public class KeyObject implements Serializable{
	
	private static final long serialVersionUID = -8220240961265616248L;

	private String keyName;
	
	private String parentKeyName;
	
	private KeyScope scope;
	
	public KeyObject(){
		
	}
	
	public KeyObject(String keyname,KeyScope scope,String pkn){
		this.keyName = keyname;
		this.scope = scope;
		this.parentKeyName = pkn;
	}
	
	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	
	public KeyScope getScope() {
		return scope;
	}

	public void setScope(KeyScope scope) {
		this.scope = scope;
	}


	public String getParentKeyName() {
		return parentKeyName;
	}

	public void setParentKeyName(String parentKeyName) {
		this.parentKeyName = parentKeyName;
	}

	@Override
	public String toString() {
		return keyName;
	}




	

}
