package com.taobao.monitor.alarm.source.artoo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.monitor.alarm.source.change.ChangeConstants;

public class ArtooAo {
	private static final Logger logger = Logger.getLogger(ArtooAo.class);
	private  HttpClient httpClient = new HttpClient();
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private static ArtooAo ao = new ArtooAo();
	public static ArtooAo get(){
		return ao;
	}
	
	public static boolean validatorAppName(String appName){
		return ChangeConstants.appFromArtooList.contains(appName);
	}
	
	//获取artoo发布数据
	private  List<ArtooPo> getArtooData(String appName,String start,String end){
		StringBuilder sb = new StringBuilder(ChangeConstants.artooUrl);
		sb.append("appName=" + appName).append("&startTime=").append(start).append("&endTime=").append(end);
		logger.warn("getArtooData,url=" + sb.toString());
		GetMethod getMethod = new GetMethod(sb.toString());
		List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
      
        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
         
		try {
			   //执行getMethod
			   int statusCode = httpClient.executeMethod(getMethod);
			   if (statusCode == HttpStatus.SC_OK) {
				   //读取内容
				   String response = getMethod.getResponseBodyAsString();
				   JSONObject jsonObject = JSONObject.fromObject(response);
				   JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("data"));
				   List<ArtooPo> artooList = new ArrayList<ArtooPo>();
				   for (int i = 0; i < jsonArray.size(); i++) {
					   if(ChangeConstants.appFromArtooList.contains(appName)){
						   ArtooPo po = getArtooPoFromJson(jsonArray.getJSONObject(i));
						   if(po == null) continue;
						   artooList.add(po);
						   }
					}
				   return artooList;
			   }else{
				   logger.warn("http status is not OK,url=" + ChangeConstants.artooUrl);
			   }
		} catch (HttpException e) {
			logger.warn("request artoo url failed", e);
		} catch (IOException e) {
			logger.warn("request artoo io error", e);
		} catch (Exception e) {
			logger.warn("parse json error", e);
		} finally {
			   getMethod.releaseConnection();
		}
		getMethod = null;
		return null;
	}

	private ArtooPo getArtooPoFromJson(JSONObject json) {
		try{
			ArtooPo artooPo = new ArtooPo();
			artooPo.setId(json.getString("id"));
			artooPo.setAppName(json.getString("appName") );
			artooPo.setCallSystem(json.getString("callSystem") );
			artooPo.setCreator(json.getString("creator"));
			artooPo.setDeployTime(json.getString("deployTime"));
			artooPo.setPlanType(json.getString("planType"));
			artooPo.setState(json.getString("state"));
			artooPo.setCompleteServerNum(json.getString("completeServerNum"));
			artooPo.setTotalServerNum(json.getString("totalServerNum"));
			artooPo.setFinishTime(json.getString("finishTime"));
			return artooPo;
		}catch(JSONException e){
			logger.warn("getArtooPoFromJson exception,json=" + json.toString(), e);
			//state字段可能为空，代表发布失败
			if(json.toString().indexOf("state") == -1){
				ArtooPo artooPo = new ArtooPo();
				artooPo.setId(json.getString("id"));
				artooPo.setAppName(json.getString("appName") );
				artooPo.setCallSystem(json.getString("callSystem") );
				artooPo.setCreator(json.getString("creator"));
				artooPo.setDeployTime(json.getString("deployTime"));
				artooPo.setPlanType(json.getString("planType"));
				artooPo.setState("failed");
				artooPo.setCompleteServerNum(json.getString("completeServerNum"));
				artooPo.setTotalServerNum(json.getString("totalServerNum"));
				artooPo.setFinishTime(json.getString("finishTime"));
				return artooPo;
			}
			return null;
		}
		
	}
	
	/**
	 * 查询某个时间段的appName的发布数据
	 * @param appName
	 * @param start
	 * @param end
	 * @return
	 */
	public List<ArtooPo> findArtooPoListByAppNameAndTime(String appName,Date startDate,Date endDate){
		if(StringUtil.isEmpty(appName) || startDate ==null || endDate == null){
			return null;
		}
		
		String start = sdf.format(startDate);
		String end = sdf.format(endDate);
		return getArtooData(appName,start,end);
		
	}
	
}
