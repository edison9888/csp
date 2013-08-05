
/**
 * monitorstat-alarm
 */
package com.taobao.csp.alarm.check;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.alarm.check.mode.CheckMode;
import com.taobao.csp.alarm.check.mode.CheckModeFactory;
import com.taobao.csp.alarm.check.mode.DataMessage;
import com.taobao.csp.alarm.sender.AlarmSender;
import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.memcache.entry.DataEntry;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.csp.time.cache.KeyCache;
import com.taobao.csp.time.cache.ShieldIpCache;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.po.CspKeyMode;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 *
 * 下午9:15:45
 */
public class AlarmKeyProcessor {
	
	
	
	private static final Logger logger = Logger.getLogger(AlarmKeyProcessor.class);
	
	private String appName;
	
	private String keyName;
	
	private String keyAlias;
	
	private Map<String,CheckMode> appScopePropMap = new ConcurrentHashMap<String, CheckMode>();
	
	private Map<String,CheckMode> hostScopePropMap = new ConcurrentHashMap<String, CheckMode>();
	
	private long preAppAlarmTime=System.currentTimeMillis();
	private int appcontinuous = 0;
	
	private long preHostAlarmTime=System.currentTimeMillis();
	private int hostcontinuous = 0;
	
	public AlarmKeyProcessor(CspKeyMode mode){
		this.appName = mode.getAppName();
		this.keyName = mode.getKeyName();
		this.keyAlias = mode.getKeyAlias();
	}
	
	
	private Map<String,List<DataEntry>> fllValue(Map<String, Map<String, Object>> map){
		
		Map<String,List<DataEntry>> dataMap = new HashMap<String, List<DataEntry>>();
		for(Map.Entry<String, Map<String, Object>> entry:map.entrySet()){
			long time = Long.parseLong(entry.getKey());
			Map<String, Object> data = entry.getValue();
			for(Map.Entry<String, Object> e: data.entrySet()){
				String p = e.getKey();
				Object value = e.getValue();
				List<DataEntry> list = dataMap.get(p);
				if(list == null){
					list = new ArrayList<DataEntry>();
					dataMap.put(p, list);
				}
				
				DataEntry d = new DataEntry();
				d.setTimeAndValue(time, value);
				list.add(d);
			}
		}
		return dataMap;
	}
	
	public void doAppAlarmHandle(){
		
		if(appScopePropMap.size() >0){
			try {
				
				Map<String,AlarmReport> alarmMap = new HashMap<String, AlarmReport>();
				
				
				Map<String, Map<String, Object>> map = QueryUtil.querySingleRealTime(appName, keyName);
				
				if(map == null){
					return ;
				}
				
				Map<String,List<DataEntry>> dataMap = fllValue(map);
				for(Map.Entry<String,CheckMode> entry:appScopePropMap.entrySet()){
					String p = entry.getKey();
					CheckMode mode = entry.getValue();
					List<DataEntry> list = dataMap.get(p);
					
					sort(list);
					DataMessage dataMessage = new DataMessage();
					dataMessage.setAppName(appName);
					dataMessage.setKeyName(keyName);
					dataMessage.setDataList(list);
					dataMessage.setPropertyName(p);
					if(list != null){
						if(mode.checkData(dataMessage)){
							AlarmReport report = new AlarmReport();
							report.setAppName(appName);
							report.setKeyName(keyName);
							report.setContinuous(appcontinuous);
							report.setKeyScope("APP");
							report.setModeName(mode.getModeName());
							report.setCause(mode.getAlarmCause());
							report.setPropertyName(p);
							report.setKeyLevel(mode.getKeyLevel() + "");
							DataEntry ed = mode.getAlarmData();
							report.setTime(ed.getTime());
							report.setValue(ed.getValue());
							report.setKeyAlias(keyAlias);
							CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
							transformAlarmReportToPo(report,po);
							List<CspTimeKeyAlarmRecordPo> poList = new ArrayList<CspTimeKeyAlarmRecordPo>();
							poList.add(po);
							CspTimeKeyAlarmRecordAo.get().insert(poList);
							alarmMap.put(p, report);
							logger.info("应用级别:"+appName+":"+keyName+":"+p+mode.getAlarmData());
							
						}
					}
				}
				
				//发送
				if(alarmMap.size() >0){
					
					//在三分钟呢再次出现表示连续
					if(System.currentTimeMillis() - preAppAlarmTime>300000){
						preAppAlarmTime = System.currentTimeMillis();
						appcontinuous=1;
					}else{
						appcontinuous++;
					}
//					
					
					for(Map.Entry<String,AlarmReport> entry:alarmMap.entrySet()){
						entry.getValue().setContinuous(appcontinuous);
						Integer level = KeyCache.getCache().getLevel(entry.getValue().getKeyName());
						if(level<appcontinuous){
							AlarmSender.get().putAlarmInfo(entry.getValue());
						}
					}
						
						
				}
				
				
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		
	}
	private void transformAlarmReportToPo(AlarmReport alarmReport,CspTimeKeyAlarmRecordPo po){
		po.setAlarm_cause(alarmReport.getCause());
		po.setAlarm_time(new Timestamp(alarmReport.getTime()));
		po.setAlarm_value(alarmReport.getValue().toString());
		po.setApp_name(alarmReport.getAppName());
		po.setKey_name(alarmReport.getKeyName());
		po.setKey_scope(alarmReport.getKeyScope());
		po.setMode_name(alarmReport.getModeName());
		po.setProperty_name(alarmReport.getPropertyName());
		po.setIp(alarmReport.getIp());
	}
	
	private void sort(List<DataEntry> list){
		if(list == null)return;
		Collections.sort(list, new Comparator<DataEntry>() {
			@Override
			public int compare(DataEntry o1, DataEntry o2) {
				if (o1.getTime() < o2.getTime()) {
					return 1;
				} else if (o1.getTime() > o2.getTime()) {
					return -1;
				}
				return 0;
			}
		});
		
	}
	
	
	
	public void doHostAlarmHandle(){
		if(hostScopePropMap.size() >0){
			//获取机器
			List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(appName);
			if(ipList==null)return;
			try {
				//查询所有机器 最近值
				Map<String, Map<String, Map<String, Object>>> ipdataMap = QueryUtil.queryHostRealTime(appName, keyName, ipList);
				
				Map<String,Map<String,AlarmReport>> alarmIpmap = new HashMap<String, Map<String,AlarmReport>>();
				//遍历所有机器数据
				for(Map.Entry<String, Map<String, Map<String, Object>>> entry: ipdataMap.entrySet()){
					String ip =  entry.getKey();
					ip = ip.substring(ip.lastIndexOf(Constants.S_SEPERATOR)+1);
					
					if(ShieldIpCache.get().isShield(ip)){
						logger.info("屏蔽"+ip+"->"+appName+"->"+keyName);
						continue;
					}
					
					Map<String, Map<String, Object>> map = entry.getValue();
					
					if(map == null||map.size() == 0){
						continue ;
					}
					
					Map<String,List<DataEntry>> dataMap = fllValue(map);
					
					Map<String,AlarmReport> ipMap = alarmIpmap.get(ip);
					if(ipMap == null){
						ipMap = new HashMap<String, AlarmReport>();
						alarmIpmap.put(ip, ipMap);
					}
					
					for(Map.Entry<String,CheckMode> d :  hostScopePropMap.entrySet()){
						String p = d.getKey(); //属性名称
						CheckMode m = d.getValue();
						
						List<DataEntry> v = dataMap.get(p);
						sort(v);
						DataMessage dataMessage = new DataMessage();
						dataMessage.setAppName(appName);
						dataMessage.setKeyName(keyName);
						dataMessage.setDataList(v);
						dataMessage.setPropertyName(p);
						dataMessage.setIp(ip.substring(ip.lastIndexOf(Constants.S_SEPERATOR)+1));
						if(v != null){
							if(m.checkData(dataMessage)){								
								AlarmReport report = new AlarmReport();
								report.setAppName(appName);
								report.setKeyName(keyName);
								report.setContinuous(appcontinuous);
								report.setKeyScope("HOST");
								report.setModeName(m.getModeName());
								report.setCause(m.getAlarmCause());
								report.setKeyLevel(m.getKeyLevel() + "");
								String tmp = ip.substring(ip.lastIndexOf(Constants.S_SEPERATOR)+1);
								report.setIp(tmp);
								report.setPropertyName(p);
								DataEntry ed = m.getAlarmData();
								report.setTime(ed.getTime());
								report.setValue(ed.getValue());
								report.setKeyAlias(keyAlias);
								CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
								transformAlarmReportToPo(report,po);
								List<CspTimeKeyAlarmRecordPo> poList = new ArrayList<CspTimeKeyAlarmRecordPo>();
								poList.add(po);
								CspTimeKeyAlarmRecordAo.get().insert(poList);
								ipMap.put(p, report);
								logger.info("机器级别:"+ip+":"+appName+":"+keyName+":"+p+m.getAlarmData());
							}
						}
					}
				}
				//发送机器告警
				if(alarmIpmap.size() >0){
					
					//在5分钟呢再次出现表示连续
					if(System.currentTimeMillis() - preHostAlarmTime>300000){
						preHostAlarmTime = System.currentTimeMillis();
						hostcontinuous=1;
					}else{
						hostcontinuous++;
					}
					
					for(Map.Entry<String,Map<String,AlarmReport>> entry: alarmIpmap.entrySet()){
						String ip = entry.getKey();
						if(entry.getValue()!=null){
							for(Map.Entry<String,AlarmReport> pEntry:entry.getValue().entrySet()){
								pEntry.getValue().setContinuous(hostcontinuous);
								AlarmSender.get().putAlarmInfo(pEntry.getValue(),ip);
							}
						}
						
					}
				}
			} catch (Exception e) {
			}
		}
	}
	
	
	
	public void addModeConfig(CspKeyMode mode){
		String scope = mode.getKeyScope();
		String propertyName = mode.getPropertyName();
				
		
		
		if("APP".equals(scope)||"ALL".equals(scope)){
			
			CheckMode checkmode = CheckModeFactory.createCheckMode( mode.getAppModeConfig());
			if(checkmode != null){
				checkmode.setKeyLevel(mode.getKeyLevel());
				appScopePropMap.put(propertyName, checkmode);
			}
		}
		if("HOST".equals(scope)||"ALL".equals(scope)){
			
			CheckMode checkmode = CheckModeFactory.createCheckMode( mode.getHostModeConfig());
			if(checkmode != null){
				checkmode.setKeyLevel(mode.getKeyLevel());
				hostScopePropMap.put(propertyName, checkmode);
			}
			
		}
		
	}
	
	
	public void clearMode(CspKeyMode mode){
		appScopePropMap.clear();
		hostScopePropMap.clear();
	}
}
