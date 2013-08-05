<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>

<%@page import="com.taobao.monitor.common.util.Utlitites"%>
<%@page import="com.taobao.monitor.web.core.po.JprofHost"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicator"%>
<%@page import="com.taobao.monitor.web.rating.IndicatorEnum"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>���</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
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
	font-family: "����";
	font-size: 12px;
	font-weight: bold;
	text-indent: 3px;
	border-color: #316cb2;
	/*text-transform: uppercase;*/
	background: url(<%=request.getContextPath () %>/statics/images/4_17.gif);
}
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>
<script type="text/javascript">

	function checkSubmit(){
		var obj = document.getElementById("appSelectId");
		var v = obj.options[obj.selectedIndex].text;

		var arr = $(".quanzhong");
		for(var j = 0; j < arr.length; j++) {
	
			if(arr.get(j).value == "") {
	
				arr.get(j).focus();
				alert('Ȩ�ز�����Ϊ�գ�����������');  
				return false;
			}
		}
		
		if(window.confirm("��ȷ�����Ӧ��: "+v+" ��?")){
			return true;
		}
		
		return false;
	}

</script>
</head>
<body>
		<jsp:include page="../head.jsp"></jsp:include>	
		<jsp:include page="../left.jsp"></jsp:include>
<%

String action = request.getParameter("action");


if("add".equals(action)){
	RatingIndicator indicator = new RatingIndicator();
	String appNameSelect = request.getParameter("appNameSelect");
	
	int rushHourStart = 0;
	int rushHourEnd = 0;
	SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
	try {
		

		String rushHourStart1 = request.getParameter("rushHour_start");
		String rushHourEnd1 = request.getParameter("rushHour_end");
		if(rushHourStart1.length() != 4 || rushHourEnd1.length() != 4) {
			
			rushHourStart = 2030;
			rushHourEnd = 2230;
		} else {
			rushHourStart = Integer.parseInt(sdf.format(sdf.parse(rushHourStart1)));	//���rushHour_start
			rushHourEnd = Integer.parseInt(sdf.format(sdf.parse(rushHourEnd1)));		//���rushHour_end
		}
	} catch (Exception e) {
		
		//�����дʱ��������Ĭ��ֵ
		rushHourStart = 2030;
		rushHourEnd = 2230;
	}
	
	indicator.setAppId(Integer.parseInt(appNameSelect));
	for(IndicatorEnum i:IndicatorEnum.values()){
		String qps_quanzhong = request.getParameter(i.getName()+"_quanzhong");
		String qps_qujian = request.getParameter(i.getName()+"_qujian");
		//indicator.setIndicatorKey(i);
		indicator.setIndicatorWeight(Integer.parseInt(qps_quanzhong));
		indicator.setIndicatorThresholdValue(qps_qujian);
		
		indicator.setRushHour_start(rushHourStart);		//���ü�ؿ�ʼʱ��
		indicator.setRushHour_end(rushHourEnd);			//���ü�ؽ���ʱ��
		
		MonitorRatingAo.get().addRatingIndicator(indicator);
	}
	out.print("��ӳɹ�!");
	out.print("<input type=\"button\" value=\"����Ӧ�����ֹ���\" onclick=\"location.href='./manage_rating_app.jsp'\">");
}else{


List<AppInfoPo> appList = AppInfoAo.get().findAllEffectiveAppInfo();

%>
<form action="./add_rating_app.jsp" method="post" onsubmit="return checkSubmit()">
<input type="button" value="����Ӧ�����ֹ���" onclick="location.href='./manage_rating_app.jsp'">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >���������Ӧ������</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content">
 <tr >
    <td  colspan="3">Ӧ����:<select name="appNameSelect" id="appSelectId">
					<%for(AppInfoPo appInfo:appList){ %>
					<option value="<%=appInfo.getAppId() %>"><%=appInfo.getAppName() %></option>
					<%} %>
				</select></td>	
  </tr>
  
  
  
  <tr>
  <td>����ʱ�䷶Χ���ã�</td>
  	<td>��ʼ: <input type="text" name="rushHour_start" value="2030" size="10"></td>
  	<td>����: <input type="text" name="rushHour_end" value="2230" size="10">
  		{ʱ���ʽ��HHMM,����2030��ʾ20:30,�����д������Ĭ������Ϊ2030��2230}
  	</td>
  </tr>
  
  
  <tr class="headcon ">
    <td width="13%" align="center">ָ������</td>
	 <td width="13%" align="center">Ȩ��</td>
	 <td width="74%" align="center">��������</td>
  </tr>
  <%
  for(IndicatorEnum i:IndicatorEnum.values()){  
  %>
  <tr>
    <td align="center"><%=i.getName().toUpperCase() %></td>
	<td align="center"><input type="text" name="<%=i.getName()%>_quanzhong" class = "quanzhong" value="" size="10"/>
	  % </td>
	<td align="center"><input type="text" name="<%=i.getName()%>_qujian" value="{(0,0):0.5}{(0,0):0.5}{(0,0):0.5}" size="80"/> </td>
  </tr>
<%} %>
 <tr>
    <td align="center" colspan="3"><input type="hidden" value="add" name="action"/><input type="submit" value="�ύ������Ӧ��" /></td>
  </tr>
</table>

˵��:�����������ã�����{}{}{}��ʾ3��״̬���䣬��˳��Ϊ�������ǽ�����Σ�գ�{10&lt;value&lt;60:0.5}  ,��{}�ڵ�value��ʾ��ֵ�����r��ֵ

</div>
</div>
</form>
<%} %>
</body>

</html>