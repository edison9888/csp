package com.taobao.monitor.alarm.source.change;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.taobao.monitor.web.util.DateFormatUtil;

public class ChangeFreeAoTest {
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	@Test
	public void test_case1_获取应用系统的变更信息(){
		String systemName = "tradeplatform";
		String startTimeString ="2012-03-13 18:40:00"; 
		Date startTime = DateFormatUtil.stringToDate(startTimeString);
		List<ChangeFreePo> list = ChangeFreeAo.get().getChangeFreeInfo(startTime, systemName);
		Assert.assertNotNull(list);
		System.out.println(list.get(0));
		Assert.assertEquals(1, list.size());
	}

	@Test
	public void test_case3_获取数据库的变更信息(){
		String systemName = "mysql_taobao";
		String startTimeString = "2012-04-20 03:00:00";
		Date startTime = DateFormatUtil.stringToDate(startTimeString);
		List<ChangeFreePo> list = ChangeFreeAo.get().getChangeFreeInfo(startTime, systemName);
		Assert.assertNotNull(list);
		Assert.assertEquals(1, list.size());
	}
	
}
