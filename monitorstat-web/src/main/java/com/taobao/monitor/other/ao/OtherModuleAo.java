
package com.taobao.monitor.other.ao;

import com.taobao.monitor.other.dao.OtherModuleDao;
import com.taobao.monitor.other.tbsession.TbSeesionLog;

/**
 * 
 * @author xiaodu
 * @version 2011-5-13 ионГ10:07:48
 */
public class OtherModuleAo {
	
	public static OtherModuleAo ao = new OtherModuleAo();
	
	
	private OtherModuleDao dao = new OtherModuleDao();
	
	
	public static OtherModuleAo get(){
		return ao;
	}
	
	
	
	
	public void addTbsessionLog(TbSeesionLog log){
		dao.addTbsessionLog(log);
	}
	

}
