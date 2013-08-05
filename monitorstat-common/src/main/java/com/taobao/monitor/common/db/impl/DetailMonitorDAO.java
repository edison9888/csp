/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.common.db.impl;

import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 这个类从web 中移入common
 * 
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version 2010-5-24:下午08:39:17
 * 
 */
public class DetailMonitorDAO extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(DetailMonitorDAO.class);

	public Map<Integer, Integer> queryStatic(String tableSuffix, String keyIds, int appId, Date start, Date end) {

		final Map<Integer, Integer> keyId2Value = new HashMap<Integer, Integer>();
		String sql = "select a.key_id,a.m_data " + "  from ms_monitor_data_" + tableSuffix + " as a"
				+ " where a.key_id in( " + keyIds + " ) and a.app_id= ? and a.collect_time between ? and ?";

		try {
			this.query(sql, new Object[] { appId, start, end }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					Integer value;

					try {
						Integer keyId = rs.getInt("key_id");

						Integer key_value = rs.getInt("m_data");
						//
						value = keyId2Value.get(keyId);
						if (value == null) {
							value = new Integer(0);
							keyId2Value.put(keyId, value);
						}
						// 在这里进行累加
						// 最好是把这部分逻辑放到外面去，
						value += key_value;
						keyId2Value.put(keyId, value);

					}

					catch (Exception e) {
						logger.warn("rowset transfor error:", e);
					}
				}
			},DbRouteManage.get().getDbRouteByTimeAppid(appId));
		} catch (Exception e) {
			logger.error("detail busi statistic:", e);
		}

		return keyId2Value;
	}

	public Map<Integer, String> queryKeyInfoByLikedName(String keyName) {

		String sql = "select key_id,key_value from ms_monitor_key where key_value like ?";
		final Map<Integer, String> keyInfoMap = new HashMap<Integer, String>();
		try {
			this.query(sql, new Object[] { keyName + "%" }, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					keyInfoMap.put(rs.getInt("key_id"), rs.getString("key_value"));

				}
			});
		} catch (Exception e) {
			logger.error(keyName + "获取 key_id 出错", e);
		}

		return keyInfoMap;

	}

}
