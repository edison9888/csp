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
	 * ���һ���ռ�������Ϣ
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
	 * ������������� �����5����¼
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
	 * ɾ����ʱ���е�����
	 * @param date
	 */
//	public void deleteMonitorLimit(Date date){
//		monitorTimeDao.deleteMonitorLimit(date);
//	}
	/**
	 * ɾ����ʱ���е�����
	 * @param date
	 */
	public void deleteMonitorLimit(int appId,Date date){
		monitorTimeDao.deleteMonitorLimit(appId,date);
	}
	/**
	 * ȡ�����м�س־û����ݵ�ip��Ϣ
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
	 * ����ʵʱ�ձ�
	 * 
	 * @param collectdate
	 *            yyyyMMdd
	 * @throws SQLException
	 */
	public void createDateTable(String collectdate) throws SQLException {
		monitorTimeDao.createDateTable(collectdate);
	}
	
	
	/**
	 * ��ȡӦ�� key ��һ��ĳ�εĲɼ����� 
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
