package com.taobao.monitor.alarm.source.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * cart对依赖的应用的监控，如果这些key出问题了。表名对应依赖的这个系统有问题。
 * cart本身应用的监控需要完善
 * @author baiyan
 *
 */
public class CartAlarmKeyConstants {
	public static List<String> cartDBKeyList  = new ArrayList<String>();
	public static List<String> uicFinalKeyList  = new ArrayList<String>();
	
	
	static{
		for(int i=0;i<16;i++){
			cartDBKeyList.add("cartDB_timeout_mysql" + i +"_AVERAGEUSERTIMES");
		}
		
	}
	
}
