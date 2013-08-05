package com.taobao.monitor.alarm.opsfree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.po.HSFProviderHosts;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.web.core.dao.impl.MonitorAlarmDao;
import com.taobao.monitor.web.vo.AlarmDataPo;

/**
 * 需要启动一段时间后再运行，等待keyMap的加载
 * @author baiyan
 *
 */
public class GetAppHostAmountForConfigServerRunnable implements Runnable{
	private static final Logger logger =  Logger.getLogger(GetAppHostAmountForConfigServerRunnable.class);
	private static List<String> appNameList = new ArrayList<String>();
	private static List<String> hostCMList = new ArrayList<String>();
	private static String configServerSysName = "configserver";
	private MonitorAlarmDao dao = new MonitorAlarmDao();
	private static Map<String,HSFProviderHosts> appHostMap = new HashMap<String,HSFProviderHosts>();
	
	static{
		appNameList.add("tradeplatform");
		appNameList.add("itemcenter");
		appNameList.add("uicfinal");
		
		hostCMList.add("_TotalCnt");
		hostCMList.add("_CM3Cnt");
		hostCMList.add("_CM4Cnt");
		hostCMList.add("_CM5Cnt");
	}
	
	@Override
	public void run() {
		for(String appName:appNameList){
			 HSFProviderHosts host = OpsfreeJsonUtil.getHostsByAppname(appName);
			 if(host == null){
				 logger.warn("host from opsFree is null,appName=" + appName);
				 continue;
			 }
			 appHostMap.put(appName, host);
			 for(String hostCM:hostCMList){
				 String totalAmountKey = "ConfigServer_" + host.getSystemName() + hostCM;
				 KeyPo keyPo = KeyAo.get().getKeyByName(totalAmountKey);
				
				 if(keyPo !=null){
					 AlarmDataPo po = dao.findAlarmKeyDefByAppIdAndKeyId(AppInfoAo.get().getAppInfoName2IdMap().get(configServerSysName),keyPo.getKeyId());
					 if(po!=null){
						 //设置新的阀值范围，按照推送地址列表数目不少于总体的80%
						 int count = getCountByCM(hostCM,host);
						 int minTotal = (int) (count*0.8);
						 int maxTotal = count;
						 //2#2$00:00#23:59;
						 String alarmDefine =minTotal + "#" +maxTotal + "$" +  "00:00#23:59;";
						 po.setAlarmDefine(alarmDefine);
						 boolean result = dao.updateKeyAlarm(po);
						 if(!result){
							 logger.warn("updateKeyAlarm is not success,appId=" + po.getAppId() + ",alarmDefine=" + po.getAlarmDefine() + ",keyId=" + 
									 po.getKeyId());
						 }
					 }else{
						 Log.warn("po from dao.findAlarmKeyDefByAppIdAndKeyId is null,appId=" + AppInfoAo.get().getAppInfoName2IdMap().get(configServerSysName) + ",keyId=" + keyPo.getKeyId());
					 }
				 }
			 }
		}
	}

	private int getCountByCM(String hostCM,HSFProviderHosts host ){
		if("_TotalCnt".equals(hostCM)) return host.getTotalCnt();
		if("_CM3Cnt".equals(hostCM)) return host.getCm3Cnt();
		if("_CM4Cnt".equals(hostCM)) return host.getCm4Cnt();
		else return host.getCm5Cnt();
		
		
	}
	
}