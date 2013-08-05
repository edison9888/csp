/*
 * Taobao.com Inc.
 * Copyright (c) 2010-2011 All Rights Reserved.
 */
package com.taobao.monitor.web.ao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.taobao.monitor.common.util.Constants;
import com.taobao.monitor.web.core.dao.impl.MonitorReportConfigDao;
import com.taobao.monitor.web.vo.ReportAcceptor;
import com.taobao.monitor.web.vo.ReportTemplate;

/**
 * .
 * @author <a href="mailto:zhanfei.tm@taobao.com">斩飞</a>
 * 2011-5-11 - 下午09:02:22
 * @version 1.0
 */
@Service
public class MonitorReportConfigAo {
	
	private static final Logger logger = Logger.getLogger(MonitorReportConfigAo.class);
	
	private MonitorReportConfigDao reportConfigDao = new MonitorReportConfigDao();
	
	/**
	 * 新增报表
	 * @author 斩飞
	 * @param reportTemplate
	 * 2011-5-11 - 下午09:08:09
	 */
	public void addReport(ReportTemplate reportTemplate){
		reportConfigDao.addReport(reportTemplate);
	}
	
	/**
	 * 修改报表
	 * @author 斩飞
	 * @param reportId
	 * 2011-5-13 - 下午01:19:59
	 */
	public void updateReport(ReportTemplate reportTemplate){
		reportConfigDao.updateBaseReportInfo(reportTemplate);
		reportConfigDao.updateReportTemplate(reportTemplate);
		
	}
	
	/**
	 * 根据任务名称获取任务ID
	 * @author 斩飞
	 * @param reportName
	 * @return
	 * 2011-5-13 - 上午10:36:12
	 */
//	public int getReportIdByName(String reportName){
//		return reportConfigDao.getReportIdByName(reportName);
//	}
	
	/**
	 * 根据任务名称获取任务ID
	 * @author 斩飞
	 * @param reportName
	 * @return
	 * 2011-5-13 - 上午10:36:12
	 */
	public ReportTemplate getReportById(int reportId){
		return reportConfigDao.getReportById(reportId);
	}
	
	/**
	 * 根据任务名称获取任务ID
	 * @author 斩飞
	 * @param reportName
	 * @return
	 * 2011-5-13 - 上午10:36:12
	 */
	public ReportTemplate getReportTemplateById(int reportId, String type){
		return reportConfigDao.getReportTemplateById(reportId, type);
	}
	
	/**
	 * 获取所有当前报表
	 * @author 斩飞
	 * @return
	 * 2011-5-11 - 下午09:19:50
	 */
	public List<ReportTemplate> findAllReport(){
		return reportConfigDao.findAllReport();
	}
	
	/**
	 * 删除报表
	 * @author 斩飞
	 * @param reportId
	 * 2011-5-12 - 上午10:33:34
	 */
	public void deleteReport(int reportId){
		reportConfigDao.deleteReport(reportId);
		reportConfigDao.deleteReportTemplate(reportId);
		reportConfigDao.deleteReAcceptorByReportId(reportId);
	}
	
	/**
	 * 获取报表接收人
	 * @author 斩飞
	 * @param appId
	 * @param type->email|sms|ww
	 * @return
	 * 2011-5-12 - 下午01:38:51
	 */
	public Set<String> getReportAcceptor(int reportId, String type){
		Set<String> set = new HashSet<String>();
		List<ReportAcceptor> list = reportConfigDao
		.getReportAppAndAcceptor(reportId, type);
		for(ReportAcceptor acceptor : list){
			set.add(acceptor.getAddress());
		}
		return set;
	}
	
	/**
	 * 获取报表接收人信息
	 * @author 斩飞
	 * @param reportId
	 * @param type
	 * @return
	 * 2011-5-12 - 下午04:18:23
	 */
	public String getReportApp(int reportId, String type){
		List<ReportAcceptor> list = reportConfigDao
		.getReportAppAndAcceptor(reportId, type);
		return getReportParamIds(list);
	}
	
	/**
	 * 获取定制报表的应用
	 * @author 斩飞
	 * @param appId
	 * @param type->email|sms|ww
	 * @return
	 * 2011-5-12 - 下午01:38:51
	 */
	public String getReportParamsByAddress(int reportId, 
			String type, String addressList){
		List<ReportAcceptor> list = null;
		if(addressList != null && !addressList.equals("")){
			String[] addressArray = addressList.split(",");
			StringBuffer adrs = new StringBuffer();
			for(String address:addressArray){
				adrs.append("'"+address+"',");
			}
			list = reportConfigDao.getReportParamsByAddress(reportId,
					type, adrs.toString().substring(0, adrs.toString()
							.lastIndexOf(",")));
		}
		return getReportParamIds(list);
	}
	
	/**
	 * 组装配置报表应用以及对应接受人批量入库数据
	 * @author 斩飞
	 * @param hidEmailList
	 * @param hidSelectIds
	 * @param type 帐号类型
	 * 2011-5-12 - 下午03:08:09
	 */
	public void addReportAcceptor(int reportId ,String[] addressArray, 
			String reportParam, String type, ReportTemplate report){
		//hidSelectIds为空表示设置当前邮件帐号无需要发送报表的应用
		if(addressArray != null && addressArray.length > 0){
			String path = report.getPath();
			int question_mark = path.indexOf("?");
			StringBuffer adrs = new StringBuffer();
			for(String address:addressArray){
				adrs.append("'"+address+"',");
			}
			//1.根据reportId先删除addressArray已经配置的报表接受信息
			reportConfigDao.deleteReAcceptorByReIdAndAddress(reportId, 
					adrs.toString().substring(0, adrs.toString()
							.lastIndexOf(",")));
			//2.配置用户应用的报表接受信息

			List<ReportAcceptor> acceptors = new ArrayList<ReportAcceptor>();
			ReportAcceptor acceptor = null;
			for(String address:addressArray){
				if(reportParam != null && !reportParam.equals("")
						|| (report.getReportType() == Constants.REPORT_TYPE_USER
						&& question_mark == -1)){//用户自定义且不带参数也保存
					acceptor = new ReportAcceptor();
					acceptor.setAddress(address);
					acceptor.setReportParam(reportParam);
					acceptor.setType(type);
					acceptor.setReportId(reportId);
					acceptors.add(acceptor);
				}

			}
			reportConfigDao.addReportAcceptor(acceptors);
		}		
	}
	
	/**
	 * 删除报表接收人信息
	 * @author 斩飞
	 * @param reportId
	 * @param addressArray
	 * 2011-5-19 - 上午11:14:58
	 */
	public void removeAcceptorByAddress(int reportId ,String[] addressArray){
		if(addressArray != null && addressArray.length > 0){
			StringBuffer adrs = new StringBuffer();
			for(String address:addressArray){
				adrs.append("'"+address+"',");
			}
			//1.根据reportId先删除addressArray已经配置的报表接受信息
			reportConfigDao.deleteReAcceptorByReIdAndAddress(reportId, 
					adrs.toString().substring(0, adrs.toString()
							.lastIndexOf(",")));
		}
	}
	
	/**
	 * 获取报表参数中间以,分割
	 * @author 斩飞
	 * @param list
	 * @return
	 * 2011-5-12 - 下午05:55:24
	 */
	private String getReportParamIds(List<ReportAcceptor> list){
		StringBuffer appIds = new StringBuffer();
		if(list == null)return "";
		for(ReportAcceptor acceptor : list){
			appIds.append(acceptor.getReportParam()+",");
		}
		String reStr = appIds.toString();
		if( reStr != null && !reStr.equals("")){
			reStr = reStr.substring(0, reStr.lastIndexOf(","));
		}
		return reStr;
	}
}
