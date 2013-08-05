package com.taobao.csp.time.web.action;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taobao.csp.time.job.ResponseTimeJob;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.util.CommonUtil;
import com.taobao.monitor.common.util.GroupManager;

@Controller
@RequestMapping("/app/mannual.do")
public class ManualTriggerAction extends BaseController {
	private static Logger logger = Logger.getLogger(ManualTriggerAction.class);

	//�ֶ�������ִ���ض�һ��ļ��㡣
	@RequestMapping(params="method=calculateRTByTime")
	public void calculateRTByTime(String timeString, HttpServletResponse response) {
		String msg = "���óɹ�";
		Map<String,String> map = new HashMap<String,String>();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date triggerTime = null;
		try{
			triggerTime = sdf.parse(timeString);
			new ResponseTimeJob().calculateNowByTime(triggerTime);
		}catch (Exception e) {
			triggerTime = new Date();
			logger.error("�����ʱ���쳣" ,e);
			msg = "�����ʱ��ת���쳣->" + e.toString();
		}
		
		map.put("msg", msg);
		try {
			this.writeJSONToResponseJSONObject(response, map);
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
//	@RequestMapping(params="method=calculateGroup")
//	public void calculateGroup(String timeString, HttpServletResponse response) {
//		String msg = "���óɹ�";
//		Map<String,String> map = new HashMap<String,String>();
//		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		Date triggerTime = null;
//		try{
//			triggerTime = sdf.parse(timeString);
//			new GroupCalculateJob().calculateGroupInfoByTime(triggerTime);
//		}catch (Exception e) {
//			triggerTime = new Date();
//			logger.error("�����ʱ���쳣" ,e);
//			msg = "�����ʱ��ת���쳣->" + e.toString();
//		}
//		
//		map.put("msg", msg);
//		try {
//			this.writeJSONToResponseJSONObject(response, map);
//		} catch (IOException e) {
//			logger.error("", e);
//		}
//	}	
	/**
	 * ��鵱ǰ�������������Ӧ�õ�CSP
	 * @param response
	 */
	@RequestMapping(params="method=addGroupAppAuto")
	public void addGroupAppAuto(HttpServletResponse response) {
		StringBuilder sb = new StringBuilder();
		GroupManager manager = GroupManager.get();
		Map<String, Map<String, List<String>>> groupMap = manager.getGroupInfo();
		for(String appName: groupMap.keySet()) {
			AppInfoPo appPo = AppInfoAo.get().getAppInfoByAppName(appName);
			if(appPo == null) {
				sb.append("appName=" + appName + "������ЧӦ�õ��У�������Ϣ�����!\n");
				continue;
			}
			Set<String> groupSet = groupMap.get(appName).keySet();
			for(String groupName:groupSet) {
				String combineApp = CommonUtil.combinAppNameAndGroupName(appName, groupName);
				AppInfoPo combinePo = AppInfoAo.get().getAppInfoByAppName(combineApp);
				if(combinePo != null) {
					sb.append("combineApp=" + combineApp + "�Ѵ��ڣ������!\n");
				} else {
					AppInfoPo po = new AppInfoPo();
					po.setAppName(combineApp);
					po.setSortIndex(appPo.getSortIndex());
					po.setFeature(appPo.getFeature());
					po.setOpsName(combineApp);
					po.setGroupName(appPo.getGroupName());
					po.setOpsField(appPo.getOpsField());
					//�ձ���ʵʱ��Ĭ�ϲ���Ч��Important
					po.setDayDeploy(0);
					po.setTimeDeploy(0);
					po.setLoginName(appPo.getLoginName());
					po.setLoginPassword(appPo.getLoginPassword());
					po.setAppRushHours(appPo.getAppRushHours());
					po.setAppType(appPo.getAppType());
					boolean b = AppInfoAo.get().addAppInfoData(po);
					if(b) {
						sb.append("combineApp=" + combineApp + "��ӳɹ�!\n");
					} else {
						sb.append("combineApp=" + combineApp + "���ʧ��!\n");
					}
				}
			}
		}
		try {
			this.writeToResponse(response, sb.toString());
		} catch (IOException e) {
			logger.error("", e);
		}
	}
	
	@RequestMapping(params="method=syncCspCacheHostInfo")
	public void syncCspCacheHostInfo(HttpServletResponse response){
		StringBuilder sb = new StringBuilder();
		sb.append("��ʼͬ��������Ϣ<br/>");
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			String ip=addr.getHostAddress().toString();//��ñ���IP
			String address=addr.getHostName().toString();//��ñ�������
			sb.append("ip=" + ip + ";address=" + address + "<br/>");
		} catch (UnknownHostException e) {
			sb.append("Exception:" + e.toString() + "<br/>");
		}
		try {
			this.writeToResponse(response, sb.toString());
		} catch (IOException e) {
			logger.error("", e);
		}
		
	}
	
//	@RequestMapping(params="method=test")
//	public void test(HttpServletResponse response){
//		HsfTransfer.getInstance().formatTranser("", new ArrayList());
//	}
}
