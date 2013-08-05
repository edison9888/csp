package com.taobao.monitor.web.poc;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.web.core.dao.impl.LoadRunHostConfigDao;
import com.taobao.monitor.web.core.dao.impl.MonitorJprofDao;
import com.taobao.monitor.web.core.dao.impl.StressTestResultDao;
import com.taobao.monitor.web.core.po.JprofClassMethod;
import com.taobao.monitor.web.core.po.LoadRunHost;
import com.taobao.monitor.web.vo.HostConfigPo;

/**
 * POC的AO
 * @author shutong.dy E-mail:shutong.dy@taobao.com
 * @version 1.0
 * @since 2011-10-17 上午10:45:38
 */
public class PocAO {
	private static final Logger logger = Logger.getLogger(PocAO.class);

	public static final PocAO instance = new PocAO();

	private PocAO() {
	}

	private StressTestResultDao stressDao = new StressTestResultDao();
	private MonitorJprofDao jprofDao = new MonitorJprofDao();
	private LoadRunHostConfigDao loadRunDao = new LoadRunHostConfigDao();
	private AppInfoDao appInfoDao = new AppInfoDao();
	/**
	 * 缓存应用列表一天
	 */
	private static Map<Integer, String> appMap = new ConcurrentHashMap<Integer, String>();
	
	/**
	 * 取得应用列表
	 * @return
	 */
	public List<LoadRunHost> getAppList() {
		long current = System.currentTimeMillis();
		// 24小时初始化一次
		if (appMap.get(Integer.MIN_VALUE) == null || current - Long.valueOf(appMap.get(Integer.MIN_VALUE)) > 86400000L) {
			appMap.put(Integer.MIN_VALUE, String.valueOf(current));
			List<AppInfoPo> list = appInfoDao.findAllAppInfo();
			for (AppInfoPo po : list) {
				appMap.put(po.getAppId(), po.getAppName());
			}
		}

		List<LoadRunHost> listLoad = loadRunDao.findAllLoadRunHost();
		for (LoadRunHost h : listLoad) {
			h.setAppName(appMap.get(h.getAppId()));
		}
		return listLoad;
	}

	/**
	 * 取得压测机器配置
	 * @param appId
	 * @return
	 */
	public HostConfigPo getLoadrunAppConfig(int appId) {
		if (logger.isDebugEnabled())
			logger.debug("========取得压测机器配置信息");
		HostConfigPo hostConfigPo = loadRunDao.getHostConfig(appId);
		if (hostConfigPo.getAppId() == 0) {
			hostConfigPo = initHostConfigPo(appId);
			loadRunDao.insertHostConfig(hostConfigPo);
		} else {
			Date updateTime = hostConfigPo.getUpdateTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			// 如果记录不是当天的
			if (!df.format(updateTime).equals(df.format(new Date()))){
				hostConfigPo = initHostConfigPo(appId);
				loadRunDao.updateHostConfig(hostConfigPo);
			}
		}
		return hostConfigPo;
	}

	/**
	 * 初始化读取压测机器配置
	 * @param appId
	 * @return
	 */
	private HostConfigPo initHostConfigPo(int appId) {
		LoadRunHost loadRunHost = loadRunDao.findLoadRunHostByAppId(appId);
		String ip = loadRunHost.getHostIp();
		String username = loadRunHost.getUserName();
		String password = loadRunHost.getPassword();
		HostConfigPo configPo = new HostConfigPo();
		SshFetchSystemInfo.instance.fetch(configPo, ip, username, password);
		configPo.setAppId(appId);
		configPo.setUpdateTime(new Date());
		return configPo;
	}

	/**
	 * 获取QPS与系统资源关系
	 * @param appId
	 * @return
	 */
	public String getSystemResourceRelation(int appId, String date, String date1) {
		List<LoadrunResult> list = stressDao.getSystemResourceRelation(appId, date, date1);
		StringBuffer series = new StringBuffer(1024);
		StringBuffer graph1 = new StringBuffer(1024);
		StringBuffer graph2 = new StringBuffer(1024);
		StringBuffer graph3 = new StringBuffer(1024);
		StringBuffer graph4 = new StringBuffer(1024);
		// 并发用户数
		series.append("<series>");
		graph1.append("<graph gid='1' title='Rest'>");// Rest
		graph2.append("<graph gid='2' title='CPU'>");// CPU
		graph3.append("<graph gid='3' title='Qps'>");// Qps
		graph4.append("<graph gid='4' title='Load'>");// Load
		String controlFeature = null;
		int coordinate = 0;
		// 去掉结束时的那次采集
		int loadrunOrder = 0;
		series.append("<value xid='0'>0</value>");
		graph1.append("<value xid='0'>0</value>");
		graph2.append("<value xid='0'>0</value>");
		graph3.append("<value xid='0'>0</value>");
		graph4.append("<value xid='0'>0</value>");
		int modjkLBfactorcount = 0;
		for (int i = 0; i < list.size(); i++) {
			LoadrunResult result = list.get(i);
			String currentUserCount = result.getControlFeature();
			if (currentUserCount == null) {
				currentUserCount = "0";
			}
			if (!currentUserCount.equals(controlFeature)) {
				controlFeature = currentUserCount;
				coordinate++;
				modjkLBfactorcount = calculateCurrentUserCount(result, modjkLBfactorcount);
				series.append("<value xid='").append(coordinate).append("'>").append(modjkLBfactorcount)
						.append("</value>");
				graph2.append("<value xid='").append(coordinate).append("'>").append(result.getValue())
						.append("</value>");
			} else if (loadrunOrder != result.getLoadrunOrder()) {
				// 去掉结束时的那次采集
				continue;
			} else if (result.getKey() == ResultKey.Rest) {
				graph1.append("<value xid='").append(coordinate).append("'>").append(result.getValue())
						.append("</value>");
			} else if (result.getKey() == ResultKey.Qps) {
				graph3.append("<value xid='").append(coordinate).append("'>").append(result.getValue())
						.append("</value>");
			} else if (result.getKey() == ResultKey.Load) {
				graph4.append("<value xid='").append(coordinate).append("'>").append(result.getValue())
						.append("</value>");
			}
			loadrunOrder = result.getLoadrunOrder();
		}

		graph1.append("</graph>");
		graph2.append("</graph>");
		graph3.append("</graph>");
		graph4.append("</graph>");
		series.append("</series>");
		return new StringBuffer("<chart>").append(series).append("<graphs>").append(graph2).append(graph4)
				.append(graph1).append(graph3).append("</graphs>").append("</chart>").toString();
	}
	private int calculateCurrentUserCount(LoadrunResult result, int modjkLBfactorcount) {
		int currentUserCount = 0;
		String stringControlFeature = result.getControlFeature();
		if (result.getLoadrunType() == AutoLoadType.httpLoad) {
			currentUserCount = Integer.valueOf(stringControlFeature);
		} else if (result.getLoadrunType() == AutoLoadType.apache) {
			currentUserCount = 0;
			if (modjkLBfactorcount == 0) {
				modjkLBfactorcount = Integer.valueOf(stringControlFeature.split(":")[1].split("_")[0]) * 2;
			}
			currentUserCount = Integer.valueOf(stringControlFeature.split(":")[1].split("_")[1]);
		}

		return currentUserCount + modjkLBfactorcount;
	}
	/**
	 * 获取压测GC信息
	 * @param appId
	 * @return
	 */
	public String getMinorGcInfo(int appId, String date, String date1) {
		List<LoadrunResult> list = stressDao.getMinorGcInfo(appId, date, date1);
		StringBuffer series = new StringBuffer(1024);
		StringBuffer graph1 = new StringBuffer(1024);
		StringBuffer graph2 = new StringBuffer(1024);
		// 并发用户数
		series.append("<series>");
		graph1.append("<graph gid='1' title='GC Memory'>");// GC Memory
		graph2.append("<graph gid='2' title='GC Time'>");// GC Time
		String controlFeature = null;
		int coordinate = 0;
		// 去掉结束时的那次采集
		int loadrunOrder = 0;
		series.append("<value xid='0'>0</value>");
		graph1.append("<value xid='0'>0</value>");
		graph2.append("<value xid='0'>0</value>");
		int modjkLBfactorcount = 0;
		for (int i = 0; i < list.size(); i++) {
			LoadrunResult result = list.get(i);
			String currentUserCount = result.getControlFeature();

			if (!currentUserCount.equals(controlFeature)) {
				controlFeature = currentUserCount;
				coordinate++;
				modjkLBfactorcount = calculateCurrentUserCount(result, modjkLBfactorcount);
				series.append("<value xid='").append(coordinate).append("'>").append(modjkLBfactorcount)
						.append("</value>");
				graph1.append("<value xid='").append(coordinate).append("'>").append(result.getValue() / 1024)
						.append("</value>");
			} else if (loadrunOrder != result.getLoadrunOrder()) {
				// 去掉结束时的那次采集
				continue;
			} else if (result.getKey() == ResultKey.GC_Min_Time) {
				graph2.append("<value xid='").append(coordinate).append("'>").append(result.getValue())
						.append("</value>");
			}
			loadrunOrder = result.getLoadrunOrder();
		}

		graph1.append("</graph>");
		graph2.append("</graph>");
		series.append("</series>");
		return new StringBuffer("<chart>").append(series).append("<graphs>").append(graph1).append(graph2)
				.append("</graphs>").append("</chart>").toString();
	}

	/**
	 * 获取页面占用内存信息
	 * @param appId
	 * @return
	 */
	public String getPageMemoryInfo(int appId, String date, String date1) {
		List<LoadrunResult> list = stressDao.getPageMemoryInfo(appId, date, date1);
		StringBuffer series = new StringBuffer(1024);
		StringBuffer graph1 = new StringBuffer(1024);
		StringBuffer graph2 = new StringBuffer(1024);

		// 并发用户数
		series.append("<series>");
		graph1.append("<graph gid='1' title='Page Size'>");
		graph2.append("<graph gid='2' title='Memory/Request'>");
		String controlFeature = null;
		int coordinate = 0;
		// 去掉结束时的那次采集
		int loadrunOrder = 0;
		series.append("<value xid='0'>0</value>");
		graph1.append("<value xid='0'>0</value>");
		graph2.append("<value xid='0'>0</value>");
		int modjkLBfactorcount = 0;
		double memory = 0;
		for (int i = 0; i < list.size(); i++) {
			LoadrunResult result = list.get(i);
			String currentUserCount = result.getControlFeature();

			if (!currentUserCount.equals(controlFeature)) {
				controlFeature = currentUserCount;
				coordinate++;
				modjkLBfactorcount = calculateCurrentUserCount(result, modjkLBfactorcount);
				series.append("<value xid='").append(coordinate).append("'>").append(modjkLBfactorcount)
						.append("</value>");
				memory = result.getValue();
			} else if (loadrunOrder != result.getLoadrunOrder()) {
				// 去掉结束时的那次采集
				continue;
			} else if (result.getKey() == ResultKey.PageSize) {
				graph1.append("<value xid='").append(coordinate).append("'>").append(result.getValue())
						.append("</value>");
			} else if (result.getKey() == ResultKey.Qps) {
				graph2.append("<value xid='").append(coordinate).append("'>").append(memory / result.getValue())
						.append("</value>");
			}
			loadrunOrder = result.getLoadrunOrder();
		}

		graph1.append("</graph>");
		graph2.append("</graph>");
		series.append("</series>");
		return new StringBuffer("<chart>").append(series).append("<graphs>").append(graph1).append(graph2)
				.append("</graphs>").append("</chart>").toString();
	}

	public List<JprofClassMethod> findJprofClassMethod(int appId, String date) {
		return jprofDao.findJprofClassMethod(appMap.get(appId), date);
	}

	public List<JprofClassMethod> findJprofTopDaoMethod(int appId, String date) {
		return jprofDao.findJprofTopDaoMethod(appMap.get(appId), date);
	}

	public List<JprofClassMethod> findJprofTopExternalMethod(int appId, String date) {
		return jprofDao.findJprofTopExternalMethod(appMap.get(appId), date);
	}
	public String dayAdd1(String date){
        Calendar cd = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
        	cd.setTime(df.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
        cd.add(Calendar.DAY_OF_YEAR, +1);
        return df.format(cd.getTime());
	}
}
