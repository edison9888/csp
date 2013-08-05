<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>成本中心</title>
</head>
<body style="padding-top:50px" class="span20">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<div width="100%" align="right">
<strong><font color="#870A08" size = "2"><lable>1个成本 = 10的负9次方台机器&nbsp; </lable></font></strong>
</div>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      
      <tr align="center">
      	<th align="center" colspan="9">成本信息</td>
      </tr> 
      
      <tr align="center">
      	<th width="30" align="center">序号</td>
      	<th width="100" align="center">名称</td>
      	<th width="100" align="center">成本类型</td>
        <th width="120" align="center">总机器单位成本</td>
        <th width="150" align="right">提供调用量</td>
        <th width="150" align="center">单次调用直接成本</td>
        <th width="150" align="center">单次调用依赖成本</td>
        <th width="150" align="center">单次调用总成本</td>
      </tr>
      </thead>
      
      <tbody>
      
      <c:forEach items="${costList}" var="cost">
      <tr>
      	<td align="center">${cost.num}</td>
      	<td align="center">
      		<a href="#" onclick='viewSelf("${cost.appName}", "${cost.machineNum}", "${cost.pv}", "${cost.formatPerCost}", "${cost.selfCost}")'>
      			${cost.appName}
      		</a>
      	</td>
      	<td align="center">${cost.costType}</td>
      	<td align="center">${cost.machineNum} + ${cost.indirectMachine}(依赖)</td>
      	<td align="center">${cost.formatPv}</td>
      	<td align="center">${cost.formatPerCost}</td>
      	<td align="center">
      		<a href="#" onclick='viewDep("${cost.appName}", "${cost.pv}")'>
      			${cost.formatDependPerCost}
      		</a>
      	</td>
      	<td align="center">${cost.formatTotalPerCost}</td>
	  </tr>
	  </c:forEach>
	 
 	 </tbody>

</table>
</div>

<script type="text/javascript">
function viewDep(appName, pv) {
	var url = "show.do?method=showCapacityCostDep&appName=" + appName + "&pv=" + pv;
	window.open (url, '_blank')
}

function viewSelf(appName, machineNum, pv, perCost, selfCost) {
	var url = "show.do?method=showCapacityCostSelf&appName=" + appName + "&pv=" + pv + "&machineNum=" + machineNum + "&perCost=" + perCost + "&selfCost=" + selfCost;
	window.open (url, '_blank')
}


$("#mytable tr:eq(0)").find("th:eq(0)").css("background-color", "#ECECEC");
$("#mytable tr:eq(1)").find("th:eq(0),th:eq(1),th:eq(2),th:eq(3),th:eq(4),th:eq(5),th:eq(6),th:eq(7)").css("background-color", "#ECECEC");

$(function(){
      $("#mytable tr td").mouseover(function(){
         $(this).parent().children("td").addClass("report_on");
      })
      $("#mytable tr td").mouseout(function(){
         $(this).parent().children("td").removeClass("report_on");
      })
   });

$(document).ready(function() { 
   $("#mytable").fixedtableheader({ 
   　　headerrowsize:2
   }); 
});

</script>
</body>
</html>