
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.script;

import java.util.Map;

/**
 * @author xiaodu
 *
 * ����6:18:47
 */
public interface ScriptAnalyse {
	

	/**
	 * ������Ҫ�������ݣ����ݽ���һ��ʱ�����������
	 * 
	 * @param time yyyy-MM-dd HH:mm
	 * @param key
	 * @param value
	 */
	public void putCountData(String time,String title,String key,long value);
	/**
	 * ������Ҫƽ�������ݣ����ݽ���һ��ʱ�������ƽ��������
	 * @param time yyyy-MM-dd HH:mm
	 * @param key
	 * @param value
	 */
	public void putAverageData(String time,String title,String key,double value);
	
	/**
	 * �����ı���Ϣ������ı���Ϣֻ�ܱ���һ��ʱ������һ����Ϣ��Ϣ������������
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
	 * ִ����������
	 */
	public  void doAnalyse();
		
	/**
	 * ��������������һ����¼,����������һ�첢���Դ���ͱ�����Ϣ
	 * @param line
	 */
	public  void analyseOneLine(String line);
	

}
