package com.taobao.csp.day.apache;

import java.util.ArrayList;
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

public class ApacheSpecialTaskCenter extends TaskCenter {
	
	public static Logger logger = Logger.getLogger(ApacheSpecialTaskCenter.class);
	
	private static TaskCenter taskCenter = new ApacheSpecialTaskCenter(); 
	
	private ApacheSpecialTaskCenter() {
		super();
	}
	
	public  static TaskCenter getInstance() {
		return taskCenter;
	}
	
	@Override
	protected  AbstractLogTask createTask(AbstractAnalyser analyser, String appName, 
			HostInfo hostInfo, DataType dataType, String realLogPath, long fetchSize) {
		List<String> contentFilterL = new ArrayList<String>(); 
		contentFilterL.add("10/Nov/2012:23");
		AbstractLogTask task = new StandardLogTask(analyser, appName, hostInfo, dataType, 
				realLogPath, fetchSize, contentFilterL);
		
		task.setPosition(100 * 1024 * 1024);
		
		return task;
	}
	
	@Override
	protected List<AppInfoPo> getApps() {
		List<AppInfoPo> appL = AppUtil.getApacheAppsSpecialApps();
		
		return appL;
	}

	@Override
	protected DataType getDataType() {
		return DataType.APACHE_SPECIAL;
	}

	@Override
	protected int taskWaitTime() {
		return 60;
	}

}
