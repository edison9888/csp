package com.taobao.monitor.web.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.web.ao.MonitorBaseLineAo;

/**
 * MonitorBaseLineAo和MonitorTimeAo的中间层。
 * 封装了一层接口
 * @author zhongting.zy
 *
 */
public class MiddleLayerForBaseLineAndMonitorTimeAo {
	/**
	 * 模拟MonitorTimeAo.get().findKeyValueByDate的方法实现。对时间做了一些处理
	 * @param appId
	 * @param keyId
	 * @param start
	 * @return
	 */
	public static Map<String, KeyValuePo> findKeyValueByDate(int appId, int keyId) {
		List<KeyValuePo> list = MonitorBaseLineAo.get().findKeyBaseValue(appId, keyId);
		
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		final Map<String, KeyValuePo> timeMap = new HashMap<String, KeyValuePo>();
		final String app_name = AppCache.get().getKey(appId).getAppName();
		for(KeyValuePo basePo: list) {
			Date date = basePo.getCollectTime();
			String time = parseLogFormatDate.format(date);
			timeMap.put(time, basePo);
			basePo.setAppName(app_name);
			basePo.setKeyId(keyId);
			basePo.setAppId(appId);
			//basePo.putValue(siteId, value),没有siteId，不过也没发现哪里有用
		}
		return timeMap;
	}
}
