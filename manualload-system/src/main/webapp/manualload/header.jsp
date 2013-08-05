<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="com.taobao.www.arkclient.csp.ManualCurUser"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>全量压测-<% Object obj = request.getAttribute("title"); out.println(obj.toString());%></title>
		<link rel="stylesheet" href="${baseUrl}/style/style.css">
		<link rel="shortcut icon" type="image/png" href="${baseUrl}/images/favicon.png">
		<link rel="stylesheet" href="${baseUrl}/style/bootstrap-1.4.0.css">
		<script type="text/javascript" src="${baseUrl}/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="${baseUrl}/script/jquery-1.6.4.min.js"></script>
	</head>
   <body>
   <div id="header" class="topbar">
			<div class="fill">
				<div class="container fixed" >
					<h3>
						<a href="index.jsp">manualload&nbsp;<img src="images/logoicon.png"/></a>
					</h3>
					<ul class="menu">
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="${baseUrl}/addPressures.do" class="dropdown-toggle">新增配置</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="${baseUrl}/getAllPressures.do" class="dropdown-toggle">压测应用</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="${baseUrl}/getAllPreConfigs.do" class="dropdown-toggle">压测配置</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="${baseUrl}/getAllAppMachineByAppId.do" class="dropdown-toggle">压测机器</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="${baseUrl}/getAllPresureResultInfo.do" class="dropdown-toggle">压测结果</a>
						</li>
					</ul>
			        <div style="color:white;float:right;padding-top:10px;">欢迎您:
					<% 
					  out.print(ManualCurUser.getLoginUserName(request)); 
					 %>
					来到全量压测系统！
					</div>
				</div>
			</div>
		</div>
