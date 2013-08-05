<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.time.web.po.SortEntry"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
<head>
<title>Key详细信息</title>
<%@ include file="/time/common/base.jsp"%>

<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/raphael.js" type="text/javascript"></script>


<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>

<script>

</script>
<style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
</style>
</head>
<body >
<%@ include file="/header.jsp"%>
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	

</div>
	<div class="row-fluid">
        <div class="span2">
			<%@include file="/leftmenu.jsp"%>
		</div>
		<div class="span12">
			<%--用于导航 --%>
			<%request.setAttribute("s_sperator", Constants.S_SEPERATOR);%>
			<c:forEach var="item" items="${fn:split(param.key,  s_sperator)}" varStatus="status">
				<c:if test="${status.index==1}">
					<c:set var="referAppName" value="${item}"/>
				</c:if>
			</c:forEach>
			<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> 
			-><a href="<%=request.getContextPath()%>/app/conf/key/show.do?method=showIndex&appId=${appInfo.appId}" >Key管理  ${appInfo.appName}</a> 
			 ->详细信息查看
			<hr>
			
			<%
				String keyScope = (String)request.getAttribute("keyScope");
				String keyName = (String)request.getAttribute("keyName");
				String[] timeArray = (String[])request.getAttribute("timeArray");
				Map<String, Integer> orderMap = (Map<String, Integer>)request.getAttribute("orderMap");
				List<SortEntry<TimeDataInfo>> ipValueList = (List<SortEntry<TimeDataInfo>>)request.getAttribute("ipValueList");
				List<TimeDataInfo> appValueList = (List<TimeDataInfo>)request.getAttribute("appValueList");
				if(timeArray == null)
					timeArray = new String[10];
			%>
			<%
				if(("ALL".equals(keyScope) || "APP".equals(keyScope)) && appValueList != null) {
				//app info
			%>
				<h4>${keyName}应用级别的信息</h4>
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td  style="text-align:center;">属性</td>
				<%
					for(int i=0; i<timeArray.length; i++) {
				%>
						<td style="text-align:center;" id="time<%=i%>"><%=timeArray[i]%></td>
				<%
					}
				%>
						<td style="text-align:center;">查看历史</td>
						</tr>
					</thead>
					<%
					TimeDataInfo[] appArray = new TimeDataInfo[timeArray.length];
					for(TimeDataInfo dataInfo : appValueList) { 
						Integer index = orderMap.get(dataInfo.getFtime());
						if(index != null) {
							appArray[index] = dataInfo;
						}
					}
					%>
					<tr>
						<td width="250">${appInfo.appName}</td>
					<%
						for(int i=0; i<appArray.length; i++) {
							TimeDataInfo info = appArray[i];
							if(info == null)
								out.println("<td style='text-align: center;' id='_time" + i + "'>0</td>");
							else {
								Map<String, Object> property = info.getPropertyMap();
								String dataContent = "";
								if(property != null) {
									dataContent = "<table>";
									for(String key:property.keySet()) {
										if(key.equals("desc"))
											continue;
										dataContent += "<tr><td>" + key + "&nbsp;&nbsp;</td><td>" + property.get(key) + "</td></tr>";
									}
									dataContent += "</table>";
								}
								//dataContent = "1234";
								out.println("<td style='text-align: center;' " + i + "'> <a data-content='" + dataContent + "' href=''  rel='popover'>" + info.getMainValue() + "</a></td>");
							}
						}
					%>
						<td style="text-align: center;"><a href="<%=request.getContextPath()%>/app/detail/history.do?method=showHistory&appName=${appInfo.appName}&keyName=${keyName}" target="_blank">查看历史</a></td>
					</tr>
					<%					
				}
			%>
			</table>
			<br/>
			<%
			if(("ALL".equals(keyScope) || "HOST".equals(keyScope)) && ipValueList != null) {
				%>
				<h4>${keyName}主机级别的信息</h4>
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td  style="text-align:center;">属性</td>
				<%
					for(int i=0; i<timeArray.length; i++) {
				%>
						<td style="text-align:center;" id="time<%=i%>"><%=timeArray[i]%></td>
				<%
					}
				%>
						<td style="text-align:center;">查看历史</td>
						</tr>
					</thead>
					<%
					for(SortEntry<TimeDataInfo> entry : ipValueList) {
						TimeDataInfo[] ipArray = new TimeDataInfo[timeArray.length];
						for(String keyTime :  entry.getObjectMap().keySet()) {
							Integer index = orderMap.get(keyTime);
							if(index != null) {
								ipArray[index] = entry.getObjectMap().get(keyTime);
							}							
						}
						%>
						<tr>
							<td width="250"><%=entry.getIp()%></td>
						<%
							for(int i=0; i<ipArray.length; i++) {
								TimeDataInfo info = ipArray[i];
								if(info == null)
									out.println("<td style='text-align: center;' id='_time" + i + "'>0</td>");
								else {
									Map<String, Object> property = info.getPropertyMap();
									String dataContent = "";
									if(property != null) {
										dataContent = "<table>";
										for(String key:property.keySet()) {
											if(key.equals("desc"))
												continue;
											dataContent += "<tr><td>" + key + "&nbsp;&nbsp;</td><td>" + property.get(key) + "</td></tr>";
										}
										dataContent += "</table>";
									}
									//dataContent = "1234";
									out.println("<td style='text-align: center;' " + i + "'> <a data-content='" + dataContent + "' href=''  rel='popover'>" + info.getMainValue() + "</a></td>");									
								}
							}
						%>
							<td style="text-align: center;"><a href="<%=request.getContextPath()%>/app/detail/history.do?method=showHistoryByip&appName=${appInfo.appName}&keyName=${keyName}&ip=<%=entry.getIp()%>" target="_blank">查看历史</a></td>
						</tr>
						<%
					}				
				}
			%>
			</table>				
		</div>			
    </div>
</div>
   
</body>
<script type="text/javascript">
	$("a[rel='popover']").popover({placement:'left'}).click(function(e) {
		e.preventDefault()
	});
</script>

</html>
