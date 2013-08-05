
package com.taobao.monitor.dependent.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.dependent.appinfo.AppJar;
import com.taobao.monitor.dependent.appinfo.AppStatus;

/**
 * 
 * @author xiaodu
 * @version 2011-5-3 下午03:32:45
 */
public class AppJarDao extends MysqlRouteBase{
	
	private static final Logger logger = Logger.getLogger(AppJarDao.class);
	
	public AppJarDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	
	
	public void addAppStatus(AppStatus app){
		
		String sql = "insert into CSP_APP_STATUS(id,host_ip,host_site,app_name,webx_xml_info,web_xml_info,httpd_start_time,jboss_start_time,collect_time)" +
				"values(?,?,?,?,?,?,?,?,CURRENT_DATE())";
		try {
			this.execute(sql, new Object[]{app.getId(),app.getHostIp(),app.getHostSite(),app.getAppName(),app.getWebxInfo(),app.getWebInfo(),app.getHttpdStartTime(),app.getJbossStartTime()});
		} catch (SQLException e) {
			logger.error("addAppStatus", e);
		}
		
	}
	
	public void addAppJar(AppJar jar){
		
		String sql ="insert into CSP_APP_JAR(app_status_id,jar_name,meta_info,jar_size,modify_time,collect_time)values(?,?,?,?,?,CURRENT_DATE())";
		try {
			this.execute(sql, new Object[]{jar.getAppStatusId(),jar.getJarName(),jar.getMetaInfo(),jar.getJarSize(),jar.getModifyTime()});
		} catch (SQLException e) {
			logger.error("addAppJar", e);
		}		
	}
	
	
	public List<String> findjarName(Date date){
		
		String sql ="select distinct jar_name from CSP_APP_JAR where collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		final List<String> list = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[]{date}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					list.add(rs.getString("jar_name"));
				}});
		} catch (Exception e) {
			logger.error("findjarName", e);
		}
		
		return list;
	}
	
	
	
	public List<AppStatus> findAppStatus(String jarName,Date date){
		
		final List<AppStatus> appList = new ArrayList<AppStatus>();
		
		String sql = "select s.id,s.host_ip,s.host_site,s.app_name,s.webx_xml_info,s.web_xml_info,s.httpd_start_time,s.jboss_start_time,s.collect_time " +
				" from CSP_APP_STATUS s,CSP_APP_JAR j where s.id=j.app_status_id and j.jar_name=? and j.collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";

		try {
			this.query(sql, new Object[]{jarName,date}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					AppStatus appstatus = new AppStatus();
					appstatus.setId(rs.getString("id"));
					appstatus.setAppName(rs.getString("app_name"));
					appstatus.setHostIp(rs.getString("host_ip"));
					appstatus.setHostSite(rs.getString("host_site"));
					appstatus.setHttpdStartTime(rs.getString("httpd_start_time"));
					appstatus.setJbossStartTime(rs.getString("jboss_start_time"));
					appstatus.setWebInfo(rs.getString("web_xml_info"));
					appstatus.setWebxInfo(rs.getString("webx_xml_info"));
					
					appList.add(appstatus);
				}});
		} catch (Exception e) {
			logger.error("findjarName", e);
		}
		
		return appList;
	}
	
	
	public Map<String,List<AppJar>> findAppStatusLikejarname(String appName,String jarName,Date date){
		
		
		String sql = "select s.id,s.host_ip,s.host_site,s.app_name,s.webx_xml_info,s.web_xml_info,s.httpd_start_time,s.jboss_start_time,s.collect_time,j.jar_name,j.jar_size,j.modify_time" +
				" from CSP_APP_STATUS s,CSP_APP_JAR j where s.app_name=? and s.id=j.app_status_id and j.jar_name like ? and j.collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";
		final Map<String,List<AppJar>> appMap = new HashMap<String,List<AppJar>>();//apppName hostIP appstatus
		try {
			this.query(sql, new Object[]{appName,"%"+jarName+"%",date}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("app_name");
					String hostIP = rs.getString("host_ip");
					List<AppJar> jarList = appMap.get(hostIP);
					if(jarList == null){
						jarList = new ArrayList<AppJar>();
						appMap.put(hostIP, jarList);
					}					
					AppJar jar = new AppJar();
					jar.setJarName(rs.getString("jar_name"));
					jar.setJarSize(rs.getInt("jar_size"));
					jar.setModifyTime(rs.getString("modify_time"));
					
					jarList.add(jar);
				}});
		} catch (Exception e) {
			logger.error("findjarName", e);
		}
		
		return appMap;
	}
	
	
	public Map<String,Map<String,AppStatus>> findAllAppStatus(Date date){
		
		
		String sql = "select s.id,s.host_ip,s.host_site,s.app_name,s.webx_xml_info,s.web_xml_info,s.httpd_start_time,s.jboss_start_time,s.collect_time" +
				" from CSP_APP_STATUS s where s.collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";
		final Map<String,Map<String,AppStatus>> appMap = new HashMap<String, Map<String,AppStatus>>();//apppName hostIP appstatus
		try {
			this.query(sql, new Object[]{date}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("app_name");
					String hostIP = rs.getString("host_ip");
					Map<String,AppStatus> mapHost = appMap.get(appName);
					if(mapHost == null){
						mapHost = new HashMap<String, AppStatus>();
						appMap.put(appName, mapHost);
					}
					AppStatus appstatus = mapHost.get(hostIP);
					if(appstatus == null){
						appstatus = new AppStatus();
						appstatus.setId(rs.getString("id"));
						appstatus.setAppName(rs.getString("app_name"));
						appstatus.setHostIp(rs.getString("host_ip"));
						appstatus.setHostSite(rs.getString("host_site"));
						appstatus.setHttpdStartTime(rs.getString("httpd_start_time"));
						appstatus.setJbossStartTime(rs.getString("jboss_start_time"));
						appstatus.setWebInfo(rs.getString("web_xml_info"));
						appstatus.setWebxInfo(rs.getString("webx_xml_info"));
						mapHost.put(hostIP, appstatus);
					}					
				}});
		} catch (Exception e) {
			logger.error("findjarName", e);
		}
		
		return appMap;
	}
	
	
	
	
	
	
	
	public AppStatus getAppStatus(String id){
		String sql = "select s.id,s.host_ip,s.host_site,s.app_name,s.webx_xml_info,s.web_xml_info,s.httpd_start_time,s.jboss_start_time,s.collect_time " +
		" from CSP_APP_STATUS s where s.id=?";
		final AppStatus appstatus = new AppStatus();
		try {
			this.query(sql, new Object[]{id}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					appstatus.setId(rs.getString("id"));
					appstatus.setHostIp(rs.getString("host_ip"));
					appstatus.setHostSite(rs.getString("host_site"));
					appstatus.setHttpdStartTime(rs.getString("httpd_start_time"));
					appstatus.setJbossStartTime(rs.getString("jboss_start_time"));
					appstatus.setWebInfo(rs.getString("web_xml_info"));
					appstatus.setWebxInfo(rs.getString("webx_xml_info"));
				}});
		} catch (Exception e) {
			logger.error("getAppStatus", e);
		}
		return appstatus;
	}
	
	public List<AppJar> getAppJar(String appStatusId){
		String sql = "select app_status_id,jar_name,meta_info,jar_size,modify_time,collect_time from CSP_APP_JAR where app_status_id=? ";
		
		final List<AppJar> jarList = new ArrayList<AppJar>();
		
		try {
			this.query(sql, new Object[]{appStatusId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					AppJar appJar = new AppJar();
					appJar.setAppStatusId(rs.getString("app_status_id"));
					appJar.setJarName(rs.getString("jar_name"));
					appJar.setJarSize(rs.getInt("jar_size"));
					appJar.setMetaInfo(rs.getString("meta_info"));
					appJar.setModifyTime(rs.getString("modify_time"));
					
					jarList.add(appJar);
				}});
		} catch (Exception e) {
			logger.error("getAppJar", e);
		}
		
		return jarList;
	}
	
	/**
	 * 获取最近一次收集的时间
	 * @return
	 */
	public Date getRecentlyCollectTime(){
		String sql = "select max(collect_time) from CSP_APP_STATUS";
		
		try {
			return this.getDateValue(sql);
		} catch (SQLException e) {
			logger.error("getAppJar", e);
		}
		return null;
	}
	

}
