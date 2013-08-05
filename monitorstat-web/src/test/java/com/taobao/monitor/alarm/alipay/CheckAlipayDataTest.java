package com.taobao.monitor.alarm.alipay;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.monitor.time.util.TimeUtil;

public class CheckAlipayDataTest {
	@Test
	public void  test1() throws Exception{
		Calendar c = Calendar.getInstance();
		Date end = c.getTime();
		c.add(Calendar.MINUTE, -5);
		Date start = c.getTime();
		
		Map<Date, AlipayBizPo>  map = AlipayBizAo.get().getAlipayBizPoByTime(start, end);
		Assert.assertNotNull(map);
		System.out.println(map);
	}
	
	@Test
	public void test_getTheMostNearTime() throws Exception{
		Calendar c = Calendar.getInstance();
		Date now  = c.getTime();
		c.add(Calendar.SECOND, -1);
		Date oneSecondBefore = c.getTime();
		c.add(Calendar.MINUTE, -1);
		Date oneMiniteBefore = c.getTime();
		c.add(Calendar.HOUR, -1);
		Date oneHourBefore = c.getTime();
		Set<Date> set = new HashSet<Date>();
		set.add(oneSecondBefore);
		set.add(oneMiniteBefore);
		set.add(oneHourBefore);
		
		Date result = TimeUtil.getTheMostNearTime(now, set);
		Assert.assertEquals(oneSecondBefore.getTime(), result.getTime());
		
	}
	
	@Test
	public void test_check() throws Exception{
		Calendar c  = Calendar.getInstance();
		c.add(Calendar.MINUTE,-2);
		Date end = c.getTime();
		c.add(Calendar.MINUTE,-3);
		Date start = c.getTime();
		c.add(Calendar.MINUTE,1);
		Date collectTime = c.getTime();
		
		//添加测试数据
		CollectDataUtilMulti.collect("tradeplatform", "172.23.115.32", collectTime.getTime(), 
				new String[]{"tp交易相关总量","P1-trade_create"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, new String[]{"total","success"}, 
				new Object[]{120,115},
				new ValueOperate[]{ValueOperate.ADD,ValueOperate.ADD});
		
		
		
		AlipayBizAo.get().checkAlipay(start, end);
	}

}
