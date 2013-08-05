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
<%@page import="com.taobao.monitor.web.core.po.JprofHost"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<META HTTP-EQUIV="pragma" CONTENT="no-cache">
<META HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate">
<title>���</title>
<style>
.report_on{background:#bce774;}
</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.core.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.tabs.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.draggable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.resizable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/ui.dialog.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery.bgiframe.js"></script>
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/swfobject.js"></script>
<script type="text/javascript">
function deletehost(hostid){
			if(window.confirm('ȷ��ɾ��?')){
				location.href="manage_jprof_host.jsp?action=delete&id="+hostid;
			}
		}


$.ui.dialog.defaults.bgiframe = true;
$(function() {
	$("#upload_jprof_panel").dialog({ autoOpen: false ,width:800,modal:true});
	$("#fetch_jprof_panel").dialog({ autoOpen: false ,width:800,modal:true});
});

function showUploadPanel(id){
    $("#upload_jprof_panel" ).dialog( 'open' );
	$("#hostId").val(id);
}

function showfetchPanel(id,ip,path){

	$("#fetch_ip").text("IP:"+ip);
	$("#fetch_path").text("PATH:"+path);
	$("#fetch_hostId").val(id);
	
	 $("#fetch_jprof_panel" ).dialog( 'open' );
}


function changeAutoFetch(id,runtype){
	if(runtype == 0){
		if(window.confirm('ȷ��ȡ���Զ���ȡjprof��־��?')){			
			location.href="./manage_jprof_host.jsp?id="+id+"&action=runtype&runtype=0";
		}
	}
	if(runtype == 1){
		if(window.confirm('ȷ���Զ���ȡjprof��־��?')){			
			location.href="./manage_jprof_host.jsp?id="+id+"&action=runtype&runtype=1";
		}
	}
}

</script>
</head>
<body>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
	<tr>
	  <td> <jsp:include page="../top.jsp"></jsp:include></td>
  	</tr>
<jsp:include page="../left.jsp"></jsp:include>
<%


String action = request.getParameter("action");

if("delete".equals(action)){
	String ip = request.getParameter("id");
	if(!UserPermissionCheck.check(request,"jprof","")){
		out.print("��û��Ȩ�޲���!");
		return;
	}
	
	
	
	JprofHost host = new JprofHost();
	
	host.setId(Integer.parseInt(ip));
	MonitorJprofAo.get().deleteJprofHosts(host);
}

if("runtype".equals(action)){
	
	if(!UserPermissionCheck.check(request,"jprof","")){
		out.print("��û��Ȩ�޲���!");
		return;
	}
	
	String runtype = request.getParameter("runtype");
	String id = request.getParameter("id");	
	JprofHost h = MonitorJprofAo.get().getJprofHosts(Integer.parseInt(id));
	h.setRunType(Integer.parseInt(runtype));	
	MonitorJprofAo.get().updateJprofHost(h);
}


List<JprofHost> hostList = MonitorJprofAo.get().findAllJprofHosts();
%>
	<tr><td align="center">
		<input type="button" value="����»���" onclick="location.href='./add_jprof_host.jsp'">
		<input type="button" onclick="location.href='./manage_jprof_class_method.jsp'" value="jprof��Ϣ��ѯ">
	 </td></tr>
	 <tr class="headcon">
        <td colspan="12" align="center">jprof �����б�</td>
      </tr>
	  <tr><td><table class="datalist"  width="1000">
      <tr class="ui-widget-header ">
		<td >Ӧ����</td>	
		<td >����IP</td>	
		<td >�ļ�·��</td>
		<td >ִ��״̬</td>
		<td >����</td>
	</tr>
	<%
	for(JprofHost host:hostList){
		
	%>
	<tr >
		<td width="80"><%=host.getAppName() %></td>	
		<td width="100"><%=host.getHostIp() %></td>	
		<td width="400"><%=host.getFilePath() %></td>
		<td width="100"><%=host.getRunType()==1?"��ʱ��ȡ":"�Ƕ�ʱ��ȡ" %></td>
		<td >
		<a onclick="deletehost('<%=host.getId() %>')"href="#">ɾ��</a>&nbsp;&nbsp;
		<a href="./update_jprof_host.jsp?id=<%=host.getId() %>">�޸�</a>&nbsp;&nbsp;
		<%=host.getRunType()==1?"<a onclick=\"changeAutoFetch('"+host.getId()+"','"+0+"')\" href=\"#\">ȡ����ʱ��ȡ</a>&nbsp;&nbsp;":"<a onclick=\"changeAutoFetch('"+host.getId()+"','"+1+"')\" href=\"#\">��ʱ��ȡ</a>&nbsp;&nbsp;" %>		
		<a onclick="showfetchPanel('<%=host.getId() %>','<%=host.getHostIp() %>','<%=host.getFilePath() %>')" href="#">������ȡ</a>&nbsp;&nbsp;
		<a onclick="showUploadPanel('<%=host.getId() %>')" href="#">�ϴ�jprof��־</a>&nbsp;&nbsp;
		</td>
	</tr>	
	<%
	} 
	%>
</table></td></tr>

	<tr><td align="left">
˵��:��������Զ���ȡ�������ȡ���������,��ȡ��־�ĺ������ʱ�� �磺/home/admin/logs/jprof.txt.2010-08-11
<div id="upload_jprof_panel" title="�ϴ�jprof��־">
	<form action="upload_file.jsp" method="post" enctype="multipart/form-data" onsubmit="$('#uploadDiv').toggle();$('#uploadDiv_msg').toggle();">
		<div id="uploadDiv">
			<input type="file" name="uploadfile"><input type="submit" value="�ύ">
			<input type="hidden" id="hostId" name="id" value="">
		</div>
		<div id="uploadDiv_msg" style="display:none">
			���ڽ����С����������ڴ�
		</div>
	</form>
</div> 

<div id="fetch_jprof_panel" title="��ȡ����jprof">
	<form  action="get_jprof_msg.jsp" method="post" onsubmit="$('#fetch_div').toggle();$('#fetch_msg').toggle();">		
		<div id="fetch_div">
			<table>
				<tr>
					<td id="fetch_ip">ip</td>
				</tr>
				<tr>
					<td id="fetch_path">ip</td>
				</tr>
			</table>
			<input type="submit" value="ȷ���ύ">
			<input type="hidden" id="fetch_hostId" name="id" value="">
		</div>
		<div id="fetch_msg" style="display:none">
			���ڽ����С����������ڴ�
		</div>
	</form>
</div> 
</td></tr>
<tr height="10"></tr>
<tr>
	  <td><img  src="<%=request.getContextPath () %>/statics/images/line.png"  width="1000" height="1"></td>
	</tr>
	<tr><td><jsp:include page="../bottom.jsp"></jsp:include></td></tr>
</table>
</body>

</html>