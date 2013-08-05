
package com.taobao.monitor.alarm.n.event;

import com.taobao.monitor.alarm.n.AlarmContext;
import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.ao.MonitorBaseLineAo;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 ÏÂÎç03:52:05
 */
public class BaseLineAlarmEvent implements AlarmEvent{

	@Override
	public void onAlarm(AlarmContext context) {
		KeyValueBaseLinePo basePo = MonitorBaseLineAo.get().findKeyBaseValueByDate(context.getAppId(), context.getKeyId(), context.getRecentlyDate());
		if(basePo!=null){
			double bv = basePo.getBaseLineValue();
			if(context.getKeyId() == 176){
				bv = Arith.div(bv, 1000);
			}
			context.setBaseLineValue(bv+"");
		}	
	}

}
