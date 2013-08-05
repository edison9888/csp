
/**
 * monitorstat-alarm
 */
package com.taobao.csp.dataserver.example;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
import com.taobao.csp.dataserver.item.KeyScope;
import com.taobao.csp.dataserver.item.ValueOperate;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

/**
 * @author xiaodu
 *
 * 下午3:51:48
 */
public class CollectExample {
	
	
	
	public static int randomInt(){
		return (int)(100*Math.random());
	}
	
	public static float randomFloat(){
		return (float)(100*Math.random());
	}
	
	public static void main(String[] args){
		
		List<String> list = new ArrayList<String>();
		list.add("detail");
//		list.add("shopsystem");
//		list.add("hesper");
//		list.add("login");
//		list.add("tf_tm");
//		list.add("tf_buy");
//		list.add("itemcenter");
//		list.add("tradeplatform");
//		list.add("shopcenter");
//		list.add("uicfinal");
//		list.add("cart");
//		list.add("ump");
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		
		
		while(true){
			
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
				
				System.out.println(sdf.format(cal.getTimeInMillis()));
				
				
				for(String appname:list){
					try {
						
					
						
						Map<String,HostPo> map = CspCacheTBHostInfos.get().getHostInfoMapByOpsName(appname);
						if(map == null){
							System.out.println(appname);
							continue;
						}
						int i=0;
						for(Map.Entry<String, HostPo> entry:map.entrySet()){
							
							i++;
							if(i>70){
								continue;
							}
							String[] keys = new String[]{"E-times","C-time","P-size","C-200","C-302","c-other","机房3","机房4","机房5"};
							Object[] values = new Object[]{randomInt(),randomInt(),randomInt(),randomInt(),randomInt(),randomInt(),randomInt(),randomInt(),randomInt()};
							String ip=entry.getValue().getHostIp();
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), new String[]{KeyConstants.MBEAN,KeyConstants.THREADPOOL,"ajp-thread-pool"}, 
									new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.HOST},new String[]{"max","current"},
									new Object[]{randomInt(),randomInt()},
									new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE});
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), new String[]{KeyConstants.MBEAN,KeyConstants.THREAD,"SocketConnector"}, 
									new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.HOST},new String[]{"BLOCKED","WAITING","TERMINATED","TIMEDWAITING","RUNNABLE"},
									new Object[]{randomInt(),randomInt(),randomInt(),randomInt(), randomInt()},
									new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), new String[]{KeyConstants.MBEAN,KeyConstants.THREAD,"TFSCLIENT"}, 
									new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.HOST},new String[]{"BLOCKED","WAITING","TERMINATED","TIMEDWAITING","RUNNABLE"},
									new Object[]{randomInt(),randomInt(),randomInt(),randomInt(), randomInt()},
									new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(),new String[]{KeyConstants.MBEAN,KeyConstants.DATASOURCE,"JmsXA"}, 
									new KeyScope[]{KeyScope.NO,KeyScope.NO,KeyScope.HOST},new String[]{"InUse","Available"},
									new Object[]{randomInt(),randomInt()},new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE});

							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV","item.taobao.com/item.htm"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, keys, values);
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"region","浙江"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"region","北京"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"region","上海"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"region","CC"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"region","DD"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"network","网通"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"network","电信"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"network","aa"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"network","bb"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"network","CC"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, keys, values);
							
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.HSF_AREA_RATE,"CM3"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, new String[]{"CM3","CM4","CM5"},  new Object[]{randomInt(),randomInt(),randomInt()});
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.HSF_AREA_RATE,"CM4"}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, new String[]{"CM3","CM4","CM5"},  new Object[]{randomInt(),randomInt(),randomInt()});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.HSF_CONSUMER_RATE,"CM3","CM3"}, new KeyScope[]{KeyScope.NO,KeyScope.APP,KeyScope.APP}, new String[]{"itemcenter","shopcenter","ump"},  new Object[]{randomInt(),randomInt(),randomInt()});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.HSF_CONSUMER_RATE,"CM4","CM3"}, new KeyScope[]{KeyScope.NO,KeyScope.APP,KeyScope.APP}, new String[]{"itemcenter","shopcenter","ump"},  new Object[]{randomInt(),randomInt(),randomInt()});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.HSF_CONSUMER_RATE,"CM3","CM4"}, new KeyScope[]{KeyScope.NO,KeyScope.APP,KeyScope.APP}, new String[]{"itemcenter","shopcenter","ump"},  new Object[]{randomInt(),randomInt(),randomInt()});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.HSF_CONSUMER_RATE,"CM4","CM4"}, new KeyScope[]{KeyScope.NO,KeyScope.APP,KeyScope.APP}, new String[]{"itemcenter","shopcenter","ump"},  new Object[]{randomInt(),randomInt(),randomInt()});
							
							
							
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV-network-region","网通","浙江","宁波市"}, new KeyScope[]{KeyScope.NO,KeyScope.APP,KeyScope.APP,KeyScope.APP}, keys, values);
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV-network-region","网通","江苏","徐州市"}, new KeyScope[]{KeyScope.APP,KeyScope.APP,KeyScope.APP,KeyScope.APP}, keys, values);
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV-network-region","网通","江苏","苏州市"}, new KeyScope[]{KeyScope.APP,KeyScope.APP,KeyScope.APP,KeyScope.APP}, keys, values);
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV-network-region","电信","浙江","杭州"}, new KeyScope[]{KeyScope.APP,KeyScope.APP,KeyScope.APP,KeyScope.APP}, keys, values);
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV-network-region","电信","浙江","宁波市"}, new KeyScope[]{KeyScope.APP,KeyScope.APP,KeyScope.APP,KeyScope.APP}, keys, values);
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV-network-region","电信","江苏","徐州市"}, new KeyScope[]{KeyScope.APP,KeyScope.APP,KeyScope.APP,KeyScope.APP}, keys, values);
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV-network-region","电信","江苏","苏州市"}, new KeyScope[]{KeyScope.APP,KeyScope.APP,KeyScope.APP,KeyScope.APP}, keys, values);
							
							
							if(appname.equals("shopsystem")){
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"PV-refer","http://item.taobao.com/item.htm"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"PV-refer","http://detail.tmall.com/item.htm"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"PV-refer","http://store.taobao.com/shop/view_shop.htm"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"PV-refer","http://login.taobao.com/member/loginByIm.do"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
							}
							
							if(appname.equals("detail")){
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"PV-refer","http://item.taobao.com/item.htm"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"PV-refer","http://detail.tmall.com/item.htm"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"PV-refer","http://store.taobao.com/shop/view_shop.htm"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"PV-refer","http://login.taobao.com/member/loginByIm.do"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
						
							}
							
							
							
							
							if(appname.equals("itemcenter")){
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-provider","com.taobao.item.service.CardCodeService:1.0.0-L0","getCardCodeInfoByQuery"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"HSF-provider","com.taobao.item.service.ItemBidService:1.0.0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Refer","detail","com.taobao.item.service.CardCodeService:1.0.0-L0","getCardCodeInfoByQuery"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Refer","shopsystem","com.taobao.item.service.CardCodeService:1.0.0-L0","getCardCodeInfoByQuery"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Refer","login","com.taobao.item.service.CardCodeService:1.0.0-L0","getCardCodeInfoByQuery"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
							
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Refer","detail","com.taobao.item.service.ItemBidService:1.0.0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Refer","shopsystem","com.taobao.item.service.CardCodeService:1.0.0-L0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
							
							}
							
							
							if(appname.equals("shopcenter")){
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-provider","com.taobao.item.service.CardCodeService:1.0.0-L0","getCardCodeInfoByQuery"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"HSF-provider","com.taobao.item.service.ItemBidService:1.0.0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								
								
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Refer","detail","com.taobao.item.service.ItemBidService:1.0.0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Refer","shopsystem","com.taobao.item.service.CardCodeService:1.0.0-L0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.NO,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
							
							}
							
							
							if(appname.equals("detail")){
								
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Consumer","shopcenter","com.taobao.item.service.ItemBidService:1.0.0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Consumer","itemcenter","com.taobao.item.service.CardCodeService:1.0.0-L0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
							
							}
							
							
							if(appname.equals("shopsystem")){
								
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Consumer","shopcenter","com.taobao.item.service.ItemBidService:1.0.0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
								CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
										new String[]{"HSF-Consumer","itemcenter","com.taobao.item.service.CardCodeService:1.0.0-L0","updateAuctionQuantity"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size"}, new Object[]{randomInt(),randomInt(),randomInt()});
							
							}
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"Tair-Consumer","groupSession1","namespace1","get"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size","sucRate","error","timeout"}, new Object[]{randomInt(),randomInt(),randomInt(),randomInt(),randomInt(),randomInt()});
				
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"Tair-Consumer","groupSession2","namespace1","get"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size","sucRate","error","timeout"}, new Object[]{randomInt(),randomInt(),randomInt(),randomInt(),randomInt(),randomInt()});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"Tair-Consumer","groupSession3","namespace1","get"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size","sucRate","error","timeout"}, new Object[]{randomInt(),randomInt(),randomInt(),randomInt(),randomInt(),randomInt()});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"Tair-Consumer","groupSession4","namespace1","get"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL,KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times","C-time","P-size","sucRate","error","timeout"}, new Object[]{randomInt(),randomInt(),randomInt(),randomInt(),randomInt(),randomInt()});
				
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"TOPINFO"}, new KeyScope[]{KeyScope.HOST}, new String[]{"CPU","LOAD","SWAP"}, new Object[]{randomFloat(),randomFloat(),randomFloat()},new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.REPLACE,ValueOperate.REPLACE});
							
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"JVMINFO"}, new KeyScope[]{KeyScope.HOST}, new String[]{"JVMMEMORY","JVMGC","JVMFULLGC","JVMCMSGC"}, new Object[]{randomFloat(),randomInt(),randomInt(),randomInt()},new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"Exception","XPathException"}, new KeyScope[]{KeyScope.ALL,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{randomInt()});
							
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{"PV-Block"}, new KeyScope[]{KeyScope.ALL}, new String[]{"PV-TDOD","PV-SS"}, new Object[]{randomInt(),randomInt()},new ValueOperate[]{ValueOperate.REPLACE,ValueOperate.ADD});
						
						
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.NOTIFY_CONSUMER,"groupSession4","type1"}, new KeyScope[]{KeyScope.APP,KeyScope.ALL,KeyScope.ALL}, new String[]{"s","s_rt","s_f","timeout"}, new Object[]{randomInt(),randomInt(),randomInt(),randomInt()});
				
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.NOTIFY_CONSUMER,"groupSession3","type2"}, new KeyScope[]{KeyScope.APP,KeyScope.ALL,KeyScope.ALL}, new String[]{"s","s_rt"}, new Object[]{randomInt(),randomInt()});
							
							CollectDataUtilMulti.collect(appname, ip, cal.getTimeInMillis(), 
									new String[]{KeyConstants.SEARCH_CONSUMER,"URL-action_action"}, new KeyScope[]{KeyScope.APP,KeyScope.ALL}, new String[]{"E-times","C-time"}, new Object[]{randomInt(),randomInt()});
				
						}
						
					} catch (Exception e) {
						e.printStackTrace();
					}
				
			}
			
			
				try {
					Thread.sleep(60000);
				} catch (InterruptedException e1) {
				}
				
			
		}
		
		
		
	}

}
