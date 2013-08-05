package com.taobao.monitor.web;

import java.io.IOException;
import java.util.Map;

import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import junit.framework.Assert;

import org.junit.Test;

import com.taobao.monitor.alarm.external.ao.DiamondMonitorAO;

public class DiamondInfoAOTest {
	private static final String url = "service:jmx:rmi:///jndi/rmi://10.232.13.23:6670/diamond-monitor";
	private static final String className = "com.taobao.diamond.monitor.jmx:type=com.taobao.diamond.monitor.jmx.DiamondMonitor";
	private static final String methodDiamondServerStatus = "DiamondServerStatus";
	String methodHttpServerStatus = "HttpServerStatus";

	@Test
	public void test_case1() throws IOException, InstanceNotFoundException, MalformedObjectNameException, MBeanException, ReflectionException, NullPointerException, AttributeNotFoundException {

		JMXServiceURL jmxUrl = new JMXServiceURL(url);
		JMXConnector connector = JMXConnectorFactory.connect(jmxUrl);
		MBeanServerConnection connect = connector.getMBeanServerConnection();
		ObjectName obj = new ObjectName(className);
		Object result = connect.getAttribute(obj, methodDiamondServerStatus);
		
		Map<String, Boolean> serverStatus = (Map<String,Boolean>)result ;

		Assert.assertNotNull(serverStatus);
		System.out.println(serverStatus.toString());
		Map<String, String> httpStatsu = (Map<String,String>)connect.getAttribute(obj, methodHttpServerStatus);
		System.out.println(httpStatsu.toString());
		
	}
	
	@Test
	public void test_case2(){
		DiamondMonitorAO ao =  DiamondMonitorAO.instance();
		Map<String, Boolean> serverStatus = ao.getDiamondServerStatus() ;
		Assert.assertNotNull(serverStatus);
		Assert.assertTrue(serverStatus.size() > 0);
		
		Map<String, Boolean > httpStatus = ao.getHttpServerStatus();
		Assert.assertNotNull(httpStatus);
		Assert.assertTrue(httpStatus.size() > 0);
		System.out.println("serverStatus: " + serverStatus.toString() +"\nhttp" + httpStatus.toString());
		
	}
}
