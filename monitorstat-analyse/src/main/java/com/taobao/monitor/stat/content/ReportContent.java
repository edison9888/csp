
package com.taobao.monitor.stat.content;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.stat.db.impl.AnalyseLogDao;
import com.taobao.monitor.stat.db.po.MonitorDetail;
import com.taobao.monitor.stat.db.po.MonitorKey;
import com.taobao.monitor.stat.util.DepIpInfo;
import com.taobao.monitor.stat.util.DepIpInfoContain;

/**
 * 
 * @author xiaodu
 * @version 2010-4-2 下午06:44:23
 */
public class ReportContent implements ReportContentInterface{
	
	private static  Logger log = Logger.getLogger(ReportContent.class);
	
	private AnalyseLogDao dao = new AnalyseLogDao();	
	//private static ReportContent content = new ReportContent();
	
	
	private Map<String,AppInfoPo> appCacheMap = new HashMap<String, AppInfoPo>();//
	private Map<String,MonitorKey> keyCacheMap =new HashMap<String, MonitorKey>();
	
	/*+--------+----------+------------+---------+----------+
	| app_id | app_name | sort_index | feature | app_type |
	+--------+----------+------------+---------+----------+
	|     10 | tbdb1    |          0 | NULL    | db       |
	|     11 | tbdb2    |          0 | NULL    | db       |
	|     12 | heart    |          0 | NULL    | db       |
	|     13 | comm     |          0 | NULL    | db       |
	|     14 | bmw      |          0 | NULL    | db       |
	|     15 | misc     |          0 | NULL    | db       |
	+--------+----------+------------+---------+----------+*/
	{
		AppInfoPo tbdb1 = new AppInfoPo();
		tbdb1.setAppDayId(10);
		tbdb1.setOpsName("tbdb1");		
		appCacheMap.put("tbdb1", tbdb1);
		
		
		AppInfoPo tbdb2 = new AppInfoPo();
		tbdb2.setAppDayId(11);
		tbdb2.setOpsName("tbdb2");
		
		appCacheMap.put("tbdb2", tbdb2);
		
		AppInfoPo heart = new AppInfoPo();
		heart.setAppDayId(12);
		heart.setOpsName("heart");		
		appCacheMap.put("heart", heart);
		
		AppInfoPo comm = new AppInfoPo();
		comm.setAppDayId(13);
		comm.setOpsName("comm");	
		appCacheMap.put("comm", comm);
		
		AppInfoPo bmw = new AppInfoPo();
		bmw.setAppDayId(14);
		bmw.setOpsName("bmw");	
		appCacheMap.put("bmw", bmw);
		
		AppInfoPo misc = new AppInfoPo();
		misc.setAppDayId(15);
		misc.setOpsName("misc");	
		appCacheMap.put("misc", misc);
		
	}
	
	public AppInfoPo getAppByName(String opsName){
		return appCacheMap.get(opsName);
	}
	
	
	
	public ReportContent(){
		//将所有应用放入内存中
		log.info("载入应用名称");
		List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
		for(AppInfoPo po:appList){
			appCacheMap.put(po.getOpsName(), po);
		}
		log.info("载入key名称");
		List<MonitorKey> keyList =dao.findAllMonitorKey();
		for(MonitorKey key:keyList){
			keyCacheMap.put(key.getKeyName(), key);
		}		
	}
	
//	public static ReportContent getInstance(){
//		return content;
//	}
	
	
	public void putReportData(String appName,Map<String,Long> data,String collectTime){
		AppInfoPo app = appCacheMap.get(appName);
		if(app==null){
			//log.warn("在库表中不存在应用:"+appName);
		}else{
			Iterator<Map.Entry<String, Long>> dataIt = data.entrySet().iterator();
			while(dataIt.hasNext()){
				Map.Entry<String, Long> entry = dataIt.next();				
				putReportData(appName,entry.getKey(),entry.getValue().toString(),collectTime);
			}
		}
		
	}
	
	public void putReportData(String appName,String keyValue,String valueData,String collectTime){
		
		AppInfoPo app = appCacheMap.get(appName);
		if(app==null){
			//log.warn("在库表中不存在应用:"+appName);
		}else{
			MonitorDetail detail = new MonitorDetail();
			String keyName = keyValue.replaceAll(" ", "");
			
			log.debug(appName+" "+keyName+"="+valueData+" "+collectTime);
			
			detail.setAppId(app.getAppDayId());
			
			MonitorKey key = keyCacheMap.get(keyName);
			if(key==null){
				key = createNewKey(keyName);
			}
			
			if(key!=null){
				keyCacheMap.put(keyName, key);					
				detail.setKeyId(key.getId());
				detail.setValueData(valueData);	
				detail.setCollectTime(collectTime);
				createNewMonitorData(detail);
			}
		}
		
	}
	public void putReportData(String appName,String keyValue,Long valueData,String collectTime){		
		putReportData( appName, keyValue,valueData.toString(),collectTime);		
	}
	
	public void putReportDataByCount(String appName,String key,String value,String collectDate){
		MonitorDetail detail = new MonitorDetail();		
		key = key.replaceAll(" ", "");
		AppInfoPo app = appCacheMap.get(appName);
		if(app==null){
			//log.warn("在库表中不存在应用:"+appName);
		}else{
			detail.setAppId(app.getAppDayId());
			MonitorKey monitorKey = keyCacheMap.get(key);
			if(monitorKey==null){
				monitorKey = createNewKey(key);
			}
			
			if(key!=null&&monitorKey!=null){				
				log.debug(appName+" "+key+" "+"="+value+" "+collectDate);
				
				keyCacheMap.put(key, monitorKey);					
				detail.setKeyId(monitorKey.getId());
				detail.setValueData(value);	
				detail.setCollectTime(collectDate);
				dao.addMonitorDataCount(detail);
			}
		}
	}
	
	public void putReportDataByCount(String appName,String key,Long value,String collectDate){
		putReportDataByCount(appName,key,value.toString(),collectDate);
	}
	public void putReportDataByCount(String appName,String key,Integer value,String collectDate){
		putReportDataByCount(appName,key,value.toString(),collectDate);
	}
	public void putReportDataByCount(String appName,Map<String,Long> data,String collectDate){
					
		AppInfoPo app = appCacheMap.get(appName);
		if(app==null){
			//log.warn("在库表中不存在应用:"+appName);
		}else{
			Iterator<Map.Entry<String, Long>> dataIt = data.entrySet().iterator();
			while(dataIt.hasNext()){
				Map.Entry<String, Long> entry = dataIt.next();
				putReportDataByCount(appName,entry.getKey(),entry.getValue(),collectDate);
			}
		}
	}
	public void putReportDataByCount1(String appName,Map<String,Float> data,String collectDate){
		
		AppInfoPo app = appCacheMap.get(appName);
		if(app==null){
			//log.warn("在库表中不存在应用:"+appName);
		}else{
			Iterator<Map.Entry<String, Float>> dataIt = data.entrySet().iterator();
			while(dataIt.hasNext()){
				Map.Entry<String, Float> entry = dataIt.next();					
				putReportDataByCount(appName,entry.getKey(),entry.getValue().toString(),collectDate);
			}
		}
	}
	
	
	
	private MonitorKey createNewKey(String key){
		return dao.addMonitorKey(key);
	} 
	
	
	private void createNewMonitorData(MonitorDetail detail){
		dao.addMonitorData(detail);
	}



	public void putReportDataByProvider(String appName, String keyValue, String cm_ip, Long value, Double time,
			String collectDate) {
		
		DepIpInfo dep = DepIpInfoContain.get().getByIp(cm_ip);
		if(dep != null){
			dao.addMonitorProvider(appName, dep.getAppName(), keyValue, cm_ip,dep.getSiteName(), value, time, collectDate);
		}else{
			dao.addMonitorProvider(appName, "empty", keyValue, cm_ip,"empty", value, time, collectDate);
		}
		
		
		
	}



	public void putReportDataByCustomer(String customerName, String providerName, String key, String machineName, Long value,
			Double time, String collectDate) {
		DepIpInfo dep = DepIpInfoContain.get().getByName(machineName);
		if(dep != null){
			dao.addMonitorCustomer(providerName, customerName, key, dep.getIp(), dep.getSiteName(), value, time, collectDate);
		}else{
			dao.addMonitorCustomer(providerName, customerName, key, "empty", "empty", value, time, collectDate);
		}
		
		
	}
	

}
