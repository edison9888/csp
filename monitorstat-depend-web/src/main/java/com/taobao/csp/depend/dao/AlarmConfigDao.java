package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.alarm.AlarmConfigPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

public class AlarmConfigDao extends MysqlRouteBase {
	private static final Logger logger =  Logger.getLogger(AlarmConfigDao.class);

	public AlarmConfigDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}

	/**
	 * 按报警模式查询
	 * @param alarmMode
	 * @return
	 */
	public List<AlarmConfigPo> getAlarmConfig(int alarmMode) {
		String sql = "select * from csp_alarm_config where alarm_mode=?";
		final List<AlarmConfigPo> list = new ArrayList<AlarmConfigPo>();
		try {
			this.query(sql, new Object[]{alarmMode},  new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					AlarmConfigPo po = new AlarmConfigPo();
					po.setId(rs.getInt("id"));
					po.setAppName(rs.getString("appName"));
					po.setAlarmMode(rs.getInt("alarm_mode"));
					po.setCollectDate(rs.getDate("collect_date"));
					po.setDaysPre(rs.getInt("days_pre"));
					po.setEmailString(rs.getString("emails"));
					po.setWangwangString(rs.getString("wangwangs"));
					po.setLastSendTime(rs.getLong("last_send_time"));
					po.setGMTCreate(rs.getDate("GMT_CREATE"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	/**
	 * 修改发送时间
	 * @param id
	 * @param time
	 * @throws SQLException
	 */
	public void updateSendTime(int id, long time) throws SQLException {
		String sql = "update csp_alarm_config set last_send_time = ? where id = ?";
		this.execute(sql, new Object[]{time, id});
	}
	
	/**
	 * 添加或修改
	 * @param config
	 * @throws SQLException 
	 */
	public void insertOrUpdate(AlarmConfigPo config) throws SQLException {
		if(config.getId()>0) {	//修改
			String sql = "update csp_alarm_config set appName = ?, alarm_mode=?,collect_date=?,days_pre=?,emails=?,wangwangs=?,last_send_time=?,GMT_CREATE=?" +
					" where id=?";
			this.execute(sql, new Object[]{
					config.getAppName(),config.getAlarmMode(),config.getCollectDate(),config.getDaysPre(),
					config.getEmailString(),config.getWangwangString(),config.getLastSendTime(),config.getGMTCreate(),
					config.getId()
			});
		} else {	//插入
			String sql = "insert into csp_alarm_config(appName,alarm_mode,collect_date,days_pre,emails,wangwangs,last_send_time,GMT_CREATE)" +
					"value (?,?,?,?,?,?,?,?)";
			this.execute(sql, new Object[]{
					config.getAppName(),config.getAlarmMode(),config.getCollectDate(),config.getDaysPre(),
					config.getEmailString(),config.getWangwangString(),config.getLastSendTime(),config.getGMTCreate()
			});
		}
	}
}