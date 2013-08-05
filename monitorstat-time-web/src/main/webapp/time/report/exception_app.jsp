<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=GBK"%>
	<%@page import="com.taobao.csp.dataserver.Constants"%>
<%@page import="com.taobao.csp.dataserver.KeyConstants"%>
<%@ page import="java.text.*"
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
	<head>
		<link href="http://time.csp.taobao.net:9999/time/statics/css/bootstrap.css"
			rel="stylesheet">
		<link
			href="http://time.csp.taobao.net:9999/time/statics/css/bootstrap-responsive.css"
			rel="stylesheet">
		<script
			src="http://time.csp.taobao.net:9999/time/statics/js/jquery/jquery.min.js"></script>
		<script src="http://time.csp.taobao.net:9999/time/statics/js/bootstrap.js"></script>


		<style type="text/css">
body {
	padding-top: 60px;
}
</style>
	</head>

	<body>
		<div class="container">
			<div class="row">
				<div class="span12" style="text-align: center; padding-bottom: 30px;">
					<% 
						Calendar c = Calendar.getInstance(); 
						c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) - 1); 
						Date yesterday = c.getTime(); 
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); 
						String yd = sdf.format(yesterday); 
						 
					%>
					<h3>异常报表 —<%=yd %></h3>
				</div>
			</div>
			<div class="row">
				<div class="span12">
					<table class="table table-striped table-condensed table-bordered"
						id="table1" style="table-layout: fixed">
						<thead>
							<tr>
								<!-- class用于指定要显示的一些列，并按序显示 ；（so即show order的缩写，加前缀是为了和普通的class区分开来，这个是有逻辑意义的）-->
								<th style="text-align: center;">
									应用
								</th>
								<th style="text-align: center;">
									异常总量
								</th>
								<th style="text-align: center;">
									最大值
								</th>


								<th style="text-align: center;">
									平均值
								</th>
								<th style="text-align: center;">
									走势图
								</th>
								<th style="text-align: center;">
									详情
								</th>
							</tr>
						</thead>
						<tbody>

							<c:forEach items="${reportPoList}" var="item">
								<tr>
									<td style="text-align: center;" title="">

										${ item.appName}

									</td>


									<td style="text-align: center;">
										${ item.total}
									</td>

									<td style="text-align: center;">


										${ item.max}&nbsp;&nbsp;<c:if test="${! empty item.maxPointTime}">(${item.maxPointTime })</c:if> 
									</td>


									<td style="text-align: center;">


										${ item.average}
									</td>
									<td style="text-align: center;" >
										<a target="_blank" href='http://time.csp.taobao.net:9999/time/app/detail/history.do?method=showHistory&appName=${item.appName}&keyName=<%=KeyConstants.EXCEPTION %>'>走势图</a>
									</td>
									<td style="text-align: center;">
										<a target="_blank" href="http://time.csp.taobao.net:9999/time/app/detail/exception/show.do?method=exceptionReport&appName=${item.appName}">详情</a>
									</td>
								</tr>
							</c:forEach>
					</table>
				</div>
			</div>
		</div>
</html>
