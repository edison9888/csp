package com.taobao.monitor.alarm.external.ao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.log4j.Logger;

/**
 * 获取Diamond的报警机器列表<br/>
 * 这里取到得数据是报警结果数据: <hostIp:isAlarming>，报警策略由Diamond控制
 * @author dilu.kxq
 * 
 */
public class DiamondMonitorAO {
	private static final Logger log = Logger.getLogger(DiamondMonitorAO.class);
	private static final String KEY_URL = "diamond.url";
	private static final String KEY_OBJECT_NAME = "diamond.objname";
	private static final String SERVER_STATUS = "DiamondServerStatus";
	private static final String HTTP_STATUS = "HttpServerStatus";
	private static final DiamondMonitorAO instance = new DiamondMonitorAO();
	
	private static String url;
	private static String objectName;
	private static MBeanServerConnection connect = null;
	static {
		Properties pro = new Properties();
		InputStream in = DiamondMonitorAO.class.getClassLoader().getResourceAsStream("jmx.properties");
		try {
			pro.load(in);
			url = pro.getProperty(KEY_URL);
			objectName = pro.getProperty(KEY_OBJECT_NAME);
			JMXServiceURL jmxUrl = new JMXServiceURL(url);
			JMXConnector connector = JMXConnectorFactory.connect(jmxUrl);
			connect = connector.getMBeanServerConnection();
		} catch (IOException ex) {
			log.warn("load jmx access properties fail! ", ex);
			throw new RuntimeException(ex);
		}
	}
	public static DiamondMonitorAO instance(){
		return instance ;
	}
	public Map<String, Boolean> getDiamondServerStatus() {

		Map<String, Boolean> result = null;
		result = getAttributes(SERVER_STATUS);
		return result == null ? new HashMap<String, Boolean>() : result;
	}

	public Map<String, Boolean> getHttpServerStatus() {

		Map<String, Boolean> result = null;
		result = getAttributes(HTTP_STATUS);
		return result == null ? new HashMap<String, Boolean>() : result;
	}

	public List<String> getAlarmServers() {

		List<String> servers = new ArrayList<String>();
		for (Map.Entry<String, Boolean> entry : getDiamondServerStatus().entrySet()) {
			if (entry.getValue())
				servers.add(entry.getKey());
		}
		return servers;
	}

	public List<String> getAlarmHttpServers() {

		List<String> servers = new ArrayList<String>();
		for (Map.Entry<String, Boolean> entry : getHttpServerStatus().entrySet()) {
			if (entry.getValue())
				servers.add(entry.getKey());
		}
		return servers;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Boolean> getAttributes(String attrName) {

		try {
			return (Map<String, Boolean>) connect.getAttribute(new ObjectName(objectName), attrName);
		} catch (Exception e) {
			log.warn("Access DiamondMonitor for " + SERVER_STATUS + " encounter " + e.getClass().getName() + ", msg="
					+ e.getMessage(), e);
		}
		return null;
	}
	
	private DiamondMonitorAO(){
		
	}
}
