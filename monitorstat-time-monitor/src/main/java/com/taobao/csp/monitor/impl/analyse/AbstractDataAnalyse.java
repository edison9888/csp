
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse;

import com.taobao.csp.monitor.DataAnalyse;

/**
 * @author xiaodu
 *
 * ÏÂÎç6:35:12
 */
public abstract class AbstractDataAnalyse implements DataAnalyse{
	
	private String appName;
	private String ip;
	private String feature;
	
	public AbstractDataAnalyse(String appName,String ip){
		this.appName = appName;
		this.ip = ip;
	}
	
	@Override
	public void doAnalyse() {
		
	}
	
	public AbstractDataAnalyse(String appName,String ip,String feature){
		this.appName = appName;
		this.ip = ip;
		this.feature = feature;
	}
	public String getAppName() {
		return appName;
	}
	public String getIp() {
		return ip;
	}
	public String getFeature() {
		return feature;
	}

}
