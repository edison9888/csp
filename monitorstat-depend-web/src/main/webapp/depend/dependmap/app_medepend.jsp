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
<title>����ģ��</title>
</head>
<body>
<strong>
<span style="color: red;">����ǿ������</span>
����<a href="<%=StartUpParamWraper.getEosUrl()%>" target="blank" >ǿ������ϵͳ(EOS)</a>����Ӧ�����ǿ����⣬��ϵ��������ͤ��
</strong>
<span>
<%
Map<String,ReleaseInfo> releaseMap = (Map<String, ReleaseInfo>)request.getAttribute("releaseMap");
ReleaseInfo releaseInfo = (ReleaseInfo)request.getAttribute("releaseInfo");
String selectDate = (String)request.getAttribute("selectDate");
String opsName = (String)request.getAttribute("opsName");
if(releaseInfo == null)
	out.println("����" + selectDate + "û����ʽ������Ϣ��");
else {
%>
<br/>
Ӧ��<%=opsName%>"��ʽ����"������������<%=releaseInfo.getFinishTime()%>��<a href="<%=request.getContextPath()%>/show/dependmap.do?method=gotoReleaseListPage&appName=<%=opsName%>&dateStart=<%=selectDate%>&dateEnd=<%=selectDate%>&pubLevel=publish" target="_blank">�鿴��ϸ</a>
<%	
}
%>
</span>
				<table width="100%"  class="table table-bordered table-striped">
													<thead>
														<tr>
															<td width="15%" style="text-align: center;">Ӧ������</td>
															<td width="10%" style="text-align: center;">�����˿�<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
															<td width="10%" style="text-align: center;">Ӧ������</td>
															<td width="10%" style="text-align: center;">������</td>
															<td width="10%" style="text-align: center;">����</td>
															<td  style="text-align: center;">����(����|����)<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
															<td  style="text-align: center;">�ӳ�(����|����)<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
															<td width="15%" style="text-align: center;">��ʽ��������ʱ��</td>
															<td style="text-align: center;">�鿴�������<img src="<%=request.getContextPath()%>/statics/css/images/newhotcool.gif" /></td>
														</tr>									
													</thead>
													<%
													List<AppDepApp> meDependList = (List<AppDepApp>)request.getAttribute("meDependList");
													
													for(AppDepApp dep : meDependList) {
														if(dep.getDependOpsName().equals("δ֪"))
															continue;
														out.println("<tr>");
														if(dep.getExistStatus().equals("sub")) {
															out.println("<td style='background-color:#C0C0C0'>" + dep.getDependOpsName() + "&nbsp;&nbsp;<font color='red'>����</font></td>");
														} else if(dep.getExistStatus().equals("add")){
															out.println("<td>" + dep.getDependOpsName() + "&nbsp;&nbsp;<font color='red'>����</font></td>");
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
														if("ǿ".equals(dep.getConfig().getStartPreventIntensity())) {
															color = "red";
														}
														out.println("<td style='text-align: center;'><font color='" + color + "'>" + dep.getConfig().getStartPreventIntensity() + "</font>");
														if("ǿ".equals(dep.getConfig().getRunPreventIntensity())) {
															color = "red";
														} else {
															color = "green";
														}
														out.println("|<font color='" + color + "'>" + dep.getConfig().getRunPreventIntensity() + "</font></td>");
														if("ǿ".equals(dep.getConfig().getStartDelayIntensity())) {
															color = "red";
														} else {
															color = "green";
														}
														out.println("<td style='text-align: center;'><font color='" + color + "'>" + dep.getConfig().getStartDelayIntensity() + "</font>");
														if("ǿ".equals(dep.getConfig().getRunDelayIntensity())) {
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
																	out.println("û�з�����Ϣ");
																} else {
															%>
																<a href="<%=request.getContextPath()%>/show/dependmap.do?method=gotoReleaseListPage&appName=<%=dep.getDependOpsName()%>&dateStart=<%=selectDate%>&dateEnd=<%=selectDate%>&pubLevel=publish" target="_blank"><%=info.getFinishTime()%>,�鿴��ϸ</a>
															<%		
																}
															%>
															</td>															
															<td style="text-align: center;">
															<a href="<%=StartUpParamWraper.getEosUrl()%>dailycheck.do?method=showCheckResult&opsName=<%=dep.getOpsName()%>&targetOpsName=<%=dep.getDependOpsName()%>" target="_blank">�鿴��ϸ������</a>
															</td>															
														<%	
														out.println("</tr>");
													}
													%>
												</table>
</body>
</html>