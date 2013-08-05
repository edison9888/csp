package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.AppDescPo;
import com.taobao.monitor.common.po.AppInfoPo;
import com.taobao.monitor.common.po.AppKeyRelation;
import com.taobao.monitor.common.po.AppUrlRelation;
import com.taobao.monitor.common.po.CspAppRtCount;
import com.taobao.monitor.common.util.Constants;

/**
 * 应用的DAO
 * @author wuhaiqian.pt
 *
 */
public class AppInfoDao extends MysqlRouteBase {
	private static final Logger logger = Logger.getLogger(AppInfoDao.class);


	public AppInfoPo getAppInfoPoByOpsName(String opsName){
		String sql = "select * from MS_MONITOR_APP where OPS_NAME=?";

		final AppInfoPo po = new AppInfoPo();

		try {
			this.query(sql, new Object[]{opsName}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					setApp(po,rs);
				}});
		} catch (Exception e) {
			logger.error("getAppInfoPoByOpsName 出错,", e);
		}
		return po.getAppId()==0?null:po;
	}

	/**
	 * 通过opsname获取日报监控的应用
	 * @author denghaichuan.pt
	 * @param opsName
	 * @return
	 */
	public AppInfoPo getDayAppByOpsName(String opsName){
		String sql = "select * from MS_MONITOR_APP where OPS_NAME=? and day_deploy=?";

		final AppInfoPo po = new AppInfoPo();

		try {
			this.query(sql, new Object[]{opsName, Constants.DEFINE_DATA_EFFECTIVE}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					setApp(po,rs);
				}});
		} catch (Exception e) {
			logger.error("getAppInfoPoByOpsName 出错,", e);
		}
		return po.getAppId()==0?null:po;
	} 

	public AppInfoPo getAppInfoPoByAppName(String appName){
		String sql = "select * from MS_MONITOR_APP where APP_NAME=?";

		final AppInfoPo po = new AppInfoPo();

		try {
			this.query(sql, new Object[]{appName}, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					setApp(po,rs);
				}});
		} catch (Exception e) {
			logger.error("getAppInfoPoByOpsName 出错,", e);
		}
		return po.getAppId()==0?null:po;
	}

	/**
	 * 添加addAppInfoData
	 * @param AppInfoPo
	 */
	public boolean addAppInfoData(AppInfoPo appInfoPo) {
		try {

			String sql = "insert into MS_MONITOR_APP "
					+ "(APP_NAME, SORT_INDEX, FEATURE, OPS_NAME, GROUP_NAME, day_deploy, " +
					"time_deploy, app_status,ops_field,login_name,login_password,app_rush_hours,app_type,company_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			this.execute(sql, new Object[] {appInfoPo.getAppName(), appInfoPo.getSortIndex(),
					appInfoPo.getFeature(), appInfoPo.getOpsName(), appInfoPo.getGroupName()
					, appInfoPo.getDayDeploy(), appInfoPo.getTimeDeploy(), appInfoPo.getAppStatus(),
					appInfoPo.getOpsField(),appInfoPo.getLoginName(),appInfoPo.getLoginPassword(),appInfoPo.getAppRushHours(),appInfoPo.getAppType(), appInfoPo.getCompanyName()});
		} catch (Exception e) {
			logger.error("addAppInfoData 出错,", e);
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * 删除AppInfoPo
	 * @param appId
	 */
	public boolean deleteAppInfoData(int appId) {
		String sql = "delete from MS_MONITOR_APP where APP_ID=?";
		try {
			this.execute(sql, new Object[] { appId });
		} catch (SQLException e) {
			logger.error("deleteAppInfoData: ", e);
			return false;
		}
		return true;
	}

	/**
	 * 更改app的status状态来标记AppInfoPo给删除
	 * @param appId
	 */
	public boolean ModifyAppStatus(int status, int appId) {
		String sql = "update MS_MONITOR_APP set app_status = ? where APP_ID=?";
		try {
			this.execute(sql, new Object[] { status, appId });
		} catch (SQLException e) {
			logger.error("deleteAppInfoData: ", e);
			return false;
		}
		return true;
	}

	/**
	 * 删除AppInfoPo
	 * @param AppInfoPo
	 */
	public boolean deleteAppInfoData(AppInfoPo appInfoPo) {
		String sql = "delete from MS_MONITOR_APP where APP_ID=?";
		try {
			this.execute(sql, new Object[] { appInfoPo.getAppId() });
		} catch (SQLException e) {
			logger.error("deleteTimeConfData: ", e);
			return false;
		}
		return true;
	}

	/**
	 * 获取全部AppInfoPo
	 * 
	 * @return
	 */
	public List<AppInfoPo> findAllAppInfo() {
		String sql = "select * from MS_MONITOR_APP order by app_name ASC";

		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();

		try {
			this.query(sql, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {
					AppInfoPo po = new AppInfoPo();
					setApp(po,rs);
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppInfo: ", e);
		}


		return appInfoPoList;
	}

	/**
	 * 获取全部分组名称
	 * 
	 * @return
	 */
	public List<String> findAllAppGroupName() {
		String sql = "select distinct group_name from MS_MONITOR_APP";

		final List<String> groupNameList = new ArrayList<String>();

		try {
			this.query(sql, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {
					groupNameList.add(rs.getString("GROUP_NAME"));
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppGroupName: ", e);
		}
		return groupNameList;
	}

	/**
	 * 根据分组名称来查找appInfoPo
	 * @param groupName
	 * @return
	 */
	public List<AppInfoPo> findAppInfoByGroupName(String groupName) {
		String sql = "select * from MS_MONITOR_APP where group_name = ?";
		final List<AppInfoPo> appInfoPoList = new ArrayList<AppInfoPo>();
		try {
			this.query(sql, new Object[]{groupName}, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {
					AppInfoPo po = new AppInfoPo();
					setApp(po,rs);
					appInfoPoList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAppInfoByGroupName: ", e);
		}

		return appInfoPoList;
	}

	/**
	 * 根据id来查找appInfoPo
	 * @param appId
	 * @return
	 */
	public AppInfoPo findAppInfoById(int appId) {
		String sql = "select * from MS_MONITOR_APP where app_id=? ";

		final AppInfoPo po = new AppInfoPo();

		try {
			this.query(sql, new Object[]{appId}, new SqlCallBack() {

				public void readerRows(ResultSet rs) throws Exception {
					setApp(po,rs);
				}
			});
		} catch (Exception e) {
			logger.error("findAllAppInfo: ", e);
		}
		return po;
	}


	private void setApp(AppInfoPo po,ResultSet rs) throws SQLException{

		po.setAppId(rs.getInt("APP_ID"));
		po.setAppName(rs.getString("APP_NAME"));
		po.setFeature(rs.getString("feature"));
		po.setSortIndex(rs.getInt("SORT_INDEX"));
		po.setOpsName(rs.getString("OPS_NAME"));
		po.setGroupName(rs.getString("GROUP_NAME"));
		po.setDayDeploy(rs.getInt("day_deploy"));
		po.setTimeDeploy(rs.getInt("time_deploy"));
		po.setAppStatus(rs.getInt("app_status"));
		po.setLoginName(rs.getString("login_name"));
		po.setLoginPassword(rs.getString("login_password"));
		po.setCompanyName(rs.getString("company_name"));
		int app_day_id = rs.getInt("app_day_id");
		String app_day_featrue = rs.getString("app_day_featrue");
		String app_type = rs.getString("app_type");

		String ops_field = rs.getString("ops_field");
		String app_rush_hours = rs.getString("app_rush_hours");
		po.setAppType(app_type);
		po.setOpsField(ops_field);
		po.setAppRushHours(app_rush_hours);
		if(app_day_id != 0){
			po.setAppDayFeature(app_day_featrue);
			po.setDefineType(app_type);
			po.setAppDayId(app_day_id);
		}else{
			po.setAppDayFeature(app_day_featrue);
			po.setDefineType("app");
			po.setAppDayId(po.getAppId());
		}
	}

	/**
	 * 根据AppInfoPo更新
	 * @param AppInfoPo
	 * @return
	 */
	public boolean updateappInfo(AppInfoPo appInfoPo){
		String sql = "update MS_MONITOR_APP set feature=?,APP_NAME=?,SORT_INDEX=?,OPS_NAME=?, " +
				"GROUP_NAME=?, day_deploy=?, time_deploy=? , app_status=? ,ops_field =?,login_name=?,login_password=?,app_rush_hours=?,app_type=?, company_name =? where APP_ID=? ";
		try {
			this.execute(sql, new Object[]{appInfoPo.getFeature(),appInfoPo.getAppName(),
					appInfoPo.getSortIndex(),appInfoPo.getOpsName(),appInfoPo.getGroupName(),
					appInfoPo.getDayDeploy(), appInfoPo.getTimeDeploy(), 
					appInfoPo.getAppStatus(),appInfoPo.getOpsField(),appInfoPo.getLoginName(),appInfoPo.getLoginPassword(),
					appInfoPo.getAppRushHours(),appInfoPo.getAppType(),appInfoPo.getCompanyName(),appInfoPo.getAppId()});
		} catch (SQLException e) {
			logger.error("updateappInfo: ", e);
			return false;
		}
		return true;
	}

	/**
	 * 查询出应用和key的关系信息
	 * @return
	 */
	public List<AppKeyRelation> findAllAppKeyRelation() {
		final List<AppKeyRelation> appList = new ArrayList<AppKeyRelation>();
		try {
			this.query("select * from MS_MONITOR_APP_KEY ", new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AppKeyRelation key = new AppKeyRelation();
					key.setAppId(rs.getInt("APP_ID"));
					key.setKeyId(rs.getInt("KEY_ID"));
					appList.add(key);
				}
			});
		} catch (Exception e) {
			logger.error("读取应用列表 出错", e);
		}

		return appList;
	}
	/**
	 * 添加应用和key的关系
	 * @param appKey
	 * @return
	 */
	public AppKeyRelation addAppKeyRelation(AppKeyRelation appKey) {
		String sql = "insert into MS_MONITOR_APP_KEY(KEY_ID,APP_ID) values(?,?)";
		try {
			this.execute(sql, new Object[] { appKey.getKeyId(), appKey.getAppId() });
		} catch (SQLException e) {
			logger.error("addAppKeyRelation 出错", e);
		}

		return appKey;

	}


	public List<AppDescPo> findAppDesc(){
		String sql = "select * from CSP_APP_Desc";

		final List<AppDescPo> list = new ArrayList<AppDescPo>();

		try {
			this.query(sql, new SqlCallBack() {
				public void readerRows(ResultSet rs) throws Exception {
					AppDescPo key = new AppDescPo();
					key.setAppAttention(rs.getString("app_attention"));
					key.setAppDesc(rs.getString("app_desc"));
					key.setAppPe(rs.getString("app_pe"));
					key.setFirstProductLine(rs.getString("first_product_line"));
					key.setOpsName(rs.getString("ops_name"));
					key.setSecProductLine(rs.getString("sec_product_line"));
					list.add(key);
				}
			});
		} catch (Exception e) {
			logger.error("读取应用列表 出错", e);
		}

		return list;
	}




	/**
	 * 获取全部的应用的URL关系列表
	 * @return
	 */
	public Map<String,List<AppUrlRelation>> findAllAppUrlRelation(){

		String sql = "select * from CSP_APP_URL_RELATION";
		final Map<String,List<AppUrlRelation>> map = new HashMap<String, List<AppUrlRelation>>();

		try {
			this.query(sql, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppUrlRelation url = new AppUrlRelation();
					url.setAppName(rs.getString("app_name"));
					url.setAppUrl(rs.getString("app_url"));
					url.setTopUrl(rs.getString("top_url"));
					url.setModifyDate(new Date(rs.getTimestamp("modify_time").getTime()));
					url.setDynamic("yes".equals(rs.getString("dynamic_url")));
					List<AppUrlRelation> list = map.get(url.getAppName());
					if(list == null){
						list = new ArrayList<AppUrlRelation>();
						map.put(url.getAppName(), list);
					}

					list.add(url);
				}});
		} catch (Exception e) {
			logger.error("findAllAppUrlRelation", e);
		}
		return map;
	}



	/**
	 * 获取全部的应用的URL关系列表
	 * @return Map<URL,appName>
	 */
	public Map<String,String> findAllAppUrlRelationMap(){

		String sql = "select * from CSP_APP_URL_RELATION";
		final Map<String,String> map = new HashMap<String, String>();

		try {
			this.query(sql, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					map.put(rs.getString("app_url"), rs.getString("app_name"));
				}});
		} catch (Exception e) {
			logger.error("findAllAppUrlRelation", e);
		}
		return map;
	}

	/**
	 * 获取全部的应用的URL关系列表
	 * @return
	 */
	public List<AppUrlRelation> findAllAppUrlRelation(String appName){

		String sql = "select * from CSP_APP_URL_RELATION where app_name=?";
		final List<AppUrlRelation> list = new ArrayList<AppUrlRelation>();

		try {
			this.query(sql, new Object[]{appName},new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {

					AppUrlRelation url = new AppUrlRelation();
					url.setId(rs.getInt("id"));
					url.setAppName(rs.getString("app_name"));
					url.setAppUrl(rs.getString("app_url"));
					url.setTopUrl(rs.getString("top_url"));
					url.setModifyDate(new Date(rs.getTimestamp("modify_time").getTime()));
					url.setDynamic("yes".equals(rs.getString("dynamic_url")));
					list.add(url);
				}});
		} catch (Exception e) {
			logger.error("findAllAppUrlRelation", e);
		}
		return list;
	}

	/**
	 * 返回URL 一级域名 和应用的map
	 * @return
	 */
	public Map<String,String> findAllTopUrlRelation(){

		final Map<String,String> map = new HashMap<String, String>();

		String sql = "select top_url,app_name from CSP_APP_URL_RELATION  group by top_url,app_name";

		try {
			this.query(sql, new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					map.put(rs.getString("top_url"), rs.getString("app_name"));

				}});
		} catch (Exception e) {
			logger.error("findAllAppUrlRelation", e);
		}
		return map;

	}


	/**
	 * 返回单个实例
	 * @return
	 */
	public AppUrlRelation getAppUrlRelationById(int id){
		final	AppUrlRelation url = new AppUrlRelation();
		String sql = "select * from CSP_APP_URL_RELATION where id=?";

		try {
			this.query(sql, new Object[]{id},new SqlCallBack(){

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					url.setId(rs.getInt("id"));
					url.setAppName(rs.getString("app_name"));
					url.setAppUrl(rs.getString("app_url"));
					url.setTopUrl(rs.getString("top_url"));
					url.setModifyDate(new Date(rs.getTimestamp("modify_time").getTime()));
					url.setDynamic("yes".equals(rs.getString("dynamic_url")));
				}});
		} catch (Exception e) {
			logger.error("findAllAppUrlRelation", e);
		}
		return url;

	}

	/**
	 * 添加
	 * @param 
	 */
	public boolean addAppUrlRelation(AppUrlRelation po) {
		try {

			String sql = "insert into CSP_APP_URL_RELATION "
					+ "(app_name, app_url, top_url, modify_time, dynamic_url) values(?,?,?,?,?)";
			this.execute(sql, new Object[] {po.getAppName(), po.getAppUrl(), po.getTopUrl(),  po.getModifyDate(),  
					po.isDynamic()==true?"yes":"no"});
		} catch (Exception e) {
			logger.error("addDataBaseInfoData 出错,", e);

			return false;
		}

		return true;
	}

	/**
	 * @param 
	 * @return
	 */
	public boolean updateAppUrlRelation(AppUrlRelation po){
		String sql = "UPDATE csp_app_url_relation SET app_name =?, app_url = ?, top_url = ? , modify_time = ? , dynamic_url = ? where id = ?";
		try {
			this.execute(sql, new Object[] {po.getAppName(), po.getAppUrl(), po.getTopUrl(),  po.getModifyDate(),  
					po.isDynamic()==true?"yes":"no", po.getId()});
		} catch (SQLException e) {
			logger.error("updateDataBaseInfo: ", e);
			return false;
		}
		return true;
	}
	//	public String getPassword(){
	//		String sql = "select ssh_pwd from my_psw_table";
	//	}
	/**
	 * 删除
	 * @param
	 */
	public boolean deleteAppUrlRelation(int id) {
		String sql = "delete from csp_app_url_relation where id=?";
		try {
			this.execute(sql, new Object[] { id });
		} catch (SQLException e) {
			logger.error("deleteAppInfoData: ", e);
			return false;
		}

		return true;
	}


	/**
	 * 添加应用rt 统计数据
	 *@author xiaodu
	 * @param rt
	 *TODO
	 */
	public void addCspAppRtCount(CspAppRtCount rt){

		String sql = "insert into csp_app_rt_count" +
				"(app_name,url,pv_rt_count,pv_rt_100,pv_rt_500,pv_rt_1000,pv_rt_error,pv_rt_type,collect_time)" +
				"values(?,?,?,?,?,?,?,?,?)";
		try {
			this.execute(sql, new Object[]{rt.getAppName(),rt.getUrl(),rt.getPvRtCount(),rt.getPvRt100(),rt.getPvRt500()
					,rt.getPvRt1000(),rt.getPvRtError(),rt.getPvRtType(),rt.getCollectTime()});
		} catch (SQLException e) {
			logger.error("addCspAppRtCount: ", e);
		}
	}
	private void fillCspAppRtCount(CspAppRtCount count, ResultSet rs) throws SQLException {
		count.setAppName(rs.getString("app_name"));
		count.setCollectTime(rs.getDate("collect_time"));
		count.setPvRt100(rs.getLong("pv_rt_100"));
		count.setPvRt1000(rs.getLong("pv_rt_1000"));
		count.setPvRt500(rs.getLong("pv_rt_500"));
		count.setPvRtCount(rs.getLong("pv_rt_count"));
		count.setPvRtError(rs.getLong("pv_rt_error"));
		count.setPvRtType(rs.getString("pv_rt_type"));
		count.setUrl(rs.getString("url"));
	}


	public List<CspAppRtCount> getCspAppRtCountList(String time, String pvRtType){
		final List<CspAppRtCount> list = new ArrayList<CspAppRtCount>();
		String sql = "select * from csp_app_rt_count where collect_time = ? and pv_rt_type = ?";
		//		"(app_name,url,pv_rt_count,pv_rt_100,pv_rt_500,pv_rt_1000,pv_rt_error,pv_rt_type,collect_time)" +
		//		"values(?,?,?,?,?,?,?,?,?)";
		try {
			this.query(sql, new Object[]{time, pvRtType}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspAppRtCount count = new CspAppRtCount();
					fillCspAppRtCount(count, rs);
					list.add(count);
				}
			});
		} catch (Exception e) {
			logger.error("getCspAppRtCountList: ", e);
		}
		return list;
	}
	
	public List<CspAppRtCount> getCspAppRtCountList(String time, String pvRtType, String appName){
		final List<CspAppRtCount> list = new ArrayList<CspAppRtCount>();
		String sql = "select * from csp_app_rt_count where collect_time = ? and pv_rt_type = ? and app_name=?";
		//		"(app_name,url,pv_rt_count,pv_rt_100,pv_rt_500,pv_rt_1000,pv_rt_error,pv_rt_type,collect_time)" +
		//		"values(?,?,?,?,?,?,?,?,?)";
		try {
			this.query(sql, new Object[]{time, pvRtType, appName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspAppRtCount count = new CspAppRtCount();
					fillCspAppRtCount(count, rs);
					list.add(count);
				}
			});
		} catch (Exception e) {
			logger.error("getCspAppRtCountList: ", e);
		}
		return list;
	}	

	public CspAppRtCount getCspAppRtCount(String time, String pvRtType, String queryValue){
		String sql = "select * from csp_app_rt_count where collect_time = ? and pv_rt_type = ?";
		if(pvRtType.equals("app")) {
			sql += " and app_name = ?";
		} else {	//url
			sql += " and url = ?";
		}
		final CspAppRtCount count = new CspAppRtCount();

		try {
			this.query(sql, new Object[]{time, pvRtType, queryValue}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					fillCspAppRtCount(count, rs);
				}
			});
		} catch (Exception e) {
			logger.error("getCspAppRtCountList: ", e);
		}
		return count;
	}
	
	public void deleteAppRtCountByTime(Date time) throws SQLException {
		String sql = "delete from csp_app_rt_count where collect_time = ?";
		this.execute(sql, new Object[]{time});
	}

}
