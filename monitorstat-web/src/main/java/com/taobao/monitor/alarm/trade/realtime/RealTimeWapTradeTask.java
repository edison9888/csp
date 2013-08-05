package com.taobao.monitor.alarm.trade.realtime;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
/**
 * 无线秒级数据
 * @author hongbing.ww
 * @since 2013-05-29
 */
public class RealTimeWapTradeTask extends TimerTask {
	private static final Logger logger = Logger.getLogger(RealTimeTradeTask.class);
	private static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	private static String URL = "http://110.75.14.21/sharereport/sumRealTimeTrade.do?";
	private HttpClient httpClient = new HttpClient();
	@Override
	public void run() {
		Date start = new Date();
		start.setTime(start.getTime()-5000);		//延迟5s，查询5s前的数据
		StringBuilder sbCreate = new StringBuilder(URL);
		sbCreate.append("start=").append(sdfTime.format(start)).append("&end=").append(sdfTime.format(start)).append("&interval=wapsec&invalid=true");
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
		try {
			int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			if (statusCodeCreate != HttpStatus.SC_OK) {
				logger.warn("RealTimeTradeTask get Time=" + sdfTime.format(start));
			}
		} catch (Exception e) {
			logger.warn("RealTimeTradeTask exception,url=" + sbCreate.toString(), e);
		} finally {
			getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
	}
}
