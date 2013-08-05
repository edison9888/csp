<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.KeyPo"%>
<%@page import="com.taobao.monitor.common.util.Constants"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<% request.setAttribute("title","ѹ������б�"); %>
<jsp:include page="../header.jsp"></jsp:include>
 
<script type="text/javascript">
$(function(){
	$("#myTable tr td").mouseover(function(){
		$(this).parent().children("td").addClass("report_on");
	})
	$("#myTable tr td").mouseout(function(){
		$(this).parent().children("td").removeClass("report_on");
	})

	$(".manualControl").click(function(){
		return confirm("ȷ����ʼ�ֶ�ѹ��?");
	});
	$(".autoControl").click(function(){
		return confirm("ȷ����ʼ�Զ�ѹ��?");
	});
})
</script>
	 
		<div class="container">
			<div id="bd" class="resource" style="width: 1050px;">
				<div style="width: 1050px; height: auto; margin: 0 auto">
					<table id="myTable" align="center">
						<tr>
							<td align="center" width="80">
								Ӧ����
							</td>
							<td align="center" width="100">
								ѹ��Ŀ��
							</td>
							<td align="center" width="100">
								ѹ������
							</td>
							<td align="center" width="100">
								�Ƿ��Զ�
							</td>
							<td align="center" width="280">
								ѹ��ʱ��
							</td>
							<td align="center" width="180">
								������
							</td>
							<td align="center" width="260">
								����
							</td>
						</tr>
						<c:forEach items="${loadconfigList}" var="apploadconfig">
							<tr onMouseOver="this.bgColor='#BCE774'"
								onMouseOut="this.bgColor='#FFFFFF'">
								<td align="center" width="150">
									${apploadconfig.appName}
								</td>
								<td align="center" width="30">
									${apploadconfig.hostIp}
								</td>
								<td align="center" width="20">
									${apploadconfig.loadType}
								</td>
								<td align="center" width="10">
									<c:choose>
										<c:when test="${apploadconfig.loadAuto == 1}">��</c:when>
										<c:when test="${apploadconfig.loadAuto == 0}">��</c:when>
									</c:choose>
								</td>
								<td align="center" width="250">
									${apploadconfig.startTime}
								</td>
								<td align="center" width="350">
									${apploadconfig.wangwangs}
								</td>
								<td width="230" align="center">
									<a class="manualControl"
										href="<%=request.getContextPath() %>/loadrun/control.do?method=manuals&appId=${apploadconfig.appId}">�ֶ�����</a>&nbsp;&nbsp;&nbsp;
									<a class="autoControl"
										href="<%=request.getContextPath() %>/loadrun/control.do?method=auto&appId=${apploadconfig.appId}">�Զ�����</a>
								</td>
							</tr>
						</c:forEach>
					</table>
				</div>
			</div>
		<jsp:include page="../footer.jsp"></jsp:include>
