//package com.taobao.monitor.stat.util;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//
//import org.apache.log4j.Logger;
//import org.dom4j.Document;
//import org.dom4j.Node;
//import org.dom4j.io.SAXReader;
//
//import com.taobao.monitor.common.po.HostPo;
//import com.taobao.monitor.common.util.OpsFreeHostCache;
//
///**
// * 
// * @author xiaodu
// * @version 2010-11-7 上午10:43:13
// */
//public class DepIpInfoContain_tmp {
//	
//	
//	private static DepIpInfoContain_tmp instance = new DepIpInfoContain_tmp(); 
//
//	private static final Logger logger = Logger.getLogger(DepIpInfoContain_tmp.class);
//
//	private DepIpInfoContain_tmp() {
//	};
//	
//	public static DepIpInfoContain_tmp get(){
//		return instance;
//	}
//
//	private  Map<String, DepIpInfo> deipInfoMap = new HashMap<String, DepIpInfo>();
//	
//	private  Map<String, DepIpInfo> deipInfoNameMap = new HashMap<String, DepIpInfo>();
//	
//	
//	
//	public  void clear(){
//		deipInfoMap.clear();
//		deipInfoNameMap.clear();
//	}
//	
//	
//	public  DepIpInfo getByName(String name){		
//		DepIpInfo depIpInfo = deipInfoNameMap.get(name);		
//		if (depIpInfo != null) {
//			
//			if(depIpInfo.getIp() == null){
//				return null;
//			}
//			
//			return depIpInfo;
//		} else {
//			
//			
//			
//			List<HostPo> list = OpsFreeHostCache.get().getHostListNoCache(name);
//			if(list.size() ==0){
//				logger.info("" + name + "：  无法获取信息");
//				return null;
//			}else{
//				
//			}
//			
//
//			BufferedInputStream input = null;
//			try {
//				URL url = new URL(
//						"http://opsdb2.ops.cnz.alimama.com:9999/index.php/search_device_full_info/?return_types=xml&name="
//								+ name);
//				URLConnection urlCon = url.openConnection();
//				urlCon.setDoInput(true);
//				urlCon.setConnectTimeout(1000000);
//				urlCon.connect();
//				input = new BufferedInputStream(urlCon.getInputStream());
//				SAXReader reader = new SAXReader();
//				Document doc = reader.read(input);
//
//				Node node = doc.selectSingleNode("/root/device");
//				DepIpInfo deInfo = new DepIpInfo();
//				if(node == null){
//					deipInfoNameMap.put(name, deInfo);
//					logger.info("" + name + "：  无法获取信息");
//					return null;
//				}else{
//					String machine_room_name = node.selectSingleNode("machine_room_name").getStringValue();
//					String module_name = node.selectSingleNode("module_name").getStringValue();
//					String ip = node.selectSingleNode("ip_0").getStringValue();
//					deInfo.setAppName(module_name);
//					deInfo.setSiteName(machine_room_name);
//					deInfo.setIp(ip);
//					deInfo.setMachineName(name);
//					deipInfoMap.put(ip, deInfo);
//					deipInfoNameMap.put(name, deInfo);
//					logger.info("获取：" + ip + "信息");
//					getByOpsName(module_name);
//					return deInfo;
//				}
//				
//				
//			} catch (Exception e) {
//				deipInfoMap.put(name, new DepIpInfo());
//			} finally {
//				if (input != null) {
//					try {
//						input.close();
//					} catch (IOException e) {
//
//					}
//				}
//			}
//			return null;
//
//		}
//	}
//
//	public  DepIpInfo getByIp(String ip) {
//		DepIpInfo depIpInfo = deipInfoMap.get(ip);		
//		if (depIpInfo != null) {			
//			if(depIpInfo.getIp() == null){
//				return null;
//			}			
//			return depIpInfo;
//		} else {
//			BufferedInputStream input = null;
//			try {
//				URL url = new URL(
//						"http://opsdb2.ops.cnz.alimama.com:9999/index.php/search_device_full_info/?return_types=xml&ip_0="
//								+ ip);
//				URLConnection urlCon = url.openConnection();
//				urlCon.setDoInput(true);
//				urlCon.setConnectTimeout(1000000);
//				urlCon.connect();
//				input = new BufferedInputStream(urlCon.getInputStream());
//				SAXReader reader = new SAXReader();
//				Document doc = reader.read(input);
//
//				Node node = doc.selectSingleNode("/root/device");
//				DepIpInfo deInfo = new DepIpInfo();
//				if(node == null){
//					deipInfoMap.put(ip, deInfo);
//					deInfo.setAppName("未知");
//					deInfo.setIp(ip);
//					deInfo.setSiteName("未知");
//					deInfo.setMachineName("未知");
//					logger.info("" + ip + "：  无法获取信息");
//					return deInfo;
//				}else{
//					String machine_room_name = node.selectSingleNode("machine_room_name").getStringValue();
//					String module_name = node.selectSingleNode("module_name").getStringValue();
//					String name = node.selectSingleNode("name").getStringValue();
//					deInfo.setAppName(module_name);
//					deInfo.setSiteName(machine_room_name);
//					deInfo.setIp(ip);
//					deInfo.setMachineName(name);
//					deipInfoMap.put(ip, deInfo);
//					deipInfoNameMap.put(name, deInfo);
//					logger.info("获取：" + ip + "信息");
//					getByOpsName(module_name);
//					return deInfo;
//				}			
//				
//			} catch (Exception e) {
//				deipInfoMap.put(ip, new DepIpInfo());
//			} finally {
//				if (input != null) {
//					try {
//						input.close();
//					} catch (IOException e) {
//
//					}
//				}
//			}
//			return null;
//		}
//	}
//	
//	public  void getByOpsName(String opsName){
//		
//
//		BufferedInputStream input = null;
//		try {
//			URL url = new URL(
//					"http://opsdb2.ops.cnz.alimama.com:9999/index.php/search_device_full_info/?return_types=xml&module_name="
//							+ opsName);
//			URLConnection urlCon = url.openConnection();
//			urlCon.setDoInput(true);
//			urlCon.setConnectTimeout(1000000);
//			urlCon.connect();
//			input = new BufferedInputStream(urlCon.getInputStream());
//			SAXReader reader = new SAXReader();
//			Document doc = reader.read(input);
//
//			List<Node> nodeList = doc.selectNodes("/root/device");
//			logger.info("获取：" + opsName + "信息");
//			for (int i = 0; i < nodeList.size(); i++) {
//				DepIpInfo deInfo = new DepIpInfo();
//				Node node = nodeList.get(i);
//				String machine_room_name = node.selectSingleNode("machine_room_name").getStringValue();
//				String module_name = node.selectSingleNode("module_name").getStringValue();
//				String name = node.selectSingleNode("name").getStringValue();
//				String ip_0 = node.selectSingleNode("ip_0").getStringValue();
//				String state_name =node.selectSingleNode("state_name").getStringValue();
//				if(!"working_online".equals(state_name)){
//					continue;
//				}
//				deInfo.setAppName(module_name);
//				deInfo.setSiteName(machine_room_name);
//				deInfo.setIp(ip_0);
//				deInfo.setMachineName(name);
//				deipInfoMap.put(ip_0, deInfo);
//				deipInfoNameMap.put(name, deInfo);
//			}
//		} catch (Exception e) {
//		} finally {
//			if (input != null) {
//				try {
//					input.close();
//				} catch (IOException e) {
//
//				}
//			}
//		}
//		
//	}
//	
//}
