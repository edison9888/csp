<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","hsfѹ�����"); %>
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
								�����ӿ�
							</td>
							<td>
								<c:forTokens items="${loadConfig.configFeature}" delims=","
									var="tech">
									<c:out value="${tech}" /><br/>
				                </c:forTokens>
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
								<td width="700">
									��ǰip������
									<input style="width:500px;" type="text" value="${currentTask.curCopyNum}"
										name="ipCopy" />
								</td>
								<td align="right" width="300">
									<button class="btn info" type="submit">
										�ύIP����
									</button>
									<button class="btn info" type="button"
										onclick="window.open('http://ops.jm.taobao.org:9999/config-ops/weighting/list.html');">
										�鿴configserver
									</button>
								</td>
							</tr>
						</table>
					</form>

					<table>
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
								<td colspan="2" align="center">
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