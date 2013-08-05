
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.csp.time.web.po.SortEntry;
import com.taobao.csp.time.web.po.TimeDataInfo;

/**
 * @author xiaodu
 *
 * ����9:06:11
 */
public interface CommonServiceInterface {
	/**
	 * ��ȡӦ�õ�key ��������key
	 *@author xiaodu
	 * @param appName
	 * @param parentKey
	 * @return
	 *TODO
	 */
	public List<String> childKeyList(String appName,String parentKey) ;
	
	/**
	 * ��ȡkey�����л�����ƽ��ֵ
	 *@author xiaodu
	 * @param appName
	 * @param keyName
	 * @param mainProp 
	 * @return Map<String, Float> ��������ӵ�����ƽ��ֵ
	 *TODO
	 */
	public Map<String,Float>  queryAverageKeyDataByHost(String appName,String keyName,String mainProp);
	
	
	/**
	 * ��ȡӦ��ĳ��key��ȫ��ƽ��ֵ,�����ջ�������
	 *@author xiaodu
	 * @param appName
	 * @param keyName
	 * @param mainProp 
	 * @returnMap<siteName,Map<ftime,TimeDataInfo>> ��������ӵ�����ƽ��ֵ
	 *TODO
	 */
	public Map<String,Map<String,TimeDataInfo>>  queryAverageKeyDataByHostForSite(String appName,
			String keyName,String mainProp);
	
	
	/**
	 * ��ѯĳ��key����ʷ����
	 *@author xiaodu
	 * @param appName Ӧ������
	 * @param key key������
	 * @param mainProp ����������
	 * @param date
	 * @return
	 *TODO
	 */
	public List<TimeDataInfo> queryKeyDataHistory(String appName, String key,String mainProp,Date date);
	
	/**
	 * ��ѯĳ��ip��key����ʷ����
	 *@author xiaodu
	 * @param appName Ӧ������
	 * @param key key������
	 * @param mainProp ����������
	 * @param ip ��ѯĿ�������ip
	 * @param date
	 * @return
	 *TODO
	 */
	public List<TimeDataInfo> queryKeyDataHistory(String appName, String key,String mainProp,String ip,Date date);
	
	
	/**
	 * ��ѯĳ��key �����л����ϵ�������Ϣ
	 *@author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp �����ԣ���������չʾ������
	 * @return  Map<ip,List<TimeDataInfo>> list ������������ý���
	 *TODO
	 */
	public  Map<String,List<TimeDataInfo>> querykeyDataForHost(String appName, String key,
			String mainProp,boolean isSecond);
	
	
	/**
	 * ��ѯĳ��key �����л����ϵ�������Ϣ
	 *@author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp �����ԣ���������չʾ������
	 * @return  List<SortEntry<TimeDataInfo>> ������������ý���
	 *TODO
	 */
	public List<SortEntry<TimeDataInfo>> querykeyDataForHostBySort(String appName, String key,
			String mainProp,boolean isSecond);
	
	
	public List<SortEntry<TimeDataInfo>> querykeyDataForHostBySort(String appName, String key,
			String mainProp);
	
	
	/**
	 * ȡ��ĳ����key �����������key������
	 *@author xiaodu
	 * @param appName
	 * @param key ��key����
	 * @param mainProp �����ԣ���������չʾ������
	 * @return Map<��key����,List<TimeDataInfo>> List<TimeDataInfo> ��ʾ����ļ���ֵ
	 *TODO
	 */
	public  Map<String,List<TimeDataInfo>> querykeyDataForChild(String appName, String key,String mainProp);
	
	
	
	
	/**
	 * ȡ��ĳ����key �����������key������
	 *@author xiaodu
	 * @param appName
	 * @param key ��key����
	 * @param mainProp �����ԣ���������չʾ������
	 * @return
	 *TODO
	 */
	public  List<SortEntry<TimeDataInfo>> querykeyDataForChildBySort(String appName, String key,String mainProp);
	
	
	/**
	 * ȡ��key�����������һ�ε�����
	 *@author xiaodu
	 * @param appName
	 * @param key ��key����
	 * @param mainProp �����ԣ���������չʾ������
	 * @return List<TimeDataInfo>
	 *TODO
	 */
	public  List<TimeDataInfo> querykeyRecentlyDataForHostBySort(String appName, String key
			,String mainProp);
	
	
	/**
	 * ȡ�ø�key��������key���һ�ε�����
	 *@author xiaodu
	 * @param appName
	 * @param key ��key����
	 * @param mainProp �����ԣ���������չʾ������
	 * @return List<TimeDataInfo>
	 *TODO
	 */
	public  List<TimeDataInfo> querykeyRecentlyDataForChildBySort(String appName, String key,String mainProp);
	
	
	/**
	 * ��ѯӦ������ȡ����ЩӦ�ö�Ӧ��key������ �����������Ӧ�ü���
	 *@author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp �����ԣ���������չʾ������
	 * @return  Map<appName,List<TimeDataInfo>> list ������������ý���
	 *TODO
	 */
	public  Map<String,List<TimeDataInfo>> querykeyDataForApps(List<String> appName, String key,String mainProp);
	

	/**
	 * ��ѯĳ̨������ȡ��ĳ����key �����������key������
	 *@author xiaodu
	 * @param appName
	 * @param key ��key����
	 * @param mainProp �����ԣ���������չʾ������
	 * @param ip ����IP
	 * @return Map<��key����,List<TimeDataInfo>> List<TimeDataInfo> ��ʾ����ļ���ֵ
	 *TODO
	 */
	public  Map<String,List<TimeDataInfo>> querykeyDataForChild(String appName, String key,String mainProp,String ip);
	
	
	/**
	 * ��ѯĳ̨������ȡ��ĳ����key �����������key������
	 *@author xiaodu
	 * @param appName
	 * @param key ��key����
	 * @param mainProp �����ԣ���������չʾ������
	 * @return
	 *TODO
	 */
	public List<SortEntry<TimeDataInfo>> querykeyDataForChildBySort(String appName, String key, String mainProp,String ip) ;
	
	/**
	 *��ѯĳ̨������ ȡ�ø�key��������key���һ�ε�����
	 *@author xiaodu
	 * @param appName
	 * @param key ��key����
	 * @param mainProp �����ԣ���������չʾ������
	 * @return List<TimeDataInfo>
	 *TODO
	 */
	public  List<TimeDataInfo> querykeyRecentlyDataForChildBySort(String appName, String key,String mainProp,String ip);
	/**
	 * ��ѯĳ��key��������Ϣ
	 *@author xiaodu
	 * @param appName Ӧ������
	 * @param key key������
	 * @param mainProp ����������
	 * @param date
	 * @return
	 *TODO
	 */
	public List<TimeDataInfo> querySingleKeyData(String appName, String key,String mainProp);

	public List<TimeDataInfo> querySingleKeyData(String appName, String key,String mainProp,boolean isSecond);

	/**
	 * ��ѯĳ��key��������Ϣ
	 *@author xiaodu
	 * @param appName Ӧ������
	 * @param key key������
	 * @param mainProp ����������
	 * @param date
	 * @return
	 *TODO
	 */
	public TimeDataInfo querySingleRecentlyKeyData(String appName, String key,String mainProp);
	
	/**
	 * ��ѯĳ��key��������Ϣ
	 *@author xiaodu
	 * @param appName Ӧ������
	 * @param key key������
	 * @param mainProp ����������
	 * @param ip ����ip
	 * @param date
	 * @return
	 *TODO
	 */
	public List<TimeDataInfo> querySingleKeyData(String appName, String key,String mainProp,String ip);
	
	/**
	 * ��ѯ���key�� �������
	 *@author xiaodu
	 * @param appName
	 * @param keys
	 * @param mainProp
	 * @param ip
	 * @return
	 *TODO
	 */
	public Map<String,List<TimeDataInfo>> queryMutilKeyData(String appName, List<String> keys,String mainProp);
	public List<SortEntry<TimeDataInfo>> queryMutilKeyDataBySort(
			String appName, List<String> keys, String mainProp);
}
