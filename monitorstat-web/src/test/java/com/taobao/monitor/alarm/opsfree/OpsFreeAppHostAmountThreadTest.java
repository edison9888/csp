package com.taobao.monitor.alarm.opsfree;

import org.junit.Test;

public class OpsFreeAppHostAmountThreadTest {
	@Test
	public void test1() throws Exception{
		OpsFreeAppHostAmountThread.get().startup(); 
		Thread.sleep(1000*60 * 10);
	}

}
