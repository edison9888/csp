package com.taobao.monitor.common.ao.center;

import java.util.List;

import com.taobao.monitor.common.db.impl.center.BeiDouAlertDao;
import com.taobao.monitor.common.po.DbHostGroup;

public class BeiDouAlertAo {
  private BeiDouAlertDao beiDouAlertDao = new BeiDouAlertDao();
  private static BeiDouAlertAo ao = new BeiDouAlertAo();
  private BeiDouAlertAo() {

  }
  public static BeiDouAlertAo getBeiDouAlertAo() {
    return ao;
  }

  public  List<DbHostGroup> findHostGroupList() {
    return beiDouAlertDao.findHostGroupList();
  }

}
