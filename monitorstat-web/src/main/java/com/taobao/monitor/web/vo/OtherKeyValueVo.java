
package com.taobao.monitor.web.vo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * ����ͼ���������� �ṹΪ ǰ׺_����_key 
 *  ���� ����Ϊ ִ�д�����ʱ���
 * @author xiaodu
 * @version 2010-6-3 ����01:12:14
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
