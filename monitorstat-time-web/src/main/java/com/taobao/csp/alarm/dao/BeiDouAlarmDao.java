package com.taobao.csp.alarm.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.time.web.po.BeiDouAlarmRecordPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.CspCacheTBHostInfos;

public class BeiDouAlarmDao extends MysqlRouteBase{
	private static final Logger logger = Logger.getLogger(MysqlRouteBase.class);
	public BeiDouAlarmDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("BEIDOU_ALERT_URL"));
	}
	public List<BeiDouAlarmRecordPo> getDbAlarmInfoRecently(Integer n) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, -n);
		final List<BeiDouAlarmRecordPo> list =new ArrayList<BeiDouAlarmRecordPo>();
		String sql = "select d.db_name,h.* from alert_msg_history h,db_host d where d.host_name=h.alert_source and h.check_time > ?";
		try {
			this.query(sql, new Object[]{cal.getTime()}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					BeiDouAlarmRecordPo po = new BeiDouAlarmRecordPo();
					fillPo(rs,po);
					list.add(po);
				}});
		} catch (Exception e) {
			logger.info(e);
		}
		return list;
	}
	private void fillPo(ResultSet rs, BeiDouAlarmRecordPo po) throws SQLException{
		po.setAlarmMsg(rs.getString("alert_msg"));
		po.setHostName(rs.getString("alert_source"));
		po.setAlarmType(rs.getString("alert_type"));
		po.setAlarmClass(rs.getString("alert_class"));
		po.setTime(rs.getTimestamp("check_time"));
		po.setDbName(rs.getString("db_name"));
		HostPo hostPo = CspCacheTBHostInfos.get().getHostInfoByHostName(po.getHostName());
		if(hostPo!=null){
			try {
				po.setIp(hostPo.getHostIp());
				po.setGroupName(hostPo.getOpsName());
			} catch (Exception e) {
				logger.info(e);
			}
		}
	}
	public static void main(String args[]){
		
	}
}
