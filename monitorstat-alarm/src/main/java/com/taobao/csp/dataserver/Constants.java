package com.taobao.csp.dataserver;

public class Constants {

	//10分钟
	public final static long twelMinute=1000*60*12;
	//3分钟
	public final static long threeMinute=1000*60*3;
	
	public static  int HSF_APPNAME_CACHE = 1;
	
	public static int HSF_KEYNAME_CACHE=2;
	
	
	public static int HSF_CONSUMER_TYPE=1;
	
	public static int HSF_PROVIDER_TYPE=2;

	public static final int CACHE_TIME_INTERVAL = 10;		//内存中数据保存时间
	public static final int CACHE_SECOND = 60;		//内存中数据保存时间
	
	public static final int DATA_FAIL_TIME_INTERVAL = 1;	//数据插入数据库的时间间隔
	
	public static final String S_SEPERATOR = "`";			//key拼装时的间隔
	
	/**
	 * 是否写入bdb queue 
	 * 在磁盘空间不足的情况下，该标志位为false
	 */
	public static boolean IN_BDB=true;
	
	//这个时间点之前的数据全按string查，这个时间点之后的数据，按照id查
	public static long QUERY_DEAD_TIME=1354240026966L;

	
	
	public static int ZK_TIME_OUT =  10000;
	
	public static long HEART_FREQUENCY = 10000;
	
	
	public static  String BASELINE_HBASE_COL="BASELINE";
	
	public static String ZK_ALARM_ACCEPT_NODE = "/csp/alarm_server";//告警数据接收节点
	
	public static String ZK_DS_ROOT_NODE = "/csp/data_cache_server";//缓存数据节点
	
//	public static String ZK_DS_ROOT_NODE = "/csp/cache_tmp";//缓存数据节点
	
	public static String ZK_DS_COLLECT_NODE = "/csp/data_collect_client";//收集节点
	
	public static String ZK_KEY_CONFIG_NODE = "/csp/key_config";//key的配置变更节点
	
	public static String ZK_CSP_ROOT_NODE = "/csp";

	
	//缓存日志名称
	public static final String RPC_READ_LOG = "rpc_read";
	public static final String RPC_WRITE_LOG = "rpc_write";
	public static final String MEMORY_DATA_STATUS_LOG = "memory_data_status";
	public static final String DATA_PERSISTENCE_LOG = "data_persistence";
	
	//采集 日志名称
	public static final String COLLECTION_KEYSTATUS = "collection_keystatus";
	public static final String COLLECTION_APP_SEND = "collection_appsend";
}
