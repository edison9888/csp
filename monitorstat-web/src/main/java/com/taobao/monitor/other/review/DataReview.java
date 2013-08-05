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

	private List<ServerInfoPo> serverList;// ���з������б�

	private List<DataBaseInfoPo> dbInfoList;// ���ݿ��б�

	private List<AppHostPo> ahpList = new ArrayList<AppHostPo>();

	private static DataReview dr = new DataReview();

	private HostAo hostAo = HostAo.get();

	private DataReview() {
	}

	public static DataReview getInstance() {
		return dr;
	}

	/**
	 * ��ȡ�����������Ϣ
	 * 
	 * @return
	 */
	public List<ServerCurrentInfo> getServerInfo() {
		List<ServerCurrentInfo> currentList = new ArrayList<ServerCurrentInfo>();// ��������Ϣ�����б�

		serverList = ServerInfoAo.get().findAllServerInfo();
		dbInfoList = DataBaseInfoAo.get().findAllDataBaseInfo();
		for (ServerInfoPo po : serverList) {
			final StringBuffer sb = new StringBuffer();// ʹ������df�ӷ������˴��ص������ַ�
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

			// ��ȡ���̿ռ�
			String usedSpace = getUsedSpace(sb.toString());
			String allSpace = getAllSpace(sb.toString());

			// ��ȡ�ձ����app����
			int timeAppcount = AppInfoAo.get().findAllAppByServerIdAndAppType(serverId, "time").size();
			// ��ȡʱ�����app����
			int dayAppCount = AppInfoAo.get().findAllAppByServerIdAndAppType(serverId, "day").size();
			// ��ȡmysql���ݿ�����
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
	 * �Ӵ����ֶ�����ȡ���ռ�ռ��
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
	 * �Ӵ����ֶ�����ȡ�����̿ռ�
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
	 * ��ȡ�����ʱ��
	 */
	public String getYesterday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(cal.getTime());
	}

	/**
	 * ����һ�ֻ�ȡ�����ʱ��
	 */
	public String getOtherYesterday() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return sdf.format(cal.getTime());
	}

	/**
	 * �������ݿ�id�����������AppMysqlInfo�б�
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
	 * ��ȡ���е�app
	 */
	private List<AppInfoPo> getAppList() {
		return AppInfoAo.get().findAllEffectiveAppInfo();
	}

	/**
	 * ���ػ�ȡ���Ľ��list
	 */
	public List<AppHostPo> getResult() {
		return ahpList;
	}

	/**
	 * ��ȡ��¼ÿ��app��֤����Ϣ������Ա����ahpList��ֵ
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
			writer.write("δͨ����֤�����б�\n");
			for (int i = 0; i < appList.size(); i++) {
				try {
					AppInfoPo aip = appList.get(i);
					if (aip.getDayDeploy() == 0 && aip.getTimeDeploy() == 0) {
						continue;
					}
					String opsName = aip.getOpsName();
					List<HostPo> hostList = CspCacheTBHostInfos.get().getHostInfoListByOpsName(opsName);
					String appName = aip.getAppName();
					int appId = aip.getAppId();// ��ȡӦ��id
					int monitorAcount = hostAo.findTimeAppHost(appId).size();// ͨ��Ӧ��id��ȡ��ص�������
					List<HostPo> monitorHostList = hostAo.findTimeAppHost(appId);
					int online = 0, offline = 0;// ����еĻ����˻���֤
					for (HostPo h : monitorHostList) {
						String hostIp = h.getHostIp();
						String hostUser = h.getUserName();// �������û���
						String hostPSW = h.getUserPassword();// ����������
						/* ���������Ƿ����� */
						if (RemoteCommonUtil.authenticate(hostIp, hostUser, hostPSW)) {
							online++;
						} else {
							offline++;
						}
					}
					int success = 0, fail = 0;// ��֤�ɹ�����ʧ����
					for (HostPo host : hostList) {
						String hostIp = host.getHostIp();
						String hostUser = aip.getLoginName();// �������û���
						String hostPSW = aip.getLoginPassword();// ����������
						/* ����Ӧ���ϵ�ÿ�������Ƿ��ܹ���֤�ɹ� */
						if (RemoteCommonUtil.authenticate(hostIp, hostUser, hostPSW)) {
							success++;
						} else {
							fail++;
							writer.newLine();
							writer.write(host.getHostName());
						}
					}
					int hostAcount = hostList.size();// ��������

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
