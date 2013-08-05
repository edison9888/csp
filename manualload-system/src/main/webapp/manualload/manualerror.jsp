<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>全量压测-错误页面</title>
		<link rel="stylesheet" href="${baseUrl}/style/style.css">
		<link rel="shortcut icon" type="image/png" href="${baseUrl}/images/favicon.png">
		<link rel="stylesheet" href="${baseUrl}/style/bootstrap-1.4.0.css">
		<script type="text/javascript">
		function fanhuiHome(){
		   window.location.href="${baseUrl}/index.do";
		}
		</script>

	</head>
	<body>
		<div id="header" class="topbar">
			<div class="fill">
				<div class="container fixed">
					<h3>
						<a href="/manualload/index.do">manualload&nbsp;<img
								src="${baseUrl}/images/logoicon.png" /> </a>
					</h3>
					<ul class="menu">
						<li class="dropdown" data-dropdown="dropdown">
							<a href="addPressures.do" class="dropdown-toggle">新增配置</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
							<a href="getAllPressures.do" class="dropdown-toggle">压测应用</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
							<a href="getAllPreConfigs.do" class="dropdown-toggle">压测配置</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
							<a href="getAllPresureResultInfo.do" class="dropdown-toggle">压测结果</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
							<a href="getAllAppMachineByAppId.do" class="dropdown-toggle">压测机器</a>
						</li>
					</ul>
					<div style="color: white; float: right; padding-top: 9px;">
						欢迎您:${manualCurUser}来到全量压测系统！
					</div>
				</div>
			</div>
		</div>
		<div class="container">
			<div style="width: 990px; height: auto; margin: 0 auto">
				<div align="center" style="color: #42413C;">
					<table align="center" width="600">
						<tr>
							<td align="left">
									1、你请求的URL地址不存在！
							</td>
						</tr>
						<tr>
							<td align="left">
									 2、错误的URL地址：<s:property value="#request.gobleString" />
							</td>
						</tr>
						<br />
						<br />
						<br />
						<br />
						<br />
						<tr>
							<td align="center">
								<button data-controls-modal="modal-from-dom"
									data-backdrop="true" data-keyboard="true" class="btn info"
									onClick="fanhuiHome()">
									返回首页
								</button>
							</td>
						</tr>
					</table>
				</div>
			</div>
<jsp:include page="./footer.jsp"></jsp:include>

