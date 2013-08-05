package com.taobao.csp.alarm;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryHistoryUtil;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.service.impl.CommonServiceImpl;
import com.taobao.csp.time.util.SortMapHelp;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.CspKeyMode;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

public class AlarmKeyAutoAlarm {
	public static AlarmKeyAutoAlarm single = new AlarmKeyAutoAlarm();
	private static Logger logger = Logger.getLogger(AlarmKeyAutoAlarm.class);
	private AlarmKeyAutoAlarm(){
	}
	public static AlarmKeyAutoAlarm get(){
		return single;
	}
	public void generateAppAlarm(String appName,String keyName,String propertyName) throws Exception{
		int deep =0;
			Map<String, Map<String, DataEntry>> map2 = QueryUtil.queryRecentlyChildRealTime(appName, keyName);
			if(map2==null || map2.size()==0)return;
			Integer etimes = 0;
			for(Map.Entry<String, Map<String,DataEntry>> entry : map2.entrySet()){
				try {
					etimes+=(Integer)entry.getValue().get(PropConstants.E_TIMES).getValue();
				} catch (Exception e) {
					logger.info(e);
					return;
				}
			}
			generateAppAlarmIterater(appName,keyName,propertyName,etimes,0.1,++deep);
			return;
	}
	
	public void generateAppAlarmIterater(String appName,String keyName,String propertyName,Integer total,Double proportion,int deep) throws Exception{
		try {
			if(deep>2)return;
			if(keyName.matches("Tair.*")&&deep>1)return;
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, keyName);
			if(map==null||map.size()==0)return;
			for(Map.Entry<String, Map<String,Map<String,Object>>> entry : map.entrySet()){
				if(entry.getValue()==null||entry.getValue().size()==0)continue;
				
				String childKey = entry.getKey().substring(entry.getKey().indexOf(Constants.S_SEPERATOR)+1);
				Integer etimes = (Integer)sortMapByValue2(entry.getValue(),PropConstants.E_TIMES).get(0).getValue().get(PropConstants.E_TIMES);
				if(etimes*1.0/total>proportion*deep){
					CspKeyMode po = new CspKeyMode();
					po.setAppName(appName);
					po.setKeyName(childKey);
					po.setPropertyName(propertyName);
					if(propertyName.equals(PropConstants.E_TIMES)){
						String appModeConfig = "baseline^"+50+"#"+150+"$"+"00:10#23:59;";
						po.setAppModeConfig(appModeConfig);
					}
					if(propertyName.equals(PropConstants.E_TIMES)&&keyName.matches("Exception.*")){
						String appModeConfig = "baseline^"+-1+"#"+150+"$"+"00:10#23:59;";
						po.setAppModeConfig(appModeConfig);
					}
					if(propertyName.equals(PropConstants.C_TIME)){
						String appModeConfig = "baseline^"+-1+"#"+200+"$"+"00:10#23:59;";
						po.setAppModeConfig(appModeConfig);
					}
					List<String> ips =CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
					Integer min=0;
					Integer max=0;
					Pair pair = new Pair();
					pair = getMaxAndMin(appName,childKey,propertyName,ips);
					max = pair.max;
					min = pair.min;
					if(propertyName.equals(PropConstants.E_TIMES)){
						String hostModeConfig = "Threshold^"+min/2+"#"+max*2+"$"+"00:10#23:59;";
						po.setHostModeConfig(hostModeConfig);
					}
					if(propertyName.equals(PropConstants.E_TIMES)&&keyName.matches("Exception.*")){
						String hostModeConfig = "Threshold^"+-1+"#"+max*2+"$"+"00:10#23:59;";
						po.setHostModeConfig(hostModeConfig);
					}
					if(propertyName.equals(PropConstants.C_TIME)){
						if(max==0)max=1;
						String hostModeConfig = "Threshold^"+-1+"#"+max*5+"$"+"00:10#23:59";
						if(max<10)hostModeConfig = "Threshold^"+-1+"#"+max*10+"$"+"00:10#23:59;";
						po.setHostModeConfig(hostModeConfig);
					}
					po.setKeyScope("ALL");
					KeyAo.get().addKeyMode(po);
					generateAppAlarmIterater(appName,childKey,propertyName,total,proportion,++deep);
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
	}
	public Pair getMaxAndMin(String appName,String keyName,String propertyName,List<String> ips){
		Integer max;
		Integer min;
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -7);
		Date past = calendar.getTime();
		max =0;
		min = Integer.MAX_VALUE;
		String maxIp="";
		String minIp="";
		try {
			Map<String, Map<String, DataEntry>> map = QueryUtil.queryRecentlyHostRealTime(appName, keyName, ips);
			Iterator<Map.Entry<String, Map<String,DataEntry>>> iter = map.entrySet().iterator();
			while(iter.hasNext()){
				Map.Entry<String, Map<String,DataEntry>> entry = iter.next();
				if(entry.getValue()==null){
					iter.remove();
				}
				if(entry.getValue().get(propertyName)==null){
					iter.remove();
				}
			}
			if(map.size()==0){
				Pair pair = new Pair();
				pair.max = max;
				pair.min = min;
				return pair;
			}
			List<Entry<String, Map<String, DataEntry>>> list = SortMapHelp.sortNestedMapBySecondFloorKeyValue(map, propertyName);
			max = (Integer)list.get(0).getValue().get(propertyName).getValue();
			min = (Integer)list.get(list.size()-1).getValue().get(propertyName).getValue();
			maxIp = list.get(0).getKey().substring(list.get(0).getKey().lastIndexOf(Constants.S_SEPERATOR)+1);
			minIp = list.get(list.size()-1).getKey().substring(list.get(list.size()-1).getKey().lastIndexOf(Constants.S_SEPERATOR)+1);
		} catch (Exception e1) {
			logger.info(e1);
		}
			
			Map<Date, String> historyMap = QueryHistoryUtil.querySingleHost(appName, keyName, maxIp, propertyName, past);
			if(historyMap!=null&&historyMap.size()!=0){
				List<Entry<Date, String>> list = sortMapByValue(historyMap);
				for(Integer i=list.size()-1;i>=0;i--){
					if(list.get(i)==null)continue;
					try {
						int ma = Integer.parseInt(list.get(i).getValue());
						if(ma>max)max = ma;
					} catch (NumberFormatException e) {
						logger.info(e);
					}
					break;
				}
			}
			historyMap = QueryHistoryUtil.querySingleHost(appName, keyName, minIp, propertyName, past);
			if(historyMap!=null&&historyMap.size()!=0){
				List<Entry<Date, String>> list = sortMapByValue(historyMap);
				for(Integer i=0;i<list.size();i++){
					if(list.get(i)==null)continue;
					try {
						int mi = Integer.parseInt(list.get(i).getValue());
						if(mi<min&&mi>0)min = mi;
					} catch (NumberFormatException e) {
						logger.info(e);
					}
					break;
				}
			}
			if(min==Integer.MAX_VALUE)min=5;
		Pair pair = new Pair();
		pair.max = max;
		pair.min = min;
		return pair;
	}
	public static void generate(){
		try {
			String[] apps = {"login","shopsystem","itemcenter","tf_tm","tradeplatform","detail","hesper","tf_buy","shopcenter","cart","uicfinal","ump"};
			String[] keys = {KeyConstants.HSF_CONSUMER,KeyConstants.HSF_PROVIDER,KeyConstants.HSF_REFER,KeyConstants.TAIR_CONSUMER,KeyConstants.PV,KeyConstants.PV_BLOCK,KeyConstants.PV_REFER,KeyConstants.TAIR_CONSUMER,KeyConstants.SEARCH_CONSUMER};
			String[] properties = {PropConstants.E_TIMES,PropConstants.C_TIME}; 
			for(String app: apps){
				for(String key : keys){
					for(String property : properties){
						try {
							AlarmKeyAutoAlarm.get().generateAppAlarm(app, key, property);
						} catch (Exception e) {
							logger.info(e);
						}
					}
				}
			}
			for(String app: apps){
				try {
					AlarmKeyAutoAlarm.get().generateAppAlarm2(app, KeyConstants.NOTIFY_CONSUMER, PropConstants.NOTIFY_C_S,PropConstants.NOTIFY_C_S);
					AlarmKeyAutoAlarm.get().generateAppAlarm2(app, KeyConstants.NOTIFY_CONSUMER, PropConstants.NOTIFY_C_S_RT,PropConstants.NOTIFY_C_S);
					AlarmKeyAutoAlarm.get().generateAppAlarm(app, KeyConstants.EXCEPTION, PropConstants.E_TIMES);
				} catch (Exception e) {
					logger.info(e);
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
	}
	public static void main(String args[]) throws IOException{
//		AlarmKeyAutoAlarm.generate();
		AlarmKeyAutoAlarm.mbean();
	}
	public static void mbean() throws IOException{
		CommonServiceInterface commonService = new CommonServiceImpl();
		File file = new File("/home/zunyan.zb/test/MBean.sql");
		if(!file.exists()){
			file.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(file);
		String[] apps = {"login","shopsystem","itemcenter","tf_tm","tradeplatform","detail","hesper","tf_buy","shopcenter","cart","uicfinal","ump"};
		for(String app: apps){
			List<String> threadList = commonService.childKeyList( app,KeyConstants.MBEAN + Constants.S_SEPERATOR + KeyConstants.THREAD);
			for(String childkey : threadList){
				String fullkey = childkey;
				String sql = "insert into csp_key_alarm_mode(app_name,key_name,property_name,host_mode_config,key_scope) values('"+app+"','"+fullkey+"','BLOCKED',"+"'Threshold^0#20$00:10#23:59;'"+",'HOST');";
				fileWriter.write(sql+"\n");
			}
			List<String> dataSourceList = commonService.childKeyList( app,KeyConstants.MBEAN + Constants.S_SEPERATOR + KeyConstants.DATASOURCE);
			for(String childkey : dataSourceList){
				String fullkey = childkey;
				String sql = "insert into csp_key_alarm_mode(app_name,key_name,property_name,host_mode_config,key_scope) values('"+app+"','"+fullkey+"','InUse',"+"'Threshold^0#20$00:10#23:59;'"+",'HOST');";
				fileWriter.write(sql+"\n");
			}
			List<String> threadPoolList = commonService.childKeyList( app,KeyConstants.MBEAN + Constants.S_SEPERATOR + KeyConstants.THREADPOOL);
			for(String childkey : threadPoolList){
				String fullkey = childkey;
				String sql = "insert into csp_key_alarm_mode(app_name,key_name,property_name,host_mode_config,key_scope) values('"+app+"','"+fullkey+"','current',"+"'Threshold^0#100$00:10#23:59;'"+",'HOST');";
				fileWriter.write(sql+"\n");
			}
			fileWriter.flush();
		}
		fileWriter.close();
	}
	public List<Map.Entry<String,Map<String,Object>>> sortMapByValue2(Map<String,Map<String,Object>> map,final String name){
		Iterator<Map.Entry<String, Map<String,Object>>> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String, Map<String,Object>> entry = iter.next();
			if(entry.getValue()==null){iter.remove();continue;}
			if(entry.getValue().get(name)==null)iter.remove();
		}
		List<Map.Entry<String,Map<String,Object>>> list = new ArrayList<Map.Entry<String,Map<String,Object>>>(map.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String,Map<String,Object>>>(){

			@Override
			public int compare(Entry<String, Map<String, Object>> o1,
					Entry<String, Map<String, Object>> o2) {
				Object v1 = o1.getValue().get(name);
				Object v2 = o2.getValue().get(name);
				if(v1 instanceof Integer && v2 instanceof Integer){
					return (Integer)v2 - (Integer)v1;
				}
				return 0;
			}

			
		});
		return list;
	}
	public  List<Map.Entry<Date, String>> sortMapByValue(Map<Date,String> map) {
		if(map == null)return null;
		Iterator<Map.Entry<Date, String>> iter = map.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<Date, String> entry = iter.next();
			if(entry.getValue()==null)iter.remove();
		}
		List<Map.Entry<Date, String>> info = new ArrayList<Map.Entry<Date, String>>(map.entrySet());
		Collections.sort(info, new Comparator<Map.Entry<Date, String>>() {
			public int compare(Map.Entry<Date, String> obj1, Map.Entry<Date, String> obj2) {
				String value1 = obj1.getValue();
				String value2 = obj2.getValue();
				try {
					Integer v1 = Integer.parseInt(value1);
					Integer v2 = Integer .parseInt(value2);
					return v1 - v2;
				} catch (NumberFormatException e) {
					logger.info(e);
				}
				return value1.compareTo(value2); 
			}
		});
		return info;
	}
	
	private class Pair{
		public Integer max;
		public Integer min;
	}
	public void generateAppAlarm2(String appName,String keyName,String propertyName,String mainProperty) throws Exception{
		int deep =0;
			Map<String, Map<String, DataEntry>> map2 = QueryUtil.queryRecentlyChildRealTime(appName, keyName);
			if(map2==null || map2.size()==0)return;
			Integer etimes = 0;
			for(Map.Entry<String, Map<String,DataEntry>> entry : map2.entrySet()){
				try {
					etimes+=(Integer)entry.getValue().get(mainProperty).getValue();
				} catch (Exception e) {
					logger.info(e);
					return;
				}
			}
			generateAppAlarmIterater2(appName,keyName,propertyName,etimes,0.1,++deep,mainProperty);
			return;
	}
	
	public void generateAppAlarmIterater2(String appName,String keyName,String propertyName,Integer total,Double proportion,int deep,String mainProperty) throws Exception{
		try {
			if(deep>2)return;
			if(keyName.matches("Tair.*")&&deep>1)return;
			Map<String, Map<String, Map<String, Object>>> map = QueryUtil.queryChildRealTime(appName, keyName);
			if(map==null||map.size()==0)return;
			for(Map.Entry<String, Map<String,Map<String,Object>>> entry : map.entrySet()){
				if(entry.getValue()==null||entry.getValue().size()==0)continue;
				
				String childKey = entry.getKey().substring(entry.getKey().indexOf(Constants.S_SEPERATOR)+1);
				Integer etimes = (Integer)sortMapByValue2(entry.getValue(),mainProperty).get(0).getValue().get(mainProperty);
				if(etimes*1.0/total>proportion*deep){
					CspKeyMode po = new CspKeyMode();
					po.setAppName(appName);
					po.setKeyName(childKey);
					po.setPropertyName(propertyName);
					if(propertyName.equals(PropConstants.E_TIMES)){
						String appModeConfig = "baseline^"+50+"#"+150+"$"+"00:10#23:59;";
						po.setAppModeConfig(appModeConfig);
					}
					if(propertyName.equals(PropConstants.C_TIME)){
						String appModeConfig = "baseline^"+-1+"#"+200+"$"+"00:10#23:59;";
						po.setAppModeConfig(appModeConfig);
					}
					if(propertyName.equals(PropConstants.NOTIFY_C_S)){
						String appModeConfig = "baseline^"+50+"#"+150+"$"+"00:10#23:59;";
						po.setAppModeConfig(appModeConfig);
					}
					if(propertyName.equals(PropConstants.NOTIFY_C_S_RT)){
						String appModeConfig = "baseline^"+-1+"#"+200+"$"+"00:10#23:59;";
						po.setAppModeConfig(appModeConfig);
					}
					List<String> ips =CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
					Integer min=0;
					Integer max=0;
					Pair pair = new Pair();
					pair = getMaxAndMin(appName,childKey,propertyName,ips);
					max = pair.max;
					min = pair.min;
					if(propertyName.equals(PropConstants.E_TIMES)){
						String hostModeConfig = "Threshold^"+min/2+"#"+max*2+"$"+"00:10#23:59;";
						po.setHostModeConfig(hostModeConfig);
					}
					if(propertyName.equals(PropConstants.C_TIME)){
						if(max==0)max=1;
						String hostModeConfig = "Threshold^"+-1+"#"+max*5+"$"+"00:10#23:59";
						if(max<10)hostModeConfig = "Threshold^"+-1+"#"+max*10+"$"+"00:10#23:59;";
						po.setHostModeConfig(hostModeConfig);
					}
					if(propertyName.equals(PropConstants.NOTIFY_C_S)){
						String hostModeConfig = "Threshold^"+min/2+"#"+max*2+"$"+"00:10#23:59;";
						po.setHostModeConfig(hostModeConfig);
					}
					if(propertyName.equals(PropConstants.NOTIFY_C_S_RT)){
						if(max==0)max=1;
						String hostModeConfig = "Threshold^"+-1+"#"+max*5+"$"+"00:10#23:59";
						if(max<10)hostModeConfig = "Threshold^"+-1+"#"+max*10+"$"+"00:10#23:59;";
						po.setHostModeConfig(hostModeConfig);
					}
					po.setKeyScope("ALL");
					KeyAo.get().addKeyMode(po);
					generateAppAlarmIterater2(appName,childKey,propertyName,total,proportion,++deep,mainProperty);
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
	}
}
