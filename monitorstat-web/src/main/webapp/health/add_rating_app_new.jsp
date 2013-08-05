<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.core.po.JprofHost"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicator"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.common.ao.center.KeyAo"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<style type="text/css">
div {
	font-size: 12px;
}
table {
	margin: 1em 0;
	border-collapse: collapse;
	width: 100%;
	font-size: 12px;
	margin: 0px 0;
}

table td {
font-size: 12px;
}

.ui-button {
	outline: 0;
	margin: 0;
	padding: .4em 1em .5em;
	text-decoration: none; ! important;
	cursor: pointer;
	position: relative;
	text-align: center;
}

.ui-dialog .ui-state-highlight,.ui-dialog .ui-state-error {
	padding: .3em;
}

.headcon {
	font-family: "宋体";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>
<script type="text/javascript">

	function checkSubmit(){
		var obj = document.getElementById("appSelectId");
		var v = obj.options[obj.selectedIndex].text;

		var arr = $(".quanzhong");
		for(var j = 0; j < arr.length; j++) {
	
			if(arr.get(j).value == "") {
	
				arr.get(j).focus();
				alert('权重不允许为空，请重新输入');  
				return false;
			}
		}
		
		if(window.confirm("你确认添加应用: "+v+" 吗?")){
			return true;
		}
		
		return false;
	}

</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >应用已经设置</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">	
<%
String appId = request.getParameter("appId");

List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
boolean selectApp = true;
if(appId == null){
	appId = appList.get(0).getAppId()+"";
}else{
	selectApp = false;
}
AppInfoPo appInfopo = AppCache.get().getKey(Integer.parseInt(appId));

String action = request.getParameter("action");
String keyId = request.getParameter("keyId");
if("add".equals(action)){
	RatingIndicator indicator = new RatingIndicator();

	String name = request.getParameter("name");
	String unit = request.getParameter("unit");
	String quanzhong = request.getParameter("quanzhong");
	String qujian = request.getParameter("qujian");
	String rushHour_start = request.getParameter("rushHour_start");
	String rushHour_end = request.getParameter("rushHour_end");
	
	if(!"".equals(name)&&!"".equals(quanzhong)&&!"".equals(qujian)&&!"".equals(rushHour_start)&&!"".equals(rushHour_end)){
		int rushHourStart = 2030;
		int rushHourEnd = 2230;
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		try {
			String rushHourStart1 = request.getParameter("rushHour_start");
			String rushHourEnd1 = request.getParameter("rushHour_end");
			if(rushHourStart1.length() != 4 || rushHourEnd1.length() != 4) {
				rushHourStart = 2030;
				rushHourEnd = 2230;
			} else {
				rushHourStart = Integer.parseInt(sdf.format(sdf.parse(rushHourStart1)));	//获得rushHour_start
				rushHourEnd = Integer.parseInt(sdf.format(sdf.parse(rushHourEnd1)));		//获得rushHour_end
			}
		} catch (Exception e) {
			//如果填写时间出错就填默认值
			rushHourStart = 2030;
			rushHourEnd = 2230;
		}
		indicator.setAppId(Integer.parseInt(appId));
		indicator.setIndicatorKeyName(name);
		indicator.setKeyId(Integer.parseInt(keyId));
		indicator.setIndicatorWeight(Integer.parseInt(quanzhong));
		indicator.setIndicatorThresholdValue(qujian);
		indicator.setRushHour_start(rushHourStart);		//设置监控开始时间
		indicator.setRushHour_end(rushHourEnd);			//设置监控结束时间
		indicator.setKeyUnit(unit);
		MonitorRatingAo.get().addRatingIndicator(indicator);
	}
	
	
	
}
%>
<form action="./add_rating_app_new.jsp">
	<table>
		<tr>
			<td colspan="3">请选择添加的应用:
				<select id="parentGroupSelect" onchange="groupChange(this)"></select>
				<select id="appNameSelect" name="appId"></select><input type="submit" value="选择应用">
			</td>
		</tr>
		<tr >
			<td colspan="3" height="20"></td>
		</tr>
	</table>
	<%
	List<RatingIndicator> ratingIndicatorList = MonitorRatingAo.get().getRatingIndicatorByAppId(Integer.parseInt((appId)));
	if(!selectApp){
	%>
	<table border="1">	
		 <tr >
		     <td width="5%" align="center">keyId</td>
		     <td width="13%" align="center">指标名称</td>
		     <td width="13%" align="center">单位</td>
			 <td width="13%" align="center">权重</td>
			 <td width="34%" align="center">健康区间</td>
			 <td width="34%" align="center">采样时间范围</td>
		 </tr>
		 <%for(RatingIndicator i:ratingIndicatorList){ %>
		 <tr>
		 	<td align="center"><%=i.getKeyId() %></td>
		    <td align="center"><%=i.getIndicatorKeyName() %></td>
		    <td align="center"><%=i.getKeyUnit() %></td>
			<td align="center"><%=i.getIndicatorWeight()%></td>
			<td align="center"><%=i.getIndicatorThresholdValue() %></td>
			<td align="center">开始: <%=i.getRushHour_start() %>结束: <%=i.getRushHour_end()%></td>
		  </tr>
		  <%} %>
	</table>
	<%} %>	
</form>
</div>
</div>
	<%if(!selectApp){ %>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >添加新的性能跟踪体系应用</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<form action="./add_rating_app_new.jsp" method="post">
<table border="1">

		<tr>
			<td colspan="6">请选择指标key:
				<select name="keyId" id="selectKeyId" onchange="$('#keyIdTd').text($('#selectKeyId').val())">
					<%
					List<KeyPo> keyList = KeyAo.get().findAllAppKey(Integer.parseInt(appId));
					for(KeyPo key:keyList){
					%>
					<option value="<%=key.getKeyId() %>" <%if((key.getKeyId()+"").equals(keyId)){out.print("selected");} %>><%=key.getKeyName() %></option>
					<%} %>
				</select>
			</td>
		</tr>
		
		 <tr >
		     <td width="5%" align="center">keyId</td>
		     <td width="13%" align="center">指标名称</td>
		     <td width="13%" align="center">单位</td>
			 <td width="13%" align="center">权重</td>
			 <td width="24%" align="center">健康区间</td>
			 <td width="44%" align="center">采样时间范围</td>
		 </tr>
		 <tr>
		 	<td align="center" id="keyIdTd"><%=keyId==null?"":keyId %></td>
		    <td align="center"><input type="text" name="name" value="" size="10"/></td>
		    <td align="center"><input type="text" name="unit" value="" size="10"/></td>
			<td align="center"><input type="text" name="quanzhong" class = "quanzhong" value="" size="10"/>%</td>
			<td align="center"><input type="text" name="qujian" value="{(0,0):0.5}{(0,0):0.5}{(0,0):0.5}" size="80"/> </td>
			<td align="center">开始: <input type="text" name="rushHour_start" value="2030" size="10">结束: <input type="text" name="rushHour_end" value="2230" size="10"></td>
		  </tr>
		  <tr>
			<td colspan="5" align="center">
			<input type="submit" value="提交新指标">
			<input type="hidden" name="action" value="add"/>
			<input type="hidden" name="appId" value="<%=appId %>"/>
			</td>
		</tr>
	</table>
</form>	
</div>
</div>
<%} %>
{时间格式是HHMM,例如2030表示20:30,如果填写出错则默认设置为2030—2230}
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
	Map<String,List<AppInfoPo>> appInfoGroupMap = new HashMap<String,List<AppInfoPo>>();
	for(AppInfoPo app:appList){
	%>
	addAppGroup("<%=app.getGroupName()%>","<%=app.getAppName()%>","<%=app.getAppId()%>")
	<%				
	}
	%>
	 initParentSelect("<%=appInfopo.getGroupName()%>","<%=appInfopo.getAppName()%>");
</script>
</body>

</html>