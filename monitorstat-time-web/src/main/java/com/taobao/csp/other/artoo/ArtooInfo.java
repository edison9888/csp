
/**
 * monitorstat-time-web
 */
package com.taobao.csp.other.artoo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

/**
 * @author xiaodu
 *
 *http://artoo.taobao.net:9999/appops-deploy/api/getDeployList.htm 获取当天的发布信息
 在线上 需要在host 文件中执行 artoo的vip 为110.75.2.111
 *
 * 下午4:36:47
 */
public class ArtooInfo {
	
	private static final Logger logger =  Logger.getLogger(ArtooInfo.class);
	
	public static final String artooUrl = "http://artoo2.taobao.net:9999/api/getPlanListByCondition.htm?";
	
	private static ArtooInfo artoo = new ArtooInfo();
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	private HttpClient httpClient = new HttpClient();
	
	private ArtooInfo(){
		//handleArtoo();
	}
	
	public static ArtooInfo get(){
		return artoo;
	}
	
	
	public List<Artoo>getRecentlyArtoo(String appName){
		Calendar cal = Calendar.getInstance();
		Date date1 = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		Date date2 = cal.getTime();
		return findArtooListByAppNameAndTime(appName,date2,date1);
	}
	
	public Artoo getRecentlySingleArtoo(String appName){
		Calendar cal = Calendar.getInstance();
		Date date1 = cal.getTime();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		Date date2 = cal.getTime();
		 List<Artoo> list = findArtooListByAppNameAndTime(appName,date2,date1);
		if(list != null&&list.size()>0){
			return list.get(0);
		}
		return null;
	}
	
	
	//获取artoo发布数据
		private  List<Artoo> getArtooData(String appName,String start,String end){
			logger.info("getArtooData,appName=" + appName + ",start=" + start + ",end=" + end);
			StringBuilder sb = new StringBuilder(artooUrl);
			sb.append("appName=" + appName).append("&startTime=").append(start).append("&endTime=").append(end);
			GetMethod getMethod = new GetMethod(sb.toString());
			List<Header> headers = new ArrayList<Header>();
	        headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
	      
	        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
	        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(200);
	        httpClient.getHttpConnectionManager().getParams().setSoTimeout(300);
	         
			try {
				   //执行getMethod
				   int statusCode = httpClient.executeMethod(getMethod);
				   if (statusCode == HttpStatus.SC_OK) {
					   //读取内容
					   String response = getMethod.getResponseBodyAsString();
					   JSONObject jsonObject = JSONObject.fromObject(response);
					   JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("data"));
					   List<Artoo> artooList = new ArrayList<Artoo>();
					   for (int i = 0; i < jsonArray.size(); i++) {
						   Artoo po = getArtooFromJson(jsonArray.getJSONObject(i));
						   if(po == null) continue;
						   artooList.add(po);
						}
					   return artooList;
				   }else{
					   logger.warn("http status is not OK,url=" + artooUrl);
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
			return new ArrayList<Artoo>();
		}

		private Artoo getArtooFromJson(JSONObject json) {
			try{
				Artoo Artoo = new Artoo();
				Artoo.setAppName(json.getString("appName") );
				Artoo.setCallSystem(json.getString("callSystem") );
				Artoo.setCreator(json.getString("creator"));
				Artoo.setDeployTime(json.getString("deployTime"));
				Artoo.setPlanType(json.getString("planType"));
				Artoo.setState(json.getString("state"));
				Artoo.setCompleteServerNum(json.getString("completeServerNum"));
				Artoo.setTotalServerNum(json.getString("totalServerNum"));
				Artoo.setFinishTime(json.getString("finishTime"));
				return Artoo;
			}catch(JSONException e){
				logger.warn("getArtooFromJson exception,json=" + json.toString(), e);
				//state字段可能为空，代表发布失败
				if(json.toString().indexOf("state") == -1){
					Artoo Artoo = new Artoo();
					Artoo.setId(json.getString("id"));
					Artoo.setAppName(json.getString("appName") );
					Artoo.setCallSystem(json.getString("callSystem") );
					Artoo.setCreator(json.getString("creator"));
					Artoo.setDeployTime(json.getString("deployTime"));
					Artoo.setPlanType(json.getString("planType"));
					Artoo.setState("failed");
					Artoo.setCompleteServerNum(json.getString("completeServerNum"));
					Artoo.setTotalServerNum(json.getString("totalServerNum"));
					Artoo.setFinishTime(json.getString("finishTime"));
					return Artoo;
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
		public List<Artoo> findArtooListByAppNameAndTime(String appName,Date startDate,Date endDate){
			if(StringUtil.isEmpty(appName) || startDate ==null || endDate == null){
				return new ArrayList<Artoo>();
			}
			
			String start = sdf.format(startDate);
			String end = sdf.format(endDate);
			return getArtooData(appName,start,end);
			
		}
	
}
