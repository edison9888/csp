
/**
 * monitorstat-common
 */
package com.taobao.monitor.common.db.impl.center;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.CspAppDepAppPo;
import com.taobao.monitor.common.po.CspAppHotInterface;
import com.taobao.monitor.common.po.CspCheckupDependNetstatInfo;
import com.taobao.monitor.common.po.CspKeyDependInfoPo;
import com.taobao.monitor.common.po.CspTimeAppDependInfo;
import com.taobao.monitor.common.po.CspTimeExtraAppInfo;
import com.taobao.monitor.common.po.CspTimeExtraAppKey;
import com.taobao.monitor.common.po.CspTimeKeyDependInfo;
import com.taobao.monitor.common.po.HsfOpsPo;
import com.taobao.monitor.common.po.ReleaseInfo;
import com.taobao.monitor.common.po.ReportInvokeDataPo;

public class CspDependInfoDao extends MysqlRouteBase{

	private static final Logger logger = Logger.getLogger(CspDependInfoDao.class);
	public CspDependInfoDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}

	public static final long LIMIT_SIZE = 1000;
	public static final int APP_TYPE = 1;
	public static final int KEY_TYPE = 0;

	public static final int ME_DEPEND = 0;
	public static final int DEPEND_ME = 1;

	public static final String SEARCHTYPE_APP = "APP";
	public static final String SEARCHTYPE_KEY = "KEY";


	/**
	 * 清空表自动表的的数据，索引置为0
	 * 
	 * @throws SQLException
	 */
	public void clearApp_CspKeyDependInfo(){
		try {

			// 清空表中数据，重置索引
			this.execute("delete from csp_key_depend_info where d_level = 'APP'");
			//this.execute("alter table csp_key_depend_info AUTO_INCREMENT = 1");

		} catch (SQLException e) {
			logger.error(e);
		} finally {
			logger.info("清空依赖关系，计数清零");			
		}
	}

	public void deleteKeyByUrl_CspKeyDependInfo(String url){
		String sql = "delete from csp_key_depend_info where url=? and d_level = 'KEY'";
		try {
			this.execute(sql, new Object[]{url});
		} catch (SQLException e) {
			logger.info("deleteKeyByUrl_CspKeyDependInfo",e);			
		}
	}

	public void deleteAppByUrl_CspKeyDependInfo(String url){
		String sql = "delete from csp_key_depend_info where url=? and d_level = 'APP'";
		try {
			this.execute(sql, new Object[]{url});
		} catch (SQLException e) {
			logger.info("deleteAppByUrl_CspKeyDependInfo",e);			
		}
	}
	/**
	 * 插入key依赖关系到表csp_key_depend_info
	 * @param curKeyname
	 * @param parentKeyname
	 * @param url
	 * @throws Exception 
	 */
	public void insertKeyRelToDependInfo(String curKeyname,String curApp, String parentKeyname,String parentApp, String url) throws Exception {

		String sql = "select id,cur_keyname,parent_keynames from csp_key_depend_info where cur_keyname = ? and url = ? and parent_keynames = ? and d_level = 'KEY'";
		final List<CspKeyDependInfoPo> list = new ArrayList<CspKeyDependInfoPo>();
		this.query(sql, new Object[]{curKeyname, url, parentKeyname}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspKeyDependInfoPo po = new CspKeyDependInfoPo();
				po.setId(rs.getLong("id"));
				po.setCurKeyame(rs.getString("cur_keyname"));
				po.setParentKeynames(rs.getString("parent_keynames"));
				list.add(po);
			}
		});

		if(list.size() == 0) {
			sql = "insert into csp_key_depend_info(cur_appName,cur_keyname,parent_appName,parent_keynames,url,d_level) value (?,?,?,?,?,?)";
			this.execute(sql, new Object[] { curApp,curKeyname, parentApp,parentKeyname, url, "KEY" });
		} 
		//		else {  //否则不处理
		//		} 
	}

	/**
	 * 根据时间查询，返回表csp_app_depend_app的list
	 * 
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public List<CspAppDepAppPo> getAppDepAppList(String time) throws Exception {
		String sql = "select ops_name,dep_ops_name,dep_app_type,collect_time from csp_app_depend_app where collect_time = ?";
		final List<CspAppDepAppPo> list = new ArrayList<CspAppDepAppPo>();
		this.query(sql, new Object[] { time }, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspAppDepAppPo po = new CspAppDepAppPo();
				po.setOpsName(rs.getString("ops_name"));
				po.setDepOpsName(rs.getString("dep_ops_name"));
				po.setDepAppType(rs.getString("dep_app_type"));
				po.setCollectTime(rs.getString("collect_time"));
				list.add(po);
			}
		});
		return list;
	}

	public List<CspAppDepAppPo> getSubAppDepAppList(String time, String appName) throws Exception {
		String sql = "select ops_name,dep_ops_name,dep_app_type,collect_time from csp_app_depend_app where collect_time = ? and ops_name=?";
		final List<CspAppDepAppPo> list = new ArrayList<CspAppDepAppPo>();
		this.query(sql, new Object[] { time, appName}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspAppDepAppPo po = new CspAppDepAppPo();
				po.setOpsName(rs.getString("ops_name"));
				po.setDepOpsName(rs.getString("dep_ops_name"));
				po.setDepAppType(rs.getString("dep_app_type"));
				po.setCollectTime(rs.getString("collect_time"));
				list.add(po);
			}
		});
		return list;
	}

	public List<CspAppDepAppPo> getAppDepAppList(String time, String appName) throws Exception {
		String sql = "select ops_name,dep_ops_name,dep_app_type,collect_time from csp_app_depend_app where collect_time = ?";
		final List<CspAppDepAppPo> list = new ArrayList<CspAppDepAppPo>();
		this.query(sql, new Object[] { time }, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspAppDepAppPo po = new CspAppDepAppPo();
				po.setOpsName(rs.getString("ops_name"));
				po.setDepOpsName(rs.getString("dep_ops_name"));
				po.setDepAppType(rs.getString("dep_app_type"));
				po.setCollectTime(rs.getString("collect_time"));
				list.add(po);
			}
		});
		return list;
	}

	/**
	 * 插入App的依赖关系到表csp_key_depend_info
	 * 
	 * @param curAppname
	 * @param parentAppname
	 * @param dependTypeId
	 * @throws Exception
	 */
	public void insertAppRelToDependInfo(CspAppDepAppPo po)
			throws Exception {

		String curAppname, parentAppname, dependTypeName;
		curAppname = po.getDepOpsName();
		parentAppname = po.getOpsName();
		dependTypeName = po.getDepAppType();

		String sql = "select id from csp_key_depend_info where cur_appName = ? and d_level = 'APP' and parent_appName = ?";
		String id = this.getString(sql, new Object[] { curAppname, parentAppname });
		if (id != null) {
			sql = "update csp_key_depend_info set parent_appTypeName = ? where id = ?";
			this.execute(sql, new Object[] { dependTypeName, id });
			return;
		}

		sql = "insert into csp_key_depend_info(cur_appName,parent_appName,parent_appTypeName,d_level) value (?,?,?,'APP')";
		this.execute(sql,
				new Object[] { curAppname, parentAppname, dependTypeName });
	}

	/*
	 * 查询程序自动生成的依赖关系。
	 */
	public Map<String, CspKeyDependInfoPo> getCspKeyDependInfoAuto(final String appName, final String type) throws Exception {
		String sql;
		final Map<String, CspKeyDependInfoPo> map = new HashMap<String, CspKeyDependInfoPo>();
		if(CspDependInfoDao.SEARCHTYPE_APP.equals(type)) {
			sql = "select * from csp_key_depend_info where parent_appName = ? and d_level = 'APP'";
		} else {
			return map;
		}
		this.query(sql, new Object[] { appName}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspKeyDependInfoPo po = new CspKeyDependInfoPo();
				setCspKeyDependInfoPo(po,rs);
				if(CspDependInfoDao.SEARCHTYPE_KEY.equals(type)) {
					map.put(rs.getString("cur_keyName"), po);
				} else if(CspDependInfoDao.SEARCHTYPE_APP.equals(type)) {
					map.put(rs.getString("cur_appName"), po);
				}
			}
		});
		return map;
	}

	private void setCspKeyDependInfoPo(CspKeyDependInfoPo po, ResultSet rs) throws SQLException {
		po.setId(rs.getLong("id"));
		po.setCurAppName(rs.getString("cur_appName"));
		po.setDepend_me_appname(rs.getString("parent_appName"));
		po.setCurKeyame(rs.getString("cur_keyname"));
		po.setParentKeynames(rs.getString("parent_keynames"));
		po.setDepend_app_type(rs.getString("parent_appTypeName"));
		po.setdDevel(rs.getString("d_level"));
		po.setUrl(rs.getString("url"));
		po.setConfigType("auto");    
	}

	public void saveAppConfigAuto(CspKeyDependInfoPo po) throws SQLException {
		String sql = "insert into csp_key_depend_info(cur_appName,parent_appName,d_level) value(?,?,'APP')";
		this.execute(sql,
				new Object[] { po.getCurAppName(), po.getDepend_me_appname() });
	}

	public void deleteAppByNameSetAuto(Set<String> appNameSet, String appName,
			final int type) {
		for (String appNameTarget : appNameSet) {
			try {
				String sql = "delete from csp_key_depend_info where cur_appName = ? and d_level = 'APP' and parent_appName = ?";
				Object[] params = null;
				if (type == ME_DEPEND) {
					params = new Object[] { appNameTarget, appName };
				} else if (type == DEPEND_ME) {
					params = new Object[] { appName, appNameTarget };
				} else {
					return;
				}
				this.execute(sql, params);
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	/*
	 * 查询getCspTimeAppDependInfo
	 */
	public Map<String, CspTimeAppDependInfo> getCspTimeAppDependInfo(String sourceAppName, String appName) throws Exception{
		final Map<String, CspTimeAppDependInfo> map = new HashMap<String, CspTimeAppDependInfo>();
		String sql = "select * from csp_time_app_depend_info where source_app_name = ? and app_name = ?";
		this.query(sql, new Object[] { sourceAppName, appName}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeAppDependInfo po = new CspTimeAppDependInfo();
				setCspTimeAppDependInfoQuery(po, rs);
				map.put(po.getDepAppName(), po);
			}
		});
		return map;
	}

	public void deleteCspTimeAppDependInfo(String[] ids) throws Exception{
		if(ids.length == 0)
			return;
		StringBuilder sb = new StringBuilder("delete from csp_time_app_depend_info where id in (");
		for(String id : ids) {
			sb.append(id).append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append(")");

		this.execute(sb.toString());
	}  

	public void insertCspTimeAppDependInfo(CspTimeAppDependInfo info) throws Exception{
		List<CspTimeAppDependInfo> list = new ArrayList<CspTimeAppDependInfo>();
		list.add(info);
		insertCspTimeAppDependInfo(list);
	} 

	public void insertCspTimeAppDependInfo(List<CspTimeAppDependInfo> insertList) throws Exception{
		if(insertList == null ||insertList.size() == 0)
			return;

		StringBuilder sb = new StringBuilder("insert into csp_time_app_depend_info(app_name,dep_app_name,source_app_name,depend_status,depend_type) values(?,?,?,?,?)");
		List<Object[]> parmList = new ArrayList<Object[]>();
		for(CspTimeAppDependInfo info : insertList) {
			parmList.add(new Object[]{info.getAppName(),info.getDepAppName(),info.getSourceAppName(),info.getDependStatus(),info.getDependtype()});
		}
		this.executeBatch(sb.toString(), parmList);
	} 

	public List<CspTimeAppDependInfo> getCspTimeAppDependInfo(String sourceAppName) throws Exception{
		final List<CspTimeAppDependInfo> list = new ArrayList<CspTimeAppDependInfo>();
		String sql = "select * from csp_time_app_depend_info where source_app_name = ?";
		this.query(sql, new Object[] { sourceAppName}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeAppDependInfo po = new CspTimeAppDependInfo();
				setCspTimeAppDependInfoQuery(po, rs);
				list.add(po);
			}
		});
		return list;
	}

	private void setCspTimeAppDependInfoQuery(CspTimeAppDependInfo po, ResultSet rs) throws SQLException {
		po.setId(rs.getLong("id"));
		po.setSourceAppName(rs.getString("source_app_name"));
		po.setAppName(rs.getString("app_name"));
		po.setDepAppName(rs.getString("dep_app_name"));
		po.setDependtype(rs.getString("depend_type"));
		po.setDependStatus(rs.getString("depend_status"));
	}

	/*
	 * 查询程序自动生成的依赖关系。
	 */
	public List<CspKeyDependInfoPo> getDependMapByIds_Auto(String[] ids) throws Exception {

		if(ids.length == 0)
			return new ArrayList<CspKeyDependInfoPo>();

		StringBuilder sb = new StringBuilder("select * from csp_key_depend_info where id in (");
		for(String id : ids) {
			sb.append(id).append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append(")");
		final List<CspKeyDependInfoPo> list = new ArrayList<CspKeyDependInfoPo>();
		this.query(sb.toString(), new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspKeyDependInfoPo po = new CspKeyDependInfoPo();
				setCspKeyDependInfoPo(po, rs);
				list.add(po);
			}
		});
		return list;
	}

	public List<CspTimeKeyDependInfo> getCspTimeKeyDependInfo(String sourceAppName) throws Exception {
		final List<CspTimeKeyDependInfo> list = new ArrayList<CspTimeKeyDependInfo>();
		String sql = "select * from csp_time_key_depend_info where source_app_name = ?";
		this.query(sql, new Object[] { sourceAppName}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
				setCspTimeKeyDependInfoQuery(po, rs);
				list.add(po);
			}
		});
		return list;
	}  

	public List<CspTimeKeyDependInfo> getCspTimeKeyDependInfo_Key(String sourceAppName, String sourceKeyName) throws Exception{
		final List<CspTimeKeyDependInfo> list = new ArrayList<CspTimeKeyDependInfo>();
		String sql = "select * from csp_time_key_depend_info where source_app_name = ? and source_key_name = ?";
		this.query(sql, new Object[] { sourceAppName, sourceKeyName}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
				setCspTimeKeyDependInfoQuery(po, rs);
				list.add(po);
			}
		});
		return list;
	}  

	public Map<String, CspTimeKeyDependInfo> getCspTimeKeyDependInfoSub(String sourceAppName, String sourceKeyName, String keyName) throws Exception{
		final Map<String, CspTimeKeyDependInfo> map = new HashMap<String, CspTimeKeyDependInfo>();
		String sql = "select * from csp_time_key_depend_info where source_app_name = ? and source_key_name = ? and key_name = ?";
		this.query(sql, new Object[] { sourceAppName, sourceKeyName, keyName}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
				setCspTimeKeyDependInfoQuery(po, rs);
				map.put(po.getDependKeyName(), po);
			}
		});
		return map;
	}  

	/**
	 * 这个是通过key的名称。获取所有它依赖的key的信息
	 *@author xiaodu
	 * @param keyName
	 * @return
	 * @throws Exception
	 *TODO
	 */
	public List<CspTimeKeyDependInfo> getKeyDependByKeyName( String appName,String keyName) {
		final List<CspTimeKeyDependInfo> list = new ArrayList<CspTimeKeyDependInfo>();
		String sql = "select * from csp_time_key_depend_info where  key_name = ? and app_name=?";
		try {
			this.query(sql, new Object[] { keyName,appName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
					setCspTimeKeyDependInfoQuery(po, rs);

					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.info("getKeyDependByKeyName",e);			
		}
		return list;
	}


	/**
	 * 通过key名称，查询出所有对应这个key的执行路径
	 *@author xiaodu
	 * @param keyName
	 * @return
	 * @throws Exception
	 *TODO
	 */
	public Map<String,List<CspTimeKeyDependInfo>> getKeyDependTraceByKeyName( String appName,String keyName) {
		final Map<String,List<CspTimeKeyDependInfo>> map = new HashMap<String, List<CspTimeKeyDependInfo>>();
		String sql = "select * from csp_time_key_depend_info where  key_name = ? and app_name=?";
		try {
			this.query(sql, new Object[] { keyName,appName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
					setCspTimeKeyDependInfoQuery(po, rs);

					List<CspTimeKeyDependInfo> list = map.get(po.getSourceKeyName());
					if(list == null){
						list = new ArrayList<CspTimeKeyDependInfo>();
						map.put(po.getSourceKeyName(), list);
					}

					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.info("getKeyDependTraceByKeyName",e);			
		}
		return map;
	}


	private void setCspTimeKeyDependInfoQuery(CspTimeKeyDependInfo po, ResultSet rs) throws SQLException {
		po.setId(rs.getLong("id"));
		po.setSourceAppName(rs.getString("source_app_name"));
		po.setSourceKeyName(rs.getString("source_key_name"));
		po.setAppName(rs.getString("app_name"));
		po.setKeyName(rs.getString("key_name"));
		po.setDependAppName(rs.getString("depend_app_name"));
		po.setDependKeyName(rs.getString("depend_key_name"));
		po.setDependStatus(rs.getString("depend_status"));
		po.setRate(rs.getFloat("rate"));
	}

	public void deleteCspTimeKeyDependInfo(String[] ids) throws Exception{
		if(ids.length == 0)
			return;
		StringBuilder sb = new StringBuilder("delete from csp_time_key_depend_info where id in (");
		for(String id : ids) {
			sb.append(id).append(",");
		}
		sb.deleteCharAt(sb.length() - 1).append(")");

		this.execute(sb.toString());
	} 

	public void deleteSourceKey(String sourceAppName, String sourceKeyName) throws Exception{
		StringBuilder sb = new StringBuilder("delete from csp_time_key_depend_info where source_app_name = ? and source_key_name = ?");
		this.execute(sb.toString(),new Object[]{
			sourceAppName, sourceKeyName
		});
	} 


	public void insertCspTimeKeyDependInfo(CspTimeKeyDependInfo info) throws Exception{
		List<CspTimeKeyDependInfo> list = new ArrayList<CspTimeKeyDependInfo>();
		list.add(info);
		insertCspTimeKeyDependInfo(list);
	} 

	public void insertCspTimeKeyDependInfo(List<CspTimeKeyDependInfo> insertList) throws Exception{
		if(insertList == null ||insertList.size() == 0)
			return;
		StringBuilder sb = new StringBuilder("insert into csp_time_key_depend_info(app_name,key_name,depend_app_name,depend_key_name,source_app_name,source_key_name,depend_status,rate) values(?,?,?,?,?,?,?,?)");
		List<Object[]> parmList = new ArrayList<Object[]>();
		for(CspTimeKeyDependInfo info : insertList) {
			parmList.add(new Object[]{info.getAppName(),info.getKeyName(),info.getDependAppName(),info.getDependKeyName(),info.getSourceAppName(),info.getSourceKeyName(),info.getDependStatus(),info.getRate()});
		}
		this.executeBatch(sb.toString(), parmList);
	}   

	//添加额外App信息
	public void insertCspTimeKeyExtraAppInfo(CspTimeExtraAppInfo appInfo) throws Exception{
		StringBuilder sb = new StringBuilder("insert into csp_time_extra_app_info(extra_app_name,extra_app_desc,extra_app_type) values(?,?,?)");
		this.execute(sb.toString(), new Object[]{appInfo.getExtraAppName(), appInfo.getExtraAppDesc(), appInfo.getExtraAppType()});
	}

	//添加额外Key信息
	public void insertCspTimeExtraAppKey(CspTimeExtraAppKey appInfo) throws Exception{
		StringBuilder sb = new StringBuilder("insert into csp_time_extra_app_key(extra_app_name, key_name) values(?,?)");
		this.execute(sb.toString(), new Object[]{appInfo.getExtraAppName(), appInfo.getKeyName()});
	}    

	//根据Appname获取单个的应用信息
	public CspTimeExtraAppInfo getCspTimeExtraAppInfo(String extra_app_name) throws Exception{
		String sql = "select * from csp_time_extra_app_info where extra_app_name = ?";
		final CspTimeExtraAppInfo po = new CspTimeExtraAppInfo();
		this.query(sql, new Object[] { extra_app_name}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				setCspTimeExtraAppInfoQuery(po, rs);
			}
		});
		return po;
	}   

	//获取所有的App列表
	public Map<String, CspTimeExtraAppInfo> getCspTimeExtraAppInfoMap() throws Exception{
		final Map<String, CspTimeExtraAppInfo> map = new HashMap<String, CspTimeExtraAppInfo>();
		String sql = "select * from csp_time_extra_app_info";
		this.query(sql, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeExtraAppInfo po = new CspTimeExtraAppInfo();
				setCspTimeExtraAppInfoQuery(po, rs);
				map.put(po.getExtraAppName(), po);
			}
		});
		return map;
	}     

	private void setCspTimeExtraAppInfoQuery(CspTimeExtraAppInfo po, ResultSet rs) throws SQLException {
		po.setExtraAppName(rs.getString("extra_app_name"));
		po.setExtraAppDesc(rs.getString("extra_app_desc"));
		po.setExtraAppType(rs.getString("extra_app_type"));
	}

	//获取某一个应用下面的key
	public Map<String, CspTimeExtraAppKey> getCspTimeExtraAppKeyMap(String extra_app_name) throws Exception{
		final Map<String, CspTimeExtraAppKey> map = new HashMap<String, CspTimeExtraAppKey>();
		String sql = "select * from csp_time_extra_app_key where extra_app_name = ?";
		this.query(sql, new Object[] { extra_app_name}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeExtraAppKey po = new CspTimeExtraAppKey();
				setCspTimeExtraAppKeyQuery(po, rs);
				map.put(po.getExtraAppName(), po);
			}
		});
		return map;
	}    

	//获取多个key的信息
	public Map<String, CspTimeExtraAppKey> getCspTimeExtraAppKeyMap(String extra_app_name, String[] key_nameArray) throws Exception{
		final Map<String, CspTimeExtraAppKey> map = new HashMap<String, CspTimeExtraAppKey>();
		StringBuilder sb = new StringBuilder("select * from csp_time_extra_app_key where extra_app_name = ? ");

		if(key_nameArray == null || key_nameArray.length == 0)
			return map;
		else if(key_nameArray.length == 1) {
			sb.append(" and key_name = ?");
		} else {
			sb.append(" and key_name in (");
			for(String keyname: key_nameArray) {
				sb.append(keyname).append(",");
			}
			sb.deleteCharAt(sb.length()).append(")");
		}

		Object[] params = new Object[key_nameArray.length + 1];
		params[0] = extra_app_name;
		int i = 1;
		for(String keyName: key_nameArray) {
			params[i++] = keyName;
		}

		this.query(sb.toString(), params, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeExtraAppKey po = new CspTimeExtraAppKey();
				setCspTimeExtraAppKeyQuery(po, rs);
				map.put(po.getExtraAppName(), po);
			}
		});
		return map;
	}    

	//获取所有的Key列表
	public List<CspTimeExtraAppKey> getCspTimeExtraAppKeyList() throws Exception{
		final List<CspTimeExtraAppKey> list = new ArrayList<CspTimeExtraAppKey>();
		String sql = "select * from csp_time_extra_app_key";
		this.query(sql, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspTimeExtraAppKey po = new CspTimeExtraAppKey();
				setCspTimeExtraAppKeyQuery(po, rs);
				list.add(po);
			}
		});
		return list;
	}  

	private void setCspTimeExtraAppKeyQuery(CspTimeExtraAppKey po, ResultSet rs) throws SQLException {
		po.setExtraAppName(rs.getString("extra_app_name"));
		po.setKeyName(rs.getString("key_name"));
	}  


	//  public Set<String> getAutoUrlAll() throws Exception {
	//    String sql = "select url from csp_key_depend_info where d_level = 'KEY'";
	//    final Set<String> set = new HashSet<String>();
	//    this.query(sql, new SqlCallBack() {
	//      @Override
	//      public void readerRows(ResultSet rs) throws Exception {
	//        set.add(rs.getString("url"));
	//      }
	//    });
	//    return set;
	//  }

	public void addHsfOpsToDb(HsfOpsPo po) {
		StringBuilder sb = new StringBuilder("insert csp_checkup_depend_opsip(ip,hostname,type,appname,markings) values(?,?,?,?,?)");
		try {
			this.execute(sb.toString(), new Object[]{po.getIp(), po.getHostname(), po.getType(), po.getAppname(), po.getMarkings()});
		} catch (SQLException e) {
			logger.error("",e);
		}
	}

	public void updateHsfOpsToDb(HsfOpsPo po) {
		StringBuilder sb = new StringBuilder("update csp_checkup_depend_opsip set ip = ?,hostname = ?,type = ?,appname = ?,markings = ? where ip = ? and appname = ?");
		try {
			this.execute(sb.toString(), new Object[]{po.getIp(), po.getHostname(), po.getType(), po.getAppname(), po.getMarkings(), po.getIp(), po.getAppname()});
		} catch (SQLException e) {
			logger.error("",e);
		}
	}  

	public List<HsfOpsPo> searchOpsHsfOpsPo(HsfOpsPo po) {
		final List<HsfOpsPo> list = new ArrayList<HsfOpsPo>();
		try {
			StringBuilder sb = new StringBuilder("select * from csp_checkup_depend_opsip where");
			Object[] params = null;
			if(po == null) {
				sb.append(" 1=?");
				params = new Object[]{1};        
			} else {
				sb.append(" ip = ? and appname = ?");
				params = new Object[]{po.getIp(), po.getAppname()};
			}

			this.query(sb.toString(), params, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					HsfOpsPo tmpPo = new HsfOpsPo();
					tmpPo.setAppname(rs.getString("appname"));
					tmpPo.setHostname(rs.getString("hostname"));
					tmpPo.setIp(rs.getString("ip"));
					tmpPo.setType(rs.getString("type"));
					tmpPo.setMarkings(rs.getString("markings"));
					list.add(tmpPo);
				}
			});
		} catch (Exception e) {
			logger.error("",e);
		}
		return list;
	}

	public void addNetstatInfo(CspCheckupDependNetstatInfo po) {
		StringBuilder sb = new StringBuilder("insert csp_checkup_depend_netstatinfo(appname,ip,port,dependapp,dependip,dependport,gmt_create) values(?,?,?,?,?,?,now())");
		try {
			this.execute(sb.toString(), new Object[]{po.getAppName(), po.getIp(), po.getPort(), po.getDependApp(),po.getDependIp(), po.getDependport()});
		} catch (SQLException e) {
			logger.error("",e);
		}
	}

	public List<CspCheckupDependNetstatInfo> searchNetstatInfo(CspCheckupDependNetstatInfo po) {
		final List<CspCheckupDependNetstatInfo> list = new ArrayList<CspCheckupDependNetstatInfo>();
		try {
			StringBuilder sb = new StringBuilder("select * from csp_checkup_depend_netstatinfo where appname=? and ip=?");
			Object[] params = new Object[]{po.getAppName() ,po.getIp()};

			this.query(sb.toString(), params, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspCheckupDependNetstatInfo tmpPo = new CspCheckupDependNetstatInfo();
					tmpPo.setAppName(rs.getString("appname"));
					tmpPo.setIp(rs.getString("ip"));
					tmpPo.setPort(rs.getInt("port"));
					tmpPo.setDependApp(rs.getString("dependapp"));
					tmpPo.setDependIp(rs.getString("dependip"));
					tmpPo.setDependport(rs.getInt("dependport"));
					tmpPo.setGmt_create(rs.getDate("gmt_create"));
					list.add(tmpPo);
				}
			});
		} catch (Exception e) {
			logger.error("",e);
		}
		return list;
	}  

	public void deleteNetstatInfo(CspCheckupDependNetstatInfo po) {
		StringBuilder sb = new StringBuilder("delete from csp_checkup_depend_netstatinfo where appname = ? and ip = ?");
		try {
			this.execute(sb.toString(), new Object[]{po.getAppName(), po.getIp()});
		} catch (SQLException e) {
			logger.error("",e);
		}
	}

	public List<CspTimeKeyDependInfo> getDistinctSourceUrlList(String keyName) {
		final List<CspTimeKeyDependInfo> list = new ArrayList<CspTimeKeyDependInfo>();
		String sql = "select distinct source_key_name,source_app_name from csp_time_key_depend_info where key_name=? or depend_key_name=?";
		try {
			this.query(sql, new Object[]{keyName,keyName}, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					CspTimeKeyDependInfo po = new CspTimeKeyDependInfo();
					po.setSourceAppName(rs.getString("source_app_name"));
					po.setSourceKeyName(rs.getString("source_key_name"));
					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("",e);
		}
		return list;
	}

	public Map<String, List<CspAppHotInterface>> getCspAppHotInterfaceByAppName(String[] appArray) {
		String sql = "select * from csp_app_hot_interface"; //FIXME 未来可能加入时间
		final Map<String, List<CspAppHotInterface>> map = new HashMap<String, List<CspAppHotInterface>>();
		SqlCallBack sqlCallBack = new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				CspAppHotInterface po = new CspAppHotInterface();
				fillCspAppHotInterface(rs, po);
				List<CspAppHotInterface> list = map.get(po.getAppName());
				if(list == null) {
					list = new ArrayList<CspAppHotInterface>();
					map.put(po.getAppName(), list);
				}
				list.add(po);
			}
		};
		try {
			if(appArray != null && appArray.length > 0) {
				sql += " where appname in (";
				for(String appName: appArray) {
					sql += "?,";
				}
				sql = sql.substring(0, sql.length() - 1);
				sql += ")";
				this.query(sql, appArray, sqlCallBack);
			} else {
				this.query(sql, sqlCallBack);
			}
		} catch (Exception e) {
			logger.error("", e);
		}
		return map;
	}

	public List<ReportInvokeDataPo> getTopNUrls(int n, final String appName, final Date date) {
		String sql = "select request_url, request_num, request_time from csp_app_request_url_summary where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and app_name = ? limit ?";
		final List<ReportInvokeDataPo> list = new ArrayList<ReportInvokeDataPo>();

		try {
			this.query(sql, new Object[]{ date, appName, n }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String rUrl = rs.getString("request_url");
					long rNum = rs.getLong("request_num");
					double rTime = rs.getDouble("request_time");

					ReportInvokeDataPo po = new ReportInvokeDataPo();
					po.setAppName(appName);
					po.setCollectDate(date);
					po.setCount(rNum);
					po.setTime(rTime);
					po.setResouceName(rUrl);
					po.setInvokeType("URL");

					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		return list;
	}

	public List<ReportInvokeDataPo> getTopNOutInterface(int n, final String appName, final Date date) {
		String sql = "select key_name, sum(call_num) as call_num, sum(call_num*use_time) as call_time  from csp_hsf_consumer_app_detail where DATE_FORMAT(collect_date,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and customer_app = ? group by key_name order by call_num desc limit ?";
		final List<ReportInvokeDataPo> list = new ArrayList<ReportInvokeDataPo>();

		try {
			this.query(sql, new Object[]{ date, appName, n }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					DecimalFormat df = new DecimalFormat("0.00");
					String oInterface = rs.getString("key_name");
					long rNum = rs.getLong("call_num");
					double callTime = rs.getDouble("call_time");
					double aveTime = Double.parseDouble(df.format(callTime / rNum));

					ReportInvokeDataPo po = new ReportInvokeDataPo();
					po.setAppName(appName);
					po.setCollectDate(date);
					po.setCount(rNum);
					po.setTime(aveTime);
					po.setResouceName(oInterface);
					po.setInvokeType("HSF_OUT");

					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		return list;
	}

	public List<ReportInvokeDataPo> getTopNInInterface(int n, final String appName, final Date date) {
		String sql = "select key_name, sum(call_num) as call_num, sum(call_num*use_time) as call_time from csp_hsf_provider_app_detail where DATE_FORMAT(collect_date,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\") " +
				" and provider_app = ? group by key_name order by call_num desc limit ?";
		final List<ReportInvokeDataPo> list = new ArrayList<ReportInvokeDataPo>();

		try {
			this.query(sql, new Object[]{ date, appName, n }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					DecimalFormat df = new DecimalFormat("0.00");
					String iInterface = rs.getString("key_name");
					Long rNum = rs.getLong("call_num");
					double callTime = rs.getDouble("call_time");
					double aveTime = Double.parseDouble(df.format(callTime / rNum));

					ReportInvokeDataPo po = new ReportInvokeDataPo();
					po.setAppName(appName);
					po.setCollectDate(date);
					po.setCount(rNum);
					po.setTime(aveTime);
					po.setResouceName(iInterface);
					po.setInvokeType("HSF_IN");

					list.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		return list;
	}

	public int getLatestUvByDomain(String domain) {
		String sql = "select uv  from csp_domian_url_uv  where url = ? and url_type='domain' order by collect_time desc limit 1";
		final List<Integer> list = new ArrayList<Integer>();

		try {
			this.query(sql, new Object[]{ domain }, new SqlCallBack() {

				@Override
				public void readerRows(ResultSet rs) throws Exception {
					Integer uv = rs.getInt("uv");
					list.add(uv);
				}
			});
		} catch (Exception e) {
			logger.error("", e);
		}

		return list.size() == 0 ? 0 : list.get(0);
	}

	private void fillCspAppHotInterface(ResultSet rs, CspAppHotInterface po) throws SQLException {
		po.setAppName(rs.getString("appname"));
		po.setAppType(rs.getString("apptype"));
		po.setCallNum(rs.getLong("call_num"));
		po.setCollect_time(rs.getDate("collect_time"));
		po.setKeyName(rs.getString("keyname"));
	}
	
	public void addReleaseInfoToDb(ReleaseInfo info) throws SQLException {
		String sql = "insert into releaseinfo(planId,appName,planTime,pubType,planKind,pubLevel,releaseId,callSystem,creator,sign,notifyType,finishTime,result) value (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		this.execute(sql, new Object[] {info.getPlanId(),info.getAppName(),info.getPlanTime(),info.getPubType(), info.getPlanKind(),info.getPubLevel(),info.getReleaseId(),info.getCallSystem(),info.getCreator(),info.getSign(),info.getNotifyType(),info.getFinishTime(),info.getResult()});
	}
	
	public List<ReleaseInfo> getReleaseInfoList(String appName, String dateBegin, String endDate) throws Exception {
		final List<ReleaseInfo> list = new ArrayList<ReleaseInfo>();
		String sql = "select * from releaseinfo where appName = ? and finishTime >= ? and finishTime <= ? order by finishTime desc";
		this.query(sql, new Object[] {appName,dateBegin,endDate}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				list.add(fillReleaseInfo(rs));
			}
		});
		return list;
	}
	
	private ReleaseInfo fillReleaseInfo(ResultSet rs) throws SQLException {
		ReleaseInfo po = new ReleaseInfo();
		po.setAppName(rs.getString("appName"));
		po.setCallSystem(rs.getString("callSystem"));
		po.setCreator(rs.getString("creator"));
		po.setFinishTime(rs.getString("finishTime"));
		po.setNotifyType(rs.getString("notifyType"));
		po.setPlanId(rs.getString("planId"));
		po.setPlanKind(rs.getString("planKind"));
		po.setPlanTime(rs.getString("planTime"));
		po.setPubLevel(rs.getString("pubLevel"));
		po.setPubType(rs.getString("pubType"));
		po.setReleaseId(rs.getString("releaseId"));
		po.setResult(rs.getString("result"));
		po.setSign(rs.getString("sign"));
		return po;
	}
	
	public Map<String, List<ReleaseInfo>> getReleaseInfoMap(final Set<String> appSet, String dateBegin, String endDate) throws Exception {
		final Map<String, List<ReleaseInfo>> map = new HashMap<String, List<ReleaseInfo>>();
		String sql = "select * from releaseinfo where finishTime >= ? and finishTime <= ? order by finishTime desc";
		this.query(sql, new Object[] {dateBegin,endDate}, new SqlCallBack() {
			@Override
			public void readerRows(ResultSet rs) throws Exception {
				String appName = rs.getString("appName");
				if(!appSet.contains(appName)) {
					return;
				}
				ReleaseInfo po = fillReleaseInfo(rs);
				List<ReleaseInfo> list = map.get(appName);
				if(list == null) {
					list = new ArrayList<ReleaseInfo>();
					map.put(appName, list);
				}
				list.add(po);
			}
		});
		return map;
	}
	//CspCheckupDependNetstatInfo
	public static void main(String[] args) {
	}
}
