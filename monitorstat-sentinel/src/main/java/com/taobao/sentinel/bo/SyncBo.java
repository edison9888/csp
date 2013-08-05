package com.taobao.sentinel.bo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.sentinel.dao.RefIpDao;
import com.taobao.sentinel.po.RefIpPo;
import com.taobao.sentinel.util.LocalUtil;

public class SyncBo {
	
	public static Logger logger = Logger.getLogger(SyncBo.class);
	
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
	
	@Resource(name = "refIpDao")
	private RefIpDao refIpDao;
	
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

	public RefIpDao getRefIpDao() {
		return refIpDao;
	}

	public void setRefIpDao(RefIpDao refIpDao) {
		this.refIpDao = refIpDao;
	}

	public Set<String> synchApp(String appName) {
		Map<String, List<RefIpPo>> refIdIps = refIpDao.findAppRefIps(appName);
		Set<String> refIds = refIdIps.keySet();
		Set<String> changeRefApps = new HashSet<String>();
		
		for (String refId : refIds) {
			int updated = 0;
			List<RefIpPo> list = refIdIps.get(refId);
			String refApp = LocalUtil.getRefAppFromRefId(refId);
			
			List<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(refApp);
			Set <String> ips = new HashSet<String>();
			for (HostPo host : hosts) {
				ips.add(host.getHostIp());
			}
			
			if (ips == null || ips.size() == 0) {
				logger.info("find no ip:" + refId);
				continue;
			}
			
			for (RefIpPo po : list) {
				String ip = po.getIp();
				if (!ips.contains(ip)) {
					String id = po.getId();
					refIpDao.removeRefIpById(id);
					updated ++;
					changeRefApps.add(refApp);
					logger.info("synchApp " + appName + ". Remove ip " + ip + " from " + refApp);
				} else {
					ips.remove(ip);
				}
			}
			
			Iterator<String> iterator = ips.iterator();
			while (iterator.hasNext()) {
				String newIp = iterator.next();
				refIpDao.addRefIp(LocalUtil.generateId(), newIp, refId);
				updated ++;
				changeRefApps.add(refApp);
				logger.info("synchApp " + appName + ". Add ip " + newIp + " from " + refApp);
			}
			
			if (updated > 0) {
				SyncThread syncThread = new SyncThread(appName, refApp, LocalUtil.generateVersion());
				syncThread.start();
			}
		}
		
		return changeRefApps;
	}
	
	class SyncThread extends Thread {
		
		private String appName;
		
		private String version;
		
		private String refApp;
		
		
		public SyncThread(String appName, String refApp, String version) {
			this.appName = appName;
			this.refApp = refApp;
			this.version = version;
		}
		
		public void run() {
			whiteListInterfaceBo.updateVersion(appName, refApp, version);
			blackListInterfaceBo.updateVersion(appName, refApp, version);
			blackListAppBo.updateVersion(appName, refApp, version);
			whiteListCustomerBo.updateVersion(appName, refApp, version);
			blackListCustomerBo.updateVersion(appName, refApp, version);
			flowControlInterfaceBo.updateVersion(appName, refApp, version);
			flowControlAppBo.updateVersion(appName, refApp, version);
		}
	}

}
