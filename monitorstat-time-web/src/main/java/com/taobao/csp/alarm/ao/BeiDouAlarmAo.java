package com.taobao.csp.alarm.ao;

import java.util.List;

import com.taobao.csp.alarm.dao.BeiDouAlarmDao;
import com.taobao.csp.time.web.po.BeiDouAlarmRecordPo;


public class BeiDouAlarmAo {
	private static BeiDouAlarmAo ao = new BeiDouAlarmAo();
	private static BeiDouAlarmDao dao = new BeiDouAlarmDao();
	public static BeiDouAlarmAo get(){
		return ao;
	}
	private BeiDouAlarmAo(){
	}
	public List<BeiDouAlarmRecordPo> getDbAlarmInfoRecently(Integer n){
		return dao.getDbAlarmInfoRecently(n);
	}
}
