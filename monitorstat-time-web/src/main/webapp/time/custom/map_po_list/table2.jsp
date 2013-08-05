<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>实时监控系统</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>
<script type="text/javascript">
		$(document).ready(function() {
			/*
			 *  Simple image gallery. Uses default settings
			 */

			$('.fancybox').fancybox();

			$("a.fancybox fancybox.iframe").fancybox({
				width			: '75%',
				height			: '75%',
		        autoScale     	: true,
		        transitionIn	: 'none',
				transitionOut	: 'none',
				type			: 'iframe'
			});	
			$("a.fancybox fancybox.iframe").fancybox().mouseover(function() {
			    $(this).click();
			});
		});
	</script>
	<style type="text/css">
		.fancybox-custom .fancybox-skin {
			box-shadow: 0 0 50px #222;
		}
	</style>
</head>
<body>
	<div style="height:300pt;overflow:scroll;width:50%">
	<table class="table table-striped table-bordered table-condensed"
		id="mapPoListTable" >
		<caption>
			<strong>${appName } ${keyName }</strong>
		</caption>
		<thead>
			<tr>
				<td width="200" style="text-align: center;">子KEY</td>
				<td>主机详情</td>
			</tr>
			<c:forEach var="item" items = "${childKeys }">
			<tr>
				<td width="200" style="text-align: center;">${item}</td>
			
				<td width="200"><a class="fancybox fancybox.iframe" target="_blank" href="<%=request.getContextPath() %>/app/detail/custom/show.do?method=querySingleHostRealTime&appName=${appName}&keyName=${item}&property=${property}&viewMod=mapPoListTable3">查看详情</a></td>
			
				</tr>
			</c:forEach>
		</thead>
		<tbody id="mapPoListTableBody">
		</tbody>
	</table>
	</div>
	</body>
</html>