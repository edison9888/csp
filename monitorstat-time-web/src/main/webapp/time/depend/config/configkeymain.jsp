<%@ page language="java" import="java.util.*" pageEncoding="GBK"
	isELIgnored="false"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" import="java.util.List"%>
<%@ page language="java" import="com.taobao.monitor.common.po.CspTimeKeyDependInfo"%>
<!doctype html>
<html>
	<head>
		<title>Key��ϵ����</title>
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
<body>
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
					<a href="<%=request.getContextPath()%>/index.jsp">ʵʱ��ҳ</a> ->
					<a href="<%=request.getContextPath()%>/config/dependconfig.do?method=showSourceKeyList&appId=${appInfo.appId}">Ӧ��${appInfo.appName}��Key��ϵ����</a>
					<hr>
					<div class="row-fluid">
						<div class="span12">
						<!-- ��ѯ���ͱ�����ֿ� -->
							<div class="row-fluid">
								<form id="form2" method="post"
									action="<%=request.getContextPath()%>/config/dependconfig.do">
									<input type="button" value="���" name="addButton" />
									<input type="button" value="ɾ��" name="deleteButton" />
									<input type="hidden" name="method" id="formmethod" value=""/>
									<input type="hidden" name="sourceAppName" value="${sourceAppName}"/>
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
													Key
												</td>
												<td style="text-align: center;" id="time1">
													����Ӧ��
												</td>
												<td style="text-align: center;" id="time2">
													״̬
												</td>
											</tr>
										</thead>

										<tbody id="exctbody">
											<c:forEach items="${sourceKeyList}" var="item">
												<tr>
													<td>
														<input type="checkbox" name="combineId" value="${item.keyName}_#${item.appName}"/>
													</td>
													<td width="250" style="text-align: left;">
														<a title="���������key����" 
														href="<%=request.getContextPath()%>/config/dependconfig.do?method=gotoSubkeyConfig&sourceAppName=${sourceAppName}&sourceKeyName=${item.keyName}&appName=${item.appName}&appId=${appInfo.appId}">
														${item.keyName}</a>		
													</td>
													<td style="text-align: center;" >
														${item.appName }
													</td>
													<td style="text-align: center;">
													<c:if test="${item.dependStatus == '3'}"> 
														����			 
													</c:if> 
													<c:if test="${item.dependStatus == '1'}"> 
														<strong style="color: red" title="�Զ��������������������ϵ">����</strong>			 
													</c:if> 
													</td>
												</tr>
											</c:forEach>
										</tbody>
									</table>
									<input type="button" value="ɾ��" name="deleteButton" />
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
		
		
		var deleteButtons = document.getElementsByName("deleteButton");
		$(deleteButtons).click(function(){
			deleteChoosedItem();
		});
		
		var addButton = document.getElementsByName("addButton");
		$(addButton).click(function(){
			addButtonFuc();
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
		function deleteChoosedItem() {
			var a = confirm("ȷ��Ҫɾ����");
			if (!a)
				return;
			$("#formmethod")[0].value="deleteSourceKey";
			$("#form2").submit();
		}
		function addButtonFuc() {
			$("#formmethod")[0].value="gotoAddSourceKeyPage";
			$("#form2").submit();
		}
	</script>
</html>
