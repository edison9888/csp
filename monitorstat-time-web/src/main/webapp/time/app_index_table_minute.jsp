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
						<td><%=indexMain.getAppName() + "-" + groupName%>的容量信息:</td>
						<td>机器数:<%=indexMain.getMachines()%>&nbsp;&nbsp;单机容量QPS：<%=indexMain.getCapcityQps()%>&nbsp;&nbsp;集群容量QPS：<%=indexMain.getCapcityQps()*indexMain.getMachines()%>
							</td>
					</tr>
				</table>
				<table width="100%" class="table table-striped table-bordered table-condensed">
					<tr>
						<td>时间</td>
						<td>调用次数(或PV)</td>
						<td>200流量</td>
						<td>单机QPS</td>
						<td>RT</td>
						<td>Load</td>
						<td>CPU</td>
						<td>内存</td>
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
		out.println("应用不存在！");
	}
%>

