<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","压测结果信息"); %>
<jsp:include page="../header.jsp"></jsp:include>
<script type="text/javascript">
 $(function(){
	$(".deleteAction").click(function(){
		return confirm("是否要删除压测结果.");
	});
})
</script>
		<div class="container">
			<div id="bd" class="resource" style="width: 1050px;">
				<div class="alert-message block-message success">
					<form action="<%=request.getContextPath() %>/loadrun/show.do?method=show"
						method="post" name="appForm">
									<input type="text" style="width:150px;" readonly="readonly" value="${loadconfig.appName}"/>
									<input type="hidden" value="${loadconfig.appId}" name="appId">
									<input type="text" style="width:100px;" class="Wdate" value="${collectTime}"
										name="collectTime"
										onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
									<button type="submit" class="btn primary">
										确定
									</button>
					</form>
				</div>
				</div>
				<div style="width: 1050px; height: auto; margin: 0 auto">
					<c:forEach items="${loadrunMap}" var="loadrun">
						<table id="myTable">
							<% boolean print = true; %>
							<tr class="ui-widget-header ">
								<td align="center">
									顺序
								</td>
								<td align="center">
									${loadmessage}
								</td>
								<c:forEach items="${keys}" var="key">
									<td width="100" align="center">
										${key.name }
									</td>
								</c:forEach>
								<td width="100" align="center">
									压测时间
								</td>
							</tr>
							<c:forEach items="${sortList}" var="sort">
								<tr onMouseOver="this.bgColor='#BCE774'"
									onMouseOut="this.bgColor='#FFFFFF'">
									<td width="100" align="center">
										${sort}
									</td>
									<td width="100" align="center">
										${loadControlMap[loadrun.key][sort]}
									</td>
									<c:forEach items="${keys}" var="key">
										<td align="center">
											${loadrun.value[sort][key].value}
										</td>
									</c:forEach>
									<td width="100" align="center">
										${runTime[loadrun.key][sort]}
									</td>
								</tr>
							</c:forEach>
							<tr>
								<td colspan="100">压测时阀值设置：${limitFeature[loadrun.key]}
									&nbsp;&nbsp;
									<a href="${pageContext.request.contextPath}/loadrun/config.do?method=detail&appId=${appId}" target="_blank">查看配置</a>&nbsp;
									<a href="${pageContext.request.contextPath}/loadrun/show.do?method=showDetail&id=${loadrun.key}" target="_blank">压测过程记录</a>&nbsp;
									<a href="${pageContext.request.contextPath}/loadrun/show.do?method=showReport&id=${loadrun.key}" target="_blank">压测报表</a>&nbsp;
									<a class="deleteAction" href="${pageContext.request.contextPath}/loadrun/manage.do?method=deleteResult&resultId=${loadrun.key}&appId=${appId}&collectTime=${searchDate}">删除结果</a>
								</td>
		
							</tr>
						</table>
					</c:forEach>
				</div>
				<div style="width: 1050px; height: auto; margin: 0 auto">
					<table>
						<tr>
							<td width="100%" valign="top">
								<table width="100%" border="1"
									class="ui-widget ui-widget-content">
									<tr>
										<td id="loadchartdiv"></td>
									</tr>
								</table>
							</td>
						</tr>
					</table>
				</div>
<script type="text/javascript">
var so3 = new SWFObject("<%=request.getContextPath() %>/statics/amline/amline.swf", "amline_load", "100%", "300", "6", "#FFFFFF");
so3.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
so3.addVariable("chart_id", "amline3");   
so3.addVariable("settings_file", "<%=request.getContextPath() %>/statics/amline/loadrun.xml");
so3.addVariable("data_file", escape('${pageContext.request.contextPath}/loadrun/show.do?method=qps&appId=${loadconfig.appId}'));
so3.addVariable("preloader_color", "#999999");
so3.addParam("wmode", "opaque");
so3.write("loadchartdiv");
</script>
			<jsp:include page="../footer.jsp"></jsp:include>