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
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<title>b2b OceanӦ��</title>
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
<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>
		<%
		String appName = (String)request.getAttribute("appName");
		String keyName = (String)request.getAttribute("keyName");
		String title = "";
		//Ocean_Service_Application
		if("Ocean_Service_Application".equals(keyName)) {
			title = "ocean api����";
		} else if("Ocean_Service_Application_Reverse".equals(keyName)) {
			title = "ocean app����";
		} else if("Ocean_Service_Exception".equals(keyName)) {
			title = "ocean �쳣����";
		}
		%>
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
		<table id="topoAll" title="<%=title%>��Ϣ��˵��:ͼ��Ԫ�����������Ϊ������,�Ҳ�Ϊƽ����Ӧʱ�䣩">
		</table>
		<br/>
		</div></div>
</body>
<script type="text/javascript">
$('#topoAll').treegrid({  
    url:'<%=request.getContextPath() %>/app/b2b/show.do?method=showOceanRoot&appName=${appName}&keyName=${keyName}',  
    idField:'uuid',  
    treeField:'map',  //���Ƿ���ʾ�㼶��
    loadMsg:'���ݼ��������Ժ󡭡�',
    //frozenColumns:[[{title:'Task Name',field:'keyName',width:500}]],	//�̶�ĳ�У���ѡ
    columns:[[  
        {title:'key����',field:'map',width:600,
       	 formatter:function(value, rowData){//qps ����Ϊ�����У�ռλʹ��
	       		if(value.keyName == "��ǰʱ��") {
	       			return "<strong></strong>";		       			
	       		} else {
	       			if(value.keyName != undefined) {
	       				if(value.keyName.lastIndexOf('`') >= 0) {
		       				var keyNameTmp = value.keyName.substring(value.keyName.lastIndexOf('`')+1,value.keyName.length);
			       			return keyNameTmp + "&nbsp;<a href='<%=request.getContextPath()%>/app/b2b/show.do?method=showHistoryInfo&appName=<%=appName%>&keyName=" + value.keyName + "&time1=2012-08-23&selectProperty=E-times' target='_blank;'>�鿴��ʷ����</a>";	       				
		       			} else 
		       				return value.keyName + "&nbsp;<a href='<%=request.getContextPath()%>/app/b2b/show.do?method=showHistoryInfo&appName=<%=appName%>&keyName=" + value.keyName + "&time1=2012-08-23&selectProperty=E-times' target='_blank;'>�鿴��ʷ����</a>";	       				
	       			}  else {
	       				return value;
	       			}
	       			
	       		}       		 
          }
        },
        <%
        for(int i=0; i<timeinterval; i++){
        %>
	        {field:'<%="time" + i%>',title:'<%="    "%>',width:70,align:'center',
	       	 formatter:function(value, rowData){
		     	var array = rowData['map'].<%="time" + i%>;
		       	if(array == undefined)
		       		return ""
		       	else {
		       		if(rowData['map'].keyName == "��ǰʱ��") {
		       			return "<strong>" + array[0] + "</strong>";		       			
		       		} else {
		       			return array[1] + "/" + array[2];
		       		}
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
    ]],
    onBeforeLoad:function(row,param){
        if (row){
        	if(row.map != undefined)
        		$(this).treegrid('options').url = '<%=request.getContextPath() %>/app/b2b/show.do?method=showOceanSub&appName=${appName}&keyName=' + row.map.keyName;
        	else
        		$(this).treegrid('options').url = '<%=request.getContextPath() %>/app/b2b/show.do?method=showOceanRoot&appName=${appName}&keyName=${keyName}';
        } else {
         $(this).treegrid('options').url = '<%=request.getContextPath() %>/app/b2b/show.do?method=showOceanRoot&appName=${appName}&keyName=${keyName}';
        }
       }
});
</script>
</html>