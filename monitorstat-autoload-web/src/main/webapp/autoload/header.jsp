<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.taobao.arkclient.csp.UserPermissionCheck"%>
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
		<script language="JavaScript"
			src="<%=request.getContextPath() %>/statics/swfobject.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath() %>/statics/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/jquery/jquery.js"></script>
		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath() %>/statics/jquery/ui/ui.core.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/jquery/ui/ui.tabs.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/jquery/ui/jquery.ui.widget.js"></script>
		<script type="text/javascript" 
		   src="<%=request.getContextPath() %>/statics/jquery/validation/jquery.validate.js" ></script>	
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/style/css/style.css" />
		<link rel="shortcut icon" type="image/png"
			href="<%=request.getContextPath() %>/style/images/favicon.png" />
		<link rel="stylesheet"
			href="<%=request.getContextPath() %>/style/css/bootstrap-1.4.0.css" />
	
	 	<script type="text/javascript"
			src="<%=request.getContextPath() %>/statics/js/bootstrap-dropdown.js"></script>

		<title>CSP系统-<% Object obj = request.getAttribute("title"); out.println(obj.toString());%> </title>
	</head>
	<body>
	<div id="header" class="topbar">
			<div class="fill">
			    <div style="color:white;float:left;padding-top:9px;"></div>
				<div class="container fixed" >
					<h3>
						<a href="/autoload">Autoload</a>
					</h3>
					<ul class="menu">
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="#" class="dropdown-toggle">压测配置及结果</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/config.do?method=list">压测查询</a></li>
								<li><a href="<%=request.getContextPath() %>/loadrun/config.do?method=gotoAdd">新增压测</a></li>
						    </ul>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="#" class="dropdown-toggle">压测操作</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/control.do?method=list">压测控制</a></li>
								<li><a href="<%=request.getContextPath() %>">压测状态</a></li>
						    </ul>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="#" class="dropdown-toggle">压测帮助</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=showGuide">压测指南</a></li>
						        <li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=showWarn">压测提醒</a></li>
								<li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=tairGuide">Tair压测指南</a></li>
						    </ul>
						</li>
					</ul>
					<div style="color:white;float:right;padding-top:10px;">欢迎您:
					<% 
					  out.print(UserPermissionCheck.getLoginUserName(request)); 
					 %>
					来到自动压测系统！</div>
				</div>
			</div>
	</div>
		
	
	
	
	
