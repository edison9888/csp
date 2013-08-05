package com.taobao.csp.loadrun.core.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.fetch.UrlElement;

public class LoadrunDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(LoadrunDao.class);
	
	private static final String MAIN = "Main";
	
	private static LoadrunDao dao = new LoadrunDao();
	
	private LoadrunDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload"));
	}

	public static LoadrunDao getInstance() {
		return dao;
	}
	
	public List<UrlElement> findUrlsByApp(String opsName) {
		String sql = "select * from csp_app_url_relation where app_name=?";

		final List<UrlElement> list = new ArrayList<UrlElement>();

		try {
			this.query(sql, new Object[] { opsName }, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					String address = rs.getString("app_url");
					String danymicUrl = rs.getString("dynamic_url");
					
					UrlElement element = new UrlElement();
					element.setAddress(address);
					if (danymicUrl.equals("yes")) {
						element.setDanymicUrl(true);
					} else {
						element.setDanymicUrl(false);
					}
					
					list.add(element);
				}
			}, DbRouteManage.get().getDbRouteByRouteId(MAIN));
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
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
			}, DbRouteManage.get().getDbRouteByRouteId(MAIN));
		} catch (Exception e) {
			logger.error("findAllDataBaseInfo: ", e);
			e.printStackTrace();
		}
		return dbInfoPoPoList;
	}

}
