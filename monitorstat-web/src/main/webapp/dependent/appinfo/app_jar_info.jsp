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
<%@page import="java.util.Locale"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/My97DatePicker/WdatePicker.js"></script>
<title>Insert title here</title>
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
}
</style>
</head>

<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

SimpleDateFormat sdf1 = new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy",Locale.ENGLISH);
SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.ENGLISH);
String webxml = request.getParameter("webxml");
String jarName = request.getParameter("jarName");
String collectTime = request.getParameter("collectTime");
String showApp = request.getParameter("showApp");
if(collectTime ==null){
	collectTime = sdf.format(new Date());
}else{
	
}
if(StringUtils.isBlank(jarName)){
	jarName = "session"; 
}
List<String> jars = AppJarAo.get().findjarName(sdf.parse(collectTime));
if(webxml==null){
	webxml = "online";
}
Pattern pattern = Pattern.compile(">(.*"+webxml+".*)<");

%>

<body>
<jsp:include page="../../head.jsp"></jsp:include>
<%
if(jarName != null){
	Map<String,List<AppJar>> mapHost = AppJarAo.get().findAppStatusLikejarname(showApp,jarName,sdf.parse(collectTime));
	Map<String,Map<String,AppStatus>> mapapp =  AppJarAo.get().findAllAppStatus(sdf.parse(collectTime));
	
	Map<String,AppStatus> hostipmap = mapapp.get(showApp);
	if(hostipmap == null){
		hostipmap = new HashMap<String,AppStatus>();
	}
	
%>
<table border="1" width="100%">
	<%
		
	%>
	<tr>
		<td><h3><a style="color:#3366CC" href="javascript:showPanel('<%=showApp %>')">&nbsp;&nbsp;<%=showApp %>&nbsp;</a></h3></td>
	</tr>
	<tr id="panel_<%=showApp %>" >
		<td >
			<table width="100%" border="1">
				<tr>
					<td>机器IP</td>
					<td>机房</td>
					<td>
						<table width="100%" border="1">
							<tr>
								<td colspan="3">jar</td>								
							</tr>
							<tr>
								<td>版本</td>
								<td>大小</td>
								<td>时间</td>
							</tr>
						</table>
					</td>
					<td>web.xml信息</td>
					<td>jboss启动时间</td>
					<td>apache启动时间</td>
				</tr>
				<%
				for(Map.Entry<String,AppStatus> appEntry:hostipmap.entrySet() ){
					String hostIP = appEntry.getKey();
					AppStatus app = appEntry.getValue();
					%>
					<tr>
						<td><a href="./ip_jar_info.jsp?id=<%=app.getId() %>"><%=app.getHostIp() %></a></td>
						<td><%=app.getHostSite() %></td>
						<td>
						<table >
							<%
								List<AppJar> list = mapHost.get(hostIP);
								if(list!= null){
								for(AppJar jar:mapHost.get(hostIP)){
							%>
							<tr>
								<td><%=jar.getJarName() %></td>
								<td><%=jar.getJarSize() %></td>
								<td><%=sdf2.format(sdf1.parse(jar.getModifyTime()))%></td>
							</tr>
							<%
								}}
							%>							
						</table>						
						</td>
						<td>
						<%
						Matcher m = pattern.matcher(app.getWebInfo());
						if(m.find()){
							out.print(m.group(1));
						}
						%>
						</td>
						<td><%=app.getJbossStartTime() %></td>
						<td><%=app.getHttpdStartTime().replaceAll("\n","</br>") %></td>
					</tr>
				<%} %>
			</table>			
		</td>
	</tr>
</table>
<%} %>
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
<jsp:include page="../../buttom.jsp"></jsp:include>
</body>
</html>