
package com.taobao.monitor.stat.newanalyse.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.stat.newanalyse.FetchData;

/**
 * <logFile name="app-" recordSeparator = "\002" fieldSeparator = "\001" fieldList="key1,key2" 
sum_name1="key1" sum_name2="key2" average_name3="name1/name2"  averageMachine_name4="name1"
indb="PV:name1;OUT_HSF_${key1}_${key2}:name3"
/>
 * @author xiaodu
 * @version 2010-5-10 ÉÏÎç09:51:52
 */
public class AnalyseFileLog {
	
	private String appName;
	
	private String logName;
	
	private char recordSeparator;
	
	private String fieldSeparator;
	
	private FetchData fetchData;
	
	
	private List<String> fieldList = new ArrayList<String>();	
	private Map<String,String> fieldMap = new HashMap<String, String>();
	private Map<String,Integer> fieldIndexMap = new HashMap<String, Integer>();
	
	
	private List<String> sumList;
	private Map<String,SumBo> sumMap;
	
	private List<String> averageList;
	private Map<String,String> averageMap;
	
	
	private List<String> averageMachineList;
	private Map<String,String> averageMachineMap;
	
	private List<String> indbList;
	
	private String collectTimePattern;
	
	private String collectTimeFormat;
	
	
	
	
	
	
	public String getCollectTimePattern() {
		return collectTimePattern;
	}

	public void setCollectTimePattern(String collectTimePattern) {
		this.collectTimePattern = collectTimePattern;
	}

	public String getCollectTimeFormat() {
		return collectTimeFormat;
	}

	public void setCollectTimeFormat(String collectTimeFormat) {
		this.collectTimeFormat = collectTimeFormat;
	}

	
	public List<String> getIndbList() {
		return indbList;
	}

	public void setIndbList(List<String> indbList) {
		this.indbList = indbList;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public char getRecordSeparator() {
		return recordSeparator;
	}

	public void setRecordSeparator(char recordSeparator) {
		this.recordSeparator = recordSeparator;
	}

	public String getFieldSeparator() {
		return fieldSeparator;
	}

	public void setFieldSeparator(String fieldSeparator) {
		this.fieldSeparator = fieldSeparator;
	}

	public List<String> getFieldList() {
		return fieldList;
	}

	public void setFieldList(List<String> fieldList) {
		this.fieldList = fieldList;
	}

	public Map<String, String> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(Map<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public List<String> getSumList() {
		return sumList;
	}

	public void setSumList(List<String> sumList) {
		this.sumList = sumList;
	}

	public List<String> getAverageList() {
		return averageList;
	}

	public void setAverageList(List<String> averageList) {
		this.averageList = averageList;
	}


	public List<String> getAverageMachineList() {
		return averageMachineList;
	}

	public void setAverageMachineList(List<String> averageMachineList) {
		this.averageMachineList = averageMachineList;
	}

	public Map<String, SumBo> getSumMap() {
		return sumMap;
	}

	public void setSumMap(Map<String, SumBo> sumMap) {
		this.sumMap = sumMap;
	}

	public Map<String, String> getAverageMap() {
		return averageMap;
	}

	public void setAverageMap(Map<String, String> averageMap) {
		this.averageMap = averageMap;
	}

	public Map<String, String> getAverageMachineMap() {
		return averageMachineMap;
	}

	public void setAverageMachineMap(Map<String, String> averageMachineMap) {
		this.averageMachineMap = averageMachineMap;
	}

	public Map<String, Integer> getFieldIndexMap() {
		return fieldIndexMap;
	}

	public void setFieldIndexMap(Map<String, Integer> fieldIndexMap) {
		this.fieldIndexMap = fieldIndexMap;
	}

	public FetchData getFetchData() {
		return fetchData;
	}

	public void setFetchData(FetchData fetchData) {
		this.fetchData = fetchData;
	}
	
	
}
