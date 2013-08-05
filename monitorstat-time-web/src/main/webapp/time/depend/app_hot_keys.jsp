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

<title>应用热点接口分布</title>
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}
table {
	padding-left: 10px;
}
</style>
<%
Integer timeinterval = (Integer)request.getAttribute("timeinterval");
if(timeinterval == null)
  timeinterval = 0;
%>
</head>
<body>
<%@ include file="../../header.jsp"%>
		<div align="center">
			<h1>核心应用热点接口信息</h1>
		</div>
<div class="container-fluid">
<span>
<strong>默认显示最近${timeinterval}的分钟的接口调用信息（括号内红色数字为当前分钟内的报警数量）,每个应用的接口按调用量显示前10个</strong>
</span>
<div class="row-fluid" style="text-align: center">
		<table id="topoAll" style="width:1380px;" title="核心应用热点接口信息统计（全网）">
		</table>
		<br/>
		</div></div>
</body>
<script type="text/javascript">
$('#topoAll').treegrid({  
    url:'<%=request.getContextPath() %>/app/depend/query/show.do?method=queryHotInterfaceMultiJson&appNameStr=${appNameStr}&company=${company}',  
    idField:'id',  
    treeField:'keyName',  
    loadMsg:'数据加载中请稍后……',
    //frozenColumns:[[{title:'Task Name',field:'keyName',width:500}]],	//固定某列，可选
    columns:[[  
        {title:'名称',field:'keyName',width:650,
       	 formatter:function(value, rowData){//qps 这列为虚拟列，占位使用
       		var appName = rowData['appName'];
       		var targetUrl = "<%=request.getContextPath() %>/app/depend/query/show.do?method=hotKeyGotoTimeReal&appName=" + appName + "&keyName=" + value;
       		var showMsg = value;
          	//return showMsg + '&nbsp;<a href="' + targetUrl + '" target="_blank"><strong>查看历史数据</strong></a>&nbsp;<a href="' + targetUrl + '" target="_blank"><strong>查看实时数据</strong></a>';
          	return value + '&nbsp;<a href="' + targetUrl + '" target="_blank"><strong>查看更多</strong></a>';
          }
        }, 
        <%
        for(int i=0; i<timeinterval; i++){
        %>
	        {field:'<%="time" + i%>',title:'<%="    "%>',width:70,align:'center',
	       	 formatter:function(value, rowData){
	       		 	var value = rowData['addedPo'].map.<%="time" + i%>;
	       		 	var appNameTmp = rowData.appName;
	       		 	var keyNameTmp = rowData.keyName;
 		            if(appNameTmp != undefined && keyNameTmp != undefined && appNameTmp == keyNameTmp) {
 		       		 	if(value == undefined)
 		       		 		value = "暂无";
		            	return "<strong>" + value + "</strong>"
		            } else {
 		       		 	if(value == undefined)
 		       		 		return value = "-";	
 		       		 	else
		            		return "<a href='<%=request.getContextPath() %>/app/depend/query/show.do?method=gotoAlarmDetail&appName=" + appNameTmp + "&keyName=" + keyNameTmp + "' target='_blank'>" + value + "</a>";
		            }
	            }
	       	},        
        <%	  
        }
        %>
        {field:'',title:'',width:5,align:'center',
       	 formatter:function(value, rowData){
       		 //占位
         }
       }        
    ]]
});

//$('#topoURL').treegrid({  
//url:'<%=request.getContextPath()%>/app/depend/query/show.do?method=queryAlarmMultiJson&appName=${appName}&keyName=${keyName}',  
//idField:'id',  
//treeField:'keyName',  
////frozenColumns:[[{title:'Task Name',field:'keyName',width:500}]],  //固定某列，可选
//columns:[[  
//  {title:'名称',field:'keyName',width:1000},  
//  {field:'addedPo',title:'报警次数',width:100,align:'left',
//     formatter:function(value){
//        return '<a href="#"><span style="color:red">' + value.alarmCount + '</span></a>';
//       }
//  },  
//  {field:'rate',title:'调用比例',width:100}  
//]]  
//});
</script>
</html>