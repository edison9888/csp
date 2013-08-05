<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">



<%@page import="java.util.Calendar"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.csp.capacity.po.CapacityRankingPo"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
<title>容量规划</title>
</head>
<body>
<%


List<CapacityRankingPo> ulist = (List<CapacityRankingPo>)request.getAttribute("list");
Object year = request.getAttribute("year");
DateFormat df = new SimpleDateFormat("yyyy-MM");
%>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
    <tr>
      <td> </td>
    </tr>
    <tr class="headcon">
        <td colspan="12" align="center">服务器数量预测</td>
    </tr>
	<tr>
    <td><table  class="datalist"  width="1000">
      <tr class="ui-widget-header ">
      	<td>排名</td>
        <td>应用名</td>
        <td>当前机器数</td>
        <td>机器使用率</td>
        <%
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -2);
        for(int m =0 ;m<12;m++){ 
        %>
        <td ><%=df.format(cal.getTime())%></td>
        <%cal.add(Calendar.MONTH, 1); } %>
		<td >详细</td>
      </tr>
      <%
     
      for(int i=0;i<ulist.size();i++){
    	  Calendar cal1 = Calendar.getInstance();
    	  cal1.add(Calendar.DAY_OF_MONTH, -2);
    	  CapacityRankingPo po = ulist.get(i);
    %>
     <tr class="z">
         <td><%=i+1 %></td>
         <td><a href="app_machine_detail.jsp?year=<%=year %>&appId=<%=po.getAppId() %>"><%=po.getAppName() %></a></td>
		 <td><%=po.getFeatureData("当前机器数")%></td>
		 <td><%=String.format("%.2f",Arith.mul(po.getCData(),100)) %>%</td>
		 <%for(int m =0 ;m<12;m++){ %>
       	<td><%=po.getFeatureData(df.format(cal1.getTime()))%></td>
       	 <%cal1.add(Calendar.MONTH, 1); } %>
      <td><a href="app_machine_detail.jsp?year=<%=year %>&appId=<%=po.getAppId() %>">详情</a></td>
      </tr><%}%>
     
    </table></td>
	</tr>	
	<tr>
      <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
    </tr>
    <tr><td></td></tr>
</table>
</body>
</html>