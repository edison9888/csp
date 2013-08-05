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
				<input type="hidden" name="level" value="${level}"/>
				<input type="hidden" name="appName" value="${appName}"/>	 
				<input type="hidden" name="method" value="showTop"/>	  
				<input type="hidden" name="requestType" value="post"/>	 


				<input type="submit" value="查询"  style="width:60px;" class="btn btn-success">
				</form>
				<input type='hidden' id="hiddenPieData" value='${jsonInfos}'>
			</div>
	</div>
	
	
	<div id="chartPie">
	</div>
	
	 <c:if test="${( level == 'GROUP_COUNT')}"> 
					<th align="center" colspan="9">一级成本排行</td>
					<input type='hidden' id="hiddenPipeData" value='一级成本排行'>
			</c:if>
			<c:if test="${( level == 'LINE_COUNT')}"> 
					<input type='hidden' id="hiddenPipeData" value='二级成本排行----->${appName}'>
			</c:if>
			<c:if test="${( level == 'ALL')}"> 
					<th align="center" colspan="9">三级成本排行----->${appName}</td>
					<input type='hidden' id="hiddenPipeData" value='三级成本排行----->${appName}'>
			</c:if>

	<div class="span20">
	
	<table class="bordered-table"  id="mytable" >
	      <thead align="center">
	      
	      <tr align="center">
		      <c:if test="${( level == 'GROUP_COUNT')}"> 
					<th align="center" colspan="9">一级成本排行</th>
			</c:if>
			<c:if test="${( level == 'LINE_COUNT')}"> 
			</c:if>
			<c:if test="${( level == 'ALL')}"> 
					<th align="center" colspan="9">三级成本排行----->${appName}</th>
			</c:if>
	      </tr> 
	      
	      <tr align="center">
	      	<th width="30" align="center">序号</td>
	      	<th width="100" align="center">产品线</td>
	      	<th width="100" align="center">平均成本</th>
	      	<th width="100" align="center">下级</td>
	      </tr>
	      </thead>
	      
	      <tbody>
	      
	      <c:forEach items="${topInfos}" var="tinfo" varStatus="status">
	      <tr>
	      	<td align="center">${status.index+1}</td>
	      	<td align="center">${tinfo.sName}</td>
	      	<td align="center">${tinfo.costString}</td>
	      	<td align="center">
			
			  <c:if test="${( level == 'GROUP_COUNT')}"> 
					<a href="<%=request.getContextPath() %>/appCost.do?method=showTop&level=LINE_COUNT&appName=${tinfo.sName}&dateStr=${dateStr}&requestType=1" target="_blank">下级</a>
			</c:if>
			<c:if test="${( level == 'LINE_COUNT')}"> 
					<a href="<%=request.getContextPath() %>/appCost.do?method=showTop&level=ALL&appName=${tinfo.sName}&dateStr=${dateStr}&requestType=1" target="_blank">
					应用</a>
			</c:if>
			<c:if test="${( level == 'ALL')}"> 
					<a href="<%=request.getContextPath() %>/appCost.do?method=showApp&appName=${tinfo.sName}" target="_blank">
					详细</a>
			</c:if>
			
			</td>
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
	
	      var chart;
	      var timeStr=$("#hiddenPipeData").val();
	      var dataStr = document.getElementById("hiddenPieData").value;
	      var costdata=eval(dataStr);
	      $(document).ready(function() {
	          chart = new Highcharts.Chart({
	              chart: {
	                  renderTo: 'chartPie',
	                  plotBackgroundColor: null,
	                  plotBorderWidth: null,
	                  plotShadow: false
	              },
	              title: {
	                  text: timeStr+",成本排行"
	              },
	              tooltip: {
	                  pointFormat: '{series.name}: <b>{point.percentage}%</b>',
	                  percentageDecimals: 1
	              },
	              plotOptions: {
	                  pie: {
	                      allowPointSelect: true,
	                      cursor: 'pointer',
	                      dataLabels: {
	                          enabled: true,
	                          color: '#000000',
	                          connectorColor: '#000000',
	                          formatter: function() {
	                              return '<b>'+ this.point.name +'</b>: '+ Math.round(this.percentage) +' %';
	                          }
	                      }
	                  }
	              },
	              series: [{
	                  type: 'pie',
	                  name: '成本占比',
		             size: '60%',
	                  data: costdata 
	                 
	              }]
	          });
	      });
	
	
	});
	
	</script>
</body>
</html>