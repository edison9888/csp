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
<title>�������</title>
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
			<a href="<%=request.getContextPath()%>/index.jsp">ʵʱ��ҳ</a> ->
			<a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}" >${appInfo.appName}</a> ->
			<a href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=${appInfo.appId}">�쳣��Ϣ</a>->${exception }
			<hr>
			<div class="row-fluid">
				<h4>${appInfo.appName}��${exception }ÿ̨������Ϣ(������ֿ��Կ��쳣����)</h4>
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td width="250" style="text-align:center;">�쳣����</td>
							<%for(String time:timeList){ %>
							<td style="text-align:center;"><%=time %></td>
							<%} %>
							<td style="text-align:center;">��ʷ</td>
						</tr>
					</thead>
					
					<tbody id="exctbody">
					
						<%
							for(SortEntry<TimeDataInfo> se:sortEntryList){
						%>
							<tr>
								<td width="250"><%=se.getIp()%></td>
								<%
								for(String time:timeList){ 
									TimeDataInfo td = se.getObjectMap().get(time);
									if(td == null){
								%>
								<td style="text-align:center;" > - </td>
								<%}else{
									%>
									<td style="text-align:center;" >
									<a href="javascript:popoverTitle('<%=se.getIp()%>_<%=td.getTime()%>')"><%=td.getMainValue()%><%=td.getMainValueRate()%></a>
									<div id="<%=se.getIp()%>_<%=td.getTime()%>" style="display:none">
										<%=td.getOriginalPropertyMap().get("desc")%>
									</div>
									</td>
									<%
									}} %>
								<td  style="text-align:center;"><a target='_blank' title='�鿴��ʷ������' href='<%=request.getContextPath()%>/app/detail/history.do?method=showHistoryByip&appName=${appInfo.appName}&keyName=<%=se.getKeyName()%>&ip=<%=se.getIp()%>'>�鿴</a></td>
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
	$( "#mesageBox" ).dialog( "option", "title","�쳣��Ϣ" );
	$( "#mesageBox" ).dialog( "option", "height", 400  );
	$( "#mesageBox" ).dialog( "open" );
}


</script>

</html>
