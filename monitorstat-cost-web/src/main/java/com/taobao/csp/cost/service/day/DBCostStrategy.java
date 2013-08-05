package com.taobao.csp.cost.service.day;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.taobao.csp.cost.dao.CspTddlDependDao;
import com.taobao.csp.cost.util.CostConstants;
import com.taobao.csp.cost.util.LocalUtil;

/**
 * DB的总成本计算
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-17
 */
public class DBCostStrategy implements ICostStrategy {
	
	public static Logger logger = Logger.getLogger(DBCostStrategy.class);
	
	private Map<String, Long> dbQps = new HashMap<String, Long>();

	@Resource(name = "cspTddlDependDao")
	private CspTddlDependDao cspTddlDependDao;
	
	private Map<String, Set<String>> dbMachines = new HashMap<String, Set<String>>();
	
	/*** db1$detail, 1000 ***/
	private Map<String, Long> depCall = new HashMap<String, Long>();

	/**
	 * 返回结果：
	 * 1.db-->被调用总次数
	 * 2.db$app--->调用次数
	 * 3.db的机器数
	 */
	@Override
	public void caculateCost(Date date,int tableId) {
		
		//<dbName,callSum>
		dbQps.clear();
		dbQps = cspTddlDependDao.getDbConsumerSummary(date);
		
		//<dbName$app,callSum>
		depCall.clear();
		for (String db : dbQps.keySet()) {
			Map<String, Long> dep = cspTddlDependDao.getDbDepSummary(db,date);
			depCall.putAll(dep);
		}
		
		//<dbName,[ip,ip,ip]>
		dbMachines = cspTddlDependDao.getDbMachines(tableId);
		
		//initCustomizationData();
	}
	
	@Override
	public Map<String, Long> getCallNum() {
		return dbQps;
	}

	@Override
	public Map<String, Long> getCallDep() {
		return depCall;
	}
	
	@Override
	public int getMachineNum(String dbName) {
		int num = 0;
		if (dbMachines != null && dbMachines.containsKey(dbName) && dbMachines.get(dbName) != null) {
			num = dbMachines.get(dbName).size();
		}
		
		return num;
	}

	public Set<String> getMachines(String name) {
		return dbMachines.get(name);
	}
	@Override
	public CostType getCostType() {
		return CostType.DB;
	}
//	
//	private void initCustomizationData() {
//		try {
//			// 初始化自定义的db信息，苦逼了，北斗的库好卡啊，导出来还要计算
//			InputStream inputStream = DBCostStrategy.class.getClassLoader().getResourceAsStream("db.txt");
//			InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
//			BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//			
//			String line = null;
//			while ((line = bufferedReader.readLine()) != null) {
//				String [] values = line.split(" ");
//				if (values.length != 4) continue;
//				
//				String appName = values[0];
//				String dbGroup = values[1];
//				int machineNum = Integer.parseInt(values[2]);
//				long pv = Long.parseLong(values[3]);
//				
//				dbQps.put(dbGroup, pv);
//				depCall.put(dbGroup + CostConstants.DEP_SEP + appName, pv);
//				Set<String> machines = new HashSet<String>();
//				for (int i = 0; i < machineNum; i++) {
//					machines.add(String.valueOf(i));
//				}
//				dbMachines.put(dbGroup, machines);
//			}
//			
//		} catch (Exception e) {
//			logger.error("init customization db error", e);
//		}
//	}
	
}
