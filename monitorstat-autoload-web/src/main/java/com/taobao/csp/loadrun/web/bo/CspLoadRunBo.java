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
 * @version 2010-9-2 ����09:40:49
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
	 * ɾ��loadRunHost
	 * 
	 * @param appId
	 */
	public boolean deleteLoadRunHost(int appId) {

		return cspLoadRunDao.deleteLoadRunHost(appId);
	}
	
	/***
	 * ɾ��ѹ����
	 * @param loadId
	 * @return
	 */
	public boolean deleteLoadrunResultById(String loadId) {
		return cspLoadRunDao.deleteLoadrunResultById(loadId);
	}

	/**
	 * ���ѹ����
	 * 
	 * @param result
	 */
	public void addLoadRunResult(LoadrunResult result) {
		cspLoadRunDao.addLoadRunResult(result);
	}
	
	/**
	 * ���ѹ������ϸ��Ϣ
	 * 
	 * @param result
	 */
	public void addLoadRunResultDetail(LoadrunResultDetail result) {
		cspLoadRunDao.addLoadRunResultDetail(result);
	}
	
	/***
	 * ����ѹ�����ϸ��Ϣ
	 * @param loadrunId
	 *        ѹ���id
	 * @return
	 */
	public List<LoadrunResultDetail> findLoadrunResultDetail(String loadrunId, Set<String> filter) {
		return cspLoadRunDao.findLoadrunResultDetail(loadrunId, filter);
	}
	
	/***
	 * ����ʱ����
	 * @param loadrunId
	 * @return
	 */
	public List<String> findLoadrunResultDetailTimes(String loadrunId) {
		return cspLoadRunDao.findLoadrunResultDetailTimes(loadrunId);
	}
	
	/***
	 * ���Ҷ�Ӧһ��key�Ͷ���key
	 * �ظ�ʱ�����ƽ��
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
	 * ���Ҷ�Ӧһ��key�Ͷ���key
	 * �ظ�ʱ������
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
	 * ���Ҷ�Ӧһ��key�Ͷ���key������ѹ����̵Ļ�������
	 * @param loadrunId
	 * @param mainKey
	 * @param secondKey
	 * @return
	 */
	public List<LoadrunReportObject> findLoadrunResultDetailSum(String loadrunId, String mkey) {
		return cspLoadRunDao.findLoadrunResultDetailSum(loadrunId, mkey);
	}
	

	/**
	 * ����appidȡ��ָ��LoadRunHost
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
	 * ���ѹ��Ӧ��
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
	 * ����ѹ��Ӧ��
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
	 * ���ص���ѹ����
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
	 * ȡ�����һ��ѹ��ʱ��
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
	 * ��ȡȫ��AppInfoPo
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppInfo() {
		
		return appInfoDao.findAllAppInfo();
	}
	/**
	 * ��ȡȫ����Ч��Ӧ��
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
	 * ��ȡ��Ӧid��app��Ϣ
	 * @param appId
	 * @return
	 */
	public AppInfoPo findAppInfoById(int appId){
		return appInfoDao.findAppInfoById(appId);
	}
	
	/***
	 * ��ȡ��ӦappName��app��Ϣ
	 * @param appName
	 * @return
	 */
	public AppInfoPo findAppInfoByName(String appName){
		return appInfoDao.getAppInfoPoByAppName(appName);
	}
	
	/**
	 * ��ѯͳ�Ʊ���ʱ����ڵ�����
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
	 * ����ǰһ��ѹ��Ļ������ݣ�����ѹ���ڲ�����
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
