package com.taobao.csp.monitor.impl.script;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * d:���жԻ��ܡ�ƽ�����ַ�������ʽkey����ͳ�Ƶķ���
 */
public class ScriptValueBox {
	private static final Logger logger =  Logger.getLogger(ScriptValueBox.class);

	/**
	 * map<time,Map<keyName, sumData>>
	 * ��һ��ʱ����һ��key�γɻ�������
	 */
	
	private  Map<String,Map<String, Long>> countMap = new HashMap<String, Map<String,Long>>();
	
	/**
	 * map<time,Map<keyName, averageData>>
	 * ��һ��ʱ����һ��key�γ�ƽ������
	 */
	private  Map<String,Map<String, Double>> doubleMap = new HashMap<String, Map<String,Double>>();
	
	/**
	 * Map<time, Map< 
	 */
	private  Map<String,Map<String, String>> stringMap = new HashMap<String, Map<String,String>>();
	
	
	public void release(){
		countMap.clear();
		doubleMap.clear();
		stringMap.clear();
	}
	
	/**
	 * ������Ҫ�������ݣ����ݽ���һ��ʱ�����������
	 * @author denghaichuan.pt
	 * @param time
	 * @param title ǰ׺
	 * @param key
	 * @param value
	 */
	final public void putCountData(String time, String title, String key, long value){
		if(time==null||title==null||key==null){
			return ;
		}
		String dbKey = title.trim()+"_"+key.trim();
		Map<String, Long> tmpMap = countMap.get(time);
		if(tmpMap == null){
			tmpMap = new HashMap<String, Long>();
			countMap.put(time, tmpMap);
		}
		Long count = tmpMap.get(dbKey);
		if(count == null){
			tmpMap.put(dbKey, value);
		}else{
			tmpMap.put(dbKey, value+count);
		}
		
		
	}
	/**
	 * ������Ҫƽ�������ݣ����ݽ���һ��ʱ�������ƽ��������
	 * @param time yyyy-MM-dd HH:mm
	 * @param key
	 * @param value
	 */
	final public void putAverageData(String time, String title, String key, double value){
		if(time==null||title==null||key==null){
			return ;
		}
		String dbKey = title.trim()+"_"+key.trim();
		Map<String, Double> tmpMap = doubleMap.get(time);
		if(tmpMap == null){
			tmpMap = new HashMap<String, Double>();
			doubleMap.put(time, tmpMap);
		}
		Double average = tmpMap.get(dbKey);
		if(average == null){
			tmpMap.put(dbKey, value);
		}else{
			tmpMap.put(dbKey, (value+average)/2);
		}
	}
	
	/**
	 * �����ı���Ϣ������ı���Ϣֻ�ܱ���һ��ʱ������һ����Ϣ��Ϣ������������
	 * @param time yyyy-MM-dd HH:mm
	 * @param key
	 * @param value
	 */
	final public void putTextData(String time, String title, String key, String value){
		if(time==null||title==null||key==null){
			return ;
		}
		String dbKey = title.trim()+"_"+key.trim();
		Map<String, String> tmpMap = stringMap.get(time);
		if(tmpMap == null){
			tmpMap = new HashMap<String, String>();
			stringMap.put(time, tmpMap);
		}
		tmpMap.put(dbKey, value);
		
	}
	final public Map<String, Map<String, Double>> getAverageKeyValue() {
		return doubleMap;
	}


	final public Map<String, Map<String, Long>> getCountKeyValue() {
		return countMap;
	}


	final public Map<String, Map<String, String>> getTextKeyValue() {
		return stringMap;
	}
	
	final public void log(String message) {
		logger.error(message);
	}

}
