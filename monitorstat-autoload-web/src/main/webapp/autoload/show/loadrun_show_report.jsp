<%@ page language="java" contentType="text/html; charset=gbk" pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<% request.setAttribute("title","压测报表"); %>
<jsp:include page="../header.jsp"></jsp:include>
<script type="text/javascript">

</script>

<div class="container">
	<div style="width: 1050px; height: auto; margin: 0 auto">
		<table id="myTable">
			<tr>
				<td>
					<div style="width: 1050px; height: auto; margin: 0 auto">
						<table>
								<tr>
									<td width="50%" valign="top">
										<table width="100%" border="1" class="ui-widget ui-widget-content">
											<tr>
												<td id="loadchartdiv1"></td>
											</tr>
										</table>
									</td>
									<td width="50%" valign="top">
										<table width="100%" border="1" class="ui-widget ui-widget-content">
											<tr>
												<td id="loadchartdiv2"></td>
											</tr>
										</table>
									</td>
								</tr>
								<tr>
									<td width="50%" valign="top">
										<table width="100%" border="1" class="ui-widget ui-widget-content">
											<tr>
												<td id="loadchartdiv3"></td>
											</tr>
										</table>
									</td>
									<td width="50%" valign="top">
										<table width="100%" border="1" class="ui-widget ui-widget-content">
											<tr>
												<td id="loadchartdiv4"></td>
											</tr>
										</table>
									</td>
								</tr>
						</table>
					</div>
				</td>
			</tr>
		</table>
	</div>	
	
	<div style="width: 1050px; height: auto; margin: 0 auto">
		<table>
			<tr>
				<td width="45%"><strong>URL</strong></td>
				<td>调用次数</td>
				<td>平均rt</td>
				<td>占资源系数</td>
				<td>采集点最大rt</td>
				<td>采集点最小rt</td>
				<td>rt波动</td>
			</tr>
			<c:forEach items="${urls}" var="obj">
				<tr onMouseOver="this.bgColor='#BCE774'" onMouseOut="this.bgColor='#FFFFFF'">
					<td width="100">
						<a href="${pageContext.request.contextPath}/loadrun/show.do?method=showReportChart&id=${id}&mkey=URL&skey=${obj.resource}" target="_blank">
						${obj.resource}
						</a>
					</td>
					<td width="100">
						${obj.totalPv}
					</td>
					<td width="100">
						${obj.avgRt}
					</td>
					<td width="100">
						${obj.resourceRatio}
					</td>
					<td width="100">
						${obj.maxRt}
					</td>
					<td width="100">
						${obj.minRt}
					</td>
					<td width="100" align="center">
						${obj.waveRatio}
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>	
	
	<div style="width: 1050px; height: auto; margin: 0 auto">
		<table>
			<tr>
				<td width="45%"><strong>HSF接口Provider</strong></td>
				<td>调用次数</td>
				<td>平均rt</td>
				<td>占资源系数</td>
				<td>最大rt</td>
				<td>最小rt</td>
				<td>rt波动比</td>
			</tr>
			<c:forEach items="${hsfProviders}" var="obj">
				<tr onMouseOver="this.bgColor='#BCE774'" onMouseOut="this.bgColor='#FFFFFF'">
					<td width="100">
						<a href="${pageContext.request.contextPath}/loadrun/show.do?method=showReportChart&id=${id}&mkey=HSF_PROVIDER&skey=${obj.resource}" target="_blank">
						${obj.resource}
						</a>
					</td>
					<td width="100">
						${obj.totalPv}
					</td>
					<td width="100">
						${obj.avgRt}
					</td>
					<td width="100">
						${obj.resourceRatio}
					</td>
					<td width="100">
						${obj.maxRt}
					</td>
					<td width="100">
						${obj.minRt}
					</td>
					<td width="100">
						${obj.waveRatio}
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>		
</div>	
				

<script type="text/javascript">
var so1 = new SWFObject("<%=request.getContextPath() %>/statics/amline/amline.swf", "amline_load", "100%", "300", "6", "#FFFFFF");
so1.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
so1.addVariable("chart_id", "amline3");   
so1.addVariable("settings_file", "<%=request.getContextPath() %>/statics/amline/cpu.xml");
so1.addVariable("data_file", escape('${pageContext.request.contextPath}/loadrun/show.do?method=reportCpuInfo&loadrunId=${id}'));
so1.addVariable("preloader_color", "#999999");
so1.addParam("wmode", "opaque");
so1.write("loadchartdiv1");

var so2 = new SWFObject("<%=request.getContextPath() %>/statics/amline/amline.swf", "amline_load", "100%", "300", "6", "#FFFFFF");
so2.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
so2.addVariable("chart_id", "amline3");   
so2.addVariable("settings_file", "<%=request.getContextPath() %>/statics/amline/load.xml");
so2.addVariable("data_file", escape('${pageContext.request.contextPath}/loadrun/show.do?method=reportLoadInfo&loadrunId=${id}'));
so2.addVariable("preloader_color", "#999999");
so2.addParam("wmode", "opaque");
so2.write("loadchartdiv2");

var so3 = new SWFObject("<%=request.getContextPath() %>/statics/amline/amline.swf", "amline_load", "100%", "300", "6", "#FFFFFF");
so3.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
so3.addVariable("chart_id", "amline3");   
so3.addVariable("settings_file", "<%=request.getContextPath() %>/statics/amline/io.xml");
so3.addVariable("data_file", escape('${pageContext.request.contextPath}/loadrun/show.do?method=reportIoInfo&loadrunId=${id}'));
so3.addVariable("preloader_color", "#999999");
so3.addParam("wmode", "opaque");
so3.write("loadchartdiv3");

var so4 = new SWFObject("<%=request.getContextPath() %>/statics/amline/amline.swf", "amline_load", "100%", "300", "6", "#FFFFFF");
so4.addVariable("path", "<%=request.getContextPath() %>/statics/amline/");
so4.addVariable("chart_id", "amline3");   
so4.addVariable("settings_file", "<%=request.getContextPath() %>/statics/amline/thread.xml");
so4.addVariable("data_file", escape('${pageContext.request.contextPath}/loadrun/show.do?method=reportThreadInfo&loadrunId=${id}'));
so4.addVariable("preloader_color", "#999999");
so4.addParam("wmode", "opaque");
so4.write("loadchartdiv4");
</script>
<jsp:include page="../footer.jsp"></jsp:include>