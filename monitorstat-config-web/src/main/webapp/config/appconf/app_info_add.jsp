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
	
	<script>
		$(function() {

			$("#close").click(function() {
				var groupName = $("#group").val();
				if (groupName=="") {
					groupName="AllApp";
				}
				var coderName = encodeURI(encodeURI(groupName));
				location.href="appconfig.do?method=showAppInfo&groupName=" + coderName;
			});
			
		    $("#update").click( function() {
			    
		    	var str = "appconfig.do?method=addAppDetail&appName="+$("#appName").val()+"&appType="+$("#appType").val()+"&feature="+$("#feature").val()
				+"&opsName="+$("#opsName").val()+"&opsField="+$("#opsField").val()+"&group="+$("#group").val()+"&rushTime="+$("#rushTime").val()+"&userName="+$("#userName").val()+"&password="+$("#password").val()
				+"&dayDeploy="+$("#dayDeploy").val()+"&timeDeploy="+$("#timeDeploy").val()+"&status="+$("#status").val();
				
		    	$.ajax({
					url : str,
				    type: 'GET',
				    dataType: 'html',
				    timeout: 1000,
				    error: function(){
				        alert('Error loading document');
				    },
				    success: function(xml){
				    	var groupName = $("#group").val();
						var coderName = encodeURI(encodeURI(groupName));
				    	location.href="appconfig.do?method=showAppInfo&groupName=" + coderName;
				    }
				});
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
		<fieldset>
			<div class="clearfix">
				<label>Ӧ������&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="appName" name="appName" size="30" type="text" value="" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>Ӧ�����ͣ�&nbsp;&nbsp;</label>
				<div class="input">
					<select id="appType" name="appType">
						<option value="pv">ǰ��Ӧ��</option>
						<option value="center">centerӦ��</option>
					</select>
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>Feature��&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="feature" name="feature" size="30" type="text" value=""  />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>OPS_Name��&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="opsName" name="opsName" size="30" type="text"  value="" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>OPS_Field��&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="opsField" name="opsField" size="30" type="text"  value="" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>Group��&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="group" name="group" size="30" type="text"  value="" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>�߷���ʱ��Σ�&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="rushTime" name="rushTime" size="30" type="text"  value="" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>��½�û�����&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="userName" name="userName" size="30" type="text"  value="" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>��½���룺&nbsp;&nbsp;</label>
				<div class="input">
					<input class="xlarge" id="password" name="password" size="30" type="password"  value="" />
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>day_Deploy��&nbsp;&nbsp;</label>
				<div class="input">
					<select id="dayDeploy" name="dayDeploy">
						<option value="0">����Ч</option>
						<option value="1">��Ч</option>
					</select>
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>time_Deploy��&nbsp;&nbsp;</label>
				<div class="input">
					<select id="timeDeploy" name="timeDeploy">
						<option value="0">����Ч</option>
						<option value="1">��Ч</option>
					</select>
				</div>
			</div>
			<br>
			<div class="clearfix">
				<label>״̬��&nbsp;&nbsp;</label>
				<div class="input">
					<select id="status" name="status">
						<option value="0">����</option>
						<option value="1">ɾ��</option>
					</select>
				</div>
			</div>
			
			<div class="actions">
				<input type="button" class="btn primary" id="update" value="���Ӧ��" >
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="btn primary" type="button" id="close" value="����" >
			</div>
		</fieldset>
		<footer>
		  <p>&copy; TaoBao 2011</p>
		</footer>
		
	</div>
</div>

</body>
</html>