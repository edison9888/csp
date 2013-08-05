package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.CspAppKeyRelation;
import com.taobao.monitor.common.po.CspKeyInfo;
import com.taobao.monitor.common.po.CspKeyMode;
import com.taobao.monitor.common.po.CspKeyPropertyRelation;
import com.taobao.monitor.common.po.CspNeedBaseline;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.monitor.common.util.page.Pagination;

public class KeyDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(KeyDao.class);

	/**
	 *@author wb-lixing 2012-3-31 上午10:28:09
	 * 
	 *@param keyNamePart
	 *            支持keyName模糊查询
	 */
	public Pagination<CspKeyMode> findAlarmKeyListByAppIdPageable(int appId,
			String keyNamePart, int pageNo, int pageSize) {
		final Pagination<CspKeyMode> page = new Pagination<CspKeyMode>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		String oriSql = "SELECT cka.* FROM csp_key_alarm_mode cka INNER JOIN ms_monitor_app app ON cka.app_name = app.app_name "
				+" WHERE app.app_id = ? AND cka.key_name LIKE ? order by cka.id desc";
		String countSql = "select count(*) total from (" + oriSql + ") t ";

		String sql = oriSql + " limit ?, ?";

		final List<CspKeyMode> poList = new ArrayList<CspKeyMode>();

		try {
			// 查询分页数
			this.query(countSql,
					new Object[] { appId, "%" + keyNamePart + "%" },
					new SqlCallBack() {
						public void readerRows(ResultSet rs) throws Exception {
							long total = rs.getLong("total");
							page.setTotalCount((int) total);
						}
					});

			// 分页数据
			this.query(sql, new Object[] { appId, "%" + keyNamePart + "%",
					(pageNo - 1) * pageSize, pageSize }, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					CspKeyMode mode = new CspKeyMode();
					fillCspKeyMode(rs, mode);
					poList.add(mode);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		page.setList(poList);
		return page;
	}

	/**
	 * 更新告警key
	 * 
	 * @author wb-lixing *2012-4-12 下午05:03:51
	 */
	public boolean updateKeyMode(CspKeyMode po) {
		String sql = "UPDATE  `csp_key_alarm_mode` SET"
				+ " `app_name` = ? , `key_name` = ? , `property_name` = ? , `baseline` = ? , `alarm` = ? ,"
				+ " `key_scope` = ? , `key_alias` = ?, app_mode_config = ?, host_mode_config = ?,key_level=? WHERE `id` = ? ";

		try {
			this.execute(sql, new Object[] { po.getAppName(), po.getKeyName(),
					po.getPropertyName(), po.getBaseline(), po.getAlarm(),
					  po.getKeyScope(),
					po.getKeyAlias(),po.getAppModeConfig(), po.getHostModeConfig(),po.getKeyLevel(), po.getId()});
		} catch (SQLException e) {
			logger.error("csp addKeyPropertyRelation 错误", e);
			return false;
		}

		return true;

	}

	/**
	 * 添加告警key
	 * 
	 * @author wb-lixing 2012-4-12 下午02:39:16
	 */
	public boolean addKeyMode(CspKeyMode po) {
		String sql = "INSERT INTO `csp_key_alarm_mode` ("
				+ " `app_name`, `key_name`, `property_name`, `baseline`,"
				+ " `alarm`,  `key_scope`, `key_alias`,app_mode_config, host_mode_config,key_level) VALUES"
				+ " ( ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

		try {
			this.execute(sql, new Object[] { po.getAppName(), po.getKeyName(),
					po.getPropertyName(), po.getBaseline(), po.getAlarm(),
					 po.getKeyScope(),
					po.getKeyAlias(),po.getAppModeConfig(),po.getHostModeConfig(),po.getKeyLevel() });
		} catch (SQLException e) {
			logger.error("csp addKeyPropertyRelation 错误", e);
			return false;
		}

		return true;

	}

	/**
	 * 查询出告警的属性列表
	 * 
	 * @author wb-lixing 2012-4-11 下午05:05:21
	 * @throws SQLException
	 */
	public List<CspKeyMode> findKeyModes(String appName, String keyName) {
		String sql = "SELECT * FROM csp_key_alarm_mode "
				+ " WHERE app_name=? AND key_name=?";
		final List<CspKeyMode> list = new ArrayList<CspKeyMode>();

		try {
			this.query(sql, new Object[] { appName, keyName },
					new SqlCallBack() {

						@Override
						public void readerRows(ResultSet rs) throws Exception {
							CspKeyMode mode = new CspKeyMode();
							fillCspKeyMode(rs, mode);
							list.add(mode);
						}
					});
		} catch (Exception e) {
			logger.error("csp findAllKeyModes 错误", e);
		}

		return list;
	}
	
	/**
	 * 查询出告警的属性列表
	 * 
	 * @author wb-lixing 2012-4-11 下午05:05:21
	 * @throws SQLException
	 */
	public List<CspKeyMode> findKeyModes(String appName) {
		String sql = "SELECT * FROM csp_key_alarm_mode "
				+ " WHERE app_name=? ";
		final List<CspKeyMode> list = new ArrayList<CspKeyMode>();

		try {
			this.query(sql, new Object[] { appName },
					new SqlCallBack() {

						@Override
						public void readerRows(ResultSet rs) throws Exception {
							CspKeyMode mode = new CspKeyMode();
							fillCspKeyMode(rs, mode);
							list.add(mode);
						}
					});
		} catch (Exception e) {
			logger.error("csp findAllKeyModes 错误", e);
		}

		return list;
	}
	
	/**
	 * 查询出告警的属性列表
	 * 
	 * @author wb-lixing 2012-4-11 下午05:05:21
	 * @throws SQLException
	 */
	public List<CspKeyMode> findKeyModes(String appName, String keyName,String property) {
		String sql = "SELECT * FROM csp_key_alarm_mode "
				+ " WHERE app_name=? AND key_name=? AND property_name = ?";
		final List<CspKeyMode> list = new ArrayList<CspKeyMode>();

		try {
			this.query(sql, new Object[] { appName, keyName,property},
					new SqlCallBack() {

						@Override
						public void readerRows(ResultSet rs) throws Exception {
							CspKeyMode mode = new CspKeyMode();
							fillCspKeyMode(rs, mode);
							list.add(mode);
						}
					});
		} catch (Exception e) {
			logger.error("csp findAllKeyModes 错误", e);
		}

		return list;
	}

	/**
	 * 添加一个新的key
	 * 
	 * @param key
	 * @return
	 */
	public KeyPo addMonitorKey(String key) {

		String sql = "insert into MS_MONITOR_KEY(key_value)values(?)";

		String select = "select * from MS_MONITOR_KEY where key_value=?";

		String select1 = "select count(*) from MS_MONITOR_KEY where key_value=?";
		int count = 0;
		try {
			count = this.getIntValue(select1, new String[] { key });
			if (count == 0) {
				this.execute(sql, new Object[] { key });
			}
			final KeyPo monitorKey = new KeyPo();
			this.query(select, new String[] { key }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int id = rs.getInt("key_id");
					String value = rs.getString("key_value");
					monitorKey.setKeyId(id);
					monitorKey.setKeyName(value);
				}
			});
			return monitorKey;
		} catch (Exception e) {
			final KeyPo monitorKey1 = new KeyPo();
			try {
				this.query(select, new String[] { key }, new SqlCallBack() {
					public void readerRows(ResultSet rs) throws Exception {
						int id = rs.getInt("key_id");
						String value = rs.getString("key_value");
						monitorKey1.setKeyId(id);
						monitorKey1.setKeyName(value);
					}
				});
				return monitorKey1;
			} catch (Exception e1) {
				logger.error("addMonitorKey 出错", e1);
				return null;
			}
		}
	}

	/**
	 * 
	 * @param keyName
	 * @return
	 */
	public KeyPo getKeyPo(String keyName) {
		String sql = "select * from ms_monitor_key where key_value=?";

		final KeyPo po = new KeyPo();

		try {
			this.query(sql, new Object[] { keyName }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int id = rs.getInt("key_id");
					String value = rs.getString("key_value");
					po.setKeyId(id);
					po.setKeyName(value);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return po.getKeyName() == null ? null : po;
	}

	/**
	 * 
	 * @param keyId
	 * @return
	 */
	public KeyPo getKeyPo(int keyId) {
		String sql = "select * from ms_monitor_key where key_id=?";

		final KeyPo po = new KeyPo();

		try {
			this.query(sql, new Object[] { keyId }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					int id = rs.getInt("key_id");
					String value = rs.getString("key_value");
					po.setKeyId(id);
					po.setKeyName(value);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return po.getKeyName() == null ? null : po;
	}

	/**
	 * 查询出所有key
	 * 
	 * @return
	 */
	public Map<Integer, KeyPo> findAllKey() {
		String sql = "select * from ms_monitor_key";

		final Map<Integer, KeyPo> map = new HashMap<Integer, KeyPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					map.put(po.getKeyId(), po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllKey 出错,", e);
		}
		return map;
	}

	/**
	 * 取得所有应用的key
	 * 
	 * @param appId
	 * @return
	 */
	public List<KeyPo> findAllAppKey(int appId) {

		String sql = "select k.* from ms_monitor_app_key ak ,ms_monitor_key k where ak.app_id=? and ak.key_id=k.key_id";

		final List<KeyPo> poList = new ArrayList<KeyPo>();

		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					poList.add(po);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return poList;

	}

	/**
	 * 根据 key 名称 模糊查询出所有相关的 key 信息，并返回列表
	 * 
	 * @param name
	 * @return
	 */
	public List<KeyPo> findKeyLikeName(String keyname, Integer appId) {

		String sql = "select k.* from ms_monitor_key k,MS_MONITOR_APP_KEY ak where ak.key_id=k.key_id and ak.app_id=? ";
		List<Object> params = new ArrayList<Object>();
		if (keyname != null && !"".equals(keyname)) {
			sql += "  and k.key_value like '%" + keyname + "%'";
		}
		params.add(appId);
		final List<KeyPo> keyList = new ArrayList<KeyPo>();

		try {
			this.query(sql, params.toArray(), new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {
					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setDefaultconfig(rs.getString("defaultconfig"));
					po.setFeature(rs.getString("feature"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					keyList.add(po);

				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keyList;
	}

	/**
	 * 根据key info
	 * 
	 * @param po
	 * @return
	 */
	public boolean updateKeyInfo(KeyPo po) {
		String sql = "update ms_monitor_key set alias_name=?,key_type=?,feature=? where key_id=?";
		try {
			this.execute(sql, new Object[] { po.getAliasName(),
					po.getKeyType(), po.getFeature(), po.getKeyId() });
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		return true;
	}

	/**
	 * 查询需要在实时监控baseline表中有数据的key
	 * 
	 * @return
	 */
	public List<KeyPo> findAllMonitorBaseLineKey(int appId) {
		String sql = "SELECT * FROM ms_monitor_key k, ms_monitor_app_key ak WHERE ak.APP_ID=? AND k.key_id=ak.KEY_ID";
		final List<KeyPo> poList = new ArrayList<KeyPo>();

		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					if (rs.getString("key_value") == null)
						return;

					String keyValue = rs.getString("key_value").toUpperCase();

					// 代码过滤掉不需要统计的部分
					if (keyValue.indexOf("SELF_THREAD") >= 0
							|| keyValue.indexOf("SELF_DATASOURCE") >= 0
							|| keyValue.indexOf("SELF_THREADPOOL") >= 0
							|| keyValue.indexOf("NAGIOSMAIDIAN") >= 0
							|| keyValue.indexOf("EXCEPTION") >= 0) {
						return;
					}

					// 只统计PV，LOAD，CPU，HSF部分
					if (keyValue.indexOf("PV") >= 0
							|| keyValue.indexOf("LOAD") >= 0
							|| keyValue.indexOf("CPU") >= 0
							|| keyValue.indexOf("HSF") >= 0) {
						KeyPo po = new KeyPo();
						po.setKeyId(rs.getInt("key_id"));
						po.setKeyName(rs.getString("key_value"));
						po.setAliasName(rs.getString("alias_name"));
						po.setKeyType(rs.getString("key_type"));
						po.setFeature(rs.getString("feature"));
						poList.add(po);
					}
				}
			});
		} catch (Exception e) {
			logger.error("findAllMonitorBaseLineKey异常：", e);
		}

		return poList;
	}

	/**
	 * 查询需要在实时监控baseline表中有数据的key <key,value> : <app-id - key-id , KeyPo>
	 * 
	 * @return
	 */
	public Map<String, KeyPo> findAllMonitorBaseLineKey() {
		String sql = "SELECT *,ak.APP_ID as appId FROM ms_monitor_key k, ms_monitor_app_key ak WHERE ak.APP_ID=? AND k.key_id=ak.KEY_ID AND key_value NOT LIKE ? AND key_value NOT LIKE ? AND key_value NOT LIKE ? AND key_value NOT LIKE ? AND key_value NOT LIKE ? ";
		final Map<String, KeyPo> map = new HashMap<String, KeyPo>();

		try {
			this.query(sql, new Object[] { "SELF_Thread_%",
					"SELF_DataSource_%", "SELF_ThredPool_%", "NAGIOSMAIDIAN%",
					"%EXCEPTION%" }, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {
					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					map.put(rs.getString("appId") + "-"
							+ rs.getString("key_id"), po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllMonitorBaseLineKey异常：", e);
		}

		return map;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

	public List<CspAppKeyRelation> findAllAppKeyRelation() {
		String sql = "select * from csp_app_key_relation";
		final List<CspAppKeyRelation> list = new ArrayList<CspAppKeyRelation>();
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {

					CspAppKeyRelation ckpr = new CspAppKeyRelation();
					ckpr.setKeyId(rs.getInt("key_id"));
					ckpr.setAppId(rs.getInt("app_id"));
					list.add(ckpr);
				}
			});
		} catch (Exception e) {
			logger.error("csp findAllAppKeyRelation 错误", e);
		}
		return list;
	}

	public List<CspKeyPropertyRelation> findAllKeyPropertyRelation() {
		String sql = "select * from csp_key_property_relation";
		final List<CspKeyPropertyRelation> list = new ArrayList<CspKeyPropertyRelation>();
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {

					CspKeyPropertyRelation ckpr = new CspKeyPropertyRelation();
					ckpr.setKeyId(rs.getInt("key_id"));
					ckpr.setPropertyName(rs.getString("property_name"));
					list.add(ckpr);
				}
			});
		} catch (Exception e) {
			logger.error("csp findAllKeyPropertyRelation 错误", e);
		}
		return list;
	}

	/**
	 *@author wb-lixing 2012-4-12 下午03:29:42
	 *@param keyName
	 *@return
	 */
	public List<CspKeyPropertyRelation> findKeyPropertyRelation(String keyName) {
		String sql = "SELECT DISTINCT * FROM csp_key_property_relation  ck_p "
				+ "WHERE key_id =  (SELECT id FROM csp_key_info cki WHERE key_name=?)";
		final List<CspKeyPropertyRelation> list = new ArrayList<CspKeyPropertyRelation>();
		try {
			this.query(sql, new Object[] { keyName }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspKeyPropertyRelation ckpr = new CspKeyPropertyRelation();
					ckpr.setKeyId(rs.getInt("key_id"));
					ckpr.setPropertyName(rs.getString("property_name"));
					list.add(ckpr);
				}
			});
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	public List<CspKeyPropertyRelation> findKeyPropertyRelation(Integer keyId) {
		String sql = "select distinct * from csp_key_property_relation where key_id = ?";
		final List<CspKeyPropertyRelation> list = new ArrayList<CspKeyPropertyRelation>();
		try {
			this.query(sql, new Object[] { keyId }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspKeyPropertyRelation ckpr = new CspKeyPropertyRelation();
					ckpr.setKeyId(rs.getInt("key_id"));
					ckpr.setPropertyName(rs.getString("property_name"));
					list.add(ckpr);
				}
			});
		} catch (Exception e) {
			logger.error(e);
		}
		return list;
	}

	public void addAppKeyRelation(int appId, int keyId) {
		String sql = "insert into csp_app_key_relation(app_id,key_id) values(?,?)";
		try {
			this.execute(sql, new Object[] { appId, keyId });
		} catch (SQLException e) {
			logger.error("csp addKeyPropertyRelation 错误", e);
		}
	}

	public void addKeyPropertyRelation(CspKeyPropertyRelation kp) {
		String sql = "insert into csp_key_property_relation(key_id,property_name) values(?,?)";
		try {
			this.execute(sql, new Object[] { kp.getKeyId(),
					kp.getPropertyName() });
		} catch (SQLException e) {
			logger.error("csp addKeyPropertyRelation 错误", e);
		}
	}

	public CspKeyInfo addKeyInfo(CspKeyInfo info) {

		String sql = "insert into CSP_KEY_INFO(key_name,parent_key,alias_name,key_scope, key_desc, key_level) values(?,?,?,?,?,?)";

		try {
			this
					.execute(sql, new Object[] { info.getKeyName(),
							info.getParentKeyName(), info.getAliasName(),
							info.getKeyScope(), info.getKeyDesc(),
							info.getKeyLevel() });
		} catch (SQLException e) {
			logger.error("csp addKeyInfo 错误", e);
		}

		return getKeyInfo(info.getKeyName());
	}

	public CspKeyInfo getKeyInfo(String keyName) {

		String sql = "select * from CSP_KEY_INFO where key_name=?";

		final CspKeyInfo info = new CspKeyInfo();

		try {
			this.query(sql, new Object[] { keyName }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					info.setKeyName(rs.getString("key_name"));
					info.setAliasName(rs.getString("alias_name"));
					info.setParentKeyName(rs.getString("parent_key"));
					info.setKeyScope(rs.getString("key_scope"));
					info.setKeyLevel(rs.getInt("key_level"));
					info.setKeyId(rs.getInt("id"));
				}
			});
		} catch (Exception e) {
			logger.error("csp getKeyInfo 错误", e);
		}

		return info.getKeyId() > 0 ? info : null;
	}

	public List<CspKeyInfo> findAllKeyInfos() {

		String sql = "select * from CSP_KEY_INFO ";

		final List<CspKeyInfo> infos = new ArrayList<CspKeyInfo>();

		try {
			this.query(sql, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspKeyInfo info = new CspKeyInfo();
					info.setKeyName(rs.getString("key_name"));
					info.setAliasName(rs.getString("alias_name"));
					info.setParentKeyName(rs.getString("parent_key"));
					info.setKeyScope(rs.getString("key_scope"));
					info.setKeyLevel(rs.getInt("key_level"));
					info.setKeyId(rs.getInt("id"));
					infos.add(info);
				}
			});
		} catch (Exception e) {
			logger.error("csp findAllKeyInfos 错误", e);
		}
		return infos;
	}

	public List<CspKeyMode> findAllKeyModes() {

		String sql = "select * from CSP_KEY_ALARM_MODE ";

		final List<CspKeyMode> list = new ArrayList<CspKeyMode>();

		try {
			this.query(sql, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspKeyMode mode = new CspKeyMode();
					fillCspKeyMode(rs, mode);
					list.add(mode);
				}
			});
		} catch (Exception e) {
			logger.error("csp findAllKeyModes 错误", e);
		}

		return list;
	}

	/**
	 *@author wb-lixing 2012-4-12 下午02:06:39
	 *@param id
	 *@return
	 */
	public CspKeyMode getKeyMode(int id) {
		String sql = "select * from CSP_KEY_ALARM_MODE where id = ? ";

		final CspKeyMode mode = new CspKeyMode();

		try {
			this.query(sql, new Object[] { id }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					fillCspKeyMode(rs, mode);
				}
			});
		} catch (Exception e) {
			logger.error("csp findAllKeyModes 错误", e);
		}
		return mode;

	}

	/**
	 *@author wb-lixing 2012-4-12 上午10:58:14
	 *@param appName
	 *@param keyname
	 *@param propertyname
	 *@return
	 */
	public CspKeyMode getKeyMode(String appName, String keyname,
			String propertyname) {
		String sql = "select * from CSP_KEY_ALARM_MODE where app_name=? and key_name=? and property_name=? ";

		final CspKeyMode mode = new CspKeyMode();

		try {
			this.query(sql, new Object[] { appName, keyname, propertyname },
					new SqlCallBack() {

						@Override
						public void readerRows(ResultSet rs) throws Exception {
							fillCspKeyMode(rs,mode);
						}
					});
		} catch (Exception e) {
			logger.error("csp findAllKeyModes 错误", e);
		}
		return mode;
	}

	public CspKeyMode getKeyMode(String appName, String keyname,
			String propertyname, String scope) {

		String sql = "select * from CSP_KEY_ALARM_MODE where app_name=? and key_name=? and property_name=? and key_scope=?";

		final CspKeyMode mode = new CspKeyMode();

		try {
			this.query(sql, new Object[] { appName, keyname, propertyname,
					scope }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					fillCspKeyMode(rs,mode);
				}
			});
		} catch (Exception e) {
			logger.error("csp findAllKeyModes 错误", e);
		}
		return mode.getAppName() != null ? mode : null;
	}
	public List<CspKeyMode> getKeyModeByAppName(String appName) {

		String sql = "select * from CSP_KEY_ALARM_MODE where app_name=?";

		final List<CspKeyMode> list = new ArrayList<CspKeyMode>();
		try {
			this.query(sql, new Object[] { appName}, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					final CspKeyMode mode = new CspKeyMode();
					fillCspKeyMode(rs,mode);
					list.add(mode);
				}
			});
		} catch (Exception e) {
			logger.error("csp findAllKeyModes 错误", e);
		}
		return list;
	}
	public void fillCspKeyMode(ResultSet rs, CspKeyMode mode) throws SQLException{
		mode.setId(rs.getInt("id"));
		mode.setAlarm(rs.getInt("alarm"));
		mode.setAppName(rs.getString("app_name"));
		mode.setBaseline(rs.getInt("baseline"));
		mode.setKeyName(rs.getString("key_name"));
		mode.setPropertyName(rs.getString("property_name"));
		mode.setKeyScope(rs.getString("key_scope"));
		mode.setKeyAlias(rs.getString("key_alias"));
		mode.setHostModeConfig(rs.getString("host_mode_config"));
		mode.setAppModeConfig(rs.getString("app_mode_config"));
		mode.setKeyLevel(rs.getInt("key_level"));
	}

	/**
	 * 根据 key 名称 模糊查询出所有相关的 key 信息，并返回列表
	 * 
	 * @param name
	 * @return
	 */
	public List<CspKeyInfo> findKeyLikeName(String keyname) {
		if (keyname == null || "".equals(keyname.trim()))
			return null;

		String sql = "SELECT * FROM csp_key_info WHERE key_name LIKE ?";
		List<Object> params = new ArrayList<Object>();
		params.add(keyname);
		final List<CspKeyInfo> keyList = new ArrayList<CspKeyInfo>();

		try {
			this.query(sql, params.toArray(), new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					CspKeyInfo po = new CspKeyInfo();
					po.setKeyId(rs.getInt("id"));
					po.setKeyName(rs.getString("key_name"));
					po.setAliasName(rs.getString("alias_name"));
					po.setParentKeyName(rs.getString("parent_key"));
					po.setKeyScope(rs.getString("key_scope"));
					po.setKeyLevel(rs.getInt("key_level"));
					keyList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keyList;
	}

	public List<CspKeyInfo> findCspKeyLikeName(String keyname, Integer appId,
			Integer n) {
		if (keyname == null || "".equals(keyname.trim()))
			return null;
		keyname = "%" + keyname + "%";
		String sql = "select * from csp_key_info c1 where c1.key_name like ? and c1.id in (SELECT key_id FROM csp_app_key_relation c2 where app_id = ?) order by c1.key_name asc limit ?";
		final List<CspKeyInfo> keyList = new ArrayList<CspKeyInfo>();
		try {
			this.query(sql, new Object[] { keyname, appId, n },
					new SqlCallBack() {
						public void readerRows(ResultSet rs) throws Exception {
							CspKeyInfo po = new CspKeyInfo();
							po.setKeyId(rs.getInt("id"));
							po.setKeyName(rs.getString("key_name"));
							po.setAliasName(rs.getString("alias_name"));
							po.setParentKeyName(rs.getString("parent_key"));
							po.setKeyScope(rs.getString("key_scope"));
							po.setKeyLevel(rs.getInt("key_level"));
							keyList.add(po);
						}
					});
		} catch (Exception e) {
			logger.error("", e);
		}
		return keyList;
	}

	public boolean findKeyByKeyId(Integer keyId, final CspKeyInfo cki) {
		try {
			String sql = "select * from csp_key_info where id = ?";
			this.query(sql, new Object[] { keyId }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					cki.setKeyId(rs.getInt("id"));
					cki.setKeyName(rs.getString("key_name"));
					cki.setAliasName(rs.getString("alias_name"));
					cki.setParentKeyName(rs.getString("parent_key"));
					cki.setKeyScope(rs.getString("key_scope"));
					cki.setKeyLevel(rs.getInt("key_level"));
				}
			});
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 通过key的名称获取这个key 下面的属性名称列表
	 * 
	 * @param keyName
	 * @return
	 */
	public List<String> findKeyPropertyNames(String keyName) {

		String sql = "select kpr.property_name from csp_key_property_relation kpr,csp_key_info k where k.id=kpr.key_id and k.key_name=?";

		final List<String> poList = new ArrayList<String>();

		try {
			this.query(sql, new Object[] { keyName }, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					poList.add(rs.getString("property_name"));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return poList;

	}

	/**
	 *@author wb-lixing 2012-3-31 上午10:28:09
	 * 
	 *@param keyNamePart
	 *            支持keyName模糊查询
	 */
	public Pagination<CspKeyInfo> findKeyListByAppIdPageable(int appId,
			String keyNamePart, int pageNo, int pageSize) {
		final Pagination<CspKeyInfo> page = new Pagination<CspKeyInfo>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);

		String oriSql = "SELECT k.* FROM csp_app_key_relation ak , csp_key_info k WHERE ak.app_id=? and k.key_name like ? AND ak.key_id=k.id order by k.key_name";
		String countSql = "select count(*) total from (" + oriSql + ") t ";

		String sql = oriSql + " limit ?, ?";

		final List<CspKeyInfo> poList = new ArrayList<CspKeyInfo>();

		try {
			// 查询分页数
			this.query(countSql,
					new Object[] { appId, "%" + keyNamePart + "%" },
					new SqlCallBack() {

						public void readerRows(ResultSet rs) throws Exception {
							long total = rs.getLong("total");
							page.setTotalCount((int) total);
						}
					});

			// 分页数据
			this.query(sql, new Object[] { appId, "%" + keyNamePart + "%",
					(pageNo - 1) * pageSize, pageSize }, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					CspKeyInfo po = new CspKeyInfo();
					po.setKeyId(rs.getInt("id"));
					po.setKeyName(rs.getString("key_name"));
					po.setAliasName(rs.getString("alias_name"));
					po.setParentKeyName(rs.getString("parent_key"));
					po.setKeyScope(rs.getString("key_scope"));
					po.setKeyDesc(rs.getString("key_desc"));
					po.setKeyLevel(rs.getInt("key_level"));

					poList.add(po);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		page.setList(poList);
		return page;
	}
	
	
	
	public List<CspKeyInfo> findKeyListByAppId(int appId){
		final List<CspKeyInfo> poList = new ArrayList<CspKeyInfo>();

		
		String sql = "SELECT k.* FROM csp_app_key_relation ak , csp_key_info k WHERE ak.app_id=?  AND ak.key_id=k.id ";
		
		try {

			// 分页数据
			this.query(sql, new Object[] { appId}, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					CspKeyInfo po = new CspKeyInfo();
					po.setKeyId(rs.getInt("id"));
					po.setKeyName(rs.getString("key_name"));
					po.setAliasName(rs.getString("alias_name"));
					po.setParentKeyName(rs.getString("parent_key"));
					po.setKeyScope(rs.getString("key_scope"));
					po.setKeyDesc(rs.getString("key_desc"));
					po.setKeyLevel(rs.getInt("key_level"));

					poList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("csp findKeyListByAppId 错误", e);
		}
		return poList;
	}

	/**
	 * 查询子key*
	 * 
	 * @author wb-lixing 2012-4-6 下午02:47:19
	 */
	public List<CspKeyInfo> findChild(String parentKey) {
		String sql = "SELECT * FROM csp_key_info WHERE parent_key = ?";

		final List<CspKeyInfo> poList = new ArrayList<CspKeyInfo>();

		try {
			this.query(sql, new Object[] { parentKey }, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {

					CspKeyInfo po = new CspKeyInfo();
					po.setKeyId(rs.getInt("id"));
					po.setKeyName(rs.getString("key_name"));
					po.setAliasName(rs.getString("alias_name"));
					po.setParentKeyName(rs.getString("parent_key"));
					po.setKeyScope(rs.getString("key_scope"));
					po.setKeyLevel(rs.getInt("key_level"));
					po.setKeyDesc(rs.getString("key_desc"));
					poList.add(po);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return poList;
	}

	/**
	 * 查询某个应用下的这个key所包含的子key
	 * 
	 * @param appId
	 * @param parentKey
	 * @return
	 */
	public List<CspKeyInfo> findKeyChildByApp(int appId, String parentKey) {
		String sql = "SELECT k.* FROM csp_key_info k,csp_app_key_relation r WHERE r.app_id=? and k.parent_key = ? and r.key_id=k.id";

		final List<CspKeyInfo> poList = new ArrayList<CspKeyInfo>();

		try {
			this.query(sql, new Object[] { appId, parentKey },
					new SqlCallBack() {

						public void readerRows(ResultSet rs) throws Exception {

							CspKeyInfo po = new CspKeyInfo();
							po.setKeyId(rs.getInt("id"));
							po.setKeyName(rs.getString("key_name"));
							po.setAliasName(rs.getString("alias_name"));
							po.setParentKeyName(rs.getString("parent_key"));
							po.setKeyScope(rs.getString("key_scope"));
							po.setKeyLevel(rs.getInt("key_level"));
							po.setKeyDesc(rs.getString("key_desc"));
							poList.add(po);
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
		return poList;
	}

	//根据keyname查询关联的app，如果一个key关联了多个App，则默认使用第一个
	public Map<String,String> findAppNameByKeyName(Set<String> keyNameSet) throws Exception {
	  StringBuilder sb = new StringBuilder("select app.ops_name as opsname,cspkey.key_name as keyname from MS_MONITOR_APP app, csp_app_key_relation rel, csp_key_info cspkey where app.app_id = rel.app_id and rel.key_id =cspkey.id and cspkey.key_name in (");
	  
	  final Map<String,String> map = new HashMap<String,String>();
	  if(keyNameSet.size() == 0)
	    return map;
	  
	  Object[] params = keyNameSet.toArray();
	  for(int i=0; i<params.length; i++) {
	    sb.append("?,");
	  }
	  sb.deleteCharAt(sb.length() - 1).append(")");
	  this.query(sb.toString(),params,new SqlCallBack() {
	    public void readerRows(ResultSet rs) throws Exception {
	      if(rs.getString("opsname") == null) {  //key不可能为null
	        map.put(rs.getString("keyname"), ""); 
	      } else {
	        map.put(rs.getString("keyname"), rs.getString("opsname"));
	      }
	    }
	  });
	  return map;
	}
	
	
	/**
	 * 获取全部hsf 标记的key 与应用对应信息
	 *@author xiaodu
	 * @return
	 * @throws Exception
	 *TODO
	 */
	public Map<String,String> findAppNameByHsfKeyName()  {
		
		String sql = "select app.ops_name as opsname,cspkey.key_name as keyname from ms_monitor_app  app, " +
				"csp_app_key_relation rel, csp_key_info cspkey where app.app_id = rel.app_id and rel.key_id =cspkey.id and cspkey.key_name like 'HSF-provider`%'";
		final Map<String,String> map = new HashMap<String,String>();
		  try {
			this.query(sql,new SqlCallBack() {
			    public void readerRows(ResultSet rs) throws Exception {
			    	
			    	String hsf = rs.getString("keyname");
			    	int index = hsf.indexOf("~");
			    	if(hsf.indexOf("~")>0){
			    		hsf = hsf.substring(0, index);
			    	}
			    	map.put(hsf, rs.getString("opsname"));
			    }
			  });
		} catch (Exception e) {
			logger.error("findAppNameByHsfKeyName: ", e);
		}
		  return map;
		}
	
	
	/**
	 *@author wb-lixing 2012-4-12 下午04:05:29
	 *@param id
	 *@return
	 */
	public boolean deleteKeyMode(int id) {

		String sql = "delete FROM csp_key_alarm_mode where id=?";
		try {
			this.execute(sql, new Object[] { id });
		} catch (SQLException e) {
			logger.error("deleteAppInfoData: ", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 *@author xiaodu
	 * @param need
	 *TODO
	 */
	public void addNeedBaseline(CspNeedBaseline need){
		String sql = "insert into CSP_KEY_NEED_BASELINE(app_name,key_name,property_name)values(?,?,?)";
		try {
			this.execute(sql, new Object[]{need.getAppName(),need.getKeyName(),need.getPropertyName()});
		} catch (SQLException e) {
			logger.error("addNeedBaseline: ", e);
		}
	}
	
	/**
	 * 
	 *@author xiaodu
	 *TODO
	 */
	public void deleteAllNeedBaseline(){
		String sql = "delete from  CSP_KEY_NEED_BASELINE";
		try {
			this.execute(sql);
		} catch (SQLException e) {
			logger.error("addNeedBaseline: ", e);
		}
	}
	
	public Map<String,List<CspNeedBaseline>> findAllNeedBaseline(){
		
		String sql = "select * from CSP_KEY_NEED_BASELINE";
		
		final Map<String,List<CspNeedBaseline>> map = new HashMap<String, List<CspNeedBaseline>>();
		
		try {
			this.query(sql, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					List<CspNeedBaseline> list = map.get(rs.getString("app_name"));
					if(list == null){
						list = new ArrayList<CspNeedBaseline>();
						map.put(rs.getString("app_name"), list);
					}
					CspNeedBaseline need = new CspNeedBaseline();
					need.setAppName(rs.getString("app_name"));
					need.setKeyName(rs.getString("key_name"));
					need.setPropertyName(rs.getString("property_name"));
					
					list.add(need);
				}});
		} catch (Exception e) {
			logger.error("findAllNeedBaseline: ", e);
		}
		return map;
	}
	
	

}
