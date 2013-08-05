package com.taobao.sentinel.bo;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.sentinel.dao.ConfigVersionDao;
import com.taobao.sentinel.dao.DataUrlDao;
import com.taobao.sentinel.dao.FlowControlInterfaceDao;
import com.taobao.sentinel.dao.QpsPeriodDao;
import com.taobao.sentinel.dao.RefIpDao;
import com.taobao.sentinel.po.BaseConfigPo;
import com.taobao.sentinel.po.ConfigVersionPo;
import com.taobao.sentinel.po.FlowControlInterfacePo;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.util.LocalUtil;

/***
 * business class
 * interface flow control
 * @author youji.zj
 *
 */
public class FlowControlInterfaceBo {
	
	@Resource(name = "flowControlInterfaceDao")
	private FlowControlInterfaceDao flowControlInterfaceDao;
	
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

	
	public FlowControlInterfaceDao getFlowControlInterfaceDao() {
		return flowControlInterfaceDao;
	}

	public void setFlowControlInterfaceDao(FlowControlInterfaceDao flowControlInterfaceDao) {
		this.flowControlInterfaceDao = flowControlInterfaceDao;
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
	
	public List<FlowControlInterfacePo> find(String appName, boolean includeDisable, String strategy) {
		return flowControlInterfaceDao.find(appName, includeDisable, strategy);
	}
	
	public List<RefIpPo> findIps(String refId) {
		return refIpDao.findRefIps(refId);
	}
	
	public boolean checkExist(String appName, String flowApp, String interfaceInfo, String strategy) {
		return flowControlInterfaceDao.checkExist(appName, flowApp, interfaceInfo, strategy);
	}
	
	
	public ConfigVersionDao getConfigVersionDao() {
		return configVersionDao;
	}

	public void setConfigVersionDao(ConfigVersionDao configVersionDao) {
		this.configVersionDao = configVersionDao;
	}
	
	public List<FlowControlInterfacePo> findAll(String strategy) {
		List<FlowControlInterfacePo> list = flowControlInterfaceDao.findAll(strategy);
		
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
		boolean isSuccess = flowControlInterfaceDao.updateLimitFlow(id, limitFlow); 
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			flowControlInterfaceDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public boolean changeIps(String id) {
		boolean isSuccess = flowControlInterfaceDao.updateState(id);
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			flowControlInterfaceDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public int add(HttpServletRequest request) {
		String appName = request.getParameter("appName").trim();
		String interfaceInfo = request.getParameter("interfaceInfo").trim();
		String flowApp = request.getParameter("flowApp").trim();
		int limitFlow = Integer.parseInt(request.getParameter("limitFlow").trim());
		String strategy = request.getParameter("strategy").trim();
		String user = LocalUtil.getCurrentUser(request);
		String version = LocalUtil.generateVersion();
		
		FlowControlInterfacePo po = new FlowControlInterfacePo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setInterfaceInfo(interfaceInfo);
		po.setRefAppName(flowApp);
		po.setLimitFlow(limitFlow);
		po.setUser(user);
		po.setVersion(version);
		po.setStrategy(strategy);
		
		flowControlInterfaceDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		qpsPeriodDao.addWhenNotExist(appName);
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	public Map<String, Map<String, Integer>> generateConfig(String appName, String strategy) {
		Map<String, Map<String, Integer>> configMap = new LinkedHashMap<String, Map<String, Integer>>();
		
		List<FlowControlInterfacePo> flowControlInterfaces = flowControlInterfaceDao.find(appName, false, strategy);
		
		for (FlowControlInterfacePo flowControlInterface : flowControlInterfaces) {
			String interfaceInfo  = flowControlInterface.getInterfaceInfo();
			
			String refApp = flowControlInterface.getRefAppName();
			int threshold = flowControlInterface.getLimitFlow();
			
			if (configMap.containsKey(interfaceInfo)) {
				Map<String, Integer> existMap =  configMap.get(interfaceInfo);
				existMap.put(refApp, threshold);
			} else {
				Map<String, Integer> ipThreadMap = new LinkedHashMap<String, Integer>();
				ipThreadMap.put(refApp, threshold);
				configMap.put(interfaceInfo, ipThreadMap);
			}
		}
		return configMap;
	}
	
	public boolean deleteConfig(String id) {
		boolean deteleHead = flowControlInterfaceDao.remove(id);
		return deteleHead;
	}
	
	public boolean updateVersion(String appName, String refApp, String version) {
		return flowControlInterfaceDao.updateVersion(appName, refApp, version);
	}
	
	public boolean updateVersion(String appName, String version) {
		return flowControlInterfaceDao.updateVersionByName(appName, version);
	}
	
	public int add4Test(String appName, String flowApp, String interfaceInfo, int limitFlow) {
		String user = "youji.zj@taobao.com";
		String version = LocalUtil.generateVersion();
		
		FlowControlInterfacePo po = new FlowControlInterfacePo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setInterfaceInfo(interfaceInfo);
		po.setRefAppName(flowApp);
		po.setLimitFlow(limitFlow);
		po.setUser(user);
		po.setVersion(version);
		
		flowControlInterfaceDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	private int addOrUpdateRefIps(FlowControlInterfacePo po) {
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
