package com.taobao.sentinel.bo;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.taobao.sentinel.dao.ConfigVersionDao;
import com.taobao.sentinel.dao.DataUrlDao;
import com.taobao.sentinel.dao.FlowControlDependencyDao;
import com.taobao.sentinel.dao.QpsPeriodDao;
import com.taobao.sentinel.po.BaseConfigPo;
import com.taobao.sentinel.po.ConfigVersionPo;
import com.taobao.sentinel.po.FlowControlDependencyPo;
import com.taobao.sentinel.util.LocalUtil;

/***
 * business class
 * dependency flow control
 * @author youji.zj
 *
 */
public class FlowControlDependencyBo {
	@Resource(name = "flowControlDependencyDao")
	private FlowControlDependencyDao flowControlDependencyDao;
	
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

	
	public FlowControlDependencyDao getFlowControlDependencyDao() {
		return flowControlDependencyDao;
	}

	public void setFlowControlDependencyDao(FlowControlDependencyDao flowControlDependencyDao) {
		this.flowControlDependencyDao = flowControlDependencyDao;
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
	
	public boolean checkExist(String appName, String flowApp, String interfaceInfo) {
		return flowControlDependencyDao.checkExist(appName, flowApp, interfaceInfo);
	}
	
	public boolean updateLimitFlow(String id, int limitFlow) {
		boolean isSuccess = flowControlDependencyDao.updateLimitFlow(id, limitFlow); 
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			flowControlDependencyDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public boolean changeIps(String id) {
		boolean isSuccess = flowControlDependencyDao.updateState(id);
		if (isSuccess) {
			String version = LocalUtil.generateVersion();
			flowControlDependencyDao.updateVersion(id, version);
		}
		return isSuccess;
	}
	
	public List<FlowControlDependencyPo> findAll() {
		List<FlowControlDependencyPo> list = flowControlDependencyDao.findAll();
		
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
	
	public List<FlowControlDependencyPo> find(String appName, boolean includeDisable) {
		return flowControlDependencyDao.find(appName, includeDisable);
	}
	
	public void add(HttpServletRequest request) {
		String appName = request.getParameter("appName").trim();
		String flowApp = request.getParameter("flowApp").trim();
		String interfaceInfo = request.getParameter("interfaceInfo").trim();
		int limitFlow = Integer.parseInt(request.getParameter("limitFlow").trim());
		String user = LocalUtil.getCurrentUser(request);
		String version = LocalUtil.generateVersion();
		
		FlowControlDependencyPo po = new FlowControlDependencyPo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setRefAppName(flowApp);
		po.setInterfaceInfo(interfaceInfo);
		po.setLimitFlow(limitFlow);
		po.setUser(user);
		po.setVersion(version);
		
		flowControlDependencyDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
		qpsPeriodDao.addWhenNotExist(appName);
	}
	
	public Map<String, Map<String, Integer>> generateConfig(String appName) {
		Map<String, Map<String, Integer>> configMap = new LinkedHashMap<String, Map<String, Integer>>();
		
		List<FlowControlDependencyPo> flowControlDependencys = flowControlDependencyDao.find(appName, false);
		

		for (FlowControlDependencyPo flowControlDependency : flowControlDependencys) {
			Map<String, Integer> app = new HashMap<String, Integer>();
			configMap.put(flowControlDependency.getInterfaceInfo(), app);
			app.put("local", flowControlDependency.getLimitFlow());
		}
		
		return configMap;
	}
	
	public boolean deleteConfig(String id) {
		boolean deteleHead = flowControlDependencyDao.remove(id);
		return deteleHead;
	}
	
	public boolean updateVersion(String appName, String version) {
		return flowControlDependencyDao.updateVersionByName(appName, version);
	}
	
	public void add4Test(String appName, String flowApp, String interfaceInfo, int limitFlow) {
		String user = "youji.zj@taobao.com";
		String version = LocalUtil.generateVersion();
		
		FlowControlDependencyPo po = new FlowControlDependencyPo();
		po.setId(LocalUtil.generateId());
		po.setAppName(appName);
		po.setRefAppName(flowApp);
		po.setInterfaceInfo(interfaceInfo);
		po.setLimitFlow(limitFlow);
		po.setUser(user);
		po.setVersion(version);
		
		flowControlDependencyDao.add(po);
		dataUrlDao.addWhenNotExist(LocalUtil.generateId(), appName, LocalUtil.generateDataUrl(appName));
	}
	
}
