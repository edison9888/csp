package com.taobao.csp.loadrun.core.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * 数据库实体的DAO
 * @author wuhaiqian.pt
 *
 */
public class DataBaseInfoDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(DataBaseInfoDao.class);
	
	public DataBaseInfoDao(){
		super();
	}
	
	public DataBaseInfoDao(DbRoute defaultDbRoute){
		super(defaultDbRoute);
	}
	
	/**
	 * 获取全部dataBaseInfoPo
	 * 
	 * @return
	 */
	public List<DataBaseInfoPo> findAllDataBaseInfo() {
		String sql = "select * from MS_DATABASE_INFO";

		final List<DataBaseInfoPo> dbInfoPoPoList = new ArrayList<DataBaseInfoPo>();

		try {
			this.query(sql, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					DataBaseInfoPo po = new DataBaseInfoPo();
					po.setDbId(rs.getInt("database_id"));
					po.setDbName(rs.getString("database_name"));
					po.setDbUser(rs.getString("database_user"));
					po.setDbPwd(rs.getString("database_pwd"));
					po.setDbUrl(rs.getString("database_url"));
					po.setDbDesc(rs.getString("database_desc"));
					po.setDbType(rs.getInt("database_type"));
					
					dbInfoPoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllDataBaseInfo: ", e);
			e.printStackTrace();
		}
		return dbInfoPoPoList;
	}
	
	/**
	 * 根据id获得dataBaseInfoPo
	 * 
	 * @return
	 */
	public DataBaseInfoPo findDataBaseInfoById(int id) {
		String sql = "select * from MS_DATABASE_INFO where database_id = ?";

		final DataBaseInfoPo po = new DataBaseInfoPo();
		try {
			this.query(sql, new Object[]{id},new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					//DataBaseInfoPo po = new DataBaseInfoPo();
					po.setDbId(rs.getInt("database_id"));
					po.setDbName(rs.getString("database_name"));
					po.setDbUser(rs.getString("database_user"));
					po.setDbPwd(rs.getString("database_pwd"));
					po.setDbUrl(rs.getString("database_url"));
					po.setDbDesc(rs.getString("database_desc"));
					po.setDbType(rs.getInt("database_type"));
				}
			});
		} catch (Exception e) {
			logger.error("findAllDataBaseInfoById: ", e);
		}
		return po;
	}
	
}
