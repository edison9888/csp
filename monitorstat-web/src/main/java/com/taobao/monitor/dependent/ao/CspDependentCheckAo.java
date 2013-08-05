
package com.taobao.monitor.dependent.ao;

import java.util.List;

import com.taobao.monitor.dependent.dao.CspDependentCheckDao;
import com.taobao.monitor.dependent.po.AutoCheckDependentPo;

/**
 * 
 * @author xiaodu
 * @version 2011-5-10 下午01:37:17
 */
public class CspDependentCheckAo {
	
	private static CspDependentCheckAo ao = new CspDependentCheckAo();
	
	private CspDependentCheckDao dao = new CspDependentCheckDao();
	
	private CspDependentCheckAo(){}
	
	public static CspDependentCheckAo get(){
		return ao;
	}

	
	/**
	 * 根执行状态，查找出所有的记录
	 * @return
	 */
	public List<AutoCheckDependentPo> findAllunRunCheck(String status){
		return dao.findAllunRunCheck(status);
	}
	
	/**
	 * 修改记录
	 * @param po
	 */
	public boolean updateDependentCheck(AutoCheckDependentPo po){
		return dao.updateDependentCheck(po);
	}
	
	
}
