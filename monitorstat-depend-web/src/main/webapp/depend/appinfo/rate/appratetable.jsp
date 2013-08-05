<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=GB18030"
	isELIgnored="false" pageEncoding="GB18030"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/themes/icon.css">
<%%>

<title>接口调用比例(beta)</title>
</head>
<body>
<%@ include file="../../header.jsp"%>
		<div align="center">
			<h1>接口调用比例(beta)</h1>
		</div>
		<div>
		<strong>说明：</strong><br/>
		1.接口调用路径及比例展示分为两部分，<strong>按接口（调用汇总）展示</strong>和<strong>按业务（URL）展示</strong>。<br/>
		2.调用比例指根节点（起始的URL或接口）发起1次调用，调用子节点（接口）的次数。所有调用关系和调用比例数据来自EagleEye的线上的埋点数据。<br/>
		3.方法如果被重载的话，查询时候默认去掉末尾的标记(如:com.taobao.item.service.ItemQueryService:1.0.0_queryItemById~lQA变为com.taobao.item.service.ItemQueryService:1.0.0_queryItemById)<br/>
		</div>
		<form action="" method="post" style="display: none">
			<input type="hidden" value="${appName}" name="appName" id="appName" />
			<table>
				<tr>
					<td>查询时间<input type="text" id="startDate" value="${startDate}"
						name="startDate"
						onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"
						class="span2" />
					<td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="查询"
						class="btn btn-success"></td>
				</tr>
			</table>
		</form>
		<table id="topoAll" title="1.调用路径（汇总）" class="easyui-treegrid" style="width:1200px;"
			url='<%=request.getContextPath() %>/rate/callrate.do?method=getTopoBySourceKey&sourceKey=${sourceKey}'
			rownumbers="true"
			idField="id" treeField="keyName">
			<thead>
				<tr>
					<th field="keyName" width="1100">名称</th>
					<th field="rate" width="100">调用比例</th>
				</tr>
			</thead>
		</table>
		<br/>
		<table id="topoURL" title="2.调用路径（以URL区分）" class="easyui-treegrid" style="width:1200px;"
			url='<%=request.getContextPath() %>/rate/callrate.do?method=getTopoByOriginKey&sourceKey=${sourceKey}'
			rownumbers="true"
			idField="id" treeField="keyName">
			<thead>
				<tr>
					<th field="keyName" width="1100">名称</th>
					<th field="rate" width="100">调用比例</th>
				</tr>
			</thead>
		</table>	
</body>
</html>