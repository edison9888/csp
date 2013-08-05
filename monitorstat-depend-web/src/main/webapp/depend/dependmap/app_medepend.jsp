<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.csp.depend.po.AppDepApp"%>
<%@page import="com.taobao.csp.depend.util.StartUpParamWraper"%>
<%@page import="com.taobao.monitor.common.po.ReleaseInfo"%>
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
<strong>
<span style="color: red;">依赖强弱数据</span>
来自<a href="<%=StartUpParamWraper.getEosUrl()%>" target="blank" >强弱依赖系统(EOS)</a>。给应用添加强弱检测，联系旺旺“中亭”
</strong>
<span>
<%
Map<String,ReleaseInfo> releaseMap = (Map<String, ReleaseInfo>)request.getAttribute("releaseMap");
ReleaseInfo releaseInfo = (ReleaseInfo)request.getAttribute("releaseInfo");
String selectDate = (String)request.getAttribute("selectDate");
String opsName = (String)request.getAttribute("opsName");
if(releaseInfo == null)
	out.println("日期" + selectDate + "没有正式发布信息。");
else {
%>
<br/>
应用<%=opsName%>"正式发布"，发布结束于<%=releaseInfo.getFinishTime()%>，<a href="<%=request.getContextPath()%>/show/dependmap.do?method=gotoReleaseListPage&appName=<%=opsName%>&dateStart=<%=selectDate%>&dateEnd=<%=selectDate%>&pubLevel=publish" target="_blank">查看详细</a>
<%	
}
%>
</span>
				<table width="100%"  class="table table-bordered table-striped">
													<thead>
														<tr>
															<td width="15%" style="text-align: center;">应用名称</td>
															<td width="10%" style="text-align: center;">依赖端口<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
															<td width="10%" style="text-align: center;">应用类型</td>
															<td width="10%" style="text-align: center;">调用量</td>
															<td width="10%" style="text-align: center;">比例</td>
															<td  style="text-align: center;">屏蔽(启动|运行)<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
															<td  style="text-align: center;">延迟(启动|运行)<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
															<td width="15%" style="text-align: center;">正式发布结束时间</td>
															<td style="text-align: center;">查看检测流程<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
														</tr>									
													</thead>
													<%
													List<AppDepApp> meDependList = (List<AppDepApp>)request.getAttribute("meDependList");
													
													for(AppDepApp dep : meDependList) {
														if(dep.getDependOpsName().equals("未知"))
															continue;
														out.println("<tr>");
														if(dep.getExistStatus().equals("sub")) {
															out.println("<td style='background-color:#C0C0C0'>" + dep.getDependOpsName() + "&nbsp;&nbsp;<font color='red'>减少</font></td>");
														} else if(dep.getExistStatus().equals("add")){
															out.println("<td>" + dep.getDependOpsName() + "&nbsp;&nbsp;<font color='red'>新增</font></td>");
														} else {
															out.println("<td>" + dep.getDependOpsName() + "</td>");
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
															<td style="text-align: center;">
															<%
																String targetOpsName = dep.getDependOpsName();
																ReleaseInfo info = releaseMap.get(targetOpsName);
																System.out.println(targetOpsName + "---" + info);
																if(info == null) {
																	out.println("没有发布信息");
																} else {
															%>
																<a href="<%=request.getContextPath()%>/show/dependmap.do?method=gotoReleaseListPage&appName=<%=dep.getDependOpsName()%>&dateStart=<%=selectDate%>&dateEnd=<%=selectDate%>&pubLevel=publish" target="_blank"><%=info.getFinishTime()%>,查看详细</a>
															<%		
																}
															%>
															</td>															
															<td style="text-align: center;">
															<a href="<%=StartUpParamWraper.getEosUrl()%>dailycheck.do?method=showCheckResult&opsName=<%=dep.getOpsName()%>&targetOpsName=<%=dep.getDependOpsName()%>" target="_blank">查看详细检测过程</a>
															</td>															
														<%	
														out.println("</tr>");
													}
													%>
												</table>
</body>
</html>