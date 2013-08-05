
/**
 * monitorstat-time-web
 */
package com.taobao.csp.alarm.url;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.cache.BaseLineCache;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.util.DataUtil;
import com.taobao.csp.time.web.po.TimeDataInfo;

/**
 * @author xiaodu
 *
 * 下午3:16:47
 */
public class UrlAppDataCheck extends BaseDataCheck {
	
	private CheckUrl checkurl;
	
	private String time;//HH:mm 
	
	private CommonServiceInterface commonService = null;
	
	private StringBuffer reportString = new StringBuffer();
	
	public UrlAppDataCheck(CheckUrl checkUrl,String ftime){
		this.checkurl = checkUrl;
		this.time = ftime;
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		commonService = (CommonServiceInterface)context.getBean("commonService");
		
	}

	public boolean checking() {
		
		String appName = checkurl.getAppName();
		String url = checkurl.getUrl();
		String keyName = "PV`"+url;
		
		List<TimeDataInfo> list = commonService.querySingleKeyData(appName, keyName, PropConstants.E_TIMES);
		
		Map<String,TimeDataInfo> map = new HashMap<String, TimeDataInfo>();
		for(TimeDataInfo td:list){
			map.put(td.getFtime(), td);
		}
		TimeDataInfo td1 = map.get(time);
		 Map<String, Double> baseMap = BaseLineCache.get().getBaseLine(appName, keyName, PropConstants.E_TIMES);
		if(baseMap != null){
			Double d1 = baseMap.get(time);
			if(d1!=null&&d1>100){
				double v1 = td1.getMainValue();
				if((rate(v1,d1) >0.15||rate(v1,d1) <-0.15)){
					reportString.append(url+"当前时间:["+time+"] 当前流量:["+v1+"] 基线数据:["+d1+"]"+DataUtil.scale(v1, d1));
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public StringBuffer report() {
		return reportString;
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.alarm.url.BaseDataCheck#reportReferAffect()
	 */
	@Override
	public StringBuffer reportReferAffect() {
		
		StringBuffer referAffect = new StringBuffer();
		
		String appName = checkurl.getAppName();
		Map<String,List<TimeDataInfo>> referMap = commonService.querykeyDataForChild(appName, KeyConstants.PV_REFER, PropConstants.E_TIMES);
		for(Map.Entry<String,List<TimeDataInfo>> entry:referMap.entrySet()){
			String referUrl = entry.getKey();
			List<TimeDataInfo> list = entry.getValue();
			Map<String,TimeDataInfo> map = new HashMap<String, TimeDataInfo>();
			for(TimeDataInfo td:list){
				map.put(td.getFtime(), td);
			}
			TimeDataInfo td1 = map.get(time);
			 Map<String, Double> baseMap = BaseLineCache.get().getBaseLine(appName, td1.getKeyName(), PropConstants.E_TIMES);
				if(baseMap != null){
					Double d1 = baseMap.get(time);
					if(d1!=null&&d1>1000){
						double v1 = td1.getMainValue();
						if((rate(v1,d1) >0.1||rate(v1,d1) <-0.1)){
							referAffect.append(referUrl+"当前时间:["+time+"] 当前流量:["+v1+"] 基线数据:["+d1+"]"+DataUtil.scale(v1, d1));
						}
					}
				}
		}
		return referAffect;
	}

}
