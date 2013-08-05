package com.taobao.monitor.alarm.source.artoo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.taobao.monitor.alarm.source.dao.ArtooCacheDao;
import com.taobao.monitor.web.util.DateFormatUtil;

public class ArtooAoTest {
	ArtooCacheDao dao = new ArtooCacheDao();
	@Test
	public void test_获取shopsystem的发布数据(){
		String appName = "shopsystem";
		String startTimeString = "2012-06-27 12:12:00";
		String endTimeString = "2012-06-29 17:12:00"; 
		Date startTime = DateFormatUtil.stringToDate(startTimeString);
		Date endTime = DateFormatUtil.stringToDate(endTimeString);
		List<ArtooPo> list = ArtooAo.get().findArtooPoListByAppNameAndTime(appName,startTime,endTime);
		Assert.assertNotNull(list);
	}
	
	@Test
	public void test_date(){
		Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_YEAR, -20);
		StringBuilder sb = new StringBuilder(String.valueOf(c.get(Calendar.YEAR)));
		sb.append("-").append(c.get(Calendar.MONTH)< 10 ?"0":"").append(c.get(Calendar.MONTH))
		.append("-").append(c.get(Calendar.MONTH)< 10 ?"0":"").append(c.get(Calendar.DAY_OF_MONTH));
		System.out.println(sb.toString());
//		System.out.println(c.get(Calendar.YEAR));
		
	}
	
}
