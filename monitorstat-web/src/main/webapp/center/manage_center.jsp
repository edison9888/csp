<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.*"%>
<%@page import="com.taobao.monitor.common.ao.center.*"%>

<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>���ļ��-��������</title>
<script type="text/javascript"	src="<%=request.getContextPath() %>/statics/js/jquery.min.js"></script>
<script type="text/javascript">
	$(function(){
		//IN_HSF-ProviderDetail  OUT_PageCache  OUT_HSF-Consumer  OUT_forest  OUT_TairClient EXCEPTION
		$("#userInfoTableId tr td").mouseover(function(){
			$(this).parent().children("td").addClass("report_on");
		})
		$("#userInfoTableId tr td").mouseout(function(){
			$(this).parent().children("td").removeClass("report_on");
		})
	})
</script>
</head>
<body>


<%
/*
request.setCharacterEncoding("gbk");
if(!UserPermissionCheck.check(request,"center","")){
	out.print("��û��Ȩ�޲���!");
	return;
}
*/
%>
<br>
<table width="1000" align="center" cellspacing="0" cellpadding="0" border="0"  style="margin:0 auto;">
<tr><td><jsp:include page="../top.jsp"></jsp:include></td></tr>
<tr ><td><div class=" ui-widget-content " style="width: 1000;">
<div  class=" ui-widget-header ui-corner-all ">�����б�</div>
<div  class="ui-widget-content">

<a href="./db_info_center.jsp">���ݿ����</a><br>
<a href="./server_info_center.jsp">����������</a><br>
<a href="./app_info_center.jsp">Ӧ�ù���</a><br>
<a href="./dayConfTmp_center.jsp">�ձ�����ģ�����</a><br>
<a href="./timeConfTmp_center.jsp">ʵʱ����ģ�����</a><br>
<a href="./manage_alter.jsp">���ñ��֪ͨ</a><br>
</div>
</div>
</td></tr>
<tr><td>
<jsp:include page="../bottom.jsp"></jsp:include>
</td></tr>
</table>
</body>
</html>