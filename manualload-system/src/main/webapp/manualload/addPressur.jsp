<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<% request.setAttribute("title","新增压测"); %>
<jsp:include page="./header.jsp" />
<script type="text/javascript">
var xmlhttp; //全局使用 
var data;
/**
 *  创建一个XmlHttp对象，并且返回
 *  作者：wb-tangjinge
 *  时间：2011-11-29
 */
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
/**  
 *  功能：效验应用名
 *  作者：wb-tangjinge
 *  时间：2011-11-29
 */
function checkAppName() {
        var appname = document.getElementById("appname").value;
        var url = "${baseUrl}/checkAppName.do?appname=" + appname;
        var partxmlhttp = createXmlHttp();
        partxmlhttp.open("POST", url, false);
        partxmlhttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
        partxmlhttp.send(null);
        var result;
        if (partxmlhttp.readyState == 4) {
            if (partxmlhttp.status == 200) {
                var returnStr = partxmlhttp.responseText;
                if (returnStr != null && returnStr != '') {
                    document.getElementById("app").innerHTML = returnStr;
                }
            }
        }
}
// 回车键提交数据
function subCheck() {
    if (event.keyCode == 13) {
        return true;
    } else {
        return false;
    }
}
</script>
<div class="container">
	<div id="bd" class="resource">
		<div style="width: 990px; height: auto; margin: 0 auto">
			<form method="post" action="${baseUrl}/saveOnePreConfig.do">
				<table>
					<tbody>
						<tr>
							<td>
								应用名称：
								<input name="appConfig.appName" id="appname" size="30"
									type="text" style="height:25px;" value="${appConfig.appName}"/>
								<s:if test="appname != ''">
                                 <span id="app" style="color: red;">${appname}</span>
                                </s:if>
							</td>
						</tr>
						<tr>
						    <s:if test="appConfig.preKinds != ''">	
							<td>
								压测类别：
								<input id="all"  name="appConfig.preKinds"  size="30" type="radio"  
								<s:if test="appConfig.preKinds == 'all'">	
								 checked="checked" 
								</s:if>	
									value="all"/>
								全部
								<input id="half"  name="appConfig.preKinds"  size="30" 
								<s:if test="appConfig.preKinds == 'half'">checked="checked" </s:if>	
								type="radio" value="half" />
								一半
							</td>
							</s:if>
							<s:else>
							   <td>
								压测类别：
								<input id="all"  name="appConfig.preKinds"  size="30" type="radio"  
								 checked="checked" 
									value="all"/>
								全部
								<input id="half"  name="appConfig.preKinds"  size="30" 
								type="radio" value="half" />
								一半
							</td>
							</s:else>	
						</tr>
						<tr>
							<td>
								压测方式：
								手动
								<input name="appConfig.preWay" size="30" type="hidden"  value="0"/>
							</td>
						</tr>
						<tr>
							<td>
								压测类型：
								httpload
								<input name="appConfig.preType" size="30" type="hidden" value="httpload"/>
							</td>
						</tr>

						<tr>
							<td>
								压测端口：
								<input name="appConfig.port" id="prePort"  style="height:25px;" size="30"
									type="text" value="7001" />
								<s:if test="gobleString != ''">	
								<span id="port" style="color: red;">${gobleString}</span>
								</s:if>
							</td>
						</tr>
						<tr>
							<td>
								&nbsp;&nbsp;&nbsp;压测人：
								<input name="appConfig.userName" id="userName" readonly="readonly" style="height:25px;" size="30"
									type="text" value="${manualCurUser}"/>
								<span id="uname" style="color: red;"></span>
							</td>
						</tr>
						<tr>
							<td style="border-bottom:#ddd 1px solid;">
								压测密码：
								<input name="appConfig.userPass" id="userPass" style="height:25px;" size="30"
									type="password" value="${appConfig.userPass }"/>
							    <s:if test="userPass != ''">
                                 <span id="PassId" style="color: red;">${userPass}</span>
                                </s:if>
							</td>
						</tr>
	                    <tr>
							<td style="border-bottom:#ddd 1px solid;color: red;">
								&nbsp;&nbsp;&nbsp;说明：新增的应用必须是CSP系统中自动压测系统的应用而且应用名要一致。
								<a style="font-weight: bolder;" href="http://autoload.taobao.net:9999/autoload/loadrun/show.do?method=list" 
								target="_blank">查看CSP应用</a>
							</td>
						</tr>
					</tbody>
				</table>
		<div class="alert-message block-message success">
			<button type="submit" class="btn primary">
				提交
			</button>
			<button type="reset" class="btn primary">
				清空
			</button>
			<button type="button" class="btn primary" onclick="checkAppName();">
				检查应用名
			</button>
		</div>
		</form>
	</div>
</div>
</div>
<jsp:include page="./footer.jsp" />

