<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","httpload压测控制"); %>
<jsp:include page="../header.jsp"></jsp:include>
		<div class="container">
			<div id="bd" class="resource" style="width: 1050px;">
				<div style="width: 1050px; height: auto; margin: 0 auto">
					<table id="myTable">
						<tr>
							<td>
								应用名称
							</td>
							<td>
								<font size="4" color="#000">${loadConfig.appName}</font>
							</td>
						</tr>
						<tr>
							<td>
								目标机器
							</td>
							<td>
								${loadConfig.hostIp}
							</td>
						</tr>
						<tr>
							<td>
								负责人
							</td>
							<td>
								${loadConfig.wangwangs}
							</td>
						</tr>
						<tr>
							<td>
								个人账户：
							</td>
							<td>
								${loadConfig.userName }
							</td>
						</tr>
						<tr>
							<td>
								压测限制
							</td>
							<td>
								${loadConfig.limitFeature}
							</td>
						</tr>
						<tr>
							<td>
								流量高峰期时间
							</td>
							<td>
								${loadConfig.appRushHours}
							</td>
						</tr>
						<tr>
							<td>
								http_load路径
							</td>
							<td>
								${loadConfig.httploadpath}
							</td>
						</tr>
					</table>
				</div>
				<div style="width: 1050px; height: auto; margin: 0 auto">
					<form
						action="<%=request.getContextPath() %>/loadrun/control.do?method=doControl"
						method="post">
						<table id="myTable">
							<tr>
								<td>
									当前用户数量
								</td>
								<td>
									操作
								</td>
							</tr>
							<tr>
								<td>
									${currentTask.curParallel}
									<input type="hidden" name="appId" value="${loadConfig.appId}" />
								</td>
								<td>
									<c:choose>
										<c:when test="${currentTask.doload == false}">
											<input type="text" value="${currentTask.curParallel}"
												id="parallel" name="parallel" />
											<button class="btn info" type="submit">
												确认提交
											</button>说明:填入需要并发的用户数量
			                            </c:when>
										<c:when test="${currentTask.doload == true}">正在执行中...</c:when>
									</c:choose>
									<c:if test="${loadMsg != null}">
										<font color="red">${loadMsg}</font>
									</c:if>
								</td>
							</tr>
						</table>
					</form>
					<table>
						<tr>
							<td>
								数据key
							</td>
							<td>
								最近数据value
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
								<td>
									<button class="btn info" type="submit">
										关闭压测任务
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