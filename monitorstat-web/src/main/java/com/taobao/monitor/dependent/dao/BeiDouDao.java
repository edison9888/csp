package com.taobao.monitor.dependent.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.vo.beidou.BeidouAlarmDataPo;
import com.taobao.monitor.web.vo.beidou.BeidouHostGroupPo;

/**
 * 获取北斗监控DB的实时监控数据和报警指标
 * 
 * @author xiaodu
 * @version 2011-4-27 下午03:37:34
 */
public class BeiDouDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(BeiDouDao.class);

	public BeiDouDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("BEIDOU_ALERT_URL"));
	}

	public Map<String, String> findOracleInfo() {

		String sql = "select  ip_addr,service from hosts";

		final Map<String, String> map = new HashMap<String, String>();

		try {
			this.query(sql, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					map.put(rs.getString("ip_addr"), rs.getString("service"));

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		return map;
	}
	
	/**
	 * 查询所有的dbhost信息
	 */
	public List<BeidouHostGroupPo> findAllGroupHost() {
		String sql = "select group_name,member_name,owner_name from db_host_group";

		final List<BeidouHostGroupPo> list = new ArrayList<BeidouHostGroupPo>();
		try {
			this.query(sql, new Object[] {},
					new SqlCallBack() {
						public void readerRows(ResultSet rs) throws Exception {
							list.add(getBeidouHostGroup(rs));

						}
					});
		} catch (Exception e) {
			logger.error("findAllGroupHost 出错,", e);
		}
		return list;
	}
	
	BeidouHostGroupPo getBeidouHostGroup(ResultSet rs) throws SQLException{
		BeidouHostGroupPo beidouHostGroupDO = new BeidouHostGroupPo();
		//返回的groupName添加db，用于区分应用和db的命名
		beidouHostGroupDO.setGroupName(rs.getString("group_name") + BeidouHostGroupPo.dbConstants);
		beidouHostGroupDO.setMemberName(rs.getString("member_name"));
		beidouHostGroupDO.setOwnerName(rs.getString("owner_name"));
		return beidouHostGroupDO;
	}
	
	public List<BeidouAlarmDataPo> findBeidouAlarmData(String groupName,Date start,Date end){
		String sql = "select check_time,node_name,group_name,mesg,alert_type from alert_msg_history where group_name like ? and check_time>=? and check_time<? and alert_type !=?";

		final List<BeidouAlarmDataPo> list = new ArrayList<BeidouAlarmDataPo>();
		try {
			//过滤掉旺旺告警，旺旺告警阀值设置较低
			this.query(sql, new Object[] {groupName+"%",start,end,"w"},
					new SqlCallBack() {
						public void readerRows(ResultSet rs) throws Exception {
							list.add(getBeidouAlarmData(rs));

						}
					});
		} catch (Exception e) {
			logger.error("findAllGroupHost 出错,", e);
		}
		return list;
	}
	
	BeidouAlarmDataPo getBeidouAlarmData(ResultSet rs) throws SQLException{
		BeidouAlarmDataPo beidouAlarmData = new BeidouAlarmDataPo();
		beidouAlarmData.setAlertGroup(rs.getString("group_name"));
		beidouAlarmData.setAlertMsg(rs.getString("mesg"));
		beidouAlarmData.setAlertSource(rs.getString("node_name"));
		beidouAlarmData.setAlertType(rs.getString("alert_type"));
		beidouAlarmData.setCheckTime(new Date(rs.getTimestamp("check_time").getTime()));
		return beidouAlarmData;
	}

}
