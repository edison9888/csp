<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.taobao.www.arkclient.csp.ManualCurUser"%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>全量压测-错误信息页面</title>
		<link rel="stylesheet" href="./style/style.css">
		<link rel="shortcut icon" type="image/png" href="./images/favicon.png">
		<link rel="stylesheet" href="./style/bootstrap-1.4.0.css">
		<script type="text/javascript">
		function goHome(){
		   window.locatin.href="./index.jsp";
		}
		</script>
	</head>
	<body>
		<div id="header" class="topbar">
			<div class="fill">
				<div class="container fixed">
					<h3>
						<a href="./index.jsp">manualload&nbsp;<img
								src="images/logoicon.png" /> </a>
					</h3>
					<ul class="menu">
						<li class="dropdown" data-dropdown="dropdown">
							<a href="getAllPressures.do" class="dropdown-toggle">压测应用</a>
						</li>
						<li class="dropdown" data-dropdown="dropdown">
							<a href="addPressures.do" class="dropdown-toggle">新增配置</a>
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
					<div style="color:white;float:right;padding-top:10px;">欢迎您:
					<% 
					  out.print(ManualCurUser.getLoginUserName(request)); 
					 %>
					来到全量压测系统！
					</div>
			 </div>
			</div>
		</div>
		<div class="container">
			<div style="width: 990px; height: auto; margin: 0 auto">
				<div align="center" style="color: #42413C;">
					<table align="center">
						<tr>
							<td align="center">
							    <p style="font-size: 14; font-weight: bold; color: red; text-align: center;">
									${gobleString}
								</p>
							</td>
						</tr>
						<tr>
							<td align="center">
								<div class="alert-message block-message success">
									<button type="submit" class="btn primary" onClick="goHome()">
										返回首页
									</button>
								</div>
							</td>
						</tr>
					</table>
				</div>
			</div>
			</div>
			<jsp:include page="./manualload/footer.jsp"></jsp:include>