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

<title>�ӿڵ��ñ���(beta)</title>
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

		<div align="center">
			<h1>�����澯</h1>
		</div>
		&nbsp;&nbsp;<strong style="color:red">����Ӧ������</strong>${appName}<br/>
		&nbsp;&nbsp;<strong style="color:red">����key��</strong>${keyName}<br/>
		&nbsp;&nbsp;<strong>˵����</strong><br>
		&nbsp;&nbsp;1.�ṩ������������������Ϊĳ�ӿ�ÿ����ȫ������������������Ϊȫ�����5���ӱ���������<br/>
		&nbsp;&nbsp;2.�ṩ������ָ����ӿ��ṩ�ķ�����������������ָ���ϼ��ӿڣ����θ��ڵ㣩������Ӧ�����ѵķ���������<br/>
		<%
		String keyName = (String)request.getAttribute("keyName");
		AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("AppInfoPo");
		if(appInfoPo != null) {
		%>
			&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/config/dependconfig.do?method=showSourceKeyList&appId=<%=appInfoPo.getAppId()%>" target="_blank" style="display: none;">�ֶ�����key������ϵ</a>
		<%  
		} else {
		%>
			&nbsp;&nbsp;<span style="color:red">������!!!��ѯ����Ӧ������${appName}</span><br/>
		<%  		  
		}
		%>
		<%
			List<CspTimeKeyDependInfo> urlList = (List<CspTimeKeyDependInfo>)request.getAttribute("urlList");
			if(urlList != null && urlList.size() > 0 && appInfoPo != null ) {
			  %>
			  	
				<table  class="table table-striped table-condensed table-bordered" style="table-layout: fixed">
					<thead>
						<tr>
							<th width="200">Ӧ����</th>
							<th width="1000">URL</th>
						</tr>
					</thead>			  
			  <%
				for(CspTimeKeyDependInfo info: urlList) {
					String querySubUrl = request.getContextPath() + "/app/depend/query/show.do?method=queryKeyDetailWithTimeData&appName="  + info.getSourceAppName() + "&keyName=" + info.getSourceKeyName();
					out.println("<tr>");
					out.println("<td>" + info.getSourceAppName() + "</td>");
					out.println("<td><a href='" + querySubUrl + "' target='_blank'>" + info.getSourceKeyName() + "</a></td>");
					out.println("</tr>");
				}
				%>
				</table>		
				<hr/>		
				<%		  
			}
		%>
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
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
		<table id="topoAll" style="width:1240px;" title="�����澯·��">
		</table>
		<br/>
		<table id="topoURL" style="width:1200px;display: none" title="2.����·������URL���֣�">
		</table>
		</div></div>
</body>
<script type="text/javascript">
$('#topoAll').treegrid({  
    url:'<%=request.getContextPath() %>/app/depend/query/show.do?method=queryAlarmSingleJson&appName=${appName}&keyName=${keyName}',  
    idField:'id',  
    treeField:'keyName',  
    //frozenColumns:[[{title:'Task Name',field:'keyName',width:500}]],	//�̶�ĳ�У���ѡ
    columns:[[  
        {title:'key����',field:'keyName',width:900,
       	 formatter:function(value, rowData){//qps ����Ϊ�����У�ռλʹ��
       		var appName = rowData['appName'];
       		var targetUrlSelf = "<%=request.getContextPath() %>/app/depend/query/show.do?method=queryKeyDetailWithTimeData&appName=" + appName + "&keyName=" + value;
       		var targetUrl = "<%=request.getContextPath() %>/app/depend/query/show.do?method=gotoAlarmDetail&appName=" + appName + "&keyName=" + value;
       		var showMsg = value;
       		//if(value == '<%=keyName%>') {
       		//	showMsg = '<span style="color:red"><%=keyName%></span>';
       		//} else {
       		//	showMsg = '<a href="' + targetUrlSelf + '" target="_blank" title="�鿴�ӵ���·��">' + value + '</a>';
       		//}
       		showMsg = '<a href="' + targetUrlSelf + '" target="_blank" title="�鿴�ӵ���·��">' + value + '</a>';
          	return showMsg + '&nbsp;<a href="' + targetUrl + '" target="_blank"><strong>�鿴������Ϣ</strong></a>';
          }
        },  
        {field:'addedPo',title:'��������',width:60,align:'center',
        	 formatter:function(value){
        		var spanName = "normalSpan";
        		if(value.alarmCount > 0)
    	    		return '<span name="warnSpan" style="color: red" title="���5���ӱ�������"><strong>' + value.alarmCount + '</strong></span>';
        		else
 	            	return '<span name="normalSpan" title="���5���ӱ�������">' + value.alarmCount + '</span>';
             }
        },  
        {field:'qps',title:'��������/�ṩ������',width:130,align:'center',
        	 formatter:function(value, rowData){//qps ����Ϊ�����У�ռλʹ��
             	return rowData['addedPo'].consumeCount + "/" + rowData['addedPo'].callCount;
             }
        },  
        {field:'qps2',title:'·�����ñ���',width:60,align:'center',
        	 formatter:function(value, rowData){//qps ����Ϊ�����У�ռλʹ��
             	return rowData.rate;
             }
        },  
        //{field:'rate',title:'���ñ���',width:100},
        {field:'appName',title:'����Ӧ������',width:80,align:'center'},
    ]]
});

//$('#topoURL').treegrid({  
//url:'<%=request.getContextPath() %>/app/depend/query/show.do?method=queryAlarmMultiJson&appName=${appName}&keyName=${keyName}',  
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
<script type='text/javascript'>
function toggleCSS(id, pty, v1, v2, ms)
{
  var objArray = document.getElementsByName(id);
	  for(var i in objArray) {
		  //var os=document.getElementById(id).style;
		  os = objArray[i].style;
		  if(os == undefined)
			  return;
		  os[pty]= (os[pty] == v1 ) ? v2 : v1;
		  setTimeout('toggleCSS("'+id+'","'+pty+'","'+v1+'","'+v2+'",'+ms+')', ms);	  
	  }	  
}
setTimeout('toggleCSS("warnSpan", "color", "red", "black", 1000)',5000);//��ʱ3��   
//toggleCSS("warnSpan", "backgroundColor", "red", "white", 1000);	//�ı䱳��
//toggleCSS("test", "color", "red", "green", 500); //�ı�����
</script>
</html>