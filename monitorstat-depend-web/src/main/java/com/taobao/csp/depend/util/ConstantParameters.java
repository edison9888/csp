package com.taobao.csp.depend.util;

/**
 * 
 * @author zhongting.zy
 * �����ӿڣ���������һЩԼ���õ�����
 */
public interface ConstantParameters {
	
	/*�Ա�����״̬*/
	public static final String CONTROST_ADD = "add";
	public static final String CONTROST_SUB = "sub";
	public static final String CONTROST_SAME = "same";
	
	/*����ǿ��*/
	public static final String DEPEND_STATE_STRONG = "strong";
	public static final String DEPEND_STATE_WEAK = "weak";
	
	public static final String DEPEND_STATE_STRONG_CN = "ǿ";
	public static final String DEPEND_STATE_WEAK_CN = "��";
	public static final String DEPEND_STATE_WEAK_UNKOWN = "δ֪";
	
	/*Ӧ������*/
	public static final String APPTYPE_FRONT = "front";
	public static final String APPTYPE_CENTER = "center";
	
	public static final int NUMBER_OF_DAY_PRE = 7;		//�Աȵ�����
	public static String FORMAT_STRING = "yyyy-MM-dd";
	public static String FORMAT_STRING_FULL = "yyyy-MM-dd HH:mm:ss";
	
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";

	//GroupName
	public static final String EMPTY_TAIR_GROUPNAME = "��";

	//tair��ʾ��صı���
	public static final String STRING_TYPE_HIT = "hit";
	public static final String STRING_TYPE_LEN = "len";
	
	//�����0��ʼ��CTairNameSpaceUnit�������±�
	public static final int INT_TYPE_NOR = 0;
	public static final int INT_TYPE_HIT = 1;
	public static final int INT_TYPE_LEN = 2;
	
	public static final String APPSTATUS_EFFECT = "1";
	public static final String APPSTATUS_INVALID = "-1";
	
	public static final String DEPEND_MIDDLE_SEPERATOR = "<seperate>";  
	public static final String DEPEND_CHECK_MESSAGE_SEPERATOR = "<br/>";  //������ⲿ�֣���Ϣ���б�ʾ
	public static final String IMAGE_PLACEHODER_START = "<IMAGE>"; // ͼƬ��ʼ����
	public static final String IMAGE_PLACEHODER_END = "</IMAGE>"; // ͼƬ��������
	public static final String ERROR_MESSAGE = "<ERROR>"; //��ӡ�쳣
	public static final int SLEEP_SECONDS_SELENIUM = 10; //Selenium�ű���ÿһ��˯�ߵ�ʱ��
	
	public static final long HTTPLOAD_TIMEOUT_SECONDS = 60*5; // 5���ӳ�ʱ
	
	public static int HSF_PROVIDE_ALARM = 0;
	
	/*****������ͼ��صĳ���*******/
	public static int CSP_DEPEND_MAP_STATUS_AUTO = 0;
	public static int CSP_DEPEND_MAP_STATUS_CONFIG = 1;
	
	public static int CSP_DEPEND_MAP_BLACK = 1;
	public static int CSP_DEPEND_MAP_NOT_BLACK = 0;
	
	//������ͼcontrol_type �ֶ�
	public static int CSP_DEPEND_MAP_NO_CONTROL = 0;
	public static int CSP_DEPEND_MAP_TMD = 1;
	public static int CSP_DEPEND_MAP_SS = 2;
	
	public static String CSP_DEPEND_MAP_DEFAULT_CREATOR = "csp_auto";	//Ĭ�ϴ�����
	public static String DEFAULT_URL = "firsturl";	//Ĭ�ϴ�����
}
