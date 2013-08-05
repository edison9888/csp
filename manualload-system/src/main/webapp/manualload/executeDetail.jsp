<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","压测"); %>
<jsp:include page="./header.jsp" />
<script type="text/javascript">
		 function saveInfo() {
          var reqsec = document.getElementById("reqsec").value;
          var reqinc = document.getElementById("reqinc").value;
          var cycleTotle = document.getElementById("cycleTotle").value;
          var reqTotle = (cycleTotle * reqinc);
          if (reqsec > (1000 - reqTotle)) {
            alert("请求数不能超过1000个");
            document.getElementById("reqsec").value = "";
            document.getElementById("reqinc").value = "";
            document.getElementById("cycleTotle").value = "";
            return false;
           }
           return true;
          }
</script>
<div class="container">
	<div id="bd" class="resource">
		<div style="width: 990px; height: auto; margin: 0 auto">
			<img src="${baseUrl}/images/logo.png" width="497" height="60"
				style="margin-top: -20px;" />
		</div>
		<div class="alert-message block-message success">
			<button type="submit" class="btn primary"
						onClick="document.location='${baseUrl}/getAllPressures.do'">返回列表</button>
		</div>
		<div style="width: 990px; height: auto; margin: 0 auto">
		<form id="form" method="post" action="${baseUrl}/executePressure.do" >
			<table>
				<tr>
					<td >
						&nbsp;&nbsp;压测时间 :
						<input name="pretime" id="pretime" style="height:25px;"/>
						单位：秒
						<span id="pretimeId" style="color: red;"></span>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;每秒请求 :
						<input name="other" id="reqsec" style="height:25px;"/>
						<span id="reqsecId" style="color: red;"></span>
					</td>
				</tr>
				<tr>
					<td>
						增加请求数:
						<input name="reqinc" id="reqinc" style="height:25px;"/>
						<span id="reqincId" style="color: red;"></span>
					</td>
				</tr>
				<tr>
					<td>
						&nbsp;&nbsp;压测次数 :
						<input name="cycleTotle" id="cycleTotle" style="height:25px;"/>
						<span id="cycleTotleId" style="color: red;"></span>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<input name="appId" id="appId" value="${appId}" type="hidden" />
						<span id="reqestCount" style="color: red;">注：每台压测机器的请求数不能超过1000个。<br />(请求数
							= 增加请求数 * 压测次数 + 每秒请求数)</span>
					</td>
				</tr>
			</table>
			
			<div class="alert-message block-message success">
			<button type="submit" class="btn primary" onclick="return saveInfo()">
				压测
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