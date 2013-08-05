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
 
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      
      <tr align="center">
      	<th align="center" colspan="3">成本因子信息</td>
      </tr> 
      
      <tr align="center">
      	<th width="400" align="center">名称</td>
      	<th width="200" align="center">机器成本因子</td>
        <th width="100" align="center">操作</td>
      </tr>
      </thead>
      
      <tbody>
      
      <c:forEach items="${list}" var="costRatio">
      <tr>
      	<td align="center">${costRatio.appName}</td>
      	<td align="center">${costRatio.ratio}</td>
      	<td align="center">
      		<a href="#" onclick='alterCostRatio("${costRatio.appName}")'>
      			修改成本因子
      		</a>
      	</td>
	  </tr>
	  </c:forEach>
	 
 	 </tbody>

</table>
</div>

<script type="text/javascript">

function alterCostRatio(appName) {
	var ratio = window.prompt("请输入新的成本因子");
	if (ratio == null) {
		return;
	}
	var patrn=/^\d{1,10}(\.\d{1,2})?$/; 
	if (!patrn.exec(ratio)) {
		alert("成本因子需为整数或不超过两位小数位的小数");
		return;
	}
	var urlDest = "./capacity/manage.do?method=alterCapacityCostRatio";
	var parameters = "appName=" + appName + "&ratio=" + ratio;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    			window.location.reload();
    			
    	}
   	});
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