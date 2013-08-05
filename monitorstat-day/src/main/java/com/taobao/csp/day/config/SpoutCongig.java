package com.taobao.csp.day.config;

import java.util.HashMap;
import java.util.Map;

import com.taobao.csp.day.base.DataType;

/***
 * 配置日志类型对应的spout个数
 * @author youji.zj
 * @version 1.0 2012-08-31
 *
 */
public class SpoutCongig {
	
	private static Map<DataType, Integer> config = new HashMap<DataType, Integer>();
	
	static {
		config.put(DataType.TDDL, 3);
		config.put(DataType.SPH, 2);
		config.put(DataType.TDOD, 2);
		config.put(DataType.APACHE_SPECIAL, 3);
		config.put(DataType.PINKIE_ACCESS, 14);
		config.put(DataType.GC, 3);
	}
	
	public static int getTddlSpoutNum() {
		int num = 2;
		if (config.containsKey(DataType.TDDL)) {
			num = config.get(DataType.TDDL);
		}
		return num;
	}
	
	public static void setTddlSpoutNum(int num) {
		config.put(DataType.TDDL, num);
	}
	
	public static int getSphSpoutNum() {
		int num = 2;
		if (config.containsKey(DataType.SPH)) {
			num = config.get(DataType.SPH);
		}
		return num;
	}
	
	public static int getApacheRequestSpoutNum() {
		int num = 2;
		if (config.containsKey(DataType.APACHE_SPECIAL)) {
			num = config.get(DataType.APACHE_SPECIAL);
		}
		return num;
	}
	
	public static int getTdodSpoutNum() {
		int num = 2;
		if (config.containsKey(DataType.TDOD)) {
			num = config.get(DataType.TDOD);
		}
		return num;
	}
	
	public static int getPinkieAccessSpoutNum() {
		int num = 2;
		if (config.containsKey(DataType.PINKIE_ACCESS)) {
			num = config.get(DataType.PINKIE_ACCESS);
		}
		return num;
	}
	
	public static int getGcSpoutNum() {
		int num = 2;
		if (config.containsKey(DataType.GC)) {
			num = config.get(DataType.GC);
		}
		return num;
	}

}
