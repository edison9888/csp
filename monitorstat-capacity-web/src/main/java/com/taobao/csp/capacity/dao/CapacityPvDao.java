package com.taobao.csp.capacity.dao;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.po.DomainPvPo;
import com.taobao.csp.capacity.po.PvcountPo;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;


public class CapacityPvDao extends MysqlRouteBase {
	
	private static final Logger logger =  Logger.getLogger(CapacityPvDao.class);
	
	public CapacityPvDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	

	
/**
 * 插入数据
 * @param appId
 * @param date
 * @param mData
 * @param pvType:
 * 				1:pv
 * 				2:apache_pv
 * 				3.hsf_pv
 */
	public void insertToCapacity(int appId, String date, Double pv, String pvType) {
		
		String sql = "INSERT INTO csp_capacity_pv(app_id, collect_time, pv, pv_type) VALUES(?,?,?,?);";
		try {
			this.execute(sql, new Object[]{appId, date,pv, pvType});
		} catch (SQLException e) {
			logger.error("", e);
		}
	
	}

	/**
	 * 插入数据
	 * @param po
	 */
	public void insertToCapacity(PvcountPo po) {
		
		String sql = "INSERT INTO csp_capacity_pv(app_id, collect_time, pv, pv_type, collect_year) VALUES(?,?,?,?,?);";
		try {
			this.execute(sql, new Object[]{po.getAppId(), po.getCollectTime(),po.getPvCount(), po.getPvType(), po.getYear()});
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	/***
	 * 删除指定日期的数据
	 * @param date
	 */
	public void deleteCpapacityByDate(Date date) {
		String sql = "delete from csp_capacity_pv where collect_time = ?";
		try {
			this.execute(sql, new Object[]{ date });
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	
	
	
	
	/**
	 * 查找所有记录数
	 * @return
	 */
	public List<PvcountPo> findAll(){
		
		String sql = "select * from csp_capacity_pv ";
		final List<PvcountPo> list = new ArrayList<PvcountPo>();
		try {
			
			this.query(sql, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					PvcountPo po = new PvcountPo();
					po.setAppId(rs.getInt("app_id"));
					po.setCollectTime(rs.getDate("collect_time"));
					po.setPvCount(rs.getDouble("pv"));
					po.setPvType(rs.getString("pv_type"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	/**
	 * 查询出 这年这个应用每天的pv 情况
	 * @param appId
	 * @param year
	 * @return
	 */
	public List<PvcountPo> findAllByAppIdAndYear(int appId,int year){
		
		String sql = "select * from csp_capacity_pv where app_id = ? and collect_year=? order by collect_time asc";
		final List<PvcountPo> list = new ArrayList<PvcountPo>();
		try {
			
			this.query(sql, new Object[]{appId,year},new SqlCallBack(){
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					PvcountPo po = new PvcountPo();
					po.setAppId(rs.getInt("app_id"));
					po.setCollectTime(rs.getDate("collect_time"));
					po.setPvCount(rs.getDouble("pv"));
					po.setPvType(rs.getString("pv_type"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	
	
	/**
	 * 根据appId查询所有记录
	 * @param appId
	 * @return
	 */
	public List<PvcountPo> findAllByAppId(int appId){
		
		String sql = "select * from csp_capacity_pv where app_id = ? ";
		final List<PvcountPo> list = new ArrayList<PvcountPo>();
		try {
			
			this.query(sql, new Object[]{appId},new SqlCallBack(){
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					PvcountPo po = new PvcountPo();
					po.setAppId(rs.getInt("app_id"));
					po.setCollectTime(rs.getDate("collect_time"));
					po.setPvCount(rs.getDouble("pv"));
					po.setPvType(rs.getString("pv_type"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	/**
	 * 根据appId以及时间段查询所有记录
	 * @param appId
	 * @return
	 */
	public List<PvcountPo> findAllByAppId(Date start, Date end ,int appId){
		
		String sql = "select * from csp_capacity_pv where app_id = ? and collect_time between ? and ?";
		final List<PvcountPo> list = new ArrayList<PvcountPo>();
		try {
			
			this.query(sql, new Object[]{appId, start, end},new SqlCallBack(){
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					PvcountPo po = new PvcountPo();
					po.setAppId(rs.getInt("app_id"));
					po.setCollectTime(rs.getDate("collect_time"));
					po.setPvCount(rs.getDouble("pv"));
					po.setPvType(rs.getString("pv_type"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	/**
	 * 根据appId以及时间返回是否存在记录
	 * @param appId
	 * @return
	 */
	public boolean isExisted(Date date,int appId){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String sql = "select * from csp_capacity_pv where app_id = ? and collect_time = ? ";
		final List<PvcountPo> list = new ArrayList<PvcountPo>();
		try {
			
			this.query(sql, new Object[]{appId, sdf.format(date)},new SqlCallBack(){
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					PvcountPo po = new PvcountPo();
					po.setAppId(rs.getInt("app_id"));
					po.setCollectTime(rs.getDate("collect_time"));
					po.setPvCount(rs.getDouble("pv"));
					po.setPvType(rs.getString("pv_type"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		if(list.size() > 0) {
			if(list.get(0).getPvCount() != 0)
			return true;
		}
		return false;
	}
	
	
	/**
	 * 查询出应用某天的pv 数据
	 * @param appId
	 * @param date
	 * @return
	 */
	public PvcountPo findPvCount(int appId,Date date){
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String sql = "select * from csp_capacity_pv where app_id = ? and collect_time = ? ";
		final PvcountPo po = new PvcountPo();
		try {
			
			this.query(sql, new Object[]{appId, sdf.format(date)},new SqlCallBack(){
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					po.setAppId(rs.getInt("app_id"));
					po.setCollectTime(rs.getDate("collect_time"));
					po.setPvCount(rs.getDouble("pv"));
					po.setPvType(rs.getString("pv_type"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return po.getAppId()==0?null:po;
		
	}
	
	/***
	 * 查询最新的pv数据
	 * @param appId
	 * @return
	 */
	public PvcountPo findLatestPvCount(int appId){
		String sql = "select * from csp_capacity_pv where app_id = ? order by collect_time desc limit 1 ";
		final PvcountPo po = new PvcountPo();
		try {
			this.query(sql, new Object[]{ appId },new SqlCallBack(){
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					po.setAppId(rs.getInt("app_id"));
					po.setCollectTime(rs.getDate("collect_time"));
					po.setPvCount(rs.getDouble("pv"));
					po.setPvType(rs.getString("pv_type"));
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return po.getAppId()==0?null:po;
	}
	
	
	
	/**
	 *
	 * @param year 输入需要预测的年份
	 * @return 返回预测年份的 前两年数据
	 */
	public List<PvcountPo> findFeatureNeedData(int appId,int year){
		
		List<PvcountPo> list = new ArrayList<PvcountPo>();
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, year);
		
		cal.add(Calendar.YEAR, -1);
		
		int pYear = cal.get(Calendar.YEAR);
		cal.add(Calendar.YEAR, -1);
		int ppYear = cal.get(Calendar.YEAR);
		
		List<PvcountPo> pList = findAllByAppIdAndYear(appId, pYear);
		List<PvcountPo> ppList = findAllByAppIdAndYear(appId, ppYear);
		
		list.addAll(pList);
		list.addAll(ppList);
		
		
		
		
		
		return list;
	}
	
	/**
	 * 根据domain以及时间查询所有记录
	 * @param domain
	 * @param date yy-mm-dd
	 * @return
	 */
	public DomainPvPo findAllByDomainAndData(String domain, String date) {
		
		String sql="select * from ap_seo_domain_pv where DOMAIN = ? and GMT_CREATE = ?";
		final DomainPvPo dpp = new DomainPvPo();
		
		try {
			this.query(sql, new Object[]{domain, date + " 00:00:00"}, new SqlCallBack(){
				
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					dpp.setCreatTime(rs.getDate("GMT_CREATE"));
					dpp.setDomain(rs.getString("DOMAIN"));
					if(rs.getInt("ALL_PV") >dpp.getAllPv())
						dpp.setAllPv(rs.getInt("ALL_PV"));
					if(rs.getInt("MEMBER_PV") >dpp.getMemberPv())
						dpp.setMemberPv(rs.getInt("MEMBER_PV"));
					if(rs.getInt("VISIT_PV") >dpp.getVisitPv())
						dpp.setVisitPv(rs.getInt("VISIT_PV"));
				}
			});
			
		} catch (Exception e) {
			logger.error("", e);
		}
		return dpp;
	}
	
	
	
	
	
	
}
