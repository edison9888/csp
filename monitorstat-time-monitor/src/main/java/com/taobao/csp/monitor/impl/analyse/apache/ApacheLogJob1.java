//
///**
// * monitorstat-monitor
// */
//package com.taobao.csp.monitor.impl.analyse.apache;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Locale;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//
//import com.taobao.csp.dataserver.KeyConstants;
//import com.taobao.csp.dataserver.collect.CollectDataUtilMulti;
//import com.taobao.csp.dataserver.item.KeyScope;
//import com.taobao.csp.dataserver.item.ValueOperate;
//import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;
//import com.taobao.monitor.common.po.HostPo;
//import com.taobao.monitor.common.util.CspCacheTBHostInfos;
//
///**
// * 
// * 下面是完整的一个apache 请求日志
// *218.61.9.168 19476 - [21/Feb/2012:10:41:23 +0800] "GET http://item.taobao.com/item.htm?spm=1103GB_3.1-2ngzA.s-2bc5Ha&id=8309787464" 200 22088 "http://shop61497219.taobao.com/" "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; Trident/4.0)" -
// * 
// * 
// * 分析目标是：
// * 获取PV的流量 时间 大小 "C-200","C-302","c-other"
// * 
// * 通过IP 获取区域信息和网络信息
// * 
// * 自身URL(TOP10) 信息和refer (TOP20)信息
// * 
// * 这个rt时间是只和200做除法
// * 
// * @author xiaodu
// * 上午9:51:11
// */
//public class ApacheLogJob1 extends AbstractDataAnalyse {
//	
//	private static final Logger logger = Logger.getLogger(ApacheLogJob.class);
//	
//	private SimpleDateFormat rTimeFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm", Locale.ENGLISH);
//	
//	public ApacheLogJob1(String appName,String ip,String feature){
//		super(appName,ip,feature);
//	} 
//	
//	
//	public void analyseOneLine(String line) {
//		try{
//			
//			if (line.indexOf("status.taobao") > 0) {
//				return;
//			}
//			
//			String[] tmp =  StringUtils.splitPreserveAllTokens(line, "\"");
//			
//			String[] p1 =StringUtils.splitByWholeSeparator( tmp[0], " ");
//			
//			String ip = p1[0];
//			
//			String referIp = tmp[tmp.length-1].trim();
//			if(!referIp.equals("-")){
//				String[] t = referIp.split("\\.");
//				if(t.length == 4){
//					ip = referIp;
//				}
//			}
//			
//			
//			
//			int rest = Integer.parseInt(p1[1]);
//			String time = p1[p1.length-3];
//			String source_url =  tmp[1].substring(4);
//			String httpCode =  "500";
//			String pagesize = "";
//			String refer_url = null;
//			try{
//				 String[] p2 =StringUtils.splitByWholeSeparator( tmp[2], " ");
//				 httpCode =  p2[0];
//				 pagesize = p2[1];
//				 refer_url = tmp[3];
//			}catch (Exception e) {
//				try{
//					String[] p2 =StringUtils.splitByWholeSeparator( tmp[3], " ");
//					httpCode =  p2[0];
//					pagesize = p2[1];
//					refer_url = tmp[4];
//				 }catch (Exception e1) {
//				}
//			}
//			
//			Date collectTime = rTimeFormat.parse(time.substring(1,18));
//			long cTime = collectTime.getTime();
//			
//			boolean hit = true;
//			if(referIp.indexOf("MISS")>0){
//				hit = false;
//			}
//			
//			analyseSource(cTime,rest,pagesize,httpCode,source_url,hit);
//			analyseIp(cTime,ip);
//			analyseRefer(cTime,refer_url);
//		}catch (Exception e) {
//			logger.error("分析"+line+" "+this.getAppName(), e);
//		}
//	}
//	
//	
//	public void submit(){
//		
//		if(sourceUrl.size()==0){
//			
//			
//			Calendar cal = Calendar.getInstance();
//			cal.set(Calendar.SECOND, 0);
//			cal.set(Calendar.MILLISECOND, 0);
//			
//			try {
//				CollectDataUtilMulti.collect(getAppName(), getIp(), cal.getTimeInMillis(), new String[]{KeyConstants.PV},
//						new KeyScope[]{KeyScope.ALL}, new String[]{"E-times"}, 
//						new Object[]{0},new ValueOperate[]{ValueOperate.ADD});
//			} catch (Exception e) {
//				logger.error("发送失败", e);
//			}
//			
//			return ;
//		}
//		
//		
//		
//		Map<Long,PvInfo> allMap = new HashMap<Long, PvInfo>();
//		
//		for(Map.Entry<Long,Map<String,PvInfo>> entry:sourceUrl.entrySet()){
//			Long time = entry.getKey();
//			Map<String,PvInfo> v = entry.getValue();
//			for(Map.Entry<String,PvInfo> ventry:v.entrySet()){
//				PvInfo m = ventry.getValue();
//				
//				
//				
//				PvInfo all =allMap.get(time);
//				if(all == null){
//					all =  new PvInfo();
//					allMap.put(time, all);
//				}
//				
//				all.allPv+=m.allPv;
//				all.hitpv+=m.hitpv;
//				all.pv+=m.pv;
//				all.rt+=m.rt;
//				all.pagesize+=m.pagesize;
//				all.c200+=m.c200;
//				all.c204+=m.c204;
//				all.c304+=m.c304;
//				all.rterror+=m.rterror;
//				all.rt100+=m.rt100;
//				all.rt500+=m.rt500;
//				all.rt1000+=m.rt1000;
//				if(m.pv <1){
//					continue;
//				}
//				String url = ventry.getKey();
//				int rt = (int)(m.rt/m.pv/1000l);
//				
//				if(m.c200!=0){
//					rt =  (int)(m.rt/m.c200/1000l);
//				}
//				
//				
//				int p = (m.pagesize/m.pv);
//			
//				try {
//					Object[] values = new Object[]{m.allPv,rt,p,m.c200,m.c302,m.rterror};
//					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.PV,url},
//							new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times","C-time","P-size","C-200","C-302","rt_error"}, 
//							values,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
//					} catch (Exception e) {
//					logger.error("发送失败", e);
//				}
//			}
//		}
//		
//		for(Map.Entry<Long,PvInfo> entry:allMap.entrySet()){
//			try {
//				PvInfo m = entry.getValue();
//				if(m.pv ==0){
//					continue;
//				}
//				
//			
//				int rt = (int)(m.rt/m.pv/1000l);
//				if(m.c200!=0){
//					rt =  (int)(m.rt/m.c200/1000l);
//				}
//				
//				int p = (m.pagesize/m.pv);
//				
//				
//				if("detail".equals(getAppName())){
//					Object[] values = new Object[]{m.allPv,rt,p,m.c200,m.c302,m.rterror,m.rt100,m.rt500,m.rt1000,m.c204,m.hitpv,m.c304};
//					CollectDataUtilMulti.collect(getAppName(), getIp(), entry.getKey(), new String[]{KeyConstants.PV},
//						new KeyScope[]{KeyScope.ALL}, new String[]{"E-times","C-time","P-size","C-200","C-302","rt_error","rt_100","rt_500","rt_1000","C-204","pv-hit","C-304"}, 
//						values,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE,ValueOperate.AVERAGE,
//						ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,
//						ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
//				}else{
//					Object[] values = new Object[]{m.allPv,rt,p,m.c200,m.c302,m.rterror,m.rt100,m.rt500,m.rt1000};
//					CollectDataUtilMulti.collect(getAppName(), getIp(), entry.getKey(), new String[]{KeyConstants.PV},
//							new KeyScope[]{KeyScope.ALL}, new String[]{"E-times","C-time","P-size","C-200","C-302","rt_error","rt_100","rt_500","rt_1000"}, 
//							values,new ValueOperate[]{ValueOperate.ADD,ValueOperate.AVERAGE,ValueOperate.AVERAGE,ValueOperate.ADD,ValueOperate.ADD,
//						ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD,ValueOperate.ADD});
//				}
//				
//				HostPo host = CspCacheTBHostInfos.get().getHostInfoByIp(getIp());
//				if(host!= null){
//					CollectDataUtilMulti.collect(getAppName(), getIp(), entry.getKey(), new String[]{"PV-CM",host.getHostSite().toUpperCase()},
//							new KeyScope[]{KeyScope.NO,KeyScope.APP}, new String[]{"E-times"}, 
//							new Object[]{m.allPv},new ValueOperate[]{ValueOperate.ADD});
//				}
//			} catch (Exception e) {
//				logger.error("发送失败", e);
//			}
//		}
//		
//		
//		
//		
//		for(Map.Entry<Long,Map<String,Integer>> entry: referTimeMap.entrySet()){
//			Long time = entry.getKey();
//			for(Map.Entry<String,Integer> ventry:entry.getValue().entrySet()){
//				String refer = ventry.getKey();
//				Integer count = ventry.getValue();
//				if(count <5){//如果总量少于10的直接放弃
//					continue;
//				}
//				try {
//					CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.PV_REFER,refer}, new KeyScope[]{KeyScope.NO,KeyScope.ALL}, new String[]{"E-times"}, new Object[]{count});
//				} catch (Exception e) {
//					logger.error("发送失败", e);
//				}
//				
//			}
//		}
//		
//		
//		Map<Long,Map<String,Integer>> timeallnetMap = new HashMap<Long, Map<String,Integer>>();
//		
//		for(Map.Entry<Long,Map<String,Map<String,Integer>>> entry:regionTimeMap.entrySet()){
//			Long time = entry.getKey();
//			Map<String,Map<String,Integer>> netMap = entry.getValue();
//			for(Map.Entry<String,Map<String,Integer>> netentry:netMap.entrySet()){
//				String net = netentry.getKey();
//				
//				for(Map.Entry<String,Integer> provinceEntry:netentry.getValue().entrySet()){
//					String province = provinceEntry.getKey();
//					Integer count = provinceEntry.getValue();
//					
//					Map<String,Integer> allnetMap = timeallnetMap.get(time);
//					if(allnetMap == null){
//						allnetMap = new HashMap<String, Integer>();
//						timeallnetMap.put(time, allnetMap);
//					}
//					
//					Integer allnet = allnetMap.get(net);
//					if(allnet == null){
//						allnetMap.put(net, count);
//					}else{
//						allnetMap.put(net, count+allnet);
//					}
//					
//					if(count <5){//如果总量少于10的直接放弃
//						continue;
//					}
//					try {
//						CollectDataUtilMulti.collect(getAppName(), getIp(), time, new String[]{KeyConstants.PV_REGION,province}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, new String[]{"E-times"}, new Object[]{count});
//					} catch (Exception e) {
//						logger.error("发送失败", e);
//					}
//				}
//			}
//		}
//		
//		for(Map.Entry<Long,Map<String,Integer>> entry: timeallnetMap.entrySet() ){
//			
//			for(Map.Entry<String,Integer> k:entry.getValue().entrySet()){
//				
//				if(k.getValue() <5){//如果总量少于10的直接放弃
//					continue;
//				}
//				
//				try {
//					CollectDataUtilMulti.collect(getAppName(), getIp(), entry.getKey(), new String[]{KeyConstants.PV_NETWORK,k.getKey()}, new KeyScope[]{KeyScope.NO,KeyScope.APP}, new String[]{"E-times"}, new Object[]{k.getValue()});
//				} catch (Exception e) {
//					logger.error("发送失败", e);
//				}
//			}
//		}
//		
//		
//		
//	}
//	
//	
//	
//	//
//	//Map<时间,Map<URL,int[]>>
//	//int[0]=pv,int[1]=rest,int[2]=psize,int[3]=c200,int[4]=c302,int[5]=cother	
//	Map<Long,Map<String,PvInfo>> sourceUrl = new HashMap<Long, Map<String,PvInfo>>();
//	
//	/**
//	 * 解析URL的流量信息
//	 * @param rest
//	 * @param pagesize
//	 * @param httpCode
//	 * @param source_url
//	 */
//	public void analyseSource(long cTime,int rt,String pagesize,String httpCode,String source_url,boolean ishit){
//		String url = AppUrlCache.get().translateUrl(source_url);
//		
//		int code =0;
//		try{
//			code = Integer.parseInt(httpCode);
//		}catch (Exception e) {
//		}
//		int psize =0;
//		try{
//			psize = Integer.parseInt(pagesize);
//		}catch (Exception e) {
//		}
//		Map<String,PvInfo> urlMap = sourceUrl.get(cTime);
//		if(urlMap == null){
//			urlMap = new HashMap<String,PvInfo>();
//			sourceUrl.put(cTime, urlMap);
//		}
//		
//		PvInfo v = urlMap.get(url);
//		if(v== null){
//			v = new PvInfo();
//			urlMap.put(url, v);
//		}
//		
//		if(ishit){
//			v.hitpv +=1;
//		}
//		
//		v.allPv+=1;
//		
//		if(rt >= 1000000){//如果这个请求时间大于一分钟 ，就记录上问题请求
//			v.rterror+=1;// 问题
//		}else{
//			
//			if(500000<rt&&rt<1000000){
//				v.rt1000+=1;
//			}else if(100000<rt&&rt<500000){
//				v.rt500+=1;
//			}else if(rt<100000){
//				v.rt100+=1;
//			}
//			v.pv+=1;//pv量
//			v.rt+=rt;//响应时间
//			v.pagesize+=psize;//字节数
//			if(code == 200){
//				v.c200+=1;
//			}else if(code ==302){
//				v.c302+=1;
//			}else if(code ==204){
//				v.c204+=1;
//			}else if(code ==304){
//				v.c304+=1;
//			}
//		}
//	}
//	
//	
//	
//	
//	//Map<URL,Integer>
//	Map<Long,Map<String,Integer>> referTimeMap = new HashMap<Long, Map<String,Integer>>();
//	/**
//	 * 分析来源URL
//	 * @param referUrl
//	 */
//	protected void analyseRefer(long cTime,String referUrl){
//		if(referUrl == null){
//			return ;
//		}
//		
//		Map<String,Integer> referMap = referTimeMap.get(cTime);
//		if(referMap == null){
//			referMap = new HashMap<String, Integer>();
//			referTimeMap.put(cTime, referMap);
//		}
//		
//		String refer = AppUrlCache.get().translateUrl(referUrl);
//		
//		Integer count = referMap.get(refer);
//		if(count == null){
//			referMap.put(refer, 1);
//		}else{
//			referMap.put(refer, 1+count);
//		}
//	}
//	
//	
//	
//	//Map<network,Map<province,Map<city,Integer>>>
//	private Map<Long,Map<String,Map<String,Integer>>> regionTimeMap =new HashMap<Long, Map<String,Map<String,Integer>>>();
//	/**
//	 * 分析 ip的地区和网络 ，结果保存在regionMap中
//	 * @param ip
//	 */
//	protected void analyseIp(long cTime,String ip){
//		
//		
//		Map<String,Map<String,Integer>>  regionMap=regionTimeMap.get(cTime);
//		
//		
//		IpRegion region = IpRegionCache.getIpRegion(ip);
//		String network = "未知";
//		String province = "未知";
//		
//		if(region != null){
//			network = region.getNetwork();
//			province = region.getProvince();
//		}
//		if(regionMap == null){
//			regionMap = new HashMap<String, Map<String,Integer>>();
//			regionTimeMap.put(cTime, regionMap);
//		}
//		
//		Map<String,Integer> netMap = regionMap.get(network);
//		if(netMap == null){
//			netMap = new HashMap<String,Integer>();
//			regionMap.put(network, netMap);
//		}
//		
//		Integer count = netMap.get(province);
//		if(count == null){
//			netMap.put(province, 1);
//		}else{
//			netMap.put(province, 1+count);
//		}
//	}
//	
//	
//	
//	public static void main(String[] args){
//		
//		ApacheLogJob job = new ApacheLogJob("detail","172.17.134.4","");
//		job.analyseOneLine("60.174.100.133 19430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 111119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 11119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 1119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 519430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 19430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 111119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 11119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 1119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 19430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 111119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 1119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 119430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.analyseOneLine("60.174.100.133 19430 - [13/Jun/2012:00:00:00 +0800] \"GET http://favorite.taobao.com/json/latest_up_item_list.htm?ownerId=373397553&pageNum=4\" 200 689 \"http://favorite.taobao.com/collect_list.htm?itemtype=0&pagesize=20&isBigImgShow=true&orderby=time&startrow=40\" \"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1; .NET CLR 2.0.50727; 360SE)\"");
//		job.submit();	
//		String line = null;
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("D:\\work\\csp\\monitorstat-time-monitor\\target\\f")));
//			int i = 0;
//			while((line=reader.readLine())!=null){
//			//	System.out.println(line);
//				job.analyseOneLine(line);
//				i++;
//				if(i>10000){
//					job.submit();
//					job.release();
//					Thread.sleep(60000);
//					i=0;
//				}
//			}
//			job.submit();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}  catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
//
//
//
//
//	@Override
//	public void release() {
//		regionTimeMap.clear();
//		sourceUrl.clear();
//		referTimeMap.clear();
//	}
//
//
//	
//
//}
