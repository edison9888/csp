package com.taobao.csp.monitor.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CollectFlag {

	//�Ƿ�ȫд��storm
	public static volatile boolean IS_ALL_STORM=false;
	//�Ƿ�д��storm
	public static volatile boolean IS_STORM=false;
	//д��storm��Ӧ���б�
	public static Map<String, Integer> storms=new ConcurrentHashMap<String, Integer>();
	
}
