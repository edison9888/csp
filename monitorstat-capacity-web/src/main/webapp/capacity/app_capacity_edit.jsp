<%@ page language="java" contentType="text/html; charset=gbk"
    pageEncoding="gbk"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.csp.capacity.po.CapacityAppPo" %>

<%@page import="com.taobao.monitor.common.po.ProductLine"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>�����滮-����Ӧ���޸�</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet" />
<script language="JavaScript" src="<%=request.getContextPath() %>/statics/js/capacity.js"></script>
</head>
<body>

<%
request.setCharacterEncoding("gbk");

// ҳ���ϴ������Ĳ���
String appId = request.getParameter("appId");
%>
<form id="myForm" name="myForm" action="./manage.do?method=editCapacityAppDataSource" method="post">
<%

ProductLine productLine = (ProductLine)request.getAttribute("productLine");
CapacityAppPo editCapacityAppPo = (CapacityAppPo)request.getAttribute("editCapacityAppPo");

String action = (String)request.getAttribute("action");
if("edit".equals(action)){
	boolean success = ((Boolean)request.getAttribute("success")).booleanValue();
		
%>
	<font size="+3" color="red">
	<%
		if(success) {
			out.print("�޸ĳɹ�");
		} else { 
				out.print("�����쳣,�޸�ʧ��"); 
		}
	%>
	</font>	
	
	<%
}else{
	// ����ԭʼֵ
	String initialCompany = productLine == null? "" : productLine.getDevelopGroup();
	String initialAppGroup = productLine == null? "" : productLine.getProductline();
	String initialAppName = productLine == null? "" : productLine.getAppName();
	String initialDataSource = editCapacityAppPo == null ? "" : editCapacityAppPo.getDatasourceName();
	String initialAppType = editCapacityAppPo == null ? "" : editCapacityAppPo.getAppType();
	String initialDataFeature = editCapacityAppPo == null ? "" : editCapacityAppPo.getDataFeature();
	String initialGrouwthRate = editCapacityAppPo == null ? "" : String.valueOf(editCapacityAppPo.getGrowthRate());
	String initialItemName = editCapacityAppPo == null ? "" : String.valueOf(editCapacityAppPo.getItemName());
	String initialDataName = editCapacityAppPo == null ? "" : String.valueOf(editCapacityAppPo.getDataName());
%>
<table>
	
	<tr>	
		<td>��Ʒ��:</td>
		<td><input type="text" id="company" name="company" value="<%=initialCompany%>" readonly="true" ></td>
	</tr>
	
	<tr>	
		<td>������Ʒ��:</td>
		<td><input type="text" id="appGroupName" name="appGroupName" value="<%=initialAppGroup%>" readonly="true" ></td>
	</tr>
	
	<tr>	
		<td>Ӧ����:</td>
		<td><input type="text" id="appName" name="appName" value="<%=initialAppName%>" readonly="true" ></td>
	</tr>
	
	<tr>	
		<td>����Դ:</td>
		<td><input type="text" name="dataSource" value="<%=initialDataSource%>" style="width: 80%;"></td>
	</tr>
	<tr>
		<td>Ӧ������:</td>
		<td><input type="text" name="appType" value="<%=initialAppType%>" style="width: 80%;"></td>
	</tr>
	<tr>
		<td>����:</td>
		<td><input type="text" name="dataFeature" value="<%=initialDataFeature%>" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td>ҵ�������ٷֱ�:</td>
		<td><input type="text" id="growthRate" name="growthRate" value="<%=initialGrouwthRate%>" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td>���������:</td>
		<td><input type="text" name="itemName" value="<%=initialItemName%>" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td>qps����������:</td>
		<td><input type="text" name="dataName" value="<%=initialDataName%>" style="width: 80%;"></td>
	</tr>
	
	<tr>
		<td colspan="2"> <input type="hidden" name="appId" value="<%=appId%>"></td>
	</tr>			
	<tr>
		<td align="right" colspan="2">
			<input type="hidden" value="edit" name="action">
			<input type="submit" value="��    ��">&nbsp;&nbsp;
			<input type="button" value="��    ��" onclick="closeWindow()">
		</td>
	</tr>
	
</table>
</form>
<%} %>
<script type="text/javascript">
function alterApp() {
		var growthRate = $("#growthRate").attr("value");
		if (growthRate==null || growthRate=="") {
			alert("ҵ�������ʲ���Ϊ��");
			return;
		}
		var patrn=/^[0-9]{1,20}$/; 
		if (!patrn.exec(growthRate)) {
			alert("ҵ�������ʱ���Ϊ����");
			return;
		}
		
		document.myForm.submit();
	}
</script>
</body>
</html>