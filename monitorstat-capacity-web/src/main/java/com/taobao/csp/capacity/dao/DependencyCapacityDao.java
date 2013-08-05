package com.taobao.csp.capacity.dao;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.capacity.po.DepCapacityPo;
import com.taobao.csp.capacity.util.DependencyCapacityUtil;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;

/***
 * the last day of 2011
 * @author youji.zj
 *
 */
public class DependencyCapacityDao extends MysqlRouteBase {
	
	private static final Logger logger =  Logger.getLogger(DependencyCapacityDao.class);
	
	public DependencyCapacityDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main_Capacity"));
	}
	
	/***
	 * insert a new dependency capacity record
	 * @param po
	 */
	public void addCapacityDependency(DepCapacityPo po) {
		String sql = "insert into csp_capacity_depend(consumer_name, provider_name, provider_group, rush_time_qps, room_feature, collect_time) VALUES(?,?,?,?,?,?);";
		try {
			this.execute(sql, new Object[]{ po.getConsumerApp(), po.getProviderApp(), po.getProviderGroup(), po.getDepQps(), po.getRoomQps(), po.getCollectTime() });
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	/***
	 * delete dependency capacity record by date
	 * @param date
	 */
	public void deleteCapacityDependencyByDate(Date date){
		String sql = "delete from csp_capacity_depend where DATE_FORMAT(collect_time,\"%Y-%m-%d\") = DATE_FORMAT(?,\"%Y-%m-%d\")";
		try {
			this.execute(sql, new Object[] { date });
		} catch (Exception e) {
			logger.error("", e);
		}
	}
	
	/***
	 * find all dependency capacity record by app name and date
	 * records include the type 'All' and group types.
	 * @param providerApp
	 * @param date
	 * @return
	 */
	public List<DepCapacityPo> findCapacityDependencyByProvider(String providerApp, Date date) {
		String sql = "select * from csp_capacity_depend where provider_name = ? and DATE_FORMAT(collect_time,\"%Y-%m-%d\")=DATE_FORMAT(?,\"%Y-%m-%d\")";
		
		final List<DepCapacityPo> list = new ArrayList<DepCapacityPo>();
		try {
			this.query(sql, new Object[]{ providerApp, date }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					DepCapacityPo depCapacityPo = new DepCapacityPo();
					depCapacityPo.setConsumerApp(rs.getString("consumer_name"));
					depCapacityPo.setProviderApp(rs.getString("provider_name"));
					depCapacityPo.setProviderGroup(rs.getString("provider_group"));
					depCapacityPo.setDepQps(rs.getDouble("rush_time_qps"));
					String roomFeature = rs.getString("room_feature");
					depCapacityPo.setCollectTime(rs.getDate("collect_time"));
					depCapacityPo.setRoomQps(roomFeature);
					depCapacityPo.setRoomQpsMap(DependencyCapacityUtil.generateRoomQps(roomFeature));
					
					list.add(depCapacityPo);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return list;
	}
	
	/***
	 * find dependency capacity record by app name and date
	 * only type 'All'
	 * @param providerApp
	 * @param date
	 * @return
	 */
	public List<DepCapacityPo> findAllDependencyByProvider(String providerApp, Date date) {
		String sql = "select * from csp_capacity_depend where provider_name = ? and DATE_FORMAT(collect_time,\"%Y-%m-%d\")=DATE_FORMAT(?,\"%Y-%m-%d\") and provider_group='All'";
		
		final List<DepCapacityPo> list = new ArrayList<DepCapacityPo>();
		try {
			this.query(sql, new Object[]{ providerApp, date }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					DepCapacityPo depCapacityPo = new DepCapacityPo();
					depCapacityPo.setConsumerApp(rs.getString("consumer_name"));
					depCapacityPo.setProviderApp(rs.getString("provider_name"));
					depCapacityPo.setProviderGroup(rs.getString("provider_group"));
					depCapacityPo.setDepQps(rs.getDouble("rush_time_qps"));
					String roomFeature = rs.getString("room_feature");
					depCapacityPo.setCollectTime(rs.getDate("collect_time"));
					depCapacityPo.setRoomQps(roomFeature);
					depCapacityPo.setRoomQpsMap(DependencyCapacityUtil.generateRoomQps(roomFeature));
					
					list.add(depCapacityPo);
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		return sort(list);
	}
	
	/***
	 * find group dependency capacity record by app name and date
	 * @param providerApp
	 * @param date
	 * @return
	 */
	public Map<String, List<DepCapacityPo>>  findGroupDependencyByProvider(String providerApp, Date date) {
		String sql = "select * from csp_capacity_depend where provider_name = ? and DATE_FORMAT(collect_time,\"%Y-%m-%d\")=DATE_FORMAT(?,\"%Y-%m-%d\") and provider_group!='All'";
		
		final Map<String, List<DepCapacityPo>> groupDep = new HashMap<String, List<DepCapacityPo>>();
		final List<DepCapacityPo> list = new ArrayList<DepCapacityPo>();
		try {
			this.query(sql, new Object[]{ providerApp, date }, new SqlCallBack(){
				@Override
				public void readerRows(ResultSet rs) throws Exception {
					String providerGroup = rs.getString("provider_group");
					DepCapacityPo depCapacityPo = new DepCapacityPo();
					depCapacityPo.setConsumerApp(rs.getString("consumer_name"));
					depCapacityPo.setProviderApp(rs.getString("provider_name"));
					depCapacityPo.setProviderGroup(providerGroup);
					depCapacityPo.setDepQps(rs.getDouble("rush_time_qps"));
					String roomFeature = rs.getString("room_feature");
					depCapacityPo.setCollectTime(rs.getDate("collect_time"));
					depCapacityPo.setRoomQps(roomFeature);
					depCapacityPo.setRoomQpsMap(DependencyCapacityUtil.generateRoomQps(roomFeature));
					
					if (!groupDep.keySet().contains(providerGroup)) {
						List<DepCapacityPo> list = new ArrayList<DepCapacityPo>();
						list.add(depCapacityPo);
						groupDep.put(providerGroup, list);
					} else {
						groupDep.get(providerGroup).add(depCapacityPo);
					}
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}
		for (List<DepCapacityPo> value : groupDep.values()) {
			sort(value);
		}
		return groupDep;
	}
	
	private  List<DepCapacityPo> sort(List<DepCapacityPo> list) {
		DepCapacityPo tmp;
		for (int i = 0; i < list.size() - 1; i++)
			for (int j = i + 1; j < list.size(); j++) {
				if (list.get(i).getDepQps() < list.get(j).getDepQps()) {
					tmp = list.get(i);
					list.set(i, list.get(j));
					list.set(j, tmp);
				}
			}
		return list;
	}

}
