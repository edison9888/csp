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
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery-ui-1.8.custom.min.js"></script> 

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

<form action="./user_info_update1.jsp" method="post" onsubmit="getNewListOptionsValue()">
<%

List<ReportInfoPo> listReportList = MonitorTimeAo.get().findAllReport();

String action = request.getParameter("action");
if("update".equals(action)){	
	String wangwang = request.getParameter("wangwang");
	String phone =request.getParameter("phone");
	String mail =request.getParameter("mail");
	String permissionDesc =request.getParameter("permissionDesc");
	String gs = request.getParameter("newListValue_app");

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
	loginUserPo.setGroup(gs);
	if(loginUserPo.getPermissionDesc()==null){
		loginUserPo.setPermissionDesc("alarmKey:"+gs+";");
	}else{
		loginUserPo.setPermissionDesc(loginUserPo.getPermissionDesc().replaceAll("alarmKey:[\\w,]*;","alarmKey:"+gs+";"));
	}
	
	
	String report="";
	for(ReportInfoPo info:listReportList){
		int reportId = info.getId();
		if(info.getType()==0){
			String value = request.getParameter("report_"+reportId);
			if(value!=null){
				report += reportId+";";
			}
		}else{
			String appIds = request.getParameter("newListValue_" + reportId);
			if(appIds!=null && !appIds.equals("")){
				String appStr = "";
				appStr += appIds;			
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

<%
	//把已经包含在group的应用发在map中，方便生成左右选择列表的时候很清楚那些是已经选择过的
	Map<String,String> map = new HashMap<String,String>();
	String group = po.getGroup();
	if(group!=null){
		String[] updategroups = group.split(",");
		for(String g:updategroups)map.put(g,g);
	}
%>
<table width="100%" border="1" class="ui-widget ui-widget-content">
<tr>
	<td>
<div style="float:left;">
 未添加收告警应用:<br/>
<select id="old_list_app" multiple="multiple" name="old_list_app" size="10" style="width:200px;">

  	<%
	for(AppInfoPo app:listApp){
		
		if(map.get(app.getAppId()+"")==null) {
			%>
			<option value="<%=app.getAppId() %>" ><%=app.getAppName() %></option>
			<%
		}
	}
			%>	
</select>
</div>
<div style="float:left;"><br/><br/><br/><br/><br/>
    <input type="button" id="opt_add_app" value="ADD">
    <input type="button" id="opt_add_all_app" value="ADD ALL">
    <input type="button" id="opt_del_app" value="DEL">
    <input type="button" id="opt_del_all_app" value="DEL ALL">
</div>
<div style="float:left;">
 已添加收告警应用:<br/>
    <select id="new_list_app" name="new_list_app" size="10" multiple="multiple" style="width:200px;">
      	<%
	for(AppInfoPo app:listApp){
		
		if(map.get(app.getAppId()+"")!=null) {
			%>
			<option value="<%=app.getAppId() %>" ><%=app.getAppName() %></option>
			<%
		}
	}
			%>	
    
    
    </select>
    <input type="hidden" name="newListValue_app" id="newListValue_app">
</div>
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

String report = po.getReportDesc();
String groupNamesStr = "";
Map<String,String> groupMap = new HashMap<String,String>();
if(report != null){
	for(String reportInfo:report.split(";")){
		
		int index = reportInfo.indexOf(":");
		if(index > -1){
			groupMap.put(reportInfo.substring(0,index),reportInfo.substring(index+1,reportInfo.length()));
		}else{
			groupMap.put(reportInfo,reportInfo);
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
				<%=info.getName()%>:<input type="checkbox" name="report_<%=info.getId() %>"  value="<%=info.getId() %>" <%if(groupMap.get(info.getId()+"")!=null) {out.print("checked");} %>>
			</td>	
		</tr>	
		<%	
		}else{
			
			String appIds = groupMap.get(info.getId()+"");		
			Map<String,String> tmpAppMap = new HashMap<String,String>();
			if(appIds != null){
				for(String appId:appIds.split(",")){
					tmpAppMap.put(appId,appId);
				}
			}

		%>	
		<tr class="ui-widget-header ">
			<td>
				<%=info.getName()%>:
			</td>	
		</tr>	
<!-- 这里是左右选择列表框 -->

<tr>
	<td>
		<div style="float:left;">
		  未添加应用:<br/>
		<select id="old_list_<%=info.getId() %>" multiple="multiple" name="old_list_<%=info.getId() %>" size="10" style="width:200px;">
		<%
			for(AppInfoPo app:listApp){
				
				if(tmpAppMap.get(app.getAppId()+"")==null) {
					%>
					<option value="<%=app.getAppId() %>" ><%=app.getAppName() %></option>
					<%
				}
			}
		%>	
		</select>
		</div>
		<div style="float:left;"><br/><br/><br/><br/><br/>
		    <input type="button" id="opt_add_<%=info.getId() %>" value="ADD">
		    <input type="button" id="opt_add_all_<%=info.getId() %>" value="ADD ALL">
		    <input type="button" id="opt_del_<%=info.getId() %>" value="DEL">
		    <input type="button" id="opt_del_all_<%=info.getId() %>" value="DEL ALL">
		</div>
		<div style="float:left;">
		      已添加应用:<br/>
		    <select id="new_list_<%=info.getId() %>" name="new_list_<%=info.getId() %>" size="10" multiple="multiple" style="width:200px;">
		    <%
			for(AppInfoPo app:listApp){
				
				if(tmpAppMap.get(app.getAppId()+"")!=null) {
					%>
					<option value="<%=app.getAppId() %>" ><%=app.getAppName() %></option>
					<%
				}
			}
		%>	
		    </select>
		    <input type="hidden" name="newListValue_<%=info.getId() %>" id="newListValue_<%=info.getId() %>">
		</div>
	</td>
</tr>	

		<%	
		} 
		%>
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

<script type="text/javascript">

//获取左右选择列表的添加值
function getNewListOptionsValue(){

	//拼接告警应用的添加值。
	for(var i=0; i < document.getElementById("new_list_app").length; i++) {

		document.getElementById("newListValue_app").value += document.getElementById("new_list_app").options[i].value;
		if(i != (document.getElementById("new_list_app").length)) {

			document.getElementById("newListValue_app").value += ",";
		}
	}

	<%
	
	//拼接报表接收信息的添加值
	for(ReportInfoPo info:listReportList){
		
	%>

		for(var i=0; i < document.getElementById("new_list_<%=info.getId()%>").length; i++) {
	
			document.getElementById("newListValue_<%=info.getId()%>").value += document.getElementById("new_list_<%=info.getId()%>").options[i].value;
			if(i != (document.getElementById("new_list_<%=info.getId()%>").length)) {
	
				document.getElementById("newListValue_<%=info.getId()%>").value += ",";
			}
		}
	<%
	}		
	%>
	return true;
}

//这里是为左右列表选择框添加响应事件
function SelectOperation(id){

		this.target = id;
		var self = this;
		this.init=function() {
			//alert(this.target);
			document.getElementById('opt_add_'+this.target).onclick = this.add;
			document.getElementById('opt_add_all_'+this.target).onclick = this.add_all;
	        document.getElementById('opt_del_'+this.target).onclick = this.del;
	        document.getElementById('opt_del_all_'+this.target).onclick = this.del_all;
			self.old_lsit = document.getElementById('old_list_'+this.target);
	        self.new_lsit = document.getElementById('new_list_'+this.target);
		},
		
		this.add=function() {

			//alert(1);
			var tmp_id = [];
            for (var i = 0; i < self.old_lsit.length; i++) {
                if (self.old_lsit.options[i].selected) {
                    self.new_lsit.options[self.new_lsit.options.length] = new Option(self.old_lsit.options[i].text, self.old_lsit.options[i].value);
                    tmp_id.push(self.old_lsit.options[i]);
                }
            }
            for (var del = 0; del < tmp_id.length; del++) {
                for (var j = 0; j < self.old_lsit.options.length; j++) {
                    if (tmp_id[del].value == self.old_lsit.options[j].value) {
                        self.old_lsit.remove(j);
                    }
                }
            }
		},
        this.del=function() {
            var tmp_id = [];
            for (var i = 0; i < self.new_lsit.options.length; i++) {
                if (self.new_lsit.options[i].selected) {
                    self.old_lsit.options[self.old_lsit.options.length] = new Option(self.new_lsit.options[i].text, self.new_lsit.options[i].value);
                    tmp_id.push(self.new_lsit.options[i]);
                }
            }
            for (var del = 0; del < tmp_id.length; del++) {
                for (var j = 0; j < self.new_lsit.length; j++) {
                    if (tmp_id[del].value == self.new_lsit.options[j].value) {
                        self.new_lsit.remove(j);
                    }
                }
            }
        },
        this.add_all=function() {

            for (var i = 0; i < self.old_lsit.length; i++) {
                self.new_lsit.options[self.new_lsit.options.length] = new Option(self.old_lsit.options[i].text, self.old_lsit.options[i].value);
            }
            self.old_lsit.options.length = 0;
        },
        this.del_all=function() {
            for (var i = 0; i < self.new_lsit.length; i++) {
                self.old_lsit.options[self.old_lsit.options.length] = new Option(self.new_lsit.options[i].text, self.new_lsit.options[i].value);
            }
            self.new_lsit.options.length = 0;
        }
	};

	var a = new SelectOperation("app");
	a.init();
	<%
	for(ReportInfoPo info:listReportList){
		
	%>
		var <%="ReportInfo_" + info.getId()%> = new SelectOperation("<%=info.getId()%>");
		<%="ReportInfo_" + info.getId()%>.init();
	<%
	}		
	%>
</script>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>