<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.taobao.www.arkclient.csp.ManualCurUser"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
 <html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>全量压测-首页</title>
		<link rel="stylesheet" href="${baseUrl}/style/style.css">
		<link rel="shortcut icon" type="image/png" href="${baseUrl}/images/favicon.png">
		<link rel="stylesheet" href="${baseUrl}/style/bootstrap-1.4.0.css">
	</head>
   <body>
   <div id="header" class="topbar">
			<div class="fill">
				<div class="container fixed">
					<h3>
						<a href="index.jsp">manualload&nbsp;<img src="${baseUrl}/images/logoicon.png"/></a>
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
						 <div style="color:white;float:right;padding-top:10px;">欢迎您:
					<% 
					  out.print(ManualCurUser.getLoginUserName(request)); 
					 %>
					来到全量压测系统！
					</div>
					</ul>
				</div>
			</div>
		</div>
<div class="container">
	<div id="bd" style="width: 990px; height: 20px; margin: 0 auto"></div>
	<div style="width: 990px; height: 100px; margin: 0 auto;font-size:14px;">
		<div class="indexpills-div" style="width: 350px;margin-left:100px;">
			<ul class="nav basic indexpills">
				<li><a href="${baseUrl}/addPressures.do">新增配置</a></li>
			</ul>
		</div>
		<div class="indexpills-div" style="width: 300px; ">
			<ul class="nav basic indexpills">
				<li><a href="${baseUrl}/getAllPresureResultInfo.do">压测结果</a></li>
			</ul>
		</div>
		 <br/>
		 <br/>
		 <br/>
		<div class="indexpills-div" style="width: 350px;margin-left:100px; ">
			<ul class="nav basic indexpills">
				<li><a href="${baseUrl}/getAllPressures.do">压测应用</a></li>
			</ul>
		</div>
		<div class="indexpills-div" style="width: 300px; ">
			<ul class="nav basic indexpills">
				<li><a href="${baseUrl}/getAllPreConfigs.do">压测配置</a></li>
			</ul>
		</div>
	</div>
</div>
<jsp:include page="./footer.jsp"></jsp:include>