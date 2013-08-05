package com.taobao.monitor.web.ao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.web.core.dao.impl.MonitorTddlDao;
import com.taobao.monitor.web.tddl.DBTddlData;
import com.taobao.monitor.web.tddl.TddlPo;

/**
 * 对tddl数据进行操作的ao类
 * @version 2011-10-31
 */

public class MonitorTddlAo {
	
	private static MonitorTddlAo monitorTddlAo = new MonitorTddlAo();
	
	private MonitorTddlAo() {
		
	}
	
	public static MonitorTddlAo getInstence() {
		return monitorTddlAo;
	}
	
	private MonitorTddlDao dao = new MonitorTddlDao();
	
	/**
	 * 查询当前应用、当前时间所有的记录
	 * @param appId
	 * @param date
	 * @return
	 */
		
	public List<TddlPo> findTddlListByApp(String opsName, String date) {
		return dao.findTddlListByApp(opsName, date);
	}
	
	/**
	 * 查询当前应用、当前时间所有的记录
	 * @param appId
	 * @param date
	 * @return map<DBName, List<访问量前十的Po>>
	 */
	public Map<String, List<TddlPo>> findDBNameMapByApp(String opsName, String date) {
		
		Map<String, List<TddlPo>> map = new HashMap<String, List<TddlPo>>();
		
		List<TddlPo> tddlList = dao.findTddlListByApp(opsName, date);
		
		for (TddlPo po : tddlList) {
			String dbName = po.getDbName();
			List<TddlPo> dbTddlList = map.get(dbName);
			if (dbTddlList == null) {
				dbTddlList = new ArrayList<TddlPo>();
				map.put(dbName, dbTddlList);
			}
			dbTddlList.add(po);
		}
		
		for (Map.Entry<String, List<TddlPo>> entry : map.entrySet()) {
			String dbName = entry.getKey();
			List<TddlPo> dbTddlList = entry.getValue();
			Collections.sort(dbTddlList);
			if (dbTddlList.size() > 10) {
				dbTddlList.subList(9, dbTddlList.size()).clear();
			}
		}
		
		return map;
	}
	
	/**
	 * 查询当前应用、当前时间所有的记录
	 * @param appId
	 * @param date
	 * @return map<type,map<DBName, List<访问量前十的Po>>>
	 */
	public Map<String, Map<String, DBTddlData>> findTypeMapByApp(String opsName, String date) {
		
		Map<String, Map<String, DBTddlData>> map = new HashMap<String, Map<String, DBTddlData>>();
		
		List<TddlPo> tddlList = dao.findTddlListByApp(opsName, date);
		
		for (TddlPo po : tddlList) {
			String dbName = po.getDbName();
			String type = po.getExecuteType();
			Map<String, DBTddlData> typeMap = map.get(type);
			if (typeMap == null) {
				typeMap = new HashMap<String, DBTddlData>();
				map.put(type, typeMap);
			}
			DBTddlData dbTddlData = typeMap.get(dbName);
			if (dbTddlData == null) {
				dbTddlData = new DBTddlData();
				typeMap.put(dbName, dbTddlData);
			}
			dbTddlData.addTopList(po);
			dbTddlData.addExeTimes(po.getExecuteSum());
		}
		
		for (Map.Entry<String, Map<String, DBTddlData>> entry : map.entrySet()) {
			String type = entry.getKey();
			Map<String, DBTddlData> dbTddlMap = entry.getValue();
			for (Map.Entry<String, DBTddlData> entry1 : dbTddlMap.entrySet()) {
				DBTddlData dbTddlData = entry1.getValue();
				List<TddlPo> dbTddlList = dbTddlData.getTopTddlList();
				Collections.sort(dbTddlList);
				if (dbTddlList.size() > 10) {
					dbTddlList.subList(9, dbTddlList.size()).clear();
				}
			}
			
		}
		
		return map;
	}
	
	/**
	 * 查询当前应用、当前时间所有的记录
	 * @param appId
	 * @param date
	 * @return
	 */
		
	public List<TddlPo> findTddlListByInfo(String opsName, String dbName, String type, String date) {
		List<TddlPo> dbTddlList = dao.findTddlListByInfo(opsName, dbName, type, date);
		Collections.sort(dbTddlList);
		return dbTddlList;
	}

}
