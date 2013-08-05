<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk"%>
<%@ page session="false"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.vo.AlarmRecordPo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.ao.MonitorAlarmAo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gbk">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>Ӧ�ü��ϵͳ-�澯��ʷ��¼</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
</head>
<body>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr>
	  <td> <jsp:include page="../top.jsp"></jsp:include></td>
  	</tr>
<jsp:include page="../left.jsp"></jsp:include>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String currentDay = request.getParameter("currentDay");
if(currentDay==null){
	currentDay = sdf.format(new Date());
}

String appId = request.getParameter("appId");
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
if(appId==null){
	appId = listApp.get(0).getAppId()+"";
}
%>
<tr><td align="center">
<form action="alarm_record.jsp">
<table width="1000" >
  <tr>
    <td colspan="4" align="center">
    	����:<input type="text" name="currentDay" value="<%=currentDay %>"/>
		Ӧ������:<select name="appId">
			<%for(AppInfoPo app:listApp){%> 
				<option value="<%=app.getAppId() %>" <%if(app.getAppId()==Integer.parseInt(appId)){out.print("selected");} %>><%=app.getAppName() %></option>
			<%} %>
			</select>
		<input type="submit" value="�鿴" />&nbsp;&nbsp;&nbsp;<input type="button" value="�ύ�Ż���¼" onclick="window.open('http://cm.taobao.net:9999/monitorstat/health/manage_optimize_record.jsp?appid=<%=appId %>')"/>
	</td>
  </tr>
   <tr><td><table class="datalist"  width="1000">
  <tr class="ui-widget-header ">
    <td width="200" align="center">key����</td>
    <td width="400" align="center">���ո澯</td>
    <td width="200" align="center">����</td>    
    <td width="200" align="center">�鿴��ʷ�澯</td>
  </tr>
  <%
  List<AlarmRecordPo> list = MonitorAlarmAo.get().getAppAlarmCountByDate(Integer.parseInt(appId),sdf.parse(currentDay));
  
  for(AlarmRecordPo po:list){
	  int sum = 0;
  %>
	<tr>
    	<td align="center"><%=po.getAlarmKeyName() %></td>
    	<td align="center">
    		<%
    		Map<String,Integer> siteMap = po.getSiteMap();
    		for(Map.Entry<String,Integer> siteEntry:siteMap.entrySet()){
    			sum+=siteEntry.getValue();
    			%>
    			<%=siteEntry.getKey() %>:<font color="red"><%=siteEntry.getValue() %></font>��<br/>
    			<%
    		}
    		%>
		</td>
		<td align="center">
    		<%=sum %>
		</td>
   	 	<td align="center"><a href="alarm_record_flash.jsp?appId=<%=appId%>&keyId=<%=po.getAlarmkeyId()%>" target="_blank">�鿴��ʷ�澯</a>&nbsp;&nbsp;<a href="alarm_record_flash_pie.jsp?appId=<%=appId%>" target="_blank">�鿴�澯�ֲ�</a></td>
  	</tr>	
  <%
  }
  %>  
  </table></td></tr>
</table>
<jsp:include page="../bottom.jsp"></jsp:include>
</form>
</td></tr></table>
</body>
</html>