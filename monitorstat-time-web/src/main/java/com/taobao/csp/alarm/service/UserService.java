package com.taobao.csp.alarm.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.alarm.ao.MonitorAlarmAo;
import com.taobao.csp.alarm.po.AlarmContext;
import com.taobao.csp.alarm.po.ReportRange;
import com.taobao.csp.alarm.po.UserAcceptInfo;
import com.taobao.csp.alarm.po.UserDefine;
import com.taobao.csp.alarm.transfer.HsfTransfer;
import com.taobao.csp.alarm.transfer.PhoneTransfer;
import com.taobao.csp.alarm.transfer.WangwangTransfer;
import com.taobao.monitor.common.ao.center.CspUserInfoAo;
import com.taobao.monitor.common.po.CspUserInfoPo;

/**
 * ��¼�û�����������Ϣ������û��Ƿ�������
 * 
 * @author xiaodu
 * 
 *         ����1:37:07
 */
public class UserService implements Service<UserDefine> {

	private static Logger logger = Logger.getLogger(UserService.class);

	private static UserService userService = new UserService();
	
	private static Set<String> filterUser = new HashSet<String>();
	static {
		//filterUser.add("����");
	//	filterUser.add("��ͤ");
	}

	private UserService() {
		init();
	}

	private Map<Integer, UserDefine> userMap = new ConcurrentHashMap<Integer, UserDefine>();

	public static UserService get() {
		return userService;
	}

	public void init() {
		userMap.clear();
		List<CspUserInfoPo> poList = CspUserInfoAo.get().findAllCspUserInfo();
		for (CspUserInfoPo po : poList) {
			register(createUserDefine(po));
		}
	}

	public void register(UserDefine define) {
		if (define != null) {
			userMap.put(define.getUserId(), define);
			logger.info("�澯�û�ע��:"+define.getWangwang()+" phone:"+define.getPhone()+" apps:"+define.getAcceptAppIdSet());
		}
	}
	
	

	public void unregister(UserDefine define) {
		if (define != null){
			userMap.remove(define.getUserId());
			logger.info("�澯�û�ȡ��:"+define.getWangwang()+" phone:"+define.getPhone()+" apps:"+define.getAcceptAppIdSet());
		}
	}
	/**
	 * �����û���Ϣ
	 *@author xiaodu
	 * @param po
	 * @return
	 *TODO
	 */
	public UserDefine createUserDefine(CspUserInfoPo po) {

		UserDefine userDefine = new UserDefine();
		userDefine.setEmail(po.getMail());
		userDefine.setPhone(po.getPhone());
		userDefine.setWangwang(po.getWangwang());
		userDefine.setPhoneRangeList(parseReportRange(po.getPhone_feature()));
		userDefine.setWangwangRangeList(parseReportRange(po.getWangwang_feature()));
		userDefine.setUserId(po.getId());
		if (po.getAccept_apps() != null && !"".equals(po.getAccept_apps())) {
			String appIds = po.getAccept_apps();
			Set<Integer> appSet = new HashSet<Integer>();
			for (String appId : appIds.split(",")) {
				try {
					appSet.add(Integer.parseInt(appId));
				} catch (Exception e) {
					logger.info(e);
				}
			}
			userDefine.setAcceptAppIdSet(appSet);
		} else {
			userDefine.setAcceptAppIdSet(new HashSet<Integer>());
		}
		return userDefine;
	}

	/**
	 * ������Χ
	 * 
	 * @param desc
	 * @return
	 */
	private List<ReportRange> parseReportRange(String desc) {
		if (StringUtils.isBlank(desc)) {
			return new ArrayList<ReportRange>();
		}
		List<ReportRange> rangeList = new ArrayList<ReportRange>();
		String[] ranges = desc.split("\\$");
		for (String range : ranges) {
			String[] datas = range.split("\\#");
			if (datas.length == 4) {
				ReportRange r = new ReportRange();
				String times = datas[0];
				String weekday = datas[1];
				String start = datas[2].replace(":", "");
				String end = datas[3].replace(":", "");
				r.setContinueTimes(Integer.parseInt(times));
				r.setWeekDay(Integer.parseInt(weekday));
				r.setStart(Integer.parseInt(start));
				r.setEnd(Integer.parseInt(end));
				rangeList.add(r);
			}
		}
		return rangeList;
	}

	public boolean lookup(AlarmContext context) {

		for (Map.Entry<Integer, UserDefine> entry : userMap.entrySet()) {
			UserDefine define = entry.getValue();
			Set<Integer> appSet = define.getAcceptAppIdSet();
			
			if(!filterUser.contains(define.getWangwang())&&!appSet.contains(context.getAppId())){// �ж�����û��Ƿ� �������Ӧ�ø澯
				continue;
			}
			
			if (StringUtils.isNotBlank(define.getPhone()) ) {
				if (checkIsNeedAlarm(define.getPhoneRangeList(), context)) {
					PhoneTransfer.getInstance().doTranser(define.getPhone(), context);
					UserAcceptInfo userAcceptInfo = new UserAcceptInfo();
					userAcceptInfo.setAppId(context.getAppId());
					userAcceptInfo.setKeyName(context.getKeyName());
					userAcceptInfo.setUserId(define.getUserId());
					userAcceptInfo.setAlarmMsg(context.getRangeMessage());
					userAcceptInfo.setAlarmType("phone");
					userAcceptInfo.setAcceptDate(new Date());
					MonitorAlarmAo.get().addUserAcceptMsg(userAcceptInfo);
					logger.info("�û� phone:"+define.getPhone()+" ���ո澯:"+context.getContinuousAlarmTimes() +" "+context.getAppName()+":"+context.getKeyName()+":"+context.getProperty());
				}else{
					logger.info("�û� phone:"+define.getPhone()+" �����ո澯:"+context.getContinuousAlarmTimes() +" "+context.getAppName()+":"+context.getKeyName()+":"+context.getProperty());
				}
			}
			
			if (StringUtils.isNotBlank(define.getWangwang())) {
				if (checkIsNeedAlarm(define.getWangwangRangeList(), context)) {
					WangwangTransfer.getInstance().doTranser(define.getWangwang(), context);
					UserAcceptInfo userAcceptInfo = new UserAcceptInfo();
					userAcceptInfo.setAppId(context.getAppId());
					userAcceptInfo.setKeyName(context.getKeyName());
					userAcceptInfo.setUserId(define.getUserId());
					userAcceptInfo.setAlarmMsg(context.getRangeMessage());
					userAcceptInfo.setAlarmType("wangwang");
					userAcceptInfo.setAcceptDate(new Date());
					MonitorAlarmAo.get().addUserAcceptMsg(userAcceptInfo);
					logger.info("�û� wangwang:"+define.getWangwang()+" ���ո澯:"+context.getContinuousAlarmTimes() +" "+context.getAppName()+":"+context.getKeyName()+":"+context.getProperty());
				}else{
					logger.info("�û� wangwang:"+define.getWangwang()+" �����ո澯:"+context.getContinuousAlarmTimes() +" "+context.getAppName()+":"+context.getKeyName()+":"+context.getProperty());
				}
			}

		}
		
		try {
			//��ʱΪCPGW��HSF�澯����Ӧ�ã����û�����
			if(context.getAppName().equalsIgnoreCase("cpgw")) {
				HsfTransfer.getInstance().doTranser("", context);
				UserAcceptInfo userAcceptInfo = new UserAcceptInfo();
				userAcceptInfo.setAppId(context.getAppId());
				userAcceptInfo.setKeyName(context.getKeyName());
				userAcceptInfo.setUserId(-1);
				userAcceptInfo.setAlarmMsg(context.getRangeMessage());
				userAcceptInfo.setAlarmType("HSF");
				userAcceptInfo.setAcceptDate(new Date());
				MonitorAlarmAo.get().addUserAcceptMsg(userAcceptInfo);
			}
		} catch (Exception e) {
			logger.error("����HSF��Ϣʧ��", e);
		}
		
		return true;
	}

	/**
	 * ���澯��Ϣ���Ƿ����û��Ľ��շ�Χ��
	 * 
	 * @param rangeList
	 * @param context
	 * @return
	 */
	private boolean checkIsNeedAlarm(List<ReportRange> rangeList, AlarmContext context) {

		Calendar cal = Calendar.getInstance();
		int tmp = cal.get(Calendar.DAY_OF_WEEK);
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int currentTime = Integer.parseInt(sdf.format(cal.getTime()));
		
		for (ReportRange range : rangeList) {
			int week = range.getWeekDay();
			if (week != tmp) {
				continue;
			}
			int continueTimes = range.getContinueTimes();

			if (continueTimes >= 0 && context.getContinuousAlarmTimes() < continueTimes) {
				continue;
			}
			int start = range.getStart();
			int end = range.getEnd();
			if (currentTime >= start && currentTime <= end) {
				return true;
			}
		}
		return false;
	}
}
