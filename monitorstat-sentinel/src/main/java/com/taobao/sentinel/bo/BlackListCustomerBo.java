package com.taobao.sentinel.bo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.sentinel.dao.BlackListCustomerDao;
import com.taobao.sentinel.dao.ConfigVersionDao;
import com.taobao.sentinel.dao.DataUrlDao;
import com.taobao.sentinel.dao.QpsPeriodDao;
import com.taobao.sentinel.dao.RefIpDao;
import com.taobao.sentinel.po.BaseConfigPo;
import com.taobao.sentinel.po.BlackListCustomerPo;
import com.taobao.sentinel.po.ConfigVersionPo;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.util.LocalUtil;

/***
 * business class
 * customization level black list
 * @author youji.zj
 *
 */
public class BlackListCustomerBo {
	
	@Resource(name = "blackListCustomerDao")
	private BlackListCustomerDao blackListCustomerDao;
	
	@Resource(name = "refIpDao")
	private RefIpDao refIpDao;
	
	@Resource(name = "configVersionDao")
	private ConfigVersionDao configVersionDao;
	
	@Resource(name = "dataUrlDao")
	private DataUrlDao dataUrlDao;
	
	@Resource(name = "qpsPeriodDao")
	private QpsPeriodDao qpsPeriodDao;
	
	public QpsPeriodDao getQpsPeriodDao() {
		return qpsPeriodDao;
	}

	public void setQpsPeriodDao(QpsPeriodDao qpsPeriodDao) {
		this.qpsPeriodDao = qpsPeriodDao;
	}

	
	public BlackListCustomerDao getBlackListCustomerDao() {
		return blackListCustomerDao;
	}

	public void setBlackListCustomerDao(BlackListCustomerDao blackListCustomerDao) {
		this.blackListCustomerDao = blackListCustomerDao;
	}

	public RefIpDao getRefIpDao() {
		return refIpDao;
	}

	public void setRefIpDao(RefIpDao refIpDao) {
		this.refIpDao = refIpDao;
	}

	public DataUrlDao getDataUrlDao() {
		return dataUrlDao;
	}

	public void setDataUrlDao(DataUrlDao dataUrlDao) {
		this.dataUrlDao = dataUrlDao;
	}
	
	public List<BlackListCustomerPo> find(String appName, boolean includeDisable) {
		return blackListCustomerDao.find(appName, includeDisable);
	}
	
	public List<RefIpPo> findIps(String refId) {
		return refIpDao.findRefIps(refId);
	}
	
	public boolean checkExist(String appName, String blackApp, String customerInfo) {
		return blackListCustomerDao.checkExist(appName, blackApp, customerInfo);
	}

	
	public ConfigVersionDao getConfigVersionDao() {
		return configVersionDao;
	}

	public void setConfigVersionDao(ConfigVersionDao configVersionDao) {
		this.configVersionDao = configVersionDao;
	}

	public List<BlackListCustomerPo> findAll() {
		List<BlackListCustomerPo>  list = blackListCustomerDao.findAll();
		
		for (BaseConfigPo po : list) {
			ConfigVersionPo effectVersion = configVersionDao.findLatestConfigVersion(po.getAppName());
			if (effectVersion != null && (po.getVersion().equals(effectVersion.getVersion()))) {
				po.setOutOfDate(false);
			} else {
				po.setOutOfDate(true);
			}
		}
		
		return list;
	}
	
	public int add(HttpServletRequest request) {
		String appName = request.getParameter("appName").trim();
		String customerInfo = request.getParameter("customerInfo").trim();
		String blackApp = request.getParameter("blackApp").trim();
		String user = LocalUtil.getCurrentUser(request);
		String version = LocalUtil.generateVersion();
		
		BlackListCustomerPo po = new BlackListCustomerPo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setCustomerInfo(customerInfo);
		po.setRefAppName(blackApp);
		po.setUser(user);
		po.setVersion(version);
		
		blackListCustomerDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		qpsPeriodDao.addWhenNotExist(appName);
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	public Map<String, Map<String, Integer>> generateConfig(String appName) {
		Map<String, Map<String, Integer>> configMap = new LinkedHashMap<String, Map<String, Integer>>();
		
		List<BlackListCustomerPo> blackListCustomers = blackListCustomerDao.find(appName, false);
		
		for (BlackListCustomerPo blackListCustomer : blackListCustomers) {
			String customerInfo  = blackListCustomer.getCustomerInfo();
			Map<String, Integer> app;
			
			if (configMap.containsKey(customerInfo)) {
				app =  configMap.get(customerInfo);	
			} else {
				app = new HashMap<String, Integer>();
				configMap.put(customerInfo, app);
			}
			
			app.put(blackListCustomer.getRefAppName(), 0);
		}
		return configMap;
	}
	
	public boolean deleteConfig(String id) {
		boolean deteleHead = blackListCustomerDao.remove(id);
		return deteleHead;
	}
	
	public boolean updateVersion(String appName, String refApp, String version) {
		return blackListCustomerDao.updateVersion(appName, refApp, version);
	}
	
	public boolean updateVersion(String appName, String version) {
		return blackListCustomerDao.updateVersionByName(appName, version);
	}
	
	public boolean changeIps(String id) {
		boolean isSuccess = blackListCustomerDao.updateState(id);
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			blackListCustomerDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public int add4Test(String appName, String blackApp, String customerInfo) {
		String user = "youji.zj@taobao.com";
		String version = LocalUtil.generateVersion();
		
		BlackListCustomerPo po = new BlackListCustomerPo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setCustomerInfo(customerInfo);
		po.setRefAppName(blackApp);
		po.setUser(user);
		po.setVersion(version);
		
		blackListCustomerDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	private int addOrUpdateRefIps(BlackListCustomerPo po) {
		int count = 0;
		String appName = po.getAppName();
		String refApp = po.getRefAppName();
		
		boolean exist = refIpDao.checkExist(appName, refApp);
		if (!exist) {
			List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(refApp);
			for (HostPo host : hosts) {
				String ip = host.getHostIp();
				String id = LocalUtil.generateId();
				String refId = LocalUtil.generateRefId(appName, refApp);
				refIpDao.addRefIp(id, ip, refId);
				count++;
			}
		} else {
			count = refIpDao.findRefIps(LocalUtil.generateRefId(appName, refApp)).size();
		}
		
		return count;
	}

}
