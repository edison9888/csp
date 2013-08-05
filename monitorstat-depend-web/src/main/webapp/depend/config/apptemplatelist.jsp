<%@ page language="java" contentType="text/html; charset=GB18030" isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@page import=" com.taobao.csp.depend.po.CheckupDependConfigRel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.depend.util.StringOptUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>应用模板列表</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/statics/css/main.css" type="text/css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<%
List<CheckupDependConfigRel> templateList = (List<CheckupDependConfigRel>)request.getAttribute("templateList");
if(templateList == null)
  templateList = new ArrayList();
String appStatus = (String)request.getAttribute("appStatus");
%>
<style type="text/css">
table{
width: 90%;
}
</style>
</head>
<body>
<%@ include file="../dailyheader.jsp"%>
	<form action="<%=request.getContextPath()%>/dailycheck.do" style="padding-top: 20px;">
		<table align="center"  class="table table-striped table-condensed table-bordered" style="width: 90%;" align="center">
			<tr>
			<td>应用状态：</td>
			<td>
				<input type="hidden" value="showTemplateList" name="method" />
				<select name="appStatus">
					<option value="1" <%if("1".equals(appStatus))out.print(" selected='selected'");%>>有效</option>
					<option value="0" <%if("0".equals(appStatus))out.print(" selected='selected'");%>>全部</option>
					<option value="-1" <%if("-1".equals(appStatus))out.print(" selected='selected'");%>>无效</option>
				</select>
			</td>
			<td>
				<input type="submit" value="查询" />
			</td>
			</tr>
		</table>
	</form>
	<strong>一共有个<%=templateList.size()%>应用的机器信息,加入CSP强弱依赖检查的应用有<%= CspCheckAppCache.get().getAppIpMap().size()%>个。</strong>
	<table class="table table-striped table-condensed table-bordered" style="width: 90%;" align="center">
		<thead>
			<tr>
				<td width="15%">应用名称</td>
				<td width="10%">应用状态</td>
				<td width="20%">second</td>
				<td width="20%">daily</td>
				<td width="10%">补充ip</td>
				<td width="25%">应用说明</td>
			</tr>
		</thead>
		<%
			for(CheckupDependConfigRel rel: templateList) {
				String sourceIp = CspCheckAppCache.get().getIpFroApp(rel.getOpsName());
				String combineIp = StringOptUtil.combineSeveralString(rel.getSecondMachineIps(),sourceIp);
				String showAppName = "";
				if(sourceIp != null) {
					showAppName = "<span  style='color: red;'>" + rel.getOpsName() + "</span>";
				} else 
					showAppName = rel.getOpsName();
		%>
		<tr>
			<td align="left" width="20%">&nbsp;&nbsp;<a href="<%=request.getContextPath() %>/dailycheck.do?method=getTempleteDetail&opsName=<%=rel.getOpsName()%>"><%=showAppName%></a></td>
			<td width="10%">
			<% 
				if("1".equals(rel.getAppStatus())) {
				  out.print("有效"); 
				} else if("-1".equals(rel.getAppStatus())) {
				  out.print("<font color='red'>无效</font>"); 
				} 
			%>
			</td>
			<td style='word-break:break-all'><div><%=combineIp%></div></td>
			<td style='word-break:break-all'><div><%=rel.getDailyMachineIps()%></div></td>
			<td style='word-break:break-all'><div><%=rel.getAddIps()%></div></td>
			<td style='word-break:break-all'><div><%=rel.getDescription()%></div></td>
		</tr>
		<%}%>
	</table>
</body>
</html>