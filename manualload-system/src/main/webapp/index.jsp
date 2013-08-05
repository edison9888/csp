<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@page import="com.taobao.www.arkclient.csp.ManualCurUser"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>全量压测-首页</title>
<link rel="stylesheet" href="./style/style.css">
<link rel="shortcut icon" type="image/png" href="./images/favicon.png">
<link rel="stylesheet" href="./style/bootstrap-1.4.0.css">
</head>
<body>
	<div id="header" class="topbar">
		<div class="fill">
			<div class="container fixed">
				<h3>
					<a href="/manualload/index.do">manualload&nbsp;<img
						src="images/logoicon.png" />
					</a>
				</h3>
				<ul class="menu">
					<li class="dropdown" data-dropdown="dropdown"><a
						href="addPressures.do" class="dropdown-toggle">新增配置</a></li>
					<li class="dropdown" data-dropdown="dropdown"><a
						href="getAllPressures.do" class="dropdown-toggle">压测应用</a></li>
					<li class="dropdown" data-dropdown="dropdown"><a
						href="getAllPreConfigs.do" class="dropdown-toggle">压测配置</a></li>
					<li class="dropdown" data-dropdown="dropdown"><a
						href="getAllPresureResultInfo.do" class="dropdown-toggle">压测结果</a>
					</li>
					<li class="dropdown" data-dropdown="dropdown"><a
						href="getAllAppMachineByAppId.do" class="dropdown-toggle">压测机器</a>
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
		<div id="bd" style="width: 990px; height: 20px; margin: 0 auto"></div>
		<div
			style="width: 990px; height: 100px; margin: 0 auto; font-size: 14px;">
			<div class="indexpills-div" style="width: 350px; margin-left: 100px;">
				<ul class="nav basic indexpills">
					<li><a href="addPressures.do">新增配置</a></li>
				</ul>
			</div>
			<div class="indexpills-div" style="width: 300px;">
				<ul class="nav basic indexpills">
					<li><a href="getAllPresureResultInfo.do">压测结果</a></li>
				</ul>
			</div>
			<br /> <br /> <br />
			<div class="indexpills-div" style="width: 350px; margin-left: 100px;">
				<ul class="nav basic indexpills">
					<li><a href="getAllPressures.do">压测应用</a></li>
				</ul>
			</div>
			<div class="indexpills-div" style="width: 300px;">
				<ul class="nav basic indexpills">
					<li><a href="getAllPreConfigs.do">压测配置</a></li>
				</ul>
			</div>
		</div>
	</div>
	<div
		style="width: 990px; height: 1px; margin: 0px auto; padding: 0px; background-color: #D5D5D5; overflow: hidden;"></div>
	<footer class="footer">
	<div style="float: left;">
		<p align="left">
			Powered by <strong>淘宝网产品技术部-中间件&稳定性平台</strong> <a
				href="mailto://wb-tangjinge@taobao.com"> <img
				src="./images/mail.png"></img>
			</a> <a target="_blank"
				href="http://amos.im.alisoft.com/msg.aw?v=2&uid=tangjinge8566&site=cntaobao&s=2&charset=utf-8">
				<img border="0"
				src="http://amos.im.alisoft.com/online.aw?v=2&uid=tangjinge8566&site=cntaobao&s=2&charset=utf-8" />
			</a> <a href="mailto://youji.zj@taobao.com"> <img
				src="./images/mail.png"></img>
			</a> <a target="_blank"
				href="http://amos.im.alisoft.com/msg.aw?v=2&uid=游骥&site=cntaobao&s=2&charset=utf-8">
				<img border="0"
				src="http://amos.im.alisoft.com/online.aw?v=2&uid=游骥&site=cntaobao&s=2&charset=utf-8" />
			</a> <br> Copyright@ <strong>2011-2012 Taobao.</strong> All Rights
			Reserved
		</p>
	</div>
	<div style="float: right; margin-top: 7px;">
		<p class="pull-right" align="right">
			<a href="#">Back to top</a> <br> <a
				href="http://hotspot.taobao.net:9999/hotspot/appScoreAnalysis"
				target="_blank">Hotspot系统</a> &nbsp;&nbsp;&nbsp;&nbsp; <a
				href="http://cm.taobao.net:9999/monitorstat/index.jsp"
				target="_blank">CSP系统</a> &nbsp;&nbsp;&nbsp;&nbsp; <a
				href="http://monitor.taobao.com/monitorportal/main/welcome.htm"
				target="_blank">哈勃监控</a> &nbsp;&nbsp;&nbsp;&nbsp; <a
				href="http://wpo.taobao.net/" target="_blank">鱼眼前端数据平台</a>
		</p>
	</div>
	</footer>
</body>
</html>