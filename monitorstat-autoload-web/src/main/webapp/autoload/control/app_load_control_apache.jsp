<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","apacheѹ�����"); %>
<jsp:include page="../header.jsp"></jsp:include>
		<div class="container">
			<div id="bd" class="resource" style="width: 1050px;">
				<div style="width: 1050px; height: auto; margin: 0 auto">
					<table id="myTable">
						<tr>
							<td>
								Ӧ������
							</td>
							<td>
								<font size="4" color="#000">${loadConfig.appName}</font>
							</td>
						</tr>
						<tr>
							<td>
								Ŀ�����
							</td>
							<td>
								${loadConfig.hostIp}
							</td>
						</tr>
						<tr>
							<td>
								������
							</td>
							<td>
								${loadConfig.wangwangs}
							</td>
						</tr>
						<tr>
							<td>
								ѹ������
							</td>
							<td>
								${loadConfig.limitFeature}
							</td>
						</tr>
						<tr>
							<td>
								jk·��
							</td>
							<td>
								${loadConfig.jkConfigPath}
							</td>
						</tr>
						<tr>
							<td>
								apache����·��
							</td>
							<td>
								${loadConfig.apachectlPath}
							</td>
						</tr>
						<tr>
							<td>
								�û���
							</td>
							<td>
								${loadConfig.userName}
							</td>
						</tr>
					</table>
					<table id="myTable">
						<tr>
							<td>
								��������IP
							</td>
							<td>
								����״̬
							</td>
						</tr>
						<c:forEach items="${currentTask.apacheControlMap}"
							var="apacheload">
							<tr>
								<td>
									${apacheload.value.ip }
								</td>
								<td>
									${apacheload.value.configName }
								</td>
							</tr>
						</c:forEach>
					</table>

					<table id="myTable">
						<tr>
							<td>
								Ԥ��IP
							</td>
						</tr>
							<tr>
								<td>
						<c:forEach items="${ipList}" var="ip">
									<c:out value="${ip}" />��  
						</c:forEach>
								</td>
							</tr>
					</table>
				</div>

				<div style="width: 1050px; height: auto; margin: 0 auto">
					<form
						action="<%=request.getContextPath() %>/loadrun/control.do?method=doControl"
						method="post">
						<input type="hidden" name="appId" value="${loadConfig.appId}" />
						<table id="myTable">
							<tr>
								<td>
									IP��ַ�� <input  type="text" value="" name="targetip" /> 
								</td>
								<td>
									Ŀ�꣺
									<input type="text" value="4" name="split_target" />
								</td>
							</tr>
							<tr>
								<td >
									apache��������(����)��
									<input type="text" value="1" name="split_local" /> 
								</td>
								<td>
									<button class="btn info" type="submit">
										�ύ����
									</button>
								</td>
							</tr>
						</table>
					</form>
					<table id="myTable">
						<tr>
							<td>
								����key
							</td>
							<td>
								�������value
							</td>
						</tr>
						<tbody id="loadrunResultTable">

						</tbody>
					</table>
					<form
						action="<%=request.getContextPath() %>/loadrun/control.do?method=stopTask"
						method="post">
						<table>
							<tr>
								<td colspan="3" align="center">
									<button class="btn info" type="submit">
										�ر�ѹ������
									</button>
									<input type="hidden" value="${loadConfig.appId}" name="appId" />
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
<script type="text/javascript">
function queryLoadValue(){
	$.getJSON("<%=request.getContextPath() %>/loadrun/control.do?method=showValue&appId=${loadConfig.appId}", {}, function(json){
		$("#loadrunResultTable").empty();
		for(key in json){
			$("#loadrunResultTable").append("<tr><td>"+key+"</td><td>"+json[key]+"</td></tr>");
		}
		
	}); 
	window.setTimeout("queryLoadValue()",10000); 	
}
queryLoadValue();
</script>
			<jsp:include page="../footer.jsp"></jsp:include>