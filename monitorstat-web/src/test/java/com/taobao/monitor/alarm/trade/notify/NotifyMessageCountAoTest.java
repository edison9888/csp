package com.taobao.monitor.alarm.trade.notify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.junit.Assert;
import org.junit.Test;

import com.taobao.csp.dataserver.query.QueryUtil;

public class NotifyMessageCountAoTest {
	@Test
	public void test_采集数据() throws Exception{
		NotifyMessageCountAo.get().checkNotifyMesageCountRecPro();
		
		/////测试保存的数据
		String appName = "tradeplatform";
		String key= "notifyMessageCount";
		String ip = "172.23.202.57";
		Map<String, Map<String, Map<String, Object>>>  map = QueryUtil.queryChildHostRealTime(appName,key,ip);
		Assert.assertNotNull(map);
	}

	
	@Test
	public void test_获取哈勃数据() throws Exception{
//		  String response ="[{\"hostgroup\":\"tradelogshost.cm3\",\"item\":\"tc-delay_merge\",\"dataitem\":\"AlipayTradeCreatedProcessor_sum\"," +
//	   		"date\":\"2012-07-02\",\"data\":[{\"00:02:00\":\"8241\"},{\"00:04:00\":\"8131\"}]}]";
////	   JSONObject jsonObject = JSONObject.fromObject(response);
////		JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("data"));
//////		  JSONArray jsonArray = JSONArray.fromObject(response);
////		List<NotifyMessageCountPo> list  = new ArrayList<NotifyMessageCountPo> ();
////		for (int i = 0; i < jsonArray.size(); i++) {
////			
////		}
		 String response = "";
	      GetMethod getMethod = null;
	      try {
//	         getMethod = new GetMethod("http://110.75.127.17/monitorapi/emonitorDetailData.do?hostgroup=tradelogshost.cm3&item=tc-delay_merge&dataitem=AlipayTradeCreatedProcessor_sum&date=2012-07-02");
	    	  getMethod = new GetMethod("http://110.75.127.17/monitorapi/emonitorDetailData.do?hostgroup=tradeplatformhost.cm3&item=sum_TP_Monitor.log&dataitem=P2-Notify-sucess_sum&date=2012-07-02");      
	         HttpClient httpClient = new HttpClient();
	      
	         List<Header> headers = new ArrayList<Header>();
	         headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
	         httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
	      
	         httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(200);
	         httpClient.getHttpConnectionManager().getParams().setSoTimeout(300);
	      
	         int statusCode = httpClient.executeMethod(getMethod);

	         if (statusCode != HttpStatus.SC_OK) {
	            if (statusCode != HttpStatus.SC_NOT_MODIFIED) {
	               String errorMsg = getMethod.getResponseBodyAsString();
	               throw new Exception(statusCode + ":" + getMethod.getStatusLine().toString() + "==>" + errorMsg);
	            } else {
	            	System.out.println("null");
	            }
	         }
	         response = getMethod.getResponseBodyAsString();
	         JSONObject jsonObject = JSONObject.fromObject(response.substring(1,response.length()-1));
	         
	         JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("data"));
	         
	         for (int i = 0; i < jsonArray.size(); i++) {
//	        	 NotifyMessageCountPo po = (NotifyMessageCountPo)JSONObject.toBean(jsonArray.getJSONObject(i), NotifyMessageCountPo.class);
	        	 NotifyMessageCountPo po = new NotifyMessageCountPo();
	        	 po.setDate(jsonArray.getJSONObject(i).toString().substring(2,10));
	        	 po.setCount(jsonArray.getJSONObject(i).toString().substring(13,jsonArray.getJSONObject(i).toString().length()-2));
	        	 System.out.println("po=" + po);
	         }
	      }catch (Throwable cause) {  
	    	  System.out.println(cause.getMessage());
	      } finally {
	         if (getMethod != null) {
	            try {               
	               getMethod.releaseConnection();
	            } catch (Exception e) {
	            }
	         }
	      }

		System.out.println("response=" + response);
	}
}
