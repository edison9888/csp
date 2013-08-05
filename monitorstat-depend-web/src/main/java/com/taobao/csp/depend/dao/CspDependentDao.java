package com.taobao.csp.depend.dao;

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

import com.taobao.csp.depend.auto.NetstatInfo;
import com.taobao.csp.depend.po.AppDepApp;
import com.taobao.csp.depend.po.AppDependentRelationPo;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

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
	
	
	public void addUnknownDepIp(String opsName,String unknownType,NetstatInfo info){
		String sql ="insert into CSP_UNKNOWN_IP_DEPEND(ops_name,self_ip,self_port,unknown_dep_ip,dep_port,unknown_type,collect_time)values(?,?,?,?,?,?,CURDATE())";
		try {
			this.execute(sql, new Object[]{opsName,info.getLocalIp(),info.getLocalPort(),info.getForeignIp(),info.getForeignPort(),unknownType});
		} catch (SQLException e) {
			logger.error("addUnknowDepIp", e);
		}
	}
	
	public List<NetstatInfo> findUnknownDepIp(String opsName,String unknownType,Date date){
		
		final List<NetstatInfo> list = new ArrayList<NetstatInfo>();
		
		String sql ="select * from CSP_UNKNOWN_IP_DEPEND where ops_name = ? and unknown_type=? and collect_time=DATE_FORMAT(?,\"%Y-%m-%d\")";
		try {
			this.query(sql, new Object[]{opsName,unknownType,date},new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					NetstatInfo info = new NetstatInfo();
					info.setForeignIp(rs.getString("unknown_dep_ip"));
					info.setForeignPort(rs.getInt("dep_port"));
					info.setLocalIp(rs.getString("self_ip"));
					info.setLocalPort(rs.getInt("self_port"));
					
					list.add(info);
				}});
		} catch (Exception e) {
			logger.error("findUnknowDepIp", e);
		}
		
		return list;
	}
	
	public void deleteAppDepApp(Date collectTime){
		
		String sql ="delete from CSP_APP_DEPEND_APP where collect_time =DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		try {
			this.execute(sql, new Object[]{collectTime});
		} catch (SQLException e) {
			logger.error("addAppDepApp", e);
		}
	}
	
	 public void deleteAppDepAppByName(Date collectTime, String opsName){
	    
	    String sql ="delete from CSP_APP_DEPEND_APP where collect_time =DATE_FORMAT(?,\"%Y-%m-%d\") and ops_name = ?";
	    
	    try {
	      this.execute(sql, new Object[]{collectTime, opsName});
	    } catch (SQLException e) {
	      logger.error("addAppDepApp", e);
	    }
	  }
	
	/** 
	 * 添加应用依赖关系
	 * @param app
	 */
	public void addAppDepApp(AppDepApp app){
	  if(app.getCollectTime() == null) { 
	    String sql = "insert into CSP_APP_DEPEND_APP(ops_name,self_app_type,dep_ops_name,dep_app_type,dep_port,collect_time)values(?,?,?,?,?,CURDATE())";
	    try {
	      this.execute(sql, new Object[]{app.getOpsName(),app.getSelfAppType(),app.getDependOpsName(),app.getDependAppType(),app.getPortInfo()});
	    } catch (SQLException e) {
	      logger.error("addAppDepApp", e);
	    }	    
	  } else {//收集时间不为空则自定义时间
	    String sql = "insert into CSP_APP_DEPEND_APP(ops_name,self_app_type,dep_ops_name,dep_app_type,dep_port,collect_time)values(?,?,?,?,?,?)";
	    try {
	      this.execute(sql, new Object[]{app.getOpsName(),app.getSelfAppType(),app.getDependOpsName(),app.getDependAppType(),app.getPortInfo(), app.getCollectTime()});
	    } catch (SQLException e) {
	      logger.error("addAppDepApp", e);
	    }
	  }
	}
	
	private void fillAppDepend(ResultSet rs,AppDepApp app) throws SQLException{
		app.setOpsName(rs.getString("ops_name"));
		app.setDependOpsName(rs.getString("dep_ops_name"));
		app.setDependAppType(rs.getString("dep_app_type"));
		app.setCollectTime(new Date(rs.getDate("collect_time").getTime()));
		app.setSelfAppType(rs.getString("self_app_type"));
		app.setPortInfo(rs.getInt("dep_port"));
	}
	
	/**
	 * 查找应用依赖信息
	 * @param opsName
	 * @param collectTime
	 * @return
	 */
  public List<AppDepApp> findAppDepend(final String opsName,Date collectTime){
		
		final List<AppDepApp> list = new ArrayList<AppDepApp>();
		
		String sql ="select CSP_APP_DEPEND_APP.*, csp_checkup_depend_config_rel.appStatus as appStatus from CSP_APP_DEPEND_APP left join csp_checkup_depend_config_rel on CSP_APP_DEPEND_APP.dep_ops_name = csp_checkup_depend_config_rel.ops_name " +
				"where CSP_APP_DEPEND_APP.ops_name = ? and collect_time =DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		try {
			this.query(sql, new Object[]{opsName,collectTime}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
				  
          String dependOpsName = rs.getString("dep_ops_name");
          if (opsName != null && !opsName.equals(dependOpsName)
              && !"未知".equals(dependOpsName) && (1 == rs.getInt("appStatus") || 0 == rs.getInt("appStatus"))) { 
            // 过滤掉“自依赖”和“未知”的情况
            AppDepApp app = new AppDepApp();
            fillAppDepend(rs,app);
            list.add(app);				    
          }
				}});
		} catch (Exception e) {
			logger.error("findAppDepend", e);
		}
		return list;
	}
  
  public Set<String> findAppDependSet(final String opsName){
    String sql ="select CSP_APP_DEPEND_APP.*, csp_checkup_depend_config_rel.appStatus as appStatus from CSP_APP_DEPEND_APP left join csp_checkup_depend_config_rel on CSP_APP_DEPEND_APP.dep_ops_name = csp_checkup_depend_config_rel.ops_name " +
        "where CSP_APP_DEPEND_APP.ops_name = ? ";
    final Set<String> set = new HashSet<String>();
    try {
      this.query(sql, new Object[]{opsName}, new SqlCallBack(){
        @Override
        public void readerRows(ResultSet rs) throws Exception {
          String dependOpsName = rs.getString("dep_ops_name");
          if (opsName != null && !opsName.equals(dependOpsName)
              && !"未知".equals(dependOpsName) && (1 == rs.getInt("appStatus") || 0 == rs.getInt("appStatus"))) { 
            // 过滤掉“自依赖”和“未知”的情况
            set.add(dependOpsName);
          }
          
        }});
    } catch (Exception e) {
      logger.error("findAppDependSet", e);
    }
    return set;
  }
  
  /**
   * 查找应用依赖信息
   * @param opsName
   * @param collectTime
   * @return
   */
  public List<AppDepApp> findAppDependByDays(final String opsName,Date collectTime, Date collectTimePre){
    
    final List<AppDepApp> list = new ArrayList<AppDepApp>();
    
    String sql ="select CSP_APP_DEPEND_APP.*, csp_checkup_depend_config_rel.appStatus as appStatus from CSP_APP_DEPEND_APP left join csp_checkup_depend_config_rel on CSP_APP_DEPEND_APP.dep_ops_name = csp_checkup_depend_config_rel.ops_name " +
        "where CSP_APP_DEPEND_APP.ops_name = ? and collect_time <=DATE_FORMAT(?,\"%Y-%m-%d\") and collect_time >=DATE_FORMAT(?,\"%Y-%m-%d\")";
    
    try {
      this.query(sql, new Object[]{opsName,collectTime, collectTimePre}, new SqlCallBack(){
        @Override
        public void readerRows(ResultSet rs) throws Exception {
          
          String dependOpsName = rs.getString("dep_ops_name");
          if (opsName != null && !opsName.equals(dependOpsName)
              && !"未知".equals(dependOpsName) && (1 == rs.getInt("appStatus") || 0 == rs.getInt("appStatus"))) { 
            // 过滤掉“自依赖”和“未知”的情况
            AppDepApp app = new AppDepApp();
            fillAppDepend(rs,app);
            list.add(app);            
          }
        }});
    } catch (Exception e) {
      logger.error("findAppDepend", e);
    }
    return list;
  }
	
	/**
	 * 查找应用依赖信息
	 * @param opsName
	 * @param collectTime
	 * @param category
	 * @return
	 */
	public List<AppDepApp> findAppDepend(String opsName,Date collectTime, String category){
		
		final List<AppDepApp> list = new ArrayList<AppDepApp>();
		
		String sql ="select * from CSP_APP_DEPEND_APP where ops_name = ? and collect_time =DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		try {
			this.query(sql, new Object[]{opsName,collectTime}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					AppDepApp app = new AppDepApp();
					fillAppDepend(rs,app);
					
					list.add(app);
					
				}});
		} catch (Exception e) {
			logger.error("findAppDepend", e);
		}
		return list;
	}	
	
	/**
	 * 查找应用被谁依赖信息
	 * @param opsName
	 * @param collectTime
	 * @return
	 */
	public List<AppDepApp> findDependApp(String opsName,Date collectTime){
		
		final List<AppDepApp> list = new ArrayList<AppDepApp>();
		
		String sql ="select * from CSP_APP_DEPEND_APP where dep_ops_name = ? and collect_time =DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		try {
			this.query(sql, new Object[]{opsName,collectTime}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					AppDepApp app = new AppDepApp();
					fillAppDepend(rs,app);
					
					list.add(app);
					
				}});
		} catch (Exception e) {
			logger.error("findAppDepend", e);
		}
		
		
		return list;
	}
	
	/**
	 * 查找某一天内CSP_APP_DEPEND_APP的所有opsname  
	 */
	public Set<String> findDistinctAppFromAppDependApp(String collectTime) {
	  final Set<String> set = new HashSet<String>();
    
    String sql ="select ops_name from CSP_APP_DEPEND_APP where collect_time =?";
    
    try {
      this.query(sql, new Object[]{collectTime}, new SqlCallBack(){
        @Override
        public void readerRows(ResultSet rs) throws Exception {
          set.add(rs.getString("ops_name"));
        }});
    } catch (Exception e) {
      logger.error("findAppDepend", e);
    }
    return set;
	}
	
	/**
	 * 应用依赖信息与历史应用信息对比
	 * @param opsName
	 * @param preTime			历史时间
	 * @param collectTime		所选时间
	 * @param opt				如ConstantParameters 中定义
	 * @return 				
	 */
	public List<AppDepApp> findHisAppDepend (String opsName,Date preTime,Date collectTime, String opt){
		
		final List<AppDepApp> list = new ArrayList<AppDepApp>();
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (");
		sb.append("select * from CSP_APP_DEPEND_APP where ops_name = ? and collect_time =DATE_FORMAT(?,\"%Y-%m-%d\")");
		sb.append(") as t1");
		sb.append(" where dep_ops_name not in (");
		sb.append("select dep_ops_name from CSP_APP_DEPEND_APP where ops_name = ? and collect_time =DATE_FORMAT(?,\"%Y-%m-%d\"))");
		
		Object[] params = null;
		
		if(ConstantParameters.CONTROST_ADD.equals(opt)) {
			params = new Object[]{opsName, collectTime, opsName, preTime};
		} else if(ConstantParameters.CONTROST_SUB.equals(opt)) {
			params = new Object[]{opsName, preTime, opsName, collectTime};
		}
		
		try {
			this.query(sb.toString(), params, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					AppDepApp app = new AppDepApp();
					fillAppDepend(rs,app);
					list.add(app);
					
				}});
		} catch (Exception e) {
			logger.error("findAppDepend", e);
		}
		sb = null;
		return list;
	}	
	
	/**
	 * 依赖我的历史应用信息的对比
	 * @param opsName
	 * @param preTime			历史时间
	 * @param collectTime		所选时间
	 * @param opt				如ConstantParameters 中定义
	 * @return
	 */
	public List<AppDepApp> findHisDependApp (String opsName,Date preTime,Date collectTime, String opt){
		
		final List<AppDepApp> list = new ArrayList<AppDepApp>();
		
		StringBuilder sb = new StringBuilder();
		sb.append("select * from (");
		sb.append("select * from CSP_APP_DEPEND_APP where dep_ops_name = ? and collect_time =DATE_FORMAT(?,\"%Y-%m-%d\")");
		sb.append(") as t1");
		sb.append(" where ops_name not in (");
		sb.append("select ops_name from CSP_APP_DEPEND_APP where dep_ops_name = ? and collect_time =DATE_FORMAT(?,\"%Y-%m-%d\"))");
		
		Object[] params = null;
		if(opt == ConstantParameters.CONTROST_ADD) {
			params = new Object[]{opsName, collectTime, opsName, preTime};
		} else if(opt == ConstantParameters.CONTROST_SUB) {
			params = new Object[]{opsName, preTime, opsName, collectTime};
		}		
		
		try {
			this.query(sb.toString(), params, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					AppDepApp app = new AppDepApp();
					fillAppDepend(rs,app);
					
					list.add(app);
					
				}});
		} catch (Exception e) {
			logger.error("findAppDepend", e);
		}
		
		sb = null;
		return list;
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
	
	/**
	 * 根据时间过去所有不相同的SQL
	 * @param time
	 * @return
	 */
	public Set<String> getDistinctHsfKeyByTime(Date time, String type, String appName) {
		String table = "csp_hsf_consumer_app_detail";
		String column = "customer_app";
		if(type == "provide") {
			table = "csp_hsf_provider_app_detail";
			column = "provider_app";
		}
		
		String sql = "select distinct key_name from " + table + " where collect_date = ? and " + column + " =?";
		final Set<String> hsfKeySet = new HashSet<String>();
		try {
			this.query(sql, new Object[] {time,appName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					hsfKeySet.add(rs.getString("key_name"));
				}
			});
		} catch (Exception e) {
			logger.error("getDistinctHsfKeyByTime", e);
		}
		return hsfKeySet;
	}
	
	/**
	 * 修改HSF接口的名称
	 * @param time
	 * @return
	 */
	public Set<String> updateHsfKey(String oldKey, String newKey, Date time, String type, String appName) {
		String table = "csp_hsf_consumer_app_detail";
		String column = "customer_app";
		if(type == "provide") {
			table = "csp_hsf_provider_app_detail";
			column = "provider_app";
		}
		String sql = "update " + table + " set key_name = ? where collect_date = ? and key_name = ? and " + column + " =?";
		final Set<String> hsfKeySet = new HashSet<String>();
		try {
			this.execute(sql, new Object[] {newKey, time, oldKey, appName});
		} catch (Exception e) {
			logger.error("updateHsfKey", e);
		}
		return hsfKeySet;
	}	
}
