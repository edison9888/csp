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
		// �õ�key_id���ض�����Ӧ�õİ󶨹�ϵ�����ҵ�����Ӧ��,�ٳ�ʼ��
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
			// keyΪ�澯��keyName��valueΪ��������ϵͳ����
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
		// DB�ĸ澯���ݣ���ΪDBû������ϵͳ��ֱ����Ϊ����Դͷ,
		Map<String,List<BeidouAlarmDataPo>> beidouAlarmMap = BeidouAlarmAO.get().getBeidouAlarmDataMapByTime(start,end);
		Set<String> groupNameSet =beidouAlarmMap.keySet();
		
		if (CollectionUtil.isNotEmpty(groupNameSet)) {
			for (String groupName : groupNameSet) {
				alarmSource.add(groupName);
			}
		}

		// �ȵõ�ȫ���ĸ澯���ݣ�Ȼ���ձ�����key�����ƶϣ��ĸ���������ϵͳ�ٽ��б��������жϱ����������ϵͳ�����Ƿ��ڸ澯�����Ҳ�ڣ�������澯Դͷ
		// �Ǳ�������ϵͳ�������Ҫ�ݹ�ѭ���ƶϣ�ֱ��û�б�������ϵͳ���߱�����keyû�ж�Ӧ�κα�������ϵͳ
		// tair server�ĸ澯����Ҳ������
		List<Integer> list =  Arrays.asList(OptionConstants.ALARM_OPTION_P0, OptionConstants.ALARM_OPTION_P2);
	    //��ǰʱ�����ʷʱ��㶼�������������ѯ
		HashMap<String, List<AlarmDataForPageViewPo>> alarmData = MonitorAlarmAo.get().getHistoryTradeRalateAlarmMapByTime(start,end,list,appNameSet);
		// û��Ӧ�ø澯���ݣ�ֻ����DB��
		if (alarmData ==null || alarmData.size() == 0) {
			return alarmSource;
		}

		Set<Map.Entry<String, List<AlarmDataForPageViewPo>>> allAlarmEntrySet = alarmData.entrySet();
		for (Map.Entry<String, List<AlarmDataForPageViewPo>> entry : allAlarmEntrySet) {
			int appId = AppInfoAo.get().getAppInfoName2IdMap().get(entry.getKey());
			List<AlarmDataForPageViewPo> alarmList = entry.getValue();
			// û�и澯����
			if (CollectionUtil.isEmpty(alarmList)) {
				continue;
			}

			// û�б�������ϵͳ������Ϊ�������Դͷ��ֱ�����
			if (allSystemDependencyMap.get(AppInfoAo.get().getAppInfoId2NameMap().get(appId)) == null) {
				alarmSource.add(AppInfoAo.get().getAppInfoId2NameMap()
						.get(appId));
				continue;
			}
			// ��ǰϵͳ���б�����key�뱻����ϵͳ�Ķ�Ӧ��ϵ
			Map<String, String> singleDependencyMap = allSystemDependencyMap.get(AppInfoAo.get().getAppInfoId2NameMap().get(appId)).get();

			//���Ӧ�õ�ǰ����������key
			for (AlarmDataForPageViewPo alarm : alarmList) {
				//alarm.getKeyName() ������alias�� ��Ҫת��ΪkeyName
				//String realKeyName =  KeyCache.get().getKey(alarm.getKeyId()).getKeyName();
				String sourceSysName = singleDependencyMap.get(alarm.getKeyName());
				
				// �˴����ܶ���Ϊint,NPE,��������ϵͳ����û��map����
				Integer dependencySystemId = AppInfoAo.get().getAppInfoName2IdMap().get(sourceSysName);
				//�����������ϵͳ��DB�����߱�ģ�����app�ı�����ڣ�����Ϊ���������Ǹ澯Դͷ
				if(dependencySystemId ==null){
					alarmSource.add(sourceSysName);
					//�����������ϵͳ�и澯������˵�����澯���ñ�����ϵͳ�ĸ澯��¼����
				}else if(CollectionUtil.isNotEmpty(alarmData.get(dependencySystemId))){
					
					//�����������ϵͳû�и澯������Ϊ��ǰ�澯��ϵͳ���Ǹ澯Դͷ
				}else if(CollectionUtil.isEmpty(alarmData.get(dependencySystemId))){
					alarmSource.add(alarm.getAppName());
				}
			}
				
		}

		return alarmSource;

	}

}
