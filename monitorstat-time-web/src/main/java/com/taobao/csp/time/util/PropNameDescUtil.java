
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.util;

import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.dataserver.PropConstants;

/**
 * @author xiaodu
 *
 * ����5:09:48
 */
public class PropNameDescUtil {
	
	private static Map<String,String> propertyNameMap = new HashMap<String, String>();
	static{
		propertyNameMap.put(PropConstants.E_TIMES, "������");
		propertyNameMap.put(PropConstants.C_200, "200״̬��");
		propertyNameMap.put(PropConstants.C_302, "302״̬��");
		propertyNameMap.put(PropConstants.C_OTHER, "����״̬��");
		propertyNameMap.put(PropConstants.C_TIME, "��Ӧʱ��(ms)");
		propertyNameMap.put(PropConstants.P_SIZE, "�����С(�ֽ�)");
		propertyNameMap.put("rt_error", "��Ӧʱ�����1s");
		propertyNameMap.put("rt_100", "��Ӧʱ��С��100ms");
		propertyNameMap.put("rt_500", "��Ӧʱ��100~500ms");
		propertyNameMap.put("rt_1000", "��Ӧʱ��500~1000ms");
		
		propertyNameMap.put(PropConstants.JVMGC, "GC����");
		propertyNameMap.put(PropConstants.JVMMEMORY, "�ڴ�ʹ����(%)");
		propertyNameMap.put(PropConstants.JVMFULLGC, "full gc����");
		propertyNameMap.put(PropConstants.JVMCMSGC, "cms����");
		
		propertyNameMap.put(PropConstants.EXCEPTION, "�쳣��");
		
		propertyNameMap.put(PropConstants.THREAD_BLOCKED, "������");
		propertyNameMap.put(PropConstants.THREAD_TERMINATED, "��ֹ");
		propertyNameMap.put(PropConstants.THREAD_TIMEDWAITING, "��ʱ�ȴ�");
		propertyNameMap.put(PropConstants.THREAD_RUNNABLE, "����ִ���߳�");
		
		propertyNameMap.put(PropConstants.DATASOURCE_IN_USE, "�Ѿ�ʹ�����ӳ�");
		propertyNameMap.put(PropConstants.DATASOURCE_AVAILABLE, "�������ӳ���");
		
		propertyNameMap.put(PropConstants.THREAD_POOL_CURRENT, "�Ѿ�ʹ���̳߳�");
		propertyNameMap.put(PropConstants.THREAD_POOL_MAX, "����̳߳�");
		
		
		
		
		propertyNameMap.put(PropConstants.NOTIFY_C_S, "ͬ���ɹ�");
		propertyNameMap.put(PropConstants.NOTIFY_C_S_RT, "ͬ���ɹ� ����ʱ��");
		propertyNameMap.put(PropConstants.NOTIFY_C_S_F, "ͬ������ʧ��");
		propertyNameMap.put(PropConstants.NOTIFY_C_RA_S, "�ɿ��첽�ɹ�");
		propertyNameMap.put(PropConstants.NOTIFY_C_RA_S_RT, "�ɿ��첽�ɹ� ����ʱ��");
		propertyNameMap.put(PropConstants.NOTIFY_C_RA_F, "�ɿ��첽ʧ��");
		propertyNameMap.put(PropConstants.NOTIFY_C_WS, "ͬ�����͵ȴ�����200ms�ɹ�");
		propertyNameMap.put(PropConstants.NOTIFY_C_TIMEOUT, "ͬ�����ͳ�ʱ");
		propertyNameMap.put(PropConstants.NOTIFY_C_RE, "ͬ�����ͽ��һ��");
		propertyNameMap.put(PropConstants.NOTIFY_C_NC, "ͬ������û������");
		
		propertyNameMap.put(PropConstants.FAIL_PERCENT, "ʧ����(%)");
		
		propertyNameMap.put(PropConstants.SUCCESS_INVOKE, "�ɹ�������");
		propertyNameMap.put(PropConstants.FAILURE_INVOKE, "ʧ��������");
		propertyNameMap.put(PropConstants.TIMEOUT_INVOKE, "��ʱ������");
		
	}
	
	/**
	 * ��ȡ�������Ƶ�����������Ҳ�������ԭֵ
	 *@author xiaodu
	 * @param propName
	 * @return
	 *TODO
	 */
	public static String getDesc(String propName){
		String name = propertyNameMap.get(propName);
		if(name !=null){
			return name;
		}
		return propName;
	}
	

}
