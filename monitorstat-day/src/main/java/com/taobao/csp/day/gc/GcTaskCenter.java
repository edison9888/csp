package com.taobao.csp.day.gc;

import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.day.base.AbstractAnalyser;
import com.taobao.csp.day.base.AbstractLogTask;
import com.taobao.csp.day.base.DataType;
import com.taobao.csp.day.base.HostInfo;
import com.taobao.csp.day.base.TailContinuesLogTask;
import com.taobao.csp.day.base.TaskCenter;
import com.taobao.csp.day.util.AppUtil;
import com.taobao.monitor.common.po.AppInfoPo;

public class GcTaskCenter extends TaskCenter {
	
	public static Logger logger = Logger.getLogger(GcTaskCenter.class);
	
	private static TaskCenter taskCenter = new GcTaskCenter(); 
	
	private GcTaskCenter() {
		super();
	}
	
	public  static TaskCenter getInstance() {
		return taskCenter;
	}
	
	@Override
	protected List<AppInfoPo> getApps() {
		List<AppInfoPo> appL = AppUtil.getGcApps();
		
		return appL;
	}

	@Override
	protected DataType getDataType() {
		return DataType.GC;
	}

	@Override
	protected int taskWaitTime() {
		return 120;
	}

	@Override
	protected AbstractLogTask createTask(AbstractAnalyser analyser,
			String appName, HostInfo hostInfo, DataType dataType, String realLogPath,
			long fetchSize) {
		return new TailContinuesLogTask(analyser, appName, hostInfo, dataType, realLogPath);
	}

}
