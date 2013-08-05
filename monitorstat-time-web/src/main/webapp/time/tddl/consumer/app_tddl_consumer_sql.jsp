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
<title>�������</title>
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
			<a href="<%=request.getContextPath()%>/index.jsp">ʵʱ��ҳ</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}">${appInfo.appName} </a>
			-><a href="<%=request.getContextPath()%>/app/detail/tddl/consumer/show.do?method=showIndex&appId=${appInfo.appId}">���ĵ�TDDL��Ϣ</a>
			->${key1Name}���ݿ�
			<hr>
			<div class="row-fluid">
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td width="250" style="text-align:center;">SQL</td>
							<%for(String time:timeList){ %>
							<td style="text-align:center;"><%=time %></td>
							<%} %>
							<td style="text-align:center;">��������</td>
							<td style="text-align:center;">��ʷ����</td>
						</tr>
					</thead>
					
					<tbody id="exctbody">
					
						<%
							for(SortEntry<TimeDataInfo> se:sortEntryList){
								String sql = se.getKeyName();
								String sqlInCell;
								final int I = 100;
								if (sql.length() > I) {
									sqlInCell = sql.substring(0, I)+"...";
								} else {
									sqlInCell = sql;
								}
						%>
							<tr>
								<td width="250" title='<%=sql %>'  style="text-align:center;" rowspan="2"><%=sqlInCell %><a target='_blank' href='<%=request.getContextPath()%>/app/conf/key/show.do?method=keyPropsList&&appId=${appInfo.appId}&keyName=<%=se.getFullKeyName()%>' ><img src='<%=request.getContextPath()%>/statics/img/add.png' width='10px' height='10px' title='����澯' /></a></td>
								<%
								for(String time:timeList){ 
									TimeDataInfo td = se.getObjectMap().get(time);
									if(td == null){
								%>
								<td style="text-align:center;"></td>
								<%}else{
									%>
									<td style="text-align:center;" title="ִ�д���"><%=td.getOriginalPropertyMap().get("E-times") == null ? "" : td.getOriginalPropertyMap().get("E-times")%></td>
									<%
									}} %>
								<td style="text-align:center;" rowspan="2"><a target='_blank' title='�鿴����������' href='<%=request.getContextPath()%>/app/detail/tddl/consumer/show.do?method=showSqlHostInfo&appId=${appInfo.appId}&key1Name=${key1Name}&key2Name=<%=sql %>'>�鿴</a></td>
								<td style="text-align:center;" rowspan="2"><a target='_blank' title='�鿴��ʷ������' href='<%=request.getContextPath()%>/app/detail/history.do?method=showHistory&appId=${appInfo.appId}&keyName=<%=se.getFullKeyName() %>'>�鿴</a></td>
							</tr>
							<tr>
								<%
								for(String time:timeList){ 
									TimeDataInfo td = se.getObjectMap().get(time);
									if(td == null){
								%>
								<td style="text-align:center;"></td>
								<%}else{
									%>
									<td class="pop"  style="text-align:center;" data-content="<table class='table table-striped table-bordered table-condensed'><tr><td>ƽ����Ӧʱ��</td><td><%=td.getOriginalPropertyMap().get("C-time") == null ? "" : td.getOriginalPropertyMap().get("C-time")+"ms"%></td></tr><tr><td>�����Ӧʱ��</td><td><%=td.getOriginalPropertyMap().get("Max-C-time") == null ? "" : td.getOriginalPropertyMap().get("Max-C-time")+"ms"%></td></tr><tr><td>��С��Ӧʱ��</td><td><%=td.getOriginalPropertyMap().get("Min-C-time") == null ? "" : td.getOriginalPropertyMap().get("Min-C-time")+"ms"%></td></tr></table>"><%=td.getOriginalPropertyMap().get("C-time") == null ? "" : td.getOriginalPropertyMap().get("C-time")+"ms"%></td>
									
									<%
									}} %>
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
