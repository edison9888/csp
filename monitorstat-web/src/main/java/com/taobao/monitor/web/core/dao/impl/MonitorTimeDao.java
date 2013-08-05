/**
 * 
 */
package com.taobao.monitor.web.core.dao.impl;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.TableNameConverUtil;
import com.taobao.monitor.common.util.Utlitites;
import com.taobao.monitor.web.cache.AppCache;
import com.taobao.monitor.web.cache.KeyCache;
import com.taobao.monitor.web.cache.SiteCache;
import com.taobao.monitor.web.vo.KeyValueVo;
import com.taobao.monitor.web.vo.OtherHaBoLogRecord;
import com.taobao.monitor.web.vo.OtherKeyValueVo;
import com.taobao.monitor.web.vo.PvUrlPo;
import com.taobao.monitor.web.vo.ReportInfoPo;
import com.taobao.monitor.web.vo.SitePo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-16 ����11:21:55
 * 
 * 
 * 
 * 
 */
public class MonitorTimeDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(MonitorTimeDao.class);
	
	/**
	 * ��ȡ��ʱ�������һ��keyһ̨����������
	 * @param appId
	 * @param keyid
	 * @param siteId
	 * @return
	 */
	public Map<Date,Double> findKeyLimitRecentlyData(int appId,int keyid,int siteId){
		String sql = "select * from ms_monitor_data_limit where app_id=? and key_id=? and site_id=?";
		
		final Map<Date,Double> map = new HashMap<Date, Double>();
		final String keyName = KeyCache.get().getKey(keyid).getKeyName();
		try {
			this.query(sql, new Object[]{appId,keyid,siteId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					Date date = new Date(rs.getTimestamp("collect_time").getTime());
					
					if(map.get(date) == null){
						map.put(date, rs.getDouble("m_data"));
					}else{
						Double v = map.get(date);		
						if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>-1){
							map.put(date, v+ rs.getDouble("m_data"));
						}else if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>-1){				
							map.put(date, Arith.div(Arith.add(v, rs.getDouble("m_data")), 2,2));
						}		
					}
					
					
				}},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("",e);
		}
		
		return map;
		
	}
	
	
	/**
	 * ͳ��ĳ��Ӧ�õ�ĳ��key ��һ��ʱ���ڴ���
	 * @param appId
	 * @param keyid
	 * @param collectTime  key site  value ͳ��ֵ
	 * @return
	 */
	public Map<String,Long> sumKeyValueBySite(int appId,int keyid,Date collectTime){
		
		final Map<String,Long> map = new HashMap<String, Long>();
		
		String sql = "select site_id,sum(m_data) as sumdata from "+getTableName(collectTime)+" where app_id=? and key_id=? group by site_id";
		try {
			this.query(sql, new Object[]{appId,keyid}, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					long m_data = rs.getLong("sumdata");
					map.put(SiteCache.get().getKey(site_id).getSiteName(), m_data);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return map;
	}
	

	

	public void dropDateTable(String collectdate) throws SQLException {
		String delete = "drop table ms_monitor_data_" + collectdate;
		try {
			this.execute(delete);
		} catch (Exception e) {
		}
	}

	/**
	 * ��ms_monitor_data_limit ������keyName ��Ϊǰ׺ ��like ��ѯ
	 * 
	 * ��key ��ǰ׺ ��Ϊlike ��ѯ��Ӧ�������ݣ�ȡ��ms_monitor_data_limit ���ݺ�׺
	 * Constants.COUNT_TIMES_FLAG ���ۼ� Constants.AVERAGE_USERTIMES_FLAG ��ƽ��
	 * 
	 * �������ݿ����Ƕ�̨�������Ὣ��̨������ƽ��
	 * 
	 * @param keyName
	 * @param appname
	 * @return Map<keyName, List<KeyValuePo>>    
	 * @throws Exception
	 */
	public Map<String, List<KeyValuePo>> findLikeKeyByLimit(String keyName, final String appname) {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -30);
		
		final Map<String, List<KeyValuePo>> keymap = new HashMap<String, List<KeyValuePo>>();

		final Map<String, Map<String, KeyValuePo>> map = new HashMap<String, Map<String, KeyValuePo>>();
		try {

			int app_id = AppCache.get().getKey(appname).getAppId();

			String sql = "select c.app_id,c.key_id,c.collect_time,c.m_data as mcount,c.site_id "
					+ " from ms_monitor_data_limit c where c.key_id in (" + getKeyIdByLikeName(app_id, keyName) + ") "
					+ " and c.app_id=? and c.collect_time between ? and ?";
			final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");

			this.query(sql, new Object[] { app_id, cal.getTime(), new Date() }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					int app_id = rs.getInt("app_id");
					int key_id = rs.getInt("key_id");
					String m_data = rs.getString("mcount");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					String key_value = KeyCache.get().getKey(key_id).getKeyName();

					Map<String, KeyValuePo> timeMap = map.get(key_value);
					if (timeMap == null) {
						timeMap = new HashMap<String, KeyValuePo>();
						map.put(key_value, timeMap);
					}
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);
					KeyValuePo po = timeMap.get(time);
					if (po == null) {
						po = new KeyValuePo();
						timeMap.put(time, po);
						po.setAppName(appname);
						po.setKeyName(key_value);
						po.setAppId(app_id);
						po.setKeyId(key_id);
						po.setSiteId(site_id);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						List<KeyValuePo> list = keymap.get(po.getKeyName());
						if (list == null) {
							list = new ArrayList<KeyValuePo>();
							keymap.put(po.getKeyName(), list);
						}
						list.add(po);
					}
					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);
				}
			},DbRouteManage.get().getDbRouteByTimeAppid(app_id));
		} catch (Exception e) {
			logger.error("", e);
		}

		for (Map.Entry<String, List<KeyValuePo>> entry : keymap.entrySet()) {
			Collections.sort(entry.getValue());
		}

		return keymap;
	}

	/**
	 * �˷���ͳ����ʱ�䷶Χ������������Ƕ���� ���� sum ��ƽ����ÿ̨���� ��Ҫ���� DbRouteManage ����ѯӦ�����ڵķֿ�
	 * 
	 * @param key_name
	 * @param tableDate
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public long countKeyByDate(int keyId, int appId, String tableDate, Date startTime, Date endTime) {
		
		List<KeyValuePo> list = findKeyValueByRangeDate(appId,keyId,startTime,endTime);
		
		double all = 0;
		Set<Integer> setSite = new HashSet<Integer>();
		for(KeyValuePo po:list){			
			for(Map.Entry<Integer, Double> entry:po.getSiteValueMap().entrySet()){
				all = Arith.add(all, entry.getValue());
				setSite.add(entry.getKey());
			}			
		}
		if (setSite.size() > 0) {
			return (long) Arith.div(all, setSite.size(),2);
		}
		
		
//		String sql = "select site_id,sum(m_data) as sumdata from ms_monitor_data_" + tableDate
//				+ " where collect_time between ? and ? and key_id=? and app_id=? group by site_id";
//
//		try {
//			final Map<String, Long> map = new HashMap<String, Long>();
//
//			this.query(sql, new Object[] { startTime, endTime, keyId, appId }, new SqlCallBack() {
//				
//				public void readerRows(ResultSet rs) throws Exception {
//
//					long sumdata = rs.getLong("sumdata");
//					String siteId = rs.getString("site_id");
//
//					Long _l = map.get(siteId);
//					if (_l == null) {
//						map.put(siteId, sumdata);
//					} else {
//						map.put(siteId, sumdata + _l);
//					}
//
//				}
//			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
//
//			int siteNum = map.size();
//			long sum = 0;
//			for (Long s : map.values()) {
//				sum += s;
//			}
//
//			if (siteNum > 0) {
//				return sum / siteNum;
//			}
//
//		} catch (Exception e) {
//			logger.error("", e);
//		}
		return 0;
	}

	/**
	 *���ݸ���ʱ��㣬�����ʱ����Ӧ������ȡ��
	 * 
	 * @param collectTimeList
	 * ��� yyyy-MM-dd HH:mm:ss ʱ���ʽ��
	 * @return
	 * @throws Exception
	 * @throws Exception
	 */
	public Map<String, KeyValueVo> findTimeMonitorByTime(String keyName, final String appname, String tableDate,
			List<String> collectTimeList) {

		final Map<String, KeyValueVo> mapVo = new HashMap<String, KeyValueVo>();// key
		// Ϊʱ��

		try {
			final int app_id = AppCache.get().getKey(appname).getAppId();

			String sql = "select c.key_id,c.collect_time,c.m_data as mcount ,c.site_id  from ms_monitor_data_"
					+ tableDate + " c where c.key_id in(" + getKeyIdByLikeName(app_id, keyName)
					+ ") and c.app_id=? and c.collect_time in" + " ("
					+ Utlitites.formatArray2Sqlin(collectTimeList.toArray(new String[] {})) + ")" + " ";

			final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

			this.query(sql, new Object[] { app_id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					int key_id = rs.getInt("key_id");
					String m_data = rs.getString("mcount");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String key_value = KeyCache.get().getKey(key_id).getKeyName();

					String timeKey = sdf.format(date);
					KeyValueVo keyVo = mapVo.get(timeKey);
					if (keyVo == null) {
						keyVo = new KeyValueVo();
						mapVo.put(timeKey, keyVo);
					}

					KeyValuePo po = keyVo.getMap().get(key_value);

					if (po == null) {
						po = new KeyValuePo();
						keyVo.getMap().put(key_value, po);
						po.setAppName(appname);
						po.setKeyName(key_value);
						po.setAppId(app_id);
						po.setKeyId(key_id);
						po.setCollectTimeStr(timeKey);
					}

					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(app_id));
		} catch (Exception e) {
			logger.error(appname+"", e);
		}

		return mapVo;
	}

	/**
	 * ��ms_monitor_data_limit ������keyName ��Ϊǰ׺ ��like ��ѯ
	 * 
	 * ��key ��ǰ׺ ��Ϊlike ��ѯ��Ӧ�������ݣ�ȡ��ms_monitor_data_limit ���ݺ�׺
	 * Constants.COUNT_TIMES_FLAG ���ۼ� Constants.AVERAGE_USERTIMES_FLAG ��ƽ��
	 * 
	 * �������ݿ����Ƕ�̨�������Ὣ��̨������ƽ��
	 * 
	 * @param keyName
	 * @param appname
	 * @return
	 * @throws Exception
	 */
	public List<KeyValueVo> findLikeKeyByLimit1(String keyName, final String appname) {
		final Map<String, KeyValueVo> mapVo = new HashMap<String, KeyValueVo>();// key
		// Ϊʱ��
		List<KeyValueVo> listVo = new ArrayList<KeyValueVo>();
		try {
			final int app_id = AppCache.get().getKey(appname).getAppId();
			String sql = "select c.key_id,c.collect_time,c.m_data as mcount ,c.site_id "
					+ "from ms_monitor_data_limit c where c.key_id in (" + getKeyIdByLikeName(app_id, keyName)
					+ ") and c.app_id=? ";

			final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

			this.query(sql, new Object[] { app_id }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					int key_id = rs.getInt("key_id");
					String m_data = rs.getString("mcount");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String key_value = KeyCache.get().getKey(key_id).getKeyName();

					String timeKey = sdf.format(date);
					KeyValueVo keyVo = mapVo.get(timeKey);
					if (keyVo == null) {
						keyVo = new KeyValueVo();
						keyVo.setDate(date);
						keyVo.setCollectTime(sdf.format(date));
						mapVo.put(timeKey, keyVo);
					}

					KeyValuePo po = keyVo.getMap().get(key_value);

					if (po == null) {
						po = new KeyValuePo();
						keyVo.getMap().put(key_value, po);
						po.setAppName(appname);
						po.setKeyName(key_value);
						po.setAppId(app_id);
						po.setKeyId(key_id);
						po.setCollectTimeStr(timeKey);
					}

					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);
				}
			},DbRouteManage.get().getDbRouteByTimeAppid(app_id));
		} catch (Exception e) {
			logger.error("", e);
		}
		listVo.addAll(mapVo.values());
		Collections.sort(listVo);
		return listVo;
	}

	/**
	 * ��ms_monitor_data_limit ������keyName ��Ϊǰ׺ ��like ��ѯ
	 * 
	 * ��key ��ǰ׺ ��Ϊlike ��ѯ��Ӧ�������ݣ�ȡ��ms_monitor_data_limit ���ݺ�׺
	 * Constants.COUNT_TIMES_FLAG ���ۼ� Constants.AVERAGE_USERTIMES_FLAG ��ƽ��
	 * 
	 * �������ݿ����Ƕ�̨�������Ὣ��̨������ƽ��
	 * 
	 * @param keyName
	 * @param appname
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, Map<String, KeyValuePo>>> findLikeKeyByLimit(int appId) {
		String sql = "select app_id,key_id,collect_time,m_data as mcount,site_id from ms_monitor_data_limit ";
		final Map<String, Map<String, Map<String, KeyValuePo>>> appMapVo = new HashMap<String, Map<String, Map<String, KeyValuePo>>>();// key
		// Ϊʱ��
		final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					int keyId = rs.getInt("key_id");
					int site_id = rs.getInt("site_id");
					String m_data = rs.getString("mcount");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());

					String app_name = AppCache.get().getKey(appId).getAppName();
					String key_value = KeyCache.get().getKey(keyId).getKeyName();

					Map<String, Map<String, KeyValuePo>> keyMapVo = appMapVo.get(app_name);
					if (keyMapVo == null) {
						keyMapVo = new HashMap<String, Map<String, KeyValuePo>>();
						appMapVo.put(app_name, keyMapVo);
					}
					Map<String, KeyValuePo> timeMapVo = keyMapVo.get(key_value);
					if (timeMapVo == null) {
						timeMapVo = new HashMap<String, KeyValuePo>();
						keyMapVo.put(key_value, timeMapVo);
					}
					String timeKey = sdf.format(date);
					KeyValuePo keypo = timeMapVo.get(timeKey);
					if (keypo == null) {
						keypo = new KeyValuePo();
						timeMapVo.put(timeKey, keypo);
						keypo.setCollectTime(date);
						keypo.setCollectTimeStr(timeKey);

						keypo.setAppName(app_name);
						keypo.setKeyName(key_value);
						keypo.setAppId(appId);
						keypo.setKeyId(keyId);
						keypo.setSiteId(site_id);
					}
					try {
						Double d1 = Double.parseDouble(m_data);
						keypo.putValue(site_id, d1);
					} catch (Exception e) {
					}
				}
			},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		return appMapVo;
	}

	/**
	 * ����key ���� ȡ�� ĳ���ȫ������ ���ݺ�׺ Constants.COUNT_TIMES_FLAG ���ۼ�
	 * Constants.AVERAGE_USERTIMES_FLAG ��ƽ��
	 * 
	 * �������ݿ����Ƕ�̨�������Ὣ��̨������ƽ��
	 * 
	 * 
	 * @param key
	 * @param collectTime
	 *            yyyy-MM-dd
	 * @return
	 * @throws Exception
	 */
	public Map<String, Map<String, KeyValuePo>> findLikeKeyByDate(String keyName, String appName, java.util.Date start,
			java.util.Date end) {

		final Map<String, Map<String, KeyValuePo>> map = new HashMap<String, Map<String, KeyValuePo>>();

		try {
			final int app_id = AppCache.get().getKey(appName).getAppId();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String sql = "select c.key_id,c.collect_time,c.m_data as mcount,c.site_id from " + getTableName(start)
					+ " c where c.key_id in (" + getKeyIdByLikeName(app_id, keyName) + ") "
					+ " and c.app_id=? and c.collect_time between ? and ?";

			final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");

			this.query(sql, new Object[] { app_id, sdf.format(start), sdf.format(end) }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int keyId = rs.getInt("key_id");
					int site_id = rs.getInt("site_id");
					String m_data = rs.getString("mcount");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String key_value = KeyCache.get().getKey(keyId).getKeyName();

					Map<String, KeyValuePo> timeMap = map.get(key_value);
					if (timeMap == null) {
						timeMap = new HashMap<String, KeyValuePo>();
						map.put(key_value, timeMap);
					}
					String time = parseLogFormatDate.format(date);

					KeyValuePo po = timeMap.get(time);
					if (po == null) {
						po = new KeyValuePo();
						timeMap.put(time, po);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						po.setKeyName(key_value);
					}
					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);

				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(app_id));
		} catch (Exception e) {
			logger.error("", e);
		}

		return map;
	}

	/**
	 * ����keyName ǰ׺ like ���ҳ�����key
	 * 
	 * @param keyName
	 * @return
	 */
	public List<KeyPo> findAllKey(String keyName) {

		String sql = "select * from ms_monitor_key where key_value like ? ";

		final List<KeyPo> keypoList = new ArrayList<KeyPo>();

		try {
			this.query(sql, new Object[] { keyName + "%" }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					keypoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keypoList;
	}
	
	/**
	 * ����key info
	 * @param po
	 * @return
	 */
	public boolean updateKeyInfo(KeyPo po){
		String sql = "update ms_monitor_key set alias_name=?,key_type=?,feature=? where key_id=?";
		try {
			this.execute(sql, new Object[]{po.getAliasName(),po.getKeyType(),po.getFeature(),po.getKeyId()});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * �����ר������pv ����url ���ֵģ��������ռ�ص�ָ�
	 * 
	 * @param keyid
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String, List<PvUrlPo>> findPvUrlDetailByDate(String keyId, String appName, Date start, Date end) {
		String sql = "select c.collect_time,c.m_data,c.site_id from "
				+ getTableName(start) + " c where c.key_id =? and c.app_id=? and c.collect_time between ? and ?";

		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		final Map<String, Map<String, PvUrlPo>> siteMap = new HashMap<String, Map<String, PvUrlPo>>();
		try {
			final int app_id = AppCache.get().getKey(appName).getAppId();
			final String key_name = KeyCache.get().getKey(Integer.parseInt(keyId)).getKeyName();
			this.query(sql, new Object[] { keyId, app_id, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					String site_name = SiteCache.get().getKey(site_id).getSiteName();
					String m_data = rs.getString("m_data");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);
					Map<String, PvUrlPo> timeMap = siteMap.get(site_name);
					if (timeMap == null) {
						timeMap = new HashMap<String, PvUrlPo>();
						siteMap.put(site_name, timeMap);
					}
					PvUrlPo po = timeMap.get(time);
					if (po == null) {
						po = new PvUrlPo();
						timeMap.put(time, po);
						po.setSiteId(site_id);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						po.setUrl(key_name);
					}
					String[] values = m_data.split(",");// VISIT+","+REST+","+PAGESIZE
					if (po == null) {
						po = new PvUrlPo();
						timeMap.put(time, po);
						po.setSiteId(site_id);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);

						po.setPvCount(Integer.parseInt(values[0]));
						po.setRest(Double.parseDouble(values[1]));
						po.setPagesize(Double.parseDouble(values[2]));

					} else {
						if (po.getSiteId().intValue() == site_id) { // ���˵����̨ͬ�������и�
							// ��־��ʱ��һ��ʱ��㱻�г�����
							po.setPvCount(Integer.parseInt(values[0]) + po.getPvCount());
							po.setRest((Double.parseDouble(values[1]) + po.getRest()) / 2);
							po.setPagesize((Double.parseDouble(values[2]) + po.getPagesize()) / 2);

						}
					}
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(app_id));
		} catch (Exception e) {
			logger.error("", e);
		}

		Map<String, List<PvUrlPo>> map = new HashMap<String, List<PvUrlPo>>();
		for (Map.Entry<String, Map<String, PvUrlPo>> entry : siteMap.entrySet()) {
			List<PvUrlPo> poList = new ArrayList<PvUrlPo>();
			poList.addAll(entry.getValue().values());
			map.put(entry.getKey(), poList);
		}
		return map;
	}

	/**
	 * ��ѯʱ���ڵ� ���ݣ����ռ�ص�ָ�
	 * 
	 * @param keyId
	 * @param appName
	 * @param start
	 * @param end
	 * @return Map<site,List<KeyValuePo>>
	 */
	public Map<String, List<KeyValuePo>> findKeyValueSiteByDate(int appId,int keyId, java.util.Date start,
			java.util.Date end) {

		String sql = "select c.collect_time,c.m_data,c.site_id from "
				+ getTableName(start) + " c where c.key_id =? and c.app_id=? and c.collect_time between ? and ?";

		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");

		final Map<String, Map<String, KeyValuePo>> siteMap = new HashMap<String, Map<String, KeyValuePo>>();

		try {
			final String key_name = KeyCache.get().getKey(keyId).getKeyName();
			
			this.query(sql, new Object[] { keyId, appId, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					String site_name = SiteCache.get().getKey(site_id).getSiteName();
					String m_data = rs.getString("m_data");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);

					Map<String, KeyValuePo> timeMap = siteMap.get(site_name);

					if (timeMap == null) {
						timeMap = new HashMap<String, KeyValuePo>();
						siteMap.put(site_name, timeMap);
					}

					KeyValuePo po = timeMap.get(time);
					if (po == null) {
						po = new KeyValuePo();
						timeMap.put(time, po);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						po.setKeyName(key_name);
					}
					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}

		Map<String, List<KeyValuePo>> map = new HashMap<String, List<KeyValuePo>>();

		for (Map.Entry<String, Map<String, KeyValuePo>> entry : siteMap.entrySet()) {
			List<KeyValuePo> poList = new ArrayList<KeyValuePo>();
			poList.addAll(entry.getValue().values());
			map.put(entry.getKey(), poList);
		}

		return map;
	}

	/**
	 * 
	 * ��ȡӦ�� key ��ĳ��ʱ���ڵ�ȫ����
	 * ����keyId ȡ�� ĳ���ȫ������ ���ݺ�׺ Constants.COUNT_TIMES_FLAG ���ۼ�
	 * Constants.AVERAGE_USERTIMES_FLAG ��ƽ��
	 * 
	 * �������ݿ����Ƕ�̨�������Ὣ��̨������ƽ��
	 * 
	 * 
	 */
	public List<KeyValuePo> findKeyValueByRangeDate(final int appId, final int keyId, java.util.Date start, java.util.Date end) {

		String sql = "select c.collect_time,c.m_data,c.site_id from " + getTableName(start)
				+ " c where c.key_id =? and c.app_id=? and c.collect_time between ? and ?";
		final List<KeyValuePo> poList = new ArrayList<KeyValuePo>();

		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");

		final Map<String, KeyValuePo> timeMap = new HashMap<String, KeyValuePo>();

		try {
			final String key_name = KeyCache.get().getKey(keyId).getKeyName();
			final String app_name = AppCache.get().getKey(appId).getAppName();
			this.query(sql, new Object[] { keyId, appId, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					String m_data = rs.getString("m_data");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);
					KeyValuePo po = timeMap.get(time);
					if (po == null) {
						po = new KeyValuePo();
						timeMap.put(time, po);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						po.setKeyName(key_name);
						po.setKeyId(keyId);
						po.setAppId(appId);
						po.setAppName(app_name);
					}
					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		poList.addAll(timeMap.values());
		Collections.sort(poList);
		return poList;
	}
	
	
	/**
	 * 
	 * ��ȡӦ�� key ��ĳ��ʱ���ڵ�ȫ����
	 * ����keyId ȡ�� ĳ���ȫ������ ���ݺ�׺ Constants.COUNT_TIMES_FLAG ���ۼ�
	 * Constants.AVERAGE_USERTIMES_FLAG ��ƽ��
	 * 
	 * �������ݿ����Ƕ�̨�������Ὣ��̨������ƽ��
	 * 
	 * return Map<time,value>
	 * 
	 */
	public Map<String, KeyValuePo> findKeyValueMapByRangeDate(final int appId, final int keyId, java.util.Date start, java.util.Date end) {

		String sql = "select c.collect_time,c.m_data,c.site_id from " + getTableName(start)
				+ " c where c.key_id =? and c.app_id=? and c.collect_time between ? and ?";

		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");

		final Map<String, KeyValuePo> timeMap = new HashMap<String, KeyValuePo>();

		try {
			final String key_name = KeyCache.get().getKey(keyId).getKeyName();
			final String app_name = AppCache.get().getKey(appId).getAppName();
			this.query(sql, new Object[] { keyId, appId, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					String m_data = rs.getString("m_data");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);
					KeyValuePo po = timeMap.get(time);
					if (po == null) {
						po = new KeyValuePo();
						timeMap.put(time, po);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						po.setKeyName(key_name);
						po.setKeyId(keyId);
						po.setAppId(appId);
						po.setAppName(app_name);
					}
					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		return timeMap;
	}
	
	/**
	 * ��ȡӦ�� key ��һ�����еĲɼ����� map����
	 * map �е�key ��ʾʱ�� HH��mm
	 * @param keyId
	 * @param appId
	 * @param start
	 * @return Map<HH:mm, KeyValuePo>
	 */
	public Map<String, KeyValuePo> findKeyValueByDate(final int appId, final int keyId, java.util.Date start) {

		String sql = "select c.collect_time,c.m_data,c.site_id from " + getTableName(start)+ " c where c.key_id =? and c.app_id=? ";

		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");

		final Map<String, KeyValuePo> timeMap = new HashMap<String, KeyValuePo>();

		try {
			
			final String key_name = KeyCache.get().getKey(keyId).getKeyName();
			final String app_name = AppCache.get().getKey(appId).getAppName();
			
			this.query(sql, new Object[] { keyId, appId }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					String m_data = rs.getString("m_data");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);
					KeyValuePo po = timeMap.get(time);
					if (po == null) {
						po = new KeyValuePo();
						timeMap.put(time, po);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						po.setKeyName(key_name);
						po.setKeyId(keyId);
						po.setAppId(appId);
						po.setAppName(app_name);
					}
					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		return timeMap;
	}

	public String getTableName(Date date) {
		return TableNameConverUtil.formatTimeTableName(date);
	}

	public String getKeyIdByLikeName(Integer appId, String keyname) {
		String sql = "select k.key_id from ms_monitor_key k,ms_monitor_app_key r where r.app_id =? and r.key_id=k.key_id and k.key_value like ? ";
		final List<String> list = new ArrayList<String>();
		try {
			this.query(sql, new Object[] { appId, keyname + "%" }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					list.add(rs.getString("key_id"));

				}
			});
		} catch (Exception e) {
			logger.error(keyname + "��ȡ key_id ����", e);
		}
		if (list.size() == 0)
			list.add("-1");

		return Utlitites.formatArray2Sqlin(list.toArray(new String[] {}));
	}

	public String getKeyIdByLikeName(String keyname) {
		String sql = "select key_id from ms_monitor_key k where k.key_value like ? ";
		final List<String> list = new ArrayList<String>();
		try {
			this.query(sql, new Object[] { keyname + "%" }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					list.add(rs.getString("key_id"));

				}
			});
		} catch (Exception e) {
			logger.error(keyname + "��ȡ key_id ����", e);
		}
		if (list.size() == 0)
			list.add("-1");

		return Utlitites.formatArray2Sqlin(list.toArray(new String[] {}));
	}

	public int getKeyId(String keyname) throws SQLException {

		String sql = "select key_id from ms_monitor_key where key_value=?";

		return this.getIntValue(sql, new Object[] { keyname });
	}

	public int getAppId(String appname) throws SQLException {

		String sql = "select app_id from ms_monitor_app where app_name=?";

		return this.getIntValue(sql, new Object[] { appname });
	}

	/**
	 * ��ȡȫ��app
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllApp() {
		String sql = "select * from ms_monitor_app";

		final List<AppInfoPo> appList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po.setAppId(rs.getInt("app_id"));
					po.setAppName(rs.getString("app_name"));
					po.setSortIndex(rs.getInt("sort_index"));
					po.setFeature(rs.getString("feature"));
					po.setOpsName(rs.getString("ops_name"));
					appList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		Collections.sort(appList);
		return appList;
	}

	/**
	 * ���� key ���� ģ����ѯ��������ص� key ��Ϣ���������б�
	 * 
	 * @param name
	 * @return
	 */
	public List<KeyPo> findKeyLikeName(String keyname, Integer appId) {

		String sql = "select k.* from ms_monitor_key k,MS_MONITOR_APP_KEY ak where ak.key_id=k.key_id and ak.app_id=? ";
		List<Object> params = new ArrayList<Object>();
		if (keyname != null && !"".equals(keyname)) {
			sql += "  and k.key_value like '%" + keyname + "%'";
		}
		params.add(appId);
		final List<KeyPo> keyList = new ArrayList<KeyPo>();

		try {
			this.query(sql, params.toArray(), new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setDefaultconfig(rs.getString("defaultconfig"));
					po.setFeature(rs.getString("feature"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					keyList.add(po);

				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keyList;
	}

	/**
	 * ����ǰ׺���� like ��������Щǰ׺��key ��value, ����ms_monitor_data_limit ���в�ѯ�����һ����¼
	 * ����Ǹ�ǰ��ΪOTHER ʹ�� �ṹΪ OTHER_keytype_keyName_type
	 * 
	 * @param Prefix
	 * @return
	 */
	public Map<String, OtherKeyValueVo> findKeyValueByLikeOtherFromLimitTable(String appname) {

		Map<String, OtherKeyValueVo> voMap = new HashMap<String, OtherKeyValueVo>();

		Map<String, List<KeyValuePo>> map = findLikeKeyByLimit("OTHER_", appname);
		for (Map.Entry<String, List<KeyValuePo>> entry : map.entrySet()) {

			String key_value = entry.getKey();
			KeyValuePo po = entry.getValue().get(0);
			Date date = po.getCollectTime();

			String[] keyStructs = key_value.split("_");
			if (keyStructs.length != 4) {
				continue;
			}//

			// String perfix = keyStructs[0];
			String keyType = keyStructs[1];
			String keyName = keyStructs[2];
			// String valuetype = keyStructs[3];

			OtherKeyValueVo vo = voMap.get(keyType);
			if (vo == null) {
				vo = new OtherKeyValueVo();
				voMap.put(keyType, vo);
				vo.setTypeName(keyType);
			}
			OtherHaBoLogRecord record = vo.getKeyMap().get(keyName);

			if (record == null) {
				record = new OtherHaBoLogRecord();
				vo.getKeyMap().put(keyName, record);
			}

			record.setTypeName(keyType);
			record.setKeyName(keyName);
			record.setCollectTime(date);

			if (key_value.indexOf(Constants.COUNT_TIMES_FLAG) > -1) {
				KeyValuePo countpo = record.getExeCount();
				if (countpo == null) {
					record.setExeCount(po);
				}

			} else if (key_value.indexOf(Constants.AVERAGE_USERTIMES_FLAG) > -1) {
				KeyValuePo averagePo = record.getAverageExeTime();
				if (averagePo == null) {
					record.setAverageExeTime(po);
				}
			}
		}

		return voMap;
	}

	/**
	 * ɾ����صļ�¼
	 * @param appId
	 */
	public boolean deleteMonitorData(int appId, String time) {

		try {
			String tableTime = time.replaceAll("-", "").substring(0, 8);
			String sql = "delete from MS_MONITOR_DATA_" + tableTime +" where app_id=?" ;
			this.execute(sql, new Object[] { appId }, DbRouteManage.get().getDbRouteByTimeAppid((int)appId));
		} catch (SQLException e) {
			logger.error("deleteMonitorData: ", e);
			return false;
		}
		return true;
	}
	
	public void addMonitorData(String time, long app, long site, long key, long value) {
		try {
			String tableTime = time.replaceAll("-", "").substring(0, 8);
			String sql = "insert into MS_MONITOR_DATA_" + tableTime
					+ "(APP_ID,SITE_ID,KEY_ID,M_DATA,COLLECT_TIME) values(?,?,?,?,?)";
			this.execute(sql, new Object[] { app, site, key, value, time }, DbRouteManage.get().getDbRouteByTimeAppid((int)app));
		} catch (Exception e) {
			logger.error("addMonitorData ����,", e);
		}

	}

	/**
	 * ��ѯ������key
	 * 
	 * @return
	 */
	public Map<Integer, KeyPo> findAllKey() {
		String sql = "select * from ms_monitor_key";

		final Map<Integer, KeyPo> map = new HashMap<Integer, KeyPo>();

		try {
			this.query(sql, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					map.put(po.getKeyId(), po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllKey ����,", e);
		}
		return map;
	}
	
	
	/**
	 * ��ѯ������key
	 * 
	 * @return
	 */
	public Map<Integer, SitePo> findAllSite() {
		String sql = "select * from ms_monitor_site";

		final Map<Integer, SitePo> map = new HashMap<Integer, SitePo>();

		try {
			this.query(sql, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					SitePo po = new SitePo();
					po.setSiteId(rs.getInt("site_id"));
					po.setSiteName(rs.getString("site_value"));
					map.put(po.getSiteId(), po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllKey ����,", e);
		}
		return map;
	}

	/**
	 * ��������������5����һ��
	 * 
	 * @param time
	 * @param app
	 * @param site
	 * @param key
	 * @param value
	 * @param gmt_create
	 */
	public void addMonitorData(String time, long app, long site, long key, long value, String gmt_create) {
		try {
			String tableTime = time.replaceAll("-", "").substring(0, 8);
			String sql = "insert into MS_MONITOR_DATA_" + tableTime
					+ "(APP_ID,SITE_ID,KEY_ID,M_DATA,COLLECT_TIME) values(?,?,?,?,?)";
			this.execute(sql, new Object[] { app, site, key, value, gmt_create }, DbRouteManage.get().getDbRouteByTimeAppid((int)app));
		} catch (Exception e) {
			logger.error("addMonitorData ����,", e);
		}

	}

	/**
	 * ȡ������Ӧ�õ�key
	 * 
	 * @param appId
	 * @return
	 */
	public List<KeyPo> findAllAppKey(int appId) {

		String sql = "select k.* from ms_monitor_app_key ak ,ms_monitor_key k where ak.app_id=? and ak.key_id=k.key_id";

		final List<KeyPo> poList = new ArrayList<KeyPo>();

		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {

					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					poList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		return poList;

	}
	
	/**
	 * ��ȡһ��Ӧ��ʱ���������ʹ�õļ�ص�
	 * 
	 * @param appId
	 * @param collectTime
	 * @return
	 */
	public List<KeyPo> findAppAllMonitorKey(int appId, Date collectTime) {
		String sql = "select distinct key_id from "	+ getTableName(collectTime) + " where app_id=?";

		final List<KeyPo> keypoList = new ArrayList<KeyPo>();

		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int key_id = rs.getInt("key_id");
					String key_name = KeyCache.get().getKey(key_id).getKeyName();
					KeyPo po = new KeyPo();
					po.setKeyId(key_id);
					po.setKeyName(key_name);
					keypoList.add(po);
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		return keypoList;
	}

	/**
	 * ��ȡ����ʷ��¼������һ��
	 * 
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public float getKeyMaxHistoryValue(int appId, int keyId) {
		String sql = "select m_data from MS_MONITOR_KEY_MAX_VALUE where app_id=? and key_id = ?";
		try {
			return this.getFloatValue(sql, new Object[] { appId, keyId });
		} catch (SQLException e) {
			logger.error("", e);
		}
		return -1;
	}

	/**
	 * �����ʷ��¼������һ��
	 * 
	 * @param appId
	 * @param keyId
	 * @param value
	 */
	public void addMaxHistoryValue(int appId, int keyId, float value) {
		String sql = "insert into MS_MONITOR_KEY_MAX_VALUE(app_id,key_id,m_data) values(?,?,?)";
		try {
			this.execute(sql, new Object[] { appId, keyId, value });
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

	/**
	 * �޸���ʷ��¼������һ��
	 * 
	 * @param appId
	 * @param keyId
	 * @param value
	 */
	public void updateMaxHistoryValue(int appId, int keyId, float value) {
		String sql = "update MS_MONITOR_KEY_MAX_VALUE set m_data=? where app_id=? and key_id=? ";
		try {
			this.execute(sql, new Object[] { value, appId, keyId });
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	
	/**
	 * ��ȡ���е�ǰ����
	 * @return
	 */
	public List<ReportInfoPo> findAllReport(){
		String sql = "select * from ms_monitor_report";
		
		final List<ReportInfoPo> list = new ArrayList<ReportInfoPo>();
		try {
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					ReportInfoPo po = new ReportInfoPo();
					po.setId(rs.getInt("id"));
					po.setName(rs.getString("report_name"));
					po.setType(rs.getInt("report_type"));
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return list;
	}
	
	
	
	public void addAppKeyRelation(int keyId,int appId) {
		String sql = "insert into MS_MONITOR_APP_KEY(KEY_ID,APP_ID) values(?,?)";
		try {
			this.execute(sql, new Object[] { keyId,appId });
		} catch (SQLException e) {
			logger.error("addAppKeyRelation ����", e);
		}
	}
	
	
	/**
	 * ����ҵ���ṹ
	 * @param date
	 * @param dbName
	 * @throws SQLException 
	 */
	public void createTimeTable(Date date,String dbName) throws SQLException{
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyyMMdd");
		String collectdate = parseLogFormatDate.format(date);
		
		
		String delete = "drop table if exists ms_monitor_data_" + collectdate+";";
		this.execute(delete, DbRouteManage.get().getDbRouteByRouteId(dbName));
		
		String sql = "create table ms_monitor_data_" + collectdate + "(" + " app_id int not null,"
				+ "key_id int not null," + "site_id int not null,"
				+ "m_data varchar(64)," + "collect_time datetime not null"
				+ ")engine=MyISAM default charset=gbk;";
		this.execute(sql, DbRouteManage.get().getDbRouteByRouteId(dbName));
		String index = "create index idx_mmd_" + collectdate + " on ms_monitor_data_" + collectdate
				+ "(app_id,key_id,collect_time);";
		this.execute(index, DbRouteManage.get().getDbRouteByRouteId(dbName));
		
	}
	
	/**
	 * ����appid��ȡ��ʱ���д�app�ļ�¼����
	 * @param appId
	 */
	
	public int findAppCountInLimit(int appId) {
		
		int count = 0;		
		String sql = "select count(*) from ms_monitor_data_limit where app_id=?";		
		try {
			count = this.getIntValue(sql, new Object[] {appId}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (SQLException e) {
			logger.error("findAppCountInLimit ����", e);
		}		
		return count;
	}
	/**
	 * ����ʱ���л�ȡӦ�õ�ĳһkey����������
	 * ����������ƽ��
	 * @param appId
	 * @param keyId
	 * @return keyΪʱ��  HH:mm 
	 */
	public Map<String, KeyValuePo> findKeyValueInLimit(final int appId, final int keyId) {
		String sql = "select c.collect_time,c.m_data,c.site_id from ms_monitor_data_limit c where c.key_id =? and c.app_id=? ";
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");

		final Map<String, KeyValuePo> timeMap = new HashMap<String, KeyValuePo>();

		try {
			
			final String key_name = KeyCache.get().getKey(keyId).getKeyName();
			final String app_name = AppCache.get().getKey(appId).getAppName();
			
			this.query(sql, new Object[] { keyId, appId }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					int site_id = rs.getInt("site_id");
					String m_data = rs.getString("m_data");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					String time = parseLogFormatDate.format(date);
					KeyValuePo po = timeMap.get(time);
					if (po == null) {
						po = new KeyValuePo();
						timeMap.put(time, po);
						po.setCollectTime(date);
						po.setCollectTimeStr(time);
						po.setKeyName(key_name);
						po.setKeyId(keyId);
						po.setAppId(appId);
						po.setAppName(app_name);
					}
					Double d1 = Double.parseDouble(m_data);
					po.putValue(site_id, d1);
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		return timeMap;
	}
	
	/***
	 * ����keyΪʱ��millis��valueΪ��site��һʱ��m_dataƽ��ֵ��ʱ��������start��end��map
	 * Ϊ��Ҫurl����ʾ���룬��������Ӧ����ע���Ƿ����㳡��
	 * @author youji.zj
	 * 
	 * @param appId
	 * @param keyId
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<String, Double> findKeyValueInTime(final int appId, final int keyId, Date start, Date end) {
		String sql = "SELECT SUM(m_data)  AS tData,COUNT(site_id) AS siteNum,collect_time AS cTime FROM " + 
			getTableName(start) + " WHERE app_id=? AND key_id=? AND collect_time >= ? AND collect_time < ? " + 
			"GROUP BY collect_time ORDER BY collect_time ";

		final Map<String, Double> data = new HashMap<String, Double>();
		final SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		try {
					
			this.query(sql, new Object[] { appId, keyId, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					String tData = rs.getString("tData");
					String siteNum = rs.getString("siteNum");
					Date cTime = rs.getTimestamp("cTime");

					
					BigDecimal aveData = (new BigDecimal(tData)).divide(new BigDecimal(siteNum), 2, BigDecimal.ROUND_HALF_UP);
					data.put(parseLogFormatDate.format(cTime), aveData.doubleValue());
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		return data;
	}
	
	/**
	 * ����appid��ȡ������ʱ��-���Сʱ~~~����ʱ��)�������ڼ�¼������
	 * @param appId
	 * @param date
	 */
	
	public int findAppCountInData(int appId, Date date) {
		int count = -1;
		//SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);		
		cal.add(Calendar.MINUTE, -30);
		//int count = 0;		
		String sql = "select count(*) from "+getTableName(date)+" where app_id=?  and collect_time between ? and ?";		
		try {
			count = this.getIntValue(sql, new Object[] {appId, cal.getTime(), date}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (SQLException e) {
			logger.error("findAppCountInData ����", e);
		}		
		return count;
	}
	
	/***
	 * ��ȡָ��ʱ���ڶ�ӦappId,keyId��ͳ������,��site_id���з���
	 * @param appId
	 * @param keyId
	 * @param startTime
	 * @param endTime
	 * @param isLimit
	 * @return
	 */
	public Map<String, BigDecimal> findToalInRangeDate(final int appId, final int keyId, java.util.Date startTime, java.util.Date endTime, boolean isLimit) {
		String tableName = isLimit ? "ms_monitor_data_limit" : getTableName(startTime);
		final Map<String, BigDecimal> dataMap = new HashMap<String, BigDecimal>();
		String sql = "select SUM(CONVERT(c.m_data,DECIMAL(17,2))) AS total_data, c.site_id AS site_id from " + tableName
		+ " c where c.key_id =? and c.app_id=? and c.collect_time between ? and ? " + " group by c.site_id";

		try {
			this.query(sql, new Object[] { keyId, appId, startTime, endTime }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {

					String total_data = rs.getString("total_data");
					String site_id = rs.getString("site_id");
					dataMap.put(site_id, new BigDecimal(total_data));
					
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return dataMap;
	}
	
	/***
	 * ��ȡ�����������ݵ��ռ�ʱ��
	 * @param appId
	 * @param keyId
	 * @return
	 */
	public java.util.Date getLatestDataInLimit(final int appId, final int keyId) {
		final List<java.util.Date> maxDate = new ArrayList<java.util.Date>();
		String sql = "SELECT MAX(c.collect_time) AS max_date FROM ms_monitor_data_limit AS c where " + 
			"c.key_id = ? and c.app_id = ? ";
		
		try {
			this.query(sql, new Object[] { keyId, appId }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {

					java.util.Date max_date = rs.getTimestamp("max_date");
					maxDate.add(max_date);
					
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("", e);
		}
		
		if (maxDate.size() > 0 && maxDate.get(0) != null) {
			return maxDate.get(0);
		}
		
		return Calendar.getInstance().getTime();
	}

}
