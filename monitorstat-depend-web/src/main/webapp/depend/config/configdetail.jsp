<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>�鿴��������</title>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/jquery/jquery.min.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/statics/css/main.css" type="text/css" />
<link href="<%=request.getContextPath() %>/statics/css/bootstrap.css" rel="stylesheet"/></head>
<body>
<form action="<%=request.getContextPath() %>/dailycheck.do?method=manualCheckup" method="post">
	<table>
				<tr>
					<td valign="top">
						<table >
							<tr>
								<td width="100%" valign="top">
									<table  width="100%" >
										<tr>
											<td valign="top">
											
												<table class="table table-striped table-bordered table-condensed">
													<tr>
														<td colspan="2">
															Ӧ��<font color="greed">${config.opsName }</font>����Ӧ��<font color="greed">${config.targetOpsName }</font>��ǿ�������������
															<input type="hidden" value="${config.opsName }" name="opsName">
															<input type="hidden" value="${config.targetOpsName }" name="targetOpsName">
														</td>
													</tr>
													<tr>
														<td>${opsName}Ӧ��IP:</td>
														<td>${config.selfIp }</td>
													</tr>
													<tr>
														<td>�Ƿ���Ҫ��ʱִ��:</td>
														<td>
														${config.autoRun }		
														<div  id="autoRunTime"><c:if test="${config.autoRun=='yes' }">ʱ������:${config.autoRunTime }</c:if></div>
														</td>
													</tr>
													<tr>
														<td>����ǿ��:</td>
														<td>${config.expectDepIntensity }	</td>
													</tr>
													<tr>
														<td>�����ʽ:</td>
														<td>
															${config.preventType}
														</td>
													</tr>
													<tr>
														<td>�������:</td>
														<td>
														<div>${config.preventFeatrue}</div>
														<div><font id="preventDescId"></font></div>
														</td>
													</tr>
														<tr>
														<td>�ӳٷ���:</td>
														<td>
															${config.delayType}
														</td>
													</tr>
													<tr id="delayDivId">
														<td>�ӳ�����:</td>
														<td>
														<div>${config.delayFeatrue}</div>
														<div></div>
														</td>
													</tr>
													<tr>
														<td>${opsName}�����ű���ַ:</td>
														<td>${config.startupPath }</td>
													</tr>
													<tr>
														<td>�����Լ�鷽ʽ:</td>
														<td>
															<c:if test="${config.checkupType=='url'}">��鵥��URL����</c:if>
															<c:if test="${config.checkupType=='selenium'}">selenium�ű����</c:if>
														</td>
													</tr>
													<tr id="checkupUrlDivId">
														<td>�����Լ��URL:</td>
														<td>${config.checkupUrl }</td>
													</tr>
													<tr id="checkupContextDivId">
														<td>���URL���ر���Ҫ��������:</td>
														<td>${config.checkupContext }</td>
													</tr>
													<tr id="seleniumScriptDivId">
														<td>�����Լ��selenium���ýű�:</td>
														<td>
														<div>${config.startupPath }</div>
														</td>
													</tr>
													<tr >
														<td colspan="2" align="center"><input type="button" value="�ر�" ><input type="submit" value="ִ��ǿ�����"></td>
													</tr>
												</table>
											</td>
										</tr>								
									</table>
								</td>
							</tr>
						</table>
					</td>
				</tr>
			</table>
</form>	




<script type="text/javascript">
	function preventDescChange(){
		var type = '${config.preventType}';
		if(type == 'IpTable') { 			
			$("#preventDescId").text("��������Ҫ���ε�IP���ƣ����IP���ö̺Ÿ��� ����IP1,IP2");
		} else if(type == 'Btrace'){			
			$("#preventDescId").text("��������Ҫ���εĽӿ����ƣ�����ӿ����ö̺Ÿ��� ����com.taobao.ic.ItemDao,com.taobao.ic.ItemDao");
		} 
	}

	function checkupDescChange(){
		var type =  '${config.checkupType}';
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
	function delayDescChange(){
		var type = $("#delayType").val();
		if(type == 'nistnet') { 			
			$("#delayDescId").text("��������Ҫ�ӳٵ�IP���ƣ����IP���ö̺Ÿ��� ����IP1,IP2");
			$("#delayDivId").show();
		} else if(type == 'Btrace'){			
			$("#delayDescId").text("��������Ҫ�ӳٵĽӿ����ƣ�����ӿ����ö̺Ÿ��� ����com.taobao.ic.ItemDao,com.taobao.ic.ItemDao");
			$("#delayDivId").show();
		} else if(type == 'no'){			
			$("#delayDescId").text("����Ҫ");
			$("#delayDivId").hide();
		} 
	}
	delayDescChange();
	preventDescChange();
	checkupDescChange();
</script>

</body>
</html>