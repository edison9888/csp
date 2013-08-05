package com.taobao.monitor.common.ao.center;

import java.util.List;

import com.taobao.monitor.common.db.impl.center.CspUserInfoDao;
import com.taobao.monitor.common.po.CspUserInfoPo;

public class CspUserInfoAo {
	private static CspUserInfoAo ao = new CspUserInfoAo();
	private CspUserInfoDao dao = new CspUserInfoDao();
	private CspUserInfoAo(){
	}
	public static CspUserInfoAo get(){
		return ao;
	}
	public boolean insertCspUserInfo(CspUserInfoPo po){
		return dao.insertCspUserInfo(po);
	}
	
	/**
	 * 通过邮件信息获取用户
	 *@author xiaodu
	 * @param email
	 * @return
	 *TODO
	 */
	public CspUserInfoPo findCspUserInfoByMail(String email){
		return dao.findCspUserInfoByMail(email);
	}
	
	public boolean findCspUserInfo(String mail){
		return dao.findCspUserInfo(mail);
	}
	public List<CspUserInfoPo> findAllCspUserInfo(){
		return dao.findAllCspUserInfo();
	}
	
	public boolean updateCspUserInfo(final CspUserInfoPo po){
		return dao.updateCspUserInfo(po);
	}
	
}
