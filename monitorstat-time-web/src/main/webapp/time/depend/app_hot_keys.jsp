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

<title>Ӧ���ȵ�ӿڷֲ�</title>
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
			<h1>����Ӧ���ȵ�ӿ���Ϣ</h1>
		</div>
<div class="container-fluid">
<span>
<strong>Ĭ����ʾ���${timeinterval}�ķ��ӵĽӿڵ�����Ϣ�������ں�ɫ����Ϊ��ǰ�����ڵı���������,ÿ��Ӧ�õĽӿڰ���������ʾǰ10��</strong>
</span>
<div class="row-fluid" style="text-align: center">
		<table id="topoAll" style="width:1380px;" title="����Ӧ���ȵ�ӿ���Ϣͳ�ƣ�ȫ����">
		</table>
		<br/>
		</div></div>
</body>
<script type="text/javascript">
$('#topoAll').treegrid({  
    url:'<%=request.getContextPath() %>/app/depend/query/show.do?method=queryHotInterfaceMultiJson&appNameStr=${appNameStr}&company=${company}',  
    idField:'id',  
    treeField:'keyName',  
    loadMsg:'���ݼ��������Ժ󡭡�',
    //frozenColumns:[[{title:'Task Name',field:'keyName',width:500}]],	//�̶�ĳ�У���ѡ
    columns:[[  
        {title:'����',field:'keyName',width:650,
       	 formatter:function(value, rowData){//qps ����Ϊ�����У�ռλʹ��
       		var appName = rowData['appName'];
       		var targetUrl = "<%=request.getContextPath() %>/app/depend/query/show.do?method=hotKeyGotoTimeReal&appName=" + appName + "&keyName=" + value;
       		var showMsg = value;
          	//return showMsg + '&nbsp;<a href="' + targetUrl + '" target="_blank"><strong>�鿴��ʷ����</strong></a>&nbsp;<a href="' + targetUrl + '" target="_blank"><strong>�鿴ʵʱ����</strong></a>';
          	return value + '&nbsp;<a href="' + targetUrl + '" target="_blank"><strong>�鿴����</strong></a>';
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
 		       		 		value = "����";
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
       		 //ռλ
         }
       }        
    ]]
});

//$('#topoURL').treegrid({  
//url:'<%=request.getContextPath()%>/app/depend/query/show.do?method=queryAlarmMultiJson&appName=${appName}&keyName=${keyName}',  
//idField:'id',  
//treeField:'keyName',  
////frozenColumns:[[{title:'Task Name',field:'keyName',width:500}]],  //�̶�ĳ�У���ѡ
//columns:[[  
//  {title:'����',field:'keyName',width:1000},  
//  {field:'addedPo',title:'��������',width:100,align:'left',
//     formatter:function(value){
//        return '<a href="#"><span style="color:red">' + value.alarmCount + '</span></a>';
//       }
//  },  
//  {field:'rate',title:'���ñ���',width:100}  
//]]  
//});
</script>
</html>