package com.taobao.monitor.alarm.trade.realtime;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.alibaba.common.lang.StringUtil;
import com.taobao.monitor.alarm.trade.realtime.po.RealTimeTradePo;
import com.taobao.monitor.alarm.trade.realtime.po.RealTimeTradeSinglePo;


public class RealTimeTradeAo {
	private static final Logger logger = Logger.getLogger(RealTimeTradeAo.class);
	private static RealTimeTradeAo  ao = new RealTimeTradeAo();
	private static int HTTP_THREAD_COUNT = 10; //线程数
	private static int HTTP_CHART_THREAD_COUNT = 10; //线程数
	private static int THIRD_TIME_INTERVAL = 41; //20s阶段查询，间隔41s就停止
	public static int BASE_DAY_INTERVAL = -1; //对比基线的数据取间隔7天的数据
	public static  RealTimeTradeAo get(){
		return ao;
	}
	
	private  HttpClient httpClient = new HttpClient();
	private String allURL = "http://tradereport.taobao.org/sharereport/sumRealTimeTrade.do?";
	private SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
	private SimpleDateFormat chartXAxisTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private DecimalFormat df = new DecimalFormat("0.00");
	/**
	 * 获得n个时刻的shareport数据
	 * 主要为了获得当前n个数据和基线对比数据
	 * 用于初始化flash数据
	 * @param startTime 查询开始时间
	 * @param intervalSecend间隔秒数（总共查询10*second的数据)
	 * @return
	 */
	public String getMultiRealTimeTradeJson(int intervalSecend, Date startTime){
		startTime.setTime( startTime.getTime() - 10000);//延后10s
		List<Future<RealTimeTradePo>> rtPoList = new ArrayList<Future<RealTimeTradePo>>();
		List<RealTimeTradePo> poList = new ArrayList<RealTimeTradePo>();
		
		try {
			ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
	    			5, 2*HTTP_CHART_THREAD_COUNT, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));
			CountDownLatch doneSignal = new CountDownLatch(2*HTTP_CHART_THREAD_COUNT);
			threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			Calendar cal = Calendar.getInstance();
			cal.setTime(startTime);
			for(int i=0; i<HTTP_CHART_THREAD_COUNT; i++){
				rtPoList.add( threadPool.submit( new ThreadTaskGetRealTimeTrade(cal.getTime(), doneSignal)));
				cal.add(Calendar.SECOND, -intervalSecend);
			}
			
			cal.setTime(startTime);
			cal.add(Calendar.DAY_OF_YEAR, BASE_DAY_INTERVAL);
			for(int i=0; i<HTTP_CHART_THREAD_COUNT; i++){
				rtPoList.add( threadPool.submit( new ThreadTaskGetRealTimeTrade(cal.getTime(), doneSignal)));
				cal.add(Calendar.SECOND, -intervalSecend);
			}
			
			doneSignal.await();
			
			for (Future<RealTimeTradePo> fr : rtPoList) {  
	            if (fr.isDone()) {  
	            	poList.add(fr.get());  
	            } else {  
	            	Log.info("getMultiRealTimeTrade not ends,fr=" + fr.toString());
	            }  
	        }
			//关闭线程池
	        threadPool.shutdown(); 
		} catch (Exception e) {
			logger.warn("getMultiRealTimeTrade error:", e);
		}
		return convertPoList2Json(poList);
	}
	
	/**
	 * 一次获取shareport的当前10s钟的创建和付款数据
	 * @return
	 */
	public List<RealTimeTradePo> getMultiRealTimeTradeNow(){
		List<Future<RealTimeTradePo>> rtPoList = new ArrayList<Future<RealTimeTradePo>>();
		List<RealTimeTradePo> poList = new ArrayList<RealTimeTradePo>();
		
		try {
			ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
					5, HTTP_THREAD_COUNT, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));
			CountDownLatch doneSignal = new CountDownLatch(10);
			threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			Calendar now = Calendar.getInstance();
			now.add(Calendar.SECOND, -10);//延后10s
			for(int i=0; i<HTTP_THREAD_COUNT; i++){
				rtPoList.add( threadPool.submit( new ThreadTaskGetRealTimeTrade(now.getTime(), doneSignal)));
				now.add(Calendar.SECOND, -1);
			}
			doneSignal.await();
			
			for (Future<RealTimeTradePo> fr : rtPoList) {  
				if (fr.isDone()) {  
					poList.add(fr.get());  
				} else {  
					Log.info("getMultiRealTimeTradeNow not ends,fr=" + fr.toString());
				}  
			}
			//关闭线程池
			threadPool.shutdown(); 
		} catch (Exception e) {
			logger.warn("getMultiRealTimeTradeNow error:", e);
		} 
		return poList;
	}
	
	/**
	 * 查询当前时间和七天前的该时刻的创建和付款数据
	 * @return json
	 */
	public String getReadlTimeTradeJson(){
		StringBuilder sb = new StringBuilder();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, -10); //延迟后10s
		Date start = cal.getTime();
		Date end = start;
		
		StringBuilder sbCreate = new StringBuilder(allURL);
		sbCreate.append("start=" + sdfDay.format(start) + "T" + sdfTime.format(start)).append("&end=").append(sdfDay.format(end) + "T" + sdfTime.format(end)).append("&interval=second");
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		
		cal.add(Calendar.DAY_OF_YEAR, BASE_DAY_INTERVAL);
		Date baseStartTime = cal.getTime();
		Date baseEndTime = baseStartTime;
		
		StringBuilder sbBaseCreate = new StringBuilder(allURL);
		sbCreate.append("start=").append(sdfDay.format(baseStartTime)).append("T").append(sdfTime.format(baseStartTime)).append("&end=").append(sdfDay.format(baseEndTime)).append("T").append(sdfTime.format(baseEndTime)).append("&interval=second");
		GetMethod getMethodBaseCreate = new GetMethod(sbBaseCreate.toString());
		
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
		
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
		
		try {
			//执行getMethod
			int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			int statusCodeBaseCreate = httpClient.executeMethod(getMethodBaseCreate);
			if(statusCodeCreate == HttpStatus.SC_OK && statusCodeBaseCreate == HttpStatus.SC_OK ){
				sb.append(getAllTradeCount(getMethodCreate.getResponseBodyAsString(), getMethodCreate.getResponseBodyAsString(), start));
			} else {
				logger.warn("http status is not OK,error:" + sbCreate.toString() +" and "+getMethodBaseCreate.toString());
			}
		} catch (Exception e) {
			logger.warn("getReadlTimeTradeJson exception,url=" + sbCreate.toString(), e);
		} finally {
			getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
		return sb.toString();
	}
	
	/**
	 * 获得n个时刻的shareport数据
	 * 主要为了获得当前n个数据和基线对比数据
	 * 用于初始化flash数据
	 * @param startTime 查询开始时间
	 * @param intervalSecend间隔秒数（总共查询10*second的数据)
	 * @return
	 */
	public String getMultiRealTimeCreateAndPaidTradeJson(int intervalSecend, Date startTime){
		List<Future<RealTimeTradePo>> rtPoList = new ArrayList<Future<RealTimeTradePo>>();
		List<RealTimeTradePo> poList = new ArrayList<RealTimeTradePo>();
		
		try {
			ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
	    			5, 2*HTTP_CHART_THREAD_COUNT, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(500));
			CountDownLatch doneSignal = new CountDownLatch(2*HTTP_CHART_THREAD_COUNT);
			threadPool.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
			Calendar cal = Calendar.getInstance();
			cal.setTime(startTime);
			for(int i=0; i<HTTP_CHART_THREAD_COUNT; i++){
				rtPoList.add( threadPool.submit( new ThreadTaskGetRealTimeTrade(cal.getTime(), doneSignal)));
				cal.add(Calendar.SECOND, -intervalSecend);
			}
			
			cal.setTime(startTime);
			cal.add(Calendar.DAY_OF_YEAR, BASE_DAY_INTERVAL);
			for(int i=0; i<HTTP_CHART_THREAD_COUNT; i++){
				rtPoList.add( threadPool.submit( new ThreadTaskGetRealTimeTrade(cal.getTime(), doneSignal)));
				cal.add(Calendar.SECOND, -intervalSecend);
			}
			
			doneSignal.await();
			
			for (Future<RealTimeTradePo> fr : rtPoList) {  
	            if (fr.isDone()) {  
	            	poList.add(fr.get());  
	            } else {  
	            	Log.info("getMultiRealTimeTrade not ends,fr=" + fr.toString());
	            }  
	        }
			//关闭线程池
	        threadPool.shutdown(); 
		} catch (Exception e) {
			logger.warn("getMultiRealTimeTrade error:", e);
		}
		return convertPoList2Json(poList);
	}
	
	/**
	 * 查询当前start的这一秒的数据
	 * and gmt_biz_create >=#startTime# and gmt_biz_create <=#endTime#  sharereport里面查询数据的格式
	 * @param start
	 * @return
	 */
	public String getTradeJSONPayAndCreateByTime(Date start){
		Date end = start;
		//延迟1s，查询1s前的数据
		start.setTime(start.getTime()-1000);
		StringBuilder sbCreate = new StringBuilder(allURL);
		sbCreate.append("start=" + sdfDay.format(start) + "T" + sdfTime.format(start)).append("&end=").append(sdfDay.format(end) + "T" + sdfTime.format(end));
		logger.warn("getReadlTimeTradeByTime,createUrl=" + sbCreate.toString());
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
		
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
		try {
			int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			if (statusCodeCreate == HttpStatus.SC_OK) {
				//读取内容
				return getMethodCreate.getResponseBodyAsString();
			}else{
				logger.warn("http status is not OK,url=" + sbCreate.toString());
			}
			
		} catch (Exception e) {
			logger.warn("getReadlTimeTradeByTime exception,url=" + sbCreate.toString(), e);
		} finally {
			getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
		return "";
	}
	
	/**
	 * 查询当前start的这一秒的数据
	 * 当前数据延迟10s查询
	 * and gmt_biz_create >=#startTime# and gmt_biz_create <=#endTime#  sharereport里面查询数据的格式
	 * @param start
	 * @return
	 */
	public RealTimeTradePo getReadlTimeTradeByTime(Date start){
		Date end = start;
		start.setTime(start.getTime()-10000);
		StringBuilder sbCreate = new StringBuilder(allURL);
		sbCreate.append("start=" + sdfDay.format(start) + "T" + sdfTime.format(start)).append("&end=").append(sdfDay.format(end) + "T" + sdfTime.format(end)).append("&interval=second");
//		logger.warn("getReadlTimeTradeByTime,createUrl=" + sbCreate.toString());
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		
		List<Header> headers = new ArrayList<Header>();
        headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
      
        httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
        httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
         
		try {
			   int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			   RealTimeTradePo po  = new RealTimeTradePo();
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
			   } else {
				   logger.warn("http status is not OK,url=" + sbCreate.toString());
			   }
			   po.setTime(start);
			   return po;
		} catch (Exception e) {
			logger.warn("getReadlTimeTradeByTime exception,url=" + sbCreate.toString(), e);
		} finally {
			   getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
		return null;
	}
	
	/**
	 * 查询当前start的所在的1秒的数据
	 * 当前数据延迟10s查询
	 * @param start
	 * @return
	 */
	public String getReadlTimeTradeBySecond(Date start){
		Date end = start;
		start.setTime(start.getTime()-10000);
		StringBuilder sbCreate = new StringBuilder(allURL);
		sbCreate.append("start=").append(sdfDay.format(start)).append("T").append(sdfTime.format(start)).append("&end=").append(sdfDay.format(end)).append("T").append(sdfTime.format(end)).append("&interval=second");
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
		
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
		StringBuilder sb = new StringBuilder();
		try {
			int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			if (statusCodeCreate == HttpStatus.SC_OK) {
				String response = getMethodCreate.getResponseBodyAsString();
				//重新组装json数据
				response = response.trim();
				sb.append(response.substring(0, response.length()-1)).append(",\"date\":\"").append(chartXAxisTime.format(start)).append("\"}");
			} else {
				logger.warn("http status is not OK,url=" + sbCreate.toString());
			}
		} catch (Exception e) {
			logger.warn("getReadlTimeTradeByTime exception,url=" + sbCreate.toString(), e);
		} finally {
			getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
		if(sb.length() == 0){
			sb.append("{\"aps\":0,\"csum\":0,\"cps\":0,\"ccs\":0,\"bp\":0,\"bps\":0,\"psum\":0,\"cp\":0,\"ap\":0,\"bps\":0,\"ac\":0,\"bc\":0,\"cc\":0,\"acs\":0").append(",\"date\":\"").append(chartXAxisTime.format(start)).append("\"}");
		} 
		return sb.toString();
	}
	/**
	 * 查询当前start-41s的所在的20秒的数据
	 * 当前数据延迟10s查询
	 * @param start
	 * @return
	 */
	public String getReadlTimeTradeByThird(Date start){
		Date end = start;
		start.setTime(start.getTime()-THIRD_TIME_INTERVAL*1000);
		StringBuilder sbCreate = new StringBuilder(allURL);
		sbCreate.append("start=").append(sdfDay.format(start)).append("T").append(sdfTime.format(start)).append("&end=").append(sdfDay.format(end)).append("T").append(sdfTime.format(end)).append("&interval=third");
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
		
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
		StringBuilder sb = new StringBuilder();
		try {
			int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			if (statusCodeCreate == HttpStatus.SC_OK) {
				String response = getMethodCreate.getResponseBodyAsString();
				//重新组装json数据
				response = response.trim();
				sb.append(response.substring(0, response.length()-1)).append(",\"day\":").append(start.getDate()).append(",\"date\":\"").append(chartXAxisTime.format(start)).append("\"}");
			} else {
				logger.warn("http status is not OK,url=" + sbCreate.toString());
			}
		} catch (Exception e) {
			logger.warn("getReadlTimeTradeByTime exception,url=" + sbCreate.toString(), e);
		} finally {
			getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
		return sb.toString();
	}
	/**
	 * 查询当前start的所在的20秒的数据
	 * 当前数据延迟10s查询
	 * @param start
	 * @return
	 */
	public String getReadlTimeTradeByThird2(Date start){
		Date end = start;
		StringBuilder sbCreate = new StringBuilder(allURL);
		Date now = new Date();
		sbCreate.append("start=").append(sdfDay.format(start)).append("T").append(sdfTime.format(start)).append("&end=").append(sdfDay.format(end)).append("T").append(sdfTime.format(end)).append("&interval=third");
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
		
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
		StringBuilder sb = new StringBuilder();
		try {
			int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			if (statusCodeCreate == HttpStatus.SC_OK) {
				String response = getMethodCreate.getResponseBodyAsString();
				//重新组装json数据
				response = response.trim();
				sb.append(response.substring(0, response.length()-1)).append(",\"day\":").append(start.getDate()).append(",\"over\":\"").append((now.getTime()-start.getTime())<=THIRD_TIME_INTERVAL*1000).append("\"}");
			} else {
				logger.warn("http status is not OK,url=" + sbCreate.toString());
			}
		} catch (Exception e) {
			logger.warn("getReadlTimeTradeByTime exception,url=" + sbCreate.toString(), e);
		} finally {
			getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
		return sb.toString();
	}
	
	/**
	 * 查询当前start的所在的60秒的数据
	 * 当前数据延迟10s查询
	 * @param start
	 * @return
	 */
	public String getReadlTimeTradeByMinute(Date start){
		Date end = start;
		StringBuilder sbCreate = new StringBuilder(allURL);
		Date now = new Date();
		sbCreate.append("start=").append(sdfDay.format(start)).append("T").append(sdfTime.format(start)).append("&end=").append(sdfDay.format(end)).append("T").append(sdfTime.format(end)).append("&interval=minute");
		GetMethod getMethodCreate = new GetMethod(sbCreate.toString());
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("Content-Type","application/x-www-form-urlencoded"));
		
		httpClient.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		httpClient.getHttpConnectionManager().getParams().setSoTimeout(5000);
		StringBuilder sb = new StringBuilder();
		try {
			int statusCodeCreate = httpClient.executeMethod(getMethodCreate);
			if (statusCodeCreate == HttpStatus.SC_OK) {
				String response = getMethodCreate.getResponseBodyAsString();
				//重新组装json数据
				response = response.trim();
				sb.append(response.substring(0, response.length()-1)).append(",\"day\":").append(start.getDate()).append(",\"over\":\"").append((now.getTime()-start.getTime())<=THIRD_TIME_INTERVAL*1000).append("\"}");
			} else {
				logger.warn("http status is not OK,url=" + sbCreate.toString());
			}
		} catch (Exception e) {
			logger.warn("getReadlTimeTradeByTime exception,url=" + sbCreate.toString(), e);
		} finally {
			getMethodCreate.releaseConnection();
		}
		getMethodCreate = null;
		return sb.toString();
	}
	
	private String getAllTradeCount(String response, String baseResponse, Date start){
		StringBuilder sb = new StringBuilder();
		JSONObject jsonObject = JSONObject.fromObject(response);
		JSONObject baseJsonObject = JSONObject.fromObject(baseResponse);
		   
		sb.append("{\"c2cc\":").append(jsonObject.getLong("ac"));
		sb.append(",\"b2cc\":").append(jsonObject.getLong("bc"));
		sb.append(",\"jhsc\":").append(jsonObject.getLong("cc"));
		sb.append(",\"c2cp\":").append(jsonObject.getLong("ap"));
		sb.append(",\"b2cp\":").append(jsonObject.getLong("bp"));
		sb.append(",\"jhsp\":").append(jsonObject.getLong("cp"));
		
		sb.append(",\"c2cbc\":").append(baseJsonObject.getLong("ac"));
		sb.append(",\"b2cbc\":").append(baseJsonObject.getLong("bc"));;
		sb.append(",\"jhsbc\":").append(baseJsonObject.getLong("cc"));;
		sb.append(",\"c2cbp\":").append(baseJsonObject.getLong("ap"));;
		sb.append(",\"b2cbp\":").append(baseJsonObject.getLong("bp"));;
		sb.append(",\"jhsbp\":").append(baseJsonObject.getLong("cp"));;
		sb.append(",\"time\":\"");
		sb.append(chartXAxisTime.format(start));
		sb.append("\"}");
		return  sb.toString();
	}
	
	private RealTimeTradeSinglePo getTradeCnt(String response){
		int firstIndex = response.indexOf("}");
		int secondIndex = response.indexOf("}", firstIndex+1);
		
		String sourceString = response.substring(secondIndex + 3,response.length()-1);
		String[] source = sourceString.split(",");
		int b2cCnt = 0;
		int c2cCnt = 0;
		int jhsCnt = 0;
		for(int i=0;i<source.length;i++){
			String []part =  source[i].split(":");
			if(part.length !=2) continue;
			if(!StringUtil.isNumeric(part[1])) continue;
			if(part[0].indexOf("b2c") != -1){
				b2cCnt +=  Integer.parseInt(part[1]);
			} else {
				c2cCnt += Integer.parseInt(part[1]);
			}
			if(part[0].indexOf("jhs") != -1){
				jhsCnt += Integer.parseInt(part[1]);
			}
		}
		
		RealTimeTradeSinglePo po = new RealTimeTradeSinglePo();
		po.setB2cCnt(b2cCnt);
		po.setC2cCnt(c2cCnt);
		po.setJhsCnt(jhsCnt);
		return  po;
	}
	
	private String convertPoList2Json( List<RealTimeTradePo> list ){
		StringBuilder sb = new StringBuilder();
		if(list.size() == 0){
			//还得处理异常，否则没法显示
			Date tmpDate = new Date();
			sb.append("[");
			for(int i=1; i<=HTTP_CHART_THREAD_COUNT-1; i++){
				int sum = i+10;
				sb.append("{");
				sb.append("\"c2cc\":");
				sb.append(sum);
				sb.append(",\"b2cc\":");
				sb.append(sum-2);
				sb.append(",\"c2cp\":");
				sb.append(sum-4);
				sb.append(",\"b2cp\":");
				sb.append(sum-6);
				sb.append(",\"jhsc\":");
				sb.append(sum-5);
				sb.append(",\"jhsp\":");
				sb.append(sum-7);
				sb.append(",\"time\":\"");
				sb.append(chartXAxisTime.format(tmpDate));
				sb.append("\"},");
			}
		} else {
			sb.append("[");
			for(RealTimeTradePo po:list){
				sb.append("{").append("\"c2cc\":").append(po.getC2cCreateCnt());
				sb.append(",\"b2cc\":").append(po.getB2cCreateCnt());
				sb.append(",\"c2cp\":").append(po.getC2cPaidCnt());
				sb.append(",\"b2cp\":").append(po.getB2cPaidCnt());
				sb.append(",\"jhsc\":").append(po.getJhsCreateCnt());
				sb.append(",\"jhsp\":").append(po.getJhsPaidCnt());
				sb.append(",\"time\":\"").append(chartXAxisTime.format(po.getTime())).append("\"},");
			}
		}
		return sb.substring(0, sb.length()-1)+"]";
	}

}
