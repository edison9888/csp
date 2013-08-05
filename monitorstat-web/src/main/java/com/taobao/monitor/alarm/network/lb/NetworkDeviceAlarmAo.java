package com.taobao.monitor.alarm.network.lb;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;

public class NetworkDeviceAlarmAo {
	private static final Logger logger = Logger.getLogger(NetworkDeviceAlarmAo.class);
	private static String jsonUrl = "http://web1.monitor.taobao.com:9999/App/index.php/Api/MonitorAlertInfo/getNetAlertInfo";
	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static NetworkDeviceAlarmAo ao = new NetworkDeviceAlarmAo();
	public static NetworkDeviceAlarmAo get(){
		return ao;
	}
	
	public  Map<String, List<NetworkDeviceAlarmPo>> getLoadBalanceAlarmDataByTime(Date start,Date end){
		Map<String, List<NetworkDeviceAlarmPo>> lbAlarmMap = new HashMap<String, List<NetworkDeviceAlarmPo>>();
		logger.warn("getLoadBalanceAlarmDataByTime,start=" + start + ",end=" + end);
		for(String lbName:LoadBalanceConstants.lbIpSegmentList.keySet()){
			lbAlarmMap.put(lbName, getlbAlarmData(lbName,start,end));
		}
		return lbAlarmMap;
		
	}
	
	public List<NetworkDeviceAlarmPo>  getlbAlarmData(String lbName,Date startTime,Date endTime){
		
		HttpClient httpClient = new HttpClient();
		StringBuilder sb = new StringBuilder(jsonUrl);
		try{
			sb.append("/hostname/" + lbName + "/start_date/" + URLEncoder.encode(format.format(startTime), "gbk") + "/end_date/" +  URLEncoder.encode(format.format(endTime), "gbk"));
		}catch (UnsupportedEncodingException e) {
			logger.warn("getlbAlarmData", e);
		}
		GetMethod getMethod = new GetMethod(sb.toString());
		
		//使用系统提供的默认的恢复策略
		getMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded");
		getMethod.addRequestHeader("Accept", "text/plain");
	
		try {
			int statusCode = httpClient.executeMethod(getMethod);
			if (statusCode == HttpStatus.SC_OK) {
				String response= getMethod.getResponseBodyAsString();//读取内容
				logger.warn("getlbAlarmData,startTime=" + startTime + ",lbName=" + lbName + ",response=" + response);
				if(StringUtil.isEmpty(response)|| response.equals("null")){
					return null;
				}
				JSONArray jsonArray = JSONArray.fromObject(response);
				List<NetworkDeviceAlarmPo> list  = new ArrayList<NetworkDeviceAlarmPo>();
				for (int i = 0; i < jsonArray.size(); i++) {
					NetworkDeviceAlarmPo networkDeviceAlarmPo = getNetworkDeviceAlarmPoFromJson(jsonArray.getJSONObject(i));
					list.add(networkDeviceAlarmPo);
				}
				return list;
			}else{
				logger.warn("http status is not OK,url=" + jsonUrl);
			}
		} catch (HttpException e) {
			logger.warn("url error, check url="+jsonUrl, e);
		} catch (IOException e) {
			logger.warn("acess changfree url="+jsonUrl+" error:", e);
		} catch (Exception e) {
			logger.warn("parse json error", e);
		} finally {
			getMethod.releaseConnection();
		}
		return null;
		
	}
	
	private NetworkDeviceAlarmPo getNetworkDeviceAlarmPoFromJson(JSONObject json) throws JSONException{
		NetworkDeviceAlarmPo po = new NetworkDeviceAlarmPo();
		if(json.toString().indexOf("ServiceName") != -1){
			po.setServiceName(json.getString("ServiceName"));
		}if(json.toString().indexOf("Output") != -1){
			po.setOutput(json.getString("Output"));
		}if(json.toString().indexOf("State") != -1){
			po.setState(json.getString("State"));
		}if(json.toString().indexOf("HostName") != -1){
			po.setHostName(json.getString("HostName"));
		}if(json.toString().indexOf("Time") != -1){
			po.setTime(json.getString("Time"));
		}
		return po;
	}
	
}
