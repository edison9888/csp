<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppDescPo"%>
<%@page import="com.taobao.monitor.dependent.ao.CspDependentAo"%>
<%@page import="java.util.Date"%>
<%@page import="com.taobao.monitor.dependent.po.AppDependentRelationPo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Set"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.ArrayList"%>

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery.ui.widget.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<title>依赖关系</title>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>

<%
request.setCharacterEncoding("gbk");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String appName = request.getParameter("selectAppOpsName");
String hostSite = request.getParameter("hostSite");
String startTime = request.getParameter("startTime");

Date collectTime = null;
if(startTime == null){
	
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
	collectTime = cal.getTime();
	
	startTime = sdf.format(collectTime);
}else{
	try{
	collectTime = sdf.parse(startTime);}catch(Exception e){
		collectTime = new Date();
	}
}


List<AppDescPo> appDescList = AppInfoAo.get().findAppDesc();
Map<String,AppDescPo> mapdesc = new HashMap<String,AppDescPo>();
for(AppDescPo desc:appDescList){
	mapdesc.put(desc.getOpsName(),desc);
}
Set<String> appSet = CspDependentAo.get().getAllAppOpsName(collectTime);


for(String app:appSet){
	
	AppDescPo desc = mapdesc.get(app);
	if(desc ==null){
		desc = new AppDescPo();
		desc.setFirstProductLine("未知");
		desc.setSecProductLine("未知");
		desc.setOpsName(app);
		mapdesc.put(app,desc);
	}
	
}


//Iterator<AppDescPo> it = appDescList.iterator();
//while(it.hasNext()){
//	if(!appSet.contains(it.next().getOpsName())){
//		it.remove();
//	}
//}


AppDescPo firstPo = appDescList.get(0);
if(appName == null){
}else{
	firstPo = mapdesc.get(appName);
}
Set<String> hostSiteSet = new HashSet<String>();

List<AppDependentRelationPo> depMeList = CspDependentAo.get().findDependentMe(firstPo.getOpsName(),collectTime);
for(AppDependentRelationPo po:depMeList){
	hostSiteSet.add(po.getSelfSite());
}
List<String> hostSiteList = new ArrayList<String>();
hostSiteList.addAll(hostSiteSet);
if(hostSite == null){
	if(hostSiteList.size() >0)
		hostSite = hostSiteList.get(0);
	else
		hostSite="CM4";
}




Map<String,Map<Integer,Map<String,Integer>>> depMemap = new HashMap<String,Map<Integer,Map<String,Integer>>>();
for(AppDependentRelationPo po:depMeList){
	
	if(!po.getSelfSite().equals(hostSite)){
		continue;
	}
	
	Map<Integer,Map<String,Integer>> depMap = depMemap.get(po.getDependentOpsName());
	if(depMap == null){
		depMap = new HashMap<Integer,Map<String,Integer>>();
		depMemap.put(po.getDependentOpsName(),depMap);
	}
	Map<String,Integer> jifanMap = depMap.get(po.getDependentPort());
	if(jifanMap == null){
		jifanMap = new HashMap<String,Integer>();
		depMap.put(po.getDependentPort(),jifanMap);
	}
	Integer count = jifanMap.get(po.getDependentSite());
	if(count == null){
		jifanMap.put(po.getDependentSite(),1);
	}else{
		jifanMap.put(po.getDependentSite(),1+count);
	}
}



List<AppDependentRelationPo> meDepList = CspDependentAo.get().findMeDependent(firstPo.getOpsName(),collectTime);

Map<String,Map<Integer,Map<String,Integer>>> meDepmap = new HashMap<String,Map<Integer,Map<String,Integer>>>();

for(AppDependentRelationPo po:meDepList){
	
	if(!po.getSelfSite().equals(hostSite)){
		continue;
	}
	
	Map<Integer,Map<String,Integer>> medepPortMap = meDepmap.get(po.getDependentOpsName());
	if(medepPortMap == null){
		medepPortMap = new HashMap<Integer,Map<String,Integer>>();
		meDepmap.put(po.getDependentOpsName(),medepPortMap);
	}
	Map<String,Integer> jifanMap = medepPortMap.get(po.getDependentPort());
	if(jifanMap == null){
		jifanMap = new HashMap<String,Integer>();
		medepPortMap.put(po.getDependentPort(),jifanMap);
	}
	Integer count = jifanMap.get(po.getDependentSite());
	if(count == null){
		jifanMap.put(po.getDependentSite(),1);
	}else{
		jifanMap.put(po.getDependentSite(),1+count);
	}
}


%>
<form action="./index.jsp" id="formId">
<table>
	<tr>
		<td>
			<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
			<select id="parentGroupSelect" onchange="groupChange(this)">	
			</select>
			<select id="appNameSelect" name="selectAppOpsName">	
			</select>
			日期: <input type="text" name="startTime" value="<%=startTime %>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})"/>
			<input type="submit" value="查看依赖情况"><input type="hidden" name="hostSite" value="<%=hostSite %>" id="jifanId"> 
			</div>
		</td>
	</tr>
</table>
</form>
<table>
	<tr>
		<td>
			应用简介:
		</td>
		<td>
			<%=firstPo.getAppDesc() %>
		</td>
	</tr>
	<tr>
		<td>
			负责人:
		</td>
		<td>
			<%=firstPo.getAppAttention() %>
		</td>
	</tr>
	<tr>
		<td>
			PE:
		</td>
		<td>
			<%=firstPo.getAppPe() %>
		</td>
	</tr>
</table>

机房选择:
<table >
	<tr>
	<%for(String site:hostSiteSet){ %>
		<td width="100">
			<input type="button" value="<%=site %>机房" onclick="changeJifan('<%=site %>')" <%if(hostSite.equals(site)){out.print("disabled='disabled'");} %>>
		</td>
	<%} %>
	</tr>
</table>
<table width="100%">
	<tr class="ui-widget-header ">
		<td align="center">当前机房为:<%=hostSite %></td>
	</tr>
</table>

<div id="tabs">
	<ul>
			<li><a href="#tabs-1">我依赖的应用</a></li>
			<li><a href="#tabs-2">依赖我的应用</a></li>
	</ul>
	<div id="tabs-1">
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td>依赖应用名</td><td>依赖端口</td><td>机房比例</td>
			</tr>
			<%
			for(Map.Entry<String,Map<Integer,Map<String,Integer>>> entry:meDepmap.entrySet()){
				
				Map<Integer,Map<String,Integer>> portMap = entry.getValue();
				
				for(Map.Entry<Integer,Map<String,Integer>> entryPort:portMap.entrySet()){
					
					Map<String,Integer> mapJifan = entryPort.getValue();
					int all = 0;
					for(Map.Entry<String,Integer> jifanEntry:mapJifan.entrySet()){
						all+=jifanEntry.getValue();
					}
					String jifan = "";
					for(Map.Entry<String,Integer> jifanEntry:mapJifan.entrySet()){
						jifan+=jifanEntry.getKey()+":"+(Arith.mul(Arith.div(jifanEntry.getValue(),all,4),100))+"%|";
					}
					
			%>
				<tr>
					<td><a href="./index.jsp?selectAppOpsName=<%=entry.getKey() %>&startTime=<%=startTime %>"><%=entry.getKey() %></a></td>
					<td><%=entryPort.getKey() %></td>
					<td><%=jifan %></td>
				</tr>
			<%}} %>
		</table>
	</div>
	<div id="tabs-2">
		<table width="100%" border="1" class="ui-widget ui-widget-content">
			<tr class="ui-widget-header ">
				<td>应用名</td>
				<td>被依赖端口</td>
				<td>机房比例</td>
			</tr>
			<%
			for(Map.Entry<String,Map<Integer,Map<String,Integer>>> entry:depMemap.entrySet()){
				
				Map<Integer,Map<String,Integer>> portMap = entry.getValue();
				
				for(Map.Entry<Integer,Map<String,Integer>> entryPort:portMap.entrySet()){
					
					Map<String,Integer> mapJifan = entryPort.getValue();
					int all = 0;
					for(Map.Entry<String,Integer> jifanEntry:mapJifan.entrySet()){
						all+=jifanEntry.getValue();
					}
					String jifan = "";
					for(Map.Entry<String,Integer> jifanEntry:mapJifan.entrySet()){
						jifan+=jifanEntry.getKey()+":"+(Arith.mul(Arith.div(jifanEntry.getValue(),all,4),100))+"%|";
					}
					
			%>
				<tr>
					<td><a href="./index.jsp?selectAppOpsName=<%=entry.getKey() %>&startTime=<%=startTime %>"><%=entry.getKey() %></a></td>
					<td><%=entryPort.getKey() %></td>
					<td><%=jifan %></td>
				</tr>
			<%}} %>
		</table>
	</div>
</div>
<script type="text/javascript">
var groupMap ={}

function addAppGroup(groupName,appName,appId){
		
		if(!groupMap[groupName]){
			groupMap[groupName]={};
		}
		if(!groupMap[groupName][appName]){
			groupMap[groupName][appName]=appId;
		}			
}
	
	function groupChange(selectObj){
		var groupName = selectObj.options[selectObj.selectedIndex].value;
		var group = groupMap[groupName];
		if(group){
			clearSubSelect();
			fillSubSelect(groupName);
		}
	}
	
	function clearSubSelect(){
		 document.getElementById("appNameSelect").options.length=0;		
		
	}
	function fillSubSelect(groupName,value){
		var group = groupMap[groupName];
	
		var ops = document.getElementById("appNameSelect").options;
		var len = ops.length;
		for (name in group){
			document.getElementById("appNameSelect").options[len++]=new Option(name,group[name]);
			if(name == value){
				document.getElementById("appNameSelect").options[len-1].selected=true;
			}
		}
	}
	
	function initParentSelect(gname,gvalue){
		clearSubSelect();
		var len = document.getElementById("parentGroupSelect").options.length;
		for (name in groupMap){
			document.getElementById("parentGroupSelect").options[len++]=new Option(name,name);
			if(name == gname){
				document.getElementById("parentGroupSelect").options[len-1].selected=true;
			}
		}
				
		if(gname&&gvalue){
			fillSubSelect(gname,gvalue);
		}else{
			groupChange(document.getElementById("parentGroupSelect"));
		}
	
	}
	<%
	for(Map.Entry<String,AppDescPo> entry:mapdesc.entrySet()){
	%>
	addAppGroup("<%=entry.getValue().getSecProductLine()%>","<%=entry.getValue().getOpsName()%>","<%=entry.getValue().getOpsName()%>")
	<%				
	}
	%>
	 initParentSelect("<%=firstPo.getSecProductLine()%>","<%=firstPo.getOpsName()%>");
</script>
<script type="text/javascript">
$(function() {
	$("#tabs").tabs();
});


function changeJifan(cm){
	$("#jifanId").val(cm);
	$("#formId").submit();
}
</script>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>