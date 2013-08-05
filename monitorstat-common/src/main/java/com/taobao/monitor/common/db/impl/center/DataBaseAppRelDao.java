package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRoute;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.DataBaseInfoPo;
import com.taobao.monitor.common.po.DatabaseAppRelPo;

/**
 * Database��App����ʵ���dao
 * @author wuhaiqian.pt
 *
 */
public class DataBaseAppRelDao extends MysqlRouteBase {
	
	private static final Logger logger = Logger.getLogger(DataBaseAppRelDao.class);

	public DataBaseAppRelDao(){
		super();
	}
	public DataBaseAppRelDao(DbRoute defaultDbRoute){
		super(defaultDbRoute);
	}
	/**
	 * ���rel
	 * @param DatabaseAppRelPo
	 */
	public boolean addRel(DatabaseAppRelPo po) {
		try {
			
			String sql = "insert into ms_database_app_rel "
				+ "(database_id, app_id, app_type) values(?,?,?)";
			this.execute(sql, new Object[] {po.getDatabaseId(), po.getAppId(), po.getAppType()});
		} catch (Exception e) {
			logger.error("addRel ����,", e);
			return false;
		}
		
		return true;
	}
	/**
	 * ���database-app-rel��list
	 * @param List<DatabaseAppRelPo>
	 */
	public boolean addRel(List<DatabaseAppRelPo> list) {
		try {
			for(DatabaseAppRelPo po : list) {
				String sql = "insert into ms_database_app_rel "
					+ "(database_id, app_id, app_type) values(?,?,?)";
				this.execute(sql, new Object[] {po.getDatabaseId(), po.getAppId(), po.getAppType()});
			}
		} catch (Exception e) {
			logger.error("addRel ����,", e);
			return false;
		}
		
		return true;
	}

	/**
	 * ɾ��rel
	 * @param DatabaseAppRelPo
	 */
	public boolean deleteRel(DatabaseAppRelPo po) {
		String sql = "delete from ms_database_app_rel where database_id=? and app_id=? and app_type=? ";
		try {
			this.execute(sql, new Object[] {po.getDatabaseId() , po.getAppId(), po.getAppType()});
		} catch (SQLException e) {
			logger.error("deleteRel: ", e);
			return false;
		}
		
		return true;
	}
	
	/**
	 * ����rel
	 * @param DatabaseAppRelPo
	 */
	public DatabaseAppRelPo findRel(DatabaseAppRelPo relPo) {
		String sql = "select * from ms_database_app_rel where database_id=? and app_id=? and app_type = ? ";
		
		final DatabaseAppRelPo po = new DatabaseAppRelPo();
		try {
			this.query(sql, new Object[]{relPo.getDatabaseId(),relPo.getAppId(), relPo.getAppType()},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setAppId(rs.getInt("app_id"));
					po.setAppType(rs.getString("app_type"));
					po.setDatabaseId(rs.getInt("database_id"));
				}
			});
		} catch (Exception e) {
			logger.error("findRel: ", e);
		}
		
		return po;
	}
	
	
	/**
	 * app_id,��app_type����rel��Ψһ�ԣ��ʸ���app_id,app_type���ж��Ƿ��Ѿ�����rel
	 * @param DatabaseAppRelPo
	 */
	public boolean isExistedRel(DatabaseAppRelPo relPo) {
		String sql = "select * from ms_database_app_rel where app_id=? and app_type = ? ";
		
		final DatabaseAppRelPo po = new DatabaseAppRelPo();
		try {
			this.query(sql, new Object[]{relPo.getAppId(), relPo.getAppType()},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					po.setAppId(rs.getInt("app_id"));
					po.setAppType(rs.getString("app_type"));
				}
			});
		} catch (Exception e) {
			logger.error("findRel: ", e);
		}
		
		if(po.getAppId() != 0 && po.getAppType() != null) {
			
			return true;
		} else {
			
			return false;
		}
	}
	
	/**
	 * ����appId���Ұ�����databaseInfo��rel��AppInfoPo���б�
	 * @param DatabaseAppRelPo
	 */
	public List<AppInfoPo> findDatabaseRel(int appId) {

		String sql = "SELECT a.app_status,a.app_id, a.app_name, a.feature, a.sort_index, a.ops_name, a.group_name, r.app_type, a.day_deploy, a.time_deploy, " +
		"d.database_id, d.database_name, d.database_url, d.database_user, d.database_pwd, d.database_desc, d.database_type " +
		"FROM ms_monitor_app a, ms_database_app_rel r, ms_database_info d " +
		" WHERE a.app_id = r.app_id and r.database_id = d.database_id and a.app_id = ?";

		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();
		
		try {
			this.query(sql, new Object[]{appId},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
		
					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setDataBaseInfoPo(setDataBaseInfo("d",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByDatabaseName����", e);
		}
		return appInfoPoList;
	}
	/**
	 *  ����appType�������л�û�й������ݿ��AppInfoPO
	 * @param appType
	 */
	public List<AppInfoPo> findAllAppWithoutRel(String appType) {
		
		final String type = appType;
		String sql = "SELECT a.app_status,a.app_id, a.app_name, a.feature, a.sort_index, a.ops_name, a.group_name,  a.day_deploy, a.time_deploy " +
		" FROM ms_monitor_app a " + 
		"WHERE a.app_id NOT IN ( " +
		"select d.app_id from ms_database_app_rel d where app_type = ? )";
		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();
		
		try {
			this.query(sql, new Object[]{appType},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					
					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(type);
					//po.setDataBaseInfoPo(setDataBaseInfo("d",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppWithoutRel����", e);
		}
		return appInfoPoList;
	}
	
	/**
	 * ����rel
	 * @param DatabaseAppRelPo
	 * @return
	 */
	public boolean updateRel(DatabaseAppRelPo relPo){
		String sql = "update ms_database_app_rel set database_id= ?,app_type = ? where app_id = ?";
		try {
			this.execute(sql, new Object[]{relPo.getDatabaseId(),relPo.getAppType(), relPo.getAppId()});
		} catch (SQLException e) {
			logger.error("updateRel����", e);
			return false;
		}
		return true;
	}
	
	/**
	 * ����databaseId��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDatabaseId(int databaseId) {
	
		String sql = "SELECT a.app_status,a.app_id, a.app_name, a.feature, a.sort_index, a.ops_name, a.group_name, a.day_deploy, a.time_deploy, r.app_type, " +
		"d.database_id, d.database_name, d.database_url, d.database_user, d.database_pwd, d.database_desc, d.database_type " +
		"FROM ms_monitor_app a, ms_database_app_rel r, ms_database_info d " +
		" WHERE a.app_id = r.app_id and r.database_id = d.database_id and d.database_id = ?";

		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new Object[]{databaseId},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setDataBaseInfoPo(setDataBaseInfo("d",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByDatabaseId: ", e);
		}
		return appInfoPoList;
	}
	
	/**
	 * ����databaseName��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDatabaseName(String databaseName) {
	
		String sql = "SELECT a.app_status,a.app_id, a.app_name, a.feature, a.sort_index, a.ops_name, a.group_name, r.app_type, a.day_deploy, a.time_deploy, " +
				"d.database_id, d.database_name, d.database_url, d.database_user, d.database_pwd, d.database_desc, d.database_type " +
				"FROM ms_monitor_app a, ms_database_app_rel r, ms_database_info d " +
				" WHERE a.app_id = r.app_id and r.database_id = d.database_id and d.database_name = ?";

		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new Object[]{databaseName},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setDataBaseInfoPo(setDataBaseInfo("d",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByDatabaseName����", e);
		}
		return appInfoPoList;
	}
	
	
	/**
	 * ����databaseName��appType��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDbNameAndAppType(String databaseName, String appType) {
	
		String sql = "SELECT a.app_status,a.app_id, a.app_name, a.feature, a.sort_index, a.ops_name, a.group_name,  a.day_deploy, a.time_deploy, r.app_type, " +
				"d.database_id, d.database_name, d.database_url, d.database_user, d.database_pwd, d.database_desc, d.database_type " +
				"FROM ms_monitor_app a, ms_database_app_rel r, ms_database_info d " +
				" WHERE a.app_id = r.app_id and r.database_id = d.database_id and d.database_name = ? and r.app_type = ?";

		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new Object[]{databaseName, appType},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {

					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setDataBaseInfoPo(setDataBaseInfo("d",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByDbNameAndAppType����", e);
		}
		return appInfoPoList;
	}
	/**
	 * ����databaseId��appType��ѯ������ص�AppInfoPO
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppByDatabaseIdAndAppType(int databaseId, String appType) {
		
		String sql = "SELECT a.app_status,a.app_id, a.app_name, a.feature, a.sort_index, a.ops_name, a.group_name,  a.day_deploy, a.time_deploy, r.app_type, " +
		"d.database_id, d.database_name, d.database_url, d.database_user, d.database_pwd, d.database_desc, d.database_type " +
		"FROM ms_monitor_app a, ms_database_app_rel r, ms_database_info d " +
		" WHERE a.app_id = r.app_id and r.database_id = d.database_id and d.database_id = ? and r.app_type = ?";
		
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();
		
		try {
			this.query(sql, new Object[]{databaseId, appType},new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					
					AppInfoPo po = new AppInfoPo();
					po = setAppInfoPo("a",rs);
					po.setAppType(rs.getString("r.app_type"));
					po.setDataBaseInfoPo(setDataBaseInfo("d",rs));
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppByDbNameAndAppType����", e);
		}
		return appInfoPoList;
	}
	
	public AppInfoPo setAppInfoPo(String alias,ResultSet rs) throws Exception {
		
		AppInfoPo po = new AppInfoPo();
		po.setAppId(rs.getInt(alias + ".APP_ID"));
		po.setAppName(rs.getString(alias + ".APP_NAME"));
		po.setFeature(rs.getString(alias + ".feature"));
		po.setSortIndex(rs.getInt(alias + ".SORT_INDEX"));
		po.setOpsName(rs.getString(alias + ".OPS_NAME"));
		po.setGroupName(rs.getString(alias + ".GROUP_NAME"));
		po.setDayDeploy(rs.getInt(alias + ".day_deploy"));
		po.setTimeDeploy(rs.getInt(alias + ".time_deploy"));
		po.setAppStatus(rs.getInt(alias + ".app_status"));
		return po;
	}
	
	public DataBaseInfoPo setDataBaseInfo(String alias,ResultSet rs) throws Exception {
		
		DataBaseInfoPo dbInfoPo = new DataBaseInfoPo();
		dbInfoPo.setDbId(rs.getInt(alias + ".database_id"));
		dbInfoPo.setDbName(rs.getString(alias + ".database_name"));
		dbInfoPo.setDbUrl(rs.getString(alias + ".database_url"));
		dbInfoPo.setDbUser(rs.getString(alias + ".database_user"));
		dbInfoPo.setDbPwd(rs.getString(alias + ".database_pwd"));
		dbInfoPo.setDbDesc(rs.getString(alias + ".database_desc"));
		dbInfoPo.setDbType(rs.getInt(alias + ".database_type"));
		return dbInfoPo;
	}
}
