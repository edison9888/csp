
package com.taobao.monitor.common.vo;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.common.util.Utlitites;

/**
 * 
 * @author xiaodu
 * @version 2010-4-10 下午02:25:15
 */
public class HsfPo implements Comparable<HsfPo>{
	
	private String name;
	
	private Integer countkeyId;
	private Integer averageKeyId;
	
	private Integer bizCountkeyId;
	private Integer excCountkeyId;
	
	private String aim;
	
	private String className;
	
	private String methodName;
	
	private String exeCount;	
	private long exeCountNum;
	
	private String exeCountBaseLine; // 执行次数的baseLine数据	
	private String averageExeBaseLine; // 平均执行时间的baseLine数据
	
	
	/** 总计(总的响应时间) add by tom 2010-5-17 15:18 */
	private Double totalRespTime;

	/** 总计(总的响应次数) add by tom 2010-5-17 15:18 */
	private Double totalRespNum;



	public String getExeCountBaseLine() {
		return exeCountBaseLine;
	}

	public void setExeCountBaseLine(String exeCountBaseLine) {
		this.exeCountBaseLine = exeCountBaseLine;
	}

	public String getAverageExeBaseLine() {
		return averageExeBaseLine;
	}

	public void setAverageExeBaseLine(String averageExeBaseLine) {
		this.averageExeBaseLine = averageExeBaseLine;
	}

	public Double getTotalRespTime() {
		return totalRespTime;
	}

	public void setTotalRespTime(Double totalRespTime) {
		this.totalRespTime = totalRespTime;
	}

	public Double getTotalRespNum() {
		return totalRespNum;
	}

	public Double getAvgRespTime() {
		if(totalRespTime == null || totalRespNum == null){
			return null;
		}
		else if(totalRespTime ==0 || totalRespNum ==0){
			return 0D;
		}else
			return totalRespTime/totalRespNum;
	}
	
	public void setTotalRespNum(Double totalRespNum) {
		this.totalRespNum = totalRespNum;
	}

	private String collectTime;
	private Map<String,Long> exeCountNumMap = new HashMap<String, Long>();//区分版本
	
	
	private String averageExe;	
	private Map<String,Double> averageExeMap = new HashMap<String, Double>();//区分版本
	
	private String bizExecptionNum;	
	private Map<String,Long> bizExecptionNumMap = new HashMap<String, Long>();//区分版本
	
	private String execptionNum;
	private Map<String,Long> execptionNumMap = new HashMap<String, Long>();//区分版本
	
	public String getHtmlMethodName() {				
		return Utlitites.formatHtmlStr(this.methodName, 40);
	}
	
	public String getHtmlClassName() {			
		return Utlitites.formatHtmlStr(this.className, 40);
	}

	public String getAim() {
		return aim;
	}

	public void setAim(String aim) {
		this.aim = aim;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getExeCount() {
		
		if(exeCountNumMap.size()>1){
			long c = 0;
			for(Long l : exeCountNumMap.values()){
				c+=l;
			}
			return c+"";			
		}
		
		return exeCount;
	}

	public void setExeCount(String exeCount) {
		this.exeCount = exeCount;
	}

	public String getAverageExe() {		
		DecimalFormat df1 = new DecimalFormat("0.00");
		
		if(averageExeMap.size()>1){
			
			double c = 0;
			for(Double l :averageExeMap.values()){
				c+=l;
			}
			return df1.format(c/averageExeMap.size());
		}
		return averageExe;
	}

	public void setAverageExe(String averageExe) {
		this.averageExe = averageExe;
	}

	public String getBizExecptionNum() {		

		if(bizExecptionNumMap.size()>1){
			long c = 0;
			for(Long l :bizExecptionNumMap.values()){
				c+=l;
			}
			return c+"";
		}
		
		return bizExecptionNum;
	}

	public void setBizExecptionNum(String bizExecptionNum) {
		this.bizExecptionNum = bizExecptionNum;
	}

	public String getExecptionNum() {
		
		if(execptionNumMap.size()>1){
			long c = 0;
			for(Long l :execptionNumMap.values()){
				c+=l;
			}
			return c+"";
		}
		
		return execptionNum;
	}

	public void setExecptionNum(String execptionNum) {
		this.execptionNum = execptionNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int compareTo(HsfPo o) {
		
		if(o.getExeCountNum()==this.getExeCountNum()){
			return 0;
		}else if(o.getExeCountNum()>this.getExeCountNum()){
			return 1;
		}else if(o.getExeCountNum()<this.getExeCountNum()){
			return -1;
		}
		return 0;
	}

	public long getExeCountNum() {		
		if(exeCountNumMap.size()>1){
			long c = 0;
			for(Long l : exeCountNumMap.values()){
				c+=l;
			}
			return c;			
		}		
		return exeCountNum;
	}

	public void setExeCountNum(long exeCountNum) {
		this.exeCountNum = exeCountNum;
	}

	public Integer getCountkeyId() {
		return countkeyId;
	}

	public void setCountkeyId(Integer countkeyId) {
		this.countkeyId = countkeyId;
	}

	public Integer getAverageKeyId() {
		return averageKeyId;
	}

	public void setAverageKeyId(Integer averageKeyId) {
		this.averageKeyId = averageKeyId;
	}

	public Integer getBizCountkeyId() {
		return bizCountkeyId;
	}

	public void setBizCountkeyId(Integer bizCountkeyId) {
		this.bizCountkeyId = bizCountkeyId;
	}

	public Integer getExcCountkeyId() {
		return excCountkeyId;
	}

	public void setExcCountkeyId(Integer excCountkeyId) {
		this.excCountkeyId = excCountkeyId;
	}

	public Map<String, Long> getExeCountNumMap() {
		return exeCountNumMap;
	}

	public void setExeCountNumMap(Map<String, Long> exeCountNumMap) {
		this.exeCountNumMap = exeCountNumMap;
	}

	public Map<String, Double> getAverageExeMap() {
		return averageExeMap;
	}

	public void setAverageExeMap(Map<String, Double> averageExeMap) {
		this.averageExeMap = averageExeMap;
	}

	public Map<String, Long> getBizExecptionNumMap() {
		return bizExecptionNumMap;
	}

	public void setBizExecptionNumMap(Map<String, Long> bizExecptionNumMap) {
		this.bizExecptionNumMap = bizExecptionNumMap;
	}

	public Map<String, Long> getExecptionNumMap() {
		return execptionNumMap;
	}

	public void setExecptionNumMap(Map<String, Long> execptionNumMap) {
		this.execptionNumMap = execptionNumMap;
	}

	public String getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(String collectTime) {
		this.collectTime = collectTime;
	}


	
	
	
}
