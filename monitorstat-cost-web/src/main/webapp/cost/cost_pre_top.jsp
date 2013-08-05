<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/highcharts.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<title>成本中心</title>
</head>
<body style="padding-top:50px" class="span20">
<%@ include file="../../top.jsp" %>

 <div class="row" style="margin-bottom:15px;">
			<div class="span3" style="padding-top:4px;">日期(成本排行精确到月份):</div>
			<div class="span4" style="margin-right:12px;">
				
				<form action="<%=request.getContextPath() %>/appCost.do" method="POST">
				<input type="text" class="myself" id="selectDate" value="${dateStr}"	
					style="width:110px;"  name="dateStr" 
						 onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM'})" />
  
				<input type="hidden" name="method" value="showPreTop"/>	 

				<input type="submit" value="查询"  style="width:60px;" class="btn btn-success">
				</form>
			</div>
	</div>
	
	<div class="span20">
	
	<table class="bordered-table"  id="mytable" >
	      <thead align="center">
		      <tr align="center">
			      <th align="center" colspan="9">每千次调用成本排行</th>
		      </tr> 
		      
		      <tr align="center">
		      	<th width="30" align="center">序号</td>
		      	<th width="100" align="center">应用</td>
		      	<th width="100" align="center">产品线</td>
		      	<th width="100" align="center">每千次调用成本</th>
		      	<th width="100" align="center">每日平均成本</th>
                <th width="100" align="center">每日平均调用次数</th>
		      </tr>
	      </thead>
	      
	      <tbody>
		      <c:forEach items="${topInfos}" var="tinfo" varStatus="status">
		      <tr>
		      	<td align="center">${status.index+1}</td>
		      	<td align="center">${tinfo.sName}</td>
		      	<td align="center">${tinfo.pName}</td>
		      	<td align="center">${tinfo.prePreCostStr}￥</td>
		      	<td align="center">${tinfo.preCostAllStr}￥</td>
                <td align="center">${tinfo.preCallNumStr}</td>
			  </tr>
			  </c:forEach>
	 	 </tbody>
	
	</table>
	</div>
	
	<script type="text/javascript">
	
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
	
	</script>
</body>
</html>