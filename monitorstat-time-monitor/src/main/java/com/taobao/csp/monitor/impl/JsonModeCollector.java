
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;

import com.taobao.csp.monitor.DataCollector;
import com.taobao.csp.monitor.JobInfo;

/**
 * @author xiaodu
 *
 * ����6:23:27
 */
public class JsonModeCollector implements DataCollector{
	
	private static final Logger logger =  Logger.getLogger(JsonModeCollector.class);
	
	private JobInfo jobInfo;
	
	public JsonModeCollector(JobInfo jobInfo){
		this.jobInfo = jobInfo;
	}

	@Override
	public void collect(CallBack call) {
		String httpUrl =this.jobInfo.getFilepath();//�����httpģʽ�£����URL�ĵ�ַ   d:��仰ò����㺯������������
		if(httpUrl == null){
			return ;
		}
		try {
			String body = getBody(httpUrl);
			if(body != null)
				call.readerLine(body);
		}  catch (Exception e) {
			logger.error("http ��ȡ"+httpUrl+" ����", e);
		} 
	}

	@Override
	public void release() {
		
		
	}

	@Override
	public String getName() {
		return "http";
	}
	
	
	
	private String getBody(String url) {
		 GetMethod postMethod = new GetMethod(url);
		 postMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 10000);
		 int statusCode;
		try {
			statusCode = HttpclientManager.executeMethod(postMethod);
			 if(statusCode == HttpStatus.SC_OK){
	        	 return postMethod.getResponseBodyAsString();
	         }
		} catch (Exception e1) {
		}finally{
			postMethod.releaseConnection();
		}
		return null;
	}
}
