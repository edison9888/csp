<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<title>����ɱ�����</title>

</head>
<body style="padding-top:50px" class="span20">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<strong><font color="#870A08" size = "2"><lable> ${appName}������Ϣ��1���ɱ� = 10�ĸ�9�η�̨������ </lable></font></strong>
<br />
<table class="bordered-table"  id="aaaa" >
	<tr>
		<th width="250">����</th>
		<th width="250">${appName}</th>
		<th width="250">������</th>
		<th width="250">${machineNum}</th>
	</tr>
	<tr>
		<th width="250">����ɱ�</th>
		<th width="250">${selfCost}</th>
		<th width="250">�����ܵ�����</th>
		<th width="250">${pv}</th>
	</tr>
	<tr>
		<th width="250">�����ε���ֱ�ӳɱ�</th>
		<th width="250">${perCost}</th>
		<th width="250"></th>
		<th width="250"></th>
	</tr>
</table>
<strong><font color="#870A08" size = "2"><lable> ����${appName}����Ϣ </lable></font></strong>
<br/>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th width="100" align="center">����</td>
      	<th width="100" align="center">��������</td>
        <th width="150" align="right">����������</td>
        <th width="150" align="center">�����ɱ�</td>
      </tr>
      </thead>
      
      <tbody>
      
      <c:forEach items="${costDepList}" var="dep">
      <tr>
      	<td align="center">${dep.appName}</td>
      	<td align="center">${dep.depType}</td>
      	<td align="center">${dep.formatDepPv}</td>
      	<td align="center">${dep.formatDepCost}</td>
	  </tr>
	  </c:forEach>
	 
 	 </tbody>

</table>
</div>

<script type="text/javascript">

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