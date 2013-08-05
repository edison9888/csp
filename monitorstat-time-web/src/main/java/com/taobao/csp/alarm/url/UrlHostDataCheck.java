
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.url;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 *
 * 下午3:17:03
 */
public class UrlHostDataCheck  extends BaseDataCheck {

	private CheckUrl checkurl;
	
	private String time;//HH:mm 
	
	private CommonServiceInterface commonService = null;
	
	private StringBuffer reportString = new StringBuffer();
	
	public UrlHostDataCheck(CheckUrl checkUrl,String ftime){
		this.checkurl = checkUrl;
		this.time = ftime;
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		commonService = (CommonServiceInterface)context.getBean("commonService");
		
	}
	
	
	@Override
	public boolean checking() {
		String appName = checkurl.getAppName();
		String url = checkurl.getUrl();
		Set<String> siteSet = checkurl.getSiteSet();
		Map<String,List<HostPo>> hostmap = CspCacheTBHostInfos.get().getHostMapByRoom(appName);
		String keyName = "PV`"+url;
		
		for(String site:siteSet){
			List<HostPo> list = hostmap.get(site);
			if(list != null&&list.size()>0){
				
				boolean result = false;
				
				for(int i=0;i<3&&i<list.size();i++){
					HostPo po = list.get(i);
					List<TimeDataInfo> datalist = commonService.querySingleKeyData(appName, keyName, PropConstants.E_TIMES,po.getHostIp());
					Map<String,TimeDataInfo> map = new HashMap<String, TimeDataInfo>();
					for(TimeDataInfo td:datalist){
						map.put(td.getFtime(), td);
					}
					
					TimeDataInfo td1 = map.get(time);
					
					 Map<String, Double> baseMap = BaseLineCache.get().getBaseLineBySite(appName, keyName, PropConstants.E_TIMES,site);
					if(baseMap != null){
						Double d1 = baseMap.get(time);
						if(d1!=null&&d1>100){
							double v1 = td1.getMainValue();
							if((rate(v1,d1) >0.1||rate(v1,d1) <-0.1)){
								reportString.append(site+":"+po.getHostIp()+"当前时间:["+time+"] 当前流量:["+v1+"] 基线数据:["+d1+"]"+DataUtil.scale(v1, d1));
							}else{
								result =true;
							}
						}
					}
				}
				
				if(!result){//这个机房的流量在上升 或下降
					return false;
				}
			}
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.alarm.url.BaseDataCheck#report()
	 */
	@Override
	public StringBuffer report() {
		return reportString;
	}


	/* (non-Javadoc)
	 * @see com.taobao.csp.alarm.url.BaseDataCheck#reportReferAffect()
	 */
	@Override
	public StringBuffer reportReferAffect() {
		return new StringBuffer();
	}

}
