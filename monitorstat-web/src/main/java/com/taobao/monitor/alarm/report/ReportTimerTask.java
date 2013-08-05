package com.taobao.monitor.alarm.report;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;

public class ReportTimerTask extends TimerTask {
	private static final Logger logger = Logger.getLogger(ReportTimerTask.class);
	private final String ALARM_REPORT_URL = "http://cm.taobao.net:9999/monitorstat/alarm/report/alarm_report.jsp";
	private final int TIME_OUT_MILLS = 8000;
	private final String MAIL_TITILE = "无线系统告警日报";
	private final String MAIL_LIST = "hongbing.ww@taobao.com";
	@Override  
	public void run() {  
		String mailContext = getMailContextByHttp();
		if(mailContext != null){
			MessageSendFactory.create(MessageSendType.Email).send(MAIL_LIST, MAIL_TITILE, mailContext);
		} else {
			logger.warn("AlarmEmail send error!");
		}
	}
	
	private String getMailContextByHttp(){
		//HTTPClient 请求告警页面
		StringBuilder sbCreate = new StringBuilder(ALARM_REPORT_URL);
		sbCreate.append("?mailTime=2012-07-05 01:40:00");
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
		HttpClient httpClient = new HttpClient();
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIME_OUT_MILLS);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(TIME_OUT_MILLS);
		try {
			int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			if (statusCodeCreate == HttpStatus.SC_OK) {
				String response = getMethodCreate.getResponseBodyAsString();
				return response;
			} else {
				logger.warn("http status is not OK,url=" + sbCreate.toString());
			}
		} catch (Exception e) {
			logger.warn("getMailContextByHttp exception,url=" + sbCreate.toString(), e);
		} finally {
			getMethodCreate.releaseConnection();
		}
		return null;
	}
}
