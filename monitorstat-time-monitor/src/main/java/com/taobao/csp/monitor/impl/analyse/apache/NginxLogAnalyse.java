package com.taobao.csp.monitor.impl.analyse.apache;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 这个是给nginx的
 * @author xiaodu
 * @version 2010-4-28 下午01:00:45
 */
public class NginxLogAnalyse extends ApacheLogJob {
	
	private static final Logger logger = Logger.getLogger(ApacheLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm", Locale.ENGLISH);
	
	/**
	 * @param appName
	 * @param ip
	 */
	public NginxLogAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

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
		
		
		float _n_rt = Float.parseFloat(p1[1]);//外部nginx的响应是就是秒 统一为微妙
		int rest = (int)(_n_rt *1000000);
		String time = p1[p1.length-3];
		String source_url =  tmp[1].substring(4);
		String httpCode =  "500";
		String pagesize = "";
		String refer_url = null;
		try{
			 String[] p2 =StringUtils.splitByWholeSeparator( tmp[2], " ");
			 httpCode =  p2[0];
			 pagesize = p2[1];
			 refer_url = tmp[3];
		}catch (Exception e) {
			try{
				String[] p2 =StringUtils.splitByWholeSeparator( tmp[3], " ");
				httpCode =  p2[0];
				pagesize = p2[1];
				refer_url = tmp[4];
			 }catch (Exception e1) {
			}
		}
		
		Date collectTime = rTimeFormat.parse(time.substring(1,18));
		long cTime = collectTime.getTime();
		
		boolean hit = true;
		if(referIp.indexOf("MISS")>0){
			hit = false;
		}
		
		analyseSource(cTime,rest,pagesize,httpCode,source_url,hit);
		analyseIp(cTime,ip);
		analyseRefer(cTime,refer_url);
	}catch (Exception e) {
		logger.error(getIp()+"--分析--"+line, e);
	}

	}
	
public static void main(String[] args){
		
	NginxLogAnalyse job = new NginxLogAnalyse("detail","172.17.134.4","");
		
			String line = null;
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\work\\csp\\monitorstat-time-monitor\\target\\log")));
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
