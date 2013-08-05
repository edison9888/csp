<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.taobao.csp.depend.po.NotifyPo" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">

		<title>Notify Consumer信息</title>

		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
					<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
		<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css"
			type="text/css" />
		<link rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css"
			type="text/css" />
		<style>
		
		body{
			padding-top:60px
		}
body td {
	font-size: 13px;
}
</style>
	</head>
<%




NotifyPo appStat = (NotifyPo)request.getAttribute("appStat");



StringBuilder sb = new StringBuilder();
sb.append("<pie>");

sb.append("<slice title='可靠异步成功'>"+appStat.getRa_s_count()+"</slice>");			
sb.append("<slice title='可靠异步失败'>"+appStat.getRa_f_count()+"</slice>");		
sb.append("<slice title='同步成功'>"+appStat.getS_count()+"</slice>");		
sb.append("<slice title='同步发送等待超过200ms成功'>"+appStat.getWs_count()+"</slice>");		

sb.append("<slice title='同步发送失败'>"+appStat.getF_count()+"</slice>");		
sb.append("<slice title='同步发送结果一次'>"+appStat.getRe_count()+"</slice>");		
sb.append("<slice title='同步发送没有连接'>"+appStat.getNc_count()+"</slice>");		
sb.append("<slice title='同步发送超时'>"+appStat.getTimeout_count()+"</slice>");		

/*
		sb.append("<slice title='可靠异步成功'>100</slice>");
sb.append("<slice title='可靠异步失败'>110</slice>");		
sb.append("<slice title='同步成功'>120</slice>");		
sb.append("<slice title='同步发送等待超过200ms成功'>130</slice>");		

sb.append("<slice title='同步发送失败'>130</slice>");		
sb.append("<slice title='同步发送结果一次'>130</slice>");		
sb.append("<slice title='同步发送没有连接'>130</slice>");		
sb.append("<slice title='同步发送超时'>130</slice>");	
*/	

sb.append("</pie>");

%>


	<body>
		<%@ include file="../../header.jsp"%>


		<form id="mainForm"
			action="<%=request.getContextPath() %>/show/notify.do"
			method="get">
			<input type="hidden" value="showIndex" name="method">

			<div style="text-align:center">
				<div id="page_nav"></div>
				</div>
				<script>
		$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {appName: '${opsName}',selectDate:'${selectDate}'});
		</script>
		<div class="container">
			<div class="row">
			
			<div class="span6" style="padding-top:30px">
			<h3>统计信息</h3>
					<table class="table table-striped table-bordered table-condensed" style="margin-top:30px">
			
						<tr>
							<td>可靠异步成功</td>
							<td><%=appStat.getRa_s_count() %> </td>
						</tr>
							<tr>
							<td>可靠异步失败</td>
							<td><%=appStat.getRa_f_count() %> </td>
						</tr>
							<tr>
							<td>同步成功</td>
							<td> <%=appStat.getS_count() %> </td>
						</tr>
							<tr>
							<td>同步发送等待超过200ms成功</td>
							<td><%=appStat.getWs_count()  %></td>
						</tr>
							<tr>
							<td>同步发送失败</td>
							<td><%=appStat.getF_count() %> </td>
						</tr>
							<tr>
							<td>同步发送结果一次</td>
							<td><%=appStat.getRe_count() %> </td>
						</tr>
							<tr>
							<td>同步发送没有连接</td>
							<td> <%=appStat.getNc_count() %> </td>
						</tr>
						<tr>
							<td>同步发送超时</td>
							<td>  <%=appStat.getTimeout_count() %> </td>
						</tr>
					</table>
				</div>
				<div class="span6">
<div id="chartdiv2" align="center"></div>
				</div>
			</div>
			<hr />
			<div class="row">
				<div class="span12">

					<h3>
						按类型统计信息：
					</h3>
				</div>
			</div>
			<div class="row">
				<div class="span12">
					<table class="table table-striped table-bordered table-condensed">
						<thead>
							<tr>
								<td style="text-align: center">
									组名
								</td>
								<td style="text-align: center">
									类型
								</td>
								<td style="text-align: center">
									可靠异步成功
								</td>
								<td style="text-align: center">
									可靠异步失败
								</td>
								<td style="text-align: center">
									同步成功
								</td>
								<td style="text-align: center">
									同步发送等待超过200ms成功
								</td>
								<td style="text-align: center">
									同步发送失败
								</td>
								<td style="text-align: center">
									同步发送结果一次
								</td>
								<td style="text-align: center">
									同步发送没有连接
								</td>
								<td style="text-align: center">
									同步发送超时
								</td>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${typeStatList }" var="item">
								<tr>
									<td>
										${item.groupName }
									</td>
									<td>
										${item.operType }
									</td>
									<td style="text-align: center">
										${item.ra_s_count }
									</td>
									<td style="text-align: center">
										${item.ra_f_count }
									</td>
									<td style="text-align: center">
										${item.s_count}
									</td>
									<td style="text-align: center">
										${item.ws_count}
									</td>
									<td style="text-align: center">
										${item.f_count }
									</td>
									<td style="text-align: center">
										${item.re_count }
									</td>
									<td style="text-align: center">
										${item.nc_count }
									</td>
									<td style="text-align: center">
										${item.timeout_count }
									</td>
								</tr>

							</c:forEach>
						</tbody>
					</table>

				</div>
			</div>
			<hr />
			<div class="row">
				<div class="span12">

					<h3>
						分机房统计信息：
					</h3>
				</div>
			</div>
			<div class="row">

				<c:forEach items="${siteStatList }" var="site">

					<div class="span12">
						<!-- 机房名称 -->
						<span style="font-weight: bolder;">
							${site[0].siteName} </span>

						<table class="table table-striped table-bordered table-condensed">
							<thead>
								<tr>
									<td style="text-align: center">
										组名
									</td>
									<td style="text-align: center">
										类型
									</td>
									<td style="text-align: center">
										可靠异步成功
									</td>
									<td style="text-align: center">
										可靠异步失败
									</td>
									<td style="text-align: center">
										同步成功
									</td>
									<td style="text-align: center">
										同步发送等待超过200ms成功
									</td>
									<td style="text-align: center">
										同步发送失败
									</td>
									<td style="text-align: center">
										同步发送结果一次
									</td>

									<td style="text-align: center">
										同步发送没有连接
									</td>
									<td style="text-align: center">
										同步发送超时
									</td>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${site }" var="item">
									<tr>
										<td>
											${item.groupName }
										</td>
										<td>
											${item.operType }
										</td>
										<td style="text-align: center">
											${item.ra_s_count }
										</td>
										<td style="text-align: center">
											${item.ra_f_count }
										</td>
										<td style="text-align: center">
											${item.s_count}
										</td>
										<td style="text-align: center">
											${item.ws_count}
										</td>
										<td style="text-align: center">
											${item.f_count }
										</td>
										<td style="text-align: center">
											${item.re_count }
										</td>
										<td style="text-align: center">
											${item.nc_count }
										</td>
										<td style="text-align: center">
											${item.timeout_count }
										</td>
									</tr>

								</c:forEach>
							</tbody>
						</table>


					</div>
				</c:forEach>

			</div>
		</div>
				</form>
		<script>
		//初始化search bar
$(document).ready(function(){
	var so1 = new SWFObject("<%=request.getContextPath() %>/statics/ampie/ampie.swf", "ampie", "500", "380", "8", "#FFFFFF");
	so1.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
	so1.addVariable("chart_id", "amline");   
	so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/ampie/ampie_settings.xml");
	so1.addVariable("chart_data", encodeURIComponent("<%=sb.toString()%>"));
	
	so1.write("chartdiv2");		
}); 
		</script>
	</body>
</html>
