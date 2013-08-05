

/**
 * monitorstat-common
 */
package com.taobao.monitor.common.ao.center;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.CspDependInfoDao;
import com.taobao.monitor.common.po.AppUrlRelation;
import com.taobao.monitor.common.po.CspAppDepAppPo;
import com.taobao.monitor.common.po.CspAppHotInterface;
import com.taobao.monitor.common.po.CspCallsRelationship;
import com.taobao.monitor.common.po.CspKeyDependInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspTimeAppDependInfo;
import com.taobao.monitor.common.po.CspTimeExtraAppInfo;
import com.taobao.monitor.common.po.CspTimeExtraAppKey;
import com.taobao.monitor.common.po.CspTimeKeyDependInfo;
import com.taobao.monitor.common.po.ReleaseInfo;
import com.taobao.monitor.common.po.ReportInvokeDataPo;
import com.taobao.monitor.common.po.TreeGridBasePo;
import com.taobao.monitor.common.po.TreeGridData;
import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.common.util.Utlitites;

public class CspDependInfoAo {

	private CspDependInfoAo(){}

	private static final Logger logger = Logger.getLogger(CspDependInfoAo.class);

	private static CspDependInfoAo ao = new CspDependInfoAo();

	public static CspDependInfoAo get(){
		return ao;
	}

	private CspDependInfoDao dependDao = new CspDependInfoDao();

	/**
	 * ����ʱ���ѯ�����ر�csp_app_depend_app��list,��֤��Ϊnull
	 * 
	 * @param time
	 * @return
	 */
	public List<CspAppDepAppPo> getAppDepAppList(String time) {
		try {
			return dependDao.getAppDepAppList(time);
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<CspAppDepAppPo>();
	}

	public List<CspAppDepAppPo> getSubAppDepAppList(String time, String appName) {
		try {
			if(time == null)
				time = Utlitites.getDateBefore(new Date(), 1, "yyyy-MM-dd");
			return dependDao.getAppDepAppList(time);
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<CspAppDepAppPo>();
	}

	/**
	 * ���� appname, id��ʽ��map
	 */
	public HashMap<String, String> getAppName_Id_Map(
			List<CspKeyDependInfoPo> appList) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			for (CspKeyDependInfoPo po : appList) {
				map.put(po.getCurAppName(), po.getId() + "");
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}

	/**
	 * ���� id��appname��ʽ��map
	 */
	public HashMap<String, String> getAppId_NameMap(
			List<CspKeyDependInfoPo> appList) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			for (CspKeyDependInfoPo po : appList) {
				map.put(po.getId() + "", po.getCurAppName());
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}

	/*
	 * �����Զ����ɵ�����������Ϣ
	 */
	public Map<String, CspTimeAppDependInfo> getMeDependMap_New(String sourceAppName,String appName) {
		try {
			//�����Զ����ɵ�App������Ϣ
			Map<String, CspKeyDependInfoPo> mapAuto = dependDao
					.getCspKeyDependInfoAuto(appName, CspDependInfoDao.SEARCHTYPE_APP);

			//			CspDependInfoAo.get().getSubAppDepAppList(null, appName);

			Map<String, CspTimeAppDependInfo> existMap = dependDao.getCspTimeAppDependInfo(sourceAppName, appName);
			for(CspTimeAppDependInfo po : existMap.values()) {
				String appNameTmp = po.getDepAppName();
				if(mapAuto.containsKey(appNameTmp)) {
					mapAuto.remove(appNameTmp);
					po.setDependStatus(Constants.AUTO_CONFIG + "");
				} else {
					po.setDependStatus(Constants.NOTAUTO_CONFIG + "");
				}
			}

			//�ֶ����õ���Ϣ
			Map<String, CspTimeExtraAppInfo> extraMap = dependDao.getCspTimeExtraAppInfoMap();
			Iterator<String> iter = extraMap.keySet().iterator();
			while(iter.hasNext()) {
				String extraAppName = iter.next();
				if(existMap.containsKey(extraAppName) || mapAuto.containsKey(extraAppName)) {
					iter.remove();
				} else {
					CspTimeAppDependInfo po = new CspTimeAppDependInfo();
					po.setId(-1);
					po.setSourceAppName(sourceAppName);
					po.setAppName(appName);
					po.setDepAppName(extraAppName);
					po.setDependtype(extraMap.get(extraAppName).getExtraAppType());
					po.setDependStatus(Constants.EXTRA_CONFIG + "");
					existMap.put(extraAppName, po);
				}
			}

			//���Զ����ɵ�Poת��Ϊ������Ҫ��Po
			for(String depAppName : mapAuto.keySet()) {
				CspKeyDependInfoPo autoPo = mapAuto.get(depAppName);
				CspTimeAppDependInfo po = new CspTimeAppDependInfo();
				po.setId(autoPo.getId());
				po.setSourceAppName(sourceAppName);
				po.setAppName(appName);
				po.setDepAppName(depAppName);
				po.setDependtype(autoPo.getDepend_app_type());
				po.setDependStatus(Constants.AUTO_NOTCONFIG + "");
				existMap.put(depAppName, po);
			}
			return existMap;
		} catch (Exception e) {
			logger.error("", e);
		}
		return new HashMap<String, CspTimeAppDependInfo>();
	}

	/*
	 * ����Ӧ������
	 */
	@SuppressWarnings("unchecked")
	public void saveAppConfig(String sourceAppName,String appName, Set<String> saveExistIdSet, Set<String> saveAutoIdSet, Set<String> extraAppNameSet) {
		try {
			List<CspTimeAppDependInfo> totalList = dependDao.getCspTimeAppDependInfo(sourceAppName);

			Map<String, CspTimeAppDependInfo> curOptMap = new HashMap<String, CspTimeAppDependInfo>();
			for(CspTimeAppDependInfo po: totalList) {
				curOptMap.put(po.getAppName() + "_" + po.getDepAppName(), po);
			}

			Map<String, Object[]> totalMap = new HashMap<String,Object[]>(); //<appName, object[] = [depend_me count, me_depend app Set]
			for(CspTimeAppDependInfo info: totalList) {
				Object[] objArray = totalMap.get(info.getAppName());
				if(objArray == null) {
					objArray = new Object[2];
					objArray[0] = new Integer(0);
					objArray[1] = new HashSet<String>();
					totalMap.put(info.getAppName(), objArray);
				}
				String depAppNameLocal = info.getDepAppName();
				HashSet<String> set = (HashSet<String>)objArray[1];
				set.add(depAppNameLocal);
				//objArray[1] = set;  //���¸��ƻ�ȥ

				//����������
				Object[] objDepArray = totalMap.get(depAppNameLocal);
				if(objDepArray == null) {
					objDepArray = new Object[2];
					objDepArray[0] = new Integer(0);
					objDepArray[1] = new HashSet<String>(); 
					totalMap.put(info.getDepAppName(), objDepArray);
				}
				//�����ô���+1
				objDepArray[0] = (Integer)objDepArray[0] + 1;
			}

			Set<String> deleteAppIdSet = new HashSet<String>();

			//��ȡ��ǰ�����App�������Ϣ
			Object[] array = totalMap.get(appName);
			if(array != null) {
				HashSet<String> depAppNameSet = (HashSet<String>) array[1];
				Iterator<String> iterator = depAppNameSet.iterator();
				while(iterator.hasNext()) {
					String depAppName = iterator.next();
					String id = curOptMap.get(appName + "_" + depAppName).getId() + "";
					if(!saveExistIdSet.contains(id)) {
						deleteAppIdSet.add(id);
						deleteDepApp(depAppName, deleteAppIdSet, totalMap, curOptMap);  //�ݹ�ɾ��
						//iterator.remove();
					}
				}
			}
			//������벿�ֵ�����
			List<CspTimeAppDependInfo> insertList = new ArrayList<CspTimeAppDependInfo>();
			List<CspKeyDependInfoPo> autoList = dependDao.getDependMapByIds_Auto(saveAutoIdSet.toArray(new String[0]));
			for(CspKeyDependInfoPo autoPo  : autoList) {  
				CspTimeAppDependInfo po = new CspTimeAppDependInfo();
				po.setSourceAppName(sourceAppName);
				po.setAppName(appName);
				po.setDepAppName(autoPo.getCurAppName());
				po.setDependtype(autoPo.getDepend_app_type());
				po.setDependStatus(Constants.AUTO_CONFIG + "");
				insertList.add(po);
			}

			//�����ֶ����õĲ���,���õ�Ӧ�ò���̫�࣬һ���Զ��������FIXME if you need
			Map<String, CspTimeExtraAppInfo> extraMap = dependDao.getCspTimeExtraAppInfoMap();
			for(String extraAppName: extraAppNameSet) {
				CspTimeExtraAppInfo info = extraMap.get(extraAppName);
				if(info != null) {
					CspTimeAppDependInfo po = new CspTimeAppDependInfo();
					po.setSourceAppName(sourceAppName);
					po.setAppName(appName);
					po.setDepAppName(info.getExtraAppName());
					po.setDependtype(info.getExtraAppType());
					po.setDependStatus(Constants.AUTO_CONFIG + "");
					insertList.add(po);
				}
			}
			//

			//������
			dependDao.deleteCspTimeAppDependInfo(deleteAppIdSet.toArray(new String[0]));
			dependDao.insertCspTimeAppDependInfo(insertList);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * ����sourceapp��һ��ҵ����
	 * @param sourceAppName
	 * @return
	 */
	public List<CspTimeAppDependInfo> getCspTimeAppDependInfoBySourceName (String sourceAppName) {
		try {
			List<CspTimeAppDependInfo> totalList = dependDao
					.getCspTimeAppDependInfo(sourceAppName);
			return totalList;
		} catch (Exception e) {
			logger.error("",e);
			return new ArrayList<CspTimeAppDependInfo>();
		}
	}

	/*
	 * �ݹ�ɾ��appName���ӽڵ�
	 */
	@SuppressWarnings("unchecked")  
	private void deleteDepApp(String appName, final Set<String> deleteAppIdSet, final Map<String, Object[]> totalMap, final Map<String, CspTimeAppDependInfo> curOptMap) {
		//��ȡappName������Ӧ����Ϣ
		Object[] array = totalMap.get(appName);
		Integer dependMeTime = (Integer) array[0];
		if(dependMeTime > 1) {
			return; //���������ң���ɾ�������ӽڵ�Ĺ�ϵ
		}
		HashSet<String> depAppNameSet = (HashSet<String>) array[1];
		for(String depAppName: depAppNameSet) {
			String id = curOptMap.get(appName + "_" + depAppName).getId() + "";
			deleteAppIdSet.add(id);
			deleteDepApp(depAppName, deleteAppIdSet, totalMap, curOptMap);
		}
	}

	/**
	 * ����sourceapp�����depth������
	 * @return
	 */
	public List<CspTimeAppDependInfo> getCspTimeAppDependInfoBySourceNameByDepth (String sourceAppName, int depth) {
		try {
			List<CspTimeAppDependInfo> totalList = dependDao
					.getCspTimeAppDependInfo(sourceAppName);
			//FIXME ����д��depthΪ1�������޸�
			Iterator<CspTimeAppDependInfo> iter = totalList.iterator();
			while(iter.hasNext()) {
				try {
					CspTimeAppDependInfo info = iter.next();
					if (!info.getAppName().equals(info.getSourceAppName())) {
						iter.remove();
					}
				} catch (Exception e) {
					logger.error("",e);
				}        
			}
			return totalList;
		} catch (Exception e) {
			logger.error("",e);
			return new ArrayList<CspTimeAppDependInfo>();
		}
	}

	public void addExtraApp(String extraAppName, String extraAppDesc, String extraAppType) {
		try {
			CspTimeExtraAppInfo extraApp = new CspTimeExtraAppInfo();
			extraApp.setExtraAppName(extraAppName);
			extraApp.setExtraAppDesc(extraAppDesc);
			extraApp.setExtraAppType(extraAppType);
			dependDao.insertCspTimeKeyExtraAppInfo(extraApp);
		} catch (Exception e) {
			logger.error("",e);
		}
	}  

	public void addExtraAppKey(String extraAppName, String keyName) {
		try {
			CspTimeExtraAppKey extraAppKey = new CspTimeExtraAppKey();
			extraAppKey.setExtraAppName(extraAppName);
			extraAppKey.setKeyName(keyName);
			dependDao.insertCspTimeExtraAppKey(extraAppKey);
		} catch (Exception e) {
			logger.error("",e);
		}
	}   

	public List<CspTimeKeyDependInfo> showSourceKeyList (String sourceAppName) {
		try {
			List<CspTimeKeyDependInfo> totalList = dependDao
					.getCspTimeKeyDependInfo(sourceAppName);
			HashMap<String, CspTimeKeyDependInfo> map = new HashMap<String, CspTimeKeyDependInfo>();
			for(CspTimeKeyDependInfo info: totalList) {
				if(!map.containsKey(info.getSourceKeyName())) {
					map.put(info.getSourceKeyName(), info);
				}
			}
			return new ArrayList<CspTimeKeyDependInfo>(map.values());
		} catch (Exception e) {
			logger.error("",e);
			return new ArrayList<CspTimeKeyDependInfo>();
		}
	}
	public List<CspTimeKeyDependInfo> getSingleSourceKeyList(String sourceAppName,String sourceKeyName) {
		try {
			return dependDao.getCspTimeKeyDependInfo_Key(sourceAppName, sourceKeyName);
		} catch (Exception e) {
			logger.info("",e);
		}
		return new ArrayList<CspTimeKeyDependInfo>();
	}
	/*
	 * ����Ӧ������,key������App����ڲ�ѯ��ʱ��ֱ��ȡ�õ������ﲻ��Ҫ�ظ���ѯһ��
	 * saveAutoMap = <String id, String appName>
	 */
	@SuppressWarnings("unchecked")
	public void saveSubKey(String sourceAppName,String sourceKeyName, String appName, String keyName,
			Set<String> saveExistIdSet, Map<String,String> saveAutoMap, Set<CspTimeExtraAppKey> extraAppKeySet) {
		try {
			List<CspTimeKeyDependInfo> totalList = dependDao.getCspTimeKeyDependInfo_Key(sourceAppName, sourceKeyName);

			Map<String, CspTimeKeyDependInfo> curOptMap = new HashMap<String, CspTimeKeyDependInfo>();
			for(CspTimeKeyDependInfo po: totalList) {
				curOptMap.put(po.getKeyName() + "_" + po.getDependKeyName(), po);
			}

			Map<String, Object[]> totalMap = new HashMap<String,Object[]>(); //<keyName, object[] = [depend_me count, me_depend key Set]
			for(CspTimeKeyDependInfo info: totalList) {
				Object[] objArray = totalMap.get(info.getKeyName());
				if(objArray == null) {
					objArray = new Object[2];
					objArray[0] = new Integer(0);
					objArray[1] = new HashSet<String>();
					totalMap.put(info.getKeyName(), objArray);
				}
				String depKeyNameLocal = info.getDependKeyName();
				HashSet<String> set = (HashSet<String>)objArray[1];
				set.add(depKeyNameLocal);

				//����������
				Object[] objDepArray = totalMap.get(depKeyNameLocal);
				if(objDepArray == null) {
					objDepArray = new Object[2];
					objDepArray[0] = new Integer(0);
					objDepArray[1] = new HashSet<String>(); 
					totalMap.put(info.getDependKeyName(), objDepArray);
				}
				//�����ô���+1
				objDepArray[0] = (Integer)objDepArray[0] + 1;
			}

			Set<String> deleteKeyIdSet = new HashSet<String>();

			//��ȡ��ǰ�����App�������Ϣ
			Object[] array = totalMap.get(keyName);
			if(array != null) {
				HashSet<String> depKeyNameSet = (HashSet<String>) array[1];
				Iterator<String> iterator = depKeyNameSet.iterator();
				while(iterator.hasNext()) {
					String depKeyName = iterator.next();
					String id = curOptMap.get(keyName + "_" + depKeyName).getId() + "";
					if(!saveExistIdSet.contains(id)) {
						deleteKeyIdSet.add(id);
						deleteDepKey(depKeyName, deleteKeyIdSet, totalMap, curOptMap);  //�ݹ�ɾ��
						//iterator.remove(); //remove or not is all ok
					}
				}
			}

			//������벿�ֵ�����
			List<CspTimeKeyDependInfo> insertList = new ArrayList<CspTimeKeyDependInfo>();
			List<CspKeyDependInfoPo> autoList = dependDao.getDependMapByIds_Auto(saveAutoMap.keySet().toArray(new String[0]));
			for(CspKeyDependInfoPo autoPo : autoList) { //���Զ����ɵ�PCת��Ϊ������Ҫ��Po
				CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
				po.setSourceAppName(sourceAppName);
				po.setSourceKeyName(sourceKeyName);
				po.setAppName(appName);
				po.setKeyName(keyName); 
				po.setDependAppName(saveAutoMap.get(autoPo.getId() + ""));
				po.setDependStatus(Constants.AUTO_CONFIG + "");

				//����key���Ͳ�ͬ��ƴ�ӳɲ�ͬ��ʽ���ַ���
				if(autoPo.getUrl() != null && !autoPo.getUrl().equals(autoPo.getCurKeyame() + "_")) {
					po.setDependKeyName("HSF-provider`" + autoPo.getCurKeyame());
				} else {
					po.setDependKeyName("PV`" + autoPo.getCurKeyame());
				}                
				insertList.add(po);
			}

			//�����ֶ���ӵĲ���
			for(CspTimeExtraAppKey extraKey: extraAppKeySet) {
				CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
				po.setSourceAppName(sourceAppName);
				po.setSourceKeyName(sourceKeyName);
				po.setAppName(appName);
				po.setKeyName(keyName); 
				po.setDependAppName(extraKey.getExtraAppName());
				po.setDependKeyName(extraKey.getKeyName());
				po.setDependStatus(Constants.AUTO_CONFIG + "");
				insertList.add(po);
			}

			//������
			dependDao.deleteCspTimeKeyDependInfo(deleteKeyIdSet.toArray(new String[0]));
			dependDao.insertCspTimeKeyDependInfo(insertList);

			//��ֹ����sourcekey��ɾ����bug
			if(deleteKeyIdSet.size() != 0 && insertList.size() == 0 ) {
				if(dependDao.getCspTimeKeyDependInfo_Key(sourceAppName, sourceKeyName).size() == 0) {
					addSourceKey(sourceAppName, sourceKeyName);
				}        
			}
		} catch (Exception e) {
			logger.error("", e);
		}
	}  

	/**
	 * ������������ɣ�Ӧ�ö�Ӧ��URL��Key��HSF-provider`��
	 * @param appId
	 * @param appName
	 * @return
	 */
	public List<String> getKeyList(int appId, String appName) {
		Set<String> set = new HashSet<String>();
		try {
			List<AppUrlRelation> list = AppInfoAo.get().findAllAppUrlRelation(appName);
			for(AppUrlRelation relation : list) {
				set.add("PV`" + relation.getAppUrl());
			}
		} catch (Exception e) {
			logger.error("",e);
		}
		List<String> list = new ArrayList<String>(set);

		List<CspKeyInfo> keyList = KeyAo.get().findKeyListByAppId(appId);
		for(CspKeyInfo info: keyList) {
			String providerKeyName = info.getKeyName(); 
			if(providerKeyName != null && providerKeyName.startsWith("HSF-provider`")) {
				list.add(info.getKeyName());
				continue;
			}
			//��HSF-provider`��HSF��Ϣȫ�����˵������eagleeye�����ԣ�ֻ��ʾprovider����Ϣ      
		}
		return list;
	}

	public void addSourceKey(String sourceAppName, String sourceKeyName) {
		try {
			CspTimeKeyDependInfo keyInfo = new CspTimeKeyDependInfo();
			keyInfo.setSourceAppName(sourceAppName);
			keyInfo.setAppName(sourceAppName);
			keyInfo.setSourceKeyName(sourceKeyName);
			keyInfo.setKeyName(sourceKeyName);
			keyInfo.setDependAppName("");
			keyInfo.setDependKeyName("");
			keyInfo.setDependStatus(Constants.AUTO_CONFIG + "");
			dependDao.insertCspTimeKeyDependInfo(keyInfo);
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	public void deleteSourceKey(String sourceAppName, String sourceKeyName, String appName) {
		try {
			dependDao.deleteSourceKey(sourceAppName,sourceKeyName);
		} catch (Exception e) {
			logger.error("",e);
		}
	}

	/*
	 * keyParams: <sourceKeyName, appName>
	 */
	public void deleteSourceKeys(String sourceAppName, Set<String[]> keyParams) {
		if(keyParams == null)
			return;
		for(String[] array : keyParams) {
			deleteSourceKey(sourceAppName, array[0], array[1]);
		}
	}

	/**
	 * �ݹ�ɾ��appName���ӽڵ�
	 */
	@SuppressWarnings("unchecked")  
	private void deleteDepKey(String keyName, final Set<String> deleteKeyIdSet, final Map<String, Object[]> totalMap, final Map<String, CspTimeKeyDependInfo> curOptMap) {
		//��ȡkeyName������Ӧ����Ϣ
		Object[] array = totalMap.get(keyName);
		Integer dependMeTime = (Integer) array[0];
		if(dependMeTime > 1) {
			return; //���������ң���ɾ�������ӽڵ�Ĺ�ϵ
		}
		HashSet<String> depKeyNameSet = (HashSet<String>) array[1];
		for(String depKeyName: depKeyNameSet) {
			String id = curOptMap.get(keyName + "_" + depKeyName).getId() + "";
			deleteKeyIdSet.add(id);
			deleteDepKey(depKeyName, deleteKeyIdSet, totalMap, curOptMap);
		}
	}

	/**
	 * �����Ӳ�ѯ
	 * @param sourceAppName
	 * @param sourceKeyName
	 * @param keyName
	 * @return
	 */
	public List<CspTimeKeyDependInfo> getCspTimeKeyDependInfoSub(String sourceAppName, String sourceKeyName, String appName, String keyName){

		try {
			//��ѯʱ�� Time key -> Eagleeye key 
			String sourceKeyNameTmp = DependRelationAo.get().changeTimeToEagleeyeKey(sourceKeyName);
			String keyNameTmp = DependRelationAo.get().changeTimeToEagleeyeKey(keyName);

			List<CspCallsRelationship> targetKeyList = EagleeyeDataAo.get().getSubCallRelationShip(sourceKeyNameTmp, keyNameTmp);
			Map<String, CspTimeKeyDependInfo> mapAuto = new HashMap<String, CspTimeKeyDependInfo>();
			for(CspCallsRelationship ship : targetKeyList) {
				//���ؽ���ǣ� Eagleeye key -> Time key 
				String targetToTime = DependRelationAo.get().changeDependHSFProvideToTimeKey(ship.getTarget());
				String originToTime = DependRelationAo.get().changeDependHSFProvideToTimeKey(ship.getOrigin());
				if(!mapAuto.containsKey(targetToTime)) { //������
					CspTimeKeyDependInfo info = new CspTimeKeyDependInfo();
					info.setAppName(ship.getOriginApp());
					info.setDependAppName(ship.getTargetApp());
					info.setDependStatus(Constants.AUTO_NOTCONFIG + "");
					info.setId(-1);
					info.setSourceAppName(ship.getSourceApp());
					info.setSourceKeyName(ship.getSourceUrl());
					mapAuto.put(targetToTime, info);
					//Eagleeye��ʽ��:com.taobao.ratecenter.service.RateCenterReadService:1.0.0|queryMallRateList
					//ת��ΪTime����key��ʽ: HSF-provider`com.taobao.ratecenter.service.RateCenterReadService:1.0.0`queryMallRateList
					info.setKeyName(originToTime);
					info.setDependKeyName(targetToTime);
				}
			}
			Map<String, CspTimeKeyDependInfo> existMap = dependDao.getCspTimeKeyDependInfoSub(sourceAppName, sourceKeyName, keyName); //ʹ�����ǰ׺���key��ѯ
			List<CspTimeExtraAppKey> extraKeyList = dependDao.getCspTimeExtraAppKeyList();

			Iterator<CspTimeKeyDependInfo> iter = existMap.values().iterator();
			while(iter.hasNext()) {
				CspTimeKeyDependInfo po = iter.next();
				if("".equals(po.getDependKeyName())) {
					iter.remove();
					continue;
				}
				if(mapAuto.containsKey(po.getDependKeyName())) {
					mapAuto.remove(po.getDependKeyName());
					po.setDependStatus(Constants.AUTO_CONFIG + "");
				} else {
					po.setDependStatus(Constants.NOTAUTO_CONFIG + "");            
				}
			}

			List<CspTimeKeyDependInfo> list = new ArrayList<CspTimeKeyDependInfo>(existMap.values());

			for(CspTimeExtraAppKey info:extraKeyList) {
				try {
					if (existMap.get(info.getKeyName()) != null
							&& info.getExtraAppName().equals(
									existMap.get(info.getKeyName()).getDependAppName())) {
						//ͨ��appname �� key �ϸ��жϣ���ֹ����key��ͬ�����
					} else {
						CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
						po.setId(-1);
						po.setSourceAppName(sourceAppName);
						po.setSourceKeyName(sourceKeyName);
						po.setAppName(appName);
						po.setKeyName(keyName);
						po.setDependAppName(info.getExtraAppName());
						po.setDependKeyName(info.getKeyName());
						po.setDependStatus(Constants.EXTRA_CONFIG + "");
						list.add(po);
					}
				} catch (Exception e) {
					logger.error("CspTimeExtraAppKey תCspTimeKeyDependInfo ���� ",e);
				}
			}

			for(CspTimeKeyDependInfo info : mapAuto.values()) {
				list.add(info);
			}


			return list;
		} catch (Exception e) {
			logger.error("", e);
		}
		return new ArrayList<CspTimeKeyDependInfo>();    
	}


	public List<CspTimeKeyDependInfo> getCspTimeKeyDependInfo_Key(String sourceAppName, String sourceKeyName) {

		try {
			return dependDao.getCspTimeKeyDependInfo_Key(sourceAppName,sourceKeyName);
		} catch (Exception e) {
			return new ArrayList<CspTimeKeyDependInfo>();
		}
	} 

	/**
	 * �����ͨ��key�����ơ���ȡ������������key����Ϣ
	 *@author xiaodu
	 * @param keyName
	 * @return
	 * @throws Exception
	 *TODO
	 */
	public List<CspTimeKeyDependInfo> getKeyDependByKeyName(  String appName,String keyName) {
		return dependDao.getKeyDependByKeyName(  appName,keyName);
	}

	/**
	 * ͨ��key���ƣ���ѯ�����ж�Ӧ���key��ִ��·��
	 *@author xiaodu
	 * @param keyName
	 * @return
	 * @throws Exception
	 *TODO
	 */
	public Map<String,List<CspTimeKeyDependInfo>> getKeyDependTraceByKeyName(  String appName,String keyName) {
		return dependDao.getKeyDependTraceByKeyName(  appName,keyName);
	}

	/***
	 * ��ȡ���������uv
	 * @param domain
	 * @return
	 */
	public int getLatestUvByDomain(String domain) {
		return dependDao.getLatestUvByDomain(domain);
	}

	/***
	 * ����ǰn�ĵ��ýӿ�
	 * @param n
	 * @param appName
	 * @param date
	 * @return
	 */
	public List<ReportInvokeDataPo> getTopNOutInterface(int n, String appName, Date date) {
		return dependDao.getTopNOutInterface(n, appName, date);
	}

	/***
	 * ����ǰn���ṩ�ӿ�
	 * @param n
	 * @param appName
	 * @param date
	 * @return
	 */
	public List<ReportInvokeDataPo> getTopNInInterface(int n, String appName, Date date) {
		return dependDao.getTopNInInterface(n, appName, date);
	}

	/**
	 * ����keyName��ѯ�����е�URL����like��ѯ����ѯkeyname��dependkeyname
	 * @param keyName
	 * @return
	 */
	public List<CspTimeKeyDependInfo> getDistinctSourceUrlList(String keyName) {
		return dependDao.getDistinctSourceUrlList(keyName);
	}

	/***
	 * ǰnurl��Ϣ
	 * @param n
	 * @param appName
	 * @param date
	 * @return
	 */
	public List<ReportInvokeDataPo> getTopNUrls(int n, String appName, Date date) {
		return dependDao.getTopNUrls(n, appName, date);
	}

	/**
	 * ��ѯ�ȵ��key
	 * @param appArray
	 * @return
	 */
	public Map<String, List<CspAppHotInterface>> getCspAppHotInterfaceByAppName(String[] appArray) {
		return dependDao.getCspAppHotInterfaceByAppName(appArray);
	}

	/**
	 * ֻ���������ȵ�ӿڵ����νṹ�����������
	 * @param appArray
	 * @return
	 */
	public List<TreeGridData> getHotInterfaceGridData(String[] appArray) {
		Map<String, List<CspAppHotInterface>> map = dependDao.getCspAppHotInterfaceByAppName(appArray);
		List<TreeGridData> rootList = new ArrayList<TreeGridData>();
		int iCounterMain = 0;
		for(Entry<String, List<CspAppHotInterface>> entry : map.entrySet()) {
			List<CspAppHotInterface> childList = entry.getValue();
			TreeGridData root = new TreeGridData();
			root.setAppName(entry.getKey());
			root.setKeyName(entry.getKey());
			root.setId(iCounterMain++);
			TreeGridBasePo[] childArray = new TreeGridBasePo[childList.size()];
			root.setChildren(childArray);

			int i=0;
			for(CspAppHotInterface hotInterface: childList) {
				childArray[i] = new TreeGridBasePo();
				childArray[i].setAppName(hotInterface.getAppName());
				childArray[i].setKeyName(hotInterface.getKeyName());
				childArray[i].setId(iCounterMain ++);
				i++;
			}
			rootList.add(root);
		}
		return rootList;
	}

	public boolean insertAppRelToDependInfo(CspAppDepAppPo po) {
		try {
			dependDao.insertAppRelToDependInfo(po);
		} catch (Exception e) {
			logger.error("",e);
			return false;
		}
		return true;
	}

	public boolean insertKeyRelToDependInfo(String curKeyname,String curApp, String parentKeyname,String parentApp, String url) {
		try {
			dependDao.insertKeyRelToDependInfo(curKeyname, curApp, parentKeyname, parentApp, url);
		} catch (Exception e) {
			logger.error("",e);
			return false;
		}
		return true;
	}

	public void deleteKeyByUrl_CspKeyDependInfo(String url){
		dependDao.deleteKeyByUrl_CspKeyDependInfo(url);
	}

	public void deleteAppByUrl_CspKeyDependInfo(String url){
		dependDao.deleteAppByUrl_CspKeyDependInfo(url);
	}

	public void clearApp_CspKeyDependInfo() {
		dependDao.clearApp_CspKeyDependInfo();
	}

	/**************����ϵͳ�������ݣ���ϵ�ˣ��淽��������Դ����ݼ��*********************************************************************************/
	public void addReleaseInfoToDb(ReleaseInfo info) throws SQLException {
		dependDao.addReleaseInfoToDb(info);
	}

	public List<ReleaseInfo> getReleaseInfoList(String appName, String dateBegin, String endDate) throws Exception {
		List<ReleaseInfo> releaseList = dependDao.getReleaseInfoList(appName, dateBegin, endDate);
		return releaseList;
	}

	public ReleaseInfo getReleaseInfoLatest(String appName, String dateBegin, String endDate, String pubLevel) throws Exception {
		List<ReleaseInfo> releaseList = dependDao.getReleaseInfoList(appName, dateBegin, endDate);
		for(ReleaseInfo info : releaseList) {
			if(info.getPubLevel().equals(pubLevel)) {
				return info;
			}
		}
		return null;
	}
	
	public List<ReleaseInfo> getReleaseInfoList(String appName, String dateBegin, String endDate, String pubLevel) throws Exception {
		List<ReleaseInfo> releaseList = dependDao.getReleaseInfoList(appName, dateBegin, endDate);
		Iterator<ReleaseInfo> iter = releaseList.iterator();
		while(iter.hasNext()) {
			ReleaseInfo info = iter.next();
			if(!info.getPubLevel().equals(pubLevel)) {
				iter.remove();
			}
		}
		return releaseList;
	}
	
	public Map<String, List<ReleaseInfo>> getReleaseInfoMap(final Set<String> appSet, String dateBegin, String endDate) throws Exception {
		if(appSet == null || appSet.size() == 0)
			throw new Exception("appSet is blank");
		return dependDao.getReleaseInfoMap(appSet, dateBegin, endDate);
	}

	public Map<String, ReleaseInfo> getReleaseInfoMap(final Set<String> appSet, String dateBegin, String endDate, String pubLevel) throws Exception {
		Map<String, List<ReleaseInfo>> map = getReleaseInfoMap(appSet, dateBegin, endDate);
		Map<String, ReleaseInfo> realMap = new HashMap<String, ReleaseInfo>();
		for(Entry<String, List<ReleaseInfo>> entry : map.entrySet()) {
			List<ReleaseInfo> list = entry.getValue();
			for(ReleaseInfo info : list) {
				if(info.getPubLevel().equals(pubLevel)) {
					realMap.put(entry.getKey(), info);
					break;
				}
			}
		}
		return realMap;
	}

	public void sortReleaseInfoList(List<ReleaseInfo> releaseList) {
		Collections.sort(releaseList, new Comparator<ReleaseInfo>(){
			public int compare(ReleaseInfo o1, ReleaseInfo o2) {
				String finishTime = o1.getFinishTime();
				String finishTime2 = o2.getFinishTime();
				if(StringUtils.isBlank(finishTime))
					return 1;
				if(StringUtils.isBlank(finishTime2))
					return -1;
				return -finishTime.compareTo(finishTime2);
			}
		} ); 
	}

	public static void main(String[] args) throws Exception {
		ReleaseInfo po = new ReleaseInfo();
		po.setAppName("detail");
		po.setCallSystem("callsystem");
		po.setCreator("creator");
		po.setFinishTime("2013-04-14 23:55");
		po.setNotifyType("sdf");
		po.setPlanId("222");
		po.setPlanKind("mmm");
		po.setPlanTime("2013-04-14 23:55");
		po.setPubLevel("ddd");
		po.setPubType("sss");
		po.setReleaseId("sdfsdf");
		po.setResult("dddd");
		po.setSign("sign");

		CspDependInfoAo.get().addReleaseInfoToDb(po);
		List<ReleaseInfo> list = CspDependInfoAo.get().getReleaseInfoList("detail", "2013-04-14 23:45", "2013-04-14 23:55");
		System.out.println(list);
	}
}
