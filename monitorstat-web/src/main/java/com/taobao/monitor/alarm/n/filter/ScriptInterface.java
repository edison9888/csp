
package com.taobao.monitor.alarm.n.filter;

import com.taobao.monitor.alarm.n.key.KeyJudgeEnum;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 обнГ04:40:39
 */
public interface ScriptInterface {
	
	
	public boolean valid(int appId,int keyId,int siteId,String value,long time,KeyJudgeEnum e);

}
