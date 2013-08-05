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
			<td><%=appInfo.getOpsName()%>��������Ϣ:</td>
			<td>������:<a href="">${hostNumber}</a>&nbsp;&nbsp;��ǰ���Ӽ�Ⱥ��������<a href="">${currentPv}</a>&nbsp;&nbsp;��Ⱥ������<a href="">${dAbility}</a></td>
		</tr>
		</table>
		<%
			if (appInfo.getAppType().equals("pv")) {
				List<TimeDataInfo> sourceList = table.getSourceList();
		%>
		<h5>
			����URL(top<%=showCount%>) 
			<a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=gotoSourceDetail&appId=<%=appInfo.getAppId()%>">��ϸ</a>
		</h5>
		<table width="100%" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<td>URL</td>
					<td>����</td>
					<td>200</td>
					<td>302</td>
					<td>��Ӧʱ��</td>
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
			} else {	//���Ӧ��
				List<TimeDataInfo> dependMeAppList = table.getDependMeAppList();
				if(dependMeAppList != null) {
				%>
				<h5>
					�����ҵ�Ӧ��(top<%=showCount%>)
					<a href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfRefer&appId=<%=appInfo.getAppId()%>">��ϸ</a>
				</h5>
				<table width="100%" class="table table-striped table-bordered table-condensed">
					<thead>
						<tr>
							<td>Ӧ������</td>
							<td>������</td>
							<td>����</td>
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
				��������Ӧ��(top<%=showCount%>)���ܵ�����(${consumePv})
				<a href="<%=request.getContextPath()%>/app/detail/hsf/consumer/show.do?method=gotohsfConsumer&appId=<%=appInfo.getAppId()%>">��ϸ</a>
			</h5>
			<table width="100%" class="table table-striped table-bordered table-condensed">
				<thead>
					<tr>
						<td>��ԴӦ��</td>
						<td>������</td>
						<td>����</td>
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
		out.println("Sorry,��ѯӦ�ò�����!");
	}
%>
