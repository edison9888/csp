package com.taobao.monitor.alarm.source;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import com.taobao.monitor.alarm.source.dao.KeySourceDependencyDao;
import com.taobao.monitor.alarm.source.po.KeySourcePo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.time.constants.OptionConstants;
import com.taobao.monitor.web.ao.BeidouAlarmAO;
import com.taobao.monitor.web.ao.MonitorAlarmAo;
import com.taobao.monitor.web.vo.AlarmDataForPageViewPo;
import com.taobao.monitor.web.vo.beidou.BeidouAlarmDataPo;
import com.taobao.util.CollectionUtil;

public class AlarmSourceConclude {
	private static HashMap<String, AtomicReference<Map<String, String>>> allSystemDependencyMap = new HashMap<String, AtomicReference<Map<String, String>>>();

	private static KeySourceDependencyDao dao = new KeySourceDependencyDao();
	private static AlarmSourceConclude alarmSourceConclude = new AlarmSourceConclude();

	public static AlarmSourceConclude get() {
		return alarmSourceConclude;
	}

	static{
		getKeySourceDefine();
	}
	
	public static void getKeySourceDefine() {
		// 得到key_id与特定依赖应用的绑定关系，先找到所有应用,再初始化
		List<AppInfoPo> alarmApp = AppInfoAo.get().findAllTimeApp();
		for (AppInfoPo app : alarmApp) {
			if (app.getAppStatus() == 1) {
				continue;
			}
			List<KeySourcePo> keySourcePoList = dao
					.findAllKeySourcePosByAppId(app.getAppId());
			if (CollectionUtil.isEmpty(keySourcePoList)) {
				continue;
			}
			// key为告警的keyName，value为被依赖的系统名称
			Map<String, String> newSingleApp = new HashMap<String, String>();
			for (KeySourcePo po : keySourcePoList) {
				newSingleApp.put(po.getKeyName(), po.getSourceAppName());
			}
			if(allSystemDependencyMap.get(app.getAppName()) == null){
				allSystemDependencyMap.put(app.getAppName(), new AtomicReference<Map<String, String>>());
			}
			allSystemDependencyMap.get(app.getAppName()).getAndSet(newSingleApp);
		}
	}

	
	public static Set<String> getAlarmSource(Date start, Date end,Set<String> appNameSet) {
		Set<String> alarmSource = new HashSet<String>();
		// DB的告警数据，认为DB没有依赖系统，直接作为报警源头,
		Map<String,List<BeidouAlarmDataPo>> beidouAlarmMap = BeidouAlarmAO.get().getBeidouAlarmDataMapByTime(start,end);
		Set<String> groupNameSet =beidouAlarmMap.keySet();
		
		if (CollectionUtil.isNotEmpty(groupNameSet)) {
			for (String groupName : groupNameSet) {
				alarmSource.add(groupName);
			}
		}

		// 先得到全部的告警数据，然后按照报警的key进行推断，哪个被依赖的系统再进行报警，再判断被依赖的这个系统本身是否在告警，如果也在，就推算告警源头
		// 是被依赖的系统，这个需要递归循环推断，直到没有被依赖的系统或者报警的key没有对应任何被依赖的系统
		// tair server的告警数据也在里面
		List<Integer> list =  Arrays.asList(OptionConstants.ALARM_OPTION_P0, OptionConstants.ALARM_OPTION_P2);
	    //当前时间和历史时间点都调用这个方法查询
		HashMap<String, List<AlarmDataForPageViewPo>> alarmData = MonitorAlarmAo.get().getHistoryTradeRalateAlarmMapByTime(start,end,list,appNameSet);
		// 没有应用告警数据，只返回DB的
		if (alarmData ==null || alarmData.size() == 0) {
			return alarmSource;
		}

		Set<Map.Entry<String, List<AlarmDataForPageViewPo>>> allAlarmEntrySet = alarmData.entrySet();
		for (Map.Entry<String, List<AlarmDataForPageViewPo>> entry : allAlarmEntrySet) {
			int appId = AppInfoAo.get().getAppInfoName2IdMap().get(entry.getKey());
			List<AlarmDataForPageViewPo> alarmList = entry.getValue();
			// 没有告警数据
			if (CollectionUtil.isEmpty(alarmList)) {
				continue;
			}

			// 没有被依赖的系统，则认为这个就是源头，直接添加
			if (allSystemDependencyMap.get(AppInfoAo.get().getAppInfoId2NameMap().get(appId)) == null) {
				alarmSource.add(AppInfoAo.get().getAppInfoId2NameMap()
						.get(appId));
				continue;
			}
			// 当前系统所有报警的key与被依赖系统的对应关系
			Map<String, String> singleDependencyMap = allSystemDependencyMap.get(AppInfoAo.get().getAppInfoId2NameMap().get(appId)).get();

			//这个应用当前报警的所有key
			for (AlarmDataForPageViewPo alarm : alarmList) {
				//alarm.getKeyName() 可能是alias， 需要转化为keyName
				//String realKeyName =  KeyCache.get().getKey(alarm.getKeyId()).getKeyName();
				String sourceSysName = singleDependencyMap.get(alarm.getKeyName());
				
				// 此处不能定义为int,NPE,被依赖的系统可能没在map里面
				Integer dependencySystemId = AppInfoAo.get().getAppInfoName2IdMap().get(sourceSysName);
				//如果被依赖的系统是DB，或者别的，不在app的表里存在，则认为被依赖的是告警源头
				if(dependencySystemId ==null){
					alarmSource.add(sourceSysName);
					//如果被依赖的系统有告警，则过滤掉这个告警，用被依赖系统的告警记录处理
				}else if(CollectionUtil.isNotEmpty(alarmData.get(dependencySystemId))){
					
					//如果被依赖的系统没有告警，则认为当前告警的系统就是告警源头
				}else if(CollectionUtil.isEmpty(alarmData.get(dependencySystemId))){
					alarmSource.add(alarm.getAppName());
				}
			}
				
		}

		return alarmSource;

	}

}
