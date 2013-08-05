package com.taobao.monitor.web.ao;

import org.junit.Test;

import com.taobao.monitor.time.ao.HistoryHSFQueryAo;

public class HistoryHSFQuyerAoTest {
	@Test
	public void test1_test() throws Exception{
		String appName = "tradeplatform";
		String keyName = "com.taobao.tc.service.TcTradeService:1.0.0`syncCreate";
		String tmp = HistoryHSFQueryAo.get().queryFlashData(appName, keyName);
		System.out.println(tmp);
	}
}
