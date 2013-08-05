<%@page import="com.taobao.csp.time.web.po.SortEntry"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%><html>
<head>
<title>监控详情</title>
<%@ include file="/time/common/base.jsp"%>

<meta http-equiv="content-type" content="text/html;charset=gbk" />
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.tooltip.min.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script>
$(function(){
	$(".pop").popover({placement : "right"});
	$(".pop").popover("hide");
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
			<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}">${appInfo.appName} </a>
			->uicfinal 访问 tair 信息
			<hr>
			<div class="row-fluid">
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td width="250" style="text-align:center;">Tair 分组</td>
							<%for(String time:timeList){ %>
							<td style="text-align:center;"><%=time %></td>
							<%} %>
							<td style="text-align:center;">主机详情</td>
							<td style="text-align:center;">历史数据</td>
						</tr>
					</thead>
					
					<tbody id="exctbody">
						<%
							for(SortEntry<TimeDataInfo> se:sortEntryList){
								String namespace = se.getKeyName();
						%>
							<tr>
								<td style="text-align:center;" width="250" rowspan="2"><%=namespace %><a target='_blank' href='<%=request.getContextPath()%>/app/conf/key/show.do?method=keyPropsList&&appId=${appInfo.appId}&keyName=<%=se.getFullKeyName()%>' ><img src='<%=request.getContextPath()%>/statics/img/add.png' width='10px' height='10px' title='加入告警' /></a></td>
								<%
								for(String time:timeList){ 
									TimeDataInfo td = se.getObjectMap().get(time);
									if(td == null){
								%>
								<td style="text-align:center;"></td>
								<%}else{
									Map<String, Object> propertyMap = td.getPropertyMap();
									%>
									<td data-content="<table class='table table-striped table-bordered table-condensed'><tr><td>Tair-Cnt</td><td><%=propertyMap.get("Tair-Cnt") %></td></tr><tr><td>Tair-Succ</td><td><%=propertyMap.get("Tair-Succ") %></td></tr><tr><td>Tair-Timeout</td><td><%=propertyMap.get("Tair-Timeout") %></td></tr><tr><td>Tair-Miss</td><td><%=propertyMap.get("Tair-Miss") %></td></tr><tr><td>Tair-Expt</td><td><%=propertyMap.get("Tair-Expt") %></td></tr></table>" class="pop"  style="text-align:center;"><%=propertyMap.get("Tair-Cnt")%></td>
									<%
									}} %>
								<td style="text-align:center;" rowspan="2"><a target='_blank' title='查看主机调用量' href='<%=request.getContextPath()%>/app/detail/uicfinal/consumer/show.do?method=showHostInfo&appId=${appInfo.appId}&keyName=<%=namespace %>'>查看</a></td>
								<td style="text-align:center;" rowspan="2"><a target='_blank' title='查看历史调用量' href='<%=request.getContextPath()%>/app/detail/history.do?method=showHistory&appName=${appInfo.appName}&keyName=<%=KeyConstants.UIC_TAIR_CLIENT+Constants.S_SEPERATOR+namespace %>'>查看</a></td>
							</tr>
							<tr>
								<%
								for(String time:timeList){ 
									TimeDataInfo td = se.getObjectMap().get(time);
									if (td == null) {
										%>
										<td style="text-align:center;"></td>
										<%	
									} else {
										Map<String, Object> propertyMap = td.getPropertyMap();
										%>
										<td data-content="<table class='table table-striped table-bordered table-condensed'><tr><td>tairShoot</td><td><%=propertyMap.get("Shoot") %>%</td></tr><tr><td>tairMissedShoot</td><td><%=propertyMap.get("Miss-Shoot") %>%</td></tr><tr><td>tairTimeoutShoot</td><td><%=propertyMap.get("Out-Shoot") %>%</td></tr></table>" class="pop" style="text-align:center;"><%=propertyMap.get("Shoot") %>%</td>
										<%
									}
								}
								%>
							</tr>
							<%} %>
					</tbody>
				</table>
				
			</div>
		</div>			
    </div>
</div>
   
</body>
<script type="text/javascript">
  
  $(document).ready(function() {
	  $("td[rel=popover]") .popover({placement:"left"}) .click(function(e) {
	      e.preventDefault()
	    });
    });
</script>

</html>
