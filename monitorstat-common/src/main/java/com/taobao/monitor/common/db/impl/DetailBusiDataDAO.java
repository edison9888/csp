/**
 *  Rights reserved by www.taobao.com
 */
package com.taobao.monitor.common.db.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.taobao.monitor.common.db.base.DbRouteManage;
import com.taobao.monitor.common.db.base.MysqlRouteBase;
import com.taobao.monitor.common.po.DetailStatisticDO;

/**
 * 
 * 这类负责对busi_daily_data表的操作，主要就是插入数据和查询数据
 * @author <a href="mailto:xiangfei@taobao.com"> xiangfei</a>
 * @version      2010-6-1:下午11:00:18
 *
 */
public class DetailBusiDataDAO extends MysqlRouteBase {

 
    private static  Logger log = Logger.getLogger(DetailBusiDataDAO.class);
 
    
    public String SQL_INSERT=" insert into BUSI_DAILY_DATA(app_id,group_id,key_id,value_sum,group_sum,stat_date,gmt_create)values(?,?,?,?,?,?,?)";
   
    public String SQL_QUERY = "select app_id,group_id,key_id,value_sum,group_sum,stat_date,gmt_create from BUSI_DAILY_DATA where app_id = ? and key_id = ? and stat_date between ? and ? ";
   
    
    public void insertDailyStatisticData(DetailStatisticDO statisticDO){
	
	Object[] param = new Object[7];
	param[0] =(Integer) statisticDO.getAppId();
	param[1] = (Integer)statisticDO.getGroupId();
	param[2] = (Integer)statisticDO.getKeyId();
	param[3] = (Integer)statisticDO.getVaue();
	param[4] = (Integer)statisticDO.getGroupSum();
	param[5] = statisticDO.getStatisticDate();
	param[6] = statisticDO.getCreateDate();
	
	
	try {
		this.execute(SQL_INSERT,param);
		
	}catch(Exception e){
	    log.error("insertDailyStatisticData error:", e);
	}
    }
    
    public List<DetailStatisticDO> queryStatisticData(int appId,int keyId,Date startDate,Date end){
	
	final List<DetailStatisticDO> statList = new ArrayList<DetailStatisticDO>();
	try {
		this.query(SQL_QUERY, new Object[]{appId,keyId,startDate,end}, new SqlCallBack(){
			public void readerRows(ResultSet rs) throws SQLException {					
			    DetailStatisticDO statistic = new DetailStatisticDO();
			    statistic.setAppId(rs.getInt("app_id"));
			    statistic.setGroupId(rs.getInt("group_id"));
			    statistic.setKeyId(rs.getInt("key_id"));
			    statistic.setVaue(rs.getInt("value_sum"));
			    statistic.setGroupSum(rs.getInt("group_sum"));
			    statistic.setStatisticDate(rs.getDate("stat_date"));
			    statistic.setCreateDate(rs.getDate("gmt_create"));
			    statList.add(statistic);
			    
			}},DbRouteManage.get().getDbRouteByTimeAppid(appId));
	} catch (Exception e) {
		e.printStackTrace();
		
	}
	
	return statList;
    }

}
