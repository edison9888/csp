package com.taobao.csp.day.tddl;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.AbstractLogTask;
import com.taobao.csp.day.base.ContinuesLogTask;
import com.taobao.csp.day.base.DataType;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.TaskCenter;
import com.taobao.csp.day.util.AppUtil;
import com.taobao.monitor.common.po.AppInfoPo;

public class TddlTaskCenter extends TaskCenter {
	
	public static Logger logger = Logger.getLogger(TddlTaskCenter.class);
	
	private static TaskCenter taskCenter = new TddlTaskCenter(); 
	
	private TddlTaskCenter() {
		super();
	}
	
	public  static TaskCenter getInstance() {
		return taskCenter;
	}
	
	@Override
	protected List<AppInfoPo> getApps() {
		List<AppInfoPo> tddlAppL = AppUtil.getTddlApps();
		
		return tddlAppL;
	}

	@Override
	protected DataType getDataType() {
		return DataType.TDDL;
	}

	@Override
	protected int taskWaitTime() {
		return 30;
	}

	@Override
	protected AbstractLogTask createTask(AbstractAnalyser analyser,
			String appName, HostInfo hostInfo, DataType dataType, String logPath,
			long fetchSize) {
		return new ContinuesLogTask(analyser, appName, hostInfo, dataType, logPath, fetchSize);
	}

}
