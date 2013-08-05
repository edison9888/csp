package com.taobao.csp.time.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.dataserver.PropConstants;
/**
 *自动映射Po类的属性与Map中Key的映射Map
 *只会映射Map中的属性
 */
public class TransferMap {
	private Map<String,String> keyMap = new ConcurrentHashMap<String,String>();
	private static TransferMap transferMap = new TransferMap();
	private TransferMap(){
		keyMap.put("rt", PropConstants.C_TIME);
		keyMap.put("cpu", PropConstants.CPU);
		keyMap.put("sucrate", PropConstants.SUC_RATE);
		keyMap.put("num", PropConstants.E_TIMES);
		keyMap.put("exception", PropConstants.EXCEPTION);
		keyMap.put("fullgc", PropConstants.JVMFULLGC);
		keyMap.put("gc", PropConstants.JVMGC);
		keyMap.put("memory", PropConstants.JVMMEMORY);
		keyMap.put("load", PropConstants.LOAD);
		keyMap.put("swap", PropConstants.SWAP);
		keyMap.put("psize", PropConstants.P_SIZE);
		keyMap.put("etimes", PropConstants.E_TIMES);
		keyMap.put("ctime", PropConstants.C_TIME);
		keyMap.put("len", PropConstants.P_SIZE);
		keyMap.put("hit", PropConstants.SUC_RATE);
		keyMap.put("s",PropConstants.NOTIFY_C_S);
		keyMap.put("s_rt", PropConstants.NOTIFY_C_S_RT);
		keyMap.put("s_f", PropConstants.NOTIFY_C_S_F);
		keyMap.put("ra_s", PropConstants.NOTIFY_C_RA_S);
		keyMap.put("ra_s_rt", PropConstants.NOTIFY_C_RA_S_RT);
		keyMap.put("ra_f", PropConstants.NOTIFY_C_RA_F);
		keyMap.put("ws", PropConstants.NOTIFY_C_WS);
		keyMap.put("timeout",PropConstants.NOTIFY_C_TIMEOUT);
		keyMap.put("re", PropConstants.NOTIFY_C_RE);
		keyMap.put("nc", PropConstants.NOTIFY_C_NC);
		keyMap.put("time", "time");
		keyMap.put("error", "error");
	}
	public  static TransferMap getSingle(){
		return transferMap;
	}
	public String get(String key){
		return keyMap.get(key);
	}
}
