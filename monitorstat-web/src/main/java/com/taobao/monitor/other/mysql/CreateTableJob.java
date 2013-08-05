
package com.taobao.monitor.other.mysql;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.taobao.monitor.alarm.transfer.WangwangTransfer;
import com.taobao.monitor.common.ao.center.DataBaseInfoAo;
import com.taobao.monitor.common.po.DataBaseInfoPo;
import com.taobao.monitor.web.ao.MonitorTimeAo;

/**
 * 定时创建 业务表 创建两天以后的表
 * @author xiaodu
 * @version 2011-6-13 下午02:10:14
 */
public class CreateTableJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 2);
		
		List<DataBaseInfoPo> dbList = DataBaseInfoAo.get().findAllDataBaseInfo();
		
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyyMMdd");
		
		
		for(DataBaseInfoPo data:dbList){
			
			if(data.getDbType() == 1){ //业务库
				String dbName = data.getDbName();
				try {
					MonitorTimeAo.get().createTimeTable(cal.getTime(), dbName);
					WangwangTransfer.getInstance().sendExtraMessage("小赌", "成功创建mysql业务表", "成功创建"+dbName+"实时监控业务[ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())+"]"+":"+data.getDbUrl());
					WangwangTransfer.getInstance().sendExtraMessage("斩飞", "成功创建mysql业务表", "成功创建"+dbName+"实时监控业务[ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())+"]"+":"+data.getDbUrl());
				} catch (SQLException e) {
					WangwangTransfer.getInstance().sendExtraMessage("小赌", "失败创建mysql业务表", "<font color='red'>失败创建"+dbName+"实时监控业务[ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())+"]"+":"+data.getDbUrl()+"--"+e.getMessage()+"</font>");
					WangwangTransfer.getInstance().sendExtraMessage("斩飞", "失败创建mysql业务表", "<font color='red'>失败创建"+dbName+"实时监控业务[ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())+"]"+":"+data.getDbUrl()+"--"+e.getMessage()+"</font>");
				}
			}
		}
	}
	
	
	public static void  main(String[] args){
		
		CreateTableJob job = new CreateTableJob();
		try {
			job.execute(null);
		} catch (JobExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
