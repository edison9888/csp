
package com.taobao.monitor.alarm.n.key.action;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.alarm.n.key.KeyDefine;

/**
 * 
 * @author xiaodu
 * @version 2011-2-27 обнГ08:29:39
 */
public interface Judge {
	public Result judge(KeyDefine define,AlarmContext context);
}
