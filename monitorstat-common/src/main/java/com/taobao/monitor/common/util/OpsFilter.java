package com.taobao.monitor.common.util;

import java.util.HashMap;

public class OpsFilter {
  public final static HashMap<String, Boolean> dbCheckMap = new HashMap<String, Boolean>();  
  static{

    dbCheckMap.put("mysql_buffer", true);
    dbCheckMap.put("mysql_other", true);
    dbCheckMap.put("mysql_taobao", true);
    dbCheckMap.put("oracle_buffer", true);
    dbCheckMap.put("oracle_other", true);
    dbCheckMap.put("oracle_rac", true);
// 暂时用不到
//    dbCheckMap.put("db_buffer", true);   
//    dbCheckMap.put("cassandra_other", true);   
//    dbCheckMap.put("db_cassandra", true);
//    dbCheckMap.put("hbase_buffer", true);
//    dbCheckMap.put("hbase_other", true);
//    dbCheckMap.put("mongo_buffer", true);
//    dbCheckMap.put("mongo_other", true);
//    dbCheckMap.put("db_mysql", true);
//    dbCheckMap.put("mysql_alibank", true);
//    dbCheckMap.put("mysql_etao", true);
//    dbCheckMap.put("ob_etao", true);
//    dbCheckMap.put("ob_buffer", true);
//    dbCheckMap.put("ob_other", true);
//    dbCheckMap.put("ob_taobao", true);
//    dbCheckMap.put("ob_tmall", true);
//    dbCheckMap.put("oceanbase", true);
//    dbCheckMap.put("ob_buffer", true);
  }
  
  /**
   * 是否是要处理的数据 
   * @param opsName
   * @return
   */
  public static boolean filterDb(String opsName){
    try {
      return dbCheckMap.containsKey(opsName);
    } catch (Exception e) {
      return false;
    }
  }
  
  public static boolean filterDbIndex(String opsName){
    try {
      if(opsName.indexOf("oracle_") ==0 || opsName.indexOf("mysql_") ==0)
        return true;
    } catch (Exception e) {
    }
    return false;
  }
}
















