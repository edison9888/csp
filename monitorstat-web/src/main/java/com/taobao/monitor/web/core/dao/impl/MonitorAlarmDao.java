/**
 * 
 */
package com.taobao.monitor.web.core.dao.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.po.AlarmSendPo;
import com.taobao.monitor.alarm.po.ExtraKeyAlarmDefine;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.web.cache.KeyCache;
import com.taobao.monitor.web.cache.SiteCache;
import com.taobao.monitor.web.vo.AlarmDataForPageViewPo;
import com.taobao.monitor.web.vo.AlarmDataPo;
import com.taobao.monitor.web.vo.AlarmDescPo;
import com.taobao.monitor.web.vo.AlarmRecordPo;
import com.taobao.monitor.web.vo.UserAcceptInfo;

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
	 * 获取此key 是否存在一些机器的额外配置
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public List<ExtraKeyAlarmDefine> getExtraKeyAlarmDefine(int appId,int keyId){
		String sql = "select * from ms_monitor_extra_key_alarm_define where app_id=? and key_id=?";
		
		final List<ExtraKeyAlarmDefine> list = new ArrayList<ExtraKeyAlarmDefine>();
		
		try {
			this.query(sql, new Object[]{appId,keyId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					ExtraKeyAlarmDefine define = new ExtraKeyAlarmDefine();
					setExtraKeyAlarmDefine(rs,define);
					list.add(define);
				}});
		} catch (Exception e) {
		}
		return list.size()==0?null:list;
	}
	
	
	private void setExtraKeyAlarmDefine(ResultSet rs,ExtraKeyAlarmDefine define) throws SQLException{
		define.setAlarmDefine(rs.getString("alarm_define"));
		define.setAppId(rs.getInt("app_id"));
		define.setHostId(rs.getInt("host_id"));
		define.setKeyId(rs.getInt("key_id"));
	}
	
	
/**
 * key添加指定主机以及额外备置
 * @param appId
 * @param keyId
 * @param define
 * @param hostList
 * @return
 */
	public boolean addExtraKeyAlarmDefine(int appId,int keyId, String define,List<String> hostList){
		
		try {
			String sql = "insert into ms_monitor_extra_key_alarm_define (app_id, key_id,host_id,alarm_define) values(?,?,?,?)";
			for(String hostId : hostList) {
			
				this.execute(sql, new Object[] {appId, keyId,hostId,define});
			}
//			this.executeBatch(sql, parms, dbRoute)
		} catch (Exception e) {
			logger.error("addExtraKeyAlarmDefine 出错,", e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * 删除特定备置的记录
	 * @param appId
	 * @param keyId
	 * @param hostId
	 */
	public boolean deleteExtraKeyAlarmDefine(int appId,int keyId, int hostId){
		String sql = "delete from ms_monitor_extra_key_alarm_define where app_Id = ? and key_id=? and host_id = ?";
		
		try {
			this.execute(sql,new Object[]{appId,keyId,hostId});
		} catch (SQLException e) {
			logger.error("", e);
			
			return false;
		}
		
		return true;
	}
		
	/**
	 * 根据ms_monitor_alarm 表中的数据 将ms_monitor_data_limit 中的数据
	 * 
	 * @return
	 */
	public List<AlarmDataPo> findAllAlarmWithAlarmTable(int appId) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -15);
		String sql = "select l.m_data,l.collect_time,l.site_id,l.app_id,l.key_id from ms_monitor_data_limit l " +
				" where  l.collect_time between ? and ? and l.app_id=?";
		


		final List<AlarmDataPo> list = new ArrayList<AlarmDataPo>();
		final Map<String, AlarmDataPo> map = new HashMap<String, AlarmDataPo>();
		try {
			this.query(sql,new Object[]{cal.getTime(),new Date(),appId},  new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					try {
						int appId = rs.getInt("app_id");
						int keyid = rs.getInt("key_id");
						int siteId = rs.getInt("site_id");
						String key = appId + "_" + keyid + "_" + siteId;
						AlarmDataPo bo = map.get(key);
						String key_value = KeyCache.get().getKey(keyid).getKeyName();
						String app_name = AppCache.get().getKey(appId).getAppName();
						String site_value = SiteCache.get().getKey(siteId).getSiteName();
						if (bo == null) {
							bo = new AlarmDataPo();
							bo.setAppName(app_name);
							bo.setKeyName(key_value);
							bo.setAppId(appId);
							bo.setKeyId(keyid+"");
							bo.setSite(site_value);
							bo.setSiteId(siteId);
							map.put(key, bo);
						}
						Timestamp value = rs.getTimestamp("collect_time");
						Date collect_time = new Date(value.getTime());
						String n = rs.getString("m_data");
						bo.putDataValue(collect_time, n);
//						Double v = bo.getLimitDataMap().get(collect_time);
//						if (v == null) {
//							bo.getLimitDataMap().put(collect_time, n);
//						} else {
//							if (key_value.indexOf(Constants.COUNT_TIMES_FLAG) > -1) {
//								bo.getLimitDataMap().put(collect_time, n + v);
//							} else if (key_value.indexOf(Constants.AVERAGE_USERTIMES_FLAG) > -1) {
//								bo.getLimitDataMap().put(collect_time, (n + v) / 2);
//							}
//						}

					} catch (Exception e) {

					}
				}
			},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}

		list.addAll(map.values());
		return list;
	}
	
	
	/**
	 *根据alarm_aim 字段 和key name 的like ,查询出所有需要告警的key 信息
	 * 
	 * @param name
	 * @return
	 */
	public Map<String,AlarmDataPo> findAlarmKeyDefByAppId(Integer appId) {

		String sql = "select a.alarm_type,a.id,a.key_id,a.alarm_define,a.alarm_feature,a.key_type,a.app_id,a.alarm_aim " +
				" from ms_monitor_key_alarm_def a where a.app_id=?";

		final Map<String,AlarmDataPo> keyMap = new HashMap<String, AlarmDataPo>();

		try {
			this.query(sql, new Object[]{appId}, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					AlarmDataPo po = new AlarmDataPo();
					po.setId(rs.getString("id"));
					po.setAppId(rs.getInt("app_id"));
					po.setKeyId(rs.getString("key_id"));
					po.setAlarmDefine(rs.getString("alarm_define"));
					po.setAlarmAim(rs.getString("alarm_aim"));
					po.setAlarmFeature(rs.getString("alarm_feature"));
					po.setAlarmType(rs.getString("alarm_type"));
					po.setKeyType(rs.getInt("key_type"));
					keyMap.put(rs.getString("key_id"), po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keyMap;
	}
	
	/**
	 *根据appId和keyId查询出告警阀值设定
	 * 
	 * @param name
	 * @return
	 */
	public AlarmDataPo findAlarmKeyDefByAppIdAndKeyId(Integer appId,Integer keyId) {
		final AlarmDataPo po = new AlarmDataPo();
		String sql = "select a.alarm_type,a.id,a.key_id,a.alarm_define,a.alarm_feature,a.key_type,a.app_id,a.alarm_aim " +
				" from ms_monitor_key_alarm_def a where a.app_id=? and key_id=?";
		try {
			this.query(sql, new Object[]{appId,keyId}, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					po.setId(rs.getString("id"));
					po.setAppId(rs.getInt("app_id"));
					po.setKeyId(rs.getString("key_id"));
					po.setAlarmDefine(rs.getString("alarm_define"));
					po.setAlarmAim(rs.getString("alarm_aim"));
					po.setAlarmFeature(rs.getString("alarm_feature"));
					po.setAlarmType(rs.getString("alarm_type"));
					po.setKeyType(rs.getInt("key_type"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getAppId() !=null ? po:null;
	}
	
	/**
	 * 
	 * @param keyAlarmid
	 */
	public boolean deleteKeyAlarm(String keyAlarmid) {
		String sql = "delete from ms_monitor_key_alarm_def where id=?";
		try {
			this.execute(sql, new Object[] { keyAlarmid });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	public boolean updateKeyAlarm(AlarmDataPo po) {
		String sql = "update ms_monitor_key_alarm_def set alarm_define=?,alarm_type=?,alarm_feature=?,key_type=? where id=?";

		try {
			this.execute(sql, new Object[] { po.getAlarmDefine(), po.getAlarmType(), po.getAlarmFeature(),
					po.getKeyType(), po.getId() });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	public boolean addKeyAlarm(AlarmDataPo po) {

		String sql1 = "select * from ms_monitor_key_alarm_def where app_id=? and key_id=? ";
		String sql = "insert into ms_monitor_key_alarm_def(app_id,key_id,alarm_define,alarm_aim,alarm_feature,alarm_type,key_type,gmt_create)"
				+ "values(?,?,?,?,?,?,?,NOW())";
		try {
			int c = this.getIntValue(sql1, new Object[] { po.getAppId(), po.getKeyId() });
			if (c > 0) {
				return false;
			}

			this.execute(sql, new Object[] { po.getAppId(), po.getKeyId(), po.getAlarmDefine(), po.getAlarmAim(),
					po.getAlarmFeature(), po.getAlarmType(), po.getKeyType() });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}
	
	
	public AlarmDataPo getKeyAlarmDefine(int id) {
		final AlarmDataPo po = new AlarmDataPo();
		String sql = "select a.alarm_type,a.id,a.app_id,a.key_id,a.alarm_define,a.alarm_aim,a.alarm_feature,"
				+ "k.key_value,p.app_name,a.key_type from ms_monitor_key_alarm_def a,ms_monitor_key k,ms_monitor_app p where a.key_id=k.key_id and a.app_id=p.app_id and a.id=?";
		try {
			this.query(sql, new Object[] { id }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
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

				}
			});
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}

		return po.getId() != null ? po : null;
	}
	

	public AlarmDataPo getKeyAlarm(String appid, String key_id) {
		final AlarmDataPo po = new AlarmDataPo();
		String sql = "select a.alarm_type,a.id,a.app_id,a.key_id,a.alarm_define,a.alarm_aim,a.alarm_feature,"
				+ "k.key_value,p.app_name,a.key_type from ms_monitor_key_alarm_def a,ms_monitor_key k,ms_monitor_app p where a.key_id=k.key_id and a.app_id=p.app_id and a.app_id=? and a.key_id=?";
		try {
			this.query(sql, new Object[] { appid, key_id }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
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

				}
			});
		} catch (Exception e) {
			logger.error("", e);
			return null;
		}

		return po.getId() != null ? po : null;
	}

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
	
	/**
	 * 记录告警信息
	 * 
	 * @param po
	 */
	public void recordAlarmEvent(int keyId,int appId,int siteId,Date time,String value) {
		String sql = "insert into MS_MONITOR_ALARM_RECORD(key_id,app_id,site_id,alarm_time,alarm_value)"
				+ " values(?,?,?,?,?)";
		try {
			this.execute(sql, new Object[] { keyId, appId, siteId, time,value});
		} catch (SQLException e) {
			logger.error("recordAlarmEvent 出错,", e);
		}
	}

	/**
	 * 查询出所有的时间段内的 告警信息
	 * 
	 * @param appid
	 * @param start
	 * @param end
	 * @return
	 */
	public List<AlarmRecordPo> findRecordAlarmByAppIdAndTime(int appid, Date start, Date end) {
		String sql = "select r.id,r.key_id,r.app_id,r.alarm_time,r.alarm_value,r.site_id,s.site_value,a.alarm_feature,k.key_value "
				+ "from ms_monitor_alarm_record r,ms_monitor_key_alarm_def a,ms_monitor_key k ,ms_monitor_site s where s.site_id=r.site_id and "
				+ "r.app_id=? and r.key_id=a.key_id and r.key_id=k.key_id and r.app_id=a.app_id and r.alarm_time > ? and r.alarm_time <?";

		final List<AlarmRecordPo> list = new ArrayList<AlarmRecordPo>();
		try {
			this.query(sql, new Object[] { appid, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {

					AlarmRecordPo po = new AlarmRecordPo();
					po.setId(rs.getLong("id"));
					po.setAppId(rs.getInt("app_id"));
					po.setAlarmkeyId(rs.getInt("key_id"));
					String feature = rs.getString("alarm_feature");
					if (feature == null || "".equals(feature.trim())) {
						po.setAlarmKeyName(rs.getString("key_value"));
					} else {
						po.setAlarmKeyName(feature);
					}
					po.setAlarmValue(rs.getString("alarm_value"));
					Timestamp alarm_time = rs.getTimestamp("alarm_time");
					po.setCollectTime(new Date(alarm_time.getTime()));
					po.setSiteName(rs.getString("site_value"));
					list.add(po);

				}
			});
		} catch (Exception e) {
			logger.error("findRecordAlarmByAppIdAndTime 出错,", e);
		}
		return list;
	}
	
	/**
	 * 根据应用名称
	 * 查询出所有的时间段内的 告警信息
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public List<AlarmDataForPageViewPo> findRecordAlarmByAppNameAndTime(String appName, Date start, Date end) {
		String sql = "select * from csp_time_key_alarm_record where app_name=? and alarm_time > ? and alarm_time <= ?";
		final List<AlarmDataForPageViewPo> list = new ArrayList<AlarmDataForPageViewPo>();
		try {
			this.query(sql, new Object[] { appName, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					
					AlarmDataForPageViewPo po = new AlarmDataForPageViewPo();
					po.setAppName(rs.getString("app_name"));
					po.setKeyName(rs.getString("key_name"));
					po.setPropertyName(rs.getString("property_name"));
					po.setModeName(rs.getString("mode_name"));
					po.setKeyScope(rs.getString("key_scope"));
					po.setIpString(rs.getString("ip"));
					Timestamp alarm_time = rs.getTimestamp("alarm_time");
					po.setAlarmTime( new Date(alarm_time.getTime()) );
					po.setAlarmValue(rs.getString("alarm_value"));
					po.setAlarmCause(rs.getString("alarm_cause"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findRecordAlarmByAppNameAndTime 出错,", e);
		}
		return list;
	}
	
	/**
	 * 根据应用名称
	 * 查询出所有的时间段内的 告警信息
	 * 分页查询
	 * @param appName
	 * @param start
	 * @param end
	 * @param size
	 * @param num
	 * @return
	 */
	public List<AlarmDataForPageViewPo> findRecordAlarmByAppNameAndTime(String appName, Date start, Date end, int size, int num) {
		String sql = "select * from csp_time_key_alarm_record where app_name=? and alarm_time > ? and alarm_time <= ? limit ?,?";
		final List<AlarmDataForPageViewPo> list = new ArrayList<AlarmDataForPageViewPo>();
		try {
			this.query(sql, new Object[] { appName, start, end, (size-1)*num, size }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					
					AlarmDataForPageViewPo po = new AlarmDataForPageViewPo();
					po.setAppName(rs.getString("app_name"));
					po.setKeyName(rs.getString("key_name"));
					po.setPropertyName(rs.getString("property_name"));
					po.setModeName(rs.getString("mode_name"));
					po.setKeyScope(rs.getString("key_scope"));
					po.setIpString(rs.getString("ip"));
					Timestamp alarm_time = rs.getTimestamp("alarm_time");
					po.setAlarmTime( new Date(alarm_time.getTime()) );
					po.setAlarmValue(rs.getString("alarm_value"));
					po.setAlarmCause(rs.getString("alarm_cause"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findRecordAlarmByAppNameAndTime 出错,", e);
		}
		return list;
	}
	
	/**
	 * 返回应用一段时间内的记录数
	 * 用于分页
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public int getAlarmCountByAppNameAndTime(String appName, Date start, Date end){
		String sql = "select count(*) as a_count from csp_time_key_alarm_record where app_name=? and alarm_time > ? and alarm_time <= ?";
		final List<Integer> l = new ArrayList<Integer>();
		try {
			this.query(sql, new Object[] { appName, start, end }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					l.add(rs.getInt("a_count"));
				}
			});
		} catch (Exception e) {
			logger.error("findRecordAlarmByAppNameAndTime 出错,", e);
		}
		return l.size() > 0 ? l.get(0).intValue() : 0;
	}
	/**
	 * 根据应用名称和开始时间查询
	 * @param appName
	 * @param start
	 * @param limit
	 * @return
	 */
	public List<AlarmDataForPageViewPo> findLimitRecordAlarmByAppNameAndTime(String appName, Date start, Date end, int limit) {
		String sql = "select * from csp_time_key_alarm_record where app_name=? and alarm_time > ? and alarm_time <= ? limit ?";
		final List<AlarmDataForPageViewPo> list = new ArrayList<AlarmDataForPageViewPo>();
		try {
			this.query(sql, new Object[] { appName, start, end, limit}, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					
					AlarmDataForPageViewPo po = new AlarmDataForPageViewPo();
					po.setAppName(rs.getString("app_name"));
					po.setKeyName(rs.getString("key_name"));
					po.setPropertyName(rs.getString("property_name"));
					po.setModeName(rs.getString("mode_name"));
					po.setKeyScope(rs.getString("key_scope"));
					po.setIpString(rs.getString("ip"));
					Timestamp alarm_time = rs.getTimestamp("alarm_time");
					po.setAlarmTime( new Date(alarm_time.getTime()) );
					po.setAlarmValue(rs.getString("alarm_value"));
					po.setAlarmCause(rs.getString("alarm_cause"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findRecordAlarmByAppNameAndTime 出错,", e);
		}
		return list;
	}
	
	/**
	 * 查询有哪些应用报警
	 * @param start
	 * @param end
	 * @return
	 */
	public List<AlarmDataForPageViewPo> findRecordAlarmAppNameByTime(Date start, Date end) {
		String sql = "select app_name, key_name from csp_time_key_alarm_record where alarm_time > ? and alarm_time <= ? group by app_name";
		final List<AlarmDataForPageViewPo> list = new ArrayList<AlarmDataForPageViewPo>();
		try {
			this.query(sql, new Object[] {start, end }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AlarmDataForPageViewPo po = new AlarmDataForPageViewPo();
					po.setAppName(rs.getString("app_name"));
					po.setKeyName(rs.getString("key_name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findRecordAlarmAppNameByTime 出错,", e);
		}
		return list;
	}
	
	/**
	 * 查询 告警表中最早时间
	 * @return Date
	 */
	public Date findRecordEarliestTime() {
		final Date eDate = new Date();
		String sql = "select min(alarm_time) as e_time from csp_time_key_alarm_record";
		try {
			this.query(sql,new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					Timestamp alarm_time = rs.getTimestamp("e_time");
					eDate.setTime(alarm_time.getTime());
				}
			});
		} catch (Exception e) {
			logger.error("findRecordEarliestTime 出错,", e);
		}
		return eDate;
	}
	/**
	 * 
	 * @param appId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<AlarmRecordPo> findExceptionMonitorDataDesc(int appId, Date start, Date end) {

		String sql = " select d.key_id,d.site_id,d.key_id,d.collect_time,data_value from ms_monitor_data_desc d where d.app_id=? and d.collect_time>? and d.collect_time<? ";

		final List<AlarmRecordPo> list = new ArrayList<AlarmRecordPo>();

		try {
			this.query(sql, new Object[] { appId, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					AlarmRecordPo po = new AlarmRecordPo();
					po.setAlarmKeyName(KeyCache.get().getKey(rs.getInt("key_id")).getKeyName());
					po.setSiteName(SiteCache.get().getKey(rs.getInt("site_id")).getSiteName());
					po.setAlarmkeyId(rs.getInt("key_id"));
					po.setCollectTime(new Date(rs.getTimestamp("collect_time").getTime()));
					po.setAlarmValue(rs.getString("data_value"));
					list.add(po);
				}
			},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("findMonitorDataDesc 出错,", e);
		}
		return list;
	}


		
	/**
	 * 
	 * @param appId
	 * @param keyId
	 * @param start
	 * @param end
	 * @return
	 */
	public List<String> findExceptionMonitorDataDesc(int appId, int keyId, Date start, Date end) {

		String sql = " select data_desc from ms_monitor_data_desc d "
				+ "where d.key_id=? and d.app_id=? and d.collect_time>? and d.collect_time<? ";

		final List<String> list = new ArrayList<String>();

		try {
			this.query(sql, new Object[] { keyId, appId, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					Blob desc = rs.getBlob("data_desc");
					StringBuilder sb = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(desc.getBinaryStream(),"GBK"));
					String s = null;
					while ((s = reader.readLine()) != null) {
						sb.append(s);
						sb.append("<br/>");
					}
					if (!list.contains(sb.toString())) {
						list.add(sb.toString());
					}

				}
			},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("findMonitorDataDesc 出错,", e);
		}
		return list;
	}
	/**
	 * 
	 * @param appId
	 * @param keyId
	 * @param collectTime
	 * @return
	 */
	public List<String> findExceptionMonitorDataDesc(int appId, int keyId, Date collectTime) {
		
		String sql = " select data_desc from ms_monitor_data_desc d "
			+ "where d.key_id=? and d.app_id=? and d.collect_time=? ";
		
		final List<String> list = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[] { keyId, appId, collectTime}, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					Blob desc = rs.getBlob("data_desc");
					StringBuilder sb = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(desc.getBinaryStream(),"GBK"));
					String s = null;
					while ((s = reader.readLine()) != null) {
						sb.append(s);
						sb.append("<br/>");
					}
					if (!list.contains(sb.toString())) {
						list.add(sb.toString());
					}
					
				}
			},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("findMonitorDataDesc 出错,", e);
		}
		return list;
	}

	/**
	 * 取得应用对应的告警key的数量
	 * 
	 * @param appName
	 * @return
	 */
	public long countAlarmKeyNum(String appName) {
		String sql = "select count(key_id) from ms_monitor_key_alarm_def a,ms_monitor_app p where a.app_id=p.app_id and p.app_name=?";
		try {
			return this.getLongValue(sql, new Object[] { appName });
		} catch (SQLException e) {
			logger.error("countAlarmKeyNum 出错,", e);
		}
		return 0;
	}


	/**
	 * 将告警信息插入数据库
	 */
	public boolean recordAlarmDesc(AlarmDescPo po) {
		String sql = "insert into MS_MONITOR_ALARM_DESC(app_id,alarm_reason,alarm_desc)" + " values(?,?,?)";
		try {
			this.execute(sql, new Object[] { po.getAppId(), po.getAlarmReason(), po.getAlarmDesc() });
		} catch (SQLException e) {
			logger.error("recordAlarmDesc 出错,", e);
			return false;
		}
		return true;
	}


	/**
	 * 提取应用名称和告警ID后插入数据库
	 */
	public boolean recordAlarmDescRelation(AlarmDescPo po) {
		String sql = "insert into MS_MONITOR_ALARM_DESC_RELATION(app_id,alarm_id)" + " values(?,?)";
		for (int i = 0; i < po.getAlarmIdList().size(); i++) {
			try {
				this.execute(sql, new Object[] { po.getAppId(), po.getAlarmIdList().get(i) });
			} catch (SQLException e) {
				logger.error("recordAlarmDescRelation 出错,", e);
				return false;
			}
		}
		return true;
	}

	public AlarmDescPo findAlarmDesc(long alarmId) {
		String sql = "select r.app_id,r.alarm_id,d.app_id,d.alarm_reason,d.alarm_desc "
				+ " from ms_monitor_alarm_desc_relation r,ms_monitor_alarm_desc d " + " where r.alarm_id=" + alarmId
				+ " and r.app_id=d.app_id";

		final AlarmDescPo po = new AlarmDescPo();
		try {

			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					String alarm_reason = rs.getString("alarm_reason");
					String alarm_desc = rs.getString("alarm_desc");

					po.setAppId(appId);
					po.setAlarmReason(alarm_reason);
					po.setAlarmDesc(alarm_desc);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po;
	}
	
	
	
	public void deleteAlarmDataDesc(int appId,Date date){
		String sql = "delete from ms_monitor_data_desc where collect_time < ? and app_id=?";
		
		try {
			this.execute(sql,new Object[]{date,appId},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (SQLException e) {
			logger.error("", e);
		}
		
		
	}
	
	
	/**
	 * 添加告警接收信息
	 * @param info
	 */
	public void addUserAcceptMsg(UserAcceptInfo info){
		
		String sql = "insert into MS_MONITOR_USER_ACCEPT(user_id,app_id,key_id,alarm_msg,accept_time,alarm_type)values(?,?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[]{info.getUserId(),info.getAppId(),info.getKeyId(),info.getAlarmMsg(),info.getAcceptDate(),info.getAlarmType()});
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	
	public void addAlarmSend(AlarmSendPo info){
		
		String sql = "insert into MS_MONITOR_USER_SEND(target_aim,app_id,alarm_msg,accept_time,alarm_type)values(?,?,?,?,?)";
		
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
			
			String sql = "SELECT target_aim, app_id,alarm_type FROM MS_MONITOR_USER_SEND WHERE accept_time > ? and accept_time < ?";
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
			
			String sql = "SELECT user_id, app_id,alarm_type FROM ms_monitor_user_accept WHERE accept_time > ? and accept_time < ?";
			final List<UserAcceptInfo> list = new ArrayList<UserAcceptInfo>();
			try {

				this.query(sql, new Object[] { alarmDateStart, alarmDateEnd },new SqlCallBack() {
					public void readerRows(ResultSet rs) throws Exception {
						UserAcceptInfo po = new UserAcceptInfo();
						int userId = rs.getInt("user_id");
						int appId = rs.getInt("app_id");
						int keyId = rs.getInt("user_id");
						String alarmType = rs.getString("alarm_type");
						po.setUserId(userId);
						po.setAppId(appId);
						po.setKeyId(keyId);
						po.setAlarmType(alarmType);
						list.add(po);
					}
				});
			} catch (Exception e) {
				logger.error("", e);
			}
			return list;
		}
		
	/**
	 * 返回指定日期有告警信息的所有不重复的所有user_Id的list
	 */
	
public List<String> findAllUser(String alarmDateStart,String alarmDateEnd) {
		
		String dateLimit = "";
		if(alarmDateStart != null && alarmDateEnd != null 
				&& !"".equals(alarmDateStart) && !"".equals(alarmDateEnd)) {
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
			String currentTime = sdf.format(Calendar.getInstance().getTime());
			alarmDateStart = alarmDateStart + " " + currentTime;
			alarmDateEnd = alarmDateEnd + " " + currentTime; 
			dateLimit = " and accept_time > '" + alarmDateStart + "' and accept_time < '" + alarmDateEnd + "'";
		}
		
		String sql = "SELECT DISTINCT user_id FROM ms_monitor_user_accept where 1 = 1 " + dateLimit;
		final List<String> list = new ArrayList<String>();

		try {

			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int userId = rs.getInt("user_id");
				
					list.add(userId + "");
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list;
	}

}
