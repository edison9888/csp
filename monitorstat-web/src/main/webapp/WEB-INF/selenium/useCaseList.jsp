<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-测试用例管理</title>
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
<li class="wbottom addto"><a href="useCaseList.do" class="selected">用例管理</a></li> 
<c:if test="${selenium=='true'}"> 
<li class="wbottom addto"><a href="seleniumServerList.do">Selenium RC客户端管理</a></li>  
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
<a onclick="showsentence();" href="#"><font style='font-weight:bold'>配置告警接收人</font></a>&nbsp;
<c:if test="${selenium}">
<font style='font-weight:bold'><a href="javascript:addCase();">新增用例</a></font>
</c:if>
</span>
</div>
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:100%;align:center">
   <thead>
   <tr>
   <th width="5%" align="center">id</th>
   <th width="15%" align="center">用例名称</th>
   <th width="15%" align="center">适配器类名称</th>
   <th width="15%" align="center">浏览器</th>
   <!--  <th width="12%" align="center">SeleniumRc客户端ID</th>
   <th width="12%" align="center">SeleniumRc客户端名称</th>
   <th width="12%" align="center">SeleniumRc客户端地址</th>
   -->
   <th width="35%" align="center">Base Url</th>
   <c:if test="${selenium}"><th width="15%" align="center">操作</th></c:if>
   </tr>
   </thead>
   <tbody>
   <c:forEach items="${ucList}" var="entity">
   <tr>
     <td>${entity.useCaseId}</td>
     <td>${entity.useCaseAlias}</td>
     <td>${entity.useCaseName}</td>
     <td>${entity.browser}</td>
     <!-- <td>${entity.rcId}</td>
     <td>${entity.rc.seleniumRcName}</td>
     <td>${entity.rc.seleniumRcIp}:${entity.rc.seleniumRcPort}</td>
     -->
     <td>${entity.baseUrl}</td>
     <c:if test="${selenium}">
     <td>
	  <span style="width:100px;">
	  <a onclick="updateCase('${entity.useCaseId}');" href="#"><font style='font-weight:bold'>修改用例</font></a>&nbsp;
	  </span>
	  <span style="width:100px;">
	  <a onclick="deleteCase('${entity.useCaseId}');" href="#"><font style='font-weight:bold'>删除用例</font></a>&nbsp;
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
function addCase(){
    var wid = window.screen.width-80;
    if(typeof(wid)==undefined || wid == ""){
    	wid = 1000;
    }
	$.layerSetup({
		 id:"toolTip",
		 title:'用例适配器管理',
		 width:wid,//设置宽度
		 height:690,
		 content:'<iframe id="frm_sentence" src="goAddUseCase.do" frameborder="0" height="99%" width="99%" ></iframe>',
		 isbg:false
		 });
    $.layershow();
}
function updateCase(useCaseId){
    var wid = window.screen.width-80;
    if(typeof(wid)==undefined || wid == ""){
    	wid = 1000;
    }
	$.layerSetup({
		 id:"toolTip",
		 title:'用例适配器管理',
		 width:wid,//设置宽度
		 height:690,
		 content:'<iframe id="frm_sentence" src="goUpdateUseCase.do?useCaseId='+useCaseId+'" frameborder="0" height="99%" width="99%" ></iframe>',
		 isbg:false
		 });
    $.layershow();
}
function deleteCase(useCaseId){
	if(confirm('确定要删除当前用例?')){
	    jQuery.ajax({
			url:"deleteCase.do",
		    type: 'POST',
		    dataType: 'html',
		    data:"useCaseId="+useCaseId,
		    timeout: 200000,
		    error: function(){},
		    success: function(data){
			   window.location.reload(true);
		    }
		})
	}
	return false;
}
function showsentence(reportId){
	$.layerSetup({
		 id:"toolTip",
		 title:'配置交易巡警告警接收人',
		 width:762,//设置宽度
		 height:650,
		 content:'<iframe id="frm_sentence" src="goSeleniumAlarmAcceptor.do" frameborder="0" height="99%" width="99%" ></iframe>',
		 isbg:false
		 });
	$.layershow();
}
</script>
</body>
</html>