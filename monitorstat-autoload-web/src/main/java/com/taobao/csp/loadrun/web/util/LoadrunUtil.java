
package com.taobao.csp.loadrun.web.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import ch.ethz.ssh2.Connection;

import com.taobao.csp.loadrun.core.LoadrunTarget;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;

/**
 * 
 * @author xiaodu
 * @version 2011-8-2 上午10:50:03
 */
public class LoadrunUtil {
	
	public static void checkTargetMessage(LoadrunTarget target) throws Exception{
		if (isTestEnv() || isScriptMode(target)) {
			return;
		}
		
		boolean hasPermission = checkPermission(target.getTargetIp(), target.getTargetUserName(), target.getTargetPassword());
		if (!hasPermission) {
			throw new Exception(target.getTargetUserName() + "没有目标机器" + target.getAppName() + target.getTargetIp() + "的权限");
		}
		
		AutoLoadType loadtype = target.getLoadrunType();
		// http_load压测方式先去掉此校验，用于压预发环境
		if (AutoLoadType.httpLoad.equals(loadtype)) {
			return;
		}
		
		Collection<HostPo> hosts = CspSyncOpsHostInfos.findOnlineHostByOpsName(target.getOpsName());
		Map<String,HostPo> map = new HashMap<String, HostPo>();
		for(HostPo po:hosts){
			map.put(po.getHostIp(), po);
		}
		String ip = target.getTargetIp();
		HostPo po = map.get(ip);
		if(po!= null){
			if(AutoLoadType.apache.equals(loadtype) || AutoLoadType.apacheProxy.equals(loadtype) || AutoLoadType.nginxProxy.equals(loadtype)){
				String load = target.getLoadFeature();
				String[] ips = load.split(",");
				for(String s:ips){
					if(map.get(s)== null){
						throw new Exception("apache 压测中的配置 IP:"+s +"已经不在"+target.getAppName()+"应用列表里");
					}
					
					if (s.equals(ip)) {
						throw new Exception(target.getAppName() + "分流机器:"+s +"与目标机器"+ip+"不允许相同");
					}
				}
			}
		}else{
			throw new Exception(ip+"已经不在"+target.getAppName()+"应用列表里");
		}
	}
	
	public static boolean isTestEnv() {
		boolean isTestEnv = false;
		
		// windows环境是测试环境
		if (System.getProperty("os.name").toLowerCase().indexOf("window") > -1) {
			isTestEnv = true;
		}
		
		return isTestEnv;
	}
	
	public static boolean isScriptMode(LoadrunTarget target) {
		boolean isScriptMode = false;
		
		// b2b的机器目前无法听过opsfree查找
		if (target.getMode() == AutoLoadMode.SCRIPT) {
			isScriptMode = true;
		}
		
		return isScriptMode;
	}
	
	public static boolean checkPermission(String ip, String userName, String pwd) {
		Connection targetSSHConn = new Connection(ip);
		try {
			targetSSHConn.connect(null, 10000, 10000);
			boolean isAuthenticated = targetSSHConn.authenticateWithPassword(userName, pwd);
			
			if (!isAuthenticated) {
				return false;
			} 
		} catch (Exception e) {
			return false;
		} finally {
			targetSSHConn.close();
		}
		return true;
	}

}
