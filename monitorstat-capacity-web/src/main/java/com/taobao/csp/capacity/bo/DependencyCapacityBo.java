package com.taobao.csp.capacity.bo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.dao.CapacityCostDao;
import com.taobao.csp.capacity.dao.CapacityDao;
import com.taobao.csp.capacity.dao.CapacityRankingDao;
import com.taobao.csp.capacity.dao.CspDependencyDao;
import com.taobao.csp.capacity.dao.DependencyCapacityDao;
import com.taobao.csp.capacity.po.CapacityAppPo;
import com.taobao.csp.capacity.po.CapacityRankingPo;
import com.taobao.csp.capacity.po.DependencyCapacityPo;
import com.taobao.csp.capacity.po.GroupAppPo;
import com.taobao.csp.capacity.util.AppGroupUtil;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;

/***
 * the last day of 2011
 * @author youji.zj
 *
 */
public class DependencyCapacityBo {
	
	private static final Logger logger = Logger.getLogger(DependencyCapacityBo.class);

	DependencyCapacityDao dependencyCapacityDao;
	
	CspDependencyDao cspDependencyDao;
	
	CapacityDao capacityDao;
	
	AppInfoDao appInfoDao;
	
	CapacityCostDao capacityCostDao;
	
	private CapacityRankingDao capacityRankingDao;
	
	public CapacityRankingDao getCapacityRankingDao() {
		return capacityRankingDao;
	}

	public void setCapacityRankingDao(CapacityRankingDao capacityRankingDao) {
		this.capacityRankingDao = capacityRankingDao;
	}

	public CapacityCostDao getCapacityCostDao() {
		return capacityCostDao;
	}

	public void setCapacityCostDao(CapacityCostDao capacityCostDao) {
		this.capacityCostDao = capacityCostDao;
	}

	public DependencyCapacityDao getDependencyCapacityDao() {
		return dependencyCapacityDao;
	}

	public void setDependencyCapacityDao(DependencyCapacityDao dependencyCapacityDao) {
		this.dependencyCapacityDao = dependencyCapacityDao;
	}

	public CspDependencyDao getCspDependencyDao() {
		return cspDependencyDao;
	}

	public void setCspDependencyDao(CspDependencyDao cspDependencyDao) {
		this.cspDependencyDao = cspDependencyDao;
	}

	public CapacityDao getCapacityDao() {
		return capacityDao;
	}

	public void setCapacityDao(CapacityDao capacityDao) {
		this.capacityDao = capacityDao;
	}

	public AppInfoDao getAppInfoDao() {
		return appInfoDao;
	}

	public void setAppInfoDao(AppInfoDao appInfoDao) {
		this.appInfoDao = appInfoDao;
	}
	
	public List<DependencyCapacityPo> findAllDependencyCapacityPos(int appId, int raisePercent) {
		if (raisePercent < -100) raisePercent = -100;
		List<DependencyCapacityPo> targetDepends = new ArrayList<DependencyCapacityPo>();
		int sourceAppId = AppGroupUtil.getSourceAppId(appId);
		AppInfoPo selfAppPo = appInfoDao.findAppInfoById(sourceAppId);
		
		List<DependencyCapacityPo> depends = capacityCostDao.findDependCapacityPos(selfAppPo.getAppName());
		 for (DependencyCapacityPo po : depends) {
			 AppInfoPo depAppPo = appInfoDao.getAppInfoPoByAppName(po.getDepApp());
			 if (depAppPo == null) {
				 logger.info("app not exist error " + po.getDepApp());
				 continue;
			 }
			 
			 CapacityAppPo capacityAppPo = capacityDao.getCapacityApp(depAppPo.getAppId());
			 if (capacityAppPo == null) {
				 logger.info("app not in capacity system " + depAppPo.getAppName());
				 continue;
			 }
			 
			 List<GroupAppPo> groupAppPos = AppGroupUtil.getAppGroupIds(capacityAppPo);
			 if (groupAppPos.size() == 0) {
				 CapacityRankingPo rankingPo = capacityRankingDao.getLatestCapacityRanking(depAppPo.getAppId());
				 String capacityLevel = rankingPo.getFeatureData("容量水位");
				 double dLevel = Double.valueOf(capacityLevel.substring(0, capacityLevel.length() - 1));  // 去掉百分号
				 dLevel = ((dLevel * (100 - po.getPercent())) +  (dLevel *  po.getPercent() * (100 + raisePercent) / 100)) / 100;
				 po.setLevel(Double.valueOf(new DecimalFormat("###,###.##").format(dLevel))); 
				 targetDepends.add(po); 
			 } else {
				 for (GroupAppPo groupAppPo : groupAppPos) {
					 DependencyCapacityPo newDependencyCapacityPo = po.clone();
					 newDependencyCapacityPo.setDepApp(groupAppPo.getAppName());
					 CapacityRankingPo rankingPo = capacityRankingDao.getLatestCapacityRanking(groupAppPo.getAppId());
					 String capacityLevel = rankingPo.getFeatureData("容量水位");
					 
					 double dLevel = Double.valueOf(capacityLevel.substring(0, capacityLevel.length() - 1));  // 去掉百分号
					 dLevel = ((dLevel * (100 - po.getPercent())) +  (dLevel *  po.getPercent() * (100 + raisePercent) / 100)) / 100;
					 newDependencyCapacityPo.setLevel(Double.valueOf(new DecimalFormat("###,###.##").format(dLevel))); 
					 targetDepends.add(newDependencyCapacityPo);
				 }
			 }	 
		 }
		 
		 return targetDepends;
	}
	
	
	
}
