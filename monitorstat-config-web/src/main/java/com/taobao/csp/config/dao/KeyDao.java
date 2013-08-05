package com.taobao.csp.config.dao;

import java.sql.ResultSet;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.KeyPo;
public class KeyDao extends MysqlRouteBase {
	
	private static final Logger logger = Logger.getLogger(KeyDao.class);
	public KeyPo getKeyPo(int keyId){
		String sql = "select * from ms_monitor_key where key_id=?";
		
		final KeyPo po = new KeyPo();
		
		try {
			this.query(sql, new Object[]{keyId}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					
				}});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return po.getKeyName()==null?null:po;
	}
}
