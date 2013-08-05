package com.taobao.csp.capacity.bo;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import com.taobao.csp.capacity.dao.CapacityCostDao;
import com.taobao.csp.capacity.dao.CspDependencyDao;
import com.taobao.csp.capacity.po.CapacityCostInfoPo;
import com.taobao.csp.capacity.util.LocalUtil;

public class DataInterfaceBo {
	
	CspDependencyDao cspDependencyDao;
	
    CapacityCostDao capacityCostDao;
	
	
	public CspDependencyDao getCspDependencyDao() {
		return cspDependencyDao;
	}

	public void setCspDependencyDao(CspDependencyDao cspDependencyDao) {
		this.cspDependencyDao = cspDependencyDao;
	}

	public CapacityCostDao getCapacityCostDao() {
		return capacityCostDao;
	}

	public void setCapacityCostDao(CapacityCostDao capacityCostDao) {
		this.capacityCostDao = capacityCostDao;
	}

	public Set<String> getDepMeHsfApp(String appName) {
		Set<String> set = cspDependencyDao.getDepMeHsfApp(appName, LocalUtil.getYesToday());
		return set;
	}
	
	public Set<String> getMeDepHsfApp(String appName) {
		return cspDependencyDao.getMeDepHsfApp(appName, LocalUtil.getYesToday());
	}
	
	public Map<String, Set<String>> getDepMeApp(String appName) {
		return cspDependencyDao.getDepMeApp(appName);
	}
	
	public Map<String, Set<String>> getMeDepApp(String appName) {
		Map<String, Set<String>> depMap = cspDependencyDao.getMeDepApp(appName);
		return depMap;
	}
	
	public Map<String, Integer> getOrigin(String appName, String date) {
		return cspDependencyDao.getOrigin(appName, date);
	}
	
	public Map<String, Integer> getRequest(String appName, String date) {
		return cspDependencyDao.getRequest(appName, date);
	}
	
	public Map<String, Integer> getHsfProviderInfo(String appName, String date) {
		return cspDependencyDao.getHsfProviderInfo(appName, date);
	}
	
	public Map<String, Long> getPvRequestStatics(String appName, Date date) {
		return cspDependencyDao.getPvRequestByAppNameDate(appName, date);
	}
	
	public Long getHsfProviderPv(String appName, Date date) {
		return cspDependencyDao.getHsfProviderPv(appName, date);
	}
	
	public CapacityCostInfoPo getCapacityCost(String appName) {
		return capacityCostDao.findLatestCapacityCostByApp(appName);
	}
	
	public int getLatestUvByDomain(String domain) {
		return cspDependencyDao.getLatestUvByDomain(domain);
	}
	
	public int getLatestIPvByDomain(String domain) {
		return cspDependencyDao.getLatestIPvByDomain(domain);
	}
	
	public int getUvByDomainAndDate(String domain, Date date) {
		return cspDependencyDao.getUvByDomainAndDate(domain, date);
	}
	
	public int getIPvByDomainAndDate(String domain, Date date) {
		return cspDependencyDao.getIPvByDomainAndDate(domain, date);
	}

}
