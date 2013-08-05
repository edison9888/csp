<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.moinitor.matrix.dataobject.*" %>
<%@ page import="com.taobao.moinitor.matrix.enumtype.*" %>
<%@ page import="com.taobao.moinitor.matrix.util.*" %>
<%@ page import="com.taobao.moinitor.matrix.*" %>
<%@ page import="com.taobao.moinitor.matrix.parser.MatrixAppParser" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%= request.getParameter("name")%> - 应用详情</title>

<link type="text/css" href="<%=request.getContextPath() %>/statics/css/sns/app_detail.css" rel="stylesheet" />
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

<div class="right-conent"><%@ include file="include/app_detail.jsp" %></div>
</div>
</body>
</html>
