
package com.taobao.monitor.web.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 次视图对象是用于 结构为 前缀_类型_key 
 *  表现 数据为 执行次数和时间的
 * @author xiaodu
 * @version 2010-6-3 下午01:12:14
 */
public class OtherKeyValueVo {
	
	private String typeName;
	
	private Map<String,OtherHaBoLogRecord> keyMap = new HashMap<String, OtherHaBoLogRecord>();

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public Map<String, OtherHaBoLogRecord> getKeyMap() {
		return keyMap;
	}

	public void setKeyMap(Map<String, OtherHaBoLogRecord> keyMap) {
		this.keyMap = keyMap;
	}
	
	
	

}
