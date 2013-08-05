<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-测试结果信息管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.1.3.2.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/js/jquery.tablesorter.pager.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/toolTip.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/global.css" rel="stylesheet" />

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery-layer.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.tablesorter.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/ajaxtabs/ajaxtabs.css" rel="stylesheet" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<style>
td{
  height:34px;
}
table.tablesorter tbody tr.error_class td {
	background-color:#FF8E8E;
}
table.tablesorter tbody tr.success_class td {
	background-color:green;
}
</style>
<script type="text/javascript">
if("${operate}"=="add" || "${operate}"=="update"){
 window.parent.location.reload(true);
}
</script>
</head>
<body style="top:0px;">
<jsp:include page="../../head.jsp"></jsp:include>
<div style="align:center; width:99%;margin-left:auto;margin-right:auto;">  
<ul class="shadetabs">  
<li class="wbottom addto"><a href="ucResultStatList.do">测试汇总信息</a></li> 
<li class="wbottom addto"><a href="useCaseList.do">用例管理</a></li>  
<c:if test="${selenium=='true'}">
<li class="wbottom addto"><a href="seleniumServerList.do">Selenium RC客户端管理</a></li>
</c:if>  
<li class="wbottom addto"><a href="ucResultList.do" class="selected">测试结果信息</a></li> 
<!--  
<li class="wbottom addto"><a href="ucDetailResult.do?ucResultId=0">测试详细信息</a></li>
-->
</ul>  
</div>  
<div id="countrydivcontainer" style="border:1px solid #4F81BD; align:center; width:99%;margin-left:auto;margin-right:auto;">
<div class="hd" align="left">
<span id="opt">
<c:if test="${selenium}">
<font style='font-weight:bold'></font>
</c:if>
</span>
</div>
<form id="ucResultForm" name="ucResultForm" action="ucResultList.do" method="post">
<div>
<table>
<tr><td style="height:2px"></td></tr>
<tr style="background-color:#C3D9FF">
<td>
&nbsp;<img src="<%=request.getContextPath()%>/statics/images/pek1.gif" />
选择流程：<select id="useCaseId" name="useCaseId" style="width:150px">
<option value="0">选择流程</option>
<c:forEach items="${ucMap}"  var="en">
<option value="${en.key}">${en.value.useCaseAlias}</option>
</c:forEach>
</select>
&nbsp;&nbsp;开始时间：<input type="text" id="startDate" name="startDate" value="${ucTestStat.startDate}" class="Wdate" style="width:140px" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
&nbsp;&nbsp;结束时间：<input type="text" id="endDate" name="endDate" value="${ucTestStat.endDate}" class="Wdate" style="width:140px" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
&nbsp;&nbsp;<input type="button" value="查询" onclick="subQuery();"/>
</td>
</tr>
</table>
</div>
</form>
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:100%;align:center">
   <thead>
   <tr>
   <th width="10%" align="center">用例名称</th>
   <th width="10%" align="center">测试结果</th>
   <th width="8%" align="center">测试命令数</th>
   <th width="8%" align="center">失败命令数</th>
   <th width="30%" align="center">最后失败的信息</th>
   <th width="10%" align="center">测试时间(ms)</th>
   <th width="12%" align="center">开始时间</th>
   <th width="12%" align="center">结束时间</th>
   </tr>
   </thead>
   <tbody>
   <c:forEach items="${usResultList}" var="entity">
   <tr title="双击查看测试详细信息" ondblclick="location='ucDetailResult.do?ucResultId=${entity.usecaseResultId}'">
     <td><a href="ucDetailResult.do?ucResultId=${entity.usecaseResultId}">${entity.useCaseAlias}</a></td>
     <td>
     <c:choose>
     <c:when test="${entity.finallyState==0}">运行成功</c:when>
     <c:otherwise><font color="red">运行失败</font></c:otherwise>
     </c:choose>
     </td>
     <td>${entity.commandsProcessed}</td>
     <td>${entity.failedCommands}</td>
     <td><a href="ucDetailResult.do?ucResultId=${entity.usecaseResultId}"><font color="red">${entity.lastFailedCommandMessage}</font></a></td>
     <td>${entity.duration}</td>
     <td>${entity.startDate}</td>
     <td>${entity.endDate}</td>
   </tr>
   </c:forEach>
   </tbody>
</table>
</div>
<script type="text/javascript">
$("#myTable").tablesorter();
function subQuery(){
	document.ucResultForm.submit();
}
</script>
</body>
</html>