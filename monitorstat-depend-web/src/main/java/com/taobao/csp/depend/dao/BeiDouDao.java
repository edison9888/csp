
package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * ��ȡ���������ݿ⣬�������ݿ�IP ��Ӧ��Ӧ����Ϣ
 * @author xiaodu
 * @version 2011-4-27 ����03:37:34
 */
public class BeiDouDao extends MysqlRouteBase{
public BeiDouDao(){
	super(DbRouteManage.get().getDbRouteByRouteId("BEIDOU_URL"));
}
	
	
public Map<String,String> findOracleInfo(){
		
		String sql = "select  ip_addr,service from hosts";
		
		final Map<String,String> map = new HashMap<String, String>();
		
		try {
			this.query(sql, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					map.put(rs.getString("ip_addr"), rs.getString("service"));
					
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return map;
	}
}
