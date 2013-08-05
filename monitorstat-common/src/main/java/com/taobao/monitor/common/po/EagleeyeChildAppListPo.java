package com.taobao.monitor.common.po;

import java.util.ArrayList;
import java.util.List;

/**
 * �ӿ�4������app��Ӧ�ĸ��ӿڷ����������Ϣ
 * @author zhongting.zy
 *
 */
public class EagleeyeChildAppListPo {
	private String appName;
	private String time;	//��ʽ: yyyy-MM-dd HH:mm
	private long successCallNum;
	private long failCallNum;
	
	private long successRt;
	private long faliRt;
	
	private List<EagleeyeChildAppListPo> childList = new ArrayList<EagleeyeChildAppListPo>();

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public long getSuccessCallNum() {
		return successCallNum;
	}

	public void setSuccessCallNum(long successCallNum) {
		this.successCallNum = successCallNum;
	}

	public long getFailCallNum() {
		return failCallNum;
	}

	public void setFailCallNum(long failCallNum) {
		this.failCallNum = failCallNum;
	}

	public long getSuccessRt() {
		return successRt;
	}

	public void setSuccessRt(long successRt) {
		this.successRt = successRt;
	}

	public long getFaliRt() {
		return faliRt;
	}

	public void setFaliRt(long faliRt) {
		this.faliRt = faliRt;
	}

	public List<EagleeyeChildAppListPo> getChildList() {
		return childList;
	}

	public void setChildList(List<EagleeyeChildAppListPo> childList) {
		this.childList = childList;
	}

}
