<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDistribFlowAo"%>
<%@page import="com.taobao.monitor.web.distrib.DistribFlowPo"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.web.distrib.AppDistribFlowBo"%>
<%@page import="com.taobao.monitor.web.distrib.KeyDistribFlowBo"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
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
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();
String opsName = request.getParameter("opsName")==null?"":request.getParameter("opsName");
String collectDay = request.getParameter("collectDay");
String action = request.getParameter("action");

Date collect = null;
if(collectDay == null){
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
	collectDay = sdf.format(cal.getTime());
}
collect =sdf.parse(collectDay) ;


%>
<form action="./distrib_provider.jsp" >
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<select name="opsName">
	<%
	for(AppInfoPo appInfo:appList){ 
		if(appInfo.getOpsName() == null){
			continue;
		}
		String name = appInfo.getOpsName();
	%>
	<option value="<%=appInfo.getOpsName() %>" <%if(name.equals(opsName)){out.print("selected");} %>><%=name %></option>
	<%} %>
</select>
<input type="text" name="collectDay"  value="<%=collectDay%>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
<input type="submit"  value="查看">
</div>
</form>
<%
List<DistribFlowPo> poList = MonitorDistribFlowAo.get().findProvideAppDistribFlow(opsName,collect);

Map<String,AppDistribFlowBo> map = new HashMap<String,AppDistribFlowBo>();



for(DistribFlowPo po:poList){
	String customerApp = po.getCustomerApp();
	String cm = po.getMachine_cm();
	String keyName = po.getKeyName();
	long callNum = po.getCallNum();
	long useTime = (long)po.getUseTime();
	
	AppDistribFlowBo appBo1 = map.get(customerApp);
	if(appBo1 == null){
		appBo1 = new AppDistribFlowBo();
		appBo1.setAppName(customerApp);
		map.put(customerApp,appBo1);
	}
	appBo1.setCallNum(appBo1.getCallNum()+callNum);
	
	
	KeyDistribFlowBo keyBo = appBo1.getKeyMap().get(keyName);
	if(keyBo == null){
		keyBo = new KeyDistribFlowBo();
		keyBo.setKeyName(keyName);		
		appBo1.getKeyMap().put(keyName,keyBo);
	}
	keyBo.setCallNum(keyBo.getCallNum()+callNum);
	
	
	
	DistribFlowPo dpo = keyBo.getMap().get(cm);
	if(dpo == null){
		dpo = new DistribFlowPo();
		keyBo.getMap().put(cm,dpo);
	}
	
	dpo.setCallNum(dpo.getCallNum()+callNum);
	
	dpo.setUseTime(Arith.add(useTime,dpo.getUseTime()));
	
	
}



List<AppDistribFlowBo> boList = new ArrayList<AppDistribFlowBo>();
boList.addAll(map.values());
Collections.sort(boList);

%>

<table >
	<tr>
		<td align="center" ><span style="font-size:16px;color: #003300"><strong>依赖<%=opsName %>的应用</strong></span></td>
	</tr>
	<tr>
		<td align="center"><a href="./distrib_provider.jsp?opsName=<%=opsName %>&collectDay=<%=collectDay %>&action=a"><strong>依赖<%=opsName %>的应用</strong></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="./distrib_customer.jsp?opsName=<%=opsName %>&collectDay=<%=collectDay %>&action=b"><strong><%=opsName %>依赖的应用</strong></a></td>
	</tr>
</table>

<table width="1440" border="1">
  <tr>
    <td colspan="11" align="center"><%=opsName %></td>
  </tr>
  <tr>
    <td width="100" align="center">来源应用</td>
    <td width="100" align="center">来源方法</td>
    <td colspan="3" align="center">总流量</td>
    <td colspan="3" align="center">cm3</td>
    <td colspan="3" align="center">cm4</td>
  </tr>
  <%
  Set<String> set = new HashSet<String>();
  for(AppDistribFlowBo appBo:boList){
	  set.addAll(appBo.getKeyMap().keySet());
  }
  %>
  <tr>
    <td align="center" ><%=boList.size() %></td>
    <td height="20" align="center" ><%=set.size() %></td>
    <td width="130" align="center">当前</td>
    <td width="130" align="center">上周</td>
    <td width="130" align="center">基线</td>
    <td width="130" align="center">当前</td>
    <td width="130" align="center">上周</td>
    <td width="130" align="center">基线</td>
    <td width="130" align="center">当前</td>
    <td width="130" align="center">上周</td>
    <td width="130" align="center">基线</td>
  </tr>
  <%
  for(AppDistribFlowBo appBo:boList){	  
	  boolean f = true; 
	  Map<String, KeyDistribFlowBo> keyboMap = appBo.getKeyMap();
	  List<KeyDistribFlowBo> keyBoList = new ArrayList<KeyDistribFlowBo>();
	  keyBoList.addAll(keyboMap.values());
	  Collections.sort(keyBoList);
	  
	  String cmInfo = "";
	  for(Map.Entry<String, Set<String>> entry:appBo.getCmIpNumMap().entrySet()){
		  cmInfo += entry.getKey()+":"+entry.getValue().size()+";";
	  }
	  
	  
  for(KeyDistribFlowBo keyBo:keyBoList){
	  
	 String keyName =  keyBo.getKeyName();
	 long cm3 = 0;
	 if(keyBo.getMap().get("CM3") != null){
		 cm3 =  keyBo.getMap().get("CM3").getCallNum();
	 }
	 
	 long cm4 = 0;
	 if(keyBo.getMap().get("CM4") != null){
		 cm4 =  keyBo.getMap().get("CM4").getCallNum();
	 }
	  
  %>
   <tr>
  <%if(f){ %>
    <td rowspan="<%=appBo.getKeyMap().size() %>" valign="top"><a href="./distrib_customer.jsp?opsName=<%=appBo.getAppName() %>"><%=appBo.getAppName() %></a>(<%=Utlitites.fromatLong(appBo.getCallNum()+"") %>)</td>
  <%f = false;} %>
  	<%
  		String[] keyNames = keyName.split("_");
  		if(keyNames[1].indexOf("Exception")>-1){
  			continue;
  		}
  	%>
    <td><a href="" style="text-decoration: none;"><%=keyNames[2]+"_"+keyNames[3] %></a></td>
    <td><%=Utlitites.fromatLong(cm3+cm4+"") %></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><%=Utlitites.fromatLong(cm3+"") %></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
    <td><%=Utlitites.fromatLong(cm4+"") %></td>
    <td>&nbsp;</td>
    <td>&nbsp;</td>
  </tr>  
  <%}} %> 
</table>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>