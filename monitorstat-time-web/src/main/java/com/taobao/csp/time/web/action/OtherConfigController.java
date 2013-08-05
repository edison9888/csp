
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.util.MonitorAppUtil;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.cache.AppInfoCache;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspNeedBaseline;
import com.taobao.monitor.common.util.CommonUtil;
import com.taobao.monitor.common.util.GroupManager;

/**
 * @author xiaodu
 *
 * 下午7:14:14
 */
@Controller
@RequestMapping("/other/otherconfig.do")
public class OtherConfigController {
	
	private static final Logger logger = Logger.getLogger(OtherConfigController.class);
	
	@Resource(name = "commonService")
	private CommonServiceInterface commonService;
	
	/**
	 * 通过查询缓存中的数据，确定什么key需要做基线, 总调用量必须要在1000 以上
	 *@author xiaodu
	 * @return
	 *TODO
	 */
	@RequestMapping(params = "method=findBaseKey")
	public void findBaseKey(HttpServletResponse response){
		
		int minCall = 1000;
		
		List<String> prefixStr = new ArrayList<String>();
		prefixStr.add("HSF");
		prefixStr.add("PV");
		prefixStr.add("JVMINFO");
		prefixStr.add("TOPINFO");
		
		KeyAo.get().deleteAllNeedBaseline();
		
		List<String> appList = MonitorAppUtil.getMonitorApps();
		for(String app:appList){
			try {
				response.getWriter().println("start app:"+app+" ！");
				response.flushBuffer();
			} catch (IOException e) {
			}
			AppInfoPo appInfo = AppInfoCache.getAppInfoByAppName(app);
			if(appInfo ==null){
				
				try {
					response.getWriter().println(" app:"+app+" is null in cache！");
					response.flushBuffer();
				} catch (IOException e) {
				}
				
				continue;
			}
			
			
			List<CspKeyInfo> keylist = KeyAo.get().findKeyListByAppId(appInfo.getAppId());
			target:
			for(CspKeyInfo key:keylist){
				for(String prefix:prefixStr){
					if(key.getKeyName().startsWith(prefix)){
						break;
					}
					continue target;
				}
				
				try{
					TimeDataInfo td = commonService.querySingleRecentlyKeyData(app, key.getKeyName(), "");
					Map<String, Object> pMap = td.getOriginalPropertyMap();
					for(Map.Entry<String, Object> entry:pMap.entrySet()){
						if(!(entry.getValue() instanceof String)){
							if(DataUtil.transformDouble(entry.getValue())>minCall){
								CspNeedBaseline need = new CspNeedBaseline();
								need.setAppName(app);
								need.setKeyName(key.getKeyName());
								need.setPropertyName(entry.getKey());
								KeyAo.get().addNeedBaseline(need);

								//add by zhongting,兼容有分组的应用
								Map<String, List<String>> groupMap = GroupManager.get().getGroupInfoByAppName(app);
								if(groupMap != null) {
									for(String groupName: groupMap.keySet()) {
										String combineApp = CommonUtil.combinAppNameAndGroupName(app, groupName);
										CspNeedBaseline needForGroup = new CspNeedBaseline();
										needForGroup.setAppName(combineApp);
										needForGroup.setKeyName(key.getKeyName());
										needForGroup.setPropertyName(entry.getKey());
										KeyAo.get().addNeedBaseline(needForGroup);
										logger.info("分组key的支持->" + String.format("appName=%s,keyName=%s,property=%s", combineApp,key.getKeyName(),entry.getKey()));
									}									
								}
							}
						}
					}
				}catch (Exception e) {
					try {
						e.printStackTrace(response.getWriter());
						response.flushBuffer();
					} catch (IOException e1) {
					}
				}
			}
		}
		try {
			response.getWriter().println("complete ！");
		} catch (IOException e) {
		}
		
	}

}
