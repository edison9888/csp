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
<%@page import="java.util.Set"%>
<%@page import="com.taobao.monitor.web.cache.CacheProviderCustomer"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	var pre = "";
	function showPanel(id){
		if(pre == id){
			document.getElementById("panel_"+pre).style.display="none";
			pre = "";
		}else{
			if(pre != ""){
				document.getElementById("panel_"+pre).style.display="none";
			}
			document.getElementById("panel_"+id).style.display="block";
			pre = id;
		}
		
	}
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<%
request.setCharacterEncoding("gbk");
Set<String> customerSet = CacheProviderCustomer.get().getCustomerAppName();
String opsName = request.getParameter("opsName");
if(opsName == null){
	for(String k :customerSet){
		opsName = k;
		break;
	}
	
}

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
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
<form action="./distrib_customer.jsp" method="post">
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<select name="opsName">
	<%

	
	
	for(String customerAppName:customerSet){ 		
	%>
	<option value="<%=customerAppName %>" <%if(customerAppName.equals(opsName)){out.print("selected");} %>><%=customerAppName %></option>
	<%} %>
</select>
<input type="text" name="collectDay"  value="<%=collectDay%>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
<input type="submit"  value="查看">&nbsp;&nbsp;&nbsp;&nbsp;<a  href="./distrib_provider.jsp"><strong>查看被依赖的应用</strong></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="./distrib_customer.jsp"><strong>查看依赖的应用</strong></a>
</div>
</form>


<table width="100%">
	<tr>
		<td align="center"><span style="font-size:16px;color: #003300"><strong><%=opsName %>依赖的应用</strong></span></td>
	</tr>
	<tr>
		<td align="center"><a href="./distrib_provider.jsp?opsName=<%=opsName %>&collectDay=<%=collectDay %>&action=a"><strong>依赖<%=opsName %>的应用</strong></a>&nbsp;&nbsp;&nbsp;&nbsp;<a href="./distrib_customer.jsp?opsName=<%=opsName %>&collectDay=<%=collectDay %>&action=b"><strong><%=opsName %>依赖的应用</strong></a></td>
	</tr>
</table>
<%
List<DistribFlowPo> poList = MonitorDistribFlowAo.get().findCustomerAppDistribFlow(opsName,collect);

Map<String,AppDistribFlowBo> map = new HashMap<String,AppDistribFlowBo>();



for(DistribFlowPo po:poList){
	String providerApp = po.getProviderApp();
	String cm = po.getMachine_cm();
	String keyName = po.getKeyName();
	long callNum = po.getCallNum();
	long useTime = (long)po.getUseTime();
	
	AppDistribFlowBo appBo1 = map.get(providerApp);
	if(appBo1 == null){
		appBo1 = new AppDistribFlowBo();
		appBo1.setAppName(providerApp);
		map.put(providerApp,appBo1);
	}
	appBo1.setCallNum(appBo1.getCallNum()+callNum);
	
	
	Set<String> n = appBo1.getCmIpNumMap().get(cm);
	if(n == null){
		n = new HashSet<String>();
		appBo1.getCmIpNumMap().put(cm,n);
	}
	n.add(po.getMachine_ip());
	
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

<div class="demo">
<div id="accordion">
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
	  %>
	<table border="1" style="padding:5px 5px 5px 5px" width="100%">
		<tr>
			<td><h3><a href="javascript:showPanel('<%=appBo.getAppName() %>')">&nbsp;&nbsp;<%=appBo.getAppName() %>&nbsp;总量:[<%=Utlitites.fromatLong(appBo.getCallNum()+"") %>]&nbsp;来源:[<%=cmInfo %>]</a></h3></td>
		</tr>
		<tr id="panel_<%=appBo.getAppName() %>" style="display:none">
			<td >
			<div>
				<% 
				  long all_cm3 = 0;
				  long all_cm4 = 0;
				  Set<String> keys = new HashSet<String>();
				  for(KeyDistribFlowBo keyBo:keyBoList){
					 String keyName =  keyBo.getKeyName();
					 String[] keyNames = keyName.split("_");
					 keys.add(keyNames[2]+keyNames[3]);
					 
			  		
					 if(keyBo.getMap().get("CM3") != null){
						 all_cm3 +=  keyBo.getMap().get("CM3").getCallNum();
					 }
					 
					
					 if(keyBo.getMap().get("CM4") != null){
						 all_cm4 +=  keyBo.getMap().get("CM4").getCallNum();
					 }
				  }
				  
				 
				  
				  %>
				<table width="100%" border="1">
				    <tr>
				    <td width="100" align="center">来源应用</td>
				    <td width="100" align="center">来源方法</td>
				    <td colspan="3" align="center">总流量(<%=Utlitites.fromatLong(appBo.getCallNum()+"") %>)</td>
				    <td colspan="3" align="center">cm3(<%=Utlitites.fromatLong(all_cm3+"") %>)</td>
				    <td colspan="3" align="center">cm4(<%=Utlitites.fromatLong(all_cm4+"") %>)</td>
				  </tr>   
				  <tr>
				    <td align="center" ></td>
				    <td height="20" align="center" ><%=keys.size() %></td>
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
				    <td rowspan="<%=appBo.getKeyMap().size() %>" valign="top" align="center">&nbsp;&nbsp;<a href="./distrib_provider.jsp?opsName=<%=appBo.getAppName() %>"><%=appBo.getAppName() %></a></td>
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
				  <%} %> 
				</table>
			</div>
			</td>
		</tr>
	</table>
	
	
	<%} %>
</div>

</div><!-- End demo -->


<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>