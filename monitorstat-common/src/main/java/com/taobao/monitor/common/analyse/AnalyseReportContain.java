package com.taobao.monitor.common.analyse;

public interface AnalyseReportContain {
	
	public void putReportDataLong(String keyValue,long valueData,String collectTime);
	public void putReportDataDouble(String keyValue,double valueData,String collectTime);
	public void putReportDataString(String keyValue,String valueData,String collectTime);
	public void putReportDataEx(String keyValue,long valueData,String message,String collectTime);
	public void putFinal();
	

}
