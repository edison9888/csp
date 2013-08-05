package com.taobao.monitor.common.db.impl.center;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.util.page.Pagination;

public class CspTimeKeyAlarmRecordDao extends MysqlRouteBase{
	private static Logger logger = Logger.getLogger(CspTimeKeyAlarmRecordDao.class);
	
	public CspTimeKeyAlarmRecordDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("main"));
	}
	/**
	 *@author wb-lixing
	 *2012-4-25 下午04:43:40 
	 */
	public Pagination<CspTimeKeyAlarmRecordPo > findAlarmMsgList(String appName, String keyNamePart,Date from,Date to, int pageNo, int pageSize){
		String destSql = "SELECT * FROM csp_time_key_alarm_record WHERE" 
				+" app_name = ? AND key_name LIKE ? AND alarm_time BETWEEN ? AND ?" 
				+" order by key_name";
		keyNamePart = "%"+keyNamePart.trim()+"%";
		Object[] destParam = {appName, keyNamePart, from, to};
		Pagination<CspTimeKeyAlarmRecordPo > page = fillPage(destSql, destParam, new SqlCallBackForAlarmMsgPo(),pageNo, pageSize);
		return page;
	}
	
	public Pagination<CspTimeKeyAlarmRecordPo > findAlarmMsgList(String appName, String keyNamePart,Date from,Date to, int pageNo, int pageSize, String orderby, String sortType){
	  String destSql = "SELECT * FROM csp_time_key_alarm_record WHERE" 
	      +" app_name = ? AND key_name LIKE ? AND alarm_time BETWEEN ? AND ?" 
	      +" order by " + orderby + " " + sortType;
	  keyNamePart = "%"+keyNamePart.trim()+"%";
	  Object[] destParam = {appName, keyNamePart, from, to};
	  Pagination<CspTimeKeyAlarmRecordPo > page = fillPage(destSql, destParam, new SqlCallBackForAlarmMsgPo(),pageNo, pageSize);
	  return page;
	}
	
	class SqlCallBackForAlarmMsgPo extends PageSqlCallBack<CspTimeKeyAlarmRecordPo> {
		public void readerRows(ResultSet rs) throws Exception {
			CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
			fillCspTimeKeyAlarmRecordPo(rs, po);
			poList.add(po);
		}
	}
	
	
	/************分页  begin  ****************/
	public <T> Pagination<T> fillPage(String oriSql, Object[] params,
			PageSqlCallBack<T> pageSqlCallBack, int pageNo, int pageSize) {
		final Pagination<T> page = new Pagination<T>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		final List<T> poList = new ArrayList<T>();
		page.setList(poList);
		String countSql = "select count(*) total from (" + oriSql + ") t ";
		String sql = oriSql + " limit ?, ?";

		

		try {
			// 查询分页数
			this.query(countSql, params, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					long total = rs.getLong("total");
					page.setTotalCount((int) total);
				}
			});
			
			// 分页数据
			params = appendParams(params, (pageNo - 1) * pageSize, pageSize);
			pageSqlCallBack.setList(poList);
			logger.debug("-----sql: "+sql);
			logger.debug("-----params: "+Arrays.toString(params));
			this.query(sql, params, pageSqlCallBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
		page.setList(poList);
		return page;
	}
	
	// <T> just for setList
	abstract class PageSqlCallBack<T> implements SqlCallBack {
		public void setList(List<T> poList) {
			this.poList = poList;
		}

		protected List<T> poList;
	}
	
	
	
	/** 在Object[]后追加参数 */
	public Object[] appendParams(Object[] params, Object... appendParams) {
		if (params == null)
			params = new Object[0];
		Object[] destParams = new Object[params.length + appendParams.length];
		for (int i = 0; i < params.length; i++) {
			destParams[i] = params[i];
		}
		for (int i = 0; i < appendParams.length; i++) {
			destParams[params.length + i] = appendParams[i];
		}
		return destParams;
	}
	
	/************分页  end  
	 * @throws SQLException ****************/
	
	private void fillCspTimeKeyAlarmRecordPo(ResultSet rs,
			CspTimeKeyAlarmRecordPo  po) throws SQLException {
		po.setAlarm_cause(rs.getString("alarm_cause"));
		po.setAlarm_time(rs.getTimestamp("alarm_time"));
		po.setAlarm_value(rs.getString("alarm_value"));
		po.setApp_name(rs.getString("app_name"));
		po.setIp(rs.getString("ip"));
		po.setKey_name(rs.getString("key_name"));
		po.setKey_scope(rs.getString("key_scope"));
		po.setMode_name(rs.getString("mode_name"));
		po.setProperty_name(rs.getString("property_name"));
		
	}
	public boolean insert(List<CspTimeKeyAlarmRecordPo> list){
		String sql = "insert into csp_time_key_alarm_record(app_name,key_name,property_name,mode_name,key_scope,alarm_time,alarm_value,alarm_cause,ip) values(?,?,?,?,?,?,?,?,?)";
		for(CspTimeKeyAlarmRecordPo po : list){
			try {
				this.execute(sql, new Object[]{po.getApp_name(),po.getKey_name(),po.getProperty_name(),po.getMode_name(),po.getKey_scope(),po.getAlarm_time(),po.getAlarm_value(),po.getAlarm_cause(),po.getIp()});
			} catch (SQLException e) {
				logger.info(e);
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 统计最近N分钟告警的个数
	 *@author xiaodu
	 * @param appName
	 * @param n  单位分钟
	 * @return
	 *TODO
	 */
	public int countRecentlyAlarmNum(String appName,int n){
		String sql = "select count(*) from csp_time_key_alarm_record where app_name=? and alarm_time>?";
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -n);
		int c=0;
		try {
			c =  this.getIntValue(sql, new Object[]{appName,cal.getTime()});
		} catch (Exception e) {
			logger.info("countRecentlyAlarmNum",e);
		}
		return c<0?0:c;
		
	}
	
	/**
	 * 统计最近N分钟告警的个数
	 *@author xiaodu
	 * @param appName
	 * @param keyname
	 * @param n  单位分钟
	 * @return
	 *TODO
	 */
	public int countRecentlyAlarmNum(String appName,String keyname,int n){
		String sql = "select count(*) from csp_time_key_alarm_record where app_name=? and key_name=? and alarm_time>?";
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -n);
		System.out.println(cal.getTime());
		int c=0;
		try {
			c =  this.getIntValue(sql, new Object[]{appName,keyname,cal.getTime()});
		} catch (Exception e) {
			logger.info("countRecentlyAlarmNum",e);
		}
		return c<0?0:c;
		
	}
	
	/**
	 * 全闭包
	 * @return
	 */
	public int countAlarmByTime(String appName,String keyname,String start, String end){
	  String sql = "select count(*) from csp_time_key_alarm_record where app_name=? and key_name=? and alarm_time>=? and alarm_time<=?";
	  
	  int c=0;
	  try {
	    c =  this.getIntValue(sql, new Object[]{appName,keyname,start,end});
	  } catch (Exception e) {
	    logger.info("countRecentlyAlarmNum",e);
	  }
	  return c<0?0:c;
	  
	}
	
	
	/**
	 * 查找最近N分钟应用的告警信息
	 *@author xiaodu
	 * @param appName
	 * @param n 单位分钟
	 * @return
	 *TODO
	 */
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,int n){
		
		final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
		
		String sql = "select * from csp_time_key_alarm_record where app_name=? and alarm_time>?";
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -n);
		try {
			this.query(sql, new Object[]{appName,cal.getTime()}, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
					po.setAlarm_cause(rs.getString("alarm_cause"));
					po.setAlarm_time(rs.getTimestamp("alarm_time"));
					po.setAlarm_value(rs.getString("alarm_value"));
					po.setApp_name(rs.getString("app_name"));
					po.setIp(rs.getString("ip"));
					po.setKey_name(rs.getString("key_name"));
					po.setKey_scope(rs.getString("key_scope"));
					po.setMode_name(rs.getString("mode_name"));
					po.setProperty_name(rs.getString("property_name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.info("findRecentlyAlarmInfo",e);
		}
		return list;
	}
public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,int n,Date time){
		
		final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
		
		String sql = "select * from csp_time_key_alarm_record where app_name=? and alarm_tim <=>?";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MINUTE, -n);
		try {
			this.query(sql, new Object[]{appName,cal.getTime()}, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
					po.setAlarm_cause(rs.getString("alarm_cause"));
					po.setAlarm_time(rs.getTimestamp("alarm_time"));
					po.setAlarm_value(rs.getString("alarm_value"));
					po.setApp_name(rs.getString("app_name"));
					po.setIp(rs.getString("ip"));
					po.setKey_name(rs.getString("key_name"));
					po.setKey_scope(rs.getString("key_scope"));
					po.setMode_name(rs.getString("mode_name"));
					po.setProperty_name(rs.getString("property_name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.info("findRecentlyAlarmInfo",e);
		}
		return list;
	}
	
	
	/**
	 * 查找最近N分钟应用的告警信息
	 *@author xiaodu
	 * @param appName
	 * @param n 单位分钟
	 * @return
	 *TODO
	 */
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,int n){
		
		final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
		
		String sql = "select * from csp_time_key_alarm_record where app_name=? and key_name=? and alarm_time>?";
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -n);
		try {
			this.query(sql, new Object[]{appName,keyName,cal.getTime()}, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
					po.setAlarm_cause(rs.getString("alarm_cause"));
					po.setAlarm_time(rs.getTimestamp("alarm_time"));
					po.setAlarm_value(rs.getString("alarm_value"));
					po.setApp_name(rs.getString("app_name"));
					po.setIp(rs.getString("ip"));
					po.setKey_name(rs.getString("key_name"));
					po.setKey_scope(rs.getString("key_scope"));
					po.setMode_name(rs.getString("mode_name"));
					po.setProperty_name(rs.getString("property_name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.info("findRecentlyAlarmInfo",e);
		}
		return list;
	}
public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,int n,Date time){
		
		final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
		
		String sql = "select * from csp_time_key_alarm_record where app_name=? and key_name=? and alarm_time <=?";
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.add(Calendar.MINUTE, -n);
		try {
			this.query(sql, new Object[]{appName,keyName,cal.getTime()}, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
					po.setAlarm_cause(rs.getString("alarm_cause"));
					po.setAlarm_time(rs.getTimestamp("alarm_time"));
					po.setAlarm_value(rs.getString("alarm_value"));
					po.setApp_name(rs.getString("app_name"));
					po.setIp(rs.getString("ip"));
					po.setKey_name(rs.getString("key_name"));
					po.setKey_scope(rs.getString("key_scope"));
					po.setMode_name(rs.getString("mode_name"));
					po.setProperty_name(rs.getString("property_name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.info("findRecentlyAlarmInfo",e);
		}
		return list;
	}
public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,String property,int n){
		
		final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
		
		String sql = "select * from csp_time_key_alarm_record where app_name=? and key_name=? and property_name =? and alarm_time>? order by alarm_time desc,key_scope asc ";
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -n);
		try {
			this.query(sql, new Object[]{appName,keyName,property,cal.getTime()}, new SqlCallBack() {
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
					po.setAlarm_cause(rs.getString("alarm_cause"));
					po.setAlarm_time(rs.getTimestamp("alarm_time"));
					po.setAlarm_value(rs.getString("alarm_value"));
					po.setApp_name(rs.getString("app_name"));
					po.setIp(rs.getString("ip"));
					po.setKey_name(rs.getString("key_name"));
					po.setKey_scope(rs.getString("key_scope"));
					po.setMode_name(rs.getString("mode_name"));
					po.setProperty_name(rs.getString("property_name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.info("findRecentlyAlarmInfo",e);
		}
		return list;
	}



public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,String property,String keySocpe,int n){
	
	final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
	
	String sql = "select * from csp_time_key_alarm_record where app_name=? and key_name=?  and property_name =? and key_scope=? and alarm_time>? order by alarm_time desc,key_scope asc ";
	
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.MINUTE, -n);
	try {
		this.query(sql, new Object[]{appName,keyName,property,keySocpe,cal.getTime()}, new SqlCallBack() {
			
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
				po.setAlarm_cause(rs.getString("alarm_cause"));
				po.setAlarm_time(rs.getTimestamp("alarm_time"));
				po.setAlarm_value(rs.getString("alarm_value"));
				po.setApp_name(rs.getString("app_name"));
				po.setIp(rs.getString("ip"));
				po.setKey_name(rs.getString("key_name"));
				po.setKey_scope(rs.getString("key_scope"));
				po.setMode_name(rs.getString("mode_name"));
				po.setProperty_name(rs.getString("property_name"));
				list.add(po);
			}
		});
	} catch (Exception e) {
		logger.info("findRecentlyAlarmInfo",e);
	}
	return list;
}


public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,String property,int n ,Date time){
	
	final List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
	
	String sql = "select * from csp_time_key_alarm_record where app_name=? and key_name=? and property_name =? and alarm_time > ? and alarm_time <= ?order by alarm_time desc,key_scope asc ";
	
	Calendar cal = Calendar.getInstance();
	cal.setTime(time);
	cal.add(Calendar.MINUTE, -n);
	try {
		this.query(sql, new Object[]{appName,keyName,property,cal.getTime(),time}, new SqlCallBack() {
			
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
				po.setAlarm_cause(rs.getString("alarm_cause"));
				po.setAlarm_time(rs.getTimestamp("alarm_time"));
				po.setAlarm_value(rs.getString("alarm_value"));
				po.setApp_name(rs.getString("app_name"));
				po.setIp(rs.getString("ip"));
				po.setKey_name(rs.getString("key_name"));
				po.setKey_scope(rs.getString("key_scope"));
				po.setMode_name(rs.getString("mode_name"));
				po.setProperty_name(rs.getString("property_name"));
				list.add(po);
			}
		});
	} catch (Exception e) {
		logger.info("findRecentlyAlarmInfo",e);
	}
	return list;
}
	
	
}
