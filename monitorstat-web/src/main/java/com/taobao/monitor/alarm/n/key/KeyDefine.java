
package com.taobao.monitor.alarm.n.key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;



/**
 * key的告警定义，此类保存有key的所有信息
 * @author xiaodu
 * @version 2011-2-25 上午10:25:43
 */
public class KeyDefine {
	private static final Logger log = Logger.getLogger(KeyDefine.class);
	private int appId;
	
	private String appName;
	
	private int keyId;
	
	private String keyName;
	
	private String keyAliasName;
	
	private int keyType;
	
	private int keyJudgeType;
	
	private List<RangeDefine> rangeList = new ArrayList<RangeDefine>();//阀值的范围集合
	
	private Map<Integer,List<RangeDefine>> hostRangeMap = new HashMap<Integer, List<RangeDefine>>();//保存一些机器 额外不同的配置
	
	/***
	 * 新加了一种alarmType 编号为3
	 * @param define
	 * @param alarmType
	 * @return
	 */
	private List<RangeDefine> parseAllRangeDefine(String define, String alarmType) {
		if (alarmType != null && alarmType.trim().equals("3")) {
			try {
				List<RangeDefine> alarmRangeList = new ArrayList<RangeDefine>();
				String[] alarmArray =  define.split(";");
				for (String alarm : alarmArray) {
					RangeDefine object = new RangeDefine();

					String[] ratioArray = alarm.split("\\$");
					object.setGreaterthan(Double.parseDouble(ratioArray[0]));
					object.setLessthan(Double.parseDouble(ratioArray[1]));

					alarmRangeList.add(object);
				}
				
				return alarmRangeList.size() == 0 ? null : alarmRangeList;
			} catch (Exception e) {
				log.warn(e);
			}
			return null;
		}if(alarmType != null && "4".equals(alarmType.trim())){
			return parseConstantRangeDefind(define);
		}else {
			return parseRangeDefine(define);
		}
	}
	
	private List<RangeDefine> parseConstantRangeDefind(String define) {

		try{
			List<RangeDefine> alarmRangeList = new ArrayList<RangeDefine>();
			String[] alarmArray =  define.split(";");
			for (String alarm : alarmArray) {
				RangeDefine def = new RangeDefine();
				
				String[] ratioArray = alarm.split("\\$");
				def.setEqualValue(Double.parseDouble(ratioArray[0]));
				
				String[] _timeRange = ratioArray[1].split("\\#");
				def.setStartTime(Integer.parseInt(_timeRange [0].replaceAll(":", "")));
				def.setEndTime(Integer.parseInt(_timeRange[1].replaceAll(":", "")));
				
				alarmRangeList.add(def);
			}
			return alarmRangeList.size() == 0 ? null : alarmRangeList;
		}catch(Exception e){
			log.warn(e);
		}
		return null ;
	}

	private List<RangeDefine> parseRangeDefine(String d){
		
		try{
			
			List<RangeDefine> alarmRangeList = new ArrayList<RangeDefine>();
			
			String[] alarmArray =  d.split(";");	
			for(String alarm:alarmArray){
				String[] ranges = alarm.split("\\$");
				if(ranges.length!=2){
					continue;
				}
				String dataRange = ranges[0];
				String timeRange = ranges[1];
				
				String[] _dataRange = dataRange.split("\\#"); 
				String[] _timeRange = timeRange.split("\\#");
				if(_dataRange.length!=2||_timeRange.length!=2){
					continue;
				}
				
				RangeDefine object = new RangeDefine();
				object.setStartTime(Integer.parseInt(_timeRange[0].replaceAll(":", "")));
				object.setEndTime(Integer.parseInt(_timeRange[1].replaceAll(":", "")));
				object.setGreaterthan(Double.parseDouble(_dataRange[0]));
				object.setLessthan(Double.parseDouble(_dataRange[1]));				
				alarmRangeList.add(object);
			}
			
			return alarmRangeList.size()==0?null:alarmRangeList;
		}catch(Exception e){
			log.warn(e);
		}
		
		return null;
	}
	
	
	/**
	 * 解析通用的范围定义
	 * @param define
	 */
	public void parseCommonRangeDefine(String define, String alarmType){
		List<RangeDefine> l = parseAllRangeDefine(define, alarmType);
		
		rangeList.clear();
		if(l!=null)
			rangeList.addAll(l);
		
		
	}
	/**
	 * 解析额外 机器的范围定义
	 * @param define
	 */
	public void parseHostRangeDefine(int hostid,String define){
		
		List<RangeDefine> list = parseRangeDefine(define);
		if(list!=null&&list.size()>0){
			hostRangeMap.put(hostid, list);
		}
		
	}


	public int getAppId() {
		return appId;
	}


	public void setAppId(int appId) {
		this.appId = appId;
	}


	public String getAppName() {
		return appName;
	}


	public void setAppName(String appName) {
		this.appName = appName;
	}


	public int getKeyId() {
		return keyId;
	}


	public void setKeyId(int keyId) {
		this.keyId = keyId;
	}


	public String getKeyName() {
		return keyName;
	}


	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}


	public String getKeyAliasName() {
		return keyAliasName;
	}


	public void setKeyAliasName(String keyAliasName) {
		this.keyAliasName = keyAliasName;
	}


	public List<RangeDefine> getRangeList() {
		return rangeList;
	}


	public void setRangeList(List<RangeDefine> rangeList) {
		this.rangeList = rangeList;
	}


	public Map<Integer, List<RangeDefine>> getHostRangeMap() {
		return hostRangeMap;
	}


	public void setHostRangeMap(Map<Integer, List<RangeDefine>> hostRangeMap) {
		this.hostRangeMap = hostRangeMap;
	}


	public int getKeyType() {
		return keyType;
	}


	public void setKeyType(int keyType) {
		this.keyType = keyType;
	}


	public int getKeyJudgeType() {
		return keyJudgeType;
	}


	public void setKeyJudgeType(int keyJudgeType) {
		this.keyJudgeType = keyJudgeType;
	}
	
	


}
