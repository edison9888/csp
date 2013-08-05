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
import com.taobao.sentinel.dao.FlowControlParamDao;
import com.taobao.sentinel.dao.QpsPeriodDao;
import com.taobao.sentinel.dao.RefIpDao;
import com.taobao.sentinel.po.BaseConfigPo;
import com.taobao.sentinel.po.ConfigVersionPo;
import com.taobao.sentinel.po.FlowControlParamPo;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.util.LocalUtil;

/***
 * business class
 * param flow control
 * @author youji.zj
 *
 */
public class FlowControlParamBo {
	
	@Resource(name = "flowControlParamDao")
	private FlowControlParamDao flowControlParamDao;
	
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

	
	public FlowControlParamDao getFlowControlParamDao() {
		return flowControlParamDao;
	}

	public void setFlowControlParamDao(FlowControlParamDao flowControlParamDao) {
		this.flowControlParamDao = flowControlParamDao;
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
	
	public List<FlowControlParamPo> find(String appName, boolean includeDisable) {
		return flowControlParamDao.find(appName, includeDisable);
	}
	
	public List<RefIpPo> findIps(String refId) {
		return refIpDao.findRefIps(refId);
	}
	
	public boolean checkExist(String appName, String flowApp, String paramInfo) {
		return flowControlParamDao.checkExist(appName, flowApp, paramInfo);
	}
	
	
	public ConfigVersionDao getConfigVersionDao() {
		return configVersionDao;
	}

	public void setConfigVersionDao(ConfigVersionDao configVersionDao) {
		this.configVersionDao = configVersionDao;
	}
	
	public List<FlowControlParamPo> findAll() {
		List<FlowControlParamPo> list = flowControlParamDao.findAll();
		
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
		boolean isSuccess = flowControlParamDao.updateLimitFlow(id, limitFlow); 
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			flowControlParamDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public boolean changeIps(String id) {
		boolean isSuccess = flowControlParamDao.updateState(id);
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			flowControlParamDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public int add(HttpServletRequest request) {
		String appName = request.getParameter("appName").trim();
		String paramInfo = request.getParameter("paramInfo").trim();
		String flowApp = request.getParameter("flowApp").trim();
		int limitFlow = Integer.parseInt(request.getParameter("limitFlow").trim());
		String user = LocalUtil.getCurrentUser(request);
		String version = LocalUtil.generateVersion();
		
		FlowControlParamPo po = new FlowControlParamPo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setParamInfo(paramInfo);
		po.setRefAppName(flowApp);
		po.setLimitFlow(limitFlow);
		po.setUser(user);
		po.setVersion(version);
		
		flowControlParamDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		qpsPeriodDao.addWhenNotExist(appName);
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	public Map<String, Map<String, Integer>> generateConfig(String appName) {
		Map<String, Map<String, Integer>> configMap = new LinkedHashMap<String, Map<String, Integer>>();
		
		List<FlowControlParamPo> flowControlParams = flowControlParamDao.find(appName, false);
		
		for (FlowControlParamPo flowControlParam : flowControlParams) {
			String paramInfo  = flowControlParam.getParamInfo();
			
			String refApp = flowControlParam.getRefAppName();
			int threshold = flowControlParam.getLimitFlow();
			
			if (configMap.containsKey(paramInfo)) {
				Map<String, Integer> existMap =  configMap.get(paramInfo);
				existMap.put(refApp, threshold);
			} else {
				Map<String, Integer> ipThreadMap = new LinkedHashMap<String, Integer>();
				ipThreadMap.put(refApp, threshold);
				configMap.put(paramInfo, ipThreadMap);
			}
		}
		return configMap;
	}
	
	public boolean deleteConfig(String id) {
		boolean deteleHead = flowControlParamDao.remove(id);
		return deteleHead;
	}
	
	public boolean updateVersion(String appName, String refApp, String version) {
		return flowControlParamDao.updateVersion(appName, refApp, version);
	}
	
	public boolean updateVersion(String appName, String version) {
		return flowControlParamDao.updateVersionByName(appName, version);
	}
	
	public int add4Test(String appName, String flowApp, String paramInfo, int limitFlow) {
		String user = "youji.zj@taobao.com";
		String version = LocalUtil.generateVersion();
		
		FlowControlParamPo po = new FlowControlParamPo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setParamInfo(paramInfo);
		po.setRefAppName(flowApp);
		po.setLimitFlow(limitFlow);
		po.setUser(user);
		po.setVersion(version);
		
		flowControlParamDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		int count = addOrUpdateRefIps(po);
		
		return count;
	}
	
	private int addOrUpdateRefIps(FlowControlParamPo po) {
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
