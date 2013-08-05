
package com.taobao.monitor.common.util;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Trigger;


/**
 * 
 * @author xiaodu
 * @version 2010-4-10 ����11:13:23
 */
public class Constants {

	/**
	 * �û����ͳ��ִ�д����� ����ֵ
	 */
	public static String COUNT_TIMES_FLAG = "COUNTTIMES";
	/**
	 * ÿ��ƽ��ֵ
	 */
	public static String AVERAGE_USERTIMES_FLAG = "AVERAGEUSERTIMES";

	/**
	 * ÿ̨����ƽ������
	 */
	public static String AVERAGE_MACHINE_FLAG = "AVERAGEMACHINEFLAG";


	/**
	 * ����ֵ������Ҫƽ����ͳ��
	 */
	public static String CONSTANTS_VALUE_FLAG = "CONSTANTSVALUEFLAG";


	/**
	 * ���������������Ч�ģ�
	 */
	public static int DEFINE_DATA_EFFECTIVE=1;
	/**
	 * �����������ʧЧ�ģ�
	 */
	public static int DEFINE_DATA_INVALIADATION=0;

	/**
	 * add by zhanfei.tm 
	 */

	/** ����ģ����ӡ�����ʱ������Ȩ�� */
	public final static String REPORT_CONFIG_POWER = "reportconfig";

	/** �����ʼ������� */
	public final static String REPORT_EMAIL_ACCEPTOR = "email";

	/** ����JobDetail����ǰ׺ */
	public final static String JOBDETAIL_PREFIX = "reportJob_";

	/** ����trigger����ǰ׺ */
	public final static String TRIGGER_PREFIX = "trigger_";

	/** ���������� */
	public static String JOB_GROUP_NAME = "JOB_GROUP_NAME";

	/** ������������ */
	public static String TRIGGER_GROUP_NAME = "TRIGGER_GROUP_NAME";  

	/** ����Ӧ�� */
	public static int REPORT_TYPE_APP = 1; 

	/** �û��Զ��� */
	public static int REPORT_TYPE_USER = 0; 

	/**
	 * ���ݿ��У�key��keyֱ�ӵķָ���
	 */
	public static String KEY_SEPERATOR = ","; 	

	/**
	 * ʧ����
	 */
	public static String KEY_FAILPERCENT = "FAILPERCENT"; 	

	/**
	 * �߳���
	 */
	public static String KEY_THREAD_COUNT = "THREADCOUNT"; 	

	/**
	 * ��ʱʱ��
	 */
	public static String KEY_COST = "COST"; 	

	/**
	 * ʵʱ�����������ʹ��
	 */
	//public final static int NOTAUTO_NOTCONFIG = 0; //�������������
	public final static int EXTRA_CONFIG = 1;  //�ֶ����Extra��Ϣ
	public final static int AUTO_NOTCONFIG = 2; //�Զ�������������δ����
	public final static int NOTAUTO_CONFIG = 3; //App�������Ѿ����ڣ�autoû�м�����
	public final static int AUTO_CONFIG = 4;  //�Զ���������ͬʱ���ù�

	public static final String API_URL_KEY_LIST = "url_key_list";
	public static final String API_HSF_KEY_LIST = "hsf_key_list";
	public static final String API_CHILD_APP = "child_app";
	public static final String API_FATHER_APP = "father_app";
	public static final String API_CHILD_KEY = "child_key";
	public static final String API_FATHER_KEY = "father_key";
	
	/*****������ͼ��صĳ���*******/
	public static int CSP_DEPEND_MAP_STATUS_AUTO = 0;
	public static int CSP_DEPEND_MAP_STATUS_CONFIG = 1;
	
	//��־��Դ
	public static int CSP_DATA_FROM_EAGLEEYE = 0;
	public static int CSP_DATA_FROM_REALLOG = 1;
	
	public static int CSP_DEPEND_MAP_BLACK = 1;
	public static int CSP_DEPEND_MAP_NOT_BLACK = 0;
	
	//������ͼcontrol_type �ֶ�
	public static int CSP_DEPEND_MAP_NO_CONTROL = 0;
	public static int CSP_DEPEND_MAP_TMD = 1;
	public static int CSP_DEPEND_MAP_SS = 2;
	
	public static String CSP_DEPEND_MAP_DEFAULT_CREATOR = "csp_auto";	//Ĭ�ϴ�����
	public static String DEFAULT_URL = "firsturl";	//Ĭ�ϴ�����
	
	/**
	 * ��ȡ����״̬
	 * @author ն��
	 * @return
	 * 2011-5-13 - ����07:08:14
	 */
	public static Map<Integer, String> triggerStateMap(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Trigger.STATE_NORMAL, "������");
		map.put(Trigger.STATE_PAUSED, "��ͣ��");
		map.put(Trigger.STATE_ERROR, "�쳣");
		map.put(Trigger.STATE_BLOCKED, "�ȴ���");
		map.put(Trigger.STATE_NONE, "�����Ƴ�");
		return map;
	}
}
