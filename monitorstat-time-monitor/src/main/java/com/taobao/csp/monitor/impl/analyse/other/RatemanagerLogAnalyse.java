
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu
 *
 * ÏÂÎç3:05:24
 */
public class RatemanagerLogAnalyse extends AbstractDataAnalyse{

	/**
	 * @param appName
	 * @param ip
	 */
	public RatemanagerLogAnalyse(String appName, String ip,String feature) {
		super(appName, ip, feature);
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#analyseOneLine(java.lang.String)
	 */
	@Override
	public void analyseOneLine(String line) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#submit()
	 */
	@Override
	public void submit() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.taobao.csp.monitor.DataAnalyse#release()
	 */
	@Override
	public void release() {
		// TODO Auto-generated method stub
		
	}

}
