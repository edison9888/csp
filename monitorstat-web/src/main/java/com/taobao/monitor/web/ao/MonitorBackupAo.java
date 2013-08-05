package com.taobao.monitor.web.ao;

import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.core.dao.impl.MonitorBackupDao;
import com.taobao.monitor.common.po.KeyValuePo;

public class MonitorBackupAo {
	private static final Logger logger =  Logger.getLogger(MonitorBackupAo.class);

	private static MonitorBackupAo  ao = new MonitorBackupAo();
	private MonitorBackupDao dao = new MonitorBackupDao();

	
	

	private MonitorBackupAo(){
		
	}


	public static  MonitorBackupAo get(){
		return ao;
	}
	
	/**
	 * ��ӱ�������
	 * @param time
	 * @param app
	 * @param key
	 * @param value
	 */
	public void addMonitorData(String time, long app,long key, long value) {
		
		dao.addMonitorData(time, app, key, value);
		
	}
	
	/**
	 * ����KeyValuePo��map�����������ݿ�
	 * @param map
	 */
	public void addMonitorData(Map<String, KeyValuePo> map) {
		
		dao.addMonitorData(map);
	}
}
