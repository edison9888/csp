package com.taobao.csp.monitor.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CollectFlag {

	//是否全写到storm
	public static volatile boolean IS_ALL_STORM=false;
	//是否写到storm
	public static volatile boolean IS_STORM=false;
	//写到storm的应用列表
	public static Map<String, Integer> storms=new ConcurrentHashMap<String, Integer>();
	
}
