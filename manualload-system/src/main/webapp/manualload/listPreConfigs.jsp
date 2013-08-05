<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","压测配置"); %>
<jsp:include page="./header.jsp" />
<script type="text/javascript">
  var xmlhttp; //全局使用 
  var data;
  function createXmlHttp() {
    //其他浏览器创建XMLHttpRequest对象的代码  
    if (window.XMLHttpRequest) {
      xmlhttp = new XMLHttpRequest();
      //避免Mozilla早些版本会有的bug  
      if (xmlhttp.overrideMimeType) {
        xmlhttp.overrideMimeType("text/xml");
      }
    }
    //IE6 IE5 IE5.5创建XMLHttpRequest对象的代码  
    else if (window.ActiveXObject) {
      var activexName = ["MSXML2.XMLHTTP", "Microsoft.XMLHTTP"];
      for (var i = 0; i < activexName.length; i++) {
        try {
          xmlhttp = new ActiveXObject(activexName[i]);
          break;
        } catch(ex) {}
      }
    }
    return xmlhttp;
  }

  function synchronousMac(appId) {
    var url = "${baseUrl}/synMacchineInfo.do?appId=" + appId;
    var partxmlhttp = createXmlHttp();
    partxmlhttp.open("POST", url, false);
    partxmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    partxmlhttp.send(null);
    var result;
    if (partxmlhttp.readyState == 4) {
      if (partxmlhttp.status == 200) {
        var returnStr = partxmlhttp.responseText;
        if (returnStr != null && returnStr != '') {
          alert(returnStr);
        }
      }
    }
  }
  function delApp(val) {
    if (confirm('确认删除选择的应用吗?')) {
      var url = "${baseUrl}/delOneAppById.do?appId=" + val;
      window.location.href = url;
    }
  }
</script>
<div class="container">
	<div id="bd" class="resource">
		<div style="width: 990px; height: auto; margin: 0 auto">
			<table>
				<tbody>
					<tr>
						<td align="center">
							应用id
						</td>
						<td align="center">
							应用名称
						</td>
						<td align="center">
							压测人
						</td>
						<td align="center">
							压测类别
						</td>
						<td align="center">
							压测类型
						</td>
						<td align="center">
							压测端口
						</td>
						<td align="center">
							创建时间
						</td>
						<td align="center">
							操作
						</td>
					</tr>
					<c:forEach items="${listMac}" var="exp" varStatus="index">
					    <tr onMouseOver="this.bgColor='#BCE774'"
								onMouseOut="this.bgColor='#FFFFFF'">
								<td align="center">
									${exp.id}
									<br>
								</td>
								<td>
									&nbsp;&nbsp;
									<span title="${exp.appName}">${exp.appName}</span>
									<br>
								</td>
								<td align="left">
									<span title="${exp.userName}">${exp.userName}</span>
									<br>
								</td>
								<td align="center">
								   <c:choose>
										<c:when test="${exp.preKinds == 'all'}">全部压测</c:when>
										<c:when test="${exp.preKinds == 'half'}">压测一半</c:when>
									</c:choose>
								</td>
								<td align="center">
									httpload
									<br>
								</td>
								<td align="center">
									<span title="${exp.prePort}">${exp.prePort}</span>
									<br>
								</td>
								<td align="center">
									&nbsp;${exp.createTime}
								</td>
								<td align="center">
									<a id="update"
										href="${baseUrl}/getOneAppConfigById.do?appId=${exp.id}">修改</a>
									<a id="delete"
										href="javascript:delApp('${exp.id}');">删除</a>
									<a id="delete"
										href="javascript:synchronousMac('${exp.id}');">同步机器</a>
								</td>
							</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<jsp:include page="./footer.jsp" />
