<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityRankingPo"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.csp.capacity.NumberUtil"%>
<html>
<head>

</head>
<body>

<%

List<CapacityRankingPo> limitList = new ArrayList<CapacityRankingPo>();

Object obj = request.getAttribute("list");
Object year = request.getAttribute("year");
if(obj != null){
	limitList = (List<CapacityRankingPo>)obj;
}

%>
<table align="center">
	<tr>
	 	<font color="#972630" size="6">
			<td colspan="15"> CSP容量信息 </td>
		</font>
	</tr>
</table>
<table  id="mytable"   border="1" width="1200">
	<thead align="center">
      <tr>
      	<!--<th rowspan="2">排名</th>-->
        <th colspan="2">应用</th>
        <th colspan="4">QPS</th>
        <th colspan="4">PV</th>
        <th colspan="3">安全机器数</th>    
		<th colspan="2">容量剩余率</th>          
      </tr>
	  <tr>
	  	<th>名称</th>
        <th>类型</th>
      	<th>最近</th>
        <th>30天最高</th>
        <th>安全</th>
        <th>极限</th>
        <th>业务</th>
        <th>日志</th>
        <th>30天最高</th>
        <th>极限</th>
        <th>总</th>
        <th>最近</th>  
		<th>30天最多</th>    
        <th>最近</th>         
        <th>30天最低</th>
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
      	<td><a href="http://capacity.taobao.net:9999/capacity/show.do?method=showCapacityDetail&year=<%=year %>&appId=<%=po.getAppId() %>">
      	<%=po.getAppName() %></a></td>
      	<td><%=po.getAppType() %></td> 
      	<td><%=po.getFeatureData("单台线上QPS") %></td>
      		<td><%=po.getFeatureData("本月最高平均QPS") %></td>     
      	<td><%=po.getFeatureData("单台安全QPS") %></td>
      	<td><a href="http://autoload.taobao.net:9999/autoload/loadrun/show.do?method=show&appId=<%=po.getAppId()%>&collectTime=" target="_blank"><%=po.getFeatureData("压测QPS") %></a></td>
      	<td><%=NumberUtil.fromatNumber(po.getFeatureData("业务PV")) %></td>
      	<td><%=NumberUtil.fromatNumber(po.getFeatureData("日志PV")) %>(<%if("center".equals(po.getAppType())){out.print("hsf");}else{out.print("apache");} %>)</td>
      	<td><%=NumberUtil.fromatNumber(po.getFeatureData("日志PV-max")) %>(<%if("center".equals(po.getAppType())){out.print("hsf");}else{out.print("apache");} %>)</td>
      	<td><%=NumberUtil.fromatNumber(po.getFeatureData("全网安全支撑PV")) %></td>
        <td><%=po.getFeatureData("机器数") %></td>
        <td><%=po.getFeatureData("安全机器数") %></td>
        <td><%=po.getFeatureData("安全机器数-max") %></td>
        <td><%=po.getFeatureData("容量剩余率") %>%</td>
        <td><%=po.getFeatureData("容量剩余率-max") %>%</td>
	</tr>
 <%} %>
	</tbody>
</table>

</body>
</html>