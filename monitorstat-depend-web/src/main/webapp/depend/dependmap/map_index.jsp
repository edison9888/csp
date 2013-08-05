<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.*"%>
<%@page import=" com.taobao.csp.depend.util.JspFormatUtil"%>
<%@page import="com.taobao.monitor.common.po.CspMapKeyInfoPo"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<%%>
<link href="<%=request.getContextPath()%>/statics/css/bootstrap.css" rel="stylesheet">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery.loadmask.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.form.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
<script language="text/javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<script language="text/javascript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.loadmask.js"></script>

<style type="text/css">
td {
	word-break:break-all;
}
</style>
<title>依赖地图首页</title>
<%
	String appName = (String)request.getAttribute("appName");
	String selectDate = (String)request.getAttribute("selectDate");
	String selectTabId = (String)request.getAttribute("selectTabId");
%>
<script type="text/javascript">
//$(document).ready(function(){
//}
var lastChooseTabId = 'tab_home';	//全局Id
function changeFrameUrl(id) {
    $('#' + lastChooseTabId).removeClass('active'); // 移除样式
    $('#' + id).addClass('active'); // 添加一个样式，但不覆盖，如果要取消以前某个样式
    lastChooseTabId = id;
    var url = '<%=request.getContextPath()%>/show/dependmap.do?method=keyindex&appName=<%=appName%>';
    
    if(id == 'tab_home') {
    	url = '<%=request.getContextPath()%>/show/dependmap.do?method=keyindex&appName=<%=appName%>';
    } else if(id == 'tab_black') {
    	url = '<%=request.getContextPath()%>/show/dependmap.do?method=gotoBlackKeyList&appName=<%=appName%>';
    } else if(id == 'tab_me_depend') {
    	url = '<%=request.getContextPath()%>/show/dependmap.do?method=appMeDependIndex&appName=<%=appName%>&selectDate=<%=selectDate%>';
    } else if(id == 'tab_depend_me') {
    	url = '<%=request.getContextPath()%>/show/dependmap.do?method=appDependMeIndex&appName=<%=appName%>&selectDate=<%=selectDate%>';
    } else if(id == 'tab_hsf_alarm') {
    	url = '<%=request.getContextPath()%>/alarmconfig.do?method=searchAlarmConfig&appName=<%=appName%>';
    }
    $("#mydiv").mask("Loading...");
    //$(".grids").mask("Loading...", 500);
		$('#content_nav').load(url, function() {
			//  alert('Load was performed.');
		//	$("#mydiv").unmask();
		});
	
	}
	$(document).ready(function() {
		changeFrameUrl('tab_home');
	});

	function removeBlack(id) {
		$.getJSON("<%=request.getContextPath()%>/show/dependmap.do?method=removeKeyFromBlack", 
			{ id: id}, 
			function(json){
				if(!json.success) {
					alert("取消黑名单失败！error=" + json.msg);
				} else {
					window.location.reload();
				}
		});
	}
function addToBlack(id) {
	$.getJSON(
			"<%=request.getContextPath()%>/show/dependmap.do?method=addKeyToBlack", 
			{ id: id}, 
			function(json){
				if(!json.success) {
					alert("加入黑名单失败！error=" + json.msg);
				} else {
					window.location.reload();
				}
	});
}

function saveHsfAlarm() {
    $(document).ready(function() {
        var form=$("#alarm_form");
        alert(form.serialize());
        form.submit(function() {
        	$.post(form.attr("action"),
        		form.serialize(),
        		function(result,status){
					alert(result);        		
        		},
        		"text");
        	return false;
        });
        
        form.submit();
    }); 
}
</script>

</head>
<body>
<%@ include file="../header.jsp"%>
	<form id="mainForm"  action="<%=request.getContextPath() %>/show/dependmap.do" method="get">
		<input type="hidden" value="index" name="method">
		<div style="text-align: center">
				<div id="page_nav"></div>
			</div>
			</form>
		<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${appName}', selectDate:'${selectDate}'});
		</script>
<ul class="nav nav-tabs">
  <li class="active" id="tab_home">
    <a href="#" onclick="changeFrameUrl('tab_home')">我的key列表</a>
  </li>
  <li id="tab_black"><a href="#" onclick="changeFrameUrl('tab_black')">查看黑名单</a></li>
  <li id="tab_me_depend"><a href="#" onclick="changeFrameUrl('tab_me_depend')">我依赖的应用</a></li>
  <li id="tab_depend_me"><a href="#" onclick="changeFrameUrl('tab_depend_me')">依赖我的应用</a></li>
  <li id="tab_hsf_alarm"><a href="#" onclick="changeFrameUrl('tab_hsf_alarm')">接口预警提醒</a></li>
</ul>
<div id="content_nav" style="width: 100%">
</div>
<div id="mydiv"></div>
</body>
</html>