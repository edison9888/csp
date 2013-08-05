//
//package com.taobao.monitor.dependent.job;
//
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.UUID;
//
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//import com.taobao.monitor.common.po.AppInfoPo;
//import com.taobao.monitor.common.po.HostPo;
//import com.taobao.monitor.common.util.CspCacheTBHostInfos;
//import com.taobao.monitor.dependent.ao.AppJarAo;
//import com.taobao.monitor.dependent.appinfo.AppJar;
//import com.taobao.monitor.dependent.appinfo.AppJarCheck_Tmp;
//import com.taobao.monitor.dependent.appinfo.AppStatus;
//import com.taobao.monitor.web.cache.AppCache;
//
///**
// * 
// * @author xiaodu
// * @version 2011-5-4 ÉÏÎç09:37:25
// */
//public class AppJarInfoJob implements Job{
//
//	@Override
//	public void execute(JobExecutionContext context) throws JobExecutionException {
//		//Set<String> opsNameSet = OpsFreeHostCache.get().getAllOpsApp();
//		
////		Set<String> opsNameSet = new HashSet<String>();
////		opsNameSet.add("tbuic");
////		opsNameSet.add("tc");
////		opsNameSet.add("shopcenter");
////		opsNameSet.add("uicfinal");
////		opsNameSet.add("tbskip");
////		opsNameSet.add("mms");
////		opsNameSet.add("mytaobao");
////		opsNameSet.add("hesper");
////		opsNameSet.add("shopsystem");
////		opsNameSet.add("detail");
////		opsNameSet.add("buy");
////		opsNameSet.add("trademanager");
////		opsNameSet.add("itemcenter");
////		opsNameSet.add("login");
//		
//		Set<Integer> appSetId = new HashSet<Integer>();
//		appSetId.add(7);
//		appSetId.add(322);
//		appSetId.add(4);
//		appSetId.add(21);
//		appSetId.add(46);
//		appSetId.add(17);
//		appSetId.add(11);
//		appSetId.add(2);
//		appSetId.add(3);
//		appSetId.add(1);
//		appSetId.add(323);
//		appSetId.add(8);
//		appSetId.add(16);
//	
//		
//		for(Integer appid:appSetId){
//			
//			AppInfoPo appinfo = AppCache.get().getKey(appid);
//			
//			System.out.println("execute :"+appinfo.getOpsField()+":"+appinfo.getOpsName());
// 			List<HostPo> hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName( appinfo.getOpsName());
// 			for(HostPo po:hostList){
// 				try {
// 					AppJarCheck_Tmp check = new AppJarCheck_Tmp(po.getHostIp());
// 	 				String id = UUID.randomUUID().toString();
// 	 				AppStatus app = new AppStatus();
// 					app.setAppName(check.getAppName());
// 					app.setHostIp(po.getHostIp());
// 					app.setHostSite(po.getHostSite());
// 					app.setHttpdStartTime(check.getApacheStartTime());
// 					app.setId(id);
// 					app.setJbossStartTime(check.getJbossStartTime());
// 					app.setWebInfo(check.getWebXml());
// 					app.setWebxInfo(check.getWebxXml());
// 					
// 					AppJarAo.get().addAppStatus(app);
// 					
// 					List<AppJar> jarList = check.getJavaJar();
// 					for(AppJar jar :jarList){
// 						jar.setAppStatusId(id);
// 						AppJarAo.get().addAppJar(jar);
// 					}
// 					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
// 				
// 			}
// 			
// 			System.out.println("end execute :"+appinfo.getOpsField()+":"+appinfo.getOpsName());
//		}
//	}
//
//}
