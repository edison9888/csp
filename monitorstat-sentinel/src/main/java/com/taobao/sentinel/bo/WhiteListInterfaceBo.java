package com.taobao.sentinel.bo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.sentinel.dao.ConfigVersionDao;
import com.taobao.sentinel.dao.DataUrlDao;
import com.taobao.sentinel.dao.QpsPeriodDao;
import com.taobao.sentinel.dao.RefIpDao;
import com.taobao.sentinel.dao.WhiteListInterfaceDao;
import com.taobao.sentinel.po.BaseConfigPo;
import com.taobao.sentinel.po.ConfigVersionPo;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.po.WhiteListInterfacePo;
import com.taobao.sentinel.util.LocalUtil;

/***
 * business class
 * interface level white list
 * @author youji.zj
 *
 */
public class WhiteListInterfaceBo {
	
	@Resource(name = "whiteListInterfaceDao")
	private WhiteListInterfaceDao whiteListInterfaceDao;
	
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
	
	public WhiteListInterfaceDao getWhiteListInterfaceDao() {
		return whiteListInterfaceDao;
	}

	public void setWhiteListInterfaceDao(WhiteListInterfaceDao whiteListInterfaceDao) {
		this.whiteListInterfaceDao = whiteListInterfaceDao;
	}

	public RefIpDao getRefIpDao() {
		return refIpDao;
	}

	public void setRefIpDao(RefIpDao refIpDao) {
		this.refIpDao = refIpDao;
	}
	

	public ConfigVersionDao getConfigVersionDao() {
		return configVersionDao;
	}

	public void setConfigVersionDao(ConfigVersionDao configVersionDao) {
		this.configVersionDao = configVersionDao;
	}

	public DataUrlDao getDataUrlDao() {
		return dataUrlDao;
	}

	public void setDataUrlDao(DataUrlDao dataUrlDao) {
		this.dataUrlDao = dataUrlDao;
	}
	
	public List<WhiteListInterfacePo> find(String appName, boolean includeDisable) {
		return whiteListInterfaceDao.find(appName, includeDisable);
	}
	
	public List<RefIpPo> findIps(String refId) {
		return refIpDao.findRefIps(refId);
	}
	
	public boolean checkExist(String appName, String whiteApp, String interfaceInfo) {
		return whiteListInterfaceDao.checkExist(appName, whiteApp, interfaceInfo);
	}
	
	public List<WhiteListInterfacePo> findAll() {
		List<WhiteListInterfacePo> list = whiteListInterfaceDao.findAll();
		
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
		String interfaceInfo = request.getParameter("interfaceInfo").trim();
		String whiteApp = request.getParameter("whiteApp").trim();
		String user = LocalUtil.getCurrentUser(request);
		String version = LocalUtil.generateVersion();
		
		WhiteListInterfacePo po = new WhiteListInterfacePo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setInterfaceInfo(interfaceInfo);
		po.setRefAppName(whiteApp);
		po.setUser(user);
		po.setVersion(version);
		
		whiteListInterfaceDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		qpsPeriodDao.addWhenNotExist(appName);
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	public Map<String, Map<String, Integer>> generateConfig(String appName) {
		Map<String, Map<String, Integer>> configMap = new LinkedHashMap<String, Map<String, Integer>>();
		
		List<WhiteListInterfacePo> whiteListInterfaces = whiteListInterfaceDao.find(appName, false);
		
		for (WhiteListInterfacePo whiteListInterface : whiteListInterfaces) {
			String interfaceInfo  = whiteListInterface.getInterfaceInfo();
			Map<String, Integer> app;
			
			if (configMap.containsKey(interfaceInfo)) {
				app =  configMap.get(interfaceInfo);		
			} else {
				app = new HashMap<String, Integer>();
				configMap.put(interfaceInfo, app);
			}
			
			app.put(whiteListInterface.getRefAppName(), 0);
		}
		return configMap;
	}
	
	public boolean deleteConfig(String id) {
		boolean deteleHead = whiteListInterfaceDao.remove(id);
		return deteleHead;
	}
	
	public boolean updateVersion(String appName, String refApp, String version) {
		return whiteListInterfaceDao.updateVersion(appName, refApp, version);
	}
	
	public boolean updateVersion(String appName, String version) {
		return whiteListInterfaceDao.updateVersionByName(appName, version);
	}
	
	public boolean changeIps(String id) {
		boolean isSuccess = whiteListInterfaceDao.updateState(id);
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			whiteListInterfaceDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public int add4Test(String appName, String whiteApp, String interfaceInfo) {
		String user = "youji.zj@taobao.com";
		String version = LocalUtil.generateVersion();
		
		WhiteListInterfacePo po = new WhiteListInterfacePo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setInterfaceInfo(interfaceInfo);
		po.setRefAppName(whiteApp);
		po.setUser(user);
		po.setVersion(version);
		
		whiteListInterfaceDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	private int addOrUpdateRefIps(WhiteListInterfacePo po) {
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
