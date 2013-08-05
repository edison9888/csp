
package com.taobao.csp.loadrun.core.constant;

import java.io.Serializable;

/**
 * 
 * @author xiaodu
 * @version 2011-7-29 下午04:05:08
 */
public enum HttpLoadLogType implements Serializable{
	
	apache("自动读取apache日志"),
	nginx("自动读取nginx日志"),
	self("自定义url"),
	scriptApache("脚本读取apache日志");
	
	public String logDesc;
	
	HttpLoadLogType(String logdesc){
		this.logDesc = logdesc;
	}

	public String getLogDesc() {
		return logDesc;
	}
	

}
