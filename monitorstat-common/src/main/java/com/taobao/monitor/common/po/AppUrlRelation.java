
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.po;

import java.util.Date;


/**
 * ǰ��Ӧ���ṩ��URL
 * 
 * 
 * @author xiaodu
 *
 * ����10:23:52
 */
public class AppUrlRelation {
	private int id;
	private String appName;
	
	private String appUrl;
	
	private String topUrl;
	
	private Date modifyDate;
	
	private boolean dynamic;//��ʾ���URL �Ƿ��Ƕ�̬��

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getTopUrl() {
		return topUrl;
	}

	public void setTopUrl(String topUrl) {
		this.topUrl = topUrl;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public boolean isDynamic() {
		return dynamic;
	}

	public void setDynamic(boolean dynamic) {
		this.dynamic = dynamic;
	}
	
	

}
