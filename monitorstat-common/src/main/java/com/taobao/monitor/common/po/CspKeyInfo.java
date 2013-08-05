
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.po;

import org.apache.commons.lang.StringUtils;

/**
 * @author xiaodu
 *
 * 上午11:53:09
 */
public class CspKeyInfo {
	
	private int keyId;
	
	private String keyName;
	
	private String parentKeyName;
	
	private String aliasName;
	
	private String keyScope;
	
	private String keyDesc;
	//是否秒级别的key
	private boolean isSecond;
	
	private int keyLevel;
	

	public int getKeyLevel() {
		return keyLevel;
	}

	public void setKeyLevel(int keyLevel) {
		this.keyLevel = keyLevel;
	}

	public String getKeyDesc() {
		return keyDesc;
	}

	public void setKeyDesc(String keyDesc) {
		this.keyDesc = keyDesc;
	}

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


	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}


	public String getParentKeyName() {
		return parentKeyName;
	}

	public void setParentKeyName(String parentKeyName) {
		this.parentKeyName = parentKeyName;
	}

	public String getKeyScope() {
		return keyScope;
	}

	public void setKeyScope(String keyScope) {
		this.keyScope = keyScope;
	}

	/**
	 * 比较挫的实现
	 * @return
	 */
	public boolean isSecond() {
		if(StringUtils.isNotBlank(keyName) && keyName.startsWith("SPV")){
			isSecond= true;
		}
		return isSecond;
	}

	public void setSecond(boolean isSecond) {
		this.isSecond = isSecond;
	}


	
	
	

}
