package com.taobao.monitor.other.review;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.DataBaseInfoAo;
import com.taobao.monitor.common.ao.center.HostAo;
import com.taobao.monitor.common.ao.center.ServerInfoAo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.AppMysqlInfo;
import com.taobao.monitor.common.po.DataBaseInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.po.ServerInfoPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;
import com.taobao.monitor.common.util.RemoteCommonUtil;
import com.taobao.monitor.common.util.RemoteCommonUtil.CallBack;
import com.taobao.monitor.web.ao.MonitorDayAo;
import com.taobao.monitor.web.jprof.AutoJprofManage;

public class DataReview {

	private List<ServerInfoPo> serverList;// 已有服务器列表

	private List<DataBaseInfoPo> dbInfoList;// 数据库列表

	private List<AppHostPo> ahpList = new ArrayList<AppHostPo>();

	private static DataReview dr = new DataReview();

	private HostAo hostAo = HostAo.get();

	private DataReview() {
	}

	public static DataReview getInstance() {
		return dr;
	}

	/**
	 * 获取所需服务器信息
	 * 
	 * @return
	 */
	public List<ServerCurrentInfo> getServerInfo() {
		List<ServerCurrentInfo> currentList = new ArrayList<ServerCurrentInfo>();// 服务器信息对象列表

		serverList = ServerInfoAo.get().findAllServerInfo();
		dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
		for (ServerInfoPo po : serverList) {
			final StringBuffer sb = new StringBuffer();// 使用命令df从服务器端传回的所有字符
			String ip = po.getServerIp();
			String name = po.getServerName();
			int serverId = po.getServerId();
			try {
				RemoteCommonUtil.excute(ip, "xiaodu", "Hello_123", "df -h", new CallBack() {
					public void doLine(String line) {
						sb.append(line + "<br/>");
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 获取磁盘空间
			String usedSpace = getUsedSpace(sb.toString());
			String allSpace = getAllSpace(sb.toString());

			// 获取日报监控app数量
			int timeAppcount = AppInfoAo.get().findAllAppByServerIdAndAppType(serverId, "time").size();
			// 获取时报监控app数量
			int dayAppCount = AppInfoAo.get().findAllAppByServerIdAndAppType(serverId, "day").size();
			// 获取mysql数据库数量
			int mysqlCount = getMysqlCount(ip);

			ServerCurrentInfo newServer = new ServerCurrentInfo();
			newServer.setServerIp(ip);
			newServer.setServerName(name);
			newServer.setStorage(usedSpace);
			newServer.setAllSpace(allSpace);
			newServer.setDayAppCount(dayAppCount);
			newServer.setTimeAppcount(timeAppcount);
			newServer.setMysqlCount(mysqlCount);
			currentList.add(newServer);
		}
		return currentList;
	}

	/**
	 * 从传入字段中提取出空间占用
	 * 
	 * @param strAllInfo
	 * @return
	 */
	private String getUsedSpace(String strAllInfo) {
		String str = "/home";
		int index = strAllInfo.indexOf(str);
		if (index == -1) {
			return " - ";
		} else {
			return strAllInfo.substring(index - 5, index - 1);
		}
	}

	/**
	 * 从传入字段中提取出磁盘空间
	 * 
	 * @param strAllInfo
	 * @return
	 */
	private String getAllSpace(String strAllInfo) {
		String str = "/home";
		int index = strAllInfo.indexOf(str);
		if (index == -1) {
			return " - ";
		} else {
			return strAllInfo.substring(index - 22, index - 18);
		}
	}

	/**
	 * 
	 */
	private int getMysqlCount(String serverIp) {
		int i = 0;
		for (DataBaseInfoPo po : dbInfoList) {
			String url = po.getDbUrl();
			int endIndex = url.indexOf(":3306");
			String dbIp = url.substring(13, endIndex);
			if (serverIp.equalsIgnoreCase(dbIp)) {
				i++;
			}
		}
		return i;
	}

	/**
	 * 获取昨天的时间
	 */
	public String getYesterday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(cal.getTime());
	}

	/**
	 * 另外一种获取昨天的时间
	 */
	public String getOtherYesterday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(cal.getTime());
	}

	/**
	 * 传入数据库id返回已有序的AppMysqlInfo列表
	 */
	public List<AppMysqlInfo> getAppMysqlList(int id) {
		int data = Integer.parseInt(getYesterday());
		List<AppMysqlInfo> appMysqlList = new ArrayList<AppMysqlInfo>();
		Map<Integer, AppMysqlInfo> map = DataBaseInfoAo.get().findAppDatabaseInfo(id, data);
		Iterator<Map.Entry<Integer, AppMysqlInfo>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry<Integer, AppMysqlInfo> entry = iter.next();
			// id=(Integer)entry.getKey();
			AppMysqlInfo appMysqlInfo = entry.getValue();

			int appId = appMysqlInfo.getAppId();

			AppInfoPo po = AppInfoAo.get().findAppInfoById(appId);

			int times = MonitorDayAo.get().getDataById(po.getAppDayId(), getOtherYesterday());

			appMysqlInfo.setDayDataNum(times);

			appMysqlList.add(appMysqlInfo);
		}
		return sortList(appMysqlList);
	}

	private List<AppMysqlInfo> sortList(List<AppMysqlInfo> list) {
		Collections.sort(list, new Comparator<Object>() {
			public int compare(final Object o1, final Object o2) {
				final AppMysqlInfo a1 = (AppMysqlInfo) o1;
				final AppMysqlInfo a2 = (AppMysqlInfo) o2;
				int k = -1;
				if (a1.getDataNum() > a2.getDataNum()) {
					k = 1;
				} else if (a1.getDataNum() < a2.getDataNum()) {
					k = -1;
				} else {
					k = 0;
				}
				return k;
			}
		});
		return list;
	}

	/**
	 * 获取所有的app
	 */
	private List<AppInfoPo> getAppList() {
		return AppInfoAo.get().findAllEffectiveAppInfo();
	}

	/**
	 * 返回获取到的结果list
	 */
	public List<AppHostPo> getResult() {
		return ahpList;
	}

	/**
	 * 获取记录每个app验证的信息，给成员变量ahpList赋值
	 */
	public void checkAuth() {
		
		
//		String classPath = DataReview.class.getResource("/").getPath();
//		String reviewPath = classPath.substring(0, classPath.lastIndexOf("WEB-INF"))+"review/";
		ahpList.clear();
		List<AppInfoPo> appList = getAppList();
		File file = new File(AutoJprofManage.get().getRealPath()+"/review/FailHostList.txt");
		
//		File file = new File("./review/FailHostList.txt");
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("未通过验证主机列表：\n");
			for (int i = 0; i < appList.size(); i++) {
				try {
					AppInfoPo aip = appList.get(i);
					if (aip.getDayDeploy() == 0 && aip.getTimeDeploy() == 0) {
						continue;
					}
					String opsName = aip.getOpsName();
					List<HostPo> hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(opsName);
					String appName = aip.getAppName();
					int appId = aip.getAppId();// 获取应用id
					int monitorAcount = hostAo.findTimeAppHost(appId).size();// 通过应用id获取监控的主机数
					List<HostPo> monitorHostList = hostAo.findTimeAppHost(appId);
					int online = 0, offline = 0;// 监控中的机器账户验证
					for (HostPo h : monitorHostList) {
						String hostIp = h.getHostIp();
						String hostUser = h.getUserName();// 主机的用户名
						String hostPSW = h.getUserPassword();// 主机的密码
						/* 测试主机是否在线 */
						if (RemoteCommonUtil.authenticate(hostIp, hostUser, hostPSW)) {
							online++;
						} else {
							offline++;
						}
					}
					int success = 0, fail = 0;// 验证成功数和失败数
					for (HostPo host : hostList) {
						String hostIp = host.getHostIp();
						String hostUser = aip.getLoginName();// 主机的用户名
						String hostPSW = aip.getLoginPassword();// 主机的密码
						/* 测试应用上的每个主机是否能够验证成功 */
						if (RemoteCommonUtil.authenticate(hostIp, hostUser, hostPSW)) {
							success++;
						} else {
							fail++;
							writer.newLine();
							writer.write(host.getHostName());
						}
					}
					int hostAcount = hostList.size();// 主机总数

					AppHostPo ahp = new AppHostPo();
					ahp.setAppName(appName);
					ahp.setHostAcount(hostAcount);
					ahp.setSuccess(success);
					ahp.setFail(fail);
					ahp.setMonitorAcount(monitorAcount);
					ahp.setOnline(online);
					ahp.setOffline(offline);

					ahpList.add(ahp);
				} catch (Exception e) {
				}
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
				}
			}
		}

	}

	public static void main(String[] a) {
		DataReview.getInstance().checkAuth();
	}

}
