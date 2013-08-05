package com.taobao.monitor.alarm.network.lb;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.taobao.monitor.common.ao.center.CspTimeKeyAlarmRecordAo;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.util.CollectionUtil;

public class NetworkDeviceAlarmAoTest {
	@Test
	public void test1_测试获取单个LB的告警数据() throws Exception{
		//start=Fri Jan 16 19:38:38 CST 1970,end=Fri Jan 16 19:55:18 CST 1970
		String lbName ="LB-APP-G5-1.cm3";
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, 2012);
		c.set(Calendar.MONTH, 6);
		c.set(Calendar.DAY_OF_MONTH, 1);
		
		
		Date start = c.getTime();
		c.set(Calendar.MONTH, 7);
		c.add(Calendar.DAY_OF_MONTH,31);
		
		Date end = c.getTime();
		System.out.println("start=" + start + ",end="  + end);
		List<NetworkDeviceAlarmPo>  list =NetworkDeviceAlarmAo.get().getlbAlarmData(lbName, start, end);
		Assert.assertTrue(list.size()>0);
		Assert.assertEquals(2, list.size());
		System.out.println("list.size()=" + list.size());
		System.out.println("list=" + list);
		
	}
	
	@Test
	public void test3_测试某个时间段LB的告警数据() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTimeString = "2012-06-26 16:32:07";
		String endTimeString = "2012-06-26 19:46:17";
		Date start = sdf.parse(startTimeString);
		Date end = sdf.parse(endTimeString);
		Map<String, List<NetworkDeviceAlarmPo>> map = NetworkDeviceAlarmAo.get().getLoadBalanceAlarmDataByTime(start, end);
		for(Map.Entry<String, List<NetworkDeviceAlarmPo>> entry:map.entrySet()){
			System.out.println("LbName="+entry.getKey());
			List<NetworkDeviceAlarmPo> list = entry.getValue();
			if(list != null){
				System.out.println("list.size()="+list.size());
				for(NetworkDeviceAlarmPo po:list){
					System.out.print("status="+po.getState() +",hostname="+po.getHostName()+ ",output="+po.getOutput() + "\n");
					
				}
			} else {
				System.out.println("list=null");
			}
			
		}
	}
	
	@Test
	public void test5_测试某个时间段LB的告警数据() throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startTimeString = "2012-06-26 12:00:00";
		String endTimeString = "2012-06-26 18:00:00";
		Date start = sdf.parse(startTimeString);
		Date end = sdf.parse(endTimeString);
		Map<String, List<NetworkDeviceAlarmPo>> map = NetworkDeviceAlarmAo.get().getLoadBalanceAlarmDataByTime(start, end);
		for(Map.Entry<String, List<NetworkDeviceAlarmPo>> entry:map.entrySet()){
			System.out.println("LbName="+entry.getKey());
			List<NetworkDeviceAlarmPo> list = entry.getValue();
			if(list != null){
				System.out.println("list.size()="+list.size());
				for(NetworkDeviceAlarmPo po:list){
					System.out.print("state="+po.getState()+",hostname="+po.getHostName()+ ",output="+po.getOutput() + "\n");
				}
			} else {
				System.out.println("list=null");
			}
			
		}
	}
	
	@Test
	public void test4_() throws Exception{
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String startTimeString = "2012-06-26 16:32:07";
//		String endTimeString = "2012-06-26 19:46:17";
//		Date start = sdf.parse(startTimeString);
//		Date end = sdf.parse(endTimeString);
//		System.out.println("&start="+start.getTime()+"&end="+end.getTime());
//		System.out.println();
//		for(Map.Entry<String,List<LBRelateApp>> entry:GetAppHostAmountRunnable.lbAppRelateMap().entrySet()){
//			System.out.println(entry.getKey() + entry.getValue());
//		}
//		List<Integer> list = Arrays.asList(1,2);
//		System.out.println(list);
//		System.out.println("asjkdhsa".startsWith(""));
		List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
		
		CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
//		po.setMode_name("阀值");
//		po.setKey_scope("APP");
//		po.setApp_name("tradeplatform");
//		po.setKey_name("到支付宝的请求量对比");
//		po.setProperty_name("count");
//		po.setAlarm_cause("到支付宝的请求量与接收量不匹配");
//		po.setAlarm_time(new Timestamp(new Date().getTime()));
//		po.setAlarm_value("t=" + 222000 + ",a=" + 333000);
//		po.setIp("172.24.168.111");  //随意写的一台tp机器的ip
		
		po.setMode_name("阀值");
		po.setKey_scope("APP");
		po.setApp_name("tradeplatform");
		po.setKey_name("tp的notify消息总量");
		po.setProperty_name("count");
		po.setAlarm_cause("tp发出消息超过接收消息的120%");
		po.setAlarm_time(new Timestamp(new Date().getTime()));
		po.setAlarm_value("tp=" +222000 + ",logs=" + 333000);
		po.setIp("172.24.168.111");  //随意
		
		list.add(po);
       
		if(CollectionUtil.isNotEmpty(list)){
			CspTimeKeyAlarmRecordAo.get().insert(list);
			}

	}
	
	@Test
	public void test5_getVlanByLBName(){
		String lbName = "LB-APP-G3-1.cm3";
		
		List<String> list = LoadBalanceConstants.getVlanByLBName(lbName);
		Assert.assertNotNull(list);
		Assert.assertEquals(8, list.size());
		System.out.println(list);
	}

}
