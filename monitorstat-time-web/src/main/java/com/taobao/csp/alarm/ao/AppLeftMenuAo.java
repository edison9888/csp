package com.taobao.csp.alarm.ao;

import java.util.List;

import com.taobao.csp.alarm.dao.AppLeftMenuDao;
import com.taobao.csp.time.web.po.LeftMenuPo;

public class AppLeftMenuAo {
	private static AppLeftMenuAo ao = new AppLeftMenuAo();
	private AppLeftMenuDao dao = new AppLeftMenuDao();
	public static AppLeftMenuAo get(){
		return ao;
	}
	private AppLeftMenuAo(){
	}
	
	public boolean update(LeftMenuPo po){
		return dao.update(po);
	}
	
	public boolean insert(LeftMenuPo po){
		return dao.insert(po);
	}
	public List<LeftMenuPo> find(final String appName){
		return dao.find(appName);
	}
	public static void main(String args[]){
		List<LeftMenuPo> list = ao.get().find("detail");
	}
}
