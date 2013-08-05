<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","压测应用"); %>
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

  function runAutoReady(appId) {
    var url = "${baseUrl}/autoReady.do?appId=" + appId;
    window.open(url);
  }
  function updateReady(appId) {
    var url = "${baseUrl}/manualload/getAllAppMachineByAppId.do?appId=" + appId;
    window.open(url);
  }

  function generatePressUrl(appId) {
    var url = "${baseUrl}/generatePressUrl.do?appId=" + appId;
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
</script>
<div class="container">
	<div id="bd" class="resource">
		<div style="width: 990px; height: auto; margin: 0 auto">
			<table>
				<thead>
					<tr>
						<td colspan="6" align="right">
							<span style="font-size:14px;color:red;">目前系统只支持单次单个应用压测</span>
						</td>

					</tr>
				</thead>

				<tbody>
					<tr>
						<td align="center">
							应用id
						</td>
						<td align="center">
							应用名称
						</td>
						<td align="center">
							压测类别
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
								<td class="table2_td" align="center" >
									${exp.id}
									<br>
								</td>
								<td class="table2_td" >
									<span title="${exp.appName}">${exp.appName}</span>
									<br>
								</td>
								<td class="table2_td" align="center" >
								  <c:choose>
										<c:when test="${exp.preKinds == 'all'}">全部压测</c:when>
										<c:when test="${exp.preKinds == 'half'}">压测一半</c:when>
									</c:choose>
								</td>
								<td class="table2_td" align="center" >
									<span title="${exp.prePort}">${exp.prePort}</span>
									<br>
								</td>
								<td class="table2_td" align="center" >
									&nbsp;${exp.createTime}
								</td>
								<td class="table2_td" align="center" >
									<a href="javascript:updateReady('${exp.id}');">修改压测机器</a>
									<a href="javascript:generatePressUrl('${exp.id}');">生成压测url</a>
									<a href="javascript:runAutoReady('${exp.id}');">全量压测</a>
								</td>
							</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</div>
</div>
<jsp:include page="./footer.jsp" />