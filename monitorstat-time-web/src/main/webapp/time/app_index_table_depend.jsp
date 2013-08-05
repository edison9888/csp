<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.time.web.po.IndexDependTable"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK" isELIgnored="false"%>

<%
	Integer showCount = (Integer) request.getAttribute("showCount");
	if (showCount == null)
		showCount = 5;
	AppInfoPo appInfo = (AppInfoPo) request.getAttribute("appInfo");
	
	IndexDependTable table =  (IndexDependTable) request.getAttribute("table");
	if (appInfo != null && table != null) {
%>
	<div class="row-fluid" >
		<table width="100%"   class="table table-striped table-bordered table-condensed">
		<tr>
			<td><%=appInfo.getOpsName()%>的容量信息:</td>
			<td>机器数:<a href="">${hostNumber}</a>&nbsp;&nbsp;当前分钟集群调用量：<a href="">${currentPv}</a>&nbsp;&nbsp;集群能力：<a href="">${dAbility}</a></td>
		</tr>
		</table>
		<%
			if (appInfo.getAppType().equals("pv")) {
				List<TimeDataInfo> sourceList = table.getSourceList();
		%>
		<h5>
			自身URL(top<%=showCount%>) 
			<a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=gotoSourceDetail&appId=<%=appInfo.getAppId()%>">详细</a>
		</h5>
		<table width="100%" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<td>URL</td>
					<td>次数</td>
					<td>200</td>
					<td>302</td>
					<td>响应时间</td>
				</tr>
			</thead>
			<tbody id="sourceUrlId">
			<%
			if(sourceList != null) {
				int index = 0;
				for(TimeDataInfo sourceUrlInfo : sourceList) {
					if(index++ < showCount) {
			%>
				<tr>
					<td><%=sourceUrlInfo.getKeyName()%></td>
					<td><%=sourceUrlInfo.getMainValue()%>(<%=sourceUrlInfo.getMainRate()%>%)<%=sourceUrlInfo.getMainValueRate()%></td>
					<td><%=sourceUrlInfo.getOriginalPropertyMap().get("C-200")%></td>
					<td><%=sourceUrlInfo.getOriginalPropertyMap().get("C-302")%></td>
					<td><%=sourceUrlInfo.getOriginalPropertyMap().get("C-time")%></td>
				</tr>			
			<%
					}
				}
			}
			%>
			</tbody>
		</table>
		<%
			} else {	//后端应用
				List<TimeDataInfo> dependMeAppList = table.getDependMeAppList();
				if(dependMeAppList != null) {
				%>
				<h5>
					依赖我的应用(top<%=showCount%>)
					<a href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfRefer&appId=<%=appInfo.getAppId()%>">详细</a>
				</h5>
				<table width="100%" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<td>应用名称</td>
							<td>调用量</td>
							<td>比例</td>
						</tr>
					</thead>
					<tbody id="sourceUrlId">
					<%
						int i=0;
						for(TimeDataInfo sourceUrlInfo : dependMeAppList) {
							if(i<showCount)
								i++;
							else 
								break;
					%>
						<tr>
							<td><%=sourceUrlInfo.getKeyName()%></td>
							<td><%=sourceUrlInfo.getMainValue() + sourceUrlInfo.getMainValueRate()%></td>
							<td><%=sourceUrlInfo.getMainRate()%>%</td>
						</tr>			
					<%
						}
					%>
					</tbody>
				</table>				
				<%
				}
			}
			List<TimeDataInfo> meDependAppList = table.getMeDependAppList();
			if(meDependAppList != null && meDependAppList.size() != 0) {
				int i=0;
			%>
			<h5>
				我依赖的应用(top<%=showCount%>)，总调用量(${consumePv})
				<a href="<%=request.getContextPath()%>/app/detail/hsf/consumer/show.do?method=gotohsfConsumer&appId=<%=appInfo.getAppId()%>">详细</a>
			</h5>
			<table width="100%" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<td>来源应用</td>
						<td>调用量</td>
						<td>比例</td>
					</tr>
				</thead>
				<tbody id="sourceUrlId">
				<%
					for(TimeDataInfo sourceUrlInfo : meDependAppList) {
						if(i<showCount)
							i++;
						else 
							break;
				%>
					<tr>
						<td><%=sourceUrlInfo.getKeyName()%></td>
						<td><%=sourceUrlInfo.getMainValue() + sourceUrlInfo.getMainValueRate()%></td>
						<td><%=sourceUrlInfo.getMainRate()%>%</td>
					</tr>			
				<%
					}
				%>
				</tbody>
			</table>				
			<%
			}			
		%>		
	</div>
<%
	} else {
		out.println("Sorry,查询应用不存在!");
	}
%>
