<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","修改压测配置"); %>
<jsp:include page="./header.jsp" />
<div class="container">
	<div id="bd" class="resource">
		<div class="alert-message block-message success">
			<button type="submit" class="btn primary"
						onClick="document.location='${baseUrl}/getAllPreConfigs.do'">返回列表</button>
		</div>
		<div style="width: 990px; height: auto; margin: 0 auto">
			<form action="${baseUrl}/updateOneAppConfig.do" method="post">
				<table align="center">
					<tr>
						<td  >
							应用名称：
							<input type="text" name="appConfig.appName" readonly="readonly"
								value="${appConfig.appName}" style="height: 25px;" />
						</td>
					</tr>
					<tr>
						<td >
							&nbsp;&nbsp;&nbsp;压测人：
							<input type="text" name="appConfig.userName" readonly="readonly"
								style="height: 25px;" value="${appConfig.userName}" />
						</td>
					</tr>
					<tr>
						<td  >
							压测类别：
							<s:if test='appConfig.preKinds == "all"'>
								<input type="radio" name="appConfig.preKinds"
									style="height: 25px;" value="all" checked="checked" />全部 
				                <input type="radio" name="appConfig.preKinds" style="height: 25px;"
									value="half" />一半 
				            </s:if>
							<s:else>
								<input type="radio" name="appConfig.preKinds"
									style="height: 25px;" value="all" /> 全部
				                <input type="radio" name="appConfig.preKinds" style="height: 25px;"
									value="half" checked="checked" /> 一半
				           </s:else>
						</td>
					</tr>
					<tr>
						<td  >
							压测端口：
							<input type="text" name="appConfig.prePort" style="height: 25px;"
								value="${appConfig.prePort}" />
						<s:if test="gobleString != ''">	
								<span id="port" style="color: red;">${gobleString}</span>
						</s:if>
						</td>
					</tr>
					<tr>
						<td   style="border-bottom:#ddd 1px solid;">
							创建时间：
							<input type="text" readonly="readonly" style="height: 25px;"
								name="appConfig.createTime" value="${appConfig.createTime}" />
							<input type="hidden" readonly="readonly" name="appConfig.preType"
								value="${appConfig.preType}" />
							<input type="hidden" readonly="readonly" name="appConfig.id"
								value="${appConfig.id}" />
							<input type="hidden" readonly="readonly"
								name="appConfig.userPass" value="${appConfig.userPass}" />
							<input type="hidden" readonly="readonly" name="appConfig.preWay"
								value="${appConfig.preWay}" />
						</td>
					</tr>
				</table>
		</div>
		<div class="alert-message block-message success">
			<button type="submit" class="btn primary">
				提交
			</button>
			<button type="reset" class="btn primary">
				清空
			</button>
		</div>
		</form>
	</div>
</div>
</div>
<jsp:include page="./footer.jsp" />