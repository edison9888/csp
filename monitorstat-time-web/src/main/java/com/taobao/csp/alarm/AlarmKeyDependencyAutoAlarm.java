package com.taobao.csp.alarm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.dataserver.Constants;
import com.taobao.csp.dataserver.KeyConstants;
import com.taobao.monitor.common.ao.center.AppInfoAo;
import com.taobao.monitor.common.ao.center.KeyAo;
import com.taobao.monitor.common.db.impl.center.CspDependInfoDao;
import com.taobao.monitor.common.po.CspAppKeyRelation;
import com.taobao.monitor.common.po.CspKeyDependInfoPo;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspKeyMode;
import com.taobao.monitor.common.po.CspTimeKeyDependInfo;

public class AlarmKeyDependencyAutoAlarm {
	private static Logger logger = Logger.getLogger(AlarmKeyDependencyAutoAlarm.class);
	private List<CspAppKeyRelation> appKeyRelation = KeyAo.get().findAllAppKeyRelation();
	private static AlarmKeyDependencyAutoAlarm single = new AlarmKeyDependencyAutoAlarm();
	private List<CspAppKeyRelation> findAppByKeyId(Integer keyId){
		List<CspAppKeyRelation> ret = new ArrayList<CspAppKeyRelation>();
		for(CspAppKeyRelation cap : appKeyRelation){
			if(cap.getKeyId()==keyId){
				ret.add(cap);
			}
		}
		return ret;
	}
	private AlarmKeyDependencyAutoAlarm(){
	}
	public static AlarmKeyDependencyAutoAlarm get(){
		return single;
	}
	private   void runiter(String sourceAppName,String sourceKeyName,String appName,String keyName,Integer deep){
		if(deep>2)return;
		CspDependInfoDao dao = new CspDependInfoDao();
		try {
			String realKeyName = "";
			if(keyName.matches("^HSF(.)*")){
				String keys[] = keyName.split(Constants.S_SEPERATOR);
				if(keys.length!=4)return;
				realKeyName = keys[2]+Constants.S_SEPERATOR+keys[3];
			}else if(keyName.matches("^PV(.)*")){
				String keys[] = keyName.split(Constants.S_SEPERATOR);
				if(keys.length!=2)return;
				realKeyName = keys[1];
			}
			Map<String, CspKeyDependInfoPo>  map = dao.getCspKeyDependInfoAuto(realKeyName,"KEY");
			for(Map.Entry<String, CspKeyDependInfoPo> entry : map.entrySet()){
				try {
					if(entry.getKey().matches("^com.(.)*")){
						String hsfproviderkey = KeyConstants.HSF_PROVIDER+Constants.S_SEPERATOR+entry.getKey();
						List<CspKeyInfo> keyList = KeyAo.get().findKeyLikeName(hsfproviderkey);
						if(keyList.size()>0){
							Integer keyId = keyList.get(0).getKeyId();
							List<CspAppKeyRelation> appList = findAppByKeyId(keyId);
							String appNametmp = AppInfoAo.get().findAppInfoById(appList.get(0).getAppId()).getAppName();
							CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
					        po.setSourceAppName(sourceAppName);
					        po.setSourceKeyName(sourceKeyName);
					        po.setAppName(appName);
					        po.setKeyName(keyName); 
					        po.setDependAppName(appNametmp);
					        po.setDependKeyName(hsfproviderkey);
					        po.setDependStatus(4+ "");
					        List<CspTimeKeyDependInfo> tmplist = new ArrayList<CspTimeKeyDependInfo>();
					        tmplist.add(po);
					        CspDependInfoDao dependDao = new CspDependInfoDao();
					        dependDao.insertCspTimeKeyDependInfo(tmplist);
					        runiter(sourceAppName,sourceKeyName,appNametmp,hsfproviderkey,++deep);
						}
					}else{
						String pvrefer = KeyConstants.PV_REFER+Constants.S_SEPERATOR+entry.getKey();
						List<CspKeyInfo> keyList = KeyAo.get().findKeyLikeName(pvrefer);
						if(keyList.size()>0){
							Integer keyId = keyList.get(0).getKeyId();
							List<CspAppKeyRelation> appList = findAppByKeyId(keyId);
							String appNametmp = AppInfoAo.get().findAppInfoById(appList.get(0).getAppId()).getAppName();
							CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
					        po.setSourceAppName(sourceAppName);
					        po.setSourceKeyName(sourceKeyName);
					        po.setAppName(appName);
					        po.setKeyName(keyName); 
					        po.setDependAppName(appNametmp);
					        po.setDependKeyName(pvrefer);
					        po.setDependStatus(4+ "");
					        List<CspTimeKeyDependInfo> tmplist = new ArrayList<CspTimeKeyDependInfo>();
					        tmplist.add(po);
					        CspDependInfoDao dependDao = new CspDependInfoDao();
					        dependDao.insertCspTimeKeyDependInfo(tmplist);
					        runiter(sourceAppName,sourceKeyName,appNametmp,pvrefer,++deep);
						}
					}
				} catch (Exception e) {
					logger.info(e);
				}
			}
		} catch (Exception e) {
			logger.info(e);
		}
	}
	public  void run(){
		String[] apps = {"login","shopsystem","itemcenter","tf_tm","tradeplatform","detail","hesper","tf_buy","shopcenter","cart","uicfinal","ump"};
		for(String app : apps){
			List<CspKeyMode> list = KeyAo.get().getKeyModeByAppName(app);
			if(list==null)continue;
			for(CspKeyMode ckm : list){
				if(ckm.getKeyName()==null)continue;
				
				if(ckm.getKeyName().matches("^PV-refer`(.)*")){
					String url= ckm.getKeyName().substring(ckm.getKeyName().lastIndexOf(Constants.S_SEPERATOR)+1);
					CspDependInfoDao dao = new CspDependInfoDao();
					try {
						Map<String, CspKeyDependInfoPo>  map = dao.getCspKeyDependInfoAuto(url,"KEY");
						for(Map.Entry<String, CspKeyDependInfoPo> entry : map.entrySet()){
							try {
								if(entry.getKey().matches("^com.(.)*")){
									String hsfproviderkey = KeyConstants.HSF_PROVIDER+Constants.S_SEPERATOR+entry.getKey();
									List<CspKeyInfo> keyList = KeyAo.get().findKeyLikeName(hsfproviderkey);
									if(keyList.size()>0){
										Integer keyId = keyList.get(0).getKeyId();
										List<CspAppKeyRelation> appList = findAppByKeyId(keyId);
										String appName = AppInfoAo.get().findAppInfoById(appList.get(0).getAppId()).getAppName();
										CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
								        po.setSourceAppName(ckm.getAppName());
								        po.setSourceKeyName(ckm.getKeyName());
								        po.setAppName(ckm.getAppName());
								        po.setKeyName(ckm.getKeyName()); 
								        po.setDependAppName(appName);
								        po.setDependKeyName(hsfproviderkey);
								        po.setDependStatus(4+ "");
								        List<CspTimeKeyDependInfo> tmplist = new ArrayList<CspTimeKeyDependInfo>();
								        tmplist.add(po);
								        CspDependInfoDao dependDao = new CspDependInfoDao();
								        dependDao.insertCspTimeKeyDependInfo(tmplist);
								        runiter(ckm.getAppName(),ckm.getKeyName(),appName,entry.getKey(),1);
									}
								}
							} catch (Exception e) {
								logger.info(e);
							}
						}
					} catch (Exception e) {
						logger.info(e);
					}
				}
				if(ckm.getKeyName().matches("^HSF-Consumer`(.)*")){
					String keys[] = ckm.getKeyName().split(Constants.S_SEPERATOR);
					if(keys.length==4){
						String hsfkey = keys[2]+Constants.S_SEPERATOR+keys[3];
						CspDependInfoDao dao = new CspDependInfoDao();
						try {
							Map<String, CspKeyDependInfoPo>  map = dao.getCspKeyDependInfoAuto(hsfkey,"KEY");
							for(Map.Entry<String, CspKeyDependInfoPo> entry : map.entrySet()){
								try {
									if(entry.getKey().matches("^com.(.)*")){
										String hsfproviderkey = KeyConstants.HSF_PROVIDER+Constants.S_SEPERATOR+entry.getKey();
										List<CspKeyInfo> keyList = KeyAo.get().findKeyLikeName(hsfproviderkey);
										if(keyList.size()>0){
											Integer keyId = keyList.get(0).getKeyId();
											List<CspAppKeyRelation> appList = findAppByKeyId(keyId);
											String appName = AppInfoAo.get().findAppInfoById(appList.get(0).getAppId()).getAppName();
											CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
									        po.setSourceAppName(ckm.getAppName());
									        po.setSourceKeyName(ckm.getKeyName());
									        po.setAppName(ckm.getAppName());
									        po.setKeyName(ckm.getKeyName()); 
									        po.setDependAppName(appName);
									        po.setDependKeyName(hsfproviderkey);
									        po.setDependStatus(4+ "");
									        List<CspTimeKeyDependInfo> tmplist = new ArrayList<CspTimeKeyDependInfo>();
									        tmplist.add(po);
									        CspDependInfoDao dependDao = new CspDependInfoDao();
									        dependDao.insertCspTimeKeyDependInfo(tmplist);
									        runiter(ckm.getAppName(),ckm.getKeyName(),appName,entry.getKey(),1);
										}
									}
								} catch (Exception e) {
									logger.info(e);
								}
							}
						} catch (Exception e) {
							logger.info(e);
						}
					}
				}
				
			}
		}
	}
}
