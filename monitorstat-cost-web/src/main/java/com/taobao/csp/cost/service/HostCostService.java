package com.taobao.csp.cost.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.taobao.csp.cost.dao.HostDetailCostDao;
import com.taobao.csp.cost.po.HostCostDetail;
import com.taobao.csp.cost.po.SimpleHostInfo;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * 机器成本计算业务类
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-11
 */
public class HostCostService {
	
	private CspCacheTBHostInfos hostInfos=CspCacheTBHostInfos.get();

	@Resource(name = "hardCostService")
	private HardCostService hardCostService;
	
	@Resource(name = "appDao")
	private AppInfoDao appDao;
	
	static Cache<String, HostCostDetail> hostDetailCache = CacheBuilder.newBuilder().
			expireAfterWrite(1, TimeUnit.DAYS).maximumSize(5000).build();

	
	public static HostCostService hcs=new HostCostService();
	private HostCostService(){
		init();
	}
	//所有类型的价格
	private void init(){
		//暂时用不到
		//hostTypePrice=hdcDao.getAllHostTypePrice();
	}
	
//	/**
//	 * 更新单机成本
//	 * 
//	 * @param hostName
//	 * @param cost
//	 * @return
//	 */
//	public String editHostCost(String hostName,double cost){
//		HostPo hp=hostInfos.getHostInfoByHostName(hostName);
//		if(hp==null){
//			return "机器不存在";
//		}
//		
//		boolean rs=hdcDao.editHostCostDetail(hp, cost);
//		if(!rs){
//			return "数据库更新失败";
//		}
//		//更新缓存的单机成本
//		HostCostDetail hcd= getHostPriceDetail(hostName);
//		if(hcd!=null){
//			hcd.setHostPrice(cost);
//		}
//		//更新缓存的总成本
//		refreshAppTotalCost(hp.getOpsName());
//		
//		return "";
//	}
//	/**
//	 * 刷新成本
//	 * 
//	 * @param appName
//	 * @return
//	 */
//	private double refreshAppTotalCost(String appName){
//		List<HostCostDetail> hds=getAppHostDetail(appName);
//		double total=0;
//		for(HostCostDetail hp:hds){
//			total+=hp.getHostPrice();
//		}
//		opsTotalCost.put(appName, total);
//		return total;
//	}
	
	/**
	 * 计算所有APP的机器成本
	 * 返回按机器类型分的数量/成本
	 * @return
	 */
	public Map<String, List<SimpleHostInfo>> caculateCostAll() {
		List<AppInfoPo> appList = appDao.findAllAppInfo();
		Map<String,List<SimpleHostInfo>> allCosts=new HashMap<String, List<SimpleHostInfo>>();
		
		for(AppInfoPo ap:appList){
			List<SimpleHostInfo> costs=getAppHostSimpleDetail(ap.getAppName());
			
			allCosts.put(ap.getAppName(), costs);
		}
		return allCosts;
	}
	
	
	
	/**
	 * 返回某个app按照机型划分的数量以及成本
	 * 
	 * @param appName
	 * @return [机型 虚拟机/实体机 数量]
	 */
	public List<SimpleHostInfo> getAppHostSimpleDetail(String appName){
		List<SimpleHostInfo> simpleHosts=new ArrayList<SimpleHostInfo>();
		Map<String,SimpleHostInfo> maps=new HashMap<String,SimpleHostInfo>();
		String key="";
		SimpleHostInfo simpleInfo=null;

		/**
		 * 查询机器的类型
		 */
		List<HostCostDetail> appCostDetails=getAppHostDetail(appName);
		if(appCostDetails==null || appCostDetails.size()==0)
			return simpleHosts;
		
		for(HostCostDetail hDetail:appCostDetails){
			key=hDetail.getHostType();
			
			if(!maps.containsKey(key)){
				simpleInfo=new SimpleHostInfo();
				simpleInfo.setHostType(hDetail.getDetailHostInfo().isVirtualHost()?"是":"否");
				simpleInfo.setMacName(key);
				
				simpleHosts.add(simpleInfo);
				maps.put(key, simpleInfo);
			}else{
				simpleInfo=maps.get(key);
				int nums=simpleInfo.getHostNum();
				
				simpleInfo.setHostNum(++nums);
			}
			//成本累加
			simpleInfo.addTotalPrice(hDetail.getHostPrice());
		}
		return simpleHosts;
	}

	/**
	 * 获取APP HOST的详细信息
	 * 
	 * @param appName
	 * @return
	 */
	public List<HostCostDetail> getAppHostDetail(String appName){
		List<HostCostDetail> hds=null;
		//一个应用的所有机器
		List<HostPo> hosts=hostInfos.getHostInfoListByOpsName(appName);
		if(hosts!=null){
			hds=new ArrayList<HostCostDetail>(hosts.size());
			for(HostPo hp:hosts){
				HostCostDetail hcd=getHostPriceDetail(hp.getHostName());
				hds.add(hcd);
			}
		}else{
			hds=new ArrayList<HostCostDetail>();
		}
		return hds;
	}
	
	/**
	 * 获得单台机器的成本
	 * 
	 * @param hostName
	 * @return
	 */
	public HostCostDetail getHostPriceDetail(String hostName){
		//看数据库中是否有配置单台机器成本
		HostCostDetail hcd=hostDetailCache.getIfPresent(hostName);
		
		if(hcd!=null){
			return hcd;
		}
		//单击成本可修改，暂时注释了
		
		//HostCostDetail hcd=hdcDao.getHostCostDetail(hostName);
		HostPo hp=hostInfos.getHostInfoByHostName(hostName);
		
		hcd=new HostCostDetail();
		hcd.setHostName(hp.getHostName());
		hcd.setHostType(hp.getHostType());
		hcd.setOpsName(hp.getOpsName());
		
		List hards=hardCostService.getBaseCostByHostName(hp.getHostName());
		double hostPrice=(Double)hards.get(1);
		String hInfo=(String)hards.get(0);
		String hardInfo=getHardInfo(hInfo);
		
		hcd.setHardInfo(hardInfo);
		hcd.setHostPrice(hostPrice);
		hcd.setDetailHostInfo(hp);
		
		if(hp.isVirtualHost() && hostInfos.getParentHostList(hp.getVmparent())!=null){
			hcd.setParentSplitSize(hostInfos.getParentHostList(hp.getVmparent()).size());
		}
		hostDetailCache.put(hostName, hcd);
		return hcd;
	}
	
	/**
	 * 返回可识别的硬件信息
	 * 
	 * @param hInfo
	 * @return
	 */
	private String getHardInfo(String hInfo){
		
		String hostInfo="";
		if(StringUtils.isBlank(hInfo) || 
				hInfo.split(";").length!=3){
			hostInfo="认证失败，您可以选择手动编辑";
		}else{
			double mem=0;
			double cpu=0;
			double disk=0;
			String[] hardInfos=hInfo.split(";");
			try{
				mem=Double.parseDouble(hardInfos[0]);
				cpu=Double.parseDouble(hardInfos[1]);
				disk=Double.parseDouble(hardInfos[2]);
			}catch(Exception e){
				mem=7500;//7g
				cpu=5;	//5core
				disk=120;//120G
			}
			String memStr=new DecimalFormat("#.#").format(mem/1024);
			String cpuStr=new DecimalFormat("#").format(cpu);
			String diskStr=new DecimalFormat("#.#").format(disk);
			
			hostInfo="内存:"+memStr+"G,CPU:"+cpuStr+"核,硬盘:"+diskStr+"G";
		}
		return hostInfo;
	}
}
