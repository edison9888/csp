
package com.taobao.monitor.web.core.dao.impl;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.web.distrib.DistribFlowPo;

/**
 * 
 * @author xiaodu
 * @version 2010-11-8 下午04:34:09
 */
public class MonitorDistribFlowDao extends MysqlRouteBase{
	private static final Logger logger =  Logger.getLogger(MonitorJprofDao.class);	
	public MonitorDistribFlowDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Jprof"));
	}
	/**
	 * 根据应用查询出所有的应用提供的服务
	 * @param provideAppName
	 * @param collectDay
	 * @return
	 */
	public List<DistribFlowPo> findProvideAppDistribFlow(String provideAppName,Date collectDay){
		
		String sql = "select * from ms_monitor_distribe_provider where provider_app=? and collect_date=?";
		
		final List<DistribFlowPo> poList = new ArrayList<DistribFlowPo>();
		
		
		
		try {
			this.query(sql, new Object[]{provideAppName,collectDay}, new SqlCallBack(){

				
				public void readerRows(ResultSet rs) throws Exception {
					DistribFlowPo po = new DistribFlowPo();
					po.setCallNum(rs.getLong("call_num"));
					po.setCollectDay(new Date(rs.getTimestamp("collect_date").getTime()));
					po.setCustomerApp(rs.getString("customer_app"));
					po.setKeyName(rs.getString("key_name"));
					po.setMachine_cm(rs.getString("customer_machine_cm"));
					po.setMachine_ip(rs.getString("customer_machine_ip"));
					po.setProviderApp(rs.getString("provider_app"));
					po.setUseTime(rs.getDouble("use_time"));
					
					if(!poList.contains(po)){
						poList.add(po);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		
		return poList;
	}
	
	/**
	 * 根据依赖的应用查询出所有的访问
	 * @param customerAppName
	 * @param collectDay
	 * @return
	 */
	public List<DistribFlowPo> findCustomerAppDistribFlow(String customerAppName,Date collectDay){
		String sql = "select * from ms_monitor_distribe_customer where customer_app=? and collect_date=?";
		
		final List<DistribFlowPo> poList = new ArrayList<DistribFlowPo>();
		
		try {
			this.query(sql, new Object[]{customerAppName,collectDay}, new SqlCallBack(){

				
				public void readerRows(ResultSet rs) throws Exception {
					DistribFlowPo po = new DistribFlowPo();
					po.setCallNum(rs.getLong("call_num"));
					po.setCollectDay(new Date(rs.getTimestamp("collect_date").getTime()));
					po.setCustomerApp(rs.getString("customer_app"));
					po.setKeyName(rs.getString("key_name"));
					po.setMachine_cm(rs.getString("provider_machine_cm"));
					po.setMachine_ip(rs.getString("provider_machine_ip"));
					po.setProviderApp(rs.getString("provider_app"));
					po.setUseTime(rs.getDouble("use_time"));
					if(!poList.contains(po)){
						poList.add(po);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return poList;
	}
	
	
	/**
	 * 获取所有提供服务的应用
	 * @return
	 */
	public Set<String> findAllProviderAppName(){
		String sql = "select provider_app from ms_monitor_distribe_provider group by provider_app";
		final Set<String> set = new  HashSet<String>();
		try {
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					set.add(rs.getString("provider_app"));
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return set;
	}
	
	/**
	 * 获取所有依赖服务的应用
	 * @return
	 */
	public Set<String> findAllCustomerAppName(){
		String sql = "select customer_app from ms_monitor_distribe_customer group by customer_app";
		final Set<String> set = new  HashSet<String>();
		try {
			this.query(sql, new SqlCallBack(){
				
				public void readerRows(ResultSet rs) throws Exception {
					set.add(rs.getString("customer_app"));
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return set;
	}
	

}
