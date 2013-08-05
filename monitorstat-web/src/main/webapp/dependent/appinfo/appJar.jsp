<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.dependent.ao.AppJarAo"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.dependent.appinfo.AppStatus"%>
<%@page import="java.util.Map"%>
<%@page import="com.taobao.monitor.dependent.appinfo.AppJar"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Collections"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<title>Insert title here</title>
</head>

<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

String webxml = request.getParameter("webxml");
String jarName = request.getParameter("jarName");
String collectTime = request.getParameter("collectTime");
if(collectTime ==null){
	collectTime = sdf.format(AppJarAo.get().getRecentlyCollectTime());
}else{
	
}

if(StringUtils.isBlank(jarName)){
	jarName = "session"; 
}
if(jarName.length()<3){
	jarName = "session"; 
}
if(webxml==null){
	webxml = "online";
}


Pattern pattern = Pattern.compile(">(.*"+webxml+".*)<");

%>

<body>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr>
	  <td> <jsp:include page="../../top.jsp"></jsp:include></td>
  	</tr>

  	<tr><td align="center">
<form action="./appJar.jsp" method="get">
jar名称:<input type="text" name="jarName" value="<%=jarName %>">&nbsp;&nbsp;
时间:<input type="text" name="collectTime" value="<%=collectTime %>" class="Wdate" onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" > &nbsp;&nbsp;
webxml匹配的内容:<input type="text" name="webxml" value="<%=webxml %>">&nbsp;&nbsp;
<input type="submit" value="提交">
</form>
</td></tr>
 <tr><td><table class="datalist"  width="1000">
  <tr class="ui-widget-header ">
    <td width="89">应用名称</td>
    <td width="95">jar版本</td>
    <td width="95">webxml信息</td>
    <td width="95">jboss启动时间</td>
    <td width="95">apache启动时间</td>
  </tr>
 <%

if(jarName != null){
	Map<String,Map<String,AppStatus>> mapApp = AppJarAo.get().findAllAppStatus(sdf.parse(collectTime));
	for(Map.Entry<String,Map<String,AppStatus>> entry:mapApp.entrySet()){
		String appName = entry.getKey();
		Map<String,AppStatus> hostmap = entry.getValue();
		int hostSize = hostmap.size();
%>
  <tr>
    <td><a href="app_jar_info.jsp?showApp=<%=entry.getKey() %>&webxml=<%=webxml %>&jarName=<%=jarName %>&collectTime=<%=collectTime %>" target="_blank"><%=entry.getKey() %></a></td>
    <td>
		<%
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH");
		Map<String,Integer> jarMap = new HashMap<String,Integer>();
		Map<String,Integer> webxmlMap = new HashMap<String,Integer>();
		Map<String,Integer> jbossMap = new HashMap<String,Integer>();
		Map<String,Integer> apacheMap = new HashMap<String,Integer>();
		
		Map<String,List<AppJar>> mapJar = AppJarAo.get().findAppStatusLikejarname(appName,jarName,sdf.parse(collectTime));
		
		
		for(Map.Entry<String,AppStatus> hostEntry:hostmap.entrySet()){
			
			String hostIP =  hostEntry.getKey();
			
			
			AppStatus app = hostEntry.getValue();
			List<AppJar> listJar = mapJar.get(hostIP);
			if(listJar !=null){
				for(AppJar jar:listJar){
					Integer s = jarMap.get(jar.getJarName());
					if(s ==null){
						jarMap.put(jar.getJarName(),1);
					}else{
						jarMap.put(jar.getJarName(),1+s);
					}
				}
			}
			Matcher m = pattern.matcher(app.getWebInfo());
			if(m.find()){
				Integer s = webxmlMap.get(m.group(1));
				if(s ==null){
					webxmlMap.put(m.group(1),1);
				}else{
					webxmlMap.put(m.group(1),1+s);
				}
			}
			
			
			String jbossTime = app.getJbossStartTime().substring(0,13);
			
			Integer t1 =jbossMap.get(jbossTime);
			if(t1 == null){
				jbossMap.put(jbossTime,1);
			}else{
				jbossMap.put(jbossTime,1+t1);
			}
			
			String[] r = app.getHttpdStartTime().split("\n");
			for(String a:r){
				if(a.length()>13){
					String apache = a.substring(0,13);
					Integer a1 =apacheMap.get(jbossTime);
					if(a1 == null){
						apacheMap.put(jbossTime,1);
					}else{
						apacheMap.put(jbossTime,1+a1);
					}
				}else{
				}
			}
			
			
		}
		for(Map.Entry<String,Integer> jarEntry:jarMap.entrySet()){
			out.print(jarEntry.getKey()+"("+jarEntry.getValue()+")</br>");
		}
		
		%>
	</td>
    <td>
		<%
		for(Map.Entry<String,Integer> webxmlEntry:webxmlMap.entrySet()){
			out.print(webxmlEntry.getKey()+"("+webxmlEntry.getValue()+")</br>");
		}
		%>
	</td>
	<td width="95">
		<%
		List<String> listJboss= new ArrayList<String>();
		listJboss.addAll(jbossMap.keySet());
		Collections.sort(listJboss);
		for(String time:listJboss){
			out.print(time+"("+jbossMap.get(time)+")</br>");
		}
		%>
	</td>
    <td width="95">
		<%
		List<String> listApache= new ArrayList<String>();
		listApache.addAll(apacheMap.keySet());
		Collections.sort(listApache);
		for(String time:listApache){
			out.print(time+"("+apacheMap.get(time)+")</br>");
		}
		%>
	</td>
  </tr>
  <%}} %>
</table></td></tr>

	<tr>
	  <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
	</tr>
	<tr><td><jsp:include page="../../bottom.jsp"></jsp:include></td></tr>
</table>
</body>
</html>