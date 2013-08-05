
package com.taobao.monitor.stable.evnet;

import java.util.List;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.stable.Feature;
import com.taobao.monitor.stable.RetrieveCallBack;
import com.taobao.monitor.stable.RetrieveEvent;
import com.taobao.monitor.stable.feature.DefaultRetrieveFeature;

/**
 * 
 * @author xiaodu
 * @version 2011-6-16 下午02:26:29
 */
@Feature(features={DefaultRetrieveFeature.class})
public  class RetrieveOpsNameEvent implements RetrieveEvent {

	public void retrieve(int appId,RetrieveCallBack call) {
		
		AppInfoPo app = AppInfoAo.get().findAppInfoById(appId);
		
		List<HostPo> opsList = CspCacheTBHostInfos.get().getHostInfoListByOpsName( app.getOpsName());
		if(opsList.size()<1){
			call.call(appId,"应用:"+app.getAppName()+" 在opsfree 上查询不到 ip 列表");
		}
	}

}
