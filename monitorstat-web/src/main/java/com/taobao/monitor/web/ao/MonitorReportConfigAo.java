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
 * @author <a href="mailto:zhanfei.tm@taobao.com">ն��</a>
 * 2011-5-11 - ����09:02:22
 * @version 1.0
 */
@Service
public class MonitorReportConfigAo {
	
	private static final Logger logger = Logger.getLogger(MonitorReportConfigAo.class);
	
	private MonitorReportConfigDao reportConfigDao = new MonitorReportConfigDao();
	
	/**
	 * ��������
	 * @author ն��
	 * @param reportTemplate
	 * 2011-5-11 - ����09:08:09
	 */
	public void addReport(ReportTemplate reportTemplate){
		reportConfigDao.addReport(reportTemplate);
	}
	
	/**
	 * �޸ı���
	 * @author ն��
	 * @param reportId
	 * 2011-5-13 - ����01:19:59
	 */
	public void updateReport(ReportTemplate reportTemplate){
		reportConfigDao.updateBaseReportInfo(reportTemplate);
		reportConfigDao.updateReportTemplate(reportTemplate);
		
	}
	
	/**
	 * �����������ƻ�ȡ����ID
	 * @author ն��
	 * @param reportName
	 * @return
	 * 2011-5-13 - ����10:36:12
	 */
//	public int getReportIdByName(String reportName){
//		return reportConfigDao.getReportIdByName(reportName);
//	}
	
	/**
	 * �����������ƻ�ȡ����ID
	 * @author ն��
	 * @param reportName
	 * @return
	 * 2011-5-13 - ����10:36:12
	 */
	public ReportTemplate getReportById(int reportId){
		return reportConfigDao.getReportById(reportId);
	}
	
	/**
	 * �����������ƻ�ȡ����ID
	 * @author ն��
	 * @param reportName
	 * @return
	 * 2011-5-13 - ����10:36:12
	 */
	public ReportTemplate getReportTemplateById(int reportId, String type){
		return reportConfigDao.getReportTemplateById(reportId, type);
	}
	
	/**
	 * ��ȡ���е�ǰ����
	 * @author ն��
	 * @return
	 * 2011-5-11 - ����09:19:50
	 */
	public List<ReportTemplate> findAllReport(){
		return reportConfigDao.findAllReport();
	}
	
	/**
	 * ɾ������
	 * @author ն��
	 * @param reportId
	 * 2011-5-12 - ����10:33:34
	 */
	public void deleteReport(int reportId){
		reportConfigDao.deleteReport(reportId);
		reportConfigDao.deleteReportTemplate(reportId);
		reportConfigDao.deleteReAcceptorByReportId(reportId);
	}
	
	/**
	 * ��ȡ���������
	 * @author ն��
	 * @param appId
	 * @param type->email|sms|ww
	 * @return
	 * 2011-5-12 - ����01:38:51
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
	 * ��ȡ�����������Ϣ
	 * @author ն��
	 * @param reportId
	 * @param type
	 * @return
	 * 2011-5-12 - ����04:18:23
	 */
	public String getReportApp(int reportId, String type){
		List<ReportAcceptor> list = reportConfigDao
		.getReportAppAndAcceptor(reportId, type);
		return getReportParamIds(list);
	}
	
	/**
	 * ��ȡ���Ʊ����Ӧ��
	 * @author ն��
	 * @param appId
	 * @param type->email|sms|ww
	 * @return
	 * 2011-5-12 - ����01:38:51
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
	 * ��װ���ñ���Ӧ���Լ���Ӧ�����������������
	 * @author ն��
	 * @param hidEmailList
	 * @param hidSelectIds
	 * @param type �ʺ�����
	 * 2011-5-12 - ����03:08:09
	 */
	public void addReportAcceptor(int reportId ,String[] addressArray, 
			String reportParam, String type, ReportTemplate report){
		//hidSelectIdsΪ�ձ�ʾ���õ�ǰ�ʼ��ʺ�����Ҫ���ͱ����Ӧ��
		if(addressArray != null && addressArray.length > 0){
			String path = report.getPath();
			int question_mark = path.indexOf("?");
			StringBuffer adrs = new StringBuffer();
			for(String address:addressArray){
				adrs.append("'"+address+"',");
			}
			//1.����reportId��ɾ��addressArray�Ѿ����õı��������Ϣ
			reportConfigDao.deleteReAcceptorByReIdAndAddress(reportId, 
					adrs.toString().substring(0, adrs.toString()
							.lastIndexOf(",")));
			//2.�����û�Ӧ�õı��������Ϣ

			List<ReportAcceptor> acceptors = new ArrayList<ReportAcceptor>();
			ReportAcceptor acceptor = null;
			for(String address:addressArray){
				if(reportParam != null && !reportParam.equals("")
						|| (report.getReportType() == Constants.REPORT_TYPE_USER
						&& question_mark == -1)){//�û��Զ����Ҳ�������Ҳ����
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
	 * ɾ�������������Ϣ
	 * @author ն��
	 * @param reportId
	 * @param addressArray
	 * 2011-5-19 - ����11:14:58
	 */
	public void removeAcceptorByAddress(int reportId ,String[] addressArray){
		if(addressArray != null && addressArray.length > 0){
			StringBuffer adrs = new StringBuffer();
			for(String address:addressArray){
				adrs.append("'"+address+"',");
			}
			//1.����reportId��ɾ��addressArray�Ѿ����õı��������Ϣ
			reportConfigDao.deleteReAcceptorByReIdAndAddress(reportId, 
					adrs.toString().substring(0, adrs.toString()
							.lastIndexOf(",")));
		}
	}
	
	/**
	 * ��ȡ��������м���,�ָ�
	 * @author ն��
	 * @param list
	 * @return
	 * 2011-5-12 - ����05:55:24
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
