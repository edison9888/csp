
package com.taobao.monitor.dependent.ao;

import java.util.List;

import com.taobao.monitor.dependent.dao.CspDependentCheckDao;
import com.taobao.monitor.dependent.po.AutoCheckDependentPo;

/**
 * 
 * @author xiaodu
 * @version 2011-5-10 ����01:37:17
 */
public class CspDependentCheckAo {
	
	private static CspDependentCheckAo ao = new CspDependentCheckAo();
	
	private CspDependentCheckDao dao = new CspDependentCheckDao();
	
	private CspDependentCheckAo(){}
	
	public static CspDependentCheckAo get(){
		return ao;
	}

	
	/**
	 * ��ִ��״̬�����ҳ����еļ�¼
	 * @return
	 */
	public List<AutoCheckDependentPo> findAllunRunCheck(String status){
		return dao.findAllunRunCheck(status);
	}
	
	/**
	 * �޸ļ�¼
	 * @param po
	 */
	public boolean updateDependentCheck(AutoCheckDependentPo po){
		return dao.updateDependentCheck(po);
	}
	
	
}
