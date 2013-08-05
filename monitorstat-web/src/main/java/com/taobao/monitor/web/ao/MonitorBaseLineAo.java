package com.taobao.monitor.web.ao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.baseline.db.po.KeyValueBaseLinePo;
import com.taobao.monitor.web.core.dao.impl.MonitorBaseLineDao;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-19 上午11:36:00
 */
public class MonitorBaseLineAo {

	private static MonitorBaseLineAo ao = new MonitorBaseLineAo();
	private MonitorBaseLineDao dao = new MonitorBaseLineDao();

	private MonitorBaseLineAo() {		

	}

	public static MonitorBaseLineAo get() {
		return ao;
	}

	/**
	 * 根据keyId 取得基线表中的 MS_MONITOR_DATA_BASELINE
	 * 
	 * @param keyId
	 * @param appName
	 * @return
	 */
	public Map<String, KeyValueBaseLinePo> findKeyBaseValueByDate(int appId, int keyId) {
		return dao.findKeyBaseValueByDate(appId, keyId);
	}

	/**
	 * 添加基线数据
	 * 
	 * @param po
	 */
	public void addMonitorDateBaseLine(KeyValueBaseLinePo po) {
		dao.addMonitorDateBaseLine(po);
	}
	
	/**
	 * 添加基线数据
	 * 
	 * @param po
	 */
	public void addMonitorDateBaseLineByList(List<KeyValueBaseLinePo> list) {
		dao.addMonitorDateBaseLineByList(list);
	}

	/**
	 * 删除基线
	 * 
	 * @param appId
	 * @param keyId
	 */
	public void deleteAppKeyBaseLine(int appId, int keyId) {
		dao.deleteAppKeyBaseLine(appId, keyId);
	}
	
	
	/**
	 * 删除MS_MONITOR_DATA_BASELINE的所有记录
	 * 
	 */
	public void deleteAllBaseLine() {
		dao.deleteAllBaseLine();
	}

	/**
	 * 查询基线数据 根据时间点来获取
	 * 
	 * @param appId
	 * @param keyId
	 * @param dateList
	 * @return
	 */
	public KeyValueBaseLinePo findKeyBaseValueByDate(int appId, int keyId, Date date) {
		return dao.findKeyBaseValueByDate(appId, keyId, date);
	}
	
	/**
	 * 根据传入的时间间隔查询基线表
	 * @param appId
	 * @param keyId
	 * @param beforeDate
	 * @param currentDate
	 * @return
	 */
	public List<KeyValueBaseLinePo> findKeyValuePoByinterval(int appId, int keyId, Date beforeDate, Date currentDate) {
		return dao.findKeyValuePoByinterval(appId, keyId, beforeDate, currentDate);
	}
	
	/**
	 * 根据keyId 取得基线表中的 MS_MONITOR_DATA_BASELINE
	 * @param keyId
	 * @param appName
	 * @return
	 */
	public List<KeyValuePo> findKeyBaseValue(int appId, int keyId) {
		return dao.findKeyBaseValue(appId, keyId);
	}

}
