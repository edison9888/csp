package com.taobao.csp.time.job;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.db.impl.capacity.CapacityLoadDao;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityLoadPo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/***
 * ��ʵʱ��ȡ��������
 * @author youji.zj  
 * @version 2012-08-15
 */
public class RealTimeLoadJob {
	
	public static Logger logger = Logger.getLogger(RealTimeLoadJob.class);
	
	private CommonServiceInterface commonService;
	
	private CapacityLoadDao capacityLoadDao;
	
	/***  ͨ��ʵʱϵͳȡ�������ɵ�����Ӧ�� ***/
	private String [] specialPvApps = {
			"top"
	};
	
	public CommonServiceInterface getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonServiceInterface commonService) {
		this.commonService = commonService;
	}

	public CapacityLoadDao getCapacityLoadDao() {
		return capacityLoadDao;
	}

	public void setCapacityLoadDao(CapacityLoadDao capacityLoadDao) {
		this.capacityLoadDao = capacityLoadDao;
	}

	public void syncLoadFromRealTimeSystem() {
		if (!passCheck()) {
			logger.info("�˻���δ����������ϵͳ����");
			return;
		}
		
		// tairӦ�õĴ���
		List<AppInfoPo> appList = AppInfoAo.get().findAllAppInfo();
		for (AppInfoPo po : appList) {
			String feature = po.getFeature();
			if (feature != null && "tair".equals(feature.trim())) {
				logger.info("tair app " + po.getAppName());
				grapLoadDataForTairApp(po.getAppName());
			}
		}
		
		// pvӦ�ô���
		for (String pvApp : specialPvApps) {
			logger.info("pv app " + pvApp);
			grapLoadDataForPvApp(pvApp);
		}
	}
	
	private void grapLoadDataForTairApp(String appName) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date sDate = calendar.getTime();
		
		// ��Ⱥ���� ��Ȼû��app����� ֻ���Լ�����
		double maxGroupQps = 0;
		
		// ��������
		double maxSingleQps = 0;
		Map<String, Double> maxSingleQpsM = new HashMap<String, Double>();  // key is HH:mm, value is max qps of this time
		Map<String, Double> maxGroupQpsM = new HashMap<String, Double>();   // key is HH:mm, value is total qps of this time
		List<HostPo> hostPos = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appName);
		if (hostPos == null || hostPos.isEmpty()) return;
		
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
		for (HostPo host : hostPos) {
			String ip = host.getHostIp();
			List<TimeDataInfo> ipL = commonService.queryKeyDataHistory(appName, KeyConstants.TAIR_PROVIDER, PropConstants.GETCOUNT, ip, sDate);
			logger.info(appName + " tair single ip :" + ip + " and data size is " + ipL.size());
			
			// ͬһ̨����ֻͳ��һ��ʱ���
			Set<String> timeSet = new HashSet<String>(); 
			for (TimeDataInfo timeDataInfo : ipL) {
				double qps = timeDataInfo.getMainValue();  // tair�Ĳ���Ҫ�ٳ���60
				Date cDate = new Date(timeDataInfo.getTime());
				String time = sf.format(cDate);
				
				//  �ӿ�ȡ���������쳣���ݣ����˵�
				if (qps > 200000) {
					continue;
				}
				
				// ����������ȡ
				if (!maxSingleQpsM.containsKey(time) || (maxSingleQpsM.get(time) < qps)) {
					maxSingleQpsM.put(time, qps);
				}
				
				// ��Ⱥ������ȡ
				if (maxGroupQpsM.containsKey(time)) {
					if (!timeSet.contains(time)) {
						double groupqps = maxGroupQpsM.get(time);
						groupqps += qps;
						maxGroupQpsM.put(time, groupqps);
						timeSet.add(time);
					} else {
						logger.info("same time has more than one data wrong");
					}
				} else {
					maxGroupQpsM.put(time, qps);
					timeSet.add(time);
				}
			}
		}
	
		// �������ɼ���
		String maxSingleTime = null;  // �������ɵ�ʱ��
		for (Map.Entry<String, Double> entry : maxSingleQpsM.entrySet()) {
			String time = entry.getKey();
			double singleQpsS = entry.getValue();
			
			if (maxSingleQps < singleQpsS) {
				maxSingleQps = singleQpsS;
				maxSingleTime = time;
			}
		}
		
		// ��Ⱥ���ɼ���
		String maxGroupTime = null;  // ��Ⱥ���ɵ�ʱ��
		for (Map.Entry<String, Double> entry : maxGroupQpsM.entrySet()) {
			String time = entry.getKey();
			double groupQpsS = entry.getValue();
			if (maxGroupQps < groupQpsS) {
				maxGroupQps = groupQpsS;
				maxGroupTime = time;
			}
		}
		
		logger.info(appName + " max single qps time is " + maxSingleTime + ", max group qps time is " + maxGroupTime);
		
		// �������ɴ洢
		CapacityLoadPo loadPoSingle= new CapacityLoadPo();
		loadPoSingle.setAppName(appName);
		loadPoSingle.setQps(maxSingleQps);
		loadPoSingle.setDataSouce("realtime");
		loadPoSingle.setDataType("single");
		loadPoSingle.setCollectTime(sDate);
		capacityLoadDao.addCapacityLoad(loadPoSingle);
		
		// ��Ⱥ���ɴ洢
		CapacityLoadPo loadPoGroup= new CapacityLoadPo();
		loadPoGroup.setAppName(appName);
		loadPoGroup.setQps(maxGroupQps);
		loadPoGroup.setDataSouce("realtime");
		loadPoGroup.setDataType("group");
		loadPoGroup.setCollectTime(sDate);
		capacityLoadDao.addCapacityLoad(loadPoGroup);
	}
	
	private void grapLoadDataForPvApp(String appName) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date sDate = calendar.getTime();
		
		// ��Ⱥ����
		double maxGroupQps = 0;
		List<TimeDataInfo> list = commonService.queryKeyDataHistory(appName, KeyConstants.PV, PropConstants.E_TIMES, sDate);
		logger.info("pv group data size is :" + list.size());
		for (TimeDataInfo timeDataInfo : list) {
			double qps = (double)((int)(timeDataInfo.getMainValue() / 60 * 100)) / 100;
			if (qps > maxGroupQps) {
				maxGroupQps = qps;
			}
		}
		CapacityLoadPo loadPoGroup = new CapacityLoadPo();
		loadPoGroup.setAppName(appName);
		loadPoGroup.setQps(maxGroupQps);
		loadPoGroup.setDataSouce("realtime");
		loadPoGroup.setDataType("group");
		loadPoGroup.setCollectTime(sDate);
		capacityLoadDao.addCapacityLoad(loadPoGroup);
		
		// ��������
		double maxSingleQps = 0;
		Set<String> sampleIpsSet = sampleIps(appName);
		for (String ip : sampleIpsSet) {
			List<TimeDataInfo> ipL = commonService.queryKeyDataHistory(appName, KeyConstants.PV, PropConstants.E_TIMES, ip, sDate);
			logger.info("pv single ip :" + ip + " and data size is " + ipL.size());
			for (TimeDataInfo timeDataInfo : ipL) {
				double qps = (double)((int)(timeDataInfo.getMainValue() / 60 * 100)) / 100;
				if (qps > maxSingleQps) {
					maxSingleQps = qps;
				}
			}
		}
		CapacityLoadPo loadPoSingle= new CapacityLoadPo();
		loadPoSingle.setAppName(appName);
		loadPoSingle.setQps(maxSingleQps);
		loadPoSingle.setDataSouce("realtime");
		loadPoSingle.setDataType("single");
		loadPoSingle.setCollectTime(sDate);
		capacityLoadDao.addCapacityLoad(loadPoSingle);
	}
	
	private Set<String> sampleIps(String appName) {
		Set<String> sampleIpS = new HashSet<String>();
		Set<String> sampleRoomS = new HashSet<String>();
		sampleRoomS.add("CM3");
		sampleRoomS.add("CM4");
		List<HostPo> hostPos = CspCacheTBHostInfos.get().getHostInfoListByOpsName(appName);
		if (hostPos == null || hostPos.isEmpty()) {
			logger.error(appName + " has no hosts, please pay attention!!!");
			return sampleIpS;
		}
		
		for (HostPo po : hostPos) {
			String site = po.getHostSite();
			if (site != null && sampleRoomS.contains(site.toUpperCase())) {
				sampleIpS.add(po.getHostIp());
				sampleRoomS.remove(site.toUpperCase());
			}
			if (sampleRoomS.isEmpty()) break;
		}
		
		return sampleIpS;
	}
	
	private  boolean passCheck() {
		boolean pass = false;
		String capacity = System.getenv("CAPACITY");
		if (capacity != null && capacity.equals("true")) {
			pass = true;
		}
		
		return pass;
	}
}
