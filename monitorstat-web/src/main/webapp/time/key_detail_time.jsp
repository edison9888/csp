<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>

<%@ page import="java.text.ParseException" %>
<%@ page import="com.taobao.monitor.common.util.Arith" %>
<%@ page import="com.taobao.monitor.common.util.Constants" %>
<%@ page import="com.taobao.monitor.web.ao.MonitorBaseLineAo"%>

<%
//这个是一天为单位显示走势变化

SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

String collectTime1 = request.getParameter("collectTime1");
if(collectTime1 == null){
	 collectTime1 = sdf.format(new Date());
}
String start1 = collectTime1+" 00:00:00";
String end1 = collectTime1+" 23:59:59";

String viewTpye = request.getParameter("viewTpye");

String collectTime2 = request.getParameter("collectTime2");


String reslut = "";
SimpleDateFormat parseLogFormatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

String aimName = request.getParameter("aimName");

String appName1 = request.getParameter("appName");
String keyName = request.getParameter("keyName");
String keyId   = request.getParameter("keyId") ;

String appId = request.getParameter("appId");
String appName = "";
if(appName1 != null){
	appName = AppCache.get().getKey(appName1).getAppName();
	appId = AppCache.get().getKey(appName1).getAppId()+"";
}
if(appId != null&&!"".equals(appId.trim())){
	appName = AppCache.get().getKey(Integer.parseInt(appId)).getAppName();
}



if(keyName == null) {
	
	if(keyId != null && !"".equals(keyId)) {
		keyName = KeyCache.get().getKey(Integer.parseInt(keyId)).getKeyName();
	}
}
Map<String,List<KeyValuePo>> map1 = MonitorTimeAo.get().findKeyValueSiteByDate(Integer.parseInt(appId),Integer.parseInt(keyId), parseLogFormatDate.parse(start1), parseLogFormatDate.parse(end1)) ;
List<KeyValuePo> baselineList = MonitorBaseLineAo.get().findKeyBaseValue(Integer.parseInt(appId),Integer.parseInt(keyId));
Map<String,List<KeyValuePo>> map2 = new HashMap<String,List<KeyValuePo>>();

if(collectTime2!=null){
	
	//若对比时间的time2没哟填写，则默认为time1
	if(collectTime2.equals("") || collectTime2.equals("null")) {
		
		collectTime2 = collectTime1;
	}
	String start2 = collectTime2+" 00:00:00";
	String end2 = collectTime2+" 23:59:59";	
	map2 = MonitorTimeAo.get().findKeyValueSiteByDate(Integer.parseInt(appId),Integer.parseInt(keyId), parseLogFormatDate.parse(start2), parseLogFormatDate.parse(end2)) ;
		
}

HashSet<String> keySet = new HashSet<String>();
keySet.addAll(map1.keySet());
keySet.addAll(map2.keySet());


//加入统计表格的逻辑
Pattern patternCount = Pattern.compile(".*COUNTTIMES$");				//以COUNTTIMES结尾的
Pattern patternAvg = Pattern.compile(".*AVERAGEUSERTIMES$");		//以AVERAGEUSERTIMES结尾，开头任意字符
Matcher matcherAvg = null;
Matcher matcherCount = null;
Map<String,Double> sumMap1 = new HashMap<String, Double>();			//保存统计后的结果
Map<String,Double> sumMap2 = new HashMap<String, Double>();			//保存统计后的结果
double sum1 = 0;
double allNum1 = 0;		//统计个数
double sum2 = 0;
double allNum2 = 0;
try {
	matcherAvg = patternAvg.matcher(keyName);
	matcherCount = patternCount.matcher(keyName);
	//结尾匹配成功才统计
	if(matcherAvg.matches() || matcherCount.matches()) {
		
		for(Map.Entry<String,List<KeyValuePo>> entry : map1.entrySet()) {
			
			sum1 = 0;
			allNum1 = 0;
			
			for(KeyValuePo k : entry.getValue()) {
									
				sum1 += Double.parseDouble(k.getValueStr());
				allNum1 += 1;
			}
			
			if(matcherAvg.matches()) {
				
				sum1 = Arith.div(sum1, allNum1, 2);
			}
			
			sumMap1.put(entry.getKey(),sum1);
		}
		
		for(Map.Entry<String,List<KeyValuePo>> entry : map2.entrySet()) {
			
			sum2 = 0;
			allNum2 = 0;
			
			for(KeyValuePo k : entry.getValue()) {
									
				sum2 += Double.parseDouble(k.getValueStr());
				allNum2 += 1;
			}
			
			if(matcherAvg.matches()) {
				
				sum2 = Arith.div(sum2, allNum2, 2);
			}
			
			sumMap2.put(entry.getKey(),sum2);
		}

	}
} catch(Exception e) {
	
	e.printStackTrace();
	System.out.println("统计表格部分出错");
}

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.web.util.AmLineFlash"%>
<%@page import="java.util.regex.Pattern"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.regex.Matcher"%>
<%@page import="com.taobao.monitor.web.cache.KeyCache"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>性能图</title>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>


<style type="text/css">

</style>
<script type="text/javascript">

function goToComparedDetailMonitor(){
	var collectTime2 = $('#datepicker').val();	
	location.href="./key_detail_time.jsp?keyId=<%=keyId%>&appName=<%=appName%>&collectTime2="+collectTime2;
}
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<jsp:include page="../left.jsp"></jsp:include>
<div class="ui-widget-content ui-corner-all" style=" font-size: 1.2em; margin: .6em 0;">
<form action="./key_detail_time.jsp" method="get">
	<table width="100%">
		<tr>
		   <td align="center">
		  	日期1: <input type="text" name="collectTime1" value="<%=collectTime1 %>"/>			
			日期2: <input type="text" name="collectTime2" value="<%=collectTime2==null?"":collectTime2 %>"/>
			<input type="submit" value="对比"  />
			<input type="hidden" value="<%=keyId%>" name="keyId"  />
			<input type="hidden" value="<%=appName==null?"":appName%>" name="appName"  />
			<input type="hidden" value="<%=appId==null?"":appId%>" name="appId"  />
			<input type="hidden" value="<%=(viewTpye==null)?"":viewTpye%>" name="viewTpye"  />
			<input type="hidden" value="<%=aimName%>" name="aimName"  />
		   </td>
		 </tr>
		 <tr>
		   <td align="center">			
			<%=appName %> ：<%=keyName %>
		   </td>
		 </tr>
	</table>
</form>
</div>
<%

	Map<String,List<KeyValuePo>> map = new HashMap<String,List<KeyValuePo>>();
	map.put("基线",baselineList);
	if(map1!=null){
		for(Map.Entry<String,List<KeyValuePo>> entry:map1.entrySet()){
			map.put(entry.getKey()+"["+collectTime1+"]",entry.getValue());
		}
	}
	if(map2!=null){
		for(Map.Entry<String,List<KeyValuePo>> entry:map2.entrySet()){
			map.put(entry.getKey()+"["+collectTime2+"]",entry.getValue());
		}
	}
	
	
	reslut = AmLineFlash.createCharXml1(map,"true".equals(viewTpye)) ; 
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" ><%=keySet.toString() %></font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<div id="chartdiv" align="center">
</div>
</div>
</div>

<%
	// 为汇总显示取数据
	String resultTotal = "";
	String totalFlag = "Total";
	Map<String,Map<String, Double>> totalMap = new HashMap<String,Map<String, Double>>();
	
	String totalKey1 = "Total" + "["+collectTime1+"]";
	Map<String, Double> totalValues1 = new HashMap<String, Double>();
	if(map1!=null){
		for(Map.Entry<String,List<KeyValuePo>> entry : map1.entrySet()) {
				
			for(KeyValuePo keyValuePo : entry.getValue()) {
					
				Double valueData = 	Double.parseDouble(keyValuePo.getValueStr());
				String timeKey = keyValuePo.getCollectTimeStr();
					
				if (totalValues1.keySet().contains(timeKey)) {
					boolean isAvg = keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG) > -1;
					if (isAvg) {
						Double newValue  = Arith.div(Arith.add(totalValues1.get(timeKey), valueData), 2,2);
						totalValues1.put(timeKey, newValue);
					} else {
						Double newValue  = totalValues1.get(timeKey) + valueData;
						totalValues1.put(timeKey, newValue);
					}
				} else {
					totalValues1.put(timeKey, valueData);
				}
			}			
		}
		if (totalValues1.size() > 0) {
			totalMap.put(totalKey1, totalValues1);			
		}
	}
	
	String totalKey2 = "Total" + "["+collectTime2+"]";
	Map<String, Double> totalValues2 = new HashMap<String, Double>();
	if(map2!=null){
		for(Map.Entry<String,List<KeyValuePo>> entry : map2.entrySet()) {
				
			for(KeyValuePo keyValuePo : entry.getValue()) {
					
				Double valueData = 	Double.parseDouble(keyValuePo.getValueStr());
				String timeKey = keyValuePo.getCollectTimeStr();
					
				if (totalValues2.keySet().contains(timeKey)) {
					boolean isAvg = keyName.indexOf(Constants.AVERAGE_USERTIMES_FLAG) > -1;
					if (isAvg) {
						Double newValue  = Arith.div(Arith.add(totalValues2.get(timeKey), valueData), 2,2);
						totalValues2.put(timeKey, newValue);
					} else {
						Double newValue  = totalValues2.get(timeKey) + valueData;
						totalValues2.put(timeKey, newValue);
					}
				} else {
					totalValues2.put(timeKey, valueData);
				}
			}			
		}
		if (totalValues2.size() > 0) {
			totalMap.put(totalKey2, totalValues2);
		}
	}
	
	resultTotal = AmLineFlash.createCommonCharXml1(totalMap);
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" ><%=totalFlag %></font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<div id="chartdiv_total" align="center">


</div>
</div>
</div>

<table>
	<tr>
		<td>
		<%
		if(sumMap1.size() != 0) {
		%>
		<table border="1" align="left">
			<tr>
				<td align="center" colspan="2" width="400" >日期1: <%=collectTime1 %></td>	
			</tr>
			<tr>		
				<td align="center" colspan="2" width="400" >KeyName: <%=keyName %></td>			
			</tr>
			<tr>
				<td align="center" width="200">机器</td>
				<td align="center" width="200"><%=matcherAvg.matches()? "平均" : "总"%>值</td>
			</tr>
			
				<%
					for(Map.Entry<String, Double> entry : sumMap1.entrySet()) {
						System.out.println(entry.getKey());
				%>
				<tr>
					<td align="center" width="200"><%=entry.getKey() %></td>
					<td align="center" width="200"><%=entry.getValue() %></td>
				</tr>
				<%
					}
				%>
		
		</table>
		<%} %>
		</td>
		<td>			
		<%
		if(sumMap2.size() != 0) {
		%>
		
		<table border="1" align="left">
			<tr>
				<td align="center" colspan="2" width="400" >日期2: <%=collectTime2 %></td>	
			</tr>
			<tr>
				<td align="center" colspan="2" width="400" >KeyName: <%=keyName %></td>			
			</tr>
			<tr>
				<td align="center" width="200">机器</td>
				<td align="center" width="200"><%=matcherAvg.matches()? "平均" : "总"%>值</td>
			</tr>
			
				<%
					for(Map.Entry<String, Double> entry : sumMap2.entrySet()) {
						System.out.println(entry.getKey());
				%>
				<tr>
					<td align="center" width="200"><%=entry.getKey() %></td>
					<td align="center" width="200"><%=entry.getValue() %></td>
				</tr>
				<%
					}
				%>
		</table>
		<%} %>
		</td>
	</tr>
</table>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>


<script type="text/javascript">
var so = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "800", "400", "8", "#FFFFFF");
so.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so.addVariable("chart_id", "amline");   
so.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
so.addVariable("chart_data", encodeURIComponent("<%=reslut%>"));
so.write("chartdiv");

var totalSo = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline", "800", "400", "8", "#FFFFFF");
totalSo.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
totalSo.addVariable("chart_id", "amline");   
totalSo.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings.xml");
totalSo.addVariable("chart_data", encodeURIComponent("<%=resultTotal%>"));
totalSo.write("chartdiv_total");

</script>

</html>