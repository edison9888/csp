package com.taobao.csp.time.job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.csp.dataserver.PropConstants;
import com.taobao.csp.time.service.CommonServiceInterface;
import com.taobao.csp.time.web.po.TimeDataInfo;
import com.taobao.monitor.common.db.impl.capacity.CspCapacityDao;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityRankingPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.common.util.GroupManager;

public class SyncCapacityPlatform {
	
	private HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
	
	{
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
	}
	
	
	public static Logger logger = Logger.getLogger(SyncCapacityPlatform.class);
	
	private CspCapacityDao cspCapacityDao;
	
	private CommonServiceInterface commonService;
	
	public CspCapacityDao getCspCapacityDao() {
		return cspCapacityDao;
	}

	public void setCspCapacityDao(CspCapacityDao cspCapacityDao) {
		this.cspCapacityDao = cspCapacityDao;
	}

	public CommonServiceInterface getCommonService() {
		return commonService;
	}

	public void setCommonService(CommonServiceInterface commonService) {
		this.commonService = commonService;
	}

	public void syncCoreAppCapacity() {
		
		long t = System.currentTimeMillis();
		
		if (!passCheck()) {
			logger.info("此机器未加入容量的系统变量");
			return;
		}
		List<CapacityRankingPo> apps = cspCapacityDao.findCapacityLatestRankingPos("容量排名");
		Set<Integer> coreApps = getCoreApps();
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		int minute = calendar.get(Calendar.MINUTE);
		if (minute % 5 != 0) {
			minute = (minute / 5) * 5;
		}
		calendar.set(Calendar.MINUTE, minute);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		String time = sf.format(calendar.getTime());
		
		for (CapacityRankingPo rankingPo : apps) {
			int appId = rankingPo.getAppId();
			if (!coreApps.contains(appId)) continue;

			String appName = rankingPo.getAppName();  // 应用名
			String group = "G1";
			
			// 取分组之一代表整个应用
			if (rankingPo.getAppId() == 376) {
				appName = "itemcenter";
				group = "G2";
			}
			if (rankingPo.getAppId() == 383) {
				appName = "tradeplatform";
				group = "G1";
			}
			if (rankingPo.getAppId() == 432) {
				appName = "ump";
				group = "G2";
			}
			
			String appType = rankingPo.getAppType();
			
			String qpsKey = ("center").equals(appType) ? KeyConstants.HSF_PROVIDER : KeyConstants.PV;
			

			double clusterLoad = 0;
			double singleLoad = 0;
			if (appName.equals("itemcenter") || appName.equals("tradeplatform") || appName.equals("ump")) {
				double singleMaxPv = 0;
				double clusterTotalPv = 0;
				List<TimeDataInfo> list = commonService.querykeyRecentlyDataForHostBySort(appName,qpsKey,PropConstants.E_TIMES);
				for (TimeDataInfo timeDataInfo : list) {
					String hostGroup = GroupManager.get().getGroupByAppAndIp(appName, timeDataInfo.getIp());
					if (group.equals(hostGroup)) {
						double pv = timeDataInfo.getMainValue();
						clusterTotalPv += pv;
						if (pv > singleMaxPv) {
							singleMaxPv = pv;
						}
					}
				}
				clusterLoad = clusterTotalPv / 60;
				singleLoad = singleMaxPv / 60;
			} else {
				TimeDataInfo maxSingle;
				TimeDataInfo maxGroup;
				maxGroup = commonService.querySingleRecentlyKeyData(appName,qpsKey,PropConstants.E_TIMES);
				List<TimeDataInfo> list = commonService.querykeyRecentlyDataForHostBySort(appName,qpsKey,PropConstants.E_TIMES);
				if (list.size() > 0) {
					maxSingle = list.get(0);
					singleLoad = maxSingle.getMainValue() / 60;
				}
				clusterLoad = maxGroup.getMainValue() / 60;
			}
			
			DecimalFormat df = new DecimalFormat("0.00");
			double singleCap = rankingPo.getCLoadrunQps();  // 单机能力
			double clusterCap = Double.valueOf(rankingPo.getFeatureData("集群能力"));  // 集群能力
			String capacity = "100.00";  // 水位
			if (clusterCap != 0) {
				capacity = df.format(clusterLoad / clusterCap * 100);
			}
			
			executeComand(appName, time, capacity, df.format(singleCap), df.format(singleLoad), df.format(clusterCap), df.format(clusterLoad));
		}
		
		
		logger.info("syncCoreAppCapacity time:"+(System.currentTimeMillis()-t));
		
	}
	
	
	public void syncNomalAppCapacity() {
		
		long t = System.currentTimeMillis();
		
		if (!passCheck()) {
			logger.info("此机器未加入容量的系统变量");
			return;
		}
		List<CapacityRankingPo> apps = cspCapacityDao.findCapacityLatestRankingPos("容量排名");
		Set<Integer> coreApps = getCoreApps();
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_YEAR, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		for (CapacityRankingPo rankingPo : apps) {
			String time = sf.format(calendar.getTime());  // 时间
			int appId = rankingPo.getAppId();
			String appName = rankingPo.getAppName();  // 应用名
			
			// 更新下昨天核心应用最大的那一条数据
			if (coreApps.contains(appId)) {
				logger.info("update core app");
				time = rankingPo.getFeatureData("采集时间");
				SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				try {
					Calendar calendar2 = Calendar.getInstance();
					Date maxTime = sf1.parse(time);
					calendar2.setTime(maxTime);
					int minute = calendar2.get(Calendar.MINUTE);
					if (minute % 5 != 0) {
						minute = (minute / 5) * 5;
					}
					calendar2.set(Calendar.MINUTE, minute);
					time = sf.format(calendar2.getTime());
					
					// 取分组之一代表整个应用
					if (appId == 376) {
						appName = "itemcenter";
					}
					if (appId == 383) {
						appName = "tradeplatform";
					}
					if (appId == 432) {
						appName = "ump";
					}
					
				} catch (ParseException e) {
					logger.info("time format error", e);
					continue;
				}
			}
			
			DecimalFormat df = new DecimalFormat("0.00");
			double clusterLoad = Double.valueOf(rankingPo.getFeatureData("集群负荷"));  // 集群负荷
			double singleLoad =  rankingPo.getCQps();  // 单机负荷
			double singleCap = rankingPo.getCLoadrunQps();  // 单机能力
			double clusterCap = Double.valueOf(rankingPo.getFeatureData("集群能力"));  // 集群能力
			String capacity = df.format(rankingPo.getCData());  // 水位
			
			executeComand(appName, time, capacity, df.format(singleCap), df.format(singleLoad), df.format(clusterCap), df.format(clusterLoad));
		}
		
		logger.info("syncNomalAppCapacity time:"+(System.currentTimeMillis()-t));
		
	}
	
	private Set<Integer> getCoreApps() {
		Set<Integer> coreApps = new HashSet<Integer>();
		coreApps.add(1);
		coreApps.add(2);
		coreApps.add(3);
		coreApps.add(4);
		coreApps.add(10); // designcenter
		coreApps.add(11);  // mytaobao
		coreApps.add(12); // mercury
		coreApps.add(16);
		coreApps.add(21);
		coreApps.add(25);
		coreApps.add(56); // malllist
		coreApps.add(59); // misccenter
		coreApps.add(61); // lottery
		coreApps.add(80); // logisticscenter
		coreApps.add(81); // promotioncenter
		coreApps.add(250); // picturecenter
		coreApps.add(286); // promotion
		coreApps.add(323);
		coreApps.add(330); // tf_buy
		coreApps.add(341); // cart 
		coreApps.add(369); // malldetail
		coreApps.add(372); // jiaju
		coreApps.add(376);  // ic-l0-c
		coreApps.add(379); // tmallbuy
		coreApps.add(381); // delivery
		coreApps.add(383); //tp-g1
		coreApps.add(390); // pointcenter
		coreApps.add(396); // lotteryservice
		coreApps.add(432); //ump-detail
		coreApps.add(436); // jiajucenter
		coreApps.add(460); // tmallpromotion
		coreApps.add(473); // detailskip
		coreApps.add(585); // inventoryplatform
		coreApps.add(590); // malldetailskip
		coreApps.add(593); // cartapi
		coreApps.add(625); // tee
		coreApps.add(674); // wdetail
		coreApps.add(675); //wmac
		coreApps.add(676); // wsm
		coreApps.add(688); // tmallcart\
		coreApps.add(705); // tmallsearch
		coreApps.add(725); // juitemcenter
		
		return coreApps;
	}
	
	private boolean executeComand(String app, String time, String capacity, String singleC, String singleL, String clusterC, String clusterL) {
		List<HostPo> hosts  = CspCacheTBHostInfos.get().getHostInfoListByOpsName(app);
		if (hosts == null || hosts.size() == 0) return false;
		HostPo host = hosts.get(0);
		String nodeGroup = host.getNodeGroup();
		StringBuffer urlBuffer = new StringBuffer("http://api2.c.alibaba-inc.com/data/add_capacity_data?");
		urlBuffer.append("cluster_name=").append(nodeGroup);
		urlBuffer.append("&time=").append(time);
		urlBuffer.append("&capacity=").append(capacity);
		urlBuffer.append("&single_capability=").append(singleC);
		urlBuffer.append("&single_cap_load=").append(singleL);
		urlBuffer.append("&cluster_capability=").append(clusterC);
		urlBuffer.append("&cluster_cap_load=").append(clusterL);
		
		boolean done = false;
		try {
			logger.info(urlBuffer.toString());
			PostMethod post = new PostMethod("http://api2.c.alibaba-inc.com/data/add_capacity_data");
			post.addParameter("cluster_name", nodeGroup);
			post.addParameter("time", time);
			post.addParameter("capacity", capacity);
			post.addParameter("single_capability", singleC);
			post.addParameter("single_cap_load", singleL);
			post.addParameter("cluster_capability", clusterC);
			post.addParameter("cluster_cap_load", clusterL);
			int statusCode =httpClient.executeMethod(post);
			if(statusCode == HttpStatus.SC_OK){	
				 InputStream in =  post.getResponseBodyAsStream();
				 InputStreamReader inputStreamReader = new InputStreamReader(in);
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
				
				StringBuffer success = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					success.append(line);
					logger.info(line);
				}
				if (success.indexOf("success") > -1) {
					done = true;
				} 
			}
		} catch (MalformedURLException e) {
			logger.error(urlBuffer.toString()+"URL构造error", e);
		} catch (IOException e1) {
			logger.error(urlBuffer.toString()+"打开URLerror", e1);
		}
		
		return done;
	}
	
	private  boolean passCheck() {
		boolean pass = false;
		String capacity = System.getenv("CAPACITY");
		if (capacity != null && capacity.equals("true")) {
			pass = true;
		}
		
		return pass;
	}
	
	public static void main(String [] args) throws Exception {
		PostMethod post = new PostMethod("http://api2.c.alibaba-inc.com/data/add_capacity_data");
		post.addParameter("cluster_name", "tmallpromotionhost");
		post.addParameter("time", "2012-10-22 11:15:00");
		post.addParameter("capacity", "5.69");
		post.addParameter("single_capability", "446.94");
		post.addParameter("single_cap_load", "48.97");
		post.addParameter("cluster_capability", "2682.00");
		post.addParameter("cluster_cap_load", "152.52");
		
		HttpClient httpClient = new HttpClient(new MultiThreadedHttpConnectionManager());
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
		int statusCode =httpClient.executeMethod(post);
		
		if(statusCode == HttpStatus.SC_OK){	
			System.out.println("ok");
		} else {
			System.out.println("fail");
		}
	}
	
}
