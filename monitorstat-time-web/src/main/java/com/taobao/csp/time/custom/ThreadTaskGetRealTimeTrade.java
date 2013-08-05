package com.taobao.csp.time.custom;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.csp.time.web.po.RealTimeTradePo;
import com.taobao.csp.time.web.po.RealTimeTradeSinglePo;

public class ThreadTaskGetRealTimeTrade implements Callable<RealTimeTradePo> {
	private static final Logger logger = Logger.getLogger(ThreadTaskGetRealTimeTrade.class);
	private Date startTime;
	private CountDownLatch latch;
	
	private  HttpClient httpClient = new HttpClient();
	private String createUrl = "http://tradereport.taobao.org/sharereport/realtimeTrade.do?";
	private String paidUrl = "http://tradereport.taobao.org/sharereport/realtimeTradePaid.do?";
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	
    public ThreadTaskGetRealTimeTrade(Date startTime, CountDownLatch latch) {
		super();
		this.startTime = startTime;
		this.latch = latch;
	}

    /**
     * Tair中交易数据按20s存储一份。每个线程请求1分钟的数据。 
     */
	@Override  
    public RealTimeTradePo call() throws Exception {  
		RealTimeTradePo po = new RealTimeTradePo();
		Date end = startTime;
		String timeString = sdfTime.format(end);
		timeString = timeString.substring(0, timeString.lastIndexOf(':')) + ":";
		String[] timeArray = new String[]{timeString + "00",timeString + "20",timeString + "21",timeString + "40",timeString + "41",timeString + "59"}; 
		String startDay = sdfDay.format(startTime);
		
		for(int i=0; i<timeArray.length;) {
			StringBuilder sbCreate = new StringBuilder(createUrl);
			sbCreate.append("start=" + startDay + "T" + timeArray[i]).append("&end=").append(startDay + "T" + timeArray[i+1]);
			GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
			
			StringBuilder sbPaid = new StringBuilder(paidUrl);
			sbPaid.append("start=" + startDay + "T" + sdfTime.format(startTime)).append("&end=").append(startDay + "T" + sdfTime.format(end));
			GetMethod getMethodPaid = new GetMethod(sbPaid.toString());
			
			List<Header> headers = new ArrayList<Header>();
	        headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
	      
	        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
	        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
	        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
	         
			try {
				   int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
				   RealTimeTradeSinglePo createPo  = new RealTimeTradeSinglePo();
				   if (statusCodeCreate == HttpStatus.SC_OK) {
					   String response = getMethodCreate.getResponseBodyAsString();
					   createPo =  extracTradeCount(response);
				   }else{
					   logger.warn("http status is not OK,url=" + sbCreate.toString());
				   }
				   int statusCodePaid = httpClient.executeMethod(getMethodPaid);
				   RealTimeTradeSinglePo paidPo =  new RealTimeTradeSinglePo();
				   if (statusCodePaid == HttpStatus.SC_OK) {
					   String response = getMethodPaid.getResponseBodyAsString();
					   paidPo =  extracTradeCount(response);
				   }else{
					   logger.warn("http status is not OK,url=" + sbCreate.toString());
				   }
//			        System.out.println("*******************************");
//			        System.out.println("sbCreate==" + sbCreate);
//			        System.out.println("sbPaid==" + sbPaid);
//			        System.out.println("createPo.getB2cCnt()==" + createPo.getB2cCnt());
//			        System.out.println("createPo.getC2cCnt()==" + createPo.getC2cCnt());
//			        System.out.println("paidPo.getB2cCnt()==" + paidPo.getB2cCnt());
//			        System.out.println("createPo.getC2cCnt()==" + createPo.getC2cCnt());
//			        
				   po.setB2cCreateCnt(po.getB2cCreateCnt() + createPo.getB2cCnt());
				   po.setC2cCreateCnt(po.getC2cCreateCnt() + createPo.getC2cCnt());
				   po.setB2cPaidCnt(po.getB2cPaidCnt() + paidPo.getB2cCnt());
				   po.setC2cPaidCnt(po.getC2cPaidCnt() + paidPo.getC2cCnt());
				   po.setTime(startTime);
			} catch (Exception e) {
				logger.warn("getThreadTaskReadlTimeTradeByTime exception,url=" + sbCreate.toString(), e);
			} finally {
				   getMethodCreate.releaseConnection();
			}
			getMethodCreate = null;
			
			//for thread
			i += 2;
			latch.countDown();
		}
		return po;
    }
	
	private RealTimeTradeSinglePo extracTradeCount(String response){
		int firstIndex = response.indexOf("}");
		int secondIndex = response.indexOf("}", firstIndex+1);
		
		String sourceString = response.substring(secondIndex + 3,response.length()-1);
		String[] source = sourceString.split(",");
		int b2cCnt = 0;
		int c2cCnt = 0;
		for(int i=0;i<source.length;i++){
			String []part =  source[i].split(":");
			if(part.length !=2) continue;
			if(!StringUtil.isNumeric(part[1])) continue;
			if(part[0].indexOf("b2c") != -1){
				b2cCnt +=  Integer.parseInt(part[1]);
			}else{
				c2cCnt += Integer.parseInt(part[1]);
			}
		}
		RealTimeTradeSinglePo po = new RealTimeTradeSinglePo();
		po.setB2cCnt(b2cCnt);
		po.setC2cCnt(c2cCnt);
		return  po;
	}
}
