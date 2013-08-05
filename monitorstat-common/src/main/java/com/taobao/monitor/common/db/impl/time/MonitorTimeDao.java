/**
 * 
 */
package com.taobao.monitor.common.db.impl.time;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.po.MonitorDetail;
import com.taobao.monitor.common.po.MonitorSite;
import com.taobao.monitor.common.util.TableNameConverUtil;

/**
 * 实时系统的主要表操作类
 * 
 * @author xiaodu
 * @version 2010-4-16 上午11:21:55 *
 */
public class MonitorTimeDao extends MysqlRouteBase {

	private static final Logger log = Logger.getLogger(MonitorTimeDao.class);

	private static final String addLimitsql = "insert into ms_monitor_data_limit(APP_ID,SITE_ID,KEY_ID,M_DATA,COLLECT_TIME) values(?,?,?,?,?)";
	private static final String sqlldeleteTime = "delete from ms_monitor_data_limit where collect_time <?";
	private static final String sqlldeleteTime1 = "delete from ms_monitor_data_limit where collect_time <? and app_id = ?";
	private static final String addSqlDesc = " insert into ms_monitor_data_desc(key_id,app_id,site_id,collect_time,data_desc,data_value) values(?,?,?,?,?,?)";

	/**
	 * 添加信息的描述
	 * 
	 * @param detail
	 */
	private void addMonitorDataDesc(MonitorDetail detail) {
		if (detail.getMonitorDesc() != null) {
			try {
				this.execute(addSqlDesc, new Object[] { detail.getKeyId(), detail.getAppId(), detail.getSiteId(), detail.getCollectTime(),
						detail.getMonitorDesc(),detail.getValueData()},DbRouteManage.get().getDbRouteByTimeAppid(detail.getAppId()));
			} catch (SQLException e) {
				log.error("addMonitorDataDesc 出错[" + detail.toString() + "]", e);
			}
		}
	}
	
	
	private void addMonitorDataDesc(int appId,List<MonitorDetail> details) {
		List<Object[]> tmp = new ArrayList<Object[]>();
		for(MonitorDetail detail:details){
			if (detail.getMonitorDesc() != null) {
				
				Object[] o =  new Object[] { detail.getKeyId(), detail.getAppId(), detail.getSiteId(), detail.getCollectTime(),
						detail.getMonitorDesc(),detail.getValueData()};
				tmp.add(o);
			}
		}
		
		if(tmp.size() > 0 ){
			try {
				this.executeBatch(addSqlDesc,tmp,DbRouteManage.get().getDbRouteByTimeAppid(appId));
			} catch (SQLException e) {
				for(MonitorDetail detail:details)
					log.error("addMonitorDataDesc 出错[" + detail.toString() + "]", e);
			}
		}
		
	}

	/**
	 * 添加一条收集到的信息
	 * 
	 * @param detail
	 * @param limit
	 */
	public void addMonitorData(MonitorDetail detail) {
		try {
			String time = detail.getCollectTime();
			String tableTime = time.replaceAll("-", "").substring(0, 8);

			String addMonitorDataSql = "insert into MS_MONITOR_DATA_" + tableTime + "(APP_ID,SITE_ID,KEY_ID,M_DATA,COLLECT_TIME) values(?,?,?,?,?)";
			this.execute(addMonitorDataSql, new Object[] { detail.getAppId(), detail.getSiteId(), detail.getKeyId(), detail.getValueData(),
					detail.getCollectTime() }, DbRouteManage.get().getDbRouteByTimeAppid(detail.getAppId()));
			addMonitorDataDesc(detail);
		} catch (Exception e) {
			log.error("addMonitorData 出错[" + detail.toString() + "]", e);
		}
		// log.info("addMonitorDatalimit :"+limit+":"+(System.currentTimeMillis()-t));
	}
	
	
	
	private Date changeTime(String time){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			return sdf.parse(time);
		} catch (ParseException e) {
			return new Date();
		}
	}
	
	
	public void addMonitorDataBatch(int appId,List<MonitorDetail> details) {
		try {
			List<Object[]> tmp = new ArrayList<Object[]>();
			String tableTime = null;
			for(MonitorDetail detail:details){
				Object[] o = new Object[] { detail.getAppId(), detail.getSiteId(), detail.getKeyId(), detail.getValueData(),changeTime(detail.getCollectTime() )};
				tmp.add(o);
				if(tableTime == null){
					String time = detail.getCollectTime();
					tableTime = time.replaceAll("-", "").substring(0, 8);
				}
			}

			String addMonitorDataSql = "insert into MS_MONITOR_DATA_" + tableTime + "(APP_ID,SITE_ID,KEY_ID,M_DATA,COLLECT_TIME) values(?,?,?,?,?)";
			this.executeBatch(addMonitorDataSql,tmp, DbRouteManage.get().getDbRouteByTimeAppid(appId));
			
			addMonitorDataDesc(appId,details);
		} catch (Exception e) {
			for(MonitorDetail detail:details)
				log.error("addMonitorDataDesc 出错[" + detail.toString() + "] ff", e);
		}
		// log.info("addMonitorDatalimit :"+limit+":"+(System.currentTimeMillis()-t));
	}
	
	/**
	 * 这个表用来保存 最近的5条记录
	 * 
	 * @param detail
	 */
	public void addMonitorDatalimit(int appId,List<MonitorDetail> details) {

		try {
			List<Object[]> tmp = new ArrayList<Object[]>();
			for(MonitorDetail detail:details){
				Object[] o = new Object[] { detail.getAppId(), detail.getSiteId(), detail.getKeyId(), detail.getValueData(), changeTime(detail.getCollectTime()) };
				tmp.add(o);				
			}
			this.executeBatch(addLimitsql,	tmp, DbRouteManage	.get().getDbRouteByTimeAppid(appId));
			tmp = null;
		} catch (Exception e) {
			for(MonitorDetail detail:details)
				log.error("addMonitorDataDesc 出错[" + detail.toString() + "] ff", e);
		}
	}
	

	/**
	 * 这个表用来保存 最近的5条记录
	 * 
	 * @param detail
	 */
	public void addMonitorDatalimit(MonitorDetail detail) {

		try {
			this.execute(addLimitsql,
					new Object[] { detail.getAppId(), detail.getSiteId(), detail.getKeyId(), detail.getValueData(), detail.getCollectTime() }, DbRouteManage
							.get().getDbRouteByTimeAppid(detail.getAppId()));
//				this.execute(addLimitsql,
//						new Object[] { detail.getAppId(), detail.getSiteId(), detail.getKeyId(), detail.getValueData(), detail.getCollectTime() },DbRouteManage
//						.get().getDbRouteByRouteId("Branch_0"));
		} catch (Exception e) {
			log.error("addMonitorData 出错[" + detail.toString() + "]", e);
		}
	}

	/**
	 * 删除临时表中的数据
	 * 
	 * @param date
	 */
//	public void deleteMonitorLimit(Date date) {
//
//		try {
//			this.execute(sqlldeleteTime, new Object[] { date },DbRouteManage.get().getDbRouteByRouteId("Branch_0"));
//		} catch (SQLException e) {
//			log.error("deleteMonitorLimit 出错", e);
//		}
//	}

	/**
	 * 删除临时表中的数据
	 * 
	 * @param date
	 */
	public void deleteMonitorLimit(int appId, Date date) {

		try {
			this.execute(sqlldeleteTime1, new Object[] { date, appId },DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (SQLException e) {
			log.error("deleteMonitorLimit 出错", e);
		}
	}

	/**
	 * 取得所有监控持久化数据的ip信息
	 * 
	 * @return
	 */
	public List<MonitorSite> findAllMonitorSite() {

		final List<MonitorSite> appList = new ArrayList<MonitorSite>();
		try {
			this.query("select * from MS_MONITOR_SITE", new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					MonitorSite key = new MonitorSite();
					key.setId(rs.getInt("SITE_ID"));
					key.setSiteName(rs.getString("SITE_VALUE"));
					appList.add(key);

				}
			});
		} catch (Exception e) {
			log.error("读取应用列表 出错", e);
		}

		return appList;
	}

	/**
	 * 
	 * @param key
	 * @return
	 */
	public MonitorSite addMonitorSite(String key) {

		String sql = "insert into MS_MONITOR_SITE(site_value)values(?)";

		try {
			this.execute(sql, new Object[] { key });

			String select = "select * from MS_MONITOR_SITE where site_value=?";
			final MonitorSite monitorKey = new MonitorSite();
			this.query(select, new String[] { key }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int id = rs.getInt("site_id");
					String value = rs.getString("site_value");
					monitorKey.setId(id);
					monitorKey.setSiteName(value);
				}
			});
			return monitorKey;

		} catch (Exception e) {
			log.error("addMonitorKey 出错", e);
		}
		return null;
	}

	/**
	 * 创建日表
	 * 
	 * @param collectdate
	 *            yyyyMMdd
	 * @throws SQLException
	 */
	public void createDateTable(String collectdate) throws SQLException {

		String delete = "drop table if exists ms_monitor_data_" + collectdate;
		try {
			this.execute(delete);
		} catch (Exception e) {
		}
		String sql = "create table ms_monitor_data_" + collectdate + "( " +
				" app_id int not null, " +
				" key_id int not null," +
				" site_id  int not null, " +
				" m_data  varchar(64), " +
				" collect_time datetime not null" +
				")engine=MyISAM default charset=gbk;";

		String index = "create index idx_mmd_" + collectdate + " on ms_monitor_data_" + collectdate + "(app_id,key_id,collect_time)";
		this.execute(sql);
		this.execute(index);
	}
	
	
	/**
	 * 
	 * 获取应用 key 的某段时间内的map
	 * map<time, 当前时间对应数值的list>
	 * 
	 */
	public Map<Long, List<Double>> findKeyValueByRangeDate(final int appId, final int keyId, java.util.Date start, java.util.Date end) {

		String sql = "select c.collect_time,c.m_data from " + getTableName(start)
				+ " c where c.key_id =? and c.app_id=? and c.collect_time between ? and ?";
		
		
		final Map<Long, List<Double>> timeMap = new HashMap<Long, List<Double>>();

		try {
			this.query(sql, new Object[] { keyId, appId, start, end }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					String m_data = rs.getString("m_data");
					Timestamp collectTime = rs.getTimestamp("collect_time");
					Date date = new Date(collectTime.getTime());
					Long time = date.getTime();
					List<Double> po = timeMap.get(time);
					if (po == null) {
						po = new ArrayList<Double>();
						timeMap.put(time, po);
					}
					po.add(Double.parseDouble(m_data));
				}
			}, DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			log.error("", e);
		}
		return timeMap;
	}
	
	public String getTableName(Date date) {
		return TableNameConverUtil.formatTimeTableName(date);
	}
	
}
