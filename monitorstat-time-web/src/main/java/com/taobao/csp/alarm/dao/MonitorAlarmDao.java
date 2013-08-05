package com.taobao.csp.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.po.AlarmSendPo;
import com.taobao.csp.alarm.po.UserAcceptInfo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class MonitorAlarmDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(MonitorAlarmDao.class);
public void addAlarmSend(AlarmSendPo info){
		
		String sql = "insert into csp_time_user_send(target_aim,app_id,alarm_msg,accept_time,alarm_type)values(?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[]{info.getTargetAim(),info.getAppId(),info.getAlarmMsg(),info.getAcceptTime(),info.getAlarmType()});
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
/**
 * 根据日期条件返回对应的告警信息集合
 */
	public List<AlarmSendPo> findAllAlarmSend(String alarmDateStart,String alarmDateEnd) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		String currentTime = sdf.format(Calendar.getInstance().getTime());
		alarmDateStart = alarmDateStart + " " + currentTime;
		alarmDateEnd = alarmDateEnd + " " + currentTime; 
		
		String sql = "SELECT target_aim, app_id,alarm_type FROM csp_time_user_send WHERE accept_time > ? and accept_time < ?";
		final List<AlarmSendPo> list = new ArrayList<AlarmSendPo>();
		try {

			this.query(sql, new Object[] { alarmDateStart, alarmDateEnd },new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AlarmSendPo po = new AlarmSendPo();
					String targetAim = rs.getString("target_aim");
					int appId = rs.getInt("app_id");
					String alarmType = rs.getString("alarm_type");
					po.setAppId(appId);
					po.setAlarmType(alarmType);
					po.setTargetAim(targetAim);
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	/**
	 * 根据日期条件返回对应的告警信息集合
	 */
		public List<UserAcceptInfo> findAllUserAcceptMsg(String alarmDateStart,String alarmDateEnd) {
			
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String currentTime = sdf.format(Calendar.getInstance().getTime());
			alarmDateStart = alarmDateStart + " " + currentTime;
			alarmDateEnd = alarmDateEnd + " " + currentTime; 
			
			String sql = "SELECT user_id, app_id,alarm_type,key_name FROM csp_time_user_accept WHERE accept_time > ? and accept_time < ?";
			final List<UserAcceptInfo> list = new ArrayList<UserAcceptInfo>();
			try {

				this.query(sql, new Object[] { alarmDateStart, alarmDateEnd },new SqlCallBack() {
					public void readerRows(ResultSet rs) throws Exception {
						UserAcceptInfo po = new UserAcceptInfo();
						int userId = rs.getInt("user_id");
						int appId = rs.getInt("app_id");
						String keyName = rs.getString("key_name");
						String alarmType = rs.getString("alarm_type");
						po.setUserId(userId);
						po.setAppId(appId);
						po.setKeyName(keyName);
						po.setAlarmType(alarmType);
						list.add(po);
					}
				});
			} catch (Exception e) {
				logger.error("", e);
			}
			return list;
		}
		public void addUserAcceptMsg(UserAcceptInfo info){
			
			String sql = "insert into csp_time_user_accept(user_id,app_id,key_name,alarm_msg,accept_time,alarm_type)values(?,?,?,?,?,?)";
			
			try {
				this.execute(sql, new Object[]{info.getUserId(),info.getAppId(),info.getKeyName(),info.getAlarmMsg(),info.getAcceptDate(),info.getAlarmType()});
			} catch (SQLException e) {
				logger.error("", e);
			}
		}
}
