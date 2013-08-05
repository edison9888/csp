package com.taobao.monitor.alarm.source;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Assert;
import org.junit.Test;

import com.taobao.monitor.web.util.DateFormatUtil;

public class AlarmSourceConcludeTest {
	@Test
	public void test1_getAlarmSource_��ѯ��ʷ�澯Դͷ() throws Exception{
		//��Ҫ��׼�����ݣ�����DB��Ӧ�õģ���history��map���и�ֵ
		Set<String> appNameSet = new HashSet<String>();
		appNameSet.add("tradeplatform");
		String dbDateString = "2012-03-06 19:37:21";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end = df.parse(dbDateString);
		Date start = DateFormatUtil.getTime(end,Calendar.HOUR_OF_DAY,-1);
		Set<String> alarmSources = AlarmSourceConclude.getAlarmSource(start,end,appNameSet);
		
		System.out.print(alarmSources);
		String groupName = "tcmaindb";
		Assert.assertTrue(alarmSources.contains(groupName));
		Assert.assertFalse(alarmSources.contains("tradeplatform"));
	}
	
	
	@Test
	public void test2_�ж�TF����TP��key�澯() throws Exception{
		Set<String> appNameSet = new HashSet<String>();
		appNameSet.add("tradeplatform");
		String dateString = "2012-03-06 19:37:21";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end = df.parse(dateString);
		Date start = DateFormatUtil.getTime(end,Calendar.HOUR_OF_DAY,-1);
		Set<String> alarmSources = AlarmSourceConclude.getAlarmSource(start,end,appNameSet);
		
		System.out.print(alarmSources);
		Assert.assertEquals(1, alarmSources.size());
		Assert.assertTrue(alarmSources.contains("tcmaindb"));
		
	}
	
	@Test
	public void test(){
//		Map<String,Integer> appIds = AppInfoAo.get().getTradeRelateAppMap();
//		System.out.println(appIds);
//		Calendar c = Calendar.getInstance();
//		c.set(Calendar.HOUR_OF_DAY, 1);
//		c.set(Calendar.MINUTE, 0);
//		c.set(Calendar.SECOND, 0);
//		c.set(Calendar.MILLISECOND, 0);
//		System.out.println(c.getTime());
//		
		HttpClient httpClient = new HttpClient();
		String  s= "http://monitor.taobao.com/monitorapi/emonitorDetailData.do?";
		PostMethod postMethod = new PostMethod(s);
		//ʹ��ϵͳ�ṩ��Ĭ�ϵĻָ�����
		postMethod.setParameter("hostgroup","tradelogshost.cm3");
		postMethod.setParameter("item","tc-delay_merge");
		postMethod.setParameter("dataitem","TradeEnableOrderProcessor_sum");
		postMethod.setParameter("date","2012-07-02");
		
		try {
			   //ִ��getMethod
			   int statusCode = httpClient.executeMethod(postMethod);
			   if (statusCode == HttpStatus.SC_OK) {
				   //��ȡ����
				   String response = postMethod.getResponseBodyAsString();
				   System.out.println("response=" + response);
			   }else{
				 
			   }
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			postMethod.releaseConnection();
		}
		postMethod = null;
		
	}
	

}
