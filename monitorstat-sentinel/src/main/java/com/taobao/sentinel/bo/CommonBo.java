package com.taobao.sentinel.bo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.monitor.common.util.PropertiesConfig;
import com.taobao.sentinel.constant.ConfigType;
import com.taobao.sentinel.constant.FlowControlType;
import com.taobao.sentinel.dao.ConfigVersionDao;
import com.taobao.sentinel.dao.DataUrlDao;
import com.taobao.sentinel.dao.QpsPeriodDao;
import com.taobao.sentinel.dao.RefIpDao;
import com.taobao.sentinel.dao.UserPermissionDao;
import com.taobao.sentinel.po.BaseConfigPo;
import com.taobao.sentinel.po.ConfigVersionPo;
import com.taobao.sentinel.po.DataUrlPo;
import com.taobao.sentinel.po.QpsPeriodPo;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.po.UserPermissionPo;
import com.taobao.sentinel.pull.DisplayObject;
import com.taobao.sentinel.push.DiamondPusherFactory;
import com.taobao.sentinel.push.IPusher;
import com.taobao.sentinel.push.PusherFactory;
import com.taobao.sentinel.util.AppGroupUtil;
import com.taobao.sentinel.util.LocalUtil;

/***
 * business class for common use
 * 
 * 1. generate configuration
 * 2. push configuration
 * 3. configuration version management
 * 4. ips from ops free
 * 5. permission management
 * 6. application data url management
 * 
 * @author youji.zj
 *
 */
public class CommonBo {
	
	public static Logger logger = Logger.getLogger(CommonBo.class);
	
	@Resource(name = "whiteListInterfaceBo")
	private WhiteListInterfaceBo whiteListInterfaceBo;
	
	@Resource(name = "blackListInterfaceBo")
	private BlackListInterfaceBo blackListInterfaceBo;
	
	@Resource(name = "blackListAppBo")
	private BlackListAppBo blackListAppBo;
	
	@Resource(name = "whiteListCustomerBo")
	private WhiteListCustomerBo whiteListCustomerBo;
	
	@Resource(name = "blackListCustomerBo")
	private BlackListCustomerBo blackListCustomerBo;
	
	@Resource(name = "flowControlInterfaceBo")
	private FlowControlInterfaceBo flowControlInterfaceBo;
	
	@Resource(name = "flowControlAppBo")
	private FlowControlAppBo flowControlAppBo;
	
	@Resource(name = "flowControlDependencyBo")
	private FlowControlDependencyBo flowControlDependencyBo;
	
	@Resource(name = "flowControlParamBo")
	private FlowControlParamBo flowControlParamBo;
	
	@Resource(name = "configVersionDao")
	private ConfigVersionDao configVersionDao;
	
	@Resource(name = "userPermissionDao")
	private UserPermissionDao userPermissionDao;
	
	@Resource(name = "dataUrlDao")
	private DataUrlDao dataUrlDao;
	
	@Resource(name = "refIpDao")
	private RefIpDao refIpDao;
	
	@Resource(name = "qpsPeriodDao")
	private QpsPeriodDao qpsPeriodDao;
	
	public QpsPeriodDao getQpsPeriodDao() {
		return qpsPeriodDao;
	}

	public void setQpsPeriodDao(QpsPeriodDao qpsPeriodDao) {
		this.qpsPeriodDao = qpsPeriodDao;
	}

	public WhiteListInterfaceBo getWhiteListInterfaceBo() {
		return whiteListInterfaceBo;
	}


	public void setWhiteListInterfaceBo(WhiteListInterfaceBo whiteListInterfaceBo) {
		this.whiteListInterfaceBo = whiteListInterfaceBo;
	}


	public BlackListInterfaceBo getBlackListInterfaceBo() {
		return blackListInterfaceBo;
	}


	public void setBlackListInterfaceBo(BlackListInterfaceBo blackListInterfaceBo) {
		this.blackListInterfaceBo = blackListInterfaceBo;
	}

	public BlackListAppBo getBlackListAppBo() {
		return blackListAppBo;
	}


	public void setBlackListAppBo(BlackListAppBo blackListAppBo) {
		this.blackListAppBo = blackListAppBo;
	}
	

	public WhiteListCustomerBo getWhiteListCustomerBo() {
		return whiteListCustomerBo;
	}


	public void setWhiteListCustomerBo(WhiteListCustomerBo whiteListCustomerBo) {
		this.whiteListCustomerBo = whiteListCustomerBo;
	}


	public BlackListCustomerBo getBlackListCustomerBo() {
		return blackListCustomerBo;
	}


	public void setBlackListCustomerBo(BlackListCustomerBo blackListCustomerBo) {
		this.blackListCustomerBo = blackListCustomerBo;
	}
	

	public FlowControlInterfaceBo getFlowControlInterfaceBo() {
		return flowControlInterfaceBo;
	}
	

	public FlowControlParamBo getFlowControlParamBo() {
		return flowControlParamBo;
	}

	public void setFlowControlParamBo(FlowControlParamBo flowControlParamBo) {
		this.flowControlParamBo = flowControlParamBo;
	}

	public void setFlowControlInterfaceBo(
			FlowControlInterfaceBo flowControlInterfaceBo) {
		this.flowControlInterfaceBo = flowControlInterfaceBo;
	}


	public FlowControlAppBo getFlowControlAppBo() {
		return flowControlAppBo;
	}


	public void setFlowControlAppBo(FlowControlAppBo flowControlAppBo) {
		this.flowControlAppBo = flowControlAppBo;
	}


	public FlowControlDependencyBo getFlowControlDependencyBo() {
		return flowControlDependencyBo;
	}


	public void setFlowControlDependencyBo(
			FlowControlDependencyBo flowControlDependencyBo) {
		this.flowControlDependencyBo = flowControlDependencyBo;
	}
	
	public ConfigVersionDao getConfigVersionDao() {
		return configVersionDao;
	}


	public void setConfigVersionDao(ConfigVersionDao configVersionDao) {
		this.configVersionDao = configVersionDao;
	}
	
	public UserPermissionDao getUserPermissionDao() {
		return userPermissionDao;
	}


	public void setUserPermissionDao(UserPermissionDao userPermissionDao) {
		this.userPermissionDao = userPermissionDao;
	}

	public DataUrlDao getDataUrlDao() {
		return dataUrlDao;
	}


	public void setDataUrlDao(DataUrlDao dataUrlDao) {
		this.dataUrlDao = dataUrlDao;
	}

	public RefIpDao getRefIpDao() {
		return refIpDao;
	}


	public void setRefIpDao(RefIpDao refIpDao) {
		this.refIpDao = refIpDao;
	}


	public String generateConfig(String appName) {
		Map<String, Object> configMap = new LinkedHashMap<String, Object>();
		
		// interface black list
		Map<String, Map<String, Integer>> blackListInterface = blackListInterfaceBo.generateConfig(appName);
		configMap.put(ConfigType.BLACK_LIST_INTERFACE.getKey(), blackListInterface);
		
		// interface white list
		Map<String, Map<String, Integer>> whiteListInterface = whiteListInterfaceBo.generateConfig(appName);
		configMap.put(ConfigType.WHITE_LIST_INTERFACE.getKey(), whiteListInterface);
		
		// application black list
		Map<String, Map<String, Integer>> blackListApp = blackListAppBo.generateConfig(appName);
		configMap.put(ConfigType.BLACK_LIST_APP.getKey(), blackListApp);
					
		// customization black list
		Map<String, Map<String, Integer>> blackListCustomer = blackListCustomerBo.generateConfig(appName);
		configMap.put(ConfigType.BLACK_LIST_CUSTOMER.getKey(), blackListCustomer);
		
		// customization white list
		Map<String, Map<String, Integer>> whiteListCustomer = whiteListCustomerBo.generateConfig(appName);
		configMap.put(ConfigType.WHITE_LIST_CUSTOMER.getKey(), whiteListCustomer);
		
		// interface flow control
		Map<String, Map<String, Integer>> flowControlInterface = flowControlInterfaceBo.generateConfig(appName, FlowControlType.THREAD);
		configMap.put(ConfigType.FLOW_CONTROL_INTERFACE.getKey(), flowControlInterface);
		
		// application flow control
		Map<String, Map<String, Integer>> flowControlApp = flowControlAppBo.generateConfig(appName, FlowControlType.THREAD);
		configMap.put(ConfigType.FLOW_CONTROL_APP.getKey(), flowControlApp);
		
		// dependency flow control
		Map<String, Map<String, Integer>> flowControlDependency = flowControlDependencyBo.generateConfig(appName);
		configMap.put(ConfigType.FLOW_CONTROL_DEPENDENCY.getKey(), flowControlDependency);
		
		// interface flow control qps
		Map<String, Map<String, Integer>> flowControlInterfaceQps = flowControlInterfaceBo.generateConfig(appName, FlowControlType.QPS);
		configMap.put(ConfigType.QPS_INTERFACE.getKey(), flowControlInterfaceQps);
		
		// application flow control qps
		Map<String, Map<String, Integer>> flowControlAppQps = flowControlAppBo.generateConfig(appName, FlowControlType.QPS);
		configMap.put(ConfigType.QPS_APP.getKey(), flowControlAppQps);
		
		// param flow control
		Map<String, Map<String, Integer>> flowControlParam = flowControlParamBo.generateConfig(appName);
		configMap.put(ConfigType.FLOW_CONTROL_PARAM.getKey(), flowControlParam);
		
		// qps period
		int qpsPeriod = this.findPeriod(appName);
		configMap.put(ConfigType.QPS_PERIOD.getKey(), qpsPeriod);
		
		// groups
		Map<String, List<String>> groupInfo = AppGroupUtil.getGroupInfo(appName);
		configMap.put(ConfigType.GROUPS.getKey(), groupInfo);
		
		// ips
		Map<String, List<String>> refAppIps = findRefApps(appName);
		configMap.put(ConfigType.IPS.getKey(), refAppIps);
		
		return JSONObject.fromObject(configMap).toString();
	}

	/***
	 * push config manual
	 * @param request
	 * @param appName
	 * @return
	 */
	public boolean pushConfig(HttpServletRequest request, String appName) {
		String configInfo = generateConfig(appName);
		
		PusherFactory pusherFactory = new DiamondPusherFactory();
		IPusher pusher = pusherFactory.createPush();
		boolean success =  pusher.pushConfig(appName, configInfo);
		
		// generate push record
		String id = LocalUtil.generateId();
		String user = LocalUtil.getCurrentUser(request);
		String version = LocalUtil.generateVersion();
		configVersionDao.addConfigVersion(id, appName, user, version);
		
		// synchronize cofnig version
		if (success) {
			SyncThread syncThread = new SyncThread(appName, version);
			syncThread.start();
		}
		
		return success;
	}
	
	/***
	 * system auto push config
	 * @param appName
	 * @return
	 */
	public boolean autoPushConfig(String appName) {
		String configInfo = generateConfig(appName);
		
		PusherFactory pusherFactory = new DiamondPusherFactory();
		IPusher pusher = pusherFactory.createPush();
		boolean success =  pusher.pushConfig(appName, configInfo);
		
		// generate push record
		String id = LocalUtil.generateId();
		String user = "system";
		String version = LocalUtil.generateVersion();
		configVersionDao.addConfigVersion(id, appName, user, version);
		
		// synchronize cofnig version
		if (success) {
			SyncThread syncThread = new SyncThread(appName, version);
			syncThread.start();
		}
		
		return success;
	}
	
	public List<DisplayObject> appIpsFromOpsFree(String appName) {
		List<DisplayObject> list = new ArrayList<DisplayObject>();
		if (StringUtils.isBlank(appName)) {
			return list;
		}
		
		List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(appName);
		Set <String> ips = new HashSet<String>();
		for (HostPo host : hosts) {
			ips.add(host.getHostIp());
		}
		
		int order = 1;
		for (String ip : ips) {
			DisplayObject displayObject = new DisplayObject();
			displayObject.setOrder(order++);
			displayObject.setIp(ip);
			list.add(displayObject);
		}
		
		return list;
	}
	
	public boolean alterFlowThread(String id, String type, String thread) {
		boolean success = false;
		if (ConfigType.valueOf(type) == ConfigType.FLOW_CONTROL_APP) {
			success = flowControlAppBo.updateLimitFlow(id, Integer.parseInt(thread));
		}
		
		if (ConfigType.valueOf(type) == ConfigType.FLOW_CONTROL_INTERFACE) {
			success = flowControlInterfaceBo.updateLimitFlow(id, Integer.parseInt(thread));
		}
		
		if (ConfigType.valueOf(type) == ConfigType.FLOW_CONTROL_DEPENDENCY) {
			success = flowControlDependencyBo.updateLimitFlow(id, Integer.parseInt(thread));
		}
		
		if (ConfigType.valueOf(type) == ConfigType.FLOW_CONTROL_PARAM) {
			success = flowControlParamBo.updateLimitFlow(id, Integer.parseInt(thread));
		}
		
		return success;
	}
	
	public boolean changeState(String id, String type) {
		boolean success = false;
		
		if (ConfigType.valueOf(type) == ConfigType.WHITE_LIST_INTERFACE) {
			success = whiteListInterfaceBo.changeIps(id);
		}
		
		if (ConfigType.valueOf(type) == ConfigType.BLACK_LIST_INTERFACE) {
			success = blackListInterfaceBo.changeIps(id);
		}
		
		if (ConfigType.valueOf(type) == ConfigType.BLACK_LIST_APP) {
			success = blackListAppBo.changeIps(id);
		}
		
		if (ConfigType.valueOf(type) == ConfigType.WHITE_LIST_CUSTOMER) {
			success = whiteListCustomerBo.changeIps(id);
		}
		
		if (ConfigType.valueOf(type) == ConfigType.BLACK_LIST_CUSTOMER) {
			success = blackListCustomerBo.changeIps(id);
		}
		
		if (ConfigType.valueOf(type) == ConfigType.FLOW_CONTROL_APP) {
			success = flowControlAppBo.changeIps(id);
		}
		
		if (ConfigType.valueOf(type) == ConfigType.FLOW_CONTROL_INTERFACE) {
			success = flowControlInterfaceBo.changeIps(id);
		}
		
		if (ConfigType.valueOf(type) == ConfigType.FLOW_CONTROL_DEPENDENCY) {
			success = flowControlDependencyBo.changeIps(id);
		}
		
		if (ConfigType.valueOf(type) == ConfigType.FLOW_CONTROL_PARAM) {
			success = flowControlParamBo.changeIps(id);
		}
		
		return success;
	}
	
	public Set<String> findAllApps() {
		Set<String> appSet = new HashSet<String>();
		
		List<BaseConfigPo> all = new ArrayList<BaseConfigPo>();
		all.addAll(whiteListInterfaceBo.findAll());
		all.addAll(blackListInterfaceBo.findAll());
		all.addAll(blackListAppBo.findAll());
		all.addAll(whiteListCustomerBo.findAll());
		all.addAll(blackListCustomerBo.findAll());
		all.addAll(flowControlInterfaceBo.findAll(FlowControlType.THREAD));
		all.addAll(flowControlAppBo.findAll(FlowControlType.THREAD));
		all.addAll(flowControlInterfaceBo.findAll(FlowControlType.QPS));
		all.addAll(flowControlAppBo.findAll(FlowControlType.QPS));
		all.addAll(flowControlDependencyBo.findAll());
		all.addAll(flowControlParamBo.findAll());
		
		for (BaseConfigPo po : all) {
			appSet.add(po.getAppName());
		}
			
		return appSet;
	}
	
	public Set<String> findAllRefApps() {
		Set<String> appSet = new HashSet<String>();
		
		List<BaseConfigPo> all = new ArrayList<BaseConfigPo>();
		all.addAll(whiteListInterfaceBo.findAll());
		all.addAll(blackListInterfaceBo.findAll());
		all.addAll(blackListAppBo.findAll());
		all.addAll(whiteListCustomerBo.findAll());
		all.addAll(blackListCustomerBo.findAll());
		all.addAll(flowControlInterfaceBo.findAll(FlowControlType.THREAD));
		all.addAll(flowControlAppBo.findAll(FlowControlType.THREAD));
		all.addAll(flowControlDependencyBo.findAll());
		
		for (BaseConfigPo po : all) {
			appSet.add(po.getAppName());
		}
			
		return appSet;
	}
	
	public Map<String, List<String>> findRefApps(String appName) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		Map<String, List<RefIpPo>>  refIdIps = refIpDao.findAppRefIps(appName);
		
		for (Map.Entry<String, List<RefIpPo>> entry : refIdIps.entrySet()) {
			String refApp = LocalUtil.getRefAppFromRefId(entry.getKey());
			List<RefIpPo> list = entry.getValue();
			
			List<String> ips =  new ArrayList<String>(); 
			for (RefIpPo po : list) {
				ips.add(po.getIp());
			}
			
			map.put(refApp, ips);
		}
			
		return map;
	}
	
	public List<ConfigVersionPo> findRecentConfigVersion() {
		return configVersionDao.findRecentConfigVersion();
	}
	
	public List<UserPermissionPo> findAllUserPsermission() {
		return userPermissionDao.findAllUserPermissions();
	}
	
	public void addUserPermission (UserPermissionPo po) {
		userPermissionDao.add(po);
	}
	
	public List<DataUrlPo> findAllDataUrl() {
		return dataUrlDao.findAll();
	}
	
	public List<QpsPeriodPo> findAllQpsPeriod() {
		return 	qpsPeriodDao.findQpsPeriods();
	}
	
	
	public DataUrlPo findDataUrl(String appName) {
		return dataUrlDao.findDataUrl(appName);
	}
	
	public  int findPeriod(String appName) {
		return qpsPeriodDao.findPeriod(appName);
	}
	
	public boolean alterDataUrl (String appName, String dataUrl) {
		return dataUrlDao.updateDataUrl(appName, dataUrl);
	}
	
	public boolean alterPeriod (String appName, int period) {
		return qpsPeriodDao.updatePeriod(appName, period);
	}
	
	class SyncThread extends Thread {
		
		private String appName;
		
		private String version;
		
		
		public SyncThread(String appName, String version) {
			this.appName = appName;
			this.version = version;
		}
		
		public void run() {
			whiteListInterfaceBo.updateVersion(appName, version);
			blackListInterfaceBo.updateVersion(appName, version);
			blackListAppBo.updateVersion(appName, version);
			whiteListCustomerBo.updateVersion(appName, version);
			blackListCustomerBo.updateVersion(appName, version);
			flowControlInterfaceBo.updateVersion(appName, version);
			flowControlAppBo.updateVersion(appName, version);
			flowControlDependencyBo.updateVersion(appName, version);
		}
	}
}


