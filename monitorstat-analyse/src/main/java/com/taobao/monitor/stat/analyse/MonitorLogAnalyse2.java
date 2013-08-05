package com.taobao.monitor.stat.analyse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.OpsFreeHostCache;
import com.taobao.monitor.stat.content.ReportContentInterface;
import com.taobao.monitor.stat.util.DepIpInfo;
import com.taobao.monitor.stat.util.DepIpInfoContain;
/**
 * 
 * 记录单位时间内的 执行次数和 时间
 * 
 * 
 * 
 * 
 * @author xiaodu
 *
 *
 * SearchEngine SearchEngine    http://auction-search.config-vip.taobao.com:2088/bin/search?    auction 23755   340     2010-04-07 00:03:56     list101b.cm3.tbsite.net
 * forest forest  client  getCatMapMainCategories 0       566     2010-04-07 00:01:56     list101b.cm3.tbsite.net
 * PageCache-请求数
 * PageCache-存Cache	
	PageCache-读Cache命中次数
	TairClient TairClient 2.2.3        put     level-3 685     838     2010-04-07 00:01:28     item123c.cm3
	HSF-Consumer-BizException
	HSF-Consumer-Exception
	HSF-Consumer HSF-Consumer    com.taobao.uic.common.service.userinfo.UserReadService:1.0.0    getUserAndUserExtraListByUserIds        org.springframework.aop.support.AopUtils@invokeJoinpointUsingReflection@304 1       3       2010-04-06 00:03:05     detail43.cm3.tbsite.net
	PageCache-parse次数 PageCache-请求数        level-2 level-3 109     499     2010-04-07 00:03:56     list101b.cm3.tbsite.net
	PageCache-读Cache次数 PageCache-读Cache命中次数       level-2 level-3 5643    0       2010-04-07 00:01:28     item123c.cm3

 * 
 */
public class MonitorLogAnalyse2 extends AnalyseFile {	
	private static final Logger logger =  Logger.getLogger(MonitorLogAnalyse2.class);
	
	private Map<String,Map<String,Hsf>> cacheValue = new HashMap<String, Map<String,Hsf>>();
	
	
	private long hsfPvAll = 0;
	
	
	public MonitorLogAnalyse2(String appName) throws Exception {
		super(appName,"monitor-app-",'\02');		
	}	
	
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private void computeHsf(long e,long t,String collectTime){
		hsfPvAll+=e;		
	}
	
	
	/**
	 * 分析临时文件，并统计得到采样时间内的 平均数据
	 * @param filepath
	 */
	private void parseTimeSelectLogLine(String logRecord,String collectTime){
		
		if(collectTime==null)return ;
		
		Map<String,Hsf> mapHsf = cacheValue.get(collectTime);
		if(mapHsf==null){
			mapHsf = new HashMap<String, Hsf>();
			cacheValue.put(collectTime, mapHsf);
		}
		
		String[] _values = StringUtils.splitPreserveAllTokens(logRecord, '\01');
		{
			String key1 = _values[0];
			if("ViewShop".equals(key1)){
				String key2 = _values[1];
				String key3 = _values[2];
				String time = _values[3];
				String valueData = _values[4];
				Hsf hsf =mapHsf.get("PV");
				if(hsf==null){
					hsf = new Hsf();
					mapHsf.put("PV", hsf);
				}
				hsf.count+=Long.parseLong(valueData);
				hsf.useTime+=Long.parseLong(time);
				hsf.size++;
				
			}
			
		}
		
		
		{
			
			String key1 = _values[0];
			
			//buy提交立即购买level-3305762010-04-12 23:19:49v022086.cm4.tbsite.net
			//buy添加到购物车level-3300112212010-04-10 15:38:26buy2.cm3.tbsite.net
			if("BUY".equals(key1.toUpperCase())){			
				String key2 = _values[1];
				String key3 = _values[2];
				String time = _values[3];
				String valueData = _values[4];
				
				
				String key = null;
				if("立即购买创建订单".equals(_values[1])){
					key = "SUBMITBUY";
				}			
				if("提交立即购买Exception".equals(_values[1])){
					key = "SUBMITBUY_Exception";
				}
				
				
				if("添加购物车".equals(_values[1])){
					key="SHOPPINGCART";
				}
				if(key!=null){
				
					Hsf hsf =mapHsf.get(key);
					if(hsf==null){
						hsf = new Hsf();
						mapHsf.put(key, hsf);
					}
					hsf.count+=Long.parseLong(valueData);
					hsf.useTime+=Long.parseLong(time);
					hsf.size++;
				}
			}
		}
		
		
		
		try{									
			String[] logResult = _values;
		
		String titleKey = logResult[0];
		
		String _titleKey = titleKey;
		//HSF-Consumer-RequestSize
		if(titleKey.startsWith("HSF-Consumer-RequestSize")){
			
			if(logRecord.indexOf("JVM")>-1)return;
			
			String flagName = "OUT_";						
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[4];
			String times = logResult[5];	
			String key =flagName+_titleKey+"_"+logkey1+"_"+logkey2;
			
			
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("HSF-Consumer")){
			
			if(logRecord.indexOf("JVM")>-1)return;
			
			String flagName = "OUT_";						
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[4];
			String times = logResult[5];	
			String key =flagName+_titleKey+"_"+logkey1+"_"+logkey2;
			
			
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("HSF-ProviderDetail")){
			if(logRecord.indexOf("JVM")>-1)return;
			//HSF-ProviderDetail^Acom.taobao.uic.common.service.userinfo.UserReadService:1.0.0^AgetUserAndUserExtraById^A172.23.4.54^A7^A11^A2010-11-08 00:01:23^Auic013055.cm3^
			//HSF-ProviderDetail      com.taobao.item.service.ItemService:1.0.0-L1    sellerUploadPropertyImage       172.24.22.52    1       10      2010-04-10 00:01:13     v015180.cm4.tbsite.net
			
			parseHshProvider(logResult);
			parseHshCustomer(logResult);
			
			String flagName = "IN_";						
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[4];
			String times = logResult[5];	
			String key =flagName+_titleKey+"_"+logkey1+"_"+logkey2;
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
			
			computeHsf(Long.parseLong(executes),Long.parseLong(times),collectTime);
			
		}else if(titleKey.startsWith("PageCache")){			
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[3];
			String times = logResult[4];							
			String key ="OUT_"+_titleKey+"_"+logkey1+"_"+logkey2;				
				
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("forest")){
			//forest  client  getCatMapCategory       0       111     2010-04-10 00:01:46     item121a.cm4.tbsite.net
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String times = logResult[3];
			String executes = logResult[4];							
			String key ="OUT_"+_titleKey+"_"+logkey1+"_"+logkey2;	
				
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("SearchEngine")){			
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String  times= logResult[3];
			String  executes= logResult[4];							
			String key ="OUT_"+_titleKey+"_"+logkey1+"_"+logkey2;				
		
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("TairClient")){
			//TairClient 2.2.3        put     level-3 855     1057    2010-04-10 00:03:46     item121a.cm4.tbsite.net
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[3];
			String times = logResult[4];							
			String key ="OUT_"+_titleKey+"_"+logkey1+"_"+logkey2;					
			
			
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("mytaobao")){
			//mytaobao^AJustDoFirstModule^A87537560^A50^A1^A2010-05-21 00:05:58^Adetail51c.cm3^
			String logkey1 = logResult[1];
			//String logkey2 = logResult[2];
			String executes = logResult[4];
			String times = logResult[3];					

			String key ="IN_HSF-ProviderDetail_"+_titleKey+"_"+logkey1;//+logkey2;
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("TcOutSystemMonitor")){
			// TcOutSystemMonitor^AKeycenterCheck-Success^Alevel-3^A358^A2110^A2010-06-02 00:01:32^Av015119.cm3^B
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[3];
			String times = logResult[4];					

			String key ="OTHER_"+_titleKey+"_"+logkey1+"-"+logkey2;//+logkey2;
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("ic-client")){
			// ic-client^AItemQueryService^AqueryItemForDetail^A7774^A116010-06-22 00:01:48^Aitem101d.cm3
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[4];
			String times = logResult[3];					

			String key ="OTHER_"+_titleKey+"_"+logkey1+"-"+logkey2;//+logkey2;
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("交易线重要页面统计")){
			// 交易线重要页面统计^A普通商品页面^Aok^A776^A74^A2010-06-02 00:01:15^Abuy11.cm3
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[4];
			String times = logResult[3];					

			String key ="OTHER_"+_titleKey+"_"+logkey1+"-"+logkey2;//+logkey2;
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("buy")){
			// buy^A立即购买创建订单^Aok^A35593^A55^A2010-08-05 00:01:39^Abuy11.cm3^
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[4];
			String times = logResult[3];					

			String key ="OTHER_"+_titleKey+"_"+logkey1+"-"+logkey2;//+logkey2;
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else if(titleKey.startsWith("webww")){
			//  webww^AwriteToWWServer^Atime^A456^A7^A2010-06-02 00:09:59^Awebwangwang019073.cm4^
			String logkey1 = logResult[1];
			String logkey2 = logResult[2];
			String executes = logResult[3];
			String times = logResult[4];					

			String key ="OTHER_"+_titleKey+"_"+logkey1+"-"+logkey2;//+logkey2;
			Hsf hsf =mapHsf.get(key);
			if(hsf==null){
				hsf = new Hsf();
				mapHsf.put(key, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
		}else{
			//logger.debug("不在分析列:"+logRecord);
		}
		}catch(Exception e){
			logger.error("分析:"+logRecord+" 出错",e);
		}			
	}
	

	@Override
	protected void parseLogLine(String logRecord) {		
		String time = parseLogLineCollectTime(logRecord);
		parseTimeSelectLogLine(logRecord,time);		
	}


	@Override
	protected String parseLogLineCollectTime(String logRecord) {		
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d \\d\\d:\\d\\d)");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			return m.group(1);
		}
		
		return null;
	}
	@Override
	protected String parseLogLineCollectDate(String logRecord) {
		Pattern pattern = Pattern.compile("(\\d\\d\\d\\d-\\d\\d-\\d\\d) ");		
		Matcher m = pattern.matcher(logRecord);		
		if(m.find()){
			return m.group(1);
		}
		
		return null;
	}
	
	private class Hsf {
		private long count;		
		private long useTime;		
		private long size;
		
	}
	//Map<keyName,Map<customer_ip,Hsf>>
	private Map<String,Map<String,Hsf>> hsfProviderMap = new HashMap<String, Map<String,Hsf>>();
	
	private void parseHshProvider(String[] logResult){
		//HSF-ProviderDetail^Acom.taobao.uic.common.service.userinfo.UserReadService:1.0.0^AgetUserAndUserExtraById^A172.23.4.54^A7^A11^A2010-11-08 00:01:23^Auic013055.cm3^
		//HSF-ProviderDetail      com.taobao.item.service.ItemService:1.0.0-L1    sellerUploadPropertyImage       172.24.22.52    1       10      2010-04-10 00:01:13     v015180.cm4.tbsite.net
		String flagName = "IN_";
		String _titleKey = logResult[0];
		String logkey1 = logResult[1];
		String logkey2 = logResult[2];
		String ip = logResult[3];
		String executes = logResult[4];
		String times = logResult[5];	
		String key =flagName+_titleKey+"_"+logkey1+"_"+logkey2;
		Map<String,Hsf> hsfIpMap =hsfProviderMap.get(key);
		if(hsfIpMap==null){
			hsfIpMap = new HashMap<String, Hsf>();
			hsfProviderMap.put(key, hsfIpMap);
		}
		Hsf hsf = hsfIpMap.get(ip);
		if(hsf == null){
			hsf = new Hsf();
			hsfIpMap.put(ip, hsf);
		}		
		hsf.count+=Long.parseLong(executes);
		hsf.useTime+=Long.parseLong(times);
		hsf.size++;
	}
	
	
	//Map<KeyName,Map<customerAppName,Map<provider_ip,Hsf>>>
	private Map<String,Map<String,Map<String,Hsf>>> hsfCustomerMap = new HashMap<String, Map<String,Map<String,Hsf>>>();
	
	private void parseHshCustomer(String[] logResult){
		
		//HSF-ProviderDetail^Acom.taobao.uic.common.service.userinfo.UserReadService:1.0.0^AgetUserAndUserExtraById^A172.23.4.54^A7^A11^A2010-11-08 00:01:23^Auic013055.cm3^
		String flagName = "IN_";
		String _titleKey = logResult[0];
		String logkey1 = logResult[1];
		String logkey2 = logResult[2];
		String customerIp = logResult[3];
		String executes = logResult[4];
		String times = logResult[5];
		String machineName = logResult[7];
		String key =flagName+_titleKey+"_"+logkey1+"_"+logkey2;		
		DepIpInfo ipInfo = DepIpInfoContain.get().getByIp(customerIp);
		if(ipInfo == null){
			return ;
		}
		Map<String,Map<String,Hsf>> map = hsfCustomerMap.get(key);
		if(map == null){
			map = new HashMap<String, Map<String,Hsf>>();
			hsfCustomerMap.put(key, map);
		}
		
		if(ipInfo != null){			
			Map<String,Hsf> provideMap = map.get(ipInfo.getAppName());
			if(provideMap == null){
				provideMap = new HashMap<String, Hsf>();
				map.put(ipInfo.getAppName(), provideMap);
			}
			Hsf hsf = provideMap.get(machineName);
			if(hsf == null){
				hsf = new Hsf();
				provideMap.put(machineName, hsf);
			}
			hsf.count+=Long.parseLong(executes);
			hsf.useTime+=Long.parseLong(times);
			hsf.size++;
			
			
		}
		
	}
	
	

	@Override
	protected void insertToDb(ReportContentInterface content) {
		
		Map<String,Hsf> allRecode = new HashMap<String, Hsf>();
		
		Map<String,Hsf> timeRecode = new HashMap<String, Hsf>();
		
		Iterator<Map.Entry<String,Map<String,Hsf>>> it = cacheValue.entrySet().iterator();
		while(it.hasNext()){			
			Map.Entry<String,Map<String,Hsf>> entry = it.next();			
			String collectTime = entry.getKey();
			Map<String,Hsf> keyMap = entry.getValue();			
			for(Map.Entry<String, Hsf> countEntry:keyMap.entrySet()){
				String key = countEntry.getKey();
				Hsf count =  countEntry.getValue();	
				
				Hsf allhsf = allRecode.get(key);
				if(allhsf==null){
					allhsf = new Hsf();
					allRecode.put(key, allhsf);
				}
				
				allhsf.count+=count.count;
				allhsf.useTime+=count.useTime;
				allhsf.size+=count.size;
				
				if("PV".equals(key)){
					content.putReportData(this.getAppName(), key,count.count, collectTime);
				}else{
					content.putReportData(this.getAppName(), key+"_"+Constants.COUNT_TIMES_FLAG,count.count, collectTime);	
					if(count.count!=0){
						content.putReportData(this.getAppName(), key+"_"+Constants.AVERAGE_USERTIMES_FLAG,count.useTime/count.count, collectTime);
					}else{
						content.putReportData(this.getAppName(), key+"_"+Constants.AVERAGE_USERTIMES_FLAG,"0", collectTime);
					}
				}
				
				
				if(key.indexOf("_HSF-ProviderDetail_")>-1){
					Hsf hsf = timeRecode.get(collectTime);
					if(hsf == null){
						hsf  = new Hsf();
						timeRecode.put(collectTime, hsf);
					}
					hsf.count+=count.count;
					hsf.useTime+=count.useTime;
				}
			}
		}
		
		
		
		
		
		
		
		content.putReportDataByCount(this.getAppName(), "IN_HSF_AllInterfacePV_"+Constants.COUNT_TIMES_FLAG, hsfPvAll, this.getCollectDate());
		Hsf rushTimeHsf = new Hsf();		
		for(Map.Entry<String,Hsf> entry:timeRecode.entrySet()){
			String time = entry.getKey();
			Hsf h = entry.getValue();
			content.putReportData(this.getAppName(), "IN_HSF_AllInterfacePV_"+Constants.COUNT_TIMES_FLAG, h.count, time);
		
			Date date;
			try {
				date = sdf.parse(time);
				if(20<=date.getHours()&&date.getHours() <=22){
					rushTimeHsf.count+=h.count;
					rushTimeHsf.useTime+=h.useTime;
					rushTimeHsf.size++;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		}
		
		List<HostPo> hostList= OpsFreeHostCache.get().getHostListNoCache(this.getAppName());
		
		if(rushTimeHsf.size != 0){
			long qps = (rushTimeHsf.count/rushTimeHsf.size)/(hostList.size()*120);
			content.putReportDataByCount(this.getAppName(), "IN_HSF_AllInterfaceQps_"+Constants.AVERAGE_USERTIMES_FLAG,qps, this.getCollectDate());
			content.putReportDataByCount(this.getAppName(), "IN_HSF_AllInterfaceRest_"+Constants.AVERAGE_USERTIMES_FLAG,Arith.div(rushTimeHsf.useTime, rushTimeHsf.count, 2)+"", this.getCollectDate());
		}
		
		
		
		
		for(Map.Entry<String,Hsf> entry:allRecode.entrySet()){			
			if("PV".equals(entry.getKey())){
				content.putReportDataByCount(this.getAppName(), entry.getKey(), entry.getValue().count, this.getCollectDate());
			}else{
				content.putReportDataByCount(this.getAppName(), entry.getKey()+"_"+Constants.COUNT_TIMES_FLAG, entry.getValue().count, this.getCollectDate());
				if(entry.getValue().count!=0){
					content.putReportDataByCount(this.getAppName(), entry.getKey()+"_"+Constants.AVERAGE_USERTIMES_FLAG,entry.getValue().useTime/entry.getValue().count, this.getCollectDate());
				}else{
					content.putReportDataByCount(this.getAppName(), entry.getKey()+"_"+Constants.AVERAGE_USERTIMES_FLAG,"0", this.getCollectDate());
				}
			}
		}
		
		
		
		for(Map.Entry<String,Map<String,Hsf>> entry:hsfProviderMap.entrySet()){
			String keyName =  entry.getKey();
			for(Map.Entry<String , Hsf> entryHdf:entry.getValue().entrySet()){
				String ip = entryHdf.getKey();
				Hsf allhsf = entryHdf.getValue();
				content.putReportDataByProvider(this.getAppName(), keyName, ip, allhsf.count, Arith.div(allhsf.useTime, allhsf.count, 2), this.getCollectDate());
				
			}
		}
		hsfProviderMap.clear();
		
		//Map<KeyName,Map<customerAppName,Map<provider_ip,Hsf>>>
		for(Map.Entry<String,Map<String,Map<String,Hsf>>> entry:hsfCustomerMap.entrySet()){
			String keyName =  entry.getKey();
			for(Map.Entry<String,Map<String,Hsf>> entryProvide:entry.getValue().entrySet()){
				String customerAppName = entryProvide.getKey();
				Map<String,Hsf> allhsf = entryProvide.getValue();
				for(Map.Entry<String,Hsf> entryHsf:allhsf.entrySet()){
					String provider_ip = entryHsf.getKey();
					Hsf hsf = entryHsf.getValue();
					content.putReportDataByCustomer(customerAppName,this.getAppName(), keyName, provider_ip, hsf.count, Arith.div(hsf.useTime, hsf.count, 2), this.getCollectDate());
				}
			}
		}
		
		hsfCustomerMap.clear();
		
		
	}

}
