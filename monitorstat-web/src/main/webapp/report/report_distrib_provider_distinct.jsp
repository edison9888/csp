<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.monitor.web.distrib.ProviderDisribFlowBo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorDistribFlowAo"%>
<%@page import="java.util.Date"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.web.distrib.AppDistribFlowBo"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.common.util.Arith"%>
<%@page import="java.util.Collections"%>
<%@page import="java.util.Comparator"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.monitor.web.distrib.KeyDistribFlowBo"%><html>
<%@ page import="com.taobao.monitor.web.cache.AppCache" %>
<%@ page import="com.taobao.monitor.common.po.AppInfoPo" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link rel="stylesheet" href="http://a.tbcdn.cn/p/header/header-min.css?t=20100913.css" />
<title>Insert title here</title>
</head>
<body>
<%
String appIds = request.getParameter("appIds");
String collectDay = request.getParameter("collectDay");
String[] opsNames = null;
if(appIds != null && !appIds.equals("") && !appIds.equals(",")){
	String[] appIdArray = appIds.split(",");
	opsNames = new String[appIdArray.length];
	for(int i=0;i<appIdArray.length;i++){
		AppInfoPo appInfo = AppCache.get().getKey(Integer.valueOf(appIdArray[i]));
		if(appInfo!=null)opsNames[i] = appInfo.getAppName();
	}
}else{
	opsNames = new String[]{};
}
for(String opsName:opsNames){

Date collect = null;

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
if(collectDay == null){
	Calendar cal = Calendar.getInstance();
	cal.add(Calendar.DAY_OF_MONTH,-1);
	collectDay = sdf.format(cal.getTime());
}
collect =sdf.parse(collectDay) ;



Calendar preCal = Calendar.getInstance();
preCal.setTime(collect);
preCal.add(Calendar.DAY_OF_MONTH,-7);
ProviderDisribFlowBo curBo = MonitorDistribFlowAo.get().getProviderDisribFlowBo(opsName,collect);
ProviderDisribFlowBo preBo = MonitorDistribFlowAo.get().getProviderDisribFlowBo(opsName,preCal.getTime());
if(preBo == null){
	preBo = new ProviderDisribFlowBo();
}
%>
<table width="100%" border="1">
	<tr>
		<td>
		
		
<table width="100%">
	<tr>
		<td align="center"><span style="font-size:20px;color: red"><strong><%=opsName %></strong></span><a href="./distrib_provider.jsp?opsName=<%=opsName %>">详细查看请点击</a></td>
	</tr>
</table>

<table width="100%" border="1">
	<tr>
		<td align="center"></td>
		<td align="center">本周</td>
		<td align="center">上周</td>
	</tr>				
	<tr>
		<td >依赖<%=opsName %>应用数:</td>
		<td align="center"><%=curBo.getCustomerAppNum() %></td>
		<td align="center"><%=preBo.getCustomerAppNum() %></td>
	</tr>
	<tr>
		<td width="100">总方法数:</td>
		<td align="center"><%=curBo.getCustomerMethodNum()%></td>
		<td align="center"><%=preBo.getCustomerMethodNum()%></td>
	</tr>				
	<tr>
		<td width="100">总调用量:</td>
		<td align="center"><%=Utlitites.fromatLong(curBo.getAllCalls()+"")%>(<%=Utlitites.scale(curBo.getAllCalls(),preBo.getAllCalls()) %>)</td>
		<td align="center"><%=Utlitites.fromatLong(preBo.getAllCalls()+"")%></td>
	</tr>
	<tr>
		<td width="100">出现总异常数:</td>
		<td align="center"><%=Utlitites.fromatLong(curBo.getAllExceptionNum()+"")%>(<%=Utlitites.scale(curBo.getAllExceptionNum(),preBo.getAllExceptionNum()) %>)</td>
		<td align="center"><%=Utlitites.fromatLong(preBo.getAllExceptionNum()+"")%></td>
	</tr>		
</table>

<%
Set<String> curAppSet = curBo.getCustomerAppNameSet();
Set<String> preAppSet = preBo.getCustomerAppNameSet();

Set<String> tmpCurAppSet = new HashSet<String>();
tmpCurAppSet.addAll(curAppSet);

tmpCurAppSet.removeAll(preAppSet);
if(tmpCurAppSet.size()>0){
	StringBuilder sb = new StringBuilder();
	Map<String, AppDistribFlowBo> curAppMap = curBo.getAppBoMap();
	for(String name:tmpCurAppSet){
		AppDistribFlowBo bo = curAppMap.get(name);
		double r = Arith.mul(Arith.div(bo.getCallNum(),curBo.getAllCalls(),6),100);
		if(r == 0 ){
			sb.append(name+"(少量);");
		}else{
			sb.append(name+"("+r+"%);");
		}
	}
	
%>
<table width="100%" border="1">
	<tr>
		<td width="100">新增依赖应用:</td>
		<td ><%=sb.toString() %></td>		
	</tr>
</table>
<%} %>



<%

Set<String> tmpPreAppSet = new HashSet<String>();
tmpPreAppSet.addAll(preAppSet);

tmpPreAppSet.removeAll(curAppSet);
if(tmpPreAppSet.size()>0){
	StringBuilder sb = new StringBuilder();
	Map<String, AppDistribFlowBo> preAppMap = preBo.getAppBoMap();
	for(String name:tmpPreAppSet){
		AppDistribFlowBo bo = preAppMap.get(name);
		double r = Arith.mul(Arith.div(bo.getCallNum(),preBo.getAllCalls(),6),100);
		if(r == 0 ){
			sb.append(name+"(少量);");
		}else{
			sb.append(name+"("+r+"%);");
		}
	}
	
%>
<table width="100%" border="1">
	<tr>
		<td >消失依赖应用:</td>
		<td ><%=sb.toString() %></td>		
	</tr>
</table>
<%} %>

<%
Map<String,KeyDistribFlowBo> curkeyAllMap = curBo.getAllMethodCall();
Map<String,KeyDistribFlowBo>  prekeyAllMap = preBo.getAllMethodCall();
List<KeyDistribFlowBo> _tmpKeyList = new ArrayList<KeyDistribFlowBo>();
_tmpKeyList.addAll(curkeyAllMap.values());

for(KeyDistribFlowBo bo:_tmpKeyList){
	KeyDistribFlowBo tmp = prekeyAllMap.get(bo.getKeyName());
	if(tmp!=null)
		bo.setPreCallNum(tmp.getCallNum());
}

Collections.sort(_tmpKeyList,new Comparator<KeyDistribFlowBo>(){
	 public int compare(KeyDistribFlowBo o1, KeyDistribFlowBo o2){
		 
		 if(o1.getCallNum() > o2.getCallNum()){
			 return -1;
		 }else if(o1.getCallNum() < o2.getCallNum()){
			 return 1;
		 }		 
		 return 0;
	 }
});






%>

<table width="100%" border="1">
	<tr>
		<td colspan="4"><span style="font-size:16px;color: #003300"><strong>方法调用次数前10名</strong></span></td>
	</tr>
	<%
		for(int i=0;i<_tmpKeyList.size()&&i<10;i++){
			KeyDistribFlowBo bo = _tmpKeyList.get(i);
			KeyDistribFlowBo prekey = prekeyAllMap.get(bo.getKeyName());
			if(prekey == null){
				prekey = new KeyDistribFlowBo();
			}
	%>
	<tr>
		<td width="300px"><%=bo.getKeyName() %></td>
		<td width="150px"><%=bo.getCallNum() %>(<%=Utlitites.scale(bo.getCallNum(),prekey.getCallNum()) %>)</td>
		<td width="100px"><%=prekey==null?"新增":prekey.getCallNum() %></td>
		<td style="WORD-BREAK: break-all; WORD-WRAP: break-word"><%=bo.getCallThisMethodAppSet() %></td>
	</tr>
	<%} %>
</table>



<%

List<KeyDistribFlowBo> _tmpKeyList1 = new ArrayList<KeyDistribFlowBo>();
for(KeyDistribFlowBo bo:_tmpKeyList){
	if(bo.getPreCallNum() >100000&&bo.getCallNum() >100000){
		_tmpKeyList1.add(bo);
	}
}
_tmpKeyList = _tmpKeyList1;

Collections.sort(_tmpKeyList,new Comparator<KeyDistribFlowBo>(){
	 public int compare(KeyDistribFlowBo o1, KeyDistribFlowBo o2){
		 
		 double r1 = Arith.div(o1.getCallNum()-o1.getPreCallNum(),o1.getPreCallNum());
		 double r2 = Arith.div(o2.getCallNum()-o2.getPreCallNum(),o2.getPreCallNum());
		 if(r1 > r2){
			 return -1;
		 }else if(r1 < r2){
			 return 1;
		 }		 
		 return 0;
	 }
});

%>

<table width="100%" border="1">
	<tr>
		<td colspan="4"><span style="font-size:16px;color: #003300"><strong>方法增长前10名</strong></span></td>
	</tr>
	<tr>
		<td>方法</td>
		<td>变化率</td>
		<td>当前次数</td>
		<td>上周次数</td>
	</tr>
	<%
	for(int i=0;i<_tmpKeyList.size()&&i<10;i++){ 
		KeyDistribFlowBo bo = _tmpKeyList.get(i);
	%>
	<tr>
		<td width="300px"><%=bo.getKeyName() %></td>
		<td width="100px"><%=Utlitites.scale(bo.getCallNum(),bo.getPreCallNum()) %> </td>
		<td width="100px"><%=bo.getCallNum() %></td>
		<td width="100px"><%=bo.getPreCallNum() %></td>
	</tr>
	<%} %>
</table>

<table width="100%" border="1">
	<tr>
		<td colspan="4"><span style="font-size:16px;color: #003300"><strong>方法下降前10名</strong></span></td>
	</tr>
	<tr>
		<td>方法</td>
		<td>变化率</td>
		<td>当前次数</td>
		<td>上周次数</td>
	</tr>
	<%
	for(int i=_tmpKeyList.size()-1;i>_tmpKeyList.size()-11&&i>0;i--){ 
		KeyDistribFlowBo bo = _tmpKeyList.get(i);
		if(bo.getCallNum()>bo.getPreCallNum()){
			continue;
		}
	%>
	<tr>
		<td width="300px"><%=bo.getKeyName() %></td>
		<td width="100px"><%=Utlitites.scale(bo.getCallNum(),bo.getPreCallNum()) %></td>
		<td width="100px"><%=bo.getCallNum() %></td>
		<td width="100px"><%=bo.getPreCallNum() %></td>
	</tr>
	<%} %>
</table>


















<%

List<AppDistribFlowBo> appBoList = curBo.getAppBoList();

List<AppDistribFlowBo> _tmpappBoList = new ArrayList<AppDistribFlowBo>();

for(AppDistribFlowBo appBo:appBoList){
	String appName = appBo.getAppName();
	AppDistribFlowBo preAppBo = preBo.getAppBoMap().get(appName);
	if(preAppBo == null){
		continue;
	}
	if(preAppBo.getCallNum()<100000&&appBo.getCallNum()<100000){
		continue;
	}
	_tmpappBoList.add(appBo);
	long _tmp = appBo.getCallNum()-preAppBo.getCallNum();
	appBo.setPreCallNum(preAppBo.getCallNum());
	appBo.setRate(Arith.mul(Arith.div(_tmp,preAppBo.getCallNum(),4),100));	
}

Collections.sort(_tmpappBoList,new Comparator<AppDistribFlowBo>(){
	 public int compare(AppDistribFlowBo o1, AppDistribFlowBo o2){
		 
		 if(o1.getRate() > o2.getRate()){
			 return -1;
		 }else if(o1.getRate() < o2.getRate()){
			 return 1;
		 }		 
		 return 0;
	 }
});


%>
<table width="100%" border="1">
	<tr>
		<td colspan="4"><span style="font-size:16px;color: #003300"><strong>应用增长前10名</strong></span></td>
	</tr>
	<tr>
		<td>应用</td>
		<td>变化率</td>
		<td>当前次数</td>
		<td>上周次数</td>
	</tr>
	<%
	for(int i=0;i<_tmpappBoList.size()&&i<10;i++){ 
		AppDistribFlowBo bo = _tmpappBoList.get(i);
	%>
	<tr>
		<td width="200px"><%=bo.getAppName() %></td>
		<td width="200px"><%=bo.getRate() %>%</td>
		<td width="200px"><%=bo.getCallNum() %></td>
		<td width="200px"><%=bo.getPreCallNum() %></td>
	</tr>
	<%} %>
</table>

<table width="100%" border="1">
	<tr>
		<td colspan="4"><span style="font-size:16px;color: #003300"><strong>应用下降前10名</strong></span></td>
	</tr>
	<tr>
		<td width="200px">应用</td>
		<td>变化率</td>
		<td>当前次数</td>
		<td>上周次数</td>
	</tr>
	<%
	for(int i=_tmpappBoList.size()-1;i>_tmpappBoList.size()-11&&i>0;i--){ 
		AppDistribFlowBo bo = _tmpappBoList.get(i);
		if(bo.getRate()>0){
			continue;
		}
	%>
	<tr>
		<td width="200px"><%=bo.getAppName() %></td>
		<td width="200px"><%=bo.getRate() %>%</td>
		<td width="200px"><%=bo.getCallNum() %></td>
		<td width="200px"><%=bo.getPreCallNum() %></td>
	</tr>
	<%} %>
</table>
</td>
	</tr>
</table>
<%} %>
</body>
</html>