
/**
 * monitorstat-monitor
 */
package com.taobao.csp.monitor.impl.analyse.other;

import com.taobao.csp.monitor.impl.analyse.AbstractDataAnalyse;

/**
 * @author xiaodu
 *
 * ÉÏÎç9:11:18
 */
public class LoginLogAnalyseFile extends AbstractDataAnalyse{

	/**
	 * @param appName
	 * @param ip
	 */
	public LoginLogAnalyseFile(String appName, String ip,String f) {
		super(appName, ip, f);
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
