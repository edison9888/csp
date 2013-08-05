<%@page import="com.taobao.csp.depend.po.tair.CTairActionTotalUnit"%>
<%@page import="com.taobao.csp.depend.po.tair.CTairMachineRoomUnit"%>
<%@page import="com.taobao.csp.depend.po.tair.CTairNameSpaceUnit"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=GB18030"  isELIgnored="false" 
    pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/userjs/mainpage.js"></script>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap-responsive.css" rel="stylesheet"/>
<title>调用Tair的机器分布统计</title>
<%
String opsName = (String)request.getAttribute("opsName");
String tairgroupname = (String)request.getAttribute("tairgroupname");
Map<String,CTairActionTotalUnit> totalMap = (Map<String, CTairActionTotalUnit>)request.getAttribute("totalMap");
%>
<style type="text/css">
h2{
	padding-left: 50px;
}
thead{
	background: #fff;
	color: #4f6b72;
}
</style>
</head>
<body>
<H4 align="center">应用<%=opsName%>调用Tair的数据统计</H4>
<H2 align="left">tairgroupname：<%=tairgroupname%></H2>
<H2 align="left">查询时间：${selectDate}</H2>
<H2 align="left">对比时间：${selectPreDate}</H2>
<div class="container">
		
	
<%
	if(totalMap != null) {
		for(CTairActionTotalUnit po: totalMap.values()) {
			%>
			<div class="row">
				<div class="span12"><table width="100%" class="table table-striped table-bordered table-condensed" >
							<tr>
								<td>操作</td>
								<td >调用量</td>
								<td >均值（毫秒/次数）</td>
								<td >长度（字节/次数）</td>
								<td >命中率</td>
							</tr>
							<tr>
								<td style="color: red;" class="firstTitleTd"><%=po.getActionShortName()%></td>
								<td ><%=Utlitites.fromatLong(po.getCallNumberArray()[0] + "")%>（<%=Utlitites.scale(po.getCallNumberArray()[0] + "", po.getPreCallNumberArray()[0] + "")%>）</td>
								<td ><%=po.getAvgRate()%>（<%=Utlitites.scale(po.getAvgRate() + "", po.getPreAvgrate() + "")%>）</td>
								<td ><%=po.getAvgLength()%>（<%=Utlitites.scale(po.getAvgLength() + "", po.getPreAvgLength() + "")%>）</td>
								<td ><%=Arith.mul(po.getAvgHit(), 100)%>%（<%=Utlitites.scale(po.getAvgHit() + "", po.getPreAvgHit() + "")%>）</td>
							</tr>
						</table></div>
			</div><!-- row -->
		
		
		

				
			<div class="row">
				
			
				<%
				for(CTairMachineRoomUnit machine: po.machineMap.values()) {
					%>
					<div class="span<%=po.machineMap.values().size()>0?12/po.machineMap.values().size():12 %>">
						<table width="100%" class="table table-striped table-bordered table-condensed" >
							<thead>
								<tr>
									<td colspan="5" class="secondTitleTd">
									<b><%=machine.getMachineRoomName()%></b>
									（<%=Utlitites.fromatLong(machine.callNumberArray[0] + "")%><%=Utlitites.scale(machine.callNumberArray[0] + "", machine.preCallNumberArray[0] + "")%>,
									<%=Arith.mul(Arith.div(machine.callNumberArray[0], po.getCallNumberArray()[0], 4), 100)%> %）
									</td>
								</tr>
							</thead>
							<tr>
								<td class="secondTitleTd">命名空间</td>
								<td class="secondTitleTd">次数</td>
								<td class="secondTitleTd">均值（毫秒/次数）</td>
								<td class="secondTitleTd">长度（字节/次数）</td>
								<td class="secondTitleTd">命中率（%）</td>
							</tr>
							<% 
								for(CTairNameSpaceUnit spaceUnit: machine.namespaceMap.values()) {
								%>
									<tr>
										<td>
										<a href="javascript:openWin('<%=request.getContextPath()%>/show/tairconsume.do?method=showMachine&opsName=${opsName}&selectDate=${selectDate}&tairgroupname=${tairgroupname}&namespace=<%=spaceUnit.getNamespace()%>&actiontype=<%=po.getActionShortName()%>',700,1100)"><%=spaceUnit.getNamespace()%></a>
										</td>									
										<td><%=Utlitites.fromatLong(spaceUnit.getCallNumberArray()[0] + "")%>（<%=Utlitites.scale(spaceUnit.getCallNumberArray()[0] + "", spaceUnit.getPreCallNumberArray()[0] + "")%>）</td>									
										<td><%=spaceUnit.getAvgRate()%>（<%=Utlitites.scale(spaceUnit.getAvgRate() + "", spaceUnit.getPreAvgrate() + "")%>）</td>									
										<td><%=spaceUnit.getAvgLength()%>（<%=Utlitites.scale(spaceUnit.getAvgLength() + "", spaceUnit.getPreAvgLength() + "")%>）</td>									
										<td><%=Arith.mul(spaceUnit.getAvgHit(), 100)%>%（<%=Utlitites.scale(spaceUnit.getAvgHit() + "", spaceUnit.getPreAvgHit() + "")%>）</td>									
									</tr>	
								<%
								}
							
							%>
						</table>
					</div>
						
					<%						
				}		
				%>
		</div><!-- row -->
			<%	
		}		
	} else {
		%>
		<h4>传来的参数totalMap为null，请查看是否正确。</h4>		
		<%
	}

%>
	</div><!-- container -->
</body>
</html>