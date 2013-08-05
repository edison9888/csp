<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>���ļ��-���Խ����Ϣ����</title>
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
<li class="wbottom addto"><a href="ucResultStatList.do">���Ի�����Ϣ</a></li> 
<li class="wbottom addto"><a href="useCaseList.do">��������</a></li>  
<c:if test="${selenium=='true'}">
<li class="wbottom addto"><a href="seleniumServerList.do">Selenium RC�ͻ��˹���</a></li>
</c:if>  
<li class="wbottom addto"><a href="ucResultList.do" class="selected">���Խ����Ϣ</a></li> 
<!--  
<li class="wbottom addto"><a href="ucDetailResult.do?ucResultId=0">������ϸ��Ϣ</a></li>
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
ѡ�����̣�<select id="useCaseId" name="useCaseId" style="width:150px">
<option value="0">ѡ������</option>
<c:forEach items="${ucMap}"  var="en">
<option value="${en.key}">${en.value.useCaseAlias}</option>
</c:forEach>
</select>
&nbsp;&nbsp;��ʼʱ�䣺<input type="text" id="startDate" name="startDate" value="${ucTestStat.startDate}" class="Wdate" style="width:140px" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
&nbsp;&nbsp;����ʱ�䣺<input type="text" id="endDate" name="endDate" value="${ucTestStat.endDate}" class="Wdate" style="width:140px" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd HH:mm:ss'})"/>
&nbsp;&nbsp;<input type="button" value="��ѯ" onclick="subQuery();"/>
</td>
</tr>
</table>
</div>
</form>
<table cellpadding="0" cellspacing="0" border="1" id="myTable" class="tablesorter" style="width:100%;align:center">
   <thead>
   <tr>
   <th width="10%" align="center">��������</th>
   <th width="10%" align="center">���Խ��</th>
   <th width="8%" align="center">����������</th>
   <th width="8%" align="center">ʧ��������</th>
   <th width="30%" align="center">���ʧ�ܵ���Ϣ</th>
   <th width="10%" align="center">����ʱ��(ms)</th>
   <th width="12%" align="center">��ʼʱ��</th>
   <th width="12%" align="center">����ʱ��</th>
   </tr>
   </thead>
   <tbody>
   <c:forEach items="${usResultList}" var="entity">
   <tr title="˫���鿴������ϸ��Ϣ" ondblclick="location='ucDetailResult.do?ucResultId=${entity.usecaseResultId}'">
     <td><a href="ucDetailResult.do?ucResultId=${entity.usecaseResultId}">${entity.useCaseAlias}</a></td>
     <td>
     <c:choose>
     <c:when test="${entity.finallyState==0}">���гɹ�</c:when>
     <c:otherwise><font color="red">����ʧ��</font></c:otherwise>
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