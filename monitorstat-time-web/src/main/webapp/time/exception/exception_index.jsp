<%@page import="com.taobao.csp.time.web.po.SortEntry"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>监控详情</title>
<%@ include file="/time/common/base.jsp"%>

<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
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

<%
List<String> timeList = (List<String>)request.getAttribute("timeList");
List<SortEntry<TimeDataInfo>> sortEntryList = (List<SortEntry<TimeDataInfo>>)request.getAttribute("sortEntryList");


%>

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
			<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}" >${appInfo.appName}</a> -><a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=showIndex&appId=${appInfo.appId}">web流量信息</a>->${keyname}
			<hr>
			<div class="row-fluid">
				<h4>${appInfo.appName}全网异常信息(点击数字可以看异常内容)</h4>
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td width="250" style="text-align:center;">异常名称</td>
							<%for(String time:timeList){ %>
							<td style="text-align:center;"><%=time %></td>
							<%} %>
							<td style="text-align:center;">查看机器</td>
							<td style="text-align:center;">历史</td>
						</tr>
					</thead>
					
					<tbody id="exctbody">
					
						<%
							for(SortEntry<TimeDataInfo> se:sortEntryList){
						%>
							<tr>
								<td width="250"><%=se.getKeyName()%><a target='_blank' href='<%=request.getContextPath()%>/app/conf/key/show.do?method=keyPropsList&&appId=${appInfo.appId}&keyName=<%=se.getFullKeyName()%>' ><img src='<%=request.getContextPath()%>/statics/img/add.png' width='10px' height='10px' title='加入告警' /></a></td>
								<%
								for(String time:timeList){ 
									TimeDataInfo td = se.getObjectMap().get(time);
									if(td == null){
								%>
								<td style="text-align:center;" > - </td>
								<%}else{
									%>
									<td style="text-align:center;" >
									<a href="javascript:popoverTitle('<%=se.getKeyName()%>_<%=td.getTime()%>')"><%=td.getMainValue()%><%=td.getMainValueRate()%></a>
									<div id="<%=se.getKeyName()%>_<%=td.getTime()%>" style="display:none">
										<%=td.getOriginalPropertyMap().get("desc")%>
									</div>
									</td>
									<%
									}} %>
								<td  style="text-align:center;"><a target='_blank' title='查看每台机器异常量' href='<%=request.getContextPath()%>/app/detail/exception/show.do?method=gotoHostException&appId=${appInfo.appId}&exception=<%=se.getFullKeyName()%>'>查看</a></td>
								<td  style="text-align:center;"><a target='_blank' title='查看历史调用量' href='<%=request.getContextPath()%>/app/detail/history.do?method=showHistory&appName=${appInfo.appName}&keyName=<%=se.getFullKeyName()%>'>查看</a></td>
							</tr>
							<%} %>
					</tbody>
				</table>
			</div>
		</div>			
    </div>
</div>
   <div id="mesageBox"></div>
</body>
<script type="text/javascript">
$(document).ready(function() {
	$( "#mesageBox" ).dialog({
		autoOpen: false,
	});
})


function popoverTitle(id){
	$( "#mesageBox" ).html($("#"+id).html());
	$( "#mesageBox" ).dialog( "option", "width", 800 );
	$( "#mesageBox" ).dialog( "option", "title","异常信息" );
	$( "#mesageBox" ).dialog( "option", "height", 400  );
	$( "#mesageBox" ).dialog( "open" );
}

</script>

</html>
