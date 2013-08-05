
package com.taobao.csp.capacity.po;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author xiaodu
 * @version 2011-4-12 обнГ07:17:45
 */
public class CapacityRankingPo implements Comparable<CapacityRankingPo>{
	
	private int appId;
	
	private String appName;
	
	private String appType;
	
	private String rankingName;
	
	private double cData;
	
	private double cQps;
	
	private double cLoadrunQps;
	
	private int cRanking;
	
	private Date rankingDate;
	
	private String rankingFeature;
	
	
	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getRankingName() {
		return rankingName;
	}

	public void setRankingName(String rankingName) {
		this.rankingName = rankingName;
	}

	public double getCData() {
		return cData;
	}

	public void setCData(double data) {
		cData = data;
	}


	public double getCQps() {
		return cQps;
	}

	public void setCQps(double qps) {
		cQps = qps;
	}

	public double getCLoadrunQps() {
		return cLoadrunQps;
	}

	public void setCLoadrunQps(double loadrunQps) {
		cLoadrunQps = loadrunQps;
	}

	public int getCRanking() {
		return cRanking;
	}

	public void setCRanking(int ranking) {
		cRanking = ranking;
	}

	public Date getRankingDate() {
		return rankingDate;
	}

	public void setRankingDate(Date rankingDate) {
		this.rankingDate = rankingDate;
	}

	@Override
	public int compareTo(CapacityRankingPo o) {
		
		if(this.cData >o.getCData()){
			return -1;
		}else if(this.cData <o.getCData()){
			return 1;
		}
		
		return 0;
	}

	public String getRankingFeature() {
		return rankingFeature;
	}

	public void setRankingFeature(String rankingFeature) {
		this.rankingFeature = rankingFeature;
	}
	
	public String getFeatureData(String key){
		if(this.rankingFeature != null){
			Pattern p = Pattern.compile(key+"\\:([^\\|]*)\\|");
			Matcher m = p.matcher(this.rankingFeature);
			if(m.find()){
				return m.group(1);
			}
		}
		return null;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
	

}
