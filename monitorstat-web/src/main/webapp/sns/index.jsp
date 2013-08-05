<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><%= request.getParameter("app-group") == null ? AppGroupPo.getIndexShowAppGroup().getGroupName()  : URLDecoder.decode(request.getParameter("app-group"), "utf-8") %> - 实时监控 </title>

<link type="text/css" href="<%=request.getContextPath() %>/statics/css/sns/app_group.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/jquery.treeview.css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.datepicker.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/i18n/ui.datepicker-zh-CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.treeview.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
</head>

<body>
<div id="content">
<div class="left-menu"><%@ include file="include/left_menu.jsp"%></div>

<div class="right-conent"><%@ include file="include/app_group.jsp" %></div>
</div>
</body>
</html>