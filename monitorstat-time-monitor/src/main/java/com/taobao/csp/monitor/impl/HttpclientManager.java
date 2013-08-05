
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 * @author xiaodu
 *
 * ÏÂÎç2:33:07
 */
public class HttpclientManager {
	
	private static HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
	
	static{
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
	}
	
	
	public static int executeMethod(HttpMethod method) throws HttpException, IOException{
		int statusCode =httpClient.executeMethod(method);
		return statusCode;
	}

}
