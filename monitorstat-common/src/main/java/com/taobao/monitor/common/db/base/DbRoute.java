
package com.taobao.monitor.common.db.base;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * 
 * @author xiaodu
 * @version 2010-6-2 ÏÂÎç05:14:52
 */
public class DbRoute {
	
	private String dbRouteId;
	
	private String url;
	
	private String user;
	
	private String password;
	
	private int maxConnect;
	
	private int maxWait;
	
	private Set<String> appNameSet = new HashSet<String>();
	
	private Set<Integer> appIdSet = new HashSet<Integer>();
	
	private BasicDataSource dataSource;
	
	public void init(){
		dataSource = new BasicDataSource();
		dataSource.setDriverClassName("org.gjt.mm.mysql.Driver");
		dataSource.setUrl(this.getUrl());
		dataSource.setUsername(this.getUser());
		dataSource.setPassword(this.getPassword());
		dataSource.setDefaultAutoCommit(false);
		dataSource.setMaxActive(this.getMaxConnect());
		dataSource.setMaxWait(this.getMaxWait());
	}
	
			

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getDbRouteId() {
		return dbRouteId;
	}

	public void setDbRouteId(String dbRouteId) {
		this.dbRouteId = dbRouteId;
	}

	public int getMaxConnect() {
		return maxConnect;
	}

	public void setMaxConnect(int maxConnect) {
		this.maxConnect = maxConnect;
	}

	public Set<String> getAppNameSet() {
		return appNameSet;
	}

	public void setAppNameSet(Set<String> appNameSet) {
		this.appNameSet = appNameSet;
	}

	public Set<Integer> getAppIdSet() {
		return appIdSet;
	}

	public void setAppIdSet(Set<Integer> appIdSet) {
		this.appIdSet = appIdSet;
	}


	public BasicDataSource getDataSource() {
		return dataSource;
	}

	public int getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(int maxWait) {
		this.maxWait = maxWait;
	}

}
