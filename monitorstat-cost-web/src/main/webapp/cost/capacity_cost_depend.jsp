<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<title>�����ɱ���Ϣ</title>

</head>
<body style="padding-top:50px" class="span20">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<div width="100%" align="right">
<strong><font color="#870A08" size = "2"><lable>1���ɱ� = 10�ĸ�9�η�̨����&nbsp; </lable></font></strong>
</div>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th colspan="6">${appName}(��pv:${pv})�����ɱ���Ϣ</td>
      </tr>
      <tr align="center">
      	<th width="250" align="center">��������</td>
      	<th width="100" align="center">��������</td>
      	<th width="100" align="center">����ģʽ</td>
        <th width="300" align="right">����������</td>
        <th width="150" align="center">�����ɱ�</td>
        <th width="200" align="center">����pv�����ɱ�</td>
      </tr>
      </thead>
      
      <tbody>
      
      <c:forEach items="${costDepList}" var="dep">
      <tr>
      	<td align="center">${dep.depAppName}(${dep.depAppMachineNum}̨)</td>
      	<td align="center">${dep.depType}</td>
      	<td align="center">${dep.depMode}</td>
      	<td align="center">${dep.formatDepPv} / ${dep.formatDepAppTotalPv}(��)</td>
      	<td align="center">${dep.formatDepCost}</td>
      	<td align="center">${dep.formatPerDepCost}</td>
	  </tr>
	  </c:forEach>
	  
	  <strong><font color="#870A08">
	  	<tr>
      		<th colspan="3">�ϼ�</th>
      		<th align="center">${totalDepPv}</th>
      		<th align="center">${totalDepCost}</th>
      		<th align="center">${totalPerDepCost}</th>
	  </tr>
	  </font></strong>
	 
 	 </tbody>

</table>
</div>

<script type="text/javascript">
$("#mytable tr:eq(0)").find("th:eq(0)").css("background-color", "#ECECEC");
$("#mytable tr:eq(1)").find("th:eq(0),th:eq(1),th:eq(2),th:eq(3),th:eq(4),th:eq(5)").css("background-color", "#ECECEC");


$(function(){
      $("#mytable tr td").mouseover(function(){
         $(this).parent().children("td").addClass("report_on");
      })
      $("#mytable tr td").mouseout(function(){
         $(this).parent().children("td").removeClass("report_on");
      })
   });
</script>

</body>
</html>