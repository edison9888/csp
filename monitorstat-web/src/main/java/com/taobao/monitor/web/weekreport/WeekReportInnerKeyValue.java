
package com.taobao.monitor.web.weekreport;


/**
 * 
 * @author xiaodu
 * @version 2011-2-14 обнГ01:26:50
 */
public class WeekReportInnerKeyValue implements Comparable<WeekReportInnerKeyValue>{
	public String keyName;
	public Double value;
	
	public boolean equals(Object obj) {
		if(obj instanceof WeekReportInnerKeyValue){
			WeekReportInnerKeyValue key = (WeekReportInnerKeyValue)obj;
			if(keyName.equals(key.keyName)){
				return true;
			}
		}
		return false;
	}
	
	
	public int compareTo(WeekReportInnerKeyValue o) {			
		if(value<(o.value)){
			return 1;
		}else if(value==(o.value)){
			return 0;
		}else if(value>(o.value)){
			return 0;
		}			
		return 0;
	}
	
}