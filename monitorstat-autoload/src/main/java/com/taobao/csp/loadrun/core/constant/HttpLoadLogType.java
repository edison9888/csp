
package com.taobao.csp.loadrun.core.constant;

import java.io.Serializable;

/**
 * 
 * @author xiaodu
 * @version 2011-7-29 ����04:05:08
 */
public enum HttpLoadLogType implements Serializable{
	
	apache("�Զ���ȡapache��־"),
	nginx("�Զ���ȡnginx��־"),
	self("�Զ���url"),
	scriptApache("�ű���ȡapache��־");
	
	public String logDesc;
	
	HttpLoadLogType(String logdesc){
		this.logDesc = logdesc;
	}

	public String getLogDesc() {
		return logDesc;
	}
	

}
