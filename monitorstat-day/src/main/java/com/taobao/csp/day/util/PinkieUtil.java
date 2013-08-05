package com.taobao.csp.day.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.csp.day.base.HostInfo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.GroupManager;


public class PinkieUtil {
	
	private static String HEAD_STATINFO = "statInfo";
	
	private static Logger logger = Logger.getLogger(PinkieUtil.class);
	
	public static HostInfo getHostInfo(HostPo po) {
		HostInfo hostInfo = new HostInfo();
		
		String ip = po.getHostIp();
		hostInfo.setAppGroup(GroupManager.get().getGroupByAppAndIp(po.getOpsName(), ip));
		hostInfo.setIp(ip);
		hostInfo.setRoom(po.getHostSite());
		
		return hostInfo;
	}
	
	
	public static long getCurrentSize(String ip, String path) {
		long size = 0;
		
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		
		// advised to involve  java net socketexception connection reset
		httpClient.getParams().setParameter("http.socket.timeout", new Integer(0)); 
		httpClient.getParams().setParameter("http.connection.stalecheck", new Boolean(true));
		
		String testUrl = "http://" + ip + ":8082/get"
		+ path + "?" + "begin=" + 0
		+ "&end=" + 1
		+ "&encode=text";
		
		GetMethod httpGet = new GetMethod(testUrl);
		try {
			int status = httpClient.executeMethod(httpGet);
			
			if (status == HttpStatus.SC_OK) {
				String statInfo = httpGet.getResponseHeader(HEAD_STATINFO).getValue();
				if (statInfo != null) {
					size = Long.parseLong(statInfo.split(",")[0].split(":")[1]);
				}
			}
		} catch (Throwable e) {
			logger.error("getCurrentSize error:" + testUrl, e);
		} finally {
			httpGet.releaseConnection();
		}
		
		return size;
	}
}
