<%@ page language="java" contentType="text/html; charset=GB18030" isELIgnored="false" pageEncoding="GB18030"%>
<%@page import="com.taobao.csp.depend.cache.CspCheckAppCache"%>    
<%@page import="java.util.*"%>    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache"/>
<meta http-equiv="expires" content="0">
<!-- JQuery��ص�JS����leftmenu.jsp�ж����� -->
<script language="javascript" type="text/javascript" src="<%=request.getContextPath() %>/statics/datePicket/WdatePicker.js"></script>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/>
<title>����ģ��</title>
</head>
<body>
<%@ include file="../dailyheader.jsp"%>
<%
Map<String, String> map = CspCheckAppCache.get().getAppIpMap();
String opsName = (String)request.getAttribute("opsName");
%>
	<form id="mainForm"  action="<%=request.getContextPath() %>/dailycheck.do" method="get" class="form-search">
		<input type="hidden" value="appIndex" name="method" >
		<div style="text-align: center">
		<select name="opsName" id="opsName" class="input-large">
		<%
		if(map != null) {
			for(String appName : map.keySet()) {
				if(appName.equals(opsName)) {
					out.println("<option value='" + appName + "' selected='selected'>" + appName + "</option>");					
				} else {
					out.println("<option value='" + appName + "'>" + appName + "</option>");
				}
			}
		}
		%>&nbsp;&nbsp;
		</select><button type="submit" class="btn">��ѯ</button>
		</div>
		</form>
			<div class="row">
				<div class="span12">
												<div style="float:right;margin-top:0px;">
												<a href="javascript:checkDependForAll()"><strong>�������</strong></a>													
												&nbsp;
												<a href="javascript:openWin('<%=request.getContextPath()%>/dailycheck.do?method=gotoAddCheckupConfig&opsName=${opsName}&selectDate=${selectDate}',700,1100)">�����������</a>
												&nbsp;
												<a href="javascript:openWin('<%=request.getContextPath()%>/dailycheck.do?method=showTemplateList',700,1100)">����ģ����Ϣ</a>
												</div></div>
			</div><!-- row -->
				<table  width="100%"  class="table table-striped table-bordered table-condensed" align="center">
													<thead>
														<tr>
															<td rowspan="2" align="center" width="10%">����Ӧ��</td>
															<td rowspan="2" align="center" width="10%">Ӧ������</td>
															<td rowspan="2" align="center" width="5%">����ǿ��</td>
															<td colspan="4" align="center" width="20%">ǿ����֤���</td>
															<td rowspan="2" align="center" width="10%">�Զ�ִ��</td>
															<td rowspan="2" align="center" width="10%">ʱ���趨</td>
															<td rowspan="2" align="center" width="15%">���ʱ��</td>
															<td rowspan="2" align="center"  width="20%">����</td>
														</tr>
														<tr >
														  <td align="center"  >��������</td>
															<td  align="center" >��������</td>
															<td align="center" >�ӳ�����</td>
															<td  align="center" >�ӳ�����</td>
														</tr>
													</thead>
													<tbody>
													<c:forEach items="${list}" var="config">
														<tr>
															<td>${config.targetOpsName}</td>
															<td align="center">${config.targetAppType}</td>
															<td align="center">${config.expectDepIntensity}</td>
															<td align="center">
															<font color="<c:choose><c:when test="${config.startPreventIntensity=='ǿ' }">red</c:when><c:when test="${config.startPreventIntensity=='��' }">green</c:when></c:choose>">${config.startPreventIntensity}</font>
															</td>
															<td align="center">
															<font color="<c:choose><c:when test="${config.runPreventIntensity=='ǿ' }">red</c:when><c:when test="${config.runPreventIntensity=='��' }">green</c:when></c:choose>">${config.runPreventIntensity}</font>
															</td>
															<td align="center">
															<font color="<c:choose><c:when test="${config.startDelayIntensity=='ǿ' }">red</c:when><c:when test="${config.startDelayIntensity=='��' }">green</c:when></c:choose>">${config.startDelayIntensity}</font>
															</td>
															<td align="center">
															<font color="<c:choose><c:when test="${config.runDelayIntensity=='ǿ' }">red</c:when><c:when test="${config.runDelayIntensity=='��' }">green</c:when></c:choose>">${config.runDelayIntensity}</font>
															</td>
															<td align="center">${config.autoRun}</td>
															<td align="center">${config.autoRunTime}</td>
															<td align="center">${config.addTime}</td>
															<td><a  href="javascript:openWin('<%=request.getContextPath() %>/checkupdepend.do?method=showCheckResult&opsName=${opsName}&targetOpsName=${config.targetOpsName}',1000,1400)">�鿴������</a>
															&nbsp;&nbsp;<a href="javascript:openWin('<%=request.getContextPath() %>/dailycheck.do?method=gotoCheckupConfig&opsName=${opsName}&targetOpsName=${config.targetOpsName}',700,1100)">�޸�</a>
															&nbsp;&nbsp;
															<a href="javascript:openWin('<%=request.getContextPath() %>/dailycheck.do?method=manualCheckup&opsName=${opsName}&targetOpsName=${config.targetOpsName}',700,1100)">����</a></td>
														</tr>
														</c:forEach>
														</tbody>
													</table>
</body>
<script type="text/javascript">
	function checkDependForAll() {
		if(confirm('ȷ������ִ�����������?')) {
			$.getJSON("<%=request.getContextPath()%>/dailycheck.do?method=addAllByOpsName",
					{opsName: '${opsName}'},
					function(jsonObj){
						if(jsonObj != null && jsonObj.msg=="success") {
							alert("����������ɹ�");
						} else {
							alert(jsonObj.failmsg);
						}
					}
			); 	
		} else {
			return;
		}
	}
	//��ʼ��search bar
	$(document).ready(function(){
		$('#mm').accordion('select', "�����������");
	}); 
</script>
</html>