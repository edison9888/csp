
package com.taobao.monitor.web.vo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * 
 * @author xiaodu
 * @version 2010-4-10 ÏÂÎç02:47:15
 */
public class MonitorVo {
	
	private String shoppingCartTmp;
	
	private String shoppingCartMember;
	
	private long allHsfInterfacePv;
	private int allHsfInterfacePvId;
	
	
	private long allHsfInterfaceQps;
	private int allHsfInterfaceQpsId;
	
	private long allHsfInterfaceRest;
	private int allHsfInterfaceRestId;
	
	private String userRequestUrlNum;
	private int userRequestUrlKeyId;
	
	private String apachePv;
	private int apachePvKeyId;
	private String apacheQps;
	private int apacheQpsKeyId;
	private String apacheRest;
	private int apacheRestKeyId;
	private Map<String, Integer> apacheStateMap = new HashMap<String, Integer>();
//z	
	private Map<String, Long> srcUrlPvMap = new HashMap<String, Long>();
	private Map<String, Long> reqUrlPvMap = new HashMap<String, Long>();
	
	public Map<String, Long> getSrcUrlPvMap(){
		return srcUrlPvMap;
	}
	public Map<String, Long> getReqUrlPvMap(){
		return reqUrlPvMap;
	}
//z
	
	public Map<String, Integer> getApacheStateMap() {
		return apacheStateMap;
	}

	public void setApacheStateMap(Map<String, Integer> apacheStateMap) {
		this.apacheStateMap = apacheStateMap;
	}

	private String submitBuy;
	private long submitBuyNum;
	
	private String amountAll;
	private long amountAllNum;
	
	private String submitBuyExc;
	private String shoppingCart;
	
	private String createOrderCount;
	private long createOrderCountNum;
	public String getPayOrderCount() {
		return payOrderCount;
	}

	public void setPayOrderCount(String payOrderCount) {
		this.payOrderCount = payOrderCount;
	}

	public long getPayOrderCountNum() {
		return payOrderCountNum;
	}

	public void setPayOrderCountNum(long payOrderCountNum) {
		this.payOrderCountNum = payOrderCountNum;
	}

	private String payOrderCount;
	private long payOrderCountNum;
	
	private Integer appId;
	private Integer qpsKeyId;
	private Integer pvKeyId;
	private Integer swapKeyId;
	private Integer rtKeyId;
	private Integer loadKeyId;
	private Integer menKeyId;
	private Integer cpuKeyId;
	private Integer iowaitKeyId;
	private Integer submitBuyKeyId;
	private Integer submitBuyExcKeyId;
	private Integer shoppingKeyId;
	
	
	private String pv;
	private String pvNum;
	private String machines="";
	private String qpsNum;
	private String swap;
	private String rtNum;	
	private String load;	
	private String men;	
	private String cpu;
	private String iowait;
	
	
	private List<TableRecord> tableSizeList = new ArrayList<TableRecord>();
	private Map<String,TableRecord> tableSizeMap = new HashMap<String,TableRecord>();
	
	
	private Map<String,HsfPo> inHsfMap = new HashMap<String, HsfPo>();
	private List<HsfPo> inHsfList = new ArrayList<HsfPo>();
	
	private List<HsfPo> sortInHsfList = new ArrayList<HsfPo>();
		
	private Map<String,HsfPo> outHsfMap = new HashMap<String, HsfPo>();	
	private List<HsfPo> sortOutHsfList = new ArrayList<HsfPo>();
	
	private Map<String,Map<String,Map<String,HsfPo>>> sortOutHsfMap = new HashMap<String, Map<String,Map<String,HsfPo>>>();
	private Map<String,Map<String,Map<String,HsfPo>>> sortInHsfMap = new HashMap<String, Map<String,Map<String,HsfPo>>>();
	
	
	private Map<String,List<HsfClass>> sortOutHsfMap1 = new HashMap<String, List<HsfClass>>();
	
	private Map<String,List<HsfClass>> sortInHsfMap1 = new HashMap<String, List<HsfClass>>();
	
	
	private Map<String,String> alarmMap = new HashMap<String, String>();
	private Map<String,Integer> alarmKeyIdMap = new HashMap<String, Integer>();
	
	private List<HsfPo> outHsfList = new ArrayList<HsfPo>();
	
	
	private GcPo gcpo= new GcPo();
	private GcPo fullGcpo = new GcPo();
	
	private List<TairClientPo> tairClientList = new ArrayList<TairClientPo>();
	private Map<String,TairClientPo> tairClientMap = new TreeMap<String, TairClientPo>();
	private List<SearchEnginePo> outSearchList = new ArrayList<SearchEnginePo>();
	private Map<String,SearchEnginePo> outSearchMap = new HashMap<String, SearchEnginePo>();
	private List<PageCachePo> pageCacheList = new ArrayList<PageCachePo>();
	private Map<String,PageCachePo> pageCacheMap = new HashMap<String, PageCachePo>();;
	private List<ForestPo> forestList = new ArrayList<ForestPo>();
	private Map<String,ForestPo> forestMap = new HashMap<String, ForestPo>();
	
	
	
	private List<SqlTop10Po> sqlTop10Exe = new ArrayList<SqlTop10Po>();	
	private List<SqlTop10Po> sqlTop10Disk = new ArrayList<SqlTop10Po>();
	private List<SqlTop10Po> sqlTop10Elap = new ArrayList<SqlTop10Po>();
	private List<SqlTop10Po> sqlTop10Buff = new ArrayList<SqlTop10Po>();
	
	
	private Map<String,AppSqlInfo> appSqlInfoMap = new HashMap<String, AppSqlInfo>();
	
	
	
	private Map<String,DataSourcePo> dataSourceMap = new HashMap<String, DataSourcePo>();
	private Map<String,ThreadPoolPo> threadPoolMap = new HashMap<String, ThreadPoolPo>();
	
	private Map<String,ThreadPo> threadPo = new HashMap<String, ThreadPo>();
	
	
	private Map<String,PingPo> pingInfoMap =new HashMap<String, PingPo>();
	
	
	private Map<String, OtherKeyValueVo> otherKeyValueMap = new HashMap<String, OtherKeyValueVo>();
	
	
	private Map<String,GcPo> gcMap = new HashMap<String, GcPo>();

	

	public Map<String, GcPo> getGcMap() {
		return gcMap;
	}

	public void setGcMap(Map<String, GcPo> gcMap) {
		this.gcMap = gcMap;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Map<String, PingPo> getPingInfoMap() {
		return pingInfoMap;
	}

	public void setPingInfoMap(Map<String, PingPo> pingInfoMap) {
		this.pingInfoMap = pingInfoMap;
	}

	public Map<String, DataSourcePo> getDataSourceMap() {
		return dataSourceMap;
	}

	public void setDataSourceMap(Map<String, DataSourcePo> dataSourceMap) {
		this.dataSourceMap = dataSourceMap;
	}

	public Map<String, ThreadPoolPo> getThreadPoolMap() {
		return threadPoolMap;
	}

	public void setThreadPoolMap(Map<String, ThreadPoolPo> threadPoolMap) {
		this.threadPoolMap = threadPoolMap;
	}

	public Map<String, ThreadPo> getThreadPo() {
		return threadPo;
	}

	public void setThreadPo(Map<String, ThreadPo> threadPo) {
		this.threadPo = threadPo;
	}

	public Map<String, String> getAlarmMap() {
		return alarmMap;
	}

	public void setAlarmMap(Map<String, String> alarmMap) {
		this.alarmMap = alarmMap;
	}

	public Map<String, TableRecord> getTableSizeMap() {
		return tableSizeMap;
	}

	public void setTableSizeMap(Map<String, TableRecord> tableSizeMap) {
		this.tableSizeMap = tableSizeMap;
	}

	public List<TableRecord> getTableSizeList() {
		return tableSizeList;
	}

	public void setTableSizeList(List<TableRecord> tableSizeList) {
		this.tableSizeList = tableSizeList;
	}

	public Map<String,AppSqlInfo> getAppSqlInfoMap() {
		return appSqlInfoMap;
	}

	public void setAppSqlInfoMap(Map<String,AppSqlInfo> appSqlInfoMap) {
		this.appSqlInfoMap = appSqlInfoMap;
	}

	public List<SqlTop10Po> getSqlTop10Exe() {
		return sqlTop10Exe;
	}

	public void setSqlTop10Exe(List<SqlTop10Po> sqlTop10Exe) {
		this.sqlTop10Exe = sqlTop10Exe;
	}

	public List<SqlTop10Po> getSqlTop10Disk() {
		return sqlTop10Disk;
	}

	public void setSqlTop10Disk(List<SqlTop10Po> sqlTop10Disk) {
		this.sqlTop10Disk = sqlTop10Disk;
	}

	public List<SqlTop10Po> getSqlTop10Elap() {
		return sqlTop10Elap;
	}

	public void setSqlTop10Elap(List<SqlTop10Po> sqlTop10Elap) {
		this.sqlTop10Elap = sqlTop10Elap;
	}

	public List<SqlTop10Po> getSqlTop10Buff() {
		return sqlTop10Buff;
	}

	public void setSqlTop10Buff(List<SqlTop10Po> sqlTop10Buff) {
		this.sqlTop10Buff = sqlTop10Buff;
	}

	public Map<String, List<HsfClass>> getSortOutHsfMap1() {
		return getSortMap1(this.outHsfList);
	}

	public Map<String, List<HsfClass>> getSortInHsfMap1() {
		return getSortMap1(this.inHsfList);
	}

	public Map<String, Map<String, Map<String, HsfPo>>> getSortOutHsfMap() {
		return getSortMap(this.outHsfList);
	}

	public Map<String, Map<String, Map<String, HsfPo>>> getSortInHsfMap() {
		return getSortMap(this.inHsfList);
	}

	public List<HsfPo> getSortInHsfList() {
		return sortHsf(this.inHsfList);
	}

	public List<HsfPo> getSortOutHsfList() {
		return sortHsf(this.outHsfList);
	}

	

	public Map<String, ForestPo> getForestMap() {
		return forestMap;
	}

	public void setForestMap(Map<String, ForestPo> forestMap) {
		this.forestMap = forestMap;
	}

	public Map<String, SearchEnginePo> getOutSearchMap() {
		return outSearchMap;
	}

	public void setOutSearchMap(Map<String, SearchEnginePo> outSearchMap) {
		this.outSearchMap = outSearchMap;
	}

	public List<HsfPo> getInHsfList() {		
		return inHsfList;
	}

	public void setInHsfList(List<HsfPo> inHsfList) {
		this.inHsfList = inHsfList;
	}

	public List<SearchEnginePo> getOutSearchList() {
		return outSearchList;
	}

	public void setOutSearchList(List<SearchEnginePo> outSearchList) {
		this.outSearchList = outSearchList;
	}

	public List<ForestPo> getForestList() {
		return forestList;
	}

	public void setForestList(List<ForestPo> forestList) {
		this.forestList = forestList;
	}

	public List<PageCachePo> getPageCacheList() {
		return pageCacheList;
	}

	public void setPageCacheList(List<PageCachePo> pageCacheList) {
		this.pageCacheList = pageCacheList;
	}

	public Map<String, PageCachePo> getPageCacheMap() {
		return pageCacheMap;
	}

	public void setPageCacheMap(Map<String, PageCachePo> pageCacheMap) {
		this.pageCacheMap = pageCacheMap;
	}

	public Map<String, TairClientPo> getTairClientMap() {
		return tairClientMap;
	}

	public void setTairClientMap(Map<String, TairClientPo> tairClientMap) {
		this.tairClientMap = tairClientMap;
	}

	public String getPv() {
		return pv;
	}

	public void setPv(String pv) {
		this.pv = pv;
	}
	public String getQpsNum() {
		return qpsNum;
	}

	public void setQpsNum(String qpsNum) {
		this.qpsNum = qpsNum;
	}

	public String getRtNum() {
		return rtNum;
	}

	public void setRtNum(String rtNum) {
		this.rtNum = rtNum;
	}

	public String getLoad() {
		return load;
	}

	public void setLoad(String load) {
		this.load = load;
	}

	public String getMen() {
		return men;
	}

	public void setMen(String men) {
		this.men = men;
	}

	
	public String getMachines() {
		return machines;
	}

	public void setMachines(String machines) {
		this.machines = machines;
	}

	public String getSwap() {
		return swap;
	}

	public void setSwap(String swap) {
		this.swap = swap;
	}
	public GcPo getGcpo() {
		return gcpo;
	}

	public void setGcpo(GcPo gcpo) {
		this.gcpo = gcpo;
	}
	public GcPo getFullGcpo() {
		return fullGcpo;
	}

	public String getSubmitBuyExc() {
		return submitBuyExc;
	}

	public void setSubmitBuyExc(String submitBuyExc) {
		this.submitBuyExc = submitBuyExc;
	}

	public void setFullGcpo(GcPo fullGcpo) {
		this.fullGcpo = fullGcpo;
	}

	public String getCpu() {
		return cpu;
	}

	public void setCpu(String cpu) {
		this.cpu = cpu;
	}

	public String getIowait() {
		return iowait;
	}

	public void setIowait(String iowait) {
		this.iowait = iowait;
	}
	public Map<String, HsfPo> getInHsfMap() {
		return inHsfMap;
	}

	public void setInHsfMap(Map<String, HsfPo> inHsfMap) {
		this.inHsfMap = inHsfMap;
	}
	
	public Map<String, HsfPo> getOutHsfMap() {
		return outHsfMap;
	}

	public void setOutHsfMap(Map<String, HsfPo> outHsfMap) {
		this.outHsfMap = outHsfMap;
	}

	public List<TairClientPo> getTairClientList() {
		return tairClientList;
	}

	public void setTairClientList(List<TairClientPo> tairClientList) {
		this.tairClientList = tairClientList;
	}

	public List<HsfPo> getOutHsfList() {		
		return outHsfList;
	}

	public void setOutHsfList(List<HsfPo> outHsfList) {
		this.outHsfList = outHsfList;
	}
	
	
	
	public String getPvNum() {
		return pvNum;
	}

	public void setPvNum(String pvNum) {
		this.pvNum = pvNum;
	}

	public String getSubmitBuy() {
		return submitBuy;
	}

	public void setSubmitBuy(String submitBuy) {
		this.submitBuy = submitBuy;
	}

	public String getShoppingCart() {
		return shoppingCart;
	}

	public void setShoppingCart(String shoppingCart) {
		this.shoppingCart = shoppingCart;
	}

	private List<HsfPo> sortHsf(List<HsfPo> oldhsfList){
		Map<String,Map<String,Map<String,HsfPo>>> map = new HashMap<String, Map<String,Map<String,HsfPo>>>();
		
		for(HsfPo po:oldhsfList){			
			String aim = po.getAim();
			String className = po.getClassName();
			String methodName = po.getMethodName();
			
			Map<String, Map<String,HsfPo>> aimMap = map.get(aim);
			if(aimMap==null){
				aimMap = new HashMap<String, Map<String,HsfPo>>();
				map.put(aim, aimMap);
			}
			
			Map<String,HsfPo> classMap = aimMap.get(className);
			
			if(classMap==null){
				classMap = new HashMap<String, HsfPo>();
				aimMap.put(className, classMap);
			}			
			classMap.put(methodName, po);
		}
		
		List<HsfPo> newList = new ArrayList<HsfPo>();
		
		Iterator<Map.Entry<String, Map<String,Map<String,HsfPo>>>> it = map.entrySet().iterator();
		while(it.hasNext()){
			Map.Entry<String, Map<String,Map<String,HsfPo>>> entry = it.next();
			
			Iterator<Map.Entry<String,Map<String,HsfPo>>> classIt = entry.getValue().entrySet().iterator();
			
			while(classIt.hasNext()){
				Map<String,HsfPo> methodMap = classIt.next().getValue();
				Iterator<Map.Entry<String, HsfPo>> methodIt = methodMap.entrySet().iterator();
				while(methodIt.hasNext()){
					newList.add(methodIt.next().getValue());
				}
				
			}
			
			
		}
		
		return newList;
	}
	
	
	
	
	private Map<String,Map<String,Map<String,HsfPo>>> getSortMap(List<HsfPo> oldhsfList){
		Map<String,Map<String,Map<String,HsfPo>>> map = new HashMap<String, Map<String,Map<String,HsfPo>>>();		
		for(HsfPo po:oldhsfList){			
			String aim = po.getAim();
			String className = po.getClassName();
			String methodName = po.getMethodName();
			
			Map<String, Map<String,HsfPo>> aimMap = map.get(aim);
			if(aimMap==null){
				aimMap = new HashMap<String, Map<String,HsfPo>>();
				map.put(aim, aimMap);
			}
			
			Map<String,HsfPo> classMap = aimMap.get(className);
			
			if(classMap==null){
				classMap = new HashMap<String, HsfPo>();
				aimMap.put(className, classMap);
			}			
			classMap.put(methodName, po);
		}		
		return map;		
	}
	
	
	private Map<String,List<HsfClass>> getSortMap1(List<HsfPo> oldhsfList){
		Map<String,List<HsfClass>> sortHsfClass = new HashMap<String, List<HsfClass>>();		
		Map<String,Map<String,HsfClass>> sortHsfClassMap = new HashMap<String, Map<String,HsfClass>>();
		
		
		for(HsfPo po:oldhsfList){			
			String aim = po.getAim();
			String className = po.getClassName();
			String methodName = po.getMethodName();
			Map<String,HsfClass> mapc = sortHsfClassMap.get(aim);
			if(mapc==null){
				mapc = new HashMap<String, HsfClass>();
				sortHsfClassMap.put(aim, mapc);
			}
			
			HsfClass hc = mapc.get(className);
			if(hc==null){
				hc = new HsfClass();
				mapc.put(className, hc);
				
				List<HsfClass> hh = sortHsfClass.get(aim);
				if(hh==null){
					hh = new ArrayList<HsfClass>();
					sortHsfClass.put(aim, hh);
				}
				hh.add(hc);
			}
			
			hc.setCount(hc.getCount()+po.getExeCountNum());
			hc.getHsfPo().add(po);			
			
		}
		
		return sortHsfClass;		
	}

	public Integer getQpsKeyId() {
		return qpsKeyId;
	}

	public void setQpsKeyId(Integer qpsKeyId) {
		this.qpsKeyId = qpsKeyId;
	}

	public Integer getPvKeyId() {
		return pvKeyId;
	}

	public void setPvKeyId(Integer pvKeyId) {
		this.pvKeyId = pvKeyId;
	}

	public Integer getSwapKeyId() {
		return swapKeyId;
	}

	public void setSwapKeyId(Integer swapKeyId) {
		this.swapKeyId = swapKeyId;
	}

	public Integer getRtKeyId() {
		return rtKeyId;
	}

	public void setRtKeyId(Integer rtKeyId) {
		this.rtKeyId = rtKeyId;
	}

	public Integer getLoadKeyId() {
		return loadKeyId;
	}

	public void setLoadKeyId(Integer loadKeyId) {
		this.loadKeyId = loadKeyId;
	}

	public Integer getMenKeyId() {
		return menKeyId;
	}

	public void setMenKeyId(Integer menKeyId) {
		this.menKeyId = menKeyId;
	}

	public Integer getCpuKeyId() {
		return cpuKeyId;
	}

	public void setCpuKeyId(Integer cpuKeyId) {
		this.cpuKeyId = cpuKeyId;
	}

	public Integer getIowaitKeyId() {
		return iowaitKeyId;
	}

	public void setIowaitKeyId(Integer iowaitKeyId) {
		this.iowaitKeyId = iowaitKeyId;
	}

	public Integer getSubmitBuyKeyId() {
		return submitBuyKeyId;
	}

	public void setSubmitBuyKeyId(Integer submitBuyKeyId) {
		this.submitBuyKeyId = submitBuyKeyId;
	}

	public Integer getSubmitBuyExcKeyId() {
		return submitBuyExcKeyId;
	}

	public void setSubmitBuyExcKeyId(Integer submitBuyExcKeyId) {
		this.submitBuyExcKeyId = submitBuyExcKeyId;
	}

	public Integer getShoppingKeyId() {
		return shoppingKeyId;
	}

	public void setShoppingKeyId(Integer shoppingKeyId) {
		this.shoppingKeyId = shoppingKeyId;
	}

	public Map<String, Integer> getAlarmKeyIdMap() {
		return alarmKeyIdMap;
	}

	public void setAlarmKeyIdMap(Map<String, Integer> alarmKeyIdMap) {
		this.alarmKeyIdMap = alarmKeyIdMap;
	}

	public long getSubmitBuyNum() {
		return submitBuyNum;
	}

	public void setSubmitBuyNum(long submitBuyNum) {
		this.submitBuyNum = submitBuyNum;
	}

	public String getAmountAll() {
		return amountAll;
	}

	public void setAmountAll(String amountAll) {
		this.amountAll = amountAll;
	}

	public long getAmountAllNum() {
		return amountAllNum;
	}

	public void setAmountAllNum(long amountAllNum) {
		this.amountAllNum = amountAllNum;
	}

	public String getCreateOrderCount() {
		return createOrderCount;
	}

	public void setCreateOrderCount(String createOrderCount) {
		this.createOrderCount = createOrderCount;
	}

	public long getCreateOrderCountNum() {
		return createOrderCountNum;
	}

	public void setCreateOrderCountNum(long createOrderCountNum) {
		this.createOrderCountNum = createOrderCountNum;
	}

	public Map<String, OtherKeyValueVo> getOtherKeyValueMap() {
		return otherKeyValueMap;
	}

	public void setOtherKeyValueMap(Map<String, OtherKeyValueVo> otherKeyValueMap) {
		this.otherKeyValueMap = otherKeyValueMap;
	}

	public String getApachePv() {
		return apachePv;
	}

	public void setApachePv(String apachePv) {
		this.apachePv = apachePv;
	}

	public int getApachePvKeyId() {
		return apachePvKeyId;
	}

	public void setApachePvKeyId(int apachePvKeyId) {
		this.apachePvKeyId = apachePvKeyId;
	}

	public String getApacheQps() {
		return apacheQps;
	}

	public void setApacheQps(String apacheQps) {
		this.apacheQps = apacheQps;
	}

	public int getApacheQpsKeyId() {
		return apacheQpsKeyId;
	}

	public void setApacheQpsKeyId(int apacheQpsKeyId) {
		this.apacheQpsKeyId = apacheQpsKeyId;
	}

	public String getApacheRest() {
		return apacheRest;
	}

	public void setApacheRest(String apacheRest) {
		this.apacheRest = apacheRest;
	}

	public int getApacheRestKeyId() {
		return apacheRestKeyId;
	}

	public void setApacheRestKeyId(int apacheRestKeyId) {
		this.apacheRestKeyId = apacheRestKeyId;
	}

	public String getShoppingCartTmp() {
		return shoppingCartTmp;
	}

	public void setShoppingCartTmp(String shoppingCartTmp) {
		this.shoppingCartTmp = shoppingCartTmp;
	}

	public String getShoppingCartMember() {
		return shoppingCartMember;
	}

	public void setShoppingCartMember(String shoppingCartMember) {
		this.shoppingCartMember = shoppingCartMember;
	}



	public int getAllHsfInterfacePvId() {
		return allHsfInterfacePvId;
	}

	public void setAllHsfInterfacePvId(int allHsfInterfacePvId) {
		this.allHsfInterfacePvId = allHsfInterfacePvId;
	}



	public int getAllHsfInterfaceQpsId() {
		return allHsfInterfaceQpsId;
	}

	public void setAllHsfInterfaceQpsId(int allHsfInterfaceQpsId) {
		this.allHsfInterfaceQpsId = allHsfInterfaceQpsId;
	}



	public int getAllHsfInterfaceRestId() {
		return allHsfInterfaceRestId;
	}

	public void setAllHsfInterfaceRestId(int allHsfInterfaceRestId) {
		this.allHsfInterfaceRestId = allHsfInterfaceRestId;
	}

	public long getAllHsfInterfacePv() {
		return allHsfInterfacePv;
	}

	public void setAllHsfInterfacePv(long allHsfInterfacePv) {
		this.allHsfInterfacePv = allHsfInterfacePv;
	}

	public long getAllHsfInterfaceQps() {
		return allHsfInterfaceQps;
	}

	public void setAllHsfInterfaceQps(long allHsfInterfaceQps) {
		this.allHsfInterfaceQps = allHsfInterfaceQps;
	}

	public long getAllHsfInterfaceRest() {
		return allHsfInterfaceRest;
	}
	public void setAllHsfInterfaceRest(long allHsfInterfaceRest) {
		this.allHsfInterfaceRest = allHsfInterfaceRest;
	}
	public String getUserRequestUrlNum() {
		return userRequestUrlNum;
	}
	public void setUserRequestUrlNum(String userRequestUrlNum) {
		this.userRequestUrlNum = userRequestUrlNum;
	}
	public int getUserRequestUrlKeyId() {
		return userRequestUrlKeyId;
	}
	public void setUserRequestUrlKeyId(int userRequestUrlKeyId) {
		this.userRequestUrlKeyId = userRequestUrlKeyId;
	}


	

	
}
