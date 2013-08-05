package com.taobao.monitor.alarm.network.asw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.monitor.alarm.network.lb.NetworkDeviceAlarmAo;
import com.taobao.monitor.alarm.network.lb.NetworkDeviceAlarmPo;

public class ASWAlaramAo {
	private static final Logger logger = Logger.getLogger(ASWAlaramAo.class);
	private static ASWAlaramAo ao = new ASWAlaramAo();
	public static ASWAlaramAo get(){
		return ao;
	}
	//保存虚拟机的ip到asw的名称的对应关系
	private ConcurrentHashMap<String,String> virtualIp2AswNameMap = new ConcurrentHashMap<String,String>();
	
	private static String getHostMachineNameByVirtualIpUrl = "http://opsdb2.ops.aliyun-inc.com/api/v2.1/node/search?start=0&num=0&_username=droid/opsfreecli";

	private static String getHostMachineIpByHostMachineNameUrl = "http://opsdb2.ops.aliyun-inc.com/api/v2.1/node/search?start=0&num=0&_username=droid/opsfreecli";

	private static String getASWNameByHostMachineIpUrl = "http://opsdb2.ops.aliyun-inc.com/api/api_getInfoByIPList.php?t=sw";

	public String getHostMachineNameByVirtualIp(String virtualIp){
		HttpClient httpClient = new HttpClient();
		StringBuilder sb = new StringBuilder(getHostMachineNameByVirtualIpUrl + "&q=dns_ip==" + virtualIp);
		GetMethod getMethod = new GetMethod(sb.toString());
		
		//使用系统提供的默认的恢复策略
		getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		getMethod.addRequestHeader("Accept", "text/plain");
	
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String response= getMethod.getResponseBodyAsString();//读取内容
				logger.warn("getHostMachineNameByVirtualIp,url=" + sb.toString() + ",response=" + response);
				if(StringUtil.isEmpty(response)){
					return null;
				}
				
				JSONObject jsonObject = JSONObject.fromObject(response);
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				if(jsonArray.size() ==0){
					return null;
				}
				String hostMachineName = jsonArray.getJSONObject(0).getString("vmparent");
				return hostMachineName;
			}else{
				logger.warn("http status is not OK,url=" + sb.toString());
			}
		} catch (HttpException e) {
			logger.warn("url error, check url="+sb.toString(), e);
		} catch (IOException e) {
			logger.warn("acess changfree url="+sb.toString()+" error:", e);
		} catch (Exception e) {
			logger.warn("parse json error", e);
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	
	public String getHostMachineIpByHostMachineName(String hostMachineName){
		HttpClient httpClient = new HttpClient();
		StringBuilder sb = new StringBuilder(getHostMachineIpByHostMachineNameUrl + "&q=nodename==" + hostMachineName);
		GetMethod getMethod = new GetMethod(sb.toString());
		//使用系统提供的默认的恢复策略
		getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		getMethod.addRequestHeader("Accept", "text/plain");
	
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String response= getMethod.getResponseBodyAsString();//读取内容
				logger.warn("getHostMachineIpByHostMachineName,url=" + sb.toString() + ",response=" + response);
				if(StringUtil.isEmpty(response)){
					return null;
				}
				
				JSONObject jsonObject = JSONObject.fromObject(response);
				JSONArray jsonArray = jsonObject.getJSONArray("result");
				if(jsonArray.size() ==0){
					return null;
				}
				String hostMachineIp = jsonArray.getJSONObject(0).getString("dns_ip");
				return hostMachineIp;
			}else{
				logger.warn("http status is not OK,url=" + sb.toString());
			}
		} catch (HttpException e) {
			logger.warn("url error, check url="+sb.toString(), e);
		} catch (IOException e) {
			logger.warn("acess changfree url="+sb.toString()+" error:", e);
		} catch (Exception e) {
			logger.warn("parse json error", e);
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	
	public String getASWNameByHostMachineIp(String hostMachineIp){
		HttpClient httpClient = new HttpClient();
		StringBuilder sb = new StringBuilder(getASWNameByHostMachineIpUrl + "&q=" + hostMachineIp);
		GetMethod getMethod = new GetMethod(sb.toString());
		//使用系统提供的默认的恢复策略
		getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		getMethod.addRequestHeader("Accept", "text/plain");
	
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String response= getMethod.getResponseBodyAsString();//读取内容
				logger.warn("getASWNameByHostMachineIp,url=" + sb.toString() + ",response=" + response);
				if(StringUtil.isEmpty(response)){
					return null;
				}
				JSONArray jsonArray = JSONArray.fromObject(response);
				if(jsonArray.size() ==0){
					return null;
				}
				String aswName = jsonArray.getJSONObject(0).getString("sw_name");
				return aswName;
			}else{
				logger.warn("http status is not OK,url=" + sb.toString());
			}
		} catch (HttpException e) {
			logger.warn("url error, check url="+sb.toString(), e);
		} catch (IOException e) {
			logger.warn("acess changfree url="+sb.toString()+" error:", e);
		} catch (Exception e) {
			logger.warn("parse json error", e);
		} finally {
			getMethod.releaseConnection();
		}
		return null;
	}
	
	public List<NetworkDeviceAlarmPo>  getlbAlarmData(String hostIp,Date startTime,Date endTime){
		List<NetworkDeviceAlarmPo> list = new ArrayList<NetworkDeviceAlarmPo>();
		String aswName = virtualIp2AswNameMap.get(hostIp);
		if(StringUtil.isBlank(aswName)){
			String hostMachineName = getHostMachineNameByVirtualIp(hostIp);
			if(StringUtil.isEmpty(hostMachineName)) return list;
			String hostMachineIp = getHostMachineIpByHostMachineName(hostMachineName);
			if(StringUtil.isEmpty(hostMachineIp)) return list;
			aswName = getASWNameByHostMachineIp(hostMachineIp);
			if(StringUtil.isEmpty(aswName)) return list;
			virtualIp2AswNameMap.putIfAbsent(hostIp, aswName);
		}
		
		return NetworkDeviceAlarmAo.get().getlbAlarmData(aswName, startTime, endTime);
	}
	
	public String getAswNameByVirtualIpFromMap(String hostIp){
		return virtualIp2AswNameMap.get(hostIp);
		
	}
}
