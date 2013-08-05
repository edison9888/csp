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
			<td colspan="15"> CSP������Ϣ </td>
		</font>
	</tr>
</table>
<table  id="mytable"   border="1" width="1200">
	<thead align="center">
      <tr>
      	<!--<th rowspan="2">����</th>-->
        <th colspan="2">Ӧ��</th>
        <th colspan="4">QPS</th>
        <th colspan="4">PV</th>
        <th colspan="3">��ȫ������</th>    
		<th colspan="2">����ʣ����</th>          
      </tr>
	  <tr>
	  	<th>����</th>
        <th>����</th>
      	<th>���</th>
        <th>30�����</th>
        <th>��ȫ</th>
        <th>����</th>
        <th>ҵ��</th>
        <th>��־</th>
        <th>30�����</th>
        <th>����</th>
        <th>��</th>
        <th>���</th>  
		<th>30�����</th>    
        <th>���</th>         
        <th>30�����</th>
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
      	<td><%=po.getFeatureData("��̨����QPS") %></td>
      		<td><%=po.getFeatureData("�������ƽ��QPS") %></td>     
      	<td><%=po.getFeatureData("��̨��ȫQPS") %></td>
      	<td><a href="http://autoload.taobao.net:9999/autoload/loadrun/show.do?method=show&appId=<%=po.getAppId()%>&collectTime=" target="_blank"><%=po.getFeatureData("ѹ��QPS") %></a></td>
      	<td><%=NumberUtil.fromatNumber(po.getFeatureData("ҵ��PV")) %></td>
      	<td><%=NumberUtil.fromatNumber(po.getFeatureData("��־PV")) %>(<%if("center".equals(po.getAppType())){out.print("hsf");}else{out.print("apache");} %>)</td>
      	<td><%=NumberUtil.fromatNumber(po.getFeatureData("��־PV-max")) %>(<%if("center".equals(po.getAppType())){out.print("hsf");}else{out.print("apache");} %>)</td>
      	<td><%=NumberUtil.fromatNumber(po.getFeatureData("ȫ����ȫ֧��PV")) %></td>
        <td><%=po.getFeatureData("������") %></td>
        <td><%=po.getFeatureData("��ȫ������") %></td>
        <td><%=po.getFeatureData("��ȫ������-max") %></td>
        <td><%=po.getFeatureData("����ʣ����") %>%</td>
        <td><%=po.getFeatureData("����ʣ����-max") %>%</td>
	</tr>
 <%} %>
	</tbody>
</table>

</body>
</html>