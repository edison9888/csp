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
import com.taobao.sentinel.dao.FlowControlAppDao;
import com.taobao.sentinel.dao.QpsPeriodDao;
import com.taobao.sentinel.dao.RefIpDao;
import com.taobao.sentinel.po.BaseConfigPo;
import com.taobao.sentinel.po.ConfigVersionPo;
import com.taobao.sentinel.po.FlowControlAppPo;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.util.LocalUtil;

/***
 * business class
 * application flow control
 * @author youji.zj
 *
 */
public class FlowControlAppBo {
	@Resource(name = "flowControlAppDao")
	private FlowControlAppDao flowControlAppDao;
	
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

	
	public FlowControlAppDao getFlowControlAppDao() {
		return flowControlAppDao;
	}

	public void setFlowControlAppDao(FlowControlAppDao flowControlAppDao) {
		this.flowControlAppDao = flowControlAppDao;
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
	
	public List<RefIpPo> findIps(String refId) {
		return refIpDao.findRefIps(refId);
	}
	
	public boolean checkExist(String appName, String flowApp, String strategy) {
		return flowControlAppDao.checkExist(appName, flowApp, strategy);
	}
	
	
	public ConfigVersionDao getConfigVersionDao() {
		return configVersionDao;
	}

	public void setConfigVersionDao(ConfigVersionDao configVersionDao) {
		this.configVersionDao = configVersionDao;
	}
	
	public List<FlowControlAppPo> findAll(String strategy) {
		List<FlowControlAppPo> list = flowControlAppDao.findAll(strategy);
		
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
	
	public boolean updateLimitFlow(String id, int limitFlow) {
		boolean isSuccess = flowControlAppDao.updateLimitFlow(id, limitFlow); 
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			flowControlAppDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public boolean changeIps(String id) {
		boolean isSuccess = flowControlAppDao.updateState(id);
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			flowControlAppDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public int add(HttpServletRequest request) {
		String appName = request.getParameter("appName").trim();
		String flowApp = request.getParameter("flowApp").trim();
		int limitFlow = Integer.parseInt(request.getParameter("limitFlow").trim());
		String strategy = request.getParameter("strategy").trim();
		String user = LocalUtil.getCurrentUser(request);
		String version = LocalUtil.generateVersion();
		
		FlowControlAppPo po = new FlowControlAppPo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setRefAppName(flowApp);
		po.setLimitFlow(limitFlow);
		po.setUser(user);
		po.setVersion(version);
		po.setStrategy(strategy);
		
		flowControlAppDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		qpsPeriodDao.addWhenNotExist(appName);
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	public Map<String, Map<String, Integer>> generateConfig(String appName, String strategy) {
		Map<String, Map<String, Integer>> configMap = new LinkedHashMap<String, Map<String, Integer>>();
		
		List<FlowControlAppPo> flowControlApps = flowControlAppDao.find(appName, false, strategy);
		
		Map<String, Integer> app = new HashMap<String, Integer>();
		for (FlowControlAppPo flowControlApp : flowControlApps) {
			app.put(flowControlApp.getRefAppName(), flowControlApp.getLimitFlow());
		}
		
		configMap.put("local://", app);
		return configMap;
	}
	
	public boolean deleteConfig(String id) {
		boolean deteleHead = flowControlAppDao.remove(id);
		return deteleHead;
	}
	
	public boolean updateVersion(String appName, String refApp, String version) {
		return flowControlAppDao.updateVersion(appName, refApp, version);
	}
	
	public boolean updateVersion(String appName, String version) {
		return flowControlAppDao.updateVersionByName(appName, version);
	}
	
	public int add4Test(String appName, String flowApp, int limitFlow) {
		String user = "youji.zj@taobao.com";
		String version = LocalUtil.generateVersion();
		
		FlowControlAppPo po = new FlowControlAppPo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setRefAppName(flowApp);
		po.setLimitFlow(limitFlow);
		po.setUser(user);
		po.setVersion(version);
		
		flowControlAppDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	private int addOrUpdateRefIps(FlowControlAppPo po) {
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
