package com.taobao.monitor.web.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

/**
 * 
 * @author xiaodu
 * @version 2010-9-16 上午10:21:55
 */
public class RequestByUrl extends PostMethod{

	private static Logger log = Logger.getLogger(RequestByUrl.class);

	public static String getMessageByJsp(String url) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = null;
			try {
//				HttpClient client = new HttpClient();
//				GetMethod getMethod = new GetMethod("http://localhost:8090/autoload/loadrun/show.do?method=showAll&appIds=330");
//				client.executeMethod(getMethod);
//				sb.append(getMethod.getResponseBodyAsString());
				java.net.URL openUrl = new java.net.URL(url);
				InputStream in = openUrl.openStream();
				reader = new BufferedReader(new InputStreamReader(in));
				String line = null;
				while((line=reader.readLine())!=null){
					sb.append(line).append("\n");
				}
			}catch (Exception e) {
				log.info("get url exception" + e);
			}finally{
				if(reader != null)
					try {
						reader.close();
					} catch (IOException e) {
					}
			}
//			HttpClient httpClient = new HttpClient();
//			GetMethod getMethod = new GetMethod(url); 
//			int statusCode = httpClient.executeMethod(getMethod);
//			if (statusCode == HttpStatus.SC_OK) {
//				return getMethod.getResponseBodyAsString();
//			}
		} catch (Exception e) {
			log.error("", e);
		}
		return sb.toString();
	}
	
	/**
	 * post提交数据处理中文乱码
	 * @author 斩飞
	 * @param url
	 * @param parms
	 * @return
	 * 2011-5-16 - 下午05:25:15
	 */
	public static String postMesageByJsp(String url,Map parms){
	      String body=null;
	      HttpClient httpClient = new HttpClient();
	      httpClient.getParams().setParameter(HttpMethodParams.
	    		  HTTP_CONTENT_CHARSET,"GBK");
	      PostMethod postMethod = new PostMethod(url);
	      postMethod.getParams().setContentCharset("GBK");  
	      NameValuePair[] data = new NameValuePair[parms.keySet().size()]; 
	      Iterator it = parms.entrySet().iterator();
	      int i=0;
	      while (it.hasNext()) { 
	         Map.Entry entry = (Map.Entry) it.next(); 
	         Object key = entry.getKey(); 
	         Object value = entry.getValue(); 
	         data[i]=new NameValuePair(key.toString(),value.toString());
	         i++;
	      }
	      postMethod.setRequestBody(data);
	      try {
	         int statusCode = httpClient.executeMethod(postMethod);
	         while (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
	            Header locationHeader = postMethod.getResponseHeader("location");
	            String location = null;
	            if (locationHeader != null) { 
	               location = locationHeader.getValue(); 
	               statusCode = httpClient.executeMethod(postMethod);	              
	               log.info("The page was redirected to:" + location+" :"+statusCode);
	            } else {
	               log.info("Location field value is null.");
	            }
	          }
	      body = postMethod.getResponseBodyAsString();
	      } catch (Exception e) {
	    	  log.info("",e);
	      }
	      return body;
	   }
}
