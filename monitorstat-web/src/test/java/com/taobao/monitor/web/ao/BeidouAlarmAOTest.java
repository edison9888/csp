package com.taobao.monitor.web.ao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.taobao.monitor.web.util.DateFormatUtil;
import com.taobao.monitor.web.vo.beidou.BeidouAlarmDataPo;



public class BeidouAlarmAOTest {

	@Test
	public void test1_getHasAlarmDataByGroupName_查询是否存在历史告警数据() throws Exception{
		
		String dateString = "2012-08-22 11:48:20";
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date end = df.parse(dateString);
		Date start =  DateFormatUtil.getTime(end,Calendar.HOUR_OF_DAY,-1);
		String tcmainGroupName = "tcmaindb";
		Map<String,List<BeidouAlarmDataPo>> map = BeidouAlarmAO.get().getBeidouAlarmDataMapByTime(start,end);
		//Assert.assertNotNull(map.get(tcmainGroupName));
		//System.out.println(map.toString());
	}

}
