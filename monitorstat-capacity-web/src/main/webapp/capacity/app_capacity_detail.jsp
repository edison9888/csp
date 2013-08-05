<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<%@page import="java.util.Map"%>
<%@page import="java.util.Map.Entry"%>
<title>容量规划</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../top.jsp" %>

<table class="nobordered-table"  id="mytable">

	<tr  class="headcon">
 	  <td align="center">${appName}各机房容量状况<!--<a href="<%=request.getContextPath() %>/show.do?method=showCapacityMore" style="float:right">返回首页</a>--></td>
 	</tr>
	<tr>
      <td>
      <table width="1000" class="datalist" >
        <tr class="ui-widget-header">
      	  <td>机房</td>
          <td>流量比例</td>
          <td>当前QPS</td>
          <td>30天内容最大QPS</td>
          <td>可以支撑安全QPS</td>
          <td>压测QPS</td>
          <td>目前机器数</td>
          <td>安全机器数</td>
          <td>容量剩余率</td>
        </tr>
    	<c:forEach items="${capacitys}" var="capacity">
        <tr>
      	  <td>${capacity.siteName}</td>
      	  <td>${capacity.pvRate}%</td>
      	  <td>${capacity.recentlyQps}</td>
      	  <td>${capacity.maxQps}</td>
      	  <td>${capacity.safeQps}</td>
      	  <td>${capacity.loadQps}</td>
      	  <td>${capacity.machineNumber}</td>
      	  <td>${capacity.safeMachineNumber}</td>
          <td>${capacity.capacityRate}%</td>
		  </tr>
	     </c:forEach>
	    
	    <tr>
      		<td colspan="9" align="left" id="capacityDepend">
        		<div id="capacityTabs">
					<ul>
						<li><a href="#tabs-c1">容量依赖图</a></li>
						<li><a href="#tabs-c2">容量依赖表格</a></li>
					</ul>
	    			<div id="tabs-c1">	
	    				
					</div>
					<div id="tabs-c2">	
					<table>
						<tr>
							<td>依赖应用名</td>
							<c:forEach items="${sites}" var="site">
								<td>依赖${site}(QPS)</td>
							</c:forEach>
         					<td>平均依赖(QPS)</td>
          					<td>依赖占比(QPS)</td>
          					<td>依赖影响度</td>
						</tr>
						
						<c:forEach items="${allDep}" var="alldep">
						<tr>
							<td>${alldep.appName}</td>
							<c:forEach items="${sites}" var="site">
								<td>${alldep.roomQps[site]}</td>
							</c:forEach>
         					<td>${alldep.averageQps}</td>
         					<td>${alldep.ratio}%</td>
         					<td>${alldep.influence}%</td>
						</tr>
						</c:forEach>
						
					</table>	
					</div>
				</div>
	  		</td>
		</tr>
      
      </table>
      </td>
      </tr>
	
      <tr>
      	<td>
      		
      		<c:forEach items="${groupCapacityMap}" var="groupCapacity">
	      		<table  width="1000" class="datalist">
				  <tr>
				    <td colspan="9" align="left" class="headcon">分组:${groupName[groupCapacity.key]}</td>
				  </tr>
				   <tr class="ui-widget-header ">
				
				      	  <td>机房</td>
				
				          <td>流量比例</td>
				
				          <td>当前QPS</td>
				          <td>30天内容最大QPS</td>
				
				          <td>可以支撑安全QPS</td>
				
				          <td>压测QPS</td>
				
				          <td>目前机器数</td>
				
				          <td>安全机器数</td>
				
				          <td>容量剩余率</td>
				     </tr>
				      <c:forEach items="${groupCapacity.value}" var="capacity">
				        <tr>
				      	  <td>${capacity.value.siteName}</td>
				      	  <td>${capacity.value.pvRate}%</td>
				      	  <td>${capacity.value.recentlyQps}</td>
				      	   <td>${capacity.value.maxQps}</td>
				      	  <td>${capacity.value.safeQps}</td>
				      	  <td>${capacity.value.loadQps}</td>
				      	  <td>${capacity.value.machineNumber}</td>
				      	  <td>${capacity.value.safeMachineNumber}</td>
				          <td>${capacity.value.capacityRate}%</td>
						  </tr>
					   </c:forEach>
				
				<tr>
      				<td colspan="9" align="left">
        				<div class="groupCapacityTabs">
							<ul>
								<li><a href="#tabs-c1${groupCapacity.key}">容量依赖图</a></li>
								<li><a href="#tabs-c2${groupCapacity.key}">容量依赖表格</a></li>
							</ul>
	    					<div id="tabs-c1${groupCapacity.key}">	
	
							</div>
							<div id="tabs-c2${groupCapacity.key}">	
							
							<table>
								<tr class="ui-widget-header">
									<td>依赖应用名</td>
									<c:forEach items="${sites}" var="site">
										<td>依赖${site}(QPS)</td>
									</c:forEach>
         							<td>平均依赖(QPS)</td>
          							<td>依赖占比(QPS)</td>
          							<td>依赖影响度</td>
								</tr>
						
								<c:forEach items="${groupdep[groupCapacity.key]}" var="groupDisplayObject">
								<tr>
									<td>${groupDisplayObject.appName}</td>
									<c:forEach items="${sites}" var="site">
										<td>${groupDisplayObject.roomQps[site]}</td>
									</c:forEach>
         							<td>${groupDisplayObject.averageQps}</td>
         							<td>${groupDisplayObject.ratio}%</td>
         							<td>${groupDisplayObject.influence}%</td>
								</tr>
								</c:forEach>
						
							</table>	
								
							</div>
						</div>
	  				</td>
		 		</tr>
			</table>
      		</c:forEach>
      	</td>
      </tr>
      
    <tr>
      <td colspan="2" align="left" id="featrueFlash">
        <div id="tabs">
			<ul>
					<li><a href="#tabs-1">日流量</a></li>
					<!--<li><a href="#tabs-1_feature">容量预测</a></li>-->
			</ul>
	    <div id="tabs-1">		
		</div>
		<div id="tabs-1_feature">		
		</div>
		</div>
 	
	  </td>
	</tr>

</table>
 
<script type="text/javascript">

 $(function() {
	$("#tabs").tabs();
	$("#capacityTabs").tabs();
	$(".groupCapacityTabs").tabs();
}); 



var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline1", "100%", "400", "8", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline1");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/capacity/amchart/day_pv_settings.xml");
so1.addVariable("data_file", escape('<%=request.getContextPath() %>/show.do?method=forecastDayPv&appId=${appId}&year=${year}'));
so1.write("tabs-1");

var soAll = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/ampie.swf", "ampie", "800", "300", "8", "#FFFFFF");
soAll.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
soAll.addVariable("chart_id", "ampie");   
soAll.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/pie_settings.xml");
soAll.addVariable("chart_data", encodeURIComponent("${allPie}"));
soAll.write("tabs-c1");

<% 
	Map<String, String> groupPie = (Map<String, String>)request.getAttribute("groupPie");
	for (Map.Entry<String, String> entry : groupPie.entrySet()) {  
	%>
		var soGroup<%=entry.getKey()%> = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/ampie.swf", "ampie", "800", "300", "8", "#FFFFFF");
		soGroup<%=entry.getKey()%>.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
		soGroup<%=entry.getKey()%>.addVariable("chart_id", "ampie<%=entry.getKey()%>");   
		soGroup<%=entry.getKey()%>.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/pie_settings.xml");
		soGroup<%=entry.getKey()%>.addVariable("chart_data", encodeURIComponent("<%=entry.getValue() %>"));
		var tapId = "tabs-c1" + "<%=entry.getKey()%>";
		soGroup<%=entry.getKey()%>.write(tapId);
		
		
	<%} %>

/**
soAll.addVariable("data_file", encodeURIComponent("<pie> <slice title='ddd'>3</slice></pie>"));
**/



</script>

</body>
</html>