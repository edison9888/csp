
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl;

import com.taobao.csp.monitor.DataCollector;
import com.taobao.csp.monitor.JobInfo;

/**
 * @author xiaodu
 *
 * ÉÏÎç10:45:48
 */
public class DataCollectorFactory {
	
	
	public static DataCollector createCollector(JobInfo info){
		
		String type = info.getCollectorType();
		
		if("3".equals(type)){//http get
			return new GetHttpModeCollector(info);
		}
		if("41".equals(type)){//http post
			return new PostHttpModeCollector(info);
		}
		if("44".equals(type)){//json
			return new JsonModeCollector(info);
		}
		if("2".equals(type)){//ssh-command
			return new SSHModeCollector(info);
		}
		if("1".equals(type)){//ssh-file
			return new SSHFileModeCollector(info);
		}
		return null;
	}

}
