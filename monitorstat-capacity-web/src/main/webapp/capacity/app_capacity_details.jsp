<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityRankingPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<%@page import="java.text.SimpleDateFormat"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>应用容量详情</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../top.jsp" %>

<%

List<CapacityRankingPo> list = new ArrayList<CapacityRankingPo>();

Object obj = request.getAttribute("list");
if(obj != null){
	list = (List<CapacityRankingPo>)obj;
}

%>

<table class="bordered-table"  id="detail">  
     <thead align="center">
      <tr align="center">
      	<th style="width:10%">应用名</th>
      	<th style="width:10%">机房数</th>
      	<th style="width:10%">机器数</th>
      	<th style="width:10%">水位标准</th>
      	<th style="width:10%">单机负荷</th>
      	<th style="width:10%">单机能力</th>
      	<th style="width:10%">集群负荷</th>
      	<th style="width:10%">集群能力</th>
      	<th style="width:10%">容量水位</th>
      	<th style="width:10%">计算时间</th>
      </tr>
      </thead>
      
      <tbody>
      <%
      SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
      for(int i=0;i<list.size();i++){
    	  CapacityRankingPo po = list.get(i);
    	  int appId = po.getAppId();
      %>
      <tr>
      	<td><%=po.getAppName() %></td>
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
      	
    	<td><%=sf.format(po.getRankingDate()) %></td>
	</tr>
 	<%} %>

	  </tbody>

</table>

<script type="text/javascript">
	
$("a[rel=popover]")  .popover({placement:'right'}) .click(function(e) {
     e.preventDefault()
})

</script>

</body>
</html>