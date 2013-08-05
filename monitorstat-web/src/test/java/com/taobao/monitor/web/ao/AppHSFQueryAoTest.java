package com.taobao.monitor.web.ao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.query.QueryUtil;
import com.taobao.monitor.time.ao.AppHSFQueryAo;
import com.taobao.monitor.time.po.AppPvPO;
import com.taobao.monitor.time.po.SortEntry;
import com.taobao.monitor.time.po.TimeDataInfo;
import java.util.regex.Pattern;
public class AppHSFQueryAoTest {
	private static String appname = "itemcenter";
	private static String ip = "172.23.202.57";
	private static Calendar cal = Calendar.getInstance();
	
	
	@Test
	public void test1_test() throws Exception{
		String seckilTairRegex = ".+[^m]seckill";
		//String seckilTairRegex = "([^m](seckill))$";
		//String keyName ="Tair-Consumer`seckill";
		String keyName ="Tair-Consumer`group_mseckill";
		System.out.println(Pattern.matches(seckilTairRegex, keyName));
	}
	@Test
	public void test1_getAppHsf_查询tp创建订单接口调用情况() throws Exception{
		String appName = "tradeplatform";
		String IP = "172.23.202.57";
		String serviceName = "com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0";
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-provider",serviceName,"createOrders" },
				new KeyScope[] { KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
				new String[] { "E-times", "C-time", "P-size" },
				new Object[] { randomInt(), randomInt(), randomInt() });
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer",
						"com.taobao.item.service.ItemBidService:1.0.0",
						"updateAuctionQuantity" }, new KeyScope[] {
						KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
				new String[] { "E-times", "C-time", "P-size" }, new Object[] {
						randomInt(), randomInt(), randomInt() });

		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer", "detail",
						"com.taobao.item.service.ItemBidService:1.0.0",
						"updateAuctionQuantity" },
				new KeyScope[] { KeyScope.NO, KeyScope.ALL, KeyScope.ALL,
						KeyScope.ALL }, new String[] { "E-times", "C-time",
						"P-size" }, new Object[] { randomInt(), randomInt(),
						randomInt() });
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer","shopsystem","com.taobao.item.service.CardCodeService:1.0.0-L0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
		
		    	String key="HSF-provider";
			//String key="HSF-Refer";
			 Map<String, Map<String, Object>> map  = QueryUtil.querySingleRealTime(appname, key);
			 System.out.print("************map" + map);
/*{1342085460000={E-times=6196, C-time=6557, P-size=6856}, 1342085400000={E-times=6850, C-time=7081, P-size=6750}, 1342085580000={E-times=6885, C-time=6948, P-size=6615}, 1342085700000={E-times=6919, C-time=6346, P-size=6758}, 1342085220000={E-times=7386, C-time=7013, P-size=7282}, 1342085160000={E-times=6877, C-time=7145, P-size=6609}, 1342085280000={E-times=7431, C-time=7504, P-size=7148}, 1342085340000={E-times=6777, C-time=7378, P-size=6357}, 1342085520000={E-times=6486, C-time=7029, P-size=7000}, 1342085640000={E-times=6597, C-time=6655, P-size=6587}}*/
	}
	
	@Test
	public void test2_getAppHsf_查询tp付款单接口调用情况() throws Exception{
		String appName = "tradeplatform";
		String IP = "172.23.202.57";
		String serviceName = "com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0";
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-provider",serviceName,"createOrders" },
				new KeyScope[] { KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
				new String[] { "E-times", "C-time", "P-size" },
				new Object[] { randomInt(), randomInt(), randomInt() });
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer",
			"com.taobao.item.service.ItemBidService:1.0.0",
		"updateAuctionQuantity" }, new KeyScope[] {
			KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
			new String[] { "E-times", "C-time", "P-size" }, new Object[] {
			randomInt(), randomInt(), randomInt() });
		
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer", "detail",
			"com.taobao.item.service.ItemBidService:1.0.0",
		"updateAuctionQuantity" },
		new KeyScope[] { KeyScope.NO, KeyScope.ALL, KeyScope.ALL,
			KeyScope.ALL }, new String[] { "E-times", "C-time",
		"P-size" }, new Object[] { randomInt(), randomInt(),
			randomInt() });
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer","shopsystem","com.taobao.item.service.CardCodeService:1.0.0-L0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
		
		String key="HSF-provider";
		//String key="HSF-Refer";
		Map<String, Map<String, Object>> map  = QueryUtil.querySingleRealTime(appname, key);
		System.out.print("************map" + map);
		/*{1342085460000={E-times=6196, C-time=6557, P-size=6856}, 1342085400000={E-times=6850, C-time=7081, P-size=6750}, 1342085580000={E-times=6885, C-time=6948, P-size=6615}, 1342085700000={E-times=6919, C-time=6346, P-size=6758}, 1342085220000={E-times=7386, C-time=7013, P-size=7282}, 1342085160000={E-times=6877, C-time=7145, P-size=6609}, 1342085280000={E-times=7431, C-time=7504, P-size=7148}, 1342085340000={E-times=6777, C-time=7378, P-size=6357}, 1342085520000={E-times=6486, C-time=7029, P-size=7000}, 1342085640000={E-times=6597, C-time=6655, P-size=6587}}*/
	}
	
	@Test
	public void test3_getAppHsf_查询pv的数据情况() throws Exception{
		String appName = "tradeplatform";
		String IP = "172.23.202.57";
		String serviceName = "com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0";
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-provider",serviceName,"createOrders" },
				new KeyScope[] { KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
				new String[] { "E-times", "C-time", "P-size" },
				new Object[] { randomInt(), randomInt(), randomInt() });
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer",
			"com.taobao.item.service.ItemBidService:1.0.0",
		"updateAuctionQuantity" }, new KeyScope[] {
			KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
			new String[] { "E-times", "C-time", "P-size" }, new Object[] {
			randomInt(), randomInt(), randomInt() });
		
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer", "detail",
			"com.taobao.item.service.ItemBidService:1.0.0",
		"updateAuctionQuantity" },
		new KeyScope[] { KeyScope.NO, KeyScope.ALL, KeyScope.ALL,
			KeyScope.ALL }, new String[] { "E-times", "C-time",
		"P-size" }, new Object[] { randomInt(), randomInt(),
			randomInt() });
		CollectDataUtilMulti.collect(appName, IP, cal.getTimeInMillis(),
				new String[] { "HSF-Refer","shopsystem","com.taobao.item.service.CardCodeService:1.0.0-L0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
		
		String key="HSF-provider";
		//String key="HSF-Refer";
		Map<String, Map<String, Object>> map  = QueryUtil.querySingleRealTime(appname, key);
		System.out.print("************map" + map);
		/*{1342085460000={E-times=6196, C-time=6557, P-size=6856}, 1342085400000={E-times=6850, C-time=7081, P-size=6750}, 1342085580000={E-times=6885, C-time=6948, P-size=6615}, 1342085700000={E-times=6919, C-time=6346, P-size=6758}, 1342085220000={E-times=7386, C-time=7013, P-size=7282}, 1342085160000={E-times=6877, C-time=7145, P-size=6609}, 1342085280000={E-times=7431, C-time=7504, P-size=7148}, 1342085340000={E-times=6777, C-time=7378, P-size=6357}, 1342085520000={E-times=6486, C-time=7029, P-size=7000}, 1342085640000={E-times=6597, C-time=6655, P-size=6587}}*/
	}
	
	@Test
	public void test4_推送所有数据在页面中展示() throws Exception{
		String tpAppName = "tradeplatform";
		String detailAppName = "detail";
		String cartAppName = "cart";
		String buyAppName = "tf_buy";
		
		String IP1 = "172.23.202.57";
		String IP2 = "172.24.202.57";
		String IP3 = "172.25.202.57";
		
		String detailUrl = "http://detail.taobao.com/item.htm";
		
		String buyUrl = "http://buy.taobao.com/auction/buy_now.jhtml";
		String buyUrl2 = "http://buy.taobao.com/auction/buy_now.htm";
		
		String cartUrl = "http://cart.taobao.com/my_cart.htm";
		
		String tmallCreatingServiceName = "com.taobao.tc.service.TcTradeService:1.0.0";
		String creatingServiceName = "com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0";
		String payServiceName = "com.taobao.trade.platform.api.pay.PayOrderService:1.0.0";
		
		//detail
		CollectDataUtilMulti.collect(detailAppName, IP2, cal.getTimeInMillis(),
				new String[] { KeyConstants.PV, detailUrl },
				new KeyScope[] { KeyScope.NO,KeyScope.ALL },
				new String[] { "E-times", "C-time"},
				new Object[] { 50*randomInt(),5*randomInt() });
		//cart
		CollectDataUtilMulti.collect(cartAppName, IP2, cal.getTimeInMillis(),
				new String[] { KeyConstants.PV, cartUrl },
				new KeyScope[] { KeyScope.NO,KeyScope.ALL },
				new String[] { "E-times", "C-time"},
				new Object[] { 5*randomInt(),5*randomInt() });
		
		//buy
		CollectDataUtilMulti.collect(buyAppName, IP1, cal.getTimeInMillis(),
				new String[] { KeyConstants.PV, buyUrl },
				new KeyScope[] { KeyScope.NO,KeyScope.ALL },
				new String[] { "E-times", "C-time"},
				new Object[] { 10*randomInt(),5*randomInt()  });
		
		CollectDataUtilMulti.collect(buyAppName, IP1, cal.getTimeInMillis(),
				new String[] { KeyConstants.PV, buyUrl2 },
				new KeyScope[] { KeyScope.NO,KeyScope.ALL },
				new String[] { "E-times", "C-time"},
				new Object[] { 10*randomInt(),5*randomInt()  });
		
		//tp
		CollectDataUtilMulti.collect(tpAppName, IP1, cal.getTimeInMillis(),
				new String[] { KeyConstants.HSF_PROVIDER, tmallCreatingServiceName, "syncCreate" },
				new KeyScope[] { KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
				new String[] { "E-times", "C-time", "P-size" },
				new Object[] { randomInt(), randomInt(), randomInt() });
		CollectDataUtilMulti.collect(tpAppName, IP1, cal.getTimeInMillis(),
				new String[] { KeyConstants.HSF_PROVIDER, creatingServiceName, "createOrders" },
				new KeyScope[] { KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
				new String[] { "E-times", "C-time", "P-size" },
				new Object[] { randomInt(), randomInt(), randomInt() });
		CollectDataUtilMulti.collect(tpAppName, IP2, cal.getTimeInMillis(),
				new String[] { KeyConstants.HSF_PROVIDER, payServiceName,"createOrders" },
				new KeyScope[] { KeyScope.ALL, KeyScope.ALL, KeyScope.ALL },
				new String[] { "E-times", "C-time", "P-size" },
				new Object[] { randomInt(), randomInt(), randomInt() });
		

	}
	
	@Test
	public void test2_something() throws Exception{
		double da = 1157d;
		double [] arr = {138,512,56,130,114,132,75};
		int count = 0; 
		for(int i=0;i<arr.length;i++){
			count += Math.round((arr[i]/da)*100);
			System.out.println("width='"+Math.round((arr[i]/da)*100)+"%'");
		}
		System.out.println(count);
	}
	@Test
	public void test_something() throws Exception{
		String tmallCreatingServiceName = "com.taobao.tc.service.TcTradeService:1.0.0";
		String taobaoCreatingServiceName = "com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0";
		String taobaoPayServiceName = "com.taobao.trade.platform.api.pay.PayOrderService:1.0.0";
		
		String tmallCreatingKeyName = "HSF-provider`com.taobao.tc.service.TcTradeService:1.0.0`syncCreate";
		String taobaoCreatingKeyName = "com.taobao.trade.platform.api.creating.ICreatingOrderService:1.0.0";
		String taobaoPayKeyName = "HSF-provider`com.taobao.trade.platform.api.pay.PayOrderService:1.0.0`pay~lF";

		String tpAppName ="tradeplatform";
		String creatingService = taobaoCreatingServiceName + Constants.S_SEPERATOR + "createOrders";
		String tmallCreatingService = tmallCreatingServiceName + Constants.S_SEPERATOR + "syncCreate";
		String payService = taobaoPayServiceName + Constants.S_SEPERATOR+"pay";;
		
		Map<String, List<TimeDataInfo>> creatingHsfInfoMap = AppHSFQueryAo.get().queryAppHSFInterface(tpAppName,creatingService);
//		Map<String, List<TimeDataInfo>> tmallCreatingHsfInfoMap = AppHSFQueryAo.get().queryAppHSFInterface(tpAppName,tmallCreatingService);
//		Map<String, List<TimeDataInfo>> payHsfInfoMap = AppHSFQueryAo.get().queryAppHSFInterface(tpAppName, payService);
	}
	public  int randomInt(){
		return (int)(100*Math.random());
	}
	@Test
	public void test_getAppHsf(){
		List<String> urlList = new ArrayList<String>();
		urlList.add(AppHSFQueryAo.DETAIL_URL);
		List<AppPvPO> pvPoList = AppHSFQueryAo.get().queryAppPv("detail", urlList);
		System.out.println(pvPoList.toString());
	}
}
