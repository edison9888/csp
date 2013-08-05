
package com.taobao.monitor.dependent.ao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.taobao.monitor.dependent.appinfo.AppJar;
import com.taobao.monitor.dependent.appinfo.AppStatus;
import com.taobao.monitor.dependent.dao.AppJarDao;

/**
 * 
 * @author xiaodu
 * @version 2011-5-3 下午05:38:32
 */
public class AppJarAo {
	
	private AppJarDao dao = new AppJarDao();
	
	private static AppJarAo ao = new AppJarAo();
	
	public static AppJarAo get(){
		return ao;
	}
	
	
	public void addAppStatus(AppStatus app){
		dao.addAppStatus(app);
	}
	
	public void addAppJar(AppJar jar){
		dao.addAppJar(jar);
	}
	
	public List<String> findjarName(Date date){
		return dao.findjarName(date);
	}
	
	
	public List<AppStatus> findAppStatus(String jarName,Date date){
		return dao.findAppStatus(jarName, date);
	}
	
	public AppStatus getAppStatus(String id){
		return dao.getAppStatus(id);
	}
	
	public List<AppJar> getAppJar(String appStatusId){
		return dao.getAppJar(appStatusId);
	}
	
	public Map<String,List<AppJar>> findAppStatusLikejarname(String appName,String jarName,Date date){
		return dao.findAppStatusLikejarname(appName,jarName, date);
	}
	
	public Map<String,Map<String,AppStatus>> findAllAppStatus(Date date){
		return dao.findAllAppStatus(date);
	}
	
	
	
	/**
	 * 获取最近一次收集的时间
	 * @return
	 */
	public Date getRecentlyCollectTime(){
		return dao.getRecentlyCollectTime();
	}

}
