<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityRankingPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@page import="com.taobao.csp.capacity.util.LocalUtil"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">


<title>容量规划</title>


</head>
<body style="padding-top:45px" class="span20">
<%@ include file="../top.jsp" %>

<%

List<CapacityRankingPo> limitList = new ArrayList<CapacityRankingPo>();

Object obj = request.getAttribute("list");
Object year = request.getAttribute("year");
if(obj != null){
	limitList = (List<CapacityRankingPo>)obj;
}

%>
<table class="bordered-table"  id="mytable">
	  <thead align="center">
      <tr>
        <th colspan="4">基础信息</th>
        <th colspan="6">当前容量</th>
        <th colspan="2">容量预测</th>    
        <th colspan="2">其它信息</th>            
      </tr>
	  <tr>
	  	<th>名称</th>
        <th>类型</th>
        <th>机房数</th>
        <th>机器数</th>
        
        <th>标准</th>
        <th>单机负荷</th>
        <th>单机能力</th>
        <th>集群负荷</th>
        <th>集群能力</th>
        <th>水位</th>
        
       <th>需机器数</th>
        <th>机器增减</th>
        
        <th>PE</th>
        <th>操作</th>
        
      </tr>
      </thead>

	  <tbody>
      <%
      for(int i=0;i<limitList.size();i++){
    	  CapacityRankingPo po = limitList.get(i);
    	  int appId = po.getAppId();
      %>
      <tr>
      	<!--<td width="30" align="center"><%=i+1 %></td>-->
      	<td><a href="<%=request.getContextPath() %>/show.do?method=showCapacityDetail&year=<%=year %>&appId=<%=po.getAppId() %>">
      	<%=po.getAppName() %></a></td>
      	<td><%=po.getAppType() %></td> 
      	<td><%=po.getFeatureData("机房数") %></td>
      	<td><%=po.getFeatureData("机器数") %></td>
      	
      	<td><%=po.getFeatureData("容量标准") %></td>
      	<td><a href="" id="single_<%=po.getAppName() %>" rel="popover" 
      		data-content="<table class='table table-striped table-bordered table-condensed'>
      			<tr><td>ip:</td><td><%=po.getFeatureData("采集机器") %></td></tr>
      			<tr><td>time:</td><td><%=po.getFeatureData("采集时间") %></td></tr></table>" data-original-title="">
      	<%=po.getFeatureData("单台负荷") %> </a></td>
      	<td><a href="http://autoload.taobao.net:9999/autoload/loadrun/show.do?method=show&appId=<%=po.getAppId()%>&collectTime=<%=po.getFeatureData("压测时间") %>" target="_blank"><%=po.getFeatureData("单台能力") %></a></td>
      	<td><%=po.getFeatureData("集群负荷") %></td>
      	<td><%=po.getFeatureData("集群能力") %></td>
      	<td><%=po.getFeatureData("容量水位") %></td>
      	
      	<td><%=po.getFeatureData("预测机器数") %></td>
      	<td><%=po.getFeatureData("预测机器增减") %></td>
      	
      	<td><%=LocalUtil.getPe(po.getAppId()) %></td>
      	<td>
      		<a  data-controls-modal="modal-from-dom" data-backdrop="true" 
						data-keyboard="true" 
					href="#"	onclick="getRealTimeCapacityHtml('<%=po.getAppId() %>','<%=po.getAppName() %>');">实时容量</a>
		</td>
      	
	</tr>
 <%} %>
 

	  </tbody>
	  </table>
	  
	  <div id="modal-from-dom" class="modal hide fade" style="background-color:#E7E8EE;">
		<div class="modal-header" style="background-color:#D1EED1;">
			<a href="#" class="close">&times;</a> <b>机器ip地址:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<SPAN id="methodnamediv"></SPAN ></b><br>
		</div>
		<div class="modal-body" style="background-color:#fff;">
			<div id="dataDetail" style="width: 500px; height: 400px;">
			</div>
		</div>
	</div>

<script type="text/javascript">

$("#mytable tr:eq(0)").find("th:eq(0)").css("background-color", "#ECECEC");
$("#mytable tr:eq(1)").find("th:eq(0),th:eq(1),th:eq(2),th:eq(3)").css("background-color", "#ECECEC");

$("#mytable tr:eq(0)").find("th:eq(1)").css("background-color", "#E5E5E5");
$("#mytable tr:eq(1)").find("th:eq(4),th:eq(5),th:eq(6),th:eq(7),th:eq(8),th:eq(9)").css("background-color", "#E5E5E5");

$("#mytable tr:eq(0)").find("th:eq(2)").css("background-color", "#f2eada");
$("#mytable tr:eq(1)").find("th:eq(10),th:eq(11)").css("background-color", "#f2eada");

$("#mytable tr:eq(0)").find("th:eq(3)").css("background-color", "#feeeed");
$("#mytable tr:eq(1)").find("th:eq(12),th:eq(13)").css("background-color", "#feeeed");

$(function(){
      $("#mytable tr td").mouseover(function(){
         $(this).parent().children("td").addClass("report_on");
      })
      $("#mytable tr td").mouseout(function(){
         $(this).parent().children("td").removeClass("report_on");
      })
   })
   
$(document).ready(function() { 
   $("#mytable").fixedtableheader({ 
   　　headerrowsize:2
   }); 
});

$("a[rel=popover]")  .popover({placement:'right'}) .click(function(e) {
     e.preventDefault()
})

function getRealTimeCapacityHtml(appId, appName) {
    var urlDest = "data.do";
    var method = "getRealTimeCapacityHtml";
    var tip = appName + "实时容量信息";
    $.ajax({
      url: urlDest,
      async: true,
      contentType: "application/json",
      cache: false,
      data:{'method':method,'appId':appId,'appName':appName},
      success: function(data) {
         $("#methodnamediv")[0].innerText = appName;
    	$("#dataDetail")[0].innerHTML = data;
      }
    });
 }

</script>

</body>
</html>