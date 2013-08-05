package com.taobao.monitor.common.ao.capacity;

import com.taobao.monitor.common.db.impl.capacity.CspLoadRunDao;

/**
 * 
 * @author xiaodu
 * @version 2010-9-2 ионГ09:18:05
 */
public class CspLoadRunAo  {

	private static CspLoadRunAo  ao = new CspLoadRunAo();
	private CspLoadRunDao dao = new CspLoadRunDao();
	
	
	private CspLoadRunAo(){
		
	}

	public static  CspLoadRunAo get(){
		return ao;
	}
	
	
	public double findRecentlyAppLoadRunQps(int appId){
		return dao.findRecentlyAppLoadRunQps(appId);
	}

}
