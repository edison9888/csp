<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","压测参数配置"); %>
<jsp:include page="./header.jsp" />
<script type="text/javascript">
 
  function clearData() {
        document.getElementById("pretime").value="";
        document.getElementById("pretimeId").innerHTML = "";
        document.getElementById("reqsec").value="";
        document.getElementById("reqsecId").innerHTML = "";
  }
  
</script>
<div class="container">
	<div id="bd" class="resource">
		<div class="alert-message block-message success">
			<button type="submit" class="btn primary"
						onClick="document.location='${baseUrl}/getAllPressures.do'">返回列表</button>
		</div>
		<div style="width: 990px; height: auto; margin: 0 auto">
		<form id="form" method="post" action="${baseUrl}/executePressure.do" >
			<table>
				<tr>
					<td >
						&nbsp;&nbsp;压测时间（秒） :
						<input name="appConfig.exeTime" id="pretime" style="height:25px;" value="${appConfig.exeTime}" placeholder="压测时间只能是数字而且不能为空"/>
						<s:if test="appname != ''">
						<span id="pretimeId" style="color: red;">${appname}</span>
						</s:if>
					</td>
				</tr>
				<tr>
					<td>
						 &nbsp;&nbsp;&nbsp;每&nbsp;&nbsp;&nbsp;秒&nbsp;&nbsp;&nbsp;请&nbsp;&nbsp;&nbsp;求 :
						<input name="appConfig.reqTotle" id="reqsec" style="height:25px;" value="${appConfig.reqTotle}"  placeholder="请求数只能是数字而且不能为空"/>
						<s:if test="userPass != ''">
						<span id="reqsecId" style="color: red;">${userPass}</span>
						</s:if>
					</td>
				</tr>
				<tr>
					<td colspan="2"  style="border-bottom:#ddd 1px solid;">
						<input name="appId" id="appId" value="${appId}" type="hidden" />
						<span id="reqestCount" style="color: red;">注：每台压测机器的请求数不能超过1000个。 </span>
					</td>
				</tr>
			</table>
			
			<div class="alert-message block-message success">
			<button type="submit" class="btn primary"  >
				压测
			</button>
			<button type="button" class="btn primary"  onclick="clearData();"> 
				清空
			</button>
		</div>
		</form>
	</div>
	</div>
	</div>
	<jsp:include page="./footer.jsp" />