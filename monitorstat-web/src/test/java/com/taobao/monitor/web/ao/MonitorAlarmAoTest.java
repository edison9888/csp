package com.taobao.monitor.web.ao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.taobao.monitor.web.vo.AlarmDataForPageViewPo;

public class MonitorAlarmAoTest {
	
	@Test
	public void test3_getHistoryTradeRalateAlarmMapByTime_查询历史告警数据() throws Exception{
		Set<String> appNameSet = new HashSet<String>();
		appNameSet.add("tradeplatform");
		String startTime = "2012-07-04 02:00:00";
		String endTime = "2012-07-04 03:00:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end = df.parse(endTime);
		Date start = df.parse(startTime);
		List<Integer> keyLevelList = new ArrayList<Integer>();
		HashMap<String, List<AlarmDataForPageViewPo>> map =  MonitorAlarmAo.get().getHistoryTradeRalateAlarmMapByTime(start,end,keyLevelList,appNameSet);
		int tpAppId = 322;
		Assert.assertNotNull(map.get(tpAppId));
		
	}
	
	@Test
	public void test5_测试时间段分隔() throws Exception{
		String startTime = "2012-07-04 02:05:00";
		String endTime = "2012-07-04 02:06:00";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end = df.parse(endTime);
		Date start = df.parse(startTime);
		//时间分隔的是20分钟
		Assert.assertEquals(1, MonitorAlarmAo.get().getIntervalCount(start,end));
	}


}
