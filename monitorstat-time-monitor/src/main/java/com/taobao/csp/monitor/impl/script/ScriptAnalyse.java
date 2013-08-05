
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.script;

import java.util.Map;

/**
 * @author xiaodu
 *
 * 下午6:18:47
 */
public interface ScriptAnalyse {
	

	/**
	 * 输入需要汇总数据，数据将在一个时间点内做汇总
	 * 
	 * @param time yyyy-MM-dd HH:mm
	 * @param key
	 * @param value
	 */
	public void putCountData(String time,String title,String key,long value);
	/**
	 * 输入需要平均的数据，数据将在一个时间点内做平均的数据
	 * @param time yyyy-MM-dd HH:mm
	 * @param key
	 * @param value
	 */
	public void putAverageData(String time,String title,String key,double value);
	
	/**
	 * 输入文本信息，这个文本信息只能保存一个时间点最后一条信息信息，其它都作废
	 * @param time yyyy-MM-dd HH:mm
	 * @param key
	 * @param value
	 */
	public void putTextData(String time,String title,String key,String value);
	
	/**
	 * 
	 * @return
	 */
	public Map<String,Map<String, Long>> getCountKeyValue();
	
	public Map<String,Map<String, Double>> getAverageKeyValue();
	
	public Map<String,Map<String, String>> getTextKeyValue();
	
	
	/**
	 * 执行整个分析
	 */
	public  void doAnalyse();
		
	/**
	 * 往分析器里输入一条记录,分析器接收一天并加以处理和保存信息
	 * @param line
	 */
	public  void analyseOneLine(String line);
	

}
