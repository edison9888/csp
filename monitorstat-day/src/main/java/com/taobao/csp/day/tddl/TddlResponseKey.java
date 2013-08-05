package com.taobao.csp.day.tddl;

/***
 * 最大响应时间与最小响应时间的key
 * 
 * @author youji.zj
 * @version 1.0 2012-09-06
 *
 */
public class TddlResponseKey {
	
	public TddlResponseKey(String appName, String dbFeature) {
		this.appName = appName;
		this.dbFeature = dbFeature;
	}
	
	private String appName;
	
	private String dbFeature;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getDbFeature() {
		return dbFeature;
	}

	public void setDbFeature(String dbFeature) {
		this.dbFeature = dbFeature;
	}
	
	@Override
	public boolean equals(Object object) {
		if (object == null) return false;
		if (!(object instanceof TddlResponseKey)) return false;
		
		TddlResponseKey po = (TddlResponseKey)object;
		if (po.getAppName().equals(this.getAppName()) &&  po.getDbFeature().equals(this.getDbFeature())) {
			return true;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		int result = 17;
		result = 37 * result + appName.hashCode();
		result = 37 * result + dbFeature.hashCode();
		
		return result;
	}

}
