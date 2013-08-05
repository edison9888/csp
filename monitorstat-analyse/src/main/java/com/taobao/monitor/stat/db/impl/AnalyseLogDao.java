package com.taobao.monitor.stat.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.util.TableNameConverUtil;
import com.taobao.monitor.common.util.TimeConvertUtil;
import com.taobao.monitor.stat.db.po.MonitorDetail;
import com.taobao.monitor.stat.db.po.MonitorKey;


public class AnalyseLogDao extends MysqlRouteBase {
	
	private static  Logger log = Logger.getLogger(AnalyseLogDao.class);
		
	
	public AnalyseLogDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("Main_DAY"));
	}
	
	
//	/**
//	 * 取得所有应用对象
//	 * @return
//	 */
//	public List<AppPo> findAllAppNameList() {
//		
//		final List<AppPo> appList = new ArrayList<AppPo>();
//		
//		try {
//			this.query("select * from MS_MONITOR_APP", new SqlCallBack(){
//				public void readerRows(ResultSet rs) throws Exception {
//					AppPo po = new AppPo();
//					int id = rs.getInt("APP_ID");
//					String name = rs.getString("APP_NAME");
//					String feature = rs.getString("feature");
//					String app_type = rs.getString("app_type");
//					int sortIndex = rs.getInt("sort_index");
//					po.setId(id);
//					po.setName(name);
//					po.setAppType(app_type);
//					po.setFeature(feature);
//					po.setSortIndex(sortIndex);
//					appList.add(po);
//					
//				}});
//		} catch (Exception e) {
//			log.error("读取应用列表 出错",e);
//		}
//		return appList;
//	}
	
	/**
	 * 取得所有的key对象
	 * @return
	 */
	public List<MonitorKey> findAllMonitorKey(){
		
		final List<MonitorKey> appList = new ArrayList<MonitorKey>();		
		try {
			this.query("select * from MS_MONITOR_KEY", new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					MonitorKey key = new MonitorKey();
					key.setId(rs.getInt("KEY_ID"));
					key.setKeyName(rs.getString("KEY_VALUE"));
					appList.add(key);
					
				}});
		} catch (Exception e) {
			log.error("读取应用列表 出错",e);
		}
		
		
		return appList;
	}
	
	/**
	 * 添加key
	 * @param key
	 * @return
	 */
	public MonitorKey addMonitorKey(String key){		
		String sql ="insert into MS_MONITOR_KEY(key_value)values(?)";
		int count = 0;
		String select = "select * from MS_MONITOR_KEY where key_value=?";
		try {			
			count = this.getIntValue(select, new String[]{key});
			if(count == 0)
			{
				this.execute(sql,new Object[]{key});											
			}
			final MonitorKey monitorKey = new MonitorKey();			
			this.query(select,new String[]{key}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					int id = rs.getInt("key_id");
					String value = rs.getString("key_value");
					monitorKey.setId(id);
					monitorKey.setKeyName(value);
				}});
			return monitorKey;			
		} catch (Exception e) {				
			try {
				final MonitorKey monitorKey1 = new MonitorKey();
				this.query(select,new String[]{key}, new SqlCallBack(){
					public void readerRows(ResultSet rs) throws Exception {
						int id = rs.getInt("key_id");
						String value = rs.getString("key_value");
						monitorKey1.setId(id);
						monitorKey1.setKeyName(value);
					}});
				return monitorKey1;
			} catch (Exception e1) {
				log.error("addMonitorKey 出错",e1);	
				return null;
			}	
			
		}
	}
	
	
	/**
	 * 添加报表数据
	 * @param detail
	 */
	public void addMonitorData(MonitorDetail detail){
		
		String collectTime = detail.getCollectTime();		
		Date date = TimeConvertUtil.parseStrToDayByFormat(collectTime, "yyyy-MM-dd HH:mm");
		int i = date.getMinutes();
		
		if(i % 5 != 0){
			return ;
		}
		
		
		String tableName = TableNameConverUtil.formatDayTableName(date);
		
		
				
		String sql = "insert into "+tableName+"(APP_ID,KEY_ID,M_DATA,COLLECT_TIME,GMT_CREATE) values(?,?,?,?,NOW())";
		
		try {
			this.execute(sql, new Object[]{detail.getAppId(),detail.getKeyId(),detail.getValueData(),detail.getCollectTime()});
		} catch (Exception e) {
			log.error("addMonitorData 出错["+detail.toString()+"]",e);
		}
		
	}
	
	
	/**
	 * 添加汇总数据
	 * @param detail
	 */
	public void addMonitorDataCount(MonitorDetail detail){
		String sql = "insert into MS_MONITOR_COUNT(APP_ID,KEY_ID,M_DATA,COLLECT_TIME,GMT_CREATE) values(?,?,?,?,NOW())";
		
		try {
			this.execute(sql, new Object[]{detail.getAppId(),detail.getKeyId(),detail.getValueData(),detail.getCollectTime()});
		} catch (Exception e) {
			log.error("addMonitorDataCount 出错["+detail.toString()+"]",e);
		}
	}
	
	
	/**
	 * c应用的流量分析
	 * @param provider_appId
	 * @param customer_appId
	 * @param keyId
	 * @param cm_ip
	 * @param call_num
	 * @param use_time
	 * @param collectDate
	 */
	public void addMonitorProvider(String provider_app,String customer_app, String keyName, String cm_ip,String cm, Long call_num, Double use_time,
			String collectDate){
		String sql = "insert into ms_monitor_distribe_provider(provider_app,key_name,customer_app,customer_machine_ip,customer_machine_cm,call_num,use_time,collect_date) values(?,?,?,?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[]{provider_app,keyName,customer_app,cm_ip,cm,call_num,use_time,collectDate}, DbRouteManage.get().getDbRouteByRouteId("Main_Jprof"));
		} catch (SQLException e) {
			log.error("addMonitorProvider 出错",e);
		}
		
	}
	
	
	/**
	 * c应用的流量分析
	 * @param provider_appId
	 * @param customer_appId
	 * @param keyId
	 * @param cm_ip
	 * @param call_num
	 * @param use_time
	 * @param collectDate
	 */
	public void addMonitorCustomer(String provider_app,String customer_app, String keyName, String cm_ip,String cm, Long call_num, Double use_time,
			String collectDate){
		String sql = "insert into ms_monitor_distribe_customer(provider_app,key_name,customer_app,provider_machine_ip,provider_machine_cm,call_num,use_time,collect_date) values(?,?,?,?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[]{provider_app,keyName,customer_app,cm_ip,cm,call_num,use_time,collectDate}, DbRouteManage.get().getDbRouteByRouteId("Main_Jprof"));
		} catch (SQLException e) {
			log.error("addMonitorSplitCount 出错",e);
		}
		
	}
	

}
