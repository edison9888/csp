package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.HistoryGraphPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.util.Constants;

public class MonitorDayDao extends MysqlRouteBase {

  public MonitorDayDao(){
    super(DbRouteManage.get().getDbRouteByRouteId("Main_DAY"));
  }
  /**
   * In and Out all Search the same table
   */
  private static final Logger logger =  Logger.getLogger(MonitorDayDao.class);
  public List<HistoryGraphPo> getAppHistoryCallProvider(String startDate, String endDate, Integer appId, final String interfaceName) {
    List<HistoryGraphPo> list = new ArrayList<HistoryGraphPo>();
    
    logger.info("sql MonitorDayDao:getAppHistoryCallProvider");
    Object[] obj = null;
    String sql = null;
    if(interfaceName == null) { //查应用
      sql = "select c.m_data as CALL_SUM,c.collect_time as collect_time,k.key_value as keyName from ms_monitor_count c,ms_monitor_key k where k.key_id = c.key_id ";
      sql += " and c.app_id = ? and c.collect_time BETWEEN ? and ?";
      obj = new Object[]{appId, startDate, endDate};
    } else {  //查接口
      String tmpInterfaceName = interfaceName + "_" + Constants.COUNT_TIMES_FLAG;  //处理后只查询总数
      sql = "select c.m_data as CALL_SUM,c.collect_time as collect_time,k.key_value as keyName from ms_monitor_count c,ms_monitor_key k where k.key_id = c.key_id ";
      sql += " and c.app_id = ? and c.collect_time BETWEEN ? and ? and k.key_value = ?";
      obj = new Object[]{appId, startDate, endDate, tmpInterfaceName};  //加后缀   
    }
    final HashMap<String, HistoryGraphPo> map = new HashMap<String, HistoryGraphPo>();
    try {
      this.query(sql, obj, new SqlCallBack() {
        @Override
        public void readerRows(ResultSet rs) throws Exception {
          if(interfaceName == null) {
            String keyName = rs.getString("keyName");
            if(!keyName.startsWith("IN_HSF-ProviderDetail_") || !keyName.endsWith("_" + Constants.COUNT_TIMES_FLAG)) {
              return; //过滤掉非HSF服务的情况
            }
          }
          
          
          String key = rs.getString("collect_time");
          if(!map.containsKey(key)) {
            HistoryGraphPo sum = new HistoryGraphPo();
            sum.setCallAllNum(rs.getLong("CALL_SUM"));
            sum.setCollectDate(rs.getDate("collect_time"));
            map.put(key, sum);
          } else {
            HistoryGraphPo sum = map.get(key);
            sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("CALL_SUM"));
          }
        }
      });
    } catch (Exception e) {
      logger.error("", e);
    }
    if(map.size() > 0) {
      list = new ArrayList<HistoryGraphPo>(map.values());
    } else {
      list = new ArrayList<HistoryGraphPo>();
    }   
    return list;
  }
  
  public List<HistoryGraphPo> getAppHistoryCallConsumer(String startDate, String endDate, Integer appId, final String interfaceName) {
    List<HistoryGraphPo> list = new ArrayList<HistoryGraphPo>();
    
    logger.info("sql MonitorDayDao:getAppHistoryCallConsumer");
    Object[] obj = null;
    String sql = null;
    if(interfaceName == null) { //查应用
      sql = "select c.m_data as CALL_SUM,c.collect_time as collect_time,k.key_value as keyName from ms_monitor_count c,ms_monitor_key k where k.key_id = c.key_id ";
      sql += " and c.app_id = ? and c.collect_time BETWEEN ? and ?";
      obj = new Object[]{appId, startDate, endDate};
    } else {  //查接口
      
      String tmpInterfaceName = "";
      //做接口转换
      //IN_HSF-ProviderDetail_com.taobao.forest.service.RemoteWriteService:1.0.0_addStdPropertyValueOrSubWithoutCPV
      if(interfaceName.startsWith("IN_HSF-ProviderDetail_")) {  //把provider方变为consumer方
        tmpInterfaceName = interfaceName.replace("IN_HSF-ProviderDetail_", "OUT_HSF-Consumer_") + "_" + Constants.COUNT_TIMES_FLAG;
      }
      sql = "select c.m_data as CALL_SUM,c.collect_time as collect_time,k.key_value as keyName from ms_monitor_count c,ms_monitor_key k where k.key_id = c.key_id ";
      sql += " and c.app_id = ? and c.collect_time BETWEEN ? and ? and k.key_value = ?";
      obj = new Object[]{appId, startDate, endDate, tmpInterfaceName};  //加后缀   
    }
    final HashMap<String, HistoryGraphPo> map = new HashMap<String, HistoryGraphPo>();
    try {
      this.query(sql, obj, new SqlCallBack() {
        @Override
        public void readerRows(ResultSet rs) throws Exception {
          if(interfaceName == null) {
            String keyName = rs.getString("keyName");
            if(!keyName.startsWith("OUT_HSF-Consumer_") || !keyName.endsWith("_" + Constants.COUNT_TIMES_FLAG)) {
              return; //过滤掉非HSF 消费的情况
            }
          }
          
          String key = rs.getString("collect_time");
          if(!map.containsKey(key)) {
            HistoryGraphPo sum = new HistoryGraphPo();
            sum.setCallAllNum(rs.getLong("CALL_SUM"));
            sum.setCollectDate(rs.getDate("collect_time"));
            map.put(key, sum);
          } else {
            HistoryGraphPo sum = map.get(key);
            sum.setCallAllNum(sum.getCallAllNum() + rs.getLong("CALL_SUM"));
          }
        }
      });
    } catch (Exception e) {
      logger.error("", e);
    }
    if(map.size() > 0) {
      list = new ArrayList<HistoryGraphPo>(map.values());
    } else {
      list = new ArrayList<HistoryGraphPo>();
    }   
    return list;
  }
}
