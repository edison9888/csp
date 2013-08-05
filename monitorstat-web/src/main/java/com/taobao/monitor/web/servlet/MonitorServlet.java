
package com.taobao.monitor.web.servlet;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.ao.MonitorJprofAo;
import com.taobao.monitor.web.core.po.JprofClassMethodStack;
import com.taobao.monitor.web.vo.AlarmDataPo;

/**
 * 
 * @author xiaodu
 * @version 2010-4-19 上午10:51:42
 */
public class MonitorServlet extends HttpServlet {
	
	private static final Logger logger =  Logger.getLogger(MonitorServlet.class);
	
	static Map<String,String> keyFilter = new HashMap<String, String>();
	static Map<String,Map<String,Inner3>> alarmKeyMap = new HashMap<String, Map<String,Inner3>>();
	
	private static Lock lock = new ReentrantLock();
	
	private static Lock cacheLock = new ReentrantLock();
	
	private static Map<String,Map<String,SimpleKey>> cacheAppKeyValueMap = new HashMap<String, Map<String,SimpleKey>>();
	
	private static long cacheTime = System.currentTimeMillis()-70000;
	
	private static Map<String,Map<String,SimpleKey>> findMonitorAll(){			
		try{
			cacheLock.lock();
			if(System.currentTimeMillis()-cacheTime>10000){
				try{
					queryMonitorAll();
				 }catch (Exception e) {
					 logger.error("", e);
				}
			}
			return cacheAppKeyValueMap;
		}finally{
			cacheLock.unlock();
		}
	}
	
	private static void queryMonitorAll(){	
//		Map<String,Map<String,Map<String,KeyValuePo>>> appMap = MonitorTimeAo.get().findLikeKeyByLimit();		
//		for(Map.Entry<String,Map<String,Map<String,KeyValuePo>>> appEntry:appMap.entrySet()){
//			Map<String,Map<String,KeyValuePo>> keyVoMap = appEntry.getValue();
//			for(Map.Entry<String,Map<String,KeyValuePo>> keyEntry:keyVoMap.entrySet()){
//				Map<String,KeyValuePo> timemap = keyEntry.getValue();
//				List<KeyValuePo> listpo = new ArrayList<KeyValuePo>();
//				listpo.addAll(timemap.values());
//				Collections.sort(listpo);	
//				
//				if(listpo.size()==0){
//					continue;
//				}
//				if(keyEntry.getKey() == null){
//					continue;
//				}
//				if(keyEntry.getKey().equals("PV_VISIT_COUNTTIMES")){
//					
//					KeyValuePo maxpo =  listpo.get(0);
//					KeyValuePo secpo = null;
//					if(listpo.size()>1){
//						secpo = listpo.get(1);
//					}
//					if(secpo!=null){
//						if(maxpo.getMaxSiteValue()>secpo.getMaxSiteValue()){
//							addMonitor(maxpo.getAppName(),maxpo.getKeyName(),maxpo.getKeyId(),maxpo.getCollectTime(),maxpo.getMaxSiteValue());
//						}else{
//							addMonitor(secpo.getAppName(),secpo.getKeyName(),secpo.getKeyId(),secpo.getCollectTime(),secpo.getMaxSiteValue());
//						}
//					}
//					
//				}else{
//					KeyValuePo maxpo = listpo.get(0);
//					addMonitor(maxpo.getAppName(),maxpo.getKeyName(),maxpo.getKeyId(),maxpo.getCollectTime(),maxpo.getMaxSiteValue());
//				}
//				
//			}
//		}
//		cacheTime = System.currentTimeMillis();
	}
	
	private static void addMonitor(String appName,String keyName,int keyId,Date time,double value){

		Map<String,SimpleKey> keyMap = cacheAppKeyValueMap.get(appName);
		if(keyMap==null){
			keyMap = new HashMap<String, SimpleKey>();
			cacheAppKeyValueMap.put(appName, keyMap);
		}
		SimpleKey key = keyMap.get(keyName);
		if(key==null){
			key = new SimpleKey();
			key.keyId = keyId;
			key.keyName = keyName;
			keyMap.put(keyName, key);
		}
		key.value = value;
		key.time = time;
	}
	
	
//	public static void putAlarmKey(AlarmDrive alarmDrive){
//		try{
//			lock.lock();
//			AlarmDataPo po = alarmDrive.getCurrentAlarmData();
//			Map<String,Inner3> map = alarmKeyMap.get(po.getAppName());
//			if(map==null){
//				map = new HashMap<String, Inner3>();
//				alarmKeyMap.put(po.getAppName(), map);
//			}
//			Inner3 inner = new Inner3();
//			inner.setDate(new Date());
//			inner.setAppName(po.getAppName());
//			inner.setMaxValue(po.getMaxValue()+"");
//			inner.setKeyName(po.getKeyName());
//			inner.setAlarmMessage(alarmDrive.getAlarmMessage());
//			map.put(po.getKeyName(), inner);
//			addMonitor(po.getAppName(),po.getKeyName(),Integer.parseInt(po.getKeyId()),po.getMaxTime(),po.getMaxValue());
//			
//		}finally{
//			lock.unlock();
//		}		
//		findMonitorAll();
//	}
	

	static{
		keyFilter.put("PV_VISIT_COUNTTIMES","PV_VISIT_COUNTTIMES");
		keyFilter.put("PV_REST_AVERAGEUSERTIMES","PV_REST_AVERAGEUSERTIMES");
		keyFilter.put("CPU","CPU");
		keyFilter.put("LOAD","LOAD");
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.item.service.ItemQueryService:1.0.0-L0_queryItemById_COUNTTIMES","PV_VISIT_COUNTTIMES");
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.item.service.ItemQueryService:1.0.0-L0_queryItemById_AVERAGEUSERTIMES","PV_REST_AVERAGEUSERTIMES");
		
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.shopservice.core.client.ShopReadService:1.0.0_queryShop_COUNTTIMES","PV_VISIT_COUNTTIMES");
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.shopservice.core.client.ShopReadService:1.0.0_queryShop_AVERAGEUSERTIMES","PV_REST_AVERAGEUSERTIMES");
		
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.uic.common.service.userinfo.UserReadService:1.0.0_getUserAndUserExtraById_COUNTTIMES","PV_VISIT_COUNTTIMES");
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.uic.common.service.userinfo.UserReadService:1.0.0_getUserAndUserExtraById_AVERAGEUSERTIMES","PV_REST_AVERAGEUSERTIMES");
		
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.tc.service.TcBaseService:1.0.0_queryMainAndDetail_COUNTTIMES","PV_VISIT_COUNTTIMES");
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.tc.service.TcBaseService:1.0.0_queryMainAndDetail_AVERAGEUSERTIMES","PV_REST_AVERAGEUSERTIMES");
		
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.tc.service.TcBaseService:1.0.0_queryMainAndDetail_COUNTTIMES","PV_VISIT_COUNTTIMES");
		keyFilter.put("IN_HSF-ProviderDetail_com.taobao.tc.service.TcBaseService:1.0.0_queryMainAndDetail_AVERAGEUSERTIMES","PV_REST_AVERAGEUSERTIMES");
	
	}
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5701274033196280294L;
	

	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doPost(req, resp);
	}
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String action = req.getParameter("action");
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		if("pv".equals(action)){//取得全部信息 一分钟一次	
			String appName = req.getParameter("appName");
			Map<String,SimpleKey> mapkey = cacheAppKeyValueMap.get(appName);
			if(mapkey!=null){
				SimpleKey pvKey = mapkey.get("PV_VISIT_COUNTTIMES");
				SimpleKey restKey = mapkey.get("PV_REST_AVERAGEUSERTIMES");
				Map<String,String> responseMap = new HashMap<String, String>();
				responseMap.put("collectTime", sdf.format(pvKey.time));
				responseMap.put("PV_VISIT_COUNTTIMES", pvKey.value+"");
				responseMap.put("PV_REST_AVERAGEUSERTIMES", Arith.div(restKey.value, 1000,2)+"");
				responseMap.put("qps", Arith.div(pvKey.value, 60,2)+"");
				JSONObject json = JSONObject.fromObject(responseMap);
				resp.setContentType("text/html;charset=utf-8"); 
				try {
					resp.getWriter().write(json.toString());
					resp.flushBuffer();
				} catch (IOException e) {
					
				}
			}			
			return ;			
		}
		
		
		if("all".equals(action)){			
			Map<String,Map<String,SimpleKey>> appMap = findMonitorAll();
			
			Map<String, Map<String,Inner>> responseMap = new HashMap<String, Map<String,Inner>>();
			
			for(Map.Entry<String,Map<String,SimpleKey>> appEntry:appMap.entrySet()){
				
				String name = appEntry.getKey();
				Map<String,SimpleKey> keyVoMap = appEntry.getValue();
				for(Map.Entry<String,SimpleKey> keyEntry:keyVoMap.entrySet()){
					String keyname = keyEntry.getKey();	
					SimpleKey simpleKey = keyEntry.getValue();
					if(keyFilter.get(keyname)==null){
						Map<String, Inner3> map = alarmKeyMap.get(name);
						if(map == null || map.get(keyname)==null){
							continue;
						}
					}					
					Map<String,Inner> appResponseMap = responseMap.get(name);
					if(appResponseMap == null){
						appResponseMap = new HashMap<String, Inner>();
						responseMap.put(name, appResponseMap);
					}
					
					Inner inner = new Inner();
					
//					if("PV_VISIT_COUNTTIMES".equals(keyname)){
//					System.out.println(keyname);
//					}else{						
//						
//					}
					
					if("PV_REST_AVERAGEUSERTIMES".equals(keyname)){
						inner.setValue(Arith.div(simpleKey.value, 1000,2)+"");
					}else{						
						inner.setValue(simpleKey.value+"");
					}
					inner.setKeyId(simpleKey.keyId+"");					
					checkKey(name,keyname,inner);
					appResponseMap.put(keyFilter.get(keyname)==null?keyname:keyFilter.get(keyname),inner);
				}
				
			}
			
			JSONObject json = JSONObject.fromObject(responseMap);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;	
			
		}
		
		
		
		if("toC".equals(action)){
			String toC = req.getParameter("toC");	
			String appName = req.getParameter("appName");
			Map<String,Inner2> innerMap = new HashMap<String, Inner2>();
			Map<String,SimpleKey>  keyMap = cacheAppKeyValueMap.get(appName);
			if(keyMap!=null){				
				for(Map.Entry<String, SimpleKey> entry:keyMap.entrySet()){
					String keyName = entry.getKey();
					if(keyName.indexOf("OUT_HSF-Consumer_com.taobao."+toC)>-1){
						String key = parseHsfName(keyName);
						Inner2 inner2 = innerMap.get(key);
						if(inner2==null){
							inner2 = new Inner2();
							innerMap.put(key, inner2);
						}
						if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>0){
							inner2.value1 = entry.getValue().value+"";
							inner2.name1 = (keyName);
							inner2.setKeyId1(entry.getValue().keyId+"");
						}
						if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>0){
							inner2.value2 = entry.getValue().value+"";
							inner2.name2 = (keyName);
							inner2.setKeyId2(entry.getValue().keyId+"");
						}
					}
				}
			}						
			JSONObject json = JSONObject.fromObject(innerMap);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;
		}
		
		if("other".equals(action)){
			String appName = req.getParameter("appName");
			Map<String, String> mapRespone = new HashMap<String, String>();
			Map<String,Inner3> map = alarmKeyMap.get(appName);
			if(map!=null){				
				for(Map.Entry<String, Inner3> entry:map.entrySet()){
					String keyName = entry.getKey();
					Inner3 inner3 = entry.getValue();
					if(System.currentTimeMillis()-inner3.getDate().getTime()<60000){
						mapRespone.put(keyName, inner3.getMaxValue()+"");						
					}
				}
			}

			JSONObject json = JSONObject.fromObject(mapRespone);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				
			}	
			return ;			
		}
		
		if("search".equals(action)){
			String appName = req.getParameter("appName");
			Map<String,SimpleKey>  keyMap = cacheAppKeyValueMap.get(appName);
			Map<String,Inner2> innerMap = new HashMap<String, Inner2>();
			if(keyMap!=null){				
				for(Map.Entry<String, SimpleKey> entry:keyMap.entrySet()){
					String keyName = entry.getKey();
					SimpleKey simpleKey = entry.getValue();
					if(keyName.indexOf("OUT_SearchEngine")>-1){
						String key = parseSearchName(keyName);
						Inner2 inner2 = innerMap.get(key);
						if(inner2==null){
							inner2 = new Inner2();
							innerMap.put(key, inner2);
						}
						if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>0){
							inner2.value1 = simpleKey.value+"";
							inner2.name1 = simpleKey.keyName;
							inner2.setKeyId1(simpleKey.keyId+"");
						}
						if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>0){
							inner2.value2 = simpleKey.value+"";
							inner2.name2 = simpleKey.keyName;
							inner2.setKeyId2(simpleKey.keyId+"");
						}
					}					
				}
				
			}						
			JSONObject json = JSONObject.fromObject(innerMap);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;
		}		
		if("Provider".equals(action)){
			String appName = req.getParameter("appName");
			Map<String,Inner2> innerMap = new HashMap<String, Inner2>();
			Map<String,SimpleKey>  keyMap = cacheAppKeyValueMap.get(appName);
			if(keyMap!=null){				
				for(Map.Entry<String, SimpleKey> entry:keyMap.entrySet()){
					String keyName = entry.getKey();
					if(keyName.indexOf("IN_HSF-ProviderDetail_")>-1){
						String key = parseHsfName(keyName);
						SimpleKey simpleKey = entry.getValue();
						Inner2 inner2 = innerMap.get(key);
						if(inner2==null){
							inner2 = new Inner2();
							innerMap.put(key, inner2);
						}
						if(keyName.indexOf(Constants.COUNT_TIMES_FLAG)>0){
							inner2.value1 =  simpleKey.value+"";
							inner2.name1 = simpleKey.keyName;
							inner2.setKeyId1(simpleKey.keyId+"");
						}
						if(keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG)>0){
							inner2.value2 =  simpleKey.value+"";
							inner2.name2 = simpleKey.keyName;
							inner2.setKeyId2(simpleKey.keyId+"");
						}
					}					
				}
			}
			JSONObject json = JSONObject.fromObject(innerMap);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;
			
		}
		
		
		if("getCurrentAlarm".equals(action)){
			SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
			List<Inner3> list = new ArrayList<Inner3>();
			for(Map.Entry<String,Map<String,Inner3>> entry:alarmKeyMap.entrySet()){
				list.addAll(entry.getValue().values());				
			}
			Collections.sort(list);
			List<SimpleAlarm> alarmList = new ArrayList<SimpleAlarm>();
			for(int i=0;i<list.size()&&i<10;i++){
				Inner3 inner = list.get(i);
				SimpleAlarm simpleAlarm = new SimpleAlarm();
				simpleAlarm.setAlarmMsg(inner.getAlarmMessage());
				simpleAlarm.setAppName(inner.getAppName());
				simpleAlarm.setTime(sdf1.format(inner.getDate()));
				alarmList.add(simpleAlarm);
			}
			
			
			JSONArray json = JSONArray.fromObject(alarmList);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;
		}
		
		if("pvNum".equals(action)){//PV_VISIT_COUNTTIMES
			
			Map<String,Double> appMap = new HashMap<String, Double>();
			for(Map.Entry<String,Map<String,SimpleKey>> entry:cacheAppKeyValueMap.entrySet()){
				String appName = entry.getKey();
				Map<String,SimpleKey> map = entry.getValue();
				for(Map.Entry<String,SimpleKey> keyEntry:map.entrySet()){
					String key = keyEntry.getKey();					
					if("PV_VISIT_COUNTTIMES".equals(key)){
						appMap.put("inter_"+appName, keyEntry.getValue().value);
					}					
					if(key.indexOf("OUT_HSF-Consumer_com.taobao.")>-1&&key.indexOf(Constants.COUNT_TIMES_FLAG)>0){
						String[] ks = key.split("\\.");	
						String toApp = ks[2];
						if(toApp.equals("item")){
							String tokey = appName+"_ic";
							Double d = appMap.get(tokey);
							if(d==null){
								appMap.put(tokey, keyEntry.getValue().value);
							}else{
								appMap.put(tokey, d+keyEntry.getValue().value);
							}							
						}else if(toApp.equals("shopservice")){
							String tokey = appName+"_shopcenter";
							Double d = appMap.get(tokey);
							if(d==null){
								appMap.put(tokey, keyEntry.getValue().value);
							}else{
								appMap.put(tokey, d+keyEntry.getValue().value);
							}
						}else if(toApp.equals("uic")){
							String tokey = appName+"_tbuic";
							Double d = appMap.get(tokey);
							if(d==null){
								appMap.put(tokey, keyEntry.getValue().value);
							}else{
								appMap.put(tokey, d+keyEntry.getValue().value);
							}
						}else if(toApp.equals("tc")){
							String tokey = appName+"_tc";
							Double d = appMap.get(tokey);
							if(d==null){
								appMap.put(tokey, keyEntry.getValue().value);
							}else{
								appMap.put(tokey, d+keyEntry.getValue().value);
							}
						}
						
					}
					if(key.indexOf("OUT_SearchEngine")>-1&&key.indexOf(Constants.COUNT_TIMES_FLAG)>0){
						String tokey = appName+"_searchengine";
						Double d = appMap.get(tokey);
						if(d==null){
							appMap.put(tokey, keyEntry.getValue().value);
						}else{
							appMap.put(tokey, d+keyEntry.getValue().value);
						}
					}
					
					
				}
			}
			JSONObject json = JSONObject.fromObject(appMap);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;
		}
		
		
		if("payway".equals(action)){	
			String appName = req.getParameter("appName");
			Map<String,Double> result = new HashMap<String, Double>();
			Map<String,SimpleKey> map = cacheAppKeyValueMap.get(appName);
			for(Map.Entry<String, SimpleKey> entry:map.entrySet()){
				if(entry.getKey().indexOf("NAGIOSMAIDIAN_")>-1&&entry.getKey().toLowerCase().indexOf("payway")>-1){
					result.put(entry.getKey(), entry.getValue().value);
				}
			}			
			JSONObject json = JSONObject.fromObject(result);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;
		}
		
		
		if("jprof_stack_root".equals(action)){	
			String appName = req.getParameter("appName");
			String className = req.getParameter("className");
			String methodName = req.getParameter("methodName");
			String collectDay = req.getParameter("collectDay");
			
			Map<String,String> map = MonitorJprofAo.get().findJprofClassMethodStackRootParent(appName, className, methodName, collectDay);
			
			
			JSONObject json = JSONObject.fromObject(map);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;
		}
		
		
		if("jprof_stack_msg".equals(action)){	
			String md5 = req.getParameter("md5");
			String appName = req.getParameter("appName");
			String collectDay = req.getParameter("collectDay");
			List<JprofClassMethodStack> list = MonitorJprofAo.get().findJprofClassMethodStack(appName, md5,collectDay);
			
			
			JSONArray json = JSONArray.fromObject(list);
			resp.setContentType("text/html;charset=utf-8"); 
			try {
				resp.getWriter().write(json.toString());
				resp.flushBuffer();
			} catch (IOException e) {
				 logger.error("", e);
			}	
			return ;
		}
		
		
	}
	/**
	 * OUT_SearchEngine_http://auction-search.config-vip.taobao.com:2088/bin/search?_auction_COUNTTIMES
	 * @param name
	 * @return
	 */
	private String parseSearchName(String name){
		String[] searchs = name.split("_");
		if(searchs.length==5){
			return searchs[2]+"_"+ searchs[3];
			
		}else{
			return name;
		}
		
	}
	
	
	/**
	 * 
	 * @param name
	 * @return
	 */
	private String parseHsfName(String name){
		
		String[] tmp = name.split("_");
		
		if(tmp.length==5){
			String[] v = tmp[2].split(":");
			if(v.length==2){
				return tmp[3]+":"+v[1];
			}
			return tmp[3];
		}		
		return "";
	}
	
	
	
	
	
	private void checkKey(String appName,String keyname,Inner inner){
		try{
			lock.lock();			
			Map<String, Inner3> map= alarmKeyMap.get(appName);
			if(map!=null){
				Inner3 inner3 = map.get(keyname);
				if(inner3!=null){				
					if(System.currentTimeMillis()-inner3.getDate().getTime()<60000){
						inner.setAlarmFlag(1);
					}else{
						inner.setAlarmFlag(0);
					}				
					
				}
			}
		}finally{
			lock.unlock();
		}
	}
	
	
	
	
	
	
	public static class Inner3 implements Comparable<Inner3>{
		private Date date;
		private String appName;
		private String keyName;
		private String maxValue;
		private String alarmMessage;
		
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}
		public String getKeyName() {
			return keyName;
		}
		public void setKeyName(String keyName) {
			this.keyName = keyName;
		}
		public String getMaxValue() {
			return maxValue;
		}
		public void setMaxValue(String maxValue) {
			this.maxValue = maxValue;
		}
		public String getAlarmMessage() {
			return alarmMessage;
		}
		public void setAlarmMessage(String alarmMessage) {
			this.alarmMessage = alarmMessage;
		}
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		
		
		
		public int compareTo(Inner3 o) {			
			if(date.before(o.getDate())){
				return 1;
			}else if(date.equals(o.getDate())){
				return 0;
			}else if(date.after(o.getDate())){
				return -1;
			}
			return 0;
		}
		
		
		
	}
	
	
	public class Inner2{
		
		public String value1;
		public String name1;
		public String keyId1;
		
		public String value2;
		public String name2;
		public String keyId2;
		public String getValue1() {
			return value1;
		}
		public void setValue1(String value1) {
			this.value1 = value1;
		}
		public String getValue2() {
			return value2;
		}
		public void setValue2(String value2) {
			this.value2 = value2;
		}
		public String getName1() {
			return name1;
		}
		public void setName1(String name1) {
			this.name1 = name1;
		}
		public String getName2() {
			return name2;
		}
		public void setName2(String name2) {
			this.name2 = name2;
		}
		public String getKeyId1() {
			return keyId1;
		}
		public void setKeyId1(String keyId1) {
			this.keyId1 = keyId1;
		}
		public String getKeyId2() {
			return keyId2;
		}
		public void setKeyId2(String keyId2) {
			this.keyId2 = keyId2;
		}
		
		
		
	}
	
	
	public static class SimpleKey{
		private Date time;
		private String keyName;
		private int keyId;
		private double value;
	}
	
	public class SimpleAlarm{
		private String time;
		private String appName;
		private String alarmMsg;
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getAppName() {
			return appName;
		}
		public void setAppName(String appName) {
			this.appName = appName;
		}
		public String getAlarmMsg() {
			return alarmMsg;
		}
		public void setAlarmMsg(String alarmMsg) {
			this.alarmMsg = alarmMsg;
		}
		
		
		
	}
	
	public class Inner{
		public String keyId;
		public String value;
		public int alarmFlag;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public int getAlarmFlag() {
			return alarmFlag;
		}
		public void setAlarmFlag(int alarmFlag) {
			this.alarmFlag = alarmFlag;
		}
		public String getKeyId() {
			return keyId;
		}
		public void setKeyId(String keyId) {
			this.keyId = keyId;
		}
		
		
		
		
	}
	
	

}
