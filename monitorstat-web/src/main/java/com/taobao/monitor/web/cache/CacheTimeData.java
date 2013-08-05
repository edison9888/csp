
package com.taobao.monitor.web.cache;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.web.ao.MonitorTimeAo;


/**
 * �����Ҫ�ǻ����� ʵʱ��ʾ�ϵ� �ϸ����ڵĶԱ����� ��Ҫ�� load pv rest gc
 * @author xiaodu
 * @version 2010-8-12 ����09:14:26
 */
public class CacheTimeData {
	
	private static final Logger logger = Logger.getLogger(CacheTimeData.class);
	private static CacheTimeData cache = new CacheTimeData();
	
	
	private CacheTimeData(){}
	
	public static CacheTimeData get(){
		return cache;
	}
	
	
	/**
	 * key :app  keyName time
	 * 
	 * ����ͬ�ڵ�һ�������
	 */
	private Map<String,Map<String,Map<String,KeyValuePo>>> cacheKeyMap = new HashMap<String, Map<String,Map<String,KeyValuePo>>>();
	private Map<String,Map<String,Map<String,KeyValuePo>>> cacheKeyBaseMap = new HashMap<String, Map<String,Map<String,KeyValuePo>>>();
	
	private List<AppInfoPo> needCacheApp = new ArrayList<AppInfoPo>();
	
	private Date startDate;
	
	
	private void initAppData(){
		needCacheApp.clear();
		List<AppInfoPo> appList = AppInfoAo.get().findAllTimeApp();
		needCacheApp.addAll(appList);
	}
	
	private void initCollectDate(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -7);
		startDate = cal.getTime();
	}
	
	
	private void clear(){
		cacheKeyMap.clear();
		cacheKeyBaseMap.clear();
	}
	
	
	
	
	private void cacheMonitorTimePvData(){				
		int pvId = 175;
		int rtId = 176;		
		logger.info("��ӡ���е�APP*******");
		for(AppInfoPo p :needCacheApp){	
			Map<String,Map<String,KeyValuePo>> keyMap = cacheKeyMap.get(p.getAppName());
			if(keyMap == null){
				keyMap = new HashMap<String, Map<String,KeyValuePo>>();
				cacheKeyMap.put(p.getAppName(), keyMap);
			}
			//�Ա�7����ϴ���
			Map<String, KeyValuePo> pvMap =MonitorTimeAo.get().findKeyValueByDate(p.getAppId(),pvId,  startDate);			
			Map<String, KeyValuePo> rtMap = MonitorTimeAo.get().findKeyValueByDate( p.getAppId(),rtId, startDate);
			keyMap.put("PV_VISIT_COUNTTIMES", pvMap);
			keyMap.put("PV_REST_AVERAGEUSERTIMES", rtMap);
			
			
			//���ߵ����ܵ�������ʷƽ��ֵ			
			Map<String,Map<String,KeyValuePo>> keyMapBase = cacheKeyBaseMap.get(p.getAppName());
			if(keyMapBase == null){
				keyMapBase = new HashMap<String, Map<String,KeyValuePo>>();
				cacheKeyBaseMap.put(p.getAppName(), keyMapBase);
			}			
			Map<String, KeyValuePo> pvMapBase = MiddleLayerForBaseLineAndMonitorTimeAo.findKeyValueByDate(p.getAppId(),pvId);			
			Map<String, KeyValuePo> rtMapBase = MiddleLayerForBaseLineAndMonitorTimeAo.findKeyValueByDate( p.getAppId(),rtId);
			keyMapBase.put("PV_VISIT_COUNTTIMES", pvMapBase);
			keyMapBase.put("PV_REST_AVERAGEUSERTIMES", rtMapBase);
			
			logger.info("appName=" + p.getAppName());
		}		
	}
	
	private void cacheMonitorTimeLoadData(){				
		int loadId = 944;
		for(AppInfoPo p :needCacheApp){	
			Map<String,Map<String,KeyValuePo>> keyMap = cacheKeyMap.get(p.getAppName());
			if(keyMap == null){
				keyMap = new HashMap<String, Map<String,KeyValuePo>>();
				cacheKeyMap.put(p.getAppName(), keyMap);
			} 
			Map<String, KeyValuePo> pvMap =MonitorTimeAo.get().findKeyValueByDate(p.getAppId(),loadId,  startDate);
			keyMap.put("System_LOAD_AVERAGEUSERTIMES", pvMap);
			
			
			Map<String,Map<String,KeyValuePo>> keyMapBase = cacheKeyBaseMap.get(p.getAppName());
			if(keyMapBase == null){
				keyMapBase = new HashMap<String, Map<String,KeyValuePo>>();
				cacheKeyBaseMap.put(p.getAppName(), keyMapBase);
			} 			
			Map<String, KeyValuePo> pvMapBase =MiddleLayerForBaseLineAndMonitorTimeAo.findKeyValueByDate(p.getAppId(),loadId);			
			keyMapBase.put("System_LOAD_AVERAGEUSERTIMES", pvMapBase);		
		}		
	}
	
	private void cacheMonitorTimeGCData(){				
		int fullgcId = 167;
		int gcId = 3196;
		for(AppInfoPo p :needCacheApp){	
			Map<String,Map<String,KeyValuePo>> keyMap = cacheKeyMap.get(p.getAppName());
			if(keyMap == null){
				keyMap = new HashMap<String, Map<String,KeyValuePo>>();
				cacheKeyMap.put(p.getAppName(), keyMap);
			}
			
			Map<String, KeyValuePo> fullGcMap =MonitorTimeAo.get().findKeyValueByDate(p.getAppId(),fullgcId,  startDate);
			Map<String, KeyValuePo> gcMap =MonitorTimeAo.get().findKeyValueByDate(p.getAppId(),gcId,  startDate);
			keyMap.put("SELF_GC_Full_AVERAGEMACHINEFLAG", fullGcMap);
			keyMap.put("SELF_GC_GC_AVERAGEMACHINEFLAG", gcMap);

			//����ͬʱ������
			Map<String,Map<String,KeyValuePo>> keyMapBase = cacheKeyBaseMap.get(p.getAppName());
			if(keyMapBase == null){
				keyMapBase = new HashMap<String, Map<String,KeyValuePo>>();
				cacheKeyBaseMap.put(p.getAppName(), keyMapBase);
			} 			
			Map<String, KeyValuePo> fullGcMapBase = MiddleLayerForBaseLineAndMonitorTimeAo.findKeyValueByDate(p.getAppId(),fullgcId);			
			keyMapBase.put("SELF_GC_Full_AVERAGEMACHINEFLAG", fullGcMapBase);	
			Map<String, KeyValuePo> gcMapBase = MiddleLayerForBaseLineAndMonitorTimeAo.findKeyValueByDate(p.getAppId(),gcId);			
			keyMapBase.put("SELF_GC_GC_AVERAGEMACHINEFLAG", gcMapBase);		
		}		
	}
	
	
		
	
	public void cacheData(){
		logger.info("ִ��CacheTimeData��ʼʱ�䣺" + new Date().toString());
		clear();
		initAppData();
		initCollectDate();
		
		logger.info("cacheMonitorTimePvData Start��" + new Date().toString());
		cacheMonitorTimePvData();
		logger.info("cacheMonitorTimePvData Start��" + new Date().toString());
		
		cacheMonitorTimeLoadData();
		cacheMonitorTimeGCData();
		logger.info("ִ��CacheTimeData����ʱ�䣺" + new Date().toString());
	}
	
	
	public List<KeyValuePo> getAppKeyData(String appName,String keyName,List<Date> dateList){
		
		List<KeyValuePo> list = new ArrayList<KeyValuePo>();
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("HH:mm");
		Map<String,Map<String,KeyValuePo>> keyMap = cacheKeyMap.get(appName);
		if(keyMap!=null){			
			Map<String,KeyValuePo> timeMap = keyMap.get(keyName);
			if(timeMap!=null){
				for(Date date :dateList){					
					KeyValuePo po = timeMap.get(parseLogFormatDate.format(date));
					if(po!=null){
						list.add(po);
					}
				}
			}
		}		
		return null;		
	}
	
	public Map<String,KeyValuePo> getAppKeyData(String appName,String keyName){
		Map<String,Map<String,KeyValuePo>> keyMap = cacheKeyMap.get(appName);
		if(keyMap!=null){			
			Map<String,KeyValuePo> timeMap = keyMap.get(keyName);
			return timeMap;			
		}		
		return null;		
	}
	public long getAppKeyCount(String appName,String keyName,Date startTime,Date endTime){
		Map<String,Map<String,KeyValuePo>> keyMap = cacheKeyMap.get(appName);
		if(keyMap == null){
			return 0;
		}
		Map<String,KeyValuePo> timeMap = keyMap.get(keyName);
		if(timeMap == null){
			return 0;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		
		Calendar start = Calendar.getInstance();
		start.setTime(startTime);
		
		Calendar end = Calendar.getInstance();
		end.setTime(endTime);
		List<String> timeList = new ArrayList<String>();
		
		do{
			String time = sdf.format(start.getTime());
			timeList.add(time);			
			start.add(Calendar.MINUTE, 1);
		}while(start.getTimeInMillis()<end.getTimeInMillis());
		
		final Map<Integer,Double> map = new HashMap<Integer, Double>();
		
		for(String t:timeList){			
			KeyValuePo po = timeMap.get(t);
			if(po!=null){
				Map<Integer, Double> dMap = po.getSiteValueMap();
				for(Map.Entry<Integer, Double> entry:dMap.entrySet()){
					Double d = map.get(entry.getKey());
					if(d==null){
						map.put(entry.getKey(),entry.getValue());
					}else{
						map.put(entry.getKey(),entry.getValue()+d);
					}
				}
				
			}
		}
		
		int siteNum = map.size();
		long sum = 0;
		for(Double s:map.values()){
			sum+=s;
		}
		
		if(siteNum>0){
			return sum/siteNum;
		}
		return 0;
	}
	
	
	
	public KeyValuePo getAppKeyData(String appName,String keyName,String time){
		
		Map<String,Map<String,KeyValuePo>> keyMap = cacheKeyMap.get(appName);
		if(keyMap!=null){			
			Map<String,KeyValuePo> timeMap = keyMap.get(keyName);
			if(timeMap!=null){
				KeyValuePo po = timeMap.get(time);
				return po;
			}
		}
		return null;		
	}
	
	/**
	 * ʵʱ���ҳ����ã���ȡ������ʷ����
	 * @param appName
	 * @param keyName
	 * @param time
	 * @return
	 */
	public KeyValuePo getAppKeyDataBase(String appName,String keyName,String time){
		
		Map<String,Map<String,KeyValuePo>> keyMap = cacheKeyBaseMap.get(appName);
		if(keyMap!=null){			
			Map<String,KeyValuePo> timeMap = keyMap.get(keyName);
			if(timeMap!=null){
				KeyValuePo po = timeMap.get(time);
				return po;
			}
		}
		return null;		
	}

	//add by zhongting ���������벻�ɹ��������������
	public Map<String, Map<String, Map<String, KeyValuePo>>> getCacheKeyMap() {
		return cacheKeyMap;
	}

	public Map<String, Map<String, Map<String, KeyValuePo>>> getCacheKeyBaseMap() {
		return cacheKeyBaseMap;
	}

	public List<AppInfoPo> getNeedCacheApp() {
		return needCacheApp;
	}
	
}
