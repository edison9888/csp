<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<%@page import="com.taobao.csp.time.cache.TimeCache"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.csp.other.artoo.Artoo"%>
<%@page import="java.util.List"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>ʵʱ���ϵͳ</title>
<%@ include file="/time/common/base.jsp"%>
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/amcharts/flash/swfobject.js" type="text/javascript"></script>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<style type="text/css">
body {
	padding-top: 60px;
}
</style>
</head>
<body>
	<%@include file="/header.jsp"%>
	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12">
				<div class="row-fluid">
					<div class="span12" id="page_nav">
	</div>
	<script>
			$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/tair/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
	</script>	
				</div>
				<div class="row-fluid">
					<div class="row-fluid">
						<div class="span8">
							<div>
								<section>
								<div class="page-header">
									<h3>
										�澯��ʷ�ͻ������ݶԱ� <small style="color: black">���ӱ���</small>
									</h3>
								</div>
								<c:if test="${hasHostScope!=false}">
									<table width="100%">
										<caption>�澯�������ߺ���ʷ����</caption>
										<tr>
											<td width="100%" id="pvchartDivId" style="height: 100px"
												colspan="2">
												<script type="text/javascript">
												 var params = 
										            {
										                bgcolor:"#FFFFFF",
										                wmode:"transparent"
										            };
												
												var flashVars = 
									            {
									                path: "<%=request.getContextPath()%>/statics/js/amcharts/flash/",
									                settings_file: "<%=request.getContextPath()%>/statics/js/amcharts/flash/setting/amline_settings1.xml",
									                data_file: escape("<%=request.getContextPath()%>/app/detail/alarm/show.do?method=chartData&appName=${appName}&keyId=${keyId}&property=${property}&ips=${ips}")
									            };
												
												swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
														"pvchartDivId", "100%", "350", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",
														flashVars, params);
												
												</script>
												</td>
										</tr>
									</table>
								</c:if> <c:if test="${hasAppScope!=false}">
									<table width="100%">
										<caption>ȫ�����ߺ���ʷ����</caption>
										<tr>
											<td width="100%" id="pvchartDivId2" style="height: 100px"
												colspan="2">
												<script type="text/javascript">
												 var params = 
										            {
										                bgcolor:"#FFFFFF",
										                wmode:"transparent"
										            };
												
												var flashVars = 
									            {
									                path: "<%=request.getContextPath()%>/statics/js/amcharts/flash/",
									                settings_file: "<%=request.getContextPath()%>/statics/js/amcharts/flash/setting/amline_settings1.xml",
									                data_file: escape("<%=request.getContextPath()%>/app/detail/alarm/show.do?method=chartData2&appName=${appName}&keyId=${keyId}&property=${property}")
									            };
												
												swfobject.embedSWF("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", 
														"pvchartDivId2", "100%", "350", "8.0.0", "<%=request.getContextPath()%>/statics/js/amcharts/flash/expressInstall.swf",
														flashVars, params);
												
												</script>
												
												</td>
										</tr>
									</table>
								</c:if> </section>
							</div>
						</div>
						<div class="span4">
							<div class="row-fluid">
								<div>
									<section>
									<div class="page-header">
										<h3>
											��ǰ�澯��Ϣ <small style="color: black">���ӱ���</small>
										</h3>
									</div>
									<table
										class="table table-striped table-bordered table-condensed">
										<caption>��ǰ�澯 ${title }</caption>
										<thead>
											<tr>
												<td>�澯ģ��</td>
												<td>�澯��Χ</td>
												<td>�澯ʱ��</td>
												<td>�澯ֵ</td>
												<td>�澯ԭ��</td>
											</tr>
										</thead>
										<c:forEach var="item" items="${alarmPos}">
											<tr>
												<td>${item.mode_name }</td>
												<td>${item.key_scope }</td>
												<td>${item.ftime }</td>
												<td>${item.alarm_value }</td>
												<td>${item.alarm_cause }</td>
											</tr>
										</c:forEach>
									</table>
									</section>
								</div>
							</div>
						</div>
					</div>
					<div class="row-fluid">
						<ul id="tab" class="nav nav-tabs">
							<li class="active"><a href="#appdepend" data-toggle="tab">Ӧ��������ϵͼ</a></li>
							<li ><a href="#keydepend" data-toggle="tab">�澯KEY��������ϵͼ</a></li>
						</ul>
						<div id="myTabContent" class="tab-content">
            				<div class="tab-pane fade in active" id="appdepend">
            						<div class="page-header">
								</div>
								<div class="row-fluid" style="height: 550px; width: 100%"
									id="dependDiv">���ڼ�����...</div>
								<script type="text/javascript">
								$("#dependDiv").load("<%=request.getContextPath()%>/app/depend/query/show.do?appName=${appInfo.appName}&method=queryAppDetailWithTimeData");
								</script> 
            				</div>
            				<div class="tab-pane fade " id="keydepend">
								<div class="row-fluid" style="height: 550px; width: 100%"
									id="dependDiv2">���ڼ�����...</div>
								<script type="text/javascript">
								$("#dependDiv2").load("<%=request.getContextPath()%>/app/depend/query/show.do?appName=${appInfo.appName}&method=queryKeyDetailWithTimeData&keyName=${keyName}");
								</script> 
            				</div>
            			</div>
					</div>
					<div class="row-fluid">
						<div class="span6">
							<section>
							<div class="page-header">
								<h3>
									Ӧ�÷�����Ϣ <small style="color: black">���ӱ���</small>
								</h3>
								<%
									List<Artoo> list = (List<Artoo>)request.getAttribute("artooList"); 
									if(list.size()>0){
								%>
								<table class="table table-striped table-bordered table-condensed">
									<thead>
											<tr>
												<td>Ӧ����</td>
												<td>����ʱ��</td>
												<td>���𷽰�</td>
												<td>����״̬</td>
												<td>������</td>
											</tr>
											</thead>
											<c:forEach var="item" items="${artooList}">
											<tr>
												<td>${item.appName }</td>
												<td>${item.deployTime }</td>
												<td>${item.planType }</td>
												<td>${item.state }</td>
												<td>${item.creator }</td>
											</tr>
										</c:forEach>	
								</table>
								<% 
									}
								%>
							</div>
							</section>
						</div>
						<div class="span6">
							<section>
							<div class="page-header">
								<h3>
									�Զ�������Ϣ <small style="color: black">���ӱ���</small>
								</h3>
							</div>
							</section>
						</div>
					</div>
				</div></body>
</html>