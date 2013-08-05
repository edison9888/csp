<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.List"%>
<%@ page language="java" import="com.taobao.monitor.common.po.CspTimeKeyDependInfo"%>
<!doctype html>
<html>
	<head>
		<title>Key关系配置</title>
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
	List<CspTimeKeyDependInfo> sourceKeyList = (List<CspTimeKeyDependInfo>)request.getAttribute("sourceKeyList");
	HashMap<String,String> keyNameMap = new HashMap<String,String>();
	String appName = (String)request.getAttribute("appName");
	String keyName = (String)request.getAttribute("keyName");
	
	int i=1000;	//1000开始防止重名
	if(sourceKeyList != null) {
		for(CspTimeKeyDependInfo info: sourceKeyList) {
		  if(!keyNameMap.containsKey(info.getKeyName()) && !info.getKeyName().equals("")) {
		    keyNameMap.put(info.getKeyName(), i++ + "");
		  }
		  if(!keyNameMap.containsKey(info.getDependKeyName()) && !info.getDependKeyName().equals("")) {
		    keyNameMap.put(info.getDependKeyName(), i++ + "");
		  }
		}	  
		if(keyNameMap.size() == 0 && keyName != null) {
		  keyNameMap.put(keyName, i+"");	//保证至少有一个节点
		}
	} else {
	  sourceKeyList = new ArrayList();
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
					<a href="<%=request.getContextPath()%>/index.jsp">实时首页</a> 
					-><a href="<%=request.getContextPath()%>/config/dependconfig.do?method=showSourceKeyList&appId=${appInfo.appId}">应用${appInfo.appName}的Key关系配置</a> 			 
					->${keyName}&nbsp;			 
					<hr>
					<div class="row-fluid">
						<div class="span12">
						<div class="row-fluid">
							<script type="text/javascript">
								putCanvas('canvas_0');	//记录画布的Id
							</script>
							<div id="canvas_0"  class="canvas " style="width:1000px; height:400px">
								<%
								for(String keyNameTmp: keyNameMap.keySet()) {
								  String color = "";
								  if(keyName != null && keyNameTmp.equals(keyName)) {
								    color = "red";
								  } else {
								    color = "#0088CC";
								  }
								  %>
								  <div id="div_<%=keyNameMap.get(keyNameTmp)%>" class="itemWindow" 
								  style="border: 2px solid <%=color%>; height: auto; position: relative;  
								  width: 200px; z-index: 20; display:block;word-break:break-all">
									<a href="<%=request.getContextPath()%>/config/dependconfig.do?method=gotoSubkeyConfig&sourceAppName=${sourceAppName}&sourceKeyName=${sourceKeyName}&appName=test&keyName=<%=keyNameTmp%>&appId=${appInfo.appId}"><%=keyNameTmp%></a>
									</div>
								  <%                      
								}
								for(CspTimeKeyDependInfo nodeKey : sourceKeyList) {
								  String keyNameTmp = keyNameMap.get(nodeKey.getKeyName());
								  String depName = keyNameMap.get(nodeKey.getDependKeyName());
								  %>
								  <script type="text/javascript">
								    putEdage('div_<%=depName%>','div_<%=keyNameTmp%>');
								  </script>
								  <%
								}
								%>
							</div>
						</div>
						<div class="row-fluid">
							<form action="<%=request.getContextPath()%>/config/dependconfig.do" id="keyform">
								<input type="hidden" name="appId" value="${appInfo.appId}"/>
								<input type="hidden" name="method" id="submethod" value="gotoSubkeyConfig"/>
								<input type="hidden" name="sourceAppName" value="${sourceAppName}"/>
								<input type="hidden" name="sourceKeyName" value="${sourceKeyName}"/>
								<input name="appName" value="${appName}" type="hidden"/>
								<input name="keyName" value="${keyName}"  style="width: 700px"/>&nbsp;
								<input value="子节点查询" type="button" id="subKeySearchButton" />
								<input value="添加子节点" type="button" id="addSubKeyButton" />
							</form>
						</div>
						<!-- 查询表单和保存表单分开 -->
							<div class="row-fluid">
								<form id="form2" method="post"
									action="<%=request.getContextPath()%>/config/dependconfig.do">
									<input type="button" value="保存" name="saveButton" />
									<input type="hidden" name="method" value="saveSubKey"/>
									<input type="hidden" name="sourceAppName" value="${sourceAppName}"/>
									<input type="hidden" name="sourceKeyName" value="${sourceKeyName}"/>
									<input type="hidden" name="appName" value="${appName}"/>
									<input type="hidden" name="keyName" value="${keyName}"/>
									<input type="hidden" name="appId" value="${appInfo.appId}"/>
									<table
										class="table table-striped table-bordered table-condensed"
										width="100%">
										<thead>
											<tr>
												<td width="100">
													<input type="checkbox" id="cb1" />
													全选
												</td>
												<td width="250" style="text-align: left;" title="点击超链接前请注意保存">
													Key
												</td>
												<td style="text-align: center;" id="time1">
													所属应用
												</td>
												<td style="text-align: center;" id="time2">
													状态
												</td>
											</tr>
										</thead>
										<tbody id="exctbody">
											<c:forEach items="${meDepKeyList}" var="item">
												<tr>
													<td>
														<input type="checkbox" name="combineId" value="${item.id}_#${item.dependStatus}_#${item.appName}_#${item.dependAppName}_#${item.dependKeyName}"
														<c:if test="${item.dependStatus == NOTAUTO_CONFIG}">checked="checked"</c:if>									
														<c:if test="${item.dependStatus == AUTO_CONFIG}">checked="checked"</c:if>									
														 />
													</td>
													<td width="250" style="text-align: left;">
													<c:if test="${item.dependStatus == NOTAUTO_CONFIG || item.dependStatus == AUTO_CONFIG}">
														<a title="点击超链接前请注意保存" 
														href="<%=request.getContextPath()%>/config/dependconfig.do?method=gotoSubkeyConfig&sourceAppName=${sourceAppName}&sourceKeyName=${sourceKeyName}&appName=${item.dependAppName}&keyName=${item.dependKeyName}&appId=${appInfo.appId}">
														${item.dependKeyName}</a>															
													</c:if>	
													<c:if test="${item.dependStatus != NOTAUTO_CONFIG && item.dependStatus != AUTO_CONFIG}">
														${item.dependKeyName}															
													</c:if>	
													</td>
													<td style="text-align: center;" >
														${item.dependAppName }
													</td>
													<td style="text-align: center;">
													<c:if test="${item.dependStatus == AUTO_CONFIG}"> 
														正常			 
													</c:if> 
													<c:if test="${item.dependStatus == EXTRA_CONFIG}"> 
														手动添加			 
													</c:if> 
													<c:if test="${item.dependStatus == NOTAUTO_CONFIG}"> 
														<strong style="color: red" title="自动检查结果不包含此依赖关系">过期</strong>			 
													</c:if> 
													<c:if test="${item.dependStatus == AUTO_NOTCONFIG}"> 
														-			 
													</c:if> 
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<input type="button" value="保存" name="saveButton" />
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
		
		//查询和添加按钮时间
		var subKeySearchButton = document.getElementById("subKeySearchButton");
		var addSubKeyButton = document.getElementById("addSubKeyButton");
		
		var submethod = document.getElementById("submethod");
			
		$(subKeySearchButton).click(function(){	//查询
			submethod.value = "gotoSubkeyConfig";
			$("#keyform").submit();
		});
		$(addSubKeyButton).click(function(){//添加
			submethod.value = "gotoAddSubKeyPage";
			$("#keyform").submit();
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
    	var a = confirm("确定要保存吗？");
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
