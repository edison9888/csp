<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-测试结果详细信息</title>
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
<li class="wbottom addto"><a href="ucResultList.do">测试结果信息</a></li>
<li class="wbottom addto"><a href="ucDetailResult.do?ucResultId=0" class="selected">测试详细信息</a></li>
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
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:100%;align:center">
   <thead>
   <tr>
   <th width="10%" align="center">Selenium命令</th>
   <th width="10%" align="center">元素1</th>
   <th width="10%" align="center">元素2</th>
   <th width="30%" align="center">Selenium执行结果</th>
   <th width="12%" align="center">SeleniumRC执行结果</th>
   <th width="15%" align="center">执行时间</th>
   <th width="15%" align="center">调用类</th>
   </tr>
   </thead>
   <tbody>
   <c:forEach items="${usResultDetail}" var="entity">
   <tr>
     <td>${entity.commandName}</td>
     <td>${entity.pageElement}</td>
     <td>${entity.elementValue}</td>
     <td <c:if test="${entity.responseRc=='ERROR'}">style='bgcolor:red'</c:if> >${entity.responseRc}</td>
     <td>${entity.responseSelenium}</td>
     <td>${entity.costTime}</td>
     <td>${entity.callingClass}</td>
   </tr>
   </c:forEach>
   </tbody>
</table>
</div>
<script type="text/javascript">
$("#myTable").tablesorter();
</script>
</body>
</html>