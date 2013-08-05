<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<title>接口限流配置</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../top.jsp" %>
 
<div class="span20">
<table class="condensed-table">
 <tr>
 <td>
 <input type="text" id="index" value="" class="xlarge"/> <input type="button" value=" 接口关键字过滤  " onclick="filter()"/>
 <input type="button" value=" 新  增  " onclick="add()"/>
 <input type="button" value="批 量 修 改" onclick="multiAlter()"/>
 </td>
 </tr>
 </table>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th style="width:10%"><input type="checkbox" name="allSelect" id="allSelect" onclick="allSelect()"/> ${strategy}限流</th>
      	<th style="width:8%">应用名</th>
      	<th style="width:37%">接口信息</th>
        <th style="width:8%">限流应用</th>
        <th style="width:8%">限流阀值</th>
        <th style="width:5%">状况</th>
        <th style="width:7%">过时标志</th>
        <th style="width:17%">操作</th>
      </tr>
      </thead>
      
      <tbody>
     
      <c:forEach items="${flowList}" var="o">
      <tr>
      	<td align="center"><input type="checkbox" name="multi" id="${o.id} ${o.appName}"/> ${o.number}</td>
      	<td align="center">${o.appName}</td>
      	<td align="center" style= "word-break:break-all">${o.interfaceInfo}</td>
      	<td align="center">${o.refAppName}</td>
      	<td align="center">${o.limitFlow}</td>
      	<td align="center">${o.stateInfo}</td>
      	<td align="center">${o.outOfDate}</td>
      	<td>
      		<a href="#" onclick='viewIps("${o.id}", "${o.appName}", "${o.refAppName}")'>查看ip&nbsp;</a>
            <a href="#" onclick='alter("${o.id}", "${o.appName}")'>修改&nbsp;</a>
            <a href="#" onclick='changeState("${o.id}", "${o.opositeStateInfo}", "${o.appName}")'>${o.opositeStateInfo}&nbsp;&nbsp;</a>
            <a href="#" onclick='deleteConfig("${o.id}", "${o.appName}")'>删除&nbsp;</a>
        </td>
	  </tr>
	 </c:forEach>
 </tbody>

</table>
</div>
<script type="text/javascript">

function add() {
	window.open ('manage.do?method=goToAddFlowControlInterface&strategy=${strategy}', 'newwindow', 'height=600, width=900, top=150, left=150')
}

function filter() {
	var val = $("#index").val();
	var strategy = "${strategy}";
	var url = "show.do?method=showFlowControlInterface&index=" +  val + "&strategy=" + strategy;
	window.location.href = url;
}

function allSelect() {
	var all = document.getElementById("allSelect");
	var ck = document.getElementsByName("multi");
	if (all.checked) {
		for(var i=0;i<ck.length;i++){
			ck[i].checked = true;
		}
	} else {
		for(var i=0;i<ck.length;i++){
			ck[i].checked = false;
		}
	}
}

function viewIps(refId, appName, refApp) {
	var urlAddr = "show.do?method=showFlowControlInterfaceIps&refId=" + refId + "&appName=" + appName + "&refApp=" + refApp;
 	window.open (urlAddr, 'newwindow', 'scrollbars=yes, height=600, width=900, top=150, left=150');	
}

function changeState(id, operator, appName) {
	var confirmMessage = "确定" + operator + "?";
	if (!confirm(confirmMessage)) {
		return;
	}
	
	var urlDest = "./manage.do?method=changeState"
    var parameters = "id=" + id + "&type=FLOW_CONTROL_INTERFACE" + "&appName=" + appName;
    $.ajax({
    	url: urlDest,
    	async:true,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		if (data=="success") {
    			window.location.reload();
    		} else {
    			alert("没有该应用的权限");
    		}	
    	}
   	});
}

function deleteConfig(id, appName) {
	if (!confirm("确定删除? ")) {
		return;
	}
	var urlDest = "./manage.do?method=deleteFlowControlInterfaceConfig";
    var parameters = "id=" + id + "&appName=" + appName;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		if (data=="true") {
    			window.location.reload();
    		} else {
    			alert("没有该应用的权限");
    		}	
    	}
   	});
}

function alter(id, appName) {
	var thread = window.prompt("请输入新的限流线程数");
	if (thread == null) {
		return;
	}
	var patrn=/^[0-9]{1,20}$/; 
	if (!patrn.exec(thread)) {
		alert("限流线程数必须为数字");
		return;
	}
	var urlDest = "./manage.do?method=alterFlowThread";
	var parameters = "id=" + id + "&type=FLOW_CONTROL_INTERFACE" + "&thread=" + thread + "&appName=" + appName;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		if (data=="success") {
    			window.location.reload();
    		} else {
    			alert("没有该应用的权限");
    		}	
    	}
   	});
}

function multiAlter(){
	var ck = document.getElementsByName("multi");
	var app = null;
	var ids = "";
	for(var i=0;i<ck.length;i++){
        if(ck[i].checked){
        	var id = ck[i].id;
        	var array = id.split(" ");
        	if (app == null) {
        		app = array[1];
        		ids += array[0];
        	} else {
        		if (app != array[1]) {
        			alert("批量修改不允许一次修改多个应用的数据!");
        			return;
        		} else {
        			ids += " ";
        			ids += array[0];
        		}
        	}
        }
    }
    
    if (app == null) {
    	alert("没有选择任何限流配置!");
		return;
    }
    
    var thread = window.prompt("请输入新的限流线程数");
	if (thread == null) {
		return;
	}
	var patrn=/^[0-9]{1,20}$/; 
	if (!patrn.exec(thread)) {
		alert("限流线程数必须为数字");
		return;
	}
    
    var urlDest = "./manage.do?method=alterFlowThreadMulti";
	var parameters = "ids=" + ids + "&type=FLOW_CONTROL_INTERFACE" + "&thread=" + thread + "&appName=" + app;
    $.ajax({
    	url: urlDest,
    	async:false,
    	type: "POST",
    	dataType: "String",
    	data: parameters,
    	cache: false,
    	success: function(data) {
    		if (data=="success") {
    			window.location.reload();
    		} else {
    			alert("没有该应用的权限");
    		}	
    	}
   	});
}

function test(id) {
	alert("Test");
}

</script>
</body>
</html>