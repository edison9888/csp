
package com.taobao.monitor.alarm.n.filter;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 下午04:12:51
 */
public class AppTmpStopFilter extends Filter{
	private static final Logger logger =  Logger.getLogger(AppTmpStopFilter.class);
	private int appId;

	public AppTmpStopFilter(int appId,long startExpiry, long endExpiry) {
		super(startExpiry, endExpiry);
		this.appId = appId;
	}

	@Override
	protected boolean valid(AlarmContext context) {
		
		if(context.getAppId() == this.appId){
			
			logger.info(this.appId+" 使用AppTmpStopFilter 过滤");
			
			return false;
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		
		if( obj instanceof AppTmpStopFilter){
			AppTmpStopFilter f = (AppTmpStopFilter)obj;
			if(f.appId == this.appId){
				return true;
			}
			
		}
		
		return false;
	}
	
	
	
	
	
	

}
