
package com.taobao.monitor.web.ao;

import java.util.List;

import com.taobao.monitor.web.core.dao.impl.MonitorPvDao;
import com.taobao.monitor.web.vo.Pv;

/**
 *
 * @author xiaodu
 * @version 2010-4-19 ÉÏÎç11:36:00
 */
public class MonitorPvAo {


	private static MonitorPvAo  ao = new MonitorPvAo();
	private MonitorPvDao dao = new MonitorPvDao();


	private MonitorPvAo(){
		
		
	}


	public static  MonitorPvAo get(){
		return ao;
	}


	public Pv findPv(int start){
    	return dao.findPv(start);
    }
    
    public void addRecordPv(int collectDay ,long pv,long uv){
    	dao.addRecordPv(collectDay, pv, uv);
    }
    
    public void updateRecordPv(int collectDay ,long pv,long uv){
    	dao.updateRecordPv(collectDay, pv, uv);
    }
    
    public List<Pv> findPv(int start,int end){
    	return dao.findPv(start, end);
    }
	
}
