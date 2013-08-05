package com.taobao.csp.depend.util;

/**
 * 
 * @author zhongting.zy
 * 常量接口，用来放置一些约定好的类型
 */
public interface ConstantParameters {
	
	/*对比依赖状态*/
	public static final String CONTROST_ADD = "add";
	public static final String CONTROST_SUB = "sub";
	public static final String CONTROST_SAME = "same";
	
	/*依赖强弱*/
	public static final String DEPEND_STATE_STRONG = "strong";
	public static final String DEPEND_STATE_WEAK = "weak";
	
	public static final String DEPEND_STATE_STRONG_CN = "强";
	public static final String DEPEND_STATE_WEAK_CN = "弱";
	public static final String DEPEND_STATE_WEAK_UNKOWN = "未知";
	
	/*应用类型*/
	public static final String APPTYPE_FRONT = "front";
	public static final String APPTYPE_CENTER = "center";
	
	public static final int NUMBER_OF_DAY_PRE = 7;		//对比的天数
	public static String FORMAT_STRING = "yyyy-MM-dd";
	public static String FORMAT_STRING_FULL = "yyyy-MM-dd HH:mm:ss";
	
	public static final String SUCCESS = "success";
	public static final String FAILURE = "failure";

	//GroupName
	public static final String EMPTY_TAIR_GROUPNAME = "无";

	//tair显示相关的变量
	public static final String STRING_TYPE_HIT = "hit";
	public static final String STRING_TYPE_LEN = "len";
	
	//必须从0开始，CTairNameSpaceUnit做数组下表
	public static final int INT_TYPE_NOR = 0;
	public static final int INT_TYPE_HIT = 1;
	public static final int INT_TYPE_LEN = 2;
	
	public static final String APPSTATUS_EFFECT = "1";
	public static final String APPSTATUS_INVALID = "-1";
	
	public static final String DEPEND_MIDDLE_SEPERATOR = "<seperate>";  
	public static final String DEPEND_CHECK_MESSAGE_SEPERATOR = "<br/>";  //依赖检测部分，信息换行表示
	public static final String IMAGE_PLACEHODER_START = "<IMAGE>"; // 图片开始符号
	public static final String IMAGE_PLACEHODER_END = "</IMAGE>"; // 图片结束符号
	public static final String ERROR_MESSAGE = "<ERROR>"; //打印异常
	public static final int SLEEP_SECONDS_SELENIUM = 10; //Selenium脚本中每一步睡眠的时间
	
	public static final long HTTPLOAD_TIMEOUT_SECONDS = 60*5; // 5分钟超时
	
	public static int HSF_PROVIDE_ALARM = 0;
	
	/*****依赖地图相关的常量*******/
	public static int CSP_DEPEND_MAP_STATUS_AUTO = 0;
	public static int CSP_DEPEND_MAP_STATUS_CONFIG = 1;
	
	public static int CSP_DEPEND_MAP_BLACK = 1;
	public static int CSP_DEPEND_MAP_NOT_BLACK = 0;
	
	//依赖地图control_type 字段
	public static int CSP_DEPEND_MAP_NO_CONTROL = 0;
	public static int CSP_DEPEND_MAP_TMD = 1;
	public static int CSP_DEPEND_MAP_SS = 2;
	
	public static String CSP_DEPEND_MAP_DEFAULT_CREATOR = "csp_auto";	//默认创建者
	public static String DEFAULT_URL = "firsturl";	//默认创建者
}
