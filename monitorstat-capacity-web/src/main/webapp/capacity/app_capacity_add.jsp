<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo" %>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo" %>

<%@page import="com.taobao.monitor.common.util.TBProductCache"%>
<%@page import="com.taobao.monitor.common.po.ProductLine"%>


<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>容量规划-容量应用新增</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/bootstrap/bootstrap.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/capacity.js"></script>

</head>
<body class="span15">

<script type="text/javascript">
	var groupMap ={}
</script>
<%
request.setCharacterEncoding("gbk");
%>


    
<form id="myForm" name="myForm" action="./manage.do?method=addCapacityAppDataSource" method="post">
<%

List<AppInfoPo> appList = null;
Object obj = request.getAttribute("appList");
if (obj != null) {
	appList = (List<AppInfoPo>)obj;
} 

String action = (String)request.getAttribute("action");
String success = (String)request.getAttribute("success");
if("add".equals(action)){		
	boolean finished = "true".equals(success);
		
%>
	<font size="+3" color="red"><%if(finished){out.print("添加成功");}else{out.print("该应用已经被添加或者出现异常");} %></font>	
	<a href="../capacity/manage.do?method=showAddPage">继续添加</a>
	<%
}else{

%>
	
</select>

<table  id="mytable">
	
	<tr>	
		<td>产品线</td>
		<td><select id="companyGroupSelect" onChange="companyChange(this)">	</select></td>
	</tr>
	<tr>	
		<td>二级产品线</td>
		<td><select id="parentGroupSelect" onchange="groupChange(this)">	</select></td>
	</tr>
	<tr>	
		<td>应用名:</td>
		<td><select name="selectAppId"  id="appNameSelect"></select></td>
	</tr>
	<tr>	
		<td>数据源:</td>
		<td><input type="text" name="dataSource" value="" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td>应用类型:</td>
		<td><input type="text" name="appType" value="" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td>域名:</td>
		<td><input type="text" name="dataFeature" value="" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td>业务增长百分比:</td>
		<td><input type="text" name="growthRate" id="growthRate" value="" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td>监控项名称:</td>
		<td><input type="text" name="itemName" value="" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td>qps数据项名称:</td>
		<td><input type="text" name="dataName" value="" style="width: 80%;"></td>
	</tr>
		
	<tr>
		<td align="right" colspan="2">
			<input type="hidden" value="add" name="action">
			<input type="button" value="添    加" onclick="addApp()">  &nbsp;&nbsp;
			<input type="button" value="关    闭" onclick="closeWindow()">
		</td>
	</tr>
	
</table>
</div>
</div>
</form>
<%} %>

<script type="text/javascript">

	function addApp() {
		var growthRate = $("#growthRate").attr("value");
		if (growthRate==null || growthRate=="") {
			alert("业务增长率不能为空");
			return;
		}
		var patrn=/^[0-9]{1,20}$/; 
		if (!patrn.exec(growthRate)) {
			alert("业务增长率必须为数字");
			return;
		}
		
		document.myForm.submit();
	}

	var groupMap ={};

function addAppGroup(company,groupName,appName,appId){
	
		if(!groupMap[company]){
			groupMap[company] = {};
		}
		
		if(!groupMap[company][groupName]){
			groupMap[company] [groupName]={};
		}
		if(!groupMap[company][groupName][appName]){
			groupMap[company][groupName][appName]=appId;
		}			
}



	function companyChange(selectObj){
		renderGroup("");
		renderApp("");
	}
	function renderCompany(name){
		
		clearSelect("companyGroupSelect");
		fillSelect("companyGroupSelect",groupMap,name);
		
	}
	
	
	function renderGroup(name){
		var companyObj = document.getElementById("companyGroupSelect");
		var company = companyObj.options[companyObj.selectedIndex].value;
		var group = groupMap[company];
		if(group){
			clearSelect("parentGroupSelect");
			fillSelect("parentGroupSelect",group,name);
		}
	}
	
	function renderApp(name){
		
		var companyObj = document.getElementById("companyGroupSelect");
		var company = companyObj.options[companyObj.selectedIndex].value;
		
		var groupObj = document.getElementById("parentGroupSelect");
		var groupName = groupObj.options[groupObj.selectedIndex].value;
		var apps = groupMap[company][groupName];
		
		
		if(apps){
			 clearSelect("appNameSelect")
			 fillSelect("appNameSelect",apps,name);
		}
	}
	
	function fillSelect(id,group,value){
		var ops = document.getElementById(id).options;
		var len = ops.length;
		for (name in group){
			if(typeof group[name]=== 'object'){
				document.getElementById(id).options[len++]=new Option(name,name);
				if(name == value){
					document.getElementById(id).options[len-1].selected=true;
				}
			}else{
				document.getElementById(id).options[len++]=new Option(name,group[name]);
				if(group[name] == value){
					document.getElementById(id).options[len-1].selected=true;
				}
			}
		}
	}
	
	
	
	
	function clearSelect(id){
		document.getElementById(id).options.length=0;		
	}
	
	
	
	function groupChange(selectObj){
		renderApp("");
	}
	
	function initParentSelect(com,gname,gvalue){
		renderCompany(com);
		renderGroup(gname);
		renderApp(gvalue);
	}
	
	<%
	if (obj != null) {
	Map<String,List<AppInfoPo>> appInfoGroupMap = new HashMap<String,List<AppInfoPo>>();
	for(AppInfoPo app:appList){
		ProductLine p = TBProductCache.getProductLineByAppName(app.getOpsName());
	%>
	
	addAppGroup("<%=p.getDevelopGroup()%>","<%=p.getProductline()%>","<%=app.getOpsName()%>","<%=app.getAppDayId()%>");
	
	<%				
	}
	
	ProductLine productLine = TBProductCache.getProductLineByAppName(appList.get(0).getOpsName());
	
	%>
	 initParentSelect("<%=productLine.getDevelopGroup()%>","<%=productLine.getProductline()%>","<%=appList.get(0).getAppDayId()%>");
	 
	 <% } %>
	 

</script>

</body>
</html>