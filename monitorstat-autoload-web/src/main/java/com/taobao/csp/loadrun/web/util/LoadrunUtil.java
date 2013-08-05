
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
 * @version 2011-8-2 ����10:50:03
 */
public class LoadrunUtil {
	
	public static void checkTargetMessage(LoadrunTarget target) throws Exception{
		if (isTestEnv() || isScriptMode(target)) {
			return;
		}
		
		boolean hasPermission = checkPermission(target.getTargetIp(), target.getTargetUserName(), target.getTargetPassword());
		if (!hasPermission) {
			throw new Exception(target.getTargetUserName() + "û��Ŀ�����" + target.getAppName() + target.getTargetIp() + "��Ȩ��");
		}
		
		AutoLoadType loadtype = target.getLoadrunType();
		// http_loadѹ�ⷽʽ��ȥ����У�飬����ѹԤ������
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
						throw new Exception("apache ѹ���е����� IP:"+s +"�Ѿ�����"+target.getAppName()+"Ӧ���б���");
					}
					
					if (s.equals(ip)) {
						throw new Exception(target.getAppName() + "��������:"+s +"��Ŀ�����"+ip+"��������ͬ");
					}
				}
			}
		}else{
			throw new Exception(ip+"�Ѿ�����"+target.getAppName()+"Ӧ���б���");
		}
	}
	
	public static boolean isTestEnv() {
		boolean isTestEnv = false;
		
		// windows�����ǲ��Ի���
		if (System.getProperty("os.name").toLowerCase().indexOf("window") > -1) {
			isTestEnv = true;
		}
		
		return isTestEnv;
	}
	
	public static boolean isScriptMode(LoadrunTarget target) {
		boolean isScriptMode = false;
		
		// b2b�Ļ���Ŀǰ�޷�����opsfree����
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
