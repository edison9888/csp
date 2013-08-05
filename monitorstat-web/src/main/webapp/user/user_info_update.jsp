<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.web.util.SessionUtil"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.vo.ReportInfoPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>核心监控-用户管理</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/js/mult/css/ui.multiselect.css" rel="stylesheet" /> 
<link type="text/css" href="<%=request.getContextPath() %>/statics/js/mult/css/common.css" rel="stylesheet" /> 
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery-ui-1.8.custom.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/mult/plugins/localisation/jquery.localisation-min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/mult/plugins/scrollTo/jquery.scrollTo-min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/mult/ui.multiselect.js"></script> 

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
img {
cursor:pointer;
}
.report_on{background:#bce774;}

</style>

<script type="text/javascript"> 
	$(function(){
		$.localise('ui-multiselect', {/*language: 'en',*/ path: '<%=request.getContextPath() %>/statics/js/mult/locale/'});
		$(".multiselect").multiselect();
		//$('#switcher').themeswitcher();
	});
</script> 

</head>
<body>
<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"updateuser","")){
	out.print("<red>你没有权限操作!");
	return;
}
%>
<jsp:include page="../head.jsp"></jsp:include>

<form action="./user_info_update.jsp" method="post">
<%
String action = request.getParameter("action");
if("update".equals(action)){	
	String wangwang = request.getParameter("wangwang");
	String phone =request.getParameter("phone");
	String mail =request.getParameter("mail");
	String permissionDesc =request.getParameter("permissionDesc");
	String[] groups =request.getParameterValues("multiselect");
	String id =request.getParameter("id");
	String phoneDesc = "";
	//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
	for(int i=1;i<=7;i++){
		String phone_week = request.getParameter("phone_week_"+i);
		String phone_num = request.getParameter("phone_num_"+i);
		if(phone_week!=null&&!"".equals(phone_week.trim())&&phone_num!=null&&!"".equals(phone_num.trim())){
			phoneDesc+=phone_num+"#"+i+"#"+phone_week+"$";
		}
	}
	String wangwangDesc = "";
	//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
	for(int i=1;i<=7;i++){
		String ww_week = request.getParameter("wangwang_week_"+i);
		String ww_num = request.getParameter("wangwang_num_"+i);
		if(ww_week!=null&&!"".equals(ww_week.trim())&&ww_num!=null&&!"".equals(ww_num.trim())){
			wangwangDesc+=ww_num+"#"+i+"#"+ww_week+"$";
		}
	}
	
	
	LoginUserPo loginUserPo = MonitorUserAo.get().getLoginUserPo(Integer.parseInt(id));
	
	loginUserPo.setPhone(phone);
	loginUserPo.setWangwang(wangwang);
	loginUserPo.setSendPhoneFeature(phoneDesc);
	loginUserPo.setSendWwFeature(wangwangDesc);
	if(permissionDesc != null) {
		loginUserPo.setPermissionDesc(permissionDesc);
	}
	loginUserPo.setMail(mail);
	String gs = "";
	if(groups!=null){
		for(String g:groups){
			gs+=g+",";
		}
	}
	loginUserPo.setGroup(gs);
	if(loginUserPo.getPermissionDesc()==null){
		loginUserPo.setPermissionDesc("alarmKey:"+gs+";");
	}else{
		loginUserPo.setPermissionDesc(loginUserPo.getPermissionDesc().replaceAll("alarmKey:[\\w,]*;","alarmKey:"+gs+";"));
	}
	
	
	List<ReportInfoPo> listReportList = MonitorTimeAo.get().findAllReport();
	String report="";
	for(ReportInfoPo info:listReportList){
		int reportId = info.getId();
		if(info.getType()==0){
			String value = request.getParameter("report_"+reportId);
			if(value!=null){
				report += reportId+";";
			}
		}else{
			String[] appIds = request.getParameterValues("report_"+reportId);
			
			if(appIds!=null&&appIds.length>0){
				String appStr = "";
				for(String appid:appIds){
					appStr += appid+",";
				}				
				report +=reportId+":"+appStr+";";
			}
		}
	}
	
	loginUserPo.setReportDesc(report);
	
	
	boolean b = MonitorUserAo.get().updateLoginUserPo(loginUserPo);
	
	
	%>
	<font size="+3" color="red"><%if(b){out.print("成功");}else{out.print("失败!出现异常");} %></font>	
		<a href="./manage_user.jsp">返回</a>
	<%
}else{

String id =request.getParameter("id");
LoginUserPo po = MonitorUserAo.get().getLoginUserPo(Integer.parseInt(id));
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();
%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">用户基本信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td>用户名:<%=po.getName() %></td>
		<td>旺旺号:<input type="text" name="wangwang" value="<%=po.getWangwang() %>"></td>
		<td>手机号:<input type="text" name="phone" value="<%=po.getPhone() %>"></td>
		<td>邮箱:<input type="text" name="mail" value="<%=po.getMail() %>"></td>
		</tr>
		<tr >
	
		<%
		LoginUserPo loginUserPo = SessionUtil.getUserSession(request);
		if(loginUserPo.getWangwang().equals("小赌") || loginUserPo.getWangwang().equals("xiaodu")) {
			%>
				<td colspan="4">权限修改:<input type="text" name="permissionDesc" size="133" value="<%=po.getPermissionDesc() %>"></td>
			<%
		}
		%>
	
	</tr>
</table>
</div>
</div>
<%
String appNamesStr = "";
String apps = po.getGroup();
for(String appId:apps.split(",")){
	try{
		appNamesStr+=AppCache.get().getKey(Integer.parseInt(appId)).getAppName()+",";
	}catch(Exception e){}
}

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">告警接收信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<table width="100%" border="1" class="ui-widget ui-widget-content">
<tr>
	<td>接收告警应用:	
	</td>
</tr>
<tr>
	<td>
	<%
	Map<String,String> map = new HashMap<String,String>();
	String group = po.getGroup();
	if(group!=null){
		String[] updategroups = group.split(",");
		for(String g:updategroups)map.put(g,g);
	}
%>
	
	<select id="multiselect" class="multiselect" multiple="multiple" name="multiselect">

  	<%
	for(AppInfoPo app:listApp){
	%> 	
		<option value="<%=app.getAppId() %>" <%if(map.get(app.getAppId()+"")!=null) {out.print("selected");} %>><%=app.getAppName() %></option>
	<%} %>
	</select>
	</td>
</tr>
</table>

<table width="100%" border="1" class="ui-widget ui-widget-content">
<tr>
	<td>
		<table>
			<tr >
				<td>手机接收设置:</td>
			</tr>
			<%
			//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
			String phoneFeature = po.getSendPhoneFeature();
			String[] week = new String[7];
			String[] num = new String[7];			
			if(phoneFeature!=null){
				String[] featrues = phoneFeature.split("\\$");
				for(String featrue:featrues){
					String[]  _tmp= featrue.split("\\#");
					if(_tmp.length==4){						
						int w = Integer.parseInt(_tmp[1]);
						String time1 = _tmp[2];
						String time2 = _tmp[3];
						week[w-1] = time1+"#"+time2;
						num[w-1] = _tmp[0];
					}
				}
			}
			
			%>
			<tr>
				<td>周一:<input type="text" name="phone_week_2"  value="<%=week[1]==null?"":week[1] %>"> &nbsp;&nbsp;告警<input type="text" name="phone_num_2"  value="<%=num[1]==null?"":num[1] %>" size="2">以上发送</td>
				<td>周二:<input type="text" name="phone_week_3"  value="<%=week[2]==null?"":week[2] %>"> &nbsp;&nbsp;告警<input type="text" name="phone_num_3"  value="<%=num[2]==null?"":num[2] %>" size="2">以上发送</td>
				<td>周三:<input type="text" name="phone_week_4"  value="<%=week[3]==null?"":week[3] %>"> &nbsp;&nbsp;告警<input type="text" name="phone_num_4"  value="<%=num[3]==null?"":num[3] %>" size="2">以上发送</td>
				<td>周四:<input type="text" name="phone_week_5"  value="<%=week[4]==null?"":week[4] %>"> &nbsp;&nbsp;告警<input type="text" name="phone_num_5"  value="<%=num[4]==null?"":num[4] %>" size="2">以上发送</td>
				<td>周五:<input type="text" name="phone_week_6"  value="<%=week[5]==null?"":week[5] %>"> &nbsp;&nbsp;告警<input type="text" name="phone_num_6"  value="<%=num[5]==null?"":num[5] %>" size="2">以上发送</td>
				<td>周六:<input type="text" name="phone_week_7"  value="<%=week[6]==null?"":week[6] %>"> &nbsp;&nbsp;告警<input type="text" name="phone_num_7"  value="<%=num[6]==null?"":num[6] %>" size="2">以上发送</td>
				<td>周日:<input type="text" name="phone_week_1"  value="<%=week[0]==null?"":week[0] %>"> &nbsp;&nbsp;告警<input type="text" name="phone_num_1"  value="<%=num[0]==null?"":num[0] %>" size="2">以上发送</td>
			</tr>			
		</table>
	</td>
</tr>
<%
//次数#星期#开始时间#结束时间$次数#星期#开始时间#结束时间
String wangwangFeature = po.getSendWwFeature();
week = new String[7];
num = new String[7];			
if(wangwangFeature!=null){
	String[] featrues = wangwangFeature.split("\\$");
	for(String featrue:featrues){
		String[]  _tmp= featrue.split("\\#");
		if(_tmp.length==4){						
			int w = Integer.parseInt(_tmp[1]);
			String time1 = _tmp[2];
			String time2 = _tmp[3];
			week[w-1] = time1+"#"+time2;
			num[w-1] = _tmp[0];
		}
	}
}

%>

<tr>
	<td>
		<table>
			<tr >
				<td>旺旺接收设置:</td>
			</tr>
			<tr>
				<td>周一:<input type="text" name="wangwang_week_2"  value="<%=week[1]==null?"":week[1] %>"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_2"  value="<%=num[1]==null?"":num[1] %>" size="2">以上发送</td>
				<td>周二:<input type="text" name="wangwang_week_3"  value="<%=week[2]==null?"":week[2] %>"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_3"  value="<%=num[2]==null?"":num[2] %>" size="2">以上发送</td>
				<td>周三:<input type="text" name="wangwang_week_4"  value="<%=week[3]==null?"":week[3] %>"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_4"  value="<%=num[3]==null?"":num[3] %>" size="2">以上发送</td>
				<td>周四:<input type="text" name="wangwang_week_5"  value="<%=week[4]==null?"":week[4] %>"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_5"  value="<%=num[4]==null?"":num[4] %>" size="2">以上发送</td>
				<td>周五:<input type="text" name="wangwang_week_6"  value="<%=week[5]==null?"":week[5] %>"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_6"  value="<%=num[5]==null?"":num[5] %>" size="2">以上发送</td>
				<td>周六:<input type="text" name="wangwang_week_7"  value="<%=week[6]==null?"":week[6] %>"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_7"  value="<%=num[6]==null?"":num[6] %>" size="2">以上发送</td>
				<td>周日:<input type="text" name="wangwang_week_1"  value="<%=week[0]==null?"":week[0] %>"> &nbsp;&nbsp;告警<input type="text" name="wangwang_num_1"  value="<%=num[0]==null?"":num[0] %>" size="2">以上发送</td>
		</tr>			
		</table>
	</td>
</tr>
</table>
</div>
</div>


<%


List<ReportInfoPo> listReportList = MonitorTimeAo.get().findAllReport();


String report = po.getReportDesc();
String groupNamesStr = "";
Map<String,String> greoupMap = new HashMap<String,String>();
if(report != null){
	for(String reportInfo:report.split(";")){
		
		int index = reportInfo.indexOf(":");
		if(index > -1){
			greoupMap.put(reportInfo.substring(0,index),reportInfo.substring(index+1,reportInfo.length()));
		}else{
			greoupMap.put(reportInfo,reportInfo);
		}		
	}
}
%>

<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">报表接收信息</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
	<%
	for(ReportInfoPo info:listReportList){
		
	%>

<table width="100%" border="1" class="ui-widget ui-widget-content">
	
		<%if(info.getType()==0){
		%>
		<tr class="ui-widget-header ">
			<td>
				<%=info.getName()%>:<input type="checkbox" name="report_<%=info.getId() %>"  value="<%=info.getId() %>" <%if(greoupMap.get(info.getId()+"")!=null) {out.print("checked");} %>>
			</td>	
		</tr>	
		<%	
		}else{
		%>
		<tr class="ui-widget-header ">
			<td>
				<%=info.getName()%>:
			</td>	
		</tr>
		<tr>
			<td>	
		<%	
			String appNames = greoupMap.get(info.getId()+"");		
			Map<String,String> tmpAppMap = new HashMap<String,String>();
			if(appNames != null){
				for(String appName:appNames.split(",")){
					tmpAppMap.put(appName,appName);
				}
			}

		%>
			<select id="report_<%=info.getId() %>" class="multiselect" multiple="multiple" name="report_<%=info.getId() %>">
		
		  	<%
			for(AppInfoPo app:listApp){
			%> 	
				<option value="<%=app.getAppId() %>" <%if(tmpAppMap.get(app.getAppId()+"")!=null) {out.print("selected");} %>><%=app.getAppName() %></option>
			<%} %>
			</select> 	
		<%} %>
		</td>
		</tr>	
</table>
<%} %>
</div>
</div>
<table width="100%">
	<tr>
		<td align="center"><input type="hidden" value="<%=id %>" name="id"><input type="hidden" value="update" name="action"><input type="submit" value="提交更新"><input type="button" value="关闭" onclick="window.close()"></td>
	</tr>
</table>
</form>
<%} %>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>