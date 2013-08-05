
package com.taobao.monitor.stable.feature;

import com.taobao.monitor.alarm.transfer.WangwangTransfer;
import com.taobao.monitor.stable.RetrieveFeature;

/**
 * 
 * @author xiaodu
 * @version 2011-6-17 上午11:35:09
 */
public class DefaultRetrieveFeature implements RetrieveFeature{

	@Override
	public void doFeature(int appId, String msg) {
		//WangwangTransfer.getInstance().sendExtraMessage("小赌", "CSP问题扫描", msg);
		//WangwangTransfer.getInstance().sendExtraMessage("斩飞", "CSP问题扫描", msg);
		
		
		
	}

}
