<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.po.AppDepApp"%>
<%@page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<title>依赖模块</title>
</head>
<body>
				<table width="100%"  class="table table-bordered table-striped">
													<thead>
														<tr>
															<td width="30%" style="text-align: center;">应用名称</td>
															<td width="10%" style="text-align: center;">服务端口</td>
															<td width="10%" style="text-align: center;">应用类型</td>
															<td width="10%" style="text-align: center;">调用量</td>
															<td width="10%" style="text-align: center;">比例</td>
															<td  style="text-align: center;">屏蔽(启动|运行)</td>
															<td  style="text-align: center;">延迟(启动|运行)</td>
														</tr>									
													</thead>
													<%
													List<AppDepApp> dependMeList = (List<AppDepApp>)request.getAttribute("dependMeList");
													
													for(AppDepApp dep : dependMeList) {
														if(dep.getOpsName().equals("未知"))
															continue;
														out.println("<tr>");
														if(dep.getExistStatus().equals("sub")) {
															out.println("<td style='background-color:#C0C0C0'>" + dep.getOpsName() + "&nbsp;&nbsp;<font color='red'>减少</font></td>");
														} else if(dep.getExistStatus().equals("add")){
															out.println("<td>" + dep.getOpsName() + "&nbsp;&nbsp;<font color='red'>新增</font></td>");
														} else {
															out.println("<td>" + dep.getOpsName() + "</td>");
														}
														
														if(dep.getPortInfo() != 0) {
															out.println("<td style='text-align: center;'>" + dep.getPortInfo() + "</td>");
														} else {
															out.println("<td style='text-align: center;'>-</td>");
														}
														
														out.println("<td style='text-align: center;'>" + dep.getDependAppType() + "</td>");
														out.println("<td style='text-align: center;'>" + Utlitites.fromatLong(dep.getCallnum() + "") + "</td>");
														out.println("<td style='text-align: center;'>" + dep.getRate() + "</td>");
														
														String color = "green";
														if("强".equals(dep.getConfig().getStartPreventIntensity())) {
															color = "red";
														}
														out.println("<td style='text-align: center;'><font color='" + color + "'>" + dep.getConfig().getStartPreventIntensity() + "</font>");
														if("强".equals(dep.getConfig().getRunPreventIntensity())) {
															color = "red";
														} else {
															color = "green";
														}
														out.println("|<font color='" + color + "'>" + dep.getConfig().getRunPreventIntensity() + "</font></td>");
														if("强".equals(dep.getConfig().getStartDelayIntensity())) {
															color = "red";
														} else {
															color = "green";
														}
														out.println("<td style='text-align: center;'><font color='" + color + "'>" + dep.getConfig().getStartDelayIntensity() + "</font>");
														if("强".equals(dep.getConfig().getRunDelayIntensity())) {
															color = "red";
														} else {
															color = "green";
														}
														out.println("|<font color='" + color + "'>" + dep.getConfig().getRunDelayIntensity() + "</font></td>");
														%>
														<%	
														out.println("</tr>");
													}
													%>
												</table>
</body>
</html>