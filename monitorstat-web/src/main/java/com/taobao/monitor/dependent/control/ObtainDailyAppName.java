//
//package com.taobao.monitor.dependent.control;
//
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.StringReader;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import java.util.regex.Pattern;
//
//import org.apache.commons.lang.StringUtils;
//
//import com.taobao.monitor.dependent.job.NetstatInfo;
//
///**
// * 
// * @author xiaodu
// * @version 2011-5-5 下午01:57:16
// */
//public class ObtainDailyAppName {
//	
//	
//	
//	public static List<String> getAppDependentIp(String targetIp,String targetAppname){
//		
//		
//		List<String> appIpList = new ArrayList<String>();
//		
//		Map<String,Set<NetstatInfo>> map = PortAppDependentCollect.getAppNetstat(targetIp);
//		Set<NetstatInfo> set = map.get("meDepend");
//		
//		for(NetstatInfo info:set){
//			String appname = getAppName(info.getForeignIp());
//			if(appname != null){
//				if(targetAppname.equals(appname)){
//					appIpList.add(info.getForeignIp());
//				}
//			}
//		}
//		return appIpList;
//	}
//	
//	/**
//	 * ip:10.232.12.130;hostname:v012130.sqa.cm4;appName:alimall;apacheVersion:;jbossVersion:;hsfVersion:;jdkVersion:;ownerName:红巾;useInfo:alimall日常测试环境;mainType:日常;groupSign:标准 
//	 * @param ip
//	 * @return
//	 */
//	public static String getAppName(String ip){
//		String path = "http://scm.taobao.net/getInfoByIp.htm?ip="+ip;
//		try{
//			URL url = new URL(path);
//			URLConnection urlCon = url.openConnection();
//			urlCon.setDoInput(true);
//			urlCon.setConnectTimeout(1000000);
//			urlCon.connect();
//			BufferedInputStream input = new BufferedInputStream(urlCon.getInputStream());
//			BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//			StringBuffer sb = new StringBuffer();
//			String str = null;
//			while((str=reader.readLine())!=null){
//				sb.append(str);
//			}
//			
//			String[] k = sb.toString().split(";");
//			if(k.length >3){
//				String app = k[2];
////				String[] name = app.split(":");
//				if(name.length ==2){
//					return name[1];
//				}
//			}
//		}catch (Exception e) {
//			// TODO: handle exception
//		}
//		
//		return null;
//		
//	}
//	
//
//}
