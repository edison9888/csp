<%@page import="com.taobao.csp.depend.util.MethodUtil"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.*"%>
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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript"
	src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/themes/icon.css">
<%%>
<title>�ӿڵ��ñ���(beta)</title>
<%
	String appName = (String)request.getAttribute("appName");
	String keyName = (String)request.getAttribute("keyName");
	String dependJsonData = (String)request.getAttribute("dependJsonData");
%>
<script type="text/javascript">
$(document).ready(function(){
	$('#topoAll').treegrid({
		idField:'id',  
		width: '800px',
		height: 'auto',
		treeField: 'keyName',	//id
		nowrap:false,
		iconCls:'icon-save',
		striped:true,
		loadMsg:'���ݼ�����......',
		columns:[[	//�������������ţ����
		{field:'keyName',title:'key����',width:660,align:'left',  
			//��ӳ����� 
			formatter:function(value,rowData,rowIndex){
			   if(rowData.black == true) {
				   return "<a href='<%=request.getContextPath()%>/show/dependmap.do?method=gotoKeyDetail&appName=" + rowData.appName + "&keyName=" + value + "' target='keydetailiframe' style='color: red' title='key���������������򸸽ڵ��Ǻ�����'>" +value + "</a>";				   
			   } else {
				   return "<a href='<%=request.getContextPath()%>/show/dependmap.do?method=gotoKeyDetail&appName=" + rowData.appName + "&keyName=" + value + "' target='keydetailiframe'>" +value + "</a>";
			   }
		    }  
		},
		{field:'appName',title: 'Ӧ����',align: 'left',width:80},
		{field:'rate',title:'����',width:50,align:'left'}
		]]});
	
	var jsonstr = '<%=dependJsonData%>';
	var data = $.parseJSON(jsonstr); 
	$('#topoAll').treegrid('loadData', data); //�����ݰ󶨵�datagrid
});
</script>
</head>
<body>
	<%@ include file="../header.jsp"%>
	<div align="center" style="font-size: x-large;">����������������</div>
	<div align="left" style="font-size: large;">Ӧ�ã�${appName}��key��${keyName}</div>
	<div align="left" style="font-size: large;color: red;">������������ӥ�ۣ�Eagleeye�������ݣ����ñ��������ο�</div>
	<hr/>
	<div style="width: 1400px;">
		<div style="width: 800px;float: left;">
			<table id="topoAll"></table>
		</div>		
		<div style="width: 600px;float: right; frameborder='no' border='0';height: 400px;" >
			<iframe id="keydetailiframe" name="keydetailiframe" 
			src="<%=request.getContextPath()%>/show/dependmap.do?method=gotoKeyDetail&appName=<%=appName%>&keyName=<%=keyName%>" width="100%" height="100%">
			</iframe>
		</div>				
	</div>
</body>
</html>