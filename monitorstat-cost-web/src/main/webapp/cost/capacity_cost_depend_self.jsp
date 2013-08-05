<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<title>自身成本详情</title>

</head>
<body style="padding-top:50px" class="span20">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<strong><font color="#870A08" size = "2"><lable> ${appName}自身信息（1个成本 = 10的负9次方台机器） </lable></font></strong>
<br />
<table class="bordered-table"  id="aaaa" >
	<tr>
		<th width="250">名称</th>
		<th width="250">${appName}</th>
		<th width="250">机器数</th>
		<th width="250">${machineNum}</th>
	</tr>
	<tr>
		<th width="250">自身成本</th>
		<th width="250">${selfCost}</th>
		<th width="250">自身总调用量</th>
		<th width="250">${pv}</th>
	</tr>
	<tr>
		<th width="250">被单次调用直接成本</th>
		<th width="250">${perCost}</th>
		<th width="250"></th>
		<th width="250"></th>
	</tr>
</table>
<strong><font color="#870A08" size = "2"><lable> 调用${appName}的信息 </lable></font></strong>
<br/>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th width="100" align="center">名称</td>
      	<th width="100" align="center">依赖类型</td>
        <th width="150" align="right">依赖调用量</td>
        <th width="150" align="center">依赖成本</td>
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