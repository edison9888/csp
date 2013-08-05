
/**
 * monitorstat-time-monitor
 */
package com.taobao.csp.monitor.impl;


import java.io.IOException;
import java.util.Map;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.commons.lang.StringUtils;

import com.taobao.csp.monitor.DataCollector;
import com.taobao.csp.monitor.JobInfo;

/**
 * @author xiaodu
 *
 * ÏÂÎç12:03:30
 */
public class JmxModeCollector  implements DataCollector{
	
	private JobInfo jobInfo;
	
	private String serverUrl;
	
	private String objectname;
	
	
	private JMXConnector connector = null;
	
	public JmxModeCollector(JobInfo jobInfo){
		this.jobInfo = jobInfo;
		String info = jobInfo.getFilepath();
		String[] tmp = info.split("#");
		if(tmp.length ==2){
			String jmxUrl = tmp[0];
			String object = tmp[1];
//			connector = JMXConnectorFactory.connect(new JMXServiceURL(jmxUrl));
			
		}
		
		
		
		
		
		
//		Map<String, Long> result = (Map<String, Long>) cnc.invoke(objName, "getAllHostMessagePileUpByTopicType", new String[]{"oracle","TRADE","*"}, new String[]{String.class.getName(),String.class.getName(),String.class.getName()});

		
		
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public void collect(CallBack call) {
		
//		MBeanServerConnection cnc = connector.getMBeanServerConnection();
//		ObjectName objName = new ObjectName(objectname);
////		cnc.iinvoke(objName, operationName, params, signature)
		
	}

	@Override
	public void release() {
		
		if(connector != null){
			try {
				connector.close();
			} catch (IOException e) {
			}
		}
	}

}
