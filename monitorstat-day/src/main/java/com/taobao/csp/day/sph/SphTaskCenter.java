package com.taobao.csp.day.sph;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.AbstractLogTask;
import com.taobao.csp.day.base.DataType;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.StandardLogTask;
import com.taobao.csp.day.base.TaskCenter;
import com.taobao.csp.day.util.AppUtil;
import com.taobao.monitor.common.po.AppInfoPo;

public class SphTaskCenter extends TaskCenter {
	
	public static Logger logger = Logger.getLogger(SphTaskCenter.class);
	
	private static TaskCenter taskCenter = new SphTaskCenter(); 
	
	private SphTaskCenter() {
		super();
	}
	
	public  static TaskCenter getInstance() {
		return taskCenter;
	}
	
	@Override
	protected List<AppInfoPo> getApps() {
		List<AppInfoPo> tddlAppL = AppUtil.getSphApps();
		
		return tddlAppL;
	}

	@Override
	protected DataType getDataType() {
		return DataType.SPH;
	}

	@Override
	protected int taskWaitTime() {
		return 60;
	}

	@Override
	protected AbstractLogTask createTask(AbstractAnalyser analyser,
			String appName, HostInfo hostInfo, DataType dataType, String realLogPath,
			long fetchSize) {
		return new StandardLogTask(analyser, appName, hostInfo, dataType, realLogPath, fetchSize, null);
	}

}
