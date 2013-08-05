package com.taobao.csp.cost.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;

import com.taobao.csp.cost.dao.CostDayDaoNew;
import com.taobao.csp.cost.po.CostAppTotal;
import com.taobao.csp.cost.po.CostAppType;
import com.taobao.csp.cost.po.CostBaseTotalPo;
import com.taobao.csp.cost.po.SimpleHostInfo;
import com.taobao.csp.cost.service.day.CostType;
import com.taobao.csp.cost.service.day.DBCostStrategy;
import com.taobao.csp.cost.service.day.HsfCostStrategy;
import com.taobao.csp.cost.service.day.ICostStrategy;
import com.taobao.csp.cost.service.day.PvCostStrategy;
import com.taobao.csp.cost.service.day.TairCostStrategy;
import com.taobao.csp.cost.util.CostConstants;
import com.taobao.csp.cost.util.CustomDateUtil;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.db.impl.day.MonitorDayDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.ProductLine;
import com.taobao.monitor.common.util.DateUtil;
import com.taobao.monitor.common.util.TBProductCache;

/**
 * 按天统计

 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-19
 */
public class DayCostServiceNew {

	private static Logger logger = Logger.getLogger(DayCostServiceNew.class);

	@Resource(name = "costDayDaoNew")
	private CostDayDaoNew costDayDaoNew;
	@Resource(name = "hostCostService")
	private HostCostService hostCostService;	//机器

	@Resource(name = "monitorDayDao")
	private MonitorDayDao monitorDayDao;		//获得机器数
	private List<ICostStrategy> baseCostContexts = new ArrayList<ICostStrategy>();
	/*** 缓存tair&db的cost信息 ***/
	private Map<String, CostBaseTotalPo> bastCostMap = new HashMap<String, CostBaseTotalPo>();
	
	/***应用的各种依赖成本***/
	private ConcurrentHashMap<String, CopyOnWriteArrayList<CostAppType>> appCostMapList = new ConcurrentHashMap<String, CopyOnWriteArrayList<CostAppType>>();
	
	@Resource(name = "dbStrategy")
	private DBCostStrategy dbStrategy;
	@Resource(name = "tairStrategy")
	private TairCostStrategy tairStrategy;
	@Resource(name = "hsfStrategy")
	private HsfCostStrategy hsfStrategy;
	@Resource(name = "pvStrategy")
	private PvCostStrategy pvStrategy;
	@Resource(name = "hardCostService")
	private HardCostService hardCostService;
	

	private AppInfoAo appInfos=AppInfoAo.get();
	
	CountDownLatch cdl=new CountDownLatch(2);
	
	static ConcurrentHashMap<String, String> chmp=new ConcurrentHashMap<String, String>();
	
	public void init(){
		//计算DB的一个总成本
		baseCostContexts.add(dbStrategy);
		//计算tair的几个总成本
		baseCostContexts.add(tairStrategy);
	}
	
	/**
	 * 
	 * 1.统计tair/DB的总调用次数，总成本,以及应用的基础依赖成本
	 * 2.统计app机器的成本
	 * 3.app的直接依赖成本=(app调用tair&DB的次数/tair&DB被调用的总次数)×tair&DB被调用的总成本+机器成本
	 * 4.计算hsf provider提供的总调用次数，和consumer调用各个provider的次数
	 * 5.app的全部成本=app的直接依赖成本+hsf依赖成本
	 * 
	 * @param date
	 */
	public synchronized void caculateCostInfo(final Date date,final String dStr,
			String isForce,final int tableId) {
		//1.计算 db/tair/tfs的总调用次数和成本
		if(StringUtils.isNotBlank(isForce)){
			chmp.remove(dStr);
		}
		if(chmp.containsKey(dStr)){
			logger.warn("has caculate done："+dStr+new Date());
			return ;
		}
		chmp.put(dStr, "baseBegin");
		for (final ICostStrategy context : baseCostContexts) {
			Thread t=new Thread(){
				public void run(){
					try{
						if(!chmp.containsKey(dStr) || !chmp.get(dStr).equals("baseBegin")){
							logger.warn("101 state errorreturn："+chmp.get(dStr));
							return ;
						}
						
						logger.warn(context+" caculate begin："+new Date());
						
						//统计tair和db的总调用次数
						context.caculateCost(date,tableId);
						
						//计算tair和db的单次调用成本
						gatherBasicInfo(context,date);
						//计算应用对tair和db依赖的成本
						gatherOtherInfo(date,context.getCallDep());
						
						cdl.countDown();
						
						logger.warn(context+" caculate end："+new Date());
					}catch(Exception e){
						logger.warn(context+" caculate exception",e);
					}
					
				}
			};
			t.start();
		}
		//2.计算应用的机器依赖成本
		//可以说没有时间，为了统一，加上时间的概念
		Thread t=new Thread(){
			public void run(){
				if(!chmp.containsKey(dStr) || !chmp.get(dStr).equals("baseBegin")){
					logger.warn("128 state errorreturn："+chmp.get(dStr));
					return ;
				}
				logger.warn("host caculdate begin"+new Date());
				
				caculateHostAll(date);
				
				cdl.countDown();
				logger.warn("host caculdate end"+new Date());
			}
		};
		t.start();
		
		logger.warn("caculate base begin："+new Date());
		
		try {
			cdl.await(1,TimeUnit.HOURS);
		} catch (InterruptedException e) {
			logger.warn("caculate base exception",e);
		}
		logger.warn("caculate base end"+new Date());
		if(!chmp.containsKey(dStr) || !chmp.get(dStr).equals("baseBegin")){
			logger.warn(" 150 tate errorreturn："+chmp.get(dStr));
			return ;
		}
		chmp.put(dStr, "totalStart");
		
		//3.1计算直接依赖的总成本（tair+db+host）
		Map<String, CostAppType> appDirectCostMap=caculateDirectCost(date);
	
		//应用被hsf和http调用的总次数以及总成本
		caculateAllNum(date,appDirectCostMap);
		

		chmp.put(dStr, "totalEnd");
		appCostMapList.clear();
		bastCostMap.clear();
		appDirectCostMap=null;
		cdl=new CountDownLatch(2);
	}
	
	/**
	 * 1.app的总调用次数  providerNum+httpNum
	 * 2.所有成本=基础依赖成本+hsf依赖成本
	 * @param date
	 * @param appDirectCostMap
	 */
	private void caculateAllNum(Date date,Map<String, CostAppType> appDirectCostMap){
		logger.warn("caculate app all cost begin"+new Date());
		//hsf
		hsfStrategy.caculateCost(date);
		
		//provider的总次数<provider,callSum>
		Map<String, Long> hsfProviderNum=hsfStrategy.getCallNum();		
		//应用调用其他provider的次数 <consumer,<provider$callSum>>
		Map<String, Map<String,Long>> consumerNum=hsfStrategy.getCallDep();
		//http <app,num>
		Map<String, Long> httPoviderNum=pvStrategy.caculateCost(date);
		
		logger.warn("total app count "+appDirectCostMap.size());
		//计算app的总成本
		for(Map.Entry<String,CostAppType> directEntry:appDirectCostMap.entrySet()){
			CostAppType catp=directEntry.getValue();
			
			//可能不依赖其他应用，所以不用计算hsf依赖成本
			if(!consumerNum.containsKey(catp.getAppName())){
				logger.warn("no this consumer？"+catp.getAppName()+new Date());
				continue;
			}
			//如果是provider的话，记录下提供调用的总次数,用来说前次调用成本
			long appProviderNum=0L;
			if(hsfProviderNum.containsKey(catp.getAppName())){
				appProviderNum+=hsfProviderNum.get(catp.getAppName());
			}
			if(httPoviderNum.containsKey(catp.getAppName())){
				appProviderNum+=httPoviderNum.get(catp.getAppName());
			}
			catp.setCallNum(appProviderNum);
			
			//consumer对应的prodider
			for(Map.Entry<String,Long> providerNum:consumerNum.get(catp.getAppName()).entrySet()){
				String providerName =providerNum.getKey(); // provider
				long caNum =providerNum.getValue(); 	// 调用这个provider的次数
				
				if(appDirectCostMap.get(providerName)==null){
					logger.warn("no this provider?"+providerName+","+new Date());
					continue;
				}
				
				//provider提供的总调用次数=hsf次数+http次数
				long providerTotalNum=0L;
				
				if(hsfProviderNum.get(providerName)!=null){
					providerTotalNum+=hsfProviderNum.get(providerName);
				}
				if(httPoviderNum.get(providerName)!=null){
					providerTotalNum+=httPoviderNum.get(providerName);
				}
			
				//provider的直接依赖成本
				double providerDirectCost=appDirectCostMap.get(providerName).getDependCost();
				
				if(appDirectCostMap.get(providerName)==null || 
						providerTotalNum<=0 || providerTotalNum<caNum){
					logger.warn("provider not has base cost error?"+providerName+","+
						providerTotalNum+","+caNum+","+new Date());
					continue;
				}
				double consumerCost=0;
				if(providerTotalNum!=0){
					 consumerCost=((double)caNum/providerTotalNum)*providerDirectCost;
				}
				consumerCost=Double.parseDouble(
						new DecimalFormat("#.#").format(consumerCost));
				logger.warn("caculate "+catp.getAppName()+","+consumerCost+","+caNum+","+
						providerTotalNum+","+providerDirectCost);
				/**
				 * 将hsf成本入库
				 */
				if(consumerCost>0){
					CostAppType hsfDepend=new CostAppType();
					hsfDepend.setAppName(catp.getAppName());
					hsfDepend.setCostType(CostType.HSF_PV.name());
					hsfDepend.setCostName(providerName);
					hsfDepend.setCallNum(caNum);

					hsfDepend.setDependCost(consumerCost);
					hsfDepend.setCollectTime(date);
					costDayDaoNew.addTypeCostAll(hsfDepend);
					
					//累加App总成本
					catp.setDependCost(catp.getDependCost()+consumerCost);
				}
			}
		}
		
		//公司总成本
		Map<String,CostAppTotal> groupCount=new HashMap<String,CostAppTotal>();
		//产品线总成本
		Map<String,CostAppTotal> lineCount=new HashMap<String,CostAppTotal>();

		for(Map.Entry<String,CostAppType> directEntry:appDirectCostMap.entrySet()){
			ProductLine pdl=TBProductCache.getProductLineByAppName(directEntry.getKey());
			String proGroup=pdl.getDevelopGroup();
			String proLine=pdl.getProductline();
			
			//添加应用总成本
			double nowTotal=directEntry.getValue().getDependCost();
			nowTotal=Double.parseDouble(
					new DecimalFormat("#.#").format(nowTotal));
			long callNum=directEntry.getValue().getCallNum();
			//不足1K次，算做1111次
			if(callNum<1000){
				callNum=1111;
			}
			CostAppTotal cacApp=new CostAppTotal();
			cacApp.setCostType(CostType.ALL.name());
			cacApp.setCostAll(nowTotal);
			cacApp.setsName(directEntry.getValue().getAppName());
			cacApp.setTimeType(CostConstants.TIME_TYPE_MONTH);
			cacApp.setsTime(date);
			cacApp.setpName(proLine);
			cacApp.setCallNum(callNum);
			if(callNum>0){
				cacApp.setPreCost(Double.parseDouble(new DecimalFormat("#.#").format(nowTotal*1000/callNum)));
			}
			
			costDayDaoNew.addTotalCostAll(cacApp);
			
			//公司
			if(groupCount.get(proGroup)==null){
				CostAppTotal cac=new CostAppTotal();
				cac.setCostType(CostType.GROUP_COUNT.name());
				cac.setCostAll(nowTotal);
				cac.setsName(pdl.getDevelopGroup());
				cac.setTimeType(CostConstants.TIME_TYPE_MONTH);
				cac.setsTime(date);
				
				groupCount.put(proGroup, cac);
			}else{
				double preCount=groupCount.get(proGroup).getCostAll();
				groupCount.get(proGroup).setCostAll(nowTotal+preCount);
			}
			//产品线
			if(lineCount.get(proLine)==null){
				CostAppTotal cac=new CostAppTotal();
				cac.setCostType(CostType.LINE_COUNT.name());
				cac.setCostAll(nowTotal);
				cac.setsName(proLine);
				cac.setTimeType(CostConstants.TIME_TYPE_MONTH);
				cac.setsTime(date);
				cac.setpName(proGroup);
				
				lineCount.put(proLine, cac);
				
			}else{
				double preCount=lineCount.get(proLine).getCostAll();
				lineCount.get(proLine).setCostAll(nowTotal+preCount);
			}
		}

		logger.warn("caculate app all cost end"+new Date());
		
		logger.warn("start insert month total:"+new Date());
		
		for(Map.Entry<String,CostAppTotal>  groupS:groupCount.entrySet()){
			double nowTotal=Double.parseDouble(
					new DecimalFormat("#.#").format(groupS.getValue().getCostAll()));
			groupS.getValue().setCostAll(nowTotal);
			
			costDayDaoNew.addTotalCostAll(groupS.getValue());
		}
		for(Map.Entry<String,CostAppTotal>  lines:lineCount.entrySet()){
			double nowTotal=Double.parseDouble(
					new DecimalFormat("#.#").format(lines.getValue().getCostAll()));
			lines.getValue().setCostAll(nowTotal);
			
			costDayDaoNew.addTotalCostAll(lines.getValue());
		}
		
		logger.warn("insert month total end:"+new Date());
	}
	
	public static void main(String[] args){
		
		double consumerCost=0;
		long caNum=4817;
		long providerTotalNum= 2467878812L;
		double providerDirectCost=887079.4999999994;
		if(providerTotalNum!=0){
			 consumerCost=((double)caNum/providerTotalNum)*providerDirectCost;
		}
		logger.warn("caculate "+","+consumerCost+","+caNum+","+
				providerTotalNum+","+providerDirectCost);
		System.out.println(consumerCost);
	}
	
	//计算直接依赖的成本
	private Map<String, CostAppType> caculateDirectCost(Date date){
		Map<String, CostAppType> appDirectCostMap=new HashMap<String, CostAppType>();
		//将所有成本累加，得出应用的直接依赖成本（不包括hsf）
		for (Map.Entry<String,CopyOnWriteArrayList<CostAppType>> appTotalsEntry : appCostMapList.entrySet()) {
			CostAppType capt=null;
			
			for(CostAppType cap:appTotalsEntry.getValue()){
				if(!appDirectCostMap.containsKey(appTotalsEntry.getKey())){
					capt=new CostAppType();
					capt.setAppName(appTotalsEntry.getKey());
					capt.setCostType(CostType.DIRECT_ALL.name());
					capt.setCostName("DIRECT");
					capt.setCallNum(-1);

					capt.setDependCost(cap.getDependCost());
					capt.setCollectTime(date);
					
					appDirectCostMap.put(appTotalsEntry.getKey(), capt);
				}else{
					capt=appDirectCostMap.get(appTotalsEntry.getKey());
					//总成本
					capt.setDependCost(capt.getDependCost()+cap.getDependCost());
				}
			}
		}

		/**
		 * 基础依赖成本总和入库
		 */
		for(Map.Entry<String, CostAppType> appE:appDirectCostMap.entrySet()){
			double dependCost=Double.parseDouble(
					new DecimalFormat("#.#").format(appE.getValue().getDependCost()));
			
			appE.getValue().setDependCost(dependCost);
			costDayDaoNew.addTypeCostAll(appE.getValue());
		}
		
		logger.warn("caculate app direct end"+new Date());
		return appDirectCostMap;
	}
	
	static long[] keys=new long[]{992L,993L,14420L,42331L,54275L,
		42426L,42429L,34628L,42428L,42427L};
	
	/**
	 * 计算应用的机器成本
	 * 1.应用+机器数+总成本
	 * 2.入库&加入缓存
	 * 
	 * @param date
	 */
	private void caculateHostAll(Date date){
		Map<String, List<SimpleHostInfo>> apHost=hostCostService.caculateCostAll();
		
		String dateStr=DateUtil.getDateYMDFormat().format(date);
		
		for (Map.Entry<String, List<SimpleHostInfo>> entry : apHost.entrySet()) {
			String appName = entry.getKey();
			AppInfoPo appInfo=appInfos.getAppInfoByAppName(appName);
			
			//获得以前的机器数
			Map<Long, Long> rs=null;
			try {
				rs = monitorDayDao.findMonitorCountMapAsValueByDate(
						appInfo.getAppId(), dateStr, keys);
			} catch (Exception e) {
				logger.warn("get app hosts exception",e);
			}
			//获得之前的机器数
			long oldMachineCount=0;
			for(Map.Entry<Long, Long> rsentry:rs.entrySet()){
				oldMachineCount=oldMachineCount+rsentry.getValue();
			}

			List<SimpleHostInfo> appHosts=entry.getValue();
			//获得当前的机器数
			long machineCount=0;
			for(SimpleHostInfo sim:appHosts){
				machineCount=machineCount+sim.getHostNum();
			}
			//成本要按照比例计算
			double percent=1;
			if(machineCount>0){
				percent=(double)oldMachineCount/(double)machineCount;
			}
			
			double totalCost = 0;
			int machineNum =0;
			CostAppType captAll=new CostAppType();
			
			for(SimpleHostInfo ah:appHosts){
				//totalCost=totalCost+ah.getTotalPrice();
				double rCost=ah.getTotalPrice()*percent;
				double rNum=ah.getHostNum()*percent;
				
				CostAppType capt=new CostAppType();
				capt.setAppName(appName);
				capt.setCostType(CostType.HOST.name());
				capt.setCostName(ah.getMacName()+","+ah.getHostType());	//机型和是否虚拟机之间以;号分隔
				capt.setCallNum((long)rNum);	//call num就是机器数量
				
				capt.setDependCost(Double.parseDouble(
						new DecimalFormat("#.#").format(rCost)));
				capt.setCollectTime(date);
				
				totalCost=totalCost+rCost;
				machineNum=machineNum+(int)rNum;
				
				//按机型的成本存储
				costDayDaoNew.addTypeCostAll(capt);
			}
			
			captAll.setAppName(appName);
			captAll.setCostType(CostType.HOST.name());
			captAll.setCostName("host");//如果是hsf就是其他业务系统名
			captAll.setCallNum(machineNum);
			captAll.setDependCost(totalCost);
			captAll.setCollectTime(date);
			
			//加入缓存
			addAppCost(appName,captAll);
		}
	}
	
	private void addAppCost(String appName,CostAppType capt){
		if(appCostMapList.get(appName)==null){
			CopyOnWriteArrayList<CostAppType> capts=new CopyOnWriteArrayList<CostAppType>();
			capts.add(capt);
			//因为并发问题，已经存在，则重新加入
			List<CostAppType> newCapts=appCostMapList.putIfAbsent(appName, capts);
			if(newCapts!=null){
				appCostMapList.get(appName).add(capt);
			}
		}else{
			appCostMapList.get(appName).add(capt);
		}
		logger.info("caculate app,"+appName+",cost is,"+capt.getDependCost()+
				",dependType,"+capt.getCostType());
	}
	
	/**
	 * 将基础依赖的成本入库和缓存
	 * 
	 * @param context
	 */
	private void gatherBasicInfo(ICostStrategy context,Date date ) {	
		//<db|groupName,callSum>
		Map<String, Long> callNums = context.getCallNum();
		
		for (Map.Entry<String, Long> entry : callNums.entrySet()) {
			//dbname or groupname
			String baseDependName = entry.getKey();
			//调用总次数和机器数
			long callNum = entry.getValue();
			if(callNum<1000){
				callNum=1111;
			}
			int machineNum = context.getMachineNum(baseDependName);
			
			CostBaseTotalPo po = new CostBaseTotalPo();
			po.setBaseName(baseDependName);
			po.setCostType(context.getCostType().name());
			po.setMachineNum(machineNum);
			
			//目前一个tair集群都先算100W,DB 50W
			//long selfCost = (long)(CostConstants.MACHINE_COST * machineNum * ratio);
			Set<String> ss=context.getMachines(baseDependName);
			double total=0;
			if(ss==null || ss.size()<1){
				logger.warn(context+",fuck "+baseDependName+"ip is empty");
			}else{
				logger.warn(context+",group "+baseDependName+"ip is "+ss.size());
				total=hardCostService.getBaseCostByIpList(ss,context.getCostType().name());
			}
			logger.warn(context+",cost "+baseDependName+" is "+total);
//			if(context.getCostType()==CostType.TAIR){
//				po.setTotalCost(total);
//			}else if(context.getCostType()==CostType.DB){
//				po.setTotalCost(CostConstants.DB__COST);
//			}

			po.setTotalCost(total);
			//每千次调用成本
			po.setPerCost((double)po.getTotalCost()*1000 / callNum);
			po.setCallNum(callNum);
			
			costDayDaoNew.addBaseCostAll(po,date);

			bastCostMap.put(baseDependName,po);
		}
	}
	
	/**
	 * 计算应用依赖tair/db的成本
	 * @param date
	 */
	private void gatherOtherInfo(Date date,Map<String, Long> directDepCallsMap ) {
		logger.info("all dependency size is " + directDepCallsMap.size());

		for (Map.Entry<String, Long> entry : directDepCallsMap.entrySet()) {
			String key = entry.getKey();
			long value = entry.getValue();  // A->B times
			
			String appName = getAppFromIndex(key, false); // 应用
			String depName = getAppFromIndex(key, true); // 依赖
			
			CostBaseTotalPo baseCostInfoPo = bastCostMap.get(depName); // B
			if (baseCostInfoPo == null) 
				throw new IllegalStateException("cost information can not be missed");
			long totalCall = baseCostInfoPo.getCallNum();  // total pv of B
			
			if(totalCall==0){
				continue;
			}
			if(value<1000){
				value=1000;
			}
			double dependCost=Double.parseDouble(
					new DecimalFormat("#.#").format((double)value/1000*baseCostInfoPo.getPerCost()));
			
			CostAppType capt=new CostAppType();
			capt.setAppName(appName);
			capt.setCostType(baseCostInfoPo.getCostType());
			capt.setCostName(depName);
			capt.setCallNum(value);
			
			//调用次数/1000*千次调用成本
			capt.setDependCost(dependCost);
			capt.setCollectTime(date);
			
			//加入缓存
			addAppCost(appName,capt);
			
			//应用对tair/db的成本入库
			costDayDaoNew.addTypeCostAll(capt);
		}
	}

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
	
	public HostCostService getHostCostService() {
		return hostCostService;
	}

	public void setHostCostService(HostCostService hostCostService) {
		this.hostCostService = hostCostService;
	}

	public CostDayDaoNew getCostDayDaoNew() {
		return costDayDaoNew;
	}

	public void setCostDayDaoNew(CostDayDaoNew costDayDaoNew) {
		this.costDayDaoNew = costDayDaoNew;
	}
	
}
