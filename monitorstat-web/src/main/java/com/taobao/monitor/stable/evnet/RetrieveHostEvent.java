
package com.taobao.monitor.stable.evnet;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.HostAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.stable.Feature;
import com.taobao.monitor.stable.RetrieveCallBack;
import com.taobao.monitor.stable.RetrieveEvent;
import com.taobao.monitor.stable.feature.DefaultRetrieveFeature;


@Feature(features={DefaultRetrieveFeature.class})
public  class RetrieveHostEvent implements RetrieveEvent {
	public void retrieve(int appId,RetrieveCallBack call) {
		
		AppInfoPo app = AppInfoAo.get().findAppInfoById(appId);
		
		List<HostPo> list = HostAo.get().findTimeAppHost(appId);
		
		Set<String> set = new HashSet<String>();
		
		List<HostPo> opsList =CspCacheTBHostInfos.get().getHostInfoListByOpsName(app.getOpsName());
		for(HostPo po:opsList){
			set.add(po.getHostIp());
		}
		opsList = null;
		
		for(HostPo po:list){
			if(!set.contains(po.getHostIp())){
				call.call(appId,"应用:"+app.getAppName()+"-"+po.getHostIp()+" 在opsfree 上查询不到");
			}
		}
	}

}
