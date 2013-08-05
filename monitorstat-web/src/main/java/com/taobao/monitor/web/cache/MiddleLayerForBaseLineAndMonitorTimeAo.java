package com.taobao.monitor.web.cache;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.web.ao.MonitorBaseLineAo;

/**
 * MonitorBaseLineAo��MonitorTimeAo���м�㡣
 * ��װ��һ��ӿ�
 * @author zhongting.zy
 *
 */
public class MiddleLayerForBaseLineAndMonitorTimeAo {
	/**
	 * ģ��MonitorTimeAo.get().findKeyValueByDate�ķ���ʵ�֡���ʱ������һЩ����
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
			//basePo.putValue(siteId, value),û��siteId������Ҳû������������
		}
		return timeMap;
	}
}
