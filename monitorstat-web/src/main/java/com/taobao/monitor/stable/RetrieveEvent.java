
package com.taobao.monitor.stable;


/**
 * 检查监控问题事件
 * @author xiaodu
 * @version 2011-6-15 下午05:17:42
 */
public interface RetrieveEvent {
	
	
	public void retrieve(int appId,RetrieveCallBack call);

}
