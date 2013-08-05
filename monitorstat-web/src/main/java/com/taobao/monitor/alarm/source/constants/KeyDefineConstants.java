package com.taobao.monitor.alarm.source.constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class KeyDefineConstants {
	public static List<String> keyDefineSuffixList =   new ArrayList<String>();
	public static String keyDefinePrefix  = "OTHER_TC-交易相关_";
	
	public static String tairArea  = "AREA";
	public static List<String> tairKeyDefineSuffixList =   new ArrayList<String>();
	public static HashSet<Integer> gropu3TairAreaList = new HashSet<Integer>();
	
	static{
		keyDefineSuffixList.add("-FAILPERCENT_AVERAGEUSERTIMES");
		keyDefineSuffixList.add("-KEY-COST_AVERAGEUSERTIMES");
		keyDefineSuffixList.add("-THREADCOUNT_AVERAGEUSERTIMES");
		
		gropu3TairAreaList.add(90);
		gropu3TairAreaList.add(81);
		gropu3TairAreaList.add(83);
		gropu3TairAreaList.add(82);
		gropu3TairAreaList.add(76);
		gropu3TairAreaList.add(78);
		gropu3TairAreaList.add(89);
		gropu3TairAreaList.add(91);
		gropu3TairAreaList.add(75);
		
		
		
		tairKeyDefineSuffixList.add("_DATA_SIZE");
		tairKeyDefineSuffixList.add("_EVICT_COUNT");
		tairKeyDefineSuffixList.add("_GET_COUNT");
		tairKeyDefineSuffixList.add("_HIT_COUNT");
		tairKeyDefineSuffixList.add("_HIT_RATE");
		tairKeyDefineSuffixList.add("_ITEM_COUNT");
		tairKeyDefineSuffixList.add("_PUT_COUNT");
		tairKeyDefineSuffixList.add("_QUATA");
		tairKeyDefineSuffixList.add("_REMOVE_COUNT");
		tairKeyDefineSuffixList.add("_USE_SIZE");
	}
	
	public static List<String> getStandardKeyNameList(String keyNameInLog){
		List<String> keyNameList = new ArrayList<String>();
		for(String keyDefine:KeyDefineConstants.keyDefineSuffixList){
			keyNameList.add(KeyDefineConstants.keyDefinePrefix + keyNameInLog + keyDefine);
		}
		return keyNameList;
		
	}
}
