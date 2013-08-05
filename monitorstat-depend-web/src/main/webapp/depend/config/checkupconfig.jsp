<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import=" com.taobao.csp.depend.po.CheckupDependConfigRel"%>
<%@page import=" com.taobao.csp.depend.po.CheckupDependConfig"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>��ϸ����</title>
<script type="text/javascript"
	src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css"
	rel="stylesheet" />
<link rel="stylesheet"
	href="<%=request.getContextPath() %>/statics/css/main.css"
	type="text/css" />
<%
CheckupDependConfigRel opsConfigRel = (CheckupDependConfigRel)request.getAttribute("opsConfigRel");
CheckupDependConfigRel targetConfigRel = (CheckupDependConfigRel)request.getAttribute("targetConfigRel");

if(opsConfigRel == null)
  opsConfigRel = new CheckupDependConfigRel();

if(targetConfigRel == null)
  targetConfigRel = new CheckupDependConfigRel();

CheckupDependConfig configPo = (CheckupDependConfig)request.getAttribute("config");
if(configPo == null)
  configPo = new CheckupDependConfig();
%>
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
<body>

	<form
		action="<%=request.getContextPath() %>/dailycheck.do?method=updateCheckupConfig"
		method="post" style="padding-top: 20px;">
								<table
									class="table table-striped table-bordered table-condensed"  style="width: 90%;" align="center">
									<tr>
										<td>Ӧ������:</td>
										<td colspan="2"><font color="#800040">${config.opsName}</font>
											&nbsp;&nbsp;<a
											href="<%=request.getContextPath() %>/dailycheck.do?method=getTempleteDetail&opsName=${config.opsName}"
											title="�������ú���ˢ��ҳ��">����ģ��</a></td>
									</tr>
									<tr>
										<td>Ӧ��IP:</td>
										<td><select name="selfIp">
												<%
													if(opsConfigRel.getSecondMachineIps() == null || "".equals(opsConfigRel.getSecondMachineIps())) {
																									  out.print("<option value='' selected='selected'></option>");
																									} else {
																									  String[] ips = opsConfigRel.getSecondMachineIps().split(",");
																									  for(String ip: ips) {
																									    String selectStr = "";
																									    if(ip.equals(configPo.getSelfIp()))
																									      selectStr = "selected='selected' ";
																									    out.print("<option value='" + ip + "' " + selectStr +">" + ip + "</option>");
																									  }
																									}
												%>
										</select></td>
									</tr>
									<tr>
										<td>Ӧ��${opsName}�����ű�:</td>
										<td>
											<%
												if(opsConfigRel.getStartupPath() == null || "".equals(opsConfigRel.getStartupPath())) {
																								  out.print("<input type='hidden' value='' name='startupPath' size='100' />");
																								} else {
																								  out.print("<input type='hidden' value='" + opsConfigRel.getStartupPath() + "' name='startupPath' size='100' />" + opsConfigRel.getStartupPath());
																								}
											%>
										</td>
									</tr>
									<tr>
										<td>������Ӧ��:</td>
										<td><input type="hidden" value="${config.opsName }"
											name="opsName"> <c:if test='${flag == "add"}'>
												<select name="targetOpsName" onchange="changeAppIps(this)">
													<c:forEach items="${dependSet}" var="appDependName">
														<option value="${appDependName}">${appDependName}</option>
													</c:forEach>
												</select>
															&nbsp;&nbsp;<a
													href="<%=request.getContextPath()%>/dailycheck.do?method=showTemplateList"
													title="�������ú���ˢ��ҳ��">����ģ��</a>
											</c:if> <c:if test='${flag != "add"}'>
												<input type="hidden" value="${config.targetOpsName }"
													name="targetOpsName" />
															${config.targetOpsName}
															&nbsp;&nbsp;<a
													href="<%=request.getContextPath() %>/dailycheck.do?method=getTempleteDetail&opsName=${config.targetOpsName}"
													title="�������ú���ˢ��ҳ��">����ģ��</a>
											</c:if></td>
									</tr>
									<tr>
										<td>������Ӧ��IP:</td>
										<td><c:if test='${flag == "add"}'>
												<input type='text' name='targetIps' id='targetIps'
													size='100' readonly="readonly"></input>
											</c:if> <c:if test='${flag != "add"}'>
												<%
													if(targetConfigRel.getSecondMachineIps() == null || "".equals(targetConfigRel.getSecondMachineIps())) {
																									  out.print("<input type='hidden' value='' name='targetIps' size='100' />");
																									} else {
																									  out.print("<input type='hidden' value='" + targetConfigRel.getSecondMachineIps() + "' name='targetIps' size='100'>" + targetConfigRel.getSecondMachineIps());
																									}
												%>
											</td>
										</c:if>
									</tr>
									<tr>
										<td>�Ƿ���Ҫ��ʱִ��:</td>
										<td><select name="autoRun" onchange="autoRunChange()"
											id="autoRun">
												<option value="no">��</option>
												<option value="yes">��</option>
										</select>
											<div id="autoRunTime">
												<input type="text" value="" name="autoRunTime">��ʹ��Cron
												���������������һ����һ�� ��:0 15 10 ? * MON ���ڿ���
												SUN��MON��TUE��WED��THU��FRI��SAT��ʾ
											</div></td>
									</tr>
									<tr>
										<td>����ǿ��:</td>
										<td><select name="expectDepIntensity">
												<option value="��">��</option>
												<option value="ǿ">ǿ</option>
										</select></td>
									</tr>
									<tr>
										<td>�������:</td>
										<td><select name="preventType"
											onchange="preventDescChange()" id="preventType">
												<option value="IpTable"
													<c:if test="${config.preventType=='IpTable'}">selected</c:if>>IpTable��ʽ</option>
												<option value="Btrace"
													<c:if test="${config.preventType=='Btrace'}">selected</c:if>>����ע��</option>
										</select></td>
									</tr>
									<tr>
										<td>�������:</td>
										<td>
											<div>
												<textarea rows="5" cols="100" id="preventFeatrue"
													name="preventFeatrue"  readonly="readonly">${config.preventFeatrue}</textarea>
											</div>
											<div>
												<font id="preventDescId"></font>
											</div>
										</td>
									</tr>
									<tr>
										<td>�ӳٷ���:</td>
										<td><select name="delayType" onchange="delayDescChange()"
											id="delayType">
												<option value="no"
													<c:if test="${config.delayType=='no'}">selected</c:if>>����Ҫ</option>
												<option value="tc"
													<c:if test="${config.delayType=='tc'}">selected</c:if>>tc����</option>
												<option value="Btrace"
													<c:if test="${config.delayType=='Btrace'}">selected</c:if>>����ע��</option>
										</select></td>
									</tr>
									<tr id="delayDivId">
										<td>�ӳ�����:</td>
										<td>
											<div>
												<textarea rows="5" cols="100" id="delayFeatrue"
													name="delayFeatrue" readonly="readonly">${config.delayFeatrue}</textarea>
											</div>
											<div>
												<font id="delayDescId"></font>
											</div>
										</td>
									</tr>
									<tr>
										<td>Ӧ��������Ԥ��ִ��ָ��:</td>
										<td>
											<div>
												<textarea rows="5" cols="100" name="beforehandRun">${config.beforehandRun}</textarea>
											</div>
										</td>
									</tr>
									<tr>
										<td>�����Լ�鷽ʽ:</td>
										<td><select name="checkupType"
											onchange="checkupDescChange()" id="checkupType">
												<option value="url"
													<c:if test="${config.checkupType=='url'}">selected</c:if>>��鵥��URL����</option>
												<option value="selenium"
													<c:if test="${config.checkupType=='selenium'}">selected</c:if>>selenium�ű����</option>
										</select></td>
									</tr>
									<tr id="checkupUrlDivId">
										<td>�����Լ��URL:</td>
										<td><input type="text"
											value="<%=opsConfigRel.getCheckupUrl()%>" name="checkupUrl"
											size="100" readonly="readonly"></td>
									</tr>
									<tr id="checkupContextDivId">
										<td>���URL���ر���Ҫ��������:</td>
										<td><div>
												<input type="text"
													value="<%=opsConfigRel.getCheckupContext()%>"
													name="checkupContext" size="100" readonly="readonly">
											</div></td>
									</tr>
									<tr id="seleniumScriptDivId">
										<td>�����Լ��selenium���ýű�:</td>
										<td>
											<div style="display: none">
												selenium������<input id="seleniumServer" name="seleniumServer"
													value="${config.seleniumServer }" size="100"
													readonly="readonly">
											</div>
											<div>
												<textarea rows="10" cols="100" id="seleniumScript"
													name="seleniumScript" readonly="readonly"><%=opsConfigRel.getSeleniumScript()%></textarea>
											</div>
										</td>
									</tr>
									<tr>
										<td colspan="2" align="center"><input type="submit"
											value="ȷ������"></td>
									</tr>
								</table>
	</form>

	<script type="text/javascript">
	function changeAppIps(selectObj) {
		$.getJSON("<%=request.getContextPath()%>/dailycheck.do?method=getTempleteDetailJson",
				{opsName: selectObj.value},
				function(jsonObj){
					if(jsonObj == null) {
						return;
					}
					var ipsT = jsonObj.opsConfigRel.machineIps;
					if(ipsT == "" || ipsT == "��") {
						alert("����Ӧ�õ�IP���Ϸ�!");
					} else {
						$("#targetIps").attr("value",jsonObj.opsConfigRel.machineIps);
						$("#preventFeatrue").attr("value",jsonObj.opsConfigRel.machineIps);
						$("#delayFeatrue").attr("value",jsonObj.opsConfigRel.machineIps);
					}
				}
		); 	
	}

	function preventDescChange(){
		var type = $("#preventType").val();
		if(type == 'IpTable') { 			
			$("#preventDescId").text("��������Ҫ���ε�IP���ƣ����IP���ö̺Ÿ��� ����IP1,IP2");
		} else if(type == 'Btrace'){			
			$("#preventDescId").text("��������Ҫ���εĽӿ����ƣ�className:method ����ӿ����ö̺Ÿ��� ����com.taobao.ic.ItemDao:getMessage,com.taobao.ic.ItemDao:getMessage");
		} 
	}

	function checkupDescChange(){
		var type = $("#checkupType").val();
		if(type == 'url') { 			
			$("#checkupUrlDivId").show();
			$("#checkupContextDivId").show();
			$("#seleniumScriptDivId").hide();
		} else if(type == 'selenium'){			
			$("#checkupUrlDivId").hide();
			$("#checkupContextDivId").hide();
			$("#seleniumScriptDivId").show();
		} 
	}
	
	function autoRunChange(){
		var type = $("#autoRun").val();
		if(type == 'no') { 			
			$("#autoRunTime").hide();
		} else if(type == 'yes'){			
			$("#autoRunTime").show();
		} 
	}
	
	
	function delayDescChange(){
		var type = $("#delayType").val();
		if(type == 'tc') { 			
			$("#delayDescId").text("��������ʱʱ�� ��λΪ����");
			$("#delayDivId").show();
		} else if(type == 'Btrace'){			
			$("#delayDescId").text("��������Ҫ�ӳٵĽӿ�����,����className:method:delaytime ��delayTime ��λΪms ����ӿ����ö̺Ÿ��� com.taobao.ic.ItemDao:getMessage��5000,com.taobao.ic.ItemDao:getMessage:3000");
			$("#delayDivId").show();
		} else if(type == 'no'){			
			$("#delayDescId").text("����Ҫ");
			$("#delayDivId").hide();
		} 
	}
	autoRunChange();
	delayDescChange();
	preventDescChange();
	checkupDescChange();
</script>

</body>
</html>