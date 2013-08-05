package com.taobao.csp.monitor.impl.analyse.apache;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * null 6 200 [14/Dec/2011:23:59:59 +0800] "GET http://login.taobao.com/member/login.jhtml" 14832 "http://item.taobao.com/item.htm?id=13001102664"
 * @author xiaodu
 * @version 2010-4-28 下午01:00:45
 */
public class JbossLogAnalyse extends ApacheLogJob {
	
	private static final Logger logger = Logger.getLogger(JbossLogAnalyse.class);
	
	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
	
	public JbossLogAnalyse( String appName,String ip,String feature) {
		super( appName,ip, feature);
	}

	

	public void analyseOneLine(String line) {
		if (line.indexOf("status.taobao") > 0) {
			return;
		}
		try{
			String[] tmp =  StringUtils.splitPreserveAllTokens(line, "\"");
			
			String[] p1 =StringUtils.splitByWholeSeparator( tmp[0], " ");
			
			String ip = p1[0];
			
			int rest = Integer.parseInt(p1[1]+"000");//外部jboss的响应是就毫秒 统一为微妙
			String time = p1[p1.length-3];
			String source_url =  tmp[1].substring(4);
			String httpCode =  p1[2];
			String pagesize = "";
			String refer_url = null;
			try{
				 pagesize = tmp[2].trim();
				 refer_url = tmp[3];
			}catch (Exception e) {
				try{
					String[] p2 =StringUtils.splitByWholeSeparator( tmp[3], " ");
					pagesize = p2[1];
					refer_url = tmp[4];
				 }catch (Exception e1) {
				}
			}
			
			Date collectTime = rTimeFormat.parse(time.substring(1));
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(collectTime);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			
			long cTime = cal.getTimeInMillis();
			analyseSource(cTime,rest,pagesize,httpCode,source_url,true);
			analyseIp(cTime,ip);
			analyseRefer(cTime,refer_url);
		}catch (Exception e) {
			logger.error("分析"+line, e);
		}

	}
	public static void main(String[] args){
		
		JbossLogAnalyse a = new JbossLogAnalyse("","","");
		a.analyseOneLine("null 6 200 [14/Dec/2011:23:59:59 +0800] \"GET http://login.taobao.com/member/login.jhtml\" 14832 \"http://item.taobao.com/item.htm?id=13001102664\"");
	}

}
