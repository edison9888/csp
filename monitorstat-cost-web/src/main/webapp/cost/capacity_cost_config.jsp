<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<title>成本中心</title>
</head>
<body style="padding-top:50px" class="span20">
<%@ include file="../../top.jsp" %>
 
<div class="span20">
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th align="center" colspan="6">成本信息</td>
      </tr> 
      <tr align="center">
      	<th>分组</td>
      	<th>类型</td>
      	<th>机器数</td>
        <th>应用名</td>
        <th>分摊机器数</td>
        <th>序号</td>
      </tr>
      </thead>
      
      <tbody>
      
      <c:forEach items="${costList}" var="cost">
      <tr>
      	<td align="center">${cost.configGroup}</td>
      	<td align="center">${cost.depType}</td>
      	<td align="center">${cost.groupMachineNum}</td>
      	<td align="center">${cost.appName}</td>
      	<td align="center">${cost.formatPerMachine}</td>
      	<td align="center">${cost.num}</td>
	  </tr>
	  </c:forEach>
	 
 	 </tbody>

</table>
</div>

<script type="text/javascript">
	var tab=document.getElementById("mytable"); 
    var name=""; 
    for(var i=0,j=0;i <tab.rows.length;i++,j++) { 
        if(name==tab.rows[i].cells[0].innerHTML) { 
            tab.rows[i].deleteCell(0); 
        } else  { 
            name=tab.rows[i].cells[0].innerHTML; 
            if(i>0)
            {
            	tab.rows[i-j].cells[0].rowSpan=j; 
            }
            j=0; 
        } 
        
        if ( i==tab.rows.length-1) {
        	tab.rows[i-j].cells[0].rowSpan=j+1;
        }
    }  
    
    
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