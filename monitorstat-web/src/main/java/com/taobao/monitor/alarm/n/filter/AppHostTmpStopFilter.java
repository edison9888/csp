
package com.taobao.monitor.alarm.n.filter;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 下午04:12:51
 */
public class AppHostTmpStopFilter extends Filter{
	private static final Logger logger =  Logger.getLogger(AppHostTmpStopFilter.class);
	private int appId;
	private int hostId;

	public AppHostTmpStopFilter(int appId,int hostId,long startExpiry, long endExpiry) {
		super(startExpiry, endExpiry);
		this.appId = appId;
		this.hostId = hostId;
	}

	@Override
	protected boolean valid(AlarmContext context) {
		
		if(context.getAppId() == this.appId){
			if(context.getSiteId()==this.hostId){
				logger.info(this.appId+" 使用AppTmpStopFilter 过滤");
				
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		
		if( obj instanceof AppHostTmpStopFilter){
			AppHostTmpStopFilter f = (AppHostTmpStopFilter)obj;
			if(f.appId == this.appId&&this.hostId == f.hostId){
				return true;
			}
			
		}
		
		return false;
	}
	
	
	
	
	
	

}
