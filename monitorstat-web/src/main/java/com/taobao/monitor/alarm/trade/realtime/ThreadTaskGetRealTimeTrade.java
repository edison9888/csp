package com.taobao.monitor.alarm.trade.realtime;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.trade.realtime.po.RealTimeTradePo;

public class ThreadTaskGetRealTimeTrade implements Callable<RealTimeTradePo> {
	private static final Logger logger = Logger.getLogger(ThreadTaskGetRealTimeTrade.class);
	private Date startTime;
	private CountDownLatch latch;
	
	private  HttpClient httpClient = new HttpClient();
	private String createAndPaidURL = "http://tradereport.taobao.org/sharereport/sumRealTimeTrade.do?";
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private DecimalFormat df = new DecimalFormat("0.00");
	
    public ThreadTaskGetRealTimeTrade(Date startTime, CountDownLatch latch) {
		super();
		this.startTime = startTime;
		this.latch = latch;
	}

	@Override  
    public RealTimeTradePo call() throws Exception {  
		RealTimeTradePo po = new RealTimeTradePo();
		Date end = startTime;
		StringBuilder sbCreate = new StringBuilder(createAndPaidURL);
		sbCreate.append("start=" + sdfDay.format(startTime) + "T" + sdfTime.format(startTime)).append("&end=").append(sdfDay.format(end) + "T" + sdfTime.format(end)).append("&interval=second");
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		
		List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
      
        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
         
		try {
			   int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			   if (statusCodeCreate == HttpStatus.SC_OK) {
				   String response = getMethodCreate.getResponseBodyAsString();
				   JSONObject jsonObject = JSONObject.fromObject(response);
				   
				   po.setC2cCreateCnt(jsonObject.getInt("ac"));
				   po.setB2cCreateCnt(jsonObject.getInt("bc"));
				   po.setJhsCreateCnt(jsonObject.getInt("cc"));
				   
				   po.setC2cCreateSum(df.format((double)jsonObject.getLong("acs")/100));
				   po.setB2cCreateSum(df.format((double)jsonObject.getLong("bcs")/100));
				   po.setJhsCreateSum(df.format((double)jsonObject.getLong("ccs")/100));
				   
				   po.setC2cPaidCnt(jsonObject.getInt("ap"));
				   po.setB2cPaidCnt(jsonObject.getInt("bp"));
				   po.setJhsPaidCnt(jsonObject.getInt("cp"));
				   
				   po.setC2cPaidSum(df.format((double)jsonObject.getLong("aps")/100));
				   po.setB2cPaidSum(df.format((double)jsonObject.getLong("bps")/100));
				   po.setJhsPaidSum(df.format((double)jsonObject.getLong("cps")/100));
				   
				   po.setCreateSum(df.format((double)jsonObject.getLong("csum")/100));
				   po.setPaidSum(df.format((double)jsonObject.getLong("psum")/100));
				   
			   }else{
				   logger.warn("http status is not OK,url=" + sbCreate.toString());
			   }
			   
		} catch (Exception e) {
			logger.warn("getThreadTaskReadlTimeTradeByTime exception,url=" + sbCreate.toString(), e);
		} finally {
			   getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
		//for thread
		latch.countDown();
		po.setTime(new Date(startTime.getTime()));
		return po;
    }
	
}
