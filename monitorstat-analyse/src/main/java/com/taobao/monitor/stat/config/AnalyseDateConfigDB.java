//
//package com.taobao.monitor.stat.config;
//
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//import org.dom4j.Document;
//import org.dom4j.DocumentException;
//import org.dom4j.Element;
//import org.dom4j.Node;
//import org.dom4j.io.SAXReader;
//
//import com.taobao.monitor.common.ao.center.AppInfoAo;
//import com.taobao.monitor.common.po.AppInfoPo;
//import com.taobao.monitor.common.util.Utlitites;
///**
// * 
// * @author xiaodu
// * @version 2010-5-19 ÉÏÎç10:36:53
// */
//public class AnalyseDateConfigDB {
//	
//	private static final Logger logger =  Logger.getLogger(AnalyseDateConfigDB.class);
//	
//	private static String SCHEDULE_XML_PATH="app_schedul.xml";
//	
//	private boolean runScpLocal = false;
//	
//	
//	private Map<String,AppAnalyseInfo> appMap = new HashMap<String, AppAnalyseInfo>();
//	
//	
//	public Map<String, AppAnalyseInfo> getAppMap() {
//		return appMap;
//	}
//
//	public void setAppMap(Map<String, AppAnalyseInfo> appMap) {
//		this.appMap = appMap;
//	}
//
//	
//	public AnalyseDateConfigDB() {
//				
//		parseApp();
//	}
//	
//	private void parseApp(){
//		
////		List<AppInfoPo> appInfoList = AppInfoAo.get().findAllAppInfo();
////		for(AppInfoPo appInfo:appInfoList){
////			AppAnalyseInfo po = new AppAnalyseInfo();
////			po.setAppName(appInfo.getAppName());
////			
////			
////		}
//		
//		
//		
//		List list = document.selectNodes("/schedule/apps/app");		
//		Iterator iter = list.iterator();
//		while (iter.hasNext()) {
//			Element app = (Element) iter.next();
//			
//			String appName = app.attributeValue("name");
//			String feature = app.attributeValue("feature");
//			String opsfree_name = app.attributeValue("opsfree_name");
//			
//			AppAnalyseInfo po = new AppAnalyseInfo();
//			po.setAppName(appName);
//			po.setFeature(feature);	
//			po.setOpsfreeName(opsfree_name);
//			List analyseList = app.selectNodes("analyses/analyse");
//			Iterator analyseiter = analyseList.iterator();
//			while (analyseiter.hasNext()) {
//				Element analyse = (Element) analyseiter.next();				
//				String className = analyse.attributeValue("class");
//				if(className!=null){
//					po.getAnalyseList().add(className);
//				}				
//			}
//			
//			List logFileList = app.selectNodes("filelist/file");
//			Iterator logiter = logFileList.iterator();
//			while (logiter.hasNext()) {
//				Element log = (Element) logiter.next();				
//				String logpath = log.attributeValue("path");
//				if(logpath!=null){
//					po.getRemoteLogPath().add(logpath);
//				}				
//			}
//			appMap.put(appName, po);
//		}
//		
//	}
//
//	public boolean isRunScpLocal() {
//		return runScpLocal;
//	}
//
//	public void setRunScpLocal(boolean runScpLocal) {
//		this.runScpLocal = runScpLocal;
//	}
//	
//
//}
