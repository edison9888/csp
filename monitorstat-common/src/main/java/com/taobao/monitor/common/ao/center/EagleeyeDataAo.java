package com.taobao.monitor.common.ao.center;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.impl.center.EagleeyeDataDao;
import com.taobao.monitor.common.po.CspCallsRelationship;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestDay;
import com.taobao.monitor.common.po.CspEagleeyeApiRequestPart;

/**
 * 下午5:30:37
 */
public class EagleeyeDataAo {

  private EagleeyeDataDao eagleeyeDataDao = new EagleeyeDataDao();
  private static Logger logger = Logger.getLogger(EagleeyeDataAo.class); 
  
  private static EagleeyeDataAo ao = new EagleeyeDataAo();
  private EagleeyeDataAo(){}

  public static EagleeyeDataAo get(){
    return ao;
  }
  
//  public Map<String,List<CspCallsRelationship>> findCallsRelationship(String origin){
//    return eagleeyeDataDao.findCallsRelationship(origin);
//  }
//
//  public Set<String> findExistKeyFromCallsRelationship(Set<String> set){
//    return eagleeyeDataDao.findExistKeyFromCallsRelationship(set);
//  }


  public void deleteCallsRelationship(String source){
    eagleeyeDataDao.deleteCallsRelationship(source);
  }

  public void addCallsRelationship(CspCallsRelationship call){
    eagleeyeDataDao.addCallsRelationship(call);
  }


  public List<CspCallsRelationship> findCallsRelationshipBySourceUrl(String sourceUrl){
    return eagleeyeDataDao.findCallsRelationshipBySourceUrl(sourceUrl);
  }


  public Map<String,List<CspCallsRelationship>> findCallsRelationship(){
    return eagleeyeDataDao.findCallsRelationship();
  }

  //获取下级子节点信息
  public List<CspCallsRelationship> getSubCallRelationShip(String sourceUrl, String origin){
    return eagleeyeDataDao.getSubCallRelationShip(sourceUrl, origin);
  }

  /**
   * 存储单位时间间隔的Eagleeye的数据
   * @param part
   */
  public boolean addCspEagleeyeApiRequestPart(CspEagleeyeApiRequestPart part){
	  try {
		eagleeyeDataDao.addCspEagleeyeApiRequestPart(part);
		return true;
	} catch (SQLException e) {
		logger.error("", e);
	}
	  return false;
  }
  
  /**
   * 存储每天Eagleeye的数据
   * @param part
   */
  public boolean addCspEagleeyeApiRequestDay(CspEagleeyeApiRequestDay day){
	  try {
		eagleeyeDataDao.addCspEagleeyeApiRequestDay(day);
		return true;
	} catch (SQLException e) {
		logger.error("", e);
	}
	  return false;
  }
  
  public CspEagleeyeApiRequestDay searchCspEagleeyeApiRequestDay(String sourcekey, String api_type, String collect_time) {
	  return eagleeyeDataDao.searchCspEagleeyeApiRequestDay(sourcekey, api_type, collect_time);
  }
  
  public CspEagleeyeApiRequestDay searchCspEagleeyeApiRequestDay(String sourcekey, String api_type, String collect_time, String appName) {
	  return eagleeyeDataDao.searchCspEagleeyeApiRequestDay(sourcekey, api_type, appName,collect_time);
  }

  //返回app，collect的所有数据
  public List<CspEagleeyeApiRequestDay> searchCspEagleeyeApiRequestDayList(String api_type, String appName, String collect_time) {
	  return eagleeyeDataDao.searchCspEagleeyeApiRequestDayList(api_type, appName, collect_time);
  }
  
  //返回最近的一条汇总信息
  public CspEagleeyeApiRequestDay searchRecentlyDayPo(String appName, String sourcekey, String api_type) {
	  return eagleeyeDataDao.searchRecentlyDayPo(appName, sourcekey, api_type);
  }

  public List<CspEagleeyeApiRequestPart> searchEagleeyeApiRequestPart(String sourcekey, String api_type, Date collect_timeStart, Date collect_timeEnd) {
	  List<CspEagleeyeApiRequestPart> list = eagleeyeDataDao.searchEagleeyeApiRequestPart(sourcekey, api_type, collect_timeStart, collect_timeEnd); 
	  if(list.size() > 1) {
		  Collections.sort(list, new Comparator<CspEagleeyeApiRequestPart>() {
			@Override
			public int compare(CspEagleeyeApiRequestPart o1, CspEagleeyeApiRequestPart o2) {
				return o2.getCollectTime().compareTo(o1.getCollectTime());
			}
		  });
	  } 
	  return list;
  }
  
  public List<CspEagleeyeApiRequestPart> searchEagleeyeApiRequestPart(String sourcekey, String api_type, Date collect_timeStart, Date collect_timeEnd, String appName) {
	  List<CspEagleeyeApiRequestPart> list = eagleeyeDataDao.searchEagleeyeApiRequestPart(sourcekey, api_type, appName, collect_timeStart, collect_timeEnd); 
	  if(list.size() > 1) {
		  Collections.sort(list, new Comparator<CspEagleeyeApiRequestPart>() {
			@Override
			public int compare(CspEagleeyeApiRequestPart o1, CspEagleeyeApiRequestPart o2) {
				return o2.getCollectTime().compareTo(o1.getCollectTime());
			}
		  });
	  } 
	  return list;
  }
  
  public Set<String> getDistinctSourceKey(String api_type, Date collect_timeStart, Date collect_timeEnd) {
	  return eagleeyeDataDao.getDistinctSourceKey(api_type, collect_timeStart, collect_timeEnd);
  }
  
  public Set<String> getDistinctSourceKey(String api_type, String appName, Date collect_timeStart, Date collect_timeEnd) {
	  return eagleeyeDataDao.getDistinctSourceKey(api_type, appName, collect_timeStart, collect_timeEnd);
  }
  
  public void deleteEagleeyePartBeforeDate(Date date){
	  eagleeyeDataDao.deleteEagleeyePartBeforeDate(date);
  }
  public static void main(String[] args) {
//	  String sourcekey = "com.taobao.item.service.ItemBidService_updateItemQuantity~liLlA";
	  String appName = "itemcenter";
	  String api_type = "child_key";
	  String collect_time ="2012-12-17";
	  List<CspEagleeyeApiRequestDay> list = EagleeyeDataAo.get().searchCspEagleeyeApiRequestDayList(api_type, appName, collect_time);
	  System.out.println(list);
  }
}
