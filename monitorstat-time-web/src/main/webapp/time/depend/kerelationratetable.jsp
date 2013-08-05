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

<title>接口调用比例(beta)</title>
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
			<h1>依赖告警</h1>
		</div>
		&nbsp;&nbsp;<strong style="color:red">报警应用名：</strong>${appName}<br/>
		&nbsp;&nbsp;<strong style="color:red">报警key：</strong>${keyName}<br/>
		&nbsp;&nbsp;<strong>说明：</strong><br>
		&nbsp;&nbsp;1.提供服务量、被消费量均为某接口每分钟全网调用量，报警次数为全网最近5分钟报警次数。<br/>
		&nbsp;&nbsp;2.提供服务量指服务接口提供的服务总量，被消费量指被上级接口（树形父节点）所属的应用消费的服务总量。<br/>
		<%
		String keyName = (String)request.getAttribute("keyName");
		AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("AppInfoPo");
		if(appInfoPo != null) {
		%>
			&nbsp;&nbsp;<a href="<%=request.getContextPath()%>/config/dependconfig.do?method=showSourceKeyList&appId=<%=appInfoPo.getAppId()%>" target="_blank" style="display: none;">手动配置key依赖关系</a>
		<%  
		} else {
		%>
			&nbsp;&nbsp;<span style="color:red">出错了!!!查询不到应用名，${appName}</span><br/>
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
							<th width="200">应用名</th>
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
<div class="container-fluid">
<div class="row-fluid" style="text-align: center">
		<table id="topoAll" style="width:1240px;" title="依赖告警路径">
		</table>
		<br/>
		<table id="topoURL" style="width:1200px;display: none" title="2.调用路径（以URL区分）">
		</table>
		</div></div>
</body>
<script type="text/javascript">
$('#topoAll').treegrid({  
    url:'<%=request.getContextPath() %>/app/depend/query/show.do?method=queryAlarmSingleJson&appName=${appName}&keyName=${keyName}',  
    idField:'id',  
    treeField:'keyName',  
    //frozenColumns:[[{title:'Task Name',field:'keyName',width:500}]],	//固定某列，可选
    columns:[[  
        {title:'key名称',field:'keyName',width:900,
       	 formatter:function(value, rowData){//qps 这列为虚拟列，占位使用
       		var appName = rowData['appName'];
       		var targetUrlSelf = "<%=request.getContextPath() %>/app/depend/query/show.do?method=queryKeyDetailWithTimeData&appName=" + appName + "&keyName=" + value;
       		var targetUrl = "<%=request.getContextPath() %>/app/depend/query/show.do?method=gotoAlarmDetail&appName=" + appName + "&keyName=" + value;
       		var showMsg = value;
       		//if(value == '<%=keyName%>') {
       		//	showMsg = '<span style="color:red"><%=keyName%></span>';
       		//} else {
       		//	showMsg = '<a href="' + targetUrlSelf + '" target="_blank" title="查看子调用路径">' + value + '</a>';
       		//}
       		showMsg = '<a href="' + targetUrlSelf + '" target="_blank" title="查看子调用路径">' + value + '</a>';
          	return showMsg + '&nbsp;<a href="' + targetUrl + '" target="_blank"><strong>查看完整信息</strong></a>';
          }
        },  
        {field:'addedPo',title:'报警次数',width:60,align:'center',
        	 formatter:function(value){
        		var spanName = "normalSpan";
        		if(value.alarmCount > 0)
    	    		return '<span name="warnSpan" style="color: red" title="最近5分钟报警次数"><strong>' + value.alarmCount + '</strong></span>';
        		else
 	            	return '<span name="normalSpan" title="最近5分钟报警次数">' + value.alarmCount + '</span>';
             }
        },  
        {field:'qps',title:'被消费量/提供服务量',width:130,align:'center',
        	 formatter:function(value, rowData){//qps 这列为虚拟列，占位使用
             	return rowData['addedPo'].consumeCount + "/" + rowData['addedPo'].callCount;
             }
        },  
        {field:'qps2',title:'路径调用比例',width:60,align:'center',
        	 formatter:function(value, rowData){//qps 这列为虚拟列，占位使用
             	return rowData.rate;
             }
        },  
        //{field:'rate',title:'调用比例',width:100},
        {field:'appName',title:'所属应用名称',width:80,align:'center'},
    ]]
});

//$('#topoURL').treegrid({  
//url:'<%=request.getContextPath() %>/app/depend/query/show.do?method=queryAlarmMultiJson&appName=${appName}&keyName=${keyName}',  
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
setTimeout('toggleCSS("warnSpan", "color", "red", "black", 1000)',5000);//延时3秒   
//toggleCSS("warnSpan", "backgroundColor", "red", "white", 1000);	//改变背景
//toggleCSS("test", "color", "red", "green", 500); //改变文字
</script>
</html>