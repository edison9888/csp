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
<%@page import="com.taobao.monitor.web.cache.KeyCache"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.po.KeyValuePo"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>监控</title>
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
border:0;
}
.body_summary{margin:5px 0;padding:2px;border-top:2px solid #ccc;border-bottom:1px solid #ccc;}
</style>


<script type="text/javascript">

</script>
</head>
<body>

<%

String appId = request.getParameter("appId");

if(appId == null){
	String opsName = request.getParameter("opsName");
	AppInfoPo appInfopo =AppInfoAo.get().getAppInfoByOpsName(opsName);
	appId = appInfopo.getAppId()+"";
}


String mBeanType = request.getParameter("mBeanType");;//SELF_Thread_ SELF_DataSource_
String mbeanName = request.getParameter("mbeanName");

List<KeyPo> appKeyList = KeyCache.get().getAppKey(Integer.parseInt(appId));

Map<Integer,String> currentDsKeyIdMap = new HashMap<Integer,String>();

//获取所有数据源名称
Set<String> mbeanNameSet = new HashSet<String>();
for(KeyPo po:appKeyList){	
	if(po.getKeyName().indexOf(mBeanType)>-1){
		String keyName = po.getKeyName();
		String[] keys = keyName.split("_");//SELF_Thread_SocketConnectorIoProcessor_RUNNABLE_COUNTTIMES
		mbeanNameSet.add(keys[2]);
		if(mbeanName==null){
			mbeanName = keys[2];
		}
		
		if(mbeanName.equals(keys[2])&&keys[4].equals(Constants.AVERAGE_USERTIMES_FLAG)){
			currentDsKeyIdMap.put(po.getKeyId(),keys[3]);
		}
		
	}
}

Map<String, List<KeyValuePo>> mapKeyValue = new HashMap<String, List<KeyValuePo>>();

for(Map.Entry<Integer,String> entry:currentDsKeyIdMap.entrySet()){
	Map<String, KeyValuePo> mapCurrent = MonitorTimeAo.get().findKeyValueByDate(Integer.parseInt(appId),entry.getKey(),new Date());
	List<KeyValuePo> list = new ArrayList<KeyValuePo>();
	list.addAll(mapCurrent.values());
	mapKeyValue.put(entry.getValue(),list);	
}
String result = AmLineFlash.createCharDateXml(mapKeyValue);
%>
<form action="show_mbean_common.jsp" method="get">
<table width="100%" border="1">
	<tr>
		<td width="100%" >
		<input type="hidden" value="<%=appId %>" name="appId">
		<input type="hidden" value="<%=mBeanType %>" name="mBeanType">
			<select name="mbeanName">
			<%for(String name:mbeanNameSet){ %>
				<option value="<%=name %>" <%if(name.equals(mbeanName)){out.print("selected");}%>><%=name %></option>
			<%} %>
			</select> <input type="submit" value="查看">
		</td>
	</tr>
	<tr>
		<td width="100%" id="pvchartdiv">
			
		</td>
	</tr>	
</table>
</form>
<script type="text/javascript">
var so1 = new SWFObject("<%=request.getContextPath() %>/statics/flash/amline/amline.swf", "amline_pv", "100%", "250", "6", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/flash/amline/");
so1.addVariable("chart_id", "amline1");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/flash/amline/amline_settings1.xml");
so1.addVariable("chart_data", encodeURIComponent("<%=result%>"));
so1.write("pvchartdiv");
</script>
</body>

</html>