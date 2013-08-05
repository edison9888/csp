<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import=" com.taobao.monitor.common.po.CspTimeKeyDependInfo"%>
<%@ page language="java" contentType="text/html; charset=GBK"
	isELIgnored="false" pageEncoding="GBK"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/easyui/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/statics/css/easyui/icon.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jsPlumb.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/index.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"   src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript"		src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/js/My97DatePicker/WdatePicker.js"></script>			
<title>b2b Ocean应用历史查询</title>
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
table {
	padding-left: 10px;
}
</style>
</head>
<body>
<%@ include file="../../header.jsp"%>
<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>
<%
		String appName = (String)request.getAttribute("appName");
		String keyName = (String)request.getAttribute("keyName");
%>
<form action="<%=request.getContextPath()%>/app/b2b/show.do?method=gotoOceanRootHistory&appId=${appName}&keyName=<%=keyName%>" method="get">
	<input type="hidden" value="gotoOceanRootHistory" name="method">
	<input type="hidden" value="${appInfo.appId}" name="appId">
	<select name="keyName">
		<option value="Ocean_Service_Application">Ocean_Service_Application</option>
		<option value="Ocean_Service_Application_Reverse">Ocean_Service_Application_Reverse</option>
		<option value="Ocean_Service_Exception">Ocean_Service_Exception</option>
	</select>
	&nbsp;&nbsp;查询日期:<input type="text" id="queryTime"
						value="${queryTime}" name="queryTime"
						onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"
						class="span2" />	
	<input type="submit" value="提交">
</form>
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
		<table id="topoAll" title="Ocean应用调用信息(时间：${queryTime})">
		</table>
		<br/>
		</div></div>
</body>
<script type="text/javascript">
$('#topoAll').treegrid({  
    url:'<%=request.getContextPath() %>/app/b2b/show.do?method=showOceanRoot&appName=${appName}&keyName=${keyName}',  
    idField:'uuid',  
    treeField:'map',  //树是否显示层级的
    loadMsg:'数据加载中请稍后……',
    //frozenColumns:[[{title:'Task Name',field:'keyName',width:500}]],	//固定某列，可选
    columns:[[  
        {title:'key名称',field:'map',width:600,
       	 formatter:function(value, rowData){//qps 这列为虚拟列，占位使用
	       		if(value.keyName.lastIndexOf('`') >= 0) {
	       			var keyNameTmp = value.keyName.substring(value.keyName.lastIndexOf('`')+1,value.keyName.length);
		       		return keyNameTmp;	       				
	       		} else 
	       			return value.keyName;
          }
        },
	    {field:'valueField',title:'调用量',width:100,align:'center',
	       	 formatter:function(value, rowData){
		     	var array = rowData['map'].value;
		       	if(array == undefined)
		       		return ""
		       	else {
		       		return array[0];
		       	} 
	       	 }
        },
	    {field:'valueField2',title:'调用时间',width:100,align:'center',
	       	 formatter:function(value, rowData){
		     	var array = rowData['map'].value;
		       	if(array == undefined)
		       		return ""
		       	else {
		       		return array[1];
		       	} 
	       	 }
       }        
    ]],
    onBeforeLoad:function(row,param){
        if (row){
        	if(row.map != undefined)
        		$(this).treegrid('options').url = '<%=request.getContextPath() %>/app/b2b/show.do?method=showOceanSubHistory&appName=${appName}&keyName=' + row.map.keyName;
        	else
        		$(this).treegrid('options').url = '<%=request.getContextPath() %>/app/b2b/show.do?method=showOceanRootHistory&appName=${appName}&keyName=${keyName}&queryTime=${queryTime}';
        } else {
        	$(this).treegrid('options').url = '<%=request.getContextPath() %>/app/b2b/show.do?method=showOceanRootHistory&appName=${appName}&keyName=${keyName}&queryTime=${queryTime}';
        }
       }
});
</script>
</html>