<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="java.util.*"%>
<%@page import="com.taobao.monitor.common.ao.center.MonitorTairAo"%>
<%@page import="com.taobao.monitor.common.po.TairSumData"%>
<%@page import="com.taobao.monitor.common.po.TairNamespacePo"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%
//这个是一天为单位显示走势变化

String appName = request.getParameter("appName");
String groupName = request.getParameter("groupName");
String startCollectTime = request.getParameter("startCollectTime");
String endCollectTime = request.getParameter("endCollectTime");
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd");
if(appName!=null&&groupName!=null&&endCollectTime!=null){	
	if(startCollectTime==null){
		Date date = parseLogFormatDate.parse(endCollectTime);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH,-7);
		startCollectTime = parseLogFormatDate.format(cal.getTime());
	}
	
	Map<String, TairSumData> timeTairMap = MonitorTairAo.getInstance().findTairProviderAppChart(groupName, appName, startCollectTime, endCollectTime);
	Map<Date, TairSumData> dateTairMap = new HashMap<Date, TairSumData>();
	// 将timeTairMap转化为dateTairMap
	for (Map.Entry<String, TairSumData> entry : timeTairMap.entrySet()) {
		Date date = parseLogFormatDate.parse(entry.getKey());
		dateTairMap.put(date, entry.getValue());
	}
	
	List<Map.Entry<Date, TairSumData>> timeTairMapList = new ArrayList<Map.Entry<Date, TairSumData>>(dateTairMap.entrySet());
	Comparator<Map.Entry<Date, TairSumData>> compara = new Comparator<Map.Entry<Date, TairSumData>>() {
		public int compare(Map.Entry<Date, TairSumData> o1, Map.Entry<Date, TairSumData> o2) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date sDate = o1.getKey();
			Date eDate = o2.getKey();
			if(sDate.after(eDate)){
				return 1;
			}else{
				return -1;
			}
		}
	};
	
	Collections.sort(timeTairMapList,compara);
	
	String reslut = createCharXml(timeTairMapList);
	
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>性能图</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/userjs/mainpage.js"></script>
<script language="javascript" type="text/javascript" src="<%=request.getContextPath()%>/statics/datePicket/WdatePicker.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>

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
	var url = "tair_date_chart.jsp?appName=<%=appName%>&groupName=<%=groupName%>&endCollectTime="+end+"&startCollectTime="+start;
	location.href=url;
}

</script>
</head>
<body>

<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<table width="100%">
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
var so = new SWFObject("<%=request.getContextPath()%>/statics/js/amcharts/flash/amline.swf", "amline", "800", "400", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath()%>/statics/js/amcharts/flash/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath()%>/statics/js/amcharts/flash/setting/amline_settings1.xml");
so.addVariable("chart_data", encodeURIComponent("<%=reslut%>"));
so.write("chartdiv");			

</script>
<%}else{ %>
参数不正确！
<%} %>


<%!
public static String createCharXml(List<Map.Entry<Date, TairSumData>> timeTairMapList){
	StringBuffer head =  new StringBuffer("<chart>");
	String cat = createCharCategories(timeTairMapList);
	head.append(cat);
	String dataSet = createDataSet(timeTairMapList);
	head.append(dataSet);		
	head.append("</chart>");
	return head.toString();
}

private static String createDataSet(List<Map.Entry<Date, TairSumData>> timeTairMapList){

	StringBuilder sb = new StringBuilder();
	sb.append("<graphs>");
	sb.append("<graph gid='0' title='走势图'>");			
	for(int i=0;i<timeTairMapList.size();i++){
		sb.append("<value xid='"+i+"'>"+timeTairMapList.get(i).getValue().getAppCallSum()+"</value>");
	}	
	sb.append("</graph>");	
	sb.append("</graphs>");
	return sb.toString();
}



private static String createCharCategories(List<Map.Entry<Date, TairSumData>> timeTairMapList){
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	StringBuilder sb = new StringBuilder();
	sb.append("<series>");
	
	for(int i=0;i<timeTairMapList.size();i++){
		String str = sdf.format(timeTairMapList.get(i).getKey());
		sb.append("<value xid='"+i+"'>"+str+"</value>");			
	}
	sb.append("</series>");
	return sb.toString();
}

%>

</html>