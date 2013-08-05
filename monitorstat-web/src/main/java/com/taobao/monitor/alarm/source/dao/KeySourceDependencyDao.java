package com.taobao.monitor.alarm.source.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.alarm.source.po.KeySourcePo;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.KeyPo;
import com.taobao.util.CollectionUtil;

public class KeySourceDependencyDao extends MysqlRouteBase {
	private static final Logger logger = Logger
			.getLogger(KeySourceDependencyDao.class);
	
	private static final int PAGE_SIZE = 40;
	/**
	 * 添加报警指标的依赖关系
	 * 
	 * @param po
	 * @return
	 */
	public boolean addKeySourcePo(KeySourcePo po) {
		try {
			String sql = "insert into ms_monitor_key_source (key_id, key_name,app_id,source_app_id,source_app_name,source_group_name) values(?,?,?,?,?,?)";
			this.execute(
					sql,
					new Object[] { po.getKeyId(), po.getKeyName(),
							po.getAppId(), po.getSourceAppId(),
							po.getSourceAppName(), po.getSourceGroupName() });
		} catch (Exception e) {
			logger.error("addKeySourcePo exception,po=" + po, e);
			return false;
		}

		return true;
	}
	
	/**
	 * 添加报警指标的依赖关系
	 * 确保是安全插入记录
	 * @param po
	 * @return
	 */
	public boolean safeAddKeySourcePo(KeySourcePo po) {
		try {
			List<KeySourcePo> listKeyPo = queryKeySource( Integer.valueOf(po.getKeyId()), po.getAppId() );
			if(CollectionUtil.isNotEmpty(listKeyPo)){
				return updateKeySourcePo(po);
			} else {
				return addKeySourcePo(po);
			}
		} catch (Exception e) {
			logger.error("safeAddKeySourcePo exception,po=" + po, e);
			return false;
		}
	}
	
	/**
	 * 更新报警指标的依赖关系
	 * 
	 * @param po
	 * @return boolean
	 */
	public boolean updateKeySourcePo(KeySourcePo po) {
		try {
			String sql = "update ms_monitor_key_source set app_id=?, source_app_id=?, source_app_name=?, source_group_name=? where id=?";
			this.execute(
					sql,
					new Object[] { po.getAppId(), po.getSourceAppId(),
							po.getSourceAppName(), po.getSourceGroupName(),
							po.getId() });
		} catch (Exception e) {
			logger.error("updateKeySourcePo exception,po=" + po, e);
			return false;
		}

		return true;
	}
	
	/**
	 * 判断是否存在关联
	 * @param keyId
	 * @param sourceAppId
	 * @return boolean
	 */
	private List<KeySourcePo> queryKeySource(int keyId, int sourceAppId){
		String sql = "select * from ms_monitor_key_source where key_id=? and app_id=?";
		final List<KeySourcePo> list = new ArrayList<KeySourcePo>();
		try {
			this.query(sql, new Object[]{keyId, sourceAppId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					KeySourcePo po = getKeySourcePoFromResultSet(rs);
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("findAllKeySourcePosByAppId exception,keyId=" + keyId + ",sourceAppId=" + sourceAppId, e);
			return null;
		}
		return list.size()==0?null:list;
	}
	
	/**
	 * 选择keyId的列表
	 * @param keyId
	 * @param sourceAppId
	 * @return boolean
	 */
	public List<KeySourcePo> queryKeySourceByKeyId(int keyId){
		String sql = "select * from ms_monitor_key_source where key_id=?";
		final List<KeySourcePo> list = new ArrayList<KeySourcePo>();
		try {
			this.query(sql, new Object[]{keyId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					KeySourcePo po = getKeySourcePoFromResultSet(rs);
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("findAllKeySourcePosByAppId exception,keyId=" + keyId, e);
			return null;
		}
		return list.size()==0?null:list;
	}
	
	/**
	 * * 删除报警指标的依赖关系
	 * 
	 * @param appId
	 * @param keyId
	 * @param hostId
	 */
	public boolean deleteKeySourcePo(int keyId, int sourceAppId) {
		String sql = "delete from ms_monitor_key_source where key_id = ? and source_app_id=?";
		try {
			this.execute(sql, new Object[] { keyId, sourceAppId });
		} catch (SQLException e) {
			logger.error("deleteKeySourcePo exception,keyId=" + keyId + ",sourceAppId=" + sourceAppId, e);
			return false;
		}

		return true;
	}

	public List<KeySourcePo> findAllKeySourcePosByAppId(int appId){
		String sql = "select * from ms_monitor_key_source where app_id=?";
		final List<KeySourcePo> list = new ArrayList<KeySourcePo>();
		try {
			this.query(sql, new Object[]{appId}, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					KeySourcePo po = getKeySourcePoFromResultSet(rs);
					list.add(po);
				}});
		} catch (Exception e) {
			logger.error("findAllKeySourcePosByAppId exception,appId=" + appId, e);
			return null;
		}
		return list.size()==0?null:list;
	}
	
	/**
	 * 提取所有应用的key总数用于分页显示
	 * @param appId
	 * @return List<KeyPo>
	 */
	public Integer countAppKeyByAppId(int appId) {
		
		String sql = "select count(k.key_id) as all_key_count from ms_monitor_app_key ak ,ms_monitor_key k where ak.app_id=? and ak.key_id=k.key_id";
		final List<Integer> list = new ArrayList<Integer>();
		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					list.add( rs.getInt("all_key_count") );
				}
			});
		} catch (Exception e) {
			logger.error("findAllKeySourcePosByAppId exception,appId=" + appId, e);
		}
		return (list.size()==0 ? 0 :list.get(0));

	}
	
	/**
	 * 分页显示取得所有应用的key
	 * @param appId
	 * @param pagenum
	 * @param pageSize
	 * @return List<KeyPo>
	 */
	public List<KeyPo> queryAppKeyByAppIdInPage(int appId, int pageNum, int pageSize) {
		
		String sql = "select k.* from ms_monitor_app_key ak ,ms_monitor_key k where ak.app_id=? and ak.key_id=k.key_id limit "+(pageSize*(pageNum-1)+1)+","+pageSize;
		final List<KeyPo> poList = new ArrayList<KeyPo>();

		try {
			this.query(sql, new Object[] { appId }, new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {

					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					poList.add(po);
				}
			});
		} catch (Exception e) {
			logger.error("findAllKeySourcePosByAppId exception,appId=" + appId + ",pageNum=" + pageNum + ",pageSize=" + pageSize, e);
		}
		return poList;

	}

	/**
	 * 根据 key名称查询/模糊查询出所有相关的 key 信息总数用于分页显示
	 * @param appId
	 * @param name
	 * @param pageNum
	 * @return
	 */
	public Integer countAppKeyLikeName(String keyname, Integer appId) {

		String sql = "select count(k.key_id) as all_key_count from ms_monitor_key k,MS_MONITOR_APP_KEY ak where ak.key_id=k.key_id and ak.app_id=? ";
		List<Object> params = new ArrayList<Object>();
		if (keyname != null && !"".equals(keyname)) {
			sql += "  and k.key_value like '%" + keyname + "%'";
		}
		params.add(appId);
		final List<Integer> list = new ArrayList<Integer>();

		try {
			this.query(sql, params.toArray(), new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					list.add( rs.getInt("all_key_count") );
				}
			});
		} catch (Exception e) {
			logger.error("countAppKeyLikeName exception,keyname=" + keyname + ",appId=" + appId, e);
		}
		return (list.size()==0 ? 0 :list.get(0));
	}
	
	/**
	 * 根据 key 名称 分页查询模糊查询出所有相关的 key 信息，并返回列表
	 * @param keyname
	 * @param pageNum
	 * @param pageSize
	 * @param name
	 * @return
	 */
	public List<KeyPo> queryAppKeyLikeNameInPage(String keyname, Integer appId, int pageNum, int pageSize) {

		String sql = "select k.* from ms_monitor_key k,MS_MONITOR_APP_KEY ak where ak.key_id=k.key_id and ak.app_id=? ";
		List<Object> params = new ArrayList<Object>();
		if (keyname != null && !"".equals(keyname)) {
			sql += "  and k.key_value like '%" + keyname + "%'";
		}
		sql += " limit "+(pageSize*(pageNum-1)+1)+","+pageSize;
		params.add(appId);
		final List<KeyPo> keyList = new ArrayList<KeyPo>();

		try {
			this.query(sql, params.toArray(), new SqlCallBack() {
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					KeyPo po = new KeyPo();
					po.setKeyId(rs.getInt("key_id"));
					po.setKeyName(rs.getString("key_value"));
					po.setDefaultconfig(rs.getString("defaultconfig"));
					po.setFeature(rs.getString("feature"));
					po.setAliasName(rs.getString("alias_name"));
					po.setKeyType(rs.getString("key_type"));
					po.setFeature(rs.getString("feature"));
					keyList.add(po);

				}
			});
		} catch (Exception e) {
			logger.error("queryAppKeyLikeNameInPage exception,keyname=" + keyname + ",appId=" + appId + ",pageNum=" + pageNum + ",pageSize=" + pageSize, e);
		}
		return keyList;
	}
	
	private KeySourcePo getKeySourcePoFromResultSet(ResultSet rs) throws SQLException{
		KeySourcePo po = new KeySourcePo();
		po.setId(rs.getInt("id"));
		po.setAppId(rs.getInt("app_id"));
		po.setKeyId(rs.getString("key_id"));
		po.setKeyName(rs.getString("key_name"));
		po.setSourceAppId(rs.getInt("source_app_id"));
		po.setSourceAppName(rs.getString("source_app_name"));
		po.setSourceGroupName(rs.getString("source_group_name"));
		return po;
	}
}
