<%@page import="com.taobao.csp.time.cache.TimeCache"%>
<%@page import="com.taobao.csp.time.util.Arith"%>
<%@page import="com.taobao.csp.time.util.DataUtil"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
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
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/flash/swfobject.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script>
$(function(){
	
});
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
	<div class="span12" id="page_nav">
		
	</div>
	<script>
			$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
	</script>	
</div>

	<%
	Map<String, List<TimeDataInfo>> map =(Map<String, List<TimeDataInfo>>) request.getAttribute("topExceptionMap");
	List<String> timelist =(List<String>) request.getAttribute("recentlyTime");
	int max = 0;
	
	for(List<TimeDataInfo> entry:map.values()){
		if(entry.size()>max){
			max = entry.size();
		}
	}
	
	%>

	<div class="row-fluid">
		<div class="row-fluid">
			<table class="table table-striped table-condensed table-bordered "	id="table1">
				<thead id="exceptionTopThread">
					<tr>
					<%for(String time:timelist){ %>
						<td><%=time %></td>
					<%} %>
					</tr>
				</thead>
				<tbody id="exceptionTopTbody">
					<%for(int i=0;i<max;i++){ %>
					<tr>
					<%
					for(String time:timelist){ 
						List<TimeDataInfo> list = map.get(time);
						
						TimeDataInfo td = list!=null&&list.size()>i? list.get(i):null;
					%>
						<td><%if(td != null){ %><a target="_blank" href="<%=request.getContextPath()%>/app/detail/exception/show.do?method=showIndex&appId=<%=td.getAppId()%>"><%=td.getAppName()+"["+td.getMainValue()+"]" %></a><%} %></td>
					<%} %>
					</tr>
					<%} %>
				</tbody>
			</table>
		</div>			
    </div>
</div>
   
</body>
</html>
