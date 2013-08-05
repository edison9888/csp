package com.taobao.csp.depend.auto;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

public class B2BRemoteUtil {
	private static final Logger logger = Logger.getLogger(B2BRemoteUtil.class);

	public static void excute(String ip, com.taobao.monitor.common.util.RemoteCommonUtil.CallBack lineCallBack) throws Exception{
		HttpClient httpClient = new HttpClient();
		String postUrl = "http://"+ip+":8082/scriptexcute";
		PostMethod postMethod =  new PostMethod(postUrl);
		postMethod.addParameter("token", "dcbeb81d186a89a11c1515ced9022bca");
		postMethod.addParameter("script", "netstatan.sh");

		int statusCode = httpClient.executeMethod(postMethod);
		logger.info(String.format("ÍøÖ·ÊÇ->%s,×´Ì¬Âë->%d", postUrl,statusCode));
		BufferedReader br = new BufferedReader(new InputStreamReader(postMethod.getResponseBodyAsStream(),"gbk"));
		String line = null;
		while((line = br.readLine()) != null){
			lineCallBack.doLine(line);
		}

	}
}