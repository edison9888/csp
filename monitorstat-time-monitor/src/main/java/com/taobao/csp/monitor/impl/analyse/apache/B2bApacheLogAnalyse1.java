package com.taobao.csp.monitor.impl.analyse.apache;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 这个是给Tnginx的
 * @author xiaodu
 * @version 2010-4-28 下午01:00:45
 */
public class B2bApacheLogAnalyse1 extends ApacheLogJob {
	
	private static final Logger logger = Logger.getLogger(ApacheLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm", Locale.ENGLISH);
	
	/**
	 * @param appName
	 * @param ip
	 */
	public B2bApacheLogAnalyse1(String appName, String ip,String feature) {
		super(appName, ip,feature);
	}
	//172.22.6.170 - - - [07/Jun/2012:14:42:37 +0800] "GET /-/ok.htm HTTP/1.0" 200 3 5357 "-" "check_http/v1.4.14 (nagios-plugins 1.4.14)" - - "a=-; b=-; c=-" -
	public void analyseOneLine(String line) {
		try{
		
		if (line.indexOf("status.taobao") > 0) {
			return;
		}
		
		String[] tmp =  StringUtils.splitPreserveAllTokens(line, "\"");
		
		String[] p1 =StringUtils.splitByWholeSeparator( tmp[0], " ");
		
		String ip = p1[0];
		
		String referIp = tmp[tmp.length-1].trim();
		if(!referIp.equals("-")){
			String[] t = referIp.split("\\.");
			if(t.length == 4){
				ip = referIp.replaceAll("[^0-9\\.]*", "");
			}
		}
		
		String[] c = StringUtils.split(tmp[2], " ");
		
		int rest =0;
		try{
			 rest = Integer.parseInt(c[2]);
		}catch (Exception e) {
			return ;
		}
		
		String[] url = StringUtils.split(tmp[1], " ");
		
		String time = p1[p1.length-3];
		String source_url =  url[1];
		String httpCode =  "500";
		String pagesize = "";
		String refer_url = null;
		try{
			 httpCode =  c[0];
			 pagesize = c[1];
			 refer_url = tmp[3];
		}catch (Exception e) {
			return ;
		}
		
		Date collectTime = rTimeFormat.parse(time.substring(1,18));
		long cTime = collectTime.getTime();
		
		boolean hit = true;
		
		analyseSource(cTime,rest,pagesize,httpCode,source_url,hit);
		analyseIp(cTime,ip);
		analyseRefer(cTime,refer_url);
		
		
		
	}catch (Exception e) {
		logger.error(getAppName()+"分析"+line, e);
	}

	}
	
public static void main(String[] args){
		
	B2bApacheLogAnalyse1 job = new B2bApacheLogAnalyse1("detail","172.17.134.4","");
	job.analyseOneLine("220.248.27.162 - - - [03/Aug/2012:16:43:08 +0800] \"POST /offer.china.alibaba.com/offer/post/fill_product_info.htm HTTP/1.1\" 302 20 99561 \"http://offer.china.alibaba.com/offer/post/fill_product_info.htm?offerId=1176973403&operator=edit&asker=ATC\" \"Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 5.1; Trident/4.0; SV1; .NET CLR 2.0.50727; 360SE)\" 220.248.27.162.1342169668299.7 - \"a=\"c_ms=2|c_mt=3|c_mid=yinyangst|c_lid=yinyangst\"; b=\"c_w_signed=Y\"; c=c_oid=1176973403\" -");
	
			String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\work\\access.log")));
			int i = 0;
			while((line=reader.readLine())!=null){
			//	System.out.println(line);
				job.analyseOneLine(line);
				i++;
				if(i>10000){
					job.submit();
					job.release();
					Thread.sleep(60000);
					i=0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}  catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	

}
