<%@page import="com.taobao.csp.time.web.po.IndexPvTable"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.csp.time.web.po.IndexHSfTable"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<%
Map<String,Map<String,IndexPvTable>> siteMap =(Map<String,Map<String,IndexPvTable>>)request.getAttribute("siteMap");
List<String> timeList  = (List<String>)request.getAttribute("timeList");

List<String> siteList = new ArrayList<String>();
siteList.addAll(siteMap.keySet());
Collections.sort(siteList);

%>
	<div class="row-fluid" >
			TMD_Block�ռ����赲��Ϣ����:punish,checkcode,hourglass��������ģʽ,TMD��������ģʽ˵�����£�<br/>
			<table class="table table-striped table-bordered table-condensed"  width="100%">
				<tr>
					<td>����</td>
					<td>˵��</td>
					<td>���ؽ��</td>
				</tr>			
				<tr>
					<td>punish</td>
					<td>����BaseQPS,����������������;��hotpatch��������</td>
					<td>��ת���ܾ�ҳ��</td>
				</tr>
				<tr>
					<td>checkcode</td>
					<td>������������</td>
					<td>��ת����֤��ҳ��</td>
				</tr>
				<tr>
					<td>hourglass</td>
					<td>�������QPS����������</td>
					<td>��ת���ȴ�ҳ��</td>
				</tr>								
			</table>
			<hr>	
		<span>${appInfo.opsName}</span>��Block��Ϣ����TMD_Block��SS_Monitor�����赲����������(Tdod Block&nbsp;/&nbsp;SS Block)&nbsp;&nbsp;
		<a href="<%=request.getContextPath()%>/index.do?method=showBlockHost&appId=${appInfo.appId}" target="_blank;">������Ϣ</a>&nbsp;&nbsp;					
		<a href="<%=request.getContextPath()%>/app/detail/history.do?appName=${appInfo.appName}&keyName=PV-Block&method=showHistory" target="_blank;">�鿴�赲��ʷ</a>
			<hr>
		<table width="100%"   class="table table-striped table-bordered table-condensed">
			<tr>
				<td>&nbsp;</td>
				<%for(String site:siteList){ %>
				<td colspan="6" align="center"><%=site%></td>
				<%} %>
			</tr>
			<tr>
				<td>ʱ��</td>
				<%for(String site:siteList){ %>
				<td>��̨ƽ��pv</td>
				<td>QPS</td>
				<td>����PV</td>
				<td>Tdod block</td>
				<td>SS block</td>
				<%} %>
			</tr>
			<%
			for(String time:timeList){
			%>
			<tr>
				<td><%=time %></td>
				<%
				for(String siteName:siteList){
				Map<String,IndexPvTable> site = siteMap.get(siteName);
				if(site!= null){
					IndexPvTable hsf = site.get(time);
					if(hsf != null){
				%>
				<td><%=hsf.getPv() %></td>
				<td><%=hsf.getQps()%></td>
				<td><%=hsf.getPvForSite()%></td>
				<td><%=hsf.getTdodBlock()%></td>
				<td><%=hsf.getSsBlock()%></td>
				<%		
					}else{
						%>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
						<td></td>						
						<td></td>
						<%				
					}
				}
				}
				%>
			</tr>
			<%} %>
		</table>
	</div>