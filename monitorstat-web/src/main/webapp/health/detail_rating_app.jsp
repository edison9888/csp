<%@ page language="java" contentType="text/html; charset=GBK"
	pageEncoding="GBK"%>
<%@ page import="com.taobao.monitor.web.ao.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.taobao.monitor.web.vo.*"%>
<%@ page import="com.taobao.monitor.web.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.web.rating.RatingApp"%>
<%@page import="com.taobao.monitor.web.rating.RatingIndicatorValue"%>
<%@page import="com.taobao.monitor.common.util.Utlitites"%>

<%@page import="com.taobao.monitor.web.rating.RatingCompute"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>��ϸ����</title>
<style>
.report_on{background:#bce774;}
</style>
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath()%>/statics/js/swfobject.js"></script>
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
	background: url(<%=request.getContextPath()%>/statics/images/4_17.gif);
}
img {
cursor:pointer;
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>
<script type="text/javascript">
function deleteapp(appid){
	if(window.confirm('ȷ��ɾ��������Ӧ��?')){
		location.href="manage_rating_app.jsp?action=delete&id="+appid;
	}
}


var appRatingDetail = {};


</script>
</head>
<body>
<jsp:include page="../left.jsp"></jsp:include>
<%
	String action = request.getParameter("action");

	if ("delete".equals(action)) {
		MonitorRatingAo.get().deleteRatingApp(
				Integer.parseInt(request.getParameter("id")));
	}

	List<AppInfoPo> appList = MonitorCommonAo.get().findAllApp();
	Map<Integer, AppInfoPo> map = new HashMap<Integer, AppInfoPo>();
	for (AppInfoPo po : appList) {
		map.put(po.getAppId(), po);
	}

	List<RatingApp> listRatingAppList = MonitorRatingAo.get()
			.findAllRatingApp();
%>
<input type="button" value="���������Ӧ��" onclick="location.href='./add_rating_app.jsp'">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix"><font color="#FF0000" >����Ӧ���б�</font>&nbsp;&nbsp; </div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content"> 
  <tr class="headcon ">
    <td width="20%" align="center">Ӧ����</td>
	 <td width="20%" align="center">�������</td>
	 <td width="60%" align="center">����</td>
  </tr>
  <%
  	for (RatingApp app : listRatingAppList) {
  		AppInfoPo po = map.get(app.getAppId());
  		double d = 0;
  		List<RatingIndicatorValue> list = MonitorRatingAo.get().getRecentHealthIndexByAppId(app.getAppId());
  		for(RatingIndicatorValue riv : list){
  			d += RatingCompute.compute(riv);
  		}
  %>
  <tr style="display:none">
  	<%
  	for(RatingIndicatorValue riv : list){
  	%>
  	<td id="<%=app.getAppName() %>_<%=riv.getIndicatorKey()%>"><%=RatingCompute.compute(riv) %></td>
  	<%} %>
  </tr>
   <tr >
    <td width="20%" align="center"><%=po.getAppName()%></td>
	 <td width="20%" align="center"><%=d %></td>
	 <td width="60%" align="center">
	 <a href="health_idx_history.jsp?appId=<%=po.getAppId()%>">�鿴��ʷ����</a>&nbsp;&nbsp;
	 <a href="health_idx_detail.jsp?appId=<%=po.getAppId()%>">�鿴��������</a>&nbsp;&nbsp;
	 <a onclick="deleteapp('<%=po.getAppId()%>')"href="#">ɾ��</a>&nbsp;&nbsp;
	 <a href="./update_rating_app.jsp?id=<%=po.getAppId()%>">�޸�</a>
	 </td>
  </tr>
  <%
  	}
  %>
</table>
</div>
</div>

</body>

</html>