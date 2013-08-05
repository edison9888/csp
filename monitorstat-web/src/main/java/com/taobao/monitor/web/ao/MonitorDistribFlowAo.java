
package com.taobao.monitor.web.ao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.web.core.dao.impl.MonitorDistribFlowDao;
import com.taobao.monitor.web.distrib.AppDistribFlowBo;
import com.taobao.monitor.web.distrib.DistribFlowPo;
import com.taobao.monitor.web.distrib.KeyDistribFlowBo;
import com.taobao.monitor.web.distrib.ProviderDisribFlowBo;

/**
 * 
 * @author xiaodu
 * @version 2010-11-8 下午05:01:41
 */
public class MonitorDistribFlowAo {
	private MonitorDistribFlowAo(){}
	
	
	private static MonitorDistribFlowAo jprofAo = new MonitorDistribFlowAo();
	
	private MonitorDistribFlowDao jprofDao = new MonitorDistribFlowDao();
	
	
	public static MonitorDistribFlowAo get(){
		return jprofAo;
	}
	
	
	/**
	 * 根据应用查询出所有的应用提供的服务
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public List<DistribFlowPo> findProvideAppDistribFlow(String provideAppName,Date collectDay){
		
		return jprofDao.findProvideAppDistribFlow(provideAppName, collectDay);
		
	}
	
	/**
	 * 这是添加按方法来排序信息显示时加的，和sortProvideAppDistribFlow类似
	 * 将查询出的 provider 按照key进行组合和排序
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public List<KeyDistribFlowBo> sortProvideKeyDistribFlow(String provideAppName,Date collectDay){

		 List<DistribFlowPo> poList = jprofDao.findProvideAppDistribFlow(provideAppName, collectDay);
		 //map<keyName, keyDistribFlowBo>
		 Map<String, KeyDistribFlowBo> keyBoMap = new HashMap<String, KeyDistribFlowBo>();
		 for(DistribFlowPo po : poList) {
			 
			 String keyName = po.getKeyName();
			 if(keyName.equals("empty")) {
				 
				 keyName = "未知";
			 }
			 
			String cm = po.getMachine_cm();
		 	String customerApp = po.getCustomerApp();
		 	long callNum = po.getCallNum();
		 	long useTime = (long)po.getUseTime();			//消耗时间
		 	
		 	KeyDistribFlowBo keyBo1 = keyBoMap.get(keyName);
		 	if(keyBo1 == null) {
		 		
		 		keyBo1 = new KeyDistribFlowBo();
		 		keyBo1.setKeyName(keyName);
		 		keyBoMap.put(keyName, keyBo1);
		 	}
		 	
		 	//往keyBo里面放入新的ip
		 	Set<String> n = keyBo1.getCmIpNumMap().get(cm);
		 	if(n == null){
		 		n = new HashSet<String>();
		 		keyBo1.getCmIpNumMap().put(cm,n);
		 	}
		 	n.add(po.getMachine_ip());
		 	
			//累加调用次数
		 	keyBo1.setCallNum(keyBo1.getCallNum()+callNum);	
		 	
		 	//根据customer的名字获取AppDistribFlowBo
		 	AppDistribFlowBo appBo = keyBo1.getAppMap().get(customerApp);
		 	if(appBo == null) {			//若此keyBo中的app列表没有，则新建一个
		 		
		 		appBo = new AppDistribFlowBo();
		 		appBo.setAppName(customerApp);
		 		keyBo1.getAppMap().put(customerApp, appBo);
		 	}
		 	appBo.getCallThisAppMethordSet().add(keyBo1.getKeyName());
		 	appBo.setCallNum(appBo.getCallNum() + callNum);
		 	
		 	//添加这个应用在每一个机房的总调用次数和消耗的总时间
		 	DistribFlowPo dpo = appBo.getCmMap().get(cm);
		 	if(dpo == null){
		 		dpo = new DistribFlowPo();
		 		appBo.getCmMap().put(cm,dpo);
		 	}
		 	dpo.setCallNum(dpo.getCallNum()+callNum);
		 	dpo.setUseTime(Arith.add(useTime,dpo.getUseTime()));	 
		 	
		 }
		 
		 List<KeyDistribFlowBo> boList = new ArrayList<KeyDistribFlowBo>();
		 boList.addAll(keyBoMap.values());
		 Collections.sort(boList);		 
		 return boList;		
		 
	}
	
	/**
	 * 这是添加按方法来排序信息显示时加的，和getProviderDisribFlowBo类似
	 * 组合 provider 应用信息
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public ProviderDisribFlowBo getProviderDisribFlowBo1(String provideAppName,Date collectDay){
		
		//将查询出的 provider 按照key进行组合和排序
		List<KeyDistribFlowBo> keyBoList = sortProvideKeyDistribFlow(provideAppName,collectDay);
		ProviderDisribFlowBo providerDisribFlowBo = new ProviderDisribFlowBo();
		providerDisribFlowBo.setKeyBoList(keyBoList);
		providerDisribFlowBo.setProviderAppName(provideAppName);
		long allCalls = 0;
		Map<String,KeyDistribFlowBo> keyBoMap = providerDisribFlowBo.getKeyBoMap();
		Map<String,Long> cmCallMap = providerDisribFlowBo.getCmCallMap();
		
		Set<String> customerAppNameSet = providerDisribFlowBo.getCustomerAppNameSet();
		Set<String> customerMethodNameSet = providerDisribFlowBo.getCustomerMethodNameSet();

		for(KeyDistribFlowBo bo : keyBoList) {
			
			allCalls+=bo.getCallNum();
			String keyName = bo.getKeyName();
			keyBoMap.put(keyName, bo);
			customerMethodNameSet.add(keyName);
			
			
			for(Map.Entry<String,AppDistribFlowBo> entry : bo.getAppMap().entrySet()){
				
				 customerAppNameSet.add(entry.getKey());
				 				 
				 for(Map.Entry<String,DistribFlowPo> e : entry.getValue().getCmMap().entrySet()){
					 String cm = e.getKey();
					 DistribFlowPo po = e.getValue();
					 Long num = cmCallMap.get(cm);
					 if(num == null){
						 cmCallMap.put(cm,po.getCallNum());
					 }else{
						 cmCallMap.put(cm,po.getCallNum()+num);
					 }
					
				 }
			}
		}
		providerDisribFlowBo.setAllCalls(allCalls);
		return providerDisribFlowBo;
	}
	
	/**
	 * 将查询出的 provide 按照应用进行组合和排序
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public List<AppDistribFlowBo> sortProvideAppDistribFlow(String provideAppName,Date collectDay){
		 List<DistribFlowPo> poList = jprofDao.findProvideAppDistribFlow(provideAppName, collectDay);
		 //Map<appName,appDistribFlowBo>
		 Map<String,AppDistribFlowBo> map = new HashMap<String,AppDistribFlowBo>();
		 for(DistribFlowPo po:poList){
		 	String customerApp = po.getCustomerApp();		 	
		 	if(customerApp.equals("empty")){
		 		customerApp = "未知";
			}
		 	
		 	String cm = po.getMachine_cm();
		 	String keyName = po.getKeyName();
		 	long callNum = po.getCallNum();
		 	long useTime = (long)po.getUseTime();			//消耗时间
		 	
		 	AppDistribFlowBo appBo1 = map.get(customerApp);
		 	if(appBo1 == null){
		 		appBo1 = new AppDistribFlowBo();
		 		appBo1.setAppName(customerApp);
		 		map.put(customerApp,appBo1);
		 	}
		 	Set<String> n = appBo1.getCmIpNumMap().get(cm);
		 	if(n == null){
		 		n = new HashSet<String>();
		 		appBo1.getCmIpNumMap().put(cm,n);
		 	}
		 	n.add(po.getMachine_ip());
		 	appBo1.setCallNum(appBo1.getCallNum()+callNum);
		 	
		 	
		 	KeyDistribFlowBo keyBo = appBo1.getKeyMap().get(keyName);
		 	if(keyBo == null){
		 		keyBo = new KeyDistribFlowBo();
		 		keyBo.setKeyName(keyName);		 		
		 		appBo1.getKeyMap().put(keyName,keyBo);
		 	}
		 	keyBo.getCallThisMethodAppSet().add(appBo1.getAppName());
		 	keyBo.setCallNum(keyBo.getCallNum()+callNum);
		 	
		 	
		 	DistribFlowPo dpo = keyBo.getMap().get(cm);
		 	if(dpo == null){
		 		dpo = new DistribFlowPo();
		 		keyBo.getMap().put(cm,dpo);
		 		
		 	}
		 	
		 	dpo.setCallNum(dpo.getCallNum()+callNum);
		 	
		 	dpo.setUseTime(Arith.add(useTime,dpo.getUseTime()));
		 	
		 	
		 }

		 List<AppDistribFlowBo> boList = new ArrayList<AppDistribFlowBo>();
		 boList.addAll(map.values());
		 Collections.sort(boList);		 
		 return boList;		
	}
	
	
	/**
	 * 组合 provider 应用信息
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public ProviderDisribFlowBo getProviderDisribFlowBo(String provideAppName,Date collectDay){
	
		//将查询出的 provide 按照应用进行组合和排序
		List<AppDistribFlowBo> boList = sortProvideAppDistribFlow(provideAppName,collectDay);
		ProviderDisribFlowBo providerDisribFlowBo = new ProviderDisribFlowBo();
		providerDisribFlowBo.setAppBoList(boList);
		providerDisribFlowBo.setProviderAppName(provideAppName);		
		long allCalls = 0;
		long allExceptionNum = 0;
		Map<String,AppDistribFlowBo> appBoMap = providerDisribFlowBo.getAppBoMap();
		Map<String,Long> cmCallMap = providerDisribFlowBo.getCmCallMap();
		Map<String,Long> appExceptionNumMap = providerDisribFlowBo.getAppExceptionNumMap();
		Map<String,Long> exceptionNameMap = providerDisribFlowBo.getExceptionNameNumMap();
		Set<String> customerAppNameSet = providerDisribFlowBo.getCustomerAppNameSet();
		Set<String> customerMethodNameSet = providerDisribFlowBo.getCustomerMethodNameSet();
		for(AppDistribFlowBo bo:boList){
			allCalls+=bo.getCallNum();
			String name = bo.getAppName();
			appBoMap.put(name, bo);
			customerAppNameSet.add(name);
			
			for(Map.Entry<String,KeyDistribFlowBo> entry:bo.getKeyMap().entrySet()){
				 String[] keyNames = entry.getKey().split("_");
				 customerMethodNameSet.add(keyNames[2]+keyNames[3]);
				 
				 if(keyNames[1].indexOf("Exception")>-1){
					 Long eNum = appExceptionNumMap.get(name);
					 if(eNum == null){
						 appExceptionNumMap.put(name,entry.getValue().getCallNum());
					 }else{
						 appExceptionNumMap.put(name,entry.getValue().getCallNum()+eNum);
					 }
					 
					 Long nNum = exceptionNameMap.get(keyNames[1]);
					 if(nNum == null){
						 exceptionNameMap.put(keyNames[1],entry.getValue().getCallNum());
					 }else{
						 exceptionNameMap.put(keyNames[1],entry.getValue().getCallNum()+nNum);
					 }					 
					 
					 allExceptionNum+=entry.getValue().getCallNum();
				 }				 				 
				 for(Map.Entry<String,DistribFlowPo> e:entry.getValue().getMap().entrySet()){
					 String cm = e.getKey();
					 DistribFlowPo po = e.getValue();
					 Long num = cmCallMap.get(cm);
					 if(num == null){
						 cmCallMap.put(cm,po.getCallNum());
					 }else{
						 cmCallMap.put(cm,po.getCallNum()+num);
					 }
					
				 }
			}	
		}
		
		providerDisribFlowBo.setAllCalls(allCalls);
		providerDisribFlowBo.setAllExceptionNum(allExceptionNum);
		return providerDisribFlowBo;
	}
	
	
	/**
	 * 根据依赖的应用查询出所有的访问
	 * @param customerAppName
	 * @param collectDay
	 * @return
	 */
	public List<DistribFlowPo> findCustomerAppDistribFlow(String customerAppName,Date collectDay){
		return jprofDao.findCustomerAppDistribFlow(customerAppName, collectDay);
	}
	
	
	/**
	 * 获取所有提供服务的应用
	 * @return
	 */
	public Set<String> findAllProviderAppName(){
		return jprofDao.findAllProviderAppName();
	}
	
	
	/**
	 * 获取所有依赖服务的应用
	 * @return
	 */
	public Set<String> findAllCustomerAppName(){
		return jprofDao.findAllCustomerAppName();
	}
	

}
