<%@page import="com.taobao.csp.time.web.po.SortEntry"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo"%>
<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@page import="com.taobao.csp.time.util.PropNameDescUtil"%>
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
List<String> properties = (List<String>)request.getAttribute("properties");
String property = (String)request.getAttribute("property");

if(properties == null) {
	properties = new ArrayList<String>();
} else {
	properties.remove(property);
}
%>

<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/notify/provider/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	
</div>
	<div class="row-fluid">
        <div class="span2">
			<%@include file="/leftmenu.jsp"%>
		</div>
		<div class="span12">
			<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> -><a href="<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=${appInfo.appId}">${appInfo.appName} </a>
			<%
				String parentKeys = (String)request.getAttribute("parentKeys");
				String keyName = (String)request.getAttribute("keyName");
				String lastParent = "";
				
				if(parentKeys == null || parentKeys.trim().equals("")) {
					parentKeys = keyName;
					lastParent = keyName;
				} else {
					String[] array = parentKeys.split("@");
					String parentKeysTmp = "";
					for(String parentKey : array) {
						parentKeysTmp += parentKey + "@";
						String href = "<a href='" + request.getContextPath() + 
								"/app/detail/customer/show.do?method=queryRealTimeBussinessLogData&appName=" + appInfo.getAppName() 
								+ "&keyName=" + keyName + "&property=" + property + "&parentKeys=" + parentKeysTmp + "'>" + keyName + "</a>";
						out.println("->" + href);	
						lastParent = parentKey;
					}
				}
				
				String href = "<a href='" + request.getContextPath() + 
						"/app/detail/customer/show.do?method=queryRealTimeBussinessLogData&appName=" + appInfo.getAppName() 
						+ "&keyName=" + keyName + "&property=" + property + "&parentKeys=" + parentKeys + "&parentKeys=" + parentKeys + "'>" + keyName + "</a>";
						
				out.println("->" + keyName);
			%>
			<hr>
			<div class="row-fluid">
				<table class="table table-striped table-bordered table-condensed"  width="100%">
					<thead>
						<tr>
							<td width="30%" style="text-align:center;">本级key</td>
							<%for(String time:timeList){ %>
							<td style="text-align:center;"><%=time %></td>
							<%} %>
							<td style="text-align:center;">主机详情</td>
							<td style="text-align:center;">下级key</td>
							<td style="text-align:center;">历史数据</td>
						</tr>
					</thead>
					
					<tbody id="exctbody">
						<%
							for(SortEntry<TimeDataInfo> se:sortEntryList){
								String keyNameTmp = se.getKeyName();
						%>
							<tr>
								<td style="text-align:center;" width="250" rowspan="<%=properties.size()%>">
								<%=keyNameTmp%><a target='_blank' href='<%=request.getContextPath()%>/app/conf/key/show.do?method=keyPropsList&&appId=${appInfo.appId}&keyName=<%=se.getFullKeyName()%>' ><img src='<%=request.getContextPath()%>/statics/img/add.png' width='10px' height='10px' title='加入告警' /></a>
								</td>
								<%
								for(String time:timeList){ 
									TimeDataInfo td = se.getObjectMap().get(time);
									if(td == null){
								%>
								<td style="text-align:center;"></td>
								<%}else{
									%>
									<td style="text-align:center;" title="<%=PropNameDescUtil.getDesc(property)%>"><%=td.getOriginalPropertyMap().get(property)%></td>
									<%}
									} %>
								<td style="text-align:center;" rowspan="<%=properties.size()%>"><a target='_blank' title='查看主机调用量' href='<%=request.getContextPath()%>/app/detail/customer/show.do?method=showHostKey1Info&appId=${appInfo.appId}&keyName=<%=keyNameTmp%>'>查看</a></td>
								<td style="text-align:center;" rowspan="<%=properties.size()%>">
								<a title='查看下级key调用量' href='<%=request.getContextPath()%>/app/detail/customer/show.do?method=queryRealTimeBussinessLogData&&appId=${appInfo.appId}&appName=<%=appInfo.getAppName()%>&property=<%=property%>&keyName=<%=lastParent + Constants.S_SEPERATOR + keyNameTmp%>'>查看</a>
								</td>
								<td style="text-align:center;" rowspan="<%=properties.size()%>"><a target='_blank' title='查看历史调用量' href='<%=request.getContextPath()%>/app/detail/history.do?method=showHistory&appName=${appInfo.appName}&keyName=<%=lastParent%>'>查看</a></td>
							</tr>
							<%
								for(String otherProperty : properties) {
							%>
							<tr>
								<%
								for(String time:timeList){ 
									TimeDataInfo td = se.getObjectMap().get(time);
									if (td == null) {
										%>
										<td style="text-align:center;"></td>
										<%	
									} else {
										%>
										<td style="text-align:center;" title="<%=PropNameDescUtil.getDesc(otherProperty)%>"><%=td.getOriginalPropertyMap().get(otherProperty)%></td>
										<%
									}
								}
								%>
							</tr>							
							<%		
								}
							%>
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
