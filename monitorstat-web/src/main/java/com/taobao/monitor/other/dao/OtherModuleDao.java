
package com.taobao.monitor.other.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.other.tbsession.TbSeesionLog;

/**
 * 其它一些（临时，，杂项）模块的的操作
 * @author xiaodu
 * @version 2011-5-13 上午09:40:09
 */
public class OtherModuleDao extends MysqlRouteBase{
	
	private static final Logger logger = Logger.getLogger(OtherModuleDao.class);
	
	public OtherModuleDao(){
		super(DbRouteManage.get().getDbRouteByRouteId("Main"));
	}
	
	public void addTbsessionLog(TbSeesionLog log){
		
		String sql = "insert into CSP_TBSESSION_LOG(key_name,value_sum,record_count,max_value_len,min_value_len,per_sum,max_per,log_type,collect_time)" +
				"values(?,?,?,?,?,?,?,?,CURRENT_DATE())";
		
		try {
			this.execute(sql,new Object[]{log.getKeyName(),log.getValueSum(),log.getValueCount(),log.getMaxValueLen(),log.getMinValueLen(),log.getPerSum(),log.getMaxPer(),log.getLogType()});
		} catch (SQLException e) {
			logger.error("", e);
		}
	}

}
