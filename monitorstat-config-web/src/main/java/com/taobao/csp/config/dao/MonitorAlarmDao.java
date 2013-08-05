/**
 * 
 */
package com.taobao.csp.config.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ibm.icu.text.SimpleDateFormat;
import com.taobao.csp.config.po.AlarmDataPo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;


/**
 * 
 * @author xiaodu
 * @version 2010-4-16 上午11:21:55
 */
/**
 * @author xiaodu
 * 
 */
public class MonitorAlarmDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(MonitorAlarmDao.class);
	
	

	/**
	 *根据alarm_aim 字段 和key name 的like ,查询出所有需要告警的key 信息
	 * 
	 * @param name
	 * @return
	 */
	public List<AlarmDataPo> findAllAlarmKeyByAimAndLikeName(Integer appId, String keyName, String alarm_aim) {

		String sql = "select a.alarm_type,a.id,a.app_id,a.key_id,a.alarm_define,a.alarm_aim,a.alarm_feature,"
				+ "k.key_value,p.app_name,a.key_type from ms_monitor_key_alarm_def a,ms_monitor_key k,ms_monitor_app p where a.key_id=k.key_id and a.app_id=p.app_id";

		List<Object> listParams = new ArrayList<Object>();

		if (appId != null ) {
			sql += " and p.app_id =?";
			listParams.add(appId);
		}
		if (keyName != null && !"".equals(keyName.trim())) {
			sql += " and k.key_value like ?";
			listParams.add("%" + keyName + "%");
		}

		if (alarm_aim != null && !"".equals(alarm_aim.trim())) {
			sql += " and a.alarm_aim = ?";
			listParams.add(alarm_aim);
		}

		final List<AlarmDataPo> keyList = new ArrayList<AlarmDataPo>();

		try {
			this.query(sql, listParams.toArray(), new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					AlarmDataPo po = new AlarmDataPo();
					po.setId(rs.getString("id"));
					po.setAppId(rs.getInt("app_id"));
					po.setAppName(rs.getString("app_name"));
					po.setKeyId(rs.getString("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAlarmDefine(rs.getString("alarm_define"));
					po.setAlarmAim(rs.getString("alarm_aim"));
					po.setAlarmFeature(rs.getString("alarm_feature"));
					po.setAlarmType(rs.getString("alarm_type"));
					po.setKeyType(rs.getInt("key_type"));
					keyList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keyList;
	}
	
	

}
