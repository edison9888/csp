<%@ page language="java" contentType="text/html; charset=gb2312"
	pageEncoding="gb2312"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.po.AuctionsRefSearchPo"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" con搜索词tent="text/html; charset=gb2312">
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
List<AuctionsRefSearchPo> poList = (List<AuctionsRefSearchPo>) request.getAttribute("poList");
if(poList == null)
	poList = new ArrayList<AuctionsRefSearchPo>();
System.out.println(poList.size());
%>
<body>
	<br/>
	<strong align="center">当前的总搜索量${count}</strong><br/>
	<strong align="center">查询的商品的跳转列表</strong>
	<table class="table table-striped">
		<thead>
			<tr>
				<th width="30%">跳转量</th>
				<th width="60%">商品名</th>
				<th width="10%">去论坛</th>
			</tr>
		</thead>
		<tbody>
		<%
			for(AuctionsRefSearchPo po: poList) {
		%>
			<tr>
				<td><%=po.getNum()%></td>
				<td><a href="http://item.taobao.com/item.htm?id=<%=po.getAuctionId()%>" target="_blank">
				<%=po.getAuctionTitle()%></a></td>
				<td><a href="show.html" target="_blank">去论坛</a></td>
			</tr>		
		<%				
			}
		%>
		</tbody>
	</table>
</body>
</html>