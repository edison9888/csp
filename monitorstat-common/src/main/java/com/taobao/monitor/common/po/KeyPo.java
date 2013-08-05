
package com.taobao.monitor.common.po;
/**
 * 
 * @author xiaodu
 * @version 2010-5-18 ÏÂÎç04:22:40
 */
public class KeyPo {
	
	private int keyId;
	
	private String keyName;
	
	private String aliasName;
	
	private String keyType ;
	
	private String defaultconfig;
	
	private String feature;
	
	
	

	public int getKeyId() {
		return keyId;
	}

	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public String getDefaultconfig() {
		return defaultconfig;
	}

	public void setDefaultconfig(String defaultconfig) {
		this.defaultconfig = defaultconfig;
	}

	public String getFeature() {
		return feature;
	}

	public void setFeature(String feature) {
		this.feature = feature;
	}

	
	public boolean equals(Object obj) {
		
		if(obj instanceof KeyPo){
			KeyPo o = (KeyPo)obj;
			if(o.keyName.equals(this.keyName)){
				return true;
			}
		}
		
		return false;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}
	
	

}
