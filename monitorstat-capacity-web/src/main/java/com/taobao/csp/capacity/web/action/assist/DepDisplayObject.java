package com.taobao.csp.capacity.web.action.assist;

import java.util.HashMap;
import java.util.Map;

/***
 * object for display
 * the last day of 2011
 * @author youji.zj
 *
 */
public class DepDisplayObject {
	
	/*** app name ***/
	private String appName;
	
	/*** dependency qps for rooms ***/
	private Map<String, Double> roomQps;
	
	/*** average dependency qps ***/
	private double averageQps;
	
	/*** ratio to the total qps by percent***/
	private double ratio;
	
	/*** influence in all dependency app by percent***/
	private double influence;
	
	public DepDisplayObject() {
		 roomQps = new HashMap<String, Double>();
		 roomQps.put("CM3", 0d);
		 roomQps.put("CM4", 0d);
		 roomQps.put("CM6", 0d);
		 roomQps.put("CM8", 0d);
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public double getAverageQps() {
		return averageQps;
	}

	public void setAverageQps(double averageQps) {
		this.averageQps = averageQps;
	}

	public double getRatio() {
		return ratio;
	}

	public void setRatio(double ratio) {
		this.ratio = ratio;
	}

	public double getInfluence() {
		return influence;
	}

	public void setInfluence(double influence) {
		this.influence = influence;
	}

	public Map<String, Double> getRoomQps() {
		return roomQps;
	}

	public void setRoomQps(Map<String, Double> roomQps) {
		this.roomQps = roomQps;
	}

}
