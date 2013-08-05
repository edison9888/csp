
package com.taobao.monitor.stat.content;

import java.util.Map;

/**
 * 
 * @author xiaodu
 * @version 2010-4-26 ÏÂÎç03:49:34
 */
public interface ReportContentInterface {
	public void putReportData(String appName,Map<String,Long> data,String collectTime);
	public void putReportData(String appName,String keyValue,String valueData,String collectTime);
	public void putReportData(String appName,String keyValue,Long valueData,String collectTime);
	
	public void putReportDataByCount(String appName,String key,String value,String collectDate);
	public void putReportDataByCount1(String appName,Map<String,Float> data,String collectDate);
	public void putReportDataByCount(String appName,Map<String,Long> data,String collectDate);
	public void putReportDataByCount(String appName,String key,Integer value,String collectDate);
	public void putReportDataByCount(String appName,String key,Long value,String collectDate);
	
	public void putReportDataByProvider(String appName,String key,String cm_ip,Long value,Double time,String collectDate);
	
	public void putReportDataByCustomer(String customerName,String providerName,String key,String machineName,Long value,Double time,String collectDate);
	

}
