<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030">
<title>CSP系统-压测详情</title>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/icon.css">
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/jquery/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/jquery/jquery.easyui.min.js"></script>
</head>
<body  align="center">
	
	逗号分隔条件:<input type="text" name="filter" id="filter" size="120" value="${filter}"/>
		<button type="button" onclick="search()">模糊查询</button>


	<table id="mytable" title="压测详情信息" class="easyui-treegrid" style="height:600px"
			url="${pageContext.request.contextPath}/loadrun/data.do?method=detailData&id=${id}&filter=${filter}"
			rownumbers="true" showFooter="false"
			idField="id" treeField="name">
		<thead frozen="true">
			<tr>
				<th field="name" width="500">名称</th>
			</tr>
		</thead>
		<thead>
			<tr>
				<c:forEach items="${times}" var="time">
					<th field="${time}" width="150" align="center">${time}</th>
				</c:forEach>
			</tr>
		</thead>
		
	</table>

<script type="text/javascript">
function search() {
	var filter = document.getElementById("filter").value;
	var url = "${pageContext.request.contextPath}/loadrun/show.do?method=showDetail&id=${id}&filter=" + filter
	window.location.href =url;
}
</script>
</body>
</html>