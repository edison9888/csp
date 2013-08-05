
/**
 * monitorstat-time-web
 */
package com.taobao.csp.time.web.po;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiaodu
 *
 * 下午3:43:53
 */
public class IndexEntry {
	
	private int appId;
	
	private String appType;
	
	private String appName;
	
	private long pv;
	private long rt;	//add by zhongting.zy
	private double load;	//平均load
	
	private String pvRate;
	
	private String ftime;
	
	private double failurerate;
	
	private int exceptionNum;
	
	private String exceptionRate;
	
	private int qps;
	
	private float qpsRate;
	
	private int machines;
	
	private int alarms;
	
	private int capcityRate;
	
	private int exceptionDBNum;
	
	private boolean highlighted;//是否需要闪烁
	
	private String pageSize; //总网页大小
	
	private Map<String, Long> referMap = new HashMap<String, Long>(); 

	public int getExceptionDBNum() {
		return exceptionDBNum;
	}

	public void setExceptionDBNum(int exceptionDBNum) {
		this.exceptionDBNum = exceptionDBNum;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public long getPv() {
		return pv;
	}

	public void setPv(long pv) {
		this.pv = pv;
	}

	public String getFtime() {
		return ftime;
	}

	public void setFtime(String ftime) {
		this.ftime = ftime;
	}



	public double getFailurerate() {
		return failurerate;
	}

	public void setFailurerate(double failurerate) {
		this.failurerate = failurerate;
	}

	public int getExceptionNum() {
		return exceptionNum;
	}

	public void setExceptionNum(int exceptionNum) {
		this.exceptionNum = exceptionNum;
	}

	public int getQps() {
		return qps;
	}

	public void setQps(int qps) {
		this.qps = qps;
	}

	public int getMachines() {
		return machines;
	}

	public void setMachines(int machines) {
		this.machines = machines;
	}

	public int getAlarms() {
		return alarms;
	}

	public void setAlarms(int alarms) {
		this.alarms = alarms;
	}

	public int getCapcityRate() {
		return capcityRate;
	}

	public void setCapcityRate(int capcityRate) {
		this.capcityRate = capcityRate;
	}

	public Map<String, Long> getReferMap() {
		return referMap;
	}

	public void setReferMap(Map<String, Long> referMap) {
		this.referMap = referMap;
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
	}

	public String getPvRate() {
		return pvRate;
	}

	public void setPvRate(String pvRate) {
		this.pvRate = pvRate;
	}

	public String getExceptionRate() {
		return exceptionRate;
	}

	public void setExceptionRate(String exceptionRate) {
		this.exceptionRate = exceptionRate;
	}

	public float getQpsRate() {
		return qpsRate;
	}

	public void setQpsRate(float qpsRate) {
		this.qpsRate = qpsRate;
	}

	public long getRt() {
		return rt;
	}

	public void setRt(long rt) {
		this.rt = rt;
	}

	public double getLoad() {
		return load;
	}

	public void setLoad(double load) {
		this.load = load;
	}

	public String getPageSize() {
		if(pageSize == null)
			return "0";
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}


}
