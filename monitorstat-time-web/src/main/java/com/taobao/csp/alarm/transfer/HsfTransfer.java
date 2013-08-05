
package com.taobao.csp.alarm.transfer;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.alarm.ao.MonitorAlarmAo;
import com.taobao.csp.alarm.po.AlarmContext;
import com.taobao.csp.alarm.po.AlarmSendPo;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.util.ApplicationContextHelper;
import com.taobao.csp.time.util.PropNameDescUtil;
import com.taobao.misccenter.domain.Result;
import com.taobao.misccenter.domain.ecard.EcardSellerWarnInfoDTO;
import com.taobao.misccenter.service.EcardSellerService;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;

/**
 * @author zhongting
 */
public class HsfTransfer extends Transfer{

	private static final Logger logger = Logger.getLogger(HsfTransfer.class);


	private static HsfTransfer ww = new HsfTransfer("HsfTransfer");

	private class HSFSend{

		private long recentlyTime=0;

		private int recentlyAlarmNum = 2;

	}

	private Map<String,HSFSend> sendRecordMap = new HashMap<String, HSFSend>();
	
	private EcardSellerService ecardSellerService = null;
	
	public boolean isNeedSend(String target,String app,String keyName,String property){

		String targetWw = target+","+app+","+keyName+","+property;

		int s = 5;
		if(PropConstants.SWAP.equals(property)){
			s = 60;
		}

		HSFSend ww = sendRecordMap.get(targetWw);
		if(ww ==null){
			ww = new HSFSend();
			ww.recentlyAlarmNum = 2;
			ww.recentlyTime = System.currentTimeMillis();
			sendRecordMap.put(targetWw, ww);
			return true;
		}

		if(ww.recentlyAlarmNum >0){
			ww.recentlyAlarmNum--;
			return true;
		}

		if(System.currentTimeMillis()-ww.recentlyTime<1000*s*60){
			return false;
		}else{
			ww.recentlyAlarmNum = 2;
			ww.recentlyTime = System.currentTimeMillis();
			return true;
		}
	}

	public static HsfTransfer getInstance(){
		return ww;
	}

	public void sendExtraMessage(String target,String title,String message){
//
	}

	SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
	private HsfTransfer(String name) {
		super(name);
	}

	@Override
	public String formatTranser(String targetSend, List<AlarmContext> targetDataList) {
		try {
			logger.info(targetSend + "---接收信息" + targetDataList.size());
			if (targetDataList.size() < 1) {
				return null;
			}
			AlarmContext data = targetDataList.get(0);
			int continueTimes = 0;
			Set<String> tmpip = new HashSet<String>();
			for (AlarmContext alarmContext : targetDataList) {
				if (alarmContext.getIp() != null) {
					tmpip.add(alarmContext.getIp());
				}
				continueTimes += alarmContext.getContinuousAlarmTimes();
			}
			String propertyView = PropNameDescUtil.getDesc(data.getProperty());
			if (propertyView == null)
				propertyView = data.getProperty();
			String warnInfo = "";
			//最多显示3个
			int a = 0;
			int b = 0;
			for (int i = 0; i < targetDataList.size(); i++) {
				AlarmContext tmp = targetDataList.get(i);
				if (tmp.getKeyScope().equals("APP")) {
					if (a > 0) {
						continue;
					}
					a++;
					warnInfo += "告警原因:" + tmp.getRangeMessage() + ";";
				}
				if (tmp.getKeyScope().equals("HOST")) {
					if (b > 0) {
						continue;
					}
					b++;
					warnInfo += "告警原因:" + tmp.getRangeMessage() + ";";
				}
			}
			if (ecardSellerService == null) {
				ecardSellerService = (EcardSellerService) ApplicationContextHelper
						.getBean("cpgwService");
				if (ecardSellerService == null) {
					logger.error("ecardSellerService获取失败。ecardSellerService == null");
					return null;
				} else {
					logger.info("ecardSellerService获取成功,ecardSellerService=" + ecardSellerService);
				}
			}
			EcardSellerWarnInfoDTO param = new EcardSellerWarnInfoDTO();
			param.setKey(data.getKeyName());
			param.setKeyDescription((StringUtils.isBlank(data.getKeyAlias()) ? data
					.getKeyName() : data.getKeyAlias()));
			param.setWarnCount(continueTimes);
			param.setWarnInfo(warnInfo);
			Result result = ecardSellerService.ecardSellerWarn(param);
			logger.info("hsf alarm send:----" + param.toString());
			logger.info("hsf alarm return:" + result.toString());
			AlarmSendPo alarmSendPo = new AlarmSendPo();
			alarmSendPo.setAcceptTime(new Date());
			alarmSendPo.setAppId(data.getAppId());
			alarmSendPo.setAlarmMsg(data.getRangeMessage());
			alarmSendPo.setAlarmType("hsf");
			alarmSendPo.setTargetAim(targetSend);
			alarmSendPo.setAlarmMsg(data.getRangeMessage());
			MonitorAlarmAo.get().addAlarmSend(alarmSendPo);
			return null;
		} catch (Exception e) {
			logger.error("发送HSF消息失败", e);
		}
		return null;
	}
}
