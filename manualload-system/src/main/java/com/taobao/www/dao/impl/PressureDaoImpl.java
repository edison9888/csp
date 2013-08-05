package com.taobao.www.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.www.dao.PressureDao;
import com.taobao.www.entity.AppConfig;
import com.taobao.www.entity.AppMachine;
import com.taobao.www.entity.PressureResult;

public class PressureDaoImpl extends MysqlRouteBase implements PressureDao {

	private static final Log logger = LogFactory.getLog(PressureDaoImpl.class);

	private static PressureDaoImpl dao = new PressureDaoImpl();

	public PressureDaoImpl() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
	}

	public PressureDaoImpl getNewPressureDaoImpl() {
		return dao;
	}

	/**
	 * 功能：根据应用id来删除机器信息。
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2012-10-30
	 * 
	 */
	public boolean delAppMachineByAppId(Integer id) {
		String sql = "DELETE FROM  app_machine WHERE  app_id = " + id;
		boolean result = false;
		try {
			this.execute(sql);
			result = true;
		} catch (SQLException e) {
			result = false;
			logger.error(" delete obj is error, appId : ==" + id, e);
		}
		return result;
	}

	public boolean delAppMachineById(Integer id) {
		String sql = "DELETE FROM app_machine WHERE  id = " + id;
		boolean result = false;
		try {
			this.execute(sql);
			result = true;
		} catch (SQLException e) {
			result = false;
			logger.error(" delete obj is error, MachineId : ==" + id, e);
		}
		return result;
	}

	public void delMultipleAppMachineById(String flag) {
		String whereId = null;
		if (flag != null && !flag.equals("")) {
			String[] ids = flag.split(",");
			StringBuffer idList = new StringBuffer();
			for (String id : ids) {
				idList.append(" id =" + id + " or");
			}
			whereId = idList.toString().substring(0, idList.toString().length() - 3);
		}
		if (whereId != null) {
			String sql = "DELETE FROM app_machine WHERE " + whereId;
			try {
				this.execute(sql);
			} catch (SQLException e) {
				logger.error(" delete obj is error, MachineId : ==" + whereId, e);
			}
		}
	}

	public void delMultipleAppMachine(List<AppMachine> macList) {
		String whereId = null;
		if (macList != null && macList.size() > 0) {
			StringBuffer idList = new StringBuffer();
			for (AppMachine mac : macList) {
				int id = mac.getId();
				idList.append(" id =" + id + " or");
			}
			whereId = idList.toString().substring(0, idList.toString().length() - 3);
		}
		if (whereId != null) {
			String sql = "DELETE FROM app_machine WHERE " + whereId;
			try {
				this.execute(sql);
			} catch (SQLException e) {
				logger.error(" delete obj is error, MachineId : ==" + whereId, e);
			}
		}
	}

	public boolean delOneAppConfig(Integer id) {
		this.delAppMachineByAppId(id);
		String sql = "DELETE FROM app_config WHERE  id =" + id;
		boolean result = false;
		try {
			this.execute(sql);
			result = true;
		} catch (SQLException e) {
			result = false;
			logger.error(" delete obj is error, appConfigId : ==" + id, e);
		}
		return result;
	}

	public List<AppMachine> getAllAppMachines(Integer id) {
		final List<AppMachine> list = new ArrayList<AppMachine>();
		String sql = "SELECT a.id as Id, a.app_name AS appName ,a.app_id AS appId ,"
				+ "a.mac_name AS macName ,a.mac_ip AS macIp," + "a.create_time AS createTime  "
				+ "FROM app_machine a WHERE a.app_id = " + id + " order by a.app_name desc ";
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AppMachine mac = new AppMachine();
					mac.setId(rs.getInt("Id"));
					mac.setAppName(rs.getString("appName"));
					mac.setAppId(rs.getInt("appId"));
					mac.setMacName(rs.getString("macName"));
					mac.setMacIp(rs.getString("macIp"));
					mac.setCreateTime(rs.getDate("createTime"));
					list.add(mac);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
		} catch (Exception e) {
			logger.error(" find  All AppMachine Info: ", e);
		}
		return list;
	}

	public int getAllAppMachinesCount(Integer id) {
		int count = 0;
		String sql = "SELECT COUNT(id) FROM  app_machine  WHERE app_id=" + id;
		try {
			count = this.getIntValue(sql, new Object[] {});
		} catch (SQLException e) {
			logger.error(" count data is error , appId : ==" + id, e);
		}
		return count;
	}

	public List<AppMachine> getAllMachineByAppId(int appId) {
		final List<AppMachine> list = new ArrayList<AppMachine>();
		String sql = "SELECT a.app_name AS appName ," + "a.mac_name AS macName ,a.mac_ip AS macIp  "
				+ "FROM app_machine a WHERE a.app_id = " + appId + " order by a.id desc ";
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AppMachine mac = new AppMachine();
					mac.setAppName(rs.getString("appName"));
					mac.setMacName(rs.getString("macName"));
					mac.setMacIp(rs.getString("macIp"));
					list.add(mac);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
		} catch (Exception e) {
			logger.error(" find  All AppMachine Info: ", e);
		}
		return list;
	}

	/**
	 * 功能：当前用户下的所有压测应用信息。
	 * 
	 * @param userName
	 * 
	 * @return list
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2011-12-05
	 * 
	 */
	public List<AppConfig> getAllPreConfigs(String userName) {
		final List<AppConfig> list = new ArrayList<AppConfig>();
		String sql = "SELECT * FROM app_config a WHERE user_name = '" + userName + "' order by a.id asc ";
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AppConfig mac = new AppConfig();
					mac.setId(rs.getInt("id"));
					mac.setAppName(rs.getString("app_name"));
					mac.setPreKinds(rs.getString("pre_kinds"));
					mac.setPreType(rs.getString("pre_type"));
					mac.setPrePort(rs.getInt("pre_port"));
					mac.setPreWay(rs.getInt("pre_way"));
					mac.setUserName(rs.getString("user_name"));
					mac.setUserPass(rs.getString("user_pass"));
					mac.setCreateTime(rs.getTimestamp("create_time"));
					list.add(mac);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
		} catch (Exception e) {
			logger.error(" find  All AppConfig Info: ", e);
		}
		return list;
	}

	@Override
	public List<PressureResult> getAllPressureResultMessage(String querydate, int appId) {
		final List<PressureResult> list = new ArrayList<PressureResult>();
		String sql = "SELECT app_id , create_time , mac_name , request_totle ,bad_count , "
				+ "response_avg ,fetches_sec ,bytes_connection ,httpState_stateCount "
				+ " from  pressure_result where  app_id = " + appId + " and DATE_FORMAT(create_time,'%Y-%m-%d') >= '"
				+ querydate + "' order by create_time desc";
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
					PressureResult mac = new PressureResult();
					mac.setAppId(rs.getInt("app_id"));
					Date date = rs.getTimestamp("create_time");
					if (date == null) {
						mac.setInsertTime("");
					} else {
						mac.setInsertTime(format.format(date));
					}
					mac.setCreateTime(date);
					mac.setMacName(rs.getString("mac_name"));
					mac.setRequestTotle(rs.getInt("request_totle"));
					mac.setBadCount(rs.getInt("bad_count"));
					mac.setResponseAvg(rs.getInt("response_avg"));
					mac.setFetchesSec(rs.getInt("fetches_sec"));
					mac.setBytesConnection(rs.getInt("bytes_connection"));
					mac.setHttpState_stateCount(rs.getString("httpState_stateCount"));
					list.add(mac);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
		} catch (Exception e) {
			logger.error(" find  All AppConfig Info: ", e);
		}
		return list;
	}

	public AppConfig getOneAppConfigMessage(int id) {
		final List<AppConfig> list = new ArrayList<AppConfig>();
		String sql = "SELECT * FROM app_config a WHERE id = " + id;
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AppConfig mac = new AppConfig();
					mac.setId(rs.getInt("id"));
					mac.setAppName(rs.getString("app_name"));
					mac.setPreKinds(rs.getString("pre_kinds"));
					mac.setPreType(rs.getString("pre_type"));
					mac.setPrePort(rs.getInt("pre_port"));
					mac.setPreWay(rs.getInt("pre_way"));
					mac.setUserName(rs.getString("user_name"));
					mac.setUserPass(rs.getString("user_pass"));
					mac.setCreateTime(rs.getTimestamp("create_time"));
					list.add(mac);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
		} catch (Exception e) {
			logger.error(" find  one AppConfig Info: ", e);
		}
		if (list == null || list.size() == 0) {
			return null;
		} else {
			return list.get(0);
		}
	}

	public List<AppMachine> getOneAppMachineByAppId(int appId) {
		final List<AppMachine> list = new ArrayList<AppMachine>();
		String sql = "SELECT  c.user_name AS userName ,c.user_pass AS userPass ,c.pre_kinds AS preKinds ,m.mac_name AS macName ,m.mac_ip AS macIp"
				+ "  FROM  app_machine m ,app_config c  WHERE m.app_id=c.id AND c.id=" + appId;
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AppMachine mac = new AppMachine();
					mac.setUserName(rs.getString("userName"));
					mac.setPassword(rs.getString("userPass"));
					mac.setPreKinds(rs.getString("preKinds"));
					mac.setMachineName(rs.getString("macName"));
					mac.setMachineIp(rs.getString("macIp"));
					list.add(mac);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
		} catch (Exception e) {
			logger.error(" find  All AppMachine Info: ", e);
		}
		return list;
	}

	/**
	 * 功能：获取csp系统的所有应用名称和id。
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-09
	 */

	public Integer getCspAppMessage(String appName) {
		final Map<String, Integer> cspAppMessage = new TreeMap<String, Integer>();
		String sql = "SELECT app_id as appId,app_name as appName FROM ms_monitor_app ORDER BY app_name ASC ";
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					String appName = rs.getString("appName");
					int appId = rs.getInt("appId");
					cspAppMessage.put(appName, appId);
				}
			}, DbRouteManage.get().getDbRouteByRouteId("Main"));
		} catch (Exception e) {
			logger.error(" find  All cspAppMessage Info: ", e);
		}

		return cspAppMessage.get(appName);
	}

	/**
	 * 功能：根据email获取权限信息。
	 * 
	 * @param email
	 * 
	 * @return
	 * 
	 * @author wb-tangjinge
	 * 
	 * @time 2013-01-09
	 */

	public String getCspAppPurviewMessage(String email) {
		final List<String> list = new ArrayList<String>();
		String permissionDesc = null;
		String sql = "SELECT permission_desc as permission FROM ms_monitor_user WHERE mail='" + email + "'";
		try {
			this.query(sql, new Object[] {}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					String perDesc = rs.getString("permission");
					if (perDesc != null) {
						list.add(perDesc);
					}
				}
			}, DbRouteManage.get().getDbRouteByRouteId("Main"));
		} catch (Exception e) {
			logger.error(" find  permission_desc Info: ", e);
		}
		if (list != null && list.size() > 0) {
			permissionDesc = list.get(0);
		}
		list.clear();
		return permissionDesc;

	}

	public Integer saveOneAppConfig(AppConfig appConfig) {
		int result = 0;
		String sql = "insert into `app_config` (`app_name`, `pre_kinds`, `pre_type`, `pre_port`, `pre_way`, `user_name`, `user_pass`, `create_time`) "
				+ "value('"
				+ appConfig.getAppName()
				+ "','"
				+ appConfig.getPreKinds()
				+ "','"
				+ appConfig.getPreType()
				+ "','"
				+ appConfig.getPrePort()
				+ "',"
				+ appConfig.getPreWay()
				+ ",'"
				+ appConfig.getUserName()
				+ "','" + appConfig.getUserPass() + "',NOW()) ";
		try {
			this.execute(sql);
			result = 1;
		} catch (SQLException e) {
			result = 0;
			logger.error(" save AppConfig is error, appConfigName : ==" + appConfig.getAppName(), e);
		}
		return result;
	}

	public long getObjectMaxId(String type) {
		String sql = "SELECT MAX(id)  FROM app_config ";
		long result = 0;
		try {
			result = this.getLongValue(sql, DbRouteManage.get().getDbRouteByRouteId("csp_autoload_old"));
		} catch (Exception e) {
			result = 0;
			logger.error("get max object id is error ", e);
		}
		return result;
	}

	public void saveOneAppMachine(AppMachine oneKey) {
		String sql = "insert into `app_machine` (`app_name`, `app_id`, `mac_name`, `mac_os`, `mac_ip`, `mac_state`, `mac_totle`, `create_time`) "
				+ " values('"
				+ oneKey.getAppName()
				+ "',"
				+ oneKey.getAppId()
				+ ",'"
				+ oneKey.getMacName()
				+ "',NULL,'" + oneKey.getMacIp() + "','" + oneKey.getMacState() + "',0,NOW());";
		try {
			this.execute(sql);
		} catch (SQLException e) {
			logger.error(
					" save AppMachine is error, appName : ==" + oneKey.getAppName() + ", machineName:"
							+ oneKey.getMacName(), e);
		}
	}

	public Integer saveOnePressureResult(PressureResult pressureResult) {
		String sql = "insert into `pressure_result` (`app_id`, `request_totle`, `process_count`,"
				+ " `bytes_totle`, `time_totle`, `bytes_connection`, `fetches_sec`, `bytes_sec`,"
				+ " `connect_avg`, `connect_max`, `connect_min`, `response_avg`, `response_max`, "
				+ "`response_min`, `bad_count`, `user_name`, `mac_name`, " + "`httpState_stateCount`, `create_time`) "
				+ " values('"
				+ pressureResult.getAppId()
				+ "','"
				+ pressureResult.getRequestTotle()
				+ "',"
				+ "'"
				+ (pressureResult.getProcessCount() == null ? 0 : pressureResult.getProcessCount())
				+ "',"
				+ "'"
				+ (pressureResult.getBytesTotle() == null ? 0 : pressureResult.getBytesTotle())
				+ "',"
				+ "'"
				+ (pressureResult.getTimeTotle() == null ? 0 : pressureResult.getTimeTotle())
				+ "',"
				+ "'"
				+ (pressureResult.getBytesConnection() == null ? 0 : pressureResult.getBytesConnection())
				+ "',"
				+ "'"
				+ (pressureResult.getFetchesSec() == null ? 0 : pressureResult.getFetchesSec())
				+ "',"
				+ "'"
				+ (pressureResult.getBytesSec() == null ? 0 : pressureResult.getBytesSec())
				+ "',"
				+ "'"
				+ (pressureResult.getConnectAvg() == null ? 0 : pressureResult.getConnectAvg())
				+ "',"
				+ "'"
				+ (pressureResult.getConnectMax() == null ? 0 : pressureResult.getConnectMax())
				+ "',"
				+ "'"
				+ (pressureResult.getConnectMin() == null ? 0 : pressureResult.getConnectMin())
				+ "',"
				+ "'"
				+ (pressureResult.getResponseAvg() == null ? 0 : pressureResult.getResponseAvg())
				+ "',"
				+ "'"
				+ (pressureResult.getResponseMax() == null ? 0 : pressureResult.getResponseMax())
				+ "',"
				+ "'"
				+ (pressureResult.getResponseMin() == null ? 0 : pressureResult.getResponseMin())
				+ "',"
				+ "'"
				+ (pressureResult.getBadCount() == null ? 0 : pressureResult.getBadCount())
				+ "',"
				+ "'"
				+ (pressureResult.getUserName() == null ? 0 : pressureResult.getUserName())
				+ "',"
				+ "'"
				+ (pressureResult.getMacName() == null ? 0 : pressureResult.getMacName())
				+ "',"
				+ "'"
				+ (pressureResult.getHttpState_stateCount() == null ? 0 : pressureResult.getHttpState_stateCount())
				+ "',NOW())";
		int result = 0;
		try {
			dao.execute(sql);
			result = 1;
		} catch (SQLException e) {
			result = 0;
			logger.error(" save AppMachine is error, appId : ==" + pressureResult.getAppId() + ", userName:"
					+ pressureResult.getUserName(), e);
		}
		return result;
	}

	public boolean updateOneAppConfig(AppConfig appConfig) {
		boolean result = false;
		String sql = " update app_config set pre_kinds ='" + appConfig.getPreKinds() + "' , pre_port = "
				+ appConfig.getPrePort() + " where id = " + appConfig.getId();
		try {
			this.execute(sql);
			result = true;
		} catch (SQLException e) {
			result = false;
			logger.error(
					" update AppConfig is error,  Id : ==" + appConfig.getId() + " , userName :"
							+ appConfig.getUserName(), e);
		}
		return result;
	}

}
