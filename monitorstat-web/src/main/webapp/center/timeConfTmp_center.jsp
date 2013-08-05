<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>


<%@page import="com.taobao.monitor.common.ao.center.TimeConfAo"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-ʵʱģ����Ϣ����</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
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
}
.report_on{background:#bce774;}
</style>
<script type="text/javascript">
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#userInfoTableId tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#userInfoTableId tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
		
		$(".deleteAction").click(function(){
			return confirm("�����Ҫɾ����û�а�");
		});
					
	})
	

	function goToManagerCenter(){
		
		var url = "<%=request.getContextPath () %>/center/manage_center.jsp";
		location.href=url;
	}
	
</script>
</head>
<body>
<jsp:include page="../head.jsp"></jsp:include>
<%
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
%>
<%


String id = request.getParameter("id");

String action = request.getParameter("action");
if("delete".equals(action)){

	TimeConfAo.get().deleteTimeConfTmp(Integer.parseInt(id));
}
%>

<input type="button" value="����µ�ʵʱ����ģ��" onclick="location.href='./timeConfTmp_add.jsp'">
<input type="button" value="����" onclick="location.href='./manage_center.jsp'">
<div class="ui-dialog ui-widget ui-widget-content ui-corner-all " style="width: 100%;">
<div class="ui-dialog-titlebar ui-widget-header ui-corner-all ui-helper-clearfix">ʵʱ����ģ���б�</div>
<div id="dialog" class="ui-dialog-content ui-widget-content">
<%
List<TimeConfTmpPo> tmpList = TimeConfAo.get().findAllAppTimeConfTmp();
%>
<table width="100%" border="1" class="ui-widget ui-widget-content" id="userInfoTableId">
	<tr>
	
		<td align="center">��־�ļ�����</td>
		<td align="center">������</td>
		<td align="center">�зָ���</td>
		<td align="center">�ռ�Ƶ��</td>
		<td align="center">tail����</td>
		<td align="center">���ݻ�ȡ</td>
		<td align="center">������ʽ</td>
		<td align="left">  ����</td>
		
	</tr>
	<%for(TimeConfTmpPo po: tmpList){ %>
	<tr >
		<td align="center"><%=po.getAliasLogName() %></td>
		<td align="center"><%=po.getClassName()%></td>
		<!-- <td align="center"><%=po.getFilePath()%></td> -->
		<td align="center"><%=po.getSplitChar() %></td>
		<!--<td align="center"><%=po.getAnalyseFuture()%></td> -->
		<td align="center"><%=po.getAnalyseFrequency()%></td>
		<td align="center"><%=po.getTailType()%></td>
		<td align="center">
			<%if(po.getObtainType()==1){out.print("��־�ļ�");}
			if(po.getObtainType()==2){out.print("shell��ʽ");} 
			if(po.getObtainType()==3){out.print("http��ʽ");}
			%>
		</td>
		<td align="center">
			<%  if(po.getAnalyseType()==1){out.print("java-class");}
				if(po.getAnalyseType()==2){out.print("javscript");} 
			%>
		</td>
		<td>
			<a href="./timeConfTmp_update.jsp?id=<%=po.getTmpId()%>">�޸�</a>&nbsp;&nbsp;
			<a id="deleteAction" class="deleteAction" href="./timeConfTmp_center.jsp?id=<%=po.getTmpId() %>&action=delete">ɾ��</a> &nbsp;&nbsp;
			<a href="./timeConfTmp_check.jsp?id=<%=po.getTmpId() %>">�鿴</a> &nbsp;&nbsp;
		</td>
	</tr>
	<%} %>
</table>
</div>
</div>
<jsp:include page="../buttom.jsp"></jsp:include>
</body>
</html>