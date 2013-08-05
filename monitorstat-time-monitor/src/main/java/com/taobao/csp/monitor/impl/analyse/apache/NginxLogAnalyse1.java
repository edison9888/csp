package com.taobao.csp.monitor.impl.analyse.apache;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * 这个是给Tnginx的
 * @author xiaodu
 * @version 2010-4-28 下午01:00:45
 */
public class NginxLogAnalyse1 extends ApacheLogJob {
	
	private static final Logger logger = Logger.getLogger(ApacheLogJob.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm", Locale.ENGLISH);
	
	/**
	 * @param appName
	 * @param ip
	 */
	public NginxLogAnalyse1(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}
	
	private Map<Long,int[]> hitmap = new HashMap<Long,int[]>();

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
		
		
		
		
		
		int rest =0;//淘宝nginx的响应是就是微妙
		try{
			 rest = Integer.parseInt(p1[1]);
		}catch (Exception e) {
			float _n_rt = Float.parseFloat(p1[1]);
			rest = (int)(_n_rt *1000000);
		}
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
		logger.error(getAppName()+"分析"+line, e);
	}

	}
	
public static void main(String[] args){
	
	NginxLogAnalyse1 job = new NginxLogAnalyse1("detail","172.17.134.4","");
	job.analyseOneLine("10.246.29.243 1000 - [22/May/2013:14:12:18 +0800] \"POST http://rebate-front-pub-hz.vip.tbsite.net/rebate.htm?t=29667d6d03cd567a47e4f45599944122&partner=1&src=setao-json-s008085.cm6\" 200 999 \"-\" \"-\"");
	
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
