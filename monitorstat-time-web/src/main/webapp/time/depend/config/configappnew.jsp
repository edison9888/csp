<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.List"%>
<%@ page language="java" import="com.taobao.monitor.common.po.CspTimeAppDependInfo"%>
<!doctype html>
<html>
	<head>
		<title>Ӧ�ù�ϵ����</title>
		<%@ include file="/time/common/base.jsp"%>
		<meta http-equiv="content-type" content="text/html;charset=gbk" />		
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap.css">
		<link type="text/css" rel="stylesheet"
			href="<%=request.getContextPath()%>/statics/css/bootstrap-responsive.css">
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/bootstrap.js"></script>		

		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery.jsPlumb-all-min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/jquery/jquery-ui.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/graph/raphael-min.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/graph/dracula_graffle.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/graph/dracula_graph.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/graph/dracula_algorithms.js"></script>
		<script type="text/javascript"
			src="<%=request.getContextPath()%>/statics/js/depend_relation.js"></script>
			
<link type="text/css" rel="stylesheet" href="<%=request.getContextPath()%>/statics/css/jquery-ui.css">
<style type="text/css">
body {
	padding-top: 60px;
	padding-bottom: 40px;
}

.sidebar-nav {
	padding: 9px 0;
}
.canvas{
	background-color: white;
	position: relative;
	width: 100%;
	margin: 10px 10px;
	padding: 10px 10px;
	height: 800px;
	overflow: hidden;
	border: 1px solid #DDDDDD;
    border-radius: 4px 4px 4px 4px;
    box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075);
    display: block;
    line-height: 1;
}
</style>

</head>
<%
	List<CspTimeAppDependInfo> sourceAppList = (List<CspTimeAppDependInfo>)request.getAttribute("sourceAppList");
	HashMap<String,String> appNameMap = new HashMap<String,String>();
	String appName = (String)request.getAttribute("appName");
	
	int i=1000;	//1000��ʼ��ֹ����
	if(sourceAppList != null) {
		for(CspTimeAppDependInfo info: sourceAppList) {
		  if(!appNameMap.containsKey(info.getAppName())) {
		    appNameMap.put(info.getAppName(), i++ + "");
		  }
		  if(!appNameMap.containsKey(info.getDepAppName())) {
		    appNameMap.put(info.getDepAppName(), i++ + "");
		  }
		}	  
		if(appNameMap.size() == 0 && appName != null) {
		  appNameMap.put(appName, i+"");	//��֤������һ���ڵ�
		}
	} else {
	  sourceAppList = new ArrayList();
	}
%>
<body onunload="jsPlumb.unload();">
		<%@ include file="/header.jsp"%>
		<div class="container-fluid">
			<div class="row-fluid" style="text-align: center">
				<div class="span12" id="page_nav"></div>
<script>
	$("#page_nav").load('<%=request.getContextPath()%>/page_nav.jsp', {urlPrefix:'<%=request.getContextPath()%>/app/detail/show.do?method=showIndex&appId=',appId: '${appInfo.appId}'});
</script>	
			</div>
			<div class="row-fluid">
				<div class="span2">
					<%@include file="/leftmenu.jsp"%>
				</div>
				<div class="span12">
					<a href="<%=request.getContextPath()%>/index.jsp">ʵʱ��ҳ</a> 
					->
					<a href="<%=request.getContextPath()%>/config/dependconfig.do?method=searchAppConfigList&appId=${appInfo.appId}">Ӧ��${appInfo.appName }����Ĺ�ϵ���� </a> 
					<c:if test="${appInfo.appName != appName}"> 
					->${appName}&nbsp;			 
					</c:if> 
					<hr>
					<div class="row-fluid">
						<div class="span12">
						<div class="row-fluid">
							<script type="text/javascript">
								putCanvas('canvas_0');	//��¼������Id
							</script>
							<div id="canvas_0"  class="canvas " style="width:1000px; height:400px">
								<%
								for(String appNameTmp: appNameMap.keySet()) {
								  String color = "";
								  if(appName != null && appNameTmp.equals(appName)) {
								    color = "red";
								  } else {
								    color = "#0088CC";
								  }
								  %>
								  <div id="div_<%=appNameMap.get(appNameTmp)%>" class="itemWindow" 
								  style="border: 2px solid <%=color%>; height: auto; position: relative;  
								  width: 100px; z-index: 20;">
								  <a href="<%=request.getContextPath()%>/config/dependconfig.do?method=searchAppConfigList&sourceAppName=${sourceAppName}&appName=<%=appNameTmp%>&appId=${appInfo.appId}"><%=appNameTmp%></a>
								  </div>
								  <%                      
								}
								for(CspTimeAppDependInfo nodeApp : sourceAppList) {
								  String appNameTmp = appNameMap.get(nodeApp.getAppName());
								  String depName = appNameMap.get(nodeApp.getDepAppName());
								  %>
								  <script type="text/javascript">
								    putEdage('div_<%=depName%>','div_<%=appNameTmp%>');
								  </script>
								  <%
								}
								
								%>
							</div>
						</div>
						<div class="row-fluid">
							<form action="<%=request.getContextPath()%>/config/dependconfig.do" id="appform">
								<input type="hidden" name="method" id="submethod" value="searchAppConfigList"/>
								<input type="hidden" name="sourceAppName" value="${sourceAppName}"/>
								<input type="hidden" name="appId" value="${appInfo.appId}"/>
								<input name="appName" value="${appName}"/>&nbsp;
								<input value="�ӽڵ��ѯ" type="button" id="subAppSearchButton" />
								<input value="����ӽڵ�" type="button" id="addSubAppButton" />
							</form>
						</div>
						<!-- ��ѯ���ͱ�����ֿ� -->
							<div class="row-fluid">
								<form id="form2" method="post"
									action="<%=request.getContextPath()%>/config/dependconfig.do">
									<input type="button" value="����" name="saveButton" />
									<input type="hidden" name="method" value="saveAppConfig"/>
									<input type="hidden" name="sourceAppName" value="${sourceAppName}"/>
									<input type="hidden" name="appName" value="${appName}"/>
									<input type="hidden" name="appId" value="${appInfo.appId}"/>
									<table
										class="table table-striped table-bordered table-condensed"
										width="100%">
										<thead>
											<tr>
												<td width="100">
													<input type="checkbox" id="cb1" />
													ȫѡ
												</td>
												<td width="250" style="text-align: center;" title="���������ǰ��ע�Ᵽ��">
													Ӧ����
												</td>
												<td style="text-align: center;" id="time1">
													��������
												</td>
												<td style="text-align: center;" id="time2">
													״̬
												</td>
											</tr>
										</thead>

										<tbody id="exctbody">
											<c:forEach items="${meDepAppList}" var="item">
												<tr>
													<td>
														<input type="checkbox" name="combineId" value="${item.id}_#${item.dependStatus}_#${item.depAppName}"
														<c:if test="${item.dependStatus == AUTO_CONFIG}">checked="checked"</c:if>									
														<c:if test="${item.dependStatus == NOTAUTO_CONFIG}">checked="checked"</c:if>									
														 />
													</td>
													<td width="250" style="text-align: center;">
													<c:if test="${item.dependStatus == NOTAUTO_CONFIG || item.dependStatus == AUTO_CONFIG}">
														<a title="���������ǰ��ע�Ᵽ��" href="<%=request.getContextPath()%>/config/dependconfig.do?method=searchAppConfigList&sourceAppName=${sourceAppName}&appName=${item.depAppName}&appId=${appInfo.appId}">
														${item.depAppName}</a>															
													</c:if>	
													<c:if test="${item.dependStatus != NOTAUTO_CONFIG && item.dependStatus != AUTO_CONFIG}">
														${item.depAppName}															
													</c:if>														
													</td>
													<td style="text-align: center;" >
														${item.dependtype }
													</td>
													<td style="text-align: center;">
													<c:if test="${item.dependStatus == AUTO_CONFIG}"> 
														����			 
													</c:if> 
													<c:if test="${item.dependStatus == EXTRA_CONFIG}"> 
														�ֶ����			 
													</c:if> 
													<c:if test="${item.dependStatus == NOTAUTO_CONFIG}"> 
														<strong style="color: red" title="�Զ��������������������ϵ">����</strong>			 
													</c:if> 
													<c:if test="${item.dependStatus == '2'}"> 
														-			 
													</c:if> 
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<input type="button" value="����" name="saveButton" />
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</body>
	<script type="text/javascript">
 
	
	$(function(){
		$("#cb1").click(function(){
				checked = this.checked;
				stateChange();
		});
		
		
		var saveButtons = document.getElementsByName("saveButton");
		$(saveButtons).click(function(){
			saveChoosedItem();
		});
		
		//��ѯ����Ӱ�ťʱ��
		var subAppSearchButton = document.getElementById("subAppSearchButton");
		var addSubAppButton = document.getElementById("addSubAppButton");
		
		var submethod = document.getElementById("submethod");
			
		$(subAppSearchButton).click(function(){	//��ѯ
			submethod.value = "searchAppConfigList";
			$("#appform").submit();
		});
		$(addSubAppButton).click(function(){//���
			submethod.value = "gotoAddAppPage";
			$("#appform").submit();
		});
	});
	

var checked;

function stateChange() {
	if (checked)
		chooseAll();
	else
		unChooseAll();

}

function chooseAll() {
	var c = document.getElementsByName("combineId");
	for (var i = 0; i < c.length; i++) {
		c[i].checked = true;
	}
}

function unChooseAll() {
	var c = document.getElementsByName("combineId");
	for (var i = 0; i < c.length; i++) {
		c[i].checked = false;
	}
}
            
    function saveChoosedItem(){
    	var a = confirm("ȷ��Ҫ������");
    	if(!a)
    		return;
		$("#form2").submit();
    }        
					$(function() {
						jsPlumb.reset();
						drawChat();
					});
				</script>
</html>
