package com.taobao.csp.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.time.web.po.AlarmMessageVisitPo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class AlarmMessageVisitDao extends MysqlRouteBase{
	private static Logger logger = Logger.getLogger(AlarmMessageVisitDao.class);
	public boolean isExist(String appName,String keyName,String propertyName,Date alarmTime){
		boolean flag = true;
		List<AlarmMessageVisitPo> list = findRecords(appName,keyName,propertyName,alarmTime);
		if(list.size()==0)return false;
		return flag;
	}
	public List<AlarmMessageVisitPo> findRecords(String appName,String keyName,String propertyName,Date alarmTime){
		final List<AlarmMessageVisitPo> list = new ArrayList<AlarmMessageVisitPo>();
		String sql = "select * from csp_time_alarm_message_visit where app_name = ? and key_name = ? and property_name = ? and alarm_time = ?";
		try {
			this.query(sql, new Object[]{appName,keyName,propertyName,alarmTime}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					AlarmMessageVisitPo po = new AlarmMessageVisitPo();
					po.setAlarmTime(rs.getTime("alarm_time"));
					po.setAppName(rs.getString("app_name"));
					po.setKeyName(rs.getString("key_name"));
					po.setPropertyName(rs.getString("property_name"));
					po.setVisitCount(rs.getInt("visit_count"));
					String visitorstr = rs.getString("visitors");
					String[] visitors = visitorstr.split(",");
					for(String visitor : visitors){
						po.addVisitor(visitor);
					}
					list.add(po);
				}});
		} catch (Exception e) {
			logger.info(e);
		}
		return list;
	}
	public boolean update(String appName,String keyName,String propertyName,Date time,String visitor){
		boolean flag = true;
		String sql = "update csp_time_alarm_message_visit set visitors = concat(visitors,?) , visit_count = visit_count+1 where app_name = ? and key_name = ? and property_name = ? and alarm_time = ?";
		try {
			this.execute(sql, new Object[]{visitor+",",appName,keyName,propertyName,time});
		} catch (SQLException e) {
			logger.info(e);
		}
		return flag;
	}
	public boolean insert(AlarmMessageVisitPo po){
		boolean flag = true;
		if(isExist(po.getAppName(),po.getKeyName(),po.getPropertyName(),po.getAlarmTime()))return true;
		String sql = "insert into csp_time_alarm_message_visit(app_name,key_name,property_name,alarm_time,visit_count,visitors) values(?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{po.getAppName(),po.getKeyName(),po.getPropertyName(),po.getAlarmTime(),0,""});
		} catch (SQLException e) {
			logger.info(e);
		}
		return flag;
	}
}
