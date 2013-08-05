
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.monitor.DataCollector;
import com.taobao.csp.monitor.JobInfo;
import com.taobao.monitor.common.util.BufferedReader2;

/**
 * @author xiaodu
 *
 * 下午6:23:27
 */
public class GetHttpModeCollector implements DataCollector{
	
	private static final Logger logger =  Logger.getLogger(GetHttpModeCollector.class);
	
	private JobInfo jobInfo;
	
	public GetHttpModeCollector(JobInfo jobInfo){
		this.jobInfo = jobInfo;
	}
	
	private String parsePathTime(String path){
		 
		 Pattern pattern = Pattern.compile("\\$\\{([\\w-]+)\\}");
		 
		 Matcher m = pattern.matcher(path);		 
		 while(m.find()){
			 String format = m.group(1);
			 if("week".equals(format)){
				 Calendar cal = Calendar.getInstance();
				 int week = cal.get(Calendar.DAY_OF_WEEK);
				 path = path.replaceAll("\\$\\{"+format+"\\}", (week-1)+"");
			 }else{
				 SimpleDateFormat sdf = new SimpleDateFormat(format);
				 String date = sdf.format(new Date());
				 path = path.replaceAll("\\$\\{"+format+"\\}", date);			
			 }
		 }		 
		 return path;
	 }
	
	private String supplyContent(String title){
		return title.replaceAll("\\$\\{ip\\}", jobInfo.getIp());
	}

	@Override
	public void collect(CallBack call) {
		
		String httpUrl =parsePathTime(supplyContent(this.jobInfo.getFilepath()));//这个在http模式下，存放URL的地址   d:这句话貌似外层函数不会起作用
		if(httpUrl == null){
			return ;
		}
		logger.info("Get请求" + httpUrl);
		int i = httpUrl.indexOf("?");
		GetMethod getMethod = null;
		
		boolean gzip = false;
		
		if(i>0){
			String postUrl = httpUrl.substring(0, i);
			String paramUrl = httpUrl.substring(i+1, httpUrl.length());
			String[] params = StringUtils.split(paramUrl,"&");
			StringBuffer newparam = new StringBuffer();
			if(params.length>0){
				for(String p:params){
					if(StringUtils.isNotBlank(p)){//"?task_id=321126&encode=gzip";
						String[] tmp = StringUtils.split(p,"=");
						if(tmp.length==2){
							if("task_id".equals(tmp[0])){
								newparam.append("task_id=321126&");
							}else if("encode".equals(tmp[0])){
								newparam.append("encode=gzip&");
								gzip = true;
							}else{
								newparam.append(p).append("&");
							}
						}else{
							newparam.append(p).append("&");
						}
					}
				}
				newparam.setLength(newparam.length()-1);
			}
			getMethod= new GetMethod(postUrl+"?"+newparam.toString());
		}else{
			getMethod = new GetMethod(httpUrl);
		}
		getMethod.getParams().setParameter(HttpMethodParams.SO_TIMEOUT, 30000);
		char separator = this.jobInfo.getLinebreaks();
		try {
		      try {
		         int statusCode = HttpclientManager.executeMethod(getMethod);
		         if(statusCode == HttpStatus.SC_OK){
		        	 if(getMethod.getResponseContentLength()>0){
				        	 InputStream in =  getMethod.getResponseBodyAsStream();
				        	 if(in != null){
				        		 BufferedReader2 br = null;
				        		 if(gzip){
				        			 br = new BufferedReader2(new InputStreamReader(new GZIPInputStream(in),"gbk"),separator);
				        		 }else{
				        			 br = new BufferedReader2(new InputStreamReader(in,"gbk"),separator);
				        		 }
				 				String line = null;
				 				while((line = br.readLine()) != null){
				 					call.readerLine(line);
				 				}
				        	 }
				         }
		        	 }
		      } catch (Exception e) {
		    	  logger.info("请求:"+httpUrl,e);
		      }
		}  catch (Exception e) {
			logger.error("http 读取"+httpUrl+" 出错", e);
		} finally{
			getMethod.releaseConnection();
		}
	}

	@Override
	public void release() {
		
	}

	@Override
	public String getName() {
		return "http";
	}
	
	    

}
