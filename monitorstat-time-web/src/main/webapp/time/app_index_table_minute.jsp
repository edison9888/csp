<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import=" com.taobao.csp.time.web.po.IndexMinuteTableMain"%>
<%@page import=" com.taobao.csp.time.web.po.IndexMinuteTable"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK" isELIgnored="false"%>

<%
	Map<String, IndexMinuteTableMain> resultMap = (Map<String, IndexMinuteTableMain>)request.getAttribute("resultMap");
	String error = (String)request.getAttribute("error");
	if(error == null && resultMap != null) {
		for(String groupName : resultMap.keySet()) {
			IndexMinuteTableMain indexMain = resultMap.get(groupName);
			out.println("<div class='row-fluid'>");
			List<IndexMinuteTable> indexSumList = indexMain.getList();
			%>
				<table width="100%"   class="table table-striped table-bordered table-condensed">
					<tr>
						<td><%=indexMain.getAppName() + "-" + groupName%>��������Ϣ:</td>
						<td>������:<%=indexMain.getMachines()%>&nbsp;&nbsp;��������QPS��<%=indexMain.getCapcityQps()%>&nbsp;&nbsp;��Ⱥ����QPS��<%=indexMain.getCapcityQps()*indexMain.getMachines()%>
							</td>
					</tr>
				</table>
				<table width="100%" class="table table-striped table-bordered table-condensed">
					<tr>
						<td>ʱ��</td>
						<td>���ô���(��PV)</td>
						<td>200����</td>
						<td>����QPS</td>
						<td>RT</td>
						<td>Load</td>
						<td>CPU</td>
						<td>�ڴ�</td>
						<td>GC</td>
					</tr>
			<%
			for(IndexMinuteTable table:indexSumList) {
			%>
					<tr>
						<td><%=table.getFtime()%></td>
						<td><%=table.getPv()%></td>
						<td><%=table.getPv200()%></td>
						<td><%=table.getHostQps() + table.getHostQpsRate()%></td>
						<td><%=table.getRt() + table.getRtRate()%></td>
						<td><%=table.getLoad() + table.getLoadRate()%></td>
						<td><%=table.getCpu() + table.getCpuRate()%></td>
						<td><%=table.getMemory() + table.getMemoryRate()%></td>
						<td><%=table.getGc() + table.getGcRate()%></td>
					</tr>				
			<%
			}
			%>
			</table>
			<hr/>
			<%
		}
		out.println("</div>");
	} else {
		out.println("Ӧ�ò����ڣ�");
	}
%>

