<%@ page language="java" contentType="text/html; charset=gbk"
	pageEncoding="gbk" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.AppInfoPo"%>
<%@page import="com.taobao.monitor.common.ao.center.AppInfoAo"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<% request.setAttribute("title","�鿴ѹ������"); %>
		<jsp:include page="../header.jsp"></jsp:include>
		<div class="container">
			<div id="bd" class="resource" style="width: 1050px;">
				<form
					action="<%=request.getContextPath() %>/loadrun/config.do?method=update"
					method="post">
					<div class="alert-message block-message success">
						<button type="button" class="btn primary" onclick="window.close()">
							�ر�
						</button>
						<button type="button" class="btn primary" onclick="check()">
							���IP
						</button>
					</div>
					<table id="myTable" style="width: 1050px;">
						<tr>
							<td width="180">
								ѹ��Ӧ��
							</td>
							<td width="700">
								<font size="4" color="#000">${loadconfig.appName}</font>
							</td>
						</tr>
						<tr>
							<td>
								ѹ��Ŀ�����IP��
							</td>
							<td>
								${loadconfig.hostIp }
							</td>
						</tr>
						<tr>
							<td colspan="2">
							<font color="red"><span id="checkApp">˵��:����·��ļ��IP��ť����У��IP��Ӧ���Ƿ�ƥ��</span></td>
							</td>
						</tr>
						<tr>
							<td>
								ѹ��ʱ��:
							</td>
							<td>
								${loadconfig.startTime }
							</td>
						</tr>
						<tr>
							<td>
								�Ƿ��Զ�ѹ��
							</td>
							<td>
								<c:choose>
									<c:when test="${loadconfig.loadAuto == 1}">��</c:when>
									<c:when test="${loadconfig.loadAuto == 0}">��</c:when>
								</c:choose>
							</td>
						</tr>
						<tr>
							<td>
								ѹ������:
							</td>
							<td>
								${loadconfig.loadType}
							</td>
						</tr>
						<tr>
							<td>
								ѹ��ģʽ:
							</td>
							<td>
								${loadconfig.loadrunMode}
							</td>
						</tr>
						<tr>
							<td>
								ѹ����Ϣ���գ�������:
							</td>
							<td>
								${loadconfig.wangwangs }
							</td>
						</tr>
						<tr>
							<td>
								ѹ���������ã�
							</td>
							<td>
								${loadconfig.limitFeature }
							</td>
						</tr>
						<tr>
							<td>
								�Զ�ѹ�����ã�
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
								���ö���˵����
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
								�����˻���
							</td>
							<td>
								${loadconfig.userName }
							</td>
						</tr>
						<tr>
							<td>
								�������룺
							</td>
							<td>
								*******
							</td>
						</tr>
						<tr>
							<td>
								ѹ�⿪ʼִ��url��
							</td>
							<td>
								${loadconfig.startUrl }
							</td>
						</tr>
						<tr>
							<td>
								ѹ�����ִ��url��
							</td>
							<td>
								${loadconfig.endUrl }
							</td>
						</tr>
						<tr>
							<td>
								Hsf��qps�����Ƿ�ͨ���ɼ�eagleeye:
							</td>
							<td>
								<c:choose>
									<c:when test="${loadconfig.collectEagleeye == 1}">��</c:when>
									<c:when test="${loadconfig.collectEagleeye == 0}">��</c:when>
								</c:choose>
							</td>
						</tr>
					</table>
					<div id="httpload_set">
						<table cellpadding="0" cellspacing="0" border="1" id="myTable"
							class="tablesorter" style="width: 99%; align: center">
							<tr>
								<td colspan="2">
									��־���ͣ�
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									${loadconfig.httploadlogtype.logDesc}
									<textarea rows="5" cols="100" name="httploadloglog"
										id="httploadloglog">${loadconfig.httploadloglog}</textarea>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									httploadѹ�ⷽʽ��
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									<c:if test="${loadconfig.httploadProxy == 'no'}">�޸�ԭʼurlΪip:7001ѹjboss</c:if>
									<c:if test="${loadconfig.httploadProxy == 'proxy'}">����ԭʼurlѹjboss</c:if>
									<c:if test="${loadconfig.httploadProxy == 'directProxy'}">ֱ��ѹapache(nginx)</c:if>
									<c:if test="${loadconfig.httploadProxy == 'ssl'}">ssl��ʽֱ��ѹapache(nginx)</c:if>
								</td>
							</tr>
							<tr>
								<td colspan="2">
									httpload·����
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
									<span id="start">�����ű���</span>
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
									${loadconfig.startScript }
								</td>
							</tr>
							<tr>
								<td colspan="2">
									<span id="end">ֹͣ�ű���</span>
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
    if (type == 'apache' && mode == "SSH") { //��������ʾ
      $("#script_set").hide();
      $("#apache_set").show();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("��д�������� 4_1,4_2,4_3,5_9 ��ǰ���ʾ���������������ʾ��������");
      $("#loadFeatureDesc").text("˵��:apache ���ip1,ip2,ip3...");
      $("#configText").text("jk�����ļ�·���� ");
      $("#runText").text("apache����·���� ");
    } else if (type == 'hsf' && mode == "SSH") {
      $("#script_set").hide();
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("hsf���͵���д�ӿ���Ϣ�̺ŷָ�,ֻҪ������Ҫ�ӿ�,�뵽�����ѯhttp://config.online.taobao.net/config-ops-online/weighting/list.html?hostId=");
      $("#loadFeatureDesc").text("˵��:hsf ���ip���� 1,2,3,4,5...");
    } else if (type == 'httpLoad' && mode == "SSH") {
      $("#script_set").hide();
      $("#apache_set").hide();
      $("#httpload_set").show();
      $("#configFeatureDesc").text("��дѹ��ʱ�� ��λ �� Ĭ��60 ");
      $("#loadFeatureDesc").text("˵��:httpload:ѹ���û�����5,10,15...");
    } else if (type == 'apacheProxy' && mode == "SSH") {
      $("#script_set").hide();
      $("#apache_set").show();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("������д");
      $("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
      $("#configText").text("proxy�����ļ�·���� ");
      $("#runText").text("apache����·���� ");
    } else if (type == 'nginxProxy' && mode == "SSH") {
      $("#script_set").hide();
      $("#apache_set").show();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("������д");
      $("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
      $("#configText").text("proxy�����ļ�·���� ");
      $("#runText").text("nginx����·���� ");
    } else if (type == 'apache' && mode == 'SCRIPT') { //��������ʾ
      $("#script_set").show();
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("��д�������� 4_1,4_2,4_3,5_9 ��ǰ���ʾ���������������ʾ��������");
      $("#loadFeatureDesc").text("˵��:apache ���ip1,ip2,ip3...");
      $("#configText").text("jk�����ļ�·���� ");
      $("#runText").text("apache����·���� ");
      $("#apachectlPath").val("/home/admin/cai/bin/apachectl");
    } else if (type == 'hsf' && mode == 'SCRIPT') {
      $("#script_set").show();
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("hsf���͵���д�ӿ���Ϣ�̺ŷָ�,ֻҪ������Ҫ�ӿ�,�뵽�����ѯhttp://ops.jm.taobao.org:9999/config-ops/pub_mgr.html");
      $("#loadFeatureDesc").text("˵��:hsf ���ip���� 1,2,3,4,5...");
      $("#apachectlPath").val("");
    } else if (type == 'httpLoad' && mode == 'SCRIPT') {
      $("#script_set").hide();
      $("#apache_set").hide();
      $("#httpload_set").show();
      $("#configFeatureDesc").text("��дѹ��ʱ�� ��λ �� Ĭ��60 ");
      $("#loadFeatureDesc").text("˵��:httpload:ѹ���û�����5,10,15...");
      $("#apachectlPath").val("");
    } else if (type == 'apacheProxy' && mode == 'SCRIPT') {
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("B2b��SCRIPT��ʽΪ�������ʽ:80:2_80:4,80:2_80:5���˿ں�:Ȩ��)");
      $("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
      $("#configText").text("proxy�����ļ�·���� ");
      $("#runText").text("apache����·���� ");
      $("#apachectlPath").val("/home/admin/cai/bin/apachectl");
    } else if (type == 'nginxProxy' && mode == 'SCRIPT') {
      $("#script_set").show();
      $("#apache_set").hide();
      $("#httpload_set").hide();
      $("#configFeatureDesc").text("������д");
      $("#loadFeatureDesc").text("��ű�����ķ�����ip1,ip2,ip3...");
      $("#configText").text("proxy�����ļ�·���� ");
      $("#runText").text("nginx����·���� ");
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