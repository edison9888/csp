<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="com.taobao.monitor.common.cache.AppInfoCache"%>
<%@ page contentType="text/html; charset=GBK"%>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<div id="deployDiv" >
</div>
<%
String appId = request.getParameter("appId");
String appName = "";
if(StringUtils.isNotBlank(appId))
 appName = AppInfoCache.getAppInfoById(Integer.parseInt(appId)).getAppName();
%>
<script type="text/javascript">
$("#deployDiv").load("<%=request.getContextPath() %>/other/show.do?method=todayArtoo&appName=<%=appName%>");
</script>

<select id="companySelect"></select>
<select id="groupSelect"></select>
<select id="appSelect"></select>


&nbsp;&nbsp;&nbsp;
<input class="btn btn-success" type="button"
	onclick="javscript:location.href='${param.urlPrefix}'+getSelectAppId()"
	value="查看应用">

<input type="text" id="appname_text"
	style="width: 150px; margin-left: 100px;" />
<input class="btn btn-success" type="button"
	onclick="javscript:location.href='${param.urlPrefix}'+getInputAppId()"
	value="应用直达">

<script>
var nw = new NavigateWidget2({appId:'${param.appId}'},"<%=request.getContextPath()%>");
</script>





