<%@ page language="java" contentType="text/html; charset=GB18030"
	pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","nginxProxy压测控制"); %>
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
								压测限制
							</td>
							<td>
								${loadConfig.limitFeature}
							</td>
						</tr>
						<tr>
							<td>
								proxy路径
							</td>
							<td>
								${loadConfig.jkConfigPath}
							</td>
						</tr>
						<tr>
							<td>
								nginx启动路径
							</td>
							<td>
								${loadConfig.apachectlPath}
							</td>
						</tr>
						<tr>
							<td>
								用户名
							</td>
							<td>
								${loadConfig.userName}
							</td>
						</tr>
					</table>

					<table id="myTable">
						<tr>
							<td>
								分流机器IP
							</td>
						</tr>
						<tr>
							<td>
								<c:forEach items="${currentTask.controls}" var="item">
									<c:out value="${item.key}" />；  
	                            </c:forEach>
							</td>
						</tr>
					</table>

					<table id="myTable">
						<tr>
							<td>
								预设IP
							</td>
						</tr>
						<tr>
							<td>
								<c:forEach items="${ipList}" var="ip">
									<c:out value="${ip}" />； 
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
									IP地址:
									<input type="text" style="width: 480px;" value=""
										name="targetip" />
									&nbsp;&nbsp;
									<button class="btn info" type="submit">
										提交分流
									</button>
								</td>
							</tr>
						</table>
					</form>
					<table id="myTable">
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
								<td colspan="2" align="center">
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
  function queryLoadValue() {
    $.getJSON("<%=request.getContextPath() %>/loadrun/control.do?method=showValue&appId=${loadConfig.appId}", {},
    function(json) {
      $("#loadrunResultTable").empty();
      for (key in json) {
        $("#loadrunResultTable").append("<tr><td>" + key + "</td><td>" + json[key] + "</td></tr>");
      }

    });
    window.setTimeout("queryLoadValue()", 10000);
  }
  queryLoadValue();
</script>
			<jsp:include page="../footer.jsp"></jsp:include>
