
package com.taobao.monitor.common.util;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Trigger;


/**
 * 
 * @author xiaodu
 * @version 2010-4-10 上午11:13:23
 */
public class Constants {

	/**
	 * 用户标记统计执行次数的 汇总值
	 */
	public static String COUNT_TIMES_FLAG = "COUNTTIMES";
	/**
	 * 每个平均值
	 */
	public static String AVERAGE_USERTIMES_FLAG = "AVERAGEUSERTIMES";

	/**
	 * 每台机器平均总数
	 */
	public static String AVERAGE_MACHINE_FLAG = "AVERAGEMACHINEFLAG";


	/**
	 * 常量值，不需要平均和统计
	 */
	public static String CONSTANTS_VALUE_FLAG = "CONSTANTSVALUEFLAG";


	/**
	 * 定义这个数据是有效的，
	 */
	public static int DEFINE_DATA_EFFECTIVE=1;
	/**
	 * 定义这个数据失效的，
	 */
	public static int DEFINE_DATA_INVALIADATION=0;

	/**
	 * add by zhanfei.tm 
	 */

	/** 报表模板添加、发送时间配置权限 */
	public final static String REPORT_CONFIG_POWER = "reportconfig";

	/** 报表邮件接收人 */
	public final static String REPORT_EMAIL_ACCEPTOR = "email";

	/** 报表JobDetail名称前缀 */
	public final static String JOBDETAIL_PREFIX = "reportJob_";

	/** 报表trigger名称前缀 */
	public final static String TRIGGER_PREFIX = "trigger_";

	/** 任务组名字 */
	public static String JOB_GROUP_NAME = "JOB_GROUP_NAME";

	/** 触发器组名字 */
	public static String TRIGGER_GROUP_NAME = "TRIGGER_GROUP_NAME";  

	/** 区分应用 */
	public static int REPORT_TYPE_APP = 1; 

	/** 用户自定义 */
	public static int REPORT_TYPE_USER = 0; 

	/**
	 * 数据库中，key与key直接的分隔符
	 */
	public static String KEY_SEPERATOR = ","; 	

	/**
	 * 失败率
	 */
	public static String KEY_FAILPERCENT = "FAILPERCENT"; 	

	/**
	 * 线程数
	 */
	public static String KEY_THREAD_COUNT = "THREADCOUNT"; 	

	/**
	 * 耗时时间
	 */
	public static String KEY_COST = "COST"; 	

	/**
	 * 实时监控依赖部分使用
	 */
	//public final static int NOTAUTO_NOTCONFIG = 0; //这种情况不存在
	public final static int EXTRA_CONFIG = 1;  //手动添加Extra信息
	public final static int AUTO_NOTCONFIG = 2; //自动检查出来，但是未配置
	public final static int NOTAUTO_CONFIG = 3; //App的配置已经过期，auto没有检查出来
	public final static int AUTO_CONFIG = 4;  //自动检查出来，同时配置过

	public static final String API_URL_KEY_LIST = "url_key_list";
	public static final String API_HSF_KEY_LIST = "hsf_key_list";
	public static final String API_CHILD_APP = "child_app";
	public static final String API_FATHER_APP = "father_app";
	public static final String API_CHILD_KEY = "child_key";
	public static final String API_FATHER_KEY = "father_key";
	
	/*****依赖地图相关的常量*******/
	public static int CSP_DEPEND_MAP_STATUS_AUTO = 0;
	public static int CSP_DEPEND_MAP_STATUS_CONFIG = 1;
	
	//日志来源
	public static int CSP_DATA_FROM_EAGLEEYE = 0;
	public static int CSP_DATA_FROM_REALLOG = 1;
	
	public static int CSP_DEPEND_MAP_BLACK = 1;
	public static int CSP_DEPEND_MAP_NOT_BLACK = 0;
	
	//依赖地图control_type 字段
	public static int CSP_DEPEND_MAP_NO_CONTROL = 0;
	public static int CSP_DEPEND_MAP_TMD = 1;
	public static int CSP_DEPEND_MAP_SS = 2;
	
	public static String CSP_DEPEND_MAP_DEFAULT_CREATOR = "csp_auto";	//默认创建者
	public static String DEFAULT_URL = "firsturl";	//默认创建者
	
	/**
	 * 获取任务状态
	 * @author 斩飞
	 * @return
	 * 2011-5-13 - 下午07:08:14
	 */
	public static Map<Integer, String> triggerStateMap(){
		Map<Integer, String> map = new HashMap<Integer, String>();
		map.put(Trigger.STATE_NORMAL, "运行中");
		map.put(Trigger.STATE_PAUSED, "暂停中");
		map.put(Trigger.STATE_ERROR, "异常");
		map.put(Trigger.STATE_BLOCKED, "等待中");
		map.put(Trigger.STATE_NONE, "任务移除");
		return map;
	}
}
