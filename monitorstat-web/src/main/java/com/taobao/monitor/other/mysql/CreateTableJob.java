
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
 * ��ʱ���� ҵ��� ���������Ժ�ı�
 * @author xiaodu
 * @version 2011-6-13 ����02:10:14
 */
public class CreateTableJob implements Job{

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, 2);
		
		List<DataBaseInfoPo> dbList = DataBaseInfoAo.get().findAllDataBaseInfo();
		
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyyMMdd");
		
		
		for(DataBaseInfoPo data:dbList){
			
			if(data.getDbType() == 1){ //ҵ���
				String dbName = data.getDbName();
				try {
					MonitorTimeAo.get().createTimeTable(cal.getTime(), dbName);
					WangwangTransfer.getInstance().sendExtraMessage("С��", "�ɹ�����mysqlҵ���", "�ɹ�����"+dbName+"ʵʱ���ҵ��[ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())+"]"+":"+data.getDbUrl());
					WangwangTransfer.getInstance().sendExtraMessage("ն��", "�ɹ�����mysqlҵ���", "�ɹ�����"+dbName+"ʵʱ���ҵ��[ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())+"]"+":"+data.getDbUrl());
				} catch (SQLException e) {
					WangwangTransfer.getInstance().sendExtraMessage("С��", "ʧ�ܴ���mysqlҵ���", "<font color='red'>ʧ�ܴ���"+dbName+"ʵʱ���ҵ��[ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())+"]"+":"+data.getDbUrl()+"--"+e.getMessage()+"</font>");
					WangwangTransfer.getInstance().sendExtraMessage("ն��", "ʧ�ܴ���mysqlҵ���", "<font color='red'>ʧ�ܴ���"+dbName+"ʵʱ���ҵ��[ms_monitor_data_"+parseLogFormatDate.format(cal.getTime())+"]"+":"+data.getDbUrl()+"--"+e.getMessage()+"</font>");
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
