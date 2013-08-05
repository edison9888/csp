package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.tfs.CspTfsProviderDetailPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * ´¦ÀíTfsµÄdao
 * 
 * @author zhongting.zy
 * 
 */
public class CspTfsProviderDao extends MysqlRouteBase {
  public CspTfsProviderDao() {
    super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
  }
  
  private static final Logger logger = Logger
      .getLogger(CspTfsProviderDao.class);

  public List<CspTfsProviderDetailPo> getTfsListByDate(String collectDay) {
    String sql = "select * from csp_tfs_provider_summary where collect_time=?";
    final List<CspTfsProviderDetailPo> list = new ArrayList<CspTfsProviderDetailPo>();
    try {
      this.query(sql, new Object[] { collectDay }, new SqlCallBack() {
        @Override
        public void readerRows(ResultSet rs) throws Exception {
          CspTfsProviderDetailPo po = new CspTfsProviderDetailPo();
          po.setAppName(rs.getString("app_name"));
          po.setOperType(rs.getString("oper_type"));
          po.setOperTimes(rs.getLong("oper_times"));
          po.setOperSize(rs.getLong("oper_size"));
          po.setOperRt(rs.getLong("oper_rt"));
          po.setOperSucc(rs.getLong("oper_succ"));
          po.setCacheHitRatio(rs.getFloat("cache_hit_ratio"));
          po.setRushOperTimes(rs.getLong("rush_oper_times"));
          po.setRushOperSize(rs.getLong("rush_oper_size"));
          po.setRushOperRt(rs.getLong("rush_oper_rt"));
          po.setRushOperSucc(rs.getLong("rush_oper_succ"));
          po.setRushcacheHitRatio(rs.getFloat("rush_cache_hit_ratio"));
          po.setCollectTime(rs.getString("collect_time"));
          list.add(po);
        }
      });
    } catch (Exception e) {
      logger.error("", e);
    }
    return list;
  }

  public static void main(String[] args) {
    System.out.println(new CspTfsProviderDao().getTfsListByDate("2012-02-29")
        .size());
  }
}