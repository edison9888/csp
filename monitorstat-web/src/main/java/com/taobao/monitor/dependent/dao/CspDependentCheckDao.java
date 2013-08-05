
package com.taobao.monitor.dependent.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.dependent.po.AutoCheckDependentPo;

/**
 * 
 * @author xiaodu
 * @version 2011-5-10 上午11:50:04
 */
public class CspDependentCheckDao extends MysqlRouteBase{
	
	
	private static final Logger logger = Logger.getLogger(CspDependentCheckDao.class);
	
	public CspDependentCheckDao() {
		super(DbRouteManage.get().getDbRouteByRouteId("csp_dependent"));
	}
	
	/**
	 * 根执行状态，查找出所有的记录
	 * @return
	 */
	public List<AutoCheckDependentPo> findAllunRunCheck(String status){
		
		String sql = "select * from CSP_AUTO_CHECK_DEPENDENT where run_status=?";
		
		final List<AutoCheckDependentPo> listPo = new ArrayList<AutoCheckDependentPo>();
		
		try {
			this.query(sql, new Object[]{status}, new SqlCallBack(){
				public void readerRows(ResultSet rs) throws Exception {
					AutoCheckDependentPo po = new AutoCheckDependentPo();
					po.setAppName(rs.getString("app_name"));
					po.setAutoRunScriptId(rs.getString("auto_run_script_id"));
					po.setForbidIps(rs.getString("forbid_Ip"));
					po.setId(rs.getString("id"));
					if(rs.getTimestamp("run_end_time")!=null){
						po.setRunEndTime(new Date(rs.getTimestamp("run_end_time").getTime()));
					}
					if(rs.getTimestamp("run_start_time")!=null){
						po.setRunStartTime(new Date(rs.getTimestamp("run_start_time").getTime()));
					}
					po.setRunResult(rs.getString("run_result"));
					po.setRunStatus(rs.getString("run_status"));
					po.setTargetId(rs.getString("target_ip"));
					po.setUserName(rs.getString("user_name"));
					po.setUserPwd(rs.getString("user_pwd"));
					
					listPo.add(po);
					
				}});
		} catch (Exception e) {
			logger.error("",e);
		}
		return listPo;
	}
	/**
	 * 修改记录
	 * @param po
	 */
	public boolean updateDependentCheck(AutoCheckDependentPo po){
		
		String sql = "update CSP_AUTO_CHECK_DEPENDENT set run_result=?, run_status=?,run_end_time=?,run_start_time=? where id=?";
		try {
			this.execute(sql, new Object[]{po.getRunResult(),po.getRunStatus(),po.getRunEndTime(),po.getRunStartTime(),po.getId()});
		} catch (SQLException e) {
			logger.error("",e);
			return false;
		}
		return true;
		
	}
	
	

}
