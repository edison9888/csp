
package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.DbHostGroup;

/**
 * ��ȡ���������ݿ⣬��ȡgroup��host�Ķ�Ӧ��ϵ
 * @author xiaodu
 * @version 2011-4-27 ����03:37:34
 */
public class BeiDouAlertDao extends MysqlRouteBase{
public BeiDouAlertDao(){
	super(DbRouteManage.get().getDbRouteByRouteId("BEIDOU_ALERT_URL"));
}
	
public List<DbHostGroup> findHostGroupList(){
		String sql = "select * from db_host_group";
		final List<DbHostGroup> list = new ArrayList<DbHostGroup>(); 
		try {
			this.query(sql, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
				  DbHostGroup po = new DbHostGroup();
				  setDbHostGroup(po, rs);
				  list.add(po);
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}

  private void setDbHostGroup(DbHostGroup po, ResultSet rs) throws SQLException {
    po.setGroupName(rs.getString("group_name"));
    po.setMemberName(rs.getString("member_name"));
    po.setOwnerName(rs.getString("owner_name"));
    po.setComment(rs.getString("comment"));
  }
}
