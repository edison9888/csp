package com.taobao.monitor.alarm.source.constants;

import java.util.ArrayList;
import java.util.List;

/**
 * cart��������Ӧ�õļ�أ������Щkey�������ˡ�������Ӧ���������ϵͳ�����⡣
 * cart����Ӧ�õļ����Ҫ����
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
