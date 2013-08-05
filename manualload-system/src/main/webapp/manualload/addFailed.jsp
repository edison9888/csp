<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>全量压测-<s:property value="#request.title" />
		</title>
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
								<p> 亲，您没有权限操作.</p> 
						    </td>
						    </tr>
						<tr>
							<td align="left">
                                <p>1、之前有权限的，应该是浏览器证书cookie过期，请关闭浏览器后再试试 <p>
                            </td>
                            </tr>
                            <tr>
							<td align="left">
                                <p>2、从未申请过权限的，请联系 游骥<a target="_blank"
					href="http://amos.im.alisoft.com/msg.aw?v=2&uid=游骥&site=cntaobao&s=2&charset=utf-8">
					<img border="0"
						src="http://amos.im.alisoft.com/online.aw?v=2&uid=游骥&site=cntaobao&s=2&charset=utf-8" />
				       </a>   
                                <p>
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

