package com.taobao.csp.loadrun.core.dao;


public class DataBaseInfoPo {

	private int dbId;	
	
	//���Ŀ� ��Ψһ������Ϊ:MS_DATABASE_MIAN.���������ֿ�����ȡ
	private String dbName;	
	
	private String dbUrl;
	
	//����û���Ϣ
	private String dbUser;
	
	//�������
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

	//���������Ϣ
	private String dbDesc;
	
	//1 ���Ŀ� 2 ҵ��� 3 �ⲿ��
	private int dbType;

}
