package com.taobao.csp.depend.dao;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.taobao.csp.depend.po.report.TairException;
import com.taobao.csp.depend.po.tair.ActionSummaryPo;
import com.taobao.csp.depend.po.tair.CExceptionUnit;
import com.taobao.csp.depend.po.tair.TairConsumeDetailPo;
import com.taobao.csp.depend.po.tair.TairConsumeMachine;
import com.taobao.csp.depend.po.tair.TairConsumeSummaryPo;
import com.taobao.csp.depend.util.ConstantParameters;
import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;


/**
 * 用于访问Center应用调用其他应用的HSF信息。
 * @author zhongting
 *
 */
public class CspAppTairConsumeDao  extends MysqlRouteBase{
	
	private static final Logger logger =  Logger.getLogger(CspAppTairConsumeDao.class);
	
	public CspAppTairConsumeDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
//	
//	public List<TairConsumeSummaryPo> findConsumeTairSummary(String consumeAppName, String collectDate) {
//		String sql = "select * from csp_app_consume_tair_summary where app_name=? and collect_time=? and app_group_name = 'all'";
//		final List<TairConsumeSummaryPo> poList = new ArrayList<TairConsumeSummaryPo>();
//		try {
//			this.query(sql, new Object[]{consumeAppName,collectDate}, new SqlCallBack(){
//				public void readerRows(ResultSet rs) throws Exception {
//					TairConsumeSummaryPo po = new TairConsumeSummaryPo();
//					po.setApp_name(rs.getString("app_name"));
//					po.setApp_group_name(rs.getString("app_group_name"));
//					po.setTair_group_name(rs.getString("tair_group_name"));
//					po.setNamespace(rs.getString("namespce"));
//					po.setRush_time_qps(rs.getFloat("rush_time_qps"));
//					po.setRush_time_rt(rs.getFloat("rush_time_rt"));
//					po.setInvoking_all_num(rs.getLong("invoking_all_num"));
//					po.setRoom_feature(rs.getString("room_feature"));
//					po.setCollect_time(new Date(rs.getTimestamp("collect_time").getTime()));
//					if(!poList.contains(po)){
//						poList.add(po);
//					}
//				}});
//		} catch (Exception e) {
//			logger.error("", e);
//		}		
//		return poList;
//	}
	
	public Map<String, TairConsumeSummaryPo> findConsumeTairSummary(String consumeAppName, String collectDate) {
		String sql = "select sum(invoking_all_num) as callNum,tair_group_name from csp_app_consume_tair_summary where app_name=? and collect_time=? and app_group_name = 'all' group by tair_group_name";
		final Map<String, TairConsumeSummaryPo> poMap = new HashMap<String, TairConsumeSummaryPo>();
		try {
			this.query(sql, new Object[]{consumeAppName,collectDate}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					TairConsumeSummaryPo po = new TairConsumeSummaryPo();
					//po.setApp_name(rs.getString("app_name"));
					//po.setApp_group_name(rs.getString("app_group_name"));
					po.setTair_group_name(rs.getString("tair_group_name"));
					//po.setNamespace(rs.getString("namespace"));
					//po.setRush_time_qps(rs.getFloat("rush_time_qps"));
					//po.setRush_time_rt(rs.getFloat("rush_time_rt"));
					po.setInvoking_all_num(rs.getLong("callNum"));
					//po.setRoom_feature(rs.getString("room_feature"));
					//po.setCollect_time(new Date(rs.getTimestamp("collect_time").getTime()));
					
					String tairGroupName = rs.getString("tair_group_name");
					if(tairGroupName == null || "".equals(tairGroupName)) {
						tairGroupName = ConstantParameters.EMPTY_TAIR_GROUPNAME;
					}
					poMap.put(tairGroupName, po);
					
				}});
		} catch (Exception e) {
			logger.error("", e);
		}		
		return poMap;
	}	
	
	/**
	 * 统计action的调用量,action的总数感觉也应该在csp_app_consume_tair_summary中做记录
	 * @return
	 */
	public List<ActionSummaryPo> getActionList(String consumeAppName, String collectDate){
		final List<ActionSummaryPo> poList = new ArrayList<ActionSummaryPo>();
		String sql = "select action_type,SUM(invoking_num) as callallnum from csp_app_consume_tair_detail " +
				"where app_name=? and collect_time=? GROUP BY action_type";
		try {
			this.query(sql, new Object[]{consumeAppName,collectDate}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					ActionSummaryPo po = new ActionSummaryPo();
					po.setActionName(rs.getString("action_type"));
					po.setCallAllNum(rs.getLong("callallnum"));
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
	 * 统计Detail中调用排名前10的数据
	 * @param consumeAppName
	 * @param collectDate
	 * @return
	 */
	public List<TairConsumeDetailPo> findConsumeTairDetailList(String consumeAppName, String collectDate) {
		String sql = "select app_name,tair_group_name,namespace,action_type,app_site_name,invoking_num as callNum,tair_version,collect_time " +
				"from csp_app_consume_tair_detail where app_name= ? and collect_time=? ";
		List<TairConsumeDetailPo> poList = null;
		final Map<String, TairConsumeDetailPo> map = new HashMap<String, TairConsumeDetailPo>();
		try {
			this.query(sql, new Object[]{consumeAppName,collectDate}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					
					if(rs.getString("action_type") == null) {
						return;
					}
					
					String name = rs.getString("action_type").trim().toLowerCase(); 
					//remove exception , hit , length , and space case
					if(name.indexOf("/") >= 0 || name.indexOf("exception") >= 0 || name.indexOf(" ") >= 0) {
						return;
					}	
					
					String tair_group_name = rs.getString("tair_group_name");
					if(tair_group_name == null) {
						tair_group_name = "";
					}
					
					String key = null;
					try {
						StringBuilder sb = new StringBuilder();					
						sb.append(tair_group_name).append("-").append(rs.getString("namespace")).append("-")
						.append(rs.getString("action_type")).append("-").append(rs.getString("app_site_name"));
						key = sb.toString();						
					} catch(Exception e) {
						logger.error("拼接key异常，过滤此记录", e);
						key = new Date().toString();
					}
					
					//通过map实现GROUP BY tair_group_name,namespace,action_type,app_site_name的功能
					if(map.containsKey(key)) {
						TairConsumeDetailPo po = map.get(key);
						po.setCallNum(po.getCallNum() + rs.getLong("callNum"));
					} else {
						TairConsumeDetailPo po = new TairConsumeDetailPo();
						po.setApp_name(rs.getString("app_name"));
						po.setTair_group_name(rs.getString("tair_group_name"));
						po.setNamespace(rs.getString("namespace"));
						po.setAction_type(rs.getString("action_type"));
						po.setApp_site_name(rs.getString("app_site_name"));
						po.setCallNum(rs.getLong("callNum"));
						po.setTair_version(rs.getString("tair_version"));
						po.setCollect_time(new Date(rs.getTimestamp("collect_time").getTime()));
						map.put(key, po);
					}
				}});
		} catch (Exception e) {
			logger.error("findConsumeTairDetailList异常", e);
		}		
		
		if(map.size() > 0) {
			poList = new ArrayList<TairConsumeDetailPo>(map.values());
		} else {
			poList = new ArrayList<TairConsumeDetailPo>();
		}
		return poList;
	}	
	
	/**
	 * 查询调用tair的机器的分布
	 * @param opsName
	 * @param selectDate
	 * @param tairgroupname
	 * @param namespace
	 * @return
	 */
	public List<TairConsumeMachine> findMachineByGroupAndNameSpace(String opsName,String selectDate,String tairgroupname,String namespace,String actionType) {
		
		
		StringBuilder sb = new StringBuilder("select action_type,SUM(invoking_num) as callNum,SUM(invoking_time)as callTime,app_host_ip, app_site_name from csp_app_consume_tair_detail ");
		sb.append("where app_name=? and collect_time=? and tair_group_name=? and namespace =? ");
		sb.append(" and action_type in (?,?,?) ");	//改用in，提高查询效率
		sb.append(" group by app_host_ip,action_type");
		
		String strHit = actionType + "/" + ConstantParameters.STRING_TYPE_HIT;
		String strLength = actionType + "/" + ConstantParameters.STRING_TYPE_LEN;
		
		Object[] params = new Object[]{opsName,selectDate,tairgroupname,namespace, actionType, strHit, strLength};
		String sql = sb.toString();
		sb = null;
		
		final List<TairConsumeMachine> poList = new ArrayList<TairConsumeMachine>();
		try {
			this.query(sql, params, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					TairConsumeMachine po = new TairConsumeMachine();
					po.setActionType(rs.getString("action_type"));	//包括length 和 hit等
					po.setCallnum(rs.getLong("callNum"));
					po.setCalltime(rs.getLong("callTime"));
					po.setConsumeMachineIp(rs.getString("app_host_ip"));
					po.setConsumeMachineCm(rs.getString("app_site_name"));
					if(!poList.contains(po)){
						poList.add(po);
					}
				}});
		} catch (Exception e) {
			logger.error("findMachineByGroupAndNameSpace查询异常：", e);
		}		
		return poList;
	}	
	
	/**
	 * 根据tairgroupname查询详细
	 * @param consumeAppName
	 * @param collectDate
	 * @param tairgroupname
	 * @return
	 */
	public List<TairConsumeDetailPo> findConsumeTairDetailList(String consumeAppName, String collectDate, String tairgroupname) {
		String sql = "select namespace,action_type,app_site_name,SUM(invoking_num) as callNum,SUM(invoking_time) as callTime " +
				"from csp_app_consume_tair_detail where app_name= ? and collect_time=? and tair_group_name = ? " +
				"GROUP BY action_type,app_site_name,namespace";
		final List<TairConsumeDetailPo> poList = new ArrayList<TairConsumeDetailPo>();
		try {
			this.query(sql, new Object[]{consumeAppName,collectDate,tairgroupname}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					TairConsumeDetailPo po = new TairConsumeDetailPo();
					po.setNamespace(rs.getString("namespace"));
					po.setAction_type(rs.getString("action_type"));
					po.setApp_site_name(rs.getString("app_site_name"));
					po.setCallNum(rs.getLong("callNum"));
					po.setInvoking_time(rs.getLong("callTime"));
					if(!poList.contains(po)){
						poList.add(po);
					}
				}});
		} catch (Exception e) {
			logger.error("", e);
		}		
		return poList;
	}	
	
	public Map<String,CExceptionUnit> findExceptionDetail(String consumeAppName, String collectDate) {
		String sql = "select SUM(invoking_time) as total,action_type,app_site_name from csp_app_consume_tair_detail where app_name=? and collect_time=? and action_type like '%exception%'" +
				" group by action_type, app_site_name";
		final Map<String,CExceptionUnit> poMap = new HashMap<String,CExceptionUnit>();
		
		try {
			this.query(sql, new Object[]{consumeAppName,collectDate}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					
					if(!poMap.containsKey(rs.getString("action_type"))) {
						poMap.put(rs.getString("action_type"), new CExceptionUnit(rs.getString("action_type")));
					}
					
					CExceptionUnit po = poMap.get(rs.getString("action_type"));
					po.machineData.put(rs.getString("app_site_name"), rs.getLong("total"));
				}});
		} catch (Exception e) {
			logger.error("", e);
		}		
		
		return poMap;
	}
	
	/**
	 * 根据时间获取各个Group的内容统计
	 * @param collectDate
	 * @return
	 */
	public Map<String, TairConsumeSummaryPo> getTairListByDate(String collectDate) {
 		String sql = "select * from csp_tair_provide_app_summary where collect_time=?";
		final Map<String, TairConsumeSummaryPo> poMap = new HashMap<String, TairConsumeSummaryPo>();
		final Map<String, Long> numMap = new HashMap<String, Long>();
		
		try {
			this.query(sql, new Object[]{collectDate}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					
					String tairGroupName = rs.getString("tair_group_name");
					if(tairGroupName == null || "".equals(tairGroupName)) {
						tairGroupName = ConstantParameters.EMPTY_TAIR_GROUPNAME;
					}
					
					if(!poMap.containsKey(tairGroupName)) {
						TairConsumeSummaryPo po = new TairConsumeSummaryPo();
						po.setApp_name(rs.getString("app_name"));
						//po.setApp_group_name(rs.getString("app_group_name"));
						po.setTair_group_name(tairGroupName);
						po.setNamespace(rs.getString("namespace"));
						po.setRush_time_qps(rs.getFloat("rush_time_qps"));
						po.setRush_time_rt(rs.getFloat("rush_time_rt"));
						po.setInvoking_all_num(rs.getLong("invoking_all_num"));
						po.setRoom_feature(rs.getString("room_feature"));
						po.setCollect_time(new Date(rs.getTimestamp("collect_time").getTime()));
						poMap.put(tairGroupName, po);
						numMap.put(tairGroupName, 1l);
					} else {
						TairConsumeSummaryPo po = poMap.get(tairGroupName);
						po.setInvoking_all_num(po.getInvoking_all_num() + rs.getLong("invoking_all_num"));
						po.setRush_time_qps(po.getRush_time_qps() + rs.getFloat("rush_time_qps"));
						po.setRush_time_rt(po.getRush_time_rt() + rs.getFloat("rush_time_rt"));
						numMap.put(tairGroupName, numMap.get(tairGroupName) + 1);
					}
				}});
		} catch (Exception e) {
			logger.error("getTairListByDate：", e);
		}		
		
		//计算平均值
		for(String groupName: poMap.keySet()) {
			TairConsumeSummaryPo po = poMap.get(groupName);
			//po.setInvoking_all_num(po.getInvoking_all_num() / numMap.get(groupName));
			po.setRush_time_qps(po.getRush_time_qps() / numMap.get(groupName));
			po.setRush_time_rt(po.getRush_time_rt() / numMap.get(groupName));
		}
		
		return poMap;
	}
	
	
	/**
	 * 统计Tair的信息
	 * @param collectDate
	 * @return
	 */
	public Map<String, TairException> getTairExceptionListByDate(String collectDate,final Map<String, TairException> poMap,final boolean isCur) {
 		String sql = "select tair_group_name, action_type, invoking_time as callNum from csp_tair_provider_app_detail where collect_time=?";
		
 		try {
			this.query(sql, new Object[]{collectDate}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					
					String action_type = rs.getString("action_type");
					if(action_type == null || action_type.toLowerCase().indexOf("exception") < 0) {
						return;
					}
					
					String tairGroupName = rs.getString("tair_group_name");
					if(tairGroupName == null || "".equals(tairGroupName)) {
						tairGroupName = ConstantParameters.EMPTY_TAIR_GROUPNAME;
					}
					
					long callNum = rs.getLong("callNum");
					
					if(!poMap.containsKey(tairGroupName)) {
						TairException po = new TairException();
						po.setTair_group_name(tairGroupName);
						if(isCur) {
							po.setCurExceptionNum(callNum);
						} else {
							po.setOldExceptionNum(callNum);
						}
						poMap.put(tairGroupName, po);
					} else {
						TairException po = poMap.get(tairGroupName);
						if(isCur) {
							po.setCurExceptionNum(callNum + po.getCurExceptionNum());
						} else {
							po.setOldExceptionNum(callNum + po.getOldExceptionNum());
						}
					}
				}});
		} catch (Exception e) {
			logger.error("getTairExceptionListByDate：", e);
		}		
		return poMap;
	}	
}
