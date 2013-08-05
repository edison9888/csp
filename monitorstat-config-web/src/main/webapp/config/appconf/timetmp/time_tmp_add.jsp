<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>实时模板管理</title>

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
		$("#obtainId").text("文件路径:");
		$("#tailId").show();
	}
	if(type == 2){
		$("#obtainId").text("shell脚本:");
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
		$("#scriptContentId").text("分析器的附带参数:");
		
	}
	if(type == 2){
		$("#classId").hide();
		$("#scriptId").show();
		$("#scriptContentId").text("脚本程序:");
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
			<h2>实时分析模板更新</h2>
		</div>
		<form action="<%=request.getContextPath() %>/show/appconfig.do?method=addTimeTmp" method="post">
		<table class="bordered-table zebra-striped condensed-table">	
			<tr>
				<td>模版名称: </td>
				<td><input type="text" name="aliasLogName"  value=""></td>
			</tr>
			
			<tr>
				<td>数据行分隔符: </td>
				<td>
					<select name="splitChar">
						<option value="\n">\n</option>
						<option value="\02">\02</option>
					</select>
				</td>
			</tr>	
			<tr>
				<td>收集频率: </td>
				<td><input type="text" name="frequency" value="">&nbsp;&nbsp;秒</td>
			</tr>
			
			<tr>
				<td>数据获取: </td>
				<td>
					<select id="obtainTypeid" name="obtainType" onchange="obtainChange(this.options[this.selectedIndex].value)">
						<option value="1">日志文件</option>
						<option value="2">shell方式</option>
						<option value="3">http方式</option>
					</select>
				</td>
			</tr>
			<tr>
				<td id="obtainId">文件路径: </td>
				<td><input type="text" name="filePath" value="" style="width:350px;"><p>如果分析器类型为执行ssh后执行分析器，请将ssh指令写这里</td>
			</tr>
			<tr id="tailId">
				<td>tail模式: </td>
				<td>
					<select name="tailType">
						<option value="line">行</option>
						<option value="char">字节</option>
					</select>
				</td>
			</tr>	
			<tr>
				<td>分析方式: </td>
				<td>
					<select id="analyseTypeid" name="analyseType" onchange="analyseChange(this.options[this.selectedIndex].value)">
						<option value="1">java-class</option>
						<option value="2">javscript</option>
					</select>
				</td>
			</tr>	
			<tr id="classId">
				<td>java分析器: </td>
				<td><input type="text" name="className" value="" style="width:350px;"></td>
			</tr>
			<tr id="scriptId">
				<td id="scriptContentId">分析器的附带参数: </td>
				<td><textarea style="width:350px;" rows="2" name="future"></textarea><p>如果是读取日志后执行javascript分析器,请将javascript写这里</td>
			</tr>
			<tr>
				<td>描述说明: </td>
				<td>
					<textarea style="width:350px;" rows="2" name="analyseDesc" value="aaaa"></textarea>
				</td>
			</tr>
		</table>
		<div class="well" style="padding: 14px 19px;">
			<center>
	        	<input class="btn primary" type="submit" value="添加模板"">&nbsp;&nbsp;&nbsp;&nbsp;
				<input class="btn primary" type="button" value="返回" onclick="goToTmpPage()">
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