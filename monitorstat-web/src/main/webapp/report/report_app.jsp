<%@ page language="java" contentType="text/html; charset=GB18030"
    pageEncoding="GB18030"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%@page import="com.taobao.monitor.web.util.UserPermissionCheck"%>
<%@ include file="../taglibs.jsp" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB18030"/>
<title>核心监控-添加报表模板</title>
<link type="text/css" href="<%=request.getContextPath() %>/statics/css/themes/base/ui.all.css" rel="stylesheet" />
<script src="<%=request.getContextPath() %>/statics/js/jquery-1.4.4.js"></script>
<link type="text/css" href="<%=request.getContextPath()%>/statics/css/global.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ui/jquery-ui-1.8.custom.min.js"></script> 
<link type="text/css" href="<%=request.getContextPath() %>/statics/datatable/css/demo_page.css" rel="stylesheet" />
<link type="text/css" href="<%=request.getContextPath() %>/statics/datatable/css/demo_table.css" rel="stylesheet" />
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/datatable/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/statics/js/ifreamUtil.js"></script>
<style>
td{
  height:34px;
}
table#example{
  bordercolor:#4f81bd;
}
</style>
<script type="text/javascript">
iframeAuto();
$(document).ready(function() {
    $('#example').dataTable( {//加载
        "bPaginate": false,//分页按钮
        "bLengthChange": true,//每行显示记录数
        "bFilter": true,//搜索栏
        "bSort": true,//排序
        "bInfo": false,//Showing 1 to 10 of 23 entries  总记录数没也显示多少等信息
        "bAutoWidth": true } );
});
$("input[name='selectId']").each(function(){
	if($(this).val()==1){
		$(this).attr("checked", "true");
	}
}) 
var allcheck = $("input");
var all = $("input:checkbox");
all.each(function () {
    this.checked = false;
});
</script>
</head>
<body style="top:0px;">
<div id="dialog">
<table align="center" id="example" border="1">
	<thead>
	<tr>
	  	<th width="50" align="center">
	  	<input name='chkAll' type='checkbox' id='chkAll' value='chkAll'/></th>
		<th align="center">应用名</th>
	    <th align="center">应用组名称</th>
	    <th align="center">应用OPS名称</th>
  </tr>  
  </thead>
  <tbody>
	<c:forEach items="${listApp}" var="valueEntry">
	<c:set var="appId" value="${valueEntry.appId}+','"></c:set>
       	<tr>
       	    <td align="center">
       	      <c:choose>
       	        <c:when test="${fn:containsIgnoreCase(appIds,appId)}">
       	          <input type="checkbox" id="selectId" name='selectId' checked="checked" class='selectId' value="${valueEntry.appId}"/>
       	        </c:when>
       	        <c:otherwise>
       	          <input type="checkbox" id="selectId" name='selectId' class='selectId' value="${valueEntry.appId}"/>
       	        </c:otherwise>
       	      </c:choose>  	      
       	    </td>
			<td align="center">${valueEntry.appName}</td>
			<td align="center">${valueEntry.groupName}</td>
			<td align="center">${valueEntry.opsName}</td>
		</tr>
	</c:forEach>
	</tbody>
</table>
</div>
</body>
</html>