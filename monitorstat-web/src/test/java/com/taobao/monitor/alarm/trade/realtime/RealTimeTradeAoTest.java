package com.taobao.monitor.alarm.trade.realtime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.taobao.monitor.alarm.trade.realtime.po.RealTimeTradePo;

public class RealTimeTradeAoTest {
	@Test
	public void test1_getCreateCnt(){

	}

	
	@Test
	public void test1(){
		List<RealTimeTradePo> poList = RealTimeTradeAo.get().getMultiRealTimeTradeNow();
		for(RealTimeTradePo po:poList)
			System.out.println(po);
	}
	@Test
	public void test2(){
		Calendar c = Calendar.getInstance();
		c.set(Calendar.MONTH, 7);
		c.set(Calendar.DAY_OF_MONTH, 9);
		c.set(Calendar.HOUR_OF_DAY, 14);
		c.set(Calendar.MINUTE, 10);
		c.set(Calendar.SECOND, 0);
		Date start = c.getTime();
		
		RealTimeTradePo po = RealTimeTradeAo.get().getReadlTimeTradeByTime(start);
		Assert.assertNotNull(po);
		System.out.println(po);
		
	}
	@Test
	public void test3(){
		
		RealTimeTradePo po = RealTimeTradeAo.get().getReadlTimeTradeByTime(new Date());
		System.out.println(po);
		
	}
	@Test
	public void getJSONRealTimeTest3(){
		System.out.println(RealTimeTradeAo.get().getReadlTimeTradeJson());
	}
	@Test
	public void getJSONRealTimeTest1(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		try {
			date = sdf.parse("2012-11-11 00:00:00");
			System.out.println(date.getMonth()+1);
			System.out.println(date.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void getJSONRealTimeAndBaseTest3(){
		System.out.println( RealTimeTradeAo.get().getMultiRealTimeTradeJson(5, new Date()));
	}
	@Test
	public void getJSONRealTimeAndBaseTest4(){
		StringBuilder str = new  StringBuilder("abssde,");
		System.out.println(str.substring(0, str.length()-1));
	}
}
