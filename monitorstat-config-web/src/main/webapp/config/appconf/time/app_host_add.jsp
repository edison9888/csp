<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>查看监控主机</title>
<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>

<%
AppInfoPo appInfoPo = (AppInfoPo)request.getAttribute("appInfoPo");
List<HostPo> cmHostList = (ArrayList<HostPo>)request.getAttribute("cmHostList");
Boolean issuccess = (Boolean)request.getAttribute("issuccess");
%>
<script>
$(function(){
	<%if (issuccess!=null&&!issuccess) {%>alert("添加失败，持久表数量必须小于等于6个")<%}%>
	
	// 用另一个复选框来控制全选/全不选
		$("#checkHostId").click(function(){
	  
			if(this.checked){     //如果当前点击的多选框被选中
		   		$('input[type=checkbox][name=selectId]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][name=selectId]').attr("checked", false );
		 	}
		})
		$("#checkSaveData1").click(function(){
	  
			if(this.checked){     //如果当前点击的多选框被选中
		   		$('input[type=checkbox][id=saveData1]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][id=saveData1]').attr("checked", false );
		 	}
		})
		$("#checkSaveData2").click(function(){
	  
			if(this.checked){     //如果当前点击的多选框被选中
		   		$('input[type=checkbox][id=saveData2]').attr("checked", true );
		 	}else{        
		   	   	$('input[type=checkbox][id=saveData2]').attr("checked", false );
		 	}
		})
})

function goToCheck() {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=checkAppHost&appId=<%=appInfoPo.getAppId()%>";
	location.href = str;
}
function checkAlarm(){
	var objs=document.getElementsByName('selectId');
	var isSel = false;
	for(var i=0;i<objs.length;i++)
	{
	  if(objs[i].checked==true)
	   {
	    isSel=true;
	    break;
	   }
	}
	if(isSel==false){
		alert("对不起!您没有选择任何的主机!"); 
		return false;
	}else{
		if(!checkAlarmSaveData()){

			return false;
		};
		return true;
	} 
	
}
</script>
</head>
<body>
	<jsp:include page="../../../header.jsp"></jsp:include>
	<div class="container" style="padding-top: 60px;">
	<form action="<%=request.getContextPath()%>/show/appconfig.do?method=insertAppHost&appId=<%=appInfoPo.getAppId()%>" onsubmit="return checkAlarm()" method="post">
      <div class="content">
        <div class="page-header">
          <h1>
          	应用：<%=appInfoPo.getOpsName() %> <small>添加主机关联总个数：<%=cmHostList.size() %></small>
            <input class="btn primary pull-right" type="button" value="返回" onclick="goToCheck()">&nbsp;&nbsp;
			&nbsp;&nbsp;<input class="btn primary pull-right" type="submit" value="添加主机关联"">
          </h1>
          
        </div>
		<table class="zebra-striped condensed-table bordered-table">
			<tr>
			  	<td width="50" align="center">
			  	<input name='check' type='checkbox' id='checkHostId' value='checkbox'></td>
				<td>主机名</td>
				<td>主机IP</td>
				<td>机房</td>
				<td><input name='check' type='checkbox' id='checkSaveData1'>&nbsp;&nbsp;临时表</td>
				<td><input name='check' type='checkbox' id='checkSaveData2'>&nbsp;&nbsp;持久表</td>
		    </tr>  
			<%	
				for(HostPo po : cmHostList) {
			%>	
			<tr>
				<td><input type="checkbox" name='selectId' id='selectId' value="<%=po.getHostIp() %>"></td>
				<td><input type="text" name="hostName_<%=po.getHostIp() %>" value="<%=po.getHostName() %>"></td>
				<td><input type="text" name="hostIp_<%=po.getHostIp() %>" value="<%=po.getHostIp() %>"></td>
				<td><input type="text" name="hostSite_<%=po.getHostIp() %>" value="<%=po.getHostSite() %>"></td>
				<td><input type="checkBox" name="saveData1_<%=po.getHostIp() %>" id="saveData1" class="saveData1" value="1" checked="checked"></td>
				<td><input type="checkBox" name="saveData2_<%=po.getHostIp() %>" id="saveData2" class="saveData2" value="1"></td>
			</tr>
			<%
				}
			%>
		</table>
		
		<%
		if(0 != cmHostList.size()) {
		%>
		<div class="well" style="padding: 14px 19px;">
			<center>
	        	<input class="btn primary" type="submit" value="添加主机关联"">
				<input class="btn primary" type="button" value="返回" onclick="goToCheck()">
			</center>
      	</div>
		<%
		} else {
		%>
		<div class="well" style="padding: 14px 19px;">
			<h1>所有主机你已经添加，请返回</h1>
			<input type="button" value="返回" onclick="goToCheck()">
      	</div>
		<%
		}
		%>
		
      </div>
	</form>
      <footer>
        <p>&copy; Taobao 2011</p>
      </footer>

    </div> <!-- /container -->
</body>
</html>