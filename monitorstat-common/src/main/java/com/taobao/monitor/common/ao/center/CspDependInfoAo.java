

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
	 * 根据时间查询，返回表csp_app_depend_app的list,保证不为null
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
	 * 返回 appname, id格式的map
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
	 * 返回 id，appname格式的map
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
	 * 返回自动生成的我依赖的信息
	 */
	public Map<String, CspTimeAppDependInfo> getMeDependMap_New(String sourceAppName,String appName) {
		try {
			//返回自动生成的App依赖信息
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

			//手动配置的信息
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

			//把自动生成的Po转换为我们需要的Po
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
	 * 保存应用配置
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
				//objArray[1] = set;  //重新复制回去

				//处理被依赖方
				Object[] objDepArray = totalMap.get(depAppNameLocal);
				if(objDepArray == null) {
					objDepArray = new Object[2];
					objDepArray[0] = new Integer(0);
					objDepArray[1] = new HashSet<String>(); 
					totalMap.put(info.getDepAppName(), objDepArray);
				}
				//被调用次数+1
				objDepArray[0] = (Integer)objDepArray[0] + 1;
			}

			Set<String> deleteAppIdSet = new HashSet<String>();

			//获取当前保存的App的相关信息
			Object[] array = totalMap.get(appName);
			if(array != null) {
				HashSet<String> depAppNameSet = (HashSet<String>) array[1];
				Iterator<String> iterator = depAppNameSet.iterator();
				while(iterator.hasNext()) {
					String depAppName = iterator.next();
					String id = curOptMap.get(appName + "_" + depAppName).getId() + "";
					if(!saveExistIdSet.contains(id)) {
						deleteAppIdSet.add(id);
						deleteDepApp(depAppName, deleteAppIdSet, totalMap, curOptMap);  //递归删除
						//iterator.remove();
					}
				}
			}
			//处理插入部分的数据
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

			//插入手动配置的部分,配置的应用不会太多，一次性都查出来。FIXME if you need
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

			//入库操作
			dependDao.deleteCspTimeAppDependInfo(deleteAppIdSet.toArray(new String[0]));
			dependDao.insertCspTimeAppDependInfo(insertList);
		} catch (Exception e) {
			logger.error("", e);
		}
	}

	/**
	 * 返回sourceapp的一条业务链
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
	 * 递归删除appName的子节点
	 */
	@SuppressWarnings("unchecked")  
	private void deleteDepApp(String appName, final Set<String> deleteAppIdSet, final Map<String, Object[]> totalMap, final Map<String, CspTimeAppDependInfo> curOptMap) {
		//获取appName依赖的应用信息
		Object[] array = totalMap.get(appName);
		Integer dependMeTime = (Integer) array[0];
		if(dependMeTime > 1) {
			return; //有人依赖我，不删除我与子节点的关系
		}
		HashSet<String> depAppNameSet = (HashSet<String>) array[1];
		for(String depAppName: depAppNameSet) {
			String id = curOptMap.get(appName + "_" + depAppName).getId() + "";
			deleteAppIdSet.add(id);
			deleteDepApp(depAppName, deleteAppIdSet, totalMap, curOptMap);
		}
	}

	/**
	 * 返回sourceapp下面的depth级依赖
	 * @return
	 */
	public List<CspTimeAppDependInfo> getCspTimeAppDependInfoBySourceNameByDepth (String sourceAppName, int depth) {
		try {
			List<CspTimeAppDependInfo> totalList = dependDao
					.getCspTimeAppDependInfo(sourceAppName);
			//FIXME 现在写死depth为1，后面修改
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
	 * 保存应用配置,key所属的App相关在查询的时候直接取得到，这里不需要重复查询一遍
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

				//处理被依赖方
				Object[] objDepArray = totalMap.get(depKeyNameLocal);
				if(objDepArray == null) {
					objDepArray = new Object[2];
					objDepArray[0] = new Integer(0);
					objDepArray[1] = new HashSet<String>(); 
					totalMap.put(info.getDependKeyName(), objDepArray);
				}
				//被调用次数+1
				objDepArray[0] = (Integer)objDepArray[0] + 1;
			}

			Set<String> deleteKeyIdSet = new HashSet<String>();

			//获取当前保存的App的相关信息
			Object[] array = totalMap.get(keyName);
			if(array != null) {
				HashSet<String> depKeyNameSet = (HashSet<String>) array[1];
				Iterator<String> iterator = depKeyNameSet.iterator();
				while(iterator.hasNext()) {
					String depKeyName = iterator.next();
					String id = curOptMap.get(keyName + "_" + depKeyName).getId() + "";
					if(!saveExistIdSet.contains(id)) {
						deleteKeyIdSet.add(id);
						deleteDepKey(depKeyName, deleteKeyIdSet, totalMap, curOptMap);  //递归删除
						//iterator.remove(); //remove or not is all ok
					}
				}
			}

			//处理插入部分的数据
			List<CspTimeKeyDependInfo> insertList = new ArrayList<CspTimeKeyDependInfo>();
			List<CspKeyDependInfoPo> autoList = dependDao.getDependMapByIds_Auto(saveAutoMap.keySet().toArray(new String[0]));
			for(CspKeyDependInfoPo autoPo : autoList) { //把自动生成的PC转换为我们需要的Po
				CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
				po.setSourceAppName(sourceAppName);
				po.setSourceKeyName(sourceKeyName);
				po.setAppName(appName);
				po.setKeyName(keyName); 
				po.setDependAppName(saveAutoMap.get(autoPo.getId() + ""));
				po.setDependStatus(Constants.AUTO_CONFIG + "");

				//根据key类型不同，拼接成不同格式的字符串
				if(autoPo.getUrl() != null && !autoPo.getUrl().equals(autoPo.getCurKeyame() + "_")) {
					po.setDependKeyName("HSF-provider`" + autoPo.getCurKeyame());
				} else {
					po.setDependKeyName("PV`" + autoPo.getCurKeyame());
				}                
				insertList.add(po);
			}

			//处理手动添加的部分
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

			//入库操作
			dependDao.deleteCspTimeKeyDependInfo(deleteKeyIdSet.toArray(new String[0]));
			dependDao.insertCspTimeKeyDependInfo(insertList);

			//防止出现sourcekey被删除的bug
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
	 * 由两个部分组成，应用对应的URL和Key（HSF-provider`）
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
			//非HSF-provider`的HSF信息全部过滤掉。结合eagleeye的特性，只显示provider的信息      
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
	 * 递归删除appName的子节点
	 */
	@SuppressWarnings("unchecked")  
	private void deleteDepKey(String keyName, final Set<String> deleteKeyIdSet, final Map<String, Object[]> totalMap, final Map<String, CspTimeKeyDependInfo> curOptMap) {
		//获取keyName依赖的应用信息
		Object[] array = totalMap.get(keyName);
		Integer dependMeTime = (Integer) array[0];
		if(dependMeTime > 1) {
			return; //有人依赖我，不删除我与子节点的关系
		}
		HashSet<String> depKeyNameSet = (HashSet<String>) array[1];
		for(String depKeyName: depKeyNameSet) {
			String id = curOptMap.get(keyName + "_" + depKeyName).getId() + "";
			deleteKeyIdSet.add(id);
			deleteDepKey(depKeyName, deleteKeyIdSet, totalMap, curOptMap);
		}
	}

	/**
	 * 返回子查询
	 * @param sourceAppName
	 * @param sourceKeyName
	 * @param keyName
	 * @return
	 */
	public List<CspTimeKeyDependInfo> getCspTimeKeyDependInfoSub(String sourceAppName, String sourceKeyName, String appName, String keyName){

		try {
			//查询时， Time key -> Eagleeye key 
			String sourceKeyNameTmp = DependRelationAo.get().changeTimeToEagleeyeKey(sourceKeyName);
			String keyNameTmp = DependRelationAo.get().changeTimeToEagleeyeKey(keyName);

			List<CspCallsRelationship> targetKeyList = EagleeyeDataAo.get().getSubCallRelationShip(sourceKeyNameTmp, keyNameTmp);
			Map<String, CspTimeKeyDependInfo> mapAuto = new HashMap<String, CspTimeKeyDependInfo>();
			for(CspCallsRelationship ship : targetKeyList) {
				//返回结果是， Eagleeye key -> Time key 
				String targetToTime = DependRelationAo.get().changeDependHSFProvideToTimeKey(ship.getTarget());
				String originToTime = DependRelationAo.get().changeDependHSFProvideToTimeKey(ship.getOrigin());
				if(!mapAuto.containsKey(targetToTime)) { //我依赖
					CspTimeKeyDependInfo info = new CspTimeKeyDependInfo();
					info.setAppName(ship.getOriginApp());
					info.setDependAppName(ship.getTargetApp());
					info.setDependStatus(Constants.AUTO_NOTCONFIG + "");
					info.setId(-1);
					info.setSourceAppName(ship.getSourceApp());
					info.setSourceKeyName(ship.getSourceUrl());
					mapAuto.put(targetToTime, info);
					//Eagleeye格式如:com.taobao.ratecenter.service.RateCenterReadService:1.0.0|queryMallRateList
					//转换为Time那种key格式: HSF-provider`com.taobao.ratecenter.service.RateCenterReadService:1.0.0`queryMallRateList
					info.setKeyName(originToTime);
					info.setDependKeyName(targetToTime);
				}
			}
			Map<String, CspTimeKeyDependInfo> existMap = dependDao.getCspTimeKeyDependInfoSub(sourceAppName, sourceKeyName, keyName); //使用添加前缀后的key查询
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
						//通过appname 和 key 严格判断，防止出现key相同的情况
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
					logger.error("CspTimeExtraAppKey 转CspTimeKeyDependInfo 出错 ",e);
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
	 * 这个是通过key的名称。获取所有它依赖的key的信息
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
	 * 通过key名称，查询出所有对应这个key的执行路径
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
	 * 获取域名最近的uv
	 * @param domain
	 * @return
	 */
	public int getLatestUvByDomain(String domain) {
		return dependDao.getLatestUvByDomain(domain);
	}

	/***
	 * 查找前n的调用接口
	 * @param n
	 * @param appName
	 * @param date
	 * @return
	 */
	public List<ReportInvokeDataPo> getTopNOutInterface(int n, String appName, Date date) {
		return dependDao.getTopNOutInterface(n, appName, date);
	}

	/***
	 * 查找前n的提供接口
	 * @param n
	 * @param appName
	 * @param date
	 * @return
	 */
	public List<ReportInvokeDataPo> getTopNInInterface(int n, String appName, Date date) {
		return dependDao.getTopNInInterface(n, appName, date);
	}

	/**
	 * 根据keyName查询出所有的URL，非like查询，查询keyname和dependkeyname
	 * @param keyName
	 * @return
	 */
	public List<CspTimeKeyDependInfo> getDistinctSourceUrlList(String keyName) {
		return dependDao.getDistinctSourceUrlList(keyName);
	}

	/***
	 * 前nurl信息
	 * @param n
	 * @param appName
	 * @param date
	 * @return
	 */
	public List<ReportInvokeDataPo> getTopNUrls(int n, String appName, Date date) {
		return dependDao.getTopNUrls(n, appName, date);
	}

	/**
	 * 查询热点的key
	 * @param appArray
	 * @return
	 */
	public Map<String, List<CspAppHotInterface>> getCspAppHotInterfaceByAppName(String[] appArray) {
		return dependDao.getCspAppHotInterfaceByAppName(appArray);
	}

	/**
	 * 只负责生成热点接口的树形结构，不添加数据
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

	/**************发布系统推送数据，联系人：骆方，需求来源：少菁。*********************************************************************************/
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
