<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>
<%@page import="org.apache.commons.lang.StringUtils"%><html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="com.taobao.monitor.web.core.dao.impl.MonitorReportConfigDao"%>
<%@page import="com.taobao.monitor.web.vo.ReportTemplate"%>
<%@page import="com.taobao.monitor.web.vo.ReportAcceptor"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="com.taobao.monitor.web.vo.ReportInfoPo"%>
<%@ include file="../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-导入原有报表接收数据</title>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery.1.3.2.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/global.css" rel="stylesheet" />
<style>
td{
  height:34px;
}
</style>
</head>
<%
out.println("开始导入报表模板数据... ...");
out.flush();
MonitorReportConfigDao reportConfigDao = new MonitorReportConfigDao();
if(!UserPermissionCheck.check(request, Constants.REPORT_CONFIG_POWER,"")){
	out.println("你没有操作权限！");
	out.flush();
}else{
	//先添加报表模板
	List<ReportInfoPo> listReportList = MonitorTimeAo.get().findAllReport();
	ReportTemplate reportTemplate = null;
	/*
	|  1 | 日报表                 |           0 | 
	|  2 | 线上自动压测表报       |           1 | 
	|  3 | 应用告警报表           |           1 | 
	|  4 | 运行对比数据报表       |           1 | 
	|  5 | 关键java类执行时间报表 |           1 | 
	|  6 | hsf依赖对比            |           1 
	*/
	out.println("<table cellpadding='1' cellspacing='0' border='1' bordercolor='blue' style='width:99%;align:center'>");
	out.flush();
	out.println("<tr><td>报表ID</td><td>报表名称</td><td>报表类型</td><td>状态</td></tr>");
	out.flush();
	for(ReportInfoPo reportInfoPo : listReportList){
		int reportId = reportInfoPo.getId();
		ReportTemplate temp = reportConfigDao.getReportTemplateById(reportId, "email");
		if(temp.getReportId() == 0){//报表不存在
			reportTemplate = new ReportTemplate();
			reportTemplate.setQuartzCron("0 0 9 * * ?");
			reportTemplate.setType("email");
			reportTemplate.setReportId(reportId);
			if(reportId == 1){
				reportTemplate.setPath("http://127.0.0.1:9999/monitorstat/report/report_day.jsp");
			}else if(reportId == 2){//默认参数?appIds="+appIds
				reportTemplate.setPath("http://127.0.0.1:9999//monitorstat/report/report_loadrun_detail.jsp");
			}else if(reportId == 3){//默认参数 ?appIds="+appIds
				reportTemplate.setPath("http://127.0.0.1:9999//monitorstat/report/report_alarm_new.jsp");
			}else if(reportId == 4){//默认参数?appIds="+appIds 或者 ?appIds="+appIds
				reportTemplate.setPath("http://127.0.0.1:9999//monitorstat/report/report_distinct.jsp");
			}else if(reportId == 5){//默认参数?appIds="+appIds
				reportTemplate.setPath("http://127.0.0.1:9999/monitorstat/report/report_jprof_class_method_new.jsp");
			}else if(reportId == 6){//默认参数?appIds="+appIds
				reportTemplate.setPath("http://127.0.0.1:9999/monitorstat/report/report_distrib_provider_distinct.jsp");
			}
			String reportType = (reportInfoPo.getType() == 0?"用户自定义参数":"区分应用");
			
			out.println("<tr><td>"+reportId+"</td><td>"+reportInfoPo.getName()+"</td><td>"+reportType+"</td><td>导入数据成功</td></tr>");
			out.flush();
			reportConfigDao.addReportTemplate(reportTemplate);
		}
	}
	out.println("</table>");
	out.println("导入报表模板数据成功... ...");
	out.flush();
	out.println("开始导入报表接收人数据... ...");
	out.flush();
	//添加报表接收人信息:yuanlun@taobao.com | 1;2:34,35,36,37,38,42,;3:34,35,36,37,38,42,;4:34,35,36,37,38,42,;5:34,35,36,37,38,42,;6:34,35,36,37,38,42,; 
	List<LoginUserPo> users = MonitorUserAo.get().findAllUser();
	List<ReportAcceptor> acceptors = new ArrayList<ReportAcceptor>(); 
	ReportAcceptor reportAcceptor = null;
	if(users != null){
		out.println("<table cellpadding='1' cellspacing='0' border='1' bordercolor='blue' style='width:99%;align:center'>");
		out.println("<tr><td>报表ID</td><td>接收地址</td><td>报表参数</td><td>状态</td></tr>");
		out.flush();
		for(LoginUserPo userPo:users){
			String reportDesc = userPo.getReportDesc();
			String appDesc = userPo.getGroup();
			if(reportDesc != null && !reportDesc.equals("") && !reportDesc.equals("null")
					&& !reportDesc.equals("Null")){
				String reportIdStr = "1,2,3,4,5,6";
				String[] reportArray = reportDesc.split(";");
				for(String reportApps:reportArray){
					if(userPo.getMail() == null || userPo.getMail().equals("")
							|| userPo.getMail().equals("null")
							|| userPo.getMail().equals("Null")
							|| userPo.getMail().equals("NULL"))continue;
					if(reportApps.equals("1")){//日报,无需选择应用
						int size = reportConfigDao.getReportAppAndAcceptor(1, "email").size();
						if(size == 0){
							reportAcceptor = new ReportAcceptor();
							reportAcceptor.setReportId(1);
							reportAcceptor.setAddress(userPo.getMail());
							reportAcceptor.setType("email");
							reportAcceptor.setReportParam("");
							out.println("<tr><td>"+1+"</td><td>"+userPo.getMail()+"</td><td></td><td>导入数据成功</td></tr>");
							out.flush();
							acceptors.add(reportAcceptor);
						}
					}else{//需要选择应用
						int reportIndex = reportApps.indexOf(":");
						if(reportIndex != -1){
							String[] values = reportApps.split(":");
							int reportId = Integer.parseInt(values[0]);
							if(reportId != 1 && reportId != 2 && reportId != 3
									&& reportId != 4 && reportId != 5 && reportId != 6)continue;
							int size = reportConfigDao.getReportAppAndAcceptor(reportId, "email").size();
							if(size == 0){
								reportAcceptor = new ReportAcceptor();
								reportAcceptor.setReportId(reportId);
								reportAcceptor.setAddress(userPo.getMail());
								reportAcceptor.setType("email");
								
								
								
								
								String[] ll = values[1].split(",");
								String[] hh = appDesc.split(",");
								Set<String> set = new HashSet<String>();
								for(String str:ll){
									set.add(str);
								}
								for(String str:hh){
									set.add(str);
								}
								StringBuffer sb = new StringBuffer();
								for(String str:set){
									if(StringUtils.isNotBlank(str)){
										sb.append(str);
										sb.append(",");
									}
								}
								
								reportAcceptor.setReportParam(sb.toString());
								
								out.println("<tr><td>"+reportId+"</td><td>"+userPo.getMail()+"</td><td>"+values[1]+"</td><td>导入数据成功</td></tr>");
								out.flush();
								acceptors.add(reportAcceptor);
							}
						}
					}
				}
			}
		}
		out.println("</table>");
		out.flush();
		reportConfigDao.addReportAcceptor(acceptors);
	}
	out.println("导入报表接收人数据成功... ...");
	out.flush();
}
%>
<body style="top:0px;">
</body>
</html>