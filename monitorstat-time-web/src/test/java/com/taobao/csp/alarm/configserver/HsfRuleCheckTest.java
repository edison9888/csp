package com.taobao.csp.alarm.configserver;

import java.io.InputStream;

import org.junit.Test;

public class HsfRuleCheckTest {
	@Test
	public void test_diamond���ͽű�() throws Exception{
		HsfRuleInfo obj = new HsfRuleInfo();
		obj.setAppName("tradeplatform");
		obj.setInterfaceName("com.taobao.tc.service.TcBaseService");
		obj.setVersion("1.0.0");
		
		HsfRuleCheck check = new HsfRuleCheck(obj);
		
		String s_xmlpath="com/taobao/csp/alarm/configserver/test.groovy";
		ClassLoader classLoader=HsfRuleCheckTest.class.getClassLoader();
		InputStream in=classLoader.getResourceAsStream(s_xmlpath);
		
		 byte b[]=new byte[50000];     //���������ļ���С������  
	     in.read(b);    //��ȡ�ļ��е����ݵ�b[]����  
	     in.close();  
	     String info = new String(b);
		
		check.receiveConfigInfo(info.substring(0,info.length()-1));
		Thread.sleep(1000);
	}

	
	@Test
	public void test_configserver��������(){
		String ip = "172.23.23.20";
		 int index = ip.indexOf(".", ip.indexOf(".") + 1);
         String key = ip.substring(0, index);
         System.out.print(key);
         
	}
}
