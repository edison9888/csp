//package com.taobao.csp.alarm.dao;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.apache.log4j.Logger;
//
//import com.taobao.csp.alarm.po.AlarmContext;
//import com.taobao.monitor.common.db.base.MysqlRouteBase;
//import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
//
//public class CspTimeKeyAlarmRecordRecentDao extends MysqlRouteBase{
//private static Logger logger = Logger.getLogger(CspTimeKeyAlarmRecordRecentDao.class);
//
//public List<CspTimeKeyAlarmRecordPo> findAlarmInfo(String appName,String keyName,String property,String keyScope){
//	
//	final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
//	
//	String sql = "select * from csp_time_key_alarm_record_recent where app_name=? and key_name=? and property_name =? and key_scope = ? order by alarm_time desc,key_scope asc ";
//	
//	
//	try {
//		this.query(sql, new Object[]{appName,keyName,property,keyScope}, new SqlCallBack() {
//			
//			@Override
//			public void readerRows(ResultSet rs) throws Exception {
//				CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
//				po.setAlarm_cause(rs.getString("alarm_cause"));
//				po.setAlarm_time(rs.getTimestamp("alarm_time"));
//				po.setAlarm_value(rs.getString("alarm_value"));
//				po.setApp_name(rs.getString("app_name"));
//				po.setIp(rs.getString("ip"));
//				po.setKey_name(rs.getString("key_name"));
//				po.setKey_scope(rs.getString("key_scope"));
//				po.setMode_name(rs.getString("mode_name"));
//				po.setProperty_name(rs.getString("property_name"));
//				list.add(po);
//			}
//		});
//	} catch (Exception e) {
//		logger.info("findRecentlyAlarmInfo",e);
//	}
//	return list;
//}
//public List<CspTimeKeyAlarmRecordPo> findAlarmInfo(String appName,String keyName,String property,String keyScope,String ip){
//	
//	final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
//	
//	String sql = "select * from csp_time_key_alarm_record_recent where app_name=? and key_name=? and property_name =? and key_scope = ? and ip = ? order by alarm_time desc,key_scope asc ";
//	
//	
//	try {
//		this.query(sql, new Object[]{appName,keyName,property,keyScope,ip}, new SqlCallBack() {
//			
//			@Override
//			public void readerRows(ResultSet rs) throws Exception {
//				CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
//				po.setAlarm_cause(rs.getString("alarm_cause"));
//				po.setAlarm_time(rs.getTimestamp("alarm_time"));
//				po.setAlarm_value(rs.getString("alarm_value"));
//				po.setApp_name(rs.getString("app_name"));
//				po.setIp(rs.getString("ip"));
//				po.setKey_name(rs.getString("key_name"));
//				po.setKey_scope(rs.getString("key_scope"));
//				po.setMode_name(rs.getString("mode_name"));
//				po.setProperty_name(rs.getString("property_name"));
//				list.add(po);
//			}
//		});
//	} catch (Exception e) {
//		logger.info("findRecentlyAlarmInfo",e);
//	}
//	return list;
//}
//public boolean deleteAlarmInfoBefore(Date time){
//	boolean flag = true;
//	String sql = "delete from csp_time_key_alarm_record_recent where alarm_time < ?";
//	try{
//		this.execute(sql, new Object[]{time});
//	}catch(Exception e){
//		logger.info(e);
//		return false;
//	}
//	return flag;
//}
//public boolean updateTime(List<AlarmContext> list){
//	boolean flag = true;
//	String sql = "update csp_time_key_alarm_record_recent set process_time = ? where app_name = ? and key_name = ? and property_name = ? and key_scope = ? and ip = ?";
//	for(AlarmContext po : list){
//		try {
//			this.execute(sql, new Object[]{po.getAppName(),po.getKeyName(),po.getProperty(),po.getKeyScope(),po.getIp()});
//		} catch (SQLException e) {
//			return false;
//		}
//	}
//	return flag;
//}
//
//public boolean updateTime(AlarmContext po){
//	boolean flag = true;
//	String sql = "update csp_time_key_alarm_record_recent set process_time = ? where app_name = ? and key_name = ? and property_name = ? and key_scope = ? and ip = ?";
//		try {
//			this.execute(sql, new Object[]{po.getAppName(),po.getKeyName(),po.getProperty(),po.getKeyScope(),po.getIp()});
//		} catch (SQLException e) {
//			return false;
//		}
//	return flag;
//}
//public boolean insert(List<AlarmContext> list){
//	String sql = "insert into csp_time_key_alarm_record_recent(app_name,key_name,property_name,mode_name,key_scope,alarm_time,alarm_value,alarm_cause,ip) values(?,?,?,?,?,?,?,?,?)";
//	for(AlarmContext po : list){
//		try {
//			Date time = new Date(po.getTime());
//			if(!isExist(po)){
//				this.execute(sql, new Object[]{po.getAppName(),po.getKeyName(),po.getProperty(),po.getModeName(),po.getKeyScope(),time,po.getValue(),po.getRangeMessage(),po.getIp()});
//			}else{
//				updateTime(po);
//			}
//		} catch (SQLException e) {
//			logger.info(e);
//			return false;
//		}
//	}
//	return true;
//}
//public boolean isExist(AlarmContext context){
//	boolean flag = false;
//	List<CspTimeKeyAlarmRecordPo> list = findAlarmInfo(context.getAppName(),context.getKeyName(),context.getProperty(),context.getKeyScope(),context.getIp());
//	if(list.size()>0)return true;
//	return flag;
//}
//}
