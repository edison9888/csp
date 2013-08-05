package com.taobao.csp.capacity.bo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.csp.capacity.constant.Constants;
import com.taobao.csp.capacity.dao.CapacityDao;
import com.taobao.csp.capacity.dao.CapacityPvDao;
import com.taobao.csp.capacity.dao.CapacityRankingDao;
import com.taobao.csp.capacity.dao.CspDayDao;
import com.taobao.csp.capacity.dao.CspLoadRunDao;
import com.taobao.csp.capacity.po.CapacityAppPo;
import com.taobao.csp.capacity.po.CapacityRankingPo;
import com.taobao.csp.capacity.po.DomainPvPo;
import com.taobao.csp.capacity.po.PvcountPo;
import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.monitor.common.db.impl.capacity.CapacityCapDao;
import com.taobao.monitor.common.db.impl.capacity.CapacityLoadDao;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityCapPo;
import com.taobao.monitor.common.db.impl.capacity.po.CapacityLoadPo;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.po.KeyValuePo;
import com.taobao.monitor.common.util.Arith;
import com.taobao.monitor.common.util.CommonUtil;
import com.taobao.monitor.common.util.CspSyncOpsHostInfos;
import com.taobao.monitor.common.util.GroupManager;


public class CapacityRankingBo {

	private static final Logger logger = Logger.getLogger(CapacityRankingBo.class);

	private CapacityRankingDao capacityRankingDao;

	private CapacityDao capacityDao;

	private CapacityPvDao capacityPvDao;

	private AppInfoDao appInfoDao;

	private CspLoadRunDao cspLoadRunDao;

	private CspDayDao cspDayDao;
	
	private CapacityCapDao capacityCapDao;
	
	private CapacityLoadDao capacityLoadDao;

	private Constants constants;

	public CapacityPvDao getCapacityPvDao() {
		return capacityPvDao;
	}

	public void setCapacityPvDao(CapacityPvDao capacityPvDao) {
		this.capacityPvDao = capacityPvDao;
	}

	public CspDayDao getCspDayDao() {
		return cspDayDao;
	}

	public void setCspDayDao(CspDayDao cspDayDao) {
		this.cspDayDao = cspDayDao;
	}

	public Constants getConstants() {
		return constants;
	}

	public void setConstants(Constants constants) {
		this.constants = constants;
	}

	public AppInfoDao getAppInfoDao() {
		return appInfoDao;
	}

	public void setAppInfoDao(AppInfoDao appInfoDao) {
		this.appInfoDao = appInfoDao;
	}

	public CspLoadRunDao getCspLoadRunDao() {
		return cspLoadRunDao;
	}

	public void setCspLoadRunDao(CspLoadRunDao cspLoadRunDao) {
		this.cspLoadRunDao = cspLoadRunDao;
	}

	public CapacityRankingDao getCapacityRankingDao() {
		return capacityRankingDao;
	}

	public void setCapacityRankingDao(CapacityRankingDao capacityRankingDao) {
		this.capacityRankingDao = capacityRankingDao;
	}

	public CapacityDao getCapacityDao() {
		return capacityDao;
	}

	public void setCapacityDao(CapacityDao capacityDao) {
		this.capacityDao = capacityDao;
	}

	public CapacityCapDao getCapacityCapDao() {
		return capacityCapDao;
	}

	public void setCapacityCapDao(CapacityCapDao capacityCapDao) {
		this.capacityCapDao = capacityCapDao;
	}

	public CapacityLoadDao getCapacityLoadDao() {
		return capacityLoadDao;
	}

	public void setCapacityLoadDao(CapacityLoadDao capacityLoadDao) {
		this.capacityLoadDao = capacityLoadDao;
	}

	/**
	 * 
	 * @param rankingName
	 * @return
	 */
	public List<CapacityRankingPo> findCapacityRankingPoList(String rankingName, Date date) {
		return capacityRankingDao.findCapacityRankingPo(rankingName, date);
	}

	/***
	 * ͬ��pv���ݣ������Ҫ�ع���
	 */
	public void executeSynch() {
		synchDataIntoCapacity();
	}

	/***
	 * ������������
	 * if you feel dizzy about this, please forgive me, I am dizzy too when I write these code, haha
	 * the process of calculation is really a little complicated to some degree
	 * another reason is the historical design and code structure which I should obey
	 */
	public synchronized void executeRanking() {
		computeCapacityRanking();
		
		try {
			computeExternalCapacity();
		} catch (Exception e) {
			logger.error("compute external capacity error!", e);
		}
	}
	
	public void synchDataIntoCapacity() {
		synchDataIntoCapacity(null);
	}

	public void synchDataIntoCapacity(Date time) {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar b = Calendar.getInstance();
		if (time != null) {
			b.setTime(time);
		}else{
			b.add(Calendar.DAY_OF_MONTH, -3);
		}

		String bussinessDate = sdf.format(b.getTime());

		String date = sdf.format(b.getTime());

		String pvType = "CSP";

		List<CapacityAppPo> appList = capacityDao.findCapacityAppList();// ��ȡ�����е�capacityapp��list

		for (CapacityAppPo cap : appList) {
			String feature = cap.getDataFeature();
			int id = cap.getAppId();
			AppInfoPo appInfo = appInfoDao.findAppInfoById(id);

			Double allPv = 0d;

			if (cap.getAppId() == 3) {

				KeyValuePo po = cspDayDao.findKeyValueFromCountByDay(982, appInfo.getAppDayId(), b.getTime());
				if (po != null)
					allPv = Double.parseDouble(po.getValueStr());
				pvType = "CSP";

			} else {
				if ("center".equals(appInfo.getAppType())) {
					// HSF ����pv keyID = 27733
					KeyValuePo po = cspDayDao.findKeyValueFromCountByDay(27733, appInfo.getAppDayId(), b.getTime());
					if (po != null)
						allPv = Double.parseDouble(po.getValueStr());
					pvType = "CSP";
				} else {
					if (feature != null) {
						String[] domains = feature.split(",");
						double all = 0;
						for (String domain : domains) {
							DomainPvPo dpp = capacityPvDao.findAllByDomainAndData(domain, bussinessDate);
							all = Arith.add(dpp.getAllPv(), all);
						}
						allPv = all;
						pvType = "BI";

					}

				}
			}

			PvcountPo pp = new PvcountPo();
			pp.setAppId(id);
			pp.setCollectTime(java.sql.Date.valueOf(date));
			pp.setPvCount(allPv);
			pp.setPvType(pvType);
			pp.setYear(Integer.valueOf(date.substring(0, 4)));
			capacityPvDao.insertToCapacity(pp);
		}
	}

	/**
	 * ������������
	 * 
	 */
	private  void computeCapacityRanking() {
		Date date = constants.getRankingTime().getTime();
		
		List<CapacityAppPo> listApp = capacityDao.findCapacityAppList();  // find capacity app
		capacityRankingDao.deleteCapacityRanking("��������", date);  // delete old date

		Map<Integer, CapacityRankingPo> mapTmp = new HashMap<Integer, CapacityRankingPo>();

		for (CapacityAppPo po : listApp) {
			AppInfoPo appInfo = appInfoDao.findAppInfoById(po.getAppId());

			// �������
			String groupNames = po.getGroupNames();
			String itemName = po.getItemName();
			String dataName = po.getDataName();
			
			if (StringUtils.isNotEmpty(itemName) && StringUtils.isNotEmpty(dataName)) {
				mapTmp.put(appInfo.getAppId(), computeCapacityRanking(po, appInfo, date));
			} else if (groupNames != null) {
				mapTmp.putAll(computeGroupCapacityRanking(po, appInfo, groupNames, date));
			} else {
				mapTmp.put(appInfo.getAppId(), computeNormalCapacityRanking(po, appInfo, date));
			}

		}

		List<CapacityRankingPo> listTmp = new ArrayList<CapacityRankingPo>();
		listTmp.addAll(mapTmp.values());
		Collections.sort(listTmp);

		for (int i = 0; i < listTmp.size(); i++) {
			CapacityRankingPo po = listTmp.get(i);
			if (po.getCRanking() != i + 1) {
				po.setCRanking(i + 1);
			}
			po.setRankingDate(date);
			capacityRankingDao.addCapacityRanking(po);
		}
	}
	
	private Map<Integer, CapacityRankingPo> computeGroupCapacityRanking(CapacityAppPo po, AppInfoPo appInfo, String groupNames, Date date) {
		Map<Integer, CapacityRankingPo>  mapTmp = new HashMap<Integer, CapacityRankingPo>();
		
		Map<String, Integer> groupQpsKeyMap = constants.getMachineGroupHsfQpsKey(); // ��ȡ����ƽ��qps��key
		Map<String, Integer> largestHsfGroupQpsMap = constants.getLargestHsfGroupQps(); // ��ȡ����߷��qps��key
		Map<String, Integer> largestHsfGroupTimeMap = constants.getLargestHsfGroupTime(); // ��ȡ����߷��ʱ���key
		Map<String, Integer> largestHsfGroupHostMap = constants.getLargestHsfGroupHost(); // ��ȡ����߷��������key
		
		// ���ձ��л�ȡ�����ƽ��qps��Ϣ
		Map<Integer, Double> groupQpsValuemap = cspDayDao.findKeyValueFromCountByDay(appInfo.getAppDayId(), groupQpsKeyMap.values(), date);
		
		// ���ձ��л�ȡ����ĸ߷��qps��Ϣ
		Map<Integer, Double> largestHsfGroupQpsValue = cspDayDao.findKeyValueFromCountByDay(appInfo.getAppDayId(), largestHsfGroupQpsMap.values(), date);
		Map<Integer, String> largestHsfGroupTimeValue = cspDayDao.findDataFromCountByDay(appInfo.getAppDayId(), largestHsfGroupTimeMap.values(), date);
		Map<Integer, String> largestHsfGroupHostValue = cspDayDao.findDataFromCountByDay(appInfo.getAppDayId(), largestHsfGroupHostMap.values(), date);
		
		// ���ձ��л�ȡ����Ļ����ֲ���Ϣ
//		Map<Integer, Double> groupHostValuemap = cspDayDao.findKeyValueFromCountByDay(appInfo.getAppDayId(), groupHostSiteKeyMap.values(), date);

		String[] groups = groupNames.split(";");
		for (String group : groups) {
			String[] infos = group.split(":");
			if (infos.length == 3) {
				String groupName = infos[0];
				String name = infos[1];
				String groupId = infos[2];

				logger.info("��ʼ������������:" + appInfo.getAppName() + ",����:" + groupName);
				// ���鵥̨���ϸ߷���ƽ��QPS
				Double groupQps = groupQpsValuemap.get(groupQpsKeyMap.get("QPS_" + groupName));
				if (groupQps == null) {
					groupQps = 0d;
				}
				logger.info("ȫ��ƽ��qps:" + groupQps);
				
				// ���鵥̨�߷��qps
				Double maxGroupQps = largestHsfGroupQpsValue.get(largestHsfGroupQpsMap.get("QPS_" + groupName));
				if (maxGroupQps == null) {
					maxGroupQps = 0d;
				}
				logger.info("�߷��qps:" + maxGroupQps);
				String maxGroupTime = largestHsfGroupTimeValue.get(largestHsfGroupTimeMap.get("TIME_" + groupName));
				String maxGroupHost = largestHsfGroupHostValue.get(largestHsfGroupHostMap.get("HOST_" + groupName));

				// ����ѹ��QPS�����һ��ѹ����ǰ7���ѹ�����ֵ��
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String fDate = "1970-01-01";
				double groupLoadrunQps = 0;
				LoadrunResult result = cspLoadRunDao.findRecentlyAppLoadRunQps(Integer.parseInt(groupId));
				if (result != null) {
					groupLoadrunQps = result.getValue();
					fDate = format.format(result.getCollectTime());
				}

				logger.info("ѹ��qps:" + groupLoadrunQps);
				
				if (groupQps == 0 || maxGroupQps == 0) {
					logger.info("Ӧ�ã�" + appInfo.getAppName() + ",����:" + groupName + ",�߷���qps��" + groupQps + ",�߷��qps:" + maxGroupQps);
				}

				// ���������
				double groupHostNum = getAppGroupMachineNum(appInfo.getOpsName(), groupName);  
				// ������
				int groupRoom = getAppGroupRoomNum(appInfo.getOpsName(), groupName);
				
				// ��������ˮλ
				double groupCapacityRate = Arith.mul(Arith.div(maxGroupQps, groupLoadrunQps, 4), 100);
				
				// Ԥ������
				double growthRate = (double)po.getGrowthRate() / 100;
				double capacityStandard = (double)CommonUtil.getCapacityStandard(groupRoom) / 100;
				Double groupQpsForrest = Arith.mul(Arith.mul(maxGroupQps, growthRate + 1), groupHostNum);  // Ԥ�ڼ�Ⱥ����
				Double forRestMachineNum = Math.ceil(Arith.div(groupQpsForrest, Arith.mul(groupLoadrunQps, capacityStandard))); // �������
				double needMachine = Arith.sub(forRestMachineNum, groupHostNum);  // �����������
				double forRestGroupCapacityRate = Arith.mul(Arith.div(groupQpsForrest, groupLoadrunQps * groupHostNum, 4), 100); // Ԥ��ˮλ

				CapacityRankingPo grouprankingPo = mapTmp.get(groupId);
				if (grouprankingPo == null) {
					grouprankingPo = new CapacityRankingPo();
					mapTmp.put(Integer.parseInt(groupId), grouprankingPo);
				}
				grouprankingPo.setAppId(Integer.parseInt(groupId));
				grouprankingPo.setRankingName("��������");
				grouprankingPo.setCData(groupCapacityRate);
				grouprankingPo.setCQps(maxGroupQps);
				grouprankingPo.setCLoadrunQps(groupLoadrunQps);
				grouprankingPo.setAppName(name);
				grouprankingPo.setAppType(appInfo.getAppType());

				grouprankingPo.setRankingFeature(
						"��̨����ƽ��:" + Math.round(groupQps) + 
						"|��̨����:" + Math.round(maxGroupQps) + 
						"|�ɼ�ʱ��:" + maxGroupTime + 
						"|�ɼ�����:" + maxGroupHost +
						"|��̨����:" + Math.round(maxGroupQps) + 
						"|��Ⱥ����:" + Math.round(maxGroupQps * groupHostNum) + 
						"|��̨����:" + Math.round(groupLoadrunQps) + 
						"|ѹ��ʱ��:" + fDate + 
						"|��Ⱥ����:" +  Math.round(groupLoadrunQps * groupHostNum)	+ 
						"|������:" + Math.round(groupHostNum) + 
						"|������:" + Math.round(groupRoom) +
						"|����ˮλ:" + groupCapacityRate + "%" +
						"|������׼:" + capacityStandard * 100 + "%" +
						"|ҵ��������:" + po.getGrowthRate() + "%" +
						"|Ԥ�⼯Ⱥ����:" + Math.round(groupQpsForrest) +
						"|Ԥ������ˮλ:" + forRestGroupCapacityRate + "%" +
						"|Ԥ�������:" + Math.round(forRestMachineNum) +
						"|Ԥ���������:" + Math.round(needMachine) + "|");

				logger.info("������Ϣ��" + name);

			}
		}
		
		return mapTmp;
	}
	
	private CapacityRankingPo computeNormalCapacityRanking(CapacityAppPo po, AppInfoPo appInfo, Date date) {
		logger.info("��ʼ������������:" + appInfo.getAppName());
		CapacityRankingPo  rankingPo = new CapacityRankingPo();
		
		double singleQps = 0;
		double maxSingleQps = 0;
		double maxGroupQps = 0;
		double loadrunQps = 0;
		
		double machineNum = 0;
		int rooms = 0;

		double capacityRate = 0;

		// ��ȡѹ��������Ϣ

		// ��̨QPS ���ձ��л�ȡ
		int qpsKeyId = 16931;
		int maxQpsKeyId = 162579;
		int maxTimeKeyId = 162580;
		int maxHostKeyId = 166308;
		// int restKeyId = 16930;
		// int apachePvKeyId = 16929;
		// int qpsAllKeyId = 124167;

		if ("center".equals(appInfo.getAppType())) {
			qpsKeyId = 27734;
			maxQpsKeyId = 166684;
			maxTimeKeyId = 166685;
			maxHostKeyId = 166683;
			// restKeyId = 27735;
//			apachePvKeyId = 27733;
//			qpsAllKeyId = 124427;
		} else {
			qpsKeyId = 16931;
			maxQpsKeyId = 162579;
			maxTimeKeyId = 162580;
			maxHostKeyId = 166308;
			// restKeyId = 16930;
//			apachePvKeyId = 16929;
//			qpsAllKeyId = 124167;
		}

		// ���ձ��л�ȡ��Ϣ���� �����ֲ�
		Map<Integer, Double> hostValuemap = cspDayDao.findKeyValueFromCountByDay(appInfo.getAppDayId(), constants.getHostSiteKey().values(), date);
		logger.info("��ȡ������Ϣ:" + hostValuemap.keySet());

		// ��ȡȫ��ƽ��qps
		KeyValuePo qpsKeyValue = cspDayDao.findKeyValueFromCountByDay(qpsKeyId, appInfo.getAppDayId(), date);
		if (qpsKeyValue != null) {
			singleQps = Double.parseDouble(qpsKeyValue.getValueStr());// ��ǰ����qps
			logger.info("ȫ��ƽ��qps:" + singleQps);
		}
		
		// ��ȡȫ���߷��qps
		KeyValuePo maxQpsKeyValue = cspDayDao.findKeyValueFromCountByDay(maxQpsKeyId, appInfo.getAppDayId(), date);
		if (maxQpsKeyValue != null) {
			maxSingleQps = Double.parseDouble(maxQpsKeyValue.getValueStr()); // �߷��qps
			logger.info("�߷��qps:" + maxSingleQps);
		} else {
			CapacityLoadPo loadPo = capacityLoadDao.findSingleCapacityLoadByAppNameDate(appInfo.getAppName(), date);
			if (loadPo != null) {
				maxSingleQps = loadPo.getQps();
			}
			
			CapacityLoadPo groupLoadPo = capacityLoadDao.findGroupCapacityLoadByAppNameDate(appInfo.getAppName(), date);
			if (groupLoadPo != null) {
				maxGroupQps = groupLoadPo.getQps();
			}
		}
		
		String time = "1970-01-01 00:00";
		KeyValuePo maxTimeKeyValue = cspDayDao.findKeyValueFromCountByDayWithStringValue(maxTimeKeyId, appInfo.getAppDayId(), date);
		if (maxTimeKeyValue != null) {
			time = maxTimeKeyValue.getValueStr();
		}
		String host = "127.0.0.1";
		KeyValuePo maxHostKeyValue = cspDayDao.findKeyValueFromCountByDayWithStringValue(maxHostKeyId, appInfo.getAppDayId(), date);
		if (maxHostKeyValue != null) {
			host = maxHostKeyValue.getValueStr();
		}

		if (singleQps == 0 || maxSingleQps == 0) {
			logger.info("Ӧ�ã�" + appInfo.getAppName() + ",�߷���qps��" + singleQps + ",�߷��qps:" + maxSingleQps);
		}
		
		// ���������
		machineNum = getAppMachineNum(appInfo.getOpsName());  
		// ������
		rooms = getAppRoomNum(appInfo.getOpsName());
		
		maxGroupQps = maxSingleQps * machineNum;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fDate = "1970-01-01";
		LoadrunResult result = cspLoadRunDao.findRecentlyAppLoadRunQps(po.getAppId());
		if (result != null) {
			loadrunQps = result.getValue();
			fDate = format.format(result.getCollectTime());
		} else {
			CapacityCapPo capPo = capacityCapDao.findCapacityCapByAppName(appInfo.getAppName());
			if (capPo != null) {
				loadrunQps = capPo.getSingleCapacity();
			}
		}

		capacityRate = Arith.mul(Arith.div(maxGroupQps, loadrunQps * machineNum, 4), 100); // ����ˮλ
		
		// Ԥ������
		double growthRate = (double)po.getGrowthRate() / 100;
		double capacityStandard = (double) CommonUtil.getCapacityStandard(rooms) / 100;
		Double groupQpsForrest = Arith.mul(maxGroupQps, growthRate + 1);  // Ԥ�ڼ�Ⱥ����
		Double forRestMachineNum = Math.ceil(Arith.div(groupQpsForrest, Arith.mul(loadrunQps, capacityStandard))); // �������
		double needMachine = Arith.sub(forRestMachineNum, machineNum);  // �����������
		double forRestGroupCapacityRate = Arith.mul(Arith.div(groupQpsForrest, loadrunQps * machineNum, 4), 100); // Ԥ��ˮλ

		rankingPo.setAppId(po.getAppId());
		rankingPo.setAppName(appInfo.getAppName());
		rankingPo.setAppType(appInfo.getAppType());
		rankingPo.setRankingName("��������");
		rankingPo.setCData(capacityRate);
		rankingPo.setCQps(maxSingleQps);
		rankingPo.setCLoadrunQps(loadrunQps);

		rankingPo.setRankingFeature(
				"��̨����ƽ��:" + Math.round(singleQps) + 
				"|��̨����:" + Math.round(maxSingleQps) + 
				"|�ɼ�ʱ��:" + time + 
				"|�ɼ�����:" + host +
				"|��Ⱥ����:" + Math.round(maxGroupQps) + 
				"|��̨����:" + Math.round(loadrunQps) + 
				"|ѹ��ʱ��:" + fDate + 
				"|��Ⱥ����:" + Math.round(loadrunQps * machineNum)	+ 
				"|������:" + Math.round(machineNum) + 
				"|������:" + Math.round(rooms) +
				"|����ˮλ:" + Math.round(capacityRate) + "%" +
				"|������׼:" + capacityStandard * 100 + "%" +
				"|ҵ��������:" + po.getGrowthRate() + "%" +
				"|Ԥ�⼯Ⱥ����:" + Math.round(groupQpsForrest) +
				"|Ԥ������ˮλ:" + forRestGroupCapacityRate + "%" +
				"|Ԥ�������:" + Math.round(forRestMachineNum) +
				"|Ԥ���������:" + Math.round(needMachine) + "|");
		return rankingPo;
	}
	
	private CapacityRankingPo computeCapacityRanking(CapacityAppPo po, AppInfoPo appInfo, Date date) {
		logger.info("��ʼ������������:" + appInfo.getAppName());
		CapacityRankingPo  rankingPo = new CapacityRankingPo();
		
		double singleQps = 0;
		
		double maxSingleQps = 0;
		String time = "1970-01-01 00:00";
		String host = "127.0.0.1";
		
		double maxGroupQps = 0;
		double loadrunQps = 0;
		
		double machineNum = 0;
		int rooms = 0;

		double capacityRate = 0;

		// ��̨QPS ���ձ��л�ȡ
		int qpsKeyId = 16931;
		if ("center".equals(appInfo.getAppType())) {
			qpsKeyId = 27734;
		}

		// ��ȡȫ��ƽ��qps
		KeyValuePo qpsKeyValue = cspDayDao.findKeyValueFromCountByDay(qpsKeyId, appInfo.getAppDayId(), date);
		if (qpsKeyValue != null) {
			singleQps = Double.parseDouble(qpsKeyValue.getValueStr());// ��ǰ����qps
			logger.info("ȫ��ƽ��qps:" + singleQps);
		}
		
		
		MaxQps maxQps = getMaxSingleQps(po, appInfo, date);
		maxSingleQps = maxQps.qps;
		time = maxQps.time;
		host = maxQps.hostIp;
		if (maxSingleQps == 0) {
			CapacityLoadPo loadPo = capacityLoadDao.findSingleCapacityLoadByAppNameDate(appInfo.getAppName(), date);
			if (loadPo != null) {
				maxSingleQps = loadPo.getQps();
			}
			
			CapacityLoadPo groupLoadPo = capacityLoadDao.findGroupCapacityLoadByAppNameDate(appInfo.getAppName(), date);
			if (groupLoadPo != null) {
				maxGroupQps = groupLoadPo.getQps();
			}
		}

		if (singleQps == 0 || maxSingleQps == 0) {
			logger.info("Ӧ�ã�" + appInfo.getAppName() + ",�߷���qps��" + singleQps + ",�߷��qps:" + maxSingleQps);
		}
		
		// ����������
		String groupName = po.getGroupNames();
		if (StringUtils.isNotEmpty(groupName)) {
			machineNum = getAppGroupMachineNum(appInfo.getOpsName(), groupName);
			rooms = getAppGroupRoomNum(appInfo.getOpsName(), groupName);
		} else {
			machineNum = getAppMachineNum(appInfo.getOpsName()); 
			rooms = getAppRoomNum(appInfo.getOpsName());
		}
		
		maxGroupQps = maxSingleQps * machineNum;
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String fDate = "1970-01-01";
		LoadrunResult result = cspLoadRunDao.findRecentlyAppLoadRunQps(po.getAppId());
		if (result != null) {
			loadrunQps = result.getValue();
			fDate = format.format(result.getCollectTime());
		} else {
			CapacityCapPo capPo = capacityCapDao.findCapacityCapByAppName(appInfo.getAppName());
			if (capPo != null) {
				loadrunQps = capPo.getSingleCapacity();
			}
		}

		capacityRate = Arith.mul(Arith.div(maxGroupQps, loadrunQps * machineNum, 4), 100); // ����ˮλ
		
		// Ԥ������
		double growthRate = (double)po.getGrowthRate() / 100;
		double capacityStandard = (double) CommonUtil.getCapacityStandard(rooms) / 100;
		Double groupQpsForrest = Arith.mul(maxGroupQps, growthRate + 1);  // Ԥ�ڼ�Ⱥ����
		Double forRestMachineNum = Math.ceil(Arith.div(groupQpsForrest, Arith.mul(loadrunQps, capacityStandard))); // �������
		double needMachine = Arith.sub(forRestMachineNum, machineNum);  // �����������
		double forRestGroupCapacityRate = Arith.mul(Arith.div(groupQpsForrest, loadrunQps * machineNum, 4), 100); // Ԥ��ˮλ

		rankingPo.setAppId(po.getAppId());
		rankingPo.setAppName(appInfo.getAppName());
		rankingPo.setAppType(appInfo.getAppType());
		rankingPo.setRankingName("��������");
		rankingPo.setCData(capacityRate);
		rankingPo.setCQps(maxSingleQps);
		rankingPo.setCLoadrunQps(loadrunQps);

		rankingPo.setRankingFeature(
				"��̨����ƽ��:" + Math.round(singleQps) + 
				"|��̨����:" + Math.round(maxSingleQps) + 
				"|�ɼ�ʱ��:" + time + 
				"|�ɼ�����:" + host +
				"|��Ⱥ����:" + Math.round(maxGroupQps) + 
				"|��̨����:" + Math.round(loadrunQps) + 
				"|ѹ��ʱ��:" + fDate + 
				"|��Ⱥ����:" + Math.round(loadrunQps * machineNum)	+ 
				"|������:" + Math.round(machineNum) + 
				"|������:" + Math.round(rooms) +
				"|����ˮλ:" + Math.round(capacityRate) + "%" +
				"|������׼:" + capacityStandard * 100 + "%" +
				"|ҵ��������:" + po.getGrowthRate() + "%" +
				"|Ԥ�⼯Ⱥ����:" + Math.round(groupQpsForrest) +
				"|Ԥ������ˮλ:" + forRestGroupCapacityRate + "%" +
				"|Ԥ�������:" + Math.round(forRestMachineNum) +
				"|Ԥ���������:" + Math.round(needMachine) + "|");
		return rankingPo;
	}
	
	private MaxQps getMaxSingleQps(CapacityAppPo po, AppInfoPo appInfo, Date date) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String dateS = sf.format(date);
		
		List<HostPo> candidateHostL = new ArrayList<HostPo>();
		MaxQps maxQps = new MaxQps();
		String appName = appInfo.getOpsName();
		String groupName = po.getGroupNames();
		String itemName = po.getItemName();
		String dataName = po.getDataName();
		Map<String, List<String>> hostM = GroupManager.get().getGroupInfoByAppName(appName);
		if (hostM != null && hostM.get(groupName) != null && hostM.get(groupName).size() > 0) {
			List<String> hostL = hostM.get(groupName);
			
			List<HostPo> hostPoL = CspSyncOpsHostInfos.findOnlineHostByOpsName(appName);
			for (HostPo hostPo : hostPoL) {
				if (hostL.contains(hostPo.getHostIp())) {
					candidateHostL.add(hostPo);
				}
			}
		} else {
			candidateHostL = CspSyncOpsHostInfos.findOnlineHostByOpsName(appName);
		}
		
		List<MaxQps> maxQpsL = new ArrayList<CapacityRankingBo.MaxQps>();
		String data = "";
		for (HostPo sampleHost : candidateHostL) {
			String hostName = sampleHost.getHostName();
			data = getEMonitorSingleData(appName, hostName, itemName, dataName, dateS);
			if (data.length() > 200) {
				MaxQps hostMaxQps = analyseMaxQps(sampleHost.getHostIp(), data);
				maxQpsL.add(hostMaxQps);
			}
			
			// ȡ5̨������
			if (maxQpsL.size() >= 5) {
				break;
			}
		}
		
		int size = maxQpsL.size();
		if (size > 0) {
			Collections.sort(maxQpsL);
			
			maxQps = maxQpsL.get(0);
		}
		
		return maxQps;
	}
	
	/***
	 * �Ӽ��e��ȡ��������
	 * 
	 * @param appName
	 * @param hostName
	 * @param itemName
	 * @param dataName
	 * @param date
	 * @return
	 */
	private String getEMonitorSingleData(String appName, String hostName, String itemName, String dataName, String date) {
		String eMonitorUrl = "http://monitor.taobao.com/monitorapi/emonitorDetailData.do?"
				+ "host=" + hostName + "&item=" + itemName
				+ "&dataitem=" + dataName + "&date=" + date;
		
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(eMonitorUrl);
		    inputStream = url.openStream();
			inputStreamReader = new InputStreamReader(inputStream);
			bufferedReader = new BufferedReader(inputStreamReader);
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
		} catch (Exception e) {
			logger.error(e);
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					logger.error(e);
				}
			}
		}
		return sb.toString();
	}
	
	private MaxQps analyseMaxQps(String hostIp, String result) {
		MaxQps maxQps = new MaxQps();
		try{
			JSONArray info = JSONArray.fromObject(result);
			if(info.size()==0){
				return maxQps;
			}
			JSONObject object = info.getJSONObject(0);
			for(Object obj : object.values())
				if (obj instanceof JSONArray) {
					JSONArray json = (JSONArray) obj;
					for (int ch = 0; ch < json.size(); ch++){
						Object tmp = json.get(ch);
						if(tmp instanceof JSONObject){
							Set keyS = ((JSONObject) tmp).keySet();
							Collection valueC = ((JSONObject) tmp).values();
							String time = keyS.toArray()[0].toString();
							double qps = Double.parseDouble(valueC.toArray()[0].toString());
							
							if (qps > maxQps.qps) {
								maxQps.qps = qps;
								maxQps.hostIp = hostIp;
								maxQps.time = time;
							}
							
						}
					}
			}
		}catch (Exception e) {
			logger.error("",e);
		}catch(OutOfMemoryError e1){
			logger.error("",e1);
		}
		return maxQps;
	} 
	
	/***
	 * ʵʱ��ȡӦ�û�����
	 * @param appName
	 * @return
	 */
	private int getAppMachineNum(String appName) {
		int machineNum = 0;
		
		List<HostPo> hostM = CspSyncOpsHostInfos.findOnlineHostByOpsName(appName);
		if (hostM != null) {
			machineNum = hostM.size();
		}
		
		return machineNum;
	}
	
	/***
	 * ʵʱ��ȡӦ�û�����
	 * @param appName
	 * @return
	 */
	private int getAppRoomNum(String appName) {
		int roomNum = 0;
		Set<String> siteS =  new HashSet<String>();
		
		List<HostPo> hostM = CspSyncOpsHostInfos.findOnlineHostByOpsName(appName);
		if (hostM != null) {
			for (HostPo hostPo : hostM) {
				if (StringUtils.isNotEmpty((hostPo.getHostSite()))) {
					siteS.add(hostPo.getHostSite());
				}
			}
		}
		roomNum = siteS.size();
		
		return roomNum;
	}
	
	/***
	 * ʵʱ��ȡ���������
	 * @param appName
	 * @param groupName G1��G2
	 * @return
	 */
	private int getAppGroupMachineNum(String appName, String groupName) {
		int machineNum = 0;
		
		List<String> gHostL = null;
		Map<String, List<String>> gHostM = GroupManager.get().getGroupInfoByAppName(appName);
		if (gHostM != null) {
			gHostL = gHostM.get(groupName);
		}
		
		if (gHostL != null) {
			machineNum = gHostL.size();
		}
		
		return machineNum;
	}
	
	/***
	 * ʵʱ��ȡ�����������
	 * @param appName
	 * @param groupName G1��G2
	 * @return
	 */
	private int getAppGroupRoomNum(String appName, String groupName) {
		int roomNum = 0;
		Set<String> siteS =  new HashSet<String>();
		
		List<HostPo> hostM = CspSyncOpsHostInfos.findOnlineHostByOpsName(appName);
		
		List<String> gHostL = null;
		Map<String, List<String>> gHostM = GroupManager.get().getGroupInfoByAppName(appName);
		if (gHostM != null) {
			gHostL = gHostM.get(groupName);
		}
		
		if (hostM != null && gHostL != null) {
			for (HostPo hostPo : hostM) {
				if (gHostL.contains(hostPo.getHostIp())) {
					siteS.add(hostPo.getHostSite());
				}
			}
		}
		roomNum = siteS.size();
		
		return roomNum;
	}
	
	private void computeExternalCapacity() throws Exception {
		HttpClient httpClient = new HttpClient();
		String url = "http://172.18.111.1/cgi-bin/ims_csp.py";
		
		GetMethod httpGet = new GetMethod(url);
		httpClient.executeMethod(httpGet);
		
		StringBuffer contentBuffer = new StringBuffer();
		InputStream in = httpGet.getResponseBodyAsStream();
		byte[] byteA = new byte[1024];
		int size = 0;
		while ((size = in.read(byteA)) > 0) {
			contentBuffer.append(new String(byteA, 0, size, "GBK"));
		}

		String content = contentBuffer.toString();
		
		JSONArray array = JSONArray.fromObject(content);
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String dateS = sf.format(calendar.getTime());
		
		String deleteSql = "delete from scp_capacity_ranking where app_type='ims' and ranking_date='" + dateS + "';";
		capacityRankingDao.execute(deleteSql);
		
		for (int i = 0; i < array.size(); i++) {
			String insertSql = "INSERT INTO scp_capacity_ranking VALUES (";
			JSONObject object = (JSONObject)array.get(i);
			
			String group_name = object.getString("groupName");
			String ops_name = object.getString("opsName");
			long single_load = object.getLong("single_load");
			long cluster_load = object.getLong("cluster_load");
			int rooms = object.getInt("rooms");
			int machine_num = object.getInt("machine_num");
			int single_cap = object.getInt("single_capacity");
			
			double level = (double)cluster_load * 100 / single_cap / machine_num;
			DecimalFormat df = new DecimalFormat("##.##");
			String levelS = df.format(level);
			
			int forestMachine = (int)Math.ceil((double)cluster_load / 0.4 / single_cap);
			int increaseOrDecrease = forestMachine - machine_num;
			
			int id = 0;
			if (group_name.equals("NORMAL_IMS48G")) {
				id = 645001;
			}
			if (group_name.equals("NORMAL_IMS24G")) {
				id = 645002;
			}
			if (group_name.equals("ECLIENT_IMS48G")) {
				id = 645003;
			}
			if (group_name.equals("ECLIENT_IMS24G")) {
				id = 645004;
			}
			
			
			insertSql = insertSql + id + ",'��������',";
			insertSql = insertSql + levelS + "," + single_load + "," + single_cap + ",201," + "'" + dateS + "',";
			insertSql = insertSql + "'��̨����ƽ��:10000|��̨����:" + single_load + "|�ɼ�ʱ��:1970-01-01 00:00|�ɼ�����:127.0.0.1|��Ⱥ����:" + cluster_load + "|��̨����:" + single_cap + "|ѹ��ʱ��:1970-01-01|��Ⱥ����:" +(single_cap*machine_num) 
			+ "|������:" + machine_num + "|������:" + rooms + "|����ˮλ:" +levelS + "%|������׼:40%|ҵ��������:0%|Ԥ�⼯Ⱥ����:" + cluster_load + "|Ԥ������ˮλ:" + levelS + "%|Ԥ�������:" + forestMachine + "|Ԥ���������:" + increaseOrDecrease + "|',";
			insertSql = insertSql + "'" + group_name + "',";
			insertSql = insertSql + "'" + ops_name + "');";
			
			capacityRankingDao.execute(insertSql);
		}
	}
	
	private class MaxQps implements Comparable<MaxQps> {
		double qps;
		String time = "1970-01-01 00:00";
		String hostIp = "127.0.0.1";
		
		@Override
		public int compareTo(MaxQps o) {
			if(this.qps == o.qps) {
				return 0;
			} else if (this.qps < o.qps){
				return -1;
			} else {
				return 1;
			}
		}
	}
}
