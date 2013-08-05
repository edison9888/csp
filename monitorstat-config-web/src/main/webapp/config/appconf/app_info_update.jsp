<%@ page language="java" contentType="text/html; charset=GBK"
	isELIgnored="false" pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.net.URLEncoder"%>
<html>
<head>
	<title>Ӧ������</title>
	
	<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
	
	<style type="text/css">
		body {
		  padding-top: 60px;
		}
	</style>
	
	<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
    <script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/bootstrap-modal.js"></script>
	<script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/jquery.tablesorter.js"></script>
	<script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/application.js"></script>
	<%
	AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("appInfoPo");
	Boolean isSuccess = (Boolean)request.getAttribute("isSuccess");
	%>
	
	<script>
		$(function() {
			
			$("#close").click(function() {
				var groupName = $("#groupName").val();
				var coderName = encodeURI(encodeURI(groupName));
				location.href="appconfig.do?method=showAppInfo&groupName=" + coderName;
			});
			
		});
	</script>
	
</head>

<body>

<jsp:include page="../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2>Ӧ����ϸ��Ϣ</h2>
		</div>
		<%
		if (isSuccess != null) {
			if (isSuccess) {
			%>
				<span class="label success">���³ɹ�</span>
			<%
			} else {
			%>
				<span class="label important">����ʧ��</span>
			<%
			}
		}
		%>
		<form action="<%=request.getContextPath()%>/show/appconfig.do?method=updateAppInfo&appId=<%=appInfoPo.getAppId() %>" method="post">
		<fieldset>
			<div class="clearfix">
				<label>Ӧ������&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="appName" name="appName" size="30" type="text" value="<%=appInfoPo.getAppName() %>" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>Ӧ�����ͣ�&nbsp;&nbsp;</label>
				<div class="input">
					<select id="appType" name="appType">
						<option value="pv" <%if("pv".equals(appInfoPo.getAppType())){out.print("selected");} %>>ǰ��Ӧ��</option>
						<option value="center" <%if("center".equals(appInfoPo.getAppType())){out.print("selected");} %>>centerӦ��</option>
					</select>
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>Feature��&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="feature" name="feature" size="30" type="text" value="<%=appInfoPo.getFeature() %>"  />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>OPS_Name��&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="opsName" name="opsName" size="30" type="text"  value="<%=appInfoPo.getOpsName() %>" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>OPS_Field��&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="opsField" name="opsField" size="30" type="text"  value="<%=appInfoPo.getOpsField() %>" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>Group��&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="groupName" name="groupName" size="30" type="text"  value="<%=appInfoPo.getGroupName() %>" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>�߷���ʱ��Σ�&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="appRushHours" name="appRushHours" size="30" type="text"  value="<%=appInfoPo.getAppRushHours() %>" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>��½�û�����&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="loginName" name="loginName" size="30" type="text"  value="<%=appInfoPo.getLoginName() %>" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>��½���룺&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="loginPassword" name="loginPassword" size="30" type="password"  value="<%=appInfoPo.getLoginPassword() %>" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>day_Deploy��&nbsp;&nbsp;</label>
				<div class="input">
					<select id="dayDeploy" name="dayDeploy">
						<option value="<%=(appInfoPo.getDayDeploy() == 0)? 0 : 1%>"><%=(appInfoPo.getDayDeploy() == 0 )? "����Ч" : "��Ч"%></option>
						<option value="<%=(appInfoPo.getDayDeploy() == 0) ? 1 : 0%>"><%=(appInfoPo.getDayDeploy() == 0) ? "��Ч" : "����Ч"%></option>
					</select>
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>time_Deploy��&nbsp;&nbsp;</label>
				<div class="input">
					<select id="timeDeploy" name="timeDeploy">
						<option value="<%=(appInfoPo.getDayDeploy() == 0)? 0 : 1%>"><%=(appInfoPo.getDayDeploy() == 0 )? "����Ч" : "��Ч"%></option>
						<option value="<%=(appInfoPo.getDayDeploy() == 0) ? 1 : 0%>"><%=(appInfoPo.getDayDeploy() == 0) ? "��Ч" : "����Ч"%></option>
					</select>
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>״̬��&nbsp;&nbsp;</label>
				<div class="input">
					<select id="appStatus" name="appStatus">
						<option value="<%=(appInfoPo.getAppStatus() == 0) ? 0 : 1%>"><%=appInfoPo.getAppStatus() == 0 ? "����" : "ɾ��"%></option>
						<option value="<%=(appInfoPo.getAppStatus() == 0) ? 1 : 0%>"><%=appInfoPo.getAppStatus() == 0 ? "ɾ��" : "����"%></option>
					</select>
				</div>
			</div>
			
			<div class="actions">
				<input type="submit" class="btn primary" value="�ύ����" >
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="btn primary" type="button" id="close" value="����" >
			</div>
		</fieldset>
		</form>
		<footer>
		  <p>&copy; TaoBao 2011</p>
		</footer>
		
	</div>
</div>

</body>
</html>