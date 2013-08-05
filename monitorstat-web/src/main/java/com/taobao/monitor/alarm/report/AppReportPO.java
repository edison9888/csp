package com.taobao.monitor.alarm.report;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.taobao.monitor.web.util.DateFormatUtil;
import com.taobao.monitor.web.vo.AlarmDataForPageViewPo;

public class AppReportPO {
	private String appName;
	private String timeString;
	private List<AlarmDataForPageViewPo> appAlarmDataList = new ArrayList<AlarmDataForPageViewPo>();
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getTimeString() {
		return timeString;
	}
	public void setTimeString(Date mailTime) {
		this.timeString = DateFormatUtil.formatMailParameterTime(mailTime);
	}
	public List<AlarmDataForPageViewPo> getAppAlarmDataList() {
		return appAlarmDataList;
	}
	public void setAppAlarmDataList(List<AlarmDataForPageViewPo> appAlarmDataList) {
		this.appAlarmDataList = appAlarmDataList;
	}
}
