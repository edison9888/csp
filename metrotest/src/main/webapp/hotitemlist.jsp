<%@ page language="java" contentType="text/html; charset=gb2312"
	pageEncoding="gb2312"%>
<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.taobao.po.AuctionsRefSearchPo"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" con������tent="text/html; charset=gb2312">
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
	<strong align="center">��ǰ����������${count}</strong><br/>
	<strong align="center">��ѯ����Ʒ����ת�б�</strong>
	<table class="table table-striped">
		<thead>
			<tr>
				<th width="30%">��ת��</th>
				<th width="60%">��Ʒ��</th>
				<th width="10%">ȥ��̳</th>
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
				<td><a href="show.html" target="_blank">ȥ��̳</a></td>
			</tr>		
		<%				
			}
		%>
		</tbody>
	</table>
</body>
</html>