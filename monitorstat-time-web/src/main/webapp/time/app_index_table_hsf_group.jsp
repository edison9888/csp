<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.time.web.po.IndexHSfTable"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>

<%
Map<String, IndexHSfTable> timeMap =(Map<String, IndexHSfTable>)request.getAttribute("timeMap");
List<String> timeList  = (List<String>)request.getAttribute("timeList");

%>
	<div class="row-fluid"  >
		<h5>����:${appInfo.appName}����Ⱥ������:${machineCount}</h5>
		<table width="100%"   class="table table-striped table-bordered table-condensed" >
			<tr>
				<td>ʱ��</td>
				<td>ÿ̨PV</td>
				<td>������PV</td>
				<td title="(SS_Block)">block</td>
				<td>QPS</td>
				<td>RT</td>
				<td>Load</td>
				<td>CPU</td>
				<td>�ڴ�</td>
				<td>GC</td>
			</tr>
			<%
			for(String time:timeList){
			%>
			<tr>
				<td><%=time%></td>
				<%
						IndexHSfTable hsf = timeMap.get(time);
						if(hsf != null){
					%>
					<td><%=hsf.getPv()%></td>
					<td><%=hsf.getPvForSite()%></td>
					<td><%=hsf.getSsBlock()%></td>
					<td><%=hsf.getQps()%></td>
					<td><%=hsf.getRt()%></td>
					<td><%=hsf.getLoad()%></td>
					<td><%=hsf.getCpu()%>%</td>
					<td><%=hsf.getMem()%>%</td>
					<td><%=hsf.getGc()%></td>					
					<%		
						}else{
							%>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>
							<td> - </td>	
							<%				
						}
				%>
			</tr>
			<%} %>
		</table>
	</div>
