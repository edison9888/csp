package com.taobao.csp.alarm.ao;

import java.util.Date;
import java.util.List;

import com.taobao.csp.alarm.dao.BaselineCheckDao;
import com.taobao.csp.time.web.po.BaselineCheckPo;

public class BaselineCheckAo {
	public static BaselineCheckAo ao = new BaselineCheckAo();
	private BaselineCheckDao dao = new BaselineCheckDao();
	private BaselineCheckAo(){
	}
	public static BaselineCheckAo get(){
		return ao;
	}
	public List<BaselineCheckPo> findByAppName(String appName){
		return dao.findByAppName(appName);
	}
	public boolean updateState(BaselineCheckPo po){
		return dao.updateState(po);
	}
	public boolean insert(BaselineCheckPo po){
		return dao.insert(po);
	}
	public static void main(String args[]){
		BaselineCheckPo po = new BaselineCheckPo();
		po.setAppName("detail2sdfsdf");
		po.setKeyName("HSF-Consumer");
		po.setPropertyName("E-times");
		po.setScope("APP");
		po.setState("t");
		po.setWeekDay(3);
		po.setProcessTime(new Date());
		BaselineCheckAo.get().updateState(po);
		System.exit(0);
	}
}
