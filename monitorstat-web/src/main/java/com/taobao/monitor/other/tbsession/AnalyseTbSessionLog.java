
package com.taobao.monitor.other.tbsession;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.RemoteCommonUtil;
import com.taobao.monitor.common.util.RemoteCommonUtil.CallBack;
import com.taobao.monitor.other.ao.OtherModuleAo;

/**
 * 这个是用来分析淘宝session日志的
 * @author xiaodu
 * @version 2011-5-12 上午11:34:47
 */
public class AnalyseTbSessionLog {
	
	private String tbsessionPath = null;
	
	private String targetIp = null;
	
	private static Pattern pattern = Pattern.compile("\\|(\\w*);(\\d*)\\|");
	
	
	Map<String,TbSeesionLog> mapTbSeesion = new HashMap<String, TbSeesionLog>();
	
	public TbSeesionLog allSeesionLogMsg = new TbSeesionLog();
	
	
	
	public AnalyseTbSessionLog(String path,String targetIp){
		this.tbsessionPath = path;
		this.targetIp =  targetIp;
	}
	
	
	
	public Map<String,TbSeesionLog>  getResult(){
		return mapTbSeesion;
	}
	
	
	
	//1305171655330 cookie f [/json/promotion.htm|_tb_token_;23|uc1;190|lzstat_ss;40|__utmc;15|cna;27|t;33|ck1;3|tg;3|_cc_;20|nt;62|tracknick;49|ssllogin;8|x;92|publishItemObj;22|lzstat_uv;37|__utma;61|__utmz;91|_lang;14|__utmb;31|cookie2;39|v;2|mt;8|_sv_;5|unb;11|_nk_;44|_l_g_;13|_wwmsg_;12|lastgetwwmsg;32|cookie1;55|cookie17;22|tlut;24|
	public void doAnalyse(){
		String tmpPath = "/tmp/session.log";
		try {
//			RemoteCommonUtil.excute(targetIp, "cat "+this.tbsessionPath,new CallBack(){
//				public void doLine(String line) {
//					
//					String[] r = line.split(" ");
//					if(r.length>3){
//						String type = r[1];
//						String flag = r[2];
//						if("cookie".equals(type)&&"f".equals(flag)){
//							analyseCookieOneLine(line);
//						}
//						
//					}
//					
//					
//					
//				}});
			
			RemoteCommonUtil.getFile(targetIp, this.tbsessionPath, tmpPath);
			
			BufferedReader reader = new BufferedReader(new FileReader(tmpPath));
			String line = null;
			
			while((line=reader.readLine())!=null){
				String[] r = line.split(" ");
				if(r.length>3){
					String type = r[1];
					String flag = r[2];
					if("cookie".equals(type)&&"f".equals(flag)){
						analyseCookieOneLine(line);
					}
					
				}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		for(Map.Entry<String,TbSeesionLog> entry:mapTbSeesion.entrySet()){
			TbSeesionLog log = entry.getValue();
			log.setLogType(0);
			OtherModuleAo.get().addTbsessionLog(log);
		}
		
		allSeesionLogMsg.setLogType(1);
		OtherModuleAo.get().addTbsessionLog(allSeesionLogMsg);
		
	}
	
	
	private void analyseCookieOneLine(String str){
		
		allSeesionLogMsg.valueCount++;
		
		Matcher matcher = pattern.matcher(str);
		
		int cookieLen = 0;
		
		Map<String ,Integer> map = new HashMap<String, Integer>();
		
		while(matcher.find()){
			String key = matcher.group(1);
			int value = Integer.valueOf(matcher.group(2));
			map.put(key, value);
			cookieLen+=value;
		}
		
		for(Map.Entry<String ,Integer> entry:map.entrySet()){
			String key = entry.getKey();
			int value = entry.getValue();
			
			TbSeesionLog log = mapTbSeesion.get(key);
			if(log == null){
				log = new TbSeesionLog();
				log.keyName = key;
				mapTbSeesion.put(key, log);
			}
			
			log.valueCount++;
			log.valueSum+=value;
			
			double p = Arith.div(value, cookieLen,3);
			log.perSum = Arith.add(log.perSum, p);
			if(log.maxValueLen <value){
				log.maxValueLen = value;
			}
			
			if(log.minValueLen >value){
				log.minValueLen = value;
			}
			
			if(log.maxPer >p){
				log.maxPer = p;
			}
			
			cookieLen+=value;
		}
		
		
		allSeesionLogMsg.valueSum+=cookieLen;
		
		
		
		if(allSeesionLogMsg.maxValueLen <cookieLen){
			allSeesionLogMsg.maxValueLen = cookieLen;
			
			
		}
		
		if(allSeesionLogMsg.minValueLen >cookieLen){
			allSeesionLogMsg.minValueLen = cookieLen;
		}
	}
	
	
	
	public static void main(String[] args){
		AnalyseTbSessionLog log = new AnalyseTbSessionLog("/home/xiaoxie/session.log","10.232.15.173");
		log.doAnalyse();
	}
	

}
