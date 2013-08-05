
package com.taobao.monitor.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.taobao.monitor.web.ao.MonitorPvAo;
import com.taobao.monitor.web.vo.Pv;

/**
 * 
 * @author xiaodu
 * @version 2010-8-17 ÏÂÎç03:11:33
 */
public class PvRecode {
	
	private static PvRecode pv = new PvRecode();
	private int currentDay;
	
	
	
	private PvRecode(){
		init();
	}
	
	private void init(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");		
		int day = Integer.parseInt(sdf.format(new Date()));
		if(currentDay != day){
			currentDay = day;
			Pv pv = MonitorPvAo.get().findPv(currentDay);
			if(pv==null){
				MonitorPvAo.get().addRecordPv(currentDay, 0, 0);
			}
		}
	}
	
	
	public static PvRecode get(){
		return pv;
	}
	
	
	public void pv(HttpServletRequest request){		
		init();			
		MonitorPvAo.get().updateRecordPv(currentDay, 1, 0);
	}
	
	

}
