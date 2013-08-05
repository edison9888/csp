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

		<title>CSPϵͳ-<% Object obj = request.getAttribute("title"); out.println(obj.toString());%> </title>
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
						    <a href="#" class="dropdown-toggle">ѹ�����ü����</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/config.do?method=list">ѹ���ѯ</a></li>
								<li><a href="<%=request.getContextPath() %>/loadrun/config.do?method=gotoAdd">����ѹ��</a></li>
						    </ul>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="#" class="dropdown-toggle">ѹ�����</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/control.do?method=list">ѹ�����</a></li>
								<li><a href="<%=request.getContextPath() %>">ѹ��״̬</a></li>
						    </ul>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
						    <a href="#" class="dropdown-toggle">ѹ�����</a>
						    <ul class="dropdown-menu">
						        <li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=showGuide">ѹ��ָ��</a></li>
						        <li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=showWarn">ѹ������</a></li>
								<li><a href="<%=request.getContextPath() %>/loadrun/show.do?method=tairGuide">Tairѹ��ָ��</a></li>
						    </ul>
						</li>
					</ul>
					<div style="color:white;float:right;padding-top:10px;">��ӭ��:
					<% 
					  out.print(UserPermissionCheck.getLoginUserName(request)); 
					 %>
					�����Զ�ѹ��ϵͳ��</div>
				</div>
			</div>
	</div>
		
	
	
	
	
