<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>ʵʱģ�����</title>

<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<style type="text/css">
	body {
	  padding-top: 60px;
	}
</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript">

function goToTmpPage() {
	var str="<%=request.getContextPath() %>/show/appconfig.do?method=gotoTimeTmp";
	location.href = str;
}

function obtainChange(type){
	if(type == 1){
		$("#obtainId").text("�ļ�·��:");
		$("#tailId").show();
	}
	if(type == 2){
		$("#obtainId").text("shell�ű�:");
		$("#tailId").hide();
	}
	if(type == 3){
		$("#obtainId").text("http-url:");
		$("#tailId").hide();
	}
}

function analyseChange(type){
	if(type == 1){
		$("#classId").show();
		$("#scriptId").show();
		$("#scriptContentId").text("�������ĸ�������:");
		
	}
	if(type == 2){
		$("#classId").hide();
		$("#scriptId").show();
		$("#scriptContentId").text("�ű�����:");
	}
}
</script>
</head>
<body>
<jsp:include page="../../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2>ʵʱ����ģ�����</h2>
		</div>
		<form action="<%=request.getContextPath() %>/show/appconfig.do?method=addTimeTmp" method="post">
		<table class="bordered-table zebra-striped condensed-table">	
			<tr>
				<td>ģ������: </td>
				<td><input type="text" name="aliasLogName"  value=""></td>
			</tr>
			
			<tr>
				<td>�����зָ���: </td>
				<td>
					<select name="splitChar">
						<option value="\n">\n</option>
						<option value="\02">\02</option>
					</select>
				</td>
			</tr>	
			<tr>
				<td>�ռ�Ƶ��: </td>
				<td><input type="text" name="frequency" value="">&nbsp;&nbsp;��</td>
			</tr>
			
			<tr>
				<td>���ݻ�ȡ: </td>
				<td>
					<select id="obtainTypeid" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
						<option value="1">��־�ļ�</option>
						<option value="2">shell��ʽ</option>
						<option value="3">http��ʽ</option>
					</select>
				</td>
			</tr>
			<tr>
				<td id="obtainId">�ļ�·��: </td>
				<td><input type="text" name="filePath" value="" style="width:350px;"><p>�������������Ϊִ��ssh��ִ�з��������뽫sshָ��д����</td>
			</tr>
			<tr id="tailId">
				<td>tailģʽ: </td>
				<td>
					<select name="tailType">
						<option value="line">��</option>
						<option value="char">�ֽ�</option>
					</select>
				</td>
			</tr>	
			<tr>
				<td>������ʽ: </td>
				<td>
					<select id="analyseTypeid" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
						<option value="1">java-class</option>
						<option value="2">javscript</option>
					</select>
				</td>
			</tr>	
			<tr id="classId">
				<td>java������: </td>
				<td><input type="text" name="className" value="" style="width:350px;"></td>
			</tr>
			<tr id="scriptId">
				<td id="scriptContentId">�������ĸ�������: </td>
				<td><textarea style="width:350px;" rows="2" name="future"></textarea><p>����Ƕ�ȡ��־��ִ��javascript������,�뽫javascriptд����</td>
			</tr>
			<tr>
				<td>����˵��: </td>
				<td>
					<textarea style="width:350px;" rows="2" name="analyseDesc" value="aaaa"></textarea>
				</td>
			</tr>
		</table>
		<div class="well" style="padding: 14px 19px;">
			<center>
	        	<input class="btn primary" type="submit" value="���ģ��"">&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="btn primary" type="button" value="����" onclick="goToTmpPage()">
			</center>
      	</div>
      	</form>
      </div>
	  <footer>
		  <p>&copy; TaoBao 2011</p>
	  </footer>
</div>
</body>
</html>