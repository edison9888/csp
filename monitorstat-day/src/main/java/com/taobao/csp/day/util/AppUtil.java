package com.taobao.csp.day.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.csp.day.base.DataType;
import com.taobao.csp.day.config.AppLogPathConfig;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

/***
 * 应用信息的工具类
 * @author youji.zj
 * 
 * @version 1.0 2012-10-16
 *
 */
public class AppUtil {

	public static Logger logger = Logger.getLogger(AppUtil.class);

	/***
	 * 获取tddl的应用列表
	 * @return
	 */
	public static List<AppInfoPo> getTddlApps() {
		List<AppInfoPo> tddlApps = new ArrayList<AppInfoPo>();
		
		logger.info("find day apps...");
		List<AppInfoPo> allApps = findAllDayApps();
		logger.info("app size is:" + allApps.size());
		for (AppInfoPo po : allApps) {
			String opsName = po.getOpsName();
			
			if (!opsName.equals("inventoryplatform") && !opsName.equals("inventoryaccount")) {
				continue;
			}
				
			List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(opsName);
			String logPath = AppLogPathConfig.getLogPath(opsName, DataType.TDDL);
			String backLogPath = AppLogPathConfig.getBackLogPath(opsName, DataType.TDDL);
			HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			for (HostPo host : hosts) {
				String ip = host.getHostIp();
				
				// 防止日志在当时未创建，利用双重校验
				String url = "http://" + ip + ":8082/get" + logPath + "?" + "begin=0&end=1&encode=text";
				String backUrl = "http://" + ip + ":8082/get" + backLogPath + "?" + "begin=0&end=1&encode=text";
				
				GetMethod httpGet = new GetMethod(url);
				GetMethod backHttpGet = new GetMethod(backUrl);
				try {
					int httpStatus = httpClient.executeMethod(httpGet);
					if (httpStatus == HttpStatus.SC_OK) {
						tddlApps.add(po);
						break;
					}
					
					int backHttpStatus = httpClient.executeMethod(backHttpGet);
					if (backHttpStatus == HttpStatus.SC_OK) {
						tddlApps.add(po);
						break;
					}
				} catch (Exception e) {
					// logger.error(e);
					break;
				} finally {
					httpGet.releaseConnection();
					backHttpGet.releaseConnection();
				}
			}
		}
		
		return tddlApps;
	}
	
	/***
	 * 获取ph的应用列表
	 * @return
	 */
	public static List<AppInfoPo> getSphApps() {
		List<AppInfoPo> sphApps = new ArrayList<AppInfoPo>();
		
//		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
//		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
//		List<AppInfoPo> allApps = findAllDayApps();
//		for (AppInfoPo po : allApps) {
//			String opsName = po.getOpsName();
//			logger.info("appName:" + opsName);
//			List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(opsName);
//			String logPath = AppLogPathConfig.getLogPath(opsName, DataType.SPH);
//			
//			for (HostPo host : hosts) {
//				String ip = host.getHostIp();
//				String url = "http://" + ip + ":8082/get" + logPath + "?" + "begin=0&end=1&encode=text";
//				
//				GetMethod httpGet = new GetMethod(url);
//				try {
//					int httpStatus = httpClient.executeMethod(httpGet);
//					if (httpStatus == HttpStatus.SC_OK) {
//						sphApps.add(po);
//						break;
//					}
//				} catch (Exception e) {
////					logger.error("getSphApps exception", e);
//					break;
//				} finally {
//					httpGet.releaseConnection();
//				}
//			}
//		}
		
		AppInfoPo po = new AppInfoPo();
		po.setAppName("tf_buy");
		po.setOpsName("tf_buy");
		sphApps.add(po);
		
		return sphApps;
	}
	
	/***
	 * 获取指定的apache 应用
	 * @return
	 */
	public static List<AppInfoPo> getApacheAppsSpecialApps() {
		List<AppInfoPo> apps = new ArrayList<AppInfoPo>();
		
		AppInfoPo po_1 = new AppInfoPo();
		po_1.setAppName("tf_buy");
		po_1.setOpsName("tf_buy");
		apps.add(po_1);
		
//		AppInfoPo po_2 = new AppInfoPo();
//		po_2.setAppName("detail");
//		po_2.setOpsName("detail");
//		apps.add(po_2);
//		
//		AppInfoPo po_3 = new AppInfoPo();
//		po_3.setAppName("shopsystem");
//		po_3.setOpsName("shopsystem");
//		apps.add(po_3);
//		
//		AppInfoPo po_4 = new AppInfoPo();
//		po_4.setAppName("tf_tm");
//		po_4.setOpsName("tf_tm");
//		apps.add(po_4);
		
//		AppInfoPo po_5 = new AppInfoPo();
//		po_5.setAppName("cart");
//		po_5.setOpsName("cart");
//		apps.add(po_5);
//		
//		AppInfoPo po_6 = new AppInfoPo();
//		po_6.setAppName("login");
//		po_6.setOpsName("login");
//		apps.add(po_6);
		
		return apps;
	}
	
	/***
	 * 获取指定的tdod 应用
	 * @return
	 */
	public static List<AppInfoPo> getTdodApps() {
		logger.info("get tddl apps...");
		List<AppInfoPo> apps = new ArrayList<AppInfoPo>();
		
		AppInfoPo po_1 = new AppInfoPo();
		po_1.setAppName("tf_buy");
		po_1.setOpsName("tf_buy");
		apps.add(po_1);
		
		AppInfoPo po_2 = new AppInfoPo();
		po_2.setAppName("detail");
		po_2.setOpsName("detail");
		apps.add(po_2);
		
		AppInfoPo po_3 = new AppInfoPo();
		po_3.setAppName("shopsystem");
		po_3.setOpsName("shopsystem");
		apps.add(po_3);
		
		AppInfoPo po_4 = new AppInfoPo();
		po_4.setAppName("tf_tm");
		po_4.setOpsName("tf_tm");
		apps.add(po_4);
		
		AppInfoPo po_5 = new AppInfoPo();
		po_5.setAppName("cart");
		po_5.setOpsName("cart");
		apps.add(po_5);
		
		AppInfoPo po_6 = new AppInfoPo();
		po_6.setAppName("login");
		po_6.setOpsName("login");
		apps.add(po_6);
		
		logger.info("end get tddl apps...");
		
		return apps;
	}
	
	/***
	 * 获取小拇指access的应用列表
	 * @return
	 */
	public static List<AppInfoPo> getPinkieAccessApps() {
		List<AppInfoPo> tddlApps = new ArrayList<AppInfoPo>();
		
		logger.info("find day apps...");
		List<AppInfoPo> allApps = findAllDayApps();
		logger.info("app size is:" + allApps.size());
		for (AppInfoPo po : allApps) {
			String opsName = po.getOpsName();
			
//			if (!opsName.equals("lottery")) {
//				continue;
//			}
				
			List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(opsName);
			String logPath = AppLogPathConfig.getLogPath(opsName, DataType.PINKIE_ACCESS);

			HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			// 抽样查询
			int sampleCount = hosts.size() / 3 + 1;
			if (sampleCount > 5) {
				sampleCount = 5;
			}
			
			int count = 0;
			for (HostPo host : hosts) {
				count++;
				if (count > sampleCount) {
					break;
				}
				
				String ip = host.getHostIp();
				
				String url = "http://" + ip + ":8082/get" + logPath + "?" + "begin=0&end=1&encode=text";
				
				GetMethod httpGet = new GetMethod(url);
				try {
					int httpStatus = httpClient.executeMethod(httpGet);
					if (httpStatus == HttpStatus.SC_OK) {
						tddlApps.add(po);
						break;
					}
				} catch (Exception e) {
					break;
				} finally {
					httpGet.releaseConnection();
				}
			}
		}
		
		return tddlApps;
	}
	
	/***
	 * 获取小拇指gc的应用列表
	 * @return
	 */
	public static List<AppInfoPo> getGcApps() {
		List<AppInfoPo> tddlApps = new ArrayList<AppInfoPo>();
		
		logger.info("find day apps...");
		List<AppInfoPo> allApps = findAllDayApps();
		logger.info("app size is:" + allApps.size());
		for (AppInfoPo po : allApps) {
			String opsName = po.getOpsName();
			
//			if (!opsName.equals("detail")) {
//				continue;
//			}
				
			List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(opsName);
			String logPath = AppLogPathConfig.getLogPath(opsName, DataType.GC);

			HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
			httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);

			// 抽样查询
			int sampleCount = hosts.size() / 3 + 1;
			if (sampleCount > 5) {
				sampleCount = 5;
			}
			
			int count = 0;
			for (HostPo host : hosts) {
				count++;
				if (count > sampleCount) {
					break;
				}
				
				String ip = host.getHostIp();
				
				String url = "http://" + ip + ":8082/get" + logPath + "?" + "begin=0&end=1&encode=text";
				
				GetMethod httpGet = new GetMethod(url);
				try {
					int httpStatus = httpClient.executeMethod(httpGet);
					if (httpStatus == HttpStatus.SC_OK) {
						tddlApps.add(po);
						break;
					}
				} catch (Exception e) {
					break;
				} finally {
					httpGet.releaseConnection();
				}
			}
		}
		
		return tddlApps;
	}
	
	/***
	 * todo 封装下
	 * @param appName
	 * @param urls
	 * @return
	 */
	private static boolean existsLog(String appName, List<String> urls) {
		boolean existLog = false;
		
		return existLog;
	}
	
	/***
	 * 获取所有的日报应用
	 * @return
	 */
	private static List<AppInfoPo> findAllDayApps() {
		List<AppInfoPo> dayApps = AppInfoAo.get().findAllDayApp();
		logger.info("day apps size:" + dayApps.size());
		return dayApps;
	}
	
}
