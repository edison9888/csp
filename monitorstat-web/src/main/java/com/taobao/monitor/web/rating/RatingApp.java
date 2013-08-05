
package com.taobao.monitor.web.rating;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author xiaodu
 * @version 2010-8-18 ÏÂÎç05:19:02
 */
public class RatingApp {
	
	private int appId;
	private String appName;
	private double rating;
	
	private List<RatingIndicator> indicatorList = new ArrayList<RatingIndicator>();

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public List<RatingIndicator> getIndicatorList() {
		return indicatorList;
	}

	public void setIndicatorList(List<RatingIndicator> indicatorList) {
		this.indicatorList = indicatorList;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public double getRating() {
		return rating;
	}

	public void setRating(double rating) {
		this.rating = rating;
	}
	
	
	

}
