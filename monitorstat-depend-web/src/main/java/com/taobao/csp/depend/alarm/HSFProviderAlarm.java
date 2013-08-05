package com.taobao.csp.depend.alarm;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.ao.AlarmConfigAo;
import com.taobao.csp.depend.po.alarm.AlarmConfigPo;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.csp.depend.util.MethodUtil;

/**
 * HSF provider ����
 * @author zhongting.zy
 *
 */
public class HSFProviderAlarm extends AlarmSuper{
	private final Logger logger = Logger.getLogger(HSFProviderAlarm.class);
	public void alarm(String selectAppName) {
		logger.info("HSFProviderAlarm��ʼ����...");
		long start = System.currentTimeMillis();
		final AlarmConfigAo configAo = AlarmConfigAo.get();
		List<AlarmConfigPo> configList = AlarmConfigAo.get().getAlarmConfig(ConstantParameters.HSF_PROVIDE_ALARM);
		final Date now = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(now);
		cal.set(Calendar.HOUR_OF_DAY, 1);

		final String yestorday = MethodUtil.getOneDayPre();
		Iterator<AlarmConfigPo> iter = configList.iterator();
		while(iter.hasNext()) {
			AlarmConfigPo po = iter.next();
			if(selectAppName == null) {
				if(po.getLastSendTime() > cal.getTimeInMillis()) {
					logger.info(po.getAppName() + "�����ѷ��͹�������ʱ��->" + po.getLastSendTime());
					iter.remove();
				}				
			} else {
				//���selectAppName ��null���������ͣ���У���ϴη��͵�ʱ��
				if(!po.getAppName().equalsIgnoreCase(selectAppName)) {
					iter.remove();
				}
			}
		}

		final String LINE_SEPERSATOR = "<br/>"; 
		
		for(AlarmConfigPo po : configList) {
			String compareDay = MethodUtil.getStringOfDate(MethodUtil.getDaysBefore(now, po.getDaysPre() + 1));
			Map<String, Set<String>> time = configAo.getHsfAlarmMap(po.getAppName(), yestorday);
			Map<String, Set<String>> timeCompare = configAo.getHsfAlarmMap(po.getAppName(), compareDay);
			
			StringBuilder sbWangwang = new StringBuilder(LINE_SEPERSATOR);
			
			boolean isSendAlarm = false;
			StringBuilder sb = new StringBuilder(LINE_SEPERSATOR);
			sb.append("Ӧ�ã�" + po.getAppName() + "���Ա�ʱ��:" + yestorday + "~" + compareDay + LINE_SEPERSATOR);
			sb.append(LINE_SEPERSATOR);
			
			sbWangwang.append(sb.toString());
			
			final Set<String> interfaceSet = new HashSet<String>(time.keySet());
			final Set<String> interfaceCompareSet = new HashSet<String>(timeCompare.keySet());

			Iterator<String> iterator = interfaceCompareSet.iterator();
			int i=0;
			while(iterator.hasNext()) {
				String interfaceName = iterator.next(); 
				if(time.containsKey(interfaceName)) {
					interfaceSet.remove(interfaceName);//����Ľӿ�
				} else {
					sb.append("���ٽӿ�->" + interfaceName).append(LINE_SEPERSATOR);
					iterator.remove();
					i++;
				}
			}
			if(i>0) {
				isSendAlarm = true;
				sb.append(LINE_SEPERSATOR);
				sbWangwang.append("���ٽӿ�:" + i + "��").append(LINE_SEPERSATOR);
			}

			
			i=0;
			for(String interfaceName : interfaceSet) {
				sb.append("�����ӿ�->" + interfaceName).append(",���ѽӿڵ�Ӧ��:").append(LINE_SEPERSATOR);
				Set<String> appSet = time.get(interfaceName);
				for(String appName: appSet) {
					sb.append(appName).append(",");
				}
				if(sb.lastIndexOf(",") == sb.length()-1) {
					sb.deleteCharAt(sb.length()-1).append(LINE_SEPERSATOR);
				}
				i++;
			}
			if(i>0) {
				isSendAlarm = true;
				sbWangwang.append("�����ӿ�:" + i + "��").append(LINE_SEPERSATOR);
			}
			
			
			//�ԱȲ���Ĺ���
			i = 0;
			for(String interfaceName : interfaceCompareSet) {
				if(timeCompare.containsKey(interfaceName)) {
					StringBuilder sbSub = new StringBuilder();
					Set<String> appSet = time.get(interfaceName);
					Set<String> appSetCompare = timeCompare.get(interfaceName);
					
					boolean isChange = false;
					
					String subApp = "����Ӧ��:";
					for(String appName: appSetCompare) {
						if(!appSet.contains(appName)) {
							subApp += appName + ",";
						} else {
							appSet.remove(appName);
						}
					}
					if(subApp.lastIndexOf(",") == subApp.length()-1) {
						subApp = subApp.substring(0, subApp.length() - 1);
						subApp += LINE_SEPERSATOR;
						isChange = true;
					}
					
					if(isChange) {
						sbSub.append(subApp);
					}
					
					String addApp = "����Ӧ��:";
					for(String appName: appSet) {
						addApp += appName + ",";
					}
					if(addApp.lastIndexOf(",") == addApp.length()-1) {
						addApp = addApp.substring(0, addApp.length() - 1);
						addApp += LINE_SEPERSATOR;
						isChange = true;
						sbSub.append(addApp);
					}					
					if(isChange) {
						i++;
						sb.append("Ӧ�ñ仯�Ľӿ�->" + interfaceName + ",�仯���:").append(LINE_SEPERSATOR);
						sb.append(sbSub.toString()).append(LINE_SEPERSATOR);
					}
					isSendAlarm |= isChange;
				}				
			}
			sbWangwang.append("����Ӧ�ñ仯�Ľӿ�:" + i + "����û�з����仯�Ľӿ���" + (interfaceCompareSet.size() - i) + "��").append(LINE_SEPERSATOR);
			
			String msg = String.format("���ͱ���.Ӧ��=%s,�ʼ�:%s,\n����:%s", po.getAppName(),po.getEmailString(),sb.toString());
			logger.info(msg);
			
			sb.append(LINE_SEPERSATOR).append("������ϸ��Ϣ�����������ϵͳ(HSF�ӿڷ����б�->Ӧ�÷ֲ�):").append(LINE_SEPERSATOR);
			sb.append("http://depend.csp.taobao.net:9999/depend/show/hsfprovide.do?method=showAppAllCenterInterface&opsName=" 
					+ po.getAppName() + "&selectDate=" + yestorday).append(LINE_SEPERSATOR);
			sb.append("���ڱ���������鿴->http://www.alibabatech.org/article/detail/3174/0").append(LINE_SEPERSATOR);
			sb.append("������ʣ�����ϵ\"��ͤ\",�����CSP����Ⱥ(911658553)").append(LINE_SEPERSATOR);

			sbWangwang.append("������Ϣ��鿴�����ʼ�������ϵͳ(HSF�ӿڷ����б�->Ӧ�÷ֲ�):").append(LINE_SEPERSATOR)
					  .append("http://depend.csp.taobao.net:9999/depend/show/hsfprovide.do?method=showAppAllCenterInterface&opsName=" 
							  + po.getAppName() + "&selectDate=" + yestorday);
			
			if(isSendAlarm) {
				this.sendEmail(sb.toString(), po);
				this.sendWangwang(sbWangwang.toString(), po);
				//���·���ʱ��
				po.setLastSendTime(System.currentTimeMillis());
				configAo.insertOrUpdate(po);
			}
		}
		logger.info("HSFProviderAlarm������������ʱ->" + (System.currentTimeMillis() - start));
	}
}
