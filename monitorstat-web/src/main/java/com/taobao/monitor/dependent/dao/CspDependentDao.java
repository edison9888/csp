package com.taobao.monitor.dependent.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.dependent.po.AppDependentRelation;
import com.taobao.monitor.dependent.po.AppDependentRelationPo;

/**
 * 
 * @author xiaodu
 * @version 2011-4-22 下午01:58:17
 */
public class CspDependentDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(CspDependentDao.class);

	public CspDependentDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}

	public boolean addDependentMe(AppDependentRelationPo po) {
		String sql = "insert into CSP_DEPENDENT_DEP_ME(self_site,self_ip,self_app_name,dependent_app_name,dependent_ip,"
				+ "dependent_site,dependent_port,dependent_type,collect_time)"
				+ "values(?,?,?,?,?,?,?,?,DATE_FORMAT(?,\"%Y-%m-%d\"))";
		try {
			this.execute(sql, new Object[] { po.getSelfSite(), po.getSelfIp(), po.getSelfOpsName(),
					po.getDependentOpsName(), po.getDependentIp(), po.getDependentSite(), po.getDependentPort(),
					po.getDependentType(), po.getCollectTime() });
		} catch (SQLException e) {
			logger.error("addAppDependentRelation", e);
			return true;
		}
		return false;
	}

	public boolean addMeDependent(AppDependentRelationPo po) {
		String sql = "insert into CSP_DEPENDENT_ME_DEP(self_site,self_ip,self_app_name,dependent_app_name,dependent_ip,"
				+ "dependent_site,dependent_port,dependent_type,collect_time)"
				+ "values(?,?,?,?,?,?,?,?,DATE_FORMAT(?,\"%Y-%m-%d\"))";
		try {
			this.execute(sql, new Object[] { po.getSelfSite(), po.getSelfIp(), po.getSelfOpsName(),
					po.getDependentOpsName(), po.getDependentIp(), po.getDependentSite(), po.getDependentPort(),
					po.getDependentType(), po.getCollectTime() });
		} catch (SQLException e) {
			logger.error("addAppDependentRelation", e);
			return true;
		}
		return false;
	}

	/**
	 * 获取所有我依赖的应用
	 * 
	 * @param self
	 * @param collectTime
	 * @return
	 */
	public List<AppDependentRelationPo> findMeDependent(String self, Date collectTime) {
		String sql = "select * from CSP_DEPENDENT_ME_DEP where self_app_name=? "
				+ "and collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";

		final List<AppDependentRelationPo> list = new ArrayList<AppDependentRelationPo>();

		try {
			this.query(sql, new Object[] { self, collectTime }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppDependentRelationPo po = new AppDependentRelationPo();
					po.setSelfIp(rs.getString("self_ip"));
					po.setSelfSite(rs.getString("self_site"));
					po.setSelfOpsName(rs.getString("self_app_name"));
					po.setDependentOpsName(rs.getString("dependent_app_name"));
					po.setDependentIp(rs.getString("dependent_ip"));
					po.setDependentPort(rs.getInt("dependent_port"));
					po.setDependentSite(rs.getString("dependent_site"));
					po.setCollectTime(new Date(rs.getTimestamp("collect_time").getTime()));

					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("getAppDependentRelation", e);
		}

		return list;

	}

	/**
	 * 获取所有我依赖的应用
	 * @author 斩飞
	 * @param self
	 * @param collectTime
	 * @return
	 * 2011-5-6 - 下午01:31:39
	 */
	public Map<String, List<AppDependentRelation>> findMeDependentByQuery(String self,
			Date collectTime) {
		StringBuffer sql = new StringBuffer("select * from CSP_DEPENDENT_ME_DEP where ");	
		if(self != null && !"".equals(self)){
			sql.append("self_app_name='"+self).append("' and collect_time = " +
					"DATE_FORMAT(?,\"%Y-%m-%d\")");
			
		}else{
			sql.append(" collect_time = DATE_FORMAT(?,\"%Y-%m-%d\")");
		}
		final Map<String, List<AppDependentRelation>> map = new HashMap<String,
	    List<AppDependentRelation>>();
		//final List<AppDependentRelationPo> list = new ArrayList<AppDependentRelationPo>();

		try {
			this.query(sql.toString(), new Object[] { collectTime }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					AppDependentRelation po = new AppDependentRelation();
					String selfAppName = rs.getString("self_app_name");
					po.setSelfIp(rs.getString("self_ip"));
					po.setSelfSite(rs.getString("self_site"));
					po.setName(rs.getString("dependent_app_name"));
					po.setIp(rs.getString("dependent_ip"));
					po.setPort(rs.getInt("dependent_port"));
					po.setSite(rs.getString("dependent_site"));
					List<AppDependentRelation> list = map.get(selfAppName);
					if(list != null){
						list.add(po);
					}else{
						list = new ArrayList<AppDependentRelation>();
						list.add(po);
						map.put(selfAppName, list);
					}	
				}
			});
		} catch (Exception e) {
			logger.error("getAppDependentRelation", e);
		}

		return map;
	}
	
	public Set<String> getAllAppOpsName(Date collectTime) {
		String sql = "select distinct self_app_name from CSP_DEPENDENT_DEP_ME where collect_time=DATE_FORMAT(?,\"%Y-%m-%d\") union"
				+ " select distinct self_app_name from CSP_DEPENDENT_ME_DEP where collect_time=DATE_FORMAT(?,\"%Y-%m-%d\") ";

		final Set<String> set = new HashSet<String>();

		try {
			this.query(sql, new Object[] { collectTime, collectTime }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					set.add(rs.getString("self_app_name"));
				}
			});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return set;
	}

	/**
	 * 获取所有依赖我的应用
	 * 
	 * @param self
	 * @param collectTime
	 * @return
	 */
	public List<AppDependentRelationPo> findDependentMe(String self, Date collectTime) {
		String sql = "select * from CSP_DEPENDENT_DEP_ME where self_app_name=? "
				+ "and collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";

		final List<AppDependentRelationPo> list = new ArrayList<AppDependentRelationPo>();

		try {
			this.query(sql, new Object[] { self, collectTime }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppDependentRelationPo po = new AppDependentRelationPo();
					po.setSelfIp(rs.getString("self_ip"));
					po.setSelfSite(rs.getString("self_site"));
					po.setSelfOpsName(rs.getString("self_app_name"));
					po.setDependentOpsName(rs.getString("dependent_app_name"));
					po.setDependentIp(rs.getString("dependent_ip"));
					po.setDependentPort(rs.getInt("dependent_port"));
					po.setDependentSite(rs.getString("dependent_site"));
					po.setCollectTime(new Date(rs.getTimestamp("collect_time").getTime()));

					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("getAppDependentRelation", e);
		}

		return list;

	}

	/**
	 * 获取所有依赖我的应用
	 * @author 斩飞
	 * @param self
	 * @param collectTime
	 * @return
	 * 2011-5-6 - 下午01:31:39
	 */
	public Map<String, List<AppDependentRelation>> findDependentMeByQuery(String self, Date collectTime) {
		StringBuffer sql = new StringBuffer("select * from CSP_DEPENDENT_DEP_ME where ");
		if(self != null && !"".equals(self)){
			sql.append("self_app_name='"+self).append("' and collect_time = " +
					"DATE_FORMAT(?,\"%Y-%m-%d\")");
			
		}else{
			sql.append(" collect_time = DATE_FORMAT(?,\"%Y-%m-%d\")");
		}
		final Map<String, List<AppDependentRelation>> map = new HashMap<String,
		    List<AppDependentRelation>>();
		//final List<AppDependentRelationPo> list = new ArrayList<AppDependentRelationPo>();
	
		try {
			this.query(sql.toString(), new Object[] { collectTime }, new SqlCallBack() {
	
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					AppDependentRelation po = new AppDependentRelation();
					po.setSelfIp(rs.getString("self_ip"));
					po.setSelfSite(rs.getString("self_site"));
					String selfAppName = rs.getString("self_app_name");
					po.setName(rs.getString("dependent_app_name"));
					po.setIp(rs.getString("dependent_ip"));
					po.setPort(rs.getInt("dependent_port"));
					po.setSite(rs.getString("dependent_site"));
					//po.setCollectTime(new Date(rs.getTimestamp("collect_time").getTime()));
					List<AppDependentRelation> list = map.get(selfAppName);
					if(list != null){
						list.add(po);
					}else{
						list = new ArrayList<AppDependentRelation>();
						list.add(po);
						map.put(selfAppName, list);
					}				
				}
			});
		} catch (Exception e) {
			logger.error("getAppDependentRelation", e);
		}
	
		return map;
	}
	
	public boolean addAppDependentRelation(AppDependentRelationPo po) {

		String sql = "insert into CSP_DEPENDENT_APP_RELATION(self_app_name,dependent_app_name,dependent_ip,"
				+ "dependent_site,dependent_port,dependent_type,collect_time)"
				+ "values(?,?,?,?,?,?,DATE_FORMAT(?,\"%Y-%m-%d\"))";
		try {
			this.execute(sql, new Object[] { po.getSelfOpsName(), po.getDependentOpsName(), po.getDependentIp(),
					po.getDependentSite(), po.getDependentPort(), po.getDependentType(), po.getCollectTime() });
		} catch (SQLException e) {
			logger.error("addAppDependentRelation", e);
			return true;
		}
		return false;
	}

	public AppDependentRelationPo getAppDependentRelation(String self, String dependent, Date collectTime) {
		String sql = "select * from CSP_DEPENDENT_APP_RELATION where self_app_name=? and dependent_app_name=? and collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";

		final AppDependentRelationPo po = new AppDependentRelationPo();

		try {
			this.query(sql, new Object[] { self, dependent, collectTime }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					po.setSelfOpsName(rs.getString("self_app_name"));
					po.setDependentOpsName(rs.getString("dependent_app_name"));
					po.setCollectTime(new Date(rs.getTimestamp("collect_time").getTime()));
				}
			});
		} catch (Exception e) {
			logger.error("getAppDependentRelation", e);
		}

		return po.getSelfOpsName() != null ? po : null;

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

}
