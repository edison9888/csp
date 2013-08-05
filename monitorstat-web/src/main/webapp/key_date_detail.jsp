<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>

<%
//这个是一天为单位显示走势变化


String appId = request.getParameter("appId");
String keyId = request.getParameter("keyId");
String startCollectTime = request.getParameter("startCollectTime");
String endCollectTime = request.getParameter("endCollectTime");
if(appId!=null&&keyId!=null&&endCollectTime!=null){	
	if(startCollectTime==null){
		SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
		Date date = parseLogFormatDate.parse(endCollectTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH,-7);
		startCollectTime = parseLogFormatDate.format(cal.getTime());
	}
	
	List<KeyValuePo> keyvalueList = MonitorDayAo.get().findMonitorDataListByDate(Integer.parseInt(appId),Integer.parseInt(keyId),startCollectTime,endCollectTime);
	
	Collections.sort(keyvalueList,new Comparator<KeyValuePo>(){
		 public int compare(KeyValuePo o1, KeyValuePo o2){
			 if(o1.getCollectTime().getTime() >o2.getCollectTime().getTime()){
				 return 1;
			 }else{
				 return -1;
			 }
		 }
	});
	
	String reslut = createCharXml(keyvalueList);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
<script type="text/javascript">



function goToAppDateDetail(){
	
	var start = $('#datepicker_start').val();
	var end = $('#datepicker_end').val();
	var url = "key_date_detail.jsp?appId=<%=appId%>&keyId=<%=keyId%>&endCollectTime="+end+"&startCollectTime="+start;
	location.href=url;
}

</script>
</head>
<body>

<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<table width="100%">
<tr>
	<td width="100%" align="center">
		<a href="key_time_detail.jsp?appId=<%=appId%>&keyId=<%=keyId%>&collectTime=<%=endCollectTime %>">查看以分钟统计</a>
		<a href="#">查看以天统计</a>
	</td>
</tr>
<tr>
	<td width="100%" align="center">
	开始日期: <input type="text" id="datepicker_start" value="<%=startCollectTime%>"/>
	结束日期: <input type="text" id="datepicker_end" value="<%=endCollectTime%>"/><button  onclick="goToAppDateDetail()">查看</button>
	</td>
</tr>
</table>

</div>


<div id="chartdiv" align="center">


</div>
</body>


<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "800", "400", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=reslut%>"));
so.write("chartdiv");			

</script>
<%}else{ %>
参数不正确！
<%} %>


<%!
public static String createCharXml(List<KeyValuePo> keyValueList){
	StringBuffer head =  new StringBuffer("<chart>");
	String cat = createCharCategories(keyValueList);
	head.append(cat);
	String dataSet = createDataSet(keyValueList);
	head.append(dataSet);		
	head.append("</chart>");
	return head.toString();
}

private static String createDataSet(List<KeyValuePo> keyValueList){

StringBuilder sb = new StringBuilder();
sb.append("<graphs>");
sb.append("<graph gid='0' title='走势图'>");			
for(int i=0;i<keyValueList.size();i++){
	KeyValuePo label = keyValueList.get(i);
	sb.append("<value xid='"+i+"'>"+label.getValueStr()+"</value>");
}	
sb.append("</graph>");	
sb.append("</graphs>");
return sb.toString();
}



private static String createCharCategories(List<KeyValuePo> keyValueList){

SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");


StringBuilder sb = new StringBuilder();
sb.append("<series>");

for(int i=0;i<keyValueList.size();i++){
	KeyValuePo po = keyValueList.get(i);
	sb.append("<value xid='"+i+"'>"+sdf.format(po.getCollectTime())+"</value>");			
}
sb.append("</series>");
return sb.toString();
}

%>

</html>