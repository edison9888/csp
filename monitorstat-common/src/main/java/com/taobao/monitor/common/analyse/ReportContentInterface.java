
package com.taobao.monitor.common.analyse;


/**
 * 
 * @author xiaodu
 * @version 2010-4-26 обнГ03:49:34
 */
public interface ReportContentInterface {
	public void putReportDataEx(int appId,String hostsite,String keyValue,String valueData,String message,String collectTime);
	public void putReportLimit(int appId,String hostsite,String keyValue,String valueData,String collectTime);
}
