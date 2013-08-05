<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<% request.setAttribute("title","查看压测配置"); %>
		<jsp:include page="../header.jsp"></jsp:include>
		<div class="container">
			<div id="bd" class="resource" style="width: 1050px;">
				<form
					action="<%=request.getContextPath() %>/loadrun/config.do?method=update"
					method="post">
					<div class="alert-message block-message success">
						<button type="button" class="btn primary" onclick="window.close()">
							关闭
						</button>
						<button type="button" class="btn primary" onclick="check()">
							检查IP
						</button>
					</div>
					<table id="myTable" style="width: 1050px;">
						<tr>
							<td width="180">
								压测应用
							</td>
							<td width="700">
								<font size="4" color="#000">${loadconfig.appName}</font>
							</td>
						</tr>
						<tr>
							<td>
								压测目标机器IP：
							</td>
							<td>
								${loadconfig.hostIp }
							</td>
						</tr>
						<tr>
							<td colspan="2">
							<font color="red"><span id="checkApp">说明:点击下方的检查IP按钮，可校验IP与应用是否匹配</span></td>
							</td>
						</tr>
						<tr>
							<td>
								压测时间:
							</td>
							<td>
								${loadconfig.startTime }
							</td>
						</tr>
						<tr>
							<td>
								是否自动压测
							</td>
							<td>
								<c:choose>
									<c:when test="${loadconfig.loadAuto == 1}">是</c:when>
									<c:when test="${loadconfig.loadAuto == 0}">否</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td>
								压测类型:
							</td>
							<td>
								${loadconfig.loadType}
							</td>
						</tr>
						<tr>
							<td>
								压测模式:
							</td>
							<td>
								${loadconfig.loadrunMode}
							</td>
						</tr>
						<tr>
							<td>
								压测信息接收（旺旺）:
							</td>
							<td>
								${loadconfig.wangwangs }
							</td>
						</tr>
						<tr>
							<td>
								压测限制设置：
							</td>
							<td>
								${loadconfig.limitFeature }
							</td>
						</tr>
						<tr>
							<td>
								自动压测设置：
							</td>
							<td width="200">
								<c:forTokens items="${loadconfig.loadFeature}" delims=","
									var="load" varStatus="dex">
									<c:out value="${load}"/>, 
			                 </c:forTokens>
							</td>
						</tr>
						<tr>
							<td>
								配置额外说明：
							</td>
							<td>
								<c:forTokens items="${loadconfig.configFeature }" delims=","
									var="tech">
									<c:out value="${tech}" /><br/> 
			                 </c:forTokens>
							</td>
						</tr>
						<tr>
							<td>
								个人账户：
							</td>
							<td>
								${loadconfig.userName }
							</td>
						</tr>
						<tr>
							<td>
								个人密码：
							</td>
							<td>
								*******
							</td>
						</tr>
						<tr>
							<td>
								压测开始执行url：
							</td>
							<td>
								${loadconfig.startUrl }
							</td>
						</tr>
						<tr>
							<td>
								压测结束执行url：
							</td>
							<td>
								${loadconfig.endUrl }
							</td>
						</tr>
						<tr>
							<td>
								Hsf的qps计算是否通过采集eagleeye:
							</td>
							<td>
								<c:choose>
									<c:when test="${loadconfig.collectEagleeye == 1}">是</c:when>
									<c:when test="${loadconfig.collectEagleeye == 0}">否</c:when>
								</c:choose>
							</td>
						</tr>
					</table>
					<div id="httpload_set">
						<table cellpadding="0" cellspacing="0" border="1" id="myTable"
							class="tablesorter" style="width: 99%; align: center">
							<tr>
								<td colspan="2">
									日志类型：
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									${loadconfig.httploadlogtype.logDesc}
									<textarea rows="5" cols="100" name="httploadloglog"
										id="httploadloglog">${loadconfig.httploadloglog}</textarea>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									httpload压测方式：
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<c:if test="${loadconfig.httploadProxy == 'no'}">修改原始url为ip:7001压jboss</c:if>
									<c:if test="${loadconfig.httploadProxy == 'proxy'}">保留原始url压jboss</c:if>
									<c:if test="${loadconfig.httploadProxy == 'directProxy'}">直接压apache(nginx)</c:if>
									<c:if test="${loadconfig.httploadProxy == 'ssl'}">ssl方式直接压apache(nginx)</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									httpload路径：
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									${loadconfig.httploadpath }
								</td>
							</tr>
						</table>
					</div>
					<div id="apache_set">
						<table id="myTable" style="width: 1050px;">
							<tr>
								<td colspan="2">
									<span id="configText"></span>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									${loadconfig.jkConfigPath }
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span id="runText"></span>
									<c:choose>
										<c:when test="${loadconfig.loadType == 'apache'}">
                                             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                             &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                         </c:when>
										<c:otherwise>
                                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                         &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                         </c:otherwise>
									</c:choose>
									${loadconfig.apachectlPath }
								</td>
							</tr>
						</table>
					</div>
					<div id="script_set">
						<table id="myTable">
							<tr>
								<td colspan="2">
									<span id="start">启动脚本：</span>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									${loadconfig.startScript }
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span id="end">停止脚本：</span>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									${loadconfig.endScript }
								</td>
							</tr>
						</table>
					</div>
				</form>
			</div>
<script type="text/javascript">
  $(function() {
    $("#tabs").tabs();
  });
  function httploadselfLog() {
    var type = '${loadconfig.httploadlogtype}';
    if ("self" == type || "scriptApache" == type) {
      $("#httploadloglog").show();
    } else {
      $("#httploadloglog").hide();
    }
  }
  function showDivByType() {
    var type = "${loadconfig.loadType}";
    var mode = "${loadconfig.loadrunMode}";
    if (type == 'apache' && mode == "SSH") { //分流就显示
      $("#script_set").hide();
      $("#apache_set").show();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("填写分流比例 4_1,4_2,4_3,5_9 。前面表示本地流量，后面表示分流流量");
      $("#loadFeatureDesc").text("说明:apache 存放ip1,ip2,ip3...");
      $("#configText").text("jk配置文件路径： ");
      $("#runText").text("apache启动路径： ");
    } else if (type == 'hsf' && mode == "SSH") {
      $("#script_set").hide();
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("hsf类型的填写接口信息短号分割,只要配置主要接口,请到这里查询http://config.online.taobao.net/config-ops-online/weighting/list.html?hostId=");
      $("#loadFeatureDesc").text("说明:hsf 存放ip倍数 1,2,3,4,5...");
    } else if (type == 'httpLoad' && mode == "SSH") {
      $("#script_set").hide();
      $("#apache_set").hide();
      $("#httpload_set").show();
      $("#configFeatureDesc").text("填写压测时间 单位 秒 默认60 ");
      $("#loadFeatureDesc").text("说明:httpload:压测用户数数5,10,15...");
    } else if (type == 'apacheProxy' && mode == "SSH") {
      $("#script_set").hide();
      $("#apache_set").show();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("无需填写");
      $("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
      $("#configText").text("proxy配置文件路径： ");
      $("#runText").text("apache启动路径： ");
    } else if (type == 'nginxProxy' && mode == "SSH") {
      $("#script_set").hide();
      $("#apache_set").show();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("无需填写");
      $("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
      $("#configText").text("proxy配置文件路径： ");
      $("#runText").text("nginx启动路径： ");
    } else if (type == 'apache' && mode == 'SCRIPT') { //分流就显示
      $("#script_set").show();
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("填写分流比例 4_1,4_2,4_3,5_9 。前面表示本地流量，后面表示分流流量");
      $("#loadFeatureDesc").text("说明:apache 存放ip1,ip2,ip3...");
      $("#configText").text("jk配置文件路径： ");
      $("#runText").text("apache启动路径： ");
      $("#apachectlPath").val("/home/admin/cai/bin/apachectl");
    } else if (type == 'hsf' && mode == 'SCRIPT') {
      $("#script_set").show();
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("hsf类型的填写接口信息短号分割,只要配置主要接口,请到这里查询http://ops.jm.taobao.org:9999/config-ops/pub_mgr.html");
      $("#loadFeatureDesc").text("说明:hsf 存放ip倍数 1,2,3,4,5...");
      $("#apachectlPath").val("");
    } else if (type == 'httpLoad' && mode == 'SCRIPT') {
      $("#script_set").hide();
      $("#apache_set").hide();
      $("#httpload_set").show();
      $("#configFeatureDesc").text("填写压测时间 单位 秒 默认60 ");
      $("#loadFeatureDesc").text("说明:httpload:压测用户数数5,10,15...");
      $("#apachectlPath").val("");
    } else if (type == 'apacheProxy' && mode == 'SCRIPT') {
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("B2b的SCRIPT方式为必填项，格式:80:2_80:4,80:2_80:5（端口号:权重)");
      $("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
      $("#configText").text("proxy配置文件路径： ");
      $("#runText").text("apache启动路径： ");
      $("#apachectlPath").val("/home/admin/cai/bin/apachectl");
    } else if (type == 'nginxProxy' && mode == 'SCRIPT') {
      $("#script_set").show();
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("无需填写");
      $("#loadFeatureDesc").text("存放被代理的服务器ip1,ip2,ip3...");
      $("#configText").text("proxy配置文件路径： ");
      $("#runText").text("nginx启动路径： ");
      $("#apachectlPath").val("/home/admin/cai/bin/nginx-proxy");
    }
  }
  showDivByType();
  httploadselfLog();

  function check() {
    var appName = '${loadconfig.appName}';
    var target = '${loadconfig.hostIp}';
    var loadType = '${loadconfig.loadType}';
    var loadFuture = '${loadconfig.loadFeature}'
    var method = "check";
    var urlDest = "config.do";
    
     $.ajax({
      url: urlDest,
      async: true,
      contentType: "application/json",
      cache: false,
      data:{'method':method,'appName':appName,'target':target,'loadType':loadType,'loadFuture':loadFuture},
      success: function(data) {
        $('#checkApp').text(data);
      }
    });
  }
</script>
		<jsp:include page="../footer.jsp"></jsp:include>