package com.taobao.csp.cost.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.taobao.csp.cost.po.HostCostDetail;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.HostPo;
import com.taobao.monitor.common.util.DateUtil;

/**
 * 单机器成本/某个机器类型的成本
 * 
 * 如果没有的话啊，按照机器类型获取
 * 
 * @author <a href="mailto:bishan.ct@taobao.com">bishan.ct</a>
 * @version 1.0
 * @since 2012-9-11
 */
public class HostDetailCostDao extends MysqlRouteBase  {

	private static Log logger = LogFactory.getLog(HostDetailCostDao.class);
	
	public HostDetailCostDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	/**
	 * 更新单机成本
	 * 
	 * @param hostPo
	 * @param hostCost
	 * @return
	 */
	public boolean editHostCostDetail(HostPo hostPo,double hostCost) {
		HostCostDetail hcd=getHostCostDetail(hostPo.getHostName());
		String sql="";
		Date date=new Date();
		if(hcd!=null){
			//update
			sql = "update csp_cost_host_price set HOST_PRICE=?,GMT_MODIFY=? where HOST_NAME=?";
			try {
				this.execute(sql, new Object[]{ hostCost,
						DateUtil.getDateYMDHMSFormat().format(date),hostPo.getHostName() });
			} catch (Exception e) {
				logger.error("updateHostCostDetail出错", e);
				return false;
			}
		}else{
			//insert
			sql = "insert into csp_cost_host_price"
					+ "(HOST_NAME,HOST_TYPE,HOST_PRICE,OPS_NAME,GMT_CREATE,GMT_MODIFY) values(?,?,?,?,?,?) ";
			try {
				this.execute(sql, new Object[] { hostPo.getHostName(),
						hostPo.getHostType(), hostCost,
						hostPo.getOpsName(), DateUtil.getDateYMDHMSFormat().format(date),
						 DateUtil.getDateYMDHMSFormat().format(date)});
			} catch (SQLException e) {
				logger.error("insertHostCostDetail出错", e);
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * 获得单台机器的单价，如果没有记录，代表需要按照机器类型获取
	 * 
	 * @param hostName
	 * @return
	 */
	public HostCostDetail getHostCostDetail(String hostName) {
		final List<HostCostDetail> list = new ArrayList<HostCostDetail>();
		
		String sql = "select * from csp_cost_host_price where HOST_NAME=?";
		try {
			this.query(sql, new Object[]{ hostName }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HostCostDetail hcd = new HostCostDetail();
					hcd.setHostPrice(rs.getDouble("HOST_PRICE"));
					hcd.setHostName(rs.getString("HOST_NAME"));
					hcd.setHostType(rs.getString("HOST_TYPE"));
					hcd.setOpsName(rs.getString("OPS_NAME"));

					list.add(hcd);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list.size() > 0 ? list.get(0) : null;
	}
	
	/**
	 * 获取某个机器类型的单价
	 * 
	 * @param hostType
	 * @return
	 */
	public double getHostTypePrice(String hostType) {
		final List<Double> list = new ArrayList<Double>();
		String sql = "select * from csp_coat_host_type_price where HOST_TYPE=?";
		try {
			this.query(sql, new Object[]{ hostType }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {

					list.add(rs.getDouble("PRICE"));
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list.size() > 0 ? list.get(0) : 1234;
	}
	
	/**
	 * 获取所有机器类型的单价
	 * 
	 * @return
	 */
	public Map<String,Double> getAllHostTypePrice() {
		final Map<String,Double> maps = new HashMap<String,Double>();
		String sql = "select * from csp_cost_host_type_price";
		try {
			this.query(sql,new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					maps.put(rs.getString("HOST_TYPE"), rs.getDouble("PRICE"));
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return maps;
	}
	
	
	/**
	 * 获取所有机器的详细信息
	 * @return
	 */
	public List<HostCostDetail> findAllHostPrice() {
		final List<HostCostDetail> pos = new ArrayList<HostCostDetail>();
		String sql = "select * from csp_cost_host_price order by OPS_NAME";
		
		try {
			this.query(sql, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HostCostDetail hcd = new HostCostDetail();
					hcd.setHostPrice(rs.getDouble("HOST_PRICE"));
					hcd.setHostName(rs.getString("HOST_NAME"));
					hcd.setHostType(rs.getString("HOST_TYPE"));
					hcd.setOpsName(rs.getString("OPS_NAME"));

					pos.add(hcd);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return pos;
	}
}
