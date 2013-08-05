<%@ page language="java" contentType="text/html; charset=gb2312"
	pageEncoding="gb2312"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.po.UrlHot"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<link
	href="<%=request.getContextPath()%>/assets/css/bootstrap-responsive.css"
	rel="stylesheet">
<link href="<%= request.getContextPath()%>/assets/css/bootstrap.css"
	rel="stylesheet">
<!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
<%
List<UrlHot> urlHotList = (List<UrlHot>) request.getAttribute("urlHotList");
if(urlHotList == null)
	urlHotList = new ArrayList<UrlHot>();
%>
<body>
	<br/>
	<table class="table table-striped">
		<thead>
			<tr>
				<th width="30%">访问量</th>
				<th width="70%">活动页面网址</th>
			</tr>
		</thead>
		<tbody>
		<%
			for(UrlHot po: urlHotList) {
		%>
			<tr>
				<td><%=po.getNum()%></td>
				<td><a href="<%=po.getUrl()%>" title="<%=po.getUrl()%>" target="_blank">
				<%=po.getUrl()%>
				</a></td>
			</tr>		
		<%				
			}
		%>
		</tbody>
	</table>
</body>
</html>