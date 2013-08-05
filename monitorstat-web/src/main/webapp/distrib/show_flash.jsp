<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<title>Insert title here</title>
</head>
<body>
<div >11111111111111111111111</div>
<%
String result = request.getParameter("result");
StringBuilder sb = new StringBuilder();
sb.append("<pie>");
for(String data:result.split(";")){
	String[] _tmp = data.split(":");
	if(_tmp.length == 2)
	sb.append("<slice title='"+_tmp[0]+"'>"+_tmp[1]+"</slice>");			
}
sb.append("</pie>");
%>
<div id="chartdiv2" align="center"></div>
<script type="text/javascript">
var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/ampie.swf", "ampie", "700", "380", "8", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/ampie_settings.xml");
so1.addVariable("chart_data", encodeURIComponent("<%=result%>"));
so1.write("chartdiv2");		

</script>
</body>
</html>