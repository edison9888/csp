<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="com.taobao.monitor.web.ao.MonitorUserAo"%>
<%@page import="com.taobao.monitor.web.vo.LoginUserPo"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="com.taobao.monitor.web.cache.AppCache"%>
<%@page import="com.taobao.monitor.web.vo.ReportInfoPo"%>
<%@page import="com.taobao.monitor.web.ao.MonitorTimeAo"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-�û�����</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
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
}
.report_on{background:#bce774;}

</style>
</head>
<body>
<%

%>


<jsp:include page="../head.jsp"></jsp:include>
<%
String id =request.getParameter("id");
LoginUserPo po = MonitorUserAo.get().getLoginUserPo(Integer.parseInt(id));
List<AppInfoPo> listApp = AppInfoAo.get().findAllEffectiveAppInfo();

%>
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�û�������Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">

<table width="100%" border="1" class="ui-widget ui-widget-content">
	<tr>
		<td>�û���:<%=po.getName() %></td>
		<td>������:<%=po.getWangwang() %></td>
		<td>�ֻ���:<%=po.getPhone() %></td>
		<td>����:<%=po.getMail() %></td>
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
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">�澯������Ϣ</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
	//���Ѿ�������group��Ӧ�÷���map�У�������������ѡ���б��ʱ��������Щ���Ѿ�ѡ�����
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
    ���ո澯Ӧ��:<br/>
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
			<tr>
				<td>�ֻ���������:</td>
			</tr>
			<%
			//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
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
				<td>��һ:����ʱ��:<%=week[1]==null?"":week[1] %> &nbsp;&nbsp;�澯<%=num[1]==null?"":num[1] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>�ܶ�:����ʱ��:<%=week[2]==null?"":week[2] %> &nbsp;&nbsp;�澯<%=num[2]==null?"":num[2] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[3]==null?"":week[3] %> &nbsp;&nbsp;�澯<%=num[3]==null?"":num[3] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[4]==null?"":week[4] %> &nbsp;&nbsp;�澯<%=num[4]==null?"":num[4] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[5]==null?"":week[5] %> &nbsp;&nbsp;�澯<%=num[5]==null?"":num[5] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[6]==null?"":week[6] %> &nbsp;&nbsp;�澯<%=num[6]==null?"":num[6] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[0]==null?"":week[0] %> &nbsp;&nbsp;�澯<%=num[0]==null?"":num[0] %>���Ϸ���</td>
			</tr>						
		</table>
	</td>
	
	<%
//����#����#��ʼʱ��#����ʱ��$����#����#��ʼʱ��#����ʱ��
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
	
	<td>
		<table>
			<tr>
				<td>������������:</td>
			</tr>
			<tr>
				<td>��һ:����ʱ��:<%=week[1]==null?"":week[1] %> &nbsp;&nbsp;�澯<%=num[1]==null?"":num[1] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>�ܶ�:����ʱ��:<%=week[2]==null?"":week[2] %> &nbsp;&nbsp;�澯<%=num[2]==null?"":num[2] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[3]==null?"":week[3] %> &nbsp;&nbsp;�澯<%=num[3]==null?"":num[3] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[4]==null?"":week[4] %> &nbsp;&nbsp;�澯<%=num[4]==null?"":num[4] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[5]==null?"":week[5] %> &nbsp;&nbsp;�澯<%=num[5]==null?"":num[5] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[6]==null?"":week[6] %> &nbsp;&nbsp;�澯<%=num[6]==null?"":num[6] %>���Ϸ���</td>
			</tr>
			<tr>
				<td>����:����ʱ��:<%=week[0]==null?"":week[0] %> &nbsp;&nbsp;�澯<%=num[0]==null?"":num[0] %>���Ϸ���</td>
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
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">���������Ϣ</div>
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
<!-- ����������ѡ���б�� -->

<tr>
	<td>
		<div style="float:left;">
		      �����Ӧ��:<br/>
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
<table width="100%">
	<tr>
		<td align="center"><input type="button" value="����" onclick="location.href='./manage_user.jsp'"><input type="button" value="�ر�" onclick="window.close()"></td>
	</tr>
</table>
</div>
</div>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>