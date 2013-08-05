<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>
<%@page import="com.taobao.csp.time.web.po.TimeDataInfo" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>  

<!DOCTYPE html>

<html>
	<head>
		<title>告警记录查询</title>

		<%
			//设置base属性，生成绝对路径
			String serverUrl = request.getScheme() + "://"
					+ request.getServerName() + ":" + request.getServerPort();

			String base = serverUrl + request.getContextPath();

			request.setAttribute("base", base);
			List<TimeDataInfo> timeDataInfoList = (List<TimeDataInfo>)request.getAttribute("timeDataInfoList");
			String keyName = (String)request.getAttribute("keyName");
		%>

		<script>
 	var base="${base}";

 </script>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
		<meta http-equiv="description" content="This is my page">
		<link href="${base }/statics/css/bootstrap.css" rel="stylesheet">

		<script src="${base }/statics/js/jquery/jquery.min.js"></script>

		<script src="${base }/statics/js/bootstrap.js"></script>
		<script src="${base}/statics/js/date.js"></script>

		<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/My97DatePicker/WdatePicker.js"></script>

<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<style type="text/css">
body {
	padding-top: 60px;
}

#table1 td {
	word-wrap: break-word
}
</style>
	</head>

	<body>
	<form action="<%=request.getContextPath()%>/app/depend/query/show.do" id="form1" method="POST" id="form1" name="form1">
		<input type="hidden" name="method" value="gotoAlarmDetail" />
		<input type="hidden" name="appName" id="appName" value="${appInfo.appName}" />
		<input type="hidden" name="keyName" id="keyName" value="${keyName}"/>
		<input type="hidden" name="pageNo" id="hidden1" value="1" />
		<input type="hidden" name="pageSize" id="hidden2" value="15" />
		<div class="container" style="width: 95%">
							<div class="row">
					<strong style="color:red">应用名：</strong>${appInfo.appName}<br/>
					<strong style="color:red">接口名称：</strong>${keyName}<br/>							
									<h3 align="center">告警信息</h3>
									<span>
									默认显示${from}~${to}的告警信息，查看更多信息-><a href="<%=request.getContextPath()%>/app/alarm/show.do?method=showIndex&appId=${appInfo.appId}&keyNamePart=${keyName}" target="_blank">告警记录查询</a>
									</span>
									<table
										class="table table-striped table-condensed table-bordered"
										id="table1" style="table-layout: fixed" >
										<thead>
											<tr>
												<!-- class用于指定要显示的一些列，并按序显示 ；（so即show order的缩写，加前缀是为了和普通的class区分开来，这个是有逻辑意义的）-->
												<th  style="text-align: center;width: 40%;" >
													Key名称
												</th>
											<th  style="width: 10%;text-align: center;">
													属性名称
												</th>
												<th  style="width: 10%;text-align: center;">
													告警模型
												</th>
												<th style="width: 10%;text-align: center;">
													告警值
												</th>
												<th style="width: 15%;text-align: center;">
													告警时间
												</th>
												<th style="width: 15%;text-align: center;">
													告警原因
												</th>
											</tr>
										</thead>
										<tbody>
											<c:forEach items="${pagination.list}" var="item">
												<tr>
													<td style="text-align: center;" >
														${ item.key_name}
													</td>
												<td style="text-align: center;">
														${ item.property_name}
													</td>
													<td style="text-align: center;">
														${ item.mode_name}
													</td>
													<td style="text-align: center;">
														${ item.alarm_value}
													</td>
															<td style="text-align: center;">
														<fmt:formatDate value="${ item.alarm_time}" pattern="yyyy-MM-dd HH:mm:ss"/>  
													</td>
															<td style="text-align: center;">
														${ item.alarm_cause}
													</td>
												</tr>
											</c:forEach>
									</table>
									<c:if test="${pagination.pageNo > 1}">
										<a href="${pagination.pageNo-1 }" id="link1">上一页</a>
									</c:if>
									当前页：${pagination.pageNo}
									<c:if test="${pagination.pageNo < pagination.totalPage}">
										<a href="${pagination.pageNo+1 }" id="link2">下一页</a>
									</c:if>

									共${pagination.totalPage}页 跳转：
									<select name="id" id="select3" style="width: 50px">
										<c:forEach begin="1" end="${pagination.totalPage}"
											var="number">
											<option value="${number}"
												<c:if test="${number==pagination.pageNo }">selected="selected"</c:if>>
												${number }
											</option>
										</c:forEach>
									</select>

								</div>
								<hr/>
								<div class="row">
								<h3 align="center">调用量统计</h3>
									<span>
									默认显示最近10分钟调用信息，查看更多信息->
									<%
										if(keyName != null && keyName.startsWith("PV`")) {
										  %>
											<a href="<%=request.getContextPath()%>/app/detail/apache/show.do?method=gotoSourceDetail&appId=${appInfo.appId}" target="_blank">自身URL-详细</a>	  
										  <%
										} else {
										  %>
											<a href="<%=request.getContextPath()%>/app/detail/hsf/provider/show.do?method=gotohsfProvider&appId=${appInfo.appId}" target="_blank">提供的接口访问信息</a>	  
										  <%										  
										}
									%>
									
									</span>								
								<table class="table table-striped table-condensed table-bordered" style="table-layout: fixed" >
								<tr>
								<th></th>
								<%
								SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
								for(TimeDataInfo info: timeDataInfoList) {
								 %>
								 <th><%=sdf.format(new Date(info.getTime()))%></th>
								 <%
								}
								%>									
								</tr>
								<tr>
								<td>接口调用量(全网):</td>
								<%
								for(TimeDataInfo info: timeDataInfoList) {
								 %>
								 <td><%=info.getMainValue()%></td>
								 <%
								}
								%>									
								</tr>
								</table>
								</div>
		</div>
		</form>
		</body>
		<script type="text/javascript">
	$(function() {
			// 点击翻页链接，提交表单
			$("#link1").click(function() {

						$("#hidden1").attr("value",
								$("#link1").attr("href"));
						$("#form1").submit();
						return false;

					});
			$("#link2").click(function() {
						$("#hidden1").attr("value",
								$("#link2").attr("href"));
						$("#form1").submit();
						return false;

					});

			$("#select3").change(function() {
						$("#hidden1").attr("value",
								$("#select3").attr("value"));
						$("#form1").submit();
						return false;
					});
		});
	</script>
</html>
