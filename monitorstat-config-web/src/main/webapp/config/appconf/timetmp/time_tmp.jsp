<%@ page language="java" contentType="text/html; charset=GBK"
    pageEncoding="GBK"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="java.util.List"%>
<%@page import="com.taobao.monitor.common.po.TimeConfTmpPo"%>
<%@page import="java.util.ArrayList"%><html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>实时模板管理</title>

<link type="text/css"  href="<%=request.getContextPath() %>/common_res/css/bootstrap.css" rel="stylesheet">
<style type="text/css">
	body {
	  padding-top: 60px;
	}
</style>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/common_res/js/jquery.js"></script>
<script type="text/javascript"	src="<%=request.getContextPath() %>/common_res/js/jquery.tablesorter.js"></script>
<%
List<TimeConfTmpPo> tmpList = (ArrayList<TimeConfTmpPo>)request.getAttribute("tmpList");
Boolean issuccess = (Boolean)request.getAttribute("issuccess");
%>
<script type="text/javascript">
$(function() {
	$("#sortTable").tablesorter( { sortList: [[ 0, 0 ]] } );
});

function deleteTmp(tmpId) {
	var con = confirm("是否确认删除");
	if (con) {
		location.href = "<%=request.getContextPath()%>/show/appconfig.do?method=deleteTimeTmp&tmpId="+tmpId;
	}
}

function gotoAddTmp() {
	var str = "<%=request.getContextPath()%>/show/appconfig.do?method=gotoAddTmpPage";
	//alert(str);
	location.href = str;
}
</script>
</head>
<body>
<jsp:include page="../../../header.jsp"></jsp:include>

<div class="container-fluid">
	<jsp:include page="../../../leftmenu.jsp"></jsp:include>
	
	<div class="content">
		<div class="page-header">
			<h2>
				实时分析模板配置
				<%
				if (issuccess != null) {
					if (issuccess) {
						%>
							<span class="label success">模板更新成功</span>
						<%	
						} else {
						%>
							<span class="label warning">模板更新失败</span>
						<%
						}
				}
				%>
				<button class="btn primary pull-right" onclick="gotoAddTmp()">增加实时模板</button>
			</h2>
		
		
		</div>
		<table class="bordered-table zebra-striped condensed-table" id="sortTable">
			<thead>
				<tr>
					<th class="blue">Id</th>
					<th class="blue">日志文件别名</th>
					<th class="blue">分析器</th>
					<th class="blue">分隔</th>
					<th class="blue">频率</th>
					<th class="blue">tail类型</th>
					<th class="blue">数据获取</th>
					<th class="blue">分析方式</th>
					<th class="blue">操作</th>
				</tr>
			</thead>
			<tbody>
			<%for(TimeConfTmpPo po: tmpList){ %>
				<tr >
					<td><%=po.getTmpId() %></td>
					<td><%=po.getAliasLogName() %></td>
					<td><%=po.getClassName()%></td>
					<!-- <td><%=po.getFilePath()%></td> -->
					<td><%=po.getSplitChar() %></td>
					<!--<td><%=po.getAnalyseFuture()%></td> -->
					<td><%=po.getAnalyseFrequency()%></td>
					<td><%=po.getTailType()%></td>
					<td>
						<%if(po.getObtainType()==1){out.print("日志文件");}
						if(po.getObtainType()==2){out.print("shell方式");} 
						if(po.getObtainType()==3){out.print("http方式");}
						%>
					</td>
					<td>
						<%  if(po.getAnalyseType()==1){out.print("java-class");}
							if(po.getAnalyseType()==2){out.print("javscript");} 
						%>
					</td>
					<td>
						<a href="<%=request.getContextPath()%>/show/appconfig.do?method=gotoTimeTmpUpdate&tmpId=<%=po.getTmpId()%>">修改</a>&nbsp;&nbsp;
						<a id="deleteAction" class="deleteAction" href="javascript:deleteTmp(<%=po.getTmpId() %>)">删除</a> &nbsp;&nbsp;
						<a href="<%=request.getContextPath()%>/show/appconfig.do?method=gotoTimeTmpCheck&tmpId=<%=po.getTmpId()%>">查看</a> &nbsp;&nbsp;
					</td>
				</tr>
				<%} %>
			</tbody>
		</table>
		<footer>
		  <p>&copy; TaoBao 2011</p>
		</footer>
	</div>
</div>
</body>
</html>