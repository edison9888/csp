<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo" %>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo" %>

<%@page import="com.taobao.monitor.common.util.TBProductCache"%>
<%@page import="com.taobao.monitor.common.po.ProductLine"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>容量规划-单机容量新增</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/bootstrap/bootstrap-dropdown.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/capacity.js"></script>
</head>
<body class="span15">

<script type="text/javascript">
	var groupMap ={}
</script>
<%
request.setCharacterEncoding("gbk");
%>


    
<form id="myForm" name="myForm" action="" method="post">

<table  id="mytable">
	
	<tr>	
		<td>应用名:</td>
		<td><input  type="text"  id="appName" name="appName" style="width: 80%;"/></td>
	</tr>
	<tr>	
		<td>单机能力:</td>
		<td><input type="text" id="singleCapacity" name="singleCapacity" value="" style="width: 80%;"></td>
	</tr>
		
	<tr>
		<td align="right" colspan="2">
			<input type="button" value="添    加" onclick="add()">  &nbsp;&nbsp;
			<input type="button" value="关    闭" onclick="closeWindow()">
		</td>
	</tr>
	
</table>
</div>
</div>
</form>

<script type="text/javascript">

function add() {
	var appName = $("#appName").attr("value");
	var singleCapacity = $("#singleCapacity").attr("value");
	if (appName==null || appName=="") {
		alert("应用名不能为空!");
		return;
	}
	var patrn=/^[0-9]{1,20}$/; 
	if (!patrn.exec(singleCapacity)) {
		alert("单机能力必须为数字");
		return;
	}
		
	var urlDest = "./manage.do?method=addSingleCapacity";
    var parameters = "appName=" + appName + "&singleCapacity=" + singleCapacity;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		window.close();
    		self.opener.location.reload();  
    	}
   	});
}
	 

</script>

</body>
</html>