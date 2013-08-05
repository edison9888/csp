package com.taobao.monitor.common.ao.time;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.db.impl.time.MonitorTimeDao;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.po.MonitorDetail;
import com.taobao.monitor.common.po.MonitorSite;

public class MonitorTimeAo {
	
	private static MonitorTimeAo ao = new MonitorTimeAo();
	
	private MonitorTimeDao monitorTimeDao = new MonitorTimeDao();
	
	private MonitorTimeAo(){}
	
	public static MonitorTimeAo get(){
		return ao;
	}
	
	
	/**
	 * 添加一条收集到的信息
	 * @param detail
	 * @param limit
	 */
	public void addMonitorData(MonitorDetail detail) {
		monitorTimeDao.addMonitorData(detail);
	}
	
	public void addMonitorData(int appId,List<MonitorDetail> details) {
		monitorTimeDao.addMonitorDataBatch(appId, details);
	}
	
	/**
	 * 这个表用来保存 最近的5条记录
	 * 
	 * @param detail
	 */
	public void addMonitorDatalimit(MonitorDetail detail) {
		monitorTimeDao.addMonitorDatalimit(detail);
	}
	
	public void addMonitorDatalimit(int appId,List<MonitorDetail> details) {
		monitorTimeDao.addMonitorDatalimit(appId, details);
	}
	
	/**
	 * 删除临时表中的数据
	 * @param date
	 */
//	public void deleteMonitorLimit(Date date){
//		monitorTimeDao.deleteMonitorLimit(date);
//	}
	/**
	 * 删除临时表中的数据
	 * @param date
	 */
	public void deleteMonitorLimit(int appId,Date date){
		monitorTimeDao.deleteMonitorLimit(appId,date);
	}
	/**
	 * 取得所有监控持久化数据的ip信息
	 * @return
	 */
	public List<MonitorSite> findAllMonitorSite() {
		return monitorTimeDao.findAllMonitorSite();
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public MonitorSite addMonitorSite(String key){
		return monitorTimeDao.addMonitorSite(key);
	}
	
	
	/**
	 * 创建实时日表
	 * 
	 * @param collectdate
	 *            yyyyMMdd
	 * @throws SQLException
	 */
	public void createDateTable(String collectdate) throws SQLException {
		monitorTimeDao.createDateTable(collectdate);
	}
	
	
	/**
	 * 获取应用 key 的一天某段的采集数据 
	 * @param appId
	 * @param keyId
	 * @param start
	 * @param end
	 * @return
	 */
	public Map<Long, List<Double>> findKeyValueByRangeDate(int appId, int keyId, java.util.Date start, java.util.Date end){
		return monitorTimeDao.findKeyValueByRangeDate(appId, keyId , start, end);
	}

}
