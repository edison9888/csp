<%@page import="com.taobao.csp.dataserver.PropConstants"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>
<head>
<title>MBean信息</title>
<%@ include file="/time/common/base.jsp"%>
<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/amcharts.js" type="text/javascript"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/raphael.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
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
<script type="text/javascript">
            function showotherInfo(id,key,mianpro){
            	$.ajax({
					url : "<%=request.getContextPath()%>/app/detail/mbean/show.do?method=queryMbeanTop&appId=${appInfo.appId}&key="+key+"&mianpro="+mianpro,
					success : function(data) {
						var tr ="";
						for(var i=0;i<data.length;i++){
							var ip =data[i].ip;
							var fullname =data[i].keyName;
							 tr += "<tr><td>"+ip+"</td><td>"+data[i].mainValue+"</td><td><a href='<%=request.getContextPath()%>/app/detail/history.do?method=showHistoryByip&appName=${appInfo.appName}&keyName="+fullname+"&ip="+ip+"'>历史</a></td></tr>"
						}
						$("#"+id).html(tr);
					}
				});
            	
            	window.setTimeout("showotherInfo()",60000);
            }
            
        </script>
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
			<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}" >${appInfo.appName}</a> ->MBean信息
			<hr>
			<div class="row-fluid">
			<div class="span12">
			<%
			int i=0;
			%>
			<div class="row-fluid thumbnail">
			<h5>Thread top 10  </h5>
			<%
			List<String> thread =(List<String>) request.getAttribute("thread");
			%>
				<%
				for(String obj:thread) {
					String[] tmp = obj.split(Constants.S_SEPERATOR);
				%>
					<div class="span3" style="margin-left: 2px;width:320px;">						
						<table class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<td colspan="3" align="center">线程 [ <%=tmp[2] %><a target='_blank' href='<%=request.getContextPath()%>/app/conf/key/show.do?method=keyPropsList&&appId=${appInfo.appId}&keyName=<%=obj%>' ><img src='<%=request.getContextPath()%>/statics/img/add.png' width='10px' height='10px' title='加入告警' /></a> ]<a href="<%=request.getContextPath()%>/app/detail/mbean/show.do?method=showMbeanHost&appId=${appInfo.appId}&key=<%=obj%>&mianpro=<%=PropConstants.THREAD_BLOCKED%>">详情</a></td>
								</tr>
								<tr>
									<td width="130" align="center">IP</td><td width="80" >block</td><td width="50" >详情</td>
								</tr>
							</thead>
							<tbody id="threadId_<%=i %>" >
							</tbody>
						</table>
					</div>
					<script type="text/javascript">
          				  showotherInfo("threadId_<%=i %>","<%=obj %>","<%=PropConstants.THREAD_BLOCKED%>");
      			  	</script>
      			  	<%i++; %>
      			  	<%} %>
			</div>
			<div class="row-fluid thumbnail">
			<h5>DataSource top 10  </h5>
			<%
			List<String> dataSource =(List<String>) request.getAttribute("dataSource");
			%>
				<%
				for(String obj:dataSource) {
					String[] tmp = obj.split(Constants.S_SEPERATOR);
				%>
					<div class="span3" style="margin-left: 2px;width:260px;">											
						<table class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<td colspan="3">数据源 [ <%=tmp[2] %> ]<a href="<%=request.getContextPath()%>/app/detail/mbean/show.do?method=showMbeanHost&appId=${appInfo.appId}&key=<%=obj %>&mianpro=<%=PropConstants.DATASOURCE_IN_USE%>">详情</a></td>
								</tr>
								<tr>
									<td width="130" align="center">IP</td><td width="80" >In use</td><td width="50" >详情</td>
								</tr>
							</thead>
							<tbody id="dataSource_<%=i %>" >
							</tbody>
						</table>
					</div>
					<script type="text/javascript">
          				  showotherInfo("dataSource_<%=i %>","<%=obj %>","<%=PropConstants.DATASOURCE_IN_USE%>");
      			  	</script>
      			  		<%i++; %>
      			  		<%} %>
			</div>
			<div class="row-fluid thumbnail">
			<h5>Thread pool top 10  </h5>
			<%
			List<String> threadPool =(List<String>) request.getAttribute("threadPool");
			%>
				<%
				for(String obj:threadPool) {
					String[] tmp = obj.split(Constants.S_SEPERATOR);
				%>
					<div class="span3" style="margin-left: 2px;width:260px;">									
						<table class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<td colspan="3">线程池 [ <%=tmp[2] %> ]<a href="<%=request.getContextPath()%>/app/detail/mbean/show.do?method=showMbeanHost&appId=${appInfo.appId}&key=<%=obj%>&mianpro=<%=PropConstants.THREAD_POOL_CURRENT%>">详情</a></td>
								</tr>
								<tr>
									<td width="130" align="center">IP</td><td width="80" >current use</td><td width="50" >详情</td>
								</tr>
							</thead>
							<tbody id="threadPool_<%=i %>" >
							</tbody>
						</table>
					</div>
					<script type="text/javascript">
          				  showotherInfo("threadPool_<%=i %>","<%=obj%>","<%=PropConstants.THREAD_POOL_CURRENT%>");
      			  	</script>
      			  		<%i++; %>
				<%} %>
			</div>
			</div>
			</div>
		</div>			
    </div>
</div>
   
</body>



</html>
