package com.taobao.csp.loadrun.web.bo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.LoadrunResultDetail;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.web.LoadRunHost;
import com.taobao.csp.loadrun.web.action.show.LoadrunReportObject;
import com.taobao.csp.loadrun.web.dao.CspLoadRunDao;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.db.impl.day.MonitorDayDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.KeyValuePo;

/**
 * 
 * @author xiaodu
 * @version 2010-9-2 上午09:40:49
 */
public class CspLoadRunBo {

	private CspLoadRunDao cspLoadRunDao ;
	
	private AppInfoDao appInfoDao ;
	
	private MonitorDayDao monitorDayDao;
	
	private Map<String, LoadrunResult> max = new ConcurrentHashMap<String, LoadrunResult>();
	
	private Map<String, LoadrunResult> min = new ConcurrentHashMap<String, LoadrunResult>();

	public MonitorDayDao getMonitorDayDao() {
		return monitorDayDao;
	}

	public void setMonitorDayDao(MonitorDayDao monitorDayDao) {
		this.monitorDayDao = monitorDayDao;
	}

	public CspLoadRunDao getCspLoadRunDao() {
		return cspLoadRunDao;
	}

	public void setCspLoadRunDao(CspLoadRunDao cspLoadRunDao) {
		this.cspLoadRunDao = cspLoadRunDao;
	}

	public AppInfoDao getAppInfoDao() {
		return appInfoDao;
	}

	public void setAppInfoDao(AppInfoDao appInfoDao) {
		this.appInfoDao = appInfoDao;
	}

	public Map<String, LoadrunResult> getMax() {
		return max;
	}

	public void setMax(Map<String, LoadrunResult> max) {
		this.max = max;
	}

	public Map<String, LoadrunResult> getMin() {
		return min;
	}

	public void setMin(Map<String, LoadrunResult> min) {
		this.min = min;
	}

	/**
	 * 删除loadRunHost
	 * 
	 * @param appId
	 */
	public boolean deleteLoadRunHost(int appId) {

		return cspLoadRunDao.deleteLoadRunHost(appId);
	}
	
	/***
	 * 删除压测结果
	 * @param loadId
	 * @return
	 */
	public boolean deleteLoadrunResultById(String loadId) {
		return cspLoadRunDao.deleteLoadrunResultById(loadId);
	}

	/**
	 * 添加压测结果
	 * 
	 * @param result
	 */
	public void addLoadRunResult(LoadrunResult result) {
		cspLoadRunDao.addLoadRunResult(result);
	}
	
	/**
	 * 添加压测结果详细信息
	 * 
	 * @param result
	 */
	public void addLoadRunResultDetail(LoadrunResultDetail result) {
		cspLoadRunDao.addLoadRunResultDetail(result);
	}
	
	/***
	 * 查找压测的详细信息
	 * @param loadrunId
	 *        压测的id
	 * @return
	 */
	public List<LoadrunResultDetail> findLoadrunResultDetail(String loadrunId, Set<String> filter) {
		return cspLoadRunDao.findLoadrunResultDetail(loadrunId, filter);
	}
	
	/***
	 * 查找时间轴
	 * @param loadrunId
	 * @return
	 */
	public List<String> findLoadrunResultDetailTimes(String loadrunId) {
		return cspLoadRunDao.findLoadrunResultDetailTimes(loadrunId);
	}
	
	/***
	 * 查找对应一级key和二级key
	 * 重复时间点做平均
	 * 
	 * @param loadrunId
	 * @param mainKey
	 * @param secondKey
	 * @return
	 */
	public List<LoadrunResultDetail> findLoadrunResultDetailSameTimeAve(String loadrunId, String mkey, String sKey) {
		return cspLoadRunDao.findLoadrunResultDetailSameTimeAve(loadrunId, mkey, sKey);
	}
	
	/***
	 * 查找对应一级key和二级key
	 * 重复时间点汇总
	 * 
	 * @param loadrunId
	 * @param mainKey
	 * @param secondKey
	 * @return
	 */
	public List<LoadrunResultDetail> findLoadrunResultDetailSameTimeSum(String loadrunId, String mkey, String sKey) {
		return cspLoadRunDao.findLoadrunResultDetailSameTimeSum(loadrunId, mkey, sKey);
	}
	
	/***
	 * 查找对应一级key和二级key，整个压测过程的汇总数据
	 * @param loadrunId
	 * @param mainKey
	 * @param secondKey
	 * @return
	 */
	public List<LoadrunReportObject> findLoadrunResultDetailSum(String loadrunId, String mkey) {
		return cspLoadRunDao.findLoadrunResultDetailSum(loadrunId, mkey);
	}
	

	/**
	 * 根据appid取得指定LoadRunHost
	 * 
	 * @return
	 */
	public LoadRunHost findLoadRunHostByAppId(int appId) {
		
		LoadRunHost d = cspLoadRunDao.findLoadRunHostByAppId(appId);
		if(d != null){
			AppInfoPo po = appInfoDao.findAppInfoById(appId);
			if(po!= null){
				d.setAppName(po.getAppName());
				d.setAppRushHours(po.getAppRushHours());
				d.setOpsField(po.getOpsField());
				d.setOpsName(po.getOpsName());
			}
		}

		return d;
	}

	/**
	 * 添加压测应用
	 * 
	 * @param loadRunHost
	 */
	public boolean addLoadRunHost(LoadRunHost loadRunHost) {
		boolean exist = cspLoadRunDao.existAppLoadConfig(loadRunHost.getAppId());
		if(!exist){
			AppInfoPo po = appInfoDao.findAppInfoById(loadRunHost.getAppId());
			if(po!= null){
				loadRunHost.setAppName(po.getAppName());
				loadRunHost.setAppRushHours(po.getAppRushHours());
				loadRunHost.setOpsField(po.getOpsField());
				loadRunHost.setOpsName(po.getOpsName());
			}
			return cspLoadRunDao.addLoadRunHost(loadRunHost);
		}else{
			return false;
		}
	}

	/**
	 * 更新压测应用
	 * 
	 * @param loadRunHost
	 */
	public boolean updateLoadRunHost(LoadRunHost loadRunHost) {
		
		AppInfoPo po = appInfoDao.findAppInfoById(loadRunHost.getAppId());
		if(po!= null){
			loadRunHost.setAppName(po.getAppName());
			loadRunHost.setAppRushHours(po.getAppRushHours());
			loadRunHost.setOpsField(po.getOpsField());
			loadRunHost.setOpsName(po.getOpsName());
		}

		return cspLoadRunDao.updateLoadRunHost(loadRunHost);
	}

	/**
	 * 
	 * @return
	 */
	public List<LoadRunHost> findAllLoadRunHost() {
		
		List<AppInfoPo> list = appInfoDao.findAllAppInfo();
		
		Map<Integer, AppInfoPo> map = new HashMap<Integer, AppInfoPo>();
		for(AppInfoPo po:list){
			map.put(po.getAppId(), po);
		}
		List<LoadRunHost> listLoad = cspLoadRunDao.findAllLoadRunHost();
		for(LoadRunHost h:listLoad){
			AppInfoPo po = map.get(h.getAppId());
			if(po != null){
				h.setAppName(po.getAppName());
				h.setAppRushHours(po.getAppRushHours());
				h.setOpsField(po.getOpsField());
				h.setOpsName(po.getOpsName());
			}
		}
		return listLoad;
	}
	
	
	/**
	 * 返回当天压测结果
	 * @param appId
	 * @param collectTime
	 * @return
	 */
	public List<LoadrunResult> findLoadrunResult(int appId,Date collectTime){
		List<LoadrunResult> list = cspLoadRunDao.findLoadrunResult(appId, collectTime);
		Collections.sort(list, new Comparator<LoadrunResult>(){
			@Override
			public int compare(LoadrunResult o1, LoadrunResult o2) {
				if(o1.getLoadrunOrder() >o2.getLoadrunOrder()){
					return 1;
				}else if(o1.getLoadrunOrder() <o2.getLoadrunOrder()){
					return -1;
				}
				return 0;
			}});
		
		return list;
	}

	public LoadrunResult findRecentLoadRunResults(int appId) {
		return cspLoadRunDao.findRecentlyAppLoadRunQps(appId);
		
	}
	/**
	 * 取得最近一个压测时间
	 * 
	 * @param appId
	 * @return
	 */
	public Date findRecentlyLoadDate(int appId) {
		return cspLoadRunDao.findRecentlyLoadDate(appId);
	}


	public List<LoadrunResult> findLoadrunResult(int appId,ResultKey key,Date start,Date end){
		return cspLoadRunDao.findLoadrunResult(appId, key, start, end);
	}
	
	public void addLoadRunThreshold(String loadrunId, String limieFeature) {
		 cspLoadRunDao.addLoadRunThreshold(loadrunId, limieFeature);
	}
	
	public String findLoadrunThreshold(String loadrunId) {
		 return cspLoadRunDao.findLoadrunThreshold(loadrunId);
	}
	

	/**
	 * 获取全部AppInfoPo
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppInfo() {
		
		return appInfoDao.findAllAppInfo();
	}
	/**
	 * 获取全部有效的应用
	 * @return
	 */
	public List<AppInfoPo> findAllEffectiveAppInfo() {
		List<AppInfoPo> newlist = new ArrayList<AppInfoPo>();
		List<AppInfoPo> list = findAllAppInfo();
		for(AppInfoPo po:list){
			if(po.getAppStatus() == 0){
				newlist.add(po);
			}
		}
		return newlist;
	}
	
	/***
	 * 获取对应id的app信息
	 * @param appId
	 * @return
	 */
	public AppInfoPo findAppInfoById(int appId){
		return appInfoDao.findAppInfoById(appId);
	}
	
	/***
	 * 获取对应appName的app信息
	 * @param appName
	 * @return
	 */
	public AppInfoPo findAppInfoByName(String appName){
		return appInfoDao.getAppInfoPoByAppName(appName);
	}
	
	/**
	 * 查询统计表中时间段内的数据
	 * @param appId
	 * @param keyId
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<KeyValuePo> findMonitorCountByDate(int appId,int keyId,Date startDate,Date endDate){
		AppInfoPo po = appInfoDao.findAppInfoById(appId);
		return monitorDayDao.findMonitorCountByDate(po.getAppDayId(), keyId, startDate, endDate);
	}
	
	/***
	 * 更新前一天压测的缓存数据，用于压测内部报表
	 */
	public void computeTodayResult() {
		max.clear();
		min.clear();
		
		Calendar calendar = Calendar.getInstance();
		Date end = calendar.getTime();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		Date start = calendar.getTime();
		
		List<LoadRunHost> apps =findAllLoadRunHost();
		for (LoadRunHost po : apps) {
			int appId = po.getAppId();
			String appName = po.getAppName();
			
			if (po == null || po.getLoadType() == null) continue;
			List<LoadrunResult> results = findLoadrunResult(appId, po.getLoadType().getQpsKey(), start, end);
			if (results.size() == 0) continue;
			
			LoadrunResult maxResult = results.get(0);
			LoadrunResult minResult = results.get(0);
			for (LoadrunResult result : results) {
				if (result.getValue() > maxResult.getValue()) {
					maxResult = result;
				}
				
				if (result.getValue() < minResult.getValue()) {
					minResult = result;
				}
			}
			
			max.put(appName, maxResult);
			min.put(appName, minResult);
		}
	}
}
