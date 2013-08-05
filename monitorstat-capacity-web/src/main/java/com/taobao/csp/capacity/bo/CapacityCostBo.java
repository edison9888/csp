package com.taobao.csp.capacity.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.constant.CostConstants;
import com.taobao.csp.capacity.cost.CostContext;
import com.taobao.csp.capacity.cost.CostType;
import com.taobao.csp.capacity.cost.DBCostStrategy;
import com.taobao.csp.capacity.cost.DepMode;
import com.taobao.csp.capacity.cost.HsfCostStrategy;
import com.taobao.csp.capacity.cost.PvCostStrategy;
import com.taobao.csp.capacity.cost.TairCostStrategy;
import com.taobao.csp.capacity.dao.CapacityCostDao;
import com.taobao.csp.capacity.po.CapacityCostInfoPo;

public class CapacityCostBo {
	
	private static Logger logger = Logger.getLogger(CapacityCostBo.class);
	
	private List<CostContext> costContexts = new ArrayList<CostContext>();
	
	private CyclicBarrier barrier;
	
	private CapacityCostDao capacityCostDao;
	
	/*** cost information ***/
	private Map<String, CapacityCostInfoPo> costMap = new HashMap<String, CapacityCostInfoPo>();
	
	/*** direct depend cost ***/
	private Map<String, Long> directDepCallsMap = new HashMap<String, Long>();
	
	/*** indirect depend cost ***/
	private Map<String, Long> inDirectDepCallsMap = new HashMap<String, Long>();
	
	/*** application types ***/
	private Map<String, CostType> costTypes = new HashMap<String, CostType>();
	
	private Object lockObject = new Object();
	
	public CapacityCostBo() {
		costContexts.add(new CostContext(new DBCostStrategy()));
		costContexts.add(new CostContext(new TairCostStrategy()));
		costContexts.add(new CostContext(new HsfCostStrategy()));
		costContexts.add(new CostContext(new PvCostStrategy()));
		
		barrier = new CyclicBarrier(costContexts.size(), new Runnable() {
			
			@Override
			public void run() {
				synchronized (lockObject) {
					gatherBasicInfo();
					gatherOtherInfo();
					calculateRestInfo();
					
					insertCapacityCostInfo();
					insertCapacityCostDep();
					
					clear();
				}
			}
		});
	}

	public CapacityCostDao getCapacityCostDao() {
		return capacityCostDao;
	}

	public void setCapacityCostDao(CapacityCostDao capacityCostDao) {
		this.capacityCostDao = capacityCostDao;
	}

	public void caculateCostInfo() {
		for (CostContext context : costContexts) {
			ContextThread contextThread = new ContextThread(context);
			contextThread.start();
		}
	}
	
	public boolean alertCapacityCostRatio(String appName, double ratio) {
		boolean succuss = false;
		succuss = capacityCostDao.updateCostRatio(appName, ratio);
		
		return succuss;
	}
	
	/***
	 * 1、gather basic cost information </br>
	 * 2、gather direct depend calls </br>
	 * 3、gather cost types
	 */
	private void gatherBasicInfo() {	
		for (CostContext context : costContexts) {
			Map<String, Long> callNums = context.getCallNum();
			Map<String, Long> callDeps = context.getCallDep();
			directDepCallsMap.putAll(callDeps);
			
			for (Map.Entry<String, Long> entry : callNums.entrySet()) {
				String appName = entry.getKey();
				long callNum = entry.getValue();
				
				if (costMap.containsKey(appName)) {
					CapacityCostInfoPo po = costMap.get(appName);
					long totalCall = po.getPv() + callNum;
					long selfCost = po.getSelfCost();
					
					po.setCostType(CostType.HSF_PV);
					po.setPv(totalCall);
					po.setPerCost((double)selfCost / totalCall);
				} else {
					int machineNum = context.getMachineNum(appName);
					double ratio = getCostRatio(appName);
					long selfCost = (long)(CostConstants.MACHINE_COST * machineNum * ratio);
					
					CapacityCostInfoPo po = new CapacityCostInfoPo();
					po.setAppName(appName);
					po.setCostType(context.getCostType());
					po.setMachineNum(machineNum);
					po.setSelfCost(selfCost);
					po.setPerCost((double)selfCost / callNum);
					po.setPv(callNum);
					
					costMap.put(appName, po);
					costTypes.put(appName, context.getCostType());
				}
			}
		}
		logger.info("cost info size is " + costMap.size());
		logger.info("derect dependency size is " + directDepCallsMap.size());
	}
	
	/***
	 * 1、gather indirect depend calls </br>
	 * this method maybe hard to understand
	 */
	private void gatherOtherInfo() {
		for (Map.Entry<String, Long> entry : directDepCallsMap.entrySet()) {
			String key = entry.getKey();
			long value = entry.getValue();  // A->B times
			
			String appName = getAppFromIndex(key, false); // A
			String depApp = getAppFromIndex(key, true); // B
			
			CapacityCostInfoPo capacityCostInfoPo = costMap.get(depApp); // B
			if (capacityCostInfoPo == null) throw new IllegalStateException("cost information can not be missed");
			long totalCall = capacityCostInfoPo.getPv();  // total pv of B
	
			if (capacityCostInfoPo.getCostType() == CostType.HSF) {
				Map<String, Long> depApps = findMeDepApps(depApp, directDepCallsMap); // C
				for (Map.Entry<String, Long> entry1 : depApps.entrySet()) {
					String appName1 = entry1.getKey(); // C
					long value1 = entry1.getValue(); // B->C times
					
					CapacityCostInfoPo po = costMap.get(appName1);  // C
					if (po == null) throw new IllegalStateException("cost information can not be missed");
					
					if (needComputeInirect(po)) {
						long callNum = (value * value1) / totalCall;  // A->B / B * B->C
						String indirectKey = appName1 + CostConstants.DEP_SEP + appName;
						
						if (inDirectDepCallsMap.containsKey(indirectKey)) {
							callNum += inDirectDepCallsMap.get(indirectKey);
						}
						
						if (callNum > 0) {
							inDirectDepCallsMap.put(indirectKey, callNum);
						}
					}
				}
			}
		}
		logger.info("indirect dependency size is " + inDirectDepCallsMap.size());
	}
	
	/***
	 * 1、calculate depend cost </br>
	 * 2、calculate total cost
	 */
	private void calculateRestInfo() {
		for (Map.Entry<String, CapacityCostInfoPo> entry : costMap.entrySet()) {
			String appName = entry.getKey();
			CapacityCostInfoPo po = entry.getValue();
			long callNum = po.getPv();
			
			long depCost = caculateDepCost(appName);
			double perDepCost = (double)depCost / callNum;
			po.setDependCost(depCost);
			po.setDependPerCost(perDepCost);
			
			po.setTotalCost(po.getSelfCost() + po.getDependCost());
			po.setTotalPerCost(po.getPerCost() + po.getDependPerCost());
		}
	}
		
	/***
	 * get applications which depends I depend
	 * @param appName
	 * @param allDepCalls
	 * @return key is depend application, value is call times
	 */
	private Map<String, Long> findMeDepApps(String appName, Map<String, Long> allDepCalls) {
		Map<String, Long> map = new HashMap<String, Long>();
		
		for (Map.Entry<String, Long> entry : allDepCalls.entrySet()) {
			String index = entry.getKey();
			Long value = entry.getValue();
			
			String selfApp = getAppFromIndex(index, false);
			String depApp = getAppFromIndex(index, true);
			
			if (appName.equals(selfApp)) {
				map.put(depApp, value);
			}
		}
		
		return map;
	}
	
	/***
	 * 
	 * @param index  ic$detail, means detail depend ic
	 * @param isDepApp if true return ic, otherwise return detail
	 * @return
	 */
	private String getAppFromIndex(String index, boolean isDepApp) {
		String appName = null;
		
		String [] depApps = index.split("\\$");
		if (depApps.length != 2) throw new IllegalStateException("depend apps length must be 2");
		
		if(isDepApp) {
			appName = depApps[0];
		} else {
			appName = depApps[1];
		}
		
		return appName;
		
	}
	
	/***
	 * whether need to compute indirect dependent cost
	 * @param po
	 * @return
	 */
	private boolean needComputeInirect(CapacityCostInfoPo po) {
		boolean need = false;
		
		if (po.getCostType() == CostType.DB) need = true;
		if (po.getCostType() == CostType.TAIR) need = true;
		
		return need;
		
	}
	
	/***
	 * calculate depend cost, include direct depend cost and indirect depend cost
	 * @param appName
	 * @param depCalls
	 * @param indirectDepCalls
	 * @param appCostInfos
	 * @return
	 */
	private long caculateDepCost(String appName) {
		long depCost = 0l;
		
		// direct depend cost
		for (Map.Entry<String, Long> entry : directDepCallsMap.entrySet()) {
			String index = entry.getKey();
			String app = getAppFromIndex(index, false);
			String depApp = getAppFromIndex(index, true);
			
			long callNum = entry.getValue();
			
			// whether is depended
			if (appName.equals(app)) {
				CapacityCostInfoPo depPo = costMap.get(depApp);
				double depPerCost = depPo.getPerCost();
				depCost += (depPerCost * callNum);
			}
		}
		
		// indirect depend cost
		for (Map.Entry<String, Long> entry : inDirectDepCallsMap.entrySet()) {
			String index = entry.getKey();
			String app = getAppFromIndex(index, false);
			String depApp = getAppFromIndex(index, true);
			
			long callNum = entry.getValue();
			
			// whether is depended
			if (appName.equals(app)) {
				CapacityCostInfoPo depPo = costMap.get(depApp);
				if (depPo == null) throw new IllegalStateException("cost information can not be missed");
				
				double depPerCost = depPo.getPerCost();
				depCost += (depPerCost * callNum);
			}
		}
		
		return depCost;
	}
	
	/***
	 * clear data trace
	 */
	private void clear() {
		costMap.clear();
		directDepCallsMap.clear();
		inDirectDepCallsMap.clear();
		costTypes.clear();
	}
	
	/***
	 * insert cost data to database
	 * @param costInfos
	 */
	private void insertCapacityCostInfo() {
		logger.info("insert to db cost info total item is " + costMap.size());
		for (CapacityCostInfoPo po : costMap.values()) {
			capacityCostDao.addCapacityCostInfo(po);
		}
	}
	
	/***
	 * insert depend data to database
	 */
	private void insertCapacityCostDep() {
		// insert direct depend
		logger.info("insert to db derect dependency total item is " + directDepCallsMap.size());
		for (Map.Entry<String, Long> entry : directDepCallsMap.entrySet()) {{
				String index = entry.getKey();
				
				String depApp = getAppFromIndex(index, true);
				String app = getAppFromIndex(index, false);
				String depType = costTypes.get(depApp).toString();
				CapacityCostInfoPo costInfo = costMap.get(depApp);
				
				long depCall = entry.getValue();
				long depCost = (long)(depCall * costInfo.getPerCost());
				
				capacityCostDao.addCapacityCostDep(app, depApp, depType, DepMode.直接, depCall, depCost);
			}
		}
		
		// insert indirect depend
		logger.info("insert to db inderect dependency total item is " + inDirectDepCallsMap.size());
		for (Map.Entry<String, Long> entry : inDirectDepCallsMap.entrySet()) {{
				String index = entry.getKey();
				
				String depApp = getAppFromIndex(index, true);
				String app = getAppFromIndex(index, false);
				String depType = costTypes.get(depApp).toString();
				CapacityCostInfoPo costInfo = costMap.get(depApp);
				
				long depCall = entry.getValue();
				long depCost = (long)(depCall * costInfo.getPerCost());
				
				capacityCostDao.addCapacityCostDep(app, depApp, depType, DepMode.间接, depCall, depCost);
			}
		}
	}	
	
	private double getCostRatio(String appName) {
		return capacityCostDao.getCostRatio(appName);
	}
	
	private class ContextThread extends Thread {
		
		CostContext context;
		
		public ContextThread(CostContext context) {
			this.context = context;
		}

		@Override
		public void run() {
			context.caculateCost();
			try {
				barrier.await();
			} catch (InterruptedException e) {
				logger.error("error", e);
			} catch (BrokenBarrierException e) {
				logger.error("error", e);
			}
		}
	}
}
