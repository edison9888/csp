package com.taobao.monitor.common.db.impl.capacity.po;

import java.util.Date;

/***
 * Ӧ�õ���������
 * @author youji.zj
 * @version 2012-08-15
 *
 */
public class CapacityLoadPo {
	
	/*** Ӧ���� ***/
	private String appName;
	
	/*** ����ֵ ***/
	private double qps;
	
	/*** �������ͣ�single(����) group��Ⱥ. ***/
	private String dataType;
	
	/*** ���ɵ�����Դ��realtime(ʵʱ) day(�ձ�) ***/
	private String dataSouce;
	
	/*** �ɼ�ʱ�� ***/
	private Date collectTime;

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public double getQps() {
		return qps;
	}

	public void setQps(double qps) {
		this.qps = qps;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getDataSouce() {
		return dataSouce;
	}

	public void setDataSouce(String dataSouce) {
		this.dataSouce = dataSouce;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

}
