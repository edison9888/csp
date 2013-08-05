package com.taobao.sentinel.client;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.util.PropertiesConfig;

public class SentinelConfig {
	
	public static Logger logger = Logger.getLogger(SentinelConfig.class);
	
	
	// mock url
	public static boolean MOCK_URL = false;
	public static String LIST_URL = "http://10.13.40.61:8090/sentinel/data.stn?type=list";
	public static String MAP_URL = "http://10.13.40.61:8090/sentinel/data.stn?type=map";
	public static String ALTER_URL = "http://10.13.40.61:8090/sentinel/data.stn?type=alter";
	
	// mock ip
	public static boolean  ADD_MOCK_IP = false;
	public static String  MOCK_IP = "127.0.0.1";
	
	// config
	public static String DIAMOND_ID = "online";
	public static String DATABASE_NAME = "csp_autoload";
	public static String RUN_SYNC_IP = "true";
	
	static {
		MOCK_URL = Boolean.valueOf(PropertiesConfig.getValue("sentinel",
				"MOCK_URL").toString());
		LIST_URL = PropertiesConfig.getValue("sentinel", "LIST_URL").toString();
		MAP_URL = PropertiesConfig.getValue("sentinel", "MAP_URL").toString();
		ALTER_URL = PropertiesConfig.getValue("sentinel", "ALTER_URL")
				.toString();

		ADD_MOCK_IP = Boolean.valueOf(PropertiesConfig.getValue("sentinel",
				"ADD_MOCK_IP").toString());
		MOCK_IP = PropertiesConfig.getValue("sentinel", "MOCK_IP").toString();
		
		DIAMOND_ID = PropertiesConfig.getValue("sentinel", "DIAMOND_ID").toString();
		DATABASE_NAME = PropertiesConfig.getValue("sentinel", "DATABASE_NAME").toString();
		RUN_SYNC_IP = PropertiesConfig.getValue("sentinel", "RUN_SYNC_IP").toString();

		logger.info("load sentinel config finished....");
	}
	
	public static void main(String [] args) {
		
	}

}
