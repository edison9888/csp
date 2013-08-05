package com.taobao.csp.loadrun.web.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.csp.loadrun.core.LoadrunResult;
import com.taobao.csp.loadrun.core.LoadrunResultDetail;
import com.taobao.csp.loadrun.core.constant.AutoLoadMode;
import com.taobao.csp.loadrun.core.constant.AutoLoadType;
import com.taobao.csp.loadrun.core.constant.HttpLoadLogType;
import com.taobao.csp.loadrun.core.constant.ResultDetailType;
import com.taobao.csp.loadrun.core.constant.ResultKey;
import com.taobao.csp.loadrun.web.LoadRunHost;
import com.taobao.csp.loadrun.web.action.show.LoadrunReportObject;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/**
 * 
 * @author xiaodu
 * @version 2010-9-2 上午09:18:05
 */
public class CspLoadRunDao extends MysqlRouteBase {

	private static final Logger logger = Logger.getLogger(CspLoadRunDao.class);
	
	
	public CspLoadRunDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("csp_autoload"));
	}


	/**
	 * 添加压测应用
	 * 
	 * @param loadRunHost
	 */
	public boolean addLoadRunHost(LoadRunHost loadRunHost) {
		String sql = "insert into ms_monitor_loadrun_host(app_id,host_ip,start_time,loadrun_type,load_auto,limit_feature,load_feature," +
				"config_feature,wangwangs,user_name,user_password, app_user, " +
				"jk_config_path,apachectl_path,apache_config_default,apache_config_split," +
				"http_load_path,http_load_proxy,http_load_log_type,http_load_log, start_execute_url, end_execute_url," +
				"loadrun_mode,start_script,end_script,collect_eagleeye)"
			+ " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[] { 
					loadRunHost.getAppId(),
					loadRunHost.getHostIp(),
					loadRunHost.getStartTime(),
					loadRunHost.getLoadType().name(),
					loadRunHost.getLoadAuto(),
					loadRunHost.getLimitFeature(),
					loadRunHost.getLoadFeature(),
					loadRunHost.getConfigFeature(),
					loadRunHost.getWangwangs(),
					loadRunHost.getUserName(),
					loadRunHost.getPassword(),
					loadRunHost.getAppUser(),
					loadRunHost.getJkConfigPath(),
					loadRunHost.getApachectlPath(),
					loadRunHost.getApache_default_config(),
					loadRunHost.getApache_split_config(),
					loadRunHost.getHttploadpath(),
					loadRunHost.getHttploadProxy(),
					loadRunHost.getHttploadlogtype().name(),
					loadRunHost.getHttploadloglog(),
					loadRunHost.getStartUrl(),
					loadRunHost.getEndUrl(),
					loadRunHost.getLoadrunMode().toString(),
					loadRunHost.getStartScript(),
					loadRunHost.getEndScript(),
					loadRunHost.getCollectEagleeye()
					});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		
		return true;
	}
	
	public boolean existAppLoadConfig(int appId){
		
		String sql = "select count(*) from ms_monitor_loadrun_host where app_id=?";
		try {
			int c = this.getIntValue(sql, new Object[]{appId});
			if(c >0){
				return true;
			}else{
				return false;
			}
		} catch (SQLException e) {
			logger.error("", e);
			return true;
		}
	}
	
	
	/**
	 * 更新压测应用
	 * 
	 * @param loadRunHost
	 */
	public boolean updateLoadRunHost(LoadRunHost loadRunHost) {
		String sql = "update ms_monitor_loadrun_host " +
				"set host_ip=?," +
				"start_time=?," +
				"loadrun_type=?," +
				"load_auto=?," +
				"limit_feature=?," +
				"load_feature=?," +
				"config_feature=?," +
				"wangwangs=?," +
				"user_name=?," +
				"user_password=?," +
				"app_user=?," +
				"jk_config_path=?," +
				"apachectl_path=?," +
				"apache_config_default=?," +
				"apache_config_split=? ," +
				"http_load_path=? ," +
				"http_load_proxy=?," +
				"http_load_log_type=?," +
				"http_load_log=?,"+
				"start_execute_url=?,"+
				"end_execute_url=?,"+
				"loadrun_mode=?,"+
				"start_script=?,"+
				"end_script=?,"+
				"collect_eagleeye=?"+
				
				" where app_id=? ";

			try {
			this.execute(sql, new Object[] {
					loadRunHost.getHostIp(),
					loadRunHost.getStartTime(),
					loadRunHost.getLoadType().name(),
					loadRunHost.getLoadAuto(),
					loadRunHost.getLimitFeature(),
					loadRunHost.getLoadFeature(),
					loadRunHost.getConfigFeature(),
					loadRunHost.getWangwangs(),
					loadRunHost.getUserName(),
					loadRunHost.getPassword(),
					loadRunHost.getAppUser(),
					loadRunHost.getJkConfigPath(),
					loadRunHost.getApachectlPath(),
					loadRunHost.getApache_default_config(),
					loadRunHost.getApache_split_config(),
					loadRunHost.getHttploadpath(),
					loadRunHost.getHttploadProxy(),
					loadRunHost.getHttploadlogtype().name(),
					loadRunHost.getHttploadloglog(),
					loadRunHost.getStartUrl(),
					loadRunHost.getEndUrl(),
					loadRunHost.getLoadrunMode().toString(),
					loadRunHost.getStartScript(),
					loadRunHost.getEndScript(),
					loadRunHost.getCollectEagleeye(),
					
					loadRunHost.getAppId()});
		} catch (SQLException e) {
			logger.error("", e);
			return false;
		}
		
		return true;
	}

	/**
	 * 删除loadRunHost
	 * @param appId
	 */
	public boolean deleteLoadRunHost(int appId) {
		String sql = "delete from ms_monitor_loadrun_host where app_id=?";
		try {
			this.execute(sql, new Object[] { appId });
		} catch (SQLException e) {
			logger.error("deleteLoadRunHost: ", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 删除压测结果
	 * @param appId
	 */
	public boolean deleteLoadrunResultById(String loadId) {
		String sql = "delete from ms_monitor_loadrun_result where loadrun_id=?";
		try {
			this.execute(sql, new Object[] { loadId });
		} catch (SQLException e) {
			logger.error("deleteLoadRunHost: ", e);
			return false;
		}
		return true;
	}
	
	/**
	 * 取得所有压测应用机器
	 * 
	 * @return
	 */
	public List<LoadRunHost> findAllLoadRunHost() {
		String sql = "select * from ms_monitor_loadrun_host ";

		final List<LoadRunHost> list = new ArrayList<LoadRunHost>();

		try {
			this.query(sql, new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					
					LoadRunHost loadRunHost = new LoadRunHost();
					fillValue(loadRunHost,rs);
					list.add(loadRunHost);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;

	}
	
	/**
	 * 根据appid取得指定LoadRunHost
	 * 
	 * @return
	 */
	public LoadRunHost findLoadRunHostByAppId(int appId) {
		String sql = "select * from ms_monitor_loadrun_host where app_id=? ";
		final LoadRunHost loadRunHost = new LoadRunHost();
		try {
			this.query(sql, new Object[]{appId},new SqlCallBack() {
				
				public void readerRows(ResultSet rs) throws Exception {
					fillValue(loadRunHost,rs);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}
		return loadRunHost;
		
	}
	
	
	private void fillValue(LoadRunHost loadRunHost,ResultSet rs) throws SQLException{
		loadRunHost.setAppId(rs.getInt("app_id"));
		loadRunHost.setHostIp(rs.getString("host_ip"));
		loadRunHost.setStartTime(rs.getString("start_time"));
		loadRunHost.setLoadAuto(rs.getInt("load_auto"));
		
		loadRunHost.setLoadType(AutoLoadType.valueOf(rs.getString("loadrun_type")));
		
		loadRunHost.setLimitFeature(rs.getString("limit_feature"));
		loadRunHost.setLoadFeature(rs.getString("load_feature"));
		loadRunHost.setConfigFeature(rs.getString("config_feature"));
		loadRunHost.setWangwangs(rs.getString("wangwangs"));
		loadRunHost.setUserName(rs.getString("user_name"));
		loadRunHost.setPassword(rs.getString("user_password"));
		loadRunHost.setAppUser(rs.getString("app_user"));
		loadRunHost.setJkConfigPath(rs.getString("jk_config_path"));
		loadRunHost.setApachectlPath(rs.getString("apachectl_path"));
		loadRunHost.setApache_default_config(rs.getString("apache_config_default"));
		loadRunHost.setApache_split_config(rs.getString("apache_config_split"));
		loadRunHost.setHttploadpath(rs.getString("http_load_path"));
		loadRunHost.setHttploadProxy(rs.getString("http_load_proxy"));
		loadRunHost.setHttploadlogtype(HttpLoadLogType.valueOf(rs.getString("http_load_log_type")));
		loadRunHost.setHttploadloglog(rs.getString("http_load_log"));
		loadRunHost.setStartUrl(rs.getString("start_execute_url"));
		loadRunHost.setEndUrl(rs.getString("end_execute_url"));
		loadRunHost.setLoadrunMode(AutoLoadMode.valueOf(rs.getString("loadrun_mode")));
		loadRunHost.setStartScript(rs.getString("start_script"));
		loadRunHost.setEndScript(rs.getString("end_script"));
		loadRunHost.setCollectEagleeye(rs.getInt("collect_eagleeye"));
	}

	
	
	/**
	 * 取得最近一个压测时间
	 * @param appId
	 * @return
	 */
	public Date findRecentlyLoadDate(int appId){
		String sql = "select max(collect_time) from  ms_monitor_loadrun_result where app_id=?";
		try {
			return this.getDateValue(sql, new Object[]{appId});
		} catch (SQLException e) {
			logger.error("", e);
		}
		return null;
	}
	
	/**
	 * 
	 * @param appId
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<LoadrunResult> findLoadrunResult(int appId,ResultKey key,Date start,Date end){
		
		String sql = "select * from ms_monitor_loadrun_result where app_id=? and loadrun_key=? and collect_time >=? and collect_time <=?";
		
		final List<LoadrunResult> listLoad = new ArrayList<LoadrunResult>();
		
		try {
			this.query(sql, new Object[]{appId,key.name(),start,end}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					LoadrunResult result = new LoadrunResult();
					result.setAppId(rs.getInt("app_id"));
					result.setCollectTime(rs.getDate("collect_time"));
					result.setControlFeature(rs.getString("control_feature"));
					result.setKey(ResultKey.valueOf(rs.getString("loadrun_key")));
					result.setLoadrunOrder(rs.getInt("loadrun_order"));
					result.setLoadrunType(AutoLoadType.valueOf(rs.getString("loadrun_type")));
					result.setTargetIp(rs.getString("target_ip"));
					result.setValue(rs.getDouble("loadrun_value"));
					result.setLoadId(rs.getString("loadrun_id"));
					listLoad.add(result);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	
	/**
	 * 添加压测结果
	 * @param result
	 */
	public void addLoadRunResult(LoadrunResult result){
		
		String sql = "insert into ms_monitor_loadrun_result(app_id,target_ip,loadrun_type,control_feature,loadrun_order,loadrun_key,loadrun_value,collect_time,loadrun_id)values(?,?,?,?,?,?,?,NOW(),?)";
		
		try {
			this.execute(sql, new Object[]{result.getAppId(),result.getTargetIp(),result.getLoadrunType().name(),result.getControlFeature(),result.getLoadrunOrder(),result.getKey().name(),result.getValue(),result.getLoadId()});
		} catch (SQLException e) {
			logger.error("", e);
		}
		
	}
	
	/**
	 * 添加压测结果的详细信息
	 * @param result
	 */
	public void addLoadRunResultDetail(LoadrunResultDetail result){
		
		String sql = "insert into " + getLoadrunDetailTableName(result.getLoadId()) + " (loadrunid,loadrunorder,mkey,skey,counts,times,collect_time)values(?,?,?,?,?,?,?)";
		
		try {
			this.execute(sql, new Object[]{result.getLoadId(),result.getLoadrunOrder(),result.getMainKey().toString(),result.getSecendaryKey(),result.getCount(),result.getTimes(),result.getCollectTime()});
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	public List<LoadrunResultDetail> findLoadrunResultDetail(String loadrunId, final Set<String> filter){
		
		String sql = "select * from " + getLoadrunDetailTableName(loadrunId) + " where loadrunid=? order by mkey,skey,collect_time";
		
		final List<LoadrunResultDetail> listLoad = new ArrayList<LoadrunResultDetail>();
		
		try {
			this.query(sql, new Object[]{ loadrunId }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					LoadrunResultDetail result = new LoadrunResultDetail();
					result.setMainKey(ResultDetailType.valueOf(rs.getString("mkey")));
					result.setSecendaryKey(rs.getString("skey"));
					result.setCount(rs.getDouble("counts"));
					result.setTimes(rs.getDouble("times"));
					result.setCollectTime(rs.getTimestamp("collect_time"));
					result.setLoadrunOrder(rs.getInt("loadrunorder"));
					
					if (result.getMainKey() != ResultDetailType.PERFORMANCE_INDEX && filter != null && filter.size() > 0) {
						for (String item : filter) {
							if (result.getSecendaryKey().indexOf(item) != -1) {
								listLoad.add(result);
								return;
							}
						}
					} else {
						listLoad.add(result);
					}

				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	/***
	 * 查找对应一级key和二级key
	 * 重复时间点做平均
	 * 
	 * @param loadrunId
	 * @param mainKey
	 * @param secondKey
	 * @return
	 */
	public List<LoadrunResultDetail> findLoadrunResultDetailSameTimeAve(String loadrunId, String mkey, String sKey){
		
		String sql = "select mkey, skey, avg(times) as times, collect_time from " + getLoadrunDetailTableName(loadrunId) + 
			" where loadrunid=? and  mkey=? and skey=? group by collect_time order by collect_time";
		
		final List<LoadrunResultDetail> listLoad = new ArrayList<LoadrunResultDetail>();
		final DecimalFormat df = new DecimalFormat("#.##");
		
		try {
			this.query(sql, new Object[]{ loadrunId, mkey, sKey }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					LoadrunResultDetail result = new LoadrunResultDetail();
					
					result.setMainKey(ResultDetailType.valueOf(rs.getString("mkey")));
					result.setSecendaryKey(rs.getString("skey"));
					result.setCount(1);
					result.setTimes(Double.parseDouble(df.format(rs.getDouble("times"))));
					result.setCollectTime(rs.getTimestamp("collect_time"));
					
					listLoad.add(result);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	/***
	 * 查找对应一级key和二级key
	 * 重复时间点汇总
	 * 
	 * @param loadrunId
	 * @param mainKey
	 * @param secondKey
	 * @return
	 */
	public List<LoadrunResultDetail> findLoadrunResultDetailSameTimeSum(String loadrunId, String mkey, String sKey){
		
		String sql = "select mkey, skey, sum(counts) as counts, sum(times) as times, collect_time from " + getLoadrunDetailTableName(loadrunId) + 
			" where loadrunid=? and  mkey=? and skey=? group by collect_time order by collect_time";
		
		final List<LoadrunResultDetail> listLoad = new ArrayList<LoadrunResultDetail>();
		final DecimalFormat df = new DecimalFormat("#");
		
		try {
			this.query(sql, new Object[]{ loadrunId, mkey, sKey }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					double counts = rs.getDouble("counts");
					double times = rs.getDouble("times");
					
					LoadrunResultDetail result = new LoadrunResultDetail();
					result.setMainKey(ResultDetailType.valueOf(rs.getString("mkey")));
					result.setSecendaryKey(rs.getString("skey"));
					result.setCount(Double.parseDouble(df.format(counts)));
					result.setTimes(Double.parseDouble(df.format(times / counts)));
					result.setCollectTime(rs.getTimestamp("collect_time"));
					
					listLoad.add(result);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	/***
	 * 查找对应一级key和二级key，整个压测过程的汇总数据
	 * @param loadrunId
	 * @param mainKey
	 * @param secondKey
	 * @return
	 */
	public List<LoadrunReportObject> findLoadrunResultDetailSum(String loadrunId, String mkey){
		
		String sql = "select skey,sum(counts) as counts,sum(times) as times, max(times / counts) as max_times, min(times / counts) as min_times from " 
			+ getLoadrunDetailTableName(loadrunId) + " where loadrunid=? and mkey=? group by skey order by counts desc";
		
		final List<LoadrunReportObject> listLoad = new ArrayList<LoadrunReportObject>();
		
		try {
			this.query(sql, new Object[]{ loadrunId, mkey }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String resource = rs.getString("skey");
					long totalPv = (long)rs.getDouble("counts");
					long totalRt = (long)rs.getDouble("times");
					
					long minRt = (long)rs.getDouble("min_times");
					long maxRt = (long)rs.getDouble("max_times");
					if (minRt == 0) {
						minRt = 1;
					}
					if (maxRt == 0) {
						maxRt = 1;
					}
					
					LoadrunReportObject result = new LoadrunReportObject();
					result.setResource(resource);
					result.setTotalPv(String.valueOf(totalPv));
					result.setResourceRatio(String.valueOf(totalRt / 1000));
					result.setAvgRt(String.valueOf(totalRt / totalPv));
					result.setMaxRt(String.valueOf(maxRt));
					result.setMinRt(String.valueOf(minRt));
					result.setWaveRatio(String.valueOf((maxRt / minRt )));
					
					listLoad.add(result);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	public List<String> findLoadrunResultDetailTimes(String loadrunId){
		
		String sql = "select distinct concat(DATE_FORMAT(collect_time,'%H:%i:%s'), '(', loadrunorder, ')') as timeFlag from " + getLoadrunDetailTableName(loadrunId) + " where loadrunid=? order by loadrunorder,collect_time";
		
		final List<String> times = new ArrayList<String>();
		
		try {
			this.query(sql, new Object[]{loadrunId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String timeFlag = rs.getString("timeFlag");
					times.add(timeFlag);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return times;
	}
	
	/**
	 * 返回当天压测结果
	 * @param appId
	 * @param collectTime
	 * @return
	 */
	public List<LoadrunResult> findLoadrunResult(int appId,Date collectTime){
		
		String sql = "select * from ms_monitor_loadrun_result where app_id=? and DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		final List<LoadrunResult> listLoad = new ArrayList<LoadrunResult>();
		
		try {
			this.query(sql, new Object[]{appId,collectTime}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					
					LoadrunResult result = new LoadrunResult();
					result.setAppId(rs.getInt("app_id"));
					result.setCollectTime(rs.getTimestamp("collect_time"));
					result.setControlFeature(rs.getString("control_feature"));
					result.setKey(ResultKey.valueOf(rs.getString("loadrun_key")));
					result.setLoadrunOrder(rs.getInt("loadrun_order"));
					result.setLoadrunType(AutoLoadType.valueOf(rs.getString("loadrun_type")));
					result.setTargetIp(rs.getString("target_ip"));
					result.setValue(rs.getDouble("loadrun_value"));
					result.setLoadId(rs.getString("loadrun_id"));
					listLoad.add(result);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		
		return listLoad;
	}
	
	
	/**
	 * 取得最近一个压测时间
	 * @param appId
	 * @return
	 */
	public Date findRecentlyLoadDate(Integer appId){
		String sql = "select max(collect_time) from ms_monitor_loadrun_result where app_id=?";
		try {
			return this.getDateValue(sql, new Object[]{appId});
		} catch (SQLException e) {
			logger.error("", e);
		}
		return null;
	}
	
	public void addLoadRunThreshold(String loadrunId, String limieFeature){
		
		String sql = "insert into ms_monitor_loadrun_threshold (loadrun_id,limit_feature) values(?,?)";
		
		try {
			this.execute(sql, new Object[]{ loadrunId, limieFeature });
		} catch (SQLException e) {
			logger.error("", e);
		}
	}
	
	public String findLoadrunThreshold(String loadrunId) {
		String sql = "select limit_feature from ms_monitor_loadrun_threshold where loadrun_id=?";
		try {
			return this.getString(sql, new Object[]{loadrunId});
		} catch (SQLException e) {
			logger.error("", e);
		}
		return null;
	}
	
	public LoadrunResult findRecentlyAppLoadRunQps(int appId){
		LoadrunResult result = null;
		
		AutoLoadType type = findLoadRunTypeByAppId(appId);
		if(type == null){
			return result;
		}
		
		Date date = findRecentlyLoadDate(appId);
		if(date !=null){
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			Date endDate = cal.getTime();
			cal.add(Calendar.DAY_OF_MONTH, -7); // 一个星期以内
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			Date startDate = cal.getTime();
			
			List<LoadrunResult> list = findLoadrunResult(appId,type.getQpsKey(),startDate,endDate);
			if (list == null || list.size() == 0) {
				return result;
			}
			
			double maxQps = 0;

			if (type != AutoLoadType.apache && type != AutoLoadType.nginxProxy && type != AutoLoadType.apacheProxy) {
				for(LoadrunResult r:list){
					double qps = r.getValue();
					if (maxQps < qps) {
						maxQps = qps;
						result = r;
					}
				}
			} else {
				// apache与nginx类型的压测，与日志的不确定性有关系
				double maxApacheQps = 0;
				LoadrunResult apacheResult = null;
				double maxTomcatQps = 0;
				LoadrunResult tomcatResult = null;
				for(LoadrunResult r:list){
					double qps = r.getValue();
					ResultKey key = r.getKey();
					if (key == ResultKey.Apache_Pv) {
						if (qps > maxApacheQps) {
							maxApacheQps = qps;
							apacheResult = r;
						}
					} else if (key == ResultKey.Tomcat_Pv) {
						if (qps > maxTomcatQps) {
							maxTomcatQps = qps;
							tomcatResult = r;
						}
					}
				}
				
				if (type == AutoLoadType.apache) {
					result = tomcatResult == null ? apacheResult : tomcatResult;
				} else {
					result = apacheResult == null ? tomcatResult : apacheResult;
				}
			}
		}
		
		return result;
	}
	
	public AutoLoadType findLoadRunTypeByAppId(int appId) {
		String sql = "select loadrun_type from ms_monitor_loadrun_host where app_id=? ";
		try {
			String tpye = this.getString(sql, new Object[]{appId});
			if (tpye == null) {
				return null;
			} else {
				return AutoLoadType.valueOf(this.getString(sql, new Object[]{appId}));
			}		
		} catch (Exception e) {
			logger.error("", e);
		}
		return null;
	}
	
	
	
	private String getLoadrunDetailTableName(String loadrunId) {
		int hashCode = loadrunId.hashCode() & 0x7FFFFFFF;
		String prefix = "ms_monitor_loadrun_detail_";
		int bucket = hashCode % 60 + 1;
		String tableFlag = bucket < 10 ? "0" + bucket : String.valueOf(bucket);
		
		return prefix + tableFlag;
	}

}
