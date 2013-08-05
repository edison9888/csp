package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRoute;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppMysqlInfo;
import com.taobao.monitor.common.po.DataBaseInfoPo;
import com.taobao.monitor.common.util.TableNameConverUtil;

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
	 * 添加addDataBaseInfoData
	 * @param dataBaseInfoPo
	 */
	public boolean addDataBaseInfoData(DataBaseInfoPo dataBaseInfoPo) {
		try {
			
			String sql = "insert into MS_DATABASE_INFO "
				+ "(database_id, database_name, database_url, database_user, database_pwd, database_desc,database_type) values(?,?,?,?,?,?,?)";
			this.execute(sql, new Object[] {dataBaseInfoPo.getDbId(), dataBaseInfoPo.getDbName(), dataBaseInfoPo.getDbUrl(),  dataBaseInfoPo.getDbUser(),  
					dataBaseInfoPo.getDbPwd(),  dataBaseInfoPo.getDbDesc(),  dataBaseInfoPo.getDbType()});
		} catch (Exception e) {
			logger.error("addDataBaseInfoData 出错,", e);
			
			return false;
		}
		
		return true;
	}
	
	/**
	 * 删除dataBaseInfoPo
	 * @param database_id
	 */
	public boolean deleteDataBaseInfo(int dbId) {
		String sql = "delete from MS_DATABASE_INFO where database_id=?";
		try {
			this.execute(sql, new Object[] { dbId });
		} catch (SQLException e) {
			logger.error("deleteAppInfoData: ", e);
			return false;
		}
		
		return true;
	}

	/**
	 * 删除dataBaseInfoPo
	 * @param database_id
	 */
	public boolean deleteDataBaseInfo(DataBaseInfoPo dataBaseInfoPo) {
		String sql = "delete from MS_DATABASE_INFO where database_id=?";
		try {
			this.execute(sql, new Object[] { dataBaseInfoPo.getDbId() });
		} catch (SQLException e) {
			logger.error("deleteDataBaseInfo: ", e);
			return false;
		}
		
		return true;
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
	
	/**
	 * 根据dataBaseInfoPo更新
	 * @param HostPo
	 * @return
	 */
	public boolean updateDataBaseInfo(DataBaseInfoPo dataBaseInfoPo){
		String sql = "update MS_DATABASE_INFO set database_name=?,database_user=?,database_pwd=?, database_url=?, database_desc=?, database_type=? where database_id=? ";
		try {
			this.execute(sql, new Object[]{dataBaseInfoPo.getDbName(), dataBaseInfoPo.getDbUser(),  dataBaseInfoPo.getDbPwd(),  dataBaseInfoPo.getDbUrl(), dataBaseInfoPo.getDbDesc(),  dataBaseInfoPo.getDbType(),dataBaseInfoPo.getDbId()});
		} catch (SQLException e) {
			logger.error("updateDataBaseInfo: ", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 查询库表中应用对应的key数量和记录数量
	 * @param dataBaseName
	 * @param date
	 * @return
	 */
	public Map<Integer,AppMysqlInfo> findMysqlDatabaseInfo(String dataBaseName,Date date){
		
		String tableName = TableNameConverUtil.formatTimeTableName(date);
		String sqlKey = "select app_id,count(distinct key_id) as keyCount from "+tableName+" group by app_id" ;
		String sqlcount = "select app_id,count(*) as allCount from "+tableName+" group by app_id" ;
		
		final Map<Integer,AppMysqlInfo> mapApp = new HashMap<Integer, AppMysqlInfo>();
		
		try {
			this.query(sqlKey, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					int keyNum = rs.getInt("keyCount");
					
					AppMysqlInfo info = mapApp.get(appId);
					if(info == null){
						info = new AppMysqlInfo();
						mapApp.put(appId, info);
					}
					info.setAppId(appId);
					info.setKeyNum(keyNum);
					
					
				}},DbRouteManage.get().getDbRouteByRouteId(dataBaseName));
		} catch (Exception e) {
			logger.error("findMysqlDatabaseInfo 出错", e);
		}
		
		try {
			this.query(sqlcount, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					int allCount = rs.getInt("allCount");
					
					AppMysqlInfo info = mapApp.get(appId);
					if(info == null){
						info = new AppMysqlInfo();
						mapApp.put(appId, info);
					}
					info.setAppId(appId);
					info.setDataNum(allCount);
					
					
				}},DbRouteManage.get().getDbRouteByRouteId(dataBaseName));
		} catch (Exception e) {
			logger.error("findMysqlDatabaseInfo 出错", e);
		}
		
		return mapApp;
		
	}
	
	
	/**
	 * 
	 * @param appMysqlInfo
	 */
	public void addAppMysqlInfo(AppMysqlInfo appMysqlInfo){
		
		
		String delete ="delete from ms_monitor_mysql_info where database_id=? and app_id=? and collect_time=?";
		try {
			this.execute(delete,new Object[]{appMysqlInfo.getDataBaseId(),appMysqlInfo.getAppId(),appMysqlInfo.getCollectTime()});
		} catch (SQLException e1) {
			logger.error("addAppMysqlInfo 出错", e1);
		}
		
		String sql = "insert into ms_monitor_mysql_info(database_id,app_id,key_num,data_num,collect_time) values(?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[]{appMysqlInfo.getDataBaseId(),appMysqlInfo.getAppId(),appMysqlInfo.getKeyNum(),appMysqlInfo.getDataNum(),appMysqlInfo.getCollectTime()});
		} catch (SQLException e) {
			logger.error("addAppMysqlInfo 出错", e);
		}
		
	}
	
	/**
	 * 从历史表中获取 存储信息
	 * @param dataBaseId
	 * @param collectTime yyyyMMdd
	 * @return
	 */
	public Map<Integer,AppMysqlInfo> findAppDatabaseInfo(int dataBaseId,int collectTime){
		
		String sql = "select * from ms_monitor_mysql_info where database_id=? and collect_time=?";
		
		final Map<Integer,AppMysqlInfo> map = new HashMap<Integer, AppMysqlInfo>();
		
		try {
			this.query(sql, new Object[]{dataBaseId,collectTime}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					int appId = rs.getInt("app_id");
					int key_num = rs.getInt("key_num");
					int data_num = rs.getInt("data_num");
					int database_id = rs.getInt("database_id");
					
					AppMysqlInfo info = map.get(appId);
					if(info == null){
						info = new AppMysqlInfo();
						map.put(appId, info);
					}
					info.setDataBaseId(database_id);
					info.setAppId(appId);
					info.setDataNum(data_num);
					info.setKeyNum(key_num);
				}});
		} catch (Exception e) {
			logger.error("findAppDatabaseInfo 出错", e);
		}
		return map;
	}
	
	
}
