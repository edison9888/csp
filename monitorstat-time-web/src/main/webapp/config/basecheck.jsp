<%@page import="org.apache.commons.math.stat.descriptive.DescriptiveStatistics"%>
<%@page import="java.util.Collections"%>
<%@page import="com.taobao.csp.time.util.AmlineFlash"%>
<%@page import="java.io.IOException"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.taobao.csp.time.util.DataSmoothing"%>
<%@page import="com.taobao.csp.dataserver.PropConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.time.util.DataUtil"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.csp.dataserver.query.QueryHistoryUtil"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Calendar"%>
<%@page import="com.taobao.csp.time.cache.KeyCache"%>
<%@page import="com.taobao.csp.time.cache.BaseLineCache"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.taobao.monitor.common.util.CspCacheTBHostInfos"%>
<%@page import="com.taobao.monitor.common.po.HostPo"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map"%>
<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
<link type="text/css" rel="stylesheet"
	href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>
<script src="<%=request.getContextPath()%>/statics/js/swfobject.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/statics/js/time_commons.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/css/jquery.fancybox.css" media="screen" />
<script language="javascript" type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/My97DatePicker/WdatePicker.js"></script>
<link type="text/css" rel="stylesheet"	href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<script type="text/javascript"	src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.fancybox.js"></script>
<title>Insert title here</title>
</head>
<body>
<%!

public class SameTime1 {
	
	private String time;
	
	private List<Double> values = new ArrayList<Double>();
	
	public SameTime1(String time){
		this.time = time;
	}
	
	public void addValue(Double v){
		values.add(v);
	}
	
	public double getMean(){
		DescriptiveStatistics ds = new DescriptiveStatistics();
		Collections.sort(values);
		if(values.size()>10){
			for(int i=1;i<values.size()-1;i++){ //去掉最大与最小的值
				ds.addValue(values.get(i));
			}
		}else{
			for(int i=0;i<values.size();i++){ 
				ds.addValue(values.get(i));
			}
		}
		return ds.getMean();
		
	}

	public String getTime() {
		return time;
	}
	

	
	
	

}

protected Map<String,Double> getMean(Map<String,SameTime1> mapTime){
	
	Map<String,Double> temp = new HashMap<String, Double>();
	
	for(Map.Entry<String,SameTime1> entry: mapTime.entrySet()){
		
		SameTime1 st = entry.getValue();
		
		temp.put(entry.getKey(), entry.getValue().getMean());
	}
	return temp;
}
%>
<%

String appName = request.getParameter("appName");
String keyName = request.getParameter("keyName");
String propertyName = request.getParameter("propertyName");

SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");

SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

List<String> dayTimeList = new ArrayList<String>();
Calendar cal = Calendar.getInstance();
cal.set(Calendar.HOUR_OF_DAY, 0);
cal.set(Calendar.MINUTE, 0);
int day = cal.get(Calendar.DAY_OF_MONTH);

while(day ==cal.get(Calendar.DAY_OF_MONTH) ){
	dayTimeList.add(sdf.format(cal.getTime()));
	cal.add(Calendar.MINUTE, 1);
}


AmlineFlash f = new AmlineFlash();

Map<String,SameTime1> mapTime = new HashMap<String, SameTime1>();

cal.setTime(new Date());

for(int i=1;i<=6;i++){
	cal.add(Calendar.DAY_OF_MONTH, -7);
	Map<Date,String> dateMap = QueryHistoryUtil.querySingle(appName,keyName, propertyName, cal.getTime());
	if(dateMap==null||dateMap.size()==0){
		return;
	}
	out.print(sdf1.format(cal.getTime())+" "+dateMap.size());
	out.print("<br/>");
	
	List<Date> listdate = new ArrayList<Date>();
	listdate.addAll(dateMap.keySet());
	Collections.sort(listdate);
	for(Date t:listdate){
		String v = dateMap.get(t);
		out.print(""+sdf.format(t)+"="+DataUtil.transformDouble(v));
		
		try{
			SameTime1 st = mapTime.get(sdf.format(t));
			
			if(st == null){
				st =new SameTime1(sdf.format(t));
				mapTime.put(sdf.format(t), st);
			}
			st.addValue(DataUtil.transformDouble(v));
		}catch (Exception e) {
			e.printStackTrace();
			out.print(e.getMessage());
		}
		
		f.addValue("ytmp1"+sdf1.format(t), t.getTime(), DataUtil.transformDouble(v));
		
	}
	

	out.print("<br/>");
	
	
}

StringBuilder sb = new StringBuilder();

Map<String,Double> map = getMean(mapTime);

List<String> timelist = new ArrayList<String>();
timelist.addAll(map.keySet());
Collections.sort(timelist);

out.print(" 平均值"+map.size());
out.print("<br/>");

for(String gg:timelist){
	out.print(""+gg+"="+map.get(gg));
}
out.print("<br/>");

double[] yvar = new double[dayTimeList.size()];
double[] xvar = new double[dayTimeList.size()];

for(int i=0;i<dayTimeList.size();i++){
	String time = dayTimeList.get(i);
	xvar[i] = i;
	Double v = map.get(time);
	if(v ==null){
		if(i!=0){
			String pre = dayTimeList.get(i-1);
			Double t = map.get(pre);
			if(t!= null){
				map.put(time, t);
				yvar[i] = t;
			}else{
				map.put(time, 0d);
				yvar[i] = 0;
			}
		}else{
			map.put(time, 0d);
			yvar[i] = 0;
		}
	}else{
		yvar[i] = v.doubleValue();
	}
}

Map<String,Double> map1 = new HashMap<String,Double>();
Map<String,Double> map2 = new HashMap<String,Double>();
try {
	double[] ytmp1 = null;
	double[] ytmp2 = null;
	
	
	
	ytmp1 = DataSmoothing.apacheLoess(xvar, yvar);
	ytmp2= DataSmoothing.cspSmooth(xvar, yvar);
	//ytmp2=DataSmoothing.apacheLoess(xvar, ytmp2);
	
	for(int i=0;i<dayTimeList.size();i++){
		map1.put(dayTimeList.get(i), ytmp1[i]);
		map2.put(dayTimeList.get(i), ytmp2[i]);
	}
	
} catch (Exception e1) {
	e1.printStackTrace();
	out.print(e1.getMessage());
}
DecimalFormat df1 = new DecimalFormat(".##");



String t = sdf1.format(new Date());

for(Map.Entry<String,Double> entry: map1.entrySet()){
	f.addValue("ytmp1", sdf2.parse(t+" "+entry.getKey()).getTime(), entry.getValue());
}
for(Map.Entry<String,Double> entry: map2.entrySet()){
	f.addValue("ytmp2", sdf2.parse(t+" "+entry.getKey()).getTime(), entry.getValue());
}

try {
	String str = sb.toString();
	
	
	out.print(sdf1.format(cal.getTime()));
	out.print(str);
	out.print("<br/>");
	
} catch (IOException e) {
	e.printStackTrace();
	out.print(e.getMessage());
}


%>

<table width="100%">
<caption>全网基线</caption>
					<tr>
						<td width="100%" id="pvchartDivId" style="height: 100px"
			colspan="2">
			<script type="text/javascript">
			var so4 = new SWFObject("<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline.swf", "amline_pv", "100%", "350", "6", "#FFFFFF");
			so4.addVariable("path", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline");
			so4.addVariable("chart_id", "amline4");  
			so4.addVariable("wmode", "transparent");
			so4.addVariable("settings_file", "<%=request.getContextPath()%>/statics/js/amcharts/old/amline/amline_settings1.xml");
			so4.addVariable("chart_data", encodeURIComponent("<%=f.getAmline()%>"));
			so4.write("pvchartDivId");
			</script>
		</td>
	</tr>
</table>	
</body>
</html>