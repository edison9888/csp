
package com.taobao.csp.capacity.po;
/**
 * 
 * @author xiaodu
 * @version 2011-9-20 ÉÏÎç11:42:25
 */
public class CapacityDetail {
	
	private String siteName;
	
	private double pvRate;
	
	private double recentlyQps;
	
	private double maxQps;
	
	private double loadQps;
	
	private double safeQps;
	
	private int machineNumber;
	
	private double capacityRate;
	
	private int safeMachineNumber;

	
	
	
	public double getMaxQps() {
		return maxQps;
	}

	public void setMaxQps(double maxQps) {
		this.maxQps = maxQps;
	}

	public int getSafeMachineNumber() {
		return safeMachineNumber;
	}

	public void setSafeMachineNumber(int safeMachineNumber) {
		this.safeMachineNumber = safeMachineNumber;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public double getPvRate() {
		return pvRate;
	}

	public void setPvRate(double pvRate) {
		this.pvRate = pvRate;
	}

	public double getRecentlyQps() {
		return recentlyQps;
	}

	public void setRecentlyQps(double recentlyQps) {
		this.recentlyQps = recentlyQps;
	}

	public double getSafeQps() {
		return safeQps;
	}

	public void setSafeQps(double safeQps) {
		this.safeQps = safeQps;
	}

	public int getMachineNumber() {
		return machineNumber;
	}

	public void setMachineNumber(int machineNumber) {
		this.machineNumber = machineNumber;
	}

	public double getCapacityRate() {
		return capacityRate;
	}

	public void setCapacityRate(double capacityRate) {
		this.capacityRate = capacityRate;
	}

	public double getLoadQps() {
		return loadQps;
	}

	public void setLoadQps(double loadQps) {
		this.loadQps = loadQps;
	}
	
	
	
	

}
