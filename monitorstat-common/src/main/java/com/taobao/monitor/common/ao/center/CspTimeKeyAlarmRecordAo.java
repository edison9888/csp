package com.taobao.monitor.common.ao.center;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.taobao.monitor.common.db.impl.center.CspTimeKeyAlarmRecordDao;
import com.taobao.monitor.common.po.CspTimeKeyAlarmRecordPo;
import com.taobao.monitor.common.util.page.Pagination;

public class CspTimeKeyAlarmRecordAo {
	private static CspTimeKeyAlarmRecordAo ao = new CspTimeKeyAlarmRecordAo();
	private static CspTimeKeyAlarmRecordDao dao = new CspTimeKeyAlarmRecordDao();
	
	private CspTimeKeyAlarmRecordAo(){
	}
	
	public Pagination<CspTimeKeyAlarmRecordPo> findAlarmMsgList(String appName,String keyNamePart,Date from,Date to, int pageNo, int pageSize){
		return dao.findAlarmMsgList(appName, keyNamePart,from,to, pageNo, pageSize);
	}
	
	/**
	 * 新增By中亭，加入order by 选项。 默认按照报警时间逆序排列
	 * @param orderbyCondition
	 * @return
	 */
	public Pagination<CspTimeKeyAlarmRecordPo> findAlarmMsgListByOrder(String appName,String keyNamePart,Date from,Date to, int pageNo, int pageSize, String orderbyCondition, String sortType){
	  if(orderbyCondition == null)
	    orderbyCondition = "alarm_time";
	  if(sortType == null)
	    sortType = "desc";
	  
	  return dao.findAlarmMsgList(appName, keyNamePart,from,to, pageNo, pageSize, orderbyCondition, sortType);
	}
	
	
	public boolean insert(List<CspTimeKeyAlarmRecordPo> list){
		return dao.insert(list);
	}
	
	
	/**
	 * 统计最近N分钟告警的个数
	 *@author xiaodu
	 * @param appName
	 * @param n  单位分钟
	 * @return
	 *TODO
	 */
	public int countRecentlyAlarmNum(String appName,int n){
		return dao.countRecentlyAlarmNum(appName, n);
	}
	
	
	/**
	 * 查找最近N分钟应用的告警信息
	 *@author xiaodu
	 * @param appName
	 * @param n 单位分钟
	 * @return
	 *TODO
	 */
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,int n){
		return dao.findRecentlyAlarmInfo(appName, n);
	}
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,int n,Date time){
		return dao.findRecentlyAlarmInfo(appName, n, time);
	}
	
	/**
	 * 查找最近N分钟应用的告警信息
	 *@author xiaodu
	 * @param appName
	 * @param n 单位分钟
	 * @return
	 *TODO
	 */
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,int n){
		return dao.findRecentlyAlarmInfo(appName, keyName, n);
	}
	
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,int n,Date time){
		return dao.findRecentlyAlarmInfo(appName, keyName, n,time);
	}
	/**
	 * 统计最近N分钟告警的个数
	 *@author xiaodu
	 * @param appName
	 * @param keyname
	 * @param n  单位分钟
	 * @return
	 *TODO
	 */
	public int countRecentlyAlarmNum(String appName,String keyname,int n){
		return dao.countRecentlyAlarmNum(appName, keyname, n);
	}
	/**
	 * 查询给定直接的报警数，全闭包
	 */
  public int countAlarmByTime(String appName,String keyname,String start, String end){
    return dao.countAlarmByTime(appName, keyname, start, end);
  }	
	
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,String property,int n){
		return dao.findRecentlyAlarmInfo(appName, keyName, property, n);
	}
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,String property,int n,Date time){
		return dao.findRecentlyAlarmInfo(appName, keyName, property, n,time);
	}
	
	public List<CspTimeKeyAlarmRecordPo> findRecentlyAlarmInfo(String appName,String keyName,String property,String keySocpe,int n){
		return dao.findRecentlyAlarmInfo(appName, keyName, property, keySocpe, n);
	}
	
	public static CspTimeKeyAlarmRecordAo get(){
		return ao;
	}
	public static void main(String args[]){
		CspTimeKeyAlarmRecordPo po = new CspTimeKeyAlarmRecordPo();
		List<CspTimeKeyAlarmRecordPo> list = new ArrayList<CspTimeKeyAlarmRecordPo>();
		list.add(po);
		po.setAlarm_cause("test");
		po.setIp("testsetst");
		CspTimeKeyAlarmRecordAo.get().insert(list);
		System.exit(0);
	}
}
