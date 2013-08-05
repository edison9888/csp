<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-Selenium RC管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.1.3.2.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/js/jquery.tablesorter.pager.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/toolTip.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/global.css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.tablesorter.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/ajaxtabs/ajaxtabs.css" rel="stylesheet" />
<style>
td{
  height:34px;
}
</style>
</head>
<body style="top:0px;">
<jsp:include page="../../head.jsp"></jsp:include>
<div style="align:center; width:99%;margin-left:auto;margin-right:auto;">  
<ul class="shadetabs">  
<li class="wbottom addto"><a href="ucResultStatList.do">测试汇总信息</a></li> 
<li class="wbottom addto"><a href="useCaseList.do">用例管理</a></li>  
<c:if test="${selenium=='true'}">
<li class="wbottom addto"><a href="seleniumServerList.do" class="selected">Selenium RC客户端管理</a></li>
</c:if>
<li class="wbottom addto"><a href="ucResultList.do">测试结果信息</a></li>  
<!--  
<li class="wbottom addto"><a href="ucDetailResult.do?ucResultId=0">测试详细信息</a></li> 
-->
</ul>  
</div>  
<div id="countrydivcontainer" style="border:1px solid #4F81BD; align:center; width:99%;margin-left:auto;margin-right:auto;">
<div class="hd" align="left">
<span id="opt">
<c:if test="${selenium}">
<font style='font-weight:bold'><a href="javascript:addSeleniumServer();">新增SeleniumRc客户端</a></font>
</c:if>
</span>
</div>
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:100%;align:center">
   <thead>
   <tr>
   <th width="4%" align="center">id</th>
   <th width="12%" align="center">Selenium RC名称</th>
   <th width="11%" align="center">Selenium RC IP</th>
   <th width="12%" align="center">Selenium RC Port</th>
   <th width="9%" align="center">操作系统</th>
   <th width="15%" align="center">浏览器</th>
   <th width="9%" align="center">Quartz Cron</th>
   <th width="11%" align="center">Quartz Job运行状态</th>
   <c:if test="${selenium}"><th width="30%" align="center">操作</th></c:if>
   </tr>
   </thead>
   <tbody>
   <c:forEach items="${rcList}" var="entity">
   <input type="hidden" id="quartzCron_${entity.id}" name="quartzCron_${entity.id}" value="${entity.quartzCron}"/>
   <tr>
     <td>${entity.id}</td>
     <td>${entity.seleniumRcName}</td>
     <td>${entity.seleniumRcIp}</td>
     <td>${entity.seleniumRcPort}</td>
     <td>${entity.operateSystemType}</td>
     <td>${entity.browsers}</td>
     <td>${entity.quartzCron}</td>
     <td align="center" id="state_${entity.id}">
		<c:choose>
		<c:when test="${entity.jobState == 0}">
		<font style="color:blue;font-weight:bold">${entity.jobStateMsg}</font>
		</c:when>
		<c:when test="${entity.jobState == 1}">
		<font style="color:green;font-weight:bold">${entity.jobStateMsg}</font>
		</c:when>
		<c:when test="${entity.jobState == -1}">
		<font style="color:red;font-weight:bold">${entity.jobStateMsg}</font>
		</c:when>
		</c:choose>
     </td>
     <c:if test="${selenium}">
     <td align="left">
	  <span style="width:100px;">
	  <a onclick="updateSeleniumServer('${entity.id}');" href="#"><font style='font-weight:bold'>修改</font></a>&nbsp;
	  </span>
	  <span style="width:100px;">
	  <a onclick="deleteSeleniumServer('${entity.id}');" href="#"><font style='font-weight:bold'>删除</font></a>&nbsp;
	  </span>
	  <span id="job_${entity.id}">
		<c:choose>
		<c:when test="${entity.jobState == 0}"><!-- 运行中 -->
		<a onclick="ajaxPauseJob('${entity.id}');" href="#"><font style='font-weight:bold'>暂停任务</font></a>&nbsp;
		<a onclick="ajaxStopJob('${entity.id}');" href="#"><font style='font-weight:bold'>停止任务</font></a>&nbsp;
		</c:when>
		<c:when test="${entity.jobState == 1}"><!-- 暂停中 -->
		<a onclick="ajaxResumeJob('${entity.id}');" href="#"><font style='font-weight:bold'>恢复任务</font></a>&nbsp;
		<a onclick="ajaxStopJob('${entity.id}');" href="#"><font style='font-weight:bold'>停止任务</font></a>&nbsp;
		</c:when>
		<c:when test="${entity.jobState == -1}"><!-- 任务删除 新启动任务 -->
		<a onclick="ajaxStartJobJob('${entity.id}');" href="#"><font style='font-weight:bold'>启动任务</font></a>&nbsp;
		</c:when>
		</c:choose>
	  </span>
	 </td>
     </c:if>
   </tr>
   </c:forEach>
   </tbody>
</table>
</div>
<script type="text/javascript">
$("#myTable").tablesorter();
function addSeleniumServer(){
	$.layerSetup({
		 id:"toolTip",
		 title:'新增Selenium Server',
		 width:680,//设置宽度
		 height:290,
		 content:'<iframe id="frm_sentence" src="goAddSeleniumServer.do" frameborder="0" height="99%" width="99%" ></iframe>',
		 isbg:false
		 });
    $.layershow();
}
function updateSeleniumServer(id){
	$.layerSetup({
		 id:"toolTip",
		 title:'修改Selenium Server',
		 width:680,//设置宽度
		 height:290,                       
		 content:'<iframe id="frm_sentence" src="goUpdateSeleniumServer.do?rcId='+id+'" frameborder="0" height="99%" width="99%" ></iframe>',
		 isbg:false
		 });
    $.layershow();
}
</script>
<script type="text/javascript">
function ajaxPauseJob(rcId){
	 if(confirm('确定要暂停执行当前Selenium Server上的用例?')){
		 jQuery.ajax({
				url:"<%=request.getContextPath()%>/selenium/ajaxPauseJob.do",
			    type: 'POST',
			    dataType: 'html',
			    data:"rcId="+rcId,
			    timeout: 200000,
			    error: function(){},
			    success: function(data){
				    if(data == '暂停中'){
				    	$("#job_"+rcId).html("<a onclick='ajaxResumeJob("+rcId+");' href='#'><font style='color:green;font-weight:bold'>恢复任务</font></a>&nbsp;<a onclick='ajaxStopJob("+rcId+");' href='#'><font style='color:green;font-weight:bold'>停止任务</font></a>&nbsp;");
				    	$("#state_"+rcId).html("<font style='color:blue;font-weight:bold'>"+data+"</font>");
				    }
			    }
			})
	 }return false;
}
function ajaxResumeJob(rcId){
	 if(confirm('确定要恢复执行当前Selenium Server上的用例?')){
		 jQuery.ajax({
				url:"<%=request.getContextPath()%>/selenium/ajaxResumeJob.do",
			    type: 'POST',
			    dataType: 'html',
			    data:"rcId="+rcId,
			    timeout: 200000,
			    error: function(){},
			    success: function(data){
				    if(data == '运行中'){
				    	$("#job_"+rcId).html("<a onclick='ajaxPauseJob("+rcId+");' href='#'><font style='color:green;font-weight:bold'>暂停任务</font></a>&nbsp;<a onclick='ajaxStopJob("+rcId+");' href='#'><font style='color:green;font-weight:bold'>停止任务</font></a>&nbsp;");
				    	$("#state_"+rcId).html("<font style='color:blue;font-weight:bold'>"+data+"</font>");
				    }
			    }
			})
	 }return false;
}

function ajaxStartJobJob(rcId){
	 if(confirm('确定要启动执行当前Selenium Server上的用例?')){
		 jQuery.ajax({
				url:"<%=request.getContextPath()%>/selenium/ajaxStartJob.do",
			    type: 'POST',
			    dataType: 'html',
			    data:"rcId="+rcId+"&quartzCron="+$("#quartzCron_"+rcId).val(),
			    timeout: 200000,
			    error: function(){},
			    success: function(data){
				    if(data == '运行中'){
				    	$("#job_"+rcId).html("<a onclick='ajaxPauseJob("+rcId+");' href='#'><font style='color:green;font-weight:bold'>暂停任务</font></a>&nbsp;<a onclick='ajaxStopJob("+rcId+");' href='#'><font style='color:green;font-weight:bold'>停止任务</font></a>&nbsp;");
				    	$("#state_"+rcId).html("<font style='color:blue;font-weight:bold'>"+data+"</font>");
				    }
			    }
			})
	 }return false;
}
function ajaxStopJob(rcId){
	 if(confirm('确定要停止执行当前Selenium Server上的用例?')){
		 jQuery.ajax({
				url:"<%=request.getContextPath()%>/selenium/ajaxStopJob.do",
			    type: 'POST',
			    dataType: 'html',
			    data:"rcId="+rcId,
			    timeout: 200000,
			    error: function(){},
			    success: function(data){
				    if(data == '任务移除'){
				    	$("#job_"+rcId).html("<a onclick='ajaxStartJobJob("+rcId+");' href='#'><font style='color:green;font-weight:bold'>启动任务</font></a>&nbsp;");
				    	$("#state_"+rcId).html("<font style='color:blue;font-weight:bold'>"+data+"</font>");
				    }
			    }
			})
	 }return false;
}
function deleteSeleniumServer(rcId){
	if(confirm('确定要删除当前Selenium Server?')){
	    jQuery.ajax({
			url:"deleteSeleniumServer.do",
		    type: 'POST',
		    dataType: 'html',
		    data:"rcId="+rcId,
		    timeout: 200000,
		    error: function(){},
		    success: function(data){
			    if(data){
			    	window.location.reload(true);
			    }else{
				    alert(data);
			    }
		    }
		})
	}
	return false;
}
</script>
</body>
</html>