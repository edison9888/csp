<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@page import="com.taobao.csp.depend.po.CheckupDependConfigRel"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>Ӧ��ģ����ϸ</title>
<script type="text/javascript" src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/statics/js/easyui/themes/gray/easyui.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/main.css" type="text/css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<%
  CheckupDependConfigRel rel = (CheckupDependConfigRel) request.getAttribute("opsConfigRel");
  if (rel == null)
    rel = new CheckupDependConfigRel();
%>
<style type="text/css">
.table_comm thead tr td { 
    background-color: #A8E0ED;
    text-align: center;
}
</style>
<script type="text/javascript">
function backToMain() {
	window.location.href="<%=request.getContextPath()%>/dailycheck.do?method=showTemplateList";	
}
</script>
<style type="text/css">
  textarea {
    width: 500px;
  }
    select {
    width: 510px;
  }
    input {
    width: 500px;
  }
</style>
</head>
<body class="table table-striped table-bordered table-condensed">
<form action="<%=request.getContextPath() %>/dailycheck.do?method=saveTemplateDetail" method="post" style="padding-top: 20px;">
	<table class="table table-striped table-bordered table-condensed" style="width: 90%;" align="center">
			<tr>
				<td>Ӧ������:</td>
				<td><%=rel.getOpsName()%>
				<input name="opsName" id="opsName" type="hidden" value="<%=rel.getOpsName()%>"/>
				<input name="opsId" id="opsId" type="hidden" value="<%=rel.getOpsId()%>"/>
				</td>
			</tr>
			<tr>
				<td>Ӧ��״̬:</td>
				<td>
				<select name="appStatus">
				<%
					String appStatus = rel.getAppStatus();
				%>
					<option value="1" <%if("1".equals(appStatus)){out.print(" selected='selected'");System.out.println("good");}%>>��Ч</option>
					<option value="-1" <%if("-1".equals(appStatus)){out.print(" selected='selected'");System.out.println("bad");}%>>��Ч</option>
				</select>
				</td>
			</tr>
			<tr>
				<td>���׻��������б�:</td>
				<td><input name="secondMachineIps" id="secondMachineIps" type="text" value="<%=rel.getSecondMachineIps()%>" size="100"/></td>
			</tr>
			<tr>
				<td>Daily�����б�:</td>
				<td><input name="dailyMachineIps" id="dailyMachineIps" type="text" value="<%=rel.getDailyMachineIps()%>" size="100"/></td>
			</tr>
			<tr>
				<td>��������б�:</td>
				<td><input name="addIps" id="addIps" type="text" value="<%=rel.getAddIps()%>" size="100"/></td>
			</tr>						
			<tr>
				<td>�����ű�:</td>
				<td><input name="startupPath" id="startupPath" type="text" value="<%=rel.getStartupPath()%>" size="100"/></td>
			</tr>
			<tr>
				<td>Ӧ��˵��:</td>
				<td><input name="description" id="description" type="text" value="<%=rel.getDescription()%>" size="100"/></td>
			</tr>
			<tr>
				<td>Url����ַ:</td>
				<td><input name="checkupUrl" id="checkupUrl" type="text" value="<%=rel.getCheckupUrl()%>" size="100"/></td>
			</tr>
			<tr>
				<td>�������</td>
				<td><input name="checkupContext" id="checkupContext" type="text" value="<%=rel.getCheckupContext()%>" size="100"/></td>
			</tr>			
			<tr>
				<td>Selenium Remote Server:</td>
				<td><textarea rows="10" cols="100" name="seleniumServer" id="seleniumServer" type="text" value="<%=rel.getSeleniumServer()%>" ><%=rel.getSeleniumServer()%></textarea></td>
			</tr>
			<tr>
				<td>Selenium���ű�:</td>
				<td><textarea rows="10" cols="100" name="seleniumScript" id="seleniumScript" type="text" value="<%=rel.getSeleniumScript()%>" ><%=rel.getSeleniumScript()%></textarea></td>
			</tr>
			<tr>
				<td>Selenium�������:</td>
				<td><textarea rows="10" cols="100" name="stepRecords" id="stepRecords" type="text" value="<%=rel.getStepRecords()%>" title="���ľ��岽���ͼƬ������Բ�����"><%=rel.getStepRecords()%></textarea></td>
			</tr>			
			<tr>
				<td colspan="1"><input type="submit" value="����"/></td>
				<td colspan="1"><input type="button" value="����" onclick="backToMain()"/></td>
			</tr>
		</table>
</form>
</body>
</html>