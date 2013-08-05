
package com.taobao.csp.loadrun.core.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.apache.log4j.Logger;

import com.taobao.diamond.manager.DiamondManager;
import com.taobao.diamond.manager.ManagerListener;
import com.taobao.diamond.manager.impl.DefaultDiamondManager;

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
	
		init();
		
	}
	private Map<String,DbRoute> dbRouteIdMap = new ConcurrentHashMap<String, DbRoute>();
	
	
	
	
	private synchronized void checkDB(){
		if(System.currentTimeMillis() - lastTime >1000*60*10){
			init();
			lastTime = System.currentTimeMillis();
		}
		
	}
	
	public void init() {
		synchronized (MAIN_DBROUTE_NAME) {	
			
			for(Map.Entry<String,DbRoute> entry:dbRouteIdMap.entrySet()){
				try {
					entry.getValue().getDataSource().close();
				} catch (SQLException e) {
				}
			}
			
			
		
			dbRouteIdMap.clear();
			
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

	@Override
	public Executor getExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void receiveConfigInfo(String configInfo) {
		init();
	}
}
