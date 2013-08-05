package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * ����Ӳ����Ϣ
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-11-2
 */
public class HostHardDao  extends MysqlRouteBase{

	private static Log logger = LogFactory.getLog(HostHardDao.class);
	/**
	 * �ҵ��ڼ�ص�Ӧ�ò���û��Ӳ����Ϣ��online����
	 * 
	 * @return	��󷵻�1W��
	 */
	public Set<String> findNoHardInfoHost() {
		StringBuilder sb=new StringBuilder("select aa.ops_name,aa.nodename from ").
				append("(select a.ops_name,a.nodename from monitor_mian.CSP_APP_HOST_INFO_SYNC a,").
				append("monitor_mian.MS_MONITOR_APP b where a.ops_name=b.app_name").
				append(" and a.state='working_online') aa left join ").
				append("monitor_mian.csp_cost_host_hard_info bb on aa.nodename=bb.host_name ").
				append("where bb.host_name is null limit 10000;");
				
		final Set<String> pos = new HashSet<String>();
		try {
			this.query(sb.toString(), new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					pos.add(rs.getString("ops_name")+","+rs.getString("nodename"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return pos;
	}

	public String  getHostHardInfo(String hostName){
		String sql="select Hard_Info from csp_cost_host_hard_info where HOST_NAME=?";
		final List<String> pos = new ArrayList<String>();
		try {
			this.query(sql, new Object[] {hostName}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					pos.add(rs.getString("Hard_Info"));
				}
			});
		} catch (Exception e) {
			logger.error("getHostHardInfo", e);
		}
		if(pos.size()>0){
			return pos.get(0);
		}
		return null;
	}
	
	public String  getHostHardInfoByIp(String ip){
		
		String sql="select Hard_Info from csp_cost_host_hard_info where Host_ip=?";
		final List<String> pos = new ArrayList<String>();
		try {
			this.query(sql, new Object[] {ip}, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					pos.add(rs.getString("Hard_Info"));
				}
			});
		} catch (Exception e) {
			logger.error("getHostHardInfoByIp", e);
		}
		if(pos.size()>0){
			return pos.get(0);
		}
		return null;
	}
	
	/**
	 * ɾ������Ӳ����Ϣ��һ���������»�ȡ
	 * 
	 * @param apps
	 * @param hosts
	 * @return
	 */
	public boolean deleteHostHardInfo(String[] apps,String[] hosts) {
		StringBuilder sb=new StringBuilder("delete from csp_cost_host_hard_info ");
		StringBuilder appQuery=new StringBuilder();
		StringBuilder hostQuery=new StringBuilder();
		String qs="";
		
		if(apps!=null){
			for(String app:apps){
				appQuery.append(app).append(",");
			}
			sb.append("where OPS_NAME in (?)");
			qs=appQuery.substring(0,appQuery.length()-1);
		}
		if(hosts!=null){
			for(String h:hosts){
				hostQuery.append(h).append(",");
			}
			sb.append("where HOST_NAME in (?)");
			qs=hostQuery.substring(0,appQuery.length()-1);
		}
		try {
			if(StringUtils.isNotBlank(qs)){
				this.execute(sb.toString(), new Object[] {qs});
			}else{
				this.execute(sb.toString());
			}
		} catch (SQLException e) {
			logger.error("insertHostCostDetail����", e);
			return false;
		}
		return true;
	}
	
	/**
	 * ����Ӳ���ɱ���Ϣ
	 * Ӳ����Ϣ��;�ָ�
	 * @param infos
	 * @return
	 */
	public boolean insertHostHardInfo(String[] infos) {
		String sql = "insert into csp_cost_host_hard_info(OPS_NAME,HOST_NAME,Host_ip,Hard_Info," +
				"GMT_CREATE,GMT_MODIFY)"
				+ "values(?,?,?,?,?,?)";
		Date date=new Date();
		
		try {
			this.execute(sql,
					new Object[] { infos[0],infos[1],infos[2],infos[3],date,date});
		} catch (Exception e) {
			logger.error("insert new capacity cost error", e);
			return false;
		}
		
		return true;
	}
}
