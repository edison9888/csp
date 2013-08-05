
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
 * 下午9:06:11
 */
public interface CommonServiceInterface {
	/**
	 * 获取应用的key 的所有子key
	 *@author xiaodu
	 * @param appName
	 * @param parentKey
	 * @return
	 *TODO
	 */
	public List<String> childKeyList(String appName,String parentKey) ;
	
	/**
	 * 获取key的所有机器的平均值
	 *@author xiaodu
	 * @param appName
	 * @param keyName
	 * @param mainProp 
	 * @return Map<String, Float> 最近几分钟的所有平均值
	 *TODO
	 */
	public Map<String,Float>  queryAverageKeyDataByHost(String appName,String keyName,String mainProp);
	
	
	/**
	 * 获取应用某个key的全网平均值,并按照机房区分
	 *@author xiaodu
	 * @param appName
	 * @param keyName
	 * @param mainProp 
	 * @returnMap<siteName,Map<ftime,TimeDataInfo>> 最近几分钟的所有平均值
	 *TODO
	 */
	public Map<String,Map<String,TimeDataInfo>>  queryAverageKeyDataByHostForSite(String appName,
			String keyName,String mainProp);
	
	
	/**
	 * 查询某个key的历史数据
	 *@author xiaodu
	 * @param appName 应用名称
	 * @param key key的名称
	 * @param mainProp 主属性名称
	 * @param date
	 * @return
	 *TODO
	 */
	public List<TimeDataInfo> queryKeyDataHistory(String appName, String key,String mainProp,Date date);
	
	/**
	 * 查询某个ip的key的历史数据
	 *@author xiaodu
	 * @param appName 应用名称
	 * @param key key的名称
	 * @param mainProp 主属性名称
	 * @param ip 查询目标机器的ip
	 * @param date
	 * @return
	 *TODO
	 */
	public List<TimeDataInfo> queryKeyDataHistory(String appName, String key,String mainProp,String ip,Date date);
	
	
	/**
	 * 查询某个key 在所有机器上的数据信息
	 *@author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return  Map<ip,List<TimeDataInfo>> list 主属性排序采用降序
	 *TODO
	 */
	public  Map<String,List<TimeDataInfo>> querykeyDataForHost(String appName, String key,
			String mainProp,boolean isSecond);
	
	
	/**
	 * 查询某个key 在所有机器上的数据信息
	 *@author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return  List<SortEntry<TimeDataInfo>> 主属性排序采用降序
	 *TODO
	 */
	public List<SortEntry<TimeDataInfo>> querykeyDataForHostBySort(String appName, String key,
			String mainProp,boolean isSecond);
	
	
	public List<SortEntry<TimeDataInfo>> querykeyDataForHostBySort(String appName, String key,
			String mainProp);
	
	
	/**
	 * 取得某个父key 下面的所有子key的数据
	 *@author xiaodu
	 * @param appName
	 * @param key 父key名称
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return Map<子key名称,List<TimeDataInfo>> List<TimeDataInfo> 表示最近的几个值
	 *TODO
	 */
	public  Map<String,List<TimeDataInfo>> querykeyDataForChild(String appName, String key,String mainProp);
	
	
	
	
	/**
	 * 取得某个父key 下面的所有子key的数据
	 *@author xiaodu
	 * @param appName
	 * @param key 父key名称
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return
	 *TODO
	 */
	public  List<SortEntry<TimeDataInfo>> querykeyDataForChildBySort(String appName, String key,String mainProp);
	
	
	/**
	 * 取得key所有主机最近一次的数据
	 *@author xiaodu
	 * @param appName
	 * @param key 父key名称
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return List<TimeDataInfo>
	 *TODO
	 */
	public  List<TimeDataInfo> querykeyRecentlyDataForHostBySort(String appName, String key
			,String mainProp);
	
	
	/**
	 * 取得父key的所有子key最近一次的数据
	 *@author xiaodu
	 * @param appName
	 * @param key 父key名称
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return List<TimeDataInfo>
	 *TODO
	 */
	public  List<TimeDataInfo> querykeyRecentlyDataForChildBySort(String appName, String key,String mainProp);
	
	
	/**
	 * 查询应用类表获取到这些应用对应的key的数据 ，这个级别是应用级别
	 *@author xiaodu
	 * @param appName
	 * @param key
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return  Map<appName,List<TimeDataInfo>> list 主属性排序采用降序
	 *TODO
	 */
	public  Map<String,List<TimeDataInfo>> querykeyDataForApps(List<String> appName, String key,String mainProp);
	

	/**
	 * 查询某台机器下取得某个父key 下面的所有子key的数据
	 *@author xiaodu
	 * @param appName
	 * @param key 父key名称
	 * @param mainProp 主属性，用来排序展示的属性
	 * @param ip 主机IP
	 * @return Map<子key名称,List<TimeDataInfo>> List<TimeDataInfo> 表示最近的几个值
	 *TODO
	 */
	public  Map<String,List<TimeDataInfo>> querykeyDataForChild(String appName, String key,String mainProp,String ip);
	
	
	/**
	 * 查询某台机器下取得某个父key 下面的所有子key的数据
	 *@author xiaodu
	 * @param appName
	 * @param key 父key名称
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return
	 *TODO
	 */
	public List<SortEntry<TimeDataInfo>> querykeyDataForChildBySort(String appName, String key, String mainProp,String ip) ;
	
	/**
	 *查询某台机器下 取得父key的所有子key最近一次的数据
	 *@author xiaodu
	 * @param appName
	 * @param key 父key名称
	 * @param mainProp 主属性，用来排序展示的属性
	 * @return List<TimeDataInfo>
	 *TODO
	 */
	public  List<TimeDataInfo> querykeyRecentlyDataForChildBySort(String appName, String key,String mainProp,String ip);
	/**
	 * 查询某个key的流量信息
	 *@author xiaodu
	 * @param appName 应用名称
	 * @param key key的名称
	 * @param mainProp 主属性名称
	 * @param date
	 * @return
	 *TODO
	 */
	public List<TimeDataInfo> querySingleKeyData(String appName, String key,String mainProp);

	public List<TimeDataInfo> querySingleKeyData(String appName, String key,String mainProp,boolean isSecond);

	/**
	 * 查询某个key的流量信息
	 *@author xiaodu
	 * @param appName 应用名称
	 * @param key key的名称
	 * @param mainProp 主属性名称
	 * @param date
	 * @return
	 *TODO
	 */
	public TimeDataInfo querySingleRecentlyKeyData(String appName, String key,String mainProp);
	
	/**
	 * 查询某个key的流量信息
	 *@author xiaodu
	 * @param appName 应用名称
	 * @param key key的名称
	 * @param mainProp 主属性名称
	 * @param ip 机器ip
	 * @param date
	 * @return
	 *TODO
	 */
	public List<TimeDataInfo> querySingleKeyData(String appName, String key,String mainProp,String ip);
	
	/**
	 * 查询多个key的 最近数据
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
