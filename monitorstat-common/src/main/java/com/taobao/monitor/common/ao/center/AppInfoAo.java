package com.taobao.monitor.common.ao.center;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.alibaba.common.lang.StringUtil;
import com.taobao.monitor.common.db.impl.center.AppInfoDao;
import com.taobao.monitor.common.db.impl.center.DataBaseAppRelDao;
import com.taobao.monitor.common.db.impl.center.HostDao;
import com.taobao.monitor.common.db.impl.center.ServerAppRelDao;
import com.taobao.monitor.common.po.AppDescPo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.AppKeyRelation;
import com.taobao.monitor.common.po.AppUrlRelation;
import com.taobao.monitor.common.po.CspAppRtCount;
import com.taobao.monitor.common.po.DatabaseAppRelPo;
import com.taobao.monitor.common.po.ServerAppRelPo;
import com.taobao.monitor.common.util.Constants;

/**
 * Ӧ�õ�DAO
 * @author wuhaiqian.pt
 *
 */
public class AppInfoAo  {
	private static final Logger logger = Logger.getLogger(AppInfoAo.class);
	private static AppInfoAo  ao = new AppInfoAo();
	private AppInfoDao dao = new AppInfoDao();
	private ServerAppRelDao serverAppReldao = new ServerAppRelDao();
	private DataBaseAppRelDao databaseAppReldao = new DataBaseAppRelDao();
	private HostDao	hostDao =new HostDao();
	private List<AppInfoPo> appInfolist = new ArrayList<AppInfoPo>();
	private Map<Integer,String> appInfoId2NameMap = new HashMap<Integer,String>();
	private Map<String,Integer> appInfoName2IdMap = new HashMap<String,Integer>();
	private AtomicBoolean initDone = new AtomicBoolean(false);
	
	private static HashSet<String> allAppNameList  = new HashSet<String>();
	private static HashSet<String> tradeRelateAppNameList  = new HashSet<String>();
	private static HashSet<String> tradeplatformRelateAppNameList  = new HashSet<String>();
	private static HashSet<String> cartRelateAppNameList  = new HashSet<String>();
	private static HashSet<String> tfBuyRelateAppNameList  = new HashSet<String>();
	private static HashSet<String> tfTmRelateAppNameList  = new HashSet<String>();
	private static HashSet<String> tradeapiRelateAppNameList  = new HashSet<String>();
    private static HashSet<String> auctionplatformRelateAppNameList = new HashSet<String>();

	private static HashMap<String,Integer> tradeRelateAppMap = new  HashMap<String,Integer>();
	private static HashMap<String,Integer> tradeplatformRelateAppMap = new  HashMap<String,Integer>();
	private static HashMap<String,Integer> cartRelateAppMap = new  HashMap<String,Integer>();
	private static HashMap<String,Integer> tfBuyRelateAppMap = new  HashMap<String,Integer>();
	private static HashMap<String,Integer> tfTmRelateAppMap = new  HashMap<String,Integer>();
	private static HashMap<String,Integer> tradeapiRelateAppMap = new  HashMap<String,Integer>();
	private static HashMap<String, Integer> auctionplatformRelateAppMap = new HashMap<String, Integer>();
	
	private static List<String> jmxAppNameList = new ArrayList<String>();
	private static HashMap<String,Integer> jmxAppNameMap = new HashMap<String,Integer>();
	
	private AppInfoAo(){
		
	}
	static{
		tradeRelateAppNameList.add("tradeplatform");
		tradeRelateAppNameList.add("tf_buy");
		tradeRelateAppNameList.add("tf_tm");
		tradeRelateAppNameList.add("diamond");
		tradeRelateAppNameList.add("uicfinal");
		tradeRelateAppNameList.add("session-tair");
		tradeRelateAppNameList.add("group3-tair-cm3");
		tradeRelateAppNameList.add("group3-tair-cm4");
		tradeRelateAppNameList.add("configserver");
		tradeRelateAppNameList.add("cart");
		//��ҳ��notify����tp��ص�����notify
		tradeRelateAppNameList.add("trade_notify");
		tradeRelateAppNameList.add("alipay_notify");
		tradeRelateAppNameList.add("trade_sub_notify");
		tradeRelateAppNameList.add("trade_notifyqd");
		
		
		cartRelateAppNameList.add("tradeplatform");
		cartRelateAppNameList.add("diamond");
		cartRelateAppNameList.add("group3-tair-cm3");
		cartRelateAppNameList.add("group3-tair-cm4");
		cartRelateAppNameList.add("configserver");
		cartRelateAppNameList.add("cart");
		cartRelateAppNameList.add("itemcenter");
		cartRelateAppNameList.add("shopcenter");
		cartRelateAppNameList.add("ump");
		cartRelateAppNameList.add("item-tair");
		
		tfBuyRelateAppNameList.add("tradeplatform");
		tfBuyRelateAppNameList.add("group3-tair-cm3");
		tfBuyRelateAppNameList.add("group3-tair-cm4");
		tfBuyRelateAppNameList.add("itemcenter");
		tfBuyRelateAppNameList.add("uicfinal");
		tfBuyRelateAppNameList.add("ump");
		tfBuyRelateAppNameList.add("misccenter");
		tfBuyRelateAppNameList.add("logisticscenter");
		tfBuyRelateAppNameList.add("delivery");
		tfBuyRelateAppNameList.add("tf_buy");
		
		tfTmRelateAppNameList.add("tradeplatform");
		tfTmRelateAppNameList.add("group3-tair-cm3");
		tfTmRelateAppNameList.add("group3-tair-cm4");
		tfTmRelateAppNameList.add("itemcenter");
		tfTmRelateAppNameList.add("tf_tm");
		tfTmRelateAppNameList.add("uicfinal");
		tfTmRelateAppNameList.add("uiclogin");
		tfTmRelateAppNameList.add("communityuis");
		tfTmRelateAppNameList.add("wwposthouse");
		tfTmRelateAppNameList.add("misccenter");
		tfTmRelateAppNameList.add("htm");
		tfTmRelateAppNameList.add("shopcenter");
		
		tradeapiRelateAppNameList.add("tradeplatform");
		tradeapiRelateAppNameList.add("timeoutcenter");
		tradeapiRelateAppNameList.add("diamond");
		tradeapiRelateAppNameList.add("configserver");
		tradeapiRelateAppNameList.add("group3-tair-cm3");
		tradeapiRelateAppNameList.add("group3-tair-cm4");
		tradeapiRelateAppNameList.add("uicfinal");
		tradeapiRelateAppNameList.add("itemcenter");
		tradeapiRelateAppNameList.add("shopcenter");
		tradeapiRelateAppNameList.add("item-tair");
		
		tradeplatformRelateAppNameList.add("tradeplatform");
		tradeplatformRelateAppNameList.add("group3-tair-cm3");
		tradeplatformRelateAppNameList.add("group3-tair-cm4");
		tradeplatformRelateAppNameList.add("itemcenter");
		tradeplatformRelateAppNameList.add("uicfinal");
		tradeplatformRelateAppNameList.add("ump");
		tradeplatformRelateAppNameList.add("misccenter");
		tradeplatformRelateAppNameList.add("logisticscenter");
		tradeplatformRelateAppNameList.add("delivery");
		tradeplatformRelateAppNameList.add("messengecenter");
		tradeplatformRelateAppNameList.add("shopcenter");
		tradeplatformRelateAppNameList.add("timeoutcenter");
		tradeplatformRelateAppNameList.add("pointcenter");
		tradeplatformRelateAppNameList.add("tradelogs");
		//��ҳ��notify����tp��ص�����notify
		tradeplatformRelateAppNameList.add("trade_notify");
		tradeplatformRelateAppNameList.add("alipay_notify");
		tradeplatformRelateAppNameList.add("trade_sub_notify");
		tradeplatformRelateAppNameList.add("trade_notifyqd");


        //TODO  �ư�
        auctionplatformRelateAppNameList.add("tradeplatform");
        auctionplatformRelateAppNameList.add("misccenter");
        auctionplatformRelateAppNameList.add("shopcenter");
        auctionplatformRelateAppNameList.add("itemcenter");
        auctionplatformRelateAppNameList.add("auctionplatform");
        auctionplatformRelateAppNameList.add("uicfinal");
        auctionplatformRelateAppNameList.add("group3_tair");
        auctionplatformRelateAppNameList.add("tbsession_tair");
        auctionplatformRelateAppNameList.add("punishcenter");   //govauction


		allAppNameList.addAll(tradeRelateAppNameList);
		allAppNameList.addAll(tradeplatformRelateAppNameList);
		allAppNameList.addAll(tfTmRelateAppNameList);
		allAppNameList.addAll(tfBuyRelateAppNameList);
		allAppNameList.addAll(cartRelateAppNameList);
        allAppNameList.addAll(auctionplatformRelateAppNameList);

		jmxAppNameList.add("notify");
		jmxAppNameList.add("diamond");
	}

	
	public  HashMap<String, Integer> getTradeRelateAppMap() {
		if(!initDone.get()) { 
			initAppInfo();
		}
		return tradeRelateAppMap;
	}

	public  HashMap<String, Integer> getTradeplatformRelateAppMap() {
		if(!initDone.get()) { 
			initAppInfo();
		}
		return tradeplatformRelateAppMap;
	}

	public  HashMap<String, Integer> getCartRelateAppMap() {
		if(!initDone.get()) { 
			initAppInfo();
		}
		return cartRelateAppMap;
	}

	public  HashMap<String, Integer> getTfBuyRelateAppMap() {
		if(!initDone.get()) { 
			initAppInfo();
		}
		return tfBuyRelateAppMap;
	}

	public  HashMap<String, Integer> getTfTmRelateAppMap() {
		if(!initDone.get()) { 
			initAppInfo();
		}
		return tfTmRelateAppMap;
	}
	
	public  HashMap<String, Integer> getTradeapiRelateAppMap() {
		if(!initDone.get()) { 
			initAppInfo();
		}
		return tradeapiRelateAppMap;
	}

	public HashMap<String,Integer> getJmxAppMap(){
		if(!initDone.get()){
			initAppInfo();
		}
		setJmxAppMap();
		return jmxAppNameMap;
	}

    public HashMap<String, Integer> getAuctionplatformRelateAppMap() {
        if(!initDone.get()) {
            initAppInfo();
        }
        return auctionplatformRelateAppMap;
    }
	
	private void setJmxAppMap(){
		for(String appName:jmxAppNameList){
			if(appInfoName2IdMap.get(appName) != null){
				jmxAppNameMap.put(appName, appInfoName2IdMap.get(appName));
			}
		}
	}
	
	
	public static  AppInfoAo get(){
		return ao;
	}
	
	
	public AppInfoPo getAppInfoByOpsName(String opsName){
		
		return dao.getAppInfoPoByOpsName(opsName);
		
	}
	
	public AppInfoPo getDayAppByOpsName(String opsName){
		
		return dao.getDayAppByOpsName(opsName);
		
	}
	
	public AppInfoPo getAppInfoByAppName(String appName){
		
		return dao.getAppInfoPoByAppName(appName);
		
	}
	
	/**
	 * ���ؼ�ص�app��Ϣ�����ñ�־λ���Ƿ��ʼ�����
	 * @return
	 */
	public List<AppInfoPo> findAllTimeApp(){
		if(initDone.get()) return appInfolist;
		else{
			initAppInfo();
			return appInfolist;
		}
		
	}

	//��ʼ��appInfo,��DB������Ϣ
	private void initAppInfo() {
		synchronized(dao){
			List<AppInfoPo> list = findAllAppInfo();
			
			for(AppInfoPo po:list){
				
				if(po.getAppStatus() == 0){
					if(po.getTimeDeploy() == Constants.DEFINE_DATA_EFFECTIVE){
						appInfolist.add(po);
						appInfoId2NameMap.put(po.getAppId(), po.getAppName());
						appInfoName2IdMap.put(po.getAppName(), po.getAppId());
					}
				}
				
			}
			
			for(String appName:tradeRelateAppNameList){
				if(appInfoName2IdMap.get(appName) != null){
					tradeRelateAppMap.put(appName, appInfoName2IdMap.get(appName));
				}
			}
			
			for(String appName:cartRelateAppNameList){
				if(appInfoName2IdMap.get(appName) != null){
					cartRelateAppMap.put(appName, appInfoName2IdMap.get(appName));
				}
			}
			
			for(String appName:tfBuyRelateAppNameList){
				if(appInfoName2IdMap.get(appName) != null){
					tfBuyRelateAppMap.put(appName, appInfoName2IdMap.get(appName));
				}
			}
			
			for(String appName:tradeplatformRelateAppNameList){
				if(appInfoName2IdMap.get(appName) != null){
					tradeplatformRelateAppMap.put(appName, appInfoName2IdMap.get(appName));
				}
			}
			
			for(String appName:tfTmRelateAppNameList){
				if(appInfoName2IdMap.get(appName) != null){
					tfTmRelateAppMap.put(appName, appInfoName2IdMap.get(appName));
				}
			}
			for(String appName:tradeapiRelateAppNameList){
				if(appInfoName2IdMap.get(appName) != null){
					tradeapiRelateAppMap.put(appName, appInfoName2IdMap.get(appName));
				}
			}

            for(String appName : auctionplatformRelateAppNameList) {
                if(appInfoName2IdMap.get(appName) != null) {
                    auctionplatformRelateAppMap.put(appName, appInfoName2IdMap.get(appName));
                }
            }
			initDone.compareAndSet(false, true);
		}
	}
	
	public Map<Integer,String> getAppInfoId2NameMap(){
		if(initDone.get()) return appInfoId2NameMap;
		else{
			initAppInfo();
			return appInfoId2NameMap;
		}
		
	}
	
	public Map<String,Integer> getAppInfoName2IdMap(){
		if(initDone.get()) return appInfoName2IdMap;
		else{
			initAppInfo();
			return appInfoName2IdMap;
		}
		
	}
	
	
	public List<AppInfoPo> findAllDayApp(){
		
		List<AppInfoPo> newlist = new ArrayList<AppInfoPo>();
		
		List<AppInfoPo> list = findAllAppInfo();
		
		for(AppInfoPo po:list){
			
			if(po.getAppStatus() == 0){
				if(po.getDayDeploy() == Constants.DEFINE_DATA_EFFECTIVE){
					newlist.add(po);
				}
			}
			
		}
		return newlist;
	}
	
	
	/**
	 * ���addAppInfoData
	 * @param AppInfoPo
	 */
	public boolean addAppInfoData(AppInfoPo appInfoPo) {
		
		return dao.addAppInfoData(appInfoPo);
	}
	
	/**
	 * ɾ��AppInfoPo
	 * @param appId
	 */
	public boolean deleteAppInfoData(int appId) {
	
		return dao.deleteAppInfoData(appId);
	}

	/**
	 * ����app��status״̬�����AppInfoPo��ɾ��
	 * @param appId
	 */
	public boolean ModifyAppStatus(int appId, int status) {
		
		return dao.ModifyAppStatus(appId, status);
	}
	
	
	
	/**
	 * ɾ��AppInfoPo
	 * @param AppInfoPo
	 */
	public boolean deleteAppInfoData(AppInfoPo appInfoPo) {
		
		return dao.deleteAppInfoData(appInfoPo);
	}
	
	
	
	/**
	 * ��ȡȫ��AppInfoPo
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppInfo() {
		
		return dao.findAllAppInfo();
	}
	
	/**
	 * ��ȡȫ��GroupName
	 * 
	 * @return
	 */
	public List<String> findAllAppGroupName() {
		
		return dao.findAllAppGroupName();
	}
	
	/**
	 * ��ȡȫ����Ч��Ӧ��
	 * @return
	 */
	public List<AppInfoPo> findAllEffectiveAppInfo() {
		List<AppInfoPo> newlist = new ArrayList<AppInfoPo>();
		List<AppInfoPo> list = findAllAppInfo();
		for(AppInfoPo po:list){
			if(po.getAppStatus() == 0){
				newlist.add(po);
			}
		}
		return newlist;
	}
	
	
	/**
	 * ����group������appInfoPo
	 * @param groupName
	 * @return
	 */
	public List<AppInfoPo> findAppInfoByGroupName(String groupName) {
		if(StringUtil.isNotBlank(groupName)){
			groupName.trim();
			groupName.replace(" ", "");
			return dao.findAppInfoByGroupName(groupName);
		}
		return new ArrayList<AppInfoPo>();
	}
	
	/**
	 * ����id������appInfoPo
	 * @param appId
	 * @return
	 */
	public AppInfoPo findAppInfoById(int appId) {
	
		return dao.findAppInfoById(appId);
	}
		
	/**
	 * ����AppInfoPo����
	 * @param AppInfoPo
	 * @return
	 */
	public boolean updateappInfo(AppInfoPo appInfoPo){
		
		return dao.updateappInfo(appInfoPo);
	}
	
	/**
	 * ����serverId�ҳ����ж�Ӧ��AppInfoPo
	 * @param serverId
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerId(int serverId) {
		
		return serverAppReldao.findAllAppByServerId(serverId);
	}
	
	
	/**
	 * ����serverId��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerId(int serverId,String appType) {
		return serverAppReldao.findAllAppByServerId(serverId,appType);
	}
	
	/**
	 * ����serverName�ҳ����ж�Ӧ��AppInfoPo
	 * @param serverId
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerName(String serverName) {
		
		return serverAppReldao.findAllAppByServerName(serverName);
	}
	
	/**
	 * ����databaseId�ҳ����ж�Ӧ��AppInfoPo
	 * @param databaseId
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDataBaseId(int databaseId) {
		
		return databaseAppReldao.findAllAppByDatabaseId(databaseId);
	}
	
	/**
	 * ����serverName�ҳ����ж�Ӧ��AppInfoPo
	 * @param serverId
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDataBaseName(String databaseName) {
		
		return databaseAppReldao.findAllAppByDatabaseName(databaseName);
	}

	/**
	 * ���database �� app �Ĺ�ϵ
	 * @param DatabaseAppRelPo
	 */
	public boolean addRel(DatabaseAppRelPo po) {
		
		return databaseAppReldao.addRel(po);
	}
	
	/**
	 * ���Server �� app �Ĺ�ϵ
	 * @param DatabaseAppRelPo
	 */
	public boolean addRel(ServerAppRelPo po) {
		
		return serverAppReldao.addRel(po);
	}
	
	/**
	 * ����rel
	 * @param DatabaseAppRelPo
	 */
	public DatabaseAppRelPo findRel(DatabaseAppRelPo po) {
		
		return databaseAppReldao.findRel(po);
	}
	
	/**
	 * ����appId���Ұ�����databaseInfo��rel��AppInfoPo���б�
	 * @param DatabaseAppRelPo
	 */
	public List<AppInfoPo> findDatabaseRel(int appId) {
		
		return databaseAppReldao.findDatabaseRel(appId);
	}

	/**
	 * ����appId��ѯ������ص�AppInfoPo
	 * 
	 * @return
	 */
	public List<AppInfoPo> findServerRel(int appId) {
		
		return serverAppReldao.findServerRel(appId);
	}
	
//	/**
//	 * �ж�rel�Ƿ����
//	 * @param databaseAppRelPo
//	 */
//	public boolean isExistDatabaseAppRel(DatabaseAppRelPo po) {
//		
//		boolean exist = true;
//		DatabaseAppRelPo dbpo = databaseAppReldao.findRel(po);
//		if(dbpo.getAppId() == 0 && dbpo.getDatabaseId() == 0 && dbpo.getAppType() == null) {
//			
//			exist = false;
//		}
//		return exist;
//	}
	
	/**
	 * app_id,��app_type����rel��Ψһ�ԣ��ʸ���app_id,app_type���ж��Ƿ��Ѿ�����rel
	 * @param DatabaseAppRelPo
	 */
	public boolean isExistDatabaseAppRel(DatabaseAppRelPo po) {
		
		return databaseAppReldao.isExistedRel(po);
	}
	/**
	 * ����rel
	 * @param ServerAppRelPo
	 */
	public ServerAppRelPo findRel(ServerAppRelPo relPo) {
		
		return serverAppReldao.findRel(relPo);
	}
	
	
//	/**
//	 *  �ж�rel�Ƿ����
//	 * @param serverAppRelPo
//	 */
//	public boolean isExistServerAppRel(ServerAppRelPo po) {
//		
//		boolean exist = true;
//		ServerAppRelPo dbpo = serverAppReldao.findRel(po);
//		if(dbpo.getAppId() == 0 && dbpo.getServerId() == 0 && dbpo.getAppType() == null) {
//			
//			exist = false;
//		}
//		return exist;
//	}
	
	
	/**
	 * app_id,��app_type����rel��Ψһ�ԣ��ʸ���app_id,app_type���ж��Ƿ��Ѿ�����rel
	 * @param ServerAppRelPo
	 */
	public boolean isExistServerAppRel(ServerAppRelPo relPo) {
		
		return serverAppReldao.isExistedRel(relPo);
	}
	/**
	 * ɾ��rel
	 * @param databaseAppRelPo
	 */
	public boolean deleteRel(DatabaseAppRelPo po) {
		
		return databaseAppReldao.deleteRel(po);
	}
	
	/**
	 * ɾ��rel
	 * @param serverAppRelPo
	 */
	public boolean deleteRel(ServerAppRelPo po) {
		
		return serverAppReldao.deleteRel(po);
	}
	
	/**
	 * ����databaseId��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDatabaseId(int databaseId) {
		
		return databaseAppReldao.findAllAppByDatabaseId(databaseId);
	}
	
	/**
	 * ����databaseName��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDatabaseName(String databaseName) {
		
		return databaseAppReldao.findAllAppByDatabaseName(databaseName);
	}
	
	/**
	 * ����databaseName��appType��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDbNameAndAppType(String databaseName, String appType) {
		
		return databaseAppReldao.findAllAppByDbNameAndAppType(databaseName, appType);
	}
	
	/**
	 * ����serverName��appType��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerNameAndAppType(String serverName, String appType) {
		
		return serverAppReldao.findAllAppByServerNameAndAppType(serverName, appType);
	}
	
	
	/**
	 * ����serverId��Ӧ�õ����Ͳ�ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByServerIdAndAppType(int serverId, String appType) {
		
		return serverAppReldao.findAllAppByServerIdAndAppType(serverId, appType);
	}
	/**
	 * ����rel
	 * @param ServerAppRelPo
	 * @return
	 */
	public boolean updateRel(ServerAppRelPo relPo){
		
		return serverAppReldao.updateRel(relPo);
	}
	
	/**
	 * ����rel
	 * @param DatabaseAppRelPo
	 * @return
	 */
	public boolean updateRel(DatabaseAppRelPo relPo){
		
		return databaseAppReldao.updateRel(relPo);
	}
	
	/**
	 * ��ѯ�����к����Ӧ����ص� key
	 * @return
	 */
	public List<AppKeyRelation> findAllAppKeyRelation(){
		
		return dao.findAllAppKeyRelation();		
	}
	
	/**
	 * ���Ӧ�ú�key�Ĺ�ϵ
	 * @param appKey
	 * @return
	 */
	public AppKeyRelation addAppKeyRelation(AppKeyRelation appKey) {
		return dao.addAppKeyRelation(appKey);
	}
	
	
	
	/**
	 * ����appId���ذ����ж�Ӧ����HostList��AppInfoPo
	 * 
	 * @return
	 */
	public AppInfoPo findAppWithHostListByAppId(int appId) {
		
		return hostDao.findAppWithHostListByAppId(appId);
	}
	
	/**
	 * ����appType�������л�û�й�����������AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppWithoutServerRel(String appType) {
		
		return serverAppReldao.findAllAppWithoutRel(appType);
	}
	
	/**
	 *  ����appType�������л�û�й������ݿ��AppInfoPO
	 * @param appType
	 */
	public List<AppInfoPo> findAllAppWithoutDatabaseRel(String appType) {
		
		return databaseAppReldao.findAllAppWithoutRel(appType);
	}
	/**
	 * ���rel��list
	 * @param List<ServerAppRelPo>
	 */
	public boolean addServerRelList(List<ServerAppRelPo> list) {
		
		return serverAppReldao.addRel(list);
	}
		
	/**
	 * ���database-app-rel��list
	 * @param List<DatabaseAppRelPo>
	 */
	public boolean addDatabaseRelList(List<DatabaseAppRelPo> list) {
		
		return databaseAppReldao.addRel(list);
	}
	/**
	 * ����databaseId��appType��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDatabaseIdAndAppType(int databaseId, String appType) {
		
		return databaseAppReldao.findAllAppByDatabaseIdAndAppType(databaseId, appType);
	}
	
	
	public List<AppDescPo> findAppDesc(){
		return dao.findAppDesc();
	}
	
	
	/**
	 * ��ȡȫ����Ӧ�õ�URL��ϵ�б�
	 * @return
	 */
	public Map<String,List<AppUrlRelation>> findAllAppUrlRelation(){
		return dao.findAllAppUrlRelation();
	}
	
	/**
	 * ��ȡȫ����Ӧ�õ�URL��ϵ�б�
	 * @return Map<URL,appName>
	 */
	public Map<String,String> findAllAppUrlRelationMap(){
		return dao.findAllAppUrlRelationMap();
	}
	
	
	/**
	 * ����Ӧ�����ƻ�ȡ��Ӧ��URL
	 * @return
	 */
	public List<AppUrlRelation> findAllAppUrlRelation(String appName){
		return dao.findAllAppUrlRelation(appName);
	}
	
	
	/**
	 * ����URL һ������ ��Ӧ�õ�map
	 * @return
	 */
	public Map<String,String> findAllTopUrlRelation(){
		return dao.findAllTopUrlRelation();
	}
	
	/**
	 * ���ص���ʵ��
	 * @return
	 */
	public AppUrlRelation getAppUrlRelationById(int id){
		return dao.getAppUrlRelationById(id);
	}	
	public boolean addAppUrlRelation(AppUrlRelation po) {
		return dao.addAppUrlRelation(po);
	}
	
	public boolean updateAppUrlRelation(AppUrlRelation po){
		return dao.updateAppUrlRelation(po);
	}
	public boolean deleteAppUrlRelation(int id) {
		return dao.deleteAppUrlRelation(id);
	}
	
	/**
	 * ���Ӧ��rt ͳ������
	 *@author xiaodu
	 * @param rt
	 *TODO
	 */
	public void addCspAppRtCount(CspAppRtCount rt){
		dao.addCspAppRtCount(rt);
	}
	
	public List<CspAppRtCount> getCspAppRtCountList(String time, String pvRtType){
		return dao.getCspAppRtCountList(time, pvRtType);
	}
	
	public List<CspAppRtCount> getCspAppRtCountList(String time, String pvRtType, String appName){
		return dao.getCspAppRtCountList(time, pvRtType, appName);
	}
	
	public CspAppRtCount getCspAppRtCount(String time, String pvRtType, String queryValue) {
		return dao.getCspAppRtCount(time, pvRtType, queryValue);
	}	
	
	public void deleteAppRtCountByTime(Date time) {
		try {
			dao.deleteAppRtCountByTime(time);
		} catch (SQLException e) {
			logger.error("", e);
		}
	}		
}
