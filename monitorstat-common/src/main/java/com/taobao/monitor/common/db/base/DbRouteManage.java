
package com.taobao.monitor.common.db.base;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;
import org.dom4j.DocumentException;

import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;
import com.taobao.monitor.common.db.impl.center.DataBaseAppRelDao;
import com.taobao.monitor.common.db.impl.center.DataBaseInfoDao;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.DataBaseInfoPo;

/**
 * 
 * @author xiaodu
 * @version 2010-6-2 下午05:55:52
 */
public class DbRouteManage implements ManagerListener{
	
	private static final Logger logger =  Logger.getLogger(DbRouteManage.class);
	
	public static String MAIN_DBROUTE_NAME="Main";
	
	private static DbRouteManage manage = new DbRouteManage();
	
	
	private DiamondManager diamondManager = new DefaultDiamondManager("CSP_GROUP", "com.taobao.csp.mysql.main", this);
	
	private long lastTime = System.currentTimeMillis();
	
	public static DbRouteManage get(){
		return manage;
	}
	
	public DbRoute getMainDBRoute(){
		return dbRouteIdMap.get(MAIN_DBROUTE_NAME);
	}
	
	
	private DbRouteManage(){
		try {
			init();
		} catch (DocumentException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}
	private Map<String,DbRoute> dbRouteIdMap = new ConcurrentHashMap<String, DbRoute>();
	private Map<Integer,DbRoute> dbRouteTimeAppIdMap = new ConcurrentHashMap<Integer, DbRoute>();
	private Map<Integer,DbRoute> dbRouteDayAppIdMap = new ConcurrentHashMap<Integer, DbRoute>();
	
	
	
	private synchronized void checkDB(){
		if(System.currentTimeMillis() - lastTime >1000*60*10){
			
			try {
				init();
			} catch (DocumentException e) {
			}
			lastTime = System.currentTimeMillis();
		}
		
	}
	
	
	
	/**
	 * 初始化主库配置文件
	 * @throws DocumentException 
	 */
	public void init() throws DocumentException{
		synchronized (MAIN_DBROUTE_NAME) {	
			
			for(Map.Entry<String,DbRoute> entry:dbRouteIdMap.entrySet()){
				try {
					entry.getValue().getDataSource().close();
				} catch (SQLException e) {
				}
			}
			
			
		
			dbRouteIdMap.clear();
			dbRouteTimeAppIdMap.clear();
			dbRouteDayAppIdMap.clear();
			
			
			Properties properties = diamondManager.getAvailablePropertiesConfigureInfomation(10000);
			
			
			
			String db_url =  properties.getProperty("db_url");
			String db_name =  properties.getProperty("db_name");
			String db_password =properties.getProperty("db_password"); 
//			int max_wait = Integer.parseInt(PropertiesConfig.getValue("monitor","max_wait"));
//			int max_active = Integer.parseInt(PropertiesConfig.getValue("monitor","max_active"));
			
			DbRoute mianDbRoute = new DbRoute();//获取主库的配置
			mianDbRoute.setUrl(db_url);
			mianDbRoute.setUser(db_name);
			mianDbRoute.setPassword(db_password);
			mianDbRoute.setMaxWait(5000);
			mianDbRoute.setMaxConnect(8);
			
			
			mianDbRoute.init();
			
			DataBaseInfoDao dao = new DataBaseInfoDao(mianDbRoute);
			DataBaseAppRelDao relDao = new DataBaseAppRelDao(mianDbRoute);
			List<DataBaseInfoPo> dataBaseList = dao.findAllDataBaseInfo();
			for(DataBaseInfoPo po:dataBaseList){
				logger.info("获取库:"+po.getDbName());	
				try{
					DbRoute route = new DbRoute();
					route.setDbRouteId(po.getDbName());
					route.setPassword(po.getDbPwd());
					route.setUser(po.getDbUser());
					route.setUrl(po.getDbUrl());
					route.setMaxConnect(10);
					route.init();
					dbRouteIdMap.put(po.getDbName(), route);
					List<AppInfoPo> relList = relDao.findAllAppByDatabaseId(po.getDbId());
					for(AppInfoPo poRel:relList){
						if(route != null&&poRel.getAppStatus()==0){
							if(poRel.getAppType().equals("time")){
								logger.info(po.getDbName()+" 关联:time:"+poRel.getAppName());	
								dbRouteTimeAppIdMap.put(poRel.getAppId(), route);
							}
							if(poRel.getAppType().equals("day")){
								logger.info(po.getDbName()+" 关联:day:"+poRel.getAppName());	
								dbRouteDayAppIdMap.put(poRel.getAppId(), route);
							}
						}
					}
					
					
				}catch (Exception e) {
					logger.error(po.getDbName()+" 构建异常",e);
				}
			}
		}
	}
	
	public DbRoute getDbRouteByRouteId(String dbName){
		DbRoute d = dbRouteIdMap.get(dbName);
		if(d == null){
			checkDB();
			d = dbRouteIdMap.get(dbName);
			if(d!=null){
				return d;
			}				
			logger.info("getDbRouteByRouteId dbName:"+dbName+" 不存在DbRoute");
		}
		
		return d;
	}
	
	public DbRoute getDbRouteByTimeAppid(int appId){
		DbRoute d = dbRouteTimeAppIdMap.get(appId);
		if(d == null){
			checkDB();
			d = dbRouteTimeAppIdMap.get(appId);
			if(d!=null){
				return d;
			}
			logger.info("getDbRouteByAppid appId:"+appId+" 不存在DbRoute");
		}
		return d ;
	}
	
	public DbRoute getDbRouteByDayAppid(int appId){
		DbRoute d = dbRouteDayAppIdMap.get(appId);
		if(d == null){
			checkDB();
			d = dbRouteDayAppIdMap.get(appId);
			if(d!=null){
				return d;
			}				
			logger.info("getDbRouteByAppid appId:"+appId+" 不存在DbRoute");
		}
		return d ;
	}


	@Override
	public Executor getExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void receiveConfigInfo(String configInfo) {
		try {
			init();
		} catch (DocumentException e) {
		}
	}
}
