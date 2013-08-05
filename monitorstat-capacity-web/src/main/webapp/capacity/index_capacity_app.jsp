<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityAppPo" %>
<%@page import="com.taobao.monitor.common.po.AppInfoPo" %>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>容量应用维护</title>
</head>
<body style="padding-top:40px" class="span20">
<%@ include file="../top.jsp" %>
 
<%

List<CapacityAppPo> capacityAppPos = new ArrayList<CapacityAppPo>();
List<AppInfoPo> appList = new ArrayList<AppInfoPo>();

Object objCapacity = request.getAttribute("capacityAppPos");
if(objCapacity != null){
	capacityAppPos = (List<CapacityAppPo>)objCapacity;
}

Object objApp = request.getAttribute("appList");
if(objApp != null){
	appList = (List<AppInfoPo>)objApp;
}

%>
<div class="span20">
<table class="condensed-table">
 <tr>
 <td><input type="button" value=" 新  增  " onclick="add()"/>
 </td>
 </tr>
 </table>
<table class="bordered-table"  id="mytable" >
      <thead align="center">
      <tr align="center">
      	<th width="30" align="center">编号</td>
        <th width="150" align="center">应用组</td>
        <th width="150" align="center">应用名</td>
        <th width="40" align="center">数据源</td>
        <th width="60" align="center">应用类型</td>
        <th width="250" align="center">域名</td>
        <th width="100" align="center">业务增长百分比</td>
        <th width="120" align="center">操作</td>
      </tr>
      </thead>
      <tbody>
      <%
      for(int i = 0; i < capacityAppPos.size(); i++){
    	  CapacityAppPo capacityPo = capacityAppPos.get(i);
    	  AppInfoPo appPo = appList.get(i);
    	  String feature = capacityPo.getDataFeature();
    	  int size = 1;
    	  String [] featureArray;
    	  if (feature == null) {
    	  	featureArray = new String[1];
  	   	    featureArray[0] = "";
    	  } else {
    	  	featureArray = feature.split(",");
    	  	size = featureArray.length;
    	  }
    	  
    	  int j = 0;
      %>
      <tr>
      	<td rowspan="<%= size%>"><%=i+1 %></td>
      	<td rowspan="<%= size%>"><%=appPo.getGroupName() %></td>
      	<td rowspan="<%= size%>"><%=appPo.getAppName() %></td>
      	<td rowspan="<%= size%>"><%=capacityPo.getDatasourceName() %></td>
      	<td rowspan="<%= size%>"><%=capacityPo.getAppType() %></td>
      	<td><%=featureArray[0] %></td>
      	<td rowspan="<%= size%>"><%=capacityPo.getGrowthRate() %>%</td>
      	<td rowspan="<%= size%>">
            <a href="#" onclick='alterApp("<%=appPo.getAppId() %>")'>修改 &nbsp;&nbsp;</a>
            <a href="#" onclick='deleteApp("<%=appPo.getAppId() %>")'>删除 &nbsp;&nbsp;</a>
        </td rowspan="<%= size%>">
	  </tr>
	  <% for(j = 1; j < size; j++) { %>
	 	 <tr> <td><%=featureArray[j] %></td> </tr>
	  <% } %>
 <%} %>
 </tbody>

</table>
</div>

<script type="text/javascript">

function add() {
	window.open ('./capacity/manage.do?method=showAddPage', 'newwindow', 'height=600, width=900, top=150, left=150');
}

function alterApp(appId) {
	var urlDest = "./capacity/manage.do?method=showEditAppDataSource&appId=" + appId;
    window.open (urlDest, 'newwindow', 'height=600, width=900, top=150, left=150');
}

function deleteApp(appId) {
	if (!confirm("确定删除? ")) {
		return;
	}
   	
   	var urlDest = "./capacity/manage.do";
    var method = "deleteAppDataSource";
    $.ajax({
      type : "GET",
      url: urlDest,
      async: false,
      dataType : 'json',
      cache: false,
      data:{'method':method,'appId':appId},
      success: function(data) {
       	if (data=="success") {
    		window.location.reload();
  
    	} else {
    		alert("没有权限");
    	}	
      },
      error: function(XMLHttpRequest, textStatus, errorThrown) { 
          
      }     
    });
}

function test() {
	alert("Test");
}

</script>

</body>
</html>