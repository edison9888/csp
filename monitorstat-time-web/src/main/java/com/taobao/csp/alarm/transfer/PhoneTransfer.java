package com.taobao.csp.alarm.transfer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.alarm.ao.MonitorAlarmAo;
import com.taobao.csp.alarm.po.AlarmContext;
import com.taobao.csp.alarm.po.AlarmSendPo;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.util.PropNameDescUtil;
import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.messagesend.MessageSend;
import com.taobao.monitor.common.messagesend.MessageSendFactory;
import com.taobao.monitor.common.messagesend.MessageSendType;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * 
 * @author xiaodu
 * @version 2011-2-12 下午02:27:28
 */
public class PhoneTransfer extends Transfer {

	private static final Logger logger = Logger.getLogger(PhoneTransfer.class);
	private static PhoneTransfer phone = new PhoneTransfer("PhoneTransfer");

	public static PhoneTransfer getInstance() {
		return phone;
	}

	private MessageSend messageSend = MessageSendFactory.create(MessageSendType.Phone);

	private PhoneTransfer(String name) {
		super(name);
	}

	@Override
	public String formatTranser(String targetSend, List<AlarmContext> targetDataList) {
		String message = "";
		if (targetDataList.size() < 1) {
			return null;
		}
		AlarmContext tmp = targetDataList.get(0);
		if(!isNeedSend(targetSend,tmp.getAppName(),tmp.getKeyName(),tmp.getProperty())){
			logger.info(targetSend + "---" + tmp.getAppName() + "---" + tmp.getKeyName() + "--" + tmp.getProperty() + "过滤发送");
			return null;
		}

		int hostsip = 0;
		Set<String> tmpip = new HashSet<String>();
		for (AlarmContext alarmContext : targetDataList) {
			if (alarmContext.getIp() != null) {
				tmpip.add(alarmContext.getIp());
			}
		}

		List<String> ipList = CspCacheTBHostInfos.get().getIpsListByOpsName(tmp.getAppName());

		List<CspTimeKeyAlarmRecordPo> list = CspTimeKeyAlarmRecordAo.get().findRecentlyAlarmInfo(tmp.getAppName(), tmp.getKeyName(), tmp.getProperty(), "HOST",
				3);

		for (CspTimeKeyAlarmRecordPo po : list) {
			tmpip.add(po.getIp());
		}
		hostsip = tmpip.size();

		//		CspKeyInfo key = KeyCache.getCache().getKeyInfo(tmp.getKeyName());
		int level = 2;
		if(NumberUtils.isNumber(tmp.getKeyLevel())){
			try {
				level = Integer.parseInt(tmp.getKeyLevel());
			} catch (Exception e) {
				logger.error("", e);
				level = 2;
			}
		}
		if (level > 1) {

			int y = ipList.size() / 10 > 10 ? 10 : ipList.size() / 10;
			if (ipList.size() <= 10) {
				y = 2;
			}
			if (hostsip < y) {
				logger.info("过滤不够告警机器数量" + tmp.getAppName() + "#" + tmp.getKeyName() + "#" + tmp.getProperty() + " 机器数量:" + ipList.size() + "告警机器数"
						+ list.size());
				Iterator<AlarmContext> it = targetDataList.iterator();
				while (it.hasNext()) {
					AlarmContext context = it.next();
					if (context.getIp() != null) {
						it.remove();
					}
				}
			}
		}

		if (targetDataList.size() < 1) {
			return null;
		}

		List<AlarmContext> hostList = new ArrayList<AlarmContext>();
		List<AlarmContext> appList = new ArrayList<AlarmContext>();
		for (AlarmContext data : targetDataList) {
			if (data.getIp() == null) {
				appList.add(data);
			} else {
				hostList.add(data);
			}
		}

		for (AlarmContext data : hostList) {
			message = "应用[" + data.getAppName() + "]KEY[" + (StringUtils.isBlank(data.getKeyAlias())?data.getKeyName():data.getKeyAlias()) + "]属性[" + PropNameDescUtil.getDesc(data.getProperty()) + "]";
			break;
		}

		for (AlarmContext data : hostList) {
			message += "单台值:" + data.getValue() + "原因:" + data.getRangeMessage();
			message += "全网" + ipList.size() + "台出现告警" + hostsip + "台";
			break;
		}
		for (AlarmContext data : appList) {
			message += "全网值:" + data.getValue() + "原因:" + data.getRangeMessage();
			break;
		}

		if (message.length() <= 160) {
			message = message.replaceAll("<br/>", "");
			messageSend.send(targetSend, "性能监控告警", message);
		} else {
			int index = 1;
			for (int i = 0; i < message.length();) {
				String msg = message.substring(i, i + 160 > message.length() ? message.length() : (i + 160));
				msg = msg.replaceAll("<br/>", "");
				messageSend.send(targetSend, "性能监控告警", "告警" + index + ":" + msg);
				i += 160;
				index++;
			}

		}
		logger.info("phone alarm:" + message);
		AlarmContext data = targetDataList.get(0);
		AlarmSendPo alarmSendPo = new AlarmSendPo();
		alarmSendPo.setAcceptTime(new Date());
		alarmSendPo.setAppId(data.getAppId());
		alarmSendPo.setAlarmMsg(data.getRangeMessage());
		alarmSendPo.setAlarmType("phone");
		alarmSendPo.setTargetAim(targetSend);
		MonitorAlarmAo.get().addAlarmSend(alarmSendPo);
		return null;
	}

	private Map<String, PhSend> sendRecordMap = new HashMap<String, PhSend>();

	private class PhSend {

		private long recentlyTime = 0;

		private int recentlyAlarmNum = 2;

	}

	/**
	 * 相同的告警连续发送2次，3次后简单30分钟再次发送
	 * 
	 * @author xiaodu
	 * @param targetWw
	 * @return TODO
	 */
	public boolean isNeedSend(String target,String app,String keyName,String property){

		String targetWw = target+","+app+","+keyName+","+property;

		int s = 30;
		if(PropConstants.SWAP.equals(property)){
			s = 120;
		}

		PhSend ww = sendRecordMap.get(targetWw);
		if (ww == null) {
			ww = new PhSend();
			ww.recentlyAlarmNum = 1;
			ww.recentlyTime = System.currentTimeMillis();
			sendRecordMap.put(targetWw, ww);
			return true;
		}

		if (ww.recentlyAlarmNum > 0) {
			ww.recentlyAlarmNum--;
			return true;
		}

		if (System.currentTimeMillis() - ww.recentlyTime < 1000 * s * 60) {
			return false;
		} else {
			ww.recentlyAlarmNum = 1;
			ww.recentlyTime = System.currentTimeMillis();
			return true;
		}

	}

}
