package com.taobao.csp.loadrun.core.dao;


public class DataBaseInfoPo {

	private int dbId;	
	
	//核心库 是唯一的名称为:MS_DATABASE_MIAN.其它库名字可以自取
	private String dbName;	
	
	private String dbUrl;
	
	//库表用户信息
	private String dbUser;
	
	//库表密码
	private String dbPwd;
	
	
	public int getDbId() {
		return dbId;
	}

	public void setDbId(int dbId) {
		this.dbId = dbId;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPwd() {
		return dbPwd;
	}

	public void setDbPwd(String dbPwd) {
		this.dbPwd = dbPwd;
	}

	public String getDbDesc() {
		return dbDesc;
	}

	public void setDbDesc(String dbDesc) {
		this.dbDesc = dbDesc;
	}

	public int getDbType() {
		return dbType;
	}

	public void setDbType(int dbType) {
		this.dbType = dbType;
	}

	//库表描述信息
	private String dbDesc;
	
	//1 核心库 2 业务库 3 外部库
	private int dbType;

}
