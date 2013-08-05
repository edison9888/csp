<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
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
<title>核心监控-导入应用异常报表接收人数据</title>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery.1.3.2.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/global.css" rel="stylesheet" />
<style>
td{
  height:34px;
}
</style>
</head>
<%
out.println("开始导入应用异常报表接收人数据... ...");
out.flush();
MonitorReportConfigDao reportConfigDao = new MonitorReportConfigDao();
if(!UserPermissionCheck.check(request, Constants.REPORT_CONFIG_POWER,"")){
	out.println("你没有操作权限！");
	out.flush();
}else{
	List<LoginUserPo> alluserList = MonitorUserAo.get().findAllUser();	
	List<ReportAcceptor> acceptors = new ArrayList<ReportAcceptor>(); 
	out.println("<table cellpadding='1' cellspacing='0' border='1' bordercolor='blue' style='width:99%;align:center'>");
	out.println("<tr><td>报表ID</td><td>接收地址</td><td>报表参数</td><td>状态</td></tr>");
	out.flush();
	ReportAcceptor reportAcceptor = null;
	for(LoginUserPo po:alluserList){
		if(po.getMail()==null||po.getMail().equals("")
				||po.getMail().equals("null")
				||po.getMail().equals("Null")){
			continue;
		}			
		//appIds="+po.getGroup());
		reportAcceptor = new ReportAcceptor();
		reportAcceptor.setReportId(14);
		reportAcceptor.setAddress(po.getMail());
		reportAcceptor.setType("email");
		reportAcceptor.setReportParam(po.getGroup());
		out.println("<tr><td>"+14+"</td><td>"+po.getMail()+"</td><td>"+po.getGroup()+"</td><td>导入数据成功</td></tr>");
		out.flush();
		acceptors.add(reportAcceptor);
	}
	out.println("</table>");
	out.flush();
	reportConfigDao.addReportAcceptor(acceptors);
	out.println("导入报表接收人数据成功... ...");
	out.flush();
}
%>
<body style="top:0px;">
</body>
</html>