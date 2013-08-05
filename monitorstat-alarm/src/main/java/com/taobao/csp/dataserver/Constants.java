package com.taobao.csp.dataserver;

public class Constants {

	//10����
	public final static long twelMinute=1000*60*12;
	//3����
	public final static long threeMinute=1000*60*3;
	
	public static  int HSF_APPNAME_CACHE = 1;
	
	public static int HSF_KEYNAME_CACHE=2;
	
	
	public static int HSF_CONSUMER_TYPE=1;
	
	public static int HSF_PROVIDER_TYPE=2;

	public static final int CACHE_TIME_INTERVAL = 10;		//�ڴ������ݱ���ʱ��
	public static final int CACHE_SECOND = 60;		//�ڴ������ݱ���ʱ��
	
	public static final int DATA_FAIL_TIME_INTERVAL = 1;	//���ݲ������ݿ��ʱ����
	
	public static final String S_SEPERATOR = "`";			//keyƴװʱ�ļ��
	
	/**
	 * �Ƿ�д��bdb queue 
	 * �ڴ��̿ռ䲻�������£��ñ�־λΪfalse
	 */
	public static boolean IN_BDB=true;
	
	//���ʱ���֮ǰ������ȫ��string�飬���ʱ���֮������ݣ�����id��
	public static long QUERY_DEAD_TIME=1354240026966L;

	
	
	public static int ZK_TIME_OUT =  10000;
	
	public static long HEART_FREQUENCY = 10000;
	
	
	public static  String BASELINE_HBASE_COL="BASELINE";
	
	public static String ZK_ALARM_ACCEPT_NODE = "/csp/alarm_server";//�澯���ݽ��սڵ�
	
	public static String ZK_DS_ROOT_NODE = "/csp/data_cache_server";//�������ݽڵ�
	
//	public static String ZK_DS_ROOT_NODE = "/csp/cache_tmp";//�������ݽڵ�
	
	public static String ZK_DS_COLLECT_NODE = "/csp/data_collect_client";//�ռ��ڵ�
	
	public static String ZK_KEY_CONFIG_NODE = "/csp/key_config";//key�����ñ���ڵ�
	
	public static String ZK_CSP_ROOT_NODE = "/csp";

	
	//������־����
	public static final String RPC_READ_LOG = "rpc_read";
	public static final String RPC_WRITE_LOG = "rpc_write";
	public static final String MEMORY_DATA_STATUS_LOG = "memory_data_status";
	public static final String DATA_PERSISTENCE_LOG = "data_persistence";
	
	//�ɼ� ��־����
	public static final String COLLECTION_KEYSTATUS = "collection_keystatus";
	public static final String COLLECTION_APP_SEND = "collection_appsend";
}
