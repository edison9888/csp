package com.taobao.monitor.time.ao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.monitor.time.po.TimeDataInfo;
import com.taobao.monitor.time.util.DataUtil;
import com.taobao.monitor.time.util.TimeUtil;

public class TairKeyQueryAo {
	private static final Logger logger =  Logger.getLogger(TairKeyQueryAo.class);
	
	/**
	 * 通过appName和Tair监控的错误码
	 * @param appName
	 * @param serviceName
	 * @return Map<String, List<TimeDataInfo>>
	 */
	public Map<String, List<TimeDataInfo>> queryAppHSF(String appName, String serviceName){
		Map<String, List<TimeDataInfo>> childMap = new HashMap<String, List<TimeDataInfo>>();
		
		try {
			String key = KeyConstants.TAIR_CONSUMER + Constants.S_SEPERATOR + serviceName;
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, key);
			List<TimeDataInfo> timeList = new ArrayList<TimeDataInfo>();
			for (Map.Entry<String, Map<String, Map<String, Object>>> entry : map.entrySet()) {

				String fullName = entry.getKey().substring((appName).length() + 1);
				String childName = entry.getKey().substring((appName + Constants.S_SEPERATOR + key).length() + 1);
				Map<String, Map<String, Object>> timeMap = entry.getValue();
				
				for (Map.Entry<String, Map<String, Object>> p : timeMap.entrySet()) {
					TimeDataInfo info = new TimeDataInfo();
					info.setAppName(appName);
					info.setKeyName(childName);
					info.setFullKeyName(fullName);
					info.setMainProp( PropConstants.E_TIMES );

					String time = p.getKey();

					Map<String, Object> m = p.getValue();
					info.setFtime(TimeUtil.formatMillisTime(Long.parseLong(time), "HH:mm"));
					info.getOriginalPropertyMap().putAll(m);
					info.setMainValue(DataUtil.transformDouble(m.get( PropConstants.E_TIMES )));
					timeList.add(info);
				}
				
			}
			
			Collections.sort(timeList,new Comparator<TimeDataInfo>() {
				@Override
				public int compare(TimeDataInfo o1, TimeDataInfo o2) {
					if(o1.getTime()>o2.getTime()){
						return -1;
					}else if(o1.getTime()<o2.getTime()){
						return 1;
					}
					return 0;
				}
			});
			
			childMap.put(serviceName, timeList);
		} catch (Exception e) {
			logger.error("querykeyDataForHost 查询 appName：" + appName + " serviceName:" + serviceName, e);
		}
		return childMap;
	}
}