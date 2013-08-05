<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<%@ page import="com.taobao.monitor.web.rating.*"%>
<%
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String collectTime1 =request.getParameter("collectTime1");
if(collectTime1 == null){
	collectTime1 =  sdf.format(new Date());
}
String start1 = collectTime1+" 00:00:00";
String end1 = collectTime1+" 23:59:59";
SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd HH:mm");
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
String reslut = "";
String aimName = request.getParameter("aimName");
String appName = request.getParameter("appName");
String keyId   = request.getParameter("keyId") ;
String appId   = request.getParameter("appId") ;
Map<String,List<KeyValuePo>> map1 = MonitorTimeAo.get().findKeyValueSiteByDate(Integer.parseInt(appId),Integer.parseInt(keyId) , parseLogFormatDate.parse(start1), parseLogFormatDate.parse(end1)) ;
HashSet<String> keySet = new HashSet<String>();
keySet.addAll(map1.keySet());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>



<style type="text/css">
body {
	font-size: 62.5%;
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
</style>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
	<table width="100%">
		<tr>
		   <td align="center">			
			<%=appName %> ：<%=aimName %>
		   </td>
		 </tr>		
	</table>
</div>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" ><%=keySet.toString() %></font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<div id="chartdiv" align="center">
<table>
	<tr>
		<%
         int k = 0;
         for(Map.Entry<String,List<KeyValuePo>> entry:map1.entrySet()){
        	
                 List<KeyValuePo> poList = entry.getValue();
                 Collections.sort(poList);
                 k++;
         %>
         <td valign="top" width="15%">
                 <table width="100%" border="1" class="ui-widget ui-widget-content">
                 		<tr class="headcon ">
                              <td align="center" colspan="2"><%=entry.getKey() %></td>                              
                         </tr>
                         <tr class="headcon ">
                              <td align="center">收集时间</td>
                              <td align="center">次数</td>
                         </tr>
                         <%for(KeyValuePo po:poList){ %>
                         <tr>
                                 <td><%=sdf2.format(po.getCollectTime()) %></td>
                                 <td><%=po.getValueStr()%></td>
                         </tr>
                         <%} %>
                 </table>

         </td>
         <%} %>
         <td valign="top" width="<%=100-(k*10)%>%">
          </td>		
	</tr>
</table>

</div>
</div>
</div>

</body>
</html>