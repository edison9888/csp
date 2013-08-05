
package com.taobao.monitor.alarm.n.filter;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.n.AlarmContext;

/**
 * 
 * @author xiaodu
 * @version 2011-2-28 ����04:12:51
 */
public class AppKeyTmpStopFilter extends Filter{
	private static final Logger logger =  Logger.getLogger(AppKeyTmpStopFilter.class);
	private int appId;
	private int keyId;

	public AppKeyTmpStopFilter(int appId,int keyId,long startExpiry, long endExpiry) {
		super(startExpiry, endExpiry);
		this.appId = appId;
		this.keyId = keyId;
	}

	@Override
	protected boolean valid(AlarmContext context) {
		
		if(context.getAppId() == this.appId){
			if(context.getKeyId()==this.keyId){
				logger.info(this.appId+" ʹ��AppTmpStopFilter ����");
				
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		
		if( obj instanceof AppKeyTmpStopFilter){
			AppKeyTmpStopFilter f = (AppKeyTmpStopFilter)obj;
			if(f.appId == this.appId&&this.keyId == f.keyId){
				return true;
			}
			
		}
		
		return false;
	}
	
	
	
	
	
	

}
