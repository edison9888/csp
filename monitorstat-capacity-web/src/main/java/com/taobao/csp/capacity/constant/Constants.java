
package com.taobao.csp.capacity.constant;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author xiaodu
 * @version 2011-9-20 ÉÏÎç11:06:02
 */
public class Constants {
	
	private float safetyRate = 3.2f;
	
	private Map<String,Integer> machinePvKey = new HashMap<String, Integer>();
	
	private Map<String,Integer> machineQpsKey = new HashMap<String, Integer>();
	
	private Map<String,Integer> machineHsfPvKey = new HashMap<String, Integer>();
	
	private Map<String,Integer> machineHsfQpsKey = new HashMap<String, Integer>();	
	
	private Map<String,Integer> machineGroupHSFPvKey = new HashMap<String, Integer>();
	
	private Map<String,Integer> machineGroupHsfQpsKey = new HashMap<String, Integer>();
	
	private Map<String,Integer> groupHostSiteKey = new HashMap<String, Integer>();
	
	private Map<String, Integer> largestPvQps = new HashMap<String, Integer>();
	
	private Map<String, Integer> largestHsfQps = new HashMap<String, Integer>();
	
	private Map<String, Integer> largestHsfGroupQps = new HashMap<String, Integer>();
	
	private Map<String, Integer> largestHsfGroupHost = new HashMap<String, Integer>();
	
	private Map<String, Integer> largestHsfGroupTime = new HashMap<String, Integer>();
	
	
	public Calendar getRankingTime(){
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return cal;
	}
	
	
	private Map<String,Integer> hostSiteKey = new HashMap<String, Integer>();
	public Map<String, Integer> getMachinePvKey() {
		return machinePvKey;
	}
	public void setMachinePvKey(Map<String, Integer> machinePvKey) {
		this.machinePvKey = machinePvKey;
	}
	public Map<String, Integer> getMachineQpsKey() {
		return machineQpsKey;
	}
	public void setMachineQpsKey(Map<String, Integer> machineQpsKey) {
		this.machineQpsKey = machineQpsKey;
	}
	public Map<String, Integer> getHostSiteKey() {
		return hostSiteKey;
	}
	public void setHostSiteKey(Map<String, Integer> hostSiteKey) {
		this.hostSiteKey = hostSiteKey;
	}
	public Map<String, Integer> getMachineHsfPvKey() {
		return machineHsfPvKey;
	}
	public void setMachineHsfPvKey(Map<String, Integer> machineHsfPvKey) {
		this.machineHsfPvKey = machineHsfPvKey;
	}
	public Map<String, Integer> getMachineHsfQpsKey() {
		return machineHsfQpsKey;
	}
	public void setMachineHsfQpsKey(Map<String, Integer> machineHsfQpsKey) {
		this.machineHsfQpsKey = machineHsfQpsKey;
	}
	public Map<String, Integer> getMachineGroupHSFPvKey() {
		return machineGroupHSFPvKey;
	}
	public void setMachineGroupHSFPvKey(Map<String, Integer> machineGroupHSFPvKey) {
		this.machineGroupHSFPvKey = machineGroupHSFPvKey;
	}
	public Map<String, Integer> getMachineGroupHsfQpsKey() {
		return machineGroupHsfQpsKey;
	}
	public void setMachineGroupHsfQpsKey(Map<String, Integer> machineGroupHsfQpsKey) {
		this.machineGroupHsfQpsKey = machineGroupHsfQpsKey;
	}
	public Map<String, Integer> getGroupHostSiteKey() {
		return groupHostSiteKey;
	}
	public void setGroupHostSiteKey(Map<String, Integer> groupHostSiteKey) {
		this.groupHostSiteKey = groupHostSiteKey;
	}
	public float getSafetyRate() {
		return safetyRate;
	}
	public void setSafetyRate(float safetyRate) {
		this.safetyRate = safetyRate;
	}
	public Map<String, Integer> getLargestPvQps() {
		return largestPvQps;
	}
	public void setLargestPvQps(Map<String, Integer> largestPvQps) {
		this.largestPvQps = largestPvQps;
	}
	public Map<String, Integer> getLargestHsfQps() {
		return largestHsfQps;
	}
	public void setLargestHsfQps(Map<String, Integer> largestHsfQps) {
		this.largestHsfQps = largestHsfQps;
	}
	public Map<String, Integer> getLargestHsfGroupQps() {
		return largestHsfGroupQps;
	}
	public void setLargestHsfGroupQps(Map<String, Integer> largestHsfGroupQps) {
		this.largestHsfGroupQps = largestHsfGroupQps;
	}
	public Map<String, Integer> getLargestHsfGroupHost() {
		return largestHsfGroupHost;
	}
	public void setLargestHsfGroupHost(Map<String, Integer> largestHsfGroupHost) {
		this.largestHsfGroupHost = largestHsfGroupHost;
	}
	public Map<String, Integer> getLargestHsfGroupTime() {
		return largestHsfGroupTime;
	}
	public void setLargestHsfGroupTime(Map<String, Integer> largestHsfGroupTime) {
		this.largestHsfGroupTime = largestHsfGroupTime;
	}

}
