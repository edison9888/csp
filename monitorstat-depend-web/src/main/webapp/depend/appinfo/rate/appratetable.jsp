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

<title>�ӿڵ��ñ���(beta)</title>
</head>
<body>
<%@ include file="../../header.jsp"%>
		<div align="center">
			<h1>�ӿڵ��ñ���(beta)</h1>
		</div>
		<div>
		<strong>˵����</strong><br/>
		1.�ӿڵ���·��������չʾ��Ϊ�����֣�<strong>���ӿڣ����û��ܣ�չʾ</strong>��<strong>��ҵ��URL��չʾ</strong>��<br/>
		2.���ñ���ָ���ڵ㣨��ʼ��URL��ӿڣ�����1�ε��ã������ӽڵ㣨�ӿڣ��Ĵ��������е��ù�ϵ�͵��ñ�����������EagleEye�����ϵ�������ݡ�<br/>
		3.������������صĻ�����ѯʱ��Ĭ��ȥ��ĩβ�ı��(��:com.taobao.item.service.ItemQueryService:1.0.0_queryItemById~lQA��Ϊcom.taobao.item.service.ItemQueryService:1.0.0_queryItemById)<br/>
		</div>
		<form action="" method="post" style="display: none">
			<input type="hidden" value="${appName}" name="appName" id="appName" />
			<table>
				<tr>
					<td>��ѯʱ��<input type="text" id="startDate" value="${startDate}"
						name="startDate"
						onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"
						class="span2" />
					<td>
				</tr>
				<tr>
					<td colspan="2"><input type="submit" value="��ѯ"
						class="btn btn-success"></td>
				</tr>
			</table>
		</form>
		<table id="topoAll" title="1.����·�������ܣ�" class="easyui-treegrid" style="width:1200px;"
			url='<%=request.getContextPath() %>/rate/callrate.do?method=getTopoBySourceKey&sourceKey=${sourceKey}'
			rownumbers="true"
			idField="id" treeField="keyName">
			<thead>
				<tr>
					<th field="keyName" width="1100">����</th>
					<th field="rate" width="100">���ñ���</th>
				</tr>
			</thead>
		</table>
		<br/>
		<table id="topoURL" title="2.����·������URL���֣�" class="easyui-treegrid" style="width:1200px;"
			url='<%=request.getContextPath() %>/rate/callrate.do?method=getTopoByOriginKey&sourceKey=${sourceKey}'
			rownumbers="true"
			idField="id" treeField="keyName">
			<thead>
				<tr>
					<th field="keyName" width="1100">����</th>
					<th field="rate" width="100">���ñ���</th>
				</tr>
			</thead>
		</table>	
</body>
</html>